package project.fsurvey.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import project.fsurvey.business.abstracts.ParticipantService;
import project.fsurvey.business.abstracts.UserService;
import project.fsurvey.business.concretes.CustomOAuth2UserService;
import project.fsurvey.core.util.RoleParser;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.entities.concretes.users.CustomOAuth2User;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.repositories.ParticipantRepository;
import project.fsurvey.security.permissions.UserRole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private FacebookConnectionSignup facebookConnectionSignup;
    private CustomOAuth2UserService oAuth2UserService;
    private ParticipantRepository participantRepository;

    @Value("${facebook.appSecret}")
    String appSecret;

    @Value("${facebook.appId}")
    String appId;

    @Autowired
    public SpringSecurityConfiguration(PasswordEncoder passwordEncoder,
                                       UserService userService,
                                       FacebookConnectionSignup facebookConnectionSignup,
                                       CustomOAuth2UserService oAuth2UserService,
                                       ParticipantRepository participantRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.facebookConnectionSignup = facebookConnectionSignup;
        this.oAuth2UserService = oAuth2UserService;
        this.participantRepository = participantRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*","/signin/**","/signup/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                   .formLogin().loginPage("/login").permitAll()
                     .defaultSuccessUrl("/welcome")
                     .and()
                      .logout()
                      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                      .logoutSuccessUrl("/login").permitAll()
                .and().
                oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .and()
                    .successHandler((request, response, authentication) -> {
                        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
                        Participant participant;

                        if(!userService.existsByUsername(principal.getEmail())){
                            participant = new Participant();
                            participant.setUsername(principal.getEmail());
                            participant.setName((String)principal.getAttributes().get("given_name"));
                            participant.setSurname((String)principal.getAttributes().get("family_name"));
                            participant.setPassword(passwordEncoder.encode((String)principal.getAttributes().get("sub")));
                            participant.setRole("PARTICIPANT");
                            String[] role = {"PARTICIPANT"};
                            participant.setAuthorities(RoleParser.parse(role, participant));
                            participantRepository.save(participant);
                        } else {
                            participant = participantRepository.findByUsername(principal.getEmail()).orElse(null);
                        }

                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        participant, participant.getPassword(),
                                        participant.getAuthorities()));
                        response.sendRedirect("/welcome");
                    });
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ConnectionFactoryLocator connectionFactoryLocator =
                connectionFactoryLocator();
        UsersConnectionRepository usersConnectionRepository =
                getUsersConnectionRepository(connectionFactoryLocator);
        ((InMemoryUsersConnectionRepository) usersConnectionRepository)
                .setConnectionSignUp(facebookConnectionSignup);
        return new ProviderSignInController(connectionFactoryLocator,
                usersConnectionRepository, new FacebookSignInAdapter());
    }

    private ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(appId, appSecret));
        return registry;
    }

    private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator
                                                                           connectionFactoryLocator) {
        return new InMemoryUsersConnectionRepository(connectionFactoryLocator);
    }
}

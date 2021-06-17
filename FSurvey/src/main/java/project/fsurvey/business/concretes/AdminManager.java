package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.UserService;
import project.fsurvey.core.adapter.abstracts.UserVerificationService;
import project.fsurvey.business.abstracts.AdminService;
import project.fsurvey.core.results.*;
import project.fsurvey.core.util.RoleParser;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.entities.concretes.users.Admin;
import project.fsurvey.mapper.AdminMapper;
import project.fsurvey.repositories.AdminRepository;

@Service
public class AdminManager implements AdminService {

    private final AdminRepository adminRepository;
    private final UserVerificationService userVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;
    private final UserService userService;

    @Autowired
    public AdminManager(AdminRepository adminRepository,
                        UserVerificationService userVerificationService,
                        PasswordEncoder passwordEncoder,
                        AdminMapper adminMapper,
                        UserService userService) {
        this.adminRepository = adminRepository;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.adminMapper = adminMapper;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<DataResult<UserGetDto>> findById(Long id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if(admin == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult("Admin not found with given id"));
        } else {
            return ResponseEntity.ok(new SuccessDataResult<>(
                    adminMapper.adminToGetDto(admin),
                    "Admin found with given id."
            ));
        }
    }

    @Override
    public ResponseEntity<DataResult<UserGetDto>> add(UserDto adminPostDto){
        if(userService.existsByUsername(adminPostDto.getUsername()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("This username already exists."));

        if(userVerificationService.validate(
                adminPostDto.getNationalIdentity(),
                adminPostDto.getName(),
                adminPostDto.getSurname(),
                adminPostDto.getBirthYear())){

            Admin admin = adminMapper.postDtoToAdmin(adminPostDto);
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setAuthorities(RoleParser.parse(admin.getRole().split(","), admin));
            return ResponseEntity.ok(
                    new SuccessDataResult<>(adminMapper.adminToGetDto(adminRepository.save(admin)))
            );

        } else
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("User information is incorrect."));
    }

    @Override
    public ResponseEntity<DataResult<UserGetDto>> update(Long id, UserDto adminPostDto) {
        if(userService.existsByUsername(adminPostDto.getUsername()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("This username already exists."));

        Admin adminToUpdate = adminRepository.findById(id).orElse(null);
        if(adminToUpdate == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("No Admin found with given id"));
        }
        if(!Strings.isNullOrEmpty(adminPostDto.getUsername()) &&
                !adminToUpdate.getUsername().equals(adminPostDto.getUsername())){
            adminToUpdate.setUsername(adminPostDto.getUsername());
        }
        if(!Strings.isNullOrEmpty(adminPostDto.getPassword()) &&
                !passwordEncoder.matches(adminPostDto.getPassword(), adminToUpdate.getPassword())){
            adminToUpdate.setPassword(passwordEncoder.encode(adminPostDto.getPassword()));
        }

        return ResponseEntity.ok(new SuccessDataResult<>(
                adminMapper.adminToGetDto(adminRepository.save(adminToUpdate)),
                "User updated successfully.")
        );
    }

    @Override
    public ResponseEntity<Result> delete(Long id) {
        if(adminRepository.existsById(id)){
            adminRepository.deleteById(id);
            return ResponseEntity.ok(new SuccessResult("Admin deleted successfully."));
        } else{
            return ResponseEntity.badRequest().body(new ErrorResult("Admin not found with given id."));
        }
    }
}

package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.fsurvey.core.adapter.abstracts.UserVerificationService;
import project.fsurvey.business.abstracts.AdminService;
import project.fsurvey.entities.concretes.users.Admin;
import project.fsurvey.core.exception.NotFoundException;
import project.fsurvey.core.exception.ParameterException;
import project.fsurvey.core.exception.UserVerificationException;
import project.fsurvey.repositories.AdminRepository;

import java.util.UUID;

@Service
public class AdminManager implements AdminService {

    private AdminRepository adminRepository;
    private UserVerificationService userVerificationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminManager(AdminRepository adminRepository,
                        UserVerificationService userVerificationService,
                        PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin findByUUID(UUID uuid) throws NotFoundException{
        return adminRepository.findAdminByUuid(uuid).orElseThrow(() -> new NotFoundException("Admin not found."));
    }

    @Override
    public Admin add(Admin admin){
        if(Strings.isNullOrEmpty(admin.getName()) ||
                Strings.isNullOrEmpty(admin.getRole()) ||
                Strings.isNullOrEmpty(admin.getBirthYear()) ||
                Strings.isNullOrEmpty(admin.getSurname()) ||
                Strings.isNullOrEmpty(admin.getNationalIdentity()) ||
                Strings.isNullOrEmpty(admin.getPassword()) ||
                Strings.isNullOrEmpty(admin.getUsername()))
            throw new ParameterException("Null or empty property found.");
        else if(userVerificationService.validate(admin.getNationalIdentity(),
                admin.getName(),
                admin.getSurname(),
                admin.getBirthYear())){
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            return adminRepository.save(admin);
        } else
            throw new UserVerificationException("User information is incorrect.");
    }

    @Override
    public Admin update(Long id, Admin admin) {
        Admin adminToUpdate = adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Admin not found."));
        if(!Strings.isNullOrEmpty(admin.getUsername()) &&
                !adminToUpdate.getUsername().equals(admin.getUsername())){
            adminToUpdate.setUsername(admin.getUsername());
        }
        if(!Strings.isNullOrEmpty(admin.getPassword()) &&
                !passwordEncoder.matches(admin.getPassword(), adminToUpdate.getPassword())){
            adminToUpdate.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        return adminRepository.save(adminToUpdate);
    }

    @Override
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}

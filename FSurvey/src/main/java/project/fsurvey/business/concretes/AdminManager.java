package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.AdminService;
import project.fsurvey.entities.concretes.users.Admin;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;
import project.fsurvey.repositories.AdminRepository;

import java.util.UUID;

@Service
public class AdminManager implements AdminService {

    private AdminRepository adminRepository;

    @Autowired
    public AdminManager(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin findByUUID(UUID uuid) throws NotFoundException{
        return adminRepository.findAdminByUuid(uuid).orElseThrow(() -> new NotFoundException("Admin not found."));
    }

    @Override
    public Admin add(Admin admin) {
        if(Strings.isNullOrEmpty(admin.getName()) ||
                Strings.isNullOrEmpty(admin.getRole()) ||
                Strings.isNullOrEmpty(admin.getBirthYear()) ||
                Strings.isNullOrEmpty(admin.getSurname()) ||
                Strings.isNullOrEmpty(admin.getNationalIdentity()) ||
                Strings.isNullOrEmpty(admin.getPassword()) ||
                Strings.isNullOrEmpty(admin.getUsername()))
            throw new ParameterException("Null or empty property found.");
        else
            return adminRepository.save(admin);
    }

    @Override
    public Admin update(Long id, Admin admin) {
        return null;
    }

    @Override
    public void delete(Long id) {
        //
    }
}

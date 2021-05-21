package project.fsurvey.controller;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.concretes.AdminManager;
import project.fsurvey.dto.AdminDto;
import project.fsurvey.entities.concretes.users.Admin;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;
import project.fsurvey.exception.UserVerificationException;
import project.fsurvey.util.RoleParser;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private AdminManager adminManager;

    @Autowired
    public AdminRestController(AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @GetMapping("/find-by-uuid")
    public ResponseEntity<Object> findByUuid(@RequestParam("uuid") UUID uuid){
        try{
            return ResponseEntity.ok(adminManager.findByUUID(uuid));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with given uuid.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody AdminDto adminDto){
        Admin admin = new Admin();
        admin.setName(adminDto.getName());
        admin.setBirthYear(adminDto.getBirthYear());
        admin.setSurname(adminDto.getSurname());
        admin.setNationalIdentity(adminDto.getNationalIdentity());
        admin.setPassword(adminDto.getPassword());
        admin.setRole(adminDto.getRole());
        admin.setUsername(adminDto.getUsername());
        if(!Strings.isNullOrEmpty(admin.getRole()))
            admin.setAuthorities(RoleParser.parse(admin.getRole().split(","), admin));
        try{
            return ResponseEntity.ok(adminManager.add(admin));
        } catch (ParameterException | UserVerificationException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Admin");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestParam("id") Long id, @RequestBody AdminDto adminDto){
        Admin admin = new Admin();
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        try{
            return ResponseEntity.ok(adminManager.update(id, admin));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with given id.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Long id){
        try{
            adminManager.delete(id);
            return ResponseEntity.ok("Admin successfully deleted.");
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with given id");
        }
    }

}

package project.fsurvey.controller.rest;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.AdminService;
import project.fsurvey.dto.AdminDto;
import project.fsurvey.entities.concretes.users.Admin;
import project.fsurvey.core.exception.NotFoundException;
import project.fsurvey.core.exception.ParameterException;
import project.fsurvey.core.exception.UserVerificationException;
import project.fsurvey.core.util.RoleParser;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/find-by-uuid/{uuid}")
    public ResponseEntity<Object> findByUuid(@PathVariable("uuid") UUID uuid){
        try{
            return ResponseEntity.ok(adminService.findByUUID(uuid));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with given uuid.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody @Valid AdminDto adminDto){
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
            return ResponseEntity.ok(adminService.add(admin));
        } catch (ParameterException | UserVerificationException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Admin");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid AdminDto adminDto){
        Admin admin = new Admin();
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        try{
            return ResponseEntity.ok(adminService.update(id, admin));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with given id.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        try{
            adminService.delete(id);
            return ResponseEntity.ok("Admin successfully deleted.");
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with given id");
        }
    }
}
package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.AdminService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;


import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<UserGetDto>> findById(@PathVariable("id") Long id){
        return adminService.findById(id);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<UserGetDto>> add(@RequestBody @Valid UserDto admin){
            return adminService.add(admin);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<UserGetDto>> update(@PathVariable("id") Long id,
                                                         @RequestBody UserDto admin){
        return adminService.update(id, admin);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        return adminService.delete(id);
    }
}

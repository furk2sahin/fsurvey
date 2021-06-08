package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DataResult<UserGetDto>> findById(@PathVariable("id") Long id){
        return adminService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<UserGetDto>> add(@RequestBody @Valid UserDto adminPostDto){
            return adminService.add(adminPostDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataResult<UserGetDto>> update(@PathVariable("id") Long id,
                                                         @RequestBody @Valid UserDto userDto){
        return adminService.update(id, userDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        return adminService.delete(id);
    }
}

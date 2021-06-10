package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.dtos.OptionDto;
import project.fsurvey.entities.concretes.survey.Option;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/option")
public class OptionRestController {

    private OptionService optionService;

    @Autowired
    public OptionRestController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAuthority('read:option')")
    public ResponseEntity<DataResult<Option>> findById(@PathVariable("id") Long id){
        return optionService.findById(id);
    }

    @PostMapping("/add/{issueId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Option>> add(@RequestBody @Valid OptionDto optionDto){
        return optionService.add(optionDto);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Option>> update(@PathVariable("id") Long id, @RequestBody OptionDto optionDto){
        return optionService.update(id, optionDto);
    }

    @GetMapping("/find-by-issue-id/{id}")
    @PreAuthorize("hasAuthority('read:option')")
    public ResponseEntity<DataResult<List<Option>>> findByIssueId(@PathVariable("id") Long id){
        return optionService.findByIssueId(id);
    }
}

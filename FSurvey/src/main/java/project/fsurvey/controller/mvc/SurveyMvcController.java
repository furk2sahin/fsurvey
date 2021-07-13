package project.fsurvey.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.fsurvey.business.abstracts.SurveyService;

@Controller
public class SurveyMvcController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyMvcController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping("/admin-survey-page/{pageNo}/{pageSize}")
    public ModelAndView getSurveysPageable(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pagedSurveys", surveyService.findAllPageable(pageNo, pageSize).getBody().getData());
        modelAndView.setViewName("adminSurveyPage");
        return modelAndView;
    }

    @RequestMapping("/create-new-survey")
    public String createSurvey(){
        return "createSurveyPage";
    }
}

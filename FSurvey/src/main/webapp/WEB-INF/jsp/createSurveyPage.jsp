<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title><spring:message code="CREATE_NEW_SURVEY"/> </title>
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
    <link href="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet" />
</head>
<body>
    <div class="container">
        <h1 class="display-4"><spring:message code="CREATE_NEW_SURVEY"/></h1>
        <div class="form-group">
            <label for="surveyName"><spring:message code="SURVEY_NAME"/></label>
            <input type="text" id="surveyName" class="form-control" placeholder="<spring:message code="ENTER_SURVEY_NAME"/>"/>
        </div>
        <div class="form-group">
            <label for="surveyDescription"><spring:message code="DESCRIPTION"/></label>
            <input type="text" id="surveyDescription" class="form-control" placeholder="<spring:message code="DESCRIPTION"/>"/>
        </div>
        <label for="questions"><spring:message code="QUESTIONS"/></label>
        <div class="border border-primary container" id="questions" style="padding: 1em">
            <div class="border border-secondary container" style="padding: 1em; margin-bottom: 0.5em">
                <div class="form-group">
                    <label for="question"><spring:message code="QUESTION"/></label>
                    <input type="text" id="question" class="form-control" placeholder="<spring:message code="QUESTION"/>" required/>
                </div>
                <label for="options"><spring:message code="OPTIONS"/></label>
                <div class="border border-success container" id="options" style="padding: 1em">
                    <span>
                        <input type="text" class="form-control" style="margin-bottom: 0.2em" placeholder="<spring:message code="ANSWER"/>" required/>
                        <input type="text" class="form-control" style="margin-bottom: 0.2em" placeholder="<spring:message code="ANSWER"/>" required/>
                    </span>
                    <button class="btn btn-success" id="addOption" value="1"><spring:message code="ADD_OPTION"/></button>
                </div>
            </div>
            <button class="btn btn-primary align-content-end" id="addQuestion"><spring:message code="ADD_QUESTION"/></button>
        </div>
    </div>
    <script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
    <script>
        $(document).ready(function(){
            $("#addOption").click(function(event){
                let newAnswer = '<input type="text" class="form-control" style="margin-bottom: 0.2em" placeholder="<spring:message code="ANSWER"/>" required/>';
                $("span").append(newAnswer)
            })
            $("#addQuestion").click(function(event){
                let newQuestion = '<div class="border border-secondary container" style="padding: 1em; margin-bottom: 0.5em">'+
                        '<div class="form-group">' +
                            '<label for="question"><spring:message code="QUESTION"/></label>' +
                            '<input type="text" id="question" class="form-control" placeholder="<spring:message code="QUESTION"/>" required/>' +
                        '</div>'+
                        '<label for="options"><spring:message code="OPTIONS"/></label>' +
                        '<div class="border border-success container" id="options" style="padding: 1em">' +
                    '<span>' +
                        '<input type="text" class="form-control" style="margin-bottom: 0.2em" placeholder="<spring:message code="ANSWER"/>" required/>'+
                        '<input type="text" class="form-control" style="margin-bottom: 0.2em" placeholder="<spring:message code="ANSWER"/>" required/>' +
                    '</span>' +
                            '<button class="btn btn-success" id="addOption" value="${event.target.value}"><spring:message code="ADD_OPTION"/></button>'+
                        '</div>'+
                    '</div>'
                console.log(newQuestion)
                console.log($(".btn-secondary"))
                $(".btn-secondary").last().append(newQuestion);
                console.log(event, "addQuestion")
            })
        });
    </script>
</body>
</html>
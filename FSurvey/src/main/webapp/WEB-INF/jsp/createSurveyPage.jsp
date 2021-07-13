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
</head>
<body>
    <div class="container">
        <form class="form-check">
            <h1 class="display-4"><spring:message code="CREATE_NEW_SURVEY"/></h1>
            <label for="surveyName"><spring:message code="SURVEY_NAME"/></label>
            <input type="text" id="surveyName" class="form-control" placeholder="<spring:message code="ENTER_SURVEY_NAME"/>" required/>
            <label for="surveyDescription"><spring:message code="DESCRIPTION"/></label>
            <input type="text" id="surveyDescription" class="form-control" placeholder="<spring:message code="DESCRIPTION"/>" required/>
            <label for="questions"><spring:message code="QUESTIONS"/></label>
            <div class="border border-primary container" id="questions" style="padding: 1em">

                <div class="border border-secondary container" style="padding: 1em; margin-bottom: 0.5em">

                    <div class="form-group">
                        <label for="question"><spring:message code="QUESTION"/> 1</label>
                        <input type="text" id="question" class="form-control" placeholder="<spring:message code="QUESTION"/>" required/>
                    </div>

                    <label for="options"><spring:message code="OPTIONS"/></label>
                    <div class="border border-success container" id="options" style="padding: 1em">
                        <span id="1">
                            <input type="text" class="form-control" style="margin-bottom: 1em" placeholder="<spring:message code="ANSWER"/>" required/>
                            <input type="text" class="form-control" style="margin-bottom: 1em" placeholder="<spring:message code="ANSWER"/>" required/>
                        </span>
                        <button class="btn btn-success" value="1"><spring:message code="ADD_OPTION"/></button>
                    </div>
                </div>
                <button class="btn btn-primary" id="addQuestion"><spring:message code="ADD_QUESTION"/></button>

            </div>
            <button class="btn btn-info btn-block mt-1" id="addSurvey"><spring:message code="ADD_SURVEY"/></button>
        </form>
    </div>
    <script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function(){
            $(document).on('click', ".btn-success",function(event){
                const value = event.target.value;
                const inputLength = $("#" + value + " input").length;
                if(inputLength < 5){
                    const inputId = value + "-" + (inputLength + 1);
                    const newAnswer =
                        '<div class="input-group mb-3" id="' + inputId + '">'+
                            '<input type="text" class="form-control" style="margin-bottom: 0.2em" ' +
                                'placeholder="<spring:message code="ANSWER"/>" required/>' +
                            '<div class="input-group-append">' +
                                '<button class="btn btn-danger" value="' + inputId + '"><spring:message code="DELETE"/></button>'+
                            '</div>'+
                    '</div>';
                    $("#" + value).append(newAnswer)
                }
            })

            $("#addQuestion").click(function(){
                let spanCount = $("span").length + 1;
                const newQuestion = '<div class="border border-secondary container" id="question-' + spanCount + '" style="padding: 1em; margin-bottom: 0.5em">'+
                        '<div class="form-group">' +
                            '<label for="question"><spring:message code="QUESTION"/></label>' +
                            '<input type="text" id="question" class="form-control" placeholder="<spring:message code="QUESTION"/>" required/>' +
                        '</div>'+
                        '<label for="options"><spring:message code="OPTIONS"/></label>' +
                        '<div class="border border-success container" id="options" style="padding: 1em">' +
                    '<span id=' + spanCount + '>' +
                        '<input type="text" class="form-control" style="margin-bottom: 1em" placeholder="<spring:message code="ANSWER"/>" required/>'+
                        '<input type="text" class="form-control" style="margin-bottom: 1em" placeholder="<spring:message code="ANSWER"/>" required/>' +
                    '</span>' +
                            '<button class="btn btn-success" value=' + spanCount + '><spring:message code="ADD_OPTION"/></button>'+
                        '</div>'+
                        '<button class="btn btn-block btn-danger mt-1" value="question-' +spanCount + '"><spring:message code="DELETE_QUESTION"/></button>'+
                    '</div>'
                $("#addQuestion").before(newQuestion);
            })

            $(document).on('click', ".btn-danger",function(event){
                let id = "#" + event.target.value;
                $(id).remove();
            })

            $("#addSurvey").click(function(event){
                event.preventDefault()
                const isValid = validateForm()
                if(isValid){
                    const data = handleData();
                    $.ajax({
                        url:"/api/v1/survey/add",
                        type:"POST",
                        contentType: "application/json",
                        datatype: "JSON",
                        data: JSON.stringify(data),
                        success : function(){
                            window.location.replace("/admin-survey-page/1/5");
                        },
                        error : function(response){
                            console.log(response);
                        }
                    });
                }
            })

            function handleData(){
                var issues = [];
                var options = [];
                let optionOrder = 1;
                let issueOrder = 1;
                $(".border-secondary").each(function (){
                    $(this).children(".border-success").children("span").each(function(){
                        $(this).children("input").each(function(){
                            options.push(
                                {
                                    optionOrder: optionOrder,
                                    answer: $(this).val()
                                }
                            )
                            optionOrder++
                        })
                        issues.push({
                            issueOrder: issueOrder,
                            question: $(".border-secondary").children(".form-group").children("input").val(),
                            options:options
                        })
                        options = []
                        optionOrder=1;
                        issueOrder++;
                    })
                })
                const data = {
                    name : $("#surveyName").val(),
                    description : $("#surveyDescription").val(),
                    issue : issues
                }
                return data
            }

            function validateForm() {
                let isValid = true;
                $('.form-control').each(function() {
                    if ( $(this).val() === '' ){
                        isValid = false;
                    }
                });
                return isValid;
            }
        });
    </script>
</body>
</html>
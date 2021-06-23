
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><spring:message code="REGISTER_BUTTON"/></title>
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
    <style>
        body{
            background-color: #3e3e3e;
            color:white;
        }
    </style>

</head>
<body>
<div class="container">
    <form class="form-signin" id="signin">
        <h2 class="form-signin-heading"><spring:message code="LOGIN_HEADER"/></h2>
        <p>
            <label for="name" class="sr-only"><spring:message code="NAME"/></label>
            <input type="text" id="name" name="name" class="form-control" placeholder="<spring:message code="NAME"/>" required="" autofocus="">
        </p>
        <p>
            <label for="surname" class="sr-only"><spring:message code="SURNAME"/></label>
            <input type="text" id="surname" name="surname" class="form-control" placeholder="<spring:message code="SURNAME"/>" required="">
        </p>
        <p>
            <label for="username" class="sr-only"><spring:message code="USERNAME"/></label>
            <input type="text" id="username" name="username" class="form-control" placeholder="<spring:message code="USERNAME"/>" required="">
        </p>
        <p>
            <label for="password" class="sr-only"><spring:message code="PASSWORD"/></label>
            <input type="password" id="password" name="password" class="form-control" placeholder="<spring:message code="PASSWORD"/>" required="">
        </p>
        <p>
            <label for="birthYear" class="sr-only"><spring:message code="BIRTH_YEAR"/></label>
            <input type="text" id="birthYear" name="birthYear" class="form-control" placeholder="<spring:message code="BIRTH_YEAR_PLACEHOLDER"/>" required="">
        </p>
        <p>
            <label for="nationalIdentity" class="sr-only"><spring:message code="NATIONAL_IDENTITY"/></label>
            <input type="text" id="nationalIdentity" name="nationalIdentity" class="form-control" placeholder="<spring:message code="NATIONAL_IDENTITY_PLACEHOLDER"/>" required="">
        </p>
        <input class="btn btn-lg btn-primary btn-block" type="button" id="add_user" value="<spring:message code="REGISTER_BUTTON"/>">
    </form>
    <div class="alert alert-warning" role="alert">
        <spring:message code="HAVE_AN_ACCOUNT"/>
            <a href="/login"><spring:message code="LOGIN_BUTTON"/></a>
    </div>
    <div class="alert alert-danger" role="alert" id="errors"/>
    <div class="alert alert-success" role="alert" id="success"/>
    <script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#errors').hide();
            $('#success').hide();
            $("#add_user").click(function() {
                $('#errors').hide();
                $('#errors').html("");
                $.ajax({
                    url: "api/v1/participant/add",
                    type: "POST",
                    contentType: "application/json",
                    datatype: "JSON",
                    data: JSON.stringify({
                        username:$('#username').val(),
                        password:$('#password').val(),
                        role:'PARTICIPANT',
                        name:$('#name').val(),
                        surname:$('#surname').val(),
                        birthYear:parseInt($('#birthYear').val()),
                        nationalIdentity:$('#nationalIdentity').val(),
                    }),
                    success : function(){
                        $('#success').show();
                        $('#success').html("<spring:message code="SUCCESSFULLY_REGISTERED"/>");
                        setTimeout(function(){window.location.replace("/login"); }, 2000);
                    },
                    error : function (response){
                        json = response.responseJSON;
                        errorInfo = "<b>" + json.message + "</b><br/><ul>";

                        if(json.data != null){
                            if(json.data.name !== undefined ){
                                errorInfo += "<li><b> <spring:message code="NAME"/> </b>: " + json.data.name + "</li>"
                            }
                            if(json.data.birthYear !== undefined){
                                errorInfo += "<li><b> <spring:message code="BIRTH_YEAR"/> </b>: " + json.data.birthYear + "</li>"
                            }
                            if(json.data.nationalIdentity !== undefined){
                                errorInfo += "<li><b> <spring:message code="NATIONAL_IDENTITY"/> </b>: " + json.data.nationalIdentity + "</li>"
                            }
                            if(json.data.password !== undefined){
                                errorInfo += "<li><b> <spring:message code="PASSWORD"/> </b>: " + json.data.password + "</li>"
                            }
                            if(json.data.surname !== undefined){
                                errorInfo += "<li><b> <spring:message code="SURNAME"/> </b>: " + json.data.surname + "</li>"
                            }
                            if(json.data.username !== undefined){
                                errorInfo += "<li><b> <spring:message code="USERNAME"/> </b>: " + json.data.username + "</li>"
                            }
                        }
                        errorInfo+="</ul>";
                        $('#errors').html(errorInfo);
                        $('#errors').show('slow');
                    }
                });
            });
        });
    </script>
</div>
</body>
</html>
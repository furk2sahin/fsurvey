
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><spring:message code="LOGIN_BUTTON"/></title>
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
        <form class="form-signin" id="signin" action="/login" method="post">
            <h2 class="form-signin-heading"><spring:message code="LOGIN_HEADER"/></h2>
            <p>
                <label for="username" class="sr-only"><spring:message code="USERNAME"/></label>
                <input type="text" id="username" name="username" class="form-control" placeholder="<spring:message code="USERNAME"/>" required="" autofocus="">
            </p>
            <p>
                <label for="password" class="sr-only"><spring:message code="PASSWORD"/></label>
                <input type="password" id="password" name="password" class="form-control" placeholder="<spring:message code="PASSWORD"/>" required="">
            </p>
            <button class="btn btn-lg btn-secondary btn-block" type="submit"><spring:message code="LOGIN_BUTTON"/></button>
        </form>
        <form action="/signin/facebook" method="POST">
            <input type="submit" value="<spring:message code="FACEBOOK_BUTTON"/>" class="btn btn-lg btn-primary btn-block"/>
        </form>
        <form action="/signin/twitter" method="POST">
            <input type="submit" value="<spring:message code="TWITTER_BUTTON"/>" class="btn btn-lg btn-primary btn-block"/>
        </form>
        <form action="/oauth2/authorization/google" method="POST">
            <input type="submit" value="<spring:message code="GOOGLE_BUTTON"/>" class="btn btn-lg btn-secondary btn-block"/>
        </form>
        <div class="alert alert-warning" role="alert">
            <spring:message code="DONT_HAVE_ACCOUNT"/>
            <a href="/register"><spring:message code="REGISTER_BUTTON"/></a>
        </div>
        <script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
        <script type="text/javascript" src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </div>
    </body>
</html>
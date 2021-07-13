<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title><spring:message code="SURVEYS"/></title>
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
</head>
<body>
    <div class="container">
        <h1 class="display-3"><spring:message code="SURVEYS"/></h1>
        <table class="table">
            <thead>
                <tr>
                    <th scope="col"><spring:message code="ID"/></th>
                    <th scope="col"><spring:message code="DESCRIPTION"/></th>
                    <th scope="col"><spring:message code="NAME"/></th>
                    <th scope="col"><spring:message code="DELETE"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${pagedSurveys}" var="survey">
                    <tr bgcolor="#d3d3d3">
                        <td>${survey.id}</td>
                        <td>${survey.description}</td>
                        <td>${survey.name}</td>
                        <td><button class="btn btn-danger" value="${survey.id}" id="delete"><spring:message code="DELETE"/></button> </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <nav>
            <ul class="pagination pagination-lg justify-content-center">
            </ul>
        </nav>
        <form action="/create-new-survey" >
            <input class="btn btn-success btn-block" type="submit" value="<spring:message code="CREATE_NEW_SURVEY"/>">
        </form>
    </div>
    <script type="text/javascript" src="/webjars/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function() {
            const pageNo = window.location.pathname.split("/")[2];
            const pageSize = window.location.pathname.split("/")[3];
            $.ajax({
                url:"/api/v1/survey/count",
                type:"GET",
                success : function(data){
                    let pageCount = data / pageSize;

                    if(pageCount % 1 > 0){
                        pageCount = pageCount - (pageCount % 1) + 1;
                    }
                    for(let i=1; i <= pageCount; i++){
                        if(i == pageNo)
                            $("ul").append('<li class="page-item" value="' + i + '"><a class="page-link  bg-info" id="' + i + '">'+i+'</a></li>')
                        else
                            $("ul").append('<li class="page-item" value="' + i + '"><a class="page-link" id="' + i + '">'+i+'</a></li>')
                    }
                },
                error : function(){

                }
            })

            $(document).on('click', ".page-item",function(event){
                event.preventDefault()
                if(!$(this).children("a").hasClass("bg-info")){
                    window.location.replace('/admin-survey-page/' + event.target.id + '/' + pageSize);
                }
            })


            $(".btn-danger").click(function(e) {
                $.ajax({
                    url:"/api/v1/survey/delete/" + e.target.value,
                    type: "DELETE",
                    success : function(){
                        location.reload();
                    },
                    error : function(response){
                        console.log(response.responseJSON)
                    },
                })
            });
        });
    </script>
</body>
</html>
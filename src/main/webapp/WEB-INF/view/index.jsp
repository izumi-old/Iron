<%@ page import="static pet.kozhinov.iron.utils.Constants.API_PREFIX" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>The Iron bank</title>
        <link href="<c:url value="/resources/css/index.css"/>" rel="stylesheet"/>
    </head>
    <body>
        <div class="wrapper">
            <h2 id="welcome--title">Hello, ${username}</h2>
            <div class="pane">
                <div class="pane__column">
                    <c:if test="${manager}">
                        <div class="pane__element">
                            <h2><a href="<%=API_PREFIX%>/manager/">Manager side</a></h2>
                        </div>
                    </c:if>
                    <c:if test="${admin}">
                        <div class="pane__element">
                            <h2><a href="<%=API_PREFIX%>/admin/">Admin side</a></h2>
                        </div>
                    </c:if>
                </div>
                <div class="pane__column">
                    <c:if test="${client}">
                        <div class="pane__element">
                            <h2><a href="<%=API_PREFIX%>/client/">Client side</a></h2>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>
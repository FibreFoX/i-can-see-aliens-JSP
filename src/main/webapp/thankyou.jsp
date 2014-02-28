<%@page contentType="text/html" pageEncoding="UTF-8"%><%
    request.setAttribute("navigation", "thankyou");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thankyou - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <h1>Thank you :)</h1>
        <%@include file="_footer.jsp" %>
    </body>
</html>

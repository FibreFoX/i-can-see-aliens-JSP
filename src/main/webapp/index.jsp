<%
    request.setAttribute("navigation", "index");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Startpage - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <div class="jumbotron">
            <div class="container">
                <h1>I can see aliens (JSP)</h1>
                <p>&nbsp;</p>
                <p><b>Welcome</b> to my example-project to the topic: testing with aliens.</p>
                <p>
                    In this project, i want to show "how to create several types of tests" within the same project,
                    even to mix some testing-frameworks.
                </p>
                <p>
                    The idea is, that users can share their experiences with being "encountered by aliens", so this billboard
                    helps them to communicate.
                </p>
                <p>
                    <a role="button" class="btn btn-primary btn-lg" href="<%=request.getContextPath()%>/register.jsp">Create account »</a>
                    <a role="button" class="btn btn-default btn-lg" id="loginButton" href="<%=request.getContextPath()%>/login.jsp">Login »</a>
                </p>
            </div>
        </div>
        <%@include file="_footer.jsp" %>
    </body>
</html>

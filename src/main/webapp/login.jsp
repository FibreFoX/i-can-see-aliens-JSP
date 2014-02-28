<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound"%>
<%@page import="javax.inject.Inject"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager" %>
<%
    boolean isFormSubmit = false;
    boolean canLogin = userSession.getCurrentUser().isAnonymous() == true;
    String stateMessage = "";

    if( "true".equals(request.getParameter("doLogin")) ){
        isFormSubmit = true;
    }
    
    if(!canLogin){
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    if( canLogin && isFormSubmit ){
        // try to login
        try{

            boolean couldLogin = userSession.login(request.getParameter("name"), request.getParameter("pwd"));
            if( !couldLogin ){
                stateMessage = "There was no user with the given username/password-combination.";
            }
        } catch(NoUserFound nuf){
            // you may change this to give more hints
            stateMessage = "There was no user with the given username/password-combination.";
        }
        if( !userSession.getCurrentUser().isAnonymous() ){
            response.sendRedirect(request.getContextPath() + "/thankyou.jsp");
            return;
        }
    }
%><%
    request.setAttribute("navigation", "login");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <div class="page-header">
            <h1>Login</h1>
        </div>
        <%
            if( stateMessage.trim().length() > 0 ){
        %>
        <div class="errorMessage"><%=stateMessage%></div>
        <%
            }
        %>
        <p class="lead">Please fill in the login-data, you have been registered with.</p>
        <form action="?" method="post" role="form" class="form-inline" id="loginForm">
            <input type="hidden" name="doLogin" value="true" />
            <div class="form-group">
                <label class="sr-only" for="name">Username</label>
                <input type="text" name="name" value="" class="form-control" placeholder="insert username" />
            </div>
            <div class="form-group">
                <label class="sr-only" for="pwd">Password</label>
                <input type="password" name="pwd" value="" class="form-control" placeholder="insert password" />
            </div>
            <button type="submit" class="btn btn-default">Login</button>
        </form>
        <%@include file="_footer.jsp" %>
    </body>
</html>

<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided"%>
<%@page import="javax.inject.Inject"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager" %>
<%

    boolean isFormSubmit = false;
    boolean canRegister = userSession.getCurrentUser().isAnonymous() == true;
    String stateMessage = "";

    if( "true".equals(request.getParameter("doRegister")) ){
        isFormSubmit = true;
    }

    if( !canRegister ){
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    if( canRegister && isFormSubmit ){
        boolean wasCreated = false;
        // try to create user
        try{
            stateMessage = "User couldn't be created.";
            wasCreated = userSession.registerUser(request.getParameter("name"), request.getParameter("pwd"));
        } catch(NoPasswordProvided npp){
            stateMessage = "There was no valid password. Please enter a password.";
        }
        if( wasCreated ){
            response.sendRedirect(request.getContextPath() + "/thankyou.jsp");
            return;
        }
    }
%><%
    request.setAttribute("navigation", "register");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Registration - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <h1>Register</h1>
        <%
            if( stateMessage.trim().length() > 0 ){
        %>
        <div class="alert alert-warning">
            <%=stateMessage%>
        </div>
        <%
            }
        %>
        <p></p>
        <form action="?" method="post" class="form-inline">
            <input type="hidden" name="doRegister" value="true" />
            <div class="form-group">
                <label for="name">Username</label>
                <input type="text" name="name" value="" placeholder="" class="form-control" />
            </div>
            <div class="form-group">
                <label for="pwd">Password</label>
                <input type="password" name="pwd" value="" placeholder="a strong password" class="form-control" />
            </div>

            <button type="submit" class="btn btn-success">Create Account</button>
        </form>
        <%@include file="_footer.jsp" %>
    </body>
</html>

<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound"%>
<%@page import="javax.inject.Inject"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager" %><%!
    @Inject
    @SLSB
    private BillboardEntryManager billboardEntryManager;
%>
<%
    boolean isFormSubmit = false;
    boolean canCreate = userSession.getCurrentUser().isAnonymous() != true;

    if( "true".equals(request.getParameter("doCreate")) ){
        isFormSubmit = true;
    }

    if( !canCreate ){
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    if( canCreate && isFormSubmit ){
        // try to create entry

        billboardEntryManager.createEntry(userSession.getCurrentUser(), request.getParameter("title"), request.getParameter("content"));

        response.sendRedirect(request.getContextPath() + "/entries.jsp");
        return;
    }
%><%
    request.setAttribute("navigation", "create_entry");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Write a new entry - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <div class="page-header">
            <h1>Write a new entry</h1>
        </div>
        <form action="?" method="post" role="form">
            <input type="hidden" name="doCreate" value="true" />
            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" name="title" value="" class="form-control" placeholder="insert a title" />
            </div>
            <div class="form-group">
                <textarea class="form-control" rows="3" name="content"></textarea>
            </div>
            <button type="submit" class="btn btn-default">Create</button>
        </form>
        <%@include file="_footer.jsp" %>
    </body>
</html>

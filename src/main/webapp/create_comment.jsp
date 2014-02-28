<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB"%>
<%!
@Inject
@SLSB
private BillboardEntryManager billboardEntryManager;

@Inject
@SLSB
private CommentManager commentManager;

@Transactional
private void createComment(User user, BillboardEntry entry, String message){
    commentManager.createComment(user, entry, message);
}
%><%
    User currentUser = userSession.getCurrentUser();
    boolean isFormSubmit = false;
    boolean canCreate = currentUser.isAnonymous() != true;
    
    if(!canCreate){
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    String id = request.getParameter("id");
    if(id == null || id.trim().length() == 0){
        response.sendRedirect(request.getContextPath() + "/entries.jsp");
        return;
    }
    // first check if billboard-entry with given ID exists
    BillboardEntry billboardEntry = billboardEntryManager.getBillboardEntry(id);
    if( billboardEntry == null ){
        response.sendRedirect(request.getContextPath() + "/entries.jsp");
        return;
    }
    
    // check if is submit
    if( "true".equals(request.getParameter("doComment")) ){
        isFormSubmit = true;
    }
    
    if( isFormSubmit ){
        // try to create new entry
        String message = request.getParameter("content");
        createComment(currentUser, billboardEntry, message);
        
        // maybe we should handle some errors :) like "no content"
        response.sendRedirect(request.getContextPath() + "/entries.jsp");
        return;
    }
%><!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Write a new comment - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <div class="page-header">
            <h1>Write a new comment</h1>
        </div>
        <form action="?" method="post" role="form">
            <input type="hidden" name="id" value="<%=id%>" />
            <input type="hidden" name="doComment" value="true" />
            <div class="form-group">
                <textarea class="form-control" rows="3" name="content"></textarea>
            </div>
            <button type="submit" class="btn btn-default">Add comment</button>
        </form>
    </body>
</html>

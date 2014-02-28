<%@page import="java.util.List"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment"%>
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
%><%
    request.setAttribute("navigation", "entries");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Billboard - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <div class="page-header">
            <h1>Billboard-Entries</h1>
        </div>
        <%
            for( BillboardEntry entry : billboardEntryManager.getAllEntries() ){
        %>
        <div class="panel panel-default panel-info">
            <div class="panel-heading">
                <%=entry.getTitle()%>
                
                <%
                    if( userSession.getCurrentUser().equals(entry.getOwningUser()) ){
                %>
                <p class="pull-right">
                    <a class="btn btn-danger btn-xs" href="<%=request.getContextPath()%>/delete_entry.jsp?id=<%=entry.getId()%>"><span class="glyphicon glyphicon-remove"></span> remove entry</a>
                </p>
                <%
                    }
                %>
            </div>
            <div class="panel-body">
                <div class="well">
                    <%=entry.getContent()%>
                </div>
                <p>Comments:</p>
                <%
                List<Comment> comments = commentManager.getComments(entry);
                
                if(!comments.isEmpty()){
                %>
                <ul>
                    <%
                    for(Comment comment: comments){
                        %><li><%=comment.getMessage()%></li><%
                    }
                    %>
                </ul>
                <%
                }
                %>
            </div>
            <div class="panel-footer">
                <div>
                    <%
                        if( !userSession.getCurrentUser().isAnonymous() ){
                    %>
                    <p class="pull-right">
                        <a class="btn btn-primary btn-xs" href="<%=request.getContextPath()%>/create_comment.jsp?id=<%=entry.getId()%>"><span class="glyphicon glyphicon-plus"></span> add comment</a>
                    </p>
                    <div class="clearfix"></div>
                    <%
                        }
                    %>
                    <p>Written by &quot;<strong><%=entry.getOwningUser().getName()%></strong>&quot;, timestamp <i><%=entry.getCreatedOn()%></i></p>
                </div>
            </div>
        </div>
        <hr />
        <%
            }
        %>
        <%
            if( !userSession.getCurrentUser().isAnonymous() ){
        %>
        <p class="pull-right">
            <a href="<%=request.getContextPath()%>/create_entry.jsp" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Write a new entry</a>
        </p>
        <%
            }
        %>
        
        <%@include file="_footer.jsp" %>
    </body>
</html>

<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User"%><%!
    @Inject
    @SLSB
    private UserManager userManager;
%><%    request.setAttribute("navigation", "members");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Members - I can see aliens (JSP)</title>
        <%@include file="_header.jsp" %>
    </head>
    <body>
        <%@include file="_navigation.jsp" %>
        <div class="page-header">
            <h1>Member-list</h1>
        </div>
        <p>Currently there are &quot;<%=userManager.getUserCount()%>&quot; members.</p>
        <%
            if( userManager.getUserCount() > 0 ){
        %>
        <ul id="allMembers">
            <%
                for( User userEntry : userManager.getAllUsers() ){
                    boolean isSelf = false;
                    if( userEntry.equals(user) ){
                        isSelf = true;
                    }
            %><li><%=(isSelf) ? "<b>" : ""%><%=userEntry.getName()%> <%=(isSelf) ? "</b>" : ""%><%
                    if( isSelf ){

                        %><a href="<%=request.getContextPath()%>/delete_user.jsp">delete user</a><%
                    }
                %>
            </li>
            <%    
            }
            %>
        </ul>
        <%
            }
        %>
        <%@include file="_footer.jsp" %>
    </body>
</html>

<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry"%>
<%@page import="javax.inject.Inject"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession"%>
<%!    
    @Inject
    @SLSB
    private UserManager userManager;
    
    @Inject
    @Current
    private UserSession userSession;
    
    @Transactional
    private void deleteUser(){
        // this trick allows without checks to delete the own account (no hijacking from the outside via parameter)
        userManager.deleteUser(userSession.getCurrentUser().getName());
    }
%><%
    request.setAttribute("navigation", "delete_user");
    
    if( !userSession.getCurrentUser().isAnonymous() ){
        deleteUser();
        HttpSession mySession = request.getSession(false);
        mySession.invalidate();
    }
    
    response.sendRedirect(request.getContextPath());
    return;
%>
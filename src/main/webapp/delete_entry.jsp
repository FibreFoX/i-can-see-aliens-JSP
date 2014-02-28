<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry"%>
<%@page import="javax.inject.Inject"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession"%>
<%!
    @Inject
    @SLSB
    private BillboardEntryManager billboardEntryManager;

    @Inject
    @Current
    private UserSession userSession;
    
    @Transactional
    private void removeEntry(BillboardEntry foundEntry, String idForDeletion){
        // check if user is allowed to delete
        if( foundEntry.getOwningUser().equals(userSession.getCurrentUser()) ){
            billboardEntryManager.removeEntryWithId(idForDeletion);
        }
    }
%><%
    request.setAttribute("navigation", "delete_entry");

    String id = request.getParameter("id");
    if( id == null || id.trim().length() == 0 || userSession.getCurrentUser().isAnonymous() ){
        // not enough information to kill an entry
    } else {
        BillboardEntry entry = billboardEntryManager.getBillboardEntry(id);
        // check if id is existing
        if( entry != null ){
            removeEntry(entry, id);
        }
    }
    response.sendRedirect(request.getContextPath() + "/entries.jsp");
    return;
%>
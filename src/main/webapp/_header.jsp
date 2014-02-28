<%@page import="javax.inject.Inject"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession"%>
<%@page import="de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current"%>
<%!
    @Inject
    @Current
    private UserSession userSession;
%><%
    User user = userSession.getCurrentUser();
%>
<!-- Bootstrap -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css" />
<link rel="stylesheet" media="screen" href="<%=request.getContextPath()%>/css/style.css" />

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="<%=request.getContextPath()%>/scripts/html5shiv.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/scripts/respond.min.js" type="text/javascript"></script>
<![endif]-->


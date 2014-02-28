<%
HttpSession mySession = request.getSession(false);
mySession.invalidate();
response.sendRedirect(request.getContextPath());
return;
%>
<%!
    private boolean isThatPage(String wanted, Object given) {
        if( given == null ){
            return false;
        }
        if( !(given instanceof String) ){
            return false;
        }
        return wanted.equals(given);
    }
%>
<header role="banner" class="navbar navbar-inverse navbar-fixed-top bs-docs-nav">
    <div class="container">
        <div class="navbar-header">
            <button data-target=".bs-navbar-collapse" data-toggle="collapse" type="button" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand<%=(isThatPage("index", request.getAttribute("navigation"))) ? " active" : ""%>" href="<%=request.getContextPath()%>/index.jsp">Billboard</a>
        </div>
        <nav role="navigation" class="collapse navbar-collapse bs-navbar-collapse">
            <ul class="nav navbar-nav">
                <li <%=(isThatPage("entries", request.getAttribute("navigation"))) ? "class='active'" : ""%>>
                    <a href="<%=request.getContextPath()%>/entries.jsp"><span class="glyphicon glyphicon-list-alt"></span> all entries</a>
                </li>
                <li <%=(isThatPage("members", request.getAttribute("navigation"))) ? "class='active'" : ""%>><a href="<%=request.getContextPath()%>/members.jsp"><span class="glyphicon glyphicon-list"></span> list members</a></li>
                <%
                    if( user.isAnonymous() ){
                %>
                <li <%=(isThatPage("login", request.getAttribute("navigation"))) ? "class='active'" : ""%>><a href="<%=request.getContextPath()%>/login.jsp">login</a></li>
                <li <%=(isThatPage("register", request.getAttribute("navigation"))) ? "class='active'" : ""%>><a href="<%=request.getContextPath()%>/register.jsp">register</a></li>
                <%
                    } else {
                %>
                <li <%=(isThatPage("create_entry", request.getAttribute("navigation"))) ? "class='active'" : ""%>><a href="<%=request.getContextPath()%>/create_entry.jsp"><span class="glyphicon glyphicon-comment"></span> create a new entry</a></li>
                <li <%=(isThatPage("logout", request.getAttribute("navigation"))) ? "class='active'" : ""%>><a href="<%=request.getContextPath()%>/logout.jsp">logout <span class="glyphicon glyphicon-eject"></span></a></li>
                <%
                    }
                %>
            </ul>
        </nav>
    </div>
</header>
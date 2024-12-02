<header>
    <link rel="stylesheet" type="text/css" href="/css/admin/header.css">
    <h1>Navigation</h1>

    <nav class="main-nav">
        <div class="nav-section">
            <h2>Evalutations</h2>
            <ul>
                <li><a href="${request.getContextPath()}/student/evaluation">Evaluation</a></li>
            </ul>
        </div>
    </nav>

    <body>
        <jsp:include page="../template/schedule.jsp" flush="true"/>  
    </body>
</header>
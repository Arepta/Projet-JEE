<header>
    <link rel="stylesheet" type="text/css" href="/css/admin/header.css">
    <h1>Navigation</h1>
    <nav class="main-nav">
        <div class="nav-section">
            <h2>Gestion</h2>
            <ul>
                <li><a href="${request.getContextPath()}/admin/student">Gestion des eleves</a></li>
                <li><a href="${request.getContextPath()}/admin/teacher">Gestion des professeurs</a></li>
            </ul>
        </div>
        <div class="nav-section">
            <h2>Configuration</h2>
            <ul>
                <li><a href="${request.getContextPath()}/admin/config/room">Salles</a></li>
                <li><a href="${request.getContextPath()}/admin/config/program">Cursus</a></li>
                <li><a href="${request.getContextPath()}/admin/config/classLevel">Promotions</a></li>
                <li><a href="${request.getContextPath()}/admin/config/class">Classes</a></li>
                <li><a href="${request.getContextPath()}/admin/config/field">Domaine</a></li>
            </ul>
        </div>
        <div class="nav-section">
            <h2>Cours</h2>
            <ul>
                <li><a href="${request.getContextPath()}/admin/courses/courses">Cours</a></li>
                <li><a href="${request.getContextPath()}/admin/courses/program">Contenu des cursus</a></li>
                <li><a href="${request.getContextPath()}/admin/courses/schedule">EDT</a></li>
            </ul>
        </div>
        <div class="nav-section">
            <h2>Évaluations</h2>
            <ul>
                <li><a href="${request.getContextPath()}/admin/evaluation">Evaluations</a></li>
            </ul>
        </div>
    </nav>
</header>
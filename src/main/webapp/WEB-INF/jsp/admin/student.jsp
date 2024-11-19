<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/admin/student.css">
    <title>Admin - Gestion des élèves</title>
</head>
<body>
    <jsp:include page="header.jsp" flush="true"/>  
    
    <div class="student-msg ${messageType}">
        <span>${message}</span>
    </div>

    <jsp:include page="table.jsp" flush="true"/>  

    <script src="/js/admin/student.js"></script>
    <script>
        initSelect('${levelListe}', '${classesListe}');
    </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/admin/student.css">
    <title>Admin - Gestion des Prof</title>
</head>
<body>
    <jsp:include page="header.jsp" flush="true"/>  
    
    <div class="teacher-msg ${messageType}">
        <span>${message}</span>
    </div>

    <jsp:include page="../template/tableSingle.jsp" flush="true"/>  

</body>
</html>
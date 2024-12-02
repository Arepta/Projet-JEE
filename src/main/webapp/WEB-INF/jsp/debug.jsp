<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Page used for debugging purposes -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Debug Page</title>
</head>
<body>
    <!-- Debugging information display -->
    <h1>Debug Information</h1>
    <!-- Iterate through debug information and display it -->
    <c:forEach var="entry" items="${debugInfo}">
        <p>${entry.key}: ${entry.value}</p>
    </c:forEach>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Page for user login -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <!-- Form for user login -->
    <form action="login" method="post">
        <!-- Input for email address -->
        Email: <input type="email" name="username"><br>
        <!-- Input for password -->
        Password: <input type="password" name="password"><br>
        <!-- Submit button -->
        <input type="submit" value="Login">
    </form>

    <!-- Display validation errors if login fails -->
    <c:if test="${not empty error}">
        <div>
            <h3>Error:</h3>
            <p>${error}</p>
        </div>
    </c:if>
</body>
</html>

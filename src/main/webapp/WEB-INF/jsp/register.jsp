<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Page for user registration -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
</head>
<body>
    <h1>Register</h1>
    <!-- Form for user registration -->
    <form action="register" method="post">
        <!-- Input for first name -->
        First Name: <input type="text" name="name"><br>
        <!-- Input for surname -->
        Surname: <input type="text" name="surname"><br>
        <!-- Input for email address -->
        Email: <input type="email" name="username"><br>
        <!-- Input for date of birth -->
        Date of Birth: <input type="date" name="dateofbirth"><br>
        <!-- Input for password -->
        Password: <input type="password" name="password"><br>
        <!-- Input to confirm password -->
        Confirm Password: <input type="password" name="confirm_password"><br>
        <!-- Submit button -->
        <input type="submit" value="Register">
    </form>

    <!-- Display validation errors if any exist -->
    <c:if test="${not empty error}">
        <div>
            <h3>Errors:</h3>
            <ul>
                <c:forEach var="err" items="${error}">
                    <li>${err}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</body>
</html>

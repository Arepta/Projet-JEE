<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register student Page</title>
</head>
<body>

    ${error}

    <form action="" method="POST">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <label for="email">Email:</label>
        <input type="email" name="username" required value="${old.get(username)}">

        <label for="surname">Nom:</label>
        <input type="text" name="surname" value="${old.get(surname)}">

        <label for="name">prenom:</label>
        <input type="text" name="name" value="${old.get(name)}">

        <label for="dateofbirth">Date de naissance:</label>
        <input type="date" name="dateofbirth" required value="${old.get(dateofbirth)}">

        <label for="password">Mot de passe:</label>
        <input type="password" name="password" required value="${old.get(password)}">

        <label for="password">Mot de passe:</label>
        <input type="password" name="confirm_password" required>

        <button type="submit">S'inscrire</button>
    </form>
</body>
</html>
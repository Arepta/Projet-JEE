<!DOCTYPE html>
<link rel="stylesheet" type="text/css" href="css/index/register.css">
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
        <input type="email" name="username" required value="${old.username}">

        <label for="surname">Nom:</label>
        <input type="text" name="surname" value="${old.surname}">

        <label for="name">prenom:</label>
        <input type="text" name="name" value="${old.name}">

        <label for="dateofbirth">Date de naissance:</label>
        <input type="date" name="dateofbirth" required value="${old.dateofbirth}">

        <label for="password">Mot de passe:</label>
        <input type="password" name="password" required value="${old.password}">

        <label for="password">Mot de passe:</label>
        <input type="password" name="confirm_password" required>

        <button type="submit">S'inscrire</button>
    </form>
</body>
</html>
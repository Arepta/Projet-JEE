<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Test Page</title>
</head>
<body>
    <h1>${content}</h1>
    <hr>
    <h1>${v}</h1>
    <hr>
    <h1>${f}</h1>
    <hr>
    <h1>${r}</h1>

    <h2>User Form</h2>
    <form action="./submitForm" method="POST">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name">
        <span style="color:red;"></span><br><br>

        <label for="email">Email:</label>
        <input id="email" name="confirm_name">
        <span style="color:red;"></span><br><br>

                
        <label>Hobbies:</label><br>
        <input type="checkbox" id="hobby1" name="hobbies" value="Reading">
        <label for="hobby1">Reading</label><br>

        <input type="checkbox" id="hobby2" name="hobbies" value="Traveling">
        <label for="hobby2">Traveling</label><br>

        <input type="checkbox" id="hobby3" name="hobbies" value="Sports">
        <label for="hobby3">Sports</label><br>

        <input type="checkbox" id="hobby4" name="hobbies" value="Music">
        <label for="hobby4">Music</label><br><br>

        <input type="submit" value="Submit">
    </form>
</body>
</html>
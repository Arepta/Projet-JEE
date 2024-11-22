<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Etudiant - Bievenue</title>
    </head>
    <body>
        <div class="dashboard">
        <p><strong>Bonjour ${studentName} ${studentSurname},</strong></p>
        <p>Vous etes inscrit dans la classe : <strong>${studentClass.title} ${studentClass.id}</strong>.</p>
        <p>Promo : <strong>${studentLevel}</strong>.</p>
        <p>Statut de confirmation : <strong>${confirmation}</strong>.</p>
        </div>
        <h2>Emploi du temps</h2>
        <c:import url="schedule.jsp" />
    </body>
    
    
</html>

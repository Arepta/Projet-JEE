<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Emploi du Temps</title>
    <link rel="stylesheet" href="/css/student/schedule.css">
</head>
<body>
    <div class="schedule-container">
        <div class="schedule-header">
            <a href="/student/?startOfWeek=${currentWeekStart.minusDays(7)}" class="navigation-btn">Semaine précédente</a>
            <span class="schedule-date-range">
                Semaine du <strong>${currentWeekStart}</strong> au <strong>${currentWeekEnd}</strong>
            </span>
            <a href="/student/?startOfWeek=${currentWeekStart.plusDays(7)}" class="navigation-btn">Semaine suivante</a>
        </div>
    
        <table class="schedule-table">
            <thead>
                <tr>
                    <th>Horaire</th>
                    <c:forEach var="day" items="${days}">
                        <th>${day}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="hour" items="${timeSlots}">
                    <tr>
                        <td class="hour">${hour}</td>
                        <c:forEach var="day" items="${days}">
                            <td class="day">
                                <c:if test="${schedule[day][hour] != null}">
                                    <c:forEach var="course" items="${schedule[day][hour]}">
                                        <div class="course">
                                            <span class="course-time">${course.startTime} - ${course.endTime}</span>
                                            <span class="course-title">${course.title}</span>
                                            <span class="course-location">${course.location}</span>
                                            <span class="course-teacher">Professeur: ${course.teacherSurname}</span>
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
</body>
</html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/css/template/scheduleDefault.css">

<div class="schedule-container">
    <div class="schedule-title">
        <h1>Emploi du Temps - ${teacherName}</h1>
    </div>

    <div class="schedule-content">
        <div class="schedule-data">
            <div class="schedule-content-title">
                <div class="schedule-content-actions">
                    <a href="/teacher/schedule">Actualiser</a>
                </div>
            </div>

            <div class="schedule-data-container">
                <div class="schedule-data-header">
                    <div class="schedule-data-day">
                        <span>Lundi</span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Mardi</span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Mercredi</span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Jeudi</span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Vendredi</span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Samedi</span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Dimanche</span>
                    </div>
                </div>
                <div class="schedule-data-body" id="schedule-data-body">
                    <!-- Le contenu de l'emploi du temps est chargé dynamiquement -->
                    <c:forEach var="schedule" items="${scheduleData}">
                        <div class="schedule-item">
                            <span>${schedule.course.name} - ${schedule.classes.name} - Salle : ${schedule.room.name}</span>
                            <span>De ${schedule.start} à ${schedule.end}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="schedule-data-action">
                <button onclick="schedule_changeWeek(-1)"><</button>
                <span id="schedule-data-action-page"></span>
                <button onclick="schedule_changeWeek(1)">></button>
            </div>
        </div>
    </div>
</div>

<script src="/js/template/schedule.js"></script>
<script>
    schedule_init('${_schedule_Data}', '${_schedule_Links}', '${_schedule_LinksData}', '${_schedule_NGValuesJSON}');
</script>

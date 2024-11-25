<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="/css/template/scheduleDefault.css">

<div class="schedule-container">
    <div class="schedule-title">
        <h1>${_schedule_Title}</h1>
    </div>

    <div class="schedule-content">
        <div class="schedule-data">
            <div class="schedule-content-title">

                <c:if test="${_schedule_Admin}">
                    <div class="schedule-content-filters">
                        <select class="schedule-data-filter" id="schedule-data-filter-manager" onchange="schedule_setUpFilter()">
                            <option value="0" selected>Par classes</option>
                            <option value="1">Par professeur</option>
                        </select>
                        <select class="schedule-data-filter" id="schedule-data-filter-class" filter="class" onchange="schedule_filter(this)">
                            <c:forEach var="map" items="${_schedule_NGValues['class']}">
                                <option value="${map.key}" >${map.value}</option>
                            </c:forEach>
                        </select>
                        <select class="schedule-data-filter" id="schedule-data-filter-teacher" filter="teacher" onchange="schedule_filter(this)" hidden>
                            <c:forEach var="map" items="${_schedule_NGValues['teacher']}">
                                <option value="${map.key}" >${map.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>  
                
                <div class="schedule-content-actions">
                    <a href="">Actualiser</a>
                    <c:if test="${_schedule_Admin}"> <button onclick="schedule_setFormMode(true);">Nouveau</button> </c:if> 
                </div>
            </div>


            <div class="schedule-data-container">
                <div class="schedule-data-header">
                    <div class="schedule-data-day">
                        <span>Lundi</span>
                        <span></span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Mardi</span>
                        <span></span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Mercredi</span>
                        <span></span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Jeudi</span>
                        <span></span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Vendredi</span>
                        <span></span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Samedi</span>
                        <span></span>
                    </div>
                    <div class="schedule-data-day">
                        <span>Dimanche</span>
                        <span></span>
                    </div>
                </div>
                <div class="schedule-data-body" id="schedule-data-body">
                    
                </div>
            </div>

            <div class="schedule-data-action">
                <button onclick="schedule_changeWeek(-1)"><</button>
                <span id="schedule-data-action-page"></span>
                <button onclick="schedule_changeWeek(1)">></button>
            </div>
        </div>

        <c:if test="${_schedule_Admin}"> 
        <div class="schedule-viewer" id="schedule-viewer">
                <div class="schedule-content-title">
                    <h2 id="schedule-viewer-content-title">Modifier</h2>
                </div>
                <div class="schedule-viewer-error">
                    <c:forEach var="msg" items="${_schedule_ErrorMessages}">
                        <p>${msg}</p>
                    </c:forEach>
                </div>
                <form id="schedule-form" action="" method="POST">
        
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                    <input type="hidden" name="_method" id="schedule-form-method" value="PUT"/>

                    <input class="schedule-form-input" name="id" type="number" value="${_schedule_OldField['id']}" hidden readonly>

                    <label for="class">Classe : </label>
                    <select onchange="schedule_onChangeSelectValue(this)" class="schedule-form-input <c:if test="${_schedule_ErrorField['class'] != null}">${'error'}</c:if>" name="class">
                        <option value=""  <c:if test="${_schedule_OldField['class'] == null || _schedule_OldField['class'].equals('')}">${'selected'}</c:if>></option>
                        <c:forEach var="map" items="${_schedule_NGValues['class']}">
                            <option value="${map.key}"  <c:if test="${_schedule_OldField['class'] == map.key}">${'selected'}</c:if>>${map.value}</option>
                        </c:forEach>
                    </select>

                    <label for="class">Cours : </label>
                    <select onchange="schedule_onChangeSelectValue(this)" class="schedule-form-input <c:if test="${_schedule_ErrorField['course'] != null}">${'error'}</c:if>" name="course">
                        <option value=""  <c:if test="${_schedule_OldField['course'] == null || _schedule_OldField['course'].equals('')}">${'selected'}</c:if>></option>
                        <c:forEach var="map" items="${_schedule_NGValues['course']}">
                            <option value="${map.key}"  <c:if test="${_schedule_OldField['course'] == map.key}">${'selected'}</c:if>>${map.value}</option>
                        </c:forEach>
                    </select>

                    <label for="class">Professeur : </label>
                    <select class="schedule-form-input <c:if test="${_schedule_ErrorField['teacher'] != null}">${'error'}</c:if>" name="teacher">
                        <option value=""  <c:if test="${_schedule_OldField['teacher'] == null || _schedule_OldField['teacher'].equals('')}">${'selected'}</c:if>></option>
                        <c:forEach var="map" items="${_schedule_NGValues['teacher']}">
                            <option value="${map.key}"  <c:if test="${_schedule_OldField['teacher'] == map.key}">${'selected'}</c:if>>${map.value}</option>
                        </c:forEach>
                    </select>

                    <label for="class">Salle : </label>
                    <select class="schedule-form-input <c:if test="${_schedule_ErrorField['room'] != null}">${'error'}</c:if>" name="room">
                        <option value=""  <c:if test="${_schedule_OldField['room'] == null || _schedule_OldField['room'].equals('')}">${'selected'}</c:if>></option>
                        <c:forEach var="map" items="${_schedule_NGValues['room']}">
                            <option value="${map.key}"  <c:if test="${_schedule_OldField['room'] == map.key}">${'selected'}</c:if>>${map.value}</option>
                        </c:forEach>
                    </select>

                    <label for="class">Debut : </label>
                    <input class="schedule-form-input <c:if test="${_schedule_ErrorField['start'] != null}">${'error'}</c:if>" name="start" type="datetime-local" value="${_schedule_OldField['start']}">

                    <label for="class">Fin : </label>
                    <input class="schedule-form-input <c:if test="${_schedule_ErrorField['end'] != null}">${'error'}</c:if>" name="end" type="datetime-local" value="${_schedule_OldField['end']}">
                    
                    <div class="schedule-form-actions" id="schedule-edit-form-actions">
                        <button type="submit">MAJ</button>
                        <button type="submit" onclick="return window.confirm('Supprimer ? Attention cette action est définitive.') ? document.getElementById('schedule-form-method').value = 'DELETE':false;">Supprimé</button>
                    </div>

                    <div class="schedule-form-actions" id="schedule-create-form-actions" style="display:none;">
                        <button type="submit">Nouveau</button>
                    </div>

                </form>
            
        </div>
        </c:if> 
    </div>
</div>

<script src="/js/template/schedule.js"></script>
<script>    
    schedule_init('${_schedule_Data}', '${_schedule_Links}', '${_schedule_LinksData}', '${_schedule_NGValuesJSON}');
   
    schedule_setFormMode(${_schedule_setEdit == null}, false);
    schedule_setUpFilter();

</script>


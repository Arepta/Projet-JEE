<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="/css/template/tableDefault.css">

<div class="table-container">
    <div class="table-title">
        <h1>${_tableSingle_Title}</h1>
    </div>

    <div class="table-content">

        <div class="table-data">
            <div class="table-content-title">
                <h2 id="table-content-number"></h2>

                <c:if test="${_tableSingle_Filters != null}">
                    <div class="table-content-filters">
                        <c:forEach var="filter" items="${_tableSingle_Filters}">

                            <div class="table-data-filter-container">
                                <span>
                                    <c:if test="${_tableSingle_ColumnToLabel[filter] != null}">${_tableSingle_ColumnToLabel[filter]}</c:if>  
                                    <c:if test="${_tableSingle_ColumnToLabel[filter] == null}">${filter}</c:if>  
                                </span>
                                <select class="table-data-filter" filter="${filter}" name="filter-${filter}" onchange="table_filter();table_onChangeSelectValue(this);">
                                    <option value="" selected></option>
                                </select>
                            </div>
                            
                        </c:forEach>
                    </div>
                </c:if>  
                
                <div class="table-content-actions">
                    <a href="">Actualiser</a>
                    <button onclick="table_setFormMode(true)">Nouveau</button>
                </div>
            </div>
            <table>
                <tr id="table-data-head" class="table-data-head">

                    <c:if test="${_tableSingle_ColumnDisplayed != null}">
                        <c:forEach var="attr" items="${_tableSingle_ColumnDisplayed}">
                            <c:if test="${_tableSingle_ColumnToLabel[attr] != null}"><th name="${attr}">${_tableSingle_ColumnToLabel[attr]}</th></c:if>  
                            <c:if test="${_tableSingle_ColumnToLabel[attr] == null}"><th name="${attr}">${attr}</th></c:if>  
                        </c:forEach>
                    </c:if>   

                    <c:if test="${_tableSingle_ColumnDisplayed == null}">
                        <c:forEach var="attr" items="${_tableSingle_DataHeader}">
                            <c:if test="${_tableSingle_ColumnToLabel[attr] != null}"><th name="${attr}">${_tableSingle_ColumnToLabel[attr.key]}</th></c:if>  
                            <c:if test="${_tableSingle_ColumnToLabel[attr] == null}"><th name="${attr}">${attr.key}</th></c:if>  
                        </c:forEach>
                    </c:if>   
                    
                </tr>
                <tbody id="table-data-body" class="table-data-body">
                </tbody>
            </table>
            <div class="table-data-action">
                <button onclick="table_firstPage()"><<</button>
                <button onclick="table_previousPage()"><</button>
                <span id="table-data-action-page"></span>
                <button onclick="table_nextPage()">></button>
                <button onclick="table_lastPage()">>></button>
            </div>
        </div>

        <div class="table-viewer">
            <div class="table-content-title">
                <h2 id="table-viewer-content-title">Modifier</h2>
            </div>
            <div class="table-viewer-error">
                <c:forEach var="msg" items="${_tableSingle_ErrorMessages}">
                    <p>${msg}</p>
                </c:forEach>
            </div>
            <form id="table-form" action="" method="POST">
    
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <input type="hidden" name="_method" id="table-form-method" value="PUT"/>
    
                <c:forEach var="attr" items="${_tableSingle_DataHeader}">
                    <div class="table-form-input-wrapper <c:if test="${_tableSingle_ErrorField[attr.key] != null}">${'error'}</c:if>">
            
                        <c:if test="${ !attr.key.equals('password')}">
                            <label for="${attr.key}">
                                <c:if test="${_tableSingle_ColumnToLabel[attr.key] != null && !fn:contains( _tableSingle_ColumnLock, attr.key )}">${_tableSingle_ColumnToLabel[attr.key]} :</c:if>  
                                <c:if test="${_tableSingle_ColumnToLabel[attr.key] == null && !fn:contains( _tableSingle_ColumnLock, attr.key )}">${attr.key} :</c:if>
                            </label>
    
                            <c:if test="${ _tableSingle_Type[attr.value] == null }">
                                <select class="table-form-input" name="${attr.key}" <c:if test="${fn:contains( _tableSingle_ColumnLock, attr.key )}">${'hidden readonly'}</c:if> onchange="table_onChangeSelectValue(this)" >
                                    <c:if test="${_tableSingle_NGValues[attr.key] != null}">
                                        <option value=""  <c:if test="${_tableSingle_OldField[attr.key] == null || _tableSingle_OldField[attr.key].equals('')}">${'selected'}</c:if>></option>
                                        <c:forEach var="map" items="${_tableSingle_NGValues[attr.key]}">
                                            <option value="${map.key}"  <c:if test="${_tableSingle_OldField[attr.key] == map.key}">${'selected'}</c:if>>${map.value}</option>
                                        </c:forEach>
                                    </c:if>  
                                </select>
                            </c:if>
    
                            <c:if test="${ !_tableSingle_Type[attr.value].equals('select') && _tableSingle_Type[attr.value] != null}">
                                <input class="table-form-input" name="${attr.key}" type="${_tableSingle_Type[attr.value]}" value="${_tableSingle_OldField[attr.key]}" <c:if test="${fn:contains( _tableSingle_ColumnLock, attr.key )}">${'hidden readonly'}</c:if>  >  
                            </c:if>    
                        </c:if>
    
                    </div>
                </c:forEach>
                
                <div class="table-form-actions" id="table-edit-form-actions">
                    <button type="submit">MAJ</button>
                    <button type="submit" onclick="return window.confirm('Supprimer ? Attention cette action est définitive.') ? document.getElementById('table-form-method').value = 'DELETE':false;">Supprimé</button>
                </div>

                <div class="table-form-actions" id="table-create-form-actions" style="display:none;">
                    <button type="submit">Nouveau</button>
                </div>

            </form>
        </div>


    </div>

</div>

<script src="/js/template/tableSingle.js"></script>
<script>    
    table_init('${_tableSingle_Data}', '${_tableSingle_Links}', '${_tableSingle_LinksData}', '${_tableSingle_NGValuesJSON}');
    table_setPage(0);
    table_setFormMode(${ _tableSingle_setEdit == null}, false);

</script>


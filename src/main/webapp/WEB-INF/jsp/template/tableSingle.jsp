<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/css/admin/tableDefault.css">

<div class="table-container">
    <div class="table-title">
        <h1>${_tableSingle_Title}</h1>
    </div>

    <div class="table-content">

        <div class="table-data">
            <div class="table-content-title">
                <h2>Données</h2>
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
                            <label for="${attr.key}"></label>
                                <c:if test="${_tableSingle_ColumnToLabel[attr.key] != null && !attr.key.equals('id')}">${_tableSingle_ColumnToLabel[attr.key]} :</c:if>  
                                <c:if test="${_tableSingle_ColumnToLabel[attr.key] == null && !attr.key.equals('id')}">${attr.key} :</c:if>
                            </label>
    
                            <c:if test="${ _tableSingle_Type[attr.value].equals('select') }">
                                <select class="table-form-input" name="${attr.key}">
                                    <option value="true" <c:if test="${_tableSingle_OldField[attr.key].equals('1')}">${'selected'}</c:if> >Oui</option>
                                    <option value="false" <c:if test="${_tableSingle_OldField[attr.key].equals('0')}">${'selected'}</c:if>>Non</option>
                                </select>
                            </c:if>
    
                            <c:if test="${ _tableSingle_Type[attr.value] == null }">
                                <select class="table-form-input" name="${attr.key}"></select>
                            </c:if>
    
                            <c:if test="${ !_tableSingle_Type[attr.value].equals('select') && _tableSingle_Type[attr.value] != null}">
                                <input class="table-form-input" name="${attr.key}" type="${_tableSingle_Type[attr.value]}" value="${_tableSingle_OldField[attr.key]}" <c:if test="${attr.key.equals('id')}">${'hidden required readonly'}</c:if>  >  
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

<script src="/js/admin/table.js"></script>
<script>    
    table_init('${_tableSingle_Data}');
    table_setPage(0);
    <c:if test="${ _tableSingle_SetCreate }">
        document.getElementById('table-create-form-actions').style = "";
        document.getElementById('table-edit-form-actions').style = "display:none;";
        document.getElementById('table-form-method').value = 'POST';
        document.getElementById('table-viewer-content-title').innerHTML = 'Nouveau';
    </c:if>
</script>

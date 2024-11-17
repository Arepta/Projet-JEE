<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="/css/admin/tableDefault.css">

<div class="table-container">
    <div class="table-title">
        ${_tableTitle}
    </div>
    <div class="table-content">
        <div class="table-data">
            <table>
                <tr id="table-data-head" class="table-data-head">

                    <c:if test="${_tableLineDisplay != null}">
                        <c:forEach var="attr" items="${_tableLineDisplay}">
                            <c:if test="${_tableFieldToLabel[attr] != null}"><th name="${attr}">${_tableFieldToLabel[attr]}</th></c:if>  
                            <c:if test="${_tableFieldToLabel[attr] == null}"><th name="${attr}">${attr}</th></c:if>  
                        </c:forEach>
                    </c:if>   

                    <c:if test="${_tableLineDisplay == null}">
                        <c:forEach var="attr" items="${_tableDataHeader}">
                            <c:if test="${_tableFieldToLabel[attr] != null}"><th name="${attr}">${_tableFieldToLabel[attr.key]}</th></c:if>  
                            <c:if test="${_tableFieldToLabel[attr] == null}"><th name="${attr}">${attr.key}</th></c:if>  
                        </c:forEach>
                    </c:if>   
                    
                </tr>
                <tbody id="table-data-body" class="table-data-body">
                </tbody>
            </table>
        </div>

        <div class="table-viewer">
            <div class="table-viewer-error">
                <c:forEach var="msg" items="${_tableErrorMessages}">
                    <p>${msg}</p>
                </c:forEach>
            </div>
            <form id="table-edit-form" action="" method="POST">
    
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    
                <c:forEach var="attr" items="${_tableDataHeader}">
                    <div class="table-viewer-input <c:if test="${_tableErrorField[attr.key] != null}">${'error'}</c:if>">
            
                        <c:if test="${ !attr.key.equals('password')}">
                            <label for="${attr.key}"></label>
                                <c:if test="${_tableFieldToLabel[attr.key] != null && !attr.key.equals('id')}">${_tableFieldToLabel[attr.key]} :</c:if>  
                                <c:if test="${_tableFieldToLabel[attr.key] == null && !attr.key.equals('id')}">${attr.key} :</c:if>
                            </label>
    
                            <c:if test="${ _tableType[attr.value].equals('select') }">
                                <select class="table-edit-form-input" name="${attr.key}">
                                    <option value="true" <c:if test="${_tableOldField[attr.key].equals('1')}">${'selected'}</c:if> >Oui</option>
                                    <option value="false" <c:if test="${_tableOldField[attr.key].equals('0')}">${'selected'}</c:if>>Non</option>
                                </select>
                            </c:if>
    
                            <c:if test="${ _tableType[attr.value] == null }">
                                <select class="table-edit-form-input" name="${attr.key}"></select>
                            </c:if>
    
                            <c:if test="${ !_tableType[attr.value].equals('select') && _tableType[attr.value] != null}">
                                <input class="table-edit-form-input" name="${attr.key}" type="${_tableType[attr.value]}" value="${_tableOldField[attr.key]}" <c:if test="${attr.key.equals('id')}">${'hidden required readonly'}</c:if>  >  
                            </c:if>    
                        </c:if>
    
                    </div>
                </c:forEach>
                
                <button type="submit">S'inscrire</button>
            </form>
        </div>
    </div>
</div>

<script src="/js/admin/table.js"></script>
<script>    
    table_init('${_tableData}');
    table_setPage(0);
</script>

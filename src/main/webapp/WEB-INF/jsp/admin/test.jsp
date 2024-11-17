<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div class="table-title">
        ${_tableTitle}
    </div>
    <div class="table-data">
        <table>
            <tr id="table-data-head" class="table-data-head">
                <c:forEach var="attr" items="${_tableDataHeader}">
                    <th>
                        <c:if test="${_tableFieldToLabel[attr.key] != null}">${_tableFieldToLabel[attr.key]}</c:if>  
                        <c:if test="${_tableFieldToLabel[attr.key] == null}">${attr.key}</c:if>  
                    </th>
                </c:forEach>
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
        <form action="." method="POST">

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <c:forEach var="attr" items="${_tableDataHeader}">
                <div class="table-viewer-input 
                    <c:if test="${_tableErrorMessages[attr] != null}">error</c:if>">
        
                        <c:if test="${!attr.key.equals('id') && !attr.key.equals('password')}">
                            <label for="${attr.key}"></label>
                                <c:if test="${_tableFieldToLabel[attr.key] != null}">${_tableFieldToLabel[attr.key]}</c:if>  
                                <c:if test="${_tableFieldToLabel[attr.key] == null}">${attr.key}</c:if>

                                <c:if test="${ _tableType[attr.value].equals('select') }">
                                    ${ attr.value }
                                    <select name="${attr.key}">
                                        <option value="1" <c:if test="${_tableOldField[attr]}">${'selected'}</c:if> >Oui</option>
                                        <option value="0" <c:if test="${!_tableOldField[attr]}">${'selected'}</c:if>>Non</option>
                                    </select>
                                </c:if>

                                <c:if test="${ _tableType[attr.value] == null }">
                                    <select name="${attr.key}" id="${attr.key}">

                                    </select>
                                </c:if>

                                <c:if test="${ !_tableType[attr.value].equals('select') && _tableType[attr.value] != null}">
                                    <input name="${attr.key}" type="${_tableType[attr.value]}" value="${_tableOldField[attr]}">  
                                </c:if>
                                
                            :</label>
                        </c:if>

                </div>
            </c:forEach>
            
            <button type="submit">S'inscrire</button>
        </form>
    </div>
</body>
</html>
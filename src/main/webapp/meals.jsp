<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        TABLE {
            border-collapse: collapse;
            width: 50%;
        }

        TD, TH {
            padding: 5px;
            border: 2px solid black;
        }

        TH {
            background: white;
        }
    </style>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<p><a href="meals?action=add">Add meal</a></p>

<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach items="${requestScope.meals}" var="meal">

        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>

        <tr style="color:${(meal.excess ? 'red' : 'green')}">
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
            </td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>

    </c:forEach>
    </tbody>
</table>
</body>
</html>
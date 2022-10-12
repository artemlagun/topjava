<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html lang="ru">
<head>
    <title>Edit meal</title>
    <style>
        dl {
            background: none repeat scroll 0 0 #FAFAFA;
            margin: 8px 0;
            padding: 0;
        }

        dt {
            display: inline-block;
            width: 170px;
        }

        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>${param.action == 'add' ? 'Add meal' : 'Edit meal'}</h2>
<hr>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        <dt>DateTime:</dt>
        <dd><label>
            <input type="datetime-local" value="${param.action == 'update' ?  meal.dateTime : null}" name="dateTime">
        </label></dd>
    </dl>
    <dl>
        <dt>Description:</dt>
        <dd><label>
            <input type="text" value="${meal.description}" size="40" name="description">
        </label></dd>
    </dl>
    <dl>
        <dt>Calories:</dt>
        <dd><label>
            <input type="number" value="${meal.calories}" name="calories">
        </label></dd>
    </dl>
    <button type="submit">Save</button>
    <button onclick="window.history.back()">Cancel</button>
</form>
</body>
</html>

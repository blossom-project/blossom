<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul>
<#list users as group>
    <li>${group.firstname} ${group.lastname}</li></#list>
</ul>
</body>
</html>
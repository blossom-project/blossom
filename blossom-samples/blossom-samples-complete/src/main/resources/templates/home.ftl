<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul>
<@spring.message "home.test.message.overriding.with.same.filename"/>
<#list users as group>
    <li>${group.firstname} ${group.lastname}</li></#list>
</ul>

<#if currentUser??>current user<#else>no current user</#if>
</body>
</html>

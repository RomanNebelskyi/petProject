<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Details</title>
</head>
<body>
<#--<#assign
known=Session.SPRING_SECURITY_CONTEXT??>
<#if known>
    <#assign
    currentUser = Session.SPRING_SECURITY_CONTEXT.authentication.principal>
<#else>
    <#assign
    currentUser=null>
</#if>-->

<h3>Task</h3>
<b>${error_message!""}</b>
<form action="/logout" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="submit" value="Log out">
</form>
<br>
<b>Id - </b>${usr.id}<br>
<b>Name - </b>${usr.name}<br>
<b>Email - </b>${usr.email}<br>
<b>Role - </b>

<#if isEditable?? && isEditable == true>
  <form action="/admin/user-details" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="hidden" name="userId" value="${usr.id}">
    <select name="role">
        <#list roles as role >
      <option <#if usr.role == role> selected="selected" </#if> value="${role}">${role}</option>
        </#list><br>
    </select>
    <input type="submit" value="Change role"><br>
  </form>
<#else>
    ${usr.role}
</#if>
<br>
<b>Registration date - </b>${usr.registrationDate}<br>
<b>Project</b>
<br>


</body>
</html>
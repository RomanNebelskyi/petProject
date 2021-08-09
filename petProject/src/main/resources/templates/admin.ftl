<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Index</title>
</head>
<body>
${error_message!""}
<form action="/admin" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <select name="toSort">
    <option <#if toSort == "id"> selected="selected"</#if> value="id">Id</option>
    <option <#if toSort == "email"> selected="selected"</#if> value="email">Email</option>
    <option <#if toSort == "name"> selected="selected"</#if> value="name">Name</option>
    <option <#if toSort == "role"> selected="selected"</#if> value="role">Role</option>
    <option <#if toSort == "registationDate"> selected="selected"</#if> value="registationDate">
      Registration Date
    </option>
  </select>
  <select name="flow">
      <#if flow == "ASC" >
        <option selected="selected" value="ASC">Ascending</option>
        <option value="DESC">Descending</option>
      <#else>
        <option value="ASC">Ascending</option>
        <option selected="selected" value="DESC">Descending</option>
      </#if>
  </select>
  <input type="submit">
</form>
<br>
<table>
  <th>Id</th>
  <th>Name</th>
  <th>Email</th>
  <th>Role</th>
  <th>Registration date</th>
  <th>Project</th>

    <#list users as usr>
      <tr>
        <td>${usr.id}</td>
        <td><a href="/admin/user-details?userId=${usr.id}">${usr.name}</a></td>
        <td>${usr.email}</td>
        <td>${usr.role}</td>

        <td>${usr.registrationDate}</td>
        <td>
            <#if usr.role == "USER">
                ${usr.tasks?size}
            <#elseif usr.role == "PROGRAMMER">
            <#else >
            </#if>
        </td>
      </tr>
    </#list>
</table>

<br><br>
<button><a href="/edit-acc">Edit your account</a></button>
<br><br>
<form action="/logout" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="submit" value="Log out">
</form>
</body>
</html>
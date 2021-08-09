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
<#--<h2>Hello, dear ${user.name!"ERROR, USER NOT FOUND, CONTACT ADMIN"} </h2>-->

<b>Current task:</b>

<table>
  <th>Order name</th>
  <th>Status</th>
  <th>Deadline</th>
  <th>Description</th>
  <th>Tech requirements</th>
  <th></th>

  <tr>

      <#if !task??>
        <h2>You have no tasks. Add Your first task!</h2>
      <#else>


        <td>${task.name!""}</td>
        <td>${task.status!""}</td>
        <td>${task.deadline?string["yyyy-MM-dd"]!""}</td>
        <td>${task.description!""}</td>
        <td>
            <#if task.techReq??>
              <a href="/techTasks/${task.techReq}" download>
                <b>Download</b>
              </a>
            </#if>
        </td>

      </#if>
  </tr>

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
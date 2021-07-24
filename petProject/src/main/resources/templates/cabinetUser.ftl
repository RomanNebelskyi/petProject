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

<h3>${error_message!""}</h3>
<table>

  <th>Order name</th>
  <th>Status</th>
  <th>Deadline</th>
  <th>Price</th>
  <th>Description</th>
  <th></th>
    <#list tasks as task>
  <tr>

          <#if tasks?size==0>
            <h2>You have no tasks. Add Your first task!</h2>
          <#else>
          </#if>
        <td>${task.name!""}</td>
        <td>${task.status!""}</td>
        <td>${task.deadline!""}</td>
        <td>${task.price!""} $</td>
        <td>${task.description!""}</td>
        <td>
                <#if task.status == "WAITING_ACCEPT"|| task.status == "QUEUED" || task.status =="ESTIMATED">
                  <button>
                    <a href="/task/cancel?id=${task.id}">Cancel task</a></button>
                </#if>
        </td>

  </tr>
    </#list>
</table>

<br>
<button><a href="/task/add">Add task</a></button>

</body>
</html>
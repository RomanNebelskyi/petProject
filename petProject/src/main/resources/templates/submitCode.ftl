<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Add new task</title>


</head>
<body>
<h3>Submit your order</h3>

<i>Properly check all the data and accept or decline this task</i><br>

<b>Buyer name - </b> ${buyerName!""}<br>
<b>Name - </b>${task.name!""}<br>
<b>Deadline - </b>${task.deadline!""}<br>
<b>Price - </b>${task.price!""}<br>
<b>Description - </b>${task.description!""}<br>

<form method="post" action="/task/submit-code">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="hidden" name="taskId" value="${taskId}">
  <input type="text" required="required" name="code" placeholder="Enter your secret code">
  <input type="submit" value="Accept">
</form>

<button><a href="/task/cancel?id=${taskId}">Decline</a></button>

</body>
</html>
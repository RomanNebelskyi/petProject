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

<form method="post" action="/task/submit-code">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="hidden" name="taskId" value="${taskId}">
  <input type="text" required="required" name="code" placeholder="Enter your secret code">
  <input type="submit">
</form>

</body>
</html>
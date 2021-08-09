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


<form action="/edit-acc/name" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <label> Enter your new email
    <input type="email" name="newEmail" value="${usr.email}" required="required"
           placeholder="Enter your new email">
  </label>
  <br>
  <label> Enter your new name
    <input type="text" required="required" name="newName" placeholder="Enter your password"
           value="${usr.name}">
  </label>
  <br>
  <input type="submit">
</form>
<br>
<button><a href="/cabinet">Cabinet</a></button>
<br><br>
<form action="/logout" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="submit" value="Log out">
</form>
</body>
</html>
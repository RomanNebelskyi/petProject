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


<form action="/edit-acc/password" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <label>
    <input type="password" name="old" required="required" placeholder="Enter your old password">
  </label><br><br>
  <label>
    <input type="password" required="required" name="password" placeholder="Enter your password">
  </label><br><br>
  <label>
    <input type="password" name="confirm" required="required" placeholder="Repeat your password">
  </label><br><br>
  <input type="submit">
</form>

<form action="/logout" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="submit" value="Log out">
</form>
</body>
</html>
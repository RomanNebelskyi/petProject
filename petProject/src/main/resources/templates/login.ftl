<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Login</title>
</head>
<body>
${message!""}
${error_message!""}


<form action="/login" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <label>
    <input type="email" required="required" name="username" placeholder="Enter email">
  </label>
  <label>
    <input type="password" name="password" required="required" placeholder="Enter password">
  </label>
  <input type="submit">
</form>
<br>
<br>
<a href="/forgot-password">Forgot your password?</a>
<a href="/registration">Register</a>

</body>
</html>
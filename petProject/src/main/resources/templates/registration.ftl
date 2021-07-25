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
<h2>${error_message!""}</h2>

<form action="/registration" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <label>
    <input type="text" name="name" required="required" placeholder="Enter your name">
  </label><br><br>
  <label>
    <input type="email" name="email" required="required" placeholder="Enter your email">
  </label><br><br>
  <label>
    <input type="password" required="required" name="password" placeholder="Enter your password">
  </label><br><br>
  <label>
    <input type="password" name="confirm" required="required" placeholder="Repeat your password">
  </label><br><br>
  <input type="submit">
</form>
<br>
<br>
<a href="/forgot-password">Forgot your password?</a><br>
<a href="/registration">Register</a>

</body>
</html>
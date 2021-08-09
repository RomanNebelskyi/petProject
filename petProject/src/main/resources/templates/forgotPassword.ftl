<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Forgot password</title>
</head>
<body>


<h2>Forgot password</h2>
${error_message!""}<br>
<b>Enter your email and we will send you your new password. Please check your spam messages.</b>

<form action="/forgot-password" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="email" required="required" name="email" placeholder="Enter email">
  <input type="submit">
</form>
<br>
<a href="/login">Login</a>
<a href="/registration">Register</a>

</body>
</html>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Add new task</title>

<script> window.onload = function() {
    var today = new Date();
    var dd = today.getDate()+1;
    var mm = today.getMonth()+1;
    var yyyy = today.getFullYear();
    if(dd<10){
      dd='0'+dd
    }
    if(mm<10){
      mm='0'+mm
    }

    today = yyyy+'-'+mm+'-'+dd;
    document.getElementById("datefield").setAttribute("min", today);
    today = (yyyy+1)+'-'+mm+'-'+dd;
    document.getElementById("datefield").setAttribute("max", today);
  };
  </script>

</head>
<body>


<form method="post" action="/task/add">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <label>
    <input type="text" required="required" name="name" placeholder="Name of your task">
  </label>
  <label>
    <input id="datefield"  required="required" type="date" name="deadline" placeholder="Enter deadline"  >
  </label>
  <label>
    <input type="number" required="required" step="10" name="price" min="50" placeholder="Price in USD">
  </label>
  <label>
    <input type="text" name="description" placeholder="Enter description">
  </label>
  <input type="submit">
</form>
<br>
<form action="/logout" method="post">
  <input type="hidden" name="_csrf" value="${_csrf.token}">
  <input type="submit" value="Log out">
</form>

</body>
</html>
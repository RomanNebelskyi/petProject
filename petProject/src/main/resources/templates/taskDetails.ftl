<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Details</title>
</head>
<body>

<h3>Task</h3>

<form method="post" action="/task/change">
  <input type="hidden" name="_csrf" value="${_csrf.token}">

  <b>Id </b> ${task.id}<br> <input type="hidden" name="id" value="${task.id}">
  <b>Name </b> <input value="${task.name}" type="text" name="name"
                      placeholder="Name of your task"><br>
  <b>Status </b> <br>
  <b>Deadline </b><input id="datefield" type="date" name="deadline" placeholder="Enter deadline"
                         value="${task.deadline}"><br>
  <b>Price </b> <input type="number" step="10" name="price" value="${task.price}" min="50"
                       placeholder="Price in USD"><br>
  <b>Buyer </b>${task.buyer.name}<br>
  <b>Workers </b> ${task.workers?size}<br>
  <details>
    <table>
        <#if workers?size == 0 >
          <th>Id</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
          <th></th>

          <h3>No workers. Please add one to start</h3>
        <#else >
            <#list workers as worker>
              <tr>
                <td>${worker.id}</td>
                <td>${worker.name}</td>
                <td>${worker.email}</td>
                <td>${worker.role}</td>
                <td>
                  <button><a href="/worker/delete?worker=${worker.id}&task=${task.id}">Delete</a>
                  </button>
                </td>
              </tr>
            </#list>
        </#if>
    </table>
  </details>
  <b>Description</b>
  <details>
    <summary>Description</summary>
      ${task.description} <br>
    <input type="text" name="description" placeholder="Enter description"
           value="${task.description}">
  </details>
  <br>
  <input type="submit" value="Change task">
</form>
<br>
<button><a href="/task/submit?id=${task.id}">Submit</a></button>
// ADD WORKERS NOT EARLIER THAN SOME PERIOD
// ESTIMATING PROJECT AND SENDING NEW DETAILS, EMAIL CONFIRMATION
// GOING TO THE QUEUE

<h2>Free workers</h2>
<h4>${error_message!""}</h4>

<#if freeWorkers?size != 0>
    <#list freeWorkers as free>
      <tr>
        <td>${free.id}</td>
        <td>${free.name}</td>
        <td>${free.email}</td>
        <td>${free.role}</td>
        <td>
          <button><a href="/worker/add?worker=${free.id}&task=${task.id}">Add</a></button>
        </td>
      </tr>
    </#list>
<#else >
  <h3>No free workers. Please wait</h3>
</#if>
</body>
</html>
<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>BLOSSOM | Login</title>

  <link href="/css/bootstrap.min.css" rel="stylesheet">
  <link href="/font-awesome/css/font-awesome.css" rel="stylesheet">

  <link href="/css/animate.css" rel="stylesheet">
  <link href="/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg">

<div class="middle-box text-center loginscreen animated fadeInDown">
  <div>
    <h3>Welcome to Blossom</h3>
    <p>Perfectly designed and easy to use Back-Office based on Spring-Boot</p>
    <p>Login in. To see it in action.</p>
    <form class="m-t" role="form" method="POST" action="/blossom/login">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <div class="form-group">
        <input type="text" class="form-control" placeholder="Username" required="required" name="username">
      </div>
      <div class="form-group">
        <input type="password" class="form-control" placeholder="Password" required="required" name="password">
      </div>
      <button type="submit" class="btn btn-primary block full-width m-b">Login</button>

      <#if error.isPresent() && SPRING_SECURITY_LAST_EXCEPTION??>
        <p class="alert alert-danger">${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
      </#if>
    </form>
    <p class="m-t"> <small>Blossom &copy; 2017</small> </p>
  </div>
</div>

<!-- Mainly scripts -->
<script src="/js/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

</body>

</html>

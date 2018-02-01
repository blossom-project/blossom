<#import "/spring.ftl" as spring>

<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Blossom | <@spring.message "login.page.title"/></title>

  <link href="/blossom/public/css/bootstrap.min.css" rel="stylesheet">
  <link href="/blossom/public/font-awesome/css/font-awesome.css" rel="stylesheet">

  <link href="/blossom/public/css/animate.css" rel="stylesheet">
  <link href="/blossom/public/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg">

<div class="middle-box text-center loginscreen">
  <div>
    <h3><@spring.message "login.page.welcome"/></h3>
    <p><@spring.message "login.page.description"/></p>
    <form class="m-t" role="form" method="POST" action="/blossom/login" autocomplete="off" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <div class="form-group">
        <input type="text" class="form-control" placeholder="<@spring.message "login.page.form.login"/>" required="required" name="username">
      </div>
      <div class="form-group">
        <input type="password" class="form-control" placeholder="<@spring.message "login.page.form.password"/>" required="required" name="password">
      </div>
      <button type="submit" class="btn btn-primary block full-width m-b" id="login"><@spring.message "login.page.form.button.login"/></button>

      <#if error.isPresent() && SPRING_SECURITY_LAST_EXCEPTION??>
        <#if SPRING_SECURITY_LAST_EXCEPTION.message??>
          <p class="alert alert-danger">${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
        </#if>
      </#if>
      <a href="/blossom/public/forgotten_password"><small><@spring.message "login.forgotten.password"/></small></a>
    </form>
    <p class="m-t"> <small>Blossom &copy; 2017</small> </p>
  </div>
</div>

<!-- Mainly scripts -->
<script src="/blossom/public/js/jquery-3.1.1.min.js"></script>
<script src="/blossom/public/js/bootstrap.min.js"></script>

</body>

</html>

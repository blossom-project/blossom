<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>BLOSSOM | <@spring.message "ask.password.page.title"/></title>

  <link href="/blossom/public/css/bootstrap.min.css" rel="stylesheet">
  <link href="/blossom/public/font-awesome/css/font-awesome.css" rel="stylesheet">

  <link href="/blossom/public/css/animate.css" rel="stylesheet">
  <link href="<@spring.theme "stylesheet"/>" rel="stylesheet">

</head>
<body class="gray-bg">
  <div class="passwordBox">
    <div class="row">

      <div class="col-md-12">
        <div class="ibox-content">

          <h2 class="font-bold"><@spring.message "ask.password.page.title"/></h2>

          <p>
            <@spring.message "ask.password.description"/>
          </p>

          <div class="row">

            <div class="col-lg-12">
              <form class="m-t" role="form" method="POST" novalidate>
                <@spring.bind "askPasswordForm.loginOrEmail"/>
                <div class="form-group">
                  <input type="text" name="loginOrEmail" class="form-control"  value="${askPasswordForm.loginOrEmail}" placeholder="<@spring.message "ask.password.form.loginOrEmail.label"/>" required="">
                </div>
                <#list spring.status.errorMessages as error>
                  <span class="help-block text-danger m-b-none">${error}</span>
                </#list>

                <br/>

                <#if resetPasswordMail?? && resetPasswordMail>
                  <p class="alert alert-success">
                    <@spring.message "ask.password.mail.sent"/>
                  </p>
                  <br/>
                </#if>

                  <button type="submit" class="btn btn-primary block full-width m-b"><@spring.message "ask.password.action"/></button>
                  <a href="/blossom/login" class="btn btn-sm btn-default block full-width m-b"><@spring.message "cancel"/></a>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <hr>
    <div class="row">
      <div class="col-md-12 text-center">
        <small>Blossom &copy; 2017</small>
      </div>
    </div>
  </div>
</body>

</html>

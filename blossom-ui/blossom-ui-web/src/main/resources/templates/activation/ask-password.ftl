<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>BLOSSOM | <@spring.message "ask.password.page.title"/></title>

  <link href="/css/bootstrap.min.css" rel="stylesheet">
  <link href="/font-awesome/css/font-awesome.css" rel="stylesheet">

  <link href="/css/animate.css" rel="stylesheet">
  <link href="/css/style.css" rel="stylesheet">

</head>
<body class="gray-bg">
  <div class="passwordBox animated fadeInDown">
    <div class="row">

      <div class="col-md-12">
        <div class="ibox-content">

          <h2 class="font-bold"><@spring.message "ask.password.page.title"/></h2>

          <p>
          <@spring.message "ask.password.description"/>
          </p>

          <div class="row">

            <div class="col-lg-12">
              <form class="m-t" role="form" method="POST">
                <div class="form-group">
                  <input type="email" name="" class="form-control" placeholder="<@spring.message "ask.password.form.loginOrEmail.label"/>" required="">
                </div>

                <button type="submit" class="btn btn-primary block full-width m-b">Send new password</button>

              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <hr>
    <div class="row">
      <div class="col-md-6">
        Copyright Example Company
      </div>
      <div class="col-md-6 text-right">
        <small>© 2014-2015</small>
      </div>
    </div>
  </div>
</body>

</html>

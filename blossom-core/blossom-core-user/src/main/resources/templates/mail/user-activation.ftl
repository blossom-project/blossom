<#import "/spring.ftl" as spring>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta name="viewport" content="width=device-width">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>${message("activation.title")} ${name}</title>
  <link href="${basePath}/mail/styles.css" media="all" rel="stylesheet" type="text/css">
</head>

<body>

<table class="body-wrap">
  <tbody>
  <tr>
    <td></td>
    <td class="container" width="600">
      <div class="content">
        <table class="main" width="100%" cellpadding="0" cellspacing="0">
          <tbody>
          <tr>
            <td class="content-wrap">
              <table cellpadding="0" cellspacing="0">
                <tbody>
                <tr>
                  <td class="content-block">
                    <#assign name=user.firstname+' '+user.lastname/>
                    <h3>${message("activation.title")} ${name} !</h3>
                  </td>
                </tr>
                <tr>
                  <td class="content-block">
                    ${message("activation.content")}
                  </td>
                </tr>
                <tr>
                  <td class="content-block">
                    ${message("activation.identifier")} <b>${user.identifier}</b>
                  </td>
                </tr>
                <tr>
                  <td class="content-block aligncenter">
                    <a href="${basePath}/blossom/public/activate?token=${token}&lang=${lang}" class="btn-primary">
                      ${message("activation.content.action")}
                    </a>
                  </td>
                </tr>
                </tbody>
              </table>
            </td>
          </tr>
          </tbody>
        </table>
        <div class="footer">
          <table width="100%">
            <tbody>
            <tr>
              <td class="aligncenter content-block">Blossom &copy;</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </td>
    <td></td>
  </tr>
  </tbody>
</table>
</body>
</html>

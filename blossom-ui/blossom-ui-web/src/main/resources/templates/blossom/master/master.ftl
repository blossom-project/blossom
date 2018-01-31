<#import "header.ftl" as header />
<#import "footer.ftl" as footer />
<#import "navigation.ftl" as navigation />

<#macro default currentUser>
<!DOCTYPE html>
<html>
  <head>
      <@header.default/>
      <@header.styles/>
      <@header.scripts/>
      <meta name="_csrf" content="${_csrf.token}"/>
      <meta name="_csrf_header" content="${_csrf.headerName}"/>
  </head>
  <body class="fixed-navigation md-skin">
    <div id="wrapper">
        <@navigation.drawer menu=menu currentUser=currentUser></@navigation.drawer>

        <div id="page-wrapper" class="gray-bg" >
          <@navigation.top currentUser=currentUser></@navigation.top>
          <#nested/>
          <@footer.default></@footer.default>
        </div>
    </div>

    <@footer.styles/>
    <@footer.scripts/>

    <script>
      $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
          xhr.setRequestHeader(header, token);
        });
      });
    </script>
  </body>
</html>
</#macro>

<#macro headless>
<!DOCTYPE html>
<html>
  <head>
      <@header.default/>
      <@header.styles/>
  </head>
  <body class="md-skin">
      <#nested/>
 	  <@footer.scripts/>
  </body>
</html>
</#macro>

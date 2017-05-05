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
  </head>
  <body class="fixed-navigation">
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
  <body>
      <#nested/>
 	  <@footer.scripts/>
  </body>
</html>
</#macro>

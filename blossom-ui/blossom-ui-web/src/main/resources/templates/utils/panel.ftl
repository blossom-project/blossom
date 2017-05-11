<#import "/spring.ftl" as spring>

<#macro autoReloadPanel id url delayInMillis=10000>
<div id="${id}" class="ibox float-e-margins">
  <div class="ibox-content">
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>
  </div>
</div>

  <script>
    $(document).ready(function(){
      var loadingTemplate = '<div class="sk-spinner sk-spinner-wave">' +
        '<div class="sk-rect1"></div>' +
        '<div class="sk-rect2"></div>' +
        '<div class="sk-rect3"></div>' +
        '<div class="sk-rect4"></div>' +
        '<div class="sk-rect5"></div>' +
        '</div>';

      var update_${id} = function(){
        $("#${id}").find(".ibox-content").toggleClass("sk-loading");

        $.get('${url}',function(data){
          var ibox = $(data);
          var content = ibox.find(".ibox-content");
          if(!content.find(".sk-spinner").length){
              content.prepend($(loadingTemplate));
          }
          $('#${id}').html(ibox.html());
        });
      };

      update_${id}();

      setInterval(update_${id}, ${delayInMillis?c});
    });
  </script>
</#macro>

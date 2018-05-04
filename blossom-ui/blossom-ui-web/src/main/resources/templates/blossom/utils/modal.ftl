<#import "/spring.ftl" as spring>

<#macro large id href>
  <div class="modal inmodal fade" id="${id}" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
      </div>
    </div>
  </div>
  <script>
    $('#${id}').on('shown.bs.modal', function () {
      $(this).find('.modal-content').load("${href}");
    })
  </script>
</#macro>

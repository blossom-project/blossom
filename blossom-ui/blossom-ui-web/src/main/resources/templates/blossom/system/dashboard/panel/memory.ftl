<#import "/spring.ftl" as spring>

<div id="memory" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5><@spring.message "dashboard.jvm.memory"/></h5>
  </div>
  <div class="ibox-content">
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>

    <div class="row">
      <div class="col-xs-12">
        <ul class="stat-list">
          <li>
            <h2 class="no-margins">${memory.jvm.percentage?string[".0"]}%</h2>
            <small><@spring.message "dashboard.jvm.memory.global"/></small>
            <div class="stat-percent small"><@spring.message "dashboard.jvm.memory.used"/>
              : ${memory.jvm.used} / <@spring.message "dashboard.jvm.memory.total"/>
              : ${memory.jvm.total}</div>
            <div class="progress progress-mini">
              <div style="width:${memory.jvm.percentage?c}%" class="progress-bar"></div>
            </div>
          </li>
          <li>
            <h2 class="no-margins ">${memory.heap.percentage?string[".0"]}%</h2>
            <small><@spring.message "dashboard.jvm.memory.heap"/></small>
            <div class="stat-percent small"><@spring.message "dashboard.jvm.memory.used"/>
              : ${memory.heap.used} / <@spring.message "dashboard.jvm.memory.committed"/>
              : ${memory.heap.committed} / <@spring.message "dashboard.jvm.memory.max"/>
              : ${memory.heap.max}</div>
            <div class="progress progress-mini">
              <div style="width: ${memory.heap.percentage?c}%" class="progress-bar"></div>
            </div>
          </li>
          <li>
            <h2 class="no-margins ">${memory.nonheap.percentage?string[".0"]}%</h2>
            <small><@spring.message "dashboard.jvm.memory.nonheap"/></small>
            <div class="stat-percent small"><@spring.message "dashboard.jvm.memory.used"/>
              : ${memory.nonheap.used} / <@spring.message "dashboard.jvm.memory.committed"/>
              : ${memory.nonheap.committed} / <@spring.message "dashboard.jvm.memory.max"/>
              : ${memory.nonheap.max}</div>
            <div class="progress progress-mini">
              <div style="width: ${memory.nonheap.percentage?c}%" class="progress-bar"></div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

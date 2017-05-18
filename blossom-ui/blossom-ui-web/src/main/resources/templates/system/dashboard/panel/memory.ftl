<div id="memory" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5>Memory</h5>
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
            <small>Global</small>
            <div class="stat-percent">used : ${memory.jvm.used} / total : ${memory.jvm.total}</div>
            <div class="progress progress-mini">
              <div style="width:${memory.jvm.percentage?c}%" class="progress-bar"></div>
            </div>
          </li>
          <li>
            <h2 class="no-margins ">${memory.heap.percentage?string[".0"]}%</h2>
            <small>Heap memory</small>
            <div class="stat-percent">used : ${memory.heap.used} / committed : ${memory.heap.committed} / max : ${memory.heap.max}</div>
            <div class="progress progress-mini">
              <div style="width: ${memory.heap.percentage?c}%" class="progress-bar"></div>
            </div>
          </li>
          <li>
            <h2 class="no-margins ">${memory.nonheap.percentage?string[".0"]}%</h2>
            <small>Non-heap memory</small>
            <div class="stat-percent">used : ${memory.nonheap.used} / committed : ${memory.nonheap.committed} / max : ${memory.nonheap.max}</div>
            <div class="progress progress-mini">
              <div style="width: ${memory.nonheap.percentage?c}%" class="progress-bar"></div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

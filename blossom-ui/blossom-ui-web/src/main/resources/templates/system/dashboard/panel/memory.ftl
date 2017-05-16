<div id="status" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5>Server status</h5>
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
            <h2 class="no-margins">{{memory.all.percentage}}%</h2>
            <small>Global</small>
            <div class="stat-percent">{{memory.all.used}} / {{memory.all.total}}</div>
            <div class="progress progress-mini">
              <div :style="{'width': memory.all.percentage+'%'}" class="progress-bar"></div>
            </div>
          </li>
          <li>
            <h2 class="no-margins ">{{memory.heap.percentage}}%</h2>
            <small>Heap memory</small>
            <div class="stat-percent">{{memory.heap.used}} / {{memory.heap.committed}}</div>
            <div class="progress progress-mini">
              <div :style="{'width': memory.heap.percentage+'%'}" class="progress-bar"></div>
            </div>
          </li>
          <li>
            <h2 class="no-margins ">{{memory.nonheap.percentage}}%</h2>
            <small>Non-heap memory</small>
            <div class="stat-percent">{{memory.nonheap.used}} / {{memory.nonheap.committed}}</div>
            <div class="progress progress-mini">
              <div :style="{'width': memory.nonheap.percentage+'%'}" class="progress-bar"></div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

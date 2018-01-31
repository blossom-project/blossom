package fr.blossom.ui.web.system.dashboard;

import com.google.common.collect.Lists;
import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Statistic;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController
@RequestMapping("/system/dashboard")
@OpenedMenu("dashboard")
@PreAuthorize("hasAuthority('system:dashboard:manager')")
public class DashboardController {

  private final static Logger logger = LoggerFactory.getLogger(DashboardController.class);

  private final HealthEndpoint healthEndpoint;
  private final MetricsEndpoint metricsEndpoint;

  @Autowired
  private MeterRegistry registry;

  public DashboardController(HealthEndpoint healthEndpoint, MetricsEndpoint metricsEndpoint) {
    this.healthEndpoint = healthEndpoint;
    this.metricsEndpoint = metricsEndpoint;
  }

  @GetMapping
  public ModelAndView dashboard() {
    return new ModelAndView("blossom/system/dashboard/dashboard");
  }

  @GetMapping("/status")
  public ModelAndView status(Model model) {
    Health health = healthEndpoint.health();
    model.addAttribute("health", health);
    model.addAttribute("uptime",
      1000 * metricsEndpoint.metric("process.uptime", null).getMeasurements().get(0).getValue());
    return new ModelAndView("blossom/system/dashboard/panel/status", model.asMap());
  }

  @GetMapping("/memory")
  public ModelAndView memory() {
    JVMMemoryUsage memoryUsage = new JVMMemoryUsage(
      metric("jvm.memory.max").longValue(),
      metric("jvm.memory.max").longValue() - metric("jvm.memory.used").longValue());
    MemoryUsage heap = new MemoryUsage(
      0,
      metric("jvm.memory.used", Lists.newArrayList("area:heap")).longValue(),
      metric("jvm.memory.committed", Lists.newArrayList("area:heap")).longValue(),
      metric("jvm.memory.max", Lists.newArrayList("area:heap")).longValue());
    MemoryUsage nonheap = new MemoryUsage(
      0,
      metric("jvm.memory.used", Lists.newArrayList("area:nonheap")).longValue(),
      metric("jvm.memory.committed", Lists.newArrayList("area:nonheap")).longValue(),
      metric("jvm.memory.max", Lists.newArrayList("area:nonheap")).longValue());

    MemoryMetrics memoryMetrics = new MemoryMetrics(memoryUsage, heap, nonheap);

    return new ModelAndView("blossom/system/dashboard/panel/memory", "memory", memoryMetrics);
  }

  @GetMapping("/jvm")
  public ModelAndView jvm() {
    ClassMetrics classes = new ClassMetrics(
      metric("jvm.classes.loaded").intValue(),
      metric("jvm.classes.unloaded").intValue());

    GCMetrics gcs = new GCMetrics(
      metricsEndpoint.metric("jvm.gc.pause", null).getAvailableTags()
        .stream()
        .filter(t -> t.getTag().equals("action")).flatMap(action -> action.getValues().stream())
        .map(value -> {
          MetricResponse metric = metricsEndpoint
            .metric("jvm.gc.pause", Lists.newArrayList("action:" + value));
          return new GCMetric(
            value,
            metric.getMeasurements().stream().filter(m -> m.getStatistic() == Statistic.COUNT)
              .findFirst().get().getValue().longValue(),
            metric.getMeasurements().stream().filter(m -> m.getStatistic() == Statistic.TOTAL_TIME)
              .findFirst().get().getValue().doubleValue()
          );
        })
        .collect(Collectors.toList())
    );

    ThreadMetrics threads = new ThreadMetrics(
      metric("jvm.threads.live").intValue(),
      metric("jvm.threads.peak").intValue(),
      metric("jvm.threads.daemon").intValue());
    ProcessorMetrics processors = new ProcessorMetrics(
      metric("system.cpu.count").intValue()
    );

    JVMMetrics jvmMetrics = new JVMMetrics(classes, gcs, threads, processors);
    return new ModelAndView("blossom/system/dashboard/panel/jvm", "jvm", jvmMetrics);
  }

  @GetMapping("/charts")
  public ModelAndView requests() {
    return new ModelAndView("blossom/system/dashboard/panel/charts");
  }

  private Double metric(String name) {
    return this.metricsEndpoint.metric(name, null).getMeasurements().get(0).getValue();
  }

  private Double metric(String name, List<String> tags) {
    return this.metricsEndpoint.metric(name, tags).getMeasurements().get(0).getValue();
  }

  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) {
      return bytes + " B";
    }
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }

  public class MemoryMetrics {

    private final JVMMemoryUsage jvm;
    private final MemoryUsage heap;
    private final MemoryUsage nonheap;

    public MemoryMetrics(JVMMemoryUsage jvm, MemoryUsage heap, MemoryUsage nonheap) {
      this.jvm = jvm;
      this.heap = heap;
      this.nonheap = nonheap;
    }

    public JVMMemoryUsage getJvm() {
      return jvm;
    }

    public MemoryUsage getHeap() {
      return heap;
    }

    public MemoryUsage getNonheap() {
      return nonheap;
    }

  }


  public static class JVMMemoryUsage {

    private final long total;
    private final long free;

    public JVMMemoryUsage(long total, long free) {
      this.total = total;
      this.free = free;
    }

    public String getTotal() {
      return humanReadableByteCount(total, true);
    }

    public String getFree() {
      return humanReadableByteCount(free, true);
    }

    public String getUsed() {
      return humanReadableByteCount(total - free, true);
    }

    public float getPercentage() {
      return ((float) total - (float) free) / (float) total * 100;
    }
  }

  public static class MemoryUsage {

    private final long init;
    private final long used;
    private final long committed;
    private final long max;

    public MemoryUsage(long init, long used, long committed, long max) {
      this.init = init;
      this.used = used;
      this.committed = committed;
      this.max = max;
    }

    public String getInit() {
      return humanReadableByteCount(init, true);
    }

    public String getUsed() {
      return humanReadableByteCount(used, true);
    }

    public String getCommitted() {
      return humanReadableByteCount(committed, true);
    }

    public String getMax() {
      return humanReadableByteCount(max, true);
    }

    public float getPercentage() {
      return (float) used / (float) committed * 100;
    }
  }

  public class JVMMetrics {

    private final ClassMetrics classes;
    private final GCMetrics gcs;
    private final ThreadMetrics threads;
    private final ProcessorMetrics processors;

    public JVMMetrics(ClassMetrics classes, GCMetrics gcs, ThreadMetrics threads,
      ProcessorMetrics processors) {
      this.classes = classes;
      this.gcs = gcs;
      this.threads = threads;
      this.processors = processors;
    }

    public ClassMetrics getClasses() {
      return classes;
    }

    public GCMetrics getGcs() {
      return gcs;
    }

    public ThreadMetrics getThreads() {
      return threads;
    }

    public ProcessorMetrics getProcessors() {
      return processors;
    }

  }


  public static class ClassMetrics {

    private final long total;
    private final long loaded;
    private final long unloaded;

    public ClassMetrics(long loaded, long unloaded) {
      this.total = loaded + unloaded;
      this.loaded = loaded;
      this.unloaded = unloaded;
    }

    public long getTotal() {
      return total;
    }

    public long getLoaded() {
      return loaded;
    }

    public long getUnloaded() {
      return unloaded;
    }
  }

  public static class GCMetrics {

    private final List<GCMetric> types;

    public GCMetrics(List<GCMetric> types) {
      this.types = types;
    }

    public List<GCMetric> getTypes() {
      return types;
    }

  }

  public static class GCMetric {

    private final String name;
    private final long count;
    private final double time;

    public GCMetric(String name, long count, double time) {
      this.name = name;
      this.count = count;
      this.time = time;
    }

    public String getName() {
      return name;
    }

    public long getCount() {
      return count;
    }

    public double getTime() {
      return time;
    }
  }

  public static class ThreadMetrics {

    private final long live;
    private final long peak;
    private final long daemon;

    public ThreadMetrics(long live, long peak, long daemon) {
      this.live = live;
      this.peak = peak;
      this.daemon = daemon;
    }

    public long getLive() {
      return live;
    }

    public long getPeak() {
      return peak;
    }

    public long getDaemon() {
      return daemon;
    }
  }

  public static class ProcessorMetrics {

    private final int total;

    public ProcessorMetrics(int total) {
      this.total = total;
    }

    public int getTotal() {
      return total;
    }
  }
}

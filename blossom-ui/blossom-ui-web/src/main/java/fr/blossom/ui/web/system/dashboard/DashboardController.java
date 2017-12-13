package fr.blossom.ui.web.system.dashboard;

import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.health.Health;
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

  public DashboardController(HealthEndpoint healthEndpoint, MetricsEndpoint metricsEndpoint) {
    this.healthEndpoint = healthEndpoint;
    this.metricsEndpoint = metricsEndpoint;
  }

  @GetMapping
  public ModelAndView dashboard() {
    return new ModelAndView("system/dashboard/dashboard");
  }

  @GetMapping("/status")
  public ModelAndView status(Model model) {
    Health health = healthEndpoint.invoke();
    Map<String, Object> metrics = metricsEndpoint.invoke();
    model.addAttribute("health", health);
    model.addAttribute("uptime", metrics.get("instance.uptime"));
    return new ModelAndView("system/dashboard/panel/status", model.asMap());
  }

  @GetMapping("/memory")
  public ModelAndView memory() {
    Map<String, Object> metrics = metricsEndpoint.invoke();
    return new ModelAndView("system/dashboard/panel/memory", "memory", new MemoryMetrics(metrics));
  }

  @GetMapping("/jvm")
  public ModelAndView jvm() {
    Map<String, Object> metrics = metricsEndpoint.invoke();
    return new ModelAndView("system/dashboard/panel/jvm", "jvm", new JVMMetrics(metrics));
  }

  @GetMapping("/charts")
  public ModelAndView requests() {
    return new ModelAndView("system/dashboard/panel/charts");
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

    public MemoryMetrics(Map<String, Object> metrics) {
      jvm = new JVMMemoryUsage(metrics);
      heap = new MemoryUsage("heap", metrics);
      nonheap = new MemoryUsage("nonheap", metrics);
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

    public class JVMMemoryUsage {

      private final long total;
      private final long free;

      public JVMMemoryUsage(Map<String, Object> metrics) {
        this.total = (Long) metrics.get("mem") * 1024;
        this.free = (Long) metrics.get("mem.free") * 1024;
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

    public class MemoryUsage {

      private final long init;
      private final long used;
      private final long committed;
      private final long max;

      public MemoryUsage(String prefix, Map<String, Object> metrics) {
        this.init = (Long) metrics.get(prefix + ".init") * 1024;
        this.used = (Long) metrics.get(prefix + ".used") * 1024;
        this.committed = (Long) metrics.get(prefix + ".committed") * 1024;
        this.max = (Long) metrics.get(prefix) * 1024;
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
  }

  public class JVMMetrics {

    private final ClassMetrics classes;
    private final GCMetrics gcs;
    private final ThreadMetrics threads;
    private final ProcessorMetrics processors;

    public JVMMetrics(Map<String, Object> metrics) {
      this.classes = new ClassMetrics(metrics);
      this.gcs = new GCMetrics(metrics);
      this.threads = new ThreadMetrics(metrics);
      this.processors = new ProcessorMetrics(metrics);
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

    public class ClassMetrics {

      private final long total;
      private final long loaded;
      private final long unloaded;

      public ClassMetrics(Map<String, Object> metrics) {
        this.total = (Long) metrics.get("classes");
        this.loaded = (Long) metrics.get("classes.loaded");
        this.unloaded = (Long) metrics.get("classes.unloaded");
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

    public class GCMetrics {

      private final List<GCMetric> types;

      public GCMetrics(Map<String, Object> metrics) {
        this.types = metrics.keySet().stream()
          .filter(key -> key.startsWith("gc."))
          .map(key -> key.split("\\.")[1])
          .distinct()
          .map(gc -> new GCMetric(gc, (Long) metrics.get("gc." + gc + ".count"),
            (Long) metrics.get("gc." + gc + ".time")))
          .collect(Collectors.toList());
      }

      public List<GCMetric> getTypes() {
        return types;
      }

      public class GCMetric {

        private final String name;
        private final long count;
        private final long time;

        public GCMetric(String name, long count, long time) {
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

        public long getTime() {
          return time;
        }
      }
    }

    public class ThreadMetrics {

      private final long total;
      private final long totalStarted;
      private final long peak;
      private final long daemon;

      public ThreadMetrics(Map<String, Object> metrics) {
        this.total = (Long) metrics.get("threads");
        this.totalStarted = (Long) metrics.get("threads.totalStarted");
        this.peak = (Long) metrics.get("threads.peak");
        this.daemon = (Long) metrics.get("threads.daemon");
      }

      public long getTotal() {
        return total;
      }

      public long getTotalStarted() {
        return totalStarted;
      }

      public long getPeak() {
        return peak;
      }

      public long getDaemon() {
        return daemon;
      }
    }

    public class ProcessorMetrics {

      private final int total;

      public ProcessorMetrics(Map<String, Object> metrics) {
        this.total = (Integer) metrics.get("processors");
      }

      public int getTotal() {
        return total;
      }
    }
  }
}

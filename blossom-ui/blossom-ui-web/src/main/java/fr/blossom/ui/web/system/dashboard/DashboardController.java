package fr.blossom.ui.web.system.dashboard;

import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
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
    return new ModelAndView("system/dashboard/dashboard");
  }

  @GetMapping("/status")
  public ModelAndView status(Model model) {
    Health health = healthEndpoint.health();
    model.addAttribute("health", health);
    model.addAttribute("uptime", 15000000);
    return new ModelAndView("system/dashboard/panel/status", model.asMap());
  }

  @GetMapping("/memory")
  public ModelAndView memory() {
    return new ModelAndView("system/dashboard/panel/memory", "memory", new MemoryMetrics());
  }

  @GetMapping("/jvm")
  public ModelAndView jvm() {
    return new ModelAndView("system/dashboard/panel/jvm", "jvm", new JVMMetrics());
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

    public MemoryMetrics() {
      jvm = new JVMMemoryUsage();
      heap = new MemoryUsage("heap");
      nonheap = new MemoryUsage("nonheap");
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

      public JVMMemoryUsage() {
        this.total = 10L * 1024;
        this.free = 5L * 1024;
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

      public MemoryUsage(String prefix) {
        this.init = 150L * 1024;
        this.used = 100L * 1024;
        this.committed = 200L * 1024;
        this.max = 500L * 1024;
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

    public JVMMetrics() {
      this.classes = new ClassMetrics();
      this.gcs = new GCMetrics();
      this.threads = new ThreadMetrics();
      this.processors = new ProcessorMetrics();
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

      public ClassMetrics() {
        this.total = 200000;
        this.loaded = 150000;
        this.unloaded = 4000;
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

      public GCMetrics() {
        this.types = IntStream.rangeClosed(0, 2)
          .mapToObj(i -> new GCMetric("GC " + i, 10, 150))
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

      public ThreadMetrics() {
        this.total = 50;
        this.totalStarted = 45;
        this.peak = 55;
        this.daemon = 25;
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

      public ProcessorMetrics() {
        this.total = 8;
      }

      public int getTotal() {
        return total;
      }
    }
  }
}

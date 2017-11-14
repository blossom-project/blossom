package fr.blossom.core.common.actuator;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Created by Maël Gargadennnec on 17/05/2017.
 */
public class TraceStatisticsMvcEndpoint extends EndpointMvcAdapter {
  private final TraceStatisticsEndpoint endpoint;

  public TraceStatisticsMvcEndpoint(TraceStatisticsEndpoint endpoint) {
    super(endpoint);
    this.endpoint = endpoint;
  }


  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String invoke(@RequestParam(name="period", required = false, defaultValue = "PAST_WEEK") Period period) {
    Instant from = null;
    Instant to = null;
    String precision= null;
    if(period!=null){
      from = period.from();
      to = period.to();
      precision=period.precision();
    }
    return this.endpoint.invoke(from,to ,precision).toString();
  }


  public enum Period {
    PAST_WEEK {
      @Override
      public Instant from() {
        return LocalDate.now().minus(1, ChronoUnit.WEEKS).atStartOfDay().toInstant(ZoneOffset.UTC);
      }
      @Override
      public String precision() {
        return "2h";
      }
    },
    PAST_5_DAYS {
      @Override
      public Instant from() {
        return LocalDate.now().minus(5,ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC);
      }

      @Override
      public String precision() {
        return "1h";
      }
    },
    PAST_3_DAYS {
      @Override
      public Instant from() {
        return LocalDate.now().minus(3,ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC);
      }
      @Override
      public String precision() {
        return "1h";
      }
    },
    PAST_2_DAYS {
      @Override
      public Instant from() {
        return LocalDate.now().minus(2,ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC);
      }

      @Override
      public String precision() {
        return "1h";
      }
    },
    YESTERDAY {
      @Override
      public Instant from() {
        return LocalDate.now().minus(1,ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC);
      }

      @Override
      public Instant to() {
        return LocalDate.now().minus(1,ChronoUnit.DAYS).atTime(23,59,59).toInstant(ZoneOffset.UTC);
      }

      @Override
      public String precision() {
        return "15m";
      }
    },
    TODAY {
      @Override
      public Instant from() {
        return LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
      }

      @Override
      public String precision() {
        return "15m";
      }
    },
    PAST_24_HOURS {
      @Override
      public Instant from() {
        return Instant.now().minus(24, ChronoUnit.HOURS);
      }
      @Override
      public String precision() {
        return "15m";
      }
    },
    PAST_12_HOURS {
      @Override
      public Instant from() {
        return Instant.now().minus(12, ChronoUnit.HOURS);
      }
      @Override
      public String precision() {
        return "5m";
      }
    },
    PAST_6_HOURS {
      @Override
      public Instant from() {
        return Instant.now().minus(6, ChronoUnit.HOURS);
      }
      @Override
      public String precision() {
        return "5m";
      }
    },
    PAST_HOUR {
      @Override
      public Instant from() {
        return Instant.now().minus(1, ChronoUnit.HOURS);
      }
      @Override
      public String precision() {
        return "1m";
      }
    },
    PAST_30_MINUTES {
      @Override
      public Instant from() {
        return Instant.now().minus(30, ChronoUnit.MINUTES);
      }

      @Override
      public String precision() {
        return "30s";
      }
    },
    PAST_5_MINUTES {
      @Override
      public Instant from() {
        return Instant.now().minus(5, ChronoUnit.MINUTES);
      }

      @Override
      public String precision() {
        return "10s";
      }
    };

    public Instant from(){
      return null;
    }

    public Instant to(){
      return null;
    }

    public String precision(){
      return "1h";
    }

  }
}

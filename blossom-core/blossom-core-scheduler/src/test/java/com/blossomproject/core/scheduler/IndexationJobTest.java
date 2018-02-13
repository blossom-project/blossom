package com.blossomproject.core.scheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.blossomproject.core.common.search.IndexationEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class IndexationJobTest {

  @Test
  public void should_execute_job() throws Exception {
    IndexationEngine indexationEngine = mock(IndexationEngine.class);
    TestIndexationJob job = spy(new TestIndexationJob(indexationEngine));


    job.execute(null);

    verify(job, times(1)).getIndexationEngine();
    verify(indexationEngine, times(1)).indexFull();
  }

  public static class TestIndexationJob extends IndexationJob {

    private final IndexationEngine indexationEngine;

    TestIndexationJob(IndexationEngine indexationEngine) {
      this.indexationEngine = indexationEngine;
    }

    @Override
    protected IndexationEngine getIndexationEngine() {
      return this.indexationEngine;
    }
  }
}

package fr.blossom.core.common.search;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndexationEngineImplTest {

  private final static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

  @Test
  public void should_object_writer_ignore_configuration_of_object_mapper()
    throws ParseException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.setDateFormat(df);

    String toParse = "20-12-2014 02:30:00";
    Date date = df.parse(toParse);
    Map<String,Object> event = Maps.newHashMap();
    event.put("party", date);

    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString(toParse));

    ObjectWriter writer = mapper.writer(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String writerResult = writer.writeValueAsString(event);
    assertThat(writerResult, not(containsString(toParse)));
  }
}

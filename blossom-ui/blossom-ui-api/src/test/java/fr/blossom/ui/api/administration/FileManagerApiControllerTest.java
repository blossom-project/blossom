package fr.blossom.ui.api.administration;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.search.SearchResult;
import fr.blossom.module.filemanager.FileDTO;
import fr.blossom.module.filemanager.FileService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class FileManagerApiControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private FileService service;

  @Mock
  private SearchEngineImpl<FileDTO> searchEngine;

  private FileManagerApiController controller;

  @Before
  public void setUp() {
    controller = new FileManagerApiController(service, searchEngine);
  }

  @Test
  public void should_get_paged_files_without_query_parameter() {
    when(service.getAll(any(Pageable.class)))
      .thenAnswer(a -> new PageImpl<FileDTO>(Lists.newArrayList()));
    controller.list(null, new PageRequest(0, 20));
    verify(service, times(1)).getAll(any(Pageable.class));
  }

  @Test
  public void should_get_paged_files_with_query_parameter() {
    when(searchEngine.search(any(String.class), any(Pageable.class)))
      .thenAnswer(a -> new SearchResult<>(0, new PageImpl<FileDTO>(Lists.newArrayList())));
    controller.list("test", null);
    verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
  }

  @Test
  public void should_create_with_null_body() {
    thrown.expect(IllegalArgumentException.class);

    controller.fileUpload(null);
  }

  @Test
  public void should_create_with_file_upload() throws IOException, SQLException {
    MultipartFile multipart = new MockMultipartFile("test.pdf", new byte[1024]);
    when(service.upload(eq(multipart))).thenReturn(new FileDTO());

    ResponseEntity<FileDTO> response = controller.fileUpload(multipart);

    verify(service, times(1)).upload(eq(multipart));

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CREATED);
  }

  @Test
  public void should_create_with_io_exception() throws IOException, SQLException {
    MultipartFile multipart = new MockMultipartFile("test.pdf", new byte[1024]);
    when(service.upload(eq(multipart))).thenThrow(new IOException());

    ResponseEntity<FileDTO> response = controller.fileUpload(multipart);

    verify(service, times(1)).upload(eq(multipart));

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void should_create_with_sql_exception() throws IOException, SQLException {
    MultipartFile multipart = new MockMultipartFile("test.pdf", new byte[1024]);
    when(service.upload(eq(multipart))).thenThrow(new SQLException());

    ResponseEntity<FileDTO> response = controller.fileUpload(multipart);

    verify(service, times(1)).upload(eq(multipart));

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void should_create_empty_multipart() throws IOException, SQLException {
    MultipartFile multipart = mock(MultipartFile.class);
    when(multipart.isEmpty()).thenReturn(true);

    ResponseEntity<FileDTO> response = controller.fileUpload(multipart);

    verify(service, times(0)).upload(eq(multipart));

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
  }

  @Test
  public void should_get_one_without_id() {
    thrown.expect(IllegalArgumentException.class);
    controller.get(null);
  }

  @Test
  public void should_get_one_with_id_found() {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenAnswer(a -> {
      FileDTO fileDTO = new FileDTO();
      fileDTO.setId((Long) a.getArguments()[0]);
      return fileDTO;
    });
    ResponseEntity<FileDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue((response.getBody().getId().equals(id)));
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_get_one_with_id_not_found(){
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<FileDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_serve_one_without_id() throws IOException, SQLException {
    thrown.expect(IllegalArgumentException.class);
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<InputStreamResource> response = controller.serve(null);
  }

  @Test
  public void should_serve_one_with_id_found() throws IOException, SQLException {
    Long id = 1L;
    FileDTO file = mock(FileDTO.class);

    when(service.getOne(eq(id))).thenReturn(file);
    when(file.getContentType()).thenReturn("application/pdf");
    when(file.getName()).thenReturn("test.pdf");
    when(file.getId()).thenReturn(1L);
    when(file.getSize()).thenReturn(1024L);
    when(service.download(eq(id))).thenReturn(new ByteArrayInputStream(new byte[1024]));

    ResponseEntity<InputStreamResource> response = controller.serve(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
    Assert.assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0).contains("application/pdf"));
    Assert.assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_LENGTH));
    Assert.assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_LENGTH).get(0).contains("1024"));
    Assert.assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
    Assert.assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).contains("test.pdf"));
    Assert.assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).contains("Content-Disposition: inline;"));


    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_serve_one_with_id_not_found() throws IOException, SQLException {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<InputStreamResource> response = controller.serve(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }
}

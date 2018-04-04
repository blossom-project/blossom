package com.blossomproject.ui.web.content.filemanager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StreamUtils;

import com.blossomproject.module.filemanager.FileDTO;
import com.blossomproject.module.filemanager.FileService;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  FileService fileService;

  @InjectMocks
  FileController fileController;

  @Test
  public void should_return_not_found_without_file() throws Exception {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = new MockHttpServletResponse();
    when(fileService.getOne(any())).thenReturn(null);

    fileController.serve(1L, request, response);

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void should_return_file() throws Exception {
    FileDTO fileDTO = new FileDTO();
    fileDTO.setContentType("type_test");
    fileDTO.setSize(1L);
    fileDTO.setName("test_name");

    byte[] bytes = new byte[StreamUtils.BUFFER_SIZE];
    InputStream inputStream = new ByteArrayInputStream(bytes);

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = new MockHttpServletResponse();
    when(fileService.getOne(any())).thenReturn(fileDTO);
    when(fileService.download(eq(1L))).thenReturn(inputStream);

    fileController.serve(1L, request, response);

    assertEquals("type_test", response.getHeader(HttpHeaders.CONTENT_TYPE));
    assertEquals(1L + "", response.getHeader(HttpHeaders.CONTENT_LENGTH));
    assertEquals("Content-Disposition: inline; filename=\"test_name\"",
        response.getHeader(HttpHeaders.CONTENT_DISPOSITION));
  }

  @Test
  public void should_throw_SQL_exception() throws Exception {
    FileDTO fileDTO = new FileDTO();
    fileDTO.setContentType("type_test");
    fileDTO.setSize(1L);
    fileDTO.setName("test_name");

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = new MockHttpServletResponse();
    when(fileService.getOne(any())).thenReturn(fileDTO);
    when(fileService.download(eq(1L))).thenThrow(new SQLException());

    thrown.expect(SQLException.class);

    fileController.serve(1L, request, response);
  }

  @Test
  public void should_throw_IO_exception() throws Exception {
    FileDTO fileDTO = new FileDTO();
    fileDTO.setContentType("type_test");
    fileDTO.setSize(1L);
    fileDTO.setName("test_name");

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = new MockHttpServletResponse();
    when(fileService.getOne(any())).thenReturn(fileDTO);
    when(fileService.download(eq(1L))).thenThrow(new FileNotFoundException());

    thrown.expect(IOException.class);

    fileController.serve(1L, request, response);
  }
}

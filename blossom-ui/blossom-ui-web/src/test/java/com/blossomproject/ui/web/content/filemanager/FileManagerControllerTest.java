package com.blossomproject.ui.web.content.filemanager;

import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SearchResult;
import com.blossomproject.module.filemanager.FileDTO;
import com.blossomproject.module.filemanager.FileService;
import com.google.common.collect.Lists;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({LoggerFactory.class})
public class FileManagerControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private FileService service;

    @Mock
    private Logger logger;

    @Mock
    private SearchEngineImpl<FileDTO> searchEngine;

    @InjectMocks
    private FileManagerController controller;

    @Test
    public void should_display_page() throws Exception {
        Model model = new ExtendedModelMap();
        ModelAndView result = controller.getPage(model);

        assertEquals("blossom/filemanager/filemanager", result.getViewName());
        assertEquals(model, result.getModel());
    }

    @Test
    public void should_display_paged_files_without_query_parameter() throws Exception {

        when(service.getAll(any(Pageable.class)))
                .thenAnswer(a -> new PageImpl<FileDTO>(Lists.newArrayList()));
        ModelAndView result = controller.getFiles(new ExtendedModelMap(), PageRequest.of(0, 20), "");

        verify(service, times(1)).getAll(any(Pageable.class));

        assertEquals("blossom/filemanager/filelist", result.getViewName());
        assertTrue(result.getModelMap().containsKey("files"));
    }

    @Test
    public void should_display_paged_files_with_query_parameter() throws Exception {

        when(searchEngine.search(any(String.class), any(Pageable.class)))
                .thenAnswer(a -> new SearchResult<>(0, new PageImpl<FileDTO>(Lists.newArrayList())));
        ModelAndView result = controller.getFiles(new ExtendedModelMap(), PageRequest.of(0, 20), "test");

        verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));

        assertEquals("blossom/filemanager/filelist", result.getViewName());
        assertTrue(result.getModelMap().containsKey("files"));
    }

    @Test
    public void should_create_with_empty_file() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(true);
        controller.fileUpload(multipartFile, null);
        verifyZeroInteractions(service);
    }


    public void should_create_with_file_upload() throws IOException, SQLException {
        MultipartFile multipart = new MockMultipartFile("test.pdf", new byte[1024]);
        when(service.upload(eq(multipart))).thenReturn(new FileDTO());

        controller.fileUpload(multipart, null);
        verify(service, times(1)).upload(eq(multipart));
    }

    @Test
    @Ignore
    public void should_create_with_io_exception() throws IOException, SQLException {
        mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);

        MultipartFile multipart = new MockMultipartFile("test.pdf", new byte[1024]);
        when(service.upload(eq(multipart))).thenThrow(new IOException());

        controller.fileUpload(multipart, null);

        verify(service, times(1)).upload(eq(multipart));
        verify(logger, times(1)).error(any());
    }

}

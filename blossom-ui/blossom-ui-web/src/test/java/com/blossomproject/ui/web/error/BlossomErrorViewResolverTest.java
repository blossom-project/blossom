package com.blossomproject.ui.web.error;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.engine.jdbc.ReaderInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlossomErrorViewResolverTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    ApplicationContext applicationContext;

    @Mock
    ResourceProperties resourceProperties;

    @Mock
    TemplateAvailabilityProviders templateAvailabilityProviders;

    private BlossomErrorViewResolver controller;

    @Before
    public void setUp() {
        controller = new BlossomErrorViewResolver(applicationContext, resourceProperties, templateAvailabilityProviders);
    }

    @Test
    public void should_display_model_with_specific_error() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        ModelAndView resultMock = new ModelAndView("specific_error");

        doReturn(resultMock).when(spyController).resolve(eq(String.valueOf(HttpStatus.OK)), any(Map.class));

        ModelAndView result = spyController.resolveErrorView(mock(HttpServletRequest.class), HttpStatus.OK, new ExtendedModelMap());

        Assert.assertEquals(resultMock, result);
        verify(spyController, times(1)).resolve(any(), any());
    }

    @Test
    public void should_display_model_with_error_not_in_series() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);

        doReturn(null).when(spyController).resolve(eq(String.valueOf(HttpStatus.OK)), any(Map.class));

        ModelAndView result = spyController.resolveErrorView(mock(HttpServletRequest.class), HttpStatus.OK, new ExtendedModelMap());

        Assert.assertNull(result);
        verify(spyController, times(1)).resolve(any(), any());
    }

    @Test
    public void should_display_model_with_error_in_series() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        ModelAndView resultMock = new ModelAndView("serie_error");

        doReturn(null).when(spyController).resolve(eq(String.valueOf(HttpStatus.NOT_FOUND)), any(Map.class));
        doReturn(resultMock).when(spyController).resolve(eq("4xx"), any(Map.class));

        ModelAndView result = spyController.resolveErrorView(mock(HttpServletRequest.class), HttpStatus.NOT_FOUND, new ExtendedModelMap());

        Assert.assertEquals(resultMock, result);
        verify(spyController, times(2)).resolve(any(), any());
    }

    @Test
    public void should_resolve_with_no_prefix() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        Map<String, Object> map = new HashMap();
        map.put("test", "testValue");

        doReturn(mock(TemplateAvailabilityProvider.class)).when(templateAvailabilityProviders).getProvider(anyString(), eq(applicationContext));

        ModelAndView result = spyController.resolve("test", map);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(result, new ModelAndView("blossom/error/test", map)));

        verify(templateAvailabilityProviders, times(1)).getProvider(anyString(), any(ApplicationContext.class));
    }

    @Test
    public void should_resolve_with_prefix() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        Map<String, Object> map = new HashMap();
        map.put("path", "/blossom/prefix_test/");

        doReturn(mock(TemplateAvailabilityProvider.class)).when(templateAvailabilityProviders).getProvider(anyString(), eq(applicationContext));

        ModelAndView result = spyController.resolve("test", map);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(result, new ModelAndView("blossom/error/blossom/test", map)));

        verify(templateAvailabilityProviders, times(1)).getProvider(anyString(), any(ApplicationContext.class));
    }

    @Test
    public void should_resolve_without_specific_provider() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        Map<String, Object> map = new HashMap();

        doReturn(null).when(templateAvailabilityProviders).getProvider(eq("blossom/error/test"), eq(applicationContext));
        doReturn(mock(TemplateAvailabilityProvider.class)).when(templateAvailabilityProviders).getProvider(eq("error/default"), eq(applicationContext));

        ModelAndView result = spyController.resolve("test", map);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(result, new ModelAndView("error/default", map)));

        verify(templateAvailabilityProviders, times(2)).getProvider(anyString(), any(ApplicationContext.class));
    }

    @Test
    public void should_resolve_with_resource_without_provider() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        Map<String, Object> map = new HashMap();

        doReturn(null).when(templateAvailabilityProviders).getProvider(eq("blossom/error/test"), eq(applicationContext));
        doReturn(null).when(templateAvailabilityProviders).getProvider(eq("error/default"), eq(applicationContext));

        ModelAndView resultMock = new ModelAndView("resolved");
        doReturn(resultMock).when(spyController).resolveResource(eq("error/default"), eq(map));

        ModelAndView result = spyController.resolve("test", map);

        Assert.assertEquals(result, resultMock);

        verify(templateAvailabilityProviders, times(2)).getProvider(anyString(), any(ApplicationContext.class));
        verify(spyController, times(1)).resolveResource(anyString(), any(Map.class));
    }


    @Test
    public void should_resolve_resource_null_without_locations() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);
        when(resourceProperties.getStaticLocations()).thenReturn(new String[0]);
        ModelAndView result = spyController.resolveResource("test", new HashMap<>());

        Assert.assertNull(result);

        verify(resourceProperties, times(1)).getStaticLocations();
    }

    @Test
    public void should_resolve_resource_null_with_resource_exception() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);

        Resource resource = mock(Resource.class);
        when(applicationContext.getResource(eq("location"))).thenReturn(resource);
        when(resource.createRelative(eq("test.html"))).thenThrow(new IOException());

        when(resourceProperties.getStaticLocations()).thenReturn(new String[]{"location"});
        ModelAndView result = spyController.resolveResource("test", new HashMap<>());

        Assert.assertNull(result);

        verify(resourceProperties, times(1)).getStaticLocations();
        verify(applicationContext, times(1)).getResource(anyString());
        verify(resource, times(1)).createRelative(anyString());
    }

    @Test
    public void should_resolve_resource_null_without_existing_resources() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);

        Resource resource = mock(Resource.class);
        when(applicationContext.getResource(eq("location"))).thenReturn(resource);
        when(resource.createRelative(eq("test.html"))).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        when(resourceProperties.getStaticLocations()).thenReturn(new String[]{"location"});
        ModelAndView result = spyController.resolveResource("test", new HashMap<>());

        Assert.assertNull(result);

        verify(resourceProperties, times(1)).getStaticLocations();
        verify(applicationContext, times(1)).getResource(anyString());
        verify(resource, times(1)).createRelative(anyString());
        verify(resource, times(1)).exists();
    }

    @Test
    public void should_resolve_resource_null_with_existing_resources() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);

        Resource resource = mock(Resource.class);
        when(applicationContext.getResource(eq("location"))).thenReturn(resource);
        when(resource.createRelative(eq("test.html"))).thenReturn(resource);
        when(resource.exists()).thenReturn(true);

        when(resourceProperties.getStaticLocations()).thenReturn(new String[]{"location", "location2"});
        ModelAndView result = spyController.resolveResource("test", new HashMap<>());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.getView() instanceof BlossomErrorViewResolver.HtmlResourceView);

        verify(resourceProperties, times(1)).getStaticLocations();
        verify(applicationContext, times(1)).getResource(anyString());
        verify(resource, times(1)).createRelative(anyString());
        verify(resource, times(1)).exists();
    }

    @Test
    public void should_resolve_resource_null_with_one_existing_resource() throws Exception {
        BlossomErrorViewResolver spyController = spy(controller);

        Resource resource1 = mock(Resource.class);
        when(applicationContext.getResource(eq("location"))).thenReturn(resource1);
        when(resource1.createRelative(eq("test.html"))).thenReturn(resource1);
        when(resource1.exists()).thenReturn(false);

        Resource resource2 = mock(Resource.class);
        when(applicationContext.getResource(eq("location2"))).thenReturn(resource2);
        when(resource2.createRelative(eq("test.html"))).thenReturn(resource2);
        when(resource2.exists()).thenReturn(true);

        when(resourceProperties.getStaticLocations()).thenReturn(new String[]{"location", "location2"});
        ModelAndView result = spyController.resolveResource("test", new HashMap<>());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.getView() instanceof BlossomErrorViewResolver.HtmlResourceView);

        verify(resourceProperties, times(1)).getStaticLocations();
        verify(applicationContext, times(2)).getResource(anyString());
        verify(resource1, times(1)).createRelative(anyString());
        verify(resource1, times(1)).exists();
        verify(resource2, times(1)).createRelative(anyString());
        verify(resource2, times(1)).exists();
    }

    @Test
    public void should_render_html_resource_view() throws Exception {
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ReaderInputStream(new StringReader("test")));

        BlossomErrorViewResolver.HtmlResourceView spyHtmlResourceView = spy(new BlossomErrorViewResolver.HtmlResourceView(resource));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getOutputStream()).thenReturn(mock(ServletOutputStream.class));

        spyHtmlResourceView.render(new HashMap<>(), request, response);

        verify(response, times(1)).setContentType(eq(MediaType.TEXT_HTML_VALUE));
        verify(resource, times(1)).getInputStream();
        verify(response, times(1)).getOutputStream();
    }

    @Test
    public void should_render_html_resource_view_input_stream_exception() throws Exception {
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenThrow(new IOException());

        BlossomErrorViewResolver.HtmlResourceView spyHtmlResourceView = spy(new BlossomErrorViewResolver.HtmlResourceView(resource));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        thrown.expect(IOException.class);
        spyHtmlResourceView.render(new HashMap<>(), request, response);
    }

    @Test
    public void should_render_html_resource_view_output_stream_exception() throws Exception {
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ReaderInputStream(new StringReader("test")));

        BlossomErrorViewResolver.HtmlResourceView spyHtmlResourceView = spy(new BlossomErrorViewResolver.HtmlResourceView(resource));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getOutputStream()).thenThrow(IOException.class);

        thrown.expect(IOException.class);
        spyHtmlResourceView.render(new HashMap<>(), request, response);
    }
}

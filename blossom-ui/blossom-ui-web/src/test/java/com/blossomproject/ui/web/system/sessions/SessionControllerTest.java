package com.blossomproject.ui.web.system.sessions;

import com.blossomproject.ui.current_user.CurrentUser;
import com.blossomproject.ui.security.LoginAttemptsService;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionControllerTest {

    @Mock
    SessionRegistry sessionRegistry;

    @Mock
    LoginAttemptsService loginAttemptsService;

    @InjectMocks
    @Spy
    private SessionController controller;

    @Test
    public void should_display_sessions_without_principals() {
        when(sessionRegistry.getAllPrincipals()).thenReturn(new ArrayList<>());

        ModelAndView result = controller.sessions(new ExtendedModelMap());
        Assert.assertTrue(((Map) result.getModel().get("sessions")).isEmpty());
    }

    @Test
    public void should_display_sessions_without_attempts() {
        when(loginAttemptsService.get()).thenReturn(new HashMap());

        ModelAndView result = controller.sessions(new ExtendedModelMap());
        Assert.assertTrue(((Map) result.getModel().get("attempts")).isEmpty());
    }

    @Test
    public void should_display_sessions_with_principal() {
        CurrentUser currentUser = mock(CurrentUser.class);
        List<SessionInformation> list = mock(List.class);
        when(sessionRegistry.getAllSessions(eq(currentUser), eq(false))).thenReturn(list);
        when(sessionRegistry.getAllPrincipals()).thenReturn(Lists.newArrayList(currentUser));

        ModelAndView result = controller.sessions(new ExtendedModelMap());
        Assert.assertEquals(list, ((Map) result.getModel().get("sessions")).get(currentUser));

        verify(sessionRegistry, times(1)).getAllPrincipals();
    }

    @Test
    public void should_display_sessions_with_attempt() {
        Map<String, Map<String, Integer>> map = new HashMap();
        Map<String, Integer> nestedMap = new HashMap<>();
        nestedMap.put("nested", 1);
        map.put("test", nestedMap);

        when(loginAttemptsService.get()).thenReturn(map);

        ModelAndView result = controller.sessions(new ExtendedModelMap());
        Assert.assertEquals(map, ((Map) result.getModel().get("attempts")));

        verify(loginAttemptsService, times(1)).get();
    }

    @Test
    public void should_invalidate_session() throws Exception {
        SessionInformation sessionInformation = mock(SessionInformation.class);
        when(sessionRegistry.getSessionInformation("id")).thenReturn(sessionInformation);

        controller.sessions("id");

        verify(sessionRegistry, times(1)).getSessionInformation(any());
        verify(sessionInformation, times(1)).expireNow();
    }

    @Test
    public void should_not_invalidate_session_without_session_information() throws Exception {
        SessionInformation sessionInformation = mock(SessionInformation.class);
        when(sessionRegistry.getSessionInformation("id")).thenReturn(null);

        controller.sessions("id");

        verify(sessionRegistry, times(1)).getSessionInformation(any());
        verify(sessionInformation, times(0)).expireNow();
    }
}


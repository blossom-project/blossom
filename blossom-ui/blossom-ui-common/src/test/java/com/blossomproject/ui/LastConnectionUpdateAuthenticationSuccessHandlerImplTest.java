package com.blossomproject.ui;

import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.current_user.CurrentUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LastConnectionUpdateAuthenticationSuccessHandlerImplTest {
    @Mock
    UserService userService;

    private LastConnectionUpdateAuthenticationSuccessHandlerImpl handler;

    @Before
    public void setUp() {
        handler = new LastConnectionUpdateAuthenticationSuccessHandlerImpl(userService);
    }

    @Test
    public void should_update_last_connection() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);


        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setIdentifier("id");
        userDTO.setPasswordHash("pass");

        CurrentUser currentUser = new CurrentUser(userDTO);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(authentication, times(1)).getPrincipal();
        verify(userService, times(1)).updateLastConnection(eq(userDTO.getId()), any(Date.class));
        verifyNoMoreInteractions(userService);
    }
}

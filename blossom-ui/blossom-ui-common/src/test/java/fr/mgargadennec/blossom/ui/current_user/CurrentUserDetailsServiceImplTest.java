package fr.mgargadennec.blossom.ui.current_user;

import com.google.common.collect.Sets;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserDetailsServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    @Spy
    private CurrentUserDetailsServiceImpl currentUserDetailsService;

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_should_throw_exception_when_no_user_found() {

        // ARRANGE
        String identifier = "USER_18918";
        Mockito.doReturn(Optional.empty()).when(this.userService).getByIdentifier(identifier);

        // ACT
        this.currentUserDetailsService.loadUserByUsername(identifier);
    }

    @Test
    public void loadUserByUsername_should_return_user() {

        // ARRANGE
        String identifier = "USER_18918";

        UserDTO user = new UserDTOBuilder().identifier(identifier).passwordHash("PASSWORD_HASH").toUserDTO();
        Mockito.doReturn(Optional.of(user)).when(this.userService).getByIdentifier(identifier);

        // ACT
        CurrentUser result = this.currentUserDetailsService.loadUserByUsername(identifier);

        // ARRANGE
        Assert.assertEquals(user, result.getUser());

    }

    @Test
    public void testTEst() {

        Set<Character> testSet = Sets.newHashSet('a', 'b');

        String character = "b";
        Character charchar = 'b';
        Assert.assertTrue(testSet.contains(character));

        character = "bc";
        Assert.assertFalse(testSet.contains(character));
    }

}
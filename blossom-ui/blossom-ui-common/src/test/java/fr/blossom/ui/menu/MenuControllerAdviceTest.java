package fr.blossom.ui.menu;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class MenuControllerAdviceTest {

  @Test
  public void should_return_menu() throws Exception {
    Menu menu = mock(Menu.class);

    Menu getMenu  = new MenuControllerAdvice(menu).getMenu();

    assertEquals(menu,getMenu);
  }
}

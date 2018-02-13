package com.blossomproject.ui.menu;

import com.blossomproject.ui.current_user.CurrentUser;
import java.util.Collection;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public interface Menu {

  Collection<MenuItem> items();

  Collection<MenuItem> filteredItems(CurrentUser currentUser);

}

package fr.mgargadennec.blossom.ui.menu;

import java.util.Collection;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public interface MenuItem extends MenuItemPlugin {

  String key();

  String icon();

  String label();

  boolean i18n();

  String link();

  int level();

  MenuItem parent();

  Collection<MenuItem> items();

}

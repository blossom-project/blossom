package com.blossomproject.ui.theme;

public interface IScss {

  IScssVariables variables();

  IScss additionnalScss(String additionnalScss);

  String additionnalScss();

  IBodyClass done();

}

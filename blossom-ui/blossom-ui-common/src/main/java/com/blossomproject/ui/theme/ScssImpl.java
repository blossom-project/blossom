package com.blossomproject.ui.theme;

public class ScssImpl implements IScss {

  private final IBodyClass parent;
  private final IScssVariables variables;
  private String additionnalScss;


  public ScssImpl(IBodyClass parent) {
    this.parent = parent;
    this.variables = new ScssVariablesImpl(this);
    this.additionnalScss = "";
  }

  @Override
  public IScssVariables variables() {
    return this.variables;
  }

  @Override
  public IScss additionnalScss(String additionnalScss) {
    this.additionnalScss = additionnalScss;
    return this;
  }

  @Override
  public String additionnalScss() {
    return additionnalScss;
  }


  @Override
  public IBodyClass done() {
    return parent;
  }
}

package com.blossomproject.ui.theme;

public class ScssVariablesImpl implements IScssVariables {

  private final IScss parent;
  private String _primary = "#1ab394";
  private String _default = "#c2c2c2";
  private String _success = "#1c84c6";
  private String _info = "#23c6c8";
  private String _warning = "#f8ac59";
  private String _danger = "#ED5565";
  private String _text = "#676a6c";
  private String _gray = "#f3f3f4";
  private String _lightGray = "#D1DADE";
  private String _labelBadgeColor = "#5E5E5E";
  private String _spinColor = "$navy";
  private String _spinMargin = "0 auto";
  private String _borderColor = "#e7eaec";
  private String _iboxTitleBg = "#ffffff";
  private String _iboxContentBg = " #ffffff";
  private String _sidebarWidth = "220px";
  private String _boxedWidth = "1200px";
  private String _boxedBackground = "url('/blossom/public/css/patterns/shattered.png')";
  private String _btnBorderRadius = "3px";
  private String _navBg = "#2F4050";
  private String _navProfilePattern = "url('/blossom/public/css/patterns/header-profile.png')";
  private String _navTextColor = "#a7b1c2";
  private String _navHeaderBg="url('/blossom/public/css/patterns/4.png') no-repeat";;

  public ScssVariablesImpl(IScss parent) {
    this.parent = parent;
  }

  @Override
  public IScssVariables _primary(String _primary) {
    this._primary = _primary;
    return this;
  }

  @Override
  public IScssVariables _success(String _success) {
    this._success = _success;
    return this;
  }
  @Override
  public IScssVariables _default(String _default) {
    this._default = _default;
    return this;
  }

  @Override
  public IScssVariables _info(String _info) {
    this._info = _info;
    return this;
  }

  @Override
  public IScssVariables _warning(String _warning) {
    this._warning = _warning;
    return this;
  }

  @Override
  public IScssVariables _danger(String _danger) {
    this._danger = _danger;
    return this;
  }

  @Override
  public IScssVariables _text(String _text) {
    this._text = _text;
    return this;
  }

  @Override
  public IScssVariables _gray(String _gray) {
    this._gray = _gray;
    return this;
  }

  @Override
  public IScssVariables _lightGray(String _lightGray) {
    this._lightGray = _lightGray;
    return this;
  }

  @Override
  public IScssVariables _labelBadgeColor(String _labelBadgeColor) {
    this._labelBadgeColor = _labelBadgeColor;
    return this;
  }

  @Override
  public IScssVariables _spinColor(String _spinColor) {
    this._spinColor = _spinColor;
    return this;
  }

  @Override
  public IScssVariables _spinMargin(String _spinMargin) {
    this._spinMargin = _spinMargin;
    return this;
  }

  @Override
  public IScssVariables _borderColor(String _borderColor) {
    this._borderColor = _borderColor;
    return this;
  }

  @Override
  public IScssVariables _iboxTitleBg(String _iboxTitleBg) {
    this._iboxTitleBg = _iboxTitleBg;
    return this;
  }

  @Override
  public IScssVariables _iboxContentBg(String _iboxContentBg) {
    this._iboxContentBg = _iboxContentBg;
    return this;
  }

  @Override
  public IScssVariables _sidebarWidth(String _sidebarWidth) {
    this._sidebarWidth = _sidebarWidth;
    return this;
  }

  @Override
  public IScssVariables _boxedWidth(String _boxedWidth) {
    this._boxedWidth = _boxedWidth;
    return this;
  }

  @Override
  public IScssVariables _boxedBackground(String _boxedBackground) {
    this._boxedBackground = _boxedBackground;
    return this;
  }

  @Override
  public IScssVariables _btnBorderRadius(String _btnBorderRadius) {
    this._btnBorderRadius = _btnBorderRadius;
    return this;
  }

  @Override
  public IScssVariables _navBg(String _navBg) {
    this._navBg = _navBg;
    return this;
  }

  @Override
  public IScssVariables _navHeaderBg(String _navHeaderBg) {
    this._navHeaderBg = _navHeaderBg;
    return this;
  }

  @Override
  public IScssVariables _navProfilePattern(String _navProfilePattern) {
    this._navProfilePattern = _navProfilePattern;
    return this;
  }

  @Override
  public IScssVariables _navTextColor(String _navTextColor) {
    this._navTextColor = _navTextColor;
    return this;
  }

  @Override
  public String _primary() {
    return _primary;
  }

  @Override
  public String _success() {
    return _success;
  }

  @Override
  public String _default() {
    return _default;
  }

  @Override
  public String _info() {
    return _info;
  }

  @Override
  public String _warning() {
    return _warning;
  }

  @Override
  public String _danger() {
    return _danger;
  }

  @Override
  public String _text() {
    return _text;
  }

  @Override
  public String _gray() {
    return _gray;
  }

  @Override
  public String _lightGray() {
    return _lightGray;
  }

  @Override
  public String _labelBadgeColor() {
    return _labelBadgeColor;
  }

  @Override
  public String _spinColor() {
    return _spinColor;
  }

  @Override
  public String _spinMargin() {
    return _spinMargin;
  }

  @Override
  public String _borderColor() {
    return _borderColor;
  }

  @Override
  public String _iboxTitleBg() {
    return _iboxTitleBg;
  }

  @Override
  public String _iboxContentBg() {
    return _iboxContentBg;
  }

  @Override
  public String _sidebarWidth() {
    return _sidebarWidth;
  }

  @Override
  public String _boxedWidth() {
    return _boxedWidth;
  }

  @Override
  public String _boxedBackground() {
    return _boxedBackground;
  }

  @Override
  public String _btnBorderRadius() {
    return _btnBorderRadius;
  }

  @Override
  public String _navBg() {
    return _navBg;
  }

  @Override
  public String _navProfilePattern() {
    return _navProfilePattern;
  }

  @Override
  public String _navTextColor() {
    return _navTextColor;
  }

  @Override
  public String _navHeaderBg(){ return _navHeaderBg;}

  @Override
  public IScss done() {
    return parent;
  }
}

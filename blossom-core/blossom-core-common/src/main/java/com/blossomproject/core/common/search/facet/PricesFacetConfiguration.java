package com.blossomproject.core.common.search.facet;

public class PricesFacetConfiguration extends MinMaxFacetConfiguration {
  private String currency;

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PricesFacetConfiguration that = (PricesFacetConfiguration) o;

    return currency != null ? currency.equals(that.currency) : that.currency == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (currency != null ? currency.hashCode() : 0);
    return result;
  }
}

package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 06.11.2016.
 */
public class FloatObject {
  private Float value;

  public FloatObject() {
  }

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by lars on 27.10.2016.
 */

public class Bounds1D {
  private float b1;
  private float b2;

  public Bounds1D(float b1, float b2) {
    this.b1 = b1;
    this.b2 = b2;
  }

  public float getB1() {
    return b1;
  }

  public float getB2() {
    return b2;
  }

  public float getCenter() {
    return Math.abs(b1 + b2) / 2f;
  }

  public float getSweepAngle() {
    return Math.abs(b2 - b1);
  }

  public boolean isFull() {
    return getSweepAngle() >= 360f;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
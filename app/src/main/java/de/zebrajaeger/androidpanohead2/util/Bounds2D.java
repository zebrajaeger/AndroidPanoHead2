package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 23.10.2016.
 */
public class Bounds2D {
  private Bounds1D x;
  private Bounds1D y;

  public Bounds2D(float x1, float x2, float y1, float y2) {
    this.x = new Bounds1D(x1, x2);
    this.y = new Bounds1D(y1, y2);
  }

  public Bounds1D getX() {
    return x;
  }

  public Bounds1D getY() {
    return y;
  }

  public boolean isFull() {
    return x.isFull() && y.isFull();
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by lars on 23.10.2016.
 */

public class Fov2D {
  private Fov1D x;
  private Fov1D y;

  public Fov2D() {
  }

  public Fov2D(float x, float y) {
    this.x = new Fov1D(x);
    this.y = new Fov1D(y);
  }

  public Fov2D(float x, float overlapX, float y, float overlapY) {
    this.x = new Fov1D(x,overlapX);
    this.y = new Fov1D(y,overlapY);
  }

  public Fov1D getX() {
    return x;
  }

  public Fov1D getY() {
    return y;
  }

  public void setX(Fov1D x) {
    this.x = x;
  }

  public void setY(Fov1D y) {
    this.y = y;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}

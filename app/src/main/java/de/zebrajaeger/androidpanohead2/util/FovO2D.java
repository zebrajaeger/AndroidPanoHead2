package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by lars on 23.10.2016.
 */
public class FovO2D implements Serializable {
  private FovO1D x;
  private FovO1D y;

  public FovO2D() {
  }
/*
  public FovO2D(float x, float y) {
    this.x = new FovO1D(x);
    this.y = new FovO1D(y);
  }

  public FovO2D(float x, float overlapX, float y, float overlapY) {
    this.x = new FovO1D(x, overlapX);
    this.y = new FovO1D(y, overlapY);
  }

  public FovO2D(Fov1D x, Fov1D y) {
    this.x = new FovO1D(x);
    this.y = new FovO1D(y);
  }
  */
  public FovO2D(Fov1D x, Fov1D y, Overlap overlap) {
    this.x = new FovO1D(x, overlap.getX());
    this.y = new FovO1D(y, overlap.getY());
  }

  public FovO1D getX() {
    return x;
  }

  public FovO1D getY() {
    return y;
  }

  public void setX(FovO1D x) {
    this.x = x;
  }

  public void setY(FovO1D y) {
    this.y = y;
  }

  public boolean hasX() {
    return x != null;
  }

  public boolean hasY() {
    return y != null;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}

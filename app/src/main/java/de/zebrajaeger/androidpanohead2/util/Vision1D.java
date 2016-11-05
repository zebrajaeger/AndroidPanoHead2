package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 30.10.2016.
 */
public class Vision1D {
  private Float angle;
  private Float fov;

  public Vision1D() {
  }

  public Vision1D(Float angle, Float fov) {
    this.angle = angle;
    this.fov = fov;
  }

  public Float getAngle() {
    return angle;
  }

  public void setAngle(Float angle) {
    this.angle = angle;
  }

  public Float getFov() {
    return fov;
  }

  public void setFov(Float fov) {
    this.fov = fov;
  }

  public boolean isComplete() {
    return angle != null && fov != null;
  }

  public Float getA1(){
    return (isComplete()) ? angle - (fov/2f) : null;
  }
  public Float getA2(){
    return (isComplete()) ? angle + (fov/2f) : null;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by lars on 27.10.2016.
 */

public class Fov1D {
  private float sweepAngle;
  private float overlap;

  public Fov1D(float sweepAngle, float overlap) {
    this.sweepAngle = sweepAngle;
    this.overlap = overlap;
  }

  public Fov1D(float sweepAngle) {
    this.sweepAngle = sweepAngle;
    this.overlap = 0f;
  }

  public float getSweepAngle() {
    return sweepAngle;
  }

  public float getOverlap() {
    return overlap;
  }

  public float getOverlapingAngle() {
    return sweepAngle * overlap;
  }

  public float getNonOverlapingAngle() {
    return sweepAngle * (1f - overlap);
  }

  public void setSweepAngle(float sweepAngle) {
    this.sweepAngle = sweepAngle;
  }

  public void setOverlap(float overlap) {
    this.overlap = overlap;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}

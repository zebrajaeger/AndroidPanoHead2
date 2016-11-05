package de.zebrajaeger.androidpanohead2.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lars on 16.10.2016.
 */
@Deprecated
public class AngleRange {
  private static final Logger LOG = LoggerFactory.getLogger(AngleRange.class);

  private Float a1;
  private Float a2;

  public AngleRange(Float a1, Float a2) {
    this.a1 = a1;
    this.a2 = a2;
  }

  public boolean isComplete() {
    return a1 != null && a2 != null;
  }

  public Float getA1() {
    return a1;
  }

  public Float getA2() {
    return a2;
  }

/*
  public Float getLowerAngle(){
      if (isComplete()) {
        return Math.min(a1,a2);
      } else {
        String msg = "getLowerAngle needs a complete range";
        throw new IllegalStateException(msg);
      }
  }
*/


  public int getA1AsInt() {
    if(a1!=null){
      float f = a1;
      return (int) f;
    }else{
      String msg = "a1 is null and cannot returned as int";
      throw new IllegalStateException(msg);
    }
  }

  public int getA2AsInt() {
    if(a2!=null){
      float f = a2;
      return (int) f;
    }else{
      String msg = "a2 is null and cannot returned as int";
      throw new IllegalStateException(msg);
    }
  }

  public Float getAngle() {
    return isComplete() ? (a1 + a2) / 2f : null;
  }

  public Float getFov() {
    return isComplete() ? a2 - a1 : null;
  }

  public void setA1(Float a1) {
    this.a1 = a1;
  }

  public void setA2(Float a2) {
    this.a2 = a2;
  }

  public void setAngle(float angle) {
    if (isComplete()) {
      Float diff = angle - getAngle();
      this.a1 += diff;
      this.a2 += diff;
    } else {
      String msg = "set angle in incomplete AngleRange";
      LOG.warn(msg, new IllegalStateException(msg));
    }
  }

  public void setFov(float fov) {
    if (isComplete()) {
      float fov2 = fov / 2f;
      this.a1 -= fov2;
      this.a2 += fov2;
    } else {
      String msg = "set angle in incomplete AngleRange";
      LOG.warn(msg, new IllegalStateException(msg));
    }
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }


  public static AngleRange newEmpty() {
    return new AngleRange(null, null);
  }

  public static AngleRange ofTwoAngles(Float a1, Float a2) {
    return new AngleRange(a1, a2);
  }

  public static AngleRange ofAngleAndFov(float angle, float fov) {
    float fov2 = fov/2f;
    return new AngleRange(angle - fov2, angle + fov2);
  }
}

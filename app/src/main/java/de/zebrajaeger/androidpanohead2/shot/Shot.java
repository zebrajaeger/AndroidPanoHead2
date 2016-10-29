package de.zebrajaeger.androidpanohead2.shot;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 08.10.2016.
 */
public class Shot {
  private float x;
  private float y;

  public Shot(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

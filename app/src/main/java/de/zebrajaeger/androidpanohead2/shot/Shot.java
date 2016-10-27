package de.zebrajaeger.androidpanohead2.shot;

import de.zebrajaeger.androidpanohead2.data.ICamAction;

/**
 * @author lars on 08.10.2016.
 */
public class Shot implements ICamAction {
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
    return "Shot{"
        + "x=" + x
        + ", y=" + y
        + '}';
  }
}

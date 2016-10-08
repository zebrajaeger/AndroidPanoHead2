package de.zebrajaeger.androidpanohead2.shot;

import de.zebrajaeger.androidpanohead2.data.ICamAction;

/**
 * Created by lars on 08.10.2016.
 */

public class Shot implements ICamAction {
  private double x;
  private double y;

  public Shot(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public String toString() {
    return "Shot{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }
}

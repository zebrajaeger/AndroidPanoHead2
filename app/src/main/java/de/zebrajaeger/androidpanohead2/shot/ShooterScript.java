package de.zebrajaeger.androidpanohead2.shot;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author lars on 08.10.2016.
 */
public class ShooterScript implements Serializable{
  private CalculatorData calculationData;
  private List<Shot> shots;

  public ShooterScript(CalculatorData calculationData, List<Shot> shots) {
    this.calculationData = calculationData;
    this.shots = shots;
  }

  public CalculatorData getCalculationData() {
    return calculationData;
  }

  public List<Shot> getShots() {
    return shots;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
  /*
  private final float camFovX;
  private final float camFovY;
  private Bounds2D bounds2D;

  public ShooterScript(List<Shot> shots, float camFovX, float camFovY) {
    this.shots = shots;
    this.camFovX = camFovX;
    this.camFovY = camFovY;
  }

  protected Bounds2D getBounds2D() {
    if (bounds2D == null) {
      float boundX1 = Float.MAX_VALUE;
      float boundX2 = Float.MIN_VALUE;
      float boundY1 = Float.MAX_VALUE;
      float boundY2 = Float.MIN_VALUE;

      float hx = camFovX / 2f;
      float hy = camFovY / 2f;
      for (Shot shot : shots) {
        boundX1 = Math.min(boundX1, shot.getX() - hx);
        boundX2 = Math.max(boundX2, shot.getX() + hx);
        boundY1 = Math.min(boundY1, shot.getY() - hy);
        boundY2 = Math.min(boundY2, shot.getY() + hy);
      }
      bounds2D = new Bounds2D(boundX1, boundX2, boundY1, boundY2);
    }
    return bounds2D;
  }

  @Override
  public String toString() {
    return "ShooterScript{" +
        "shots.size=" + shots.size()
        + ", camFovX=" + camFovX
        + ", camFovY=" + camFovY
        + ", bounds2D=" + bounds2D
        + '}';
  }
  */
}

package de.zebrajaeger.androidpanohead2.shot;

import de.zebrajaeger.androidpanohead2.util.Bounds2D;
import de.zebrajaeger.androidpanohead2.util.Fov2D;
import de.zebrajaeger.androidpanohead2.util.FovO2D;
import de.zebrajaeger.androidpanohead2.util.Overlap;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author lars on 23.10.2016.
 */
public class CalculatorData implements Serializable {
  private FovO2D camFov;
  private Bounds2D panoBounds;

  public CalculatorData(Fov2D camFov, Overlap overlap, Bounds2D panoBounds) {
    this.camFov = new FovO2D(camFov, overlap);
    this.panoBounds = panoBounds;
  }

  public FovO2D getCamFov() {
    return camFov;
  }

  public Bounds2D getPanoBounds() {
    return panoBounds;
  }

  public boolean hasX() {
    return camFov != null && camFov.hasX() && panoBounds != null && panoBounds.hasX();
  }

  public boolean hasY() {
    return camFov != null && camFov.hasY() && panoBounds != null && panoBounds.hasY();
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

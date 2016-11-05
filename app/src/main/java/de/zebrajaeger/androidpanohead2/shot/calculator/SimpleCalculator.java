package de.zebrajaeger.androidpanohead2.shot.calculator;

import de.zebrajaeger.androidpanohead2.shot.CalculatorData;
import de.zebrajaeger.androidpanohead2.shot.ShooterScript;
import de.zebrajaeger.androidpanohead2.shot.Shot;
import de.zebrajaeger.androidpanohead2.shot.ShotCalculator;
import de.zebrajaeger.androidpanohead2.util.Bounds1D;
import de.zebrajaeger.androidpanohead2.util.Fov1D;

import java.util.LinkedList;
import java.util.List;

/**
 * @author lars on 23.10.2016.
 */
public class SimpleCalculator implements ShotCalculator {
  @Override
  public ShooterScript createScript(CalculatorData data) {
    boolean hasX = data.hasX();
    boolean hasY = data.hasY();
    float[] xValues = hasX
        ? createValues(data.getCamFov().getX(), data.getPanoBounds().getX())
        : null;
    float[] yValues = data.hasY()
        ? createValues(data.getCamFov().getY(), data.getPanoBounds().getY())
        : null;

    List<Shot> result = new LinkedList<>();
    if (hasX && hasY) {
      for (float y : yValues) {
        for (float x : xValues) {
          result.add(new Shot(x, y));
        }
      }
    } else if (hasX) {
      for (float x : xValues) {
        result.add(new Shot(x, 0f));
      }
    } else if (hasY) {
      for (float y : yValues) {
        result.add(new Shot(y, 0f));
      }
    }

    return new ShooterScript(data, result);
  }

  private float[] createValues(Fov1D camFov, Bounds1D panoRange) {
    return panoRange.isFull()
        ? createValuesFull(camFov, panoRange)
        : createValuesPartial(camFov, panoRange);
  }

  private float[] createValuesFull(Fov1D camFov, Bounds1D panoRange) {
    float[] result;
    // Full circle pano. Set range to 360 to prevent a range > 360 degree
    float imgCount = (float) Math.floor(360f / camFov.getNonOverlapingAngle());
    int n = (int) imgCount;
    result = new float[n];

    if (imgCount == 1) {
      // Single Image -> take center of pano
      // Should be not occur on a full circle
      result[0] = panoRange.getCenter();
    } else {
      // Multible images -> start at zero
      for (int i = 0; i < n; ++i) {
        result[i] = (float) i * 360f / imgCount;
      }
    }
    return result;
  }

  private float[] createValuesPartial(Fov1D camFov, Bounds1D panoRange) {
    float[] result;

    // Partial pano
    // One overlapping area fewer than full round. We remove it from range for a more easy calculation
    float range = panoRange.getSweepAngle() - camFov.getOverlapingAngle();
    float imgCount = (float) Math.ceil(range / camFov.getNonOverlapingAngle());
    int n = (int) imgCount;
    result = new float[n];

    if (imgCount == 1) {
      // Single Image -> take center of pano
      result[0] = panoRange.getCenter();
    } else {
      // Multible images -> start and end at half a image
      float halfImage = camFov.getSweepAngle() / 2f;
      result[0] = panoRange.getB1() + halfImage;
      result[result.length - 1] = panoRange.getB2() - halfImage;
      float remainingAngle = result[result.length - 1] - result[0];
      float distance = remainingAngle / (float) (n - 1);
      for (int i = 1; i < (n - 1); ++i) {
        result[i] = result[i - 1] + distance;
      }
    }
    return result;
  }

}

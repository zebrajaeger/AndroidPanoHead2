package de.zebrajaeger.androidpanohead2.shot;

import de.zebrajaeger.androidpanohead2.data.ShooterData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lars on 08.10.2016.
 */
public class ShotCalculator {
  private ShooterData shooterData;

  public ShotCalculator(ShooterData shooterData) {
    this.shooterData = shooterData;
  }

  protected int computePictureCount(double panoSize, double imgSize, double imageOverlapSize) {
    // Partial
    double x = panoSize * (1 + imageOverlapSize);
    if (x < 360.0) {
      // partial panos have one less area
      x += (panoSize * imageOverlapSize);
    }
    return (int) Math.ceil(x / imgSize);
  }

  public List<Shot> createRowShots(double y) {
    double[] positions = computePositions(
        shooterData.getPanoWidthStartAngle(),
        shooterData.getPanoWidth(),
        shooterData.getImgWidth(),
        shooterData.getOverlapWidth());
    List<Shot> shots = new ArrayList<>(positions.length);
    for (double pos : positions) {
      shots.add(new Shot(pos, y));
    }
    return shots;
  }

  public List<Shot> createColumnShots(double x) {
    double[] positions = computePositions(
        shooterData.getPanoHeightStartAngle(),
        shooterData.getPanoHeight(),
        shooterData.getImgHeigth(),
        shooterData.getOverlapHeight());

    List<Shot> shots = new ArrayList<>(positions.length);
    for (double pos : positions) {
      shots.add(new Shot(x, pos));
    }
    return shots;
  }

  public List<Shot> createSimpleGridShots() {
    double[] positionsX = computePositions(
        shooterData.getPanoWidthStartAngle(),
        shooterData.getPanoWidth(),
        shooterData.getImgWidth(),
        shooterData.getOverlapWidth());

    double[] positionsY = computePositions(
        shooterData.getPanoHeightStartAngle(),
        shooterData.getPanoHeight(),
        shooterData.getImgHeigth(),
        shooterData.getOverlapHeight());
    List<Shot> shots = new ArrayList<>(positionsX.length * positionsY.length);
    for (int iY = 0; iY < positionsY.length; ++iY) {
      for (int iX = 0; iX < positionsX.length; ++iX) {
        shots.add(new Shot(positionsX[iX], positionsY[iY]));
      }
    }
    return shots;
  }

  private double[] computePositions(double offset, double panoSize, double imageSize, double overlap) {
    Integer count = computePictureCount(panoSize, imageSize, overlap);
    double[] result = new double[count];
    double period = (panoSize - imageSize) / (count - 1);
    for (int i = 0; i < count; ++i) {
      result[i] = offset + (imageSize / 2.0d) + ((double) i * period);
    }
    return result;
  }

  public ShooterScript createShooterScript() {
    boolean isRow = (shooterData.getImgWidth() != null) && (shooterData.getPanoWidth() != null) && (shooterData.getOverlapWidth() != null);
    boolean isColumn = (shooterData.getImgHeigth() != null) && (shooterData.getPanoHeight() != null) && (shooterData.getOverlapHeight() != null);

    if (!isRow && !isColumn) {
      // nothing
      return null;

    } else if (isRow && !isColumn) {
      // row
      return new ShooterScript(createRowShots(0.0d));

    } else if (isRow && !isColumn) {
      // column
      return new ShooterScript(createColumnShots(0.0d));

    } else {
      // grid
      return new ShooterScript(createSimpleGridShots());
    }
  }
}

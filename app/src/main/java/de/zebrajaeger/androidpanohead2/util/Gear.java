package de.zebrajaeger.androidpanohead2.util;

/**
 * Created by lars on 02.10.2016.
 */

public class Gear {
  private static final Gear INSTANCE = new Gear();
  public static Gear instance(){
    return INSTANCE;
  }

  private double gearRatio = 19.0d + (38.0d / 187.0d);
  private double valPerDegree = gearRatio / 360.0d;

  public double headValueToDegree(double deg) {
    return deg / valPerDegree;
  }

  public double degreeToHeadValue(double val) {
    return val * valPerDegree;
  }

}

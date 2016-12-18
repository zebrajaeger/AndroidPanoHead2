package de.zebrajaeger.androidpanohead2.shot.shooter;

/**
 * @author lars on 18.12.2016.
 */
public interface IShooterHead {
  void moveTo(float w, float h) throws InterruptedException;
  void setCam(boolean focus, boolean trigger)  throws InterruptedException;
}

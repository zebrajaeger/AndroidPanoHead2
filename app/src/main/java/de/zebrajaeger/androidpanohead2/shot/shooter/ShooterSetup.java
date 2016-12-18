package de.zebrajaeger.androidpanohead2.shot.shooter;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 17.12.2016.
 */
public class ShooterSetup {
  private int pauseBeforeShot;
  private int shotTime;
  private int focusTime;
  private int pauseAfterShot;

  public ShooterSetup(int pauseBeforeShot, int shotTime, int focusTime, int pauseAfterShot) {
    this.pauseBeforeShot = pauseBeforeShot;
    this.shotTime = shotTime;
    this.focusTime = focusTime;
    this.pauseAfterShot = pauseAfterShot;
  }

  public int getPauseAfterMoveShot() {
    return pauseBeforeShot;
  }

  public int getTriggerTime() {
    return shotTime;
  }

  public int getFocusTime() {
    return focusTime;
  }

  public int getPauseAfterShot() {
    return pauseAfterShot;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

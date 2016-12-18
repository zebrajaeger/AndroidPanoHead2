package de.zebrajaeger.androidpanohead2.shot.shooter;

import de.zebrajaeger.androidpanohead2.shot.ShotPosition;

/**
 * @author lars on 18.12.2016.
 */
public interface IShooterStateListener {
  void onShootStateChanged(ShooterRobot.ShotState from, ShooterRobot.ShotState to);
  void onRobotStateChanged(ShooterRobot.ThreadState from, ShooterRobot.ThreadState to);
  void onRobotImageChanged(int index, ShotPosition pos);
}

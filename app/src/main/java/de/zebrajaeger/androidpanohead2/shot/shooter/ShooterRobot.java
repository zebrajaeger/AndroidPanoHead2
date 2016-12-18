package de.zebrajaeger.androidpanohead2.shot.shooter;

import de.zebrajaeger.androidpanohead2.shot.ShotPosition;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Shooter thread.
 *
 * @author lars on 17.12.2016.
 */
public class ShooterRobot implements Runnable {
  private ThreadState threadState = ThreadState.STOPPED;
  private ShotState shotState = ShotState.UNDEFINED;
  private ShooterScript script;
  private ShooterSetup setup;
  private Thread thread;
  private int currentIndex;
  private IShooterHead shooterHead;
  private List<IShooterStateListener> listeners = new LinkedList<>();

  public ShooterRobot(IShooterHead shooterHead, ShooterScript script, ShooterSetup setup) {
    this.shooterHead = shooterHead;
    this.script = script;
    this.setup = setup;
  }

  public void addListener(IShooterStateListener l) {
    listeners.add(l);
  }

  public void start() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }

  public void stop() {
    if (thread != null) {
      synchronized (thread) {
        thread.interrupt();
      }
    }
  }

  @Override
  public void run() {
    setThreadState(ThreadState.RUNNING);
    try {
      for (currentIndex = 0; currentIndex < script.getShots().size(); currentIndex++) {
        // prepare pos
        ShotPosition shotPosition = script.getShots().get(currentIndex);
        for(IShooterStateListener l : listeners ){
          l.onRobotImageChanged(currentIndex, shotPosition);
        }

        // move to pos
        checkPause();
        setShotState(ShotState.MOVING);
        shooterHead.moveTo(shotPosition.getX(), shotPosition.getY());

        // wait after move
        checkPause();
        setShotState(ShotState.WAIT_AFTER_MOVE);
        Thread.sleep(setup.getPauseAfterMoveShot());

        // focus
        checkPause();
        if (setup.getFocusTime() > 0) {
          setShotState(ShotState.FOCUSSING);
          shooterHead.setCam(true, false);
          Thread.sleep(setup.getFocusTime());
        }

        // trigger
        setShotState(ShotState.TRIGGERING);
        shooterHead.setCam(true, true);
        Thread.sleep(setup.getTriggerTime());
        shooterHead.setCam(false, false);

        // pause after trigger
        checkPause();
        setShotState(ShotState.WAIT_AFTER_SHOT);
        Thread.sleep(setup.getPauseAfterShot());
      }
    } catch (InterruptedException ignore) {
    } finally {
      setThreadState(ThreadState.STOPPED);
      setShotState(ShotState.UNDEFINED);
    }
  }

  private void checkPause() throws InterruptedException {
    synchronized (thread) {
      if (threadState == ThreadState.MARKED_FOR_PAUSE) {
        setThreadState(ThreadState.PAUSED);
        thread.wait();
        setThreadState(ThreadState.RUNNING);
      }
    }
  }

  public void pauseOrResume() {
    if (thread != null) {
      synchronized (thread) {
        if (threadState == ThreadState.RUNNING) {
          setThreadState(ThreadState.MARKED_FOR_PAUSE);
        } else if (threadState == ThreadState.MARKED_FOR_PAUSE) {
          setThreadState(ThreadState.RUNNING);
        } else if (threadState == ThreadState.PAUSED) {
          thread.notify();
        }
      }
    }
  }

  private void setShotState(ShotState shotState) {
    ShotState prev = this.shotState;
    this.shotState = shotState;
    for (IShooterStateListener l : listeners) {
      l.onShootStateChanged(prev, shotState);
    }
  }

  private void setThreadState(ThreadState threadState) {
    ThreadState prev = this.threadState;
    this.threadState = threadState;

    for (IShooterStateListener l : listeners) {
      l.onRobotStateChanged(prev, threadState);
    }
  }

  public enum ShotState {
    UNDEFINED, MOVING, WAIT_AFTER_MOVE, FOCUSSING, TRIGGERING, WAIT_AFTER_SHOT
  }

  public enum ThreadState {
    STOPPED, RUNNING, MARKED_FOR_PAUSE, PAUSED
  }
}

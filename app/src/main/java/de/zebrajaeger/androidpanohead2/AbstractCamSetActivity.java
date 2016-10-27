package de.zebrajaeger.androidpanohead2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import de.zebrajaeger.androidpanohead2.panohead.PanoHead;
import de.zebrajaeger.androidpanohead2.util.Position;
import de.zebrajaeger.jgrblconnector.event.GrblStatusEvent;
import de.zebrajaeger.jgrblconnector.event.GrblStatusListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author lars on 23.10.2016.
 */
public abstract class AbstractCamSetActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, GrblStatusListener {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractCamSetActivity.class);
  private GestureDetectorCompat mDetector;
  private Position last = null;
  private Position diff = new Position();
  private Thread grblThread = null;

  private GrblStatusEvent lastEvent = null;

  private Position targetPosition = null;

  @Override
  protected void onStart() {
    super.onStart();
    //Storage.initAndLoadSilently(getApplicationContext());
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    if (getActionBar() != null) {
      getActionBar().setDisplayShowTitleEnabled(false);
    }

    findViewById(R.id.button_left).setEnabled(false);
    findViewById(R.id.button_right).setEnabled(false);
    PanoHead.instance().addListener(this);
    if (PanoHead.instance().isConnected()) {
      LOG.debug("create panoThread to send commands");
      grblThread = createCommandThread();
    } else {
      LOG.debug("dont create panoThread because we are not connected");
    }
  }

  @Override
  protected void onStop() {
    PanoHead.instance().removeListener(this);
    if (grblThread != null) {
      grblThread.interrupt();
    }
    super.onStop();
  }

  public synchronized Position takeDiff() {
    Position result = diff;
    diff = new Position();
    return result;
  }

  public synchronized void addDiff(Position toAdd) {
    diff = diff.add(toAdd);
    if (targetPosition != null) {
      targetPosition = targetPosition.add(toAdd);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDetector = new GestureDetectorCompat(this, this);
    mDetector.setIsLongpressEnabled(false);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mDetector != null) {
      this.mDetector.onTouchEvent(event);
    }
    return super.onTouchEvent(event);
  }

  @Override
  public boolean onDown(MotionEvent event) {
    return true;
  }

  @Override
  public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
    last = null;
    return true;
  }

  @Override
  public void onLongPress(MotionEvent event) {
    LOG.debug("onLongPress: " + event.toString());
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    if (last == null) {
      last = new Position(e1.getX(), e1.getY());
    }

    Position newPos = new Position(e2.getX(), e2.getY()); // diff in pixel

    // TODO we could use the dpi fora device independent solution of iE. 1°/cm screen
    DisplayMetrics m = getResources().getDisplayMetrics();
    double multiplierForTwoFingers = 10;
    double screenSizeInDegreeX = 100;
    double screenSizeInDegreeY = screenSizeInDegreeX * (double) m.heightPixels / (double) m.widthPixels;
    Position delta = newPos.sub(last).div(m.widthPixels, m.heightPixels).mul(screenSizeInDegreeX, screenSizeInDegreeY);

    if (e2.getPointerCount() == 2) {
      delta = delta.mul(multiplierForTwoFingers);
    }
    addDiff(delta);
    AngleView av = (AngleView) findViewById(R.id.cam_view_horizontal);
    av.addToTargetAngle((float) delta.getX());

    last = newPos;

    return true;
  }

  @Override
  public void onShowPress(MotionEvent event) {
  }

  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    return true;
  }

  private Thread createCommandThread() {
    Thread result = new Thread(new Runnable() {
      @Override
      public void run() {
        for (; ; ) {

          // Wait for diff pos
          Position diff;
          while ((diff = takeDiff()).isZero()) {
            // TODO stupid solution. Better let the Thread wait and notify it to wake up.
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              LOG.info("Grbl Status Thread interrupted");
              break;
            }
          }

          // send diff
          try {
            PanoHead.instance().moveXYRelativeBlocking((float) (diff.getX() / 360d), 0f);
          } catch (IOException e) {
            LOG.error("Grbl Command Thread Exception while moveXYRelativeBlocking", e);

          } catch (InterruptedException e) {
            LOG.info("Grbl Command Thread interrupted while MoveXY-command");
            break;
          }
/*
                // wait for execution
                try {
                    grbl.sync();
                } catch (IOException e) {
                    LOG.error("Grbl Command Thread Exception while sync", e);

                } catch (InterruptedException e) {
                    LOG.info("Grbl Command Thread interrupted while sync");
                    break;
                }
                */
        }
      }
    });
    result.start();
    return result;
  }

  private void enableView(final View view, boolean enable) {
    if (enable) {
      if (!view.isEnabled()) {
        view.post(new Runnable() {
          @Override
          public void run() {
            view.setEnabled(true);
          }
        });
      }
    } else {
      if (view.isEnabled()) {
        view.post(new Runnable() {
          @Override
          public void run() {
            view.setEnabled(false);
          }
        });
      }
    }
  }

  public GrblStatusEvent getLastEvent() {
    return lastEvent;
  }

  @Override
  public void grblStatus(GrblStatusEvent e) {
    if (!e.equals(lastEvent)) {
      lastEvent = e;
    }

    AngleView av = (AngleView) findViewById(R.id.cam_view_horizontal);
    av.setCamAngle(e.getMpos().getX() * 360f);
    if (GrblStatusEvent.Status.Idle.equals(e.getStatus()) && av.getTargetAngle() == null) {
      av.setTargetAngle(e.getMpos().getX() * 360f);
    } else if (!GrblStatusEvent.Status.Idle.equals(lastEvent.getStatus()) && GrblStatusEvent.Status.Idle.equals(e.getStatus())) {
      av.setTargetAngle(e.getMpos().getX() * 360f);
    }

    View bl = findViewById(R.id.button_left);
    View br = findViewById(R.id.button_right);
    if (e.getStatus() == GrblStatusEvent.Status.Idle) {
      enableView(bl, true);
      enableView(br, true);
    } else {
      enableView(bl, false);
      enableView(br, false);
    }
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent e) {
    return false;
  }

  @Override
  public boolean onDoubleTap(MotionEvent e) {
    showCloseDialog();
    return false;
  }

  @Override
  public boolean onDoubleTapEvent(MotionEvent e) {
    return false;
  }

  @Override
  public void onBackPressed() {
    if (canFinishWithResult()) {
      finishWithResult();
    } else {
      finishWithoutResult();
    }
  }

  protected void showCloseDialog() {
    AlertDialog.Builder adb = new AlertDialog.Builder(this);
    adb.setTitle("Take Values?");
    adb.setIcon(android.R.drawable.ic_dialog_alert);
    if (canFinishWithResult()) {
      adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          finishWithResult();
        }
      });
    }

    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        finishWithoutResult();
      }
    });
    adb.show();
  }

  protected abstract boolean canFinishWithResult();

  protected abstract void onFinish(Bundle conData);

  private void finishWithResult() {
    Bundle conData = new Bundle();

    onFinish(conData);

    Intent intent = new Intent();
    intent.putExtras(conData);
    setResult(RESULT_OK, intent);
    finish();
  }

  private void finishWithoutResult() {
    Intent intent = new Intent();
    setResult(RESULT_CANCELED, intent);
    finish();
  }
}

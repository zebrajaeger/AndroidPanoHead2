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
import de.zebrajaeger.androidpanohead2.util.Gear;
import de.zebrajaeger.androidpanohead2.util.Position;
import de.zebrajaeger.jgrblconnector.event.GrblStatusEvent;
import de.zebrajaeger.jgrblconnector.event.GrblStatusListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SetBorderActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, GrblStatusListener {
  private static final Logger LOG = LoggerFactory.getLogger(SetBorderActivity.class);
  private GestureDetectorCompat mDetector;
  private Position last = null;
  private Position diff = new Position();
  private Thread grblThread = null;

  private GrblStatusEvent lastEvent = null;
  private Double x1;
  private Double x2;

  @Override
  protected void onStart() {
    super.onStart();
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

  public void onButtonLeft(View view) {
    if (lastEvent != null) {
      x1 = (double) lastEvent.getMpos().getX();
    }
  }

  public void onButtonRight(View view) {
    if (lastEvent != null) {
      x2 = (double) lastEvent.getMpos().getX();
    }
  }

  public synchronized Position takeDiff() {
    Position result = diff;
    diff = new Position();
    return result;
  }

  public synchronized void addDiff(Position toAdd) {
    diff = diff.add(toAdd);
  }

  // Called when the activity is first created.
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_activit_set_border);
    mDetector = new GestureDetectorCompat(this, this);
    mDetector.setIsLongpressEnabled(false);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mDetector != null) {
      this.mDetector.onTouchEvent(event);
    }
    // Be sure to call the superclass implementation
    return super.onTouchEvent(event);
  }

  @Override
  public boolean onDown(MotionEvent event) {
    //LOG.debug("DOWN");
    return true;
  }

  @Override
  public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
    //LOG.debug("FLING");
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

    // TODO we could use the dpi fora device independet solution of iE. 1°/cm screen
    DisplayMetrics m = getResources().getDisplayMetrics();
    double multiplierForTwoFingers = 3;
    double screenSizeInDegreeX = 100;
    double screenSizeInDegreeY = screenSizeInDegreeX * (double) m.heightPixels / (double) m.widthPixels;
    Position delta = newPos.sub(last).div(m.widthPixels, m.heightPixels).mul(screenSizeInDegreeX, screenSizeInDegreeY);

    if (e2.getPointerCount() == 2) {
      delta = delta.mul(multiplierForTwoFingers);
    }
    //LOG.debug("ADD DIFF:" + delta);
    addDiff(delta);

    last = newPos;

    return true;
  }

  @Override
  public void onShowPress(MotionEvent event) {
    //LOG.debug("onShowPress: " + event.toString());
  }

  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    //LOG.debug("onSingleTapUp: " + event.toString());
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
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              LOG.info("Grbl Status Thread interrupted");
              break;
            }
          }

          // send diff
          try {
            Gear gear = Gear.instance();

            //PanoHead.instance().moveXYRelativeBlocking((float) gear.degreeToHeadValue(diff.getX()), (float) gear.degreeToHeadValue(diff.getY()));
            //LOG.debug("SEND DIFF: " + gear.degreeToHeadValue(diff.getX()));
            PanoHead.instance().moveXYRelativeBlocking((float) gear.degreeToHeadValue(diff.getX()), 0f);
            //PanoHead.instance().moveXYRelativeBlocking(0.01f, 0f);

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

  @Override
  public void grblStatus(GrblStatusEvent e) {
    if (!e.equals(lastEvent)) {
      //LOG.error(e.toString());
      lastEvent = e;
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
    //LOG.error("SINGLE-TAP-CONFIRMED" + e);
    return false;
  }

  @Override
  public boolean onDoubleTap(MotionEvent e) {
    //LOG.error("DOUBLE-TAP " + e);
    AlertDialog.Builder adb = new AlertDialog.Builder(this);
    //adb.setView(this.getWindow().getDecorView());
    adb.setTitle("Take Values?");
    adb.setIcon(android.R.drawable.ic_dialog_alert);
    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        finishWithResult();
      }
    });

    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        finishWithoutResult();
      }
    });
    adb.show();
    return false;
  }

  @Override
  public boolean onDoubleTapEvent(MotionEvent e) {
    //LOG.error("DOUBLE-TAP-EVENT" + e);
    return false;
  }

  private void finishWithResult() {
    Bundle conData = new Bundle();
    conData.putString("param_result", "Thanks Thanks");
    if (x1 != null) {
      conData.putDouble("x1", x1);
    }
    if (x2 != null) {
      conData.putDouble("x2", x2);
    }
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

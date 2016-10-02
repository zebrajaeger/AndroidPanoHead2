package de.zebrajaeger.androidpanohead2;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetBorderActivity extends AppCompatActivity implements
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

  //private static final String DEBUG_TAG = "Gestures";
  private static final Logger LOG = LoggerFactory.getLogger(SetBorderActivity.class);
  private GestureDetectorCompat mDetector;
  private ScaleGestureDetector scaleDetector;
  //private RotateGestureRecognizer rotateGestureRecognizer;

  // Called when the activity is first created.
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_activit_set_border);
    // Instantiate the gesture detector with the application context and an implementation of GestureDetector.OnGestureListener
    mDetector = new GestureDetectorCompat(this, this);
    // Set the gesture detector as the double tap listener.
    //mDetector.setOnDoubleTapListener(this);

    //scaleDetector = new ScaleGestureDetector(this,this);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mDetector != null) {
      this.mDetector.onTouchEvent(event);
    }
    if (scaleDetector != null) {
      scaleDetector.onTouchEvent(event);
    }
    // Be sure to call the superclass implementation
    return super.onTouchEvent(event);
  }

  @Override
  public boolean onDown(MotionEvent event) {
    LOG.debug("onDown: " + event.toString());
    return true;
  }

  @Override
  public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
    LOG.debug("onFling: " + event1.toString() + event2.toString());
    return true;
  }

  @Override
  public void onLongPress(MotionEvent event) {
    LOG.debug("onLongPress: " + event.toString());
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    LOG.error("SCROLL");
    LOG.info("onScroll: " + e1.toString() + e2.toString());
    return true;
  }

  @Override
  public void onShowPress(MotionEvent event) {
    LOG.debug("onShowPress: " + event.toString());
  }

  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    LOG.debug("onSingleTapUp: " + event.toString());
    return true;
  }

  @Override
  public boolean onDoubleTap(MotionEvent event) {
    LOG.debug("onDoubleTap: " + event.toString());
    return true;
  }

  @Override
  public boolean onDoubleTapEvent(MotionEvent event) {
    LOG.debug("onDoubleTapEvent: " + event.toString());
    return true;
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent event) {
    LOG.debug("onSingleTapConfirmed: " + event.toString());
    return true;
  }

  @Override
  public boolean onScale(ScaleGestureDetector detector) {
    LOG.debug("onScale: " + detector.getScaleFactor());
    return true;
  }

  @Override
  public boolean onScaleBegin(ScaleGestureDetector detector) {
    LOG.debug("onScaleBegin: " + detector.getScaleFactor());
    return true;
  }

  @Override
  public void onScaleEnd(ScaleGestureDetector detector) {
    LOG.debug("onScaleEnd: " + detector.toString());
  }
}

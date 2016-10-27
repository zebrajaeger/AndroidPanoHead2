package de.zebrajaeger.androidpanohead2;

import android.os.Bundle;
import android.view.View;

import de.zebrajaeger.jgrblconnector.event.GrblStatusEvent;

public class SetCamFOVActivity extends AbstractCamSetActivity {
  public static final String CAM_FOV = "CAM_FOV";
  private AngleView angleView;
  private Float camBorder1;
  private Float camBorder2;
  private Float camFov;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_cam_fov);
  }

  @Override
  protected boolean canFinishWithResult() {
    return (camFov != null);
  }

  @Override
  protected void onStart() {
    super.onStart();
    angleView = (AngleView) findViewById(R.id.cam_view_horizontal);
    Bundle extras = getIntent().getExtras();

    if (extras.containsKey(CAM_FOV)) {
      camFov = extras.getFloat(CAM_FOV);
    }
  }

  public void onButtonLeft(View view) {
    GrblStatusEvent lastEvent = getLastEvent();
    if (lastEvent != null) {
      camBorder1 = lastEvent.getMpos().getX();
      trySetCam();
    }
  }

  public void onButtonRight(View view) {
    GrblStatusEvent lastEvent = getLastEvent();
    if (lastEvent != null) {
      camBorder2 = lastEvent.getMpos().getX();
      trySetCam();
    }
  }

  private void trySetCam() {
    if (camBorder1 != null && camBorder2 != null) {
      camFov = Math.abs(camBorder1 - camBorder2);
      angleView.setCamFov(camFov);
    }
  }

  @Override
  protected void onFinish(Bundle conData) {
    if (camFov != null) {
      conData.putFloat(CAM_FOV, camFov);
    }
  }
}

package de.zebrajaeger.androidpanohead2.activity;

import android.os.Bundle;
import android.view.View;

import de.zebrajaeger.androidpanohead2.R;
import de.zebrajaeger.androidpanohead2.view.AngleView;
import de.zebrajaeger.jgrblconnector.event.GrblStatusEvent;

public class SetCamFOVActivity extends AbstractCamSetActivity {
  public static final String CAM_FOV = "CAM_FOV";
  private AngleView angleView;
  private Float camBorder1;
  private Float camBorder2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_cam_fov);
  }

  @Override
  protected void onStart() {
    super.onStart();

    angleView = (AngleView) findViewById(R.id.cam_view_horizontal);

    Bundle extras = getIntent().getExtras();
    if (extras != null && extras.containsKey(CAM_FOV)) {
      angleView.setCamFov(extras.getFloat(CAM_FOV));
    }
  }

  public void onButtonLeft(View view) {
    GrblStatusEvent lastEvent = getLastEvent();
    if (lastEvent != null) {
      camBorder1 = lastEvent.getMpos().getX() * 360f;
      trySetCam();
    }
  }

  public void onButtonRight(View view) {
    GrblStatusEvent lastEvent = getLastEvent();
    if (lastEvent != null) {
      camBorder2 = lastEvent.getMpos().getX() * 360f;
      trySetCam();
    }
  }

  private void trySetCam() {
    if (camBorder1 != null && camBorder2 != null) {
      angleView.setCamFov(Math.abs(camBorder1 - camBorder2));
    }
  }

  @Override
  protected boolean canFinishWithResult() {
    return (angleView.getCamFov() != null);
  }

  @Override
  protected void onFinish(Bundle conData) {
    Float camFov = angleView.getCamFov();
    if (camFov != null) {
      conData.putFloat(CAM_FOV, camFov);
    }
  }
}

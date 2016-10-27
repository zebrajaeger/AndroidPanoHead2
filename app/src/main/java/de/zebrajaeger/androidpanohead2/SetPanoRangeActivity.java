package de.zebrajaeger.androidpanohead2;

import android.os.Bundle;
import android.view.View;

import de.zebrajaeger.jgrblconnector.event.GrblStatusEvent;

public class SetPanoRangeActivity extends AbstractCamSetActivity {
  public static final String CAM_FOV = "CAM_FOV";
  public static final String PANO_BORDER_1 = "PANO_BORDER_1";
  public static final String PANO_BORDER_2 = "PANO_BORDER_2";
  private AngleView angleView;
  private Float camFov;
  private Float panoBorder1;
  private Float panoBorder2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_pano_range);
  }

  @Override
  protected void onStart() {
    super.onStart();
    angleView = (AngleView) findViewById(R.id.cam_view_horizontal);
    Bundle extras = getIntent().getExtras();

    if (extras.containsKey(CAM_FOV)) {
      camFov = extras.getFloat(CAM_FOV);
      angleView.setCamFov(camFov);
    }

    if (extras.containsKey(PANO_BORDER_1)) {
      panoBorder1 = extras.getFloat(PANO_BORDER_1);
    }

    if (extras.containsKey(PANO_BORDER_2)) {
      panoBorder2 = extras.getFloat(PANO_BORDER_2);
    }

    trySetPano();
  }

  @Override
  protected boolean canFinishWithResult() {
    return (camFov != null && panoBorder1 != null && panoBorder2 != null);
  }

  public void onButtonLeft(View view) {
    GrblStatusEvent lastEvent = getLastEvent();
    if (lastEvent != null) {
      panoBorder1 = lastEvent.getMpos().getX();
      panoBorder1 += (camFov / 2f);
      trySetPano();
    }
  }

  public void onButtonRight(View view) {
    GrblStatusEvent lastEvent = getLastEvent();
    if (lastEvent != null) {
      panoBorder2 = lastEvent.getMpos().getX();
      panoBorder2 += (camFov / 2f);
      trySetPano();
    }
  }

  private void trySetPano() {
    if (canFinishWithResult()) {
      angleView.setPanoFov(Math.abs(panoBorder2 - panoBorder1));
      angleView.setPanoAngle(Math.abs(panoBorder2 + panoBorder1) / 2f);
    }
  }

  @Override
  protected void onFinish(Bundle conData) {
    conData.putFloat(CAM_FOV, camFov);
    conData.putFloat(PANO_BORDER_1, panoBorder1);
    conData.putFloat(PANO_BORDER_2, panoBorder2);
  }
}

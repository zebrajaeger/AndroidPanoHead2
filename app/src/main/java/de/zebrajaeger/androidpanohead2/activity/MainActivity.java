package de.zebrajaeger.androidpanohead2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import de.zebrajaeger.androidpanohead2.R;
import de.zebrajaeger.androidpanohead2.panohead.PanoHead;
import de.zebrajaeger.androidpanohead2.shot.CalculatorData;
import de.zebrajaeger.androidpanohead2.shot.ShooterScript;
import de.zebrajaeger.androidpanohead2.shot.ShotCalculator;
import de.zebrajaeger.androidpanohead2.shot.calculator.SimpleCalculator;
import de.zebrajaeger.androidpanohead2.storage.AppData;
import de.zebrajaeger.androidpanohead2.storage.Storage;
import de.zebrajaeger.androidpanohead2.util.Bounds1D;
import de.zebrajaeger.androidpanohead2.util.Bounds2D;
import de.zebrajaeger.androidpanohead2.util.FinalInt;
import de.zebrajaeger.androidpanohead2.util.Fov1D;
import de.zebrajaeger.androidpanohead2.util.Fov2D;
import de.zebrajaeger.androidpanohead2.util.Overlap;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  public static final int SET_CAM_FOV_REQUEST_CODE = 90;
  public static final int SET_PANO_BOUNDS_REQUEST_CODE = 91;
  public static final int PANO_SHOT_REQUEST_CODE = 92;
  public static final int COMMON_SETTINGS = 93;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    btAutoconnect();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case SET_CAM_FOV_REQUEST_CODE:
        if (resultCode == RESULT_OK && data != null) {
          onSetCamFovResult(data.getExtras());
        }
        break;

      case SET_PANO_BOUNDS_REQUEST_CODE:
        if (resultCode == RESULT_OK && data != null) {
          onSetPanoRangeResult(data.getExtras());
        }
        break;

      case COMMON_SETTINGS:
        if (resultCode == RESULT_OK && data != null) {
          onCommonSettingsResult(data.getExtras());
        }
        break;
    }
  }

  public boolean btAutoconnect() {
    AppData appData = Storage.getInstance().getAppData(getApplicationContext());
    PanoHead.instance().refreshDeviceList();
    final ArrayList<String> names = PanoHead.instance().getSortedDeviceNames();
    String btAdapter = appData.getBtAdapter();
    if (names.contains(btAdapter)) {
      if (PanoHead.instance().setCurrentDevice(btAdapter)) {
        // TODO activate CamFocButton
        return true;
      }
    }
    return false;
  }

  public void buttonBtSelectDeviceOnClick(View v) {
    PanoHead.instance().refreshDeviceList();
    final String[] names = PanoHead.instance().getSortedDeviceNamesAsArray();

    boolean hasDevices = (names.length > 0);
    final FinalInt selected = new FinalInt(hasDevices ? 0 : -1);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(hasDevices ? "Select Bluetooth Device" : "No Devices Found");
    if (hasDevices) {
      builder.setTitle("Select Bluetooth Device");
      builder.setSingleChoiceItems(names, 0, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          selected.setValue(which);
        }
      });
      builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          String name = names[selected.getValue()];

          Storage.getInstance().getAppData(getApplicationContext()).setBtAdapter(name);
          Storage.getInstance().save(getApplicationContext());

          //if (PanoHead.instance().setCurrentDevice(name)) {
          // TODO activate CamFocButton
          //}
        }
      });
      builder.setNegativeButton("Cancel", null);
    } else {
      builder.setTitle("No Devices Found");
      builder.setPositiveButton("Ok", null);
    }

    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      //new GrblCore(null);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_settings) {
        return true;
      }

      return super.onOptionsItemSelected(item);
    }
    */
  public void onCommonSettings(View v) {

    Intent intent = new Intent(this, CommonSettingsActivity.class);

    Overlap overlap = getAppData().getOverlap();
    intent.putExtra(CommonSettingsActivity.OVERLAP, (overlap == null) ? new Overlap() : overlap);

    startActivityForResult(intent, COMMON_SETTINGS);
  }

  public void onCommonSettingsResult(Bundle res) {
    getAppData().setOverlap((Overlap) res.get(CommonSettingsActivity.OVERLAP));
    saveAppData();
  }

  public void onSetCamFov(View v) {
    Intent intent = new Intent(this, SetCamFOVActivity.class);
    Fov2D camFov = getAppData().getCamFov();
    if (camFov != null) {
      intent.putExtra(SetCamFOVActivity.CAM_FOV, camFov.getX().getSweepAngle());
    }
    startActivityForResult(intent, SET_CAM_FOV_REQUEST_CODE);
  }

  public void onSetCamFovResult(Bundle res) {
    Fov2D camFov = getAppData().getCamFov();
    if (camFov == null) {
      camFov = new Fov2D();
      getAppData().setCamFov(camFov);
    }

    Fov1D fovX = camFov.getX();
    if (fovX == null) {
      fovX = new Fov1D();
      camFov.setX(fovX);
    }

    fovX.setSweepAngle(res.getFloat(SetCamFOVActivity.CAM_FOV));
    saveAppData();

    // TODO activate PanorangeButton
  }

  public void onSetPanoRange(View v) {
    Intent intent = new Intent(this, SetPanoRangeActivity.class);

    Fov2D camFov = getAppData().getCamFov();
    if (camFov != null) {
      intent.putExtra(SetPanoRangeActivity.CAM_FOV, camFov.getX().getSweepAngle());
    }

    Bounds2D panoBounds = getAppData().getPanoBounds();
    if (panoBounds != null) {
      intent.putExtra(SetPanoRangeActivity.PANO_BORDER_1, panoBounds.getX().getB1());
      intent.putExtra(SetPanoRangeActivity.PANO_BORDER_2, panoBounds.getX().getB2());
    }

    startActivityForResult(intent, SET_PANO_BOUNDS_REQUEST_CODE);
  }

  public void onSetPanoRangeResult(Bundle res) {
    if (!res.containsKey(SetPanoRangeActivity.PANO_BORDER_1) || !res.containsKey(SetPanoRangeActivity.PANO_BORDER_2)) {
      throw new IllegalArgumentException("one or both borders missing");
    }

    Bounds2D panoBounds = getAppData().getPanoBounds();
    if (panoBounds == null) {
      panoBounds = new Bounds2D();
      getAppData().setPanoBounds(panoBounds);
    }

    Bounds1D boundsX = panoBounds.getX();
    if (boundsX == null) {
      boundsX = new Bounds1D();
      panoBounds.setX(boundsX);
    }

    boundsX.setB1(res.getFloat(SetPanoRangeActivity.PANO_BORDER_1));
    boundsX.setB2(res.getFloat(SetPanoRangeActivity.PANO_BORDER_2));

    saveAppData();
  }

  public void onBtnShot(View v) {
    Intent intent = new Intent(this, PanoShotActivity.class);

    CalculatorData cd = new CalculatorData(getAppData().getCamFov(), getAppData().getOverlap(), getAppData().getPanoBounds());
    ShotCalculator calc = new SimpleCalculator();
    ShooterScript script = calc.createScript(cd);
    intent.putExtra(PanoShotActivity.SHOOTER_SCRIPT, script);
    startActivityForResult(intent, PANO_SHOT_REQUEST_CODE);
  }

  private AppData getAppData() {
    return Storage.getInstance().getAppData(getApplicationContext());
  }

  private void saveAppData() {
    Storage.getInstance().save(getApplicationContext());
  }
}

package de.zebrajaeger.androidpanohead2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.zebrajaeger.androidpanohead2.data.Storage;
import de.zebrajaeger.androidpanohead2.panohead.PanoHead;
import de.zebrajaeger.androidpanohead2.util.FinalInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {

  private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);
  //private PosReceiver posReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

/*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });*/
  }

 /*   private PosReceiver registerPosReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("de.zebrajaeger.panohead.POS");

        PosReceiver posReceiver = new PosReceiver();
        registerReceiver(posReceiver, filter);
        return posReceiver;
    }*/

  @Override
  protected void onStart() {
    super.onStart();
    //Storage.initAndLoadSilently(getApplicationContext());
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
          PanoHead.instance().setCurrentDevice(name);
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

  public void onSetPhotoBounds(View v) {
    Intent intent = new Intent(this, SetBorderActivity.class);
    intent.putExtra("title", "Set Image Bounds");
    startActivityForResult(intent, 90);
  }

  public void onSetPhotoBoundsResult(int resultCode, Bundle res) {
    if (resultCode != RESULT_OK) {
      return;
    }

    // ?? String result = res.getString("param_result");
    boolean dirty = false;
    if (res.containsKey("x1") && res.containsKey("x2")) {
      double x1 = res.getDouble("x1");
      double x2 = res.getDouble("x2");
      double diff = Math.abs(x1 - x2);
      //Storage.instance().getShooterData().setImgWidth(diff);
      dirty = true;
    }

    if (res.containsKey("y1") && res.containsKey("y2")) {
      double y1 = res.getDouble("y1");
      double y2 = res.getDouble("y2");
      double diff = Math.abs(y1 - y2);
      //Storage.instance().getShooterData().setImgHeigth(diff);
      dirty = true;
    }
    if (dirty) {
      //Storage.storeSilently();
    }
  }

  public void onSetPanoramicBounds(View v) {
    Intent intent = new Intent(this, SetBorderActivity.class);
    intent.putExtra("title", "Set Panoramic Bounds");
    startActivityForResult(intent, 91);
  }

  public void onSetPanoramicBoundsResult(int resultCode, Bundle res) {
    if (resultCode != RESULT_OK) {
      return;
    }

    if (res.containsKey("x1") && res.containsKey("x2")) {
      double x1 = res.getDouble("x1");
      double x2 = res.getDouble("x2");
      double width = Math.abs(x1 - x2);
      // special case: full range (or more)
      if (width >= 360.0d) {
        x1 = 0.0d;
        width = 360.0d;
      }
      //Storage.instance().getShooterData().setPanoWidth(width);
      //Storage.instance().getShooterData().setPanoWidthStartAngle(x1);
    }

    if (res.containsKey("y1") && res.containsKey("y2")) {
      double y1 = res.getDouble("y1");
      double y2 = res.getDouble("y2");
      double height = Math.abs(y1 - y2);
      // special case: full range (or more)
      if (height >= 180.0d) {
        y1 = 0.0d;
        height = 180.0d;
      }
      //Storage.instance().getShooterData().setPanoWidth(height);
      //Storage.instance().getShooterData().setPanoWidthStartAngle(y1);
    }
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case 90:
        onSetPhotoBoundsResult(resultCode, data.getExtras());
        break;
      case 91:
        onSetPanoramicBoundsResult(resultCode, data.getExtras());
        break;
    }
  }


/*    @Override
    public void grblStatus(GrblStatusEvent status) {
        Intent i = new Intent("de.zebrajaeger.panohead.POS");
        i.putExtra("status", status.getStatus().toString());
        i.putExtra("m.x", status.getMpos().getX());
        i.putExtra("m.y", status.getMpos().getY());
        i.putExtra("w.x", status.getWpos().getX());
        i.putExtra("w.y", status.getWpos().getY());
        sendBroadcast(i);
    }*/


/*    public void onTest(View v) {
        Intent i = new Intent("de.zebrajaeger.panohead.POS");
        i.putExtra("status", "Yo!");
        i.putExtra("m.x", 1.0f);
        i.putExtra("m.y", 2.0f);
        i.putExtra("w.x", 3.0f);
        i.putExtra("w.y", 4.0f);
        sendBroadcast(i);
    }*/

  /*  @Override
    public void onPos(String status, float mx, float my, float wx, float wy) {
        LOG.error("ONPOS: " + status + ", " + mx + ", " + my + ", " + wx + ", " + wy);
    }*/
}

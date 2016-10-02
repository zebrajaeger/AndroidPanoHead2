package de.zebrajaeger.androidpanohead2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import de.zebrajaeger.panohead2.core.event.GrblStatusEvent;
import de.zebrajaeger.panohead2.core.event.GrblStatusListener;

public class MainActivity extends AppCompatActivity implements GrblStatusListener, PosListener {

    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);
    private static final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private PosReceiver posReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PanoHead.instance().addListener(this);
        posReceiver = registerPosReceiver();
        posReceiver.add(this);

/*
        LOG.error("############ CREATE #################");
        Log.d("foo", "bar");
        LOG.debug("############ CREATE #################");
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice dev = null;
        for (BluetoothDevice d : bta.getBondedDevices()) {
            LOG.debug("DEV: " + d + "; " + d.getName());
            dev = d;
        }

        BluetoothSocket socket = null;
        if (dev != null) {
            try {
                socket = dev.createRfcommSocketToServiceRecord(SerialPortServiceClass_UUID);
            } catch (IOException e) {
                LOG.error("Failed to create Socket", e);
            }
        }

        if (socket != null) {
            try {
                socket.connect();
            } catch (IOException e) {
                LOG.error("Failed to connect Socket", e);
                try {
                    socket.close();
                } catch (IOException e1) {
                    LOG.error("Failed to close Socket", e);
                }
            }
        }

        if (socket != null && socket.isConnected()) {
            try {
                OutputStream os = socket.getOutputStream();
                os.write("Hello World from Android via Bluetooth".getBytes());
                os.flush();

            } catch (IOException e) {
                LOG.error("Failed to send", e);
            }

            try {
                socket.close();
            } catch (IOException e) {
                LOG.error("Failed to close Socket", e);
            }
        }
*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private PosReceiver registerPosReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("de.zebrajaeger.panohead.POS");

        PosReceiver posReceiver = new PosReceiver();
        registerReceiver(posReceiver, filter);
        return posReceiver;
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
        LOG.error("SHOW");
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

    @Override
    public void grblStatus(GrblStatusEvent status) {
        Intent i = new Intent("de.zebrajaeger.panohead.POS");
        i.putExtra("status", status.getStatus().toString());
        i.putExtra("m.x", status.getMpos().getX());
        i.putExtra("m.y", status.getMpos().getY());
        i.putExtra("w.x", status.getWpos().getX());
        i.putExtra("w.y", status.getWpos().getY());
        sendBroadcast(i);
    }

    public void onSetBounds(View v) {
        Intent intent = new Intent(this, SetBorderActivity.class);
        startActivity(intent);
    }

    public void onTest(View v) {
        Intent i = new Intent("de.zebrajaeger.panohead.POS");
        i.putExtra("status", "Yo!");
        i.putExtra("m.x", 1.0f);
        i.putExtra("m.y", 2.0f);
        i.putExtra("w.x", 3.0f);
        i.putExtra("w.y", 4.0f);
        sendBroadcast(i);
    }

    @Override
    public void onPos(String status, float mx, float my, float wx, float wy) {
        LOG.error("ONPOS: " + status + ", " + mx + ", " + my + ", " + wx + ", " + wy);
    }
}

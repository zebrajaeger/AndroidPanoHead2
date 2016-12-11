package de.zebrajaeger.androidpanohead2.panohead;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import de.zebrajaeger.jgrblconnector.Grbl;
import de.zebrajaeger.jgrblconnector.command.GrblResponse;
import de.zebrajaeger.jgrblconnector.event.GrblListener;
import de.zebrajaeger.jgrblconnector.gear.SimpleGearSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by lars on 18.09.2016.
 */
public class PanoHead {
  private static final Logger LOG = LoggerFactory.getLogger(PanoHead.class);
  private static final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  public static final int REQUEST_STATUS_PERIOD_MS = 300;
  private static PanoHead singleton = null;

  private final BluetoothAdapter bta;
  private Map<String, BluetoothDevice> devices = Collections.EMPTY_MAP;
  private BluetoothDevice currentDevice = null;
  private BluetoothSocket socket = null;
  private CommAdapter comm;
  private Grbl grbl;

  public static PanoHead instance() {
    if (singleton == null) {
      singleton = new PanoHead();
    }
    return singleton;
  }

  public PanoHead() {
    bta = BluetoothAdapter.getDefaultAdapter();
    comm = new CommAdapter();
    grbl = new Grbl(comm, 5000);
    grbl.setGearSet(new SimpleGearSet((float) (19.0d + (38.0d / 187.0d))));
    createStatusThread();
  }

  private String createUniqueName(BluetoothDevice d) {
    return d.getName() + "(" + d.getAddress() + ")";
  }

  public void refreshDeviceList() {
    devices = new HashMap<>();
    if(bta!=null) {
      for (BluetoothDevice d : bta.getBondedDevices()) {
        devices.put(createUniqueName(d), d);
      }
    }
  }

  public ArrayList<String> getSortedDeviceNames() {
    return new ArrayList(new TreeSet(devices.keySet()));
  }

  public String[] getSortedDeviceNamesAsArray() {
    Set<String> names = devices.keySet();
    return new TreeSet<>(names).toArray(new String[names.size()]);
  }

  public List<BluetoothDevice> getDevices() {
    List<BluetoothDevice> result = new ArrayList<>(devices.size());
    for (String name : getSortedDeviceNames()) {
      result.add(devices.get(name));
    }
    return result;
  }

  public BluetoothDevice getByName(String name) {
    return devices.get(name);
  }

  public BluetoothDevice getCurrentDevice() {
    return currentDevice;
  }

  public String getCurrentName() {
    return currentDevice == null ? null : createUniqueName(currentDevice);
  }

  public boolean setCurrentDevice(String name) {
    BluetoothDevice dev = devices.get(name);
    return setCurrentDevice(dev);
  }

  public boolean setCurrentDevice(BluetoothDevice currentDevice) {
    if (this.socket != null) {
      if (this.socket.isConnected()) {
        try {
          this.socket.close();
        } catch (IOException e) {
          LOG.error("Could not close socket: " + e, e);
        }
      }
      this.socket = null;
      comm.removeSocket();
    }

    this.currentDevice = currentDevice;
    if (this.currentDevice != null) {
      try {
        socket = this.currentDevice.createRfcommSocketToServiceRecord(SerialPortServiceClass_UUID);
        socket.connect();
        comm.setSocket(socket);
      } catch (IOException e) {
        LOG.error("Could not create and connect socket: " + e, e);
      }
    }

    if (!isConnected()) {
      LOG.error("Socket is not connected, clean up");
      socket = null;
      this.currentDevice = null;
    }
    return currentDevice != null;
  }

  public boolean isConnected() {
    return currentDevice != null && socket != null && socket.isConnected();
  }

  private Thread createStatusThread() {
    Thread result = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          for (; ; ) {
            Thread.sleep(REQUEST_STATUS_PERIOD_MS);
            try {
              if (isConnected()) {
                grbl.requestStatus();
              }
            } catch (IOException e) {
              LOG.error("Grbl Status Thread Exception", e);
            }
          }
        } catch (InterruptedException e) {
          LOG.info("Grbl Status Thread interrupted");
        }
      }
    });
    result.start();
    return result;
  }

  public void addListener(GrblListener l) {
    grbl.addListener(l);
  }

  public void removeListener(GrblListener l) {
    grbl.removeListener(l);
  }

  public GrblResponse moveXYRelativeBlocking(float x, float y) throws InterruptedException, IOException {
    return grbl.moveXYRelativeBlocking(x, y);
  }
}

package zebrajaeger.de.androidtest2;

import android.bluetooth.BluetoothSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.zebrajaeger.panohead2.core.serial.SerialConnectionAdapter;

/**
 * Created by lars on 18.09.2016.
 */
public class CommAdapter extends SerialConnectionAdapter implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger("XXX");

    private BluetoothSocket socket;
    private OutputStream os;
    private InputStream is;
    private Thread thread;

    public void removeSocket(){
        try {
            setSocket(null);
        } catch (IOException e) {
            // no streams are requested, so no ioexception can occur but...
            throw new RuntimeException("Unexpected, but there is a IOException", e);
        }
    }

    public void setSocket(BluetoothSocket socket) throws IOException {
        this.socket = socket;

        // stop current thread if exists
        if(thread!=null){
            thread.interrupt();
            try {
                thread.join(5000);
                if(thread.isAlive()){
                    throw new IllegalStateException("Could not stop running thread");
                }
                thread = null;
            } catch (InterruptedException ignore) {
                return;
            }
        }

        if (socket == null) {
            os = null;
            is = null;
        } else {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                int val = is.read();
                sendEvent((byte) val);
            }
        } catch (IOException e) {
            LOG.error("Could not read from Stream");
        }
    }

    @Override
    public void write(byte b) throws IOException {
        if (os == null) {
            throw new IllegalStateException("No outputstream available");
        } else {
            os.write(b);
        }
    }
}

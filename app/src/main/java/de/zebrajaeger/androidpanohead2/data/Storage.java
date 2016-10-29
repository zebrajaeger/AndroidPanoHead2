package de.zebrajaeger.androidpanohead2.data;

import android.content.Context;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author lars on 09.10.2016.
 */
@Deprecated
public class Storage {
 /* private static final Logger LOG = LoggerFactory.getLogger(Storage.class);
  private static final Storage INSTANCE = new Storage();

  public static Storage instance() {
    return INSTANCE;
  }

  public static void initAndLoadSilently(Context ctx) {
    try {
      INSTANCE.init(ctx).load();
    } catch (IOException e) {
      LOG.error("Could not load Storage", e);
    }
  }

  public static void storeSilently() {
    try {
      INSTANCE.store();
    } catch (IOException e) {
      LOG.error("Could not store Storage", e);
    }
  }

  private Context context = null;
  private ObjectMapper mapper = new ObjectMapper();
  private ShooterData shooterData = new ShooterData();


  public Storage init(Context context) {
    this.context = context;
    return this;
  }

  private <T> T load(Class<T> clazz) throws IOException {
    File filesDir = context.getApplicationContext().getFilesDir();
    String name = clazz.getCanonicalName();
    File f = new File(filesDir, name + ".json");
    return mapper.readValue(f, clazz);
  }

  private void store(Object o) throws IOException {
    File filesDir = context.getApplicationContext().getFilesDir();
    String name = o.getClass().getCanonicalName();
    File f = new File(filesDir, name + ".json");
    File fbck = new File(filesDir, name + ".json.bck");
    if (f.exists()) {
      if (fbck.exists()) {
        fbck.delete();
      }
      f.renameTo(fbck);
    }

    mapper.writeValue(f, o);
  }

  public void load() throws IOException {
    shooterData = load(ShooterData.class);
  }

  public void store() throws IOException {
    store(shooterData);
  }

  public ShooterData getShooterData() {
    return shooterData;
  }*/
}

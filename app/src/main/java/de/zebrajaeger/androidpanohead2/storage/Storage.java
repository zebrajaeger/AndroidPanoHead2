package de.zebrajaeger.androidpanohead2.storage;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.annotation.Nonnull;

/**
 * @author lars on 29.10.2016.
 */
public class Storage {
  private static Storage instance = new Storage();

  public static Storage getInstance() {
    return instance;
  }

  private SharedPreferences getPreferences(Context con) {
    return PreferenceManager.getDefaultSharedPreferences(con);
  }

  private AppData appData;

  public AppData getAppData() {
    return appData;
  }

  public AppData getAppData(@Nonnull Context con) {
    if (appData == null) {
      appData = load(con, AppData.class);
      if (appData == null) {
        appData = new AppData();
      }
    }
    return appData;
  }

  public void save(Context con) {
    if (appData != null) {
      save(con, appData);
    }
  }

  protected void save(@Nonnull Context con, Object o) {
    SharedPreferences.Editor prefsEditor = getPreferences(con).edit();
    prefsEditor.putString(o.getClass().getName(), new Gson().toJson(o));
    prefsEditor.commit();
  }

  protected <T> T load(@Nonnull Context con, Class<T> clazz) {
    String json = getPreferences(con).getString(clazz.getName(), null);
    return (json != null) ? new Gson().fromJson(json, clazz) : null;
  }
}

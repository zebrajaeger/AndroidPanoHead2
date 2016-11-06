package de.zebrajaeger.androidpanohead2.util;

import android.graphics.Color;
import android.text.Editable;
import android.widget.EditText;

/**
 * @author lars on 06.11.2016.
 */
public class PercentWatcher extends TextWatcherAdapter {
  private EditText toWatch;

  public PercentWatcher(EditText toWatch) {
    this.toWatch = toWatch;
  }

  public void afterTextChanged(Editable s) {
    boolean ok = true;
    try {
      int i = Integer.parseInt(s.toString());
      ok = ok && (i >= 0 && i <= 100);
    } catch (NumberFormatException e) {
      ok = false;
    }
    toWatch.setBackgroundColor((ok) ? Color.WHITE : Color.RED);
  }

  public EditText getToWatch() {
    return toWatch;
  }

  public static PercentWatcher of(EditText toWatch) {
    PercentWatcher percentWatcher = new PercentWatcher(toWatch);
    percentWatcher.getToWatch().addTextChangedListener(percentWatcher);
    return percentWatcher;
  }
}

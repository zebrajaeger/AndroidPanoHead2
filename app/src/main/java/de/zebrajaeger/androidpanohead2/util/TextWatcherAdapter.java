package de.zebrajaeger.androidpanohead2.util;

import android.text.Editable;
import android.text.TextWatcher;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author lars on 06.11.2016.
 */
public class TextWatcherAdapter implements TextWatcher {
  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {

  }
  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}

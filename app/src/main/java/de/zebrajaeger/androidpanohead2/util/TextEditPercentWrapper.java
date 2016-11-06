package de.zebrajaeger.androidpanohead2.util;

import android.graphics.Color;
import android.text.Editable;
import android.widget.EditText;

/**
 * @author lars on 06.11.2016.
 */
public class TextEditPercentWrapper extends TextWatcherAdapter {
  private EditText toWatch;

  public TextEditPercentWrapper(EditText toWatch) {
    this.toWatch = toWatch;
  }

  public void afterTextChanged(Editable s) {
    toWatch.setBackgroundColor((isValueOk(s.toString())) ? Color.WHITE : Color.RED);
  }

  private void check(String value){
    toWatch.setBackgroundColor((isValueOk(value)) ? Color.WHITE : Color.RED);
  }

  public EditText getToWatch() {
    return toWatch;
  }

  private boolean isValueOk(String value){
    boolean ok = true;
    try {
      int i = Integer.parseInt(value);
      ok = ok && (i >= 0 && i <= 100);
    } catch (NumberFormatException e) {
      ok = false;
    }
    return ok;
  }

  public void setValue(Integer value){
    if(value==null){
      toWatch.setText("");
    }else{
      toWatch.setText(value.toString());
    }
  }
  public void setValue(Float value){
    if(value==null){
      toWatch.setText("");
    }else{
      toWatch.setText(value.toString());
    }
  }

  public Integer getValue( ){
    String s = toWatch.getText().toString();
    return (isValueOk(s)) ? Integer.parseInt(s) : null;
  }
  public Float getValueAsFloat( ){
    String s = toWatch.getText().toString();
    return (isValueOk(s)) ? Float.parseFloat(s) : null;
  }

  public static TextEditPercentWrapper of(EditText toWatch) {
    TextEditPercentWrapper textEditPercentWrapper = new TextEditPercentWrapper(toWatch);
    textEditPercentWrapper.getToWatch().addTextChangedListener(textEditPercentWrapper);
    textEditPercentWrapper.check(toWatch.getText().toString());
    return textEditPercentWrapper;
  }
}

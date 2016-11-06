package de.zebrajaeger.androidpanohead2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import de.zebrajaeger.androidpanohead2.R;
import de.zebrajaeger.androidpanohead2.util.Overlap;
import de.zebrajaeger.androidpanohead2.util.TextEditPercentWrapper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CommonSettingsActivity extends AppCompatActivity {
  public static final String OVERLAP = "overlap";

  private TextEditPercentWrapper pwX;
  private TextEditPercentWrapper pwY;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    setContentView(R.layout.activity_common_settings);

    EditText editX = (EditText) findViewById(R.id.edit_overlap_X);
    pwX = TextEditPercentWrapper.of(editX);
    EditText editY = (EditText) findViewById(R.id.edit_overlap_y);
    pwY = TextEditPercentWrapper.of(editY);
  }

  @Override
  protected void onStart() {
    super.onStart();
    Bundle extras = getIntent().getExtras();
    if (extras.containsKey(OVERLAP)) {
      Overlap overlap = (Overlap) extras.get(OVERLAP);
      pwX.setValue(overlap.getX());
      pwY.setValue(overlap.getY());
    }
  }

  @Override
  public void onBackPressed() {

    Float x = pwX.getValueAsFloat();
    Float y = pwY.getValueAsFloat();
    if (x != null && y != null) {
      Bundle conData = new Bundle();

      conData.putSerializable(OVERLAP, new Overlap(x,y));

      Intent intent = new Intent();
      intent.putExtras(conData);
      setResult(RESULT_OK, intent);
      finish();
    } else {

      Intent intent = new Intent();
      setResult(RESULT_CANCELED, intent);
      finish();
    }
  }

  public void onSetOverlap20(View v) {
    setOverlap(20);
  }

  public void onSetOverlap25(View v) {
    setOverlap(25);
  }

  public void onSetOverlap33(View v) {
    setOverlap(33);
  }

  public void onSetOverlap50(View v) {
    setOverlap(50);
  }

  private void setOverlap(Integer value) {
    pwX.setValue(value);
    pwY.setValue(value);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

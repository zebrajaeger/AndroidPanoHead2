package de.zebrajaeger.androidpanohead2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import de.zebrajaeger.androidpanohead2.R;
import de.zebrajaeger.androidpanohead2.util.Overlap;
import de.zebrajaeger.androidpanohead2.util.PercentWatcher;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CommonSettingsActivity extends AppCompatActivity {
  public static final String OVERLAP = "overlap";

  private Overlap overlap = new Overlap(0f, 0f);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    setContentView(R.layout.activity_common_settings);

    EditText editX = (EditText) findViewById(R.id.edit_overlap_X);
    PercentWatcher.of(editX);
    EditText editY = (EditText) findViewById(R.id.edit_overlap_y);
    PercentWatcher.of(editY);
  }

  @Override
  protected void onStart() {
    super.onStart();
    Bundle extras = getIntent().getExtras();
    if (extras.containsKey(OVERLAP)) {
      overlap = (Overlap) extras.get(OVERLAP);
    }
  }

  @Override
  public void onBackPressed() {
    if (overlap.isInRange()) {
      Bundle conData = new Bundle();

      conData.putSerializable(OVERLAP, overlap);

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
    setOverlap(20f);
  }

  public void onSetOverlap25(View v) {
    setOverlap(25f);
  }

  public void onSetOverlap33(View v) {
    setOverlap(33f);
  }

  public void onSetOverlap50(View v) {
    setOverlap(50f);
  }

  private void setOverlap(Float value) {
    overlap.setX(value);
    overlap.setY(value);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

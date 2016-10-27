package de.zebrajaeger.androidpanohead2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import de.zebrajaeger.androidpanohead2.shot.ShooterScript;

public class PanoShotActivity extends AppCompatActivity {

  public static final String SHOOTER_SCRIPT = "shooterScript";
  private ShooterScript shooterScript;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_pano_shot);
  }

  @Override
  protected void onStart() {
    super.onStart();
    PanoShotView shotView = (PanoShotView) findViewById(R.id.view_shot);

    Bundle extras = getIntent().getExtras();
    if (extras.containsKey(SHOOTER_SCRIPT)) {
      shooterScript = (ShooterScript) extras.get(SHOOTER_SCRIPT);
      shotView.setScript(shooterScript);
    }
  }
}

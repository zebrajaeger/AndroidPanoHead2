package de.zebrajaeger.androidpanohead2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.zebrajaeger.androidpanohead2.R;
import de.zebrajaeger.androidpanohead2.panohead.PanoHead;
import de.zebrajaeger.androidpanohead2.shot.ShotPosition;
import de.zebrajaeger.androidpanohead2.shot.shooter.IShooterHead;
import de.zebrajaeger.androidpanohead2.shot.shooter.IShooterStateListener;
import de.zebrajaeger.androidpanohead2.shot.shooter.ShooterRobot;
import de.zebrajaeger.androidpanohead2.shot.shooter.ShooterScript;
import de.zebrajaeger.androidpanohead2.shot.shooter.ShooterSetup;
import de.zebrajaeger.androidpanohead2.view.PanoShotView;
import de.zebrajaeger.jgrblconnector.common.Position;
import de.zebrajaeger.jgrblconnector.event.GrblStatusEvent;
import de.zebrajaeger.jgrblconnector.event.GrblStatusListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lars
 */
public class PanoShotActivity extends AppCompatActivity implements GrblStatusListener, IShooterStateListener {

  public static final String SHOOTER_SCRIPT = "shooterScript";
  private static final Logger LOG = LoggerFactory.getLogger(AbstractCamSetActivity.class);
  private ShooterRobot robot;
  private ShooterScript script;
  private ShooterSetup setup = new ShooterSetup(1000, 100, 500, 1000); // TODO implement me
  private PanoShotView shotView;

  public PanoShotActivity() {
    PanoHead.instance().addListener(this);
    if (PanoHead.instance().isConnected()) {
      LOG.debug("create panoThread to send commands");
      //grblThread = createCommandThread();
    } else {
      LOG.debug("dont create panoThread because we are not connected");
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    setContentView(R.layout.activity_pano_shot);
  }

  @Override
  protected void onStart() {
    super.onStart();
    shotView = (PanoShotView) findViewById(R.id.view_shot);

    Bundle extras = getIntent().getExtras();
    if (extras.containsKey(SHOOTER_SCRIPT)) {
      script = (ShooterScript) extras.get(SHOOTER_SCRIPT);
      shotView.setScript(script);
    }
  }

  @Override
  public void grblStatus(GrblStatusEvent e) {
    Position pos = e.getMpos();
    shotView.setCamPosition(pos);
  }


  private void init() {
    IShooterHead head = new IShooterHead() {
      @Override
      public void moveTo(float w, float h) throws InterruptedException {
      }

      @Override
      public void setCam(boolean focus, boolean trigger) throws InterruptedException {
      }
    };
    robot = new ShooterRobot(head, script, setup);
    robot.addListener(this);
  }


  public void onButtonStart(View view) {
    robot.start();
  }

  public void onButtonStop(View view) {
    robot.stop();
  }

  public void onButtonPause(View view) {
    robot.pauseOrResume();
  }

  public void onButtonSettings(View view) {
    // show settings Dialog
  }

  @Override
  public void onShootStateChanged(ShooterRobot.ShotState from, ShooterRobot.ShotState to) {

  }

  @Override
  public void onRobotStateChanged(ShooterRobot.ThreadState from, ShooterRobot.ThreadState to) {
    if (from == ShooterRobot.ThreadState.STOPPED && to == ShooterRobot.ThreadState.RUNNING) {
      findViewById(R.id.button_start).setEnabled(false);
      findViewById(R.id.buttonStop).setEnabled(true);
      findViewById(R.id.button_pause).setEnabled(true);
    }

    if (to == ShooterRobot.ThreadState.STOPPED) {
      findViewById(R.id.button_start).setEnabled(true);
      findViewById(R.id.buttonStop).setEnabled(false);
      findViewById(R.id.button_pause).setEnabled(false);
    }

    if (to == ShooterRobot.ThreadState.MARKED_FOR_PAUSE) {
      findViewById(R.id.button_pause).setBackgroundColor(Color.YELLOW);
    }

    if (from == ShooterRobot.ThreadState.MARKED_FOR_PAUSE && to == ShooterRobot.ThreadState.PAUSED) {
      findViewById(R.id.button_pause).setBackgroundColor(Color.RED);
    }

    if (from == ShooterRobot.ThreadState.MARKED_FOR_PAUSE && to == ShooterRobot.ThreadState.RUNNING) {
      findViewById(R.id.button_pause).setBackgroundColor(Color.GRAY);
    }

    if (from == ShooterRobot.ThreadState.PAUSED && to == ShooterRobot.ThreadState.RUNNING) {
      findViewById(R.id.button_pause).setBackgroundColor(Color.GRAY);
    }
  }

  @Override
  public void onRobotImageChanged(int index, ShotPosition pos) {
    shotView.setCurrentImage(index);
  }
}

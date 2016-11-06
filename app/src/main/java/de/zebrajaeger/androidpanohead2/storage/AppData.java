package de.zebrajaeger.androidpanohead2.storage;

import de.zebrajaeger.androidpanohead2.util.Bounds2D;
import de.zebrajaeger.androidpanohead2.util.Fov2D;
import de.zebrajaeger.androidpanohead2.util.Overlap;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by lars on 30.10.2016.
 */

public class AppData {

  private String btAdapter;
  private Fov2D camFov;
  private Bounds2D panoBounds;
  private Overlap overlap;

  public AppData() {
  }

  public String getBtAdapter() {
    return btAdapter;
  }

  public void setBtAdapter(String btAdapter) {
    this.btAdapter = btAdapter;
  }

  public Fov2D getCamFov() {
    return camFov;
  }

  public void setCamFov(Fov2D camFov) {
    this.camFov = camFov;
  }

  public Bounds2D getPanoBounds() {
    return panoBounds;
  }

  public void setPanoBounds(Bounds2D panoBounds) {
    this.panoBounds = panoBounds;
  }

  public Overlap getOverlap() {
    return overlap;
  }

  public void setOverlap(Overlap overlap) {
    this.overlap = overlap;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}

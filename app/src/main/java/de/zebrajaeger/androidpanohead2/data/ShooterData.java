package de.zebrajaeger.androidpanohead2.data;

/**
 * Created by lars on 08.10.2016.
 */

public class ShooterData {
  private Double imgWidth;
  private Double imgHeigth;

  private Double panoWidth;
  private Double panoWidthStartAngle;

  private Double panoHeight;
  private Double panoHeightStartAngle;

  /**
   * in Image Width [0.0..1.0]
   */
  private Double overlapWidth;
  /**
   * in Image Height [0.0..1.0]
   */
  private Double overlapHeight;

  public Double getImgWidth() {
    return imgWidth;
  }

  public void setImgWidth(Double imgWidth) {
    this.imgWidth = imgWidth;
  }

  public Double getImgHeigth() {
    return imgHeigth;
  }

  public void setImgHeigth(Double imgHeigth) {
    this.imgHeigth = imgHeigth;
  }

  public Double getPanoWidth() {
    return panoWidth;
  }

  public void setPanoWidth(Double panoWidth) {
    this.panoWidth = panoWidth;
  }

  public Double getPanoWidthStartAngle() {
    return panoWidthStartAngle;
  }

  public void setPanoWidthStartAngle(Double panoWidthStartAngle) {
    this.panoWidthStartAngle = panoWidthStartAngle;
  }

  public Double getPanoHeight() {
    return panoHeight;
  }

  public void setPanoHeight(Double panoHeight) {
    this.panoHeight = panoHeight;
  }

  public Double getPanoHeightStartAngle() {
    return panoHeightStartAngle;
  }

  public void setPanoHeightStartAngle(Double panoHeightStartAngle) {
    this.panoHeightStartAngle = panoHeightStartAngle;
  }

  public Double getOverlapWidth() {
    return overlapWidth;
  }

  public void setOverlapWidth(Double overlapWidth) {
    this.overlapWidth = overlapWidth;
  }

  public Double getOverlapHeight() {
    return overlapHeight;
  }

  public void setOverlapHeight(Double overlapHeight) {
    this.overlapHeight = overlapHeight;
  }
}

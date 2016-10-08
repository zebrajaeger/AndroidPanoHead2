package de.zebrajaeger.androidpanohead2.data;

/**
 * Created by lars on 08.10.2016.
 */

public class ShooterData {
  private Boolean camTurnedVertical = false;
  /**
   * in Degree
   */
  private Double imgWidthDegree;
  /**
   * in Degree
   */
  private Double imgHeigthDegree;
  /**
   * in Degree
   */
  private Double panoWidthDegree;
  /**
   * in Degree
   */
  private Double panoHeightDegree;
  /**
   * in Image Width [0.0..1.0]
   */
  private Double overlapWidth;
  /**
   * in Image Height [0.0..1.0]
   */
  private Double overlapHeight;

  public Boolean getCamTurnedVertical() {
    return camTurnedVertical;
  }

  public void setCamTurnedVertical(Boolean camTurnedVertical) {
    this.camTurnedVertical = camTurnedVertical;
  }

  public Double getImgWidthDegree() {
    return imgWidthDegree;
  }

  public void setImgWidthDegree(Double imgWidthDegree) {
    this.imgWidthDegree = imgWidthDegree;
  }

  public Double getImgHeigthDegree() {
    return imgHeigthDegree;
  }

  public void setImgHeigthDegree(Double imgHeigthDegree) {
    this.imgHeigthDegree = imgHeigthDegree;
  }

  public Double getPanoWidthDegree() {
    return panoWidthDegree;
  }

  public void setPanoWidthDegree(Double panoWidthDegree) {
    this.panoWidthDegree = panoWidthDegree;
  }

  public Double getPanoHeightDegree() {
    return panoHeightDegree;
  }

  public void setPanoHeightDegree(Double panoHeightDegree) {
    this.panoHeightDegree = panoHeightDegree;
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

  public boolean canMakeRow() {
    return imgWidthDegree != null && panoWidthDegree != null && overlapWidth != null;
  }

  public boolean canMakeColumn() {
    return imgHeigthDegree != null && panoHeightDegree != null && overlapHeight != null;
  }
}

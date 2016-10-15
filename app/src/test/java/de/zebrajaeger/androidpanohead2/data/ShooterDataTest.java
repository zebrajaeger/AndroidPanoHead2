package de.zebrajaeger.androidpanohead2.data;

import de.zebrajaeger.androidpanohead2.shot.Shot;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by lars on 08.10.2016.
 */
public class ShooterDataTest {
  /*@Test
  public void testXCount1() {
    ShooterData sd = new ShooterData();
    sd.setPanoWidthDegree(20.0d);
    sd.setImgWidthDegree(10.0d);
    sd.setOverlapWidth(0.25d);

    Assert.assertEquals(sd.canMakeRow(), true);
    List<Shot> rowShots = sd.createRowShots(0.0d);
    Assert.assertEquals(rowShots.size(), 3);
    Assert.assertEquals(rowShots.get(0).getX(), 5.0d, 0.00001d);
    Assert.assertEquals(rowShots.get(1).getX(), 10.0d, 0.00001d);
    Assert.assertEquals(rowShots.get(2).getX(), 15.0d, 0.00001d);
  }

  @Test
  public void testXCount2() {
    ShooterData sd = new ShooterData();
    sd.setPanoWidthDegree(21.0d);
    sd.setImgWidthDegree(10.0d);
    sd.setOverlapWidth(0.25d);

    Assert.assertEquals(sd.canMakeRow(), true);
    List<Shot> rowShots = sd.createRowShots(0.0d);
    Assert.assertEquals(rowShots.size(), 3);
    Assert.assertEquals(rowShots.get(0).getX(), 5.0d, 0.00001d);
    Assert.assertEquals(rowShots.get(1).getX(), 10.5d, 0.00001d);
    Assert.assertEquals(rowShots.get(2).getX(), 16.0d, 0.00001d);
  }

  @Test
  public void testXCount3() {
    ShooterData sd = new ShooterData();
    sd.setPanoWidthDegree(360.0d);
    sd.setImgWidthDegree(10.0d);
    sd.setOverlapWidth(0.25d);

    Assert.assertEquals(sd.canMakeRow(), true);
    List<Shot> rowShots = sd.createRowShots(0.0d);
    Assert.assertEquals(rowShots.size(), 45);
    Assert.assertEquals(rowShots.get(0).getX(), 5.0d, 0.00001d);
  }

  @Test
  public void testXCount4() {
    ShooterData sd = new ShooterData();
    sd.setPanoWidthDegree(359.0d);
    sd.setImgWidthDegree(10.0d);
    sd.setOverlapWidth(0.25d);

    Assert.assertEquals(sd.canMakeRow(), true);
    List<Shot> rowShots = sd.createRowShots(0.0d);
    Assert.assertEquals(rowShots.size(), 45);
    Assert.assertEquals(rowShots.get(0).getX(), 5.0d, 0.00001d);
  }*/

}
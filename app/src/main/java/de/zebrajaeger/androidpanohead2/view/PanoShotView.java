package de.zebrajaeger.androidpanohead2.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import de.zebrajaeger.androidpanohead2.shot.ShooterScript;

/**
 * @author lars on 21.10.2016.
 */
public class PanoShotView extends View {
  private int w;
  private int h;
  //@Nullable
  // private Path imgPath;
/*
  private int imgCountX = 1;
  private int imgCountY = 1;
*/

  // aspect of sensor.
  //private float aspect = 4f / 3f;
  private Paint imgPaint;
  private ShooterScript script;

  public PanoShotView(Context context) {
    super(context);
    init();
  }

  public PanoShotView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public PanoShotView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    imgPaint = new Paint();
    imgPaint.setColor(Color.BLACK);
    imgPaint.setAntiAlias(true);
    imgPaint.setStrokeWidth(1);
    imgPaint.setStyle(Paint.Style.STROKE);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    this.w = w;
    this.h = h;
    //  updateImgPath();
  }
/*
 public void setImgCountX(int imgCountX) {
    this.imgCountX = imgCountX;
    updateImgPath();
    postInvalidate();
  }

  public void setImgCountY(int imgCountY) {
    this.imgCountY = imgCountY;
    updateImgPath();
    postInvalidate();
  }

  public void setImgCount(int imgCountX, int imgCountY) {
    this.imgCountX = imgCountX;
    this.imgCountY = imgCountY;
    updateImgPath();
    postInvalidate();
  }
    private void updateImgPath() {
    if (imgCountX <= 0 || imgCountY <= 0) {
      imgPath = null;
    } else {
      // max size on screen
      float maxX = (float) w / 4;
      float maxY = (float) h / 4;

      // width and heigth of every pic
      float x = (float) (w - imgCountX + 1) / (float) imgCountX;
      x = Math.min(x, maxX);
      float y = (float) (h - imgCountY + 1) / (float) imgCountY;
      y = Math.min(y, maxY);

      // max dimensions for a pic is calculated.
      // now correct aspect ration
      if (x / aspect > y) {
        x = y * aspect;
      }
      if (y * aspect > x) {
        y = x / aspect;
      }

      // draw path
      float cv = 0.2f;
      float c = Math.min(x*cv, y*cv);

      float x1 = 0;
      float x2 = c;
      float x3 = x - c;
      float x4 = x;

      float y1 = 0;
      float y2 = c;
      float y3 = y - c;
      float y4 = y;

      RectF bl = new RectF(x1, y1, x2, y2);
      RectF br = new RectF(x3, y1, x4, y2);
      RectF tl = new RectF(x1, y3, x2, y4);
      RectF tr = new RectF(x3, y3, x4, y4);

      imgPath = new Path();
      imgPath.moveTo(x1, y2);
      imgPath.arcTo(bl, 180, 90);
      imgPath.lineTo(x3, y1);
      imgPath.arcTo(br, 270, 90);
      imgPath.lineTo(x4, y3);
      imgPath.arcTo(tr, 0, 90);
      imgPath.lineTo(x3, y4);
      imgPath.arcTo(tl, 90, 90);
      imgPath.lineTo(x1, y2);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (imgPath != null) {
      float dx = (float) w / (float) imgCountX;
      float dy = (float) h / (float) imgCountY;

      float yPos = 0f;
      for (int y = 0; y < imgCountY; ++y) {
        float xPos = 0f;
        for (int x = 0; x < imgCountX; ++x) {
          canvas.save();
          canvas.translate(xPos,yPos);
          canvas.drawPath(imgPath, imgPaint);
          canvas.restore();
          xPos += dx;
        }
        yPos += dy;
      }
    }
  }*/

  public void setScript(ShooterScript script) {
    this.script = script;
  }
}

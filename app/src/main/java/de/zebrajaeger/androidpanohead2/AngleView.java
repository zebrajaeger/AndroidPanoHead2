package de.zebrajaeger.androidpanohead2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by lars on 09.10.2016.
 */

public class AngleView extends View {
  private int w;
  private int h;
  private float centerX;
  private float centerY;
  private float radius;
  @Nullable
  private Path pointer;
  private Paint scalaPaint;
  private Paint pointerPaint;
  private Bitmap camOriginalImg;
  private Bitmap camIScaledImg;
  private int scalaAngle1 = 0;
  private int scalaAngle2 = 360;
  @Nullable
  private Float fov = null;
  @Nullable
  private Float camAngle = null;
  @Nullable
  private Float targetAngle = null;

  public enum ScalaType {
    HORIZONTAL, VERTICAL
  }

  public AngleView(Context context) {
    super(context);
    init();
  }

  public AngleView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public AngleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    scalaPaint = new Paint();
    scalaPaint.setColor(0xff000000);
    scalaPaint.setAntiAlias(true);
    pointerPaint = new Paint();
    pointerPaint.setColor(0xffaaaaaa);
    camOriginalImg = BitmapFactory.decodeResource(getResources(), R.drawable.cam2_0400);
  }

  public void setScalaType(ScalaType type) {
    switch (type) {
      case HORIZONTAL:
        scalaAngle1 = 0;
        scalaAngle2 = 360;
        break;
      case VERTICAL:
        scalaAngle1 = 0;
        scalaAngle2 = 180;
        break;
      default:
        throw new UnsupportedOperationException("ScalaType '" + type + "' unknown");
    }
    postInvalidate();
  }

  public void addToTargetAngle(@Nullable Float addToTargetAngle) {
    if(targetAngle!=null){
      targetAngle += addToTargetAngle;
    }
  }

  public void setTargetAngle(@Nullable Float targetAngle) {
    if(this.targetAngle != targetAngle) {
      this.targetAngle = targetAngle;
      postInvalidate();
    }
  }


  public void setFov(@Nullable Float angle) {
    if(this.fov != angle) {
      this.fov = angle;
      updatePointer();
      postInvalidate();
    }
  }

  public void setCamAngle(@Nullable Float camAngle) {
    if(this.camAngle != camAngle) {
      this.camAngle = camAngle;
      updatePointer();
      postInvalidate();
    }
  }

  @Nullable
  public Float getTargetAngle() {
    return targetAngle;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    this.w = w;
    this.h = h;
    super.onSizeChanged(w, h, oldw, oldh);
    centerX = w / 2f;
    centerY = h / 2f;
    radius = Math.min(centerX, centerY);
    updatePointer();

    camIScaledImg = resize(camOriginalImg, (int) (radius / 2f));
  }

  private void updatePointer() {
    if (camAngle != null && fov != null) {
      Path path = new Path();
      float r1 = radius;

      path.moveTo(centerX, centerY);

      float a1 = (float) Math.toRadians(camAngle - (fov / 2f));
      float x1 = centerX + (float) (Math.sin(a1) * r1);
      float y1 = centerY - (float) (Math.cos(a1) * r1);
      path.lineTo(x1, y1);

      float a2 = (float) Math.toRadians(camAngle + (fov / 2f));
      float x2 = centerX + (float) (Math.sin(a2) * r1);
      float y2 = centerY - (float) (Math.cos(a2) * r1);
      path.lineTo(x2, y2);

      path.close();

      pointer = path;
    } else {
      pointer = null;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // draw cam view field if exists
    if (pointer != null) {
      canvas.drawPath(pointer, pointerPaint);
    }

    // draw black line
    if (targetAngle != null) {
      float x1 = centerX + (float) (Math.sin(Math.toRadians(camAngle)) * radius);
      float y1 = centerY - (float) (Math.cos(Math.toRadians(camAngle)) * radius);

      Paint p = new Paint();
      p.setColor(Color.GREEN);
      p.setStyle(Paint.Style.FILL);
      p.setStrokeWidth(5f);
      canvas.drawLine(centerX, centerY, x1, y1, p);
    }

    // draw red line
    if (targetAngle != null) {
      float x1 = (float) (Math.sin(Math.toRadians(targetAngle)) * radius);
      x1 += centerX;
      float y1 = -(float) (Math.cos(Math.toRadians(targetAngle)) * radius);
      y1 += centerY;

      Paint p = new Paint();
      p.setColor(Color.RED);
      p.setStyle(Paint.Style.STROKE);
      p.setPathEffect(new DashPathEffect(new float[]{7, 3}, 0));
      p.setStrokeWidth(2f);

      Path path = new Path();
      path.moveTo(centerX, centerY);
      path.lineTo(x1, y1);
      canvas.drawPath(path, p);
    }

    // draw pitch lines
    float r1 = radius * 0.9f;
    float r10 = radius * 0.8f;
    float r90 = radius * 0.7f;
    for (int i = scalaAngle1; i < scalaAngle2; ++i) {
      float ri = r1;

      if (i % 10 == 0) {
        if (i % 90 == 0) {
          ri = r90;
        } else {
          ri = r10;
        }
      }

      float angleRad = (float) Math.toRadians(i);
      float sin = (float) Math.sin(angleRad);
      float cos = (float) Math.cos(angleRad);
      float x1 = sin * ri;
      x1 += centerX;
      float y1 = -cos * ri;
      y1 += centerY;

      float x2 = sin * radius;
      x2 += centerX;
      float y2 = -cos * radius;
      y2 += centerY;
      canvas.drawLine(x1, y1, x2, y2, scalaPaint);
    }

    // draw camera
    if(camAngle!=null) {
      Matrix m = new Matrix();
      canvas.save();
      canvas.rotate(camAngle, centerX, centerY);
      m.setTranslate(centerX - (camIScaledImg.getWidth() / 2), centerY - (camIScaledImg.getHeight() / 2));
      canvas.drawBitmap(camIScaledImg, m, null);
      canvas.restore();
    }
  }

  private static Bitmap resize(Bitmap image, int maxSize) {
    return resize(image, maxSize, maxSize);
  }

  private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
    if (maxHeight > 0 && maxWidth > 0) {
      int width = image.getWidth();
      int height = image.getHeight();
      float ratioBitmap = (float) width / (float) height;
      float ratioMax = (float) maxWidth / (float) maxHeight;

      int finalWidth = maxWidth;
      int finalHeight = maxHeight;
      if (ratioMax > 1) {
        finalWidth = (int) ((float) maxHeight * ratioBitmap);
      } else {
        finalHeight = (int) ((float) maxWidth / ratioBitmap);
      }
      image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
      return image;
    } else {
      return image;
    }
  }
}

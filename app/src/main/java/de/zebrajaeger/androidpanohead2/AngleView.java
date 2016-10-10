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
  @Nullable
  private Float angle1 = 10f;
  @Nullable
  private Float angle2 = 30f;
  @Nullable
  private Float angleRed = 45f;

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
    pointerPaint = new Paint();
    pointerPaint.setColor(0xffaaaaaa);
    camOriginalImg = BitmapFactory.decodeResource(getResources(), R.drawable.cam2_0400);
  }

  public void setAngle(@Nullable Float angle1, @Nullable Float angle2) {
    this.angle1 = angle1;
    this.angle2 = angle2;
    updatePointer();
    postInvalidate();
  }

  public void setAngle1(@Nullable Float angle) {
    this.angle1 = angle;
    updatePointer();
    postInvalidate();
  }

  public void setAngle2(@Nullable Float angle) {
    this.angle1 = angle;
    updatePointer();
    postInvalidate();
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
    if (angle1 != null && angle2 != null) {
      Path path = new Path();
      float r1 = radius;

      path.moveTo(centerX, centerY);
      float x1 = (float) (Math.sin(Math.toRadians(angle1)) * r1);
      x1 += centerX;
      float y1 = -(float) (Math.cos(Math.toRadians(angle1)) * r1);
      y1 += centerY;
      path.lineTo(x1, y1);

      float x2 = (float) (Math.sin(Math.toRadians(angle2)) * r1);
      x2 += centerX;
      float y2 = -(float) (Math.cos(Math.toRadians(angle2)) * r1);
      y2 += centerY;
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
    Float angleBlack = null;
    if (angle1 != null || angle2 != null) {
      angleBlack = (angle1 + angle2) / 2f;
    } else if (angle1 != null) {
      angleBlack = angle1;
    } else if (angle2 != null) {
      angleBlack = angle2;
    }

    if (angleBlack != null) {
      float x1 = (float) (Math.sin(Math.toRadians(angleBlack)) * radius);
      x1 += centerX;
      float y1 = -(float) (Math.cos(Math.toRadians(angleBlack)) * radius);
      y1 += centerY;

      Paint p = new Paint();
      p.setColor(Color.GREEN);
      p.setStyle(Paint.Style.FILL);
      p.setStrokeWidth(5f);
      canvas.drawLine(centerX, centerY, x1, y1, p);
    }

    // draw red line
    if (angleRed != null) {
      float x1 = (float) (Math.sin(Math.toRadians(angleRed)) * radius);
      x1 += centerX;
      float y1 = -(float) (Math.cos(Math.toRadians(angleRed)) * radius);
      y1 += centerY;

      Paint p = new Paint();
      p.setColor(Color.RED);
      p.setStyle(Paint.Style.STROKE);
      p.setPathEffect(new DashPathEffect(new float[]{7, 3}, 0));
      p.setStrokeWidth(2f);

      Path path = new  Path();
      path.moveTo(centerX, centerY);
      path.lineTo(x1, y1);
      canvas.drawPath(path,p);
    }

    // draw pitch lines
    float r1 = radius * 0.9f;
    float r10 = radius * 0.8f;
    float r90 = radius * 0.7f;
    float d = (float) (Math.PI * 2f / 360f);
    float angle = 0;
    for (int i = 0; i < 360; ++i) {
      float ri = r1;

      if (i % 10 == 0) {
        if (i % 90 == 0) {
          ri = r90;
        } else {
          ri = r10;
        }
      }

      float x1 = (float) (Math.sin(angle) * ri);
      x1 += centerX;
      float y1 = -(float) (Math.cos(angle) * ri);
      y1 += centerY;

      float x2 = (float) (Math.sin(angle) * radius);
      x2 += centerX;
      float y2 = -(float) (Math.cos(angle) * radius);
      y2 += centerY;
      canvas.drawLine(x1, y1, x2, y2, scalaPaint);
      angle += d;
    }

    // draw camera
    Matrix m = new Matrix();
    canvas.save();
    canvas.rotate((angle1 + angle2) / 2.0f, centerX, centerY);
    m.setTranslate(centerX - (camIScaledImg.getWidth() / 2), centerY - (camIScaledImg.getHeight() / 2));
    canvas.drawBitmap(camIScaledImg, m, null);
    canvas.restore();
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

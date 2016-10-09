package de.zebrajaeger.androidpanohead2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
  private Path pointer;
  private Paint scalaPaint;
  private Paint pointerPaint;
  private Bitmap camOriginalImg;
  private Bitmap camIScaledImg;
  private float angle1 = 10;
  private float angle2 = 30;

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

  public void setAngle(float angle1, float angle2) {
    this.angle1 = angle1;
    this.angle2 = angle2;
    updatePointer();
    postInvalidate();
  }

  public void setAngle1(float angle) {
    this.angle1 = angle;
    updatePointer();
    postInvalidate();
  }

  public void setAngle2(float angle) {
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
    Path path = new Path();
    float r1 = radius;

    path.moveTo(centerX, centerY);
    float x1 = (float) (Math.sin( Math.toRadians(angle1)) * r1);
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
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.drawPath(pointer, pointerPaint);

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
      float y1 = (float) (Math.cos(angle) * ri);
      y1 += centerY;

      float x2 = (float) (Math.sin(angle) * radius);
      x2 += centerX;
      float y2 = (float) (Math.cos(angle) * radius);
      y2 += centerY;
      canvas.drawLine(x1, y1, x2, y2, scalaPaint);
      angle += d;
    }

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

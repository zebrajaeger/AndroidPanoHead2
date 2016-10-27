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
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import de.zebrajaeger.androidpanohead2.util.AngleRange;

import javax.annotation.Nonnull;


/**
 * Created by lars on 09.10.2016.
 */

public class AngleView extends View {
  boolean test = true;

  // common
  private int w;
  private int h;

  private float centerX;
  private float centerY;
  private float radius;

  // scala
  @Nonnull
  private AngleRange scalaRange = AngleRange.ofTwoAngles(0f, 360f);
  @Nonnull
  private Paint scalaPaint;
  @Nonnull
  private RectF scalaBounds;

  // target
  @Nullable
  private Float targetAngle = null;
  private Paint targetPaint;

  // cam
  private Bitmap camOriginalImg;
  private Bitmap camIScaledImg;
  private Paint camInnerPaint;
  private Paint camOuterPaint;
  @Nonnull
  private AngleRange camRange = test ? AngleRange.ofAngleAndFov(45f, 30f) : AngleRange.newEmpty();
  @Nullable
  private Path camFovPath;

  // pano
  private Paint panoInnerPaint;
  private Paint panoOuterPaint;
  @Nonnull
  private AngleRange panoRange = test ? AngleRange.ofAngleAndFov(90f, 70f) : AngleRange.newEmpty();
  @Nullable
  private Path panoFovPath;

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
    // scala
    scalaPaint = new Paint();
    scalaPaint.setColor(Color.BLACK);
    scalaPaint.setAntiAlias(true);

    // cam
    camInnerPaint = new Paint();
    camInnerPaint.setStyle(Paint.Style.FILL);
    camInnerPaint.setStrokeWidth(1);
    camInnerPaint.setAntiAlias(true);
    camInnerPaint.setColor(Color.GREEN);
    camInnerPaint.setAlpha(100);

    camOuterPaint = new Paint();
    camOuterPaint.setStyle(Paint.Style.STROKE);
    camOuterPaint.setStrokeWidth(1);
    camOuterPaint.setAntiAlias(true);
    camOuterPaint.setColor(Color.rgb(0,100,0));
    camOuterPaint.setAlpha(200);

    // pano
    panoInnerPaint = new Paint();
    panoInnerPaint.setStyle(Paint.Style.FILL);
    panoInnerPaint.setStrokeWidth(1);
    panoInnerPaint.setAntiAlias(true);
    panoInnerPaint.setColor(Color.BLUE);
    panoInnerPaint.setAlpha(100);

    panoOuterPaint = new Paint();
    panoOuterPaint.setStyle(Paint.Style.STROKE);
    panoOuterPaint.setStrokeWidth(1);
    panoOuterPaint.setAntiAlias(true);
    panoOuterPaint.setColor(Color.rgb(0,0,100));
    panoOuterPaint.setAlpha(200);

    targetPaint = new Paint();
    targetPaint.setColor(Color.RED);
    targetPaint.setStyle(Paint.Style.STROKE);
    targetPaint.setPathEffect(new DashPathEffect(new float[]{7, 3}, 0));
    targetPaint.setStrokeWidth(2f);

    camOriginalImg = BitmapFactory.decodeResource(getResources(), R.drawable.cam2_0400);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    this.w = w;
    this.h = h;
    super.onSizeChanged(w, h, oldw, oldh);
    centerX = w / 2f;
    centerY = h / 2f;
    radius = Math.min(centerX, centerY);
    scalaBounds = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    camIScaledImg = resize(camOriginalImg, (int) (radius / 2f));
    updateCamPath();
    updatePanoPath();
  }

  public void setScalaType(ScalaType type) {
    switch (type) {
      case HORIZONTAL:
        scalaRange = AngleRange.ofTwoAngles(0f, 360f);
        break;
      case VERTICAL:
        scalaRange = AngleRange.ofTwoAngles(0f, 180f);
        break;
      default:
        throw new UnsupportedOperationException("ScalaType '" + type + "' unknown");
    }
    postInvalidate();
  }

  public void addToTargetAngle(@Nullable Float addToTargetAngle) {
    if (targetAngle != null) {
      targetAngle += addToTargetAngle;
    }
  }

  public void setTargetAngle(@Nullable Float targetAngle) {
    if (isEqual(this.targetAngle, targetAngle)) {
      this.targetAngle = targetAngle;
      postInvalidate();
    }
  }

  public static boolean isEqual(Float f1, Float f2) {
    if (f1 == f2) {
      return true;
    } else if (f1 == null || f2 == null) {
      return false;
    } else {
      return f1.equals(f2);
    }
  }

  public void setCamFov(float fov) {
    if (camRange.isComplete() && isEqual(this.camRange.getAngle(), fov)) {
      camRange.setFov(fov);
      updateCamPath();
      postInvalidate();
    }
  }

  public void setCamAngle(float angle) {
    if (camRange.isComplete() && isEqual(this.camRange.getAngle(), angle)) {
      camRange.setAngle(angle);
      updateCamPath();
      postInvalidate();
    }
  }

  public void setPanoFov(float fov) {
    if (panoRange.isComplete() && isEqual(this.panoRange.getAngle(), fov)) {
      panoRange.setFov(fov);
      updatePanoPath();
      postInvalidate();
    }
  }

  public void setPanoAngle(float angle) {
    if (panoRange.isComplete() && isEqual(this.panoRange.getAngle(), angle)) {
      panoRange.setAngle(angle);
      updatePanoPath();
      postInvalidate();
    }
  }

  @Nullable
  public Float getTargetAngle() {
    return targetAngle;
  }


  private void updateCamPath() {
    if (camRange.isComplete()) {
      Path path = new Path();
      path.moveTo(centerX, centerY);
      path.arcTo(scalaBounds, camRange.getA1() - 90, camRange.getFov());
      path.close();

      camFovPath = path;
    } else {
      camFovPath = null;
    }
  }

  private void updatePanoPath() {
    if (panoRange.isComplete()) {
      Path path = new Path();
      path.moveTo(centerX, centerY);
      path.arcTo(scalaBounds, panoRange.getA1() - 90, panoRange.getFov());
      path.close();

      panoFovPath = path;
    } else {
      panoFovPath = null;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // draw pano view field if exists
    if (panoFovPath != null) {
      canvas.drawPath(panoFovPath, panoInnerPaint);
      canvas.drawPath(panoFovPath, panoOuterPaint);
    }

    // draw cam view field if exists
    if (camFovPath != null) {
      canvas.drawPath(camFovPath, camInnerPaint);
      canvas.drawPath(camFovPath, camOuterPaint);
    }

/*    // draw black line
    if (camRange.isComplete()) {
      float a = camRange.getSweepAngle();
      float x1 = centerX + (float) (Math.sin(Math.toRadians(a)) * radius);
      float y1 = centerY - (float) (Math.cos(Math.toRadians(a)) * radius);

      Paint p = new Paint();
      p.setColor(Color.GREEN);
      p.setStyle(Paint.Style.FILL);
      p.setStrokeWidth(3f);
      canvas.drawLine(centerX, centerY, x1, y1, p);
    }*/

    // draw red line
    if (targetAngle != null) {
      float x1 = (float) (Math.sin(Math.toRadians(targetAngle)) * radius);
      x1 += centerX;
      float y1 = -(float) (Math.cos(Math.toRadians(targetAngle)) * radius);
      y1 += centerY;

      Path path = new Path();
      path.moveTo(centerX, centerY);
      path.lineTo(x1, y1);
      canvas.drawPath(path, targetPaint);
    }

    if (scalaRange.isComplete()) {
      // draw pitch lines
      float r1 = radius * 0.9f;
      float r10 = radius * 0.8f;
      float r90 = radius * 0.7f;
      int to = scalaRange.getA2AsInt();
      for (int i = scalaRange.getA1AsInt(); i < to; ++i) {
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
    }

    // draw camera
    if (camRange.isComplete()) {
      Matrix m = new Matrix();
      canvas.save();
      canvas.rotate(camRange.getAngle(), centerX, centerY);
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

package com.euphor.paperpad.widgets;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BottomCropImage extends ImageView {

public BottomCropImage(Context context) {
    super(context);
    setup();
}

public BottomCropImage(Context context, AttributeSet attrs) {
    super(context, attrs);
    setup();
}

public BottomCropImage(Context context, AttributeSet attrs,
        int defStyle) {
    super(context, attrs, defStyle);
    setup();
}

private void setup() {
    setScaleType(ScaleType.MATRIX);
}

@Override
protected boolean setFrame(int l, int t, int r, int b) {
    Matrix matrix = getImageMatrix();

    float scale;
    int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
    int drawableWidth = getDrawable().getIntrinsicWidth();
    int drawableHeight = getDrawable().getIntrinsicHeight();

    //Get the scale 
    if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
        scale = (float) viewHeight / (float) drawableHeight;
    } else {
        scale = (float) viewWidth / (float) drawableWidth;
    }

    //Define the rect to take image portion from
    RectF drawableRect = new RectF(0, drawableHeight - (viewHeight / scale), drawableWidth, drawableHeight);
    RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
    matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);


    setImageMatrix(matrix);

    return super.setFrame(l, t, r, b);
}        

}
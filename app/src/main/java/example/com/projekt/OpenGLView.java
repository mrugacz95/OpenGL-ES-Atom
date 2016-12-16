package example.com.projekt;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;

public class OpenGLView extends GLSurfaceView {

    private OpenGLRenderer kpRenderer;
    private ScaleGestureDetector mScaleDetector;

    public OpenGLView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        kpRenderer = new OpenGLRenderer();
        this.setRenderer(kpRenderer);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        TOUCH_SCALE_FACTOR =  180.f/width; // pełen obrótu (radiany) na przesunięcie po całym ekranie
    }


    private float TOUCH_SCALE_FACTOR;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleDetector.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();
        if (e.getPointerCount() > 1) return true; //jeżeli wiecej niż 2 dotyki to nie obracaj
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                kpRenderer.setAngleX(kpRenderer.getmAngleX() + dx * TOUCH_SCALE_FACTOR);
                kpRenderer.setAngleY(kpRenderer.getmAngleY() - dy * TOUCH_SCALE_FACTOR);
                requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newDistance = kpRenderer.getDistance() * 1 / detector.getScaleFactor();
            // Don't let the object get too small or too large.
            newDistance = Math.max(5f, Math.min(newDistance, 20f));
            kpRenderer.setDistance(newDistance);
            invalidate();
            return true;
        }
    }
}
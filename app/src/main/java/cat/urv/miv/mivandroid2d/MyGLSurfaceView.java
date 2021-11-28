package cat.urv.miv.mivandroid2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private MyOpenGLRenderer mRenderer;
    private final int x_res;

    public MyGLSurfaceView(Context context) {
        super(context);
        this.x_res = this.getResources().getDisplayMetrics().widthPixels;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (mRenderer != null) {
                    // Ensure we call switchMode() on the OpenGL thread.
                    // queueEvent() is a method of GLSurfaceView that will do this for us.
                    queueEvent(() -> {
                        int x = (int)event.getX();
                        if (x > x_res/2) {
                            StateManager.displaceRight();
                        } else {
                            StateManager.displaceLeft();
                        }
                        StateManager.incrementTouches();
                    });
                }
            }
        }

        return super.onTouchEvent(event);
    }

    // Hides superclass method.
    public void setRenderer(MyOpenGLRenderer renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }
}
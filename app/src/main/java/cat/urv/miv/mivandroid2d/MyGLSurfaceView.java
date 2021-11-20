package cat.urv.miv.mivandroid2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private MyOpenGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (mRenderer != null) {
                    // Ensure we call switchMode() on the OpenGL thread.
                    // queueEvent() is a method of GLSurfaceView that will do this for us.
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("Touched!");
                            int x = (int)event.getX();
                            int y = (int)event.getY();
                            System.out.printf("Position x: "+x);
                            System.out.printf("Position y: "+y);
                            StateManager.incrementTouches();
                        }
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
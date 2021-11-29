package cat.urv.miv.mivandroid2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 *
 * Se extiende la clase GLSurfaceView para implementar el control tactil
 *
 */
public class MyOpenGLSurfaceView extends GLSurfaceView {
    private MyOpenGLRenderer mRenderer;
    private final int x_res;

    public MyOpenGLSurfaceView(Context context) {
        super(context);
        // Guardamos el ancho de la pantalla
        this.x_res = this.getResources().getDisplayMetrics().widthPixels;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (mRenderer != null) {
                    queueEvent(() -> {
                        // Obtenemos la posición pulsada
                        int x = (int)event.getX();

                        if (x > x_res/2) {
                            // Si se toca la parte derecha de la pantalla, el sprite se desplazara a ese lado
                            StateManager.moveRight();
                        } else {
                            // Izquierda
                            StateManager.moveLeft();
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
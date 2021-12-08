package cat.urv.miv.mivandroid2d;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        MyOpenGLSurfaceView view = new MyOpenGLSurfaceView(this);
        //
        view.setRenderer(new MyOpenGLRenderer(this));
        setContentView(view);
    }
}
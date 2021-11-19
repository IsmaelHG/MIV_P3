package cat.urv.miv.mivandroid2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import org.w3c.dom.Text;

public class MyOpenGLRenderer implements Renderer {
	// Practica 2
	private Square mario_square;
	private AnimationManager mario_character;
	// Practica 2



	private Square square;
	private int angle = 0;

	private Context context;

	public MyOpenGLRenderer(Context context){
		this.context = context;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Image Background color
		//gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);

		// Practica 2
		gl.glEnable(GL10.GL_TEXTURE_2D);

		mario_square = new Square();
		mario_character = new AnimationManager(gl, context, R.drawable.mario, R.raw.mario);
		mario_square.setAnimation(mario_character.getAnimation("walk"));

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();	

		drawCharacters(gl);
	}

	float displacement = 0;

	public void drawCharacters(GL10 gl) {
		gl.glPushMatrix();
		displacement = 0;
		gl.glTranslatef(-0.4f+displacement, -0.7f, 0.0f);
		gl.glScalef(-0.3f, 0.3f, 0.01f);

		gl.glTranslatef(-2f-displacement, 0, 0);
		mario_square.update(System.currentTimeMillis());
		mario_square.draw(gl);
		gl.glPopMatrix();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Define the Viewport
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable( GL10.GL_BLEND );
	}

}

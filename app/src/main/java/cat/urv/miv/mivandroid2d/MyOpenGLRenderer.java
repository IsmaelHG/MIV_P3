package cat.urv.miv.mivandroid2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class MyOpenGLRenderer implements Renderer {
	// Practica 2
	private Square mario_square;
	private AnimationManager mario_character;

	private TileMap map1, map2, map3, map4, map5;

	// Practica 2

	private final Context context;

	public MyOpenGLRenderer(Context context){
		this.context = context;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Practica 2
		gl.glEnable(GL10.GL_TEXTURE_2D);

		mario_square = new Square();
		mario_character = new AnimationManager(gl, context, R.drawable.mario, R.raw.mario);
		mario_square.setAnimation(mario_character.getAnimation("idle"));

		// Background
		map1 = new TileMap (gl, context, R.drawable.background_tiles, R.raw.tilemap1, 150);
		map2 = new TileMap (gl, context, R.drawable.background_tiles, R.raw.tilemap2, 100);
		map3 = new TileMap (gl, context, R.drawable.background_tiles, R.raw.tilemap3, 75);
		map4 = new TileMap (gl, context, R.drawable.background_tiles, R.raw.tilemap4, 15);

		map5 = new TileMap (gl, context, R.drawable.foreground_tiles, R.raw.tilemap5, 15);

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		StateManager.update_touches(mario_square, mario_character);
		
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();

		// Background
		map1.setDimensions(-1f, 0.8f, 0.2f, 0.2f);
		map1.update(System.currentTimeMillis(), true);
		map1.draw(0f);

		map2.setDimensions( -1f, 0.47f, 0.13f, 0.13f);
		map2.update(System.currentTimeMillis(), true);
		map2.draw(0f);

		map3.setDimensions( -1f, -0.09f, 0.17f, 0.17f);
		map3.update(System.currentTimeMillis(), true);
		map3.draw(0f);

		map4.setDimensions( -1f, 0.2f, 0.17f, 0.17f);
		map4.update(System.currentTimeMillis(), true);
		map4.draw(0f);

		map5.setDimensions( -1f, -0.9f, 0.3f, 0.1f);
		map5.update(System.currentTimeMillis(), true);
		map5.draw(0f);

		drawCharacters(gl);
	}

	float displacement = 0;

	public void drawCharacters(GL10 gl) {
		gl.glPushMatrix();
		displacement = 0;
		gl.glTranslatef(-0.4f+displacement, -0.7f, 0.0f);
		gl.glScalef(-0.3f, 0.3f, 0.01f);

		gl.glTranslatef(-2f-displacement, 0.6f, 0);
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

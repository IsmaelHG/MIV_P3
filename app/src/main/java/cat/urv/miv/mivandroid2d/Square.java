package cat.urv.miv.mivandroid2d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {
	private float vertices[] = {-1.0f, -1.0f, 0.0f, //lower left
			-1.0f,  1.0f, 0.0f, //upper left
			1.0f,  1.0f, 0.0f, //upper right
			1.0f, -1.0f, 0.0f}; //lower right
	private short faces[] = { 0, 1, 2, 0, 2, 3 };

	// Our vertex buffer.
	private FloatBuffer vertexBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;

	// Our color buffer.
	private FloatBuffer colorBuffer;

	// Our texCoord buffer.
	private FloatBuffer texCoordBuffer;

	private Texture texture;

	private Animation animation;


	private boolean colorEnabled = false;
	private boolean textureEnabled = false;

	public Square() {
		//Move the vertices list into a buffer
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		//Move the faces list into a buffer
		ByteBuffer ibb = ByteBuffer.allocateDirect(faces.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(faces);
		indexBuffer.position(0);
	}

	//public void setColor(float r, float g, float b) {
	//}

	public void setColor(float []colors) {
		//Move the color list into a buffer
		ByteBuffer vbb = ByteBuffer.allocateDirect(colors.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		colorBuffer = vbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);

		colorEnabled = true;
	}

	public void enableColor() { colorEnabled = true; }
	public void disableColor() { colorEnabled = false; }

	public void enableTexture() { textureEnabled = true; }
	public void disableTexture() { textureEnabled = false; }

	public void setTexture(float []textcoords, Texture texture) {
		this.texture = texture;

		ByteBuffer vbb = ByteBuffer.allocateDirect(textcoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		texCoordBuffer = vbb.asFloatBuffer();
		texCoordBuffer.put(textcoords);
		texCoordBuffer.position(0);

		textureEnabled = true;
	}

	public void setAnimation(Animation animation) {

		this.animation = animation;
		this.setTexture(this.animation.getCurrentFrame(), this.animation.getTexture());
	}

	public void update(long t){
		if (animation!=null) {
			animation.update(t);
			this.setTexture(animation.getCurrentFrame(), animation.getTexture());
		}
	}

	public void draw(GL10 gl) {
		//gl.glColor4f(r, g, b, 0.0f);

		// Enabled the vertices buffer for writing and to be used during rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);


		if (colorEnabled) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		}

		if (textureEnabled) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}

		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		if (colorEnabled) {
			gl.glColorPointer(4,GL10.GL_FLOAT,0,colorBuffer);
		}

		if (textureEnabled) {
			gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,texCoordBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D,texture.getTexture()[0]);
		}

		gl.glDrawElements(GL10.GL_TRIANGLES, faces.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);


		if (colorEnabled) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}

		if (textureEnabled) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public Animation getAnimation() {
		return animation;
	}
}

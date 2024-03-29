package cat.urv.miv.mivandroid2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public class TextureAtlas {

    // Texture pointer
    private final int[] texture = new int[1];

    public TextureAtlas(GL10 gl, Context context, int resource_id) {

        // Se consigue la textura de la carpeta resource
        InputStream is = context.getResources().openRawResource(resource_id);
        Bitmap bitmap;

        try {
            //BitmapFactory is an Android graphics utility for images
            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }


        //AFEGIR CODi AQUI: CREAR TEXTURA I ASSIGNAR-LI LA IMATGE
        //Generate and fill the texture with the image

        gl.glGenTextures(1, texture, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_CLAMP_TO_EDGE, GL10.GL_REPEAT);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //Clean up
        bitmap.recycle();
    }

    public int[] getTexture() {
        return texture;
    }
}
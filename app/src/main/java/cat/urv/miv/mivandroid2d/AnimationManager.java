package cat.urv.miv.mivandroid2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

public class AnimationManager {

    // Estructura de datos interna para manejar las animaciones guardadas
    private final HashMap<String, Animation> animations;

    public AnimationManager(GL10 gl, Context context, int image_id, int text_id) {

        TextureAtlas texture = new TextureAtlas(gl, context, image_id);
        this.animations = new HashMap<>();

        // Obtenemos el tamaño del sprite
        InputStream is = context.getResources().openRawResource(image_id);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        int height = mBitmap.getHeight();
        int width = mBitmap.getWidth();

        // Leemos el fichero de animaciones
        InputStream tFile = context.getResources().openRawResource(text_id);
        BufferedReader atlasData = new BufferedReader(new InputStreamReader(tFile));
        try {
            for (String line = atlasData.readLine(); line != null; line = atlasData.readLine()) {
                String[] coords = line.split(" ");
                if (coords.length == 6) {

                    if (!animations.containsKey(coords[0])) {
                        // Añadimos la animación (siempre que no exista)
                        animations.put(coords[0], new Animation(texture));
                    }

                    // Procesamos el sprite correspondiente al frame
                    float[] coordinates = {
                            Float.parseFloat(coords[2]) / width, 1 - (Float.parseFloat(coords[3])) / height, //B
                            Float.parseFloat(coords[2]) / width, 1 - (Float.parseFloat(coords[5]) + Float.parseFloat(coords[3])) / height, // A
                            (Float.parseFloat(coords[2]) + Float.parseFloat(coords[4]) - 1) / width, 1 - (Float.parseFloat(coords[5]) + Float.parseFloat(coords[3])) / height, //D
                            (Float.parseFloat(coords[2]) + Float.parseFloat(coords[4]) - 1) / width, 1 - (Float.parseFloat(coords[3])) / height //C
                    };

                    // Añadimos el frame de la animación a la lista interna de la clase Animation
                    animations.get(coords[0]).addFrame(coordinates);

                } else {
                    System.out.println("La linea ' " + line + " ' " + "es incorrecta");
                }
            }

        } catch (IOException e) {
            System.out.println("No se puede leer el fichero de animaciones");
        }

    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

}

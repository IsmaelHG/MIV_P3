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

    private final HashMap<String, Animation> animations;


    public AnimationManager(GL10 gl, Context context, int image_id, int text_id) {

        TextureAtlas texture = new TextureAtlas(gl, context, image_id);
        this.animations = new HashMap<>();
        /* Code from texture atlas but creating new animation for each name */

        /* Get the size of the image.
         */
        InputStream is = context.getResources().openRawResource(image_id);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        int height = mBitmap.getHeight();
        int width = mBitmap.getWidth();

        /* Read text file and create structure passing each images' coordinates
        to [0,1].
         */
        InputStream tFile = context.getResources().openRawResource(text_id);
        BufferedReader atlasData = new BufferedReader(new InputStreamReader(tFile));
        String line;
        try {
            while ((line = atlasData.readLine()) != null) {
                String [] parts = line.split("\\s+");
                if (parts.length == 6) {

                    // Add animation if it does not exist.
                    if (!animations.containsKey(parts[0])) {
                        animations.put(parts[0], new Animation(texture));
                    }
                    float[] coordinates = {
                            Float.parseFloat(parts[2]) / width, 1 - (Float.parseFloat(parts[3])) / height, //B
                            Float.parseFloat(parts[2]) / width, 1 - (Float.parseFloat(parts[5]) + Float.parseFloat(parts[3])) / height, // A
                            (Float.parseFloat(parts[2]) + Float.parseFloat(parts[4]) - 1) / width, 1 - (Float.parseFloat(parts[5]) + Float.parseFloat(parts[3])) / height, //D
                            (Float.parseFloat(parts[2]) + Float.parseFloat(parts[4]) - 1) / width, 1 - (Float.parseFloat(parts[3])) / height //C
                    };

                    animations.get(parts[0]).addFrame(coordinates);

                }
            }

        }
        catch (IOException e){
            System.out.println("Error reading atlas txt file");
        }

    }

    public Animation getAnimation(String name){
        return animations.get(name);
    }

}

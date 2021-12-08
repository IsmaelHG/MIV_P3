package cat.urv.miv.mivandroid2d;

import java.util.ArrayList;

public class Animation {

    static double DEFAULT_SPEED = 100;

    // Conjunto de sprites
    private final ArrayList<float[]> frames = new ArrayList<>();

    private final double speed;
    private final TextureAtlas texture;
    private int numFrames = 0;
    private int currentFrame;
    private long lastupdate;


    public Animation(TextureAtlas text) {
        this.texture = text;
        this.speed = DEFAULT_SPEED;
        this.lastupdate = System.currentTimeMillis();
    }

    public void addFrame(float[] coords) {
        // Añadimos el frame al array
        frames.add(coords);
        numFrames++;
    }

    public float[] getCurrentFrame() {
        return frames.get(currentFrame);
    }

    public void update(Long t) {
        if ((t - lastupdate) >= (speed - Math.abs(SpriteManager.getDisplacement() * 10000))) {
            lastupdate = t;
            // Mostramos el siguiente sprite
            currentFrame = (currentFrame + 1) % numFrames;
        }
    }

    public TextureAtlas getTexture() {
        return texture;
    }
}

package cat.urv.miv.mivandroid2d;

import java.util.ArrayList;

public class Animation {

    static double DEFAULT_SPEED=50;

    private final ArrayList<float[]> frames = new ArrayList<>();
    private final double speed;
    private int numFrames=0;
    private int currentFrame;
    private long lastupdate;
    private final TextureAtlas texture;


    public Animation(TextureAtlas text){
        this.texture = text;
        this.speed=DEFAULT_SPEED;
        this.lastupdate = System.currentTimeMillis();
    }

    public boolean addFrame(float[] coords) {
        boolean ret = frames.add(coords);
        numFrames++;
        return ret;
    }

    public float[] getFrame(int i){
        if ( i > 0 && i < numFrames ) return frames.get(i);
        else return null;
    }

    public float[] getCurrentFrame(){
        return frames.get(currentFrame);
    }

    public float[] nextFrame(){
        currentFrame = (currentFrame + 1)%numFrames;
        return frames.get(currentFrame);
    }

    public float displacement_to_speed_factor = 1E4f;
    public boolean touches_active = false;

    public void enable_touches() {touches_active=true;}

    public void disable_touches() {touches_active=false;}

    public void update(Long t){
        if ((t-lastupdate)>=(touches_active
                ?speed-StateManager.getDisplacement()*displacement_to_speed_factor
                :speed)) {
            lastupdate=t;
            currentFrame = (currentFrame + 1)%numFrames;
        }
    }

    public TextureAtlas getTexture() {
        return texture;
    }
}

package cat.urv.miv.mivandroid2d;

import android.content.Context;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


enum LOOP_TYPES {SWEPT, PINGPONG};

public class Animation {

    static double DEFAULT_SPEED=50;

    private GL10 gl;
    private Context context;
    private int image_id;
    private int text_id;
    private String name;
    private ArrayList<float[]> frames = new ArrayList<>();
    private double speed;
    private int numFrames;
    private int currentFrame;
    private LOOP_TYPES loop;
    private long lastupdate;
    private Texture texture;


    public Animation  (GL10 gl, Context context, Texture text, String name, LOOP_TYPES l){
        this.gl = gl;
        this.context = context;
        this.image_id = image_id;
        this.text_id = text_id;
        this.name = name;
        this.loop = l;
        this.numFrames=0;
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
        switch (loop){
            case SWEPT: currentFrame = (currentFrame + 1)%numFrames; break;
            case PINGPONG: if (++currentFrame==numFrames) currentFrame=0; break;
        }
        return frames.get(currentFrame);
    }

    public float displacement_to_speed_factor = 1E4f;
    public boolean touches_active = false;

    public void activate_touches() {touches_active=true;}

    public void supress_touches() {touches_active=false;}

    public void update(Long t){
        if ((t-lastupdate)>=(touches_active
                ?speed-StateManager.getDisplacement()*displacement_to_speed_factor
                :speed)) {
            lastupdate=t;
            switch (loop){
                case SWEPT: currentFrame = (currentFrame + 1)%numFrames; break;
                case PINGPONG: if (++currentFrame==numFrames) currentFrame=0; break;
            }
        }
    }

    public double getSpeed() {
        return speed;
    }

    public LOOP_TYPES getLoop() {
        return loop;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setLoop(LOOP_TYPES loop) {
        this.loop = loop;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public ArrayList<float[]> getFrames() {
        return frames;
    }

    public String toString(){
        String ret = "Name: "+this.name+"\n";
        for (float[] f : frames){
            for (float c : f) {
                ret=ret+" " + c;
            }
            ret = ret + "\n";
        }
        return ret;
    }
}

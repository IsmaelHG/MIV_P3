package cat.urv.miv.mivandroid2d;

public class StateManager {

    private static float touch_counter = 0;
    private static float total_touches = 0;
    private static float lastupdate = System.nanoTime();
    private static final double speed = 1E8;
    private static float displacement = 0;

    public static void update_touches(){
        float t = System.nanoTime();
        //System.out.println(t);
        //System.out.println("displacement before: "+displacement);
        if ((t-lastupdate)>=speed && touch_counter>0) {
            lastupdate=t;
            float rt = touch_counter/150;
            touch_counter = 0;
            //System.out.println("Got "+rt);
            displacement = rt;
        }
        else {
            //Progressive slow down
            displacement -= 0.00005;
            if (displacement<0) displacement = 0;
        }
        //System.out.println("displacement after: "+displacement);
    }

    public static float getDisplacement() {
        return displacement;
    }

    public static void incrementTouches(){

        touch_counter++;
        total_touches++;
        //System.out.println("Touched! "+touch_counter+" touches");
    }

    public static float getTotal_touches() {
        return total_touches;
    }
}

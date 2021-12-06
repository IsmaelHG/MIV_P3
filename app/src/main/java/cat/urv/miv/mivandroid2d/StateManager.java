package cat.urv.miv.mivandroid2d;

public class StateManager {

    private static float touchCount = 0;
    private static float touchTotal = 0;
    private static float lastupdate = System.nanoTime();
    private static final double speed = 1E8;
    private static double displacement = 0;

    private static boolean touch_left = false;
    private static boolean touch_right = false;

    public static void update_touches(Square character, AnimationManager character_animation){
        float t = System.nanoTime();

        if ((t-lastupdate)>=speed && touchCount >0) {
            lastupdate=t;
            double rt = touchCount/150;
            touchCount = 0;

            // Cuando se toque la pantalla, el sprite tendra una animación y se desplazara al lado seleccionado.
            if (touch_left) {
                displacement = -rt;
                character.setAnimation(character_animation.getAnimation("walk"));
            }
            if (touch_right) {
                displacement = rt;
                character.setAnimation(character_animation.getAnimation("walk"));
            }
        }
        else {
            // Si no han habido nuevos toques, el sprite reducira su velocidad hasta pararse
            if (touch_left) {
                displacement += 0.00003;
                // Cuando el sprite se haya "parado", mostraremos su animación idle
                if (displacement>=0) {displacement = 0; character.setAnimation(character_animation.getAnimation("idle"));}
            }
            if (touch_right) {
                displacement -= 0.00003;
                if (displacement<=0) {displacement = 0; character.setAnimation(character_animation.getAnimation("idle"));}
            }
        }
    }

    public static double getDisplacement() {
        return displacement;
    }

    public static void incrementTouches(){

        touchCount++;
        touchTotal++;
    }

    public static void moveLeft() {
        touch_right = false;
        touch_left = true;
    }

    public static void moveRight() {
        touch_left = false;
        touch_right = true;
    }

    public static float getTouchTotal() {
        return touchTotal;
    }
}

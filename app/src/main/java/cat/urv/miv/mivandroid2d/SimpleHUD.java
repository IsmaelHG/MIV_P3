package cat.urv.miv.mivandroid2d;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

public class SimpleHUD {

    private final Square info_square, framework_square;
    private final FontAtlas font_atlas1, font_atlas2, font_atlas3;


    public SimpleHUD(Context context, GL10 gl, int font_id, int font_image_id) {

        info_square = new Square();
        float[] info_square_colors = new float[]{1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f,
                1.0f, 0.0f, 0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f};
        info_square.setColor(info_square_colors);
        framework_square = new Square();
        float[] framework_square_colors = new float[]{1.0f, 1.0f, 1.0f, 0.75f, 1.0f, 1.0f, 1.0f, 0.75f,
                1.0f, 1.0f, 1.0f, 0.75f, 1.0f, 1.0f, 1.0f, 0.75f};
        framework_square.setColor(framework_square_colors);

        font_atlas1 = new FontAtlas(context, gl, font_id, font_image_id);
        font_atlas2 = new FontAtlas(context, gl, font_id, font_image_id);
        font_atlas3 = new FontAtlas(context, gl, font_id, font_image_id);

    }

    public void draw (GL10 gl){
        drawGameInfo(gl);
        drawGameStats(gl);
    }

    public void drawGameInfo (GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0.45f, 0.45f, 0);
        gl.glScalef(0.5f, 0.35f, 1);
        framework_square.draw(gl);
        gl.glScalef(0.9f, 0.9f, 1);
        info_square.draw(gl);
        gl.glPopMatrix();
    }


    public void drawGameStats(GL10 gl){

        gl.glPushMatrix();

        gl.glTranslatef(0.04f, 0.67f, 0);
        font_atlas1.drawString("GAME STATS", 0.05f, 0.05f);
        gl.glTranslatef(0.03f, -0.2f, 0);
        String touch_string = "TOUCHES: "+(int)StateManager.getTotal_touches();
        font_atlas2.drawString(touch_string, 0.035f, 0.035f);
        gl.glTranslatef(0, -0.15f, 0);
        font_atlas3.drawString("DISTANCE: X", 0.035f, 0.035f);

        gl.glPopMatrix();
    }
}

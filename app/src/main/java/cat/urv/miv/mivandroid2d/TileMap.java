package cat.urv.miv.mivandroid2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class TileMap {

    private final GL10 gl;
    private final Context context;
    private final int image_id;
    private final int text_id;
    private int lineNumber;
    private int lineSize;
    private Square[][] tilemap;

    // Atributes for paralax
    private final double speed;
    private float paralaxDisplacement=0;
    private double lastParalaxDisplacement;

    public TileMap(GL10 gl, Context context, int image_id, int text_id, double speed){
        this.gl = gl;
        this.context = context;
        this.image_id = image_id;
        this.text_id = text_id;
        this.speed = speed;
        lastParalaxDisplacement=System.currentTimeMillis();
        // Read tilemap file.
        readTileMapData();


    }

    public void readTileMapData () {

        Texture texture = new Texture(this.gl, this.context, this.image_id);
        /* Get the size of the image.
         */
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), image_id);
        int height = mBitmap.getHeight();
        int width = mBitmap.getWidth();

        /* Read text file and create structure passing each images' coordinates
        to [0,1].
         */
        InputStream tFile = context.getResources().openRawResource(text_id);
        BufferedReader tilemapData = new BufferedReader(new InputStreamReader(tFile));
        String line;
        try {

            // Extract tile size.
            line = tilemapData.readLine();
            String[] parts = line.split("\\s+");
            int tileWidth = Integer.parseInt(parts[0]);
            int tileHeight = Integer.parseInt(parts[1]);
            int tilesPerRow = width/ tileWidth;
            line = tilemapData.readLine();
            parts = line.split("\\s+");
            lineSize = Integer.parseInt(parts[0]);
            lineNumber = Integer.parseInt(parts[1]);
            tilemap = new Square[lineNumber][lineSize];

            for (int i=0; i<lineNumber; i++){
                line = tilemapData.readLine();
                parts = line.split("\\s+");
                for (int j = 0; j<lineSize; j++) {
                    int number = Integer.parseInt(parts[j]);

                    // Get tile position
                    int row = number/tilesPerRow;
                    int column = number % tilesPerRow;
                    tilemap[i][j] = new Square();
                    float[] coords = new float [] { column* tileWidth /(float)width, ((row+1)* tileHeight -1)/(float)height,
                            column* tileWidth /(float)width, ((row)* tileHeight)/(float)height,
                            ((column+1)* tileWidth -1)/(float)width, ((row)* tileHeight)/(float)height,
                            ((column+1)* tileWidth -1)/(float)width, ((row+1)* tileHeight -1)/(float)height};
                    tilemap[i][j].setTexture(coords, texture);

                }
            }
        } catch (java.io.IOException ioe) {
            System.out.println("ERROR :: Reading tilemap text file.");
        }
    }


    float vwidth;
    float scx;
    float scy;
    float displx;
    float disply;
    private float secondary_drawer_displacement = 1;

    public void setDimensions(float displx, float disply, float scx, float scy){
        this.scx = scx;
        this.scy = scy;
        this.displx = displx;
        this.disply = disply;
    }

    public void draw(float z){

        int[] viewport = new int[4];
        gl.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
        vwidth =viewport[2];
        gl.glPushMatrix();
        gl.glTranslatef(displx+paralaxDisplacement,disply,z);
        gl.glScalef(scx, scy, 0);
        for (int i=0; i<lineNumber; i++){
            gl.glPushMatrix();
            for (int j = 0; j<lineSize; j++){
                tilemap[i][j].draw(gl);
                gl.glTranslatef(2, 0, 0) ;
            }
            gl.glPopMatrix();
            gl.glTranslatef(0,-2,0);
        }
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(displx+secondary_drawer_displacement,disply,z);
        gl.glScalef(scx, scy, 0);
        for (int i=0; i<lineNumber; i++){
            gl.glPushMatrix();
            for (int j = 0; j<lineSize; j++){
                tilemap[i][j].draw(gl);
                gl.glTranslatef(2, 0, 0) ;
            }
            gl.glPopMatrix();
            gl.glTranslatef(0,-2,0);
        }
        gl.glPopMatrix();
    }

    public void update(double ctime, boolean touches){

        // Update only based in time
        float base_displacement = 0.0f;
        if (!touches) {
            if ((ctime - lastParalaxDisplacement) > speed) {
                paralaxDisplacement = paralaxDisplacement - base_displacement;
                lastParalaxDisplacement = ctime;
            }
            if (paralaxDisplacement == -2 * lineSize) paralaxDisplacement = 0;
        }
        else {
            float displacement = StateManager.getDisplacement();
            //System.out.println(displacement+"displacement");
            if ((ctime - lastParalaxDisplacement) > speed) {
                float displacement_rate = base_displacement + displacement*50/(float)speed;
                //System.out.println("Displacement rate: "+displacement_rate);
                paralaxDisplacement = paralaxDisplacement - displacement_rate;
                secondary_drawer_displacement = secondary_drawer_displacement - displacement_rate;
                lastParalaxDisplacement = ctime;
            }
            else  {
                float displacement_rate = displacement*50/(float)speed;
                paralaxDisplacement = paralaxDisplacement - displacement_rate;
                secondary_drawer_displacement = secondary_drawer_displacement - displacement_rate;
            }

            if ((paralaxDisplacement <= (-(scx*lineSize*2f)+2f+scx)) && (paralaxDisplacement < 0)) {
                System.out.println("TRIGGER! "+paralaxDisplacement);
                System.out.println(" "+(-(scx*lineSize*2f)+2f+scx));
                System.out.println(" "+(-scx*lineSize*2f+2f+scx));
                secondary_drawer_displacement = -scx*lineSize*2f+2f+scx;
                paralaxDisplacement = 2f+scx;
            }
            if (secondary_drawer_displacement > 0) {
                System.out.println("TRIGGER 2! "+paralaxDisplacement);
                secondary_drawer_displacement = -2.5f;
                paralaxDisplacement = 0;
            }
            if (lineSize == 9) System.out.println("displaced "+paralaxDisplacement);
        }

    }

}
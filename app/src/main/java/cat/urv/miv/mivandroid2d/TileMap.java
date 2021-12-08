package cat.urv.miv.mivandroid2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class TileMap {

    private final GL10 gl;
    private final Context context;
    private final int image_id;
    private final int text_id;
    // Parallax displacement
    private final double speed;
    float vwidth, scx, scy, displ_x, displ_y;
    private int tileCols;
    private int tileRows;
    // Mapa de baldosas
    private Square[][] tilemap;
    private double paralaxDispl = 0;
    private double lastParalaxDisplacement;
    private double paralaxDispl_secondary = 1;


    public TileMap(GL10 gl, Context context, int image_id, int text_id, double speed) {
        this.gl = gl;
        this.context = context;
        this.image_id = image_id;
        this.text_id = text_id;
        this.speed = speed;
        lastParalaxDisplacement = System.currentTimeMillis();
        // Read file.
        readTileMapFile();


    }

    public void readTileMapFile() {

        TextureAtlas texture = new TextureAtlas(this.gl, this.context, this.image_id);
        // Obtenemos el tamaño de la textura
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), image_id);
        int height = mBitmap.getHeight();
        int width = mBitmap.getWidth();

        // Leemos el fichero de baldosas
        InputStream tFile = context.getResources().openRawResource(text_id);
        BufferedReader tilemapData = new BufferedReader(new InputStreamReader(tFile));
        String line;
        try {

            // La primera linea del fichero contiene la altura/anchura de la baldosa
            line = tilemapData.readLine();
            String[] parts = line.split(" ");
            int tileWidth = Integer.parseInt(parts[0]);
            int tileHeight = Integer.parseInt(parts[1]);
            // Numero maximo de baldosas que se pueden mostrar horizontalmente en pantalla
            int Rows = width / tileWidth;

            // La segunda contiene el tamaño del tilemap
            line = tilemapData.readLine();
            parts = line.split(" ");
            tileRows = Integer.parseInt(parts[0]);
            tileCols = Integer.parseInt(parts[1]);
            tilemap = new Square[tileCols][tileRows];

            for (int i = 0; i < tileCols; i++) {
                line = tilemapData.readLine();
                parts = line.split(" ");
                for (int j = 0; j < tileRows; j++) {
                    int number = Integer.parseInt(parts[j]);

                    // Obtenemos la posición en pantalla
                    int row = number / Rows;
                    int column = number % Rows;

                    // Coordenadas textura tile
                    float[] coordinates = new float[]{column * tileWidth / (float) width, ((row + 1) * tileHeight - 1) / (float) height,
                            column * tileWidth / (float) width, ((row) * tileHeight) / (float) height,
                            ((column + 1) * tileWidth - 1) / (float) width, ((row) * tileHeight) / (float) height,
                            ((column + 1) * tileWidth - 1) / (float) width, ((row + 1) * tileHeight - 1) / (float) height};
                    // Insertamos la textura
                    tilemap[i][j] = new Square();
                    tilemap[i][j].setTexture(coordinates, texture);

                }
            }
        } catch (IOException e) {
            System.out.println("No se puede leer el fichero de animaciones");
        }
    }

    public void setDimensions(float displx, float disply, float scx, float scy) {
        this.scx = scx;
        this.scy = scy;
        this.displ_x = displx;
        this.displ_y = disply;
    }

    public void draw(float z) {

        int[] viewport = new int[4];
        gl.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
        vwidth = viewport[2];
        // Dibujamos el tilemap (hay un duplicado para poder implementar el scroll parallax)

        gl.glPushMatrix();
        gl.glTranslatef((float) (displ_x + paralaxDispl), displ_y, z);
        gl.glScalef(scx, scy, 0);
        for (int i = 0; i < tileCols; i++) {
            gl.glPushMatrix();
            for (int j = 0; j < tileRows; j++) {
                tilemap[i][j].draw(gl);
                gl.glTranslatef(2, 0, 0);
            }
            gl.glPopMatrix();
            gl.glTranslatef(0, -2, 0);
        }
        gl.glPopMatrix();

        // Tilemap secundario
        gl.glPushMatrix();
        gl.glTranslatef((float) (displ_x + paralaxDispl_secondary), displ_y, z);
        gl.glScalef(scx, scy, 0);
        for (int i = 0; i < tileCols; i++) {
            gl.glPushMatrix();
            for (int j = 0; j < tileRows; j++) {
                tilemap[i][j].draw(gl);
                gl.glTranslatef(2, 0, 0);
            }
            gl.glPopMatrix();
            gl.glTranslatef(0, -2, 0);
        }
        gl.glPopMatrix();
    }

    public void update(double ctime) {
        float base_displacement = 0.0f;
        double displacement = SpriteManager.getDisplacement();
        // Actualizamos el desplazamiento relativo del tilemap
        if ((ctime - lastParalaxDisplacement) > speed) {
            double displacement_rate = base_displacement + displacement * 50 / (float) speed;
            paralaxDispl = paralaxDispl - displacement_rate;
            paralaxDispl_secondary = paralaxDispl_secondary - displacement_rate;
            lastParalaxDisplacement = ctime;
        } else {
            double displacement_rate = displacement * 50 / (float) speed;
            paralaxDispl = paralaxDispl - displacement_rate;
            paralaxDispl_secondary = paralaxDispl_secondary - displacement_rate;
        }

        // Si se produce un "hueco" en pantalla, se desplazaran los dos tilemap sin que el usuario lo note


        // Derecha
        if ((paralaxDispl <= (-(scx * tileRows * 2f) + 2f + scx)) && (paralaxDispl < 0)) {
            paralaxDispl_secondary = -scx * tileRows * 2f + 2f + scx;
            paralaxDispl = 2f + scx;
        }
        // Izquierda
        if (paralaxDispl_secondary > 0) {
            paralaxDispl_secondary = -scx * tileRows * 2f + scx;
            paralaxDispl = 0;
        }

    }

}
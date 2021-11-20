package cat.urv.miv.mivandroid2d;

public class FontData {

    private int ID;
    private char character;
    private float y;
    private float x;
    private float width;
    private float height;

    public FontData(int ID, char character, float y, float x, float width, float height) {
        this.ID = ID;
        this.character = character;
        this.y = y;
        this.x = x;
        this.width = width;
        this.height = height;

    }

    @Override
    public String toString() {
        return "FontData{" +
                "ID=" + ID +
                ", character=" + character +
                ", y=" + y +
                ", x=" + x +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

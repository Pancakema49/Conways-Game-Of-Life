import java.awt.*;
import java.security.PublicKey;

public class Square {
    private int X;
    private int Y;
    private int Width;
    private int Height;
    private boolean Alive = false;

    public Square(int x, int y, boolean alive, int width, int height){
        X = x;
        Y = y;
        Alive = alive;
        Width = width;
        Height = height;
    }

    public boolean isAlive() {
        return Alive;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public void setAlive(boolean alive) {
        Alive = alive;
    }
    public void DrawSquare(Graphics g){
        if (Alive){
            g.setColor(new Color(255,255,255));
        }
        else{
            g.setColor(new Color(18,18,18));
        }
        g.fillRect(X, Y, Width, Height);
        g.setColor(new Color(30,30,30));
        g.drawRect(X, Y, Width, Height);
    }
}

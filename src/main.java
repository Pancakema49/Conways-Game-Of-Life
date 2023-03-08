import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.time.Duration;
import java.time.LocalTime;

public class main {

    //0,0 is for some reason offset by this
    final static int XOffset = 8;
    final static int YOffset = 31;
    final static int Width = 10;
    final static int Height = 10;
    final static int UpdateSpeed = 500;
    static JFrame Frame;
    static Square[][] Squares;
    static boolean tick = true;



    public static void main(String[] args) throws InterruptedException {
        InitGame();

        LocalTime before;
        LocalTime after;
        Duration diff = Duration.ZERO;
        while (true){
            before = LocalTime.now();
            Render();
            after = LocalTime.now();
            diff = diff.plus(Duration.between(before, after));
            if (diff.toMillis() >= UpdateSpeed && tick){
                Update();
                diff = Duration.ZERO;
            }

        }
    }
    private static void Render() throws InterruptedException {

        BufferStrategy strategy = Frame.getBufferStrategy();
        do{
            do {
                Graphics g = strategy.getDrawGraphics();
                {
                    Draw(g);
                }
                g.dispose();
            }while (strategy.contentsRestored());
            strategy.show();
        }while (strategy.contentsLost());

    }
    private static void Update(){
        Square[][] nextSquares = new Square[Squares.length][Squares[0].length];
        for (int y = 0; y < Squares.length; y++) {
            for (int x = 0; x < Squares[0].length; x++) {
                nextSquares[y][x] = new Square(Squares[y][x].getX(),Squares[y][x].getY(),Squares[y][x].isAlive(),Squares[y][x].getWidth(),Squares[y][x].getHeight());
            }
        }

        for (int y = 0; y < Squares[0].length; y++) {
            for (int x = 0; x < Squares[1].length; x++) {
                int aliveBuddies = CheckBuddies(x,y);
                if (Squares[x][y].isAlive()){
                    switch (aliveBuddies){
                        case 0, 1, 4, 5, 6, 7, 8 -> nextSquares[x][y].setAlive(false);
                    }
                }
                else {
                    if (aliveBuddies == 3) {
                        nextSquares[x][y].setAlive(true);
                    }
                }
            }
        }
        for (int i = 0; i < Squares.length; i++) {
            for (int j = 0; j < Squares[0].length; j++) {
                Squares[i][j] = new Square(nextSquares[i][j].getX(),nextSquares[i][j].getY(),nextSquares[i][j].isAlive(),nextSquares[i][j].getWidth(),nextSquares[i][j].getHeight());
            }
        }
    }
    private static int CheckBuddies(int x, int y){
        int[] buddies = new int[8];
        int aliveBuddies = 0;

        try {buddies[0] = (Squares[x-1][y].isAlive()) ? 1 : 0;}catch (Exception ignored){} //left
        try {buddies[1] = (Squares[x+1][y].isAlive()) ? 1 : 0;}catch (Exception ignored){} //right
        try {buddies[2] = (Squares[x][y-1].isAlive()) ? 1 : 0;}catch (Exception ignored){} //top
        try {buddies[3] = (Squares[x][y+1].isAlive()) ? 1 : 0;}catch (Exception ignored){} //bottom
        try {buddies[4] = (Squares[x-1][y-1].isAlive()) ? 1 : 0;}catch (Exception ignored){} //left top
        try {buddies[5] = (Squares[x-1][y+1].isAlive()) ? 1 : 0;}catch (Exception ignored){} //left bottom
        try {buddies[6] = (Squares[x+1][y-1].isAlive()) ? 1 : 0;}catch (Exception ignored){} //right top
        try {buddies[7] = (Squares[x+1][y+1].isAlive()) ? 1 : 0;}catch (Exception ignored){} //right bottom

        for (int b : buddies) {
            aliveBuddies += b;
        }
        return aliveBuddies;
    }
    private static void Draw(Graphics g){
        g.clearRect(0,0, Frame.getWidth(), Frame.getHeight());
        for (int y = 0; y < Squares[0].length - 1; y++) {
            for (int x = 0; x < Squares[1].length - 1; x++) {
                if (Squares[x][y] != null)
                    Squares[x][y].DrawSquare(g);
            }
        }
    }
    private static void InitGame(){
        Frame = new JFrame();
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(new Dimension(800,600));
        Frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Frame.setResizable(false);
        Frame.setVisible(true);
        Frame.createBufferStrategy(3);
        Frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                ResizeSquareArray();
            }
        });
        Frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();

                double x1 = Math.floor((x - XOffset) / 10d);
                double y1 = Math.floor((y - YOffset) / 10d);

                if (Squares[(int)x1][(int)y1].isAlive()){
                    Squares[(int)x1][(int)y1].setAlive(false);
                }
                else {
                    Squares[(int)x1][(int)y1].setAlive(true);
                }
            }
        });
        Frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                switch (e.getKeyChar()){
                    case 'w':
                        tick = !tick;
                        break;
                    case ' ':
                        for (int x = 0; x < Squares[0].length; x++) {
                            for (int y = 0; y < Squares[1].length; y++) {
                                Squares[x][y].setAlive(false);
                            }
                        }
                        break;
                }
            }
        });
        ResizeSquareArray();
    }
    private static void ResizeSquareArray(){
        Squares = new Square[(Frame.getWidth() / Width) ][(Frame.getWidth() / Height) ];
        for (int y = 0; y < Squares[0].length; y++) {
            for (int x = 0; x < Squares[1].length; x++) {
                Squares[x][y] = new Square((x * Width) + XOffset,(y * Height) + YOffset, false, Width, Height);
            }
        }
    }

}

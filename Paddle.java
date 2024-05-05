import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Paddle {
    private int x,y,height,id,up,down,speed=5;
    private long[] freeze = new long[] {0, 0, 0};
    private final static int WIDTH = 20;

    public Paddle(int x, int y, int height, int id, int up, int down){
        this.x=x;
        this.y=y;
        this.height=height;
        this.id=id;
        this.up=up;
        this.down=down;
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, height);
    }
    public int getID(){
        return id;
    }
    private void setFreeze(long time, int type, int duration){
      freeze = new long[] {time, type, duration};
    }

    public static ArrayList<Paddle> MakePaddle(int u0,int d0,int u1,int d1,int u2,int d2,int u3,int d3,int u4,int d4,int u5,int d5){
      ArrayList<Paddle> paddles = new ArrayList<Paddle>(Arrays.asList(
      new Paddle(100, 250, 100, 0, u0, d0),
      new Paddle(285, 250, 100, 1, u1, d1),
      new Paddle(470, 250, 100, 2, u2, d2),
      new Paddle(900, 250, 100, 3, u3, d3),
      new Paddle(1085, 250, 100, 4, u4, d4),
      new Paddle(1270, 250, 100, 5, u5, d5)));
      return paddles;
    }

    public void setSpeed(int s){
      speed=s;
    }

    public void move(boolean keys[]){
      if ((freeze[1] != 1) || (freeze[1] == 1 && freeze[0] + freeze[2] < System.currentTimeMillis())){
        if (keys[up]){ //left arrow pressed
          y = Math.max(0, y - speed);
        }
        if (keys[down]){ //right arrow pressed
          y = Math.min(600 - height, y + speed);
        }
      }
    }
    public void setIceTime(long tim){
        freeze[0]=tim;
    }
    public void shoot(ArrayList<Paddle> paddles){
      for (int i = 0; i < paddles.size(); i ++){
        if (i == id){
          continue;
        }
        else{
          paddles.get(i).setFreeze(System.currentTimeMillis(), 1, 3000);
        }
      }
    }

    public void draw(Graphics g){
      g.setColor(Color.WHITE);
      if (freeze[1] == 1 && freeze[0] + freeze[2] > System.currentTimeMillis()){
        g.setColor(Color.CYAN);
      }
      g.fillRect(x,y,WIDTH,height);
    }
}
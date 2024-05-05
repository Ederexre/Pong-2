import java.awt.*;
import java.util.*;
public class Ball {
    Random rand = new Random();
    private int x, y, vx = 0, vy, lastHit;
    private final static int SIZE = 15;
    private boolean wallBounce = false;
    private ArrayList<Paddle> paddle;
    public static int hitCounter = 1;

    public Ball(int x, int y, ArrayList<Paddle> paddle) {
        this.x = x;
        this.y = y;
        while (vx >= -1 && vx <= 1){
          this.vx = rand.nextInt(9) - 4;
        }
        this.vy = rand.nextInt(9) - 4;
        this.paddle = paddle;
    }

    public static void addHC(){
      hitCounter++;
    }

    public static void resetHC(){
      hitCounter = 1;
    }

    public static void reduceHC(){
      hitCounter = hitCounter % 5;
    }

    public int move(){
        this.x += this.vx;
        this.y += this.vy;
        if (this.y < 0) {
          vy = vy > 0 ? vy : vy*-1;
          wallBounce = true;
        }
        else if (this.y + SIZE > 600) {
          vy = vy < 0 ? vy : vy*-1;
          wallBounce = true;
        }
        for (int i = 0; i < paddle.size(); i++) {
            collision(paddle.get(i));
        }
        if (this.x < 0){
          return 2;
        }
        if (this.x + SIZE > 1400){
          return 1;
        }
        return 0;
    }

    public void collision(Paddle p) {
        //id 0,1,2 on the left
        //id 3,4,5 on right
        int id = p.getID();
        Rectangle pRect = p.getRect();
        Rectangle bRect = this.getRect();
        int pX = pRect.x, pY = pRect.y, pW = pRect.width, pH = pRect.height;
        if (bRect.intersects(pRect) && (lastHit != id || wallBounce)){
          if (y - vy >= pY + pH){
            vy = vy > 0 ? vy : vy*-1;
          }
          if (y + SIZE - vy <= pY){
            vy = vy < 0 ? vy : vy*-1;
          }
          else if (x - vx >= pX + pW){
            vx = vx > 0 ? vx : vx*-1;
            vy = (int)((pY + pH / 2 - y - SIZE/2) / (pH / -10)); //equation to determine vy so that it's more when it's further away from centre of paddle
            if (vy == 0){ //if vy is 0
              vy = rand.nextInt(3) - 1; //-1, 0, 1
		        }
          }
          if (x + SIZE - vx <= pX){
            vx = vx < 0 ? vx : vx*-1;
            vy = (int)((pY + pH / 2 - y - SIZE/2) / (pH / -10)); //equation to determine vy so that it's more when it's further away from centre of paddle
            if (vy == 0){ //if vy is 0
              vy = rand.nextInt(3) - 1; //-1, 0, 1
		        }
          }
          lastHit = id;
          wallBounce = false;
          hitCounter++;
        }
    }

    public Rectangle getRect() {//this rect is bigger than the real rectangle and includes where the ball will be in the next instance
        return new Rectangle(this.x - Math.abs(this.vx), this.y - Math.abs(this.vy), SIZE + 2 * Math.abs(this.vx), SIZE + 2 * Math.abs(this.vy));
    }
    public int getX(){
      return x;
    }
    public int getY(){
      return y;
    }
    public int getlhID(){
      return lastHit;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, SIZE, SIZE);
    }
}
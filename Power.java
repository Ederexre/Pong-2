import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
public class Power{
    private int x,y,type;
    private ArrayList<Power> onField=new ArrayList<>();
    private static final int SIZE=30;
    private Rectangle rect;
    
    public Power(){

    }
    private Power(int x, int y, int type){
        this.x=x;
        this.y=y;
        this.type=type;
        this.rect=new Rectangle(x,y,SIZE,SIZE);
    }
    private Random random=new Random();
    public void generate(){//create new random powerup
        int rX=Panel.WIDTH/2-100+random.nextInt(0,9)*20;
        int rY=random.nextInt(0,Panel.HEIGHT/SIZE)*SIZE;
        //types:0,      1,      2,         3        4
        //      split, freeze, blindness, speed  +1 point
        int rType=random.nextInt(5);
        onField.add(new Power(rX,rY,rType));
    }
    public int[] collision(Ball ball){
        int siz=onField.size();
        for(int i=0;i<siz;i++){
            if (ball.getRect().intersects(onField.get(i).rect)){
                type=onField.get(i).type;
                onField.remove(i);
                siz--;//decrease array size so not out of range
                return new int[] {type, ball.getlhID()};//return arr about type and ballID
            }
        }
        return new int[] {-1, -1};
    }

    public void stop(){
        onField.clear();
    }

    public void draw(Graphics g){
        for(Power p: onField){
            if(p.type==0){//split
              g.setColor(Color.RED);
            }
            else if(p.type==1){//freeze
              g.setColor(Color.CYAN);
            }
            else if(p.type==2){//blind opponent
              g.setColor(Color.MAGENTA);
            }
            else if(p.type==3){//speed to recent paddle
              g.setColor(Color.YELLOW);
            }
            else{//+1 point
              g.setColor(Color.GREEN);
            }
            g.fillRect(p.x,p.y,SIZE,SIZE);
        }
    }
}
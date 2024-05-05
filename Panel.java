/*Copied from Mr. R. McKenzie ICS4U*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/*This class manages the game itself (screen, making levels, lives, moving, actions, deciding what to draw)*/

class Panel extends JPanel implements KeyListener, ActionListener{
  private static final int MENU = 0, GAME = 1, PAUSE=2;
  public static final int WIDTH = 1400, HEIGHT = 600;
  private boolean []keys; //keyboard keys
  private int screen = MENU, point = 0, points1 = 0, points2 = 0;
  private int[] active = new int[] {-1, -1};
  private long time,bTime,sTime;
  private ArrayList<Paddle> paddles;
  private ArrayList<Ball> balls = new ArrayList<Ball>();
  private Timer timer;
  private boolean restart = false, pause = false;
  private boolean[] blindness={false,false};
  private Power power=new Power();

  public Panel(){
    keys = new boolean[KeyEvent.KEY_LAST + 1];
    setPreferredSize(new Dimension(1400, 600));

    setFocusable(true);
    requestFocus();
    addKeyListener(this);
    timer = new Timer(10, this);
    timer.start();
  }

  public void startGame(){//ball drop
    balls.clear();
    power.stop();
    paddles = Paddle.MakePaddle(
            KeyEvent.VK_Q,KeyEvent.VK_A,
            KeyEvent.VK_W,KeyEvent.VK_S,
            KeyEvent.VK_E,KeyEvent.VK_D,
            KeyEvent.VK_U,KeyEvent.VK_J,
            KeyEvent.VK_I,KeyEvent.VK_K,
            KeyEvent.VK_O,KeyEvent.VK_L);
    balls.add(new Ball(693, 300, paddles));
    Ball.resetHC();
    sTime=0;
    bTime=0;
    time=0;
  }

  public void moveAll(){ //method to move everything
    for(int i = 0; i < paddles.size(); i++){
      paddles.get(i).move(keys);
    }
    for (int i = 0; i < balls.size(); i++){
      point = balls.get(i).move();
      active = power.collision(balls.get(i));
      if (active[0] == 0){//split
        balls.add(new Ball(balls.get(i).getX(), balls.get(i).getY(), paddles));
        balls.add(new Ball(balls.get(i).getX(), balls.get(i).getY(), paddles));
      }
      else if (active[0] == 1){//freeze
        paddles.get(active[1]).shoot(paddles);
      }
      else if(active[0] ==2){//blind opp
        blind(active[1]);
      }
      else if(active[0]==3){//paddle spd
        paddles.get(active[1]).setSpeed(10);
        sTime=System.currentTimeMillis();//start speed up power
      }
      else if (active[0] == 4){
        if (active[1] < 3){
          points1 ++;
        }
        else{
          points2 ++;
        }
      }

      if (point != 0){
        if (point == 1){
          points1 ++;
        }
        else{
          points2 ++;
        }
        balls.remove(i);
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e){
    if (screen == MENU){
      if (keys[KeyEvent.VK_SPACE]){
        startGame();
        screen = GAME;
      }
    }
    else if (screen == GAME) {
      if (balls.isEmpty()) {
        if (restart == false) {
          restart = true;
          power.stop();
          time = System.currentTimeMillis();
        }
        if (time + 4000 < System.currentTimeMillis()) {
          balls.add(new Ball(693, 300, paddles));
          Ball.resetHC();
          restart = false;
        }
      }
      if (screen == GAME) {
        moveAll();
        if (Ball.hitCounter % 6 == 0) {
          Ball.reduceHC();
          Ball.addHC();
          power.generate();//generates a powerup every 5 collisions a ball has
        }

        if (System.currentTimeMillis() > sTime + 8000) {//end speed up powerup
          for (Paddle pad : paddles) {
            pad.setSpeed(5);
          }
        }
      }
    }
    else if(screen==PAUSE){
      timer.stop();
    }
    repaint();
  }

  @Override
  public void keyReleased(KeyEvent ke){
    int key = ke.getKeyCode();
    keys[key] = false;
  }

  @Override
  public void keyPressed(KeyEvent ke){
    int key = ke.getKeyCode();
    keys[key] = true;
    if(screen==GAME&&timer.isRunning()&&(keys[KeyEvent.VK_ESCAPE]||keys[KeyEvent.VK_R] || keys[KeyEvent.VK_P])){
      screen=PAUSE;
    }
    else if(screen==PAUSE){
      if(keys[KeyEvent.VK_ESCAPE]){
        screen=MENU;
        cont();
      }
      else if(keys[KeyEvent.VK_SPACE]){
        screen=GAME;
        cont();
      }
    }
  }
  private void cont(){
    timer.restart();
    for(Paddle paddle:paddles){
      paddle.setIceTime(System.currentTimeMillis());
    }
    time=System.currentTimeMillis();
    bTime=System.currentTimeMillis();
    sTime=System.currentTimeMillis();
  }
  @Override
  public void keyTyped(KeyEvent ke){}
  public void blind(int side){
    bTime=System.currentTimeMillis();
    if(side>=0&&side<=2){//turns right side blind
      blindness[0]=false;
      blindness[1]=true;
    }
    else if(side>=3&&side<=5){//turns left side blind
      blindness[0]=true;
      blindness[1]=true;
    }
  }

  @Override
  public void paint(Graphics g){ //drawing everything
    if (screen == MENU){
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 1400, 600);
      g.setColor(Color.WHITE);
      g.setFont(new Font("Calibri", Font.BOLD, 75));
      g.drawString("PONG 2",500,300);
      g.setFont(new Font("Calibri", Font.BOLD, 50));
      g.drawString("Press SPACE to Start the Game!", 300, 500);
      g.setFont(new Font("Calibri", Font.BOLD, 20));
      g.drawString("Paddle 1: Q/A", 100, 50);
      g.drawString("Paddle 2: W/S", 100, 100);
      g.drawString("Paddle 3: E/D", 100, 150);
      g.drawString("Paddle 4: U/J", 1200, 50);
      g.drawString("Paddle 5: I/K", 1200, 100);
      g.drawString("Paddle 6: O/L", 1200, 150);
    }
    else if (screen == GAME){
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 1400, 600);
      for(int i = 0; i < paddles.size(); i++){
        paddles.get(i).draw(g);
      }
      g.setColor(Color.GRAY);//blindness
      if(blindness[1] && System.currentTimeMillis()<bTime+5000){
        if(!blindness[0]){//right side blind
          g.fillRect(700,0,700,600);
        }
        if(blindness[0]){//left side blind
          g.fillRect(0,0,700,600);
        }
      }
      else{
        blindness[1]=false;
      }
      power.draw(g);
      g.setColor(Color.WHITE);
      g.fillRect(697, 0, 6, 600);
      g.setFont(new Font("Calibri", Font.BOLD, 50));
      g.drawString(points1 + "", 550, 80);
      g.drawString(points2 + "", 820, 80);
      for (int i = 0; i < balls.size(); i++){
        balls.get(i).draw(g);
      }
    }
    else if (screen==PAUSE){
      g.setColor(new Color(10,10,10,100));//semi transparent
      g.fillRect(0,0,1400,600);
      g.setColor(Color.RED);
      g.setFont(new Font("Calibri", Font.BOLD, 50));
      g.drawString("PAUSED", 610, 275);
      g.setFont(new Font("Calibri", Font.BOLD, 25));
      g.drawString("Press SPACE to Resume", 570, 515);
      g.drawString("Press ESCAPE to Main Menu", 570, 565);
    }
  }
}
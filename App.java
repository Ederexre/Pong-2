/*Copied from Mr. R. McKenzie ICS4U*/

import javax.swing.*;

public class App extends JFrame {
  Panel game = new Panel();
  public App(){ //managing the window
    super("Pong 2");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(game);
    pack();
    setVisible(true);
  }

    public static void main(String[] args){
      App frame = new App();
    }
}
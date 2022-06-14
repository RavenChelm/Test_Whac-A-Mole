import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

//Created by RavenChelm

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Whac A Mole");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1080, 920);
        setLocation(400, 50);
        add(new Game());
        setVisible(true);
        setBackground(new Color(239, 239, 239));
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.getContentPane().add(Game.GamePanel);
    }
}
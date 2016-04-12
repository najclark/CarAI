import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Circuit extends JPanel {


   private static final long serialVersionUID = 1;
   private BufferedImage[] street = new BufferedImage[7];
   private int[][] circuitIndex = new int[10][10];
   private boolean built = false;

   public Circuit() {

      setSize(1000, 1000);

      try {
         street[0] = ImageIO.read(this.getClass().getResource(
               "test1.jpg"));
         street[1] = ImageIO.read(this.getClass().getResource(
               "test2.png"));
         street[2] = ImageIO.read(this.getClass().getResource(
               "test3.jpg"));
         street[3] = ImageIO.read(this.getClass().getResource(
               "test1.jpg"));
         street[4] = ImageIO.read(this.getClass().getResource(
               "test2.png"));
         street[5] = ImageIO.read(this.getClass().getResource(
               "test3.jpg"));

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // TODO: read from textfile to fill the index

      circuitIndex[0][0] = 2;
      circuitIndex[0][1] = 0;
      circuitIndex[0][2] = 3;
      circuitIndex[0][3] = 6;
      circuitIndex[0][4] = 6;

      circuitIndex[1][0] = 1;
      circuitIndex[1][1] = 6;
      circuitIndex[1][2] = 5;
      circuitIndex[1][3] = 3;
      circuitIndex[1][4] = 6;

      circuitIndex[2][0] = 5;
      circuitIndex[2][1] = 0;
      circuitIndex[2][2] = 0;
      circuitIndex[2][3] = 4;
      circuitIndex[2][4] = 6;

      circuitIndex[3][0] = 6;
      circuitIndex[3][1] = 6;
      circuitIndex[3][2] = 6;
      circuitIndex[3][3] = 6;
      circuitIndex[3][4] = 6;

      circuitIndex[4][0] = 6;
      circuitIndex[4][1] = 6;
      circuitIndex[4][2] = 6;
      circuitIndex[4][3] = 6;
      circuitIndex[4][4] = 6;

   }

   @Override
   protected void paintComponent(Graphics g) {
      // TODO Auto-generated method stub
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      for (int i = 0; i <= 4; i++) {
         for (int j = 0; j <= 4; j++) {

            if (circuitIndex[j][0] == 0) {
               g2d.drawImage(street[0], i * 200, j * 200, this);
            } else if (circuitIndex[j][0] == 1) {
               g2d.drawImage(street[1], i * 200, j * 200, this);
            } else if (circuitIndex[j][0] == 2) {
               g2d.drawImage(street[2], i * 200, j * 200, this);
            } else if (circuitIndex[j][0] == 3) {
               g2d.drawImage(street[3], i * 200, j * 200, this);
            } else if (circuitIndex[j][0] == 4) {
               g2d.drawImage(street[4], i * 200, j * 200, this);
            } else if (circuitIndex[j][0] == 5) {
               g2d.drawImage(street[5], i * 200, j * 200, this);
            } else if (circuitIndex[j][0] == 0) {
               g2d.drawImage(street[1], i * 200, j * 200, this);
            }

         }
      }
   }
}
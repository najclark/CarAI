import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Car extends JPanel implements Runnable {

	private static final long serialVersionUID = 007;
	private BufferedImage car = null;
	private Image dbImage = null;
	private Graphics dbg;
	private float x = 100F, y = 100F;
	private Thread driveThread = new Thread(this);
	private static int[] key = new int[256]; // keyboard input
	private float MAX_SPEED = 10F;
	private float speed = 0F; // speed of our racing car
	private float acceleration = 0.15F;
	private int player;

	private double force = 0;
	private double traction = 10;
	private double mass = 10;
	private double chassisAngle = 0; // angel of the car
	private double wheelAngle = 0; // angel of the front wheels
	private double wheelBase = 100;
	private double absoluteAngle = 0;
	private boolean drifting = false;
	private int maxSkids = 100;

	private ArrayList<Skidmark> marks = new ArrayList<Skidmark>();

	public Car(int player) {

		this.player = player;

		this.setSize(super.getHeight(), super.getWidth());
		this.setFocusable(true); // enables keyboard

		try {
			if (player == 1) {
				// red car
				car = ImageIO.read(this.getClass().getResource("car.png"));

				wheelBase = car.getHeight();
				System.out.println(car.getColorModel());
			} else if (player == 2) {
				// blue car
				car = ImageIO.read(this.getClass().getResource("car.png"));
				x = x + 30;
			}
			// background =
			// ImageIO.read(this.getClass().getResource("ressources/level1.png"));

		} catch (IOException e) {
			System.out.println("dupi");
		}

		// starts the drive thread
		startGame();

	}

	private void startGame() {
		// TODO Auto-generated method stub
		driveThread.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
		force = mass * (speed * speed) / (wheelBase * speed / wheelAngle);
		if (Math.abs(force) > traction) {
			drifting = true;
			g.drawString(String.valueOf("Drift: " + force), 100, 100);
		} else {
			drifting = false;
			g.drawString(String.valueOf("Not Drifting: " + force), 100, 100);
		}
		g.drawString(String.valueOf("Radius: " + (wheelBase * speed / wheelAngle)), 100, 150);
		g.drawString(String.valueOf("Speed: " + (speed)), 100, 200);
		g.drawString(String.valueOf("Current Angle: " + (chassisAngle)), 100, 250);
		g.drawString(String.valueOf("Drifting Angle: " + (absoluteAngle)), 100, 300);
		if (chassisAngle == absoluteAngle) {
			g.drawString("Not Sliding", 100, 350);
		} else {
			g.drawString("Sliding", 100, 350);
			marks.add(new Skidmark((int) x, (int) y, 25, 40, chassisAngle, Color.black));
		}
		if (marks.size() > maxSkids) {
			marks.remove(0);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {

		// super.paintComponent(g);
		this.setOpaque(false);

		int i = 0;
		for (Skidmark sm : marks) { // Draws skids before car
			Color c = new Color((int) (50 - i / ((double) maxSkids / 50)), (int) (50 - i / ((double) maxSkids / 50)),
					(int) (50 - i / ((double) maxSkids / 50)));
			sm.setColor(c);
			sm.draw(g);
			i++;
		}

		// rotation
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform rot = new AffineTransform();
		// Rotation at the center of the car
		float xRot = x + 12.5F;
		float yRot = y + 20F;
		rot.rotate(Math.toRadians(chassisAngle), xRot, yRot);
		g2d.setTransform(rot);
		// Draws the cars new position and angle

		Line2D.Double l1 = new Line2D.Double(x, y + 10, x - 100, y + 10);
		Line2D.Double l2 = new Line2D.Double(x, y, x - 100, y - 100);
		Line2D.Double l3 = new Line2D.Double(x + 12.5, y, x + 12.5, y - 100);
		Line2D.Double l4 = new Line2D.Double(x + 25, y, x + 125, y - 100);
		Line2D.Double l5 = new Line2D.Double(x + 25, y + 10, x + 125, y + 10);
		
		Line2D.Double l11 = new Line2D.Double(x, y + 10, x - 50, y + 10);
		Line2D.Double l21 = new Line2D.Double(x, y, x - 50, y - 50);
		Line2D.Double l31 = new Line2D.Double(x + 12.5, y, x + 12.5, y - 50);
		Line2D.Double l41 = new Line2D.Double(x + 25, y, x + 75, y - 50);
		Line2D.Double l51 = new Line2D.Double(x + 25, y + 10, x + 75, y + 10);
		
		Line2D.Double l12 = new Line2D.Double(x, y + 10, x - 25, y + 10);
		Line2D.Double l22 = new Line2D.Double(x, y, x - 25, y - 25);
		Line2D.Double l32 = new Line2D.Double(x + 12.5, y, x + 12.5, y - 25);
		Line2D.Double l42 = new Line2D.Double(x + 25, y, x + 50, y - 25);
		Line2D.Double l52 = new Line2D.Double(x + 25, y + 10, x + 50, y + 10);
		

		g2d.drawImage(car, (int) x, (int) y, 25, 40, this);
		
		g2d.setColor(Color.green);
		g2d.draw(l1);
		g2d.draw(l2);
		g2d.draw(l3);
		g2d.draw(l4);
		g2d.draw(l5);

		g2d.setColor(Color.yellow);
		g2d.draw(l11);
		g2d.draw(l21);
		g2d.draw(l31);
		g2d.draw(l41);
		g2d.draw(l51);
		
		g2d.setColor(Color.red);
		g2d.draw(l12);
		g2d.draw(l22);
		g2d.draw(l32);
		g2d.draw(l42);
		g2d.draw(l52);
		// g2d.drawString(String.valueOf(speed), x, y);
	}
	
	public void drawCenteredCircle(Graphics g, int x, int y, int r) {
		  x = x-(r/2);
		  y = y-(r/2);
		  g.fillOval(x,y,r,r);
		}

	public Point intersection(Line2D.Double l1, Line2D.Double l2) {
		int x1 = (int) l1.getX1();
		int y1 = (int) l1.getY1();
		int x2 = (int) l1.getX2();
		int y2 = (int) l1.getY2();
		int x3 = (int) l2.getX1();
		int y3 = (int) l2.getY1();
		int x4 = (int) l2.getX2();
		int y4 = (int) l2.getY2();

		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		if (d == 0)
			return null;

		double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;

		double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

		return new Point((int)xi, (int)yi);
	}
	
	public Point test(double p, double r, double q, double s){
		double t = (q - p) * s / (r * s);
		double u = (q - p) * r / (r * s);
		if(r*s != 0 && (0 <= t && t <= 1) && (0 <= u && u <= 1)){
			System.out.println("("+p+t*r + ", " + q+u*s + ")");
			return new Point((int)(p+t*r), (int)(q+u*s));
		}
		return new Point(0, 0);
	}
	
	  public Point2D.Float getIntersectionPoint(Line2D.Double line1, Line2D.Double line2) {
		    if (! line1.intersectsLine(line2) ) return null;
		      double px = line1.getX1(),
		            py = line1.getY1(),
		            rx = line1.getX2()-px,
		            ry = line1.getY2()-py;
		      double qx = line2.getX1(),
		            qy = line2.getY1(),
		            sx = line2.getX2()-qx,
		            sy = line2.getY2()-qy;

		      double det = sx*ry - sy*rx;
		      if (det == 0) {
		        return null;
		      } else {
		        double z = (sx*(qy-py)+sy*(px-qx))/det;
		        if (z==0 ||  z==1) return null;  // intersection at end point!
		        return new Point2D.Float(
		          (float)(px+z*rx), (float)(py+z*ry));
		      }
		 } // end intersection line-line

	protected void calculateCarPosition() {

		// calculates the new X and Y - coordinates
		// bring driftingAngle to currentAngle
		double distance = chassisAngle - absoluteAngle;
		if (speed < 1F) {
			absoluteAngle = chassisAngle;
		} else if (distance > 0) {
			absoluteAngle += Math.min(distance, (speed / 10));
		} else if (distance < 0) {
			absoluteAngle += Math.max(distance, -1 * (speed / 10));
		}
		x += Math.sin(absoluteAngle * Math.PI / 180) * speed * 0.5;
		y += Math.cos(absoluteAngle * Math.PI / 180) * -speed * 0.5;

		if (x < 0) {
			x = 0;
		} else if (x > this.getWidth()) {
			x = this.getWidth();
		}

		if (y < 0) {
			y = 0;
		} else if (y > this.getHeight()) {
			y = this.getHeight();
		}

	}

	protected void carMovement() {

		// Player One Key's

		if (drifting) {

			// if (wheelAngle > 0) {
			// currentAngle += 2;
			// } else if (wheelAngle < 0) {
			// currentAngle -= 2;
			// }
			speed *= 0.99F;
		}

		if (player == 1) {

			if (key[KeyEvent.VK_LEFT] != 0) {
				chassisAngle -= 2;
				wheelAngle -= 1;

			} else if (key[KeyEvent.VK_RIGHT] != 0) {
				chassisAngle += 2;
				wheelAngle += 1;

			} else {
				if (wheelAngle > 0) {
					wheelAngle -= 1;
				} else if (wheelAngle < 0) {
					wheelAngle += 1;
				}
			}

			if (wheelAngle > 30) {
				wheelAngle = 30;
			} else if (wheelAngle < -30) {
				wheelAngle = -30;
			}

			if (key[KeyEvent.VK_UP] != 0) {

				if (speed < MAX_SPEED) {

					speed += acceleration;
				}

			} else if (key[KeyEvent.VK_DOWN] != 0 && speed > -1) {
				speed = speed - 0.1F;
			}
			speed = speed * 0.99F;

		} else {

			// Player Two Key's

			if (key[KeyEvent.VK_A] != 0) {
				chassisAngle -= 2;

			} else if (key[KeyEvent.VK_D] != 0) {
				chassisAngle += 2;
			}

			if (key[KeyEvent.VK_W] != 0) {

				if (speed < MAX_SPEED) {

					speed += acceleration;
				}

			} else if (key[KeyEvent.VK_S] != 0 && speed > -1) {
				speed = speed - 0.1F;
			}
			// reduce speed when no key is pressed
			speed = speed * 0.99F;
		}

	}

	public void getUnderground() {

	}

	// get key events!
	final protected void processKeyEvent(KeyEvent e) {
		key[e.getKeyCode()] = e.getID() & 1;
	}

	@Override
	public void run() {
		int fps = 100;
		long start = System.currentTimeMillis();
		while (true) {
			start = System.currentTimeMillis();
			repaint();
			carMovement();
			calculateCarPosition();

			try {
				int sleep = (int) ((1000 / fps) - (System.currentTimeMillis() - start));

				if (sleep > 0) {
					Thread.sleep(sleep);
				} else {
					System.out.println("Computer can't keep up with " + fps + " fps!");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Test 2d Car");
		Car c = new Car(1);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(c);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

}
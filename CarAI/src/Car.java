import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
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
			marks.add(new Skidmark((int)x, (int)y, 25, 40, chassisAngle, Color.black));
			System.out.println("(" + x + ", " + y + ")");
		}
		if(marks.size() > 500){
			marks.remove(0);
		}
		for(Skidmark sm : marks){
			sm.draw(g);
		}
		Skidmark m = new Skidmark(0, 0, 25, 40, chassisAngle, Color.black);
		m.draw(g);
	}

	@Override
	protected void paintComponent(Graphics g) {

		// super.paintComponent(g);
		this.setOpaque(false);

		// rotation
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform rot = g2d.getTransform();
		// Rotation at the center of the car
		float xRot = x + 12.5F;
		float yRot = y + 20F;
		rot.rotate(Math.toRadians(chassisAngle), xRot, yRot);
		g2d.setTransform(rot);
		// Draws the cars new position and angle
		g2d.drawImage(car, (int) x, (int) y, 25, 40, this);
		//g2d.drawString(String.valueOf(speed), x, y);
	}

	protected void calculateCarPosition() {

		// calculates the new X and Y - coordinates
		// bring driftingAngle to currentAngle
		double distance = chassisAngle - absoluteAngle;
		if (speed < 1F) {
			absoluteAngle = chassisAngle;
		} else if (distance > 0) {
			absoluteAngle += Math.min(distance, (speed/10));
		} else if (distance < 0) {
			absoluteAngle += Math.max(distance, -1*(speed/10));
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
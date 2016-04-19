import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Skidmark {

	private int x, y, width, height;
	private double angle;
	private Color c;
	
	public Skidmark(int x, int y, int width, int height, double angle, Color c){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.c = c;
	}
	
	public void draw(Graphics g){
		
		double theta = Math.toRadians(angle);
		// create rect centred on the point we want to rotate it about
		Rectangle rect = new Rectangle(x, y, width, height);

		AffineTransform transform = new AffineTransform();
		float xRot = x + 12.5F;
		float yRot = y + 20F;
		transform.rotate(theta, xRot, yRot);

		Graphics2D graphics = (Graphics2D)g; // get it from whatever you're drawing to
		graphics.setColor(c);
		graphics.setTransform(transform);
		graphics.draw(rect);
		graphics.fillRect(x, y, width, height);
	}
	
	public void setColor(Color c){
		this.c = c;
	}
	
}

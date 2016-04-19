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
		Rectangle rect1 = new Rectangle(x, y, width/3, height/3);
		Rectangle rect2 = new Rectangle(x+(width/3)*2, y, width/3, height/3);
		Rectangle rect3 = new Rectangle(x, y+(height/3)*2, width/3, height/3);
		Rectangle rect4 = new Rectangle(x+(width/3)*2, y+(height/3)*2, width/3, height/3);

		AffineTransform transform = new AffineTransform();
		float xRot = x + 12.5F;
		float yRot = y + 20F;
		transform.rotate(theta, xRot, yRot);

		Graphics2D graphics = (Graphics2D)g; // get it from whatever you're drawing to
		graphics.setColor(c);
		graphics.setTransform(transform);
		graphics.draw(rect1);
		graphics.draw(rect2);
		graphics.draw(rect3);
		graphics.draw(rect4);
		graphics.fill(rect1);
		graphics.fill(rect2);
		graphics.fill(rect3);
		graphics.fill(rect4);
	}
	
	public void setColor(Color c){
		this.c = c;
	}
	
}

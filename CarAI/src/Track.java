import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Track {

	JPanel panel = new JPanel();

	public static void main(String[] args) {
		new Track().run();
	}

	public void run() {
		JFrame f = new JFrame("Test Track");
		f.setSize(1000, 1000);
		f.add(panel);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		while (true) {
			panel.repaint();
			genertateTrack();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public JPanel genertateTrack() {
		Graphics g = panel.getGraphics();
		Random rand = new Random();
		g.drawString("Test", 100, 100);
		int pointCount = new Random().nextInt(10) + 10; // we'll have a total of
														// 10 to 20 points
		ArrayList<Point> points1 = new ArrayList<Point>();
		Point[] points = new Point[pointCount * 2];
		int[] xs = new int[pointCount * 2];
		int[] ys = new int[pointCount * 2];
		// System.out.println(points.length);

		for (int i = 0; i < pointCount * 2; i++) {
			points1.add(new Point(rand.nextInt(900) + 50, rand.nextInt(900) + 50));
			xs[i] = points1.get(i).x;
			ys[i] = points1.get(i).y;
			// drawCenteredCircle(g, points1.get(i).x, points1.get(i).y, 10);
		}

		List<Point> list = GrahamScan.getConvexHull(points1);
		// System.out.println(list.size());
		int c = 0;
		for (Point p : list) {
			// System.out.println(p.x + " " + p.y);
			g.setColor(Color.BLUE);
			drawCenteredCircle(g, p.x, p.y, 10);
			g.setColor(Color.BLACK);
			g.drawString(c+"", p.x, p.y);
			c++;
		}

		ArrayList<Point> inter = new ArrayList<Point>();

		for (int i = 0; i < list.size() - 3; i++) {
			Point P0 = list.get(i);
			Point P1 = list.get(i + 1);
			Point P2 = list.get(i + 2);
			Point P3 = list.get(i + 3);

			float x = q(P0.x, P1.x, P2.x, P3.x, 0.5f);
			float y = q(P0.y, P1.y, P2.y, P3.y, 0.5f);
			inter.add(new Point((int) x, (int) y));
			g.setColor(Color.RED);
			drawCenteredCircle(g, (int) x, (int) y, 10);
		}

		for (int i = list.size() - 2; i < list.size(); i++) {

			Point P0 = null;
			Point P1 = null;
			Point P2 = null;
			Point P3 = null;
			if (i + 1 >= list.size()) {
				P1 = list.get(0);
				P2 = list.get(1);
				P3 = list.get(2);
			} else if (i + 2 >= list.size()) {
				P1 = list.get(i + 1);
				P2 = list.get(0);
				P3 = list.get(1);
			} else if (i + 3 >= list.size()) {
				P3 = list.get(0);
				P1 = list.get(i + 1);
				P2 = list.get(i + 2);
			} else {
				P0 = list.get(i);
				P1 = list.get(i + 1);
				P2 = list.get(i + 2);
				P3 = list.get(i + 3);
			}
			P0 = list.get(i);

			// System.out.println(P0 + ", " + P1 + ", " + P2 + ", " + P3 + ".");

			float x = q(P0.x, P1.x, P2.x, P3.x, 0.5f);
			float y = q(P0.y, P1.y, P2.y, P3.y, 0.5f);
			inter.add(new Point((int) x, (int) y));
			g.setColor(Color.RED);
			drawCenteredCircle(g, (int) x, (int) y, 10);
		}

		System.out.println("List: " + list.size() + " Inter: " + inter.size());
		
		//list.addAll(inter);
		
		//List<Point> newList = GrahamScan.getConvexHull(list);
		List<Point> newList = new ArrayList<Point>();
		inter = shiftRight(inter, 1);
		for (int i = 0; i < list.size()-1; i++) {
			System.out.println(i);
			newList.add(list.get(i));
			newList.add(inter.get(i));
			//list.add(i + 1, inter.get(i));
		}
		newList.add(list.get(list.size()-1));

		g.setColor(Color.black);
		for (int q = 0; q < newList.size() - 1; q++) {
			// System.out.println("(" + list.get(q).x + ", " + list.get(q).y +
			// ")");
			int x = newList.get(q).x;
			int y = newList.get(q).y;
			//drawCenteredCircle(g, x, y, 10);
			g.drawLine(x, y, newList.get(q + 1).x, newList.get(q + 1).y);
		}

		return panel;
	}

	 public static ArrayList<Point> shiftRight(ArrayList<Point> original, int shift){

	        ArrayList<Point> myList = new ArrayList<>();

	        //Put every character inside the myList
	        myList.addAll(original);

	        ArrayList<Point> temp = new ArrayList<>();

	        //Add the rightmost characters into the temp
	        //Delete those characters from the myList
	        int count = shift;
	        while(count != 0){
	            temp.add(myList.get(myList.size() - shift));
	            myList.remove(myList.get(myList.size() - shift));
	            count--;
	        }

	        //Add the characters from the temp to the beginning of the myList
	        for(int i = 0; i < temp.size(); i++){
	            myList.add(i ,temp.get(i));
	        }


	        return myList;
	    }
	
	public void drawCenteredCircle(Graphics g, int x, int y, int r) {
		x = x - (r / 2);
		y = y - (r / 2);
		// System.out.println("(" + x + ", " + y + ")");
		if (x < 0)
			x = 5;
		if (y < 0)
			y = 15;
		g.fillOval(x, y, r, r);
	}

	public float q(int p0, int p1, int p2, int p3, float t) {
		return 0.5f * ((2 * p1) + (p2 - p0) * t + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t * t
				+ (3 * p1 - p0 - 3 * p2 + p3) * t * t * t);
	}

	public int randInt(int min, int max) {

		Random rand = new Random();
		return rand.nextInt(max - min) + min;
	}

}

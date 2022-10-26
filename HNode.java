import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class HNode implements Comparable <HNode>{
	public ArrayList<Double> cost = new ArrayList<>();
	private ArrayList<HNode> connections = new ArrayList<>();
	public HNode parent = null;
	public int r, c, state;
	public double dist, total, money, x, y;

	//cost
	private int g;

	//distance
	private int h;

	private int f;

	public HNode(int a, int b, int s, double r_x, double r_y) {
		r = a;
		c = b;
		state = s;
		x = r_x;
		y = r_y;
	}

	public double getTotal() {
		total = money + dist;
		return total;
	}

	public void setTotal(double a) {
		total = a;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double s) {
		money = s;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double d) {
		dist = d;
	}

	public int getState() {
		return state;
	}

	public void setState(int s) {
		state = s;
	}

	public ArrayList<HNode> creatingConnections() {
		if (HexGrid.Hgrid[r + 1][c].getState() != 1) {
			connections.add(HexGrid.Hgrid[r + 1][c]);
		}

		if (HexGrid.Hgrid[r][c - 1].getState() != 1) {
			connections.add(HexGrid.Hgrid[r][c - 1]);
		}

		if (HexGrid.Hgrid[r][c + 1].getState() != 1) {
			connections.add(HexGrid.Hgrid[r][c + 1]);
		}

		if (HexGrid.Hgrid[r - 1][c].getState() != 1) {
			connections.add(HexGrid.Hgrid[r - 1][c]);
		}

		if (r%2 == 1) {
			if (HexGrid.Hgrid[r - 1][c + 1].getState() != 1) {
				connections.add(HexGrid.Hgrid[r - 1][c + 1]);
			}

			if (HexGrid.Hgrid[r + 1][c + 1].getState() != 1) {
				connections.add(HexGrid.Hgrid[r + 1][c + 1]);
			}
		}

		else {
			if (HexGrid.Hgrid[r - 1][c - 1].getState() != 1) {
				connections.add(HexGrid.Hgrid[r - 1][c - 1]);
			}

			if (HexGrid.Hgrid[r + 1][c - 1].getState() != 1) {
				connections.add(HexGrid.Hgrid[r + 1][c - 1]);
			}
		}

		return connections;
	}

	public ArrayList<Double> initializeCost() {
		if (HexGrid.Hgrid[r + 1][c].getState()!= 1) {
			cost.add(1.0);
		}

		if (HexGrid.Hgrid[r][c - 1].getState()!= 1) {
			cost.add(1.0);
		}

		if (HexGrid.Hgrid[r][c + 1].getState()!= 1) {
			cost.add(1.0);
		}

		if (HexGrid.Hgrid[r - 1][c].getState()!= 1) {
			cost.add(1.0);
		}

		if (r%2 == 1) {
			if (HexGrid.Hgrid[r - 1][c + 1].getState()!= 1) {
				cost.add(1.0);
			}

			if (HexGrid.Hgrid[r + 1][c + 1].getState()!= 1) {
				cost.add(1.0);
			}
		}

		else {
			if (HexGrid.Hgrid[r - 1][c - 1].getState()!= 1) {
				cost.add(1.0);
			}

			if (HexGrid.Hgrid[r + 1][c - 1].getState()!= 1) {
				cost.add(1.0);
			}
		}

		return cost;
	}

	public int compareTo(HNode other) {
		return (int) (this.getTotal() - other.getTotal());
	}

    //collaborated with andrew cheng
	public Shape getShape() {
		// TODO Auto-generated method stub
        Point2D[] corners = new Point2D[6];
        for (int i = 0; i < 6; i++) {

        	//calculate the pts using trig!
        	//store them into an array of points
            double angle = Math.PI / 180 * (60 * i + 30);
            corners[i] = new Point2D.Double(x + 12.5 * Math.cos(angle), y + 12.5 * Math.sin(angle));
        }

        //connects the lines
        Path2D hexPath = new Path2D.Double();
        hexPath.moveTo(corners[0].getX(), corners[0].getY());
        for (int i = 0; i < 6; i++) {
            hexPath.lineTo(corners[i].getX(), corners[i].getY());
        }

        hexPath.closePath();
        Shape hex = (Shape) hexPath;
        return hex;
	}
}
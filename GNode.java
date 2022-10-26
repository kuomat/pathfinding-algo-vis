import java.util.ArrayList;

public class GNode implements Comparable <GNode>{
	public ArrayList<Double> cost = new ArrayList<>();
	private ArrayList<GNode> connections = new ArrayList<>();
	public GNode parent = null;
	public int r, c, state;
	public double dist, total, money;

	//cost
	private int g;

	//distance
	private int h;

	private int f;

	public GNode(int a, int b, int s) {
		r = a;
		c = b;
		state = s;
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

	public ArrayList<GNode> creatingConnections() {
		if (Board.grid[r - 1][c - 1].getState() != 1)
			connections.add(Board.grid[r-1][c-1]);

		if (Board.grid[r - 1][c].getState() != 1)
			connections.add(Board.grid[r-1][c]);

		if (Board.grid[r - 1][c + 1].getState() != 1)
			connections.add(Board.grid[r-1][c+1]);

		if (Board.grid[r][c + 1].getState() != 1)
			connections.add(Board.grid[r][c+1]);

		if (Board.grid[r + 1][c + 1].getState() != 1)
			connections.add(Board.grid[r+1][c+1]);

		if (Board.grid[r + 1][c].getState() != 1)
			connections.add(Board.grid[r+1][c]);

		if (Board.grid[r + 1][c - 1].getState() != 1)
			connections.add(Board.grid[r+1][c-1]);

		if (Board.grid[r][c - 1].getState() != 1)
			connections.add(Board.grid[r][c-1]);

		return connections;
	}

	public ArrayList<Double> initializeCost() {
		if (Board.grid[r - 1][c - 1].getState() != 1)
			cost.add(14.0);

		if (Board.grid[r - 1][c].getState() != 1)
			cost.add(10.0);

		if (Board.grid[r - 1][c + 1].getState() != 1)
			cost.add(14.0);

		if (Board.grid[r][c + 1].getState() != 1)
			cost.add(10.0);

		if (Board.grid[r + 1][c + 1].getState() != 1)
			cost.add(14.0);

		if (Board.grid[r + 1][c].getState() != 1)
			cost.add(10.0);

		if (Board.grid[r + 1][c - 1].getState() != 1)
			cost.add(14.0);

		if (Board.grid[r][c - 1].getState() != 1)
			cost.add(10.0);

		return cost;
	}

	public int compareTo(GNode other) {
		return (int) (this.getTotal() - other.getTotal());
	}
}
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.Timer;

public class HexGrid extends JPanel implements MouseListener, MouseMotionListener{
    public PriorityQueue<HNode> fringe = new PriorityQueue<>();
    public ArrayList<HNode> trace = new ArrayList<>();
    public ArrayList<HNode> visited = new ArrayList<>();
    public HNode currNode, neighbor;
    public boolean foundGoal = false;
    Timer timer;

    public static boolean begin, end, drawUCS, drawGreedy, drawAStar, reset = false;
    public static HNode beginNode, endNode = null;
	public static HNode[][] Hgrid = new HNode[30][36];
	public static double x, y;
	public static int boxX, boxY;
    JButton U, G, A, Reset;

	   public HexGrid() {
	        createButtons();
	        initializeGrid();
	        setFocusable(true);
	        addMouseListener(this);
	        addMouseMotionListener(this);

	        repaint();
	    }

	    public void createButtons() {
	        this.setLayout(null);
	        U = new JButton("Uniform Cost Search");
	        G = new JButton("Greedy Search");
	        A = new JButton("AStar");
	        Reset = new JButton("Reset");

	        U.setFocusable(false);
	        G.setFocusable(false);
	        A.setFocusable(false);
	        Reset.setFocusable(false);

	        U.setBounds(815, 50, 170, 30);
	        G.setBounds(815, 130, 170, 30);
	        A.setBounds(815, 210, 170, 30);
	        Reset.setBounds(815, 290, 170, 30);

	        this.add(U);
	        this.add(G);
	        this.add(A);
	        this.add(Reset);

	        U.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    drawUCS = true;
	                    UniformCostSearch();
	                }
	            });

	        G.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    drawGreedy = true;
	                    GS();
	                }
	            });

	        A.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    drawAStar = true;
	                    A_Star();
	                }
	            });

	        Reset.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                reset = true;
	                repaint();
	            }
	        });
	    }

	    public void paintComponent(Graphics g) {
	    	 System.out.println("PAINT");
	         super.paintComponent(g);
	         Graphics2D g2 = (Graphics2D)g;

	         for (int r = 0; r < Hgrid.length; r++) {
	             for (int c = 0; c < Hgrid[r].length; c++) {
	                 g.setColor(Color.BLACK);

	                 //walls
	                 if (Hgrid[r][c].getState() == 1) {
	                	 g.setColor(Color.BLACK);
	                	 g2.fill(Hgrid[r][c].getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(Hgrid[r][c].getShape());
	                 }

	                 //empty cell
	                 else if (Hgrid[r][c].getState() == 0){
	                     g2.draw(Hgrid[r][c].getShape());
	                 }

	                 //start node
	                 else if(Hgrid[r][c].getState() == 2){
	                     g.setColor(new Color(255,255,102));
	                     g2.fill(Hgrid[r][c].getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(Hgrid[r][c].getShape());
	                 }

	                 //visited
	                 else if(Hgrid[r][c].getState() == 5){
	                     g.setColor(new Color(255, 153, 255));
	                     g2.fill(Hgrid[r][c].getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(Hgrid[r][c].getShape());
	                 }

	                 //fringe not visited
	                 else if(Hgrid[r][c].getState() == 6){
	                     g.setColor(new Color(0, 225, 225));
	                     g2.fill(Hgrid[r][c].getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(Hgrid[r][c].getShape());
	                 }

	                 else {
	                     g.setColor(Color.RED);
	                     g2.fill(Hgrid[r][c].getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(Hgrid[r][c].getShape());
	                 }

	             }
	         }

	         if (drawUCS) {
	             for (int i = 1; i < trace.size(); i++) {
	                 if (i < trace.size() - 1) {
	                     g2.setColor(new Color(192,192,192));
	                     g2.fill(trace.get(i).getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(trace.get(i).getShape());
	                 }
	             }
	         }

	         if (drawGreedy) {
	             for (int i = 1; i < trace.size(); i++) {
	                 if (i < trace.size() - 1) {
	                     g2.setColor(new Color(192,192,192));
	                     g2.fill(trace.get(i).getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(trace.get(i).getShape());
	                 }
	             }
	         }

	         if (drawAStar) {
	             for (int i = 1; i < trace.size(); i++) {
	                 if (i < trace.size() - 1) {
	                     g2.setColor(new Color(192,192,192));
	                     g2.fill(trace.get(i).getShape());
	                     g2.setColor(Color.black);
	                     g2.draw(trace.get(i).getShape());
	                 }
	             }
	         }

	         if(reset) {
	         	trace.clear();
	         	visited.clear();
	         	fringe.clear();
	         	initializeGrid();
	         	drawAStar = false;
	         	drawGreedy = false;
	         	drawUCS = false;
	         	beginNode = null;
	         	endNode = null;
	         	reset = false;
	         	begin = false;
	         	end = false;
	         	currNode = null;
	         	neighbor = null;
	         	foundGoal = false;
	         	repaint();
	         }
	    }

	    public static void initializeGrid() {
	        for (int r = 0; r < Hgrid.length; r++) {
	            for (int c = 0; c < Hgrid[0].length; c++) {
	                if (r % 2 == 1) {
	                    x = 5 + c*21.65 + 10.83;
	                }

	                else {
	                    x = 5 + c*21.651;
	                }

	                double y = r*12.5 + r*12.5 - 6.25*r;

	                if (r == 0 || c == 0 || r == 29 || c == 35)
	                    Hgrid[r][c] = new HNode(r,c,1,x, y+20);
	                else
	                    Hgrid[r][c] = new HNode(r,c,0,x, y+20);
	            }
	        }
	    }

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			 // TODO Auto-generated method stub
			Point2D box = new Point2D.Double(e.getX(),e.getY());
	        System.out.println("X coor: " + e.getX() + " Y coor: " + e.getY());

	        if(e.getX() >= 753 || e.getY() <= 32 || e.getX() <= 24 || e.getY() > 552) {
	            System.out.println("hello there");
	        }

	        else {
		        for (int r = 1; r < 30; r++) {
		            for (int c = 1; c < 35; c++) {
		                if (Hgrid[r][c].getShape().contains(box)) {
		                	System.out.println("new boxX");
		                    boxX = r;
		                    boxY = c;
		                }
		            }
		        }

		        System.out.println("boxX: " + boxX + " boxY: " + boxY);
		        System.out.println();

		        if (e.getButton() == MouseEvent.BUTTON1) {
		        	if (Hgrid[boxX][boxY].getState()== 0) {
		        		Hgrid[boxX][boxY].setState(1);
		            }

		            else if (Hgrid[boxX][boxY].getState()== 1) {
		            	Hgrid[boxX][boxY].setState(0);
		            }
		        }

		        else {
		        	if (!begin && Hgrid[boxX][boxY].getState()== 0) {
		        		begin = true;
		                Hgrid[boxX][boxY].setState(2);
		                beginNode = Hgrid[boxX][boxY];
		        	}

		        	else if (!end && Hgrid[boxX][boxY].getState()== 0) {
		        		end = true;
		        		Hgrid[boxX][boxY].setState(3);
		        		endNode = Hgrid[boxX][boxY];
		        	}
		        }
		            repaint();
	        }
		}


		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	    public void UniformCostSearch() {
	        zeroDist();
	        fringe.add(beginNode);

	        timer = new Timer(10, new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    if(!fringe.isEmpty()) {
	                        currNode = fringe.poll();
	                        System.out.println();
	                        System.out.println("currNode: " + currNode.r + " " + currNode.c);
	                        System.out.println("currNode cost: " + currNode.getTotal());
	                        System.out.println("----------------------");

	                        if(currNode.r == endNode.r && currNode.c == endNode.c) {
	                            foundGoal = true;
	                            trace.add(currNode);
	                            HNode traceNode = currNode;

	                            while(traceNode.parent != beginNode) {
	                                traceNode = traceNode.parent;
	                                trace.add(traceNode);
	                            }
	                            trace.add(currNode);
	                            System.out.println("goal found!");
	                            fringe.clear();
	                            timer.stop();
	                        }

	                        if(foundGoal == false) {
	                            //adding connections
	                            ArrayList<HNode> a = currNode.creatingConnections();
	                            for(int i = 0; i < a.size(); i++) {
	                                neighbor = a.get(i);

	                                if(neighbor.c == beginNode.c && neighbor.r == beginNode.r) {
	                                    neighbor.setState(2);
	                                }

	                                else if(neighbor.c == endNode.c && neighbor.r == endNode.r) {
	                                    neighbor.setState(9);
	                                }

	                                else {
	                                    neighbor.setState(5);
	                                }
	                            }

	                            for(int i = 0; i < a.size(); i++) {
	                                neighbor = a.get(i);

	                                if(visited.contains(neighbor)) {
	                                    //System.out.println("visited money check: " + neighbor.r + " " + neighbor.c + " " + neighbor.getMoney());
	                                }

	                                if (!visited.contains(neighbor) && neighbor.getState() != 1 && neighbor.r > 0 && neighbor.c > 0 && neighbor.r < Hgrid.length && neighbor.c < Hgrid[0].length) {
	                                    double temp = currNode.initializeCost().get(i) + currNode.getMoney();
	                                    neighbor.parent = currNode;
	                                    neighbor.setMoney(temp);
	                                    fringe.add(neighbor);
	                                    visited.add(neighbor);

	                                    if(neighbor.c == beginNode.c && neighbor.r == beginNode.r) {
	                                        neighbor.setState(2);
	                                    }

	                                    else if(neighbor.c == endNode.c && neighbor.r == endNode.r) {
	                                        neighbor.setState(9);
	                                    }

	                                    else {
	                                        neighbor.setState(6);
	                                    }
	                                }
	                            }
	                        }
	                    }

	                    repaint();
	                }
	            });

	        timer.start();
	    }

	    public void GS() {
	        zeroMoney();
	        setDistToGoal();
	        fringe.add(beginNode);

	        timer = new Timer(10, new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    if(!fringe.isEmpty()) {
	                        currNode = fringe.poll();
	                        System.out.println();
	                        System.out.println("currNode: " + currNode.r + " " + currNode.c);
	                        System.out.println("currNode cost: " + currNode.getTotal());
	                        System.out.println("----------------------");

	                        //if the current node is the goal node
	                        //if(currNode.r == Board.endNode.r && currNode.c == Board.endNode.c) {
	                        if(currNode.r == endNode.r && currNode.c == endNode.c) {
	                            foundGoal = true;
	                            trace.add(currNode);
	                            HNode traceNode = currNode;

	                            while(traceNode.parent != beginNode) {
	                                traceNode = traceNode.parent;
	                                trace.add(traceNode);
	                            }
	                            trace.add(currNode);
	                            System.out.println("goal found!");
	                            fringe.clear();
	                            timer.stop();
	                        }

	                        if(foundGoal == false) {
	                            //adding connections
	                            ArrayList<HNode> a = currNode.creatingConnections();
	                            for(int i = 0; i < a.size(); i++) {
	                                neighbor = a.get(i);

	                                //if neighbor is the beginning node
	                                if(neighbor.c == beginNode.c && neighbor.r == beginNode.r) {
	                                    neighbor.setState(2);
	                                }

	                                //if neighbor is the ending node
	                                else if(neighbor.c == endNode.c && neighbor.r == endNode.r) {
	                                    neighbor.setState(9);
	                                }

	                                else {
	                                    neighbor.setState(5);
	                                }
	                            }

	                            for(int i = 0; i < a.size(); i++) {
	                                neighbor = a.get(i);

	                                if(visited.contains(neighbor)) {
	                                    //System.out.println("visited money check: " + neighbor.r + " " + neighbor.c + " " + neighbor.getMoney());
	                                }

	                                if (!visited.contains(neighbor) && neighbor.getState() != 1 && neighbor.r > 0 && neighbor.c > 0 && neighbor.r < Hgrid.length && neighbor.c < Hgrid[0].length) {
	                                    neighbor.parent = currNode;
	                                    fringe.add(neighbor);
	                                    visited.add(neighbor);

	                                    if(neighbor.c == beginNode.c && neighbor.r == beginNode.r) {
	                                        neighbor.setState(2);
	                                    }

	                                    else if(neighbor.c == endNode.c && neighbor.r == endNode.r) {
	                                        neighbor.setState(9);
	                                    }

	                                    else {
	                                        neighbor.setState(6);
	                                    }
	                                }
	                            }
	                        }
	                    }

	                    repaint();
	                }
	            });

	        timer.start();
	    }

	    public void A_Star() {
	        setDistToGoal();
	        fringe.add(beginNode);

	        timer = new Timer(10, new ActionListener() {
	                public void actionPerformed(ActionEvent e) {

	                    if(!fringe.isEmpty()) {
	                        currNode = fringe.poll();
	                        System.out.println();
	                        System.out.println("currNode: " + currNode.r + " " + currNode.c);
	                        System.out.println("currNode cost: " + currNode.getTotal());
	                        System.out.println("----------------------");

	                        //if the current node is the goal node
	                        if(currNode.r == endNode.r && currNode.c == endNode.c) {
	                            foundGoal = true;
	                            trace.add(currNode);
	                            HNode traceNode = currNode;

	                            while(traceNode.parent != beginNode) {
	                                traceNode = traceNode.parent;
	                                trace.add(traceNode);
	                            }
	                            trace.add(currNode);
	                            System.out.println("goal found!");
	                            fringe.clear();
	                            timer.stop();
	                        }

	                        if(foundGoal == false) {
	                            //adding connections
	                            ArrayList<HNode> a = currNode.creatingConnections();
	                            for(int i = 0; i < a.size(); i++) {
	                                neighbor = a.get(i);

	                                //if neighbor is the beginning node
	                                if(neighbor.c == beginNode.c && neighbor.r == beginNode.r) {
	                                    neighbor.setState(2);
	                                }

	                                //if neighbor is the ending node
	                                else if(neighbor.c == endNode.c && neighbor.r == endNode.r) {
	                                    neighbor.setState(9);
	                                }

	                                else {
	                                    neighbor.setState(5);
	                                }
	                            }

	                            for(int i = 0; i < a.size(); i++) {
	                                neighbor = a.get(i);

	                                if(visited.contains(neighbor)) {
	                                    //System.out.println("visited money check: " + neighbor.r + " " + neighbor.c + " " + neighbor.getMoney());
	                                }

	                                if (!visited.contains(neighbor) && neighbor.getState() != 1 && neighbor.r > 0 && neighbor.c > 0 && neighbor.r < Hgrid.length && neighbor.c < Hgrid[0].length) {
	                                    double temp = currNode.initializeCost().get(i) + currNode.getTotal();
	                                    neighbor.parent = currNode;
	                                    neighbor.setTotal(temp);
	                                    fringe.add(neighbor);
	                                    visited.add(neighbor);

	                                    if(neighbor.c == beginNode.c && neighbor.r == beginNode.r) {
	                                        neighbor.setState(2);
	                                    }

	                                    else if(neighbor.c == endNode.c && neighbor.r == endNode.r) {
	                                        neighbor.setState(9);
	                                    }

	                                    else {
	                                        neighbor.setState(6);
	                                    }

	                                }
	                            }
	                        }
	                    }

	                    repaint();
	                }
	            });

	        timer.start();
	    }

	    public void zeroMoney() {
	        for(int r = 0; r < Hgrid.length; r ++) {
	            for(int c = 0; c < Hgrid[0].length; c++) {
	                Hgrid[r][c].setMoney(0);
	            }
	        }
	    }

	    public double calcDist(int x1, int y1, int x2, int y2) {
	        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	    }

	    public void setDistToGoal() {
	        for(int r = 0; r < Hgrid.length; r ++) {
	            for(int c = 0; c < Hgrid[0].length; c++) {
	            	int rr = r%2;
	            	if(rr == 0) {
	            		int rTemp = r/2;
	            		int endNodeRTemp = endNode.r/2;
	            		Hgrid[r][c].setDist(calcDist(c*21+4, rTemp*38+21, endNode.c*21+4, endNodeRTemp*38+21));
	            	}

	            	else {
	            		int rTemp = r/2;
	            		int endNodeRTemp = endNode.r/2;
	            		Hgrid[r][c].setDist(calcDist(c*21+17, rTemp*37+40, endNode.c*21+17, endNodeRTemp*37+40));
	            	}
	            }

	        }
	    }

	    public Timer timer() {
	        // TODO Auto-generated method stub
	        return timer;
	    }

	    public void zeroDist() {
	        for(int r = 0; r < Hgrid.length; r ++) {
	            for(int c = 0; c < Hgrid[0].length; c++) {
	                Hgrid[r][c].setDist(0);
	            }
	        }
	    }
}

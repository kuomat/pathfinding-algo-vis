import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.Timer;

public class Board extends JPanel implements MouseListener, MouseMotionListener{

    public PriorityQueue<GNode> fringe = new PriorityQueue<>();
    public ArrayList<GNode> trace = new ArrayList<>();
    public ArrayList<GNode> visited = new ArrayList<>();
    public GNode currNode, neighbor;
    public boolean foundGoal = false;
    Timer timer;

    public static boolean begin, end, drawUCS, drawGreedy, drawAStar, reset = false;
    public static GNode beginNode, endNode = null;
    public static GNode[][] grid = new GNode[24][32];
    JButton U, G, A, Reset;

    public Board() {
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
                    repaint();
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
        super.paintComponent(g);

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                g.setColor(Color.BLACK);

                //walls
                if (grid[r][c].getState() == 1) {
                	g.setColor(Color.BLACK);
                    g.fillRect(c*800/grid[r].length, r*600/grid.length, 800/grid[r].length, 600/grid.length);
                }

                //empty cell
                else if (grid[r][c].getState() == 0){
                    g.drawRect(c*800/grid[r].length, r*600/grid.length, 800/grid[r].length, 600/grid.length);
                }

                //start node
                else if(grid[r][c].getState() == 2){
                	 g.setColor(new Color(255,255,102));
                    g.fillRect(c*800/grid[r].length+1, r*600/grid.length+1, 800/grid[r].length, 600/grid.length);
                }

                //visited
                else if(grid[r][c].getState() == 5){
                	g.setColor(new Color(255, 153, 255));
                    g.fillRect(c*800/grid[r].length+1, r*600/grid.length+1, 800/grid[r].length-1, 600/grid.length-1);
                }

                //fringe not visited
                else if(grid[r][c].getState() == 6){
                	g.setColor(new Color(0, 225, 225));
                    g.fillRect(c*800/grid[r].length+1, r*600/grid.length+1, 800/grid[r].length-1, 600/grid.length-1);
                }

                else {
                    g.setColor(Color.RED);
                    g.fillRect(c*800/grid[r].length+1, r*600/grid.length+1, 800/grid[r].length, 600/grid.length);
                }
            }
        }

        if (drawUCS) {
            for (int i = 1; i < trace.size(); i++) {
                if (i < trace.size() - 1) {
                    g.setColor(new Color(192,192,192));
                    //g.fillRect(UCS.trace.get(i).c * 25, UCS.trace.get(i).r * 25, 25, 25);
                    g.fillRect(trace.get(i).c*800/grid[trace.get(i).r].length+1, trace.get(i).r*600/grid.length+1, 800/grid[0].length-1, 600/grid.length-1);
                }
            }
        }

        if (drawGreedy) {
            for (int i = 1; i < trace.size(); i++) {
                if (i < trace.size() - 1) {
                	g.setColor(new Color(192,192,192));
                    //g.fillRect(UCS.trace.get(i).c * 25, UCS.trace.get(i).r * 25, 25, 25);
                    g.fillRect(trace.get(i).c*800/grid[trace.get(i).r].length+1, trace.get(i).r*600/grid.length+1, 800/grid[0].length-1, 600/grid.length-1);
                }
            }
        }

        if (drawAStar) {
            for (int i = 1; i < trace.size(); i++) {
                if (i < trace.size() - 1) {
                	g.setColor(new Color(192,192,192));
                    //g.fillRect(UCS.trace.get(i).c * 25, UCS.trace.get(i).r * 25, 25, 25);
                    g.fillRect(trace.get(i).c*800/grid[trace.get(i).r].length+1, trace.get(i).r*600/grid.length+1, 800/grid[0].length-1, 600/grid.length-1);
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
        for(int r = 0; r < grid.length; r ++) {
            for(int c = 0; c < grid[0].length; c++) {
                if(r == 0 || c == 0 || r == grid.length-1 || c == grid[0].length-1) {
                    grid[r][c] = new GNode(r,c,1);
                }

                else {
                    grid[r][c] = new GNode(r,c,0);
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("X coor: " + e.getX() + " Y coor: " + e.getY());

        if(e.getX() >= 775 || e.getY() > 570 || e.getX() < 25 || e.getY() < 25) {
            System.out.println("hello there");
        }

        else {
            int x = e.getX()/(800/grid[0].length);
            int y = e.getY()/(600/grid.length);

            System.out.println("X: " + x + " Y: " + y);
            System.out.println();

            if (e.getButton() == MouseEvent.BUTTON1) {
                if (grid[y][x].getState()== 0) {
                    grid[y][x].setState(1);
                }

                else if (grid[y][x].getState()== 1) {
                    grid[y][x].setState(0);
                }

            }

            else {
                if (!begin && grid[y][x].getState()== 0) {
                    begin = true;
                    grid[y][x].setState(2);
                    beginNode = grid[y][x];
                }

                else if (!end && grid[y][x].getState()== 0) {
                    end = true;
                    grid[y][x].setState(3);
                    endNode = grid[y][x];
                }
            }
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
                            GNode traceNode = currNode;

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
                            ArrayList<GNode> a = currNode.creatingConnections();
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

                                if (!visited.contains(neighbor) && neighbor.getState() != 1 && neighbor.r > 0 && neighbor.c > 0 && neighbor.r < Board.grid.length && neighbor.c < Board.grid[0].length) {
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
                            GNode traceNode = currNode;

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
                            ArrayList<GNode> a = currNode.creatingConnections();
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

                                if (!visited.contains(neighbor) && neighbor.getState() != 1 && neighbor.r > 0 && neighbor.c > 0 && neighbor.r < Board.grid.length && neighbor.c < Board.grid[0].length) {
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
        fringe.add(Board.beginNode);

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
                            GNode traceNode = currNode;

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
                            ArrayList<GNode> a = currNode.creatingConnections();
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

                                if (!visited.contains(neighbor) && neighbor.getState() != 1 && neighbor.r > 0 && neighbor.c > 0 && neighbor.r < Board.grid.length && neighbor.c < Board.grid[0].length) {
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
        for(int r = 0; r < Board.grid.length; r ++) {
            for(int c = 0; c < Board.grid[0].length; c++) {
                Board.grid[r][c].setMoney(0);
            }
        }
    }

    public double calcDist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public void setDistToGoal() {
        for(int r = 0; r < Board.grid.length; r ++) {
            for(int c = 0; c < Board.grid[0].length; c++) {
                Board.grid[r][c].setDist(calcDist(c*25+12.5, r*25+12.5, Board.endNode.c*25+12.5, Board.endNode.r*25+12.5));
            }
        }
    }

    public Timer timer() {
        // TODO Auto-generated method stub
        return timer;
    }

    public void zeroDist() {
        for(int r = 0; r < Board.grid.length; r ++) {
            for(int c = 0; c < Board.grid[0].length; c++) {
                Board.grid[r][c].setDist(0);
            }
        }
    }

}


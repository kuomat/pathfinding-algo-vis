import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.swing.JFrame;

// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;

public class runner extends JFrame{
	Board board = new Board();
	HexGrid hg = new HexGrid();

	public runner() {
		choice();
		this.setTitle("08-pathFindingGrid");
		this.getContentPane().setPreferredSize(new Dimension (1000,600));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getPreferredSize().width/2, dim.height/2-this.getPreferredSize().height/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.pack();
		this.pack();
	}

	public void choice() {
		String s = javax.swing.JOptionPane.showInputDialog(null, "Which grid do you want? h: hex; s: square");
		if(s.equals("h")) {
			this.add(hg);
		}
		else if(s.equals("s")){
			this.add(board);
		}

		else {
			choice();
		}
	}

	public static void main(String [] args) {
		runner r = new runner();
	}
}

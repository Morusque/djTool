import java.util.Vector;

import javax.swing.JFrame;

public class Run extends JFrame {
	private static final long serialVersionUID = 1L;

	Design design;
	FileSystem fileSystem;	
	Interaction interaction;
	CellsManager cellsManager;
	TracksManager tracksManager;	
	
	Window window;
	Vector<Cell> cells = new Vector<Cell>();
	Vector<Track> tracks = new Vector<Track>();
	
	int frameRate = 25;
	
	public static void main(String[] args) {
		Run r = new Run();
		r.loop();
	}

	Run() {
		design = new Design();
		fileSystem = new FileSystem(this);	
		window = new Window(this);
		interaction = new Interaction(this);
		cellsManager = new CellsManager(this);
		tracksManager = new TracksManager(this);
		window.set();
		cells = cellsManager.cellsPanel();
		tracksManager.newTrack();
	}

	private void loop() {
		while (!window.keys[27]) {
			long lastTime = System.currentTimeMillis();
			window.displayCells(cells);			// Run at specified frame rate
			if (lastTime < System.currentTimeMillis()) {
				// A small sleep prevents the game from eating all the cpu power
				try {
					Thread.sleep(Math.max(1000 / frameRate - (System.currentTimeMillis() - lastTime), 0));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lastTime = System.currentTimeMillis();
			}
		}
		// makes sure nothing is left in the background
		System.exit(0);
		
	}


	
}

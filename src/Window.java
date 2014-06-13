import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends Canvas  {
	private static final long serialVersionUID = 1L;

	Run parent;
	
	private BufferStrategy strategy;
	private Graphics2D g;
	protected int width = 1200;
	protected int height = 600;

	protected boolean[] keys = new boolean[0xFFFF];
	protected Dimension mouse = new Dimension(0,0);
	protected int mouseButton = 0;

	int nbSongsInList = 20;
	
	public Window(Run parent) {
		this.parent=parent;
	}
	
	public void set() {
		this.enableEvents(KeyEvent.KEY_PRESSED | MouseEvent.MOUSE_DRAGGED
				| MouseEvent.MOUSE_MOVED | MouseEvent.MOUSE_PRESSED
				| MouseEvent.MOUSE_RELEASED);		
		JFrame container = new JFrame("Window");
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);
		setBounds(0, 0, width, height);
		panel.add(this);
		setIgnoreRepaint(true);
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		requestFocus();
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	public void displayCells(Vector<Cell> cells) {
		g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(new Color(Design.backgroundColor()));
		g.fillRect(0,0,width,height);
		for (int i = 0; i < cells.size(); i++) {
			cells.get(i).display(g);
		}
		g.dispose();
		strategy.show();
	}
	
	public void processEvent(AWTEvent e) {
		switch (e.getID()) {
		case WindowEvent.WINDOW_CLOSING:
			System.exit(0);
		case MouseEvent.MOUSE_RELEASED:
			mouseReleased();
			break;
		case MouseEvent.MOUSE_PRESSED:
			mouseButton = ((MouseEvent) e).getButton();
			mousePressed();
		case MouseEvent.MOUSE_MOVED:
		case MouseEvent.MOUSE_DRAGGED:
			mouse.width = ((MouseEvent) e).getX();
			mouse.height = ((MouseEvent) e).getY();
			break;
		case KeyEvent.KEY_PRESSED:
			keys[((KeyEvent) e).getKeyCode()] = e.getID() == KeyEvent.KEY_PRESSED;
			parent.interaction.keyPressed(keys,(KeyEvent) e);
			break;
		case KeyEvent.KEY_RELEASED:
			keys[((KeyEvent) e).getKeyCode()] = e.getID() == KeyEvent.KEY_PRESSED;
			parent.interaction.keyReleased(keys,((KeyEvent) e));
			break;
		}
	}

	private void mousePressed() 
	{
	}

	private void mouseReleased() 
	{
		for (int i = 0;i<parent.cells.size();i++) {
			if (mouse.width>parent.cells.get(i).getX() && 
				mouse.width<parent.cells.get(i).getX() + parent.cells.get(i).getW() &&
				mouse.height>parent.cells.get(i).getY() && 
				mouse.height<parent.cells.get(i).getY() + parent.cells.get(i).getH()) 
			{
				parent.interaction.buttonUsed(	parent.cells.get(i).getId(),
												parent.cells.get(i).getCopy());
				parent.cellsManager.selectButton(i);
			}
		}
	}

}

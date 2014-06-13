import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class Cell {
	private String id;
	private int copy;
	private float x;
	private float y;
	private float w;
	private float h;
	private int passiveColor;
	private int passiveBorder;
	private int overColor;
	private int overBorder;
	private int activeColor;
	private int activeBorder;
	private int selectedColor;
	private int selectedBorder;
	protected String value;
	private float margins;
	private boolean active;
	private boolean selected;
	
	Window window;
	
	public Cell(String id, int copy, float x, float y, float w, float h,
			int passiveColor, int passiveBorder, 
			int overColor, int overBorder, 
			int activeColor, int activeBorder,
			int selectedColor, int selectedBorder,
			String value, Window window) {
		this.id = id;
		this.copy = copy;		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.passiveColor = passiveColor;
		this.passiveBorder = passiveBorder;
		this.overColor = overColor;
		this.overBorder = overBorder;
		this.activeColor = activeColor;
		this.activeBorder = activeBorder;
		this.selectedColor = selectedColor;
		this.selectedBorder = selectedBorder;
		this.value = value;		
		this.margins = Design.buttonMargins();
		this.active=false;
		this.selected=false;
		this.window=window;
	}

	public Cell(String label, int copy, float[] pX, float[] pY, int x, int y, String value, Window window) {
		this.id = label;
		this.copy = copy;
		this.x = pX[x];
		this.y = pY[y];
		this.w = pX[x + 1] - pX[x];
		this.h = pY[y + 1] - pY[y];
		this.passiveColor = Design.color(0, 0, 0);
		this.passiveBorder = Design.color(0, 1, 0);
		this.overColor = Design.color(0, 0, 1);
		this.overBorder = Design.color(0, 1, 1);
		this.activeColor = Design.color(0, 0, 2);
		this.activeBorder = Design.color(0, 1, 2);
		this.selectedColor = Design.color(0, 0, 3);
		this.selectedBorder = Design.color(0, 1, 3);
		this.value = value;
		this.margins = Design.buttonMargins();
		this.active=false;
		this.selected=false;
		this.window=window;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void display(Graphics2D g) {
		RoundRectangle2D rect = new RoundRectangle2D.Float(
				(int) (x + margins), (int) (y + margins),
				(int) (w - 2 * margins), (int) (h - 2 * margins),
				Design.rounding(),Design.rounding());
		g.setStroke(new BasicStroke(1.0f));
		g.setColor(new Color(currentBorder()));
		g.draw(rect);
		g.setColor(new Color(currentColor()));
		g.fill(rect);
		g.setColor(new Color(Design.textColor()));
		g.drawString(value, (int)(rect.getX() + margins), (int)(rect.getY() + rect.getHeight()*2/3));
	}
	private int currentColor() {
		if (selected) return selectedColor;
		if (mouseOver()) return overColor;
		if (active) return activeColor;
		return passiveColor;
	}

	private boolean mouseOver() {
		if (window.mouse.width>x &&
			window.mouse.height>y &&
			window.mouse.width<x+w &&
			window.mouse.height<y+h )
			return true;
		return false;
	}

	private int currentBorder() {
		if (selected) return selectedBorder;
		if (mouseOver()) return overBorder;
		if (active) return activeBorder;
		return passiveBorder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCopy() {
		return copy;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}

	public void switchActive() {
		active ^= true;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void write(char c) {
	}

	public void backspace() {
	}
	
	
}

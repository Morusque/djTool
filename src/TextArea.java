public class TextArea extends Cell {

	TextArea(String label, int copy, float x, float y, float w, float h, String value, Window window) 
	{
		super(	label, copy, x, y, w, h, 
				Design.color(2,0,0),Design.color(2,1,0),
				Design.color(2,0,1),Design.color(2,1,1),
				Design.color(2,0,2),Design.color(2,1,2),
				Design.color(2,0,3),Design.color(2,1,3), 
				value, window);
	}

	public TextArea(String label, int copy, float[] pX, float[] pY, int x,
			int y, String value, Window window) {
		super(label, copy, pX[x], pY[y], pX[x + 1] - pX[x], pY[y + 1] - pY[y],
				Design.color(2,0,0), Design.color(2,1,0),
				Design.color(2,0,1),Design.color(2,1,1),
				Design.color(2,0,2),Design.color(2,1,2),
				Design.color(2,0,3),Design.color(2,1,3),
				value, window);
	}

	public void write(char c) {
		value += c;
	}

	public void backspace() {
		if (value.length()>0) value = value.substring(0,value.length()-1);
	}

}

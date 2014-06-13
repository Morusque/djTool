
public class Button extends Cell 
{

	Button(String label, int copy, float x, float y, float w, float h, String value, Window window) {
		super(	label, copy, x, y, w, h, 
				Design.color(1,0,0),Design.color(1,1,0),
				Design.color(1,0,1),Design.color(1,1,1),
				Design.color(1,0,2),Design.color(1,1,2),
				Design.color(1,0,3),Design.color(1,1,3),				
				value, window);
	}

	public Button(String label, int copy, float[] pX, float[] pY, int x, int y, String value, Window window) {
		super(	label, copy, pX[x], pY[y], pX[x+1]-pX[x], pY[y+1]-pY[y], 
				Design.color(1,0,0),Design.color(1,1,0),
				Design.color(1,0,1),Design.color(1,1,1),
				Design.color(1,0,2),Design.color(1,1,2),
				Design.color(1,0,3),Design.color(1,1,3),
				value, window);
	}
	
}


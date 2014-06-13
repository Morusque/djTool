public class Design {

	Design() {
	}

	public static float buttonMargins() {
		return 1;
	}

	public static int backgroundColor() {
		return 0x202020;
	}

	public static int textColor() {
		return 0x000000;
	}

	public static float rounding() {
		return 5.0f;
	}

	public static int color(int what, int where, int how) {
		// what
		// 0 = basic
		// 1 = button
		// 2 = textArea
		// 3 = checkBox
		// where
		// 0 = fill
		// 1 = outline
		// how
		// 0 = passive
		// 1 = over
		// 2 = active
		// 3 = selected
		int color = 0x909090;
		if (what == 0) color += 0x303000;
		if (what == 1) color += 0x300030;
		if (what == 2) color += 0x303030;
		if (what == 3) color += 0x003030;
		if (where == 0) color += 0x101010;
		if (how == 1) color += 0x202000;
		if (how == 2) color += 0x002020;
		if (how == 3) color += 0x202020;
		return color;
	}

}

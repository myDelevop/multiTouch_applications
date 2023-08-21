package wwf.ClassificationGame.util;

import org.mt4j.util.MTColor;

public class MyColors {
	private MTColor color = MTColor.randomColor();

	/**
	 * Colori disponibili per le specie
	 */
	public void setColor(String col) {
		if (col.equalsIgnoreCase("arancione"))
			color = new MTColor(255, 192, 0);
		if (col.equalsIgnoreCase("azzurro"))
			color = new MTColor(30, 184, 234);
		if (col.equalsIgnoreCase("blu"))
			color = new MTColor(68, 114, 196);
		if (col.equalsIgnoreCase("verde"))
			color = new MTColor(60,	179, 113);
		if (col.equalsIgnoreCase("viola"))
			color = new MTColor(167, 95, 231);
	}

	public static MTColor lightorange() {
		MTColor darkorange = new MTColor(251, 140, 0);
		return darkorange;
	}
	
	public static MTColor orange() {
		MTColor orange = new MTColor(255, 192, 0);
		return orange;
	}
	
	public static MTColor red() {
		MTColor red = new MTColor(204, 62, 60);
		return red;
	}
	
	public static MTColor lightgray() {
		MTColor lightgray = new MTColor(222, 235, 247);
		return lightgray;
	}

	public static MTColor lightblue() {
		MTColor lightblue = new MTColor(189, 215, 238);
		return lightblue;
	}
	
	public static MTColor azzurro() {
		MTColor lightblue = new MTColor(51, 153, 255);
		return lightblue;
	}
	
	public static MTColor lightyellow() {
		MTColor lightyellow = new MTColor(255, 255, 204);
		return lightyellow;
	}
	
	public static MTColor ocra() {
		MTColor ocra = new MTColor(237, 125, 49);
		return ocra;
	}
	
	public static MTColor grigioverde() {
		MTColor grigioverde = new MTColor(129, 192, 151);
		return grigioverde;
	}
	
	public static MTColor lavanda() {
		MTColor lavanda = new MTColor(191, 191, 255);
		return lavanda;
	}

	public MTColor getColor() {
		return color;
	}

}

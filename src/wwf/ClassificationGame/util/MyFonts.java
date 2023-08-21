package wwf.ClassificationGame.util;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.util.MTColor;
import org.mt4jx.util.MTColors;

public class MyFonts {
	public static IFont titoloSplashScene(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "berlinsansfb.ttf", 110,
				MyColors.orange());
	}

	public static IFont sottotitoloSplashScene(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "caviardreams.ttf", 48,
				MyColors.orange());
	}

	public static IFont specieSplashScene(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "times.ttf", 36, MTColor.WHITE);
	}

	public static IFont titoloGioco(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "berlinsansfb.ttf", 110,
				MTColors.WHITE);
	}

	public static IFont sottotitoloGioco(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "caviardreams.ttf", 48,
				MTColors.WHITE);
	}

	public static IFont descrizioneEntita(MTApplication context) {
		return FontManager.getInstance().createFont(context, "arial.ttf", 14, MTColor.BLACK);
	}

	public static IFont titoloRegole(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibri.ttf", 45, MyColors.orange());
	}

	public static IFont corpoRegole(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibri.ttf", 35, MTColors.WHITE);
	}

	public static IFont fineRegole(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibri.ttf", 35, MyColors.orange());
	}

	public static IFont domandaGioco(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "caviardreams.ttf", 38,
				MTColor.WHITE);
	}

	public static IFont specieGioco(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "times.ttf", 48, MTColor.WHITE);
	}

	public static IFont titoloPopupRosso(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibribold.ttf", 55, MTColors.RED);
	}
	
	public static IFont titoloPopupVerde(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibribold.ttf", 55, MTColors.GREEN);
	}

	public static IFont testoPopup(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibri.ttf", 40, MTColors.BLACK);
	}
	
	public static IFont titoloFine(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibribold.ttf", 55, MyColors.orange());
	}
	
	public static IFont testoFine(MTApplication context) {
		return FontManager.getInstance().createFont(context, FilesPath.rootFont + "calibri.ttf", 40, MTColors.WHITE);
	}
}

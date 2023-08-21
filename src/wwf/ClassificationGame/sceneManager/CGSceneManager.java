/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 C.Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package wwf.ClassificationGame.sceneManager;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.sceneManagement.AbstractScene;

import wwf.ClassificationGame.data.DAO.CGCategoriaDAOImpl;
import wwf.ClassificationGame.data.DAO.CGClasseDAOImpl;
import wwf.ClassificationGame.data.DAO.CGEntitaDAOImpl;
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.data.model.CGClasse;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.view.Information;
import wwf.ClassificationGame.view.LoadingScene;
import wwf.ClassificationGame.view.SplashScene;
import wwf.ClassificationGame.view.game.FineGioco;
import wwf.ClassificationGame.view.game.GameRegole;
import wwf.ClassificationGame.view.game.MultiPlayer;
import wwf.ClassificationGame.view.game.ScegliSpecie;
import wwf.ClassificationGame.view.game.SinglePlayer;

public class CGSceneManager extends AbstractScene {
	private MTApplication mtApplication;
	private MTCanvas canvas;
	private CGClasse specie;
	private CGCategoria categoria;
	private CGEntita entita;
	private String game;
	private MTRectangle exit;
	private static List <CGClasse> listaClassi;
	private static List <CGCategoria> listaCategorie;
	private static List <CGEntita> listaEntita;

	public CGSceneManager(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.mtApplication = mtApplication;
		try {
			loadClassi();
			loadCategorie();
			loadEntita();
			buildSplashScreen();
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	public void buildLoadingScene() throws SQLException {
		System.gc();
		canvas = getCanvas();
		LoadingScene ls = new LoadingScene(this, mtApplication);
		canvas.addChild(ls.getRootComponent());
	}

	public void buildSplashScreen() throws SQLException {
		System.gc();
		canvas = getCanvas();
		SplashScene sp = new SplashScene(this, mtApplication);
		canvas.addChild(sp.getRootComponent());
	}
	
	public static void loadClassi() throws SQLException {
		CGClasse classe = new CGClasse();
		setListaClassi(new CGClasseDAOImpl().cercaAllClassi(classe));
	}
	
	public static void loadCategorie() throws SQLException {
		CGCategoria categoria = new CGCategoria();
		setListaCategorie(new CGCategoriaDAOImpl().cercaAllCategorie(categoria));
	}
	
	public static void loadEntita() throws SQLException {
		CGEntita entita = new CGEntita();
		setListaEntita(new CGEntitaDAOImpl().cercaAllEntita(entita));
	}

	public void buildInformationScene() throws SQLException {
		canvas.removeAllChildren();
		canvas.destroy();
		System.gc();
		Information inf = new Information(this, mtApplication);
		canvas.addChild(inf.getRootComponent());
	}

	public void buildGameRegoleScene() throws SQLException {
		canvas.removeAllChildren();
		canvas.destroy();
		System.gc();
		GameRegole gr = new GameRegole(this, mtApplication);
		canvas.addChild(gr.getRootComponent());
	}

	public void buildScegliSpecieScene() throws SQLException {
		canvas.removeAllChildren();
		canvas.destroy();
		System.gc();
		ScegliSpecie ss = new ScegliSpecie(this, mtApplication);
		canvas.addChild(ss.getRootComponent());
	}

	public void buildSinglePlayerScene(Giocatore g) throws SQLException {
		canvas.removeAllChildren();
		canvas.destroy();
		System.gc();
		SinglePlayer sp = new SinglePlayer(this, mtApplication, g);
		canvas.addChild(sp.getRootComponent());
	}

	public void buildMultiPlayerScene(Giocatore g1, Giocatore g2) throws SQLException {
		canvas.removeAllChildren();
		canvas.destroy();
		System.gc();
		MultiPlayer mp = new MultiPlayer(this, mtApplication, g1, g2);
		canvas.addChild(mp.getRootComponent());
	}
	
	public void buildFineGioco(Giocatore g) throws SQLException {
		canvas.removeAllChildren();
		canvas.destroy();
		System.gc();
		FineGioco fg = new FineGioco(this, mtApplication, g);
		canvas.addChild(fg.getRootComponent());
	}

	public MTApplication getMtApplication() {
		return mtApplication;
	}

	public void setMtApplication(MTApplication mtApplication) {
		this.mtApplication = mtApplication;
	}

	public void onEnter() {
		getMTApplication().registerKeyEvent(this);
	}

	public void onLeave() {
		getMTApplication().unregisterKeyEvent(this);
	}

	public void keyEvent(KeyEvent e) {
		int evtID = e.getID();
		if (evtID != KeyEvent.KEY_PRESSED)
			return;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_F:
			System.out.println("FPS: " + mtApplication.frameRate);
			break;
		default:
			break;
		}
	}

	public CGClasse getClasse() {
		return specie;
	}

	public void setClasse(CGClasse specie) {
		this.specie = specie;
	}

	public CGCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(CGCategoria categoria) {
		this.categoria = categoria;
	}

	public CGEntita getEntita() {
		return entita;
	}

	public void setEntita(CGEntita entita) {
		this.entita = entita;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public MTRectangle getExit() {
		return exit;
	}

	public void setExit(MTRectangle backRect) {
		this.exit = backRect;
	}

	public List <CGClasse> getListaClassi() {
		return listaClassi;
	}

	public static void setListaClassi(List <CGClasse> listaClassi) {
		CGSceneManager.listaClassi = listaClassi;
	}

	public List <CGCategoria> getListaCategorie() {
		return listaCategorie;
	}

	public static void setListaCategorie(List <CGCategoria> listaCategorie) {
		CGSceneManager.listaCategorie = listaCategorie;
	}

	public List <CGEntita> getListaEntita() {
		return listaEntita;
	}

	public static void setListaEntita(List <CGEntita> listaEntita) {
		CGSceneManager.listaEntita = listaEntita;
	}

}

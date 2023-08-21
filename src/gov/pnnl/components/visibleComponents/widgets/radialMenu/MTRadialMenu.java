/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.widgets.radialMenu;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.item.MTMenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.util.animation.AnimationUtil;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PPolygon;

/**
 * A radial menu
 * <p>
 * This menu is intended to be a proof-of-concept of a full-featured radial menu using MT4j<br>
 * Many of the default values defined in this class are used to determine arched button calculations<br>
 * These default values are approximations and should be replaced by general equations in the future
 * <p>
 * <b>Note: The default radius is 200. Scale to the size needed using the scale factor.</b>
 * 
 * @author Ryan LaMothe
 * 
 */
public class MTRadialMenu extends MTEllipse {

	boolean menuBuilded = false;

	// Public Constants
	public static final String NAME = "MTRadialMenu";

	// Default values
	private final float radius = 200f;

	private final float buttonXCoordAdj = 10f;

	private final float buttonTopWidthAdj = 47.5f;

	private final float buttonBottomWidthAdj = 47.5f;

	private final float topArcHeight = 80f;

	private final float topArcHeightAdj = 30f;

	// Fields
	private final PApplet pApplet;

	private final Vector3D center;

	private final IFont font;

	private float buttonHeight = 0;

	private float buttonTopWidth = 0;

	private float buttonBottomWidth = 0;

	private String closeButtonText = "Close";

	// Default MTColor is RED
	private MTColor centerButtonFillColor = new MTColor(255, 0, 0, 255);

	private List<MTRadialPoly> menuItems = new CopyOnWriteArrayList<MTRadialPoly>();

	List<MTMenuItem> mainMenuItems;

	MTEllipse closeButton = null;

	private MTPolygon referencePoly;

	LinkedList<MTRadialPoly> mainMenuItemList = new LinkedList<MTRadialPoly>();
	
	boolean visible = false;

	/**
	 * Full constructor
	 * 
	 * @param pApplet
	 *          The pApplet
	 * @param centerPoint
	 *          The center point of the circle
	 * @param font
	 *          The font to use for the menu items
	 * @param scaleFactor
	 *          The scale factor of the entire menu. Default radius is 200.
	 * @param menuItems
	 *          The list of menu items and sub-menu items to display in the radial menu
	 */
	public MTRadialMenu(final PApplet pApplet, final Vector3D centerPoint, final IFont font, final float scaleFactor, final List<MTMenuItem> menuItems) {
		super(pApplet, centerPoint, 200, 200);

		this.pApplet = pApplet;
		
		PImage texture = pApplet.loadImage(System.getProperty("user.dir")+File.separator + "src" + File.separator + File.separator + "crono"+  File.separator + File.separator  +"image"+ File.separator + File.separator+ 
		"menu_image.png");
		
		setTexture(texture);

		this.setName(NAME);

		this.center = this.getCenterPointLocal();

		final IFont f = FontManager.getInstance().createFont(pApplet, "arial.ttf",
				16, // Font size
				new MTColor(255, 255, 255, 255), // Font fill color
				new MTColor(255, 255, 255, 255), // Font fill color
				true); // Anti-alias

		this.font = f;

		// Default MTColor is GREY
		this.setFillColor(new MTColor(64, 163, 251, 220));

		this.setNoStroke(true);

		// Starts out small and then zooms out to its actual size
		//AnimationUtil.scaleIn(this);

		this.createCloseButton();

		mainMenuItems = menuItems;

		this.createMainMenuItems(menuItems);

		// Allow the user to scale the menu to the desired size
		this.scaleGlobal(scaleFactor, scaleFactor, scaleFactor, centerPoint);
		
		setGestureAllowance(TapProcessor.class, true);
		
		addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge.getId() == 2){
					visible = !visible;
					if(visible==true){
						setMainMenuVisible(visible);
					}
					else{
						setMainMenuVisible(visible);
					}
				}
				return false;
			}
		});
		
		//buildMenuButton();
	}
	

	private void buildMenuButton() {
		PImage texture = pApplet.loadImage(System.getProperty("user.dir")+File.separator + "src" + File.separator + File.separator + "crono"+  File.separator + File.separator  +"image"+ File.separator + File.separator+ 
		"menu_image.png");
		MTImageButton menuImage = new MTImageButton(pApplet, texture);
		addChild(menuImage);
		menuImage.setPositionGlobal(getCenterPointGlobal());
		menuImage.setNoStroke(true);
		setMainMenuVisible(true);
	}
	
	public void setMainMenuVisible(boolean visible) {
		LinkedList<MTRadialPoly> mainMenu = getMainMenuItemList();
		Iterator<MTRadialPoly> itMenu = mainMenu.iterator();
		while (itMenu.hasNext()) {
			MTRadialPoly mtRadialPoly = (MTRadialPoly) itMenu.next();

			List<MTRadialPoly> subMenus = mtRadialPoly.getSubMenuItems();
			Iterator<MTRadialPoly> it = subMenus.iterator();
			while (it.hasNext()) {
				MTRadialPoly subMenu = (MTRadialPoly) it.next();
				subMenu.setEnabled(false);
				subMenu.setVisible(false);
			}
			mtRadialPoly.setEnabled(visible);
			mtRadialPoly.setVisible(visible);
		}
	}


	/**
	 * Retrieve the text used for the Close button
	 * <p>
	 * <b>Note: The default text is "Close"</b>
	 * 
	 * @return the closeButtonText
	 */
	public String getCloseButtonText() {
		return this.closeButtonText;
	}

	/**
	 * Set the text used for the Close button
	 * 
	 * @param closeButtonText
	 *          the closeButtonText to set
	 */
	public void setCloseButtonText(final String closeButtonText) {
		this.closeButtonText = closeButtonText;
	}

	/**
	 * Retrieve the MTColor used for the center button fillColor
	 * 
	 * @return the centerButtonFillColor
	 */
	protected MTColor getCenterButtonFillColor() {
		return this.centerButtonFillColor;
	}

	/**
	 * Set the MTColor used for the center button fillColor
	 * 
	 * @param centerButtonFillColor
	 *          the centerButtonFillColor to set
	 */
	protected void setCenterButtonFillColor(final MTColor centerButtonFillColor) {
		this.centerButtonFillColor = centerButtonFillColor;
	}

	/**
	 * Retrieve the list of menu items
	 * 
	 * @return the menuItems
	 */
	protected List<MTRadialPoly> getMenuItems() {
		return this.menuItems;
	}

	/**
	 * Set the list of menu items
	 * 
	 * @param menuItems
	 *          the menuItems to set
	 */
	protected void setMenuItems(final List<MTRadialPoly> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * Add a menu item to an existing list of menu items
	 * 
	 * @param menuItem
	 */
	protected void addMenuItem(final MTRadialPoly menuItem) {
		if (this.getMenuItems() != null) {
			this.getMenuItems().add(menuItem);
		}
	}

	/**
	 * Removes all of the sub-menu items at or above a specified menu level
	 * 
	 * @param subMenuLevel
	 */
	protected void removeAllMenuItems(final float subMenuLevel) {
		// Retrieve all menu items at or above the specified menu level
		for (final MTRadialPoly item : this.getMenuItems()) {
			if (item.getSubMenuLevel() >= subMenuLevel) {
				//item.setFillColor(item.getUnSelectedColor());
				//item.setSubMenuActive(false);
				//item.removeAllMenuItems();

				// Default animation for removing menu items
				//AnimationUtil.scaleOut(item, false);
			}
		}
	}

	/**
	 * Removes all of the active sub-menu items at a specified menu level
	 * 
	 * @param subMenuLevel
	 */
	public void removeAllActiveMenuItems(final float subMenuLevel) {
		// Retrieve all menu items at the specified menu level
		for (final MTRadialPoly item : this.getMenuItems()) {
			if ((item.getSubMenuLevel() == subMenuLevel) && item.isSubMenuActive()) {
				// item.setFillColor(item.getUnSelectedColor());
				item.setSubMenuActive(false);

				item.removeAllMenuItems();
			}
		}
	}

	/**
	 * Creates the main menu items
	 * 
	 * @param menuItems
	 *          The list of menu items to create
	 */
	private void createMainMenuItems(final List<MTMenuItem> menuItems) {

		// Note: Math.PI can be divided by different numbers to produce different button configurations
		final float angularDiv = ((float) Math.PI) / 5;

		this.buttonHeight = this.radius / 2;
		final float innerRadius = this.radius + 60f;
		final float outerRadius = innerRadius + this.buttonHeight;
		final float halfOuterCircumference = (float) (Math.PI * outerRadius);
		final float halfInnerCircumference = (float) (Math.PI * innerRadius);
		this.buttonTopWidth = halfOuterCircumference / 7f;
		this.buttonBottomWidth = halfInnerCircumference / 7f;


		for (int i = 0; i < menuItems.size(); i++) {

			final MTMenuItem mtMenuItem = menuItems.get(i);

			// Calculate the proper angle
			final float angle = angularDiv * (5 + i + ((6 - menuItems.size()) / 2f));

			/*
			 * X coordinate: radius * cos(angle) where angle is in radians
			 * Y coordinate: radius * sin(angle) where angle is in radians
			 */
			final float xCoord = this.center.x + (innerRadius * (float) Math.cos(angle));
			final float yCoord = this.center.y + (innerRadius * (float) Math.sin(angle));

			MTRadialPoly mainMenuItem = null;

			// Main menu item
			final Vertex[] newVertices = this.calculateArchedRectangleVertices(this.center.x, this.center.y, this.center.z, 0,
					this.buttonTopWidth + (this.buttonTopWidthAdj * 0),
					this.topArcHeight + (this.topArcHeightAdj * 0),
					this.buttonBottomWidth + (this.buttonBottomWidth * .05f) + (this.buttonBottomWidthAdj * 0),
					20f + (this.buttonXCoordAdj * 0), true, 50);
			mainMenuItem = new MTRadialPoly(this.pApplet, newVertices, this.center, this.buttonHeight, this.radius, mtMenuItem.getMenuText(), null, null);


			// Add all inputEventListeners
			for (final IMTInputEventListener inputEventListener : mtMenuItem.getInputEventListeners()) {
				mainMenuItem.addInputListener(inputEventListener);
				mainMenuItem.getMtTextArea().addInputListener(inputEventListener);
			}

			// Add all gestureEventListeners
			for (final Entry<Class<? extends IInputProcessor>, IGestureEventListener> entry : mtMenuItem.getGestureEventListeners().entrySet()) {
				mainMenuItem.addGestureListener(entry.getKey(), entry.getValue());
				mainMenuItem.getMtTextArea().addGestureListener(entry.getKey(), entry.getValue());
			}


			//set enabled menu
			if(!mtMenuItem.isEnable())
				mainMenuItem.setFillColor(new MTColor(0, 0, 0, 50));
			mainMenuItem.setEnabled(mtMenuItem.isEnable());

			// Rotate the menu item
			final float degrees = ((float) Math.toDegrees(angle)) - 270f;
			mainMenuItem.setLocalMatrix(Matrix.getZRotationMatrix(this.center, degrees));

			// Sub-menu items
			if (mtMenuItem.getSubMenuItems() != null) {
				this.createSubMenuItems(mtMenuItem.getSubMenuItems(), mainMenuItem, 1);
			}

			// Position the main menu button
			mainMenuItem.setPositionRelativeToParent(new Vector3D(xCoord, yCoord, 0));

			// Add to the root object
			this.addChild(mainMenuItem);

			//set the font's color to maximum contrast color relative to item background
			//mainMenuItem.getMtTextArea().getFont().setFillColor(new MTColor(Math.abs(255 -  mainMenuItem.getUnSelectedColor().getR()) , Math.abs(255 -  mainMenuItem.getUnSelectedColor().getG()), Math.abs(255 -  mainMenuItem.getUnSelectedColor().getB()) ));


			// Add to the global menu item list
			this.addMenuItem(mainMenuItem);

			mainMenuItemList.add(mainMenuItem);
		}
	}


	/**
	 * Creates the sub-menu items
	 * 
	 * @param subMenuItems
	 *          The list of sub-menu items
	 * @param menuItemPolygon
	 *          The menu item to add the created sub-menu items
	 * @param subMenuLevel
	 *          The sub-menu level
	 */
	protected void createSubMenuItems(final List<MTMenuItem> subMenuItems, final MTRadialPoly menuItemPolygon, final int subMenuLevel) {
		//resetMenu();
		for (final MTMenuItem subMenuItem : subMenuItems) {
			final Vertex[] newVertices = this.calculateArchedRectangleVertices(this.center.x, this.center.y, this.center.z, 0,
					this.buttonTopWidth + (this.buttonTopWidthAdj * subMenuLevel),
					this.topArcHeight + (this.topArcHeightAdj * subMenuLevel),
					this.buttonBottomWidth + (this.buttonBottomWidth * .05f) + (this.buttonBottomWidthAdj * subMenuLevel),
					30f + (this.buttonXCoordAdj * subMenuLevel), true, 50);

			final MTRadialPoly smi = new MTRadialPoly(this.pApplet, newVertices, this.center, this.buttonHeight, this.radius, subMenuItem.getMenuText(), null, null);
			smi.setFillColor(subMenuItem.getColor());
			smi.setUnSelectedColor(subMenuItem.getColor());
			smi.setSelectedColor(subMenuItem.getColor());
			// Add all inputEventListeners
			for (final IMTInputEventListener inputEventListener : subMenuItem.getInputEventListeners()) {
				smi.getMtTextArea().addInputListener(inputEventListener);
				smi.addInputListener(inputEventListener);
				//smi.addListner(inputEventListener);
				//System.out.println("Ho aggiunto a " + menuItemPolygon.getName()+ " il listner " + inputEventListener);
			}

			// Add all gestureEventListeners
			for (final Entry<Class<? extends IInputProcessor>, IGestureEventListener> entry : subMenuItem.getGestureEventListeners().entrySet()) {
				smi.addGestureListener(entry.getKey(), entry.getValue());
				smi.getMtTextArea().addGestureListener(entry.getKey(), entry.getValue());
			}

			if (subMenuItem.getSubMenuItems() != null) {
				this.createSubMenuItems(subMenuItem.getSubMenuItems(), smi, subMenuLevel + 1);
			}

			// Add to the parent menu item
			menuItemPolygon.addSubMenuItem(smi);

			if(smi.getFillColor().getR() == 64 && smi.getFillColor().getG() == 163 && smi.getFillColor().getB() == 251){
				smi.getMtTextArea().getFont().setFillColor(new MTColor(MTColor.WHITE));
				smi.setNoStroke(false);
				smi.setStrokeWeight(2);
				smi.setStrokeColor(new MTColor(MTColor.WHITE));
				// Add to the global menu item list
			}
			else{
				smi.getMtTextArea().getFont().setFillColor(new MTColor(Math.abs(255 -  smi.getUnSelectedColor().getR()) , Math.abs(255 -  smi.getUnSelectedColor().getG()), Math.abs(255 -  smi.getUnSelectedColor().getB()) ));
				smi.setNoStroke(false);
				smi.setStrokeWeight(2);
				smi.setStrokeColor(new MTColor(Math.abs(255 -  smi.getUnSelectedColor().getR()) , Math.abs(255 -  smi.getUnSelectedColor().getG()), Math.abs(255 -  smi.getUnSelectedColor().getB()) ));
				// Add to the global menu item list
			}
			this.addMenuItem(smi);
		}
	}

	/**
	 * Creates a Close button and adds it to the root menu
	 */
	private void createCloseButton() {

		// Create a new Ellipse to be used for closing this menu
		closeButton = new MTEllipse(this.pApplet, this.center, this.radius * .25f, this.radius * .25f);

		// Configure this menu item
		closeButton.unregisterAllInputProcessors();
		closeButton.setName(this.getCloseButtonText());

		// Color is RED
		closeButton.setFillColor(this.getCenterButtonFillColor());
		closeButton.setNoStroke(false);

		// Configure the text area
		final MTTextArea mtTextArea = new MTTextArea(this.pApplet);
		mtTextArea.setName(this.getCloseButtonText());
		mtTextArea.setText(this.getCloseButtonText());
		mtTextArea.setFont(this.font);
		mtTextArea.setPositionGlobal(new Vector3D(closeButton.getCenterPointLocal().x, closeButton.getCenterPointLocal().y));
		mtTextArea.setNoFill(true);
		mtTextArea.setNoStroke(true);
		//mtTextArea.removeAllGestureEventListeners();

		closeButton.addChild(mtTextArea);

		mtTextArea.setGestureAllowance(DragProcessor.class, false);
		mtTextArea.setGestureAllowance(ScaleProcessor.class, false);
		mtTextArea.setGestureAllowance(RotateProcessor.class, false);

		this.addChild(closeButton);
	}

	/**
	 * Calculates the arched rectangle vertices<br>
	 * Based on code from {@link MTRoundRectangle}
	 * 
	 * @param x
	 *          The X coordinate of the rectangle
	 * @param y
	 *          The Y coordinate of the rectangle
	 * @param z
	 *          The Z coordinate of the rectangle
	 * @param height
	 *          The height of the rectangle
	 * @param topArcWidth
	 *          The top arc width
	 * @param topArcHeight
	 *          The top arc height
	 * @param bottomArcWidth
	 *          The bottom arc width
	 * @param bottomArcHeight
	 *          The bottom arc height
	 * @param createTexCoords
	 *          Create texture coordinates
	 * @param arcSegments
	 *          The number of segments to use to create the arcs
	 * @return Vertex[]
	 *         The vertices needed to create the arched rectangle
	 */
	private Vertex[] calculateArchedRectangleVertices(final float x, final float y, final float z,
			final float height,
			final float topArcWidth, final float topArcHeight,
			final float bottomArcWidth, final float bottomArcHeight,
			final boolean createTexCoords,
			final int arcSegments) {

		final MTColor currentFillColor = this.getFillColor();

		final Vertex upperLeftPoint = new Vertex(x, y + height - topArcHeight, 0);

		final Vertex upperRightPoint = new Vertex(x + topArcWidth, y + height - topArcHeight, 0);

		// This is done to help center the bottom arc
		final float bottomArcAdjustment = (topArcWidth - bottomArcWidth) / 2;

		final Vertex lowerRightPoint = new Vertex(x + bottomArcAdjustment + bottomArcWidth, y + height - bottomArcHeight, 0);

		final Vertex lowerLeftPoint = new Vertex(x + bottomArcAdjustment, y + height - bottomArcHeight, 0);

		// Calculates the arcs
		final List<Vertex> upperArc = ToolsGeometry.arcTo(upperRightPoint.x, upperRightPoint.y, topArcWidth, -topArcHeight, 0, false, true, upperLeftPoint.x, upperLeftPoint.y, arcSegments);

		final List<Vertex> lowerArc = ToolsGeometry.arcTo(lowerLeftPoint.x, lowerLeftPoint.y, bottomArcWidth / 1.5f, bottomArcHeight, 0, false, true, lowerRightPoint.x, lowerRightPoint.y, arcSegments);

		// Add vertices to list
		final ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.addAll(upperArc);
		vertices.addAll(lowerArc);

		final Vertex[] newVertices = vertices.toArray(new Vertex[vertices.size()]);

		// Set texture coordinates
		for (final Vertex vertex : newVertices) {
			if (createTexCoords) {
				// FIXME Which width to use?
				vertex.setTexCoordU((vertex.x - x) / topArcWidth);
				vertex.setTexCoordV((vertex.y - y) / height);
			}

			vertex.setRGBA(currentFillColor.getR(), currentFillColor.getG(), currentFillColor.getB(), currentFillColor.getAlpha());
		}

		return newVertices;
	}

	public void closePanel(final MTComponent[] comps){
		for (int i = 0; i < comps.length; i++) { //TODO this is stupid.. redo this whole thing
			MTComponent comp = comps[i];
			if (comp instanceof MTPolygon) {
				MTPolygon poly = (MTPolygon) comp;
				referencePoly = poly;
			}
		}
		float width = referencePoly.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

		Animation closeAnim = new Animation("comp Fade", new MultiPurposeInterpolator(width, 1, 700, 0.5f, 0.8f, 1), referencePoly);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					resize(referencePoly, comps[0], currentVal, currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					comps[0].setVisible(false);
					for (int i = comps.length-1; i >0 ; i--) {
						MTComponent currentComp =  comps[i];
						//Call destroy which fires a destroy state change event
						currentComp.destroy();

						//System.out.println("destroyed: " + currentComp.getName());
					}
					destroy();
					//System.out.println("destroyed: " + getName());
					break;	
				default:
					destroy();
					break;
				}//switch
			}//processanimation
		});//new IAnimationListener
		closeAnim.start(); 
	}

	protected void resize(MTPolygon referenceComp, MTComponent compToResize, float width, float height){ 
		Vector3D centerPoint = getRefCompCenterRelParent(referenceComp);
		compToResize.scale(1/referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), (float)1/referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
		compToResize.scale(width, width, 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
	}

	protected Vector3D getRefCompCenterRelParent(AbstractShape shape){
		Vector3D centerPoint;
		if (shape.hasBounds()){
			centerPoint = shape.getBounds().getCenterPointLocal();
			centerPoint.transform(shape.getLocalMatrix()); //macht den punkt in self space
		}else{
			Vector3D localObjCenter = shape.getCenterPointGlobal();
			localObjCenter.transform(shape.getGlobalInverseMatrix()); //to localobj space
			localObjCenter.transform(shape.getLocalMatrix()); //to parent relative space
			centerPoint = localObjCenter;
		}
		return centerPoint;
	}

	public MTEllipse getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(MTEllipse closeButton) {
		this.closeButton = closeButton;
	}

	public boolean isMenuBuilded() {
		return menuBuilded;
	}

	public void setMenuBuilded(boolean menuBuilded) {
		this.menuBuilded = menuBuilded;
	}

	public LinkedList<MTRadialPoly> getMainMenuItemList() {
		return mainMenuItemList;
	}

	public void setMainMenuItemList(LinkedList<MTRadialPoly> mainMenuItemList) {
		this.mainMenuItemList = mainMenuItemList;
	}
}
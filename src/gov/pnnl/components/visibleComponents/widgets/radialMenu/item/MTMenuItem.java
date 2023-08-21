/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.widgets.radialMenu.item;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

/**
 * This is the value object intended to contain all the information necessary to build<br>
 * an object graph of {@link MTRadialPoly} inside {@link MTRadialMenu}
 * 
 * @author Ryan LaMothe
 * 
 */
public class MTMenuItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean enabled = true;

	private String menuText;

	private String col;

	private List<IMTInputEventListener> inputEventListeners = new ArrayList<IMTInputEventListener>();

	private Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListeners = new ConcurrentHashMap<Class<? extends IInputProcessor>, IGestureEventListener>();

	private List<MTMenuItem> subMenuItems = new ArrayList<MTMenuItem>();

	/**
	 * Full constructor
	 * 
	 * @param menuText
	 *          The text to appear on the button
	 */
	public MTMenuItem(final String menuText, final Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListeners) {
		if (menuText != null) {
			this.setMenuText(menuText);
		}

		if (gestureEventListeners != null) {
			this.setGestureEventListeners(gestureEventListeners);
		}
	}

	/**
	 * Retrieve the menu text
	 * 
	 * @return the menuText
	 */
	public String getMenuText() {
		return this.menuText;
	}

	/**
	 * Set the menu text
	 * 
	 * @param menuText
	 *          the menuText to set
	 */
	public void setMenuText(final String menuText) {
		this.menuText = menuText;
	}

	/**
	 * Retrieve the list of IMTInputEventListeners
	 * 
	 * @return the inputEventListeners
	 */
	public List<IMTInputEventListener> getInputEventListeners() {
		return this.inputEventListeners;
	}

	/**
	 * Set the IMTInputEventListeners
	 * 
	 * @param inputEventListeners
	 *          the inputEventListeners to set
	 */
	public void setInputEventListeners(final List<IMTInputEventListener> inputEventListeners) {
		this.inputEventListeners = inputEventListeners;
	}

	/**
	 * Add an IMTInputEventListener to an existing list of IMTInputEventListeners
	 * 
	 * @param inputEventListener
	 *          the inputEventListener to add
	 */
	public void addInputEventListener(final IMTInputEventListener inputEventListener) {
		if (this.getInputEventListeners() != null) {
			this.getInputEventListeners().add(inputEventListener);
		}
	}

	/**
	 * Retrieve the GestureEventListeners
	 * 
	 * @return the gestureEventListeners
	 */
	public Map<Class<? extends IInputProcessor>, IGestureEventListener> getGestureEventListeners() {
		return this.gestureEventListeners;
	}

	/**
	 * Set the GestureEventListeners
	 * 
	 * @param gestureEventListeners
	 *          the gestureEventListeners to set
	 */
	public void setGestureEventListeners(final Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListeners) {
		this.gestureEventListeners = gestureEventListeners;
	}

	/**
	 * Add a GestureEventListener to an existing Map of GestureEventListeners
	 * 
	 * @param gestureEventListener
	 */
	public void addGestureEventListener(final Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListener) {
		this.getGestureEventListeners().putAll(gestureEventListener);
	}
	
	public void addGestureEventListener(Class<TapProcessor> class1,
			IGestureEventListener iGestureEventListener) {
		this.getGestureEventListeners().put(class1, iGestureEventListener);
	}

	/**
	 * Retrieve the list of sub-menu items
	 * 
	 * @return the subMenuItems
	 */
	public List<MTMenuItem> getSubMenuItems() {
		return this.subMenuItems;
	}

	/**
	 * Set the list of sub-menu items
	 * 
	 * @param subMenuItems
	 *          the subMenuItems to set
	 */
	public void setSubMenuItems(final List<MTMenuItem> subMenuItems) {
		this.subMenuItems = subMenuItems;
	}

	/**
	 * Add a sub-menu item to an existing list of sub-menu items
	 * 
	 * @param mtMenuItem
	 *          the mtMenuItem to add
	 */
	public void addSubMenuItem(final MTMenuItem mtMenuItem) {
		if (this.getSubMenuItems() != null) {
			this.getSubMenuItems().add(mtMenuItem);
		}
	}
	
	public void removeSubMenuItem(final MTMenuItem mtMenuItem) {
		if (this.getSubMenuItems() != null) {
			this.getSubMenuItems().removeAll(subMenuItems);
		}
	}
	

	public MTColor getColor() {
		MTColor color = new MTColor(128, 128, 128, MTColor.ALPHA_HALF_TRANSPARENCY);
		if (col!=null){	
			if(col.compareTo("orange")== 0)
				color = new MTColor(232,160,67);
			if(col.compareTo("pink")== 0)
				color = new MTColor(238,141,214);
			if(col.compareTo("red")== 0)
				color = MTColor.RED;
			if(col.compareTo("green")== 0)
				color = new MTColor(115, 187, 128);
			if(col.compareTo("blue")== 0)
				color = new MTColor(92,123,168);
			if(col.compareTo("yellow")== 0)
				color = new MTColor(220,232,107);
			if(col.compareTo("white")== 0)
				color = new MTColor(248,234,196,255);
			if(col.compareTo("maroon")== 0)
				color = new MTColor(182,115,115);
			if(col.compareTo("azure")== 0)
				color = new MTColor(197,209,225);
			if(col.compareTo("clear_azure")== 0)
				color = new MTColor(64, 163, 251, 220);
		}
		return color;
	}
	
/*
	public MTColor getColor() {
		MTColor color = new MTColor(128, 128, 128, MTColor.ALPHA_HALF_TRANSPARENCY);
		if (col!=null){	
			if(col.compareTo("orange")== 0)
				color = new MTColor(255,140,0);
			if(col.compareTo("pink")== 0)
				color = new MTColor(255,0,255);
			if(col.compareTo("red")== 0)
				color = MTColor.RED;
			if(col.compareTo("green")== 0)
				color = new MTColor(3, 218, 79);
			if(col.compareTo("blue")== 0)
				color = new MTColor(13,97,293);
			if(col.compareTo("yellow")== 0)
				color = MTColor.YELLOW;
			if(col.compareTo("white")== 0)
				color = new MTColor(255,255,255,255);
			if(col.compareTo("maroon")== 0)
				color = new MTColor(218,3,3);
			if(col.compareTo("azure")== 0)
				color = new MTColor(128,255,255);
			if(col.compareTo("Trasparent")== 0)
				color = new MTColor(0,0,0,100);
			
		}
		return color;
	}*/

	public void setColor(String color) {
		this.col = color;
	}

	public void setEnable(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnable() {
		return enabled;
	}

}
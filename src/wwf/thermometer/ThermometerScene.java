/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 C.Ruff, Fraunhofer-Gesellschaft 
 * All rights reserved.
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
package wwf.thermometer;

import java.awt.event.KeyEvent;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.mesh.MTSphere;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.thermometer.data.CurrentData;
import wwf.thermometer.data.Molecule;
import wwf.thermometer.proportions.ThermometerProportions;
import wwf.thermometer.util.FilesPath;
import wwf.thermometer.util.WaterStatus;

/**
 * Scena principale dell' app thermometer.
 */
public class ThermometerScene extends AbstractScene {
    
    /** The pa. */
    private MTApplication pa;
    
    /** The model 3 D. */
    private MTSphere model3D;
    
    /** The lens. */
    private MTRectangle lens;
    
    /** The back. */
    private MTBackgroundImage back;
    
    /** The lens layer. */
    private MTRectangle lensLayer;
    
    /** The thermometer layer. */
    private MTRectangle thermometerLayer;
    
    /** The thermometer. */
    private MTRectangle thermometer;
    
    /** The temperature bar. */
    private MTRectangle temperatureBar;
    
    /** The input. */
    private MTRectangle input;
    
    /** The padding. */
    private Float padding;

    /** The data. */
    private CurrentData data;
    
    /** The mol. */
    private Molecule mol;
    
    /**
     * Instantiates a new thermometer scene.
     *
     * @param mtApplication the mt application
     * @param name the name
     */
    public ThermometerScene(final MTApplication mtApplication,
            final String name) {
        super(mtApplication, name);

        this.pa = mtApplication;
        this.padding = 120f;
        drawScene();
    }
    
    
    /**
     * Draw scene.
     */
    private void drawScene() {
        
        lens = new MTRectangle(pa,
                pa.loadImage(FilesPath.IMAGES_PATH + "glass.png"));
        lens.removeAllGestureEventListeners();
        lens.unregisterAllInputProcessors();
        lens.setNoStroke(true);
        lens.setPositionGlobal(
                new Vector3D(pa.getWidth() 
                        - (lens.getWidthXY(TransformSpace.GLOBAL) / 2f 
                                + padding),
                        pa.getHeight() 
                        - (lens.getHeightXY(TransformSpace.GLOBAL) / 2f 
                                + padding)));
        
        lensLayer = new MTRectangle(pa, 
                lens.getCenterPointGlobal().x 
                    - (lens.getWidthXY(TransformSpace.GLOBAL) / 2f),
                lens.getCenterPointGlobal().y 
                    - (lens.getHeightXY(TransformSpace.GLOBAL) / 2f), 
                lens.getWidthXY(TransformSpace.GLOBAL),
                lens.getHeightXY(TransformSpace.GLOBAL));
        

        lensLayer.setNoStroke(true);
        lensLayer.setNoFill(true);
        lensLayer.removeAllGestureEventListeners();
        lensLayer.unregisterAllInputProcessors();
        
        lensLayer.addChild(lens);

        this.data = new CurrentData(pa, this);

        thermometer = new MTRectangle(pa,
                pa.loadImage(FilesPath.IMAGES_PATH + "thermometer.png"));
        thermometer.removeAllGestureEventListeners();
        thermometer.unregisterAllInputProcessors();
        thermometer.setNoStroke(true);


        thermometer.setPositionGlobal(
                new Vector3D(
                        padding + thermometer.getWidthXY(TransformSpace.GLOBAL) 
                        / 2f,
                        pa.getHeight() 
                        - (thermometer.getHeightXY(TransformSpace.GLOBAL) / 2f 
                                + padding)));


        thermometerLayer = new MTRectangle(pa, 
                thermometer.getCenterPointGlobal().x 
                - (thermometer.getWidthXY(TransformSpace.GLOBAL) / 2f),
                thermometer.getCenterPointGlobal().y 
                - (thermometer.getHeightXY(TransformSpace.GLOBAL) / 2f), 
                thermometer.getWidthXY(TransformSpace.GLOBAL),
                thermometer.getHeightXY(TransformSpace.GLOBAL));

        thermometerLayer.removeAllGestureEventListeners();
        thermometerLayer.unregisterAllInputProcessors();
        thermometerLayer.setNoStroke(true);
        thermometerLayer.setNoFill(true);
        thermometerLayer.addChild(thermometer);
        
        float defaultHeight = 0;
        
        data.getMolecules().getMolecules().get(WaterStatus.WATER).setVisible(false);
        data.getMolecules().getMolecules().get(WaterStatus.VAPOR).setVisible(false);

        back = new MTBackgroundImage(pa, 
                data.getImages().getImages().get(WaterStatus.ICE),
                false);
        mol = this.data.getMolecules().getMolecules().get(WaterStatus.ICE);
//        mol = new Molecule(pa, this, WaterStatus.ICE);
//        mol.dropObject();

        back.setName("background");
        this.getCanvas().addChild(back);

        temperatureBar = new MTRectangle(pa,
                thermometer.getCenterPointGlobal().x 
                - (thermometer.getWidthXY(TransformSpace.GLOBAL) / 2f) 
                + ThermometerProportions.THERMOMETER_X,
                thermometer.getCenterPointGlobal().y 
                - (thermometer.getHeightXY(TransformSpace.GLOBAL) / 2f) 
                + ThermometerProportions.getYPosition(
                        defaultHeight),               
                ThermometerProportions.WIDTH_THERMOMETER,
                defaultHeight);

        temperatureBar.setName("temperatureBar");
        temperatureBar.setNoStroke(true);
        temperatureBar.setFillColor(new MTColor(204f, 0f, 1f));  
        //temperatureBar.setFillColor(MTColor.GREEN);    

        temperatureBar.removeAllGestureEventListeners();
        temperatureBar.unregisterAllInputProcessors();

        
        thermometerLayer.addChild(temperatureBar);

        input = new MTRectangle(pa, 
                ThermometerProportions.getScaledThermometerX(12f) 
                + thermometer.getCenterPointGlobal().x 
                - (thermometer.getWidthXY(TransformSpace.GLOBAL) / 2f),
                ThermometerProportions.getScaledThermometerX(0f) 
                +  thermometer.getCenterPointGlobal().y 
                - (thermometer.getHeightXY(TransformSpace.GLOBAL) / 2f),
                ThermometerProportions.getScaledThermometerX(29f),
                ThermometerProportions.getScaledThermometerX(340f));
        input.removeAllGestureEventListeners();
        input.unregisterAllInputProcessors();

        registerDragProcessor(temperatureBar);

        thermometerLayer.addChild(input);
        input.setNoFill(true);
        input.setNoStroke(true);

        getCanvas().addChild(lensLayer);
        getCanvas().addChild(thermometerLayer);        
    }
    
    
    
    /**
     * Gets the glass.
     *
     * @return the glass
     */
    public final MTRectangle getGlass() {
		return lens;
	}
    


	/**
	 * Sets the glass.
	 *
	 * @param glass the new glass
	 */
	public final void setGlass(final MTRectangle glass) {
		this.lens = glass;
	}


	/**
	 * Gets the mol.
	 *
	 * @return the mol
	 */
	public final Molecule getMol() {
        return mol;
    }


    /**
     * Sets the mol.
     *
     * @param mol the new mol
     */
    public final void setMol(final Molecule mol) {
        this.mol = mol;
    }


    /**
     * Register drag processor.
     *
     * @param bar the bar
     */
    private void registerDragProcessor(final MTRectangle bar) {
        
        input.registerInputProcessor(new DragProcessor(pa));
 
        input.addGestureListener(DragProcessor.class, 
                new DragAction(bar, pa, getCanvas(), thermometer,
                        data, this, padding));

//        input.addGestureListener(DragProcessor.class, new DragAction(bar
//                ,pa,getCanvas(), thermometer, data, this, padding));

    }
    
    /**
     * Gets the temperature bar.
     *
     * @return the temperature bar
     */
    public final MTRectangle getTemperatureBar() {
        return temperatureBar;
    }

    /** (non-Javadoc).
     * @see org.mt4j.sceneManagement.AbstractScene#onEnter()
     */
    public final void onEnter() {
        getMTApplication().registerKeyEvent(this);
    }

    /** (non-Javadoc).
     * @see org.mt4j.sceneManagement.AbstractScene#onLeave()
     */
    public final void onLeave() {
        getMTApplication().unregisterKeyEvent(this);
    }

    /**
     * Key event.
     *
     * @param e the e
     */
    public final void keyEvent(final KeyEvent e) {
        int evtID = e.getID();
        if (evtID != KeyEvent.KEY_PRESSED) {
            return;            
        }
        switch (e.getKeyCode()) {
        case KeyEvent.VK_F:
            System.out.println("FPS: " + pa.frameRate);
            break;
        default:
            break;
        }
    }

}
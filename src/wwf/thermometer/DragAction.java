package wwf.thermometer;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;

/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009, C.Ruff, Fraunhofer-Gesellschaft 
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

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.input.gestureAction.ICollisionAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import wwf.thermometer.data.CurrentData;
import wwf.thermometer.data.Molecule;
import wwf.thermometer.proportions.ThermometerProportions;
import wwf.thermometer.util.WaterStatus;

/**
 * The Class DefaultDragAction.
 * 
 * @author Christopher Ruff
 */
public class DragAction implements IGestureEventListener, ICollisionAction {

    /** The drag target. */
    private IMTComponent3D dragTarget;

    /** The use custom target. */
    private boolean useCustomTarget;

    /** The gesture aborted. */
    private boolean gestureAborted = false;

    /** The last event. */
    private MTGestureEvent lastEvent;

    /** The bar. */
    private MTRectangle bar;
    
    /** The pa. */
    private MTApplication pa;
    
    /** The padding. */
    private float padding;
    
    /** The data. */
    private CurrentData data;
    
    /** The thermometer. */
    private MTRectangle thermometer;
    
    /** The back. */
    private MTBackgroundImage back;
    
    /** The canvas. */
    private MTCanvas canvas;
    
    /** The thermometer scene. */
    private ThermometerScene thermometerScene;

    /** The dynamic height. */
    private float dynamicHeight = 0f;

    /**
     * Instantiates a new default drag action.
     *
     * @param bar the bar
     * @param pa the pa
     * @param canvas the canvas
     * @param thermometer the thermometer
     * @param data the data
     * @param thermometerScene the thermometer scene
     * @param padding the padding
     */
    public DragAction(final MTRectangle bar, final MTApplication pa, 
            final MTCanvas canvas, final MTRectangle thermometer, 
            final CurrentData data, 
            final ThermometerScene thermometerScene,
            final float padding) {
        this.thermometer = thermometer;
        this.pa = pa;
        this.padding = padding;
        this.useCustomTarget = false;
        this.bar = bar;
        this.data = data;
        this.back = (MTBackgroundImage) canvas.getChildByName("background");
        this.canvas = canvas;
        this.thermometerScene = thermometerScene;
    }

    /**
     * Instantiates a new default drag action.
     *
     * @param dragTarget
     *            the drag target
     */
    public DragAction(final IMTComponent3D dragTarget) {
        this.dragTarget = dragTarget;
        this.useCustomTarget = true;
    }

    /**
     * (non-Javadoc).
     * 
     * @param g sa
     * @see
     * org.mt4j.input.inputProcessors.IGestureEventListener#processGestureEvent(
     * org.mt4j.input.inputProcessors.MTGestureEvent)
     * @return bool
     */
    public final  boolean processGestureEvent(final MTGestureEvent g) {
        if (g instanceof DragEvent) {
            float y1 = ((DragEvent) g).getDragCursor().getPosition().y;
            float y2 = padding 
                    + ThermometerProportions.getScaledThermometerY(48f);

            
            DragEvent dragEvent = (DragEvent) g;
            lastEvent = dragEvent;

            if (!useCustomTarget) {
                dragTarget = dragEvent.getTarget();                
            }

            switch (dragEvent.getId()) {
            case MTGestureEvent.GESTURE_STARTED:
            case MTGestureEvent.GESTURE_RESUMED:
                // Put target on top -> draw on top of others
                if (dragTarget instanceof MTComponent) {
                    MTComponent baseComp = (MTComponent) dragTarget;

                    baseComp.sendToFront();
                    updateBackground(y1, y2);

                }
                if (!gestureAborted) {
                    System.out.println("");
                }
                
                break;
            case MTGestureEvent.GESTURE_UPDATED:

                if (!gestureAborted) {
                    System.out.println("ads");

                    updateLine(y1, y2);

                }

                break;
            case MTGestureEvent.GESTURE_CANCELED:

                break;

            case MTGestureEvent.GESTURE_ENDED:
                updateBackground(y1, y2);
                System.gc();
                break;
            default:
                break;
            }
        }
        return false;
    }

    
    /**
     * Update background.
     *
     * @param y1 the y 1
     * @param y2 the y 2
     */
    private void updateBackground(final float y1,
            final float y2) {
        
        updateLine(y1, y2);
        PImage texture = null;
        
        //thermometerScene.getMol().dropObject();
        data.getMolecules().getMolecules().get(WaterStatus.ICE).setVisible(false);
        data.getMolecules().getMolecules().get(WaterStatus.WATER).setVisible(false);
        data.getMolecules().getMolecules().get(WaterStatus.VAPOR).setVisible(false);
        
        if ((pa.height - y1) <= (y2 + ThermometerProportions
                .getScaledThermometerY(49f))) {
           // System.out.println("ICE");
            thermometerScene.setMol(
                    this.data.getMolecules().getMolecules().get(WaterStatus.ICE));
            this.data.getMolecules().getMolecules().get(WaterStatus.ICE).setVisible(true);
            texture = 
                    data.getImages().getImages().get(WaterStatus.ICE);
        } else if ((pa.height - y1) <= (y2 + ThermometerProportions
                 .getScaledThermometerY(225f))) {
             // System.out.println("WATER");
             thermometerScene.setMol(
                     this.data.getMolecules().getMolecules().get(WaterStatus.WATER));
             this.data.getMolecules().getMolecules().get(WaterStatus.WATER).setVisible(true);

             texture = 
                     data.getImages().getImages().get(WaterStatus.WATER);
         } else if ((pa.height - y1) > (y2 + ThermometerProportions
                 .getScaledThermometerY(225f))) {
             // System.out.println("VAPOR");
             thermometerScene.setMol(
                     this.data.getMolecules().getMolecules().get(WaterStatus.VAPOR));
             this.data.getMolecules().getMolecules().get(WaterStatus.VAPOR).setVisible(true);

             texture = 
                     data.getImages().getImages().get(WaterStatus.VAPOR);
         } else {             
             System.out.println("OUT");
         }

        back.getTexture().pixels = null;

        back.setTexture(texture);

    }
 
    /**
     * Update line.
     *
     * @param y1 the y 1
     * @param y2 the y 2
     */
    private void updateLine(final float y1, final float y2) {
        dynamicHeight = pa.height - (y1 + y2);
        System.out.println(dynamicHeight);
        if (dynamicHeight 
                >= ThermometerProportions.getScaledThermometerY(283f)) {
            System.out.println("");
        } else if (dynamicHeight > 0) {
            bar.setHeightLocal(dynamicHeight);

            bar.setPositionGlobal(new Vector3D(
                    thermometer.getCenterPointGlobal().x 
                    - (thermometer.getWidthXY(TransformSpace.GLOBAL) / 2f)
                    + ThermometerProportions.THERMOMETER_X 
                    + bar.getWidthXY(TransformSpace.GLOBAL) / 2f,
                    thermometer.getCenterPointGlobal().y 
                    - (thermometer.getHeightXY(TransformSpace.GLOBAL) / 2f)
                            + ThermometerProportions.getYPosition(dynamicHeight)
                            + bar.getHeightXY(TransformSpace.GLOBAL) / 2f));
        } 
    }
    /**
     * (non-Javadoc).
     * 
     * @see org.mt4j.input.inputProcessors.ICollisionAction#gestureAborted()
     * @return bool
     */
    public final boolean gestureAborted() {
        return this.gestureAborted;
    }

    /**
     * (non-Javadoc).
     * 
     * @see org.mt4j.input.inputProcessors.ICollisionAction#getLastEvent()
     * @return last event
     */
    public final MTGestureEvent getLastEvent() {
        return this.lastEvent;
    }

    /**
     * (non-Javadoc).
     * 
     * @see org.mt4j.input.inputProcessors.ICollisionAction#setGestureAborted(
     * boolean)
     * @param aborted aborted
     */
    public final void setGestureAborted(final boolean aborted) {
        this.gestureAborted = aborted;
    }

}
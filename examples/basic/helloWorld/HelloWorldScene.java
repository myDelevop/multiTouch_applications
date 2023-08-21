package basic.helloWorld;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.video.MTMovieClip;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.components.visibleComponents.widgets.MTWebBrowser;

import com.sun.awt.AWTUtilities.Translucency;

import processing.core.PImage;

public class HelloWorldScene extends AbstractScene {

	private String pathImg = "src/euroflag/imgs/";
	MTImageButton button;

	public HelloWorldScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		String videoPath =  System.getProperty("user.dir")+File.separator + "examples" + 
		File.separator + File.separator + "basic"+  File.separator + File.separator  +"helloWorld"+
		File.separator + File.separator  +"escursione.mp4";
		MTMovieClip video = new MTMovieClip(videoPath, new Vertex(100, 100), mtApplication);
		this.getCanvas().addChild(video);

		MTColor white = new MTColor(255,255,255);
		this.setClearColor(new MTColor(146, 150, 188, 255));
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));

		IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				50, 	//Font size
				white);	//Font color
		//Create a textfield
		MTTextArea textField = new MTTextArea(mtApplication, fontArial); 

		textField.setNoStroke(true);
		textField.setNoFill(true);
		

		textField.setText("Hello World!");
		//Center the textfield on the screen
		textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		//Add the textfield to our canvas
		this.getCanvas().addChild(textField);

		PImage buttonImage =  mtApplication.loadImage(System.getProperty("user.dir")+File.separator + "examples" + 
				File.separator + File.separator + "basic"+  File.separator + File.separator  +"helloWorld"+
				File.separator + File.separator  +"play-button.png");
		button = new MTImageButton(mtApplication, buttonImage);
		this.getCanvas().addChild(button);
		button.setPositionGlobal(new Vector3D(mtApplication.width/2, mtApplication.height/2));
		button.setGestureAllowance(TapProcessor.class, true);

		//ANIMAZIONE
		final CompleteAnimation animation = new CompleteAnimation(button, 3000, 0.1f, 0.3f, 1);
		animation.setEndPoint(new Vector3D(0, 0));
		animation.setStartPoint(button.getPosition(TransformSpace.GLOBAL));
		animation.setFinalWidth(100);
		//animation.setFinalHeigth(100);
		//Vector3D puntoArrivo = new Vector3D(0, 0);
		//Vector3D puntoPartenza = button.getPosition(TransformSpace.GLOBAL);
		float finalWidth = 600;
		float finalHeigth = 400;
		//final float offsetX = (puntoArrivo.getX() - puntoPartenza.getX()) / 100 ;
	//	final float offsetY = (puntoArrivo.getY() - puntoPartenza.getY()) / 100;
		MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 100, 3000, 0.1f, 0.3f, 1);
		/*final Animation moveAnimation = new Animation("move animation", interpolator, this, 0);
		moveAnimation.addAnimationListener(new IAnimationListener() {

			@Override
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getDelta();
					button.setPositionGlobal(new Vector3D(button.getCenterPointGlobal().getX() + offsetX * currentVal, button.getCenterPointGlobal().getY() + offsetY * currentVal));
					
					//resize(button, button, currentVal, currentVal);
				//	drag(button,currentVal);
					System.out.println(""+button.getWidthXY(TransformSpace.GLOBAL));
					break;
				case AnimationEvent.ANIMATION_ENDED:

					break;	
				default:

					break;
				}
			}
		});*/


		button.addGestureListener(TapProcessor.class, new IGestureEventListener() {

			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge.getId() == 2)
					//moveAnimation.start();
						animation.start();
				System.out.println("CLICK");
				return false;
			}
		});
	}

	protected void resize(MTPolygon referenceComp,MTComponent compToResize, float width, float height){ 
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

	public void onEnter() {}

	public void onLeave() {}
}

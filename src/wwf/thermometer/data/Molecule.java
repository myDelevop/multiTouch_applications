package wwf.thermometer.data;

import javax.media.opengl.GL;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.MTLight;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.arcballProcessor.ArcBallGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.arcballProcessor.ArcballProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.modelImporter.ModelImporterFactory;
import org.mt4j.util.opengl.GLMaterial;
import org.mt4jx.input.gestureAction.Rotate3DAction;
import org.mt4jx.input.inputProcessors.componentProcessors.Rotate3DProcessor.Rotate3DProcessor;

import wwf.thermometer.ThermometerScene;
import wwf.thermometer.util.FilesPath;
import wwf.thermometer.util.WaterStatus;

/**
 * The Class Molecule.
 */
public class Molecule {
    
    /** The group 1. */
    private MTComponent group1;
    
    /** The pa. */
    private MTApplication pa;
    
    /** The status. */
    private WaterStatus status;
	

	/**
	 * Instantiates a new molecule.
	 *
	 * @param mtApplication the mt application
	 * @param thermometerScene the thermometer scene
	 * @param status the status
	 */
	public Molecule(final MTApplication mtApplication, 
	        final ThermometerScene thermometerScene,
	        final WaterStatus status) {

	    this.status = status;
	       this.pa = mtApplication;
	    group1 = new MTComponent(mtApplication);

		//Init light settings
		//MTLight.enableLightningAndAmbient(mtApplication, 150, 150, 150, 255);
		//Create a light source //I think GL_LIGHT0 is used by processing!
		MTLight light = new MTLight(mtApplication, GL.GL_LIGHT3,
		        new Vector3D(0, -300, 0));

		//Set up a material to react to the light
		GLMaterial material = new GLMaterial(Tools3D.getGL(mtApplication));
		material.setAmbient(new float[]{ .5f, .5f, .5f, 1f });
		material.setDiffuse(new float[]{ .8f, .8f, .8f, 1f });
		material.setEmission(new float[]{ .0f, .0f, .0f, 1f });
        // almost white: very reflective
		material.setSpecular(new float[]{ 0.9f, 0.9f, 0.9f, 1f });
		material.setShininess(110); // 0=no shine,  127=max shine


		// Create a group and set the light for the whole mesh group 
		// ->better for performance than setting light to more comps
		final MTComponent meshGroup 
		        = new MTComponent(mtApplication, "Mesh group");
		meshGroup.setLight(light);

		//Desired position for the meshes to appear at
		float x = thermometerScene.getGlass().getCenterPointGlobal().getX() 
		        - thermometerScene.getGlass().getWidthXY(TransformSpace.GLOBAL)
		        / 4;
		float y = thermometerScene.getGlass().getCenterPointGlobal().getY() 
		        - thermometerScene.getGlass().getHeightXY(TransformSpace.GLOBAL)
		        / 8;
		
		Vector3D destinationPosition = new Vector3D(x, y, 1);
		//Desired scale for the meshes
		float destinationScale = mtApplication.width * 0.35f;

		// Load the meshes with the ModelImporterFactory 
		// (A file can contain more than 1 mesh) Loads 3ds model

		MTTriangleMesh[] meshes = null;
		
		switch (status) {
        case ICE:
            //meshes = ModelImporterFactory.loadModel(mtApplication, 
                //FilesPath.MODELS3D_PATH + "ice.obj", 180, true, false );
            meshes = ModelImporterFactory.loadModel(mtApplication, 
                    FilesPath.MODELS3D_PATH + "honda_jazz.obj", 180, 
                    true, false);
            break;

        case WATER:
            // meshes = ModelImporterFactory.loadModel(mtApplication, 
            // FilesPath.MODELS3D_PATH + "water.obj", 180, true, false );
            meshes = ModelImporterFactory.loadModel(mtApplication, 
                    FilesPath.MODELS3D_PATH + "honda_jazz.obj", 180,
                    true, false);
            break;

        case VAPOR:
            // meshes = ModelImporterFactory.loadModel(mtApplication,
            // FilesPath.MODELS3D_PATH + "vapor.obj", 180, true, false);
            meshes = ModelImporterFactory.loadModel(mtApplication,
                    FilesPath.MODELS3D_PATH + "honda_jazz.obj", 180,
                    true, false );
            break;

        default:
            break;
		}
		
		// Get the biggest mesh in the group to use as a 
		// reference for setting the position/scale
		final MTTriangleMesh biggestMesh = this.getBiggestMesh(meshes);

		Vector3D translationToScreenCenter = new Vector3D(destinationPosition);
		translationToScreenCenter.subtractLocal(
		        biggestMesh.getCenterPointGlobal());

		Vector3D scalingPoint = new Vector3D(
		        biggestMesh.getCenterPointGlobal());
		float biggestWidth = biggestMesh.getWidthXY(TransformSpace.GLOBAL);	
		float scale = destinationScale / biggestWidth;

		//Move the group the the desired position
		group1.scaleGlobal(scale, scale, scale, scalingPoint);
		group1.translateGlobal(translationToScreenCenter);
		thermometerScene.getCanvas().addChild(group1);
		group1.addChild(meshGroup);

		// Inverts the normals, if they are calculated pointing 
		// inside of the mesh instead of outside
		boolean invertNormals = true;

		for (MTTriangleMesh mesh : meshes) {
			meshGroup.addChild(mesh);
			mesh.unregisterAllInputProcessors();
			//Clear previously registered input processors
			mesh.setPickable(true);

			if (invertNormals) {
				Vector3D[] normals = mesh.getGeometryInfo().getNormals();
				for (Vector3D vector3d : normals) {
					vector3d.scaleLocal(-1);
				}
				mesh.getGeometryInfo().setNormals(mesh.getGeometryInfo()
				        .getNormals(), mesh.isUseDirectGL(), mesh.isUseVBOs());
			}

			// If the mesh has more than 20 vertices, use a 
			// display list for faster rendering
			if (mesh.getVertexCount() > 20) {
                mesh.generateAndUseDisplayLists();			    
			}
			// Set the material to the mesh  
			// (determines the reaction to the lightning)
			if (mesh.getMaterial() == null) {
                mesh.setMaterial(material);			    
			}
			mesh.setDrawNormals(false);
		}

		//Register arcball gesture manipulation to the whole mesh-group
		meshGroup.setComposite(true); 
		//-> Group gets picked instead of its children
		meshGroup.registerInputProcessor(
		        new ArcballProcessor(mtApplication, biggestMesh));
		meshGroup.addGestureListener(ArcballProcessor.class, 
		        new IGestureEventListener() {
			//@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				ArcBallGestureEvent aEvt =  (ArcBallGestureEvent) ge;
				meshGroup.transform(aEvt.getTransformationMatrix());
				return false;
			}
		});

		meshGroup.registerInputProcessor(new ScaleProcessor(mtApplication));
		meshGroup.addGestureListener(ScaleProcessor.class, 
		        new IGestureEventListener() {
			//@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				ScaleEvent se = (ScaleEvent) ge;
				meshGroup.scaleGlobal(se.getScaleFactorX(), 
				        se.getScaleFactorY(), se.getScaleFactorX(), 
				        biggestMesh.getCenterPointGlobal());
				return false;
			}
		});

		meshGroup.registerInputProcessor(new RotateProcessor(mtApplication));
		meshGroup.addGestureListener(RotateProcessor.class, 
		        new DefaultRotateAction());

		meshGroup.registerInputProcessor(
		        new Rotate3DProcessor(mtApplication, meshGroup));
		meshGroup.addGestureListener(Rotate3DProcessor.class, 
		        new Rotate3DAction(mtApplication, meshGroup));

	}

	/**
	 * Gets the biggest mesh.
	 *
	 * @param meshes the meshes
	 * @return the biggest mesh
	 */
	public final MTTriangleMesh getBiggestMesh(final MTTriangleMesh[] meshes) {
		MTTriangleMesh currentBiggestMesh = null;
		//Get the biggest mesh and extract its width
		float currentBiggestWidth = Float.MIN_VALUE;
        for (MTTriangleMesh triangleMesh : meshes) {
            float width = triangleMesh.getWidthXY(TransformSpace.GLOBAL);
            if (width >= currentBiggestWidth 
                    || currentBiggestWidth == Float.MIN_VALUE) {
                currentBiggestWidth = width;
                currentBiggestMesh = triangleMesh;
            }
        }
		return currentBiggestMesh;
	}

   	
    public final void setVisible(boolean visibility) {
        group1.setVisible(visibility);
    }

}
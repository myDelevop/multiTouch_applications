package wwf.pictureBrowser;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;

import msafluid.MSAFluidSolver2D;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

import com.sun.opengl.util.BufferUtil;


/**
 * The Class FluidSimulationScene.
 * 
 * The original fluid simulation code was taken from
 * memo akten (www.memo.tv)
 * 
 */
public class Sfondo extends AbstractScene {
	
	/** The fluid width. */
	private final float FLUID_WIDTH = 120;
	
	/** The inv height. */
	private float invWidth, invHeight;    // inverse of screen dimensions
	
	/** The aspect ratio 2. */
	private float aspectRatio, aspectRatio2;
	
	/** The fluid solver. */
	private MSAFluidSolver2D fluidSolver;
	
	/** The img fluid. */
	private PImage imgFluid;
	
	/** The draw fluid. */
	private boolean drawFluid = true;
	
	/** The particle system. */
	private ParticleSystem particleSystem;
	/////////
	
	/** The app. */
	private MTApplication app;
	
	/** The canvas. */
	private MTCanvas canvas;

	/**
	 * Costruttore parametrico.
	 *
	 * @param mtApplication the mt application
	 * @param name the name
	 * @param canvas the canvas
	 */
	public Sfondo(final MTApplication mtApplication, final String name,
	        final MTCanvas canvas) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.canvas = canvas;
		if (!MT4jSettings.getInstance().isOpenGlMode()) {
			System.err.println(
			        "Scene only usable when using the OpenGL renderer!"
			        + " - See settings.txt");
        	return;
        }
		
        invWidth = 1.0f / mtApplication.width;
        invHeight = 1.0f / mtApplication.height;
        aspectRatio = mtApplication.width * invHeight;
        aspectRatio2 = aspectRatio * aspectRatio;
     
        // Create fluid and set options
        fluidSolver = new MSAFluidSolver2D((int) (FLUID_WIDTH), 
                (int) (FLUID_WIDTH * mtApplication.height 
                        / mtApplication.width));
        fluidSolver.enableRGB(true).setFadeSpeed(0.003f)
                .setDeltaT(0.5f).setVisc(0.00005f);
     
        // Create image to hold fluid picture
        imgFluid = mtApplication.createImage(fluidSolver.getWidth(),
                fluidSolver.getHeight(), PApplet.RGB);
        
        // Create particle system
        particleSystem = new ParticleSystem(mtApplication, fluidSolver);
        
        this.canvas.addInputListener(new IMTInputEventListener() {
        	//@Override
        	public boolean processInputEvent(final MTInputEvent inEvt) {
        		if (inEvt instanceof AbstractCursorInputEvt) {
        			AbstractCursorInputEvt posEvt = (AbstractCursorInputEvt) 
        			        inEvt;
        			if (posEvt.hasTarget() && posEvt.getTargetComponent()
        			        .equals(canvas)) {
        				InputCursor m = posEvt.getCursor();
        				AbstractCursorInputEvt prev 
        				    = m.getPreviousEventOf(posEvt);
        				if (prev == null) {
        					prev = posEvt;
        				}

        				Vector3D pos = 
        				        new Vector3D(posEvt.getPosX(), 
        				                posEvt.getPosY(), 0);
        				Vector3D prevPos = 
        				        new Vector3D(prev.getPosX(), 
        				                prev.getPosY(), 0);

        				float mouseNormX = pos.x * invWidth;
        				float mouseNormY = pos.y * invHeight;
        				float mouseVelX = (pos.x - prevPos.x) * invWidth;
        				float mouseVelY = (pos.y - prevPos.y) * invHeight;

        				addForce(mouseNormX, mouseNormY, mouseVelX, mouseVelY);
        			}
        		}
        		return true;
        	}
		});
        this.canvas.addChild(new FluidImage(mtApplication));
	}
	
	/**
	 * The Class FluidImage.
	 */
	private class FluidImage extends MTComponent {
		
		/**
		 * Instantiates a new fluid image.
		 *
		 * @param applet the applet
		 */
		FluidImage(final PApplet applet) {
			super(applet);
		}
		
		/**
		 * @see org.mt4j.components.MTComponent#drawComponent(
		 * processing.core.PGraphics)
		 */
		@Override
		public void drawComponent(final PGraphics g) {
			super.drawComponent(g);
			drawFluidImage();
		}
	}
	
	
	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#drawAndUpdate(
	 * processing.core.PGraphics, long)
	 */
	@Override
	public final void drawAndUpdate(final PGraphics graphics, 
	        final long timeDelta) {
//		this.drawFluidImage();
		super.drawAndUpdate(graphics, timeDelta);
		
		app.noSmooth();
		app.fill(255,255,255,255);
		app.tint(255,255,255,255);
		
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) app.g; 
		GL gl = pgl.gl;
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
		gl.glDisable(GL.GL_LINE_SMOOTH);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	
	
	/**
	 * Adds the force.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dx the dx
	 * @param dy the dy
	 */
	// add force and dye to fluid, and create particles
	private void addForce(float x, float y, 
	        final float dx, final float dy) {
	    float speed = dx * dx  + dy * dy * aspectRatio2;

	    if (speed > 0) {
	        if (x < 0) { 
	        	x = 0; 
	        } else if (x > 1) {
	        	x = 1;
	        } 
	        if (y < 0) {
	        	y = 0; 
	        } else if (y > 1) { 
	        	y = 1;
	        }
	        
	        float colorMult = 5;
	        float velocityMult = 30.0f;
	 
	        int index = fluidSolver.getIndexForNormalizedPosition(x, y);
	 
//	        PApplet.color drawColor;
	        app.colorMode(PApplet.HSB, 360, 1, 1);
	        float hue = ((x + y) * 180 + app.frameCount) % 360;
	        int drawColor = app.color(hue, 1, 1);
	        app.colorMode(PApplet.RGB, 1);  
	 
	        fluidSolver.rOld[index]  += app.red(drawColor) 	* colorMult;
	        fluidSolver.gOld[index]  += app.green(drawColor) 	* colorMult;
	        fluidSolver.bOld[index]  += app.blue(drawColor) 	* colorMult;
	 
	        //Particles
	        particleSystem.addParticles(x * app.width, y * app.height, 10);
	        
	        fluidSolver.uOld[index] += dx * velocityMult;
	        fluidSolver.vOld[index] += dy * velocityMult;

			app.colorMode(PApplet.RGB, 255);  
	    }
	}
	
	/**
	 * Draw fluid image.
	 */
	private void drawFluidImage() {
		app.colorMode(PApplet.RGB, 1);  
		
		fluidSolver.update();
	    if (drawFluid) {
	        for (int i = 0; i < fluidSolver.getNumCells(); i++) {
	            int d = 2;
	            imgFluid.pixels[i] = app.color(fluidSolver.r[i] * d, 
	                    fluidSolver.g[i] * d, fluidSolver.b[i] * d);
	        }  
	        imgFluid.updatePixels(); //  fastblur(imgFluid, 2);
	        app.image(imgFluid, 0, 0, app.width, app.height);
	    } 
	    particleSystem.updateAndDraw();
	    
	    app.colorMode(PApplet.RGB, 255);  
	}

	
	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#init()
	 */
	//@Override
	public final void init() {
		app.registerKeyEvent(this);
	}

	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#shutDown()
	 */
	@Override
	public final void shutDown() {
		app.unregisterKeyEvent(this);
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
		case KeyEvent.VK_M:
			break;
		case KeyEvent.VK_BACK_SPACE:
			app.popScene();
			break;
			default:
				break;
		}
	}

	
	/**
	 * The Class Particle.
	 */
	private class Particle {
		
		/** The Constant MOMENTUM. */
		private static final float MOMENTUM = 0.5f;
		
		/** The Constant FLUID_FORCE. */
		private static final float FLUID_FORCE = 0.6f;

		/** The y. */
		private float x, y;
		
		/** The vy. */
		private float vx, vy;
		
		/** The alpha. */
		//private float radius;       // particle's size
		protected float alpha;
		
		/** The mass. */
		private float mass;
		
		/** The p. */
		private PApplet p;
		
		/** The inv width. */
		private float invWidth;
		
		/** The inv height. */
		private float invHeight;
		
		/** The fluid solver. */
		private MSAFluidSolver2D fluidSolver;
		
		
		/**
		 * Instantiates a new particle.
		 *
		 * @param p the p
		 * @param fluidSolver the fluid solver
		 * @param invWidth the inv width
		 * @param invHeight the inv height
		 */
		Particle(final PApplet p, final MSAFluidSolver2D fluidSolver, 
		        final float invWidth, final float invHeight) {
			this.p = p;
			this.invWidth = invWidth;
			this.invHeight = invHeight;
			this.fluidSolver = fluidSolver;
		}

	   /**
   	 * Inits the.
   	 *
   	 * @param x the x
   	 * @param y the y
   	 */
   	public void init(final float x, final float y) {
	       this.x = x;
	       this.y = y;
	       vx = 0;
	       vy = 0;
	       //radius = 5;
	       alpha = p.random(0.3f, 1);
	       mass = p.random(0.1f, 1);
	   }


	   /**
   	 * Update.
   	 */
   	public void update() {
	       // only update if particle is visible
	       if (alpha == 0) {
            return;
        }

	       // read fluid info and add to velocity
	       int fluidIndex = fluidSolver.getIndexForNormalizedPosition(
	               x * invWidth, y * invHeight);
	       vx = fluidSolver.u[fluidIndex] * p.width 
	               * mass * FLUID_FORCE + vx * MOMENTUM;
	       vy = fluidSolver.v[fluidIndex] * p.height
	               * mass * FLUID_FORCE + vy * MOMENTUM;

	       // update position
	       x += vx;
	       y += vy;

	       // bounce of edges
	       if (x < 0) {
	           x = 0;
	           vx *= -1;
	       } else if (x > p.width) {
	           x = p.width;
	           vx *= -1;
	       }

	       if (y < 0) {
	           y = 0;
	           vy *= -1;
	       } else if (y > p.height) {
	           y = p.height;
	           vy *= -1;
	       }

	       // hackish way to make particles glitter when the slow down a lot
	       if (vx * vx + vy * vy < 1) {
	           vx = p.random(-1, 1);
	           vy = p.random(-1, 1);
	       }

	       // fade out a bit (and kill if alpha == 0);
	       alpha *= 0.999;
	       if (alpha < 0.01) {
            alpha = 0;
        }

	   }


	   /**
   	 * Update vertex arrays.
   	 *
   	 * @param i the i
   	 * @param posBuffer the pos buffer
   	 * @param colBuffer the col buffer
   	 */
   	public void updateVertexArrays(final int i, final FloatBuffer posBuffer,
   	        final FloatBuffer colBuffer) {
	       int vi = i * 4;
	       posBuffer.put(vi++, x - vx);
	       posBuffer.put(vi++, y - vy);
	       posBuffer.put(vi++, x);
	       posBuffer.put(vi++, y);

	       int ci = i * 6;
	       colBuffer.put(ci++, alpha);
	       colBuffer.put(ci++, alpha);
	       colBuffer.put(ci++, alpha);
	       colBuffer.put(ci++, alpha);
	       colBuffer.put(ci++, alpha);
	       colBuffer.put(ci++, alpha);
	   }


	   /**
   	 * Draw old school.
   	 *
   	 * @param gl the gl
   	 */
   	public void drawOldSchool(final GL gl) {
	       gl.glColor3f(alpha, alpha, alpha);
	       gl.glVertex2f(x - vx, y - vy);
	       gl.glVertex2f(x, y);
	   }

	} //end particle class
	
	
	
	
	/**
	 * The Class ParticleSystem.
	 */
	public class ParticleSystem {
		
		/** The pos array. */
		private FloatBuffer posArray;
		
		/** The col array. */
		private FloatBuffer colArray;
		
		/** The Constant maxParticles. */
		private static final int MAX_PARTICICLES = 5000;
		
		/** The cur index. */
		private int curIndex;
		
		/** The render using VA. */
		private boolean renderUsingVA = true;
		
		/** The particles. */
		private Particle[] particles;
		
		/** The p. */
		private PApplet p;
		
		/** The fluid solver. */
		private MSAFluidSolver2D fluidSolver;
		
		/** The inv width. */
		private float invWidth;
		
		/** The inv height. */
		private float invHeight;
		
		/** The draw fluid. */
		private boolean drawFluid;
		
		/**
		 * Instantiates a new particle system.
		 *
		 * @param p the p
		 * @param fluidSolver the fluid solver
		 */
		public ParticleSystem(final PApplet p, 
		        final MSAFluidSolver2D fluidSolver) {
			this.p = p;
			this.fluidSolver = fluidSolver;
			this.invWidth = 1.0f / p.width;
			this.invHeight = 1.0f / p.height;
			
			this.drawFluid = true;
			
			particles = new Particle[MAX_PARTICICLES];
			
			for (int i = 0; i < MAX_PARTICICLES; i++) {
				particles[i] = new Particle(p, 
				        this.fluidSolver, invWidth, invHeight);
			}
			
			curIndex = 0;

			posArray = BufferUtil.newFloatBuffer(MAX_PARTICICLES * 2 * 2);
			colArray = BufferUtil.newFloatBuffer(MAX_PARTICICLES * 3 * 2);
		}


		/**
		 * Update and draw.
		 */
		public final void updateAndDraw() {
			PGraphicsOpenGL pgl = (PGraphicsOpenGL) p.g;
			GL gl = pgl.beginGL();         

			gl.glEnable(GL.GL_BLEND);     
			if (!drawFluid) {
                fadeToColor(p, gl, 0, 0, 0, 0.05f);
            }

			gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE); 
			gl.glEnable(GL.GL_LINE_SMOOTH);  // make points round
			gl.glLineWidth(1);


			if (renderUsingVA) {
				for (int i = 0; i < MAX_PARTICICLES; i++) {
					if (particles[i].alpha > 0) {
						particles[i].update();
						particles[i].updateVertexArrays(i, posArray, colArray);
					}
				}    
				gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
				gl.glVertexPointer(2, GL.GL_FLOAT, 0, posArray);

				gl.glEnableClientState(GL.GL_COLOR_ARRAY);
				gl.glColorPointer(3, GL.GL_FLOAT, 0, colArray);

				gl.glDrawArrays(GL.GL_LINES, 0, MAX_PARTICICLES * 2);
			} else {
				gl.glBegin(GL.GL_LINES);               // start drawing points
				for (int i = 0; i < MAX_PARTICICLES; i++) {
					if (particles[i].alpha > 0) {
						particles[i].update();
						particles[i].drawOldSchool(gl);   
					}
				}
				gl.glEnd();
			}

			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			pgl.endGL();
		}

		
		/**
		 * Fade to color.
		 *
		 * @param p the p
		 * @param gl the gl
		 * @param r the r
		 * @param g the g
		 * @param b the b
		 * @param speed the speed
		 */
		public final void fadeToColor(final PApplet p, final GL gl, 
		        final float r, final float g, final float b,
		        final float speed) {
//			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl.glColor4f(r, g, b, speed);
			gl.glBegin(GL.GL_QUADS);
				gl.glVertex2f(0, 0);
				gl.glVertex2f(p.width, 0);
				gl.glVertex2f(p.width, p.height);
				gl.glVertex2f(0, p.height);
			gl.glEnd();
		}
		

		/**
		 * Adds the particles.
		 *
		 * @param x the x
		 * @param y the y
		 * @param count the count
		 */
		public final void addParticles(final float x,
		        final float y, final int count) {
			for (int i = 0; i < count; i++) {
                addParticle(x + p.random(-15, 15), y + p.random(-15, 15));
            }
		}


		/**
		 * Adds the particle.
		 *
		 * @param x the x
		 * @param y the y
		 */
		public final void addParticle(final float x, final float y) {
			particles[curIndex].init(x, y);
			curIndex++;
			if (curIndex >= MAX_PARTICICLES) {
                curIndex = 0;
            }
		}



		/**
		 * Checks if is draw fluid.
		 *
		 * @return true, if is draw fluid
		 */
		public final boolean isDrawFluid() {
			return drawFluid;
		}

		/**
		 * Sets the draw fluid.
		 *
		 * @param drawFluid the new draw fluid
		 */
		public final void setDrawFluid(final boolean drawFluid) {
			this.drawFluid = drawFluid;
		}

	}//end psystem class

}

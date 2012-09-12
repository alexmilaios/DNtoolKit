package graphics;

import graph.Connection;
import graph.Node;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jogamp.opengl.util.gl2.GLUT;


import sort.SenderReceiverPairs;


public class Universe extends JFrame implements KeyListener, MouseListener{
	
	// phi is the angle between y-axis and the position vector of the camera
	private  float phi = 0.0f;
	
	// thita is the angle between z-axis and the projection  
	// of the position vector of the camera at xz - plain
	private  float thita = (float) (Math.PI / 2 );
	
	// the radius of the sphere on which the camera is moved
	private  float radius = 20.0f;
	
	// the model is used for keeping the points of the model 
	// and for producing the elements of the visualizer 
	private Model model;
	
	// Coordinates on the screen
	private int screen_x, screen_y;
	
	public GLCanvas canvas;
	
	private boolean mouseClicked;
	
	public Universe (List<List<Node>> levels, Vector<SenderReceiverPairs> messages, 
				List<Connection> connections, int numOfNodes,String [] persistenPredicates, 
				String [] transientPredicates, String [] transportPredicates,String trace, String [] nodes,int Maxtime) {
		setSize(870, 700);
		setTitle("Visualization");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		model = new Model(Maxtime, numOfNodes, connections,levels,messages,persistenPredicates,
				transientPredicates,transportPredicates,trace,nodes);
		
		
		GraphicListener listener = new GraphicListener();
		canvas = new GLCanvas(new GLCapabilities(null));
		
		canvas.addGLEventListener(listener);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.setFocusable(true);
		
		screen_x = screen_y = 0;
		
		mouseClicked = false;
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center",canvas);
		
		
		JPanel label = new JPanel();
		JLabel timeline = new JLabel(" Blue Lines: timeline ");
		timeline.setForeground(Color.blue);
		JLabel msg = new JLabel(" Red Lines: messages ");
		msg.setForeground(Color.red);
		JLabel topo = new JLabel(" Green Lines: Topology ");
		topo.setForeground(Color.green);
		JLabel nodeslabel = new JLabel(" Yellow Spheres: Nodes ");
		nodeslabel.setForeground(Color.yellow);
		JLabel moments = new JLabel(" Black Spheres: Time Point ");
		moments.setForeground(Color.black);
		
		label.setLayout(new BoxLayout(label, BoxLayout.Y_AXIS));
		label.add(timeline);
		label.add(msg);
		label.add(topo);
		label.add(nodeslabel);
		label.add(moments);
		label.setBackground(new Color(0.95f, 0.95f, 0.95f));
		
		Topology topoPanel = new Topology(numOfNodes, connections,nodes);
		label.add(topoPanel);
		
		getContentPane().add("East",label);
		setVisible(true);
	}
	
	
	
	public class GraphicListener implements GLEventListener {

		@Override
		public void display(GLAutoDrawable drawable) {
			// Clear image
			GL2 gl = (GL2) drawable.getGL();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(0.95f, 0.95f, 0.95f, 0.95f);
			
			// gl settings
			gl.glEnable(GL.GL_DEPTH_TEST); 
			gl.glCullFace(GL.GL_FRONT); 
			gl.glEnable(GL.GL_CULL_FACE); 
			gl.glFrontFace(GL.GL_CCW); 
			
			gl.glMatrixMode(GL2.GL_PROJECTION);
			
			GLU glu = new GLU();
			
			// set properly the camera
			setCamera(gl, glu);
			
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			
			gl.glLineWidth(1.0f);
			gl.glDisable(GL2.GL_LINE_STIPPLE);
			gl.glEnable(GL.GL_LINE_SMOOTH);
			
			// draw the spheres of the model
			model.drawPoints(glu, gl);
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			
			// draw initial topology
			model.drawGraph(gl);
			model.drawLabels(gl, new GLUT());
			
			// draw vertical lines corresponding to time-line 
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			model.drawTimeLines(gl);
			model.drawPyramids(gl);
			
			// draw the exchange messages
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			model.drawMessages(gl);
			
			
			if(mouseClicked) {
				model.checkPosition(screen_x, screen_y ,model.getAreas(gl, glu));
				mouseClicked =false;
			}
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			GL2 gl = (GL2) drawable.getGL();
			drawable.setGL(new DebugGL2(gl));

			// Global settings.
			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LEQUAL);
			gl.glShadeModel(GL2.GL_SMOOTH);
			gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
			gl.glClearColor(0f, 0f, 0f, 1f);			
		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
	        GL gl = drawable.getGL();
	        gl.glViewport(0, 0, width, height);
		}
		
		private void setCamera(GL2 gl, GLU glu) {
			// Change to projection matrix.
	        gl.glMatrixMode(GL2.GL_PROJECTION);
	        gl.glLoadIdentity();

	        // Perspective.
	        float widthHeightRatio = (float) getWidth() / (float) getHeight();
	        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
	        glu.gluLookAt(getXCoordinate(radius), getYCoordinate(radius),getZCoordinate(radius), 
	        		0, 0, 0, 
	        		0, (correctElevation() == -1) ? -1 : 1, 0);
	        
	        // Change back to model view matrix.
	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        gl.glLoadIdentity();
		}
		
		private int correctElevation() {
			if(((thita > 0) && (thita < Math.PI )) || 
					((thita < 0) && (Math.abs(thita) > Math.PI)))
				return 0;
			else
				return -1;
		}	
		
		private float getXCoordinate(float radius) {
			return (float) (radius * Math.sin(thita) * Math.sin(phi));
		}
		
		private float getZCoordinate(float radius) {
			return (float) (radius * Math.sin(thita) * Math.cos(phi));
		}
		
		private float getYCoordinate(float radius) {
			return (float) (radius * Math.cos(thita));
		}
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) { 
			thita -= (float) (Math.PI / 14); 
			thita %= 2*Math.PI;
			canvas.repaint();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			thita += (float) (Math.PI / 14);
			thita %= 2*Math.PI;
			canvas.repaint();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			phi += (float) (Math.PI / 14);
			phi %= 2*Math.PI;
			canvas.repaint();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			phi -= (float) (Math.PI / 14);
			phi %= 2*Math.PI;
			canvas.repaint();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_I) {
			radius -= (radius > 5.0f) ? 2.0f : 0.0f;
			canvas.repaint();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_O) {
			radius +=  2.0f;
			canvas.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		screen_x = e.getX();
		screen_y = e.getY();
		mouseClicked = true;
		canvas.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

package graphics;

import graph.Connection;
import graph.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.vecmath.Vector3f;

import com.jogamp.opengl.util.gl2.GLUT;

import sort.Pair;
import sort.SenderReceiverPairs;


public class Model {
	private int levels, numOfNodes;

	private List<List<Vector3f>> layers;
	private List<Connection> connections;
	private List<List<Node>> sortedGraph;
	private Vector<SenderReceiverPairs> messages;
	private boolean pointForDraw [] [];  
	private String [] persistentPredicates, transientPredicates, transportPredicates, nodes;
	private String trace;
	
	public Model(int levels, int numOfNodes,List<Connection> connections, 
			List<List<Node>> sortedGraph, Vector<SenderReceiverPairs> messages,String [] persistentPredicates,
			String [] transientPredicates,String [] transportPredicates, String trace, String [] nodes){
		this.levels = levels;
		this.numOfNodes = numOfNodes;
		layers = new ArrayList<List<Vector3f>>();
		this.connections = connections;
		this.sortedGraph = sortedGraph;
		this.messages = messages;
		computePoints();
		pointForDraw = new boolean[levels] [numOfNodes+1];
		this.persistentPredicates = persistentPredicates;
		this.transientPredicates = transientPredicates;
		this.transportPredicates = transportPredicates;
		this.trace = trace;
		this.nodes = nodes;
	}
	
	private void computePoints () {
		
		// pre-compute the points across the circle 
		List<Vector3f> modelPoints =  new ArrayList<Vector3f>();
		for(int i = 0; i < numOfNodes; i++) {
			modelPoints.add(new Vector3f((float) ( 3 * Math.cos((2*Math.PI/numOfNodes)*(i) + Math.PI/4 )), 
						0.0f,(float) (3 * Math.sin((2*Math.PI / numOfNodes)*(i) + Math.PI/4 ))));
		}
		
		// a factor which centralizes the model towards (0,0,0)
		float layer_y =  (float) (levels-1 -(levels-1)/2.0);
		
		// construction of the the points in the three dimension universe 
		for(int i = 0; i < levels+1; i++) {
			List<Vector3f> layer = new ArrayList<Vector3f>();
			for(int j = 0; j < numOfNodes; j++) {
				layer.add(new Vector3f(modelPoints.get(j).x, layer_y - i,modelPoints.get(j).z));
			}
			layers.add(layer);
		}	
	}
	
	public void drawPoints(GLU glu, GL2 gl) {
	
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		GLUquadric quadric = glu.gluNewQuadric();
		glu.gluQuadricNormals(quadric, GL.GL_TRUE);
		
		int i,j;
		i = j = 0;
		
		for(int k = 0; k < layers.size()-1;k++) {
			List<Vector3f>layer = layers.get(k);
			for(Vector3f point : layer) {
				if(pointForDraw[i][j] || i== 0 ){
					if(i != 0)
						gl.glColor3f(0.0f,0.0f,0.0f);
					else
						gl.glColor3f(0.85f, 0.85f, 0.0f);
					
					gl.glLoadIdentity();
					gl.glTranslatef(point.x, point.y, point.z);
					glu.gluSphere(quadric,  (i==0) ? 0.11 : 0.07, 8, 8);
				}else {
					gl.glColor3f(0.6f,0.6f,0.6f);
					gl.glLoadIdentity();
					gl.glTranslatef(point.x, point.y, point.z);
					glu.gluSphere(quadric, 0.07, 8, 8);
				}
				j++;
			}
			j=0; i++;
		}
	}
	
	
	
	public void drawGraph(GL2 gl) {
		ListIterator<Connection> litr = connections.listIterator();
		
		while(litr.hasNext()) {
			Connection connection = litr.next();
			drawLine(gl,layers.get(0).get(connection.from), layers.get(0).get(connection.to));
		}
	}
	
	public void drawTimeLines(GL2 gl) {
		for(int i = 0; i < numOfNodes; i++) {
			drawLine(gl,layers.get(0).get(i), layers.get(levels).get(i));
		}
	}
	
	public void drawPyramids(GL2 gl) {
		
		List<Vector3f> tmp = layers.get(layers.size()-1);
		
		for(int i=0; i < numOfNodes; i++) {
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			Vector3f tmpVec = tmp.get(i);
			gl.glTranslatef(tmpVec.x,tmpVec.y,tmpVec.z);
			gl.glScalef(0.2f, 0.2f, 0.2f);
			
			gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex3f(0, -1, 0);
			gl.glVertex3f(1, 0, 0);
			gl.glVertex3f(0, 0, -1);
			gl.glVertex3f(-1, 0, 0);
			gl.glVertex3f(0, 0, 1);
			gl.glVertex3f(1, 0, 0);
			gl.glEnd();
		}
		
		gl.glLoadIdentity();
	}
	
	public void drawLabels(GL2 gl, GLUT glut){
		gl.glColor3f(0.85f, 0.85f, 0.0f);
		for(int i =0 ; i <numOfNodes; i++){
			Vector3f tmpVec = layers.get(0).get(i);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glRasterPos3f(tmpVec.x+0.2f, tmpVec.y+0.2f, tmpVec.z);
			glut.glutBitmapString(GLUT.BITMAP_9_BY_15, nodes[i]);
		}
		gl.glLoadIdentity();
	}
	
	public void drawMessages(GL2 gl) {
		for(int i = 1; i < sortedGraph.size(); i++) {
			List<Node> level = sortedGraph.get(i);
			for(Node node : level) {
				Vector3f end = layers.get(i).get(node.pair.getNode());
				
				pointForDraw[i][node.pair.getNode()] = true;
				
				List<Node> senderList = getSender(node);
				Iterator<Node> senderItr= senderList.iterator();
				
				while(senderItr.hasNext()) {
					Node sender = senderItr.next();
					int senderlevel = getSenderLevel(sender);
					int senderId =  sender.pair.getNode();
					Vector3f start = layers.get(senderlevel).get(senderId);
					
					pointForDraw [senderlevel] [senderId] = true;
					
					drawLine(gl, start, end);
				}
			}
		}
	}
	
	private int getSenderLevel(Node sender) {
		for(int i = 0; i < sortedGraph.size(); i++){
			for(Node n : sortedGraph.get(i)){
				if(n.toString().equals(sender.toString())){
					return i;
				}
			}
		}
		return -1;
	}
	
	private List<Node> getSender(Node receiver){
		List<Node> sendersList = new ArrayList<Node>();
		for(int i = 0; i < messages.size(); i++) {
			SenderReceiverPairs pair = messages.elementAt(i);
			if(pair.getReceiver().toString().equals(receiver.toString())) {
				sendersList.add(pair.getSender());
			}
		}
		return sendersList;
	}
	
	private void drawLine(GL2 gl, Vector3f start, Vector3f end) {
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glLineWidth(1.0f);
		gl.glDisable(GL2.GL_LINE_STIPPLE);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(start.x, start.y, start.z);
			gl.glVertex3f(end.x, end.y, end.z);
		gl.glEnd();
	}
	
	public List<List<AreaOfNode>> getAreas(GL2 gl, GLU glu) {
		List<List<AreaOfNode>> areas = new ArrayList<List<AreaOfNode>>();
		
		float [] projection = new float [16];
		gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX,projection,0);
        
		float [] modelView = new float [16]; 
		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX ,modelView,0);
        
		float [] screenCoordiants = new float[4];
        
        int [] viewPort = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort,0);
        
        for(int i = 0; i < layers.size(); i++) {
        	List<AreaOfNode> areasOfLevel = new ArrayList<AreaOfNode>();
        	for(int j = 0; j < numOfNodes; j++) {
        		Vector3f currentPoint = layers.get(i).get(j);
        		glu.gluProject(currentPoint.x, currentPoint.y, currentPoint.z, 
        				modelView, 0, 
        				projection, 0, 
        				viewPort, 0, 
        				screenCoordiants, 0);
        		screenCoordiants[1] = viewPort[3] - screenCoordiants[1] + 1;
        		Node node = new Node(new Pair(j, i-1));
        		areasOfLevel.add(new AreaOfNode(node, (int) screenCoordiants[0], (int) screenCoordiants[1]));
        	}
        	areas.add(areasOfLevel);
        }
       return areas;
	}
	
	public void checkPosition(int x, int y, List<List<AreaOfNode>> areas){
		Iterator<List<AreaOfNode>> areasItr = areas.iterator();

		while(areasItr.hasNext()) {
			List<AreaOfNode> currentLevel = areasItr.next();

			Iterator<AreaOfNode> areaItr = currentLevel.iterator();
			while(areaItr.hasNext()) {
				AreaOfNode area = areaItr.next();

				if(area.contains(x, y)){
					new Thread(new NodeWindow(area,sortedGraph,persistentPredicates,
							transientPredicates,transportPredicates,trace,nodes)).start();
					
				}
			}
		}
	}
}

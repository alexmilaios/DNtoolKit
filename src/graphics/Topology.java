package graphics;

import graph.Connection;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;
import javax.vecmath.Vector2d;

@SuppressWarnings("serial")
public class Topology extends JPanel {

	private List<Connection> connections;
	int numofnodes;
	String [] nodes;
	Vector2d [] positions;
	
	public Topology(int numodnodes, List<Connection> connections, String [] nodes){
		this.numofnodes = numodnodes;
		this.connections = connections;
		setSize(100,200);
		setBackground(new Color(0.95f, 0.95f, 0.95f));
		positions = new Vector2d [numodnodes];
		this.nodes = nodes;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.translate((int) this.getWidth()/2, (int) this.getHeight()/2);
		
		for(int i = 0; i < numofnodes; i++) {
			 positions[i] = new Vector2d(80 * Math.cos(((2*Math.PI)/numofnodes)*i), 
					 80 * Math.sin(((2*Math.PI)/numofnodes)*i));
		}
		
		drawPoints(g2d);
		
		drawConnections(g2d);
		
		drawLabels(g2d);
		
	}
	
	private void drawLabels(Graphics2D g2d) {
		g2d.setColor(Color.black);
		for(int i=0; i < numofnodes; i++) {
			g2d.drawString(nodes[i], (int) Math.round(positions[i].x), 
					(int)( Math.round(positions[i].y) + ((Math.round(positions[i].y) > 0)? 10:-5)));
		}
	}
	
	private void drawConnections(Graphics2D g2d) {
		g2d.setColor(Color.green);
		for(Connection con : connections){
			g2d.drawLine((int) Math.round(positions[con.from].x), (int) Math.round(positions[con.from].y), 
					(int) Math.round(positions[con.to].x),(int) Math.round(positions[con.to].y));
		}
	}
	
	private void drawPoints(Graphics2D g2d) {
		g2d.setColor(Color.yellow);
		for(int i = 0; i < numofnodes; i++) {
				g2d.fillOval((int) Math.round(positions[i].x)-4,(int) Math.round(positions[i].y)-4, 8, 8);
		}
	}
}

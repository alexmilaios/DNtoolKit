package dntoolkit;

import graph.Node;
import graph.Topology;
import graphics.ThreadedUniverse;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

import jogamp.nativewindow.NWJNILibLoader;

import sort.Layers;
import sort.LineReader;
import sort.SenderReceiverPairs;

@SuppressWarnings("serial")
public class Answer extends JPanel {

	public String trace;
	private DnToolKit kit;
	JLabel label;
	JButton vis ;
	JButton show ;
	String [] nodes,links;

	private void  AddListeners() {
		show.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.analize.showAns.setText(trace);
			}
		});

		vis.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String nodesStr = JTreeMethods.visitSimpleTree(kit.eastPanel.nodeTree);
				nodes= nodesStr.substring(1).split("\n");

				String[] traceTokens  = trace.split("\n");
				String trace2 ="";

				String linkStr = JTreeMethods.visitSimpleTree(kit.eastPanel.linkTree);
				links= linkStr.substring(1).split("\n");
				
				for(int i = 0; i < traceTokens.length; i++) {
					String tmp = traceTokens[i];
					StringTokenizer tokens2 = new StringTokenizer(tmp,"(");

					if(tokens2.nextToken().contains("receive"))
						trace2 += substitude(tmp,0);
					else
						trace2 += substitude(tmp,1);
				}
				
				Layers layers = new Layers(trace2);

				List<List<Node>> levels = layers.layers; 
				Vector<SenderReceiverPairs> messages = layers.messages; 
				
				
				String tmp = JTreeMethods.visitSimpleTree2(kit.westPanel.perTree,"persistent");
				String[] persistentPredicates = tmp.split(" "); 
				
				String[] transportPredicates = (JTreeMethods.visitSimpleTree2(kit.westPanel.tranTree,"transport")).split(" "); 
				System.out.println(getDummyTrace(links));
				
				new Thread(new  ThreadedUniverse(levels,messages,(new Topology(new Layers(getDummyTrace(links)).messages)).getTopology(),
						nodes.length,persistentPredicates,null,transportPredicates,trace2,nodes)).start();
			}
		});
	}
	
	
	private String getDummyTrace(String[] links) {
		String output="";
		for(int i =0; i < links.length ; i++) {
			StringTokenizer tmp = new StringTokenizer(links[i],"(");
			tmp.nextToken();
			String [] tmp2 = tmp.nextToken().split(",");
			String src = tmp2[0];
			String dest = tmp2[1].substring(0,tmp2[1].length()-1);
			output += "receive_dummy(" + findIndex(dest) + "," +findIndex(src)+",0,0)\n";
		}
		return output;
	}
	
	private String substitude(String line,int flag) {
		StringTokenizer tokens = new StringTokenizer(line,"(");
		String head = tokens.nextToken();
		String secondPart = tokens.nextToken();

		secondPart = secondPart.substring(0,secondPart.length()-1);

		String [] array = secondPart.split(",");
		array[0] = findIndex(array[0]) +"";
		if(flag == 0)
			array[array.length-3] = findIndex(array[array.length-3]) + "";

		String total ="";
		int i;
		for(i = 0; i< array.length; i++) {
			total += array[i] +",";
		}
		total = total.substring(0,total.length()-1);

		return head + "("+ total + ")\n"; 
	}
	
	private int findIndex( String target) {
		for(int i =0; i < nodes.length; i++){
			if(nodes[i].equals(target))
				return i;
		}
		return -1;
	}
	
	public Answer(String trace, DnToolKit kit, int sol){
		this.trace = trace.substring(0,trace.length()-1);
		this.kit = kit;
		label = new JLabel("Answer: " + sol);
		vis = new JButton("visualize " + sol);
		show = new JButton("show " + sol);
		setLayout(new FlowLayout());
		add(label);
		add(show);
		add(vis);
		AddListeners();
	}

}

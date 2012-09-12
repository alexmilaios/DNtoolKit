package graphics;

import graph.Node;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sort.LineReader;
import sort.Pair;

public class NodeWindow implements Runnable {

	private AreaOfNode point;
	private List<List<Node>> sortedGraph;
	private Pair pointId;
	private String [] persistentPredicates, transientPredicates, transportPredicates;
	private String trace;
	private String [] nodes;
	
	public NodeWindow(AreaOfNode node,List<List<Node>> sortedGraph, String [] persistentPredicates, 
			String [] transienPredicates, String [] transportPredicates, String trace, String [] nodes) {
		this.point= node;
		this.sortedGraph = sortedGraph;
		pointId = getOriginalTime();
		this.persistentPredicates = persistentPredicates;
		this.transientPredicates = transienPredicates;
		this.transportPredicates = transportPredicates;
		this.nodes = nodes;
		this.trace = trace;
	}
	
	private Pair getOriginalTime() {
		int level = point.node.pair.getTime() + 1, node = point.node.pair.getNode();
		List<Node> layer = sortedGraph.get(level);
		Iterator<Node> itr = layer.iterator();
		while(itr.hasNext()) {
			Node current = itr.next();
			if(current.pair.getNode() == node){
				return(new Pair(node,current.pair.getTime()));
			}
		}
		return new Pair(node, -2);
	}
		
	@Override
	public void run() {
		if(pointId.getTime() != -2)
			new Window();

	}

	
	@SuppressWarnings("serial")
	class Window extends JFrame implements ActionListener{
		
		JButton search = new JButton("Search");
		JRadioButton persisten = new JRadioButton("persistent");
		JRadioButton transien = new JRadioButton("transient");
		JRadioButton transport = new JRadioButton("transport");
		ButtonGroup group = new ButtonGroup();
		JTextArea outputPredicates = new JTextArea(10,10);
		JTextField predicateName = new JTextField(10);
		
		public Window() {
			
			super("Predicates for node " + nodes[point.node.pair.getNode()] + 
					" at time " + point.node.pair.getTime() );
			
			setSize(400,300);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			JPanel panel = new JPanel();
			JLabel typesLabel = new JLabel("Type of predicates: ");
			panel.add(typesLabel);
			persisten.setSelected(true);
			group.add(persisten);
			group.add(transien);
			group.add(transport);
			search.addActionListener(this);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(persisten);
			panel.add(transien);
			panel.add(transport);
			JPanel filter = new JPanel();
			JLabel filterName = new JLabel("Name of predicate");
			filter.setLayout(new FlowLayout());
			panel.add(filterName);
			filter.add(predicateName);
			panel.add(filter);
			outputPredicates.setEditable(false);
			JScrollPane scroll = new JScrollPane(outputPredicates);
			setLayout(new BorderLayout());
			add(panel, BorderLayout.WEST);
			JPanel south = new JPanel();
			south.setLayout(new FlowLayout());
			add(south.add(search), BorderLayout.SOUTH);
			add(scroll,BorderLayout.CENTER);
			setVisible(true);
		}
		
		private boolean isPersistent(String predicate) {
			if(persistentPredicates != null){
				for(int i = 0; i < persistentPredicates.length; i++)
					if(persistentPredicates[i].equals(predicate))
						return true;
			}
			return false;
		}
		
		private boolean isTransient(String predicate) {
			if(transientPredicates != null){
				for(int i = 0; i < transientPredicates.length; i++)
					if(transientPredicates[i].equals(predicate))
						return true;
			}
			return false;
		}
		
		private boolean isTransport(String predicate) {
			if(transportPredicates != null){
				for(int i = 0; i < transportPredicates.length; i++)
					if(transportPredicates[i].equals(predicate))
						return true;
			}
			return false;
		}
		
		
		private String getPredicates(String type, String name) throws Exception {
			
			BufferedReader in =  new BufferedReader(new InputStreamReader(new ByteArrayInputStream(trace.getBytes())));
			String line = "",output = "";
			while((line = in.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line,"(");
				String testname = token.nextToken();
				if(type == "persistent" && isPersistent(testname) && testname.equals(name)){
					Pair tmp = LineReader.getReceiver(line);
					tmp.setTime(tmp.getTime()-1);
					if(pointId.equals(tmp)){
						output += resubstitude(line, 1);
					}
				}
				else if(type == "transient" && isTransient(testname) && testname.equals(name)){
					Pair tmp = LineReader.getReceiver(line);
					tmp.setTime(tmp.getTime()-1);
					if(pointId.equals(tmp)){
						output += line + "\n";
					}
				}
				else if (type == "transport" && isTransport(testname) && testname.equals(name)){
					Pair tmp = LineReader.getReceiver(line);
					if(LineReader.isReceive(line)){
						tmp.setTime(tmp.getTime());
						if(pointId.equals(tmp)){
							output += resubstitude(line, 0);
						}
					}
					else{
						tmp.setTime(tmp.getTime()-1);
						if(pointId.equals(tmp)){
							output += line + "\n";
						}
					}
				}
			}
			return output;
		}

		private String resubstitude(String line,int flag) {
			StringTokenizer tokens = new StringTokenizer(line,"(");
			String head = tokens.nextToken();
			String secondPart = tokens.nextToken();

			secondPart = secondPart.substring(0,secondPart.length()-1);

			String [] array = secondPart.split(",");
			array[0] = nodes[Integer.parseInt(array[0])] ;
			if(flag == 0)
				array[array.length-3] = nodes[Integer.parseInt(array[array.length-3])];

			String total ="";
			int i;
			for(i = 0; i< array.length; i++) {
				total += array[i] +",";
			}
			total = total.substring(0,total.length()-1);

			return head + "("+ total + ")\n"; 
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				if(persisten.isSelected()) {	
					outputPredicates.setText(getPredicates("persistent",predicateName.getText()));
				} else if(transien.isSelected()) {
					outputPredicates.setText(getPredicates("transient",predicateName.getText()));
				}else {
					outputPredicates.setText(getPredicates("transport",predicateName.getText()));
				}
				repaint();
			}catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
	}
}

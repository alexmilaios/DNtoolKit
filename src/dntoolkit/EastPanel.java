package dntoolkit;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;


@SuppressWarnings("serial")
public class EastPanel extends JPanel {

	private JTextArea console;

	private WestPanel westpanel;

	private EastPanel east;
	
	JTabbedPane initTupples = new JTabbedPane();
	
	/*
	 * Node Panel elements
	 */
	JPanel nodeNamePanel = new JPanel();
	JLabel nodeTitle = new JLabel("Nodes");
	JLabel nodeLabel = new JLabel("Node ID: ");

	JTextField nodeID = new JTextField(15);

	/*
	 * Node Part
	 */
	JPanel nodePanel = new JPanel();
	DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("Nodes");
	public JTree nodeTree = new JTree(nodeRoot);
	public JScrollPane nodeScorl = new JScrollPane(nodeTree);

	JPanel buttonsNode = new JPanel(new FlowLayout());
	public JButton addNode = new JButton("add");
	public JButton delNode = new JButton("delete");

	/*
	 * Links Part
	 */
	JPanel linkPanel = new JPanel();
	JLabel links = new JLabel("Links");

	JPanel linksOptions = new JPanel();

	JPanel srcPanel = new JPanel();
	JLabel srcLabel = new JLabel("Source: ");
	JComboBox src = new JComboBox();

	JPanel destPanel = new JPanel();
	JLabel destLabel = new JLabel("Destination: ");
	JComboBox dest = new JComboBox();

	JCheckBox bidirect = new JCheckBox("Bidirectional", true); 

	DefaultMutableTreeNode linkRoot = new DefaultMutableTreeNode("Links");
	public JTree linkTree = new JTree(linkRoot);
	public JScrollPane linkScorl = new JScrollPane(linkTree);

	JPanel buttonsLink = new JPanel(new FlowLayout());
	public JButton addLink = new JButton("add");
	public JButton delLink = new JButton("delete");


	/*
	 * Initial Persistent Tuples
	 */
	JPanel initPer = new JPanel();
	JLabel initPerLabel = new JLabel("Inital Persisten Tuples");

	JPanel initPerOptions = new JPanel();

	JPanel initPerTuplePanel = new JPanel();
	JLabel initPerTupleLabel = new JLabel("Tuple: ");
	JTextField initPerTuple = new JTextField(15);

	JPanel nodePerPanel = new JPanel();
	JLabel nodePerLabel = new JLabel("Node: ");
	JComboBox nodePer = new JComboBox();

	DefaultMutableTreeNode initPerRoot = new DefaultMutableTreeNode("Initial Persistent Tuples");
	public JTree initPerTree = new JTree(initPerRoot);
	public JScrollPane initPerScorl = new JScrollPane(initPerTree);

	JPanel buttonsInitPer = new JPanel(new FlowLayout());
	public JButton addInitPer = new JButton("add");
	public JButton delInitPer = new JButton("delete");
	
	
	/*
	 * Initial Transport Tuples 
	 */
	JPanel initTran = new JPanel();
	JLabel initTranLabel = new JLabel("Inital Transport Tuples");

	JPanel initTranOptions = new JPanel();

	JPanel initTranTuplePanel = new JPanel();
	JLabel initTranTupleLabel = new JLabel("Tuple: ");
	JTextField initTranTuple = new JTextField(15);

	JPanel linkTranPanel = new JPanel();
	JLabel linkTranLabel = new JLabel("Link : ");
	JComboBox linkTran = new JComboBox();

	
	DefaultMutableTreeNode initTranRoot = new DefaultMutableTreeNode("Initial Transport Tuples");
	public JTree initTranTree = new JTree(initTranRoot);
	public JScrollPane initTranScorl = new JScrollPane(initTranTree);

	JPanel buttonsInitTran = new JPanel(new FlowLayout());
	public JButton addInitTran = new JButton("add");
	public JButton delInitTran = new JButton("delete");

	/*
	 * Initial Input Tuples
	 */
	JPanel initIn = new JPanel();
	JLabel initInLabel = new JLabel("Inital Input Tuples");

	JPanel initInOptions = new JPanel();

	JPanel initInTuplePanel = new JPanel();
	JLabel initInTupleLabel = new JLabel("Tuple: ");
	JTextField initInTuple = new JTextField(15);

	DefaultMutableTreeNode initInRoot = new DefaultMutableTreeNode("Initial Input Tuples");
	public JTree initInTree = new JTree(initInRoot);
	public JScrollPane initInScorl = new JScrollPane(initInTree);

	JPanel buttonsInitIn = new JPanel(new FlowLayout());
	public JButton addInitIn = new JButton("add");
	public JButton delInitIn = new JButton("delete");
	
	/*
	 * Time Part
	 */
	JPanel maxTime = new JPanel();
	JPanel fieldPanel = new JPanel();
	JLabel timeLabel = new JLabel("MaxTime: ");
	JTextField time = new JTextField(5);
	public JButton setTime = new JButton("set");
	public int Maxtime = -1; 
	
	
	
	private void addTrees() {
		nodePanel.setLayout(new BoxLayout(nodePanel,BoxLayout.Y_AXIS));

		nodeNamePanel.add(nodeLabel);
		nodeNamePanel.add(nodeID);
		nodePanel.add(nodeTitle);
		nodePanel.add(nodeNamePanel);

		nodePanel.add(nodeScorl);
		buttonsNode.add(addNode);
		buttonsNode.add(delNode);
		nodePanel.add(buttonsNode);
		add(nodePanel);


		linkPanel.setLayout(new BoxLayout(linkPanel, BoxLayout.Y_AXIS));
		linkPanel.add(links);

		srcPanel.add(srcLabel);
		srcPanel.add(src);

		destPanel.add(destLabel);
		destPanel.add(dest);

		linksOptions.add(srcPanel);
		linksOptions.add(destPanel);

		linkPanel.add(srcPanel);
		linkPanel.add(destPanel);
		linkPanel.add(bidirect);

		linkPanel.add(linkScorl);
		buttonsLink.add(addLink);
		buttonsLink.add(delLink);
		linkPanel.add(buttonsLink);

		add(linkPanel);

		initPer.setLayout(new BoxLayout(initPer, BoxLayout.Y_AXIS));
		initPer.add(initPerLabel);

		initPerTuplePanel.add(initPerTupleLabel);
		initPerTuplePanel.add(initPerTuple);

		nodePerPanel.add(nodePerLabel);
		nodePerPanel.add(nodePer);
		
		
		initPerOptions.setLayout(new BoxLayout(initPerOptions,BoxLayout.Y_AXIS));
		initPerOptions.add(initPerTuplePanel);
		initPerOptions.add(nodePerPanel);

		initPer.add(initPerOptions);

		initPer.add(initPerScorl);
		buttonsInitPer.add(addInitPer);
		buttonsInitPer.add(delInitPer);
		initPer.add(buttonsInitPer);

		initTupples.add("Pesistent",initPer);
		
		initTran.setLayout(new BoxLayout(initTran, BoxLayout.Y_AXIS));
		initTran.add(initTranLabel);

		initTranTuplePanel.add(initTranTupleLabel);
		initTranTuplePanel.add(initTranTuple);
		
		linkTranPanel.add(linkTranLabel);
		linkTranPanel.add(linkTran);
		

		initTranOptions.setLayout(new BoxLayout(initTranOptions,BoxLayout.Y_AXIS));
		
		initTranOptions.add(initTranTuplePanel);
		initTranOptions.add(linkTranPanel);
		initTranOptions.add(linkTranPanel);
		
		initTran.add(initTranOptions);

		initTran.add(initTranScorl);
		buttonsInitTran.add(addInitTran);
		buttonsInitTran.add(delInitTran);
		initTran.add(buttonsInitTran);

		initTupples.add("Transport",initTran);
		
		initIn.setLayout(new BoxLayout(initIn, BoxLayout.Y_AXIS));
		initIn.add(initInLabel);

		initInTuplePanel.add(initInTupleLabel);
		initInTuplePanel.add(initInTuple);

		initInOptions.add(initInTuplePanel);

		initIn.add(initInOptions);

		initIn.add(initInScorl);
		buttonsInitIn.add(addInitIn);
		buttonsInitIn.add(delInitIn);
		initIn.add(buttonsInitIn);

		initTupples.add("Input",initIn);
		
		maxTime.setLayout(new BoxLayout(maxTime, BoxLayout.Y_AXIS));
		fieldPanel.add(timeLabel);
		fieldPanel.add(time);
		fieldPanel.add(setTime);
		maxTime.add(fieldPanel);
		
		initTupples.add("MaxTime", maxTime);
		
		add(initTupples);
	}

	private String check_node(String name){
		String output = name.trim();
		StringTokenizer tokens = new StringTokenizer(output," ");
		String result = "";
		while(tokens.hasMoreElements()) {
			result += tokens.nextToken();
		}
		String head = result.charAt(0) + "";
		head = head.toLowerCase();
		result = head + result.substring(1);
		return result;
	}

	public void addListenersOnTrees() {

		addNode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nodeID.getText() + "";
				if (name.equals(""))
					return;

				name = check_node(name);

				if(JTreeMethods.isMember(nodeTree, name)){
					console.setText("Enter twice the same node");
					return;
				}

				DefaultTreeModel model = (DefaultTreeModel) nodeTree.getModel();

				int startRow = 0;
				String prefix = "Nodes";
				TreePath path = nodeTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(name);

				model.insertNodeInto(newNode, node, node.getChildCount());
				nodeID.setText("");
				src.addItem(name);
				dest.addItem(name);
				nodePer.addItem(name);
				repaint();
			}
		});

		delNode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)nodeTree.getModel();

				TreePath path = nodeTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					src.removeItem(node.toString());
					dest.removeItem(node.toString());
					nodePer.removeItem(node.toString());
					
					JTreeMethods.visitDeleteLastElementTree(initPerTree, node.toString());
					JTreeMethods.deleteLinksTree(linkTree, node.toString(), east);
					
					model.removeNodeFromParent(node);
				}

				repaint();
			}
		});

		addLink.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String source =  (String) src.getSelectedItem();
				String destination = (String) dest.getSelectedItem();
				String value = "neighbour("+ source +"," + destination +")";

				if(JTreeMethods.isMember(linkTree, value)){
					console.setText("Enter twice the same link");
					return;
				}

				if(source.equals(destination)){
					console.setText("destination is equal to source");
					return;
				}

				DefaultTreeModel model = (DefaultTreeModel) linkTree.getModel();

				int startRow = 0;
				String prefix = "Links";
				TreePath path = linkTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(value);

				model.insertNodeInto(newNode, node, node.getChildCount());

				if(bidirect.isSelected()){
					src.setSelectedItem(destination);
					dest.setSelectedItem(source);
					bidirect.setSelected(false);
					addLink.doClick();
				}
				linkTran.addItem(value);
				repaint();
			}
		});

		delLink.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)linkTree.getModel();

				TreePath path = linkTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					model.removeNodeFromParent(node);
					linkTran.removeItem(node.toString());
					JTreeMethods.visitDeleteLastElementTree(initTranTree, node.toString());
				}
				repaint();
			}
		});

		addInitPer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String tuple = check_node(initPerTuple.getText());
				String inputNode = (String) nodePer.getSelectedItem();

				StringTokenizer tokens = new StringTokenizer(tuple,"(");
				String name = tokens.nextToken();
				int arguments = getArguments(tokens.nextToken());

				name +="/" + arguments;

				if(!JTreeMethods.isMember(westpanel.perTree, name)){
					console.setText("There is not such a tuple: " + name);
					return;
				}

				DefaultTreeModel model = (DefaultTreeModel) initPerTree.getModel();

				int startRow = 0;
				String prefix = "Initial Persistent Tuples";
				TreePath path = initPerTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(tuple + " holds at " + inputNode);

				model.insertNodeInto(newNode, node, node.getChildCount());
				initPerTuple.setText("");
				repaint();
			}
		});

		delInitPer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)initPerTree.getModel();

				TreePath path = initPerTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					model.removeNodeFromParent(node);
				}

				repaint();
			}
		});
		
		addInitTran.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String tuple = check_node(initTranTuple.getText());
				String inputLink = (String) linkTran.getSelectedItem();
				
				StringTokenizer tokens = new StringTokenizer(tuple,"(");
				String name = tokens.nextToken();
				int arguments = getArguments(tokens.nextToken());

				name +="/" + arguments;

				if(!JTreeMethods.isMember(westpanel.tranTree, name)){
					console.setText("There is not such a tuple: " + name);
					return;
				}

				DefaultTreeModel model = (DefaultTreeModel) initTranTree.getModel();

				int startRow = 0;
				String prefix = "Initial Transport Tuples";
				TreePath path = initTranTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(tuple + " is send over: " +inputLink);

				model.insertNodeInto(newNode, node, node.getChildCount());
				initTranTuple.setText("");
				repaint();
			}
		});

		delInitTran.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)initTranTree.getModel();

				TreePath path = initTranTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					model.removeNodeFromParent(node);
				}

				repaint();
			}
		});
		
		addInitIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String tuple = check_node(initInTuple.getText());

				StringTokenizer tokens = new StringTokenizer(tuple,"(");
				String name = tokens.nextToken();
				int arguments = getArguments(tokens.nextToken());

				name +="/" + arguments;

				if(!JTreeMethods.isMember(westpanel.inTree, name)){
					console.setText("There is not such a tuple: " + name);
					return;
				}

				DefaultTreeModel model = (DefaultTreeModel) initInTree.getModel();

				int startRow = 0;
				String prefix = "Initial Input Tuples";
				TreePath path = initInTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(tuple + " holds");

				model.insertNodeInto(newNode, node, node.getChildCount());
				initInTuple.setText("");
				repaint();
			}
		});

		delInitIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)initInTree.getModel();

				TreePath path = initInTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					model.removeNodeFromParent(node);
				}

				repaint();
			}
		});
		
		setTime.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Maxtime = Integer.parseInt(time.getText());
					 console.setText("Max time is equal to: " + Maxtime);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private int getArguments(String input) {
		StringTokenizer tokens = new StringTokenizer(input,",");
		return tokens.countTokens();
	}

	public EastPanel(JTextArea console, WestPanel westpanel) {
		super();
		setPreferredSize(new Dimension(250,(int) this.getPreferredSize().getHeight()));
		this.westpanel = westpanel;
		east = this;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.console = console;
		addTrees();

	}
}

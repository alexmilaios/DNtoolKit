package dntoolkit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import dntoolkit.JTreeMethods;


@SuppressWarnings("serial")
public class WestPanel extends JPanel {
	
	
	private JTextArea console;
	
	/*
	 * Input of the Predicates
	 */
	JPanel namePanel = new JPanel();
	JLabel nameLabel = new JLabel("Name: ");

	JPanel arrityPanel = new JPanel();
	JLabel arrityLabel = new JLabel("Arrity: ");

	public JTextField namePredicate = new JTextField(15);
	public JTextField arrity = new JTextField(5);
	
	/*
	 * Persistent Part
	 */
	JPanel persistentPanel = new JPanel();
	DefaultMutableTreeNode perRoot = new DefaultMutableTreeNode("Persistent Predicates");
	public JTree perTree = new JTree(perRoot);
	public JScrollPane perScorl = new JScrollPane(perTree);

	JPanel buttonsPer = new JPanel(new FlowLayout());
	public JButton addPer = new JButton("add");
	public JButton delPer = new JButton("delete");

	/*
	 * Transport Part 
	 */
	JPanel transportPanel = new JPanel();
	DefaultMutableTreeNode tranRoot = new DefaultMutableTreeNode("Transport Predicates");
	public JTree tranTree = new JTree(tranRoot);
	public JScrollPane tranScorl = new JScrollPane(tranTree);

	JPanel buttonsTran = new JPanel(new FlowLayout());
	public JButton addTran = new JButton("add");
	public JButton delTran = new JButton("delete");
	
	
	/*
	 * Input Part
	 */
	JPanel inPanel = new JPanel();
	DefaultMutableTreeNode inRoot = new DefaultMutableTreeNode("Input Predicates");
	JTree inTree = new JTree(inRoot);
	JScrollPane inScorl = new JScrollPane(inTree);

	JPanel buttonsIn = new JPanel(new FlowLayout());
	JButton addIn = new JButton("add");
	JButton delIn = new JButton("delete");
	
	
	public void addListenersOnTrees() {

		addPer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = namePredicate.getText();
				int arr;
				try{
					arr = Integer.parseInt(arrity.getText()); 
				} catch(Exception ex){
					console.setText("The arrity should be a number\n");
					return;
				}
				if(arr < 0){
					console.setText("The arrity should not be negative\n");
					return;
				}
				if(JTreeMethods.isMember(perTree, name + "/" +arr) || 
						JTreeMethods.isMember(tranTree, name + "/" +arr) ){
					console.setText("Enter twice the same predicate");
					return;
				}
				
				DefaultTreeModel model = (DefaultTreeModel) perTree.getModel();

				int startRow = 0;
				String prefix = "Persistent Predicates";
				TreePath path = perTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(name + "/" +arr);

				model.insertNodeInto(newNode, node, node.getChildCount());
				
				namePredicate.setText("");
				arrity.setText("");
				repaint();
			}
		});

		delPer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)perTree.getModel();

				TreePath path = perTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					model.removeNodeFromParent(node);
				}
				
				repaint();
			}
		});

		addTran.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = namePredicate.getText();
				int arr;
				try{
					arr = Integer.parseInt(arrity.getText()); 
				} catch(Exception ex){
					console.setText("The arrity should be a number\n");
					return;
				}
				if(arr < 0){
					console.setText("The arrity should not be negative\n");
					return;
				}
				if(JTreeMethods.isMember(perTree, name + "/" +arr) || 
						JTreeMethods.isMember(tranTree, name + "/" +arr) ){
					console.setText("Enter twice the same predicate");
					return;
				}
				DefaultTreeModel model = (DefaultTreeModel) tranTree.getModel();

				int startRow = 0;
				String prefix = "Transport Predicates";
				TreePath path = tranTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(name + "/" +arr);

				model.insertNodeInto(newNode, node, node.getChildCount());

				namePredicate.setText("");
				arrity.setText("");
				repaint();
			}
		});

		delTran.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)tranTree.getModel();
				
				TreePath path = tranTree.getSelectionPath();

				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					
					model.removeNodeFromParent(node);
				}

				repaint();
			}
		});
		
		addIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = namePredicate.getText();
				int arr;
				try{
					arr = Integer.parseInt(arrity.getText()); 
				} catch(Exception ex){
					return;
				}
				DefaultTreeModel model = (DefaultTreeModel) inTree.getModel();

				int startRow = 0;
				String prefix = "Input Predicates";
				TreePath path = inTree.getNextMatch(prefix, startRow, Position.Bias.Forward);
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

				MutableTreeNode newNode = new DefaultMutableTreeNode(name + "/" +arr);

				model.insertNodeInto(newNode, node, node.getChildCount());

				namePredicate.setText("");
				arrity.setText("");
				repaint();
			}
		});

		delIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)inTree.getModel();

				TreePath path = inTree.getSelectionPath();
				if(path != null){
					MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
					model.removeNodeFromParent(node);
				}
				repaint();
			}
		});
	}
	
	private void addTrees() {
		persistentPanel.setLayout(new BoxLayout(persistentPanel,BoxLayout.Y_AXIS));
		persistentPanel.add(perScorl);
		buttonsPer.add(addPer);
		buttonsPer.add(delPer);
		persistentPanel.add(buttonsPer);
		add(persistentPanel);

		transportPanel.setLayout(new BoxLayout(transportPanel,BoxLayout.Y_AXIS));
		transportPanel.add(tranScorl);
		buttonsTran.add(addTran);
		buttonsTran.add(delTran);
		transportPanel.add(buttonsTran);
		add(transportPanel);
		
		inPanel.setLayout(new BoxLayout(inPanel,BoxLayout.Y_AXIS));
		inPanel.add(inScorl);
		buttonsIn.add(addIn);
		buttonsIn.add(delIn);
		inPanel.add(buttonsIn);

		add(inPanel);
	}
	
	public WestPanel(JTextArea console){
		super();
		this.console = console;
		setPreferredSize(new Dimension(230,(int) this.getPreferredSize().getHeight()));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		namePanel.add(nameLabel);
		namePanel.add(namePredicate);
		arrityPanel.add(arrityLabel);
		arrityPanel.add(arrity);
		add(namePanel);
		add(arrityPanel);
		addTrees();
	}
}

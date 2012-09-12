package dntoolkit;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import parser.ParseException;
import parser.Parser;
import rules.Input;
import rules.Persistent;
import rules.Rule;
import rules.Transport;


@SuppressWarnings("serial")
public class DnToolKit extends JFrame {

	private static int numOfEditTabs = 0; 
	private static int numOfLowTabs = 0;

	public String qurryClicked = null;

	public boolean manetFlag = false;

	/*
	 * Main File
	 */
	File mainFile = null;
	String confFileLP = "dn_files/configuration.lp";
	File confFile = null;

	/*
	 * Parser
	 */
	String input = "";
	Parser myParser = new Parser(new ByteArrayInputStream(input.getBytes()));

	/*
	 * Central Panel
	 */
	JPanel centralPanel = new JPanel(new BorderLayout());

	JMenuBar menubar = new JMenuBar();
	/*
	 * File menu
	 */
	JMenu file  = new JMenu("File");
	JMenuItem newFile = new JMenuItem("New");
	JMenuItem loadFile = new JMenuItem("Load");
	JMenuItem close = new JMenuItem("Close");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem save_as = new JMenuItem("Save as");
	JMenuItem quit = new JMenuItem("Quit");

	/*
	 * View menu
	 */
	JMenu view = new JMenu("View");
	JMenuItem showLow = new JMenuItem("Show Clingo Language");
	JMenuItem removeLow = new JMenuItem("Close Clingo Language");

	/*
	 * Parse menu
	 */
	JMenu parse = new JMenu("Parse");
	JMenuItem parser = new JMenuItem("Parse");

	/*
	 * Console
	 */
	public JTabbedPane downTap = new JTabbedPane();
	public JTextArea console = new JTextArea(5,60);

	/*
	 * Analysis menu
	 */
	JMenu analisys = new JMenu("Analysis");
	Analisys analize = new Analisys(this);
	JMenuItem loadTrace = new JMenuItem("Load Trace File");


	/*
	 * Communication menu
	 */
	JMenu communiation = new JMenu("Communication Model");
	JRadioButtonMenuItem synch = new JRadioButtonMenuItem("Synchronous");
	JRadioButtonMenuItem asynch = new JRadioButtonMenuItem("Asynchronous");
	String com_model = "dn_files/synchronous.lp";

	/*
	 * Configuration menu
	 */
	JMenu configuaration = new JMenu("Configuration");
	JMenuItem add_Conf = new JMenuItem("Add Configuation");
	JMenuItem saveAsConf = new JMenuItem("Save as Configuation");
	JMenuItem saveConf = new JMenuItem("Save Configuation");
	JMenuItem loadConf = new JMenuItem("Load Configuation");
	JMenuItem delConf = new JMenuItem("Delete Configuation");
	/*
	 * Editors Tabs
	 */
	JTabbedPane editorTabs = new JTabbedPane();
	JPanel editpane = new JPanel();
	JTextArea text ;

	/*
	 * Clingo Tabs
	 */
	JTextArea clingo ;

	/*
	 * West Side Part
	 */
	public WestPanel westPanel = new WestPanel(console);

	/*
	 * East Side Part
	 */
	public EastPanel eastPanel = new EastPanel(console,westPanel);


	/*
	 * Query menu  
	 */
	Querry query = new Querry("Query",this,eastPanel);

	private void addListenersOnView() {
		showLow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(numOfLowTabs < 1) {
					clingo = new JTextArea(30,30);
					clingo.setEditable(false);
					String line,output="";
					parser.doClick();
					if(! (console.getText().equals("Parsing finished successfully")))
						return;
					try {
						BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(new File("dn_files/output.lp")))); 
						while((line = buffer.readLine()) != null) {
							output += line + "\n";
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					clingo.setText(output);
					JPanel auxpane = new JPanel();
					JLabel header = new JLabel("Low-level language");
					auxpane.setLayout(new BorderLayout());		
					JScrollPane scroll = new JScrollPane(clingo);
					auxpane.add("North",header);
					auxpane.add("Center", scroll);
					editpane.add(auxpane);
					repaint();
					numOfLowTabs++;
				}
			}
		});

		removeLow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(numOfLowTabs == 1) {
					editpane.remove(1);
					repaint();
					numOfLowTabs--;
				}
			}
		});
	}

	private void addListenersOnFile() {

		newFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(numOfEditTabs++ < 1){
					text = new JTextArea(30,30);
					text.setEditable(true);
					JPanel auxpane = new JPanel();
					JLabel header = new JLabel("High-level language");
					auxpane.setLayout(new BorderLayout());		
					JScrollPane scroll = new JScrollPane(text);
					auxpane.add("North",header);
					auxpane.add("Center", scroll);
					editpane.add(auxpane);
				}
			}
		});

		quit.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(numOfEditTabs == 1) {
					editpane.removeAll();
					if(editorTabs.getTabCount()>1)
						editorTabs.remove(1);
					deleteLeafs(westPanel.perTree, westPanel.perRoot);
					deleteLeafs(westPanel.tranTree, westPanel.tranRoot);
					deleteLeafs(westPanel.inTree, westPanel.inRoot);
					numOfEditTabs--;
					if(numOfLowTabs == 1)
						numOfLowTabs--;
					repaint();
				}
			}
			
		});

		loadFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(numOfEditTabs < 1){
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter ft = new FileNameExtensionFilter("Declarative Networking", "dn");
					chooser.addChoosableFileFilter(ft);
					chooser.showOpenDialog(null);
					mainFile = chooser.getSelectedFile();
					if(mainFile != null){
						populateFields(mainFile);
						numOfEditTabs++;
					}
				}
			}
		});

		save_as.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(mainFile == null) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter ft = new FileNameExtensionFilter("Declarative Networking", "dn");
					chooser.addChoosableFileFilter(ft);
					chooser.showSaveDialog(null);
					mainFile = chooser.getSelectedFile();
				}
				if(mainFile != null)
					save.doClick();
			}
		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(mainFile == null){
					save_as.doClick();
				}else {
					try {
						OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mainFile));
						out.write(JTreeMethods.visitTree(westPanel.perTree,"persistent"));
						out.write(JTreeMethods.visitTree(westPanel.tranTree,"transport"));
						out.write(JTreeMethods.visitTree(westPanel.inTree,"input"));
						out.write(text.getText());
						out.flush();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	private void addListenersOnConf() {
		add_Conf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(eastPanel.Maxtime == -1){
					console.setText("Enter Max Time");
					return;
				}
				try {
					OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(confFileLP)));
					out.write("#const maxtime = " + eastPanel.Maxtime + ".\n");
					out.write("time(0.." + (eastPanel.Maxtime -1) + ").\n");
					out.write("extended_time(0.." + eastPanel.Maxtime + ").\n");
					out.write("maxtime("+ (eastPanel.Maxtime - 1) +").");
					out.write(JTreeMethods.visitNodeTree(eastPanel.nodeTree));
					out.write(JTreeMethods.visitLinkTree(eastPanel.linkTree));
					out.write(JTreeMethods.visitPerTree(eastPanel.initPerTree));
					out.write(JTreeMethods.visitTranTree(eastPanel.initTranTree));
					out.write(JTreeMethods.visitInTree(eastPanel.initInTree));
					out.flush();
					out.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		saveAsConf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(confFile == null) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter ft = new FileNameExtensionFilter("Declarative Networking", "dn");
					chooser.addChoosableFileFilter(ft);
					chooser.showSaveDialog(null);
					confFile = chooser.getSelectedFile();
				}
				if(confFile != null)
					saveConf.doClick();
			}
		});

		saveConf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(confFile == null){
					saveAsConf.doClick();
				}else {
					try {
						OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(confFile));
						TreeNode root = (TreeNode) eastPanel.nodeTree.getModel().getRoot();
						out.write("Node " + root.getChildCount());
						out.write(JTreeMethods.visitSimpleTree(eastPanel.nodeTree));

						root =  (TreeNode) eastPanel.linkTree.getModel().getRoot();
						out.write("\nLink " + root.getChildCount());
						out.write(JTreeMethods.visitSimpleTree(eastPanel.linkTree));

						root =  (TreeNode) eastPanel.initPerTree.getModel().getRoot();
						out.write("\nPersistent " + root.getChildCount());
						out.write(JTreeMethods.visitSimpleTree(eastPanel.initPerTree));

						root =  (TreeNode) eastPanel.initTranTree.getModel().getRoot();
						out.write("\nTransport " + root.getChildCount());
						out.write(JTreeMethods.visitSimpleTree(eastPanel.initTranTree));

						root =  (TreeNode) eastPanel.initInTree.getModel().getRoot();
						out.write("\nInput " + root.getChildCount());
						out.write(JTreeMethods.visitSimpleTree(eastPanel.initInTree));

						out.flush();
						out.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		loadConf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter ft = new FileNameExtensionFilter("Configuration File", "conf");
				chooser.addChoosableFileFilter(ft);
				chooser.showOpenDialog(null);
				confFile = chooser.getSelectedFile();
				if(confFile!= null){
					try {
						populateEastFields(confFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		delConf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteLeafs(eastPanel.nodeTree, eastPanel.nodeRoot);
				eastPanel.src.removeAllItems();
				eastPanel.dest.removeAllItems();
				eastPanel.nodePer.removeAllItems();
				deleteLeafs(eastPanel.linkTree, eastPanel.linkRoot);
				eastPanel.linkTran.removeAllItems();
				deleteLeafs(eastPanel.initPerTree, eastPanel.initPerRoot);
				deleteLeafs(eastPanel.initTranTree, eastPanel.initTranRoot);
				deleteLeafs(eastPanel.initInTree, eastPanel.initInRoot);
				repaint();
			}
		});
	}

	private void populateEastFields(File file) throws Exception{
		String line = "";

		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file))); 
		while((line = buffer.readLine()) != null) {
			StringTokenizer token = new StringTokenizer(line," ");
			String label = token.nextToken();
			if(label.equals("Node")) {
				int numberOfNodes = Integer.parseInt(token.nextToken());
				for(int i = 0; i< numberOfNodes ; i++) {
					line = buffer.readLine();
					JTreeMethods.addNChildrenNode(eastPanel.nodeTree, line, eastPanel);
				}
			}else if(label.equals("Link")) {
				int numberOfLinks = Integer.parseInt(token.nextToken());
				for(int i = 0; i< numberOfLinks ; i++) {
					line = buffer.readLine();
					JTreeMethods.addNChildrenLink(eastPanel.linkTree, line, eastPanel);
				}
			}else if(label.equals("Persistent")) {
				int numberOfPer = Integer.parseInt(token.nextToken());
				for(int i = 0; i< numberOfPer ; i++) {
					line = buffer.readLine();
					JTreeMethods.addNChildren(eastPanel.initPerTree, line, "Initial Persistent Tuples");
				}
			}else if(label.equals("Transport")) {
				int numberOfTran = Integer.parseInt(token.nextToken());
				for(int i = 0; i< numberOfTran ; i++) {
					line = buffer.readLine();
					JTreeMethods.addNChildren(eastPanel.initTranTree, line, "Initial Transport Tuples");
				}
			}
			else if(label.equals("Input")) {
				int numberOfIn = Integer.parseInt(token.nextToken());
				for(int i = 0; i< numberOfIn ; i++) {
					line = buffer.readLine();
					JTreeMethods.addNChildren(eastPanel.initInTree, line, "Initial Input Tuples");
				}
			}
		}
		buffer.close();
		eastPanel.nodeTree.expandPath( eastPanel.nodeTree.getNextMatch("Nodes", 0, Position.Bias.Forward));
		eastPanel.linkTree.expandPath(eastPanel.linkTree.getNextMatch("Links", 0, Position.Bias.Forward));
		eastPanel.initPerTree.expandPath(eastPanel.initPerTree.getNextMatch("Initial Persistent Tuples", 0, Position.Bias.Forward));
		eastPanel.initTranTree.expandPath(eastPanel.initTranTree.getNextMatch("Initial Transport Tuples", 0, Position.Bias.Forward));
		eastPanel.initInTree.expandPath(eastPanel.initInTree.getNextMatch("Initial Input Tuples", 0, Position.Bias.Forward));
	}

	private void addListenerOnParser() {
		parser.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {

				InputStream in;
				OutputStreamWriter out;
				try {
					out = new OutputStreamWriter(new FileOutputStream(new File("dn_files/output.lp")));
					in = new FileInputStream(mainFile);
					myParser.ReInit(in);
					Rule rule;
					while((rule = myParser.rule())!=null){
						boolean flag = false;
						if(manetFlag && rule instanceof Transport)
							flag = true;
						StringTokenizer tokens = new StringTokenizer(rule.evaluate(),".");
						while(tokens.hasMoreTokens()) {
							if(flag){
								tokens.nextToken();
								tokens.nextToken();
								flag=false;
							}
							out.write(tokens.nextToken()+".\n\n");
							out.flush();
						}
					}
					console.setText("Parsing finished successfully");
					out.close();
				} catch (Exception ex) {
					console.setText(ex.getMessage());
				}
			}
		});
	}

	private void addListenerOnCommModel() {
		asynch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				com_model = "dn_files/asynchronous.lp";
				console.setText("Asynchronous");
				synch.setSelected(false);
			}
		});

		synch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				com_model = "dn_files/synchronous.lp";
				console.setText("Synchronous");
				asynch.setSelected(false);
			}
		});
	}

	
	private void deleteLeafs(JTree tree, DefaultMutableTreeNode root){
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		List<TreePath> paths = new ArrayList<TreePath>(); 
		for(Enumeration enu = root.children(); enu.hasMoreElements();) {
			paths.add(tree.getNextMatch(enu.nextElement().toString(), 0, Position.Bias.Forward));
			
		}
		for(TreePath path : paths){
			MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
			model.removeNodeFromParent(node);
		}
		repaint();
	}
	
	@SuppressWarnings("static-access")
	private void populateFields(File file) {
		text = new JTextArea(30,30);
		text.setEditable(true);
		String line,output="";
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file))); 
			while((line = buffer.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line," ");
				String label;
				if(token.hasMoreElements()){
					label = token.nextToken();
					if(label.equals("transport")) {
						myParser.ReInit(new ByteArrayInputStream(line.getBytes()));
						try {
							Transport transport = (Transport) myParser.rule();

							if(transport.head.name.evaluate().equals("rrep") || 
									transport.head.name.evaluate().equals("rreq"))
								manetFlag = true;

							westPanel.namePredicate.setText(transport.head.name.evaluate());
							westPanel.arrity.setText(transport.head.terms.size()+"");
							westPanel.addTran.doClick();
						} catch (ParseException e) {
							console.setText("Problem at transport predicates");
						}
					} else if (label.equals("persistent")) {
						myParser.ReInit(new ByteArrayInputStream(line.getBytes()));
						try {
							Persistent persistent = (Persistent) myParser.rule();
							westPanel.namePredicate.setText(persistent.head.name.evaluate());
							westPanel.arrity.setText(persistent.head.terms.size()+"");
							westPanel.addPer.doClick();
						} catch (ParseException e) {
							console.setText("Problem at persistent predicates");
						}
					}else if(label.equals("input")){
						myParser.ReInit(new ByteArrayInputStream(line.getBytes()));
						try {
							Input input = (Input) myParser.rule();
							westPanel.namePredicate.setText(input.head.name.evaluate());
							westPanel.arrity.setText(input.head.terms.size()+"");
							westPanel.addIn.doClick();
						} catch (ParseException e) {
							console.setText("Problem at input predicates");
						}
					}else{
						output += line + "\n";
					}

				}else{
					output += line + "\n";
				}

			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		text.setText(output);
		JPanel auxpane = new JPanel();
		JLabel header = new JLabel("High-level language");
		auxpane.setLayout(new BorderLayout());		
		JScrollPane scroll = new JScrollPane(text);
		auxpane.add("North",header);
		auxpane.add("Center", scroll);
		editpane.add(auxpane);

		repaint();
	}

	
	public DnToolKit() {
		super("Declarative Networking ToolKit");
		setSize(1500, 900);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(100, 100);

		setLayout(new BorderLayout());

		addListenersOnFile();
		westPanel.addListenersOnTrees();
		addListenerOnParser();
		addListenersOnView();
		addListenerOnCommModel();
		addListenersOnConf();
		eastPanel.addListenersOnTrees();
		query.addListeners();

		file.add(newFile);
		file.add(loadFile);
		file.add(close);
		file.add(save);
		file.add(save_as);
		file.add(quit);
		menubar.add(file);

		view.add(showLow);
		view.add(removeLow);
		menubar.add(view);

		communiation.add(synch);
		communiation.add(asynch);
		menubar.add(communiation);

		configuaration.add(add_Conf);
		configuaration.add(loadConf);
		configuaration.add(saveAsConf);
		configuaration.add(saveConf);
		configuaration.add(delConf);
		menubar.add(configuaration);

		menubar.add(query);

		parse.add(parser);
		//menubar.add(parse);

		analisys.add(analize);

		loadTrace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter ft = new FileNameExtensionFilter("Text File", "txt");
				chooser.addChoosableFileFilter(ft);
				chooser.showOpenDialog(null);
				File file = null;
				if((file = chooser.getSelectedFile()) != null)
					analize.loadTrace(file);
			}
		});

		analisys.add(loadTrace);

		menubar.add(analisys);

		editpane.setLayout(new BoxLayout(editpane, BoxLayout.X_AXIS));
		editorTabs.add("Editor",editpane);
		
		centralPanel.add("Center",editorTabs);

		add("North",menubar);
		add("Center",centralPanel);
		add("West", westPanel);
		add("East",eastPanel);

		console.setEditable(false);
		JScrollPane consoleScroll = new JScrollPane(console);
		downTap.add("console",consoleScroll);
		centralPanel.add("South",downTap);
		
		setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DnToolKit();
	}
}

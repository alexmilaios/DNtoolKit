package dntoolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Querry extends JMenu {

	protected DnToolKit kit;

	public Querry same;
	
	private File importedQuerry;
	
	public String definedQuerry = "";

	private JMenu convergent = new JMenu("Convergent");

	private JMenu forLoop = new JMenu("Forwarding Loop");

	private JRadioButtonMenuItem corAlways = new JRadioButtonMenuItem("Always");

	private JRadioButtonMenuItem corSomeTimes = new JRadioButtonMenuItem("Sometimes");

	private JRadioButtonMenuItem corNever = new JRadioButtonMenuItem("Never");

	private JRadioButtonMenuItem forAlways = new JRadioButtonMenuItem("Always");

	private JRadioButtonMenuItem forSomeTimes = new JRadioButtonMenuItem("Sometimes");

	private JRadioButtonMenuItem forNever = new JRadioButtonMenuItem("Never");

	private JMenu userDef = new JMenu("User Defined Predicate");

	private JRadioButtonMenuItem userAlways = new JRadioButtonMenuItem("Always");

	private JRadioButtonMenuItem userSomeTimes = new JRadioButtonMenuItem("Sometimes");

	private JRadioButtonMenuItem userNever = new JRadioButtonMenuItem("Never");

	private JMenuItem loadQuerry = new JMenuItem("Load Querry");

	public void addListeners() {
		corAlways.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kit.qurryClicked = "Always";
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("dn_files/covergent.lp")))); 
					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					out.write(":- convergent.");
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.flush();
					corSomeTimes.setSelected(false);
					corNever.setSelected(false);
					forAlways.setSelected(false);
					forSomeTimes.setSelected(false);
					forNever.setSelected(false);
					userAlways.setSelected(false);
					userSomeTimes.setSelected(false);
					userNever.setSelected(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		corSomeTimes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kit.qurryClicked = "SomeTimes";
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("dn_files/covergent.lp")))); 
					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					out.write(":- not convergent.");
					out.flush();
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.close();
					buffer.close();
					corAlways.setSelected(false);
					corNever.setSelected(false);
					forAlways.setSelected(false);
					forSomeTimes.setSelected(false);
					forNever.setSelected(false);
					userAlways.setSelected(false);
					userSomeTimes.setSelected(false);
					userNever.setSelected(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		corNever.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				kit.qurryClicked = "Never";
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("dn_files/covergent.lp")))); 
					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					out.write(":- not convergent.");
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.flush();
					corAlways.setSelected(false);
					corSomeTimes.setSelected(false);
					forAlways.setSelected(false);
					forSomeTimes.setSelected(false);
					forNever.setSelected(false);
					userAlways.setSelected(false);
					userSomeTimes.setSelected(false);
					userNever.setSelected(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		forAlways.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kit.qurryClicked = "Always";
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("dn_files/for_loop.lp")))); 
					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					out.write(":- has_loop.");
					out.flush();
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.close();
					buffer.close();
					forSomeTimes.setSelected(false);
					forNever.setSelected(false);
					corAlways.setSelected(false);
					corSomeTimes.setSelected(false);
					corNever.setSelected(false);
					userAlways.setSelected(false);
					userSomeTimes.setSelected(false);
					userNever.setSelected(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		forSomeTimes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				kit.qurryClicked = "SomeTimes";
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("dn_files/for_loop.lp")))); 
					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					out.write(":- not has_loop.");
					out.flush();
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.close();
					buffer.close();
					forAlways.setSelected(false);
					corNever.setSelected(false);
					corAlways.setSelected(false);
					corSomeTimes.setSelected(false);
					corNever.setSelected(false);
					userAlways.setSelected(false);
					userSomeTimes.setSelected(false);
					userNever.setSelected(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		forNever.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				kit.qurryClicked = "Never";
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(new File("dn_files/for_loop.lp")))); 
					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					out.write(":- not has_loop.");
					out.flush();
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.flush();
					buffer.close();
					forSomeTimes.setSelected(false);
					forAlways.setSelected(false);
					corAlways.setSelected(false);
					corSomeTimes.setSelected(false);
					corNever.setSelected(false);
					userAlways.setSelected(false);
					userSomeTimes.setSelected(false);
					userNever.setSelected(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		userAlways.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.qurryClicked = "Always";
				new QuerryFrame(same,"always",kit,importedQuerry);
				userNever.setSelected(false);
				userSomeTimes.setSelected(false);
				corAlways.setSelected(false);
				corSomeTimes.setSelected(false);
				corNever.setSelected(false);
				forAlways.setSelected(false);
				forSomeTimes.setSelected(false);
				forNever.setSelected(false);
			}
		});

		userSomeTimes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.qurryClicked = "SomeTimes";
				new QuerryFrame(same,"sometimes",kit,importedQuerry);
				userAlways.setSelected(false);
				userNever.setSelected(false);
				corAlways.setSelected(false);
				corSomeTimes.setSelected(false);
				corNever.setSelected(false);
				forAlways.setSelected(false);
				forSomeTimes.setSelected(false);
				forNever.setSelected(false);
			}
		});

		userNever.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.qurryClicked = "Never";
				new QuerryFrame(same,"never",kit,importedQuerry);
				userAlways.setSelected(false);
				userSomeTimes.setSelected(false);
				corAlways.setSelected(false);
				corSomeTimes.setSelected(false);
				corNever.setSelected(false);
				forAlways.setSelected(false);
				forSomeTimes.setSelected(false);
				forNever.setSelected(false);
			}
		});

		loadQuerry.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter ft = new FileNameExtensionFilter("Text File", "txt");
				chooser.addChoosableFileFilter(ft);
				chooser.showOpenDialog(null);
				importedQuerry = chooser.getSelectedFile();
				
				JTextArea text = new JTextArea(30,30);
				text.setEditable(false);
				String line ="",output="";
				
				try {
					BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(importedQuerry)));
					
					while((line = buffer.readLine()) != null) {
						output += line + "\n";
					}
					text.setText(output);
					kit.editorTabs.add("Querry",text);
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public Querry (String name,DnToolKit kit,EastPanel east){
		super(name);
		this.kit = kit;
		same = this;
		convergent.add(corAlways);
		convergent.add(corSomeTimes);
		convergent.add(corNever);

		add(convergent);

		forLoop.add(forAlways);
		forLoop.add(forSomeTimes);
		forLoop.add(forNever);

		add(forLoop);

		userDef.add(userAlways);
		userDef.add(userSomeTimes);
		userDef.add(userNever);

		add(userDef);

		add(loadQuerry);

	}
}

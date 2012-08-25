package dntoolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class Querry extends JMenu {

	protected DnToolKit kit;

	private EastPanel east; 

	public Querry same;

	public String definedQuerry = "";

	private JMenu convergent = new JMenu("Convergent");

	private JMenu forLoop = new JMenu("Forwarding Loop");

	private JMenuItem corAlways = new JMenuItem("Always");

	private JMenuItem corSomeTimes = new JMenuItem("Sometimes");

	private JMenuItem corNever = new JMenuItem("Never");

	private JMenuItem forAlways = new JMenuItem("Always");

	private JMenuItem forSomeTimes = new JMenuItem("Sometimes");

	private JMenuItem forNever = new JMenuItem("Never");

	private JMenu userDef = new JMenu("User Defined Predicate");

	private JMenuItem userAlways = new JMenuItem("Always");

	private JMenuItem userSomeTimes = new JMenuItem("Sometimes");

	private JMenuItem userNever = new JMenuItem("Never");

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
					buffer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		userAlways.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.qurryClicked = "Always";
				new QuerryFrame(same,"always",kit);
			}
		});

		userSomeTimes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.qurryClicked = "SomeTimes";
				new QuerryFrame(same,"sometimes",kit);
			}
		});
		
		userNever.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kit.qurryClicked = "Never";
				new QuerryFrame(same,"never",kit);
			}
		});
	}

	public Querry (String name,DnToolKit kit,EastPanel east){
		super(name);
		this.kit = kit;
		this.east = east;
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

	}
}

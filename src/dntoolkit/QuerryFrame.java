package dntoolkit;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class QuerryFrame extends JFrame {

	Querry parent;
	DnToolKit kit;
	JPanel panel = new JPanel();
	String type;
	JPanel textPane = new JPanel();
	JLabel label = new JLabel("Define a predicate: ");
	JTextField text = new JTextField(15);
	JButton add = new JButton("Define");
	File queery;

	void addListener(){
		add.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String tmp = text.getText();
				// String result = "input " + tmp;
				parent.kit.myParser.ReInit(new ByteArrayInputStream(tmp.getBytes()));
				try {
					parent.kit.myParser.rule();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(new FileInputStream(queery))); 

					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
					String line = "";
					while((line = buffer.readLine()) != null) {
						out.write(line + "\n");
						out.flush();
					}
					if(type.equals("always"))
						out.write(":- " + tmp);
					else
						out.write(":- not " + tmp);
					out.flush();
					out.write("\n#hide.");
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.perTree,"persistent"));
					out.write(JTreeMethods.visitSimpleShowTree(kit.westPanel.tranTree,"transport"));
					out.close();
				} catch (Exception ex) {
					parent.kit.console.setText(ex.getMessage());
				}
				if(!tmp.equals(""))
					dispose();
			}
		});
	}

	public QuerryFrame( Querry parent,String type, DnToolKit kit, File querry) {
		super("Define Predicate for a Querry");
		setSize(400,100);
		this.type = type;
		this.kit = kit;
		this.queery = querry;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.parent = parent;
		panel.setLayout(new FlowLayout());
		textPane.add(label);
		textPane.add(text);
		panel.add(textPane);
		panel.add(add);
		addListener();
		add(panel);
		setVisible(true);
	}

}

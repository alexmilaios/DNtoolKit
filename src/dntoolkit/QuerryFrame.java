package dntoolkit;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import parser.ParseException;
import parser.Parser;
import rules.Input;

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

	void addListener(){
		add.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String tmp = text.getText();
				String result = "input " + tmp;
				parent.kit.myParser.ReInit(new ByteArrayInputStream(result.getBytes()));
				try {
					parent.kit.myParser.rule();

					OutputStreamWriter out = new OutputStreamWriter(
							new FileOutputStream(new File("dn_files/querry.lp")));
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

	public QuerryFrame( Querry parent,String type, DnToolKit kit) {
		super("Define Predicate for a Querry");
		setSize(400,100);
		this.type = type;
		this.kit = kit;
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

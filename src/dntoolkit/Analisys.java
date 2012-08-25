package dntoolkit;

import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Analisys extends JMenuItem {

	private DnToolKit kit;;

	public JPanel answerPane = new JPanel();
	public JTextArea showAns = new JTextArea(5,30);
	public JPanel general = new JPanel();

	File trace = new File("dn_files/trace.txt");
	List<Answer> answers;

	private void runProcess() {
		OutputStreamWriter out;
		try{
			out = new OutputStreamWriter(new FileOutputStream(trace));
			Process proc = Runtime.getRuntime().exec("./clingo 0 " + kit.com_model +  " dn_files/output.lp "
					+ " dn_files/configuration.lp  dn_files/querry.lp");
			InputStream in = proc.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			InputStream err = proc.getErrorStream();
			BufferedReader error = new BufferedReader(new InputStreamReader(err));

			out.write(reader.readLine());
			String line = "";
			while((line = reader.readLine()) != null || (line = error.readLine()) != null){
				out.write("\n" + line);
				kit.console.setText(line + "\n");
				out.flush();
			}
			in.close();
			out.close();
			kit.console.setText("");
			proc.destroy();
		}catch(Exception e) {
			kit.console.setText(e.getMessage());
			e.printStackTrace();
		}
	}

	private void addAnswers() throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(trace)));
		String line;
		int i = 1;

		while((line = buffer.readLine())!= null) {
			if(!line.equals("")){
				StringTokenizer tokens = new StringTokenizer(line," ");
				if(tokens.nextToken().equals("Answer:")) {
					line = buffer.readLine();
					StringTokenizer tokens2 = new StringTokenizer(line," ");
					String trace = "";
					while(tokens2.hasMoreTokens()){
						trace += tokens2.nextToken() + "\n";
					}
					answers.add(new Answer(trace,kit,i));
					answerPane.add(answers.get(i-1));
					i++;
				}
			}
		}
	}

	public void addListener (){
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runProcess();
				answers = new ArrayList<Answer>();
				answerPane.removeAll();
				showAns.setText("");
				try {
					addAnswers();
					if(setOutput()){
						repaint();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean setOutput() {
		if(kit.qurryClicked.equals("Always") || kit.qurryClicked.equals("Never") ) {
			if(answers.size() > 0) {
				kit.console.setText("The query doesn't hold");
				return true;
			}else if (answers.size() == 0){
				kit.console.setText("The query holds");
				return false;
			}
		}

		if(kit.qurryClicked.equals("SomeTimes")) {
			if(answers.size() > 0) {
				kit.console.setText("The query holds");
				return true;
			}else if (answers.size() == 0){
				kit.console.setText("The query doesn't hold");
				return false;
			}
		}
		return false;
	}

	public Analisys(DnToolKit kit){
		super("Analize");
		this.kit = kit;
		this.addListener();
		answerPane.setLayout(new BoxLayout(answerPane,BoxLayout.Y_AXIS));
		general.setLayout(new BoxLayout(general, BoxLayout.X_AXIS));
		general.add(new JScrollPane(answerPane));
		showAns.setEditable(false);
		general.add(new JScrollPane(showAns));
		kit.downTap.add("Visualization",general);
	}
}

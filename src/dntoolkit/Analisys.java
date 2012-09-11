package dntoolkit;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	public JPanel auxpane = new JPanel();
	public JComboBox answersMenu = new JComboBox();
	public JButton show = new JButton("show");
	public JButton viz = new JButton("visualise");


	File trace = new File("dn_files/trace.txt");
	List<Answer> answers;

	private void runProcess() {
		OutputStreamWriter out;
		try{
			out = new OutputStreamWriter(new FileOutputStream(trace));
			Process proc = Runtime.getRuntime().exec("./clingo "+kit.eastPanel.numAnswers +" "+ kit.com_model +  " dn_files/output.lp "+
					((kit.manetFlag) ? " dn_files/manet_injection.lp ":"")+ " dn_files/configuration.lp  dn_files/querry.lp");
			InputStream in = proc.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			InputStream err = proc.getErrorStream();
			BufferedReader error = new BufferedReader(new InputStreamReader(err));

			out.write(reader.readLine());
			String line = "";
			while((line = reader.readLine()) != null || (line = error.readLine()) != null){
				out.write("\n" + line);
				//kit.console.setText(line + "\n");
				out.flush();
			}
			in.close();
			out.close();
			kit.console.setText(error.readLine());
			proc.destroy();
		}catch(Exception e) {
			kit.console.setText(e.getMessage());
			e.printStackTrace();
		}
	}

	private void addAnswers(File file) throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		int i = 1;

		while((line = buffer.readLine())!= null) {
			if(!line.equals("")){
				StringTokenizer tokens = new StringTokenizer(line," ");
				if(tokens.nextToken().equals("Answer:") && (i <32000)) {
					line = buffer.readLine();
					StringTokenizer tokens2 = new StringTokenizer(line," ");
					String trace = "";
					while(tokens2.hasMoreTokens()){
						trace += tokens2.nextToken() + "\n";
					}
					answers.add(new Answer(trace,kit,i));
					answersMenu.addItem("Answer: " +i);
					i++;

				}
			}
		}
	}

	public void addListener (){
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {


				kit.parser.doClick();
				if(! (kit.console.getText().equals("Parsing finished successfully")))
					return;

				final Thread runprocess = new Thread(new Runnable() {

					@Override
					public void run() {
						runProcess();
						answers = new ArrayList<Answer>();
						answersMenu.removeAllItems();
						showAns.setText("");
						try {
							addAnswers(trace);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				runprocess.start();

				new Thread(new Runnable() {
					@Override
					public void run() {	
						while(runprocess.isAlive()){
							kit.console.setText("clingo is running");
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(setOutput()){
							kit.console.setText(kit.console.getText() + "\nThere number of answers: " + answers.size());
							reset();
						}
						if(answers.size() == 0){
							general.removeAll();
							kit.downTap.add("Visualization",general);
						}
					}
				}).start();
			}
		});

		show.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String [] options = ((String) answersMenu.getSelectedItem()).split(" ");
				Answer ans = answers.get(Integer.parseInt(options[1])-1);
				ans.show();
			}
		});

		viz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String [] options = ((String) answersMenu.getSelectedItem()).split(" ");
				Answer ans = answers.get(Integer.parseInt(options[1])-1);
				ans.visualize();
			}
		});
	}

	public void loadTrace(File inputFile) {
		answers = new ArrayList<Answer>();
		answersMenu.removeAllItems();
		showAns.setText("");
		try {
			addAnswers(inputFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(setOutput()){
			kit.console.setText(kit.console.getText() + "\nThere number of answers: " + answers.size());
			reset();
		}

		if(answers.size() == 0){
			general.removeAll();
			kit.downTap.add("Visualization",general);
		}
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

	private void reset() {
		general.removeAll();
		answerPane.setLayout(new BoxLayout(answerPane,BoxLayout.X_AXIS));
		auxpane.setLayout(new FlowLayout());
		auxpane.add(answersMenu);
		auxpane.add(show);
		auxpane.add(viz);
		answerPane.add(auxpane);
		answerPane.add(show);
		answerPane.add(viz);
		general.setLayout(new BoxLayout(general, BoxLayout.X_AXIS));
		if(answersMenu.getItemCount() !=0)
			general.add(new JScrollPane(answerPane));
		showAns.setEditable(false);
		general.add(new JScrollPane(showAns));
		kit.downTap.add("Visualization",general);
	}

	public Analisys(DnToolKit kit){
		super("GO");
		this.kit = kit;
		this.addListener();
		answerPane.setLayout(new BoxLayout(answerPane,BoxLayout.X_AXIS));
		auxpane.setLayout(new FlowLayout());
		auxpane.add(answersMenu);
		auxpane.add(show);
		auxpane.add(viz);
		answerPane.add(auxpane);
		answerPane.add(show);
		answerPane.add(viz);
		general.setLayout(new BoxLayout(general, BoxLayout.X_AXIS));
		if(answersMenu.getItemCount() !=0)
			general.add(new JScrollPane(answerPane));
		showAns.setEditable(false);
		general.add(new JScrollPane(showAns));
		kit.downTap.add("Visualization",general);
	}
}

package shinjframenotepad;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Notepad extends JFrame implements ActionListener{
	String copyText;
	JMenuItem mntmExit, mntmSave, mntmLoad;
	JMenuItem mntmCopy, mntmPaste;
	JTextArea txtLog;
	
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notepad frame = new Notepad();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Notepad() {
		setTitle("NotePad");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//File 메뉴
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		mntmSave = new JMenuItem("Save");
		mnNewMenu.add(mntmSave);
		mntmSave.addActionListener(this);
		
		mntmLoad = new JMenuItem("Load");
		mnNewMenu.add(mntmLoad);
		mntmLoad.addActionListener(this);
		
		mntmExit = new JMenuItem("Exit");		
		mnNewMenu.add(mntmExit);
		mntmExit.addActionListener(this);
		
		//Edit 메뉴
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmCopy = new JMenuItem("Copy");
		mnEdit.add(mntmCopy);
		mntmCopy.addActionListener(this);
		
		mntmPaste = new JMenuItem("Paste");
		mnEdit.add(mntmPaste);
		mntmPaste.addActionListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		txtLog = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(txtLog);
		contentPane.add(scrollPane);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//File 메뉴
		
		//종료
		if(e.getSource() == mntmExit) { System.exit(0); }
		
		//저장
		else if(e.getSource() == mntmSave) {
			
			String dfName = FileUtil.showSaveFileChooser(null).getPath();
			
			StringBuffer data = new StringBuffer();
			
			try {
				String enter = System.getProperty("line.separator");
				InputStream is = new ByteArrayInputStream(txtLog.getText().getBytes());
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				String line = null;
				
				while((line = in.readLine()) != null) {
					data.append(line);
					data.append(enter);
				}
				in.close();

			} catch (Exception e1) {
				System.out.println(e1);
			}
					
			FileUtil.save(data, dfName);
		}
			
		//로드
		else if(e.getSource() == mntmLoad) {
			
			String dfName = FileUtil.showOpenFileChooser(null).getPath();
			txtLog.setText(FileUtil.read(dfName).toString());
		} 
		
		//복사
		else if(e.getSource() == mntmCopy) {
			copyText = txtLog.getSelectedText();
		}
		
		//븥혀넣기
		else if(e.getSource() == mntmPaste) {
			   int position = txtLog.getCaretPosition();
			   txtLog.insert(copyText, position); }
	}
}

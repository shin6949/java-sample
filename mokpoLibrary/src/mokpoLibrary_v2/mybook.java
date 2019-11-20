package mokpoLibrary_v2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class mybook extends JFrame implements Runnable {
	private JPanel contentPane;
	private JTextField input_searchBooks, input_id;
	private JPasswordField input_pwd;
	private DB_info db = new DB_info();
	private login_manager login = new login_manager();
	private JButton btn_borrow, btn_return, btn_admin;
	JPanel login_btn_panel, logined_panel, login_panel;
	Initialize_table area;
	JLabel logined_status;
	
	public static void main(String[] args) {
		mybook frame = new mybook();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mybook frame = new mybook();
					Thread load_db = new Thread(frame,"DB 로딩"); 
					load_db.start(); 
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public mybook() {
		setTitle("\uB3C4\uC11C \uAD00\uB9AC \uC2DC\uC2A4\uD15C");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 637, 336);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		area = new Initialize_table(contentPane);
		
		JPanel tool_box_panel = new JPanel();
		tool_box_panel.setBounds(512, 52, 97, 194);
		contentPane.add(tool_box_panel);
		tool_box_panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		btn_borrow = new JButton("\uB300\uCD9C");
		btn_borrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Book book = db.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
				if(book.borrow_book(login_manager.logined_user, area.load_progressbar)) {
					refresh(area.model, area.load_progressbar);
					check_borrow_status(btn_borrow, btn_return);
				}
			}
		});
		tool_box_panel.add(btn_borrow);
		
		btn_return = new JButton("\uCC45 \uBC18\uB0A9");
		btn_return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(login_manager.logined_user.return_book(area.load_progressbar)) {
				refresh(area.model, area.load_progressbar);
				check_borrow_status(btn_borrow, btn_return);
				}
			}
		});
		
		tool_box_panel.add(btn_return);
		
		JButton btn_detail = new JButton("\uC0C1\uC138 \uC815\uBCF4");
		tool_box_panel.add(btn_detail);
		btn_detail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Book book = db.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
				new Detail(book);
			}
		});
		
		JButton btn_refresh = new JButton("\uCD08\uAE30\uD654\uBA74");
		tool_box_panel.add(btn_refresh);
		btn_refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh(area.model, area.load_progressbar);
				area.input_searchBooks.setText("");
			}
		});
		
		JButton btn_admin = new JButton("\uAD00\uB9AC \uBAA8\uB4DC");
		btn_admin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new adminMode();
			}
		});
		tool_box_panel.add(btn_admin);
		btn_admin.setVisible(false);
		
		Panel login_session_panel = new Panel();
		login_session_panel.setBounds(12, 252, 599, 35);
		contentPane.add(login_session_panel);
		login_session_panel.setLayout(null);
		
		login_panel = new JPanel();
		login_panel.setBounds(0, 0, 359, 35);
		login_session_panel.add(login_panel);
		login_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblId = new JLabel("ID");
		lblId.setHorizontalAlignment(SwingConstants.LEFT);
		login_panel.add(lblId);
		
		input_id = new JTextField();
		login_panel.add(input_id);
		input_id.setPreferredSize(new Dimension(130, 30));
		input_id.setColumns(10);
		
		JLabel label = new JLabel("\uBE44\uBC00\uBC88\uD638");
		login_panel.add(label);
		
		input_pwd = new JPasswordField();
		input_pwd.setPreferredSize(new Dimension(130, 30));
		login_panel.add(input_pwd);
		
		//Logined Panel
		logined_panel = new JPanel();
		logined_panel.setBounds(0, 0, 599, 35);
		login_session_panel.add(logined_panel);
		logined_panel.setVisible(false);
		logined_panel.setLayout(new BorderLayout(0, 0));
		
		logined_status = new JLabel();
		logined_panel.add(logined_status, BorderLayout.WEST);
		
		JButton btn_logout = new JButton("\uB85C\uADF8\uC544\uC6C3");
		btn_logout.setPreferredSize(new Dimension(98, 20));
		btn_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login.request_logout(area.load_progressbar);
				if(login_manager.logined == false)  {
					login_panel.setVisible(true);
					login_btn_panel.setVisible(true);
					logined_panel.setVisible(false);
					logined_status.setText("");
					btn_admin.setVisible(false);
					login_manager.logined_user = new User();
					check_borrow_status(btn_borrow, btn_return);
				}
			}
		});
		logined_panel.add(btn_logout, BorderLayout.EAST);
		
		login_btn_panel = new JPanel();
		login_btn_panel.setBounds(164, 0, 435, 35);
		login_session_panel.add(login_btn_panel);
		login_btn_panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		login_btn_panel.setVisible(true);
		
		JButton btn_login = new JButton("\uB85C\uADF8\uC778");
		login_btn_panel.add(btn_login);
		btn_login.setPreferredSize(new Dimension(100, 30));
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//login
				login.request_login(input_id.getText(), input_pwd.getText(), area.load_progressbar);
				if(login_manager.logined == true ) {
					login_panel.setVisible(false);
					login_btn_panel.setVisible(false);
					logined_panel.setVisible(true);
					input_id.setText("");
					input_pwd.setText("");
					logined_status.setText(login_manager.logined_user.name + "님 환영합니다.");
					check_borrow_status(btn_borrow, btn_return);
					
					if(login_manager.logined_user.isadmin == true) {
						btn_admin.setVisible(true);
					}
				}
			}
		});
		
		JButton btn_register = new JButton("\uD68C\uC6D0\uAC00\uC785");
		login_btn_panel.add(btn_register);
		btn_register.setPreferredSize(new Dimension(100, 30));
		btn_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Signup();
			}
		});
		
		check_borrow_status(btn_borrow, btn_return);
	}
	
	@Override
	public void run() {	db.public_ResultSet_SQL_Query(area.model, "select * from Books;", area.load_progressbar); } 
	
	private void check_borrow_status(JButton btn_borrow, JButton btn_return) {
		if(login_manager.logined == false) {
			btn_borrow.setEnabled(false);
			btn_return.setEnabled(false);
		}
		else if(login_manager.logined_user.status == true) {
			btn_borrow.setEnabled(true);
			btn_return.setEnabled(false);
		} else {
			btn_borrow.setEnabled(false);
			btn_return.setEnabled(true);
		}
	}
	
	private void refresh(DefaultTableModel model, JProgressBar progressBar) {
		model.setRowCount(0);
		db.public_ResultSet_SQL_Query(model, "select * from Books;", area.load_progressbar);
	}
}


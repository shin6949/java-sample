package mokpoLibrary_v2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

public class Signup extends JFrame {

	private JPanel signupPanel;
	private JTextField input_id;
	private JPasswordField input_pwd;
	private JTextField input_name;
	private boolean id_checked = false;
	private JDateChooser dateChooser;
	private JPasswordField input_pwd_re;

	public Signup() {
		setTitle("\uD68C\uC6D0\uAC00\uC785");
		setBounds(100, 100, 450, 300);
		signupPanel = new JPanel();
		signupPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(signupPanel);
		signupPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(28, 108, 77, 39);
		signupPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lblNewLabel_1.setBounds(28, 154, 77, 39);
		signupPanel.add(lblNewLabel_1);
		
		input_id = new JTextField();
		input_id.setBounds(134, 117, 173, 21);
		signupPanel.add(input_id);
		input_id.setColumns(10);
		
		input_pwd = new JPasswordField();
		input_pwd.setBounds(134, 166, 173, 21);
		signupPanel.add(input_pwd);
		
		JLabel label = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
		label.setBounds(28, 65, 57, 15);
		signupPanel.add(label);
		
		JLabel lblNewLabel_2 = new JLabel("\uC774\uB984");
		lblNewLabel_2.setBounds(28, 13, 57, 15);
		signupPanel.add(lblNewLabel_2);
		
		input_name = new JTextField();
		input_name.setBounds(134, 10, 173, 21);
		signupPanel.add(input_name);
		input_name.setColumns(10);
		
		JButton btn_register = new JButton("\uD68C\uC6D0\uAC00\uC785");
		btn_register.addActionListener(new ActionListener() {
				
			public void actionPerformed(ActionEvent arg0) {
				int name_len = input_name.getText().length();
				boolean name_checked, birth_checked, pwd_checked = false;
			
				if(name_len <= 1 || name_len > 11) { 
					JOptionPane.showMessageDialog(null,  "이름을 입력해주세요. 2 ~ 10글자로 구성되어야합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
					name_checked = false; } 
				else { name_checked = true; }
				
				if(dateChooser.getDate() == null) { 
					JOptionPane.showMessageDialog(null,  "생년월일을 입력하세요.", "ERROR", JOptionPane.ERROR_MESSAGE); 
					birth_checked = false;  } 
				else { birth_checked = true; }
				
				if(id_checked == false) { JOptionPane.showMessageDialog(null,  "ID 중복확인을 해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE); }
				
				if(input_pwd.getPassword().length < 4 || input_pwd.getPassword().length >= 20) { 
				JOptionPane.showMessageDialog(null, "비밀번호는 4 ~ 20글자로 구성되어야합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
				pwd_checked = false;  }
				else if (input_pwd.getText().equals(input_pwd_re.getText())) { pwd_checked = true; } 
				else { 
				JOptionPane.showMessageDialog(null, "비밀번호를 2칸 모두 정확하게 입력하세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
				pwd_checked = false;
				}
			
				if(name_checked == true && birth_checked == true && pwd_checked == true && id_checked == true) {
				
				SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd");
				String test2 = test.format(dateChooser.getDate());
				
				System.out.printf("%s %s %s\n", input_name.getText(), test2, input_id.getText());
				if(register(input_name.getText(), test2, input_id.getText(), input_pwd.getText())) {
					JOptionPane.showMessageDialog(null,  "회원 가입 완료", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
					dispose();
					} else { JOptionPane.showMessageDialog(null,  "회원 가입 실패", "ERROR", JOptionPane.ERROR_MESSAGE); } 
				}
			}
				
		});
		btn_register.setBounds(325, 228, 97, 23);
		signupPanel.add(btn_register);
		
		JButton btn_check_id = new JButton("\uC911\uBCF5\uD655\uC778");
		btn_check_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(input_id.getText());
				if(input_id.getText().length() >= 4 && input_id.getText().length() <= 10) {
					if(check_id(input_id.getText())) {
						int result = JOptionPane.showConfirmDialog(null,  "ID 사용이 가능합니다. 사용하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
						if(result == JOptionPane.CLOSED_OPTION) {}
						else if(result == JOptionPane.YES_OPTION) { input_id.setEnabled(false); id_checked = true; }
						else {} } 
						else {JOptionPane.showMessageDialog(null,  "중복된 ID가 있습니다.", "ERROR", JOptionPane.ERROR_MESSAGE); }
						
				} else {
					JOptionPane.showMessageDialog(null,  "ID 규칙에 맞지 않습니다. ID는 4 ~ 10 글자로 구성되어야 합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
	});
		
		btn_check_id.setBounds(311, 114, 90, 30);
		signupPanel.add(btn_check_id);
		
		JLabel lblNewLabel_3 = new JLabel("\uBE44\uBC00\uBC88\uD638\uD655\uC778");
		lblNewLabel_3.setBounds(28, 221, 94, 15);
		signupPanel.add(lblNewLabel_3);
		
		dateChooser = new JDateChooser();
		dateChooser.setBounds(134, 65, 173, 21);
		signupPanel.add(dateChooser);
		
		input_pwd_re = new JPasswordField();
		input_pwd_re.setBounds(134, 218, 173, 21);
		signupPanel.add(input_pwd_re);
		
		this.setVisible(true);
		
	}
	
	private boolean register(String name, String birth, String id, String pw) {
		try {
			Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
			Statement stmt = conn.createStatement();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users VALUES (?, true, ?, ?, ?, false, null, 0)");
			
			pstmt.setString(1, name);
			pstmt.setString(2,  id);
			pstmt.setString(3, pw);
			java.sql.Date date = java.sql.Date.valueOf(birth);
			pstmt.setDate(4, date);
			pstmt.executeUpdate();
			
			SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String test2 = test.format(new Date());
			java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);
			
			pstmt = conn.prepareStatement("INSERT INTO log VALUES (null, ?, ?, ?, null)");
			pstmt.setTimestamp(2, now_date);
			pstmt.setString(3, id);
			pstmt.setString(4, "Register_Success");
			pstmt.executeUpdate();

			stmt.close();
			conn.close();
			pstmt.close();
			
			return true;
		} 
		catch (Exception e1) { e1.printStackTrace(); return false; }
	}
	
	private boolean check_id(String id) {
		try {
			Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Users Where id LIKE '" + id + "'");
			int count = 0;
			
			if(rs.next()) { count++; }
			
			if(count == 0 ) { rs.close(); stmt.close(); conn.close(); return true; } 
			else {	rs.close(); stmt.close(); conn.close(); return false;	}
	   
		} 
		catch (Exception e1) { e1.printStackTrace(); return false;}
	}
}

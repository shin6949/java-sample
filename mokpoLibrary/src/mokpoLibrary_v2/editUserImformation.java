package mokpoLibrary_v2;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class editUserImformation extends JFrame {

	private DB_info db = new DB_info();
	private JTextField input_name, input_location;
	private JPasswordField input_PWD;
	private JLabel txt_status, txt_borrowedBookname;
	private JPanel contentPane;

	public editUserImformation(User user) {
		setTitle("\uC720\uC800 \uC815\uBCF4 \uD3B8\uC9D1");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel Text_Panel = new JPanel();
		Text_Panel.setBounds(10, 10, 58, 216);
		contentPane.add(Text_Panel);
		Text_Panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel label_ISBN = new JLabel("\uC774\uB984");
		Text_Panel.add(label_ISBN);
		
		JLabel lblId = new JLabel("ID");
		Text_Panel.add(lblId);
		
		JLabel label_author = new JLabel("\uBE44\uBC00\uBC88\uD638");
		Text_Panel.add(label_author);
		
		JLabel label_status = new JLabel("\uC0C1\uD0DC");
		Text_Panel.add(label_status);
		
		JLabel label_location = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
		Text_Panel.add(label_location);
		
		JLabel label_borrowedID = new JLabel("\uB300\uCD9C\uD55C \uCC45");

			
		JPanel imformation_panel = new JPanel();
		imformation_panel.setBounds(78, 10, 344, 216);
		contentPane.add(imformation_panel);
		imformation_panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		input_name = new JTextField();
		input_name.setText("\uC774\uB984");
		imformation_panel.add(input_name);
		input_name.setText(user.name);
			
		JLabel input_ID = new JLabel();
		GridBagConstraints gbc_input_ID = new GridBagConstraints();
		gbc_input_ID.fill = GridBagConstraints.BOTH;
		gbc_input_ID.insets = new Insets(0, 0, 0, 5);
		gbc_input_ID.gridx = 0;
		gbc_input_ID.gridy = 0;
		input_ID.setText(user.id);
		imformation_panel.add(input_ID, gbc_input_ID);
		
		input_PWD = new JPasswordField();
		input_PWD.setText("");
		GridBagConstraints gbc_input_PWD = new GridBagConstraints();
		gbc_input_PWD.fill = GridBagConstraints.BOTH;
		gbc_input_PWD.insets = new Insets(0, 0, 0, 5);
		gbc_input_PWD.gridx = 1;
		gbc_input_PWD.gridy = 0;
		imformation_panel.add(input_PWD, gbc_input_PWD);
		
		txt_status = new JLabel();
		txt_status.setText("\uC0C1\uD0DC");
		GridBagConstraints gbc_txt_status = new GridBagConstraints();
		gbc_txt_status.fill = GridBagConstraints.BOTH;
		gbc_txt_status.insets = new Insets(0, 0, 0, 5);
		gbc_txt_status.gridx = 2;
		gbc_txt_status.gridy = 0;
		txt_status.setText(user.convert_status(user.status));
		imformation_panel.add(txt_status, gbc_txt_status);
		
		JDateChooser input_birth = new JDateChooser();
		input_birth.setDate(user.birth);
		GridBagConstraints gbc_input_birth = new GridBagConstraints();
		gbc_input_birth.fill = GridBagConstraints.BOTH;
		gbc_input_birth.insets = new Insets(0, 0, 0, 5);
		gbc_input_birth.gridx = 3;
		gbc_input_birth.gridy = 0;
		
		imformation_panel.add(input_birth, gbc_input_birth);
						
		txt_borrowedBookname = new JLabel();
		txt_borrowedBookname.setText("\uB300\uCD9C\uD55C ID");
		GridBagConstraints gbc_txt_borrowedBookname = new GridBagConstraints();
		gbc_txt_borrowedBookname.fill = GridBagConstraints.BOTH;
		gbc_txt_borrowedBookname.gridx = 4;
		gbc_txt_borrowedBookname.gridy = 0;
		txt_borrowedBookname.setText(user.borrow_name);
		imformation_panel.add(txt_borrowedBookname, gbc_txt_borrowedBookname);
		
		this.setVisible(true);
		Text_Panel.add(label_borrowedID);
		
		JPanel panel = new JPanel();
		panel.setBounds(88, 226, 334, 35);
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btn_cancel = new JButton("\uCDE8\uC18C");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JButton btn_return = new JButton("\uBC18\uB0A9 \uCC98\uB9AC");
		btn_return.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(user.status == false ) { 
					user.return_book(null);
					refresh(user.id);
					dispose();
				}
				
			}
		});
		panel.add(btn_return);
		
		JButton btn_retire = new JButton("\uD0C8\uD1F4 \uCC98\uB9AC");
		btn_retire.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				User user = db.load_User(input_ID.getText(), null);
				int result = JOptionPane.showConfirmDialog(null,  "정말 삭제하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.CLOSED_OPTION) {}
				else if(result == JOptionPane.YES_OPTION) { 
					try {
					Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
					PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Users WHERE ID = '"+ user.id + "'");
					pstmt.executeUpdate();
					pstmt.close();
					
					if(user.status == false) {
						PreparedStatement update_user = conn.prepareStatement("UPDATE Books SET isin = true, borrow_id = NULL WHERE ISBN = " + user.borrow_ISBN + "");
						update_user.executeUpdate();
						update_user.close();
					}
					conn.close();
					} 
					catch (Exception e1) { e1.printStackTrace(); } 
					JOptionPane.showMessageDialog(null,  "유저 삭제가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
			});
		
		panel.add(btn_retire);

		panel.add(btn_cancel);
		
		JButton btn_confirm = new JButton("변경");
		btn_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(input_PWD.getPassword().length != 0) {
				if(input_PWD.getPassword().length > 4 || input_PWD.getPassword().length <= 20) { 
					try {
						SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd");
						String birth = test.format(input_birth.getDate());
						Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
						PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET name = '"+ input_name.getText() + "', pw = '" + input_PWD.getText() + "', birth = '" + java.sql.Date.valueOf(birth) + "' WHERE id = '" + user.id + "'");
						
						pstmt.executeUpdate();
						
						conn.close();
						pstmt.close();
						
						JOptionPane.showMessageDialog(null, "변경 완료", "정보", JOptionPane.INFORMATION_MESSAGE);
						
						} catch (Exception e1) { e1.printStackTrace(); }
					}
				else { JOptionPane.showMessageDialog(null, "비밀번호는 4 ~ 20글자로 구성되어야합니다.", "ERROR", JOptionPane.ERROR_MESSAGE); }
				
			} else {
				try {
					SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd");
					String test2 = test.format(input_birth.getDate());
					Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
					PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET name = '"+ input_name.getText() + "', birth = '" + java.sql.Date.valueOf(test2) + "' WHERE id = '" + user.id + "'");
					
					pstmt.executeUpdate();
					
					conn.close();
					pstmt.close();
					
					JOptionPane.showMessageDialog(null, "변경 완료", "정보", JOptionPane.INFORMATION_MESSAGE);
					dispose();
					
					} catch (Exception e1) { e1.printStackTrace(); }
			}
			
			}
		});
		

		panel.add(btn_confirm);

	}
	
	private void refresh(String ID) {
		User user = db.load_User("SELECT * FROM Users WHERE id = '" + ID + "'", null);
		new editUserImformation(user);
	}
	
}

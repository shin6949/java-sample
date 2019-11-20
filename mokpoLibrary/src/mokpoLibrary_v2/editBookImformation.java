package mokpoLibrary_v2;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class editBookImformation extends JFrame {

	private JTextField input_ISBN, input_name, input_author, input_location;
	private JLabel txt_status, txt_borrowedID;
	private JPanel contentPane;

	public editBookImformation(Book book, boolean mode) {
		//mode = true -> 변경 모드, false -> 추가 모드
		if(mode == true) { setTitle("\uCC45 \uC815\uBCF4 \uD3B8\uC9D1"); }
		else { setTitle("책 추가"); }
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel Text_Panel = new JPanel();
		Text_Panel.setBounds(10, 10, 58, 216);
		contentPane.add(Text_Panel);
		Text_Panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel label_ISBN = new JLabel("ISBN");
		Text_Panel.add(label_ISBN);
		
		JLabel label_name = new JLabel("\uC81C\uBAA9");
		Text_Panel.add(label_name);
		
		JLabel label_author = new JLabel("\uC800\uC790");
		Text_Panel.add(label_author);
		
		JLabel label_status = new JLabel("\uC0C1\uD0DC");
		Text_Panel.add(label_status);
		
		JLabel label_location = new JLabel("\uC704\uCE58");
		Text_Panel.add(label_location);
		
		JLabel label_borrowedID = new JLabel("\uB300\uCD9C ID");

			
		JPanel imformation_panel = new JPanel();
		imformation_panel.setBounds(78, 10, 344, 216);
		contentPane.add(imformation_panel);
		imformation_panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		input_ISBN = new JTextField();
		imformation_panel.add(input_ISBN);
		input_ISBN.setText(book.convert_ISBN(book.ISBN));
			
		input_name = new JTextField();
		input_name.setText("\uC81C\uBAA9");
		GridBagConstraints gbc_input_name = new GridBagConstraints();
		gbc_input_name.fill = GridBagConstraints.BOTH;
		gbc_input_name.insets = new Insets(0, 0, 0, 5);
		gbc_input_name.gridx = 0;
		gbc_input_name.gridy = 0;
		input_name.setText(book.name);
		imformation_panel.add(input_name, gbc_input_name);
		
		input_author = new JTextField();
		input_author.setText("\uC800\uC790");
		GridBagConstraints gbc_input_author = new GridBagConstraints();
		gbc_input_author.fill = GridBagConstraints.BOTH;
		gbc_input_author.insets = new Insets(0, 0, 0, 5);
		gbc_input_author.gridx = 1;
		gbc_input_author.gridy = 0;
		input_author.setText(book.author);
		imformation_panel.add(input_author, gbc_input_author);
		
		txt_status = new JLabel();
		txt_status.setText("\uC0C1\uD0DC");
		GridBagConstraints gbc_txt_status = new GridBagConstraints();
		gbc_txt_status.fill = GridBagConstraints.BOTH;
		gbc_txt_status.insets = new Insets(0, 0, 0, 5);
		gbc_txt_status.gridx = 2;
		gbc_txt_status.gridy = 0;
		txt_status.setText(book.convert_status(book.status));
		imformation_panel.add(txt_status, gbc_txt_status);
		
		input_location = new JTextField();
		input_location.setText("\uC704\uCE58");
		GridBagConstraints gbc_input_location = new GridBagConstraints();
		gbc_input_location.fill = GridBagConstraints.BOTH;
		gbc_input_location.insets = new Insets(0, 0, 0, 5);
		gbc_input_location.gridx = 3;
		gbc_input_location.gridy = 0;
		input_location.setText(book.location);
		
		imformation_panel.add(input_location, gbc_input_location);
						
		txt_borrowedID = new JLabel();
		txt_borrowedID.setText("\uB300\uCD9C\uD55C ID");
		GridBagConstraints gbc_txt_borrowedID = new GridBagConstraints();
		gbc_txt_borrowedID.fill = GridBagConstraints.BOTH;
		gbc_txt_borrowedID.gridx = 4;
		gbc_txt_borrowedID.gridy = 0;
		txt_borrowedID.setText(book.borrowed_id);
		imformation_panel.add(txt_borrowedID, gbc_txt_borrowedID);
		
		this.setVisible(true);
		Text_Panel.add(label_borrowedID);
		
		JPanel panel = new JPanel();
		panel.setBounds(278, 226, 144, 35);
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btn_cancel = new JButton("\uCDE8\uC18C");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btn_cancel.setFont(new Font("굴림", Font.PLAIN, 12));
		panel.add(btn_cancel);
		
		JButton btn_confirm = new JButton("");
		if(mode == true) {
			btn_confirm.setText("\uBCC0\uACBD");
		} else {
			btn_confirm.setText("추가");
		}
		btn_confirm.setFont(new Font("굴림", Font.PLAIN, 12));
		panel.add(btn_confirm);
		
		btn_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
					
				if(mode == true) {
					PreparedStatement pstmt = conn.prepareStatement(
						"UPDATE Books SET ISBN = ?, name = ?, author = ?, isin = ?, location = ?, borrow_id = ? WHERE ISBN = ?");
						
					pstmt.setInt(1, Integer.parseInt(input_ISBN.getText()));
					pstmt.setString(2, input_name.getText());
					pstmt.setString(3, input_author.getText());
					pstmt.setBoolean(4, book.status);
					pstmt.setString(5, input_location.getText());
					pstmt.setString(6, book.borrowed_id);
					pstmt.setInt(7, book.ISBN);
						
					pstmt.executeUpdate();
					
					if(book.status == false) {
					pstmt = conn.prepareStatement("UPDATE Users SET borrow_ISBN = ? WHERE id = ?");
					pstmt.setInt(1, Integer.parseInt(input_ISBN.getText()));
					pstmt.setString(2, book.borrowed_id);
					pstmt.executeUpdate();
					
					}
					JOptionPane.showMessageDialog(null,  "책 정보 변경이 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
					pstmt.close();
				} 
				
				else {	
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Books VALUES (?, ?, ?, true, ?, null, 0)");
				
				pstmt.setInt(1, Integer.parseInt(input_ISBN.getText()));
				pstmt.setString(2, input_name.getText());
				pstmt.setString(3, input_author.getText());
				pstmt.setString(4, input_location.getText());
				
				pstmt.executeUpdate();
				JOptionPane.showMessageDialog(null,  "책 추가가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				pstmt.close();	} 
				
				conn.close();
				dispose();

			}	catch (Exception e1) { e1.printStackTrace(); }
			}
		});
	}
}

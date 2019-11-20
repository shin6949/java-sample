package mokpoLibrary_v2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class Book {
	int ISBN;
	String name;
	String author;
	Boolean status;
	String location;
	String borrowed_id;
	int borrow_count;
	DB_info db = new DB_info();
	
	String convert_ISBN(int value) {
		String to = Integer.toString(value);
		return to;
	}
	
	String convert_status(Boolean status) {
		String string_status = null;
		
		if(status == true) { string_status = "대출 가능"; }
		else { string_status = "대출 불가"; }
		
		return string_status;
	}
	
	Boolean borrow_book(User user, JProgressBar progressBar) {
		if(status) { //책이 대출 가능한 상태이면...
			try {
				process_progressbar(progressBar, 0);
				
				Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
				System.out.printf("연결 성공\n");
				PreparedStatement pstmt = conn.prepareStatement("UPDATE Books SET isin = false, borrow_id = ?, borrow_count = ? WHERE ISBN = ?");
				pstmt.setString(1, user.id);
				pstmt.setInt(2, borrow_count + 1);
				pstmt.setInt(3, ISBN);
				System.out.printf("Books 업데이트 시도\n");
				pstmt.executeUpdate();
				process_progressbar(progressBar, 50);
				
				pstmt = conn.prepareStatement("UPDATE Users SET status = false, borrow_ISBN = ?, borrow_count = ? WHERE id = ?");
				pstmt.setInt(1,	ISBN);
				pstmt.setInt(2, user.borrow_count + 1);
				pstmt.setString(3, user.id);
				System.out.printf("Users 업데이트 시도\n");
				pstmt.executeUpdate();
				process_progressbar(progressBar, 70);
				
				
				SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String test2 = test.format(new Date());
				java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);
				
				pstmt = conn.prepareStatement("INSERT INTO log VALUES (?, ?, ?, ?, ?)");
				pstmt.setInt(1, ISBN);
				pstmt.setTimestamp(2, now_date);
				pstmt.setString(3, user.id);
				pstmt.setString(4, "Borrow");
				pstmt.setString(5, name);
				pstmt.executeUpdate();
				process_progressbar(progressBar, 80);
				
				user.status = false;
				user.borrow_ISBN = ISBN;
				user.borrow_count = user.borrow_count + 1;
				
				pstmt.close();
				conn.close();

				process_progressbar(progressBar, 100);
				
				JOptionPane.showMessageDialog(null,  "대출이 완료되었습니다.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
				
				return true;
			} catch (Exception e) {
				System.out.println(e);
				JOptionPane.showMessageDialog(null,  "대출이 가능한 상태가 아닙니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
				process_progressbar(progressBar, 100);
				return false;
			}
		} else { 
			process_progressbar(progressBar, 100);
			return false; 
		}
	}
		
	void process_progressbar(JProgressBar progressBar, int value) {
		switch(value) {
		case 0:
			if(progressBar != null) { 
				progressBar.setVisible(true);
				progressBar.setValue(0); }
			break;
			
		case 100:
			if(progressBar != null) { 
				progressBar.setValue(100);
				progressBar.setVisible(false);
				}
			break;
			
		default:
			if(progressBar != null) { progressBar.setValue(value); }
			break;
		}
	}
}

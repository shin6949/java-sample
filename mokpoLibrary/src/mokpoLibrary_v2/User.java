package mokpoLibrary_v2;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class User {
	String name;
	Boolean status;
	String id;
	Date birth;
	Boolean isadmin;
	int borrow_ISBN;
	String borrow_name;
	int borrow_count;
	
	String convert_status(Boolean status) {
		String string_status = null;
		
		if(status == true) { string_status = "대출 가능"; }
		else { string_status = "대출 불가"; }
		
		return string_status;
	}
	
	Boolean return_book(JProgressBar progressBar) {
		DB_info db = new DB_info();
		try {
			process_progressbar(progressBar, 0);
			Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
			Book book = db.load_book("SELECT * FROM Books WHERE ISBN = " + borrow_ISBN, null);
			
			PreparedStatement pstmt = conn.prepareStatement("UPDATE Books SET isin = true, borrow_id = NULL WHERE ISBN = ?");
			pstmt.setInt(1, borrow_ISBN);
			pstmt.executeUpdate();
			process_progressbar(progressBar, 50);
			
			pstmt = conn.prepareStatement("UPDATE Users SET status = true, borrow_ISBN = NULL WHERE id = ?");
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			process_progressbar(progressBar, 70);
			
			SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String test2 = test.format(new Date());
			java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);
			
			pstmt = conn.prepareStatement("INSERT INTO log VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, book.ISBN);
			pstmt.setTimestamp(2, now_date);
			pstmt.setString(3, id);
			pstmt.setString(4, "Return");
			pstmt.setString(5, book.name);
			System.out.printf("log 삽입 시도\n");
			pstmt.executeUpdate();
			process_progressbar(progressBar, 80);
			
			JOptionPane.showMessageDialog(null,  "반납 처리가 완료되었습니다.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
			status = true;
			borrow_ISBN = 0;
			process_progressbar(progressBar, 90);

			conn.close();
			
			process_progressbar(progressBar, 100);
			
			return true;
		} 
		catch (Exception e) { 
			System.out.println(e);
			JOptionPane.showMessageDialog(null,  "이 책은 반납이 불가능합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
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

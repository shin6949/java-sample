package com.cocoblue.libraryapp;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    int ISBN;
    String name;
    String author;
    Boolean status;
    String location;
    String borrowed_id;
    int borrow_count;
    DBInfo dbInfo = new DBInfo();

    String convert_ISBN(int value) {
        String to = Integer.toString(value);
        return to;
    }

    String convert_status(Boolean status) {
        String string_status = null;

        if (status) {
            string_status = "���� ����";
        } else {
            string_status = "���� �Ұ�";
        }

        return string_status;
    }

    Boolean borrow_book(User user, JProgressBar progressBar) {
        if (status) { //å�� ���� ������ �����̸�...
            try {
                process_progressbar(progressBar, 0);

                Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
                System.out.print("���� ����\n");
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Books SET isin = false, borrow_id = ?, borrow_count = ? WHERE ISBN = ?");
                pstmt.setString(1, user.id);
                pstmt.setInt(2, borrow_count + 1);
                pstmt.setInt(3, ISBN);
                System.out.print("Books ������Ʈ �õ�\n");
                pstmt.executeUpdate();
                process_progressbar(progressBar, 50);

                pstmt = conn.prepareStatement("UPDATE Users SET status = false, borrow_ISBN = ?, borrow_count = ? WHERE id = ?");
                pstmt.setInt(1, ISBN);
                pstmt.setInt(2, user.borrow_count + 1);
                pstmt.setString(3, user.id);
                System.out.print("Users ������Ʈ �õ�\n");
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

                JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);

                return true;
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "������ ������ ���°� �ƴմϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                process_progressbar(progressBar, 100);
                return false;
            }
        } else {
            process_progressbar(progressBar, 100);
            return false;
        }
    }

    void process_progressbar(JProgressBar progressBar, int value) {
        switch (value) {
            case 0:
                if (progressBar != null) {
                    progressBar.setVisible(true);
                    progressBar.setValue(0);
                }
                break;

            case 100:
                if (progressBar != null) {
                    progressBar.setValue(100);
                    progressBar.setVisible(false);
                }
                break;

            default:
                if (progressBar != null) {
                    progressBar.setValue(value);
                }
                break;
        }
    }
}

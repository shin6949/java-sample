package com.cocoblue.libraryapp.service;

import com.cocoblue.libraryapp.config.DBInfo;
import com.cocoblue.libraryapp.dto.Book;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserService {
    public String convert_status(Boolean status) {
        String string_status = null;

        if (status == true) {
            string_status = "���� ����";
        } else {
            string_status = "���� �Ұ�";
        }

        return string_status;
    }

    public void process_progressbar(JProgressBar progressBar, int value) {
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

    private boolean register(String name, String birth, String id, String pw) {
        try {
            Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users VALUES (?, true, ?, ?, ?, false, null, 0)");

            pstmt.setString(1, name);
            pstmt.setString(2, id);
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
//            pstmt.setString(4, "Register_Success");
            pstmt.executeUpdate();

            stmt.close();
            conn.close();
            pstmt.close();

            return true;
        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
    }
}

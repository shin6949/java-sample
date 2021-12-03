package com.cocoblue.libraryapp.service;

import com.cocoblue.libraryapp.config.DBInfo;
import com.cocoblue.libraryapp.dto.User;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginManager {
    public static User logined_user = new User();
    public static boolean logined = false;

    public void request_login(String id, String PW, JProgressBar ProgressBar) {
        try {
            ProgressBar.setVisible(true);
            Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, status, id, isadmin FROM Users WHERE id = '" + id + "' AND pw = '" + PW + "'");
            ProgressBar.setValue(50);

            if (rs.next()) {
                String name = rs.getString("name");
                Boolean bool_status = rs.getBoolean("status");
                String login_id = rs.getString("id");
                Boolean bool_admin = rs.getBoolean("isadmin");

                logined_user.id = login_id;
                logined_user.name = name;
                logined_user.status = bool_status;
                logined_user.isadmin = bool_admin;
            }

            if (id.equals(logined_user.id)) {
                logined = true;
                SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String test2 = test.format(new Date());
                java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);

                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO log VALUES (null, ?, ?, ?, null)");
                pstmt.setTimestamp(1, now_date);
                pstmt.setString(2, id);
                pstmt.setString(3, "Login_Success");
                pstmt.executeUpdate();
                ProgressBar.setValue(80);
                pstmt.close();
            } else {
                logined = false;
                JOptionPane.showMessageDialog(null, "���̵� �Ǵ� ��й�ȣ�� Ȯ���ϼ���.", "�α��� ����", JOptionPane.ERROR_MESSAGE);
                SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String test2 = test.format(new Date());
                java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);

                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO log VALUES (null, ?, ?, ?, null)");
                pstmt.setTimestamp(1, now_date);
                pstmt.setString(2, id);
                pstmt.setString(3, "Login_Fail");
                pstmt.executeUpdate();
                ProgressBar.setValue(80);
                pstmt.close();
            }

            ProgressBar.setValue(90);

            stmt.close();
            conn.close();
            rs.close();

            ProgressBar.setValue(100);
            ProgressBar.setVisible(false);

        } catch (Exception e1) {
            ProgressBar.setValue(0);
            e1.printStackTrace();
        }
    }

    public void request_logout(JProgressBar ProgressBar) {
        ProgressBar.setVisible(true);
        logined = false;
        logined_user = null;
        ProgressBar.setValue(100);
        ProgressBar.setVisible(false);
    }
}
package com.cocoblue.libraryapp;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUp extends JFrame {

    private JPanel signupPanel;
    private JTextField input_id;
    private JPasswordField input_pwd;
    private JTextField input_name;
    private boolean id_checked = false;
    private JDateChooser dateChooser;
    private JPasswordField input_pwd_re;

    public SignUp() {
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

                if (name_len <= 1 || name_len > 11) {
                    JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���. 2 ~ 10���ڷ� �����Ǿ���մϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    name_checked = false;
                } else {
                    name_checked = true;
                }

                if (dateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "��������� �Է��ϼ���.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    birth_checked = false;
                } else {
                    birth_checked = true;
                }

                if (!id_checked) {
                    JOptionPane.showMessageDialog(null, "ID �ߺ�Ȯ���� ���ּ���.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                if (input_pwd.getPassword().length < 4 || input_pwd.getPassword().length >= 20) {
                    JOptionPane.showMessageDialog(null, "��й�ȣ�� 4 ~ 20���ڷ� �����Ǿ���մϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    pwd_checked = false;
                } else if (input_pwd.getText().equals(input_pwd_re.getText())) {
                    pwd_checked = true;
                } else {
                    JOptionPane.showMessageDialog(null, "��й�ȣ�� 2ĭ ��� ��Ȯ�ϰ� �Է��ϼ���.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    pwd_checked = false;
                }

                if (name_checked && birth_checked && pwd_checked && id_checked) {

                    SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd");
                    String test2 = test.format(dateChooser.getDate());

                    System.out.printf("%s %s %s\n", input_name.getText(), test2, input_id.getText());
                    if (register(input_name.getText(), test2, input_id.getText(), input_pwd.getText())) {
                        JOptionPane.showMessageDialog(null, "ȸ�� ���� �Ϸ�", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "ȸ�� ���� ����", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        btn_register.setBounds(325, 228, 97, 23);
        signupPanel.add(btn_register);

        JButton btn_check_id = new JButton("\uC911\uBCF5\uD655\uC778");
        btn_check_id.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println(input_id.getText());
                if (input_id.getText().length() >= 4 && input_id.getText().length() <= 10) {
                    if (check_id(input_id.getText())) {
                        int result = JOptionPane.showConfirmDialog(null, "ID ����� �����մϴ�. ����Ͻðڽ��ϱ�?", "�˸�", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.CLOSED_OPTION) {
                        } else if (result == JOptionPane.YES_OPTION) {
                            input_id.setEnabled(false);
                            id_checked = true;
                        } else {
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "�ߺ��� ID�� �ֽ��ϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "ID ��Ģ�� ���� �ʽ��ϴ�. ID�� 4 ~ 10 ���ڷ� �����Ǿ�� �մϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
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

    private boolean check_id(String id) {
        try {
            Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users Where id LIKE '" + id + "'");
            int count = 0;

            if (rs.next()) {
                count++;
            }

            if (count == 0) {
                rs.close();
                stmt.close();
                conn.close();
                return true;
            } else {
                rs.close();
                stmt.close();
                conn.close();
                return false;
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
    }
}

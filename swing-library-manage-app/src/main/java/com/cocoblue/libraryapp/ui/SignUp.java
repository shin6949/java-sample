package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.service.UserService;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;

public class SignUp extends JFrame {
    // UI Component
    private final JTextField inputId;
    private final JPasswordField inputPwd;
    private final JTextField inputName;
    private boolean idChecked = false;
    private final JDateChooser dateChooser;
    private final JPasswordField inputPwdRe;

    // Service
    private final UserService userService = new UserService();

    public SignUp() {
        setTitle("\uD68C\uC6D0\uAC00\uC785");
        setBounds(100, 100, 450, 300);
        JPanel signupPanel = new JPanel();
        signupPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(signupPanel);
        signupPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("ID");
        lblNewLabel.setBounds(28, 108, 77, 39);
        signupPanel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638");
        lblNewLabel_1.setBounds(28, 154, 77, 39);
        signupPanel.add(lblNewLabel_1);

        inputId = new JTextField();
        inputId.setBounds(134, 117, 173, 21);
        signupPanel.add(inputId);
        inputId.setColumns(10);

        inputPwd = new JPasswordField();
        inputPwd.setBounds(134, 166, 173, 21);
        signupPanel.add(inputPwd);

        JLabel label = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
        label.setBounds(28, 65, 57, 15);
        signupPanel.add(label);

        JLabel lblNewLabel_2 = new JLabel("\uC774\uB984");
        lblNewLabel_2.setBounds(28, 13, 57, 15);
        signupPanel.add(lblNewLabel_2);

        inputName = new JTextField();
        inputName.setBounds(134, 10, 173, 21);
        signupPanel.add(inputName);
        inputName.setColumns(10);

        JButton btn_register = new JButton("\uD68C\uC6D0\uAC00\uC785");
        btn_register.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                int name_len = inputName.getText().length();
                boolean name_checked, birth_checked, pwd_checked = false;

                if (name_len <= 1 || name_len > 11) {
                    JOptionPane.showMessageDialog(null, "이름을 입력해주세요. 2 ~ 10글자로 구성되어야합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    name_checked = false;
                } else {
                    name_checked = true;
                }

                if (dateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "생년월일을 입력하세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    birth_checked = false;
                } else {
                    birth_checked = true;
                }

                if (!idChecked) {
                    JOptionPane.showMessageDialog(null, "ID 중복확인을 해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                if (inputPwd.getPassword().length < 4 || inputPwd.getPassword().length >= 20) {
                    JOptionPane.showMessageDialog(null, "비밀번호는 4 ~ 20글자로 구성되어야합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    pwd_checked = false;
                } else if (inputPwd.getText().equals(inputPwdRe.getText())) {
                    pwd_checked = true;
                } else {
                    JOptionPane.showMessageDialog(null, "비밀번호를 2칸 모두 정확하게 입력하세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    pwd_checked = false;
                }

                if (name_checked && birth_checked && pwd_checked && idChecked) {
                    LocalDate localDate = dateChooser.getDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    if (userService.register(inputName.getText(), localDate, inputId.getText(), inputPwd.getPassword())) {
                        JOptionPane.showMessageDialog(null, "회원 가입 완료", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "회원 가입 실패", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        btn_register.setBounds(325, 228, 97, 23);
        signupPanel.add(btn_register);

        JButton btn_check_id = new JButton("\uC911\uBCF5\uD655\uC778");
        btn_check_id.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println(inputId.getText());
                if (inputId.getText().length() >= 4 && inputId.getText().length() <= 10) {
                    if (check_id(inputId.getText())) {
                        int result = JOptionPane.showConfirmDialog(null, "ID 사용이 가능합니다. 사용하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.CLOSED_OPTION) {
                        } else if (result == JOptionPane.YES_OPTION) {
                            inputId.setEnabled(false);
                            idChecked = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "중복된 ID가 있습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "ID 규칙에 맞지 않습니다. ID는 4 ~ 10 글자로 구성되어야 합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
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

        inputPwdRe = new JPasswordField();
        inputPwdRe.setBounds(134, 218, 173, 21);
        signupPanel.add(inputPwdRe);

        this.setVisible(true);

    }

    private boolean check_id(String id) {
        return userService.getUserByUsername(id) == null;
    }
}

package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.dto.User;
import com.cocoblue.libraryapp.service.BookService;
import com.cocoblue.libraryapp.service.UserService;
import com.toedter.calendar.JDateChooser;
import jdk.vm.ci.meta.Local;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class EditUserInformation extends JFrame {

    private final JTextField inputName;
    private JTextField inputLocation;
    private final JPasswordField inputPwd;

    // Service
    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();

    public EditUserInformation(User user) {
        // 유저 정보 편집
        setTitle("\uC720\uC800 \uC815\uBCF4 \uD3B8\uC9D1");
        setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel textPanel = new JPanel();
        textPanel.setBounds(10, 10, 58, 216);
        contentPane.add(textPanel);
        textPanel.setLayout(new GridLayout(0, 1, 0, 0));

        // 이름
        JLabel labelIsbn = new JLabel("\uC774\uB984");
        textPanel.add(labelIsbn);

        JLabel lblId = new JLabel("ID");
        textPanel.add(lblId);

        // 비밀번호
        JLabel labelAuthor = new JLabel("\uBE44\uBC00\uBC88\uD638");
        textPanel.add(labelAuthor);

        // 상태
        JLabel labelStatus = new JLabel("\uC0C1\uD0DC");
        textPanel.add(labelStatus);

        // 생년월일
        JLabel labelLocation = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
        textPanel.add(labelLocation);

        // 대출한 책
        JLabel labelBorrowedID = new JLabel("\uB300\uCD9C\uD55C \uCC45");

        JPanel informationPanel = new JPanel();
        informationPanel.setBounds(78, 10, 344, 216);
        contentPane.add(informationPanel);
        informationPanel.setLayout(new GridLayout(0, 1, 0, 0));

        inputName = new JTextField();
        // 이름
        inputName.setText("\uC774\uB984");
        informationPanel.add(inputName);
        inputName.setText(user.getName());

        JLabel inputId = new JLabel();
        GridBagConstraints gbcInputId = new GridBagConstraints();
        gbcInputId.fill = GridBagConstraints.BOTH;
        gbcInputId.insets = new Insets(0, 0, 0, 5);
        gbcInputId.gridx = 0;
        gbcInputId.gridy = 0;
        inputId.setText(user.getUsername());
        informationPanel.add(inputId, gbcInputId);

        inputPwd = new JPasswordField();
        inputPwd.setText("");
        GridBagConstraints gbcInputPwd = new GridBagConstraints();
        gbcInputPwd.fill = GridBagConstraints.BOTH;
        gbcInputPwd.insets = new Insets(0, 0, 0, 5);
        gbcInputPwd.gridx = 1;
        gbcInputPwd.gridy = 0;
        informationPanel.add(inputPwd, gbcInputPwd);

        JLabel txtStatus = new JLabel();
        // 상태
        txtStatus.setText("\uC0C1\uD0DC");
        GridBagConstraints gbc_txt_status = new GridBagConstraints();
        gbc_txt_status.fill = GridBagConstraints.BOTH;
        gbc_txt_status.insets = new Insets(0, 0, 0, 5);
        gbc_txt_status.gridx = 2;
        gbc_txt_status.gridy = 0;
        txtStatus.setText(user.getStatus() ? "대출 가능" : "대출 중");
        informationPanel.add(txtStatus, gbc_txt_status);

        JDateChooser inputBirth = new JDateChooser();
        inputBirth.setDate(Date.from(user.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        GridBagConstraints gbc_input_birth = new GridBagConstraints();
        gbc_input_birth.fill = GridBagConstraints.BOTH;
        gbc_input_birth.insets = new Insets(0, 0, 0, 5);
        gbc_input_birth.gridx = 3;
        gbc_input_birth.gridy = 0;

        informationPanel.add(inputBirth, gbc_input_birth);

        JLabel txtBorrowedBookName = new JLabel();
        // 대출한 ID
        txtBorrowedBookName.setText("\uB300\uCD9C\uD55C ID");
        GridBagConstraints gbcTxtBorrowedBookName = new GridBagConstraints();
        gbcTxtBorrowedBookName.fill = GridBagConstraints.BOTH;
        gbcTxtBorrowedBookName.gridx = 4;
        gbcTxtBorrowedBookName.gridy = 0;
        txtBorrowedBookName.setText(user.getBorrowedBook().getName());
        informationPanel.add(txtBorrowedBookName, gbcTxtBorrowedBookName);

        this.setVisible(true);
        textPanel.add(labelBorrowedID);

        JPanel panel = new JPanel();
        panel.setBounds(88, 226, 334, 35);
        contentPane.add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // 취소
        JButton btnCancel = new JButton("\uCDE8\uC18C");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });

        // 반납 처리
        JButton btn_return = new JButton("\uBC18\uB0A9 \uCC98\uB9AC");
        btn_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!user.getStatus()) {
                    bookService.returnBook(user);
                    refresh(user.getUsername());
                    dispose();
                }

            }
        });
        panel.add(btn_return);

        // 탈퇴 처리
        final JButton btnRetire = new JButton("\uD0C8\uD1F4 \uCC98\uB9AC");
        btnRetire.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final User user = userService.getUserByUsername(inputId.getText());

                int result = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    userService.deleteUser(user);
                    JOptionPane.showMessageDialog(null, "유저 삭제가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        });

        panel.add(btnRetire);
        panel.add(btnCancel);

        JButton btnConfirm = new JButton("변경");
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Password 칸 입력 여부 판단
                if (inputPwd.getPassword().length != 0) {
                    // Password Length 정책 충족 여부 판단
                    if (inputPwd.getPassword().length > 4 || inputPwd.getPassword().length <= 20) {
                        LocalDate localDate = inputBirth.getDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        // TODO: 여기서부터
                        user = User.builder()
                                .username(user.getUsername())
                                .name(inputName.getText())
                                .password(String.valueOf(inputPwd.getPassword()))
                                .birthday(localDate)
                                .build();

                        userService.updateUser(updateUser);

                        JOptionPane.showMessageDialog(null, "변경 완료", "정보", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(null, "비밀번호는 4 ~ 20글자로 구성되어야합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    try {
                        SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd");
                        String test2 = test.format(inputBirth.getDate());
                        Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
                        PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET name = '" + inputName.getText() + "', birth = '" + java.sql.Date.valueOf(test2) + "' WHERE id = '" + user.id + "'");

                        pstmt.executeUpdate();

                        conn.close();
                        pstmt.close();

                        JOptionPane.showMessageDialog(null, "���� �Ϸ�", "����", JOptionPane.INFORMATION_MESSAGE);
                        dispose();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        panel.add(btnConfirm);

    }

    private void refresh(String ID) {
        User user = dbInfo.load_User("SELECT * FROM Users WHERE id = '" + ID + "'", null);
        new EditUserInformation(user);
    }

}

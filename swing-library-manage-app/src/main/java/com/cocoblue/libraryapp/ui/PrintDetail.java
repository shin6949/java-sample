package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import com.cocoblue.libraryapp.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintDetail extends JFrame {
    // Service
    private final UserService userService = new UserService();

    public PrintDetail(Book book) {
        setTitle("\uB3C4\uC11C\uC0C1\uC138\uC815\uBCF4");
        setBounds(100, 100, 463, 456);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(0, 10, 447, 397);
        contentPane.add(panel);
        panel.setLayout(null);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setBounds(12, 10, 58, 344);
        panel.add(textPanel);
        textPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JLabel labelISBN = new JLabel("ISBN");
        textPanel.add(labelISBN);

        JLabel labelName = new JLabel("\uC81C\uBAA9");
        textPanel.add(labelName);

        JLabel labelAuthor = new JLabel("\uC800\uC790");
        textPanel.add(labelAuthor);

        JLabel labelStatus = new JLabel("\uC0C1\uD0DC");
        textPanel.add(labelStatus);

        JLabel labelLocation = new JLabel("\uC704\uCE58");
        textPanel.add(labelLocation);

        JLabel labelBorrowedID = new JLabel("\uB300\uCD9C ID");
        textPanel.add(labelBorrowedID);

        JLabel labelBorrowCount = new JLabel("\uB300\uCD9C \uD69F\uC218");
        textPanel.add(labelBorrowCount);


        JPanel imformationPanel = new JPanel();
        imformationPanel.setBackground(Color.WHITE);
        imformationPanel.setBounds(78, 10, 356, 344);
        panel.add(imformationPanel);
        imformationPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JLabel txtISBN = new JLabel();
        imformationPanel.add(txtISBN);
        txtISBN.setText(String.valueOf(book.getIsbn()));

        JLabel txtName = new JLabel();
        txtName.setText("\uC81C\uBAA9");
        GridBagConstraints gbcTxtName = new GridBagConstraints();
        gbcTxtName.fill = GridBagConstraints.BOTH;
        gbcTxtName.insets = new Insets(0, 0, 0, 5);
        gbcTxtName.gridx = 0;
        gbcTxtName.gridy = 0;
        txtName.setText(book.getName());
        imformationPanel.add(txtName, gbcTxtName);

        JLabel txtAuthor = new JLabel();
        txtAuthor.setText("\uC800\uC790");
        GridBagConstraints gbcTxtAuthor = new GridBagConstraints();
        gbcTxtAuthor.fill = GridBagConstraints.BOTH;
        gbcTxtAuthor.insets = new Insets(0, 0, 0, 5);
        gbcTxtAuthor.gridx = 1;
        gbcTxtAuthor.gridy = 0;
        txtAuthor.setText(book.getAuthor());
        imformationPanel.add(txtAuthor, gbcTxtAuthor);

        JLabel txtStatus = new JLabel();
        txtStatus.setText("\uC0C1\uD0DC");
        GridBagConstraints gbcTxtStatus = new GridBagConstraints();
        gbcTxtStatus.fill = GridBagConstraints.BOTH;
        gbcTxtStatus.insets = new Insets(0, 0, 0, 5);
        gbcTxtStatus.gridx = 2;
        gbcTxtStatus.gridy = 0;
        txtStatus.setText(book.getStatusToString());
        imformationPanel.add(txtStatus, gbcTxtStatus);

        JLabel txtLocation = new JLabel();
        txtLocation.setText("\uC704\uCE58");
        GridBagConstraints gbcTxtLocation = new GridBagConstraints();
        gbcTxtLocation.fill = GridBagConstraints.BOTH;
        gbcTxtLocation.insets = new Insets(0, 0, 0, 5);
        gbcTxtLocation.gridx = 3;
        gbcTxtLocation.gridy = 0;
        txtLocation.setText(book.getLocation());

        imformationPanel.add(txtLocation, gbcTxtLocation);

        JLabel txtBorrowedID = new JLabel();
        txtBorrowedID.setText("\uB300\uCD9C\uD55C ID");
        GridBagConstraints gbcTxtBorrowedID = new GridBagConstraints();
        gbcTxtBorrowedID.fill = GridBagConstraints.BOTH;
        gbcTxtBorrowedID.gridx = 4;
        gbcTxtBorrowedID.gridy = 0;
        if (!book.getStatus()) {
            final User borrowedUser = userService.getUserByBorrowedBook(book);
            txtBorrowedID.setText(borrowedUser.getUsername().substring(0, 3) + "****");
        } else {
            txtBorrowedID.setText("");
        }
        imformationPanel.add(txtBorrowedID, gbcTxtBorrowedID);

        JLabel txtCount = new JLabel("\uB300\uCD9C \uD69F\uC218");
        txtCount.setText(Integer.toString(book.getBorrowCount()));
        imformationPanel.add(txtCount);

        SimpleDateFormat nowDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeString = nowDateTime.format(new Date());

        JLabel labelNowTime = new JLabel("\uD604\uC7AC \uC2DC\uAC04");
        labelNowTime.setBounds(12, 364, 422, 15);
        labelNowTime.setText("인쇄 시간: " + nowTimeString);
        panel.add(labelNowTime);

        this.setVisible(true);
        print();
        this.setVisible(false);
        dispose();
    }

    // 출처: https://annehouse.tistory.com/?page=234
    public void print() {
        PrintJob pjb = getToolkit().getPrintJob(this, "인쇄하기", null);

        if (pjb != null) {
            Graphics pg = pjb.getGraphics();

            if (pg != null) {
                paint(pg);
                pg.dispose();
            }
            pjb.end();
        }
    }
}

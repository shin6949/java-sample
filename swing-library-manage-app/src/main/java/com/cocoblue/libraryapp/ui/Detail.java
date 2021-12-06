package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import com.cocoblue.libraryapp.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Detail extends JFrame {
    // Service
    private final UserService userService = new UserService();

    public Detail(Book book) {
        setTitle("\uB3C4\uC11C\uC0C1\uC138\uC815\uBCF4");
        setBounds(100, 100, 463, 456);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        panel.setBounds(0, 10, 447, 364);
        contentPane.add(panel);
        panel.setLayout(null);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(new Color(240, 240, 240));
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

        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(new Color(240, 240, 240));
        informationPanel.setBounds(78, 10, 356, 344);
        panel.add(informationPanel);
        informationPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JLabel txtIsbn = new JLabel();
        informationPanel.add(txtIsbn);
        txtIsbn.setText(String.valueOf(book.getIsbn()));

        JLabel txtName = new JLabel();
        txtName.setText("\uC81C\uBAA9");
        GridBagConstraints gbcTxtName = new GridBagConstraints();
        gbcTxtName.fill = GridBagConstraints.BOTH;
        gbcTxtName.insets = new Insets(0, 0, 0, 5);
        gbcTxtName.gridx = 0;
        gbcTxtName.gridy = 0;
        txtName.setText(book.getName());
        informationPanel.add(txtName, gbcTxtName);

        JLabel txtAuthor = new JLabel();
        txtAuthor.setText("\uC800\uC790");
        GridBagConstraints gbcTxtAuthor = new GridBagConstraints();
        gbcTxtAuthor.fill = GridBagConstraints.BOTH;
        gbcTxtAuthor.insets = new Insets(0, 0, 0, 5);
        gbcTxtAuthor.gridx = 1;
        gbcTxtAuthor.gridy = 0;
        txtAuthor.setText(book.getAuthor());
        informationPanel.add(txtAuthor, gbcTxtAuthor);

        JLabel txtStatus = new JLabel();
        txtStatus.setText("\uC0C1\uD0DC");
        GridBagConstraints gbcTxtStatus = new GridBagConstraints();
        gbcTxtStatus.fill = GridBagConstraints.BOTH;
        gbcTxtStatus.insets = new Insets(0, 0, 0, 5);
        gbcTxtStatus.gridx = 2;
        gbcTxtStatus.gridy = 0;
        txtStatus.setText(book.getStatusToString());
        informationPanel.add(txtStatus, gbcTxtStatus);

        JLabel txtLocation = new JLabel();
        txtLocation.setText("\uC704\uCE58");
        GridBagConstraints gbcTxtLocation = new GridBagConstraints();
        gbcTxtLocation.fill = GridBagConstraints.BOTH;
        gbcTxtLocation.insets = new Insets(0, 0, 0, 5);
        gbcTxtLocation.gridx = 3;
        gbcTxtLocation.gridy = 0;
        txtLocation.setText(book.getLocation());

        informationPanel.add(txtLocation, gbcTxtLocation);

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
        informationPanel.add(txtBorrowedID, gbcTxtBorrowedID);

        JLabel txtCount = new JLabel("\uB300\uCD9C \uD69F\uC218");
        txtCount.setText(Integer.toString(book.getBorrowCount()));
        informationPanel.add(txtCount);

        JButton btnPrint = new JButton("\uC778\uC1C4");
        btnPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PrintDetail(book);
            }
        });
        btnPrint.setBounds(360, 384, 75, 23);
        contentPane.add(btnPrint);

        this.setVisible(true);
    }
}

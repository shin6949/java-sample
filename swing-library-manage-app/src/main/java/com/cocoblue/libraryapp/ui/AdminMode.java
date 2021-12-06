package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import com.cocoblue.libraryapp.service.BookService;
import com.cocoblue.libraryapp.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminMode extends JFrame {
    InitializeTable area;

    // Service
    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();

    public AdminMode() {
        setTitle("\uAD00\uB9AC\uC790 \uBAA8\uB4DC");
        setBounds(100, 100, 637, 321);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // 책 관리
        JMenu menuBook = new JMenu("\uCC45 \uAD00\uB9AC");
        menuBar.add(menuBook);

        // 책 추가
        JMenuItem itemAddBook = new JMenuItem("\uCC45 \uCD94\uAC00");
        itemAddBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Book book = Book.builder()
                        .status(true)
                        .build();

                new EditBookInformation(book, false);
            }
        });
        menuBook.add(itemAddBook);

        // 선택한 책 삭제
        JMenuItem itemDelBook = new JMenuItem("\uC120\uD0DD\uD55C \uCC45 \uC0AD\uC81C");
        itemDelBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String isbn = (String) area.model.getValueAt(area.table.getSelectedRow(), 0);
                final Book book = bookService.getBookByIsbn(isbn);

                int result = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.CLOSED_OPTION) {
                } else if (result == JOptionPane.YES_OPTION) {
                    bookService.deleteBook(book);
                    JOptionPane.showMessageDialog(null, "책 삭제가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        menuBook.add(itemDelBook);

        // 선택한 책 정보 편집
        JMenuItem Item_modifyBook = new JMenuItem("\uC120\uD0DD\uD55C \uCC45 \uC815\uBCF4 \uD3B8\uC9D1");
        Item_modifyBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String isbn = (String) area.model.getValueAt(area.table.getSelectedRow(), 0);
                final Book book = bookService.getBookByIsbn(isbn);

                new EditBookInformation(book, true);
            }
        });
        menuBook.add(Item_modifyBook);

        // 통계
        JMenu menuStatistics = new JMenu("\uD1B5\uACC4");
        menuBar.add(menuStatistics);

        // 책 대출 통계
        JMenuItem itemBookStat = new JMenuItem("\uCC45 \uB300\uCD9C \uD1B5\uACC4");
        itemBookStat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new BookStatistics();
            }
        });
        menuStatistics.add(itemBookStat);

        // 유저 대출 통계
        JMenuItem itemUserStat = new JMenuItem("\uC720\uC800 \uB300\uCD9C \uD1B5\uACC4");
        itemUserStat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new UserStatistics();
            }
        });
        menuStatistics.add(itemUserStat);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        area = new InitializeTable(contentPane);

        JPanel toolBoxPanel = new JPanel();
        toolBoxPanel.setBounds(512, 48, 97, 199);
        contentPane.add(toolBoxPanel);
        toolBoxPanel.setLayout(new GridLayout(0, 1, 0, 0));

        // 대출
        JButton btnBorrow = new JButton("\uB300\uCD9C");
        btnBorrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 2)).equals("상태")) {
                    final String inputId = JOptionPane.showInputDialog(null, "대출자의 ID를 입력하세요.", null);
                    final User requestedUser = userService.getUserByUsername(inputId);

                    if (inputId.equals(requestedUser.getUsername())) {
                        final String isbn = String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0));
                        final Book book = bookService.getBookByIsbn(isbn);
                        if (bookService.borrowBook(book, requestedUser)) {
                            refresh();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "해당 ID가 존재하지 않습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
        toolBoxPanel.add(btnBorrow);

        // 책 반납
        JButton btnReturn = new JButton("\uCC45 \uBC18\uB0A9");
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String inputId = JOptionPane.showInputDialog(null, "대출자의 ID를 입력하세요.", null);
                final User requestedUser = userService.getUserByUsername(inputId);

                if (inputId.equals(requestedUser.getUsername())) {
                    // 대출한 책이 있는지 체크
                    if (!requestedUser.getStatus()) {
                        bookService.returnBook(requestedUser);
                        refresh();
                    } else {
                        JOptionPane.showMessageDialog(null, "해당 유저는 대출한 책이 없습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "해당 ID가 존재하지 않습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        toolBoxPanel.add(btnReturn);

        // 유저 편집
        JButton btn_editUser = new JButton("\uC720\uC800 \uD3B8\uC9D1");
        btn_editUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String inputId = JOptionPane.showInputDialog(null, "수정할 ID를 입력하세요.", null);

                if (inputId != null) {
                    User user = userService.getUserByUsername(inputId);
                    new EditUserInformation(user);
                }
            }
        });
        toolBoxPanel.add(btn_editUser);
        // 새로고침
        JButton btnRefresh = new JButton("\uC0C8\uB85C\uACE0\uCE68");
        toolBoxPanel.add(btnRefresh);

        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                refresh();
                area.input_searchBooks.setText("");
            }
        });

        this.setVisible(true);

        refresh();
    }

    private void refresh() {
        List<Book> bookList = bookService.getAllBook();
        bookService.reflectDataOnModel(area.model, bookList);
    }
}


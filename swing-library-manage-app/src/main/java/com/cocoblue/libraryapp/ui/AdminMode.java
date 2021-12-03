package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.config.DBInfo;
import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AdminMode extends JFrame {
    private static JPanel contentPane;
    private static DBInfo db = new DBInfo();
    private JButton btn_borrow, btn_return;
    InitializeTable area;

    public AdminMode() {
        setTitle("\uAD00\uB9AC\uC790 \uBAA8\uB4DC");
        setBounds(100, 100, 637, 321);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu Menu_Book = new JMenu("\uCC45 \uAD00\uB9AC");
        menuBar.add(Menu_Book);

        JMenuItem Item_addBook = new JMenuItem("\uCC45 \uCD94\uAC00");
        Item_addBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Book book = new Book();
                book.ISBN = 0;
                book.name = "";
                book.author = "";
                book.status = true;
                book.location = "";
                book.borrowed_id = "";
                new EditBookInformation(book, false);
            }
        });
        Menu_Book.add(Item_addBook);

        JMenuItem Item_delBook = new JMenuItem("\uC120\uD0DD\uD55C \uCC45 \uC0AD\uC81C");
        Item_delBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Book book = db.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
                int result = JOptionPane.showConfirmDialog(null, "���� �����Ͻðڽ��ϱ�?", "�˸�", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.CLOSED_OPTION) {
                } else if (result == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Books WHERE ISBN = ?");
                        pstmt.setInt(1, book.ISBN);
                        pstmt.executeUpdate();
                        pstmt.close();

                        if (!book.status) {
                            PreparedStatement update_user = conn.prepareStatement("UPDATE Users SET status = true, borrow_ISBN = null WHERE id = '" + book.borrowed_id + "'");
                            update_user.executeUpdate();
                            update_user.close();
                        }

                        conn.close();


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "å ������ �Ϸ�Ǿ����ϴ�.", "����", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        Menu_Book.add(Item_delBook);

        JMenuItem Item_modifyBook = new JMenuItem("\uC120\uD0DD\uD55C \uCC45 \uC815\uBCF4 \uD3B8\uC9D1");
        Item_modifyBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Book book = db.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
                new EditBookInformation(book, true);
            }
        });
        Menu_Book.add(Item_modifyBook);

        JMenu Menu_Statistics = new JMenu("\uD1B5\uACC4");
        menuBar.add(Menu_Statistics);

        JMenuItem Item_bookstat = new JMenuItem("\uCC45 \uB300\uCD9C \uD1B5\uACC4");
        Item_bookstat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new BookStatistics();
            }
        });
        Menu_Statistics.add(Item_bookstat);

        JMenuItem Item_userstat = new JMenuItem("\uC720\uC800 \uB300\uCD9C \uD1B5\uACC4");
        Item_userstat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new UserStatistics();
            }
        });
        Menu_Statistics.add(Item_userstat);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        area = new InitializeTable(contentPane);

        JPanel tool_box_panel = new JPanel();
        tool_box_panel.setBounds(512, 48, 97, 199);
        contentPane.add(tool_box_panel);
        tool_box_panel.setLayout(new GridLayout(0, 1, 0, 0));

        btn_borrow = new JButton("\uB300\uCD9C");
        btn_borrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                if (String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 2)).equals("���� �Ұ�")) {
                    JOptionPane.showMessageDialog(null, "�� å�� ������ �Ұ����մϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    String input_id = JOptionPane.showInputDialog(null, "�������� ID�� �Է��ϼ���.", null);
                    User requested_user = db.load_User(input_id, area.load_progressbar);

                    if (input_id.equals(requested_user.id)) {
                        Book book = db.load_book("SELECT e* FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
                        if (book.borrow_book(requested_user, area.load_progressbar)) {
                            refresh(area.model, area.load_progressbar);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "�ش� ID�� �������� �ʽ��ϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
        tool_box_panel.add(btn_borrow);

        btn_return = new JButton("\uCC45 \uBC18\uB0A9");
        btn_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input_id = JOptionPane.showInputDialog(null, "�������� ID�� �Է��ϼ���.", null);
                User requested_user = db.load_User(input_id, area.load_progressbar);

                if (input_id.equals(requested_user.id)) {
                    if (!requested_user.status) {
                        requested_user.return_book(area.load_progressbar);
                        refresh(area.model, area.load_progressbar);
                    } else {
                        JOptionPane.showMessageDialog(null, "���� å�� �������� �ʽ��ϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "�ش� ID�� �������� �ʽ��ϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tool_box_panel.add(btn_return);

        JButton btn_editUser = new JButton("\uC720\uC800 \uD3B8\uC9D1");
        btn_editUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input_id = JOptionPane.showInputDialog(null, "������ ID�� �Է��ϼ���.", null);

                if (input_id != null) {
                    User user = db.load_User(input_id, area.load_progressbar);
                    new EditUserInformation(user);
                }
            }
        });
        tool_box_panel.add(btn_editUser);

        JButton btn_refresh = new JButton("\uC0C8\uB85C\uACE0\uCE68");
        tool_box_panel.add(btn_refresh);

        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                refresh(area.model, area.load_progressbar);
                area.input_searchBooks.setText("");
            }
        });

        this.setVisible(true);

        db.public_ResultSet_SQL_Query(area.model, "select * from Books;", area.load_progressbar);
    }

    private void refresh(DefaultTableModel model, JProgressBar progressBar) {
        model.setRowCount(0);
        db.public_ResultSet_SQL_Query(model, "select * from Books;", area.load_progressbar);
    }
}


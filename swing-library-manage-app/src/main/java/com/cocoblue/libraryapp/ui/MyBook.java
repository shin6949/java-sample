package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.config.DBInfo;
import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import com.cocoblue.libraryapp.service.BookService;
import com.cocoblue.libraryapp.service.LoginManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyBook extends JFrame implements Runnable {
    // GUI Component
    private JPanel contentPane;
    private JTextField inputSearchBooks;
    private final JTextField inputId;
    private final JPasswordField inputPwd;
    private final DBInfo dbInfo = new DBInfo();
    private LoginManager loginManager = new LoginManager();
    private final JButton btnBorrow;
    private final JButton btnReturn;
    private JButton btnAdmin;
    private final JPanel loginBtnPanel;
    private final JPanel loginedPanel;
    private final JPanel loginPanel;
    private InitializeTable area;
    private final JLabel loginedStatus;

    // Service
    private final BookService bookService = new BookService();

    public static void main(String[] args) {
        MyBook frame = new MyBook();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyBook frame = new MyBook();
                    Thread load_db = new Thread(frame, "DB 로딩");
                    load_db.start();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyBook() {
        setTitle("\uB3C4\uC11C \uAD00\uB9AC \uC2DC\uC2A4\uD15C");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 637, 336);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        area = new InitializeTable(contentPane);

        JPanel tool_box_panel = new JPanel();
        tool_box_panel.setBounds(512, 52, 97, 194);
        contentPane.add(tool_box_panel);
        tool_box_panel.setLayout(new GridLayout(0, 1, 0, 0));

        btnBorrow = new JButton("\uB300\uCD9C");
        btnBorrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Book book = dbInfo.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
                if (bookService.borrowBook(LoginManager.logined_user, area.load_progressbar)) {
                    refresh(area.model, area.load_progressbar);
                    check_borrow_status(btnBorrow, btnReturn);
                }
            }
        });
        tool_box_panel.add(btnBorrow);

        btnReturn = new JButton("\uCC45 \uBC18\uB0A9");
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (LoginManager.logined_user.return_book(area.load_progressbar)) {
                    refresh(area.model, area.load_progressbar);
                    check_borrow_status(btnBorrow, btnReturn);
                }
            }
        });

        tool_box_panel.add(btnReturn);

        JButton btn_detail = new JButton("\uC0C1\uC138 \uC815\uBCF4");
        tool_box_panel.add(btn_detail);
        btn_detail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Book book = dbInfo.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(area.model.getValueAt(area.table.getSelectedRow(), 0)) + "'", area.load_progressbar);
                new Detail(book);
            }
        });

        JButton btn_refresh = new JButton("\uCD08\uAE30\uD654\uBA74");
        tool_box_panel.add(btn_refresh);
        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                refresh(area.model, area.load_progressbar);
                area.input_searchBooks.setText("");
            }
        });

        JButton btn_admin = new JButton("\uAD00\uB9AC \uBAA8\uB4DC");
        btn_admin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new AdminMode();
            }
        });
        tool_box_panel.add(btn_admin);
        btn_admin.setVisible(false);

        Panel login_session_panel = new Panel();
        login_session_panel.setBounds(12, 252, 599, 35);
        contentPane.add(login_session_panel);
        login_session_panel.setLayout(null);

        loginPanel = new JPanel();
        loginPanel.setBounds(0, 0, 359, 35);
        login_session_panel.add(loginPanel);
        loginPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JLabel lblId = new JLabel("ID");
        lblId.setHorizontalAlignment(SwingConstants.LEFT);
        loginPanel.add(lblId);

        inputId = new JTextField();
        loginPanel.add(inputId);
        inputId.setPreferredSize(new Dimension(130, 30));
        inputId.setColumns(10);

        JLabel label = new JLabel("\uBE44\uBC00\uBC88\uD638");
        loginPanel.add(label);

        inputPwd = new JPasswordField();
        inputPwd.setPreferredSize(new Dimension(130, 30));
        loginPanel.add(inputPwd);

        //Logined Panel
        loginedPanel = new JPanel();
        loginedPanel.setBounds(0, 0, 599, 35);
        login_session_panel.add(loginedPanel);
        loginedPanel.setVisible(false);
        loginedPanel.setLayout(new BorderLayout(0, 0));

        loginedStatus = new JLabel();
        loginedPanel.add(loginedStatus, BorderLayout.WEST);

        JButton btn_logout = new JButton("\uB85C\uADF8\uC544\uC6C3");
        btn_logout.setPreferredSize(new Dimension(98, 20));
        btn_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                loginManager.request_logout(area.load_progressbar);
                if (!LoginManager.logined) {
                    loginPanel.setVisible(true);
                    loginBtnPanel.setVisible(true);
                    loginedPanel.setVisible(false);
                    loginedStatus.setText("");
                    btn_admin.setVisible(false);
                    LoginManager.logined_user = new User();
                    check_borrow_status(btnBorrow, btnReturn);
                }
            }
        });
        loginedPanel.add(btn_logout, BorderLayout.EAST);

        loginBtnPanel = new JPanel();
        loginBtnPanel.setBounds(164, 0, 435, 35);
        login_session_panel.add(loginBtnPanel);
        loginBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        loginBtnPanel.setVisible(true);

        JButton btn_login = new JButton("\uB85C\uADF8\uC778");
        loginBtnPanel.add(btn_login);
        btn_login.setPreferredSize(new Dimension(100, 30));
        btn_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // login
                loginManager.request_login(inputId.getText(), inputPwd.getText(), area.load_progressbar);
                if (LoginManager.logined) {
                    loginPanel.setVisible(false);
                    loginBtnPanel.setVisible(false);
                    loginedPanel.setVisible(true);
                    inputId.setText("");
                    inputPwd.setText("");
                    loginedStatus.setText(LoginManager.logined_user.name + "님 환영합니다.");
                    check_borrow_status(btnBorrow, btnReturn);

                    if (LoginManager.logined_user.isadmin == true) {
                        btn_admin.setVisible(true);
                    }
                }
            }
        });

        JButton btn_register = new JButton("\uD68C\uC6D0\uAC00\uC785");
        loginBtnPanel.add(btn_register);
        btn_register.setPreferredSize(new Dimension(100, 30));
        btn_register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignUp();
            }
        });

        check_borrow_status(btnBorrow, btnReturn);
    }

    @Override
    public void run() {
        dbInfo.public_ResultSet_SQL_Query(area.model, "select * from Books;", area.load_progressbar);
    }

    private void check_borrow_status(JButton btn_borrow, JButton btn_return) {
        if (!LoginManager.logined) {
            btn_borrow.setEnabled(false);
            btn_return.setEnabled(false);
        } else if (LoginManager.logined_user.status) {
            btn_borrow.setEnabled(true);
            btn_return.setEnabled(false);
        } else {
            btn_borrow.setEnabled(false);
            btn_return.setEnabled(true);
        }
    }

    private void refresh(DefaultTableModel model, JProgressBar progressBar) {
        model.setRowCount(0);
        dbInfo.public_ResultSet_SQL_Query(model, "select * from Books;", area.load_progressbar);
    }
}


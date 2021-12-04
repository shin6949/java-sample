package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import com.cocoblue.libraryapp.service.BookService;
import com.cocoblue.libraryapp.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main extends JFrame implements Runnable {
    // UI Component
    private static JPanel contentPane;
    private JTextField inputSearchBooks, inputId;
    private final Panel loginSessionPanel = new Panel();
    private JPasswordField inputPwd;
    private static JProgressBar loadProgressbar;
    static DefaultTableModel model;
    JButton btnBorrow, btnReturn;

    // Service
    private BookService bookService = new BookService();
    private UserService userService = new UserService();

    private static DB_info db = new DB_info();
    private static login_manager login = new login_manager();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    Thread loadDb = new Thread(frame,"DB 로딩");
                    loadDb.start();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() {
        // 도서 관리 시스템
        setTitle("\uB3C4\uC11C \uAD00\uB9AC \uC2DC\uC2A4\uD15C");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 637, 322);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(12, 10, 597, 23);
        contentPane.add(searchPanel);
        searchPanel.setLayout(new BorderLayout(0, 0));

        // 검색
        JButton btnSearch = new JButton("\uAC80\uC0C9");
        searchPanel.add(btnSearch, BorderLayout.EAST);

        // Define Search Bar
        inputSearchBooks = new JTextField();
        searchPanel.add(inputSearchBooks, BorderLayout.CENTER);

        inputSearchBooks.setToolTipText("\uAC80\uC0C9\uD560 \uB3C4\uC11C\uBA85\uC744 \uC785\uB825\uD558\uC138\uC694.");
        inputSearchBooks.setColumns(10);

        // Search Button Define
        btnSearch.setPreferredSize(new Dimension(98, 20));
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookService.searchBookByNameAndReflectUi(inputSearchBooks.getText(), model, loadProgressbar);
            }
        });

        Panel tablePanel = new Panel();
        tablePanel.setBounds(12, 39, 494, 194);
        contentPane.add(tablePanel);

        String[] title = {"ISBN", "이름", "저자", "상태", "위치"};
        model = new DefaultTableModel(title, 0) {
            public boolean isCellEditable(int rowIndex, int mColIndex) { // 수정 불가
                return false;
            }
        };

        tablePanel.setLayout(new BorderLayout(0, 0));

        JTable table = new JTable(model);
        table.setBounds(12, 78, 1, 1);
        table.getTableHeader().setReorderingAllowed(false); // 이동 불가
        // table.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
        tablePanel.add(new JScrollPane(table));

        JPanel toolBoxPanel = new JPanel();
        toolBoxPanel.setBounds(512, 39, 97, 194);
        contentPane.add(toolBoxPanel);
        toolBoxPanel.setLayout(new GridLayout(0, 1, 0, 0));

        // 대출
        btnBorrow = new JButton("\uB300\uCD9C");
        btnBorrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!String.valueOf(model.getValueAt(table.getSelectedRow(), 0)).equals("ISBN")) {
                    Book book = bookService.getBookByIsbn((String) model.getValueAt(table.getSelectedRow(), 0));

                    db.borrow(loadProgressbar, book, login_manager.logined_id);
                    refresh(model, loadProgressbar);
                    check_borrow_status(btnBorrow, btnReturn);
                }
            }
        });
        toolBoxPanel.add(btnBorrow);

        // 반납
        btnReturn = new JButton("\uCC45 \uBC18\uB0A9");
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                db.return_books(loadProgressbar, login_manager.logined_id);
                refresh(model, loadProgressbar);
                check_borrow_status(btnBorrow, btnReturn);
            }
        });

        check_borrow_status(btnBorrow, btnReturn);

        toolBoxPanel.add(btnReturn);

        JButton btn_detail = new JButton("\uC0C1\uC138 \uC815\uBCF4");
        toolBoxPanel.add(btn_detail);
        btn_detail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Book book = db.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(model.getValueAt(table.getSelectedRow(), 0)) + "'", loadProgressbar);
                new Detail(book);
            }
        });

        JButton btn_refresh = new JButton("\uCD08\uAE30\uD654\uBA74");
        toolBoxPanel.add(btn_refresh);

        JButton btn_admin = new JButton("\uAD00\uB9AC \uBAA8\uB4DC");
        btn_admin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new adminMode();
            }
        });
        toolBoxPanel.add(btn_admin);
        btn_admin.setVisible(false);

        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                refresh(model, loadProgressbar);
                inputSearchBooks.setText("");
            }
        });

        JLabel lblNewLabel_1 = new JLabel("\uCC45 \uB9AC\uC2A4\uD2B8");
        tablePanel.add(lblNewLabel_1, BorderLayout.NORTH);
        lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 20));
        //Login Panel end

        loadProgressbar = new JProgressBar(0, 100);
        tablePanel.add(loadProgressbar, BorderLayout.SOUTH);
        loadProgressbar.setStringPainted(true);

        loginSessionPanel.setBounds(10, 239, 599, 35);
        contentPane.add(loginSessionPanel);
        loginSessionPanel.setLayout(null);

        //Logined Panel
        JPanel logined_panel = new JPanel();
        logined_panel.setBounds(0, 0, 599, 35);
        loginSessionPanel.add(logined_panel);
        logined_panel.setVisible(false);
        logined_panel.setLayout(new BorderLayout(0, 0));

        JLabel logined_status = new JLabel("New label");
        logined_panel.add(logined_status, BorderLayout.WEST);

        //Logined Panel End

        //Login Panel
        JPanel login_panel = new JPanel();
        login_panel.setBounds(0, 0, 599, 35);
        loginSessionPanel.add(login_panel);
        login_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblId = new JLabel("ID");
        lblId.setHorizontalAlignment(SwingConstants.LEFT);
        login_panel.add(lblId);
        lblId.setFont(new Font("굴림", Font.PLAIN, 12));

        inputId = new JTextField();
        inputId.setPreferredSize(new Dimension(50, 20));
        login_panel.add(inputId);
        inputId.setToolTipText("");
        inputId.setColumns(10);

        JLabel label = new JLabel("\uBE44\uBC00\uBC88\uD638");
        login_panel.add(label);
        label.setFont(new Font("굴림", Font.PLAIN, 12));

        inputPwd = new JPasswordField();
        inputPwd.setPreferredSize(new Dimension(100, 20));
        login_panel.add(inputPwd);

        JButton btn_login = new JButton("\uB85C\uADF8\uC778");
        login_panel.add(btn_login);
        btn_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //login
                login.request_login(inputId.getText(), inputPwd.getText(), loadProgressbar);
                if(login_manager.logined == true ) {
                    login_panel.setVisible(false);
                    logined_panel.setVisible(true);
                    inputId.setText("");
                    inputPwd.setText("");
                    logined_status.setText(login_manager.logined_name + "님 안녕하세요.");
                    check_borrow_status(btnBorrow, btnReturn);

                    if(login_manager.logined_is_admin == true) {
                        btn_admin.setVisible(true);
                    }
                }
            }
        });
        btn_login.setFont(new Font("굴림", Font.PLAIN, 12));

        JButton btn_logout = new JButton("\uB85C\uADF8\uC544\uC6C3");
        btn_logout.setPreferredSize(new Dimension(98, 20));
        btn_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                login.request_logout(loadProgressbar);
                if(login_manager.logined == false)  {
                    login_panel.setVisible(true);
                    logined_panel.setVisible(false);
                    logined_status.setText("");
                    btn_admin.setVisible(false);
                    check_borrow_status(btnBorrow, btnReturn);
                }
            }
        });
        logined_panel.add(btn_logout, BorderLayout.EAST);

        JButton btn_register = new JButton("\uD68C\uC6D0\uAC00\uC785");
        btn_register.setFont(new Font("굴림", Font.PLAIN, 12));
        login_panel.add(btn_register);
        btn_register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Signup();
            }
        });
    }

    @Override
    public void run() {	db.public_ResultSet_SQL_Query(model, "select * from Books;", loadProgressbar); }

    private void check_borrow_status(JButton btn_borrow, JButton btn_return) {
        if(login_manager.logined == false) {
            btn_borrow.setEnabled(false);
            btn_return.setEnabled(false);
        }
        else if(login_manager.logined_borrow_status == true) {
            btn_borrow.setEnabled(true);
            btn_return.setEnabled(false);
        } else {
            btn_borrow.setEnabled(false);
            btn_return.setEnabled(true);
        }
    }

    private void refresh(DefaultTableModel model, JProgressBar progressBar) {
        model.setRowCount(0);
        db.public_ResultSet_SQL_Query(model, "select * from Books;", loadProgressbar);
    }
}
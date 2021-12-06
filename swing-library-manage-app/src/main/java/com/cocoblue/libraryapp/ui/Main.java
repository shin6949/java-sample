package com.cocoblue.libraryapp.ui;

import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.service.BookService;
import com.cocoblue.libraryapp.service.UiService;
import com.cocoblue.libraryapp.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static com.cocoblue.libraryapp.LibraryApp.CURRENT_USER;

public class Main extends JFrame implements Runnable {
    // UI Component
    private static JPanel contentPane;
    private final JTextField inputSearchBooks;
    private final JTextField inputId;
    private final JPasswordField inputPwd;
    private static JProgressBar loadProgressbar;
    private final DefaultTableModel model;
    private final JButton btnBorrow, btnReturn;

    // Service
    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();
    private final UiService uiService = new UiService();

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
                uiService.processProgressbar(loadProgressbar, 0);

                final List<Book> searchResult = bookService.searchBookByName(inputSearchBooks.getText());
                uiService.processProgressbar(loadProgressbar, 50);

                bookService.reflectDataOnModel(model, searchResult);
                uiService.processProgressbar(loadProgressbar, 100);
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
                // Header를 클릭했는지 확인
                if(!String.valueOf(model.getValueAt(table.getSelectedRow(), 0)).equals("ISBN")) {
                    final String selectedBookIsbn = (String) model.getValueAt(table.getSelectedRow(), 0);
                    final Book book = bookService.getBookByIsbn(selectedBookIsbn);

                    if(bookService.borrowBook(book, CURRENT_USER)) {
                        refreshTable(model);
                        checkBorrowStatus(btnBorrow, btnReturn);

                        JOptionPane.showMessageDialog(null,  "대출이 완료되었습니다.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,  "이 책은 대출이 불가능합니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        toolBoxPanel.add(btnBorrow);

        // 반납
        btnReturn = new JButton("\uCC45 \uBC18\uB0A9");
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookService.returnBook();
                refreshTable(model);
                checkBorrowStatus(btnBorrow, btnReturn);
            }
        });

        checkBorrowStatus(btnBorrow, btnReturn);

        toolBoxPanel.add(btnReturn);

        // 자세한 정보 보기 버튼
        JButton btnDetail = new JButton("\uC0C1\uC138 \uC815\uBCF4");
        toolBoxPanel.add(btnDetail);
        btnDetail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                final Book book = bookService.getBookByIsbn(String.valueOf(model.getValueAt(table.getSelectedRow(), 0)));
                new Detail(book);
            }
        });

        JButton btn_refresh = new JButton("\uCD08\uAE30\uD654\uBA74");
        toolBoxPanel.add(btn_refresh);

        JButton btn_admin = new JButton("\uAD00\uB9AC \uBAA8\uB4DC");
        btn_admin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new AdminMode();
            }
        });
        toolBoxPanel.add(btn_admin);
        btn_admin.setVisible(false);

        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                refreshTable(model);
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

        Panel loginSessionPanel = new Panel();
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
                // Login
                uiService.processProgressbar(loadProgressbar, 0);
                userService.login(inputId.getText(), inputPwd.getPassword());
                uiService.processProgressbar(loadProgressbar, 70);

                if(CURRENT_USER != null) {
                    login_panel.setVisible(false);
                    logined_panel.setVisible(true);
                    inputId.setText("");
                    inputPwd.setText("");
                    logined_status.setText(CURRENT_USER.getName() + "님 안녕하세요.");
                    checkBorrowStatus(btnBorrow, btnReturn);

                    if(CURRENT_USER.getIsAdmin()) {
                        btn_admin.setVisible(true);
                    }
                }
                uiService.processProgressbar(loadProgressbar, 100);
            }
        });
        btn_login.setFont(new Font("굴림", Font.PLAIN, 12));

        // 로그아웃
        JButton btn_logout = new JButton("\uB85C\uADF8\uC544\uC6C3");
        btn_logout.setPreferredSize(new Dimension(98, 20));
        btn_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                userService.loginOut();
                if(CURRENT_USER == null)  {
                    login_panel.setVisible(true);
                    logined_panel.setVisible(false);
                    logined_status.setText("");
                    btn_admin.setVisible(false);
                    checkBorrowStatus(btnBorrow, btnReturn);
                }
            }
        });
        logined_panel.add(btn_logout, BorderLayout.EAST);

        JButton btn_register = new JButton("\uD68C\uC6D0\uAC00\uC785");
        btn_register.setFont(new Font("굴림", Font.PLAIN, 12));
        login_panel.add(btn_register);
        btn_register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignUp();
            }
        });
    }

    @Override
    public void run() {
        uiService.processProgressbar(loadProgressbar, 0);
        final List<Book> allBooks = bookService.getAllBook();
        uiService.processProgressbar(loadProgressbar, 50);
        bookService.reflectDataOnModel(model, allBooks);
        uiService.processProgressbar(loadProgressbar, 100);
    }

    private void checkBorrowStatus(JButton btnBorrow, JButton btnReturn) {
        if(CURRENT_USER == null) {
            btnBorrow.setEnabled(false);
            btnReturn.setEnabled(false);
        }
        else if(CURRENT_USER.getBorrowedBook() == null) {
            btnBorrow.setEnabled(true);
            btnReturn.setEnabled(false);
        } else {
            btnBorrow.setEnabled(false);
            btnReturn.setEnabled(true);
        }
    }

    private void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Book> allBooks = bookService.getAllBook();
        bookService.reflectDataOnModel(model, allBooks);
    }
}
package com.cocoblue.libraryapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DBInfo {
    public static String DB_URL = "jdbc:mysql://52.231.153.175/Library";
    public static String DB_ID = "lhj";
    public static String DB_PW = "mysqltest1234!";
    public static boolean test = false;
    Connection conn;

    public void public_ResultSet_SQL_Query(DefaultTableModel model, String Query, JProgressBar ProgressBar) {
        try {
            ProgressBar.setVisible(true);
            make_conn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Query);
            ProgressBar.setValue(50);

            while (rs.next()) {
                String name = rs.getString("name");
                String author = rs.getString("author");
                Boolean isin = rs.getBoolean("isin");
                String location = rs.getString("location");
                String status;

                if (isin) {
                    status = "���� ����";
                } else {
                    status = "���� �Ұ�";
                }

                String[] list = {name, author, status, location};
                model.addRow(list);
            }
            ProgressBar.setValue(70);

            close_conn(null, rs, stmt);

            ProgressBar.setValue(100);
            ProgressBar.setVisible(false);

        } catch (Exception e1) {
            ProgressBar.setValue(0);
            e1.printStackTrace();
        }
    }

    public String id_return(String Query, JProgressBar ProgressBar) {
        String id = null;
        try {
            ProgressBar.setVisible(true);
            make_conn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Query);
            ProgressBar.setValue(50);

            if (rs.next()) {
                id = rs.getString("id");
            }

            ProgressBar.setValue(70);
            close_conn(null, rs, stmt);

            ProgressBar.setValue(100);
            ProgressBar.setVisible(false);

        } catch (Exception e1) {
            ProgressBar.setValue(0);
            e1.printStackTrace();
        }
        return id;
    }

    public Book load_book(String Query, JProgressBar ProgressBar) {
        Book book = new Book();

        try {
            process_progressbar(ProgressBar, 0);
            make_conn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Query);
            process_progressbar(ProgressBar, 50);

            if (rs.next()) {
                book.ISBN = rs.getInt("ISBN");
                book.name = rs.getString("name");
                book.author = rs.getString("author");
                book.status = rs.getBoolean("isin");
                book.location = rs.getString("location");
                book.borrowed_id = rs.getString("borrow_id");
                book.borrow_count = rs.getInt("borrow_count");
            }

            process_progressbar(ProgressBar, 70);

            close_conn(null, rs, stmt);
            process_progressbar(ProgressBar, 100);
        } catch (Exception e1) {
            process_progressbar(ProgressBar, 100);
            System.out.println(e1 + "\n");
            e1.printStackTrace();
        }
        return book;
    }

    public User load_User(String id, JProgressBar ProgressBar) {
        User user = new User();
        Book book = new Book();

        try {
            process_progressbar(ProgressBar, 0);

            make_conn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE id = '" + id + "'");


            process_progressbar(ProgressBar, 50);

            if (rs.next()) {
                user.borrow_ISBN = 1;
                user.name = rs.getString("name");
                user.status = rs.getBoolean("status");
                user.id = rs.getString("id");
                user.birth = rs.getDate("birth");
                user.isadmin = rs.getBoolean("isadmin");
                user.borrow_ISBN = rs.getInt("borrow_ISBN");
                user.borrow_count = rs.getInt("borrow_count");

                if (user.borrow_ISBN != 1) {
                    book = load_book("SELECT * FROM Books WHERE ISBN = " + user.borrow_ISBN + "", null);
                    user.borrow_name = book.name;
                }
            }

            process_progressbar(ProgressBar, 70);

            close_conn(null, rs, stmt);
            process_progressbar(ProgressBar, 100);

        } catch (Exception e1) {
            process_progressbar(ProgressBar, 100);
            e1.printStackTrace();
        }
        return user;
    }

    void process_progressbar(JProgressBar progressBar, int value) {
        switch (value) {
            case 0:
                if (progressBar != null) {
                    progressBar.setVisible(true);
                    progressBar.setValue(0);
                }
                break;

            case 100:
                if (progressBar != null) {
                    progressBar.setValue(100);
                    progressBar.setVisible(false);
                }
                break;

            default:
                if (progressBar != null) {
                    progressBar.setValue(value);
                }
                break;
        }
    }

    public void make_conn() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PW);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void close_conn(PreparedStatement pstmt, ResultSet rs, Statement stmt) {
        try {
            conn.close();
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            ;

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}


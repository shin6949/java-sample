package com.cocoblue.libraryapp.service;

import com.cocoblue.libraryapp.config.DBInfo;
import com.cocoblue.libraryapp.config.HibernateConfig;
import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.lang.reflect.Member;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.cocoblue.libraryapp.config.HibernateConfig.sessionFactory;


@NoArgsConstructor
public class BookService {
    private final SessionFactory factory = HibernateConfig.getSessionFactory();

    public String convertIsbn(int value) {
        return Integer.toString(value);
    }

    public String convertStatus(Boolean status) {
        return status ? "대출 가능" : "대출 불가";
    }

    public Book getBookByIsbnUsingHibernate(int isbn) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        // 한 개 불러오기
        return (Book) session.get(Book.class, isbn);
    }

    public Book getBookFromDb(String Query, JProgressBar ProgressBar) {
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

    public Boolean borrowBook(User user, JProgressBar progressBar) {
        if (status) { //å�� ���� ������ �����̸�...
            try {
                process_progressbar(progressBar, 0);

                Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
                System.out.print("���� ����\n");
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Books SET isin = false, borrow_id = ?, borrow_count = ? WHERE ISBN = ?");
                pstmt.setString(1, user.id);
                pstmt.setInt(2, borrow_count + 1);
                pstmt.setInt(3, ISBN);
                System.out.print("Books ������Ʈ �õ�\n");
                pstmt.executeUpdate();
                process_progressbar(progressBar, 50);

                pstmt = conn.prepareStatement("UPDATE Users SET status = false, borrow_ISBN = ?, borrow_count = ? WHERE id = ?");
                pstmt.setInt(1, ISBN);
                pstmt.setInt(2, user.borrow_count + 1);
                pstmt.setString(3, user.id);
                System.out.print("Users ������Ʈ �õ�\n");
                pstmt.executeUpdate();
                process_progressbar(progressBar, 70);


                SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String test2 = test.format(new Date());
                java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);

                pstmt = conn.prepareStatement("INSERT INTO log VALUES (?, ?, ?, ?, ?)");
                pstmt.setInt(1, ISBN);
                pstmt.setTimestamp(2, now_date);
                pstmt.setString(3, user.id);
                pstmt.setString(4, "Borrow");
                pstmt.setString(5, name);
                pstmt.executeUpdate();
                process_progressbar(progressBar, 80);

                user.status = false;
                user.borrow_ISBN = ISBN;
                user.borrow_count = user.borrow_count + 1;

                pstmt.close();
                conn.close();

                process_progressbar(progressBar, 100);

                JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);

                return true;
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "������ ������ ���°� �ƴմϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
                process_progressbar(progressBar, 100);
                return false;
            }
        } else {
            process_progressbar(progressBar, 100);
            return false;
        }
    }

    public Boolean return_book(JProgressBar progressBar) {
        DBInfo dbInfo = new DBInfo();
        try {
            process_progressbar(progressBar, 0);
            Connection conn = DriverManager.getConnection(DBInfo.DB_URL, DBInfo.DB_ID, DBInfo.DB_PW);
            Book book = dbInfo.load_book("SELECT * FROM Books WHERE ISBN = " + borrow_ISBN, null);

            PreparedStatement pstmt = conn.prepareStatement("UPDATE Books SET isin = true, borrow_id = NULL WHERE ISBN = ?");
            pstmt.setInt(1, borrow_ISBN);
            pstmt.executeUpdate();
            process_progressbar(progressBar, 50);

            pstmt = conn.prepareStatement("UPDATE Users SET status = true, borrow_ISBN = NULL WHERE id = ?");
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            process_progressbar(progressBar, 70);

            SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String test2 = test.format(new Date());
            java.sql.Timestamp now_date = java.sql.Timestamp.valueOf(test2);

            pstmt = conn.prepareStatement("INSERT INTO log VALUES (?, ?, ?, ?, ?)");
            pstmt.setInt(1, book.ISBN);
            pstmt.setTimestamp(2, now_date);
            pstmt.setString(3, id);
            pstmt.setString(4, "Return");
            pstmt.setString(5, book.name);
            System.out.printf("log ���� �õ�\n");
            pstmt.executeUpdate();
            process_progressbar(progressBar, 80);

            JOptionPane.showMessageDialog(null, "�ݳ� ó���� �Ϸ�Ǿ����ϴ�.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            status = true;
            borrow_ISBN = 0;
            process_progressbar(progressBar, 90);

            conn.close();

            process_progressbar(progressBar, 100);

            return true;
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "�� å�� �ݳ��� �Ұ����մϴ�.", "ERROR", JOptionPane.ERROR_MESSAGE);
            process_progressbar(progressBar, 100);
            return false;
        }
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
}

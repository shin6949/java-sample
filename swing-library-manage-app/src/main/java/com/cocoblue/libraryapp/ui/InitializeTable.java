package com.cocoblue.libraryapp.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitializeTable extends JFrame {
    private DBInfo dbInfo = new DBInfo();
    DefaultTableModel model;
    JProgressBar load_progressbar;
    JTable table;
    JTextField input_searchBooks;

    public InitializeTable(JPanel contentPane) {
        JPanel search_panel = new JPanel();
        search_panel.setBounds(12, 10, 597, 35);
        contentPane.add(search_panel);
        search_panel.setLayout(null);

        Panel table_panel = new Panel();
        table_panel.setBounds(12, 52, 494, 194);
        contentPane.add(table_panel);

        load_progressbar = new JProgressBar(0, 100);
        table_panel.add(load_progressbar, BorderLayout.SOUTH);
        load_progressbar.setStringPainted(true);

        String title[] = {"ISBN", "이름", "저자", "상태", "위치"};
        model = new DefaultTableModel(title, 0) {
            public boolean isCellEditable(int rowIndex, int mColIndex) { //���� �Ұ�
                return false;
            }
        };

        String list[] = {"�̸�", "ISBN", "����"};
        JComboBox comboBox = new JComboBox(list);
        comboBox.setBounds(0, 0, 85, 35);
        search_panel.add(comboBox);

        input_searchBooks = new JTextField();
        input_searchBooks.setBounds(88, 0, 408, 35);
        search_panel.add(input_searchBooks);
        input_searchBooks.setToolTipText("\uAC80\uC0C9\uD560 \uB3C4\uC11C\uBA85\uC744 \uC785\uB825\uD558\uC138\uC694.");
        input_searchBooks.setColumns(10);

        JButton btn_search = new JButton("\uAC80\uC0C9");
        btn_search.setBounds(497, 0, 100, 35);
        search_panel.add(btn_search);
        btn_search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                switch (comboBox.getSelectedItem().toString()) {
                    case "�̸�":
                        dbInfo.public_ResultSet_SQL_Query(model, "SELECT * FROM Books WHERE name LIKE '" + input_searchBooks.getText() + "%'", load_progressbar);
                        break;
                    case "ISBN":
                        dbInfo.public_ResultSet_SQL_Query(model, "SELECT * FROM Books WHERE ISBN LIKE '" + input_searchBooks.getText() + "%'", load_progressbar);
                        break;
                    case "����":
                        dbInfo.public_ResultSet_SQL_Query(model, "SELECT * FROM Books WHERE author LIKE '" + input_searchBooks.getText() + "%'", load_progressbar);
                        break;
                }
            }
        });

        table_panel.setLayout(new BorderLayout(0, 0));

        table = new JTable(model);
        table.setBounds(12, 78, 1, 1);
        table.getTableHeader().setReorderingAllowed(false); //�̵� �Ұ�
        table_panel.add(new JScrollPane(table));

        JLabel lblNewLabel_1 = new JLabel("\uCC45 \uB9AC\uC2A4\uD2B8");
        table_panel.add(lblNewLabel_1, BorderLayout.NORTH);
    }
}

package mokpoLibrary_v2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JMonthChooser;

public class Book_Statistics extends JFrame {
	
	JLabel label_mode;
	JProgressBar load_progressbar;
	JDateChooser date_from, date_to;
	DefaultTableModel model;
	JTable table;
	private DB_info db = new DB_info();
	private JPanel contentPane;
	
	public Book_Statistics() {
		setBounds(100, 100, 569, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 5, 543, 35);
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		date_from = new JDateChooser();
		date_from.setPreferredSize(new Dimension(100, 30));
		panel.add(date_from);
		
		JLabel lblNewLabel = new JLabel("~");
		panel.add(lblNewLabel);
		
		date_to = new JDateChooser();
		date_to.setPreferredSize(new Dimension(100, 30));
		panel.add(date_to);
		
		JLabel lblNewLabel_1 = new JLabel("\uAE4C\uC9C0\uC758 \uB300\uCD9C \uD1B5\uACC4");
		panel.add(lblNewLabel_1);
		
		JButton btn_viewStat = new JButton("\uBCF4\uAE30");
		btn_viewStat.setPreferredSize(new Dimension(80, 30));
		btn_viewStat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat format_from = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format_to = new SimpleDateFormat("yyyy-MM-dd");
				String from = format_from.format(date_from.getDate());
				String to = format_to.format(date_to.getDate());
				
				label_mode.setText("날짜 지정 보기 모드");
				
				load_table(model, "SELECT ISBN, action_name, COUNT(*) FROM log WHERE ISBN > 0 AND action_type = 'Borrow' AND action_date between date('" + from + "') and date('"+ to +"') + 1 GROUP BY ISBN, action_name HAVING COUNT(*) > 0 ORDER BY 3 DESC", load_progressbar);
			}
		});
		panel.add(btn_viewStat);
		
		JButton btn_reset = new JButton("\uCD08\uAE30\uD654");
		btn_reset.setPreferredSize(new Dimension(80, 30));
		btn_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				date_from = new JDateChooser();
				date_to = new JDateChooser();
				
				label_mode.setText("초기화 모드");
				
				load_table(model, "SELECT ISBN, action_name, COUNT(*) FROM log WHERE ISBN > 0 AND action_type = 'Borrow' GROUP BY ISBN, action_name HAVING COUNT(*) > 0 ORDER BY 3 DESC", load_progressbar);
			}
		});
		panel.add(btn_reset);
		
		Panel table_panel = new Panel();
		table_panel.setBounds(5, 50, 543, 182);
		contentPane.add(table_panel);
		
		load_progressbar = new JProgressBar(0, 100);
		table_panel.add(load_progressbar, BorderLayout.SOUTH);
		load_progressbar.setStringPainted(true);
		
		String title[]= {"ISBN", "이름", "대출 횟수"};
		model = new DefaultTableModel(title, 0) {
			public boolean isCellEditable(int rowIndex, int mColIndex) { //수정 불가
	            return false;
				}
			}; 
		
		String list[] = {"이름", "ISBN", "저자"};
		
		table_panel.setLayout(new BorderLayout(0, 0));
		
		table = new JTable(model);
		table.setBounds(12, 78, 1, 1);
		table.getTableHeader().setReorderingAllowed(false); //이동 불가
		JScrollPane scrollPane = new JScrollPane(table);
		table_panel.add(scrollPane);
		
		this.setVisible(true);
		load_table(model, "SELECT ISBN, action_name, COUNT(*) FROM log WHERE ISBN > 0 AND action_type = 'Borrow' GROUP BY ISBN, action_name HAVING COUNT(*) > 0 ORDER BY 3 DESC", load_progressbar);
		
		JPanel toolbar_panel = new JPanel();
		toolbar_panel.setBounds(5, 238, 329, 40);
		contentPane.add(toolbar_panel);
		toolbar_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btn_graph = new JButton("\uADF8\uB798\uD504 \uD1B5\uACC4");
		btn_graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
				
				for(int i = 0; i < table.getRowCount(); i++) {
					barDataset.setValue(Integer.parseInt(String.valueOf(model.getValueAt(i, 2))), "Book", String.valueOf(model.getValueAt(i, 1)));
				}
				
				JFreeChart chart = ChartFactory.createBarChart("전체 기간의 인기 책", "ID", "대출 횟수", barDataset, PlotOrientation.HORIZONTAL, false, true, false);;
				
				if(label_mode.getText().equals("날짜 지정 보기 모드")) { 
				SimpleDateFormat format_from = new SimpleDateFormat("yyyy년 M월 d일");
				SimpleDateFormat format_to = new SimpleDateFormat("yyyy년 M월 d일");
				String from = format_from.format(date_from.getDate());
				String to = format_to.format(date_to.getDate());
				
				chart = ChartFactory.createBarChart(from + "부터 " + to + "까지의 인기 책", "이름", "대출 횟수", barDataset, PlotOrientation.HORIZONTAL, false, true, false); }

				ChartFrame chartFrame = new ChartFrame("통계", chart);
				chartFrame.setVisible(true);
				chartFrame.setSize(720, 500);
				    
				CategoryPlot p = chart.getCategoryPlot();
				chart.getTitle().setFont(new Font("돋움", Font.BOLD, 20));
				p.getDomainAxis().setLabelFont(new Font("돋움", Font.BOLD, 20));
				p.getDomainAxis().setTickLabelFont(new Font("돋움", Font.BOLD, 13));
				p.getRangeAxis().setLabelFont(new Font("돋움", Font.BOLD, 13));
				p.getRangeAxis().setTickLabelFont(new Font("돋움", Font.BOLD, 13));
			}
		});
		btn_graph.setPreferredSize(new Dimension(130, 35));
		toolbar_panel.add(btn_graph);
		
		JButton btn_detail = new JButton("\uC0C1\uC138 \uC815\uBCF4");
		btn_detail.setPreferredSize(new Dimension(100, 35));
		btn_detail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Book book = db.load_book("SELECT * FROM Books WHERE name = '" + String.valueOf(model.getValueAt(table.getSelectedRow(), 1)) + "'", load_progressbar);
				new Detail(book);
			}
		});
		toolbar_panel.add(btn_detail);
		
		JPanel mode_panel = new JPanel();
		mode_panel.setBounds(346, 253, 202, 25);
		contentPane.add(mode_panel);
		mode_panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		label_mode = new JLabel("\uCD08\uAE30\uD654 \uBAA8\uB4DC");
		mode_panel.add(label_mode);
	}
	
	public void load_table(DefaultTableModel model, String Query, JProgressBar ProgressBar) {
	try {
		ProgressBar.setVisible(true);
		Connection conn = DriverManager.getConnection(DB_info.DB_URL, DB_info.DB_ID, DB_info.DB_PW);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		ProgressBar.setValue(50);
		model.setRowCount(0);
		
		while(rs.next()){
			int ISBN = rs.getInt("ISBN");
			String name = rs.getString("action_name");
			//int borrow_count = rs.getInt("borrow_count");
			int count = rs.getInt(3);

			String []list = {Integer.toString(ISBN), name, Integer.toString(count)};
			model.addRow(list);
	   }
	   ProgressBar.setValue(70);

	   stmt.close();
	   conn.close();
	   rs.close();
	   
	   ProgressBar.setValue(100);
	   ProgressBar.setVisible(false);

	} catch (Exception e1) {
		System.out.printf(e1 + "\n");
		ProgressBar.setValue(0);
		e1.printStackTrace();
	  }
	}
}

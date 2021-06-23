package databasePackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class gameLog extends JDialog {

	private JPanel contentPanel;
	private JTable gameTable;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private String url = "jdbc:sqlite:/Users/eshaangovil/Desktop/Programming/Libraries/sqlite_databases/CricketChampionship.db";
	private Vector row;

	public gameLog(StandingsDisplay sd) {
		initializeGUI(sd);
	}

	void initializeGUI(StandingsDisplay sd) {
		setBounds(100, 100, 630, 309);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(sd);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		{
			JButton okButton = new JButton("OK");
			okButton.setBounds(549, 246, 75, 29);
			getContentPane().add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sd.setVisible(true);
					dispose();
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}

		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 40, 618, 204);

		gameTable = new JTable();
		gameTable.setColumnSelectionAllowed(true);
		gameTable.setBounds(6, 40, 618, 204);
		scrollPane.setViewportView(gameTable);
		contentPanel.add(scrollPane);

		model = new DefaultTableModel()
				// make cells in table non-editable
				{
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
				};

				model.addColumn("Date");
				model.addColumn("Home");
				model.addColumn("Away");
				model.addColumn("Winner");
				model.addColumn("Format");
				model.addColumn("gameId");
				
				

				gameTable.setModel(model);


				JLabel lblNewLabel = new JLabel("Game Log");
				lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setBounds(255, 6, 120, 27);
				contentPanel.add(lblNewLabel);

				putGamesInTable();
				
				gameTable.getColumnModel().getColumn(5).setPreferredWidth(0);
				gameTable.getColumnModel().getColumn(5).setMinWidth(0);
				gameTable.getColumnModel().getColumn(5).setMaxWidth(0);
	}

	void putGamesInTable(){
		Connection conn;

		try {
			// open a database connection
			conn = DriverManager.getConnection(url);

			// do the select
			String sql = "SELECT * FROM game";
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// put the items in the JTable
			model.setRowCount(0);

			while(rs.next()){
				row = new Vector();
				row.add(rs.getString("date"));
				row.add(convertIdToTeamName(rs.getString("homeId")));
				row.add(convertIdToTeamName(rs.getString("awayId")));
				row.add(convertIdToTeamName(rs.getString("winnerId")));
				row.add(rs.getString("format"));
				row.add(rs.getString("gameId"));
				model.addRow(row);
			}

			// close the connection
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String convertIdToTeamName(String teamId) {

		String returnString = "";

		if (teamId.equals("0")) {
			returnString = "Tie/Draw";
		} else {
			Connection conn;

			try {
				// open a database connection
				conn = DriverManager.getConnection(url);

				// do the select
				String sql = "SELECT name FROM team WHERE teamId=" + teamId + ";";
				java.sql.Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				// put the name in string
				returnString = rs.getString("name");

				// close the connection
				stmt.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}
}

package databasePackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TeamDetails extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tblPlayers;
	private DefaultTableModel model;
	private Vector row;
	private String url = "jdbc:sqlite:/Users/eshaangovil/Desktop/Programming/Libraries/sqlite_databases/CricketChampionship.db";
	private JLabel lblTeam;
	private JScrollPane scrollPane;

	public TeamDetails(StandingsDisplay sd, int teamId) {
		initializeGUI(sd,teamId);

	}

	void initializeGUI(StandingsDisplay sd, int teamId) {

		setBounds(100, 100, 572, 336);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 450, 233);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		setAlwaysOnTop(true);

		lblTeam = new JLabel("New label");
		lblTeam.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblTeam.setText("<Team Name>");
		lblTeam.setBounds(25, 6, 180, 25);
		contentPanel.add(lblTeam);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 43, 425, 218);
		tblPlayers = new JTable();
		tblPlayers.setBounds(25, 43, 425, 218);
		scrollPane.setViewportView(tblPlayers);
		contentPanel.add(scrollPane);

		model = new DefaultTableModel()
				// make cells in table non-editable
				{
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
				};

				model.addColumn("Name");
				model.addColumn("Age");
				model.addColumn("Role");
				model.addColumn("Runs");
				model.addColumn("Wickets");
				model.addColumn("Bowling");
				model.addColumn("Batting");
				model.addColumn("playerId");

				tblPlayers.setModel(model);
				
				
				//width = 425
				tblPlayers.getColumnModel().getColumn(0).setPreferredWidth(100);
				tblPlayers.getColumnModel().getColumn(1).setPreferredWidth(31);
				tblPlayers.getColumnModel().getColumn(2).setPreferredWidth(60);
				tblPlayers.getColumnModel().getColumn(3).setPreferredWidth(60);
				tblPlayers.getColumnModel().getColumn(4).setPreferredWidth(60);
				tblPlayers.getColumnModel().getColumn(5).setPreferredWidth(57);
				tblPlayers.getColumnModel().getColumn(6).setPreferredWidth(57);
				
				//set playerId column to zero width ie. invisible
				tblPlayers.getColumnModel().getColumn(7).setPreferredWidth(0);
				tblPlayers.getColumnModel().getColumn(7).setMaxWidth(0);
				tblPlayers.getColumnModel().getColumn(7).setMinWidth(0);
				
				
				JTableHeader header= tblPlayers.getTableHeader();

				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				header.setDefaultRenderer(centerRenderer);
				tblPlayers.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
				tblPlayers.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
				tblPlayers.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
				tblPlayers.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
				tblPlayers.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
				tblPlayers.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
				
				
				{
					JPanel buttonPane = new JPanel();
					buttonPane.setBounds(0, 269, 572, 39);
					buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
					getContentPane().add(buttonPane);
					{
						JButton okButton = new JButton("OK");
						okButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								sd.setVisible(true);
								dispose();
							}
						});
						okButton.setActionCommand("OK");
						buttonPane.add(okButton);
						getRootPane().setDefaultButton(okButton);
					}
					{
						JButton cancelButton = new JButton("Cancel");
						cancelButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								sd.setVisible(true);
								dispose();
							}
						});
						cancelButton.setActionCommand("Cancel");
						buttonPane.add(cancelButton);
					}
				}

				JButton btnAddPlayer = new JButton("Add Player");
				btnAddPlayer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
				btnAddPlayer.setBounds(449, 42, 117, 29);
				getContentPane().add(btnAddPlayer);

				JButton btnDeletePlayer = new JButton("Delete Player");
				btnDeletePlayer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deletePlayer(teamId);
					}
				});
				btnDeletePlayer.setBounds(449, 126, 117, 29);
				getContentPane().add(btnDeletePlayer);

				putPlayersInTable(teamId);
	}

	void putPlayersInTable(int teamId) {

		Connection conn;
		//Connection conn2;
		
		try {
			// open a database connection
			conn = DriverManager.getConnection(url);

			// do the select
			String sql = "SELECT * FROM player WHERE teamId="+teamId+";";
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// put the items in the JTable
			model.setRowCount(0);

			while(rs.next()){
				row = new Vector();
				row.add(rs.getString("name"));
				row.add(rs.getString("age"));
				row.add(rs.getString("role"));
				row.add(rs.getString("runs"));
				row.add(rs.getString("wickets"));
				row.add(rs.getString("battingHand"));
				row.add(rs.getString("bowlingHand"));
				row.add(rs.getString("playerId"));
				model.addRow(row);
			}

			// close the connection
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setTeamLabel(teamId);
	}
	
	void setTeamLabel(int teamId){
		Connection conn;
		
		try {
			// open a database connection
			conn = DriverManager.getConnection(url);
			
			// select team
			String sql = "SELECT name FROM team WHERE teamId="+teamId+";";
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			// set label text
			lblTeam.setText(rs.getString("name"));
			
			// close the connection
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void deletePlayer(int teamId) {
		if (tblPlayers.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(contentPanel, "Please select a player to delete.");
		} else if (tblPlayers.getSelectedRowCount() >1) {
			JOptionPane.showMessageDialog(contentPanel,"Please delete one player at a time.");
		} else {

			JFrame jf = new JFrame();
			jf.setAlwaysOnTop(true);
			int result = JOptionPane.showConfirmDialog(jf, "Press OK to confirm", "Confirmation", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < model.getRowCount(); i++) {
					if (tblPlayers.isRowSelected(i)) {
						int playerId = Integer.parseInt(model.getValueAt(i, 7).toString());
						deletePlayerFromSQL(playerId);
						putPlayersInTable(teamId);
					}
				}
			} else if (result == JOptionPane.NO_OPTION){
				dispose();
			}
		}

	}
	
	void deletePlayerFromSQL(int playerId) {
		Connection conn;

		try {
			// open a database connection
			conn = DriverManager.getConnection(url);

			// do the select
			String sql = "DELETE FROM player WHERE playerId="+ playerId +";";
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			// close the connection
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

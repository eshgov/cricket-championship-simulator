package databasePackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.w3c.dom.css.Counter;

import javax.swing.JLabel;
import java.awt.Font;

public class StandingsDisplay extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable tblTeams;
	private DefaultTableModel model;
	private Vector row;
	private String url = "jdbc:sqlite:/Users/eshaangovil/Desktop/Programming/Libraries/sqlite_databases/CricketChampionship.db";
	private StandingsDisplay sd = this;
	private Connection conn;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StandingsDisplay frame = new StandingsDisplay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StandingsDisplay() {
		// Load the database driver
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initializeGUI();
		
	}

	void initializeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 621, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 47, 439, 207);

		tblTeams = new JTable();
		tblTeams.setBounds(44, 57, 243, 186);
		scrollPane.setViewportView(tblTeams);
		contentPane.add(scrollPane);

		model = new DefaultTableModel()
				// make cells in table non-editable
				{
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
				};



				model.addColumn("Ranking");
				model.addColumn("Team");
				model.addColumn("G");
				model.addColumn("W");
				model.addColumn("L");
				model.addColumn("T/D");
				model.addColumn("Points");
				model.addColumn("teamId");

				tblTeams.setModel(model);
				//width = 152

				//TableColumnModel columnModel = tblTeams.getColumnModel();
/*
				tblTeams.getColumnModel().getColumn(0).setPreferredWidth(30);
				tblTeams.getColumnModel().getColumn(1).setPreferredWidth(85);
				tblTeams.getColumnModel().getColumn(2).setPreferredWidth(37);*/
				tblTeams.getColumnModel().getColumn(7).setPreferredWidth(0);
				tblTeams.getColumnModel().getColumn(7).setMinWidth(0);
				tblTeams.getColumnModel().getColumn(7).setMaxWidth(0);

				JTableHeader header= tblTeams.getTableHeader();

				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				header.setDefaultRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
				tblTeams.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

				putTeamsInTable();

				/*
				JButton btnNewButton = new JButton("Select Teams");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						putTeamsInTable();
					}
				});
				btnNewButton.setBounds(299, 54, 126, 29);
				contentPane.add(btnNewButton);
				 */
/*
				JButton btnTeamDetails = new JButton("Team Details");
				btnTeamDetails.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editTeam();
					}
				});
				btnTeamDetails.setBounds(299, 57, 117, 29);
				contentPane.add(btnTeamDetails);*/

				JLabel lblNewLabel = new JLabel("Cricket Championship");
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
				lblNewLabel.setBounds(89, 6, 268, 29);
				contentPane.add(lblNewLabel);

				JButton btnAddTeam = new JButton("Add Team");
				btnAddTeam.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addTeam at = new addTeam(sd);
						at.setVisible(true);
					}
				});
				btnAddTeam.setBounds(487, 43, 117, 29);
				contentPane.add(btnAddTeam);

				JButton btnDeleteTeam = new JButton("Delete Team");
				btnDeleteTeam.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deleteTeam();
					}
				});
				btnDeleteTeam.setBounds(487, 100, 117, 29);
				contentPane.add(btnDeleteTeam);

				JButton btnGame = new JButton("New Game");
				btnGame.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (model.getRowCount() > 1) {
							gameGUI gameGUI = new gameGUI(sd);
							gameGUI.setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(contentPane, "There must be 2 teams to play a game.");
						}
					}
				});
				btnGame.setBounds(487, 166, 117, 29);
				contentPane.add(btnGame);
				
				JButton btnGameLog = new JButton("Game Log");
				btnGameLog.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gameLog gl = new gameLog(sd);
						gl.setVisible(true);
					}
				});
				btnGameLog.setBounds(487, 219, 117, 29);
				contentPane.add(btnGameLog);
	}

	void putTeamsInTable(){

		try {
			// do the select
			String sql = "SELECT * FROM team ORDER BY points DESC";
			System.out.println(sql);
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// put the items in the JTable
			model.setRowCount(0);

			int counter = 1;

			while(rs.next()){
				row = new Vector();
				row.add(counter);
				row.add(rs.getString("name"));
				ArrayList<Integer> teamInfo = updateInfo(rs.getString("teamId"));
				row.add(teamInfo.get(0)); // games
				row.add(teamInfo.get(1)); // wins
				row.add(teamInfo.get(2)); // losses
				row.add(teamInfo.get(3)); // t/d
				row.add(teamInfo.get(4)); // points
				row.add(rs.getString("teamId"));
				model.addRow(row);
				counter++;
			}

			// close the connection
			stmt.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		//updateRankings();
	}

	void editTeam() {
		if (tblTeams.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(contentPane, "Please select a team to view.");
		} else if (tblTeams.getSelectedRowCount() > 1){
			JOptionPane.showMessageDialog(contentPane, "Please select only one team to view.");
		} else {
			for (int i = 0; i < model.getRowCount(); i++) {
				if (tblTeams.isRowSelected(i)) {
					int teamId = Integer.parseInt(model.getValueAt(i, 7).toString());
					System.out.println(teamId);
					TeamDetails td = new TeamDetails(this, teamId);
					td.setVisible(true);
					dispose();
				}
			}
		}
	}

	void deleteTeam() {
		if (tblTeams.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(contentPane, "Please select a team to delete.");
		} else if (tblTeams.getSelectedRowCount() >1) {
			JOptionPane.showMessageDialog(contentPane,"Please delete one team at a time.");
		} else {

			JFrame jf = new JFrame();
			jf.setAlwaysOnTop(true);
			int result = JOptionPane.showConfirmDialog(jf, "Please note that all matches containing this team will also be deleted.\nPress OK to confirm", "Confirmation", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < model.getRowCount(); i++) {
					if (tblTeams.isRowSelected(i)) {
						int teamId = Integer.parseInt(model.getValueAt(i, 7).toString());
						deleteTeamFromSQL(teamId);
						putTeamsInTable();
					}
				}
			} else if (result == JOptionPane.NO_OPTION){
				dispose();
			}
		}

	}

	void deleteTeamFromSQL(int teamId) {
		
		try {
		

			// do the select
			String sql = "DELETE FROM team WHERE teamId="+ teamId +";"
					+ "DELETE FROM game WHERE (homeId="+ teamId +" OR awayId="+teamId+");";
			System.out.println(sql);
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			// close the connection
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ArrayList<Integer> updateInfo(String teamId) {
	
		int gameCounter = 0;
		int winCounter = 0;
		int lossCounter = 0;
		int tdCounter = 0;
		
		try {
			// do the select
			String sql = "SELECT winnerId FROM game WHERE (homeId="+teamId+" OR awayId="+teamId+");";
			System.out.println(sql);
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// count wins and losses

			while(rs.next()){
				gameCounter++;
				if (rs.getString("winnerId").equals(teamId)) {
					winCounter++;
				} else if (rs.getString("winnerId").equals("0")) {
					tdCounter++;
				} else {
					lossCounter++;
				}
			}

			// close the connection
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> teamInfo = new ArrayList<Integer>();
		teamInfo.add(gameCounter);
		teamInfo.add(winCounter);
		teamInfo.add(lossCounter);
		teamInfo.add(tdCounter);
		teamInfo.add((2*winCounter)+tdCounter);
		
		updateSQL(teamInfo, teamId);
		
		return teamInfo;
		
	}
	
	void updateSQL(ArrayList<Integer> teamInfo, String teamId) {
		try {
			// do the select
			String sql = "UPDATE team SET G="+teamInfo.get(0)+", W="+teamInfo.get(1)+", L="+teamInfo.get(2)+", TD="+teamInfo.get(3)+", points="+teamInfo.get(4)+" "
					+ "WHERE teamId="+teamId+";";
			System.out.println(sql);
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			// close the connection
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

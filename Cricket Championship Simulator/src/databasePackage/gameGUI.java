package databasePackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.List;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class gameGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox cmbxHome;
	private JComboBox cmbxAway;
	private JComboBox cmbxFormat;
	private JComboBox cmbxWinner;
	private String url = "jdbc:sqlite:/Users/eshaangovil/Desktop/Programming/Libraries/sqlite_databases/CricketChampionship.db";
	private JTextField txtDate;
	private gameGUI gameGUI = this;

	public gameGUI(StandingsDisplay sd) {
		initializeGUI(sd);
	}

	void initializeGUI(StandingsDisplay sd) {
		setBounds(100, 100, 431, 329);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 431, 263);
		setLocationRelativeTo(sd);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("New Game");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblNewLabel.setBounds(123, 6, 112, 22);
		contentPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Home Team:");
		lblNewLabel_1.setBounds(32, 47, 85, 16);
		contentPanel.add(lblNewLabel_1);
		{
			JLabel lblNewLabel_2 = new JLabel("Away Team:");
			lblNewLabel_2.setBounds(241, 47, 75, 16);
			contentPanel.add(lblNewLabel_2);
		}

		cmbxHome = new JComboBox();
		cmbxHome.setBounds(16, 75, 142, 27);
		contentPanel.add(cmbxHome);

		cmbxAway = new JComboBox();
		cmbxAway.setBounds(204, 75, 149, 27);
		contentPanel.add(cmbxAway);

		JLabel lblNewLabel_3 = new JLabel("Format:");
		lblNewLabel_3.setBounds(32, 113, 61, 16);
		contentPanel.add(lblNewLabel_3);

		cmbxFormat = new JComboBox();
		cmbxFormat.setModel(new DefaultComboBoxModel(new String[] {"T20I", "ODI", "Test"}));
		cmbxFormat.setBounds(23, 141, 112, 27);
		contentPanel.add(cmbxFormat);

		JLabel lblNewLabel_4 = new JLabel("Winner:");
		lblNewLabel_4.setBounds(255, 113, 61, 16);
		contentPanel.add(lblNewLabel_4);

		cmbxWinner = new JComboBox();
		cmbxWinner.setBounds(249, 142, 149, 27);
		contentPanel.add(cmbxWinner);

		txtDate = new JTextField();
		txtDate.setBounds(129, 189, 130, 26);
		contentPanel.add(txtDate);
		txtDate.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Date:");
		lblNewLabel_5.setBounds(133, 168, 61, 16);
		contentPanel.add(lblNewLabel_5);

		JButton btnCalender = new JButton("Calender");
		btnCalender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtDate.setText(new DatePicker(gameGUI).setPickedDate());
			}
		});
		btnCalender.setBounds(133, 215, 117, 29);
		contentPanel.add(btnCalender);

		cmbxHome.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ((cmbxHome.getSelectedItem().toString()).equals(cmbxAway.getSelectedItem().toString())) {
					JOptionPane.showMessageDialog(contentPanel, "Home and away teams can not be same.");
					cmbxAway.setSelectedIndex(0);
					cmbxHome.setSelectedIndex(1);
				} else {
					setWinnerComboBoxOptions();
				}
			}
		});

		cmbxAway.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ((cmbxHome.getSelectedItem().toString()).equals(cmbxAway.getSelectedItem().toString())) {
					JOptionPane.showMessageDialog(contentPanel, "Home and away teams can not be same.");
					cmbxAway.setSelectedIndex(0);
					cmbxHome.setSelectedIndex(1);
				} else {
					setWinnerComboBoxOptions();
				}
			}
		});

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 262, 431, 39);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						logGame();
						StandingsDisplay sd1 = new StandingsDisplay();
						sd1.setVisible(true);
						sd1.putTeamsInTable();
						dispose();
					}
				});
				okButton.setBounds(6, 5, 75, 29);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						StandingsDisplay sd1 = new StandingsDisplay();
						sd1.setVisible(true);
						dispose();
					}
				});
				cancelButton.setBounds(339, 5, 86, 29);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		putTeamsInComboBox();
	}

	void putTeamsInComboBox() {
		Connection conn;

		try {
			// open a database connection
			conn = DriverManager.getConnection(url);

			// do the select
			String sql = "SELECT name FROM team ORDER BY name";
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// put the items in both JComboBox - home and away

			ArrayList<String> cmbxHomeList = new ArrayList<String>();
			ArrayList<String> cmbxAwayList = new ArrayList<String>();

			while(rs.next()){
				cmbxHomeList.add(rs.getString("name"));
				cmbxAwayList.add(rs.getString("name"));
			}

			String [] homeArray = cmbxHomeList.toArray(new String [] {});
			cmbxHome.setModel(new DefaultComboBoxModel(homeArray));

			String [] awayArray = cmbxAwayList.toArray(new String [] {});
			cmbxAway.setModel(new DefaultComboBoxModel(awayArray));

			// close the connection
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		setWinnerComboBoxOptions();
	}

	void setWinnerComboBoxOptions() {
		String homeTeam = cmbxHome.getSelectedItem().toString();
		String awayTeam = cmbxAway.getSelectedItem().toString();

		cmbxWinner.setModel(new DefaultComboBoxModel(new String[] {homeTeam, awayTeam, "Tie/Draw"}));
	}
	/*
	void checkComboBoxDuplicateSelection() {
		if ((cmbxHome.getSelectedItem().toString()).equals(cmbxAway.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(contentPanel, "Home and away teams can not be same.");
			cmbxAway.setSelectedIndex(0);
			cmbxHome.setSelectedIndex(1);
		} else {
			return;
		}
	}
	 */
	void logGame() {
		if ((cmbxHome.getSelectedItem().toString()).equals(cmbxAway.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(contentPanel, "Home and away teams can not be same.");
			cmbxAway.setSelectedIndex(0);
			cmbxHome.setSelectedIndex(1);
		} else {
			String date = txtDate.getText();
			String format = cmbxFormat.getSelectedItem().toString();
			int homeId = getTeamId(cmbxHome);
			int awayId = getTeamId(cmbxAway);

			int winnerId;
			if (cmbxWinner.getSelectedIndex() != 2) {
				winnerId = getTeamId(cmbxWinner);
			} else {
				winnerId = 0;
			}

			Connection conn;

			try {
				// open a database connection
				conn = DriverManager.getConnection(url);

				// do the select
				String sql = "INSERT INTO game(date, homeId, awayId, winnerId, format) "
						+ "VALUES ('"+ date +"', "+ homeId +", "+ awayId +", "+ winnerId + ", '"+ format +"');";
				java.sql.Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);

				// close the connection
				stmt.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(contentPanel, "Please check all fields.");
			}
		}

	}

	int getTeamId(JComboBox cmbx) {
		String name = cmbx.getSelectedItem().toString();
		Connection conn;
		int id = 0;

		try {
			// open a database connection
			conn = DriverManager.getConnection(url);

			// select team
			String sql = "SELECT teamId FROM team WHERE name='"+name+"';";
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// set label text
			id = Integer.parseInt(rs.getString("teamId"));

			// close the connection
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ID is "+ id);
		return id;
	}
}

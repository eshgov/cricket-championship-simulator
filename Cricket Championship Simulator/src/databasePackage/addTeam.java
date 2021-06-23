package databasePackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class addTeam extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtTeamName;
	private JTextField txtPoints;
	private String url = "jdbc:sqlite:/Users/eshaangovil/Desktop/Programming/Libraries/sqlite_databases/CricketChampionship.db";

	public addTeam(StandingsDisplay sd) {
		initizalizeGUI(sd);
	}

	void initizalizeGUI(StandingsDisplay sd) {
		setBounds(100, 100, 238, 161);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(sd);
		contentPanel.setLayout(null);

		txtTeamName = new JTextField();
		txtTeamName.setBounds(30, 44, 172, 26);
		contentPanel.add(txtTeamName);
		txtTeamName.setColumns(10);

		JLabel lblNewLabel = new JLabel("Enter Team Name:");
		lblNewLabel.setBounds(30, 16, 114, 16);
		contentPanel.add(lblNewLabel);
/*
		txtPoints = new JTextField();
		txtPoints.setBounds(31, 126, 130, 26);
		contentPanel.add(txtPoints);
		txtPoints.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Points:");
		lblNewLabel_1.setBounds(32, 110, 61, 16);
		contentPanel.add(lblNewLabel_1);*/
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 94, 238, 39);
			contentPanel.add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setBounds(24, 5, 75, 29);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addNewTeam();
						//sd.setVisible(true);
						sd.putTeamsInTable();
						dispose();
					}
				});
				buttonPane.setLayout(null);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setBounds(115, 5, 86, 29);
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
	}

	void addNewTeam() {
		String teamName = txtTeamName.getText();
		//int points = Integer.parseInt(txtPoints.getText());

		Connection conn;

		try {
			// open a database connection
			conn = DriverManager.getConnection(url);

			// do the select
			String sql = "INSERT INTO team(name) "+"VALUES ('"+teamName+"');";
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

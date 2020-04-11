package gui.dataMining;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

class ClusteringPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JComboBox<String> clusterSelectComboBox;
	private JTable clusterTable;


	public ClusteringPanel() {
		setSize(465, 217);
		setLayout(null);

		JLabel clusterSelectLabel = new JLabel("\u2022 Select a cluster :");
		clusterSelectLabel.setBounds(128, 9, 110, 16);
		add(clusterSelectLabel);

		clusterSelectComboBox = new JComboBox<String>();
		clusterSelectComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Cluster 1", "Cluster 2", "Cluster 3", "Cluster 4", "Cluster 5"}));
		clusterSelectComboBox.setBounds(240, 4, 94, 26);
		add(clusterSelectComboBox);

		JPanel resultPanel = new JPanel();
		resultPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		resultPanel.setBounds(6, 35, 453, 176);
		add(resultPanel);

		clusterTable = new JTable();
		clusterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		clusterTable.setEnabled(false);
		clusterTable.setRowSelectionAllowed(false);
		resultPanel.add(clusterTable, BorderLayout.CENTER);
		resultPanel.add(new JScrollPane(clusterTable));
	}

}

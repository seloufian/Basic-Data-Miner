package gui.dataMining;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dataMining.clustering.Cluster;
import dataMining.clustering.UtilMethods;
import main.Instance;

class ClusteringResultPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private DataMiningPanel dataMiningPanel;

	JComboBox<String> selectClusterComboBox;
	private JTextField absoluteErrorField;
	private JTable medoidTable;
	private JTable instancesTable;

	private ArrayList<Cluster<Instance>> clusters = new ArrayList<Cluster<Instance>>();
	private ArrayList<Instance> medoids = new ArrayList<Instance>();


	public ClusteringResultPanel(DataMiningPanel dataMiningPanel) {
		this.dataMiningPanel = dataMiningPanel;

		setBorder(new LineBorder(Color.GRAY, 2, true));
		setSize(465, 217);
		setLayout(null);

		JLabel selectClusterLabel = new JLabel("\u2022 Select a cluster :");
		selectClusterLabel.setBounds(20, 14, 109, 16);
		add(selectClusterLabel);

		selectClusterComboBox = new JComboBox<String>();
		selectClusterComboBox.setBounds(126, 9, 125, 26);
		add(selectClusterComboBox);

		JLabel absoluteErrorLabel = new JLabel("\u2022 Absolute error :");
		absoluteErrorLabel.setBounds(268, 14, 101, 16);
		add(absoluteErrorLabel);

		absoluteErrorField = new JTextField();
		absoluteErrorField.setEditable(false);
		absoluteErrorField.setBounds(368, 8, 101, 28);
		add(absoluteErrorField);
		absoluteErrorField.setColumns(10);

		JLabel medoidLabel = new JLabel("Cluster's medoid :");
		medoidLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
		medoidLabel.setForeground(Color.decode("#404040"));
		medoidLabel.setBounds(6, 42, 449, 16);
		add(medoidLabel);

		JPanel medoidPanel = new JPanel();
		medoidPanel.setBounds(6, 60, 550, 60);
		add(medoidPanel);
		medoidPanel.setLayout(new BorderLayout(0, 0));

		medoidTable = new JTable();
		medoidTable.setEnabled(false);
		medoidTable.setRowSelectionAllowed(false);
		medoidTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		medoidPanel.add(medoidTable, BorderLayout.CENTER);
		medoidPanel.add(new JScrollPane(medoidTable));

		JLabel instancesLabel = new JLabel("Cluster's instances :");
		instancesLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
		instancesLabel.setForeground(Color.decode("#404040"));
		instancesLabel.setBounds(6, 123, 449, 16);
		add(instancesLabel);

		JPanel instancesPanel = new JPanel();
		instancesPanel.setBounds(6, 140, 550, 163);
		add(instancesPanel);
		instancesPanel.setLayout(new BorderLayout(0, 0));

		instancesTable = new JTable();
		instancesTable.setEnabled(false);
		instancesTable.setRowSelectionAllowed(false);
		instancesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		instancesPanel.add(instancesTable, BorderLayout.CENTER);
		instancesPanel.add(new JScrollPane(instancesTable));
	}


	public void fillClusters(ArrayList<Cluster<Instance>> inClusters) {
		clusters = inClusters;

		selectClusterComboBox.setModel(new DefaultComboBoxModel<String>());
		for(int i=1; i<=clusters.size(); i++)
			selectClusterComboBox.addItem("Cluster " + i);
		medoids.clear();

		for(Cluster<Instance> cluster : clusters)
			medoids.add(cluster.getMedoid());

		double absoluteError = round(UtilMethods.get_absolute_error(dataMiningPanel.parentFrame.getDataset().getInstances(), medoids), 5);
		absoluteErrorField.setText(Double.toString(absoluteError));

		updateMedoidTable(medoids.get(0));
		updateInstancesTable(clusters.get(0));

		selectClusterComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					int selectedIndex = selectClusterComboBox.getSelectedIndex();

					updateMedoidTable(medoids.get(selectedIndex));
					updateInstancesTable(clusters.get(selectedIndex));
				}
			}
		});
	}


	private Double round(double value, int places) {
		if (places < 0)
			return null;

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


	private void updateMedoidTable(Instance instance) {
		ArrayList<String> attrNames = dataMiningPanel.parentFrame.getDataset().getNames();

		DefaultTableModel tableModel = new DefaultTableModel();

		for(String attrName : attrNames)
			tableModel.addColumn(attrName);

		int numAttributes = instance.getNumAttributes();

		String[] tableRow = new String[numAttributes];

		for(int i=0; i<numAttributes; i++)
			tableRow[i] = String.valueOf(instance.getAttribute(i).getValue());

		tableModel.addRow(tableRow);

		medoidTable.setModel(tableModel);
	}


	private void updateInstancesTable(Cluster<Instance> cluster) {
		ArrayList<String> attrNames = dataMiningPanel.parentFrame.getDataset().getNames();

		DefaultTableModel tableModel = new DefaultTableModel();

		for(String attrName : attrNames)
			tableModel.addColumn(attrName);

		int numAttributes = attrNames.size();

		for(Instance instance : cluster.getCluster_items()) {
			String[] tableRow = new String[numAttributes];

			for(int i=0; i<numAttributes; i++)
				tableRow[i] = String.valueOf(instance.getAttribute(i).getValue());

			tableModel.addRow(tableRow);
		}

		instancesTable.setModel(tableModel);
	}
}

package gui.datasetImport;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gui.Frame;
import main.DataSet;

class VisualizationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable datasetTable;
	private Frame parentWindow;

	VisualizationPanel(Frame parentWindow) {
		this.parentWindow = parentWindow;
		setBounds(9, 25, 564, 385);
		setLayout(new BorderLayout(0, 0));

		datasetTable = new JTable();
		datasetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		datasetTable.setEnabled(false);
		datasetTable.setRowSelectionAllowed(false);
		add(datasetTable, BorderLayout.CENTER);
		add(new JScrollPane(datasetTable));
	}

	String loadDataSet() {
		DataSet ds = parentWindow.getDataset();

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Instance");

		for(String attrName : ds.getNames())
			tableModel.addColumn(attrName);

		String[] tableRow = new String[ds.getNumAttributes()+1];

		for(int i=1; i<=ds.getNumInstances(); i++) {
			tableRow[0] = String.valueOf(i);

			for(int j=1; j<=ds.getNumAttributes(); j++)
				tableRow[j] = String.valueOf(ds.getIntance(i-1).getAttribute(j-1).getValue());

			tableModel.addRow(tableRow);
		}

		datasetTable.setModel(tableModel);

		return "\"" + ds.getName() + "\" dataset loaded :  " + ds.getNumInstances() + " instances,  " + ds.getNumAttributes() + " attributes/instance.";
	}

}

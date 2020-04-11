package gui.boxplot;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.DataSet;

class OutliersPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable outliersTable;


	OutliersPanel(){
		setBounds(9, 269, 294, 200);
		setLayout(new BorderLayout(0, 0));

		outliersTable = new JTable();
		outliersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		outliersTable.setEnabled(false);
		outliersTable.setRowSelectionAllowed(false);
		add(outliersTable, BorderLayout.CENTER);
		add(new JScrollPane(outliersTable));
	}


	void showOutliers(DataSet ds, float lowerBound, float upperBound, int position, String attrName) {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Instance");
		tableModel.addColumn("Outlier Value ("+attrName+")");

		for(int i=0; i<ds.getNumInstances(); i++) {
			float currVal = Float.parseFloat(ds.getIntance(i).getAttribute(position).getValue());
			if(currVal > upperBound || currVal < lowerBound)
				tableModel.addRow(new String[] {Integer.toString(i+1), ds.getIntance(i).getAttribute(position).getValue()});
		}

		outliersTable.setModel(tableModel);
	}


	void clearOutliers() {
		outliersTable.setModel(new DefaultTableModel());
	}
}

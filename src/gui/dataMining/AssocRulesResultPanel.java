package gui.dataMining;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataMining.frequentPatterns.apriori.AssociationRules;
import dataMining.frequentPatterns.apriori.Itemset;
import dataMining.frequentPatterns.apriori.Rule;

class AssocRulesResultPanel extends JPanel {
	private static final long serialVersionUID = 1L;


	private JTable assocRulesTable;


	public AssocRulesResultPanel() {
		setSize(465, 217);
		setLayout(new BorderLayout(0, 0));

		assocRulesTable = new JTable();
		//		assocRulesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ON);
		assocRulesTable.setEnabled(false);
		assocRulesTable.setRowSelectionAllowed(false);
		add(assocRulesTable, BorderLayout.CENTER);
		add(new JScrollPane(assocRulesTable));
	}


	public void fillAssocRules(AssociationRules assocRules) {

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("N°");
		tableModel.addColumn("Condition");
		tableModel.addColumn("Consequence");
		tableModel.addColumn("Confidence (%)");

		for(int i=0; i<assocRules.numRules(); i++) {
			Rule currRule = assocRules.getRule(i);

			Itemset currRuleConds = currRule.getConditions();
			String conditions = "";
			for(int j=0; j<currRuleConds.numItems()-1; j++)
				conditions += currRuleConds.getItem(j) + ", ";
			conditions += currRuleConds.getItem(currRuleConds.numItems()-1);

			Itemset currRuleCons = currRule.getConsequences();
			String consequences = "";
			for(int j=0; j<currRuleCons.numItems()-1; j++)
				consequences += currRuleCons.getItem(j) + ", ";
			consequences += currRuleCons.getItem(currRuleCons.numItems()-1);

			String[] tableRow = new String[4];
			tableRow[0] = String.valueOf(i+1);
			tableRow[1] = "{" + conditions + "}";
			tableRow[2] = "{" + consequences + "}";
			tableRow[3] = String.valueOf(currRule.getConfidence());

			tableModel.addRow(tableRow);
		}

		assocRulesTable.setModel(tableModel);
	}
}

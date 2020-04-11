package gui.dataMining;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

class KMedoidsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JSpinner numClustersSpinner;


	public KMedoidsPanel() {
		setSize(183, 116);
		setLayout(null);

		JLabel numClustersLabel = new JLabel("\u2022 Number of clusters :");
		numClustersLabel.setBounds(28, 31, 132, 16);
		add(numClustersLabel);

		numClustersSpinner = new JSpinner();
		numClustersSpinner.setModel(new SpinnerNumberModel(10, 2, 50, 1));
		numClustersSpinner.setBounds(57, 59, 70, 28);
		add(numClustersSpinner);
	}


	public int getNumClusters() { return (int) numClustersSpinner.getValue(); }
}

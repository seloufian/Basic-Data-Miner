package gui.dataMining;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

class ClaransPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JSpinner numClustersSpinner;
	private JSpinner localMinSpinner;
	private JSpinner maxNeighborSpinner;


	public ClaransPanel() {
		setSize(183, 116);
		setLayout(null);

		JLabel numClustersLabel = new JLabel("\u2022 Num Clusters :");
		numClustersLabel.setBounds(9, 13, 102, 16);
		add(numClustersLabel);

		numClustersSpinner = new JSpinner();
		numClustersSpinner.setModel(new SpinnerNumberModel(10, 2, 50, 1));
		numClustersSpinner.setBounds(109, 7, 65, 28);
		add(numClustersSpinner);

		JLabel localMinLabel = new JLabel("\u2022 Local Minima :");
		localMinLabel.setBounds(9, 50, 102, 16);
		add(localMinLabel);

		localMinSpinner = new JSpinner();
		localMinSpinner.setModel(new SpinnerNumberModel(50, 2, 500, 1));
		localMinSpinner.setBounds(109, 44, 65, 28);
		add(localMinSpinner);

		JLabel maxNeighborLabel = new JLabel("\u2022 Max. Neighbor :");
		maxNeighborLabel.setBounds(9, 86, 102, 16);
		add(maxNeighborLabel);

		maxNeighborSpinner = new JSpinner();
		maxNeighborSpinner.setModel(new SpinnerNumberModel(20, 1, 100, 1));
		maxNeighborSpinner.setBounds(109, 81, 65, 28);
		add(maxNeighborSpinner);
	}


	public int getNumClusters() { return (int) numClustersSpinner.getValue(); }

	public int getLocalMin() { return (int) localMinSpinner.getValue(); }

	public int getMaxNeighbor() { return (int) maxNeighborSpinner.getValue(); }
}

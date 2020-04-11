package gui.dataMining;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

class FreqPatternsAssocRulesPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JSpinner minSuppCountSpinner;
	private JSpinner minConfThresholdSpinner;


	public FreqPatternsAssocRulesPanel() {
		setSize(183, 116);
		setLayout(null);

		JLabel minSuppCountLabel = new JLabel("\u2022 Min. Support Count :");
		minSuppCountLabel.setBounds(28, 6, 128, 16);
		add(minSuppCountLabel);

		minSuppCountSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 100, 1));
		minSuppCountSpinner.setBounds(55, 28, 63, 28);
		add(minSuppCountSpinner);

		JLabel minSuppCountPercentageLabel = new JLabel("%");
		minSuppCountPercentageLabel.setBounds(123, 33, 20, 16);
		add(minSuppCountPercentageLabel);

		JLabel minConfThresholdLabel = new JLabel("\u2022 Min. Confidence Threshold :");
		minConfThresholdLabel.setBounds(6, 58, 171, 16);
		add(minConfThresholdLabel);

		minConfThresholdSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 100, 1));
		minConfThresholdSpinner.setBounds(55, 79, 63, 28);
		add(minConfThresholdSpinner);

		JLabel minConfThresholdPercentageLabel = new JLabel("%");
		minConfThresholdPercentageLabel.setBounds(123, 84, 20, 16);
		add(minConfThresholdPercentageLabel);
	}


	public int getMinSuppCount() { return (int) minSuppCountSpinner.getValue(); }
	public int getMinConfThreshold() { return (int) minConfThresholdSpinner.getValue(); }
}

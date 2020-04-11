package gui.boxplot;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

class FiveNumSumPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private JTextField q1Field;
	private JTextField q2Field;
	private JTextField q3Field;
	private JTextField maxField;
	private JTextField minField;


	FiveNumSumPanel(){
		setBorder(new LineBorder(Color.GRAY, 2, true));
		setBounds(9, 64, 294, 178);
		setLayout(null);

		JLabel q1Label = new JLabel("\u2022 Quartile 1 (Q1) :");
		q1Label.setBounds(14, 8, 103, 25);
		add(q1Label);

		q1Field = new JTextField();
		q1Field.setEditable(false);
		q1Field.setFont(new Font("SansSerif", Font.BOLD, 12));
		q1Field.setBounds(120, 6, 160, 28);
		add(q1Field);
		q1Field.setColumns(10);

		JLabel q2Label = new JLabel("\u2022 Quartile 2 (Median) :");
		q2Label.setBounds(14, 41, 126, 25);
		add(q2Label);

		q2Field = new JTextField();
		q2Field.setColumns(10);
		q2Field.setEditable(false);
		q2Field.setFont(new Font("SansSerif", Font.BOLD, 12));
		q2Field.setBounds(143, 40, 137, 28);
		add(q2Field);

		JLabel q3Label = new JLabel("\u2022 Quartile 3 (Q3) :");
		q3Label.setBounds(14, 74, 103, 25);
		add(q3Label);

		q3Field = new JTextField();
		q3Field.setColumns(10);
		q3Field.setEditable(false);
		q3Field.setFont(new Font("SansSerif", Font.BOLD, 12));
		q3Field.setBounds(120, 72, 160, 28);
		add(q3Field);

		JLabel minLabel = new JLabel("\u2022 Minimum :");
		minLabel.setBounds(14, 107, 75, 25);
		add(minLabel);

		minField = new JTextField();
		minField.setColumns(10);
		minField.setEditable(false);
		minField.setFont(new Font("SansSerif", Font.BOLD, 12));
		minField.setBounds(92, 105, 188, 28);
		add(minField);

		JLabel maxLabel = new JLabel("\u2022 Maximum :");
		maxLabel.setBounds(14, 140, 75, 25);
		add(maxLabel);

		maxField = new JTextField();
		maxField.setColumns(10);
		maxField.setEditable(false);
		maxField.setFont(new Font("SansSerif", Font.BOLD, 12));
		maxField.setBounds(92, 138, 188, 28);
		add(maxField);
	}


	void showFiveNumSum(float q1, float q2, float q3, float min, float max, float lower, float upper) {
		q1Field.setEnabled(true);		q1Field.setText(Float.toString(q1));
		q2Field.setEnabled(true);		q2Field.setText(Float.toString(q2));
		q3Field.setEnabled(true);		q3Field.setText(Float.toString(q3));
		minField.setEnabled(true);		minField.setText(Float.toString(min) + "   (Lower :  "+ lower+")");
		maxField.setEnabled(true);		maxField.setText(Float.toString(max) + "   (Upper :  "+ upper+")");
	}


	void clearFields() {
		q1Field.setText("");	q1Field.setEnabled(false);
		q2Field.setText("");	q2Field.setEnabled(false);
		q3Field.setText("");	q3Field.setEnabled(false);
		minField.setText("");	minField.setEnabled(false);
		maxField.setText("");	maxField.setEnabled(false);
	}
}

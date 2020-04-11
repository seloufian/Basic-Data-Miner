package gui.datasetImport;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import gui.Frame;
import main.DataSet;

class CentralTendencyPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField attrTypeField;
	private JTextField attrMeanField;
	private JTextField attrMedianField;
	private JTextField attrModeField;
	private JTextField attrSymetricField;
	private JComboBox<String> selectAttrComboBox;

	private Frame parentFrame;

	CentralTendencyPanel(Frame parentFrame) {
		this.parentFrame = parentFrame;

		setBorder(new LineBorder(new Color(128, 128, 128), 2, true));
		setBounds(584, 61, 198, 300);

		setVisible(false);
		setLayout(null);

		JLabel selectAttrLabel = new JLabel("Select an attribute :");
		selectAttrLabel.setBounds(43, 10, 111, 16);
		add(selectAttrLabel);

		selectAttrComboBox = new JComboBox<String>();
		selectAttrComboBox.setBounds(14, 36, 169, 26);
		add(selectAttrComboBox);

		JLabel attrTypeLabel = new JLabel("\u2022 Type :");
		attrTypeLabel.setBounds(14, 83, 50, 25);
		add(attrTypeLabel);

		attrTypeField = new JTextField();
		attrTypeField.setEnabled(false);
		attrTypeField.setEditable(false);
		attrTypeField.setFont(new Font("SansSerif", Font.BOLD, 12));
		attrTypeField.setBounds(70, 81, 113, 28);
		add(attrTypeField);
		attrTypeField.setColumns(10);

		JLabel attrMeanLabel = new JLabel("\u2022 Mean :");
		attrMeanLabel.setToolTipText("Average value");
		attrMeanLabel.setBounds(14, 125, 55, 25);
		add(attrMeanLabel);

		attrMeanField = new JTextField();
		attrMeanField.setEnabled(false);
		attrMeanField.setEditable(false);
		attrMeanField.setFont(new Font("SansSerif", Font.BOLD, 12));
		attrMeanField.setBounds(75, 123, 108, 28);
		add(attrMeanField);
		attrMeanField.setColumns(10);

		JLabel attrMedianLabel = new JLabel("\u2022 Median :");
		attrMedianLabel.setToolTipText("Middle value");
		attrMedianLabel.setBounds(14, 167, 64, 25);
		add(attrMedianLabel);

		attrMedianField = new JTextField();
		attrMedianField.setEnabled(false);
		attrMedianField.setFont(new Font("SansSerif", Font.BOLD, 12));
		attrMedianField.setEditable(false);
		attrMedianField.setColumns(10);
		attrMedianField.setBounds(84, 165, 99, 28);
		add(attrMedianField);

		JLabel attrModeLabel = new JLabel("\u2022 Mode :");
		attrModeLabel.setToolTipText("Most common value");
		attrModeLabel.setBounds(14, 209, 55, 25);
		add(attrModeLabel);

		attrModeField = new JTextField();
		attrModeField.setEnabled(false);
		attrModeField.setFont(new Font("SansSerif", Font.BOLD, 12));
		attrModeField.setEditable(false);
		attrModeField.setColumns(10);
		attrModeField.setBounds(75, 207, 108, 28);
		add(attrModeField);

		JLabel attrSymetricLabel = new JLabel("\u2022 Symetric ? :");
		attrSymetricLabel.setBounds(14, 251, 78, 25);
		add(attrSymetricLabel);

		attrSymetricField = new JTextField();
		attrSymetricField.setEnabled(false);
		attrSymetricField.setFont(new Font("SansSerif", Font.BOLD, 12));
		attrSymetricField.setEditable(false);
		attrSymetricField.setColumns(10);
		attrSymetricField.setBounds(98, 249, 85, 28);
		add(attrSymetricField);

		selectAttrComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String initialSelect = "- - - - - - - - - - - - - - -";

				if(selectAttrComboBox.getSelectedItem().equals(initialSelect))
					return;

				selectAttrComboBox.removeItem(initialSelect);

				int selectAttr = selectAttrComboBox.getSelectedIndex();

				attrTypeField.setText(parentFrame.getDataset().getAttributeType(selectAttr));
				attrTypeField.setEnabled(true);

				List<Float> centralTendency = parentFrame.getDataset().calculateCentralTendency(selectAttr);

				if(centralTendency != null) {
					attrMeanField.setText(Float.toString(centralTendency.get(0)));
					attrMeanField.setEnabled(true);

					attrMedianField.setText(Float.toString(centralTendency.get(1)));
					attrMedianField.setEnabled(true);

					attrModeField.setText(Float.toString(centralTendency.get(2)));
					attrModeField.setEnabled(true);

					if((centralTendency.get(0) == centralTendency.get(1)) && (centralTendency.get(1) == centralTendency.get(2)))
						attrSymetricField.setText("Yes");
					else
						attrSymetricField.setText("No");

					attrSymetricField.setEnabled(true);
				}
				else {
					attrMeanField.setText("");		attrMeanField.setEnabled(false);
					attrMedianField.setText("");	attrMedianField.setEnabled(false);
					attrModeField.setText("");		attrModeField.setEnabled(false);
					attrSymetricField.setText("");	attrSymetricField.setEnabled(false);
				}
			}
		});
	}

	void updateSelectAttrComboBox() {
		DataSet ds = parentFrame.getDataset();

		attrTypeField.setText("");		attrTypeField.setEnabled(false);
		attrMeanField.setText("");		attrMeanField.setEnabled(false);
		attrMedianField.setText("");	attrMedianField.setEnabled(false);
		attrModeField.setText("");		attrModeField.setEnabled(false);
		attrSymetricField.setText("");	attrSymetricField.setEnabled(false);

		selectAttrComboBox.setModel(new DefaultComboBoxModel<String>());

		selectAttrComboBox.addItem("- - - - - - - - - - - - - - -");

		for(int i=0; i<ds.getNumAttributes(); i++)
			selectAttrComboBox.addItem(ds.getAttributeName(i));

		setVisible(true);
	}
}

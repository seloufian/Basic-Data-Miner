package gui.discretization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import gui.Frame;
import main.DataSet;

public class DiscretizationPanel extends JPanel {
	private static final long serialVersionUID = 1L;


	private JTable datasetTable;

	private Frame parentFrame;


	public DiscretizationPanel(Frame inParentFrame) {
		parentFrame = inParentFrame;

		setBounds(parentFrame.getBounds());
		setLayout(null);

		JLabel discParamsLabel = new JLabel("Equal Width Binning discretization parameters :");
		discParamsLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		discParamsLabel.setForeground(Color.decode("#404040"));
		discParamsLabel.setBounds(207, 3, 386, 25);
		add(discParamsLabel);

		JPanel discParamsPanel = new JPanel();
		discParamsPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		discParamsPanel.setBounds(205, 27, 390, 105);
		add(discParamsPanel);
		discParamsPanel.setLayout(null);

		JLabel numBinsLabel = new JLabel("\u2022 Number of bins :");
		numBinsLabel.setBounds(16, 16, 109, 16);
		discParamsPanel.add(numBinsLabel);

		JSpinner numBinsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
		numBinsSpinner.setBounds(124, 11, 59, 28);
		discParamsPanel.add(numBinsSpinner);

		JRadioButton replaceMeanRadio = new JRadioButton("Replace by MEAN value.");
		replaceMeanRadio.setBounds(21, 46, 160, 18);
		replaceMeanRadio.setSelected(true);
		discParamsPanel.add(replaceMeanRadio);

		JRadioButton replaceMedianRadio = new JRadioButton("Replace by MEDIAN value.");
		replaceMedianRadio.setBounds(21, 73, 175, 18);
		discParamsPanel.add(replaceMedianRadio);

		ButtonGroup radioBtnGroup = new ButtonGroup();
		radioBtnGroup.add(replaceMeanRadio);
		radioBtnGroup.add(replaceMedianRadio);

		JButton startDiscBtn = new JButton("Start discretization");
		startDiscBtn.setBounds(235, 30, 135, 45);
		discParamsPanel.add(startDiscBtn);

		JLabel discDatasetLabel = new JLabel();
		discDatasetLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		discDatasetLabel.setForeground(Color.decode("#404040"));
		discDatasetLabel.setBounds(11, 141, 621, 19);
		discDatasetLabel.setVisible(false);
		add(discDatasetLabel);

		JPanel discDatasetPanel = new JPanel();
		discDatasetPanel.setBounds(9, 160, 775, 310);
		discDatasetPanel.setLayout(new BorderLayout(0, 0));
		discDatasetPanel.setVisible(false);
		add(discDatasetPanel);

		datasetTable = new JTable();
		datasetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		datasetTable.setEnabled(false);
		datasetTable.setRowSelectionAllowed(false);
		discDatasetPanel.add(datasetTable, BorderLayout.CENTER);
		discDatasetPanel.add(new JScrollPane(datasetTable));

		JLabel statusBarLabel = new JLabel("Adjust parameters and click on \"Start discretization\"..");
		statusBarLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		statusBarLabel.setForeground(Color.decode("#404040"));
		statusBarLabel.setBounds(6, 476, 724, 25);
		add(statusBarLabel);


		startDiscBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DataSet ds = parentFrame.getDataset();

				int binSize = (int) numBinsSpinner.getValue();
				ds.discretizeAllAttributes(binSize, replaceMeanRadio.isSelected());

				DefaultTableModel tableModel = new DefaultTableModel();
				tableModel.addColumn("Instance");

				for(String attrName : ds.getNames())
					tableModel.addColumn(attrName);

				String[] tableRow = new String[ds.getNumAttributes()+1];

				for(int i=1; i<=ds.getNumInstances(); i++) {
					tableRow[0] = String.valueOf(i);

					for(int j=1; j<=ds.getNumAttributes(); j++)
						tableRow[j] = ds.getIntance(i-1).getAttribute(j-1).getDiscValue();

					tableModel.addRow(tableRow);
				}

				datasetTable.setModel(tableModel);

				discDatasetLabel.setText("Dataset \"" + ds.getName() + "\" (After discretization) :");
				statusBarLabel.setText("Dataset's \"" + ds.getName() + "\" attributes have been descritized.");

				discDatasetLabel.setVisible(true);
				discDatasetPanel.setVisible(true);

				parentFrame.enablePanel(5);
			}
		});
	}
}

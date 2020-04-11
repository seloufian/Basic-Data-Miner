package gui.datasetImport;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.Frame;
import main.DataSet;

public class DataPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Frame parentFrame;


	public DataPanel(Frame parentFrame) {
		this.parentFrame = parentFrame;
		setBounds(parentFrame.getBounds());
		setLayout(null);

		JLabel importedFileLabel = new JLabel();
		importedFileLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		importedFileLabel.setForeground(Color.decode("#404040"));
		importedFileLabel.setBounds(11, 3, 365, 25);
		add(importedFileLabel);

		VisualizationPanel visualizationPanel = new VisualizationPanel(this.parentFrame);
		add(visualizationPanel);

		JLabel centralTendencyLabel = new JLabel("Central tendency :");
		centralTendencyLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		centralTendencyLabel.setForeground(Color.decode("#404040"));
		centralTendencyLabel.setBounds(586, 37, 365, 25);
		centralTendencyLabel.setVisible(false);
		add(centralTendencyLabel);

		CentralTendencyPanel centralTendencyPanel = new CentralTendencyPanel(this.parentFrame);
		add(centralTendencyPanel);

		JLabel selectDatasetLabel = new JLabel("Select a dataset :");
		selectDatasetLabel.setBounds(70, 430, 112, 25);
		add(selectDatasetLabel);

		JComboBox<String> selectDatasetComboBox = new JComboBox<String>();
		selectDatasetComboBox.setBounds(190, 429, 152, 27);
		add(selectDatasetComboBox);

		File dsDirectory = new File("Datasets");
		for(String dataset : Arrays.asList(dsDirectory.list()))
			selectDatasetComboBox.addItem(dataset);

		JButton importButton = new JButton("Import dataset file");
		importButton.setBounds(360, 422, 137, 41);
		add(importButton);

		JLabel statusBarLabel = new JLabel("Welcome to \"Data Mining Project\". To begin, click on \"Import dataset file\"..");
		statusBarLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		statusBarLabel.setForeground(Color.decode("#404040"));
		statusBarLabel.setBounds(6, 476, 724, 25);
		add(statusBarLabel);

		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSet ds = new DataSet("Datasets/" + selectDatasetComboBox.getSelectedItem());
				parentFrame.setDataset(ds);
				importedFileLabel.setText("File \"" + selectDatasetComboBox.getSelectedItem() + "\" :");
				statusBarLabel.setText(visualizationPanel.loadDataSet());

				centralTendencyPanel.updateSelectAttrComboBox();
				centralTendencyLabel.setVisible(true);
				parentFrame.updatePanels();


				//	JFileChooser fileChooser = new JFileChooser();
				//	fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				//	fileChooser.setAcceptAllFileFilterUsed(false);
				//	fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Attribute-Relation File Format (.arff)", "arff"));
				//	fileChooser.showOpenDialog(null);
				//
				//	try {
				//		File selectedFile = fileChooser.getSelectedFile();
				//		DataSet ds = new DataSet(selectedFile.getAbsolutePath());
				//		parentFrame.setDataset(ds);
				//		importedFileLabel.setText("File \"" + selectedFile.getName() + "\" :");
				//		statusBarLabel.setText(visualizationPanel.loadDataSet());
				//
				//		centralTendencyPanel.updateSelectAttrComboBox();
				//		centralTendencyLabel.setVisible(true);
				//		parentFrame.updatePanels();
				//
				//	} catch (NullPointerException ignore) {}
			}
		});
	}
}

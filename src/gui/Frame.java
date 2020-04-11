package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import gui.barchart.BarchartPanel;
import gui.boxplot.BoxplotPanel;
import gui.dataMining.DataMiningPanel;
import gui.datasetImport.DataPanel;
import gui.discretization.DiscretizationPanel;
import gui.scatterplot.ScatterPlotPanel;
import main.DataSet;

public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private BoxplotPanel boxplotPanel;
	private BarchartPanel barchartPanel;
	private ScatterPlotPanel scatterPlotPanel;
	private DiscretizationPanel discretizationPanel;
	private DataMiningPanel dataMiningPanel;

	private DataSet dataset = null;

	public Frame(String title) {
		setTitle(title);
		setBounds(200, 100, 800, 560);
		setResizable(false);
		getContentPane().setLayout(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception ignored){}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 850, 560);
		contentPane.add(tabbedPane);

		DataPanel dataPanel = new DataPanel(this);
		tabbedPane.addTab("Dataset visualization", null, dataPanel, null);

		boxplotPanel = new BoxplotPanel(this);
		boxplotPanel.setVisible(false);
		tabbedPane.addTab("Boxplot & Outliers", null, boxplotPanel, null);
		tabbedPane.setEnabledAt(1, false);

		barchartPanel = new BarchartPanel(this);
		tabbedPane.addTab("Barchart", null, barchartPanel, null);
		tabbedPane.setEnabledAt(2, false);

		scatterPlotPanel = new ScatterPlotPanel(this);
		tabbedPane.addTab("Scatter plot", null, scatterPlotPanel, null);
		tabbedPane.setEnabledAt(3, false);

		discretizationPanel = new DiscretizationPanel(this);
		tabbedPane.addTab("Attributes Discretization", null, discretizationPanel, null);
		tabbedPane.setEnabledAt(4, false);

		dataMiningPanel = new DataMiningPanel(this);
		tabbedPane.addTab("Data Mining", null, dataMiningPanel, null);
		tabbedPane.setEnabledAt(5, false);
	}


	public DataSet getDataset() { return dataset; }
	public void setDataset(DataSet dataset) { this.dataset = dataset; }

	public void updatePanels() {
		boxplotPanel.updatePanel();
		tabbedPane.setEnabledAt(1, true);

		barchartPanel.updatePanel();
		tabbedPane.setEnabledAt(2, true);

		scatterPlotPanel.updatePanel();
		tabbedPane.setEnabledAt(3, true);

		tabbedPane.setEnabledAt(4, true);

		tabbedPane.setEnabledAt(5, true);
		dataMiningPanel.updatePanel(0);
	}

	public void enablePanel(int index) {
		tabbedPane.setEnabledAt(index, true);
		dataMiningPanel.updatePanel(1);
	}
}

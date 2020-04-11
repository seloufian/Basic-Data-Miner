package gui.boxplot;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import gui.Frame;
import main.DataSet;
import main.Instance;

public class BoxplotPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JComboBox<String> attrSelectComboBox;
	private FiveNumSumPanel fiveNumSumPanel;
	private OutliersPanel outliersPanel;
	private JPanel boxplotPanel;
	private JLabel statusBarLabel;
	private Frame parentFrame;

	private ArrayList<Integer> chooseNumericAttrList = new ArrayList<Integer>();


	public BoxplotPanel(Frame parentFrame) {
		this.parentFrame = parentFrame;

		setBounds(parentFrame.getBounds());
		setLayout(null);

		attrSelectComboBox = new JComboBox<String>();
		attrSelectComboBox.setBounds(12, 10, 285, 26);
		add(attrSelectComboBox);

		JLabel boxplotLabel = new JLabel("Boxplot :");
		boxplotLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		boxplotLabel.setForeground(Color.decode("#404040"));
		boxplotLabel.setBounds(334, 7, 55, 16);
		add(boxplotLabel);

		boxplotPanel = new JPanel();
		boxplotPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		boxplotPanel.setBounds(336, 25, 447, 444);
		add(boxplotPanel);

		JLabel fiveNumSumLabel = new JLabel("Five-Number Summary :");
		fiveNumSumLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		fiveNumSumLabel.setForeground(Color.decode("#404040"));
		fiveNumSumLabel.setBounds(11, 42, 180, 20);
		add(fiveNumSumLabel);

		fiveNumSumPanel = new FiveNumSumPanel();
		add(fiveNumSumPanel);

		JLabel outliersLabel = new JLabel("Outliers :");
		outliersLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		outliersLabel.setForeground(Color.decode("#404040"));
		outliersLabel.setBounds(11, 250, 290, 16);
		add(outliersLabel);

		outliersPanel = new OutliersPanel();
		add(outliersPanel);

		statusBarLabel = new JLabel("Choose an attribute..");
		statusBarLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		statusBarLabel.setForeground(Color.decode("#404040"));
		statusBarLabel.setBounds(6, 476, 724, 25);
		add(statusBarLabel);


		attrSelectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String initialSelect = "- - - - - -  Select an attribute  - - - - - - -";

				if(attrSelectComboBox.getSelectedItem().equals(initialSelect))
					return;

				attrSelectComboBox.removeItem(initialSelect);

				int selectAttr = chooseNumericAttrList.get(attrSelectComboBox.getSelectedIndex());
				statusBarLabel.setText("Attribute \"" + attrSelectComboBox.getSelectedItem() + "\" choosed.");

				DataSet ds = parentFrame.getDataset();

				ArrayList<Float> selectAttrValues = new ArrayList<Float>();
				for(Instance instance : ds.getInstances())
					selectAttrValues.add(Float.parseFloat(instance.getAttribute(selectAttr).getValue()));

				boxplotPanel.removeAll();
				boxplotPanel.add(drawBoxplot(selectAttrValues, attrSelectComboBox.getSelectedItem().toString()));
				boxplotPanel.revalidate();

				List<Float> fiveNumSumAttr = parentFrame.getDataset().calculateFiveNumSum(selectAttr);

				float iqr = (float) ((fiveNumSumAttr.get(2) - fiveNumSumAttr.get(0)) * 1.5);
				float lowerBound = fiveNumSumAttr.get(0) - iqr; // q1 - IQR * 1.5
				float upperBound = fiveNumSumAttr.get(2) + iqr; // q3 + IQR * 1.5

				fiveNumSumPanel.showFiveNumSum(fiveNumSumAttr.get(0), fiveNumSumAttr.get(1), fiveNumSumAttr.get(2),
						fiveNumSumAttr.get(3), fiveNumSumAttr.get(4), lowerBound, upperBound);

				outliersPanel.showOutliers(ds, lowerBound, upperBound, selectAttr, attrSelectComboBox.getSelectedItem().toString());
			}
		});
	}


	public void updatePanel() {
		DataSet ds = parentFrame.getDataset();

		chooseNumericAttrList.clear();

		boxplotPanel.removeAll();
		boxplotPanel.revalidate();

		fiveNumSumPanel.clearFields();
		outliersPanel.clearOutliers();

		statusBarLabel.setText("Choose an attribute..");

		attrSelectComboBox.setModel(new DefaultComboBoxModel<String>());

		attrSelectComboBox.addItem("- - - - - -  Select an attribute  - - - - - - -");

		for(int i=0; i<ds.getNumAttributes(); i++) {
			if(ds.getAttributeType(i).matches("numeric")) {
				attrSelectComboBox.addItem(ds.getAttributeName(i));
				chooseNumericAttrList.add(i);
			}
		}
	}


	public ChartPanel drawBoxplot(ArrayList<Float> data, String attributeName) {
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		dataset.add(data, "", attributeName);

		CategoryAxis xAxis = new CategoryAxis();

		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRangeIncludesZero(false);

		BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		renderer.setMaximumBarWidth(0.3);
		renderer.setMeanVisible(false);

		CategoryPlot plot = new CategoryPlot(dataset , xAxis, yAxis, renderer);
		plot.setBackgroundPaint(Color.decode("#D6D9DF"));

		JFreeChart chart = new JFreeChart("", new Font("SansSerif", Font.BOLD, 16), plot, false);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);
		chartPanel.setPopupMenu(null);
		chartPanel.setLayout(null);
		chartPanel.setPreferredSize(new java.awt.Dimension(435, 435));
		return chartPanel;
	}
}

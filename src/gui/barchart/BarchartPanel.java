package gui.barchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import gui.Frame;
import main.DataSet;
import main.Instance;

public class BarchartPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel barchartPanel;
	private JComboBox<String> selectAttrComboBox;
	private Frame parentFrame;
	private JLabel statusBarLabel;
	private ArrayList<Integer> chooseNumericAttrList = new ArrayList<Integer>();


	public BarchartPanel(Frame parentFrame) {
		this.parentFrame = parentFrame;

		setBounds(parentFrame.getBounds());
		setLayout(null);

		selectAttrComboBox = new JComboBox<String>();
		selectAttrComboBox.setBounds(258, 10, 285, 26);
		add(selectAttrComboBox);

		barchartPanel = new JPanel();
		barchartPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		barchartPanel.setBounds(9, 45, 775, 425);
		add(barchartPanel);

		statusBarLabel = new JLabel("Choose an attribute..");
		statusBarLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		statusBarLabel.setForeground(Color.decode("#404040"));
		statusBarLabel.setBounds(6, 476, 724, 25);
		add(statusBarLabel);


		selectAttrComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectAttrComboBox.getSelectedItem().equals("- - - - - -  Select an attribute  - - - - - - -"))
					return;

				selectAttrComboBox.removeItem("- - - - - -  Select an attribute  - - - - - - -");

				int selectAttr = chooseNumericAttrList.get(selectAttrComboBox.getSelectedIndex());
				statusBarLabel.setText("Attribute \"" + selectAttrComboBox.getSelectedItem() + "\" choosed.");

				DataSet ds = parentFrame.getDataset();

				HashMap<Float, Integer> attrFreq = new HashMap<Float, Integer>();

				for(Instance instance : ds.getInstances()) {
					float currAttrVal = Float.parseFloat(instance.getAttribute(selectAttr).getValue());
					Integer currAttrFreq = attrFreq.get(currAttrVal);
					attrFreq.put(currAttrVal,  currAttrFreq == null ? 1 : currAttrFreq+1);
				}

				barchartPanel.removeAll();
				ArrayList<Integer> data = new ArrayList<Integer>(attrFreq.values());
				barchartPanel.add(drawBarchart(data, selectAttrComboBox.getSelectedItem().toString()));
				barchartPanel.revalidate();
			}
		});
	}


	public void updatePanel() {
		DataSet ds = parentFrame.getDataset();

		chooseNumericAttrList.clear();

		barchartPanel.removeAll();
		barchartPanel.revalidate();

		statusBarLabel.setText("Choose an attribute..");

		selectAttrComboBox.setModel(new DefaultComboBoxModel<String>());

		selectAttrComboBox.addItem("- - - - - -  Select an attribute  - - - - - - -");

		for(int i=0; i<ds.getNumAttributes()-1; i++) {
			if(ds.getAttributeType(i).matches("numeric|binary")) {
				selectAttrComboBox.addItem(ds.getAttributeName(i));
				chooseNumericAttrList.add(i);
			}
		}
	}


	public ChartPanel drawBarchart(ArrayList<Integer> data, String attributeName) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for(int i=0; i<data.size(); i++)
			dataset.addValue(data.get(i), "", Integer.toString(i));

		JFreeChart barChart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);

		CategoryPlot plot = barChart.getCategoryPlot();

		((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
		plot.getDomainAxis().setVisible(false);

		barChart.setBackgroundPaint(Color.decode("#D6D9DF"));

		CategoryItemRenderer CatRenderer = ((CategoryPlot) barChart.getPlot()).getRenderer();
		CatRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		ChartPanel chartPanel = new ChartPanel( barChart );
		chartPanel.setLayout(null);
		chartPanel.setPreferredSize(new Dimension(771, 411));

		return chartPanel;
	}
}

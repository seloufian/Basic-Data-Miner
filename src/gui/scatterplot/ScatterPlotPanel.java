package gui.scatterplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import gui.Frame;
import main.DataSet;
import main.Instance;

public class ScatterPlotPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel scatterPlotPanel;
	private JComboBox<String> axisXComboBox;
	private JComboBox<String> axisYComboBox;
	private JLabel statusBarLabel;
	private Frame parentFrame;


	public ScatterPlotPanel(Frame parentFrame) {
		this.parentFrame = parentFrame;

		setBounds(parentFrame.getBounds());
		setLayout(null);

		JLabel attrSelectionLabel = new JLabel("Axis attributes selection :");
		attrSelectionLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		attrSelectionLabel.setForeground(Color.decode("#404040"));
		attrSelectionLabel.setBounds(87, 4, 185, 20);
		add(attrSelectionLabel);

		JPanel attrSelectionPanel = new JPanel();
		attrSelectionPanel.setBorder(new LineBorder(new Color(128, 128, 128), 2, true));
		attrSelectionPanel.setBounds(85, 25, 631, 80);
		add(attrSelectionPanel);
		attrSelectionPanel.setLayout(null);

		JLabel axisXLabel = new JLabel("\u2022 Attribute 1 (axis X) :");
		axisXLabel.setBounds(12, 10, 123, 25);
		attrSelectionPanel.add(axisXLabel);

		axisXComboBox = new JComboBox<String>();
		axisXComboBox.setBounds(143, 10, 240, 26);
		attrSelectionPanel.add(axisXComboBox);

		JLabel axisYLabel = new JLabel("\u2022 Attribute 2 (axis Y) :");
		axisYLabel.setBounds(12, 45, 123, 25);
		attrSelectionPanel.add(axisYLabel);

		axisYComboBox = new JComboBox<String>();
		axisYComboBox.setBounds(143, 45, 240, 26);
		attrSelectionPanel.add(axisYComboBox);

		JButton drawScatterPlotBtn = new JButton("Draw scatter plot");
		drawScatterPlotBtn.setBounds(450, 20, 147, 40);
		drawScatterPlotBtn.setEnabled(false);
		attrSelectionPanel.add(drawScatterPlotBtn);

		scatterPlotPanel = new JPanel();
		scatterPlotPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		scatterPlotPanel.setBounds(6, 112, 781, 360);
		add(scatterPlotPanel);

		statusBarLabel = new JLabel("Choose \"attribute 1\" (on X axis) and \"attribute 2\" (on Y axis)..");
		statusBarLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		statusBarLabel.setForeground(Color.decode("#404040"));
		statusBarLabel.setBounds(6, 476, 724, 25);
		add(statusBarLabel);


		axisXComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String initialSelect = "- - - - - -  Select an attribute  - - - - - - -";

				if(axisXComboBox.getSelectedItem().equals(initialSelect))
					return;

				axisXComboBox.removeItem(initialSelect);

				if(axisXComboBox.getItemAt(0).equals(initialSelect)
						|| axisYComboBox.getItemAt(0).equals(initialSelect)
						|| (axisXComboBox.getSelectedIndex() == axisYComboBox.getSelectedIndex()))
					drawScatterPlotBtn.setEnabled(false);
				else
					drawScatterPlotBtn.setEnabled(true);
			}
		});

		axisXComboBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					drawScatterPlotBtn.doClick();
			}
		});


		axisYComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String initialSelect = "- - - - - -  Select an attribute  - - - - - - -";

				if(axisYComboBox.getSelectedItem().equals(initialSelect))
					return;

				axisYComboBox.removeItem(initialSelect);

				if(axisYComboBox.getItemAt(0).equals(initialSelect)
						|| axisXComboBox.getItemAt(0).equals(initialSelect)
						|| (axisYComboBox.getSelectedIndex() == axisXComboBox.getSelectedIndex()))
					drawScatterPlotBtn.setEnabled(false);
				else
					drawScatterPlotBtn.setEnabled(true);
			}
		});

		axisYComboBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					drawScatterPlotBtn.doClick();
			}
		});


		drawScatterPlotBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataSet ds = parentFrame.getDataset();

				int axisXindex = axisXComboBox.getSelectedIndex();
				int axisYindex = axisYComboBox.getSelectedIndex();


				if(ds.getAttributeType(axisXindex).matches("numeric|binary") &&
						ds.getAttributeType(axisYindex).matches("numeric|binary")) {

					ArrayList<Float> axisXValues = new ArrayList<Float>();
					ArrayList<Float> axisYValues = new ArrayList<Float>();

					for(Instance instance : ds.getInstances()) {
						axisXValues.add(Float.parseFloat(instance.getAttribute(axisXindex).getValue()));
						axisYValues.add(Float.parseFloat(instance.getAttribute(axisYindex).getValue()));
					}

					scatterPlotPanel.removeAll();
					scatterPlotPanel.add(drawScatterPlot(axisXValues, axisYValues, ds.getAttributeName(axisXindex), ds.getAttributeName(axisYindex)));
					statusBarLabel.setText("Scatter plot for : \""+ds.getAttributeName(axisXindex)+"\" (on X axis) and \""+ds.getAttributeName(axisYindex)+"\" (on Y axis).");
				}
				else
					scatterPlotPanel.removeAll();

				scatterPlotPanel.revalidate();
			}
		});
	}


	public void updatePanel() {
		DataSet ds = parentFrame.getDataset();

		scatterPlotPanel.removeAll();
		scatterPlotPanel.revalidate();

		statusBarLabel.setText("Choose \"attribute 1\" (on X axis) and \"attribute 2\" (on Y axis)..");

		axisXComboBox.setModel(new DefaultComboBoxModel<String>());
		axisYComboBox.setModel(new DefaultComboBoxModel<String>());

		axisXComboBox.addItem("- - - - - -  Select an attribute  - - - - - - -");
		axisYComboBox.addItem("- - - - - -  Select an attribute  - - - - - - -");

		for(int i=0; i<ds.getNumAttributes()-1; i++) {
			axisXComboBox.addItem(ds.getAttributeName(i));
			axisYComboBox.addItem(ds.getAttributeName(i));
		}
	}


	public ChartPanel drawScatterPlot(ArrayList<Float> data1, ArrayList<Float> data2, String attr1Name, String attr2Name) {
		if(data1.size() != data2.size())
			return null;

		XYSeries series1 = new XYSeries("");
		XYSeriesCollection dataset = new XYSeriesCollection();

		for(int i=0; i<data1.size(); i++)
			series1.add(data1.get(i), data2.get(i));

		dataset.addSeries(series1);

		JFreeChart chart = ChartFactory.createScatterPlot("", "", "", dataset);
		chart.setBackgroundPaint(null);
		chart.removeLegend();

		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(Color.decode("#D6D9DF"));

		Shape shape  = new Ellipse2D.Double(0, 0, 7, 7);
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setDefaultShape(shape);
		renderer.setDefaultPaint(Color.red);
		renderer.setSeriesShape(0, shape);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setLayout(null);
		chartPanel.setPreferredSize(new Dimension(777, 348));

		return chartPanel;
	}
}

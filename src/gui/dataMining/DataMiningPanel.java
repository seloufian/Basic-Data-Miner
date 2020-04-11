package gui.dataMining;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dataMining.clustering.CLARANS;
import dataMining.clustering.Cluster;
import dataMining.clustering.K_MEDOIDS;
import dataMining.frequentPatterns.apriori.Apriori;
import dataMining.frequentPatterns.apriori.AssociationRules;
import dataMining.frequentPatterns.eclat.Eclat;
import gui.Frame;
import main.DataSet;
import main.Instance;

public class DataMiningPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JComboBox<String> classComboBox;

	private CardLayout paramsCardLayout;
	private FreqPatternsAssocRulesPanel freqPatternsAssocRulesPanel;
	private KMedoidsPanel kMedoidsPanel;
	private ClaransPanel claransPanel;

	private JList<String> attributeSelectList;

	private JComboBox<String> algoComboBox;

	private CardLayout resultsCardLayout;
	private AssocRulesResultPanel assocRulesResultPanel;
	private ClusteringResultPanel clusteringResultPanel;

	private JPanel paramsPanel;
	private JLabel paramsLabel;

	private JTextField execTimeField;

	protected Frame parentFrame;


	public DataMiningPanel(Frame parentFrame) {
		this.parentFrame = parentFrame;

		setBounds(parentFrame.getBounds());
		setLayout(null);

		JLabel dmAlgoLabel = new JLabel("Data Mining algorithm :");
		dmAlgoLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		dmAlgoLabel.setForeground(Color.decode("#404040"));
		dmAlgoLabel.setBounds(11, 3, 262, 19);
		add(dmAlgoLabel);

		JPanel dmAlgoPanel = new JPanel();
		dmAlgoPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		dmAlgoPanel.setBounds(9, 23, 268, 116);
		add(dmAlgoPanel);
		dmAlgoPanel.setLayout(null);

		classComboBox = new JComboBox<String>();
		classComboBox.setBounds(7, 22, 254, 26);
		dmAlgoPanel.add(classComboBox);

		JLabel algoLabel = new JLabel("\u2022 Algorithm :");
		algoLabel.setBounds(27, 69, 77, 16);
		dmAlgoPanel.add(algoLabel);

		algoComboBox = new JComboBox<String>();
		algoComboBox.setBounds(104, 64, 133, 26);
		algoComboBox.setEnabled(false);
		dmAlgoPanel.add(algoComboBox);


		paramsCardLayout = new CardLayout();

		paramsLabel = new JLabel("Parameters :");
		paramsLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		paramsLabel.setForeground(Color.decode("#404040"));
		paramsLabel.setBounds(293, 3, 174, 19);
		add(paramsLabel);

		paramsPanel = new JPanel(paramsCardLayout);
		paramsPanel.setBorder(new LineBorder(Color.GRAY, 2, true));
		paramsPanel.setBounds(291, 23, 183, 116);
		add(paramsPanel);

		freqPatternsAssocRulesPanel = new FreqPatternsAssocRulesPanel();
		paramsPanel.add(freqPatternsAssocRulesPanel, "0");

		kMedoidsPanel = new KMedoidsPanel();
		paramsPanel.add(kMedoidsPanel, "1");

		claransPanel = new ClaransPanel();
		paramsPanel.add(claransPanel, "2");


		JButton startMiningBtn = new JButton("Start mining");
		startMiningBtn.setBounds(621, 360, 127, 46);
		startMiningBtn.setEnabled(false);
		add(startMiningBtn);


		resultsCardLayout = new CardLayout();

		JLabel resultsLabel = new JLabel("Results :");
		resultsLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		resultsLabel.setForeground(Color.decode("#404040"));
		resultsLabel.setBounds(11, 145, 608, 19);
		add(resultsLabel);

		JPanel resultsPanel = new JPanel(resultsCardLayout);
		resultsPanel.setBounds(8, 163, 563, 310);
		add(resultsPanel);

		assocRulesResultPanel = new AssocRulesResultPanel();
		resultsPanel.add(assocRulesResultPanel, "0");

		clusteringResultPanel = new ClusteringResultPanel(this);
		resultsPanel.add(clusteringResultPanel, "1");


		JLabel execTimeLabel = new JLabel("\u2022 Execution time (ms) :");
		execTimeLabel.setBounds(584, 477, 134, 16);
		add(execTimeLabel);

		execTimeField = new JTextField();
		execTimeField.setEditable(false);
		execTimeField.setBounds(719, 472, 68, 28);
		add(execTimeField);
		execTimeField.setColumns(10);

		JLabel statusBarLabel = new JLabel("Select a Data Mining algorithm and adjust its parameters..");
		statusBarLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		statusBarLabel.setForeground(Color.decode("#404040"));
		statusBarLabel.setBounds(6, 476, 568, 25);
		add(statusBarLabel);

		JLabel attributeSelectLabel = new JLabel("Attributes selection :");
		attributeSelectLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		attributeSelectLabel.setForeground(Color.decode("#404040"));
		attributeSelectLabel.setBounds(585, 3, 143, 19);
		add(attributeSelectLabel);

		attributeSelectList = new JList<String>();
		attributeSelectList.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(attributeSelectList);
		attributeSelectList.setLayoutOrientation(JList.VERTICAL);
		scrollPane.setBorder(new LineBorder(Color.GRAY, 2, true));
		scrollPane.setBounds(583, 23, 200, 310);
		add(scrollPane);


		attributeSelectList.setSelectionModel(new DefaultListSelectionModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				if(attributeSelectList.isSelectedIndex(index0))
					attributeSelectList.removeSelectionInterval(index0, index1);
				else
					attributeSelectList.addSelectionInterval(index0, index1);
			}
		});


		attributeSelectList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (! e.getValueIsAdjusting()) {
					int[] selectedAttributes = attributeSelectList.getSelectedIndices();
					startMiningBtn.setEnabled(selectedAttributes.length > 1 ? true : false);
				}
			}
		});


		classComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() != ItemEvent.SELECTED)
					return;

				String initialSelect = "-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -";

				if(classComboBox.getSelectedItem().equals(initialSelect))
					return;

				classComboBox.removeItem(initialSelect);


				attributeSelectList.clearSelection();

				if(classComboBox.getSelectedItem().equals("Frequent Patterns & Association Rules")) {
					algoComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Apriori", "Eclat"}));
					paramsPanel.setVisible(true);
					paramsCardLayout.show(paramsPanel, "0");

					attributeSelectList.setEnabled(true);
					startMiningBtn.setEnabled(false);
				}
				else {
					paramsLabel.setVisible(true);
					paramsPanel.setVisible(true);
					paramsCardLayout.show(paramsPanel, "1");
					algoComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"K-medoids", "CLARANS"}));

					attributeSelectList.setEnabled(false);
					startMiningBtn.setEnabled(true);
				}

				algoComboBox.setEnabled(true);
			}
		});


		algoComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(classComboBox.getSelectedItem().equals("Unsupervised Classification")) {
					int selectedAlgo = algoComboBox.getSelectedIndex();
					paramsCardLayout.show(paramsPanel, String.valueOf(selectedAlgo+1));
				}
				if(classComboBox.getSelectedItem().equals("Frequent Patterns & Association Rules")) {
					paramsCardLayout.show(paramsPanel, "0");
				}
			}
		});


		startMiningBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArrayList<Integer> selectedAttributes = new ArrayList<Integer>();
				for(int index : attributeSelectList.getSelectedIndices())
					selectedAttributes.add(index);

				String classSelectedCategory = classComboBox.getSelectedItem().toString();
				int algoSelectedIndex = algoComboBox.getSelectedIndex();

				long elapsedTime, startTime;

				if(classSelectedCategory.equals("Frequent Patterns & Association Rules")) { /* "Frequent Patterns & Association Rules" class selected */
					AssociationRules assocRules = null;
					int minSupportCount = freqPatternsAssocRulesPanel.getMinSuppCount();
					int minConfidenceThreshold = freqPatternsAssocRulesPanel.getMinConfThreshold();

					if(algoSelectedIndex == 0) { /* "Apriori" algorithm selected */
						startTime = System.currentTimeMillis();
						assocRules = Apriori.apriori(parentFrame.getDataset(), selectedAttributes, minSupportCount, minConfidenceThreshold);
						elapsedTime = System.currentTimeMillis() - startTime;
					}
					else { /* "Eclat" algorithm selected */
						startTime = System.currentTimeMillis();
						assocRules = Eclat.eclat(parentFrame.getDataset(), selectedAttributes, minSupportCount, minConfidenceThreshold);
						elapsedTime = System.currentTimeMillis() - startTime;
					}

					if(assocRules != null) {
						resultsCardLayout.show(resultsPanel, "0");
						assocRulesResultPanel.fillAssocRules(assocRules);
						statusBarLabel.setText("Association Rules generated (using \"" + algoComboBox.getSelectedItem() + " algorithm\").");
					}
				}else { /* "Unsupervised Classification" class selected */
					ArrayList<Cluster<Instance>> clusters = null;

					if(algoSelectedIndex == 0) { /* "K-Medoids" algorithm selected */
						startTime = System.currentTimeMillis();
						clusters = K_MEDOIDS.clustering(parentFrame.getDataset().getInstances(), kMedoidsPanel.getNumClusters());
						elapsedTime = System.currentTimeMillis() - startTime;
					}else { /* "CLARANS" algorithm selected */
						int numClusters = claransPanel.getNumClusters();
						int localMin = claransPanel.getLocalMin();
						int maxNeighbor = claransPanel.getMaxNeighbor();

						startTime = System.currentTimeMillis();
						clusters = CLARANS.clustering(parentFrame.getDataset().getInstances(), numClusters, localMin, maxNeighbor);
						elapsedTime = System.currentTimeMillis() - startTime;
					}

					if(clusters != null) {
						resultsCardLayout.show(resultsPanel, "1");
						clusteringResultPanel.fillClusters(clusters);
						statusBarLabel.setText("Clusters generated (using \"" + algoComboBox.getSelectedItem() + " algorithm\").");
					}
				}

				execTimeField.setText(String.valueOf(elapsedTime));
			}
		});
	}


	public void updatePanel(int index) {
		if (index == 0) {
			classComboBox.setModel(new DefaultComboBoxModel<String>());
			classComboBox.addItem("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
			classComboBox.addItem("Unsupervised Classification");

			paramsPanel.setVisible(false);
			paramsLabel.setVisible(false);

			algoComboBox.setEnabled(false);

			DefaultListModel<String> listModel = new DefaultListModel<String>();

			attributeSelectList.setModel(listModel);
		}else {
			classComboBox.addItem("Frequent Patterns & Association Rules");

			DefaultListModel<String> listModel = new DefaultListModel<String>();

			DataSet ds = parentFrame.getDataset();

			for(int i=0; i<ds.getNumAttributes()-1; i++)
				listModel.addElement((i+1) + ") " + ds.getAttributeName(i));

			attributeSelectList.setModel(listModel);
		}
	}
}

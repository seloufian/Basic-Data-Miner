package dataMining.frequentPatterns.apriori;

import java.util.ArrayList;

import main.DataSet;

public class AssociationRules {
	private ArrayList<Rule> rules = new ArrayList<Rule>();


	public ArrayList<Rule> getRules() { return rules; }


	public int numRules() {
		return rules.size();
	}


	public Rule getRule(int position) {
		if(position >= numRules() || position < 0)
			return null;

		return rules.get(position);
	}


	public void addRule(Rule rule) {
		if(! rules.contains(rule))
			rules.add(rule);
	}


	public void generateAssociationRules(DataSet ds, ArrayList<FrequentItemsets> frequentItemsetsList, int minConfidenceThreshold) {
		for(FrequentItemsets freqItemsets : frequentItemsetsList) {
			for(int i=0; i<freqItemsets.numItemsets(); i++) {
				Itemset currItemset = freqItemsets.getItemset(i);

				int suppNominator = freqItemsets.getSupportCount(currItemset);

				ArrayList<Itemset> allSubItemsets = currItemset.getAllSubItemsets();

				for(Itemset subItemset : allSubItemsets) {
					Itemset consequent = currItemset.substract(subItemset);

					Integer suppDenominator = frequentItemsetsList.get(subItemset.numItems()-1).getSupportCount(subItemset);

					if(suppDenominator == null)
						continue;

					int confidence = (int) ((1.0 * suppNominator / suppDenominator) * 100.0);

					if(confidence >= minConfidenceThreshold) {
						Itemset subItemsetTrans = new Itemset(), consequentTrans = new Itemset();

						for(String item : subItemset.getItems())
							subItemsetTrans.addItem(ds.translateFromLabel(item));

						for(String item : consequent.getItems())
							consequentTrans.addItem(ds.translateFromLabel(item));

						addRule(new Rule(subItemsetTrans, consequentTrans, confidence));
					}
				}
			}
		}
	}


	@Override
	public String toString() {
		return "AssociationRules [rules=" + rules + "]";
	}
}

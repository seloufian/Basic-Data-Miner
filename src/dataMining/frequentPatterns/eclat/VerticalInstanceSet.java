package dataMining.frequentPatterns.eclat;

import java.util.ArrayList;
import java.util.HashMap;

import dataMining.frequentPatterns.apriori.Candidate;
import dataMining.frequentPatterns.apriori.Itemset;


public class VerticalInstanceSet {
	private HashMap<Itemset, Candidate> instances = new HashMap<Itemset, Candidate>();
	private ArrayList<Itemset> itemsets = new ArrayList<Itemset>();


	public HashMap<Itemset, Candidate> getInstances() { return instances; }
	public ArrayList<Itemset> getItemsets() { return itemsets; }


	public VerticalInstanceSet() { }


	public VerticalInstanceSet(VerticalInstanceSet anotherVerticalInstanceSet) {
		for(Itemset itemset : anotherVerticalInstanceSet.getItemsets()) {
			instances.put(itemset, anotherVerticalInstanceSet.getCandidate(itemset));
			itemsets.add(itemset);
		}
	}


	public int numInstances() {
		return itemsets.size();
	}


	public boolean isEmpty() {
		return itemsets.isEmpty();
	}


	public int numItemsPerItemset() {
		return (numInstances() == 0) ? 0 : getItemset(0).numItems();
	}


	public Itemset getItemset(int position) {
		if(position >= numInstances() || position < 0)
			return null;

		return itemsets.get(position);
	}


	public Candidate getCandidate(int position) {
		if(position >= numInstances() || position < 0)
			return null;

		return instances.get(getItemset(position));
	}


	public Candidate getCandidate(Itemset itemset) {
		return instances.get(itemset);
	}


	public Candidate getCandidate(String item) {
		Itemset currItemset = new Itemset();
		currItemset.addItem(item);

		return instances.get(currItemset);
	}


	public void addInstance(Itemset itemset, Candidate candidate) {
		Candidate cand = instances.get(itemset);

		if(cand != null) {
			for(String item : candidate.getItemset().getItems())
				cand.addItem(item);
			cand.setSupportCount(cand.getItemset().numItems());
		}else {
			instances.put(itemset, candidate);
			itemsets.add(itemset);
		}
	}


	public void addInstance(String attributeLabel, int numIstance) {
		Itemset attrLabelItem = new Itemset();
		attrLabelItem.addItem(attributeLabel);

		Candidate currCandidate = instances.get(attrLabelItem);

		if(currCandidate != null) {
			currCandidate.addItem(Integer.toString(numIstance));
			currCandidate.incrementSupportCount();
		}
		else {
			Itemset numInstItem = new Itemset();
			numInstItem.addItem(Integer.toString(numIstance));

			currCandidate = new Candidate(numInstItem);
			currCandidate.incrementSupportCount();

			instances.put(attrLabelItem, currCandidate);
			itemsets.add(attrLabelItem);
		}
	}


	public void keepOnlySatisfiesMinSupport(int minSupportCount) {
		ArrayList<Itemset> removeItemsets = new ArrayList<Itemset>();

		for(int i=0; i<numInstances(); i++) {
			if(! instances.get(getItemset(i)).satisfiesMinSupportCount(minSupportCount)) {
				removeItemsets.add(getItemset(i));
				instances.remove(getItemset(i));
			}
		}

		for(Itemset itemset : removeItemsets)
			itemsets.remove(itemset);
	}


	public boolean contains(Itemset itemset) {
		return itemsets.contains(itemset);
	}


	@Override
	public String toString() {
		return "VerticalInstanceSet [instances=" + instances + "]";
	}
}

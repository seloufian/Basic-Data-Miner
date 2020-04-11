package dataMining.frequentPatterns.apriori;

import java.util.ArrayList;
import java.util.HashMap;

import dataMining.frequentPatterns.eclat.VerticalInstanceSet;

public class FrequentItemsets {
	private HashMap<Itemset, Integer> itemsetsMap = new HashMap<Itemset, Integer>();
	private ArrayList<Itemset> itemsets = new ArrayList<Itemset>();


	public FrequentItemsets(CandidateItemsets candidateItemsets, int minSupportCount) {
		for(int i=0; i<candidateItemsets.numCandidates(); i++) {
			Candidate cand = candidateItemsets.getCandidate(i);

			if(cand.satisfiesMinSupportCount(minSupportCount)) {
				itemsetsMap.put(cand.getItemset(), cand.getSupportCount());
				itemsets.add(cand.getItemset());
			}
		}
	}


	public FrequentItemsets(FrequentItemsets otherFrequentItemsets) {
		for(Itemset itemset : otherFrequentItemsets.getItemsets()) {
			itemsetsMap.put(itemset, otherFrequentItemsets.getItemsetsMap().get(itemset));
			itemsets.add(itemset);
		}
	}


	public FrequentItemsets(VerticalInstanceSet vetricaInstanceSet) {
		for(int i=0; i<vetricaInstanceSet.numInstances(); i++) {
			itemsetsMap.put(vetricaInstanceSet.getItemset(i), vetricaInstanceSet.getCandidate(i).getSupportCount());
			itemsets.add(vetricaInstanceSet.getItemset(i));
		}
	}


	public HashMap<Itemset, Integer> getItemsetsMap() { return itemsetsMap; }
	public ArrayList<Itemset> getItemsets() { return itemsets; }


	public int numItemsets() {
		return itemsets.size();
	}


	public boolean isEmpty() {
		return itemsets.isEmpty();
	}


	public Itemset getItemset(int position) {
		if(position >= numItemsets() || position < 0)
			return null;

		return itemsets.get(position);
	}


	public Integer getSupportCount(int position) {
		if(position >= numItemsets() || position < 0)
			return null;

		return itemsetsMap.get(itemsets.get(position));
	}


	public Integer getSupportCount(Itemset itemset) {
		return itemsetsMap.get(itemset);
	}


	public boolean contains(Itemset itemset) {
		for(Itemset itemS : itemsets) {
			if(itemS.equals(itemset))
				return true;
		}

		return false;
	}


	@Override
	public String toString() {
		return "FrequentItemsets [itemsets=" + itemsets + "]";
	}
}

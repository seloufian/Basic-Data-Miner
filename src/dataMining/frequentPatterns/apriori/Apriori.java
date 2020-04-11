package dataMining.frequentPatterns.apriori;

import java.util.ArrayList;

import main.DataSet;

public class Apriori {

	public static AssociationRules apriori(DataSet ds, ArrayList<Integer> positions, int minSupportCount, int minConfidenceThreshold) {
		ArrayList<FrequentItemsets> FrequentItemsetsList = new ArrayList<FrequentItemsets>();

		CandidateItemsets candidates = new CandidateItemsets();

		for(int i=0; i<ds.getNumInstances(); i++) {
			ArrayList<String> currInstanceLabels = ds.getIntance(i).getAttributes_discLabels();

			for(Integer position : positions) {
				Itemset itemset = new Itemset();
				itemset.addItem(currInstanceLabels.get(position));
				candidates.updateCandidates(new Candidate(itemset));
			}
		}

		FrequentItemsets currL = new FrequentItemsets(new FrequentItemsets(candidates, minSupportCount));

		while(! currL.isEmpty()) {
			FrequentItemsetsList.add(new FrequentItemsets(currL));

			candidates = aprioriGen(currL);

			candidates.calculateSupports(ds);

			currL = new FrequentItemsets(candidates, minSupportCount);
		}

		AssociationRules assocRules = new AssociationRules();
		assocRules.generateAssociationRules(ds, FrequentItemsetsList, minConfidenceThreshold);

		return assocRules;
	}


	private static CandidateItemsets aprioriGen(FrequentItemsets l) {
		CandidateItemsets candidates = new CandidateItemsets();

		for(int i=0; i<l.numItemsets(); i++) {
			Itemset firstItemset = l.getItemset(i);

			for(int j=0; j<l.numItemsets(); j++) {
				Itemset secondItemset = l.getItemset(j);

				for(int k=0; k<secondItemset.numItems(); k++) {
					Itemset currItemset = new Itemset(firstItemset);

					currItemset.addItem(secondItemset.getItem(k));

					if(! hasInfrequentSubset(currItemset, l))
						candidates.addCandidate(new Candidate(currItemset));
				}
			}
		}

		return candidates;
	}


	private static boolean hasInfrequentSubset(Itemset itemset, FrequentItemsets l) {
		int size = itemset.numItems();

		Itemset subItemset = new Itemset(size-1);

		return ! combinations(itemset, size-1, 0, subItemset, l);
	}


	private static boolean combinations(Itemset itemset, int len, int startPosition, Itemset subItemset, FrequentItemsets l){
		if (len == 0)
			return l.contains(subItemset);

		boolean frequent = true;

		for (int i=startPosition; i <= itemset.numItems()-len; i++){
			subItemset.relaceItem(subItemset.numItems() - len, itemset.getItem(i));

			frequent = combinations(itemset, len-1, i+1, subItemset, l);

			if(! frequent)
				return false;
		}

		return frequent;
	}
}

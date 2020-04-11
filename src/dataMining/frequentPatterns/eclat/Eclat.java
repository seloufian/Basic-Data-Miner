package dataMining.frequentPatterns.eclat;

import java.util.ArrayList;

import dataMining.frequentPatterns.apriori.AssociationRules;
import dataMining.frequentPatterns.apriori.Candidate;
import dataMining.frequentPatterns.apriori.FrequentItemsets;
import dataMining.frequentPatterns.apriori.Itemset;
import main.DataSet;

public class Eclat {

	public static AssociationRules eclat(DataSet ds, ArrayList<Integer> positions, int minSupportCount, int minConfidenceThreshold) {

		ArrayList<VerticalInstanceSet> frequentItemsetsList = new ArrayList<VerticalInstanceSet>();

		VerticalInstanceSet frequentItemsets = new VerticalInstanceSet();

		for(int i=0; i<ds.getNumInstances(); i++) {
			ArrayList<String> currInstanceLabels = ds.getIntance(i).getAttributes_discLabels();

			for(Integer position : positions)
				frequentItemsets.addInstance(currInstanceLabels.get(position), i);
		}

		frequentItemsets.keepOnlySatisfiesMinSupport(minSupportCount);

		VerticalInstanceSet initL = new VerticalInstanceSet(frequentItemsets);
		VerticalInstanceSet currL = new VerticalInstanceSet(frequentItemsets);

		while(! currL.isEmpty()) {
			frequentItemsetsList.add(new VerticalInstanceSet(currL));

			currL = eclatGen(initL, currL);

			currL.keepOnlySatisfiesMinSupport(minSupportCount);
		}

		ArrayList<FrequentItemsets> frequentItemsetsListTemp = new ArrayList<FrequentItemsets>();
		for(VerticalInstanceSet currInstance : frequentItemsetsList)
			frequentItemsetsListTemp.add(new FrequentItemsets(currInstance));

		AssociationRules assocRules = new AssociationRules();
		assocRules.generateAssociationRules(ds, frequentItemsetsListTemp, minConfidenceThreshold);

		return assocRules;
	}


	private static VerticalInstanceSet eclatGen(VerticalInstanceSet initL, VerticalInstanceSet currL) {
		VerticalInstanceSet resL = new VerticalInstanceSet();

		for(int i=0; i<currL.numInstances(); i++) {

			for(Itemset itemset : currL.getItemsets()) {

				for(String item : itemset.getItems()) {
					Itemset unionItems = currL.getItemset(i).union(item);

					if(unionItems.numItems() == itemset.numItems() || resL.contains(unionItems))
						continue;

					boolean infreqSubsets = hasInfrequentSubset(unionItems, currL);

					if(! infreqSubsets) {
						Candidate interCands = currL.getCandidate(i).intersect(initL.getCandidate(item));

						if(! interCands.getItemset().isEmpty())
							resL.addInstance(unionItems, interCands);
					}
				}
			}
		}

		return resL;
	}


	private static boolean hasInfrequentSubset(Itemset itemset, VerticalInstanceSet currL) {
		int size = itemset.numItems();

		Itemset subItemset = new Itemset(size-1);

		return ! combinations(itemset, size-1, 0, subItemset, currL);
	}


	private static boolean combinations(Itemset itemset, int len, int startPosition, Itemset subItemset, VerticalInstanceSet currL){
		if (len == 0)
			return currL.contains(subItemset);

		boolean frequent = true;

		for (int i=startPosition; i <= itemset.numItems()-len; i++){
			subItemset.relaceItem(subItemset.numItems() - len, itemset.getItem(i));

			frequent = combinations(itemset, len-1, i+1, subItemset, currL);

			if(! frequent)
				return false;
		}

		return frequent;
	}
}

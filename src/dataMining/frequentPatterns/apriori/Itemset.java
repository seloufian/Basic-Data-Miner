package dataMining.frequentPatterns.apriori;

import java.util.ArrayList;
import java.util.Arrays;

public class Itemset {
	private ArrayList<String> items = new ArrayList<String>();


	public Itemset() { }


	public Itemset(int initNum) {
		items = new ArrayList<String>(Arrays.asList(new String[initNum]));
	}


	public Itemset(Itemset otherItemset) {
		for(String item : otherItemset.getItems())
			items.add(item);
	}


	public ArrayList<String> getItems() { return items; }


	public void addItem(String item) {
		if(! items.contains(item))
			items.add(item);
	}


	public int numItems() {
		return items.size();
	}


	public boolean isEmpty() {
		return items.isEmpty();
	}


	public boolean contains(String item) {
		return items.contains(item);
	}


	public String getItem(int position) {
		if(position >= numItems() || position < 0)
			return null;

		return items.get(position);
	}


	public void removeItem(String item) {
		items.remove(item);
	}


	public void relaceItem(int position, String newItem) {
		if(position >= numItems() || position < 0)
			return;

		items.set(position, newItem);
	}


	public Itemset union(Itemset otherItemset) {
		Itemset currItemset = new Itemset(this);

		for(String item : otherItemset.getItems())
			currItemset.addItem(item);

		return currItemset;
	}


	public Itemset union(String item) {
		Itemset currItemset = new Itemset(this);

		currItemset.addItem(item);

		return currItemset;
	}


	public Itemset intersect(Itemset otherItemset) {
		Itemset resItemset = new Itemset(this);

		resItemset.items.retainAll(otherItemset.items);

		return resItemset;
	}


	public Itemset substract(Itemset otherItemset) {
		Itemset currItemset = new Itemset(this);

		for(String item : otherItemset.getItems())
			currItemset.removeItem(item);

		return currItemset;
	}


	public ArrayList<Itemset> getAllSubItemsets(){
		ArrayList<Itemset> allSubsets = new ArrayList<Itemset>();

		int n = numItems();

		for (int i = 0; i < (1<<n); i++){
			Itemset currSubset = new Itemset();

			for (int j = 0; j < n; j++)
				if ((i & (1 << j)) > 0)
					currSubset.addItem(getItem(j));

			if(! currSubset.isEmpty() && currSubset.numItems() != n)
				allSubsets.add(currSubset);
		}

		return allSubsets;
	}


	@Override
	public int hashCode() {
		String itemsStr = "";

		for(String item : items)
			itemsStr += item;

		return itemsStr.hashCode();
	}


	@Override
	public boolean equals(Object otherItemset) {
		if (this == otherItemset)
			return true;

		if (otherItemset == null || getClass() != otherItemset.getClass())
			return false;

		Itemset other = (Itemset) otherItemset;

		if(items.size() != other.getItems().size())
			return false;

		for(String item : items)
			if(! other.getItems().contains(item))
				return false;

		for(String item : other.getItems())
			if(! items.contains(item))
				return false;

		return true;
	}


	@Override
	public String toString() {
		return "Itemset [items=" + items + "]";
	}
}

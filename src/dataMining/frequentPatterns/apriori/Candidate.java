package dataMining.frequentPatterns.apriori;

public class Candidate {
	private Itemset itemset;
	private int supportCount;


	public Candidate(Itemset itemset) {
		this.itemset = itemset;
		supportCount = 0;
	}

	public Candidate(Itemset itemset, int supportCount) {
		this.itemset = itemset;
		this.supportCount = supportCount;
	}


	public Itemset getItemset() { return itemset; }
	public int getSupportCount() { return supportCount; }

	public void setSupportCount(int supportCount) { this.supportCount = supportCount; }


	public void incrementSupportCount() {
		supportCount++;
	}


	public boolean satisfiesMinSupportCount(int minSupportCount) {
		if(supportCount >= minSupportCount)
			return true;

		return false;
	}


	public void addItem(String item) {
		itemset.addItem(item);
	}


	public Candidate intersect(Candidate otherCandidate) {
		Candidate resCandidate = new Candidate(getItemset().intersect(otherCandidate.getItemset()));

		resCandidate.setSupportCount(resCandidate.getItemset().numItems());

		return resCandidate;
	}


	@Override
	public boolean equals(Object otherCandidate) {
		if (this == otherCandidate)
			return true;

		if (otherCandidate == null || getClass() != otherCandidate.getClass())
			return false;

		Candidate other = (Candidate) otherCandidate;

		return itemset.equals(other.getItemset());
	}


	@Override
	public String toString() {
		return "Candidate [itemset=" + itemset + ", supportCount=" + supportCount + "]";
	}
}

package dataMining.frequentPatterns.apriori;

public class Rule {
	private Itemset conditions, consequences;
	private int confidence;


	public Rule(Itemset conditions, Itemset consequences, int confidence) {
		this.conditions = conditions;
		this.consequences = consequences;
		this.confidence = confidence;
	}


	public Itemset getConditions() { return conditions; }
	public Itemset getConsequences() { return consequences; }
	public int getConfidence() { return confidence; }


	@Override
	public boolean equals(Object otherRule) {
		if (this == otherRule)
			return true;

		if (otherRule == null || getClass() != otherRule.getClass())
			return false;

		Rule other = (Rule) otherRule;

		return conditions.equals(other.getConditions()) && consequences.equals(other.getConsequences());
	}


	@Override
	public String toString() {
		return "Rule [conditions=" + conditions + ", consequences=" + consequences + ", confidence=" + confidence + "]";
	}
}

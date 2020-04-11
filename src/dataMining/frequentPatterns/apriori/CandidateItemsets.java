package dataMining.frequentPatterns.apriori;

import java.util.ArrayList;

import main.DataSet;

class CandidateItemsets {
	private ArrayList<Candidate> candidates = new ArrayList<Candidate>();


	public ArrayList<Candidate> getCandidates() { return candidates; }


	public int numCandidates() {
		return candidates.size();
	}


	public Candidate getCandidate(int position) {
		if(position >= numCandidates() || position < 0)
			return null;

		return candidates.get(position);
	}


	public void clearAllCandidates() {
		candidates.clear();
	}


	public void addCandidate(Candidate candidate) {
		if(! candidates.contains(candidate))
			candidates.add(candidate);
	}


	public void updateCandidates(Candidate candidate) {
		int candIndex = candidates.indexOf(candidate);

		if(candIndex == -1) {
			candidate.incrementSupportCount();
			candidates.add(candidate);
		}
		else {
			Candidate cand = candidates.get(candIndex);
			cand.incrementSupportCount();
			candidates.set(candIndex, cand);
		}
	}


	public void calculateSupports(DataSet ds) {
		for(int i=0; i<ds.getNumInstances(); i++) {
			ArrayList<String> currInstanceLabels = ds.getIntance(i).getAttributes_discLabels();

			for(int j=0; j<numCandidates(); j++) {
				Candidate currCandidate = getCandidate(j);

				boolean candidateExist = true;

				for(String item : currCandidate.getItemset().getItems())
					if(! currInstanceLabels.contains(item)) {
						candidateExist = false;
						break;
					}

				if(candidateExist)
					currCandidate.incrementSupportCount();
			}
		}
	}


	@Override
	public String toString() {
		return "CandidateItemsets [candidates=" + candidates + "]";
	}
}

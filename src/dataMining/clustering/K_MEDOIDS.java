package dataMining.clustering;

import java.util.ArrayList;

public class K_MEDOIDS {

	public static <T> ArrayList<Cluster<T>> clustering(ArrayList<T> dataset, int nb_clusters){
		/*
		 * Parameters :
		 * 		nb_clusters : the number of clusters that will be generated
		 *
		 * Returns :
		 * 		A list containing nb_clusters generated using the K-Medoids method
		 */

		// Define clusters
		ArrayList<Cluster<T>> clusters = new ArrayList<Cluster<T>>();

		ArrayList<T> current_medoids = UtilMethods.initializeMedoids(dataset, nb_clusters), new_medoids = new ArrayList<T>();

		// Prohibited_objects : the selected objects until now.
		ArrayList<T> prohibited_objects = new ArrayList<T>();
		for (T medoid : current_medoids)
			prohibited_objects.add(medoid);

		// Calculate the current cost (the absolute error
		double absolute_error_current_medoids = UtilMethods.get_absolute_error(dataset, current_medoids), absolute_error_replacing_oi_by_oh;
		boolean change;
		int index_Oi;

		do {
			change = false;

			index_Oi = 0;

			while(index_Oi < current_medoids.size()) {
				new_medoids.clear();

				for (T medoid : current_medoids)
					new_medoids.add(medoid);

				// For each Oh (not selected yet), compare the new cost(when replacing Oi by Oh) with
				// The old cost, if it is best then replace Obest(firstly is Oi) by Oh
				for (T oh : dataset) {
					// If oh is already selected or not?
					if (prohibited_objects.contains(oh))
						continue; // object treated

					// Replace Obest(Oi at the first time) by Oi
					new_medoids.set(index_Oi, oh);

					// The new cost(absolute error)
					absolute_error_replacing_oi_by_oh = UtilMethods.get_absolute_error(dataset, new_medoids);

					// Compare the new cost with the old one
					if(absolute_error_replacing_oi_by_oh < absolute_error_current_medoids) {
						// If the new cost is better then : 1- there is a change. 2- replace Obest (Oi at the first time) by Oh
						change = true;
						absolute_error_current_medoids = absolute_error_replacing_oi_by_oh;
						current_medoids.set(index_Oi, oh);
					}
				}

				// Add Obest(!= from Oi) to the selected objects
				if(change)
					prohibited_objects.add(current_medoids.get(index_Oi));

				index_Oi++;
			}

		} while(change);

		// Initialize each cluster with a representative item
		for (int i=0; i<nb_clusters; i++)
			clusters.add(new Cluster<T>(current_medoids.get(i)));

		// Fill in the clusters by the non-selected objects
		UtilMethods.fill_in_clusters(dataset, clusters, current_medoids);

		return clusters;
	}
}

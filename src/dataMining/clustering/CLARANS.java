package dataMining.clustering;

import java.util.ArrayList;

public class CLARANS {

	public static <T> ArrayList<Cluster<T>> clustering(ArrayList<T> dataset, int nb_clusters, int numlocal, int maxneighbor){

		ArrayList<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
		ArrayList<T> best_medoids = null, current_medoids = null, random_neighbor = null;

		double min_cost = 100000, absolute_error_current_medoids, absolute_error_random_neighbor;

		for (int i=0; i<numlocal; i++){
			current_medoids = UtilMethods.initializeMedoids(dataset, nb_clusters);
			absolute_error_current_medoids = UtilMethods.get_absolute_error(dataset, current_medoids);

			int j = 1;
			while(true) {
				random_neighbor = UtilMethods.getRandomNeighbor(dataset, current_medoids);
				absolute_error_random_neighbor = UtilMethods.get_absolute_error(dataset, random_neighbor);

				if(absolute_error_random_neighbor < absolute_error_current_medoids) {
					current_medoids = random_neighbor;
					absolute_error_current_medoids = absolute_error_random_neighbor;
					j = 1;
					continue;
				}
				else {
					j++;
					if(j > maxneighbor)
						break;
				}
			}

			if(absolute_error_current_medoids < min_cost) {
				min_cost = absolute_error_current_medoids;
				best_medoids = current_medoids;
			}
		}

		// Initialize each cluster with a representative item
		for (int k=0; k<nb_clusters; k++)
			clusters.add(new Cluster<T>(best_medoids.get(k)));

		// Fill-in the clusters items
		UtilMethods.fill_in_clusters(dataset, clusters, best_medoids);

		return clusters;
	}
}

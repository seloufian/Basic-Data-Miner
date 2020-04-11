package dataMining.clustering;

import java.util.ArrayList;
import java.util.Random;

import main.DataSet;
import main.Instance;

public class UtilMethods {

	public static <T> double dist(T o1,T o2) {
		Instance instance1 = (Instance)o1, instance2 = (Instance)o2;

		return instance1.distance(instance2, DataSet.information_about_attributes);
	}


	public static <T> T getNewRandomObject(ArrayList<T> dataset, ArrayList<T> prohibitedObjects) {
		T newObject;
		Random random = new Random();
		int bound = dataset.size();

		while(true){
			newObject = dataset.get(random.nextInt(bound));

			if (! prohibitedObjects.contains(newObject))
				return newObject;
		}
	}


	public static <T> ArrayList<T> initializeMedoids(ArrayList<T> dataset, int nb_medoids) {
		/*
		 * Parameters :
		 * 		nb_medoids : the number of the representative items that will be returned randomly from the dataset
		 * Returns :
		 * 		A list containing the representative items of nb_medoids clusters
		 */
		ArrayList<T> medoids = new ArrayList<T>();

		for (int i=0; i<nb_medoids; i++)
			medoids.add(UtilMethods.getNewRandomObject(dataset, medoids));

		return medoids;
	}


	public static <T> double get_absolute_error(ArrayList<T> objects, ArrayList<T> medoids) {
		double absolute_error = 0;

		for (T object : objects) {
			if (! medoids.contains(object)) {
				double min_dist = 10000;

				for (T medoid : medoids) {
					double dist_object_medoid = UtilMethods.dist(object, medoid);

					if (dist_object_medoid < min_dist)
						min_dist = dist_object_medoid;
				}

				absolute_error += min_dist;
			}
		}

		return absolute_error;
	}


	public static <T> void fill_in_clusters(ArrayList<T> objects, ArrayList<Cluster<T>> clusters, ArrayList<T> medoids) {
		for (T object : objects) {
			int index = medoids.indexOf(object);

			if (index > -1) // Object is one of the medoids
				clusters.get(index).addNewItem(object);

			else {
				// Object is an element other than medoids
				// Select the index of the medoid that is much more similar to object
				double distance = 0;

				// Consider the first medoid having the minimum distance
				double min_dist = UtilMethods.dist(object, medoids.get(0));
				int index_min = 0;

				for (int i=1; i<medoids.size(); i++) {
					distance = UtilMethods.dist(object, medoids.get(i));

					if (distance < min_dist) {
						index_min = i;
						min_dist = distance;
					}
				}

				// Now, we have the medoid very similar to object. Add it to cluster that medoid is the representative item
				clusters.get(index_min).addNewItem(object);
			}
		}
	}


	public static <T> ArrayList<T> getRandomNeighbor(ArrayList<T> objects, ArrayList<T> medoids){
		Random random = new Random();
		ArrayList<T> neighbor_medoids = new ArrayList<T>();

		for (int i=0; i<medoids.size(); i++)
			neighbor_medoids.add(medoids.get(i));

		neighbor_medoids.set(random.nextInt(medoids.size()-1), getNewRandomObject(objects, medoids));

		return neighbor_medoids;
	}
}

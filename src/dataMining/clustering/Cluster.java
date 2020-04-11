package dataMining.clustering;

import java.util.ArrayList;

public class Cluster<T> {
	private ArrayList<T> cluster_items = new ArrayList<T>();
	private T medoid;


	public Cluster(T representative) {
		medoid = representative;
	}


	public ArrayList<T> getCluster_items() {return cluster_items;}
	public T getMedoid() {return medoid;}

	public void setMedoid(T medoid) {this.medoid = medoid;}


	public void addNewItem(T object) {
		cluster_items.add(object);
	}


	public void clearItems() {
		cluster_items.clear();
	}
}

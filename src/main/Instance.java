package main;

import java.util.ArrayList;

import main.DataSet.InfoAttribute;

public class Instance {
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();


	public ArrayList<Attribute> getAttributes() { return attributes; }

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	public Attribute getAttribute(int position) {
		if(position >= attributes.size() || position < 0)
			return null;
		return attributes.get(position);
	}

	public int getNumAttributes() {
		return attributes.size();
	}


	public ArrayList<String> getAttributes_values(){
		ArrayList<String> attrsVals = new ArrayList<String>();

		for(Attribute currAttr : attributes)
			attrsVals.add(currAttr.getValue());

		return attrsVals;
	}


	public ArrayList<String> getAttributes_discValues(){
		ArrayList<String> attrsDiscVals = new ArrayList<String>();

		for(Attribute currAttr : attributes)
			attrsDiscVals.add(currAttr.getDiscValue());

		return attrsDiscVals;
	}


	public ArrayList<String> getAttributes_discLabels(){
		ArrayList<String> attrsDiscLabels = new ArrayList<String>();

		for(Attribute currAttr : attributes)
			attrsDiscLabels.add(currAttr.getDiscLabel());

		return attrsDiscLabels;
	}


	public double distance(Instance instance2, ArrayList<InfoAttribute> information_about_attributes) {
		double nominator = 0, denominator = 0;

		for (int i=0; i<attributes.size(); i++) {
			if(information_about_attributes.get(i).getType().equals("numeric")) {
				nominator += Math.abs(Double.valueOf(attributes.get(i).getValue())-Double.valueOf(instance2.getAttribute(i).getValue()))/(information_about_attributes.get(i).getMax()-information_about_attributes.get(i).getMin());
				denominator++;
			}
			else {
				if(information_about_attributes.get(i).getType().equals("ordinal")) {

					nominator += Math.abs((Double.valueOf(attributes.get(i).getValue()) - 1)/(information_about_attributes.get(i).getNb_values() -1) - (Double.valueOf(instance2.getAttribute(i).getValue()) - 1)/(information_about_attributes.get(i).getNb_values() -1));
					denominator++;
				}
				else {
					nominator += attributes.get(i).getValue().equals(instance2.getAttribute(i).getValue()) ? 1 : 0 ;
					denominator++;
				}
			}
		}

		return nominator/denominator;
	}


	@Override
	public String toString() {
		return "Instance [attributes=" + attributes + "]";
	}
}

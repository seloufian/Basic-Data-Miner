package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSet {
	private ArrayList<Instance> instances = new ArrayList<Instance>();
	private ArrayList<String> names = new ArrayList<String>();
	public static ArrayList<InfoAttribute> information_about_attributes = new ArrayList<InfoAttribute>();

	private ArrayList<List<Float>> centralTendency = new ArrayList<List<Float>>();
	private ArrayList<List<Float>> fiveNumSum = new ArrayList<List<Float>>();

	private ArrayList<ArrayList<String>> attrLabelCorrespDescVal = new ArrayList<ArrayList<String>>();

	private String name;


	public DataSet(String dataSetPath) {
		information_about_attributes.clear();

		File file = new File(dataSetPath);

		BufferedReader br = null;
		try { br = new BufferedReader(new FileReader(file)); } catch (FileNotFoundException ignore) {}

		Pattern pattern = Pattern.compile("'(.*?)'");
		String line, ignoreLine = "\\s*|%(\n|.)*";

		try {
			while(! (line = br.readLine().toLowerCase()).equals("@data")) {
				if(line.matches(ignoreLine))
					continue;

				line = line.replaceAll("\\s+", " ");

				if(line.startsWith("@relation"))
					name = line.split(" ")[1];

				if(line.startsWith("@type")) {
					String[] treated_line = line.split(" ");

					for (int i = 2; i < treated_line.length; i++)
						information_about_attributes.get(Integer.valueOf(treated_line[i])-1).setType(treated_line[1]);
				}

				if(line.startsWith("@attribute")) {
					Matcher matcher = pattern.matcher(line);

					if(matcher.find() && matcher.groupCount() > 0)
						names.add(matcher.group(1).replace(" ", "_"));
					else
						names.add(line.split(" ")[1]);

					information_about_attributes.add(new InfoAttribute());
				}
			}

			while((line = br.readLine()) != null) {
				if(line.matches(ignoreLine))
					continue;

				instances.add(new Instance());

				for(String currAttrVal : line.toLowerCase().split(",")) {
					instances.get(instances.size()-1).addAttribute(new Attribute(currAttrVal));
				}
			}
		} catch (NumberFormatException ignore) {} catch (IOException ignore) {}

		for(int i=0; i<names.size(); i++) {
			centralTendency.add(null);
			fiveNumSum.add(null);

			attrLabelCorrespDescVal.add(new ArrayList<String>());
		}

		discretizeNominalAttributes();
		fillIn_information_about_attributes();
	}


	private void fillIn_information_about_attributes() {
		for (Instance instance : instances) {
			for(int i=0; i<information_about_attributes.size(); i++) {
				InfoAttribute attribute = information_about_attributes.get(i);

				if(attribute.getType().equals("numeric")) {
					if(Double.valueOf(instance.getAttribute(i).getValue()) < attribute.getMin())
						information_about_attributes.get(i).setMin(Double.valueOf(instance.getAttribute(i).getValue()));

					if(Double.valueOf(instance.getAttribute(i).getValue()) > attribute.getMax())
						information_about_attributes.get(i).setMax(Double.valueOf(instance.getAttribute(i).getValue()));
				}

				if(attribute.getType().equals("ordinal")) {
					information_about_attributes.get(i).addNewValue(Integer.valueOf(instance.getAttribute(i).getValue()));
				}
			}
		}
	}


	public ArrayList<Instance> getInstances() { return instances; }
	public ArrayList<String> getNames() { return names; }
	public String getName() { return name; }


	public int getNumInstances() {
		return instances.size();
	}

	public Instance getIntance(int position) {
		if(position >= instances.size() || position < 0)
			return null;
		return instances.get(position);
	}

	public String getAttributeType(int position) {
		if(position >= information_about_attributes.size() || position < 0)
			return null;
		return information_about_attributes.get(position).getType();
	}

	public String getAttributeName(int position) {
		if(position >= names.size() || position < 0)
			return null;
		return names.get(position);
	}

	public int getNumAttributes() {
		return names.size();
	}


	public List<Float> calculateCentralTendency(int position) {
		if(position >= information_about_attributes.size() || position < 0)
			return null;

		if(! getAttributeType(position).matches("numeric|ordinal"))
			return null;

		if(centralTendency.get(position) != null)
			return centralTendency.get(position);

		ArrayList<Float> attrList = new ArrayList<Float>();
		HashMap<Float, Integer> attrFreq = new HashMap<Float, Integer>();

		float meanVal = 0f;

		for(Instance instance : instances) {
			float currAttrVal = Float.parseFloat(instance.getAttribute(position).getValue());
			meanVal += currAttrVal;

			attrList.add(currAttrVal);

			Integer currAttrFreq = attrFreq.get(currAttrVal);
			attrFreq.put(currAttrVal,  currAttrFreq == null ? 1 : currAttrFreq+1);
		}

		Collections.sort(attrList);

		float medianVal;
		int arraySize = attrList.size();
		if(arraySize % 2 == 0)
			medianVal = (attrList.get(arraySize/2) + attrList.get(1+arraySize/2))/2;
		else
			medianVal = attrList.get(Math.round(arraySize/2));

		int maxAttrFreq = Collections.max(attrFreq.values());
		float modeVal = 0f;
		for(Float key : attrFreq.keySet())
			if(attrFreq.get(key) == maxAttrFreq) {
				modeVal = key;
				break;
			}

		List<Float> returnVal = Arrays.asList(meanVal/arraySize, medianVal, modeVal);
		centralTendency.set(position, returnVal);

		return returnVal;
	}


	public List<Float> calculateFiveNumSum(int position){
		if(position >= information_about_attributes.size() || position < 0)
			return null;

		if(! getAttributeType(position).matches("numeric|ordinal"))
			return null;

		if(fiveNumSum.get(position) != null)
			return centralTendency.get(position);

		ArrayList<Float> attrList = new ArrayList<Float>();
		for(Instance instance : instances)
			attrList.add(Float.parseFloat(instance.getAttribute(position).getValue()));

		Collections.sort(attrList);

		int arraySize = attrList.size();

		float min=attrList.get(0), max=attrList.get(arraySize-1);
		float q1 = arraySize / 4;
		float q2 = arraySize / 2;
		float q3 = arraySize * 3 / 4;

		if(arraySize % 2 == 0) {
			q1 = (attrList.get(Math.round(q1)) + attrList.get(Math.round(q1)+1))/2;
			q2 = (attrList.get(Math.round(q2)) + attrList.get(Math.round(1+q2)))/2;
			q3 = (attrList.get(Math.round(q3)) + attrList.get(Math.round(q3)+1))/2;
		}
		else {
			q1 = attrList.get(Math.round(q1));
			q2 = attrList.get(Math.round(q2));
			q3 = attrList.get(Math.round(q3));
		}

		List<Float> returnVal = Arrays.asList(q1, q2, q3, min, max);
		centralTendency.set(position, returnVal);

		return returnVal;
	}


	public void discretizeAllAttributes(int binSize, boolean withMean) {
		for(int i=0; i<getNumInstances(); i++)
			discretizeAttribute(i, binSize, withMean);
	}


	public String translateFromLabel(String discLabel) {
		List<Integer> currAttrInfo = getAttributeInfoFromLabel(discLabel);

		if(currAttrInfo == null)
			return null;

		int currAttrPos = currAttrInfo.get(0);
		int currAttrNumDesc = currAttrInfo.get(1);

		return getAttributeName(currAttrPos) + " = " + attrLabelCorrespDescVal.get(currAttrPos).get(currAttrNumDesc);
	}


	@Override
	public String toString() {
		String returnStr = "";

		for(Instance instance : instances)
			returnStr += instance.getAttributes() + "\n";

		return returnStr;
	}


	private Boolean discretizeAttribute(int position, int binSize, boolean withMean) {
		if(position >= information_about_attributes.size() || position < 0 || binSize <= 0)
			return null;

		if(!information_about_attributes.get(position).getType().matches("numeric"))
			return false;

		String prefix = getPrefix(position);

		ArrayList<Float> selectedAttrValues = new ArrayList<Float>();
		for(int i=0; i<getNumInstances(); i++)
			selectedAttrValues.add(Float.parseFloat(getIntance(i).getAttribute(position).getValue()));

		Collections.sort(selectedAttrValues);

		Float min = selectedAttrValues.get(0);
		Float max = selectedAttrValues.get(selectedAttrValues.size()-1);
		Float width = (max - min) / binSize;
		Float currentDown = Float.NEGATIVE_INFINITY, currentUp = width+min;

		int numSelectedAttr = selectedAttrValues.size(), binCpt = 0, currValIdx = 0;

		ArrayList<Float> currentBin = new ArrayList<Float>();

		HashMap<String, String> replaceValues = new HashMap<String, String>();
		HashMap<String, String> assignLabels = new HashMap<String, String>();

		while(currValIdx < numSelectedAttr) {
			Float value = selectedAttrValues.get(currValIdx);

			if(currentDown < value && value <= currentUp) {
				currentBin.add(value);
				currValIdx++;
			}

			if(currentDown >= value || value > currentUp || currValIdx == numSelectedAttr) {
				float result = 0;

				if(! currentBin.isEmpty()) {
					if(withMean) { /* Discretize with MEAN value of each bin */
						float sum = 0;
						for(float val : currentBin)
							sum += val;
						result = sum/currentBin.size();

					}else { /* Discretize with MEDIAN value of each bin */
						int binHalf = Math.round(currentBin.size() / 2);

						if(currentBin.size() % 2 == 0)
							result = (currentBin.get(binHalf-1) + currentBin.get(binHalf))/2;
						else
							result = currentBin.get(binHalf);
					}

					for(float val : currentBin) {
						String keyVal = String.format("%.2f", val);
						replaceValues.put(keyVal, Float.toString(round(result, 2)));
						assignLabels.put(keyVal, prefix+binCpt);
					}

					attrLabelCorrespDescVal.get(position).add(Float.toString(round(result, 2)));
				}

				binCpt++;
				currentDown = currentUp;
				currentUp += (binCpt == binSize-1) ? Float.POSITIVE_INFINITY : width;

				currentBin.clear();
			}
		}

		for(int i=0; i<getNumInstances(); i++) {
			float currentValue = Float.parseFloat(getIntance(i).getAttribute(position).getValue());
			String keyValue = String.format("%.2f", currentValue);
			String replaceValue = replaceValues.get(keyValue);
			String insertLabel = assignLabels.get(keyValue);

			getIntance(i).getAttribute(position).setDiscretization(replaceValue, insertLabel);
		}

		return true;
	}


	private void discretizeNominalAttributes() {
		for(int i=0; i<getNumAttributes(); i++) {
			String prefix = getPrefix(i);

			if(getAttributeType(i).matches("nominal|string")) {
				int numCpt = 0;
				HashMap<String, Integer> assignLabels = new HashMap<String, Integer>();

				for(int j=0; j<getNumInstances(); j++) {

					Attribute currentAttr = getIntance(j).getAttribute(i);
					Integer labelNum = assignLabels.get(currentAttr.getValue());

					if(labelNum == null) {
						labelNum = numCpt;
						assignLabels.put(currentAttr.getValue(), labelNum);
						numCpt++;
					}

					currentAttr.setDiscretization(currentAttr.getValue(), prefix+labelNum);

					attrLabelCorrespDescVal.get(i).add(currentAttr.getValue());
				}
			}

			if(getAttributeType(i).matches("binary")) {
				for(int j=0; j<getNumInstances(); j++) {
					Attribute currentAttr = getIntance(j).getAttribute(i);
					int booleanVal = Integer.parseInt(currentAttr.getValue());
					currentAttr.setDiscretization(currentAttr.getValue(), prefix+booleanVal);

					attrLabelCorrespDescVal.get(i).add(currentAttr.getValue());
				}
			}
		}
	}


	private List<Integer> getAttributeInfoFromLabel(String discLabel) {
		if(! discLabel.matches("\\w+\\d+"))
			return null;

		Matcher lettersMatcher = Pattern.compile("([A-Z]+)").matcher(discLabel);
		lettersMatcher.find();

		String letters = lettersMatcher.group(1);

		int constPart=letters.length()-1, attrPosition=0;

		for(int i=0; i<letters.length(); i++)
			attrPosition += Math.pow(26, constPart-i) * (1 + letters.charAt(i) - 65);

		Matcher numbersMatcher = Pattern.compile("([0-9]+)").matcher(discLabel);
		numbersMatcher.find();

		int number = Integer.valueOf(numbersMatcher.group(1));

		List<Integer> returnVal = Arrays.asList(attrPosition-1, number);

		return returnVal;
	}


	private String getPrefix(int position) {
		if(position >= information_about_attributes.size() || position < 0)
			return null;

		String prefix = "";
		int result, mode = position;

		do {
			result = mode / 26;
			mode %= 26;

			if(result == 0)
				prefix += (char) (65 + mode);
			else
				prefix += (char) (64 + result);

		}while(result != 0);

		return prefix;
	}


	private float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}



	class InfoAttribute {
		private ArrayList<Double> info; // #Integer|Float 0 : Min ; 1 : Max #Ordinal 0 : nb_values; 1,2,.. : value1,value2,..
		private String type;


		public InfoAttribute() {}

		public InfoAttribute(String type) {
			setType(type);
		}


		public void setType(String type) {
			this.type = type;

			if(type.equals("numeric")) {
				info = new ArrayList<Double>();

				info.add(new Double(Double.POSITIVE_INFINITY)); // Min
				info.add(new Double(Double.NEGATIVE_INFINITY)); // Max
			}

			if(type.equals("ordinal")) {
				info = new ArrayList<Double>();
				info.add(new Double(0));
			}
		}


		public double getMin() {
			if(type.equals("numeric"))
				return info.get(0);

			return 0.0;
		}


		public void setMin(double min) {
			if(type.equals("numeric"))
				info.set(0, min);
		}


		public double getMax() {
			if(type.equals("numeric"))
				return info.get(1);

			return 0.0;
		}


		public void setMax(double max) {
			if(type.equals("numeric"))
				info.set(1, max);
		}


		public int getNb_values() {
			if(type.equals("ordinal"))
				return info.get(0).intValue();

			return 0;
		}


		public void setNb_values(int nb_values) {
			info.set(0, Double.valueOf(nb_values));
		}

		public String getType() {
			return type;
		}


		public void addNewValue(int value) {
			if(type.equals("ordinal")) {
				if (! info.subList(1, (int)(info.get(0)+1)).contains(Double.valueOf(value))) {
					info.add(Double.valueOf(value));
					info.set(0, info.get(0)+1);
				}
			}
		}
	}
}

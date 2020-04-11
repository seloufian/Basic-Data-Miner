package main;

public class Attribute {
	private String value, discValue, discLabel;


	public Attribute(String value){
		this.value = value;

		discValue = null;
		discLabel = null;
	}

	public String getValue() { return value; }
	public String getDiscValue() { return discValue; }
	public String getDiscLabel() { return discLabel; }


	public void setDiscretization(String discValue, String discLabel) {
		this.discValue = discValue;
		this.discLabel = discLabel;
	}


	@Override
	public String toString() {
		return "Attribute [value=" + value + ", discValue=" + discValue + ", discLabel=" + discLabel + "]";
	}
}

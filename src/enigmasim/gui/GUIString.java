package enigmasim.gui;

class GUIString {
	private String text = "";
	
	GUIString(String text) {
		this.text = text;
	}
	
	public String getString() {
		return text;
	}
	
	public void setString(String text) {
		this.text = text;
	}
}

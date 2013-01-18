package parser;

public class StringEntry extends ParserConfigEntry {

	private String name;
	
	private String[] compareTo;
	
	public StringEntry(String name, String[] compareTo) {
		this.name = name;
		this.compareTo = compareTo;
	}
	
	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}
	
	
}

package parser;

public class IntegerEntry extends ParserConfigEntry {
	
	private String name;
		
	public IntegerEntry(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}

}

package parser;

public class DoubleEntry extends ParserConfigEntry{
	
	private String name;
	
	public DoubleEntry(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}

}

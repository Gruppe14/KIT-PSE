package parser;

public class ParserConfig {
	
	private ParserConfigEntry[] config;
	
	private int numberOfEntries = 0;
	
	public ParserConfig(int i) {
		config = new ParserConfigEntry[i];
	}
	
	public void addEntry(ParserConfigEntry pce) {
		config[numberOfEntries] = pce;
		numberOfEntries++;
	}
	
	public ParserConfigEntry getEntryAt(int i) {
		return config[i];
	}

}

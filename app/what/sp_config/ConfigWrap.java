package what.sp_config;
 

public class ConfigWrap {
	
	private RowEntry[] entries; 

	
	public ConfigWrap(int numberOfEntries) {
		entries = new RowEntry[numberOfEntries];
	}
	
	public void addEntry(RowEntry entry) {
		for (int i = 0; i < entries.length; i++) {
			if (entries[i] == null) {
				entries[i] = entry;
				return;
			}
		}
		throw new ArrayStoreException();
		
	}
	
	public int getSize() {
		return entries.length;
	}
	
	public RowEntry getEntryAt(int i) {
		if ((i < 0) || (i >= entries.length)) {
			throw new IllegalArgumentException();
		}
		
		return entries[i];
	}
	


	
	
}

package what.sp_config;

// intern imports
import what.sp_parser.DataEntry;

/**
 * This class represents a dummy for a RowEntry.<br>
 * 
 * For example if there is a row in the pars file which is of
 * no interest, this Dummy can be used to say the parser: 
 * There is a row, but ignore it.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see RowEntry
 */
public class DummyRow extends RowEntry {
	
	/** Singleton instance of this class DummyRow. */
	private static final DummyRow INSTANCE = new DummyRow("Dummy", ConfigWrap.NOT_AVAILABLE, 1, 
														"dummy", ConfigWrap.NOT_AVAILABLE);
	
	/**
	 * Private constructor for the class DummyRow.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 */
	private DummyRow(String name, String logId, int lvl, String category, String scale) {
		super(name, logId, lvl, category, scale, RowId.DUMMY);
		// nothing to do here... just to avoid warnings.
	}

	/**
	 * Returns a instance of DummyRow.
	 * 
	 * @return a instance of DummyRow
	 */
	public static DummyRow getInstance() {
		return INSTANCE;
	}

	// -- OVERRIDE -- OVERRIDE -- OVERRIDE -- OVERRIDE -- OVERRIDE --
	@Override
	public boolean split(DataEntry de, String string, int location) {
		// Returns false, shouldn't be used.
		return false;
	}

	@Override
	public String getTableType() {
		throw new UnsupportedOperationException();
	}

}

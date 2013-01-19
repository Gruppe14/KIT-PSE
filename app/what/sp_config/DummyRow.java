package what.sp_config;

public class DummyRow extends RowEntry {
	
	private RowId id =  RowId.DUMMY;
	
	private static final DummyRow INSTANCE = new DummyRow("Dummy", "...", 1, "dummy", "XX");
	
	private DummyRow(String name, String logId, int lvl, String categorie, String scale) {
		super(name, logId, lvl, categorie, scale,RowId.DUMMY);
		// nothing to do here... just to avoid warnings.
	}

	public static DummyRow getInstance() {
		return INSTANCE;
	}
}

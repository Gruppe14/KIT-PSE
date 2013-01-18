package what.sp_config;

public class DummyRow {
	
	public static final DummyRow INSTANCE = new DummyRow("Dummy");
	
	private DummyRow(String name) {
		
	}

	public static DummyRow getInstance() {
		return INSTANCE;
	}
}

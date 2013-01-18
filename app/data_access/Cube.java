package data_access;

public class Cube implements Create_Cube {
	
	private ConnectionToOracle conn;
	
	public Cube() {
		this.conn = new ConnectionToOracle();
	}

	@Override
	public void createAndMapCube() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createAndMapMeasures() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

}

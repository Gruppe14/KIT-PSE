package oracle.olapi.test;

import java.util.ArrayList;

import oracle.olapi.metadata.mdm.MdmPrimaryDimension;
import oracle.olapi.metadata.mdm.MdmHierarchy;
import oracle.olapi.metadata.mdm.MdmLevelHierarchy;
import oracle.olapi.metadata.mdm.MdmHierarchyLevel;
import oracle.olapi.metadata.mdm.MdmDimensionLevel;
import oracle.olapi.metadata.mdm.HierarchyLevelMap;
import oracle.olapi.syntax.ColumnExpression;

public class Location_Dim implements Create_Dim{
	
	private ArrayList<MdmDimensionLevel> dimLevelList = new ArrayList();
	private ArrayList<String> dimLevelNames = new ArrayList();
	private ArrayList<String> keyColumns = new ArrayList();
	private ArrayList<String> lDescColNames = new ArrayList();
	
	private MdmPrimaryDimension mdmTimeDim;
	private MdmHierarchy        mdmTimeHier;
	private MdmLevelHierarchy   mdmTimeLevelHier;
	
	private ConnectionToOracle conn;
	
	public Location_Dim() {
		
		this.conn = new ConnectionToOracle();
		
	}
	
	@Override
	public void createDim() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createAndMapDimensionLevels() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createAndMapHierarchies() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createAtributte() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		
		try {
	           
		      (this.conn.getDataProvider().getTransactionProvider()).commitCurrentTransaction();
		    }
		    catch (Exception ex) {
		    	
		          System.out.println("Could not commit the Transaction. " + ex);
		          
		    }
		
	}

}

package what.sp_dataMediation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

public class DataChanger {

	public static TreeSet<String> getTreeSetFromRS(ResultSet re) {
		TreeSet<String> strings = new TreeSet<String>();
		
		try {
			while (re.next()) {
				strings.add(re.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Reading from result set failed.");
			e.printStackTrace();
		}
		
		return strings;
	}

}

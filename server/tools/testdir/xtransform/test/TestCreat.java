/**
 * 
 */

/**
 * @author DevUser
 *
 */
public class TestCreat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TestXtransformCreat tx = new TestXtransformCreat( args );
		
		try {
			tx.creatDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	"table_int_xbean", 100000,
//	"table_long_map", 105421, 
//	"table_short_set", 34456
	
}

/**
 * 
 */

/**
 * @author DevUser
 *
 */
public class TestVerify {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestXtransformVerify tx = new TestXtransformVerify(args);
		try {
			tx.verifyDb(tx.DstDir);
			//tx.verifyDb(tx.SrcDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Verify the DstDB OK");
	}
}

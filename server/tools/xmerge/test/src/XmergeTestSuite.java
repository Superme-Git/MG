package src;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 *  �������е�Xmerge��������
 * @note: �������������
 * 		@Suite.SuiteClasses({
	        TCheckDupKey.class, 
	        Test.class,
	        Test1.class,
	        .....
	        })
 */


@RunWith(Suite.class)
@Suite.SuiteClasses({
	        TestXmergeNotRef.class,
	        TestXmergeRef.class
	       // TestMerger.class//Merger���� added:ly 8.19	        
	        })
public class XmergeTestSuite {

}

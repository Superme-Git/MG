package src;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 *  包含所有的Xmerge测试用例
 * @note: 添加用例方法：
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
	       // TestMerger.class//Merger测试 added:ly 8.19	        
	        })
public class XmergeTestSuite {

}

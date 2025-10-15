package fire.pb.main;

import org.junit.Test;

import fire.pb.main.DAG;
import fire.pb.main.DAGNode;

public class DAGTest {

	@Test
	public void testCreateNodeIfNotExist() {
		DAG<String> d=new DAG<String>();
		DAGNode<String>  n=d.createNodeIfNotExist("abc");
		@SuppressWarnings("unused")
		DAGNode<String> n2=d.createNodeIfNotExist("ddd");
		junit.framework.Assert.assertTrue(n==d.createNodeIfNotExist("abc"));		
	}

	@Test
	public void testWalk() {
		//fail("Not yet implemented");
		DAG<String> d=new DAG<String>();
		d.createNodeIfNotExist("5");
		d.createNodeIfNotExist("11").addPrev("5").addPrev("7");
		d.createNodeIfNotExist("2").addPrev("11");
		d.createNodeIfNotExist("10").addPrev("11").addPrev("3");
		d.createNodeIfNotExist("9").addPrev("11").addPrev("8");
		d.createNodeIfNotExist("3");
		d.createNodeIfNotExist("7");
		d.createNodeIfNotExist("8").addPrev("7").addPrev("3");
		d.walk(new DAG.IWalk<String>(){

			@Override
			public void onNode(DAGNode<String> n) {
				System.out.print(n.getName());
				System.out.print(" ");	
				System.out.flush();
			}
			
		});
	}

	
	
}

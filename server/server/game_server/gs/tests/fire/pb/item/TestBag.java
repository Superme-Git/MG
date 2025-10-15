
package fire.pb.item;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;

public class TestBag {
	//某个可用来测试的物品id
	@SuppressWarnings("unused")
	private static final int itemid=32003 ;

	
	private static java.util.TreeSet<Long> allRoleidset=new java.util.TreeSet<Long>();
	@BeforeClass
 	public static void setUpBeforeClass() throws Exception {
		fire.pb.main.Gs.init();
		xtable.Bag.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.Bag>(){

			@Override
			public boolean onRecord(Long k, xbean.Bag v) {
				allRoleidset.add(k);
				return true;
			}
			
		});
	}
	
	private static long genRoleid(){
		long roleid;
		while(allRoleidset.contains((roleid=1+mkdb.Mkdb.random().nextInt(1<<30))));
		return roleid;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		fire.pb.main.Gs.docleanup();
	}
	
	@org.junit.Test
	public void testadd(){
		final long roleid=genRoleid();
		Assert.assertTrue(new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
	//			fire.pb.item.Module.getInstance().getItemColumn( roleid,BagTypes.BAG,false );
//				ItemColumn bag=fire.pb.item.Module.getInstance().getItemColumn( roleid,BagTypes.BAG,false );
//				Assert.assertEquals(0, bag.addItem(Module.getInstance().getItemManager().genBasicItem(itemid,1),-1,"autotest"));
				return true;
			}
			
		}.call());
		ItemMaps bag=fire.pb.item.Module.getInstance().getItemMaps( roleid,BagTypes.BAG,false );
		Assert.assertEquals(1, bag.size());
	}
	
	
	@org.junit.Test
	public void testadd2(){
		final long roleid=genRoleid();
		Assert.assertTrue(new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
//				ItemColumn bag=fire.pb.item.Module.getInstance().getItemColumn( roleid,BagTypes.BAG,false );
//				Assert.assertEquals(true, bag.addItem(Module.getInstance().getItemManager().genBasicItem(itemid, 1),-1,"autotest"));
				return true;
			}
			
		}.call());
		Assert.assertTrue(new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
//				ItemColumn bag=fire.pb.item.Module.getInstance().getItemColumn( roleid,BagTypes.BAG,false );
//				Assert.assertEquals(true, bag.addItem(Module.getInstance().getItemManager().genBasicItem(itemid, 1),-1,"autotest"));
				return true;
			}
			
		}.call());
		
		ItemMaps bag=fire.pb.item.Module.getInstance().getItemMaps( roleid,BagTypes.BAG,false );
		Assert.assertEquals(2, bag.size());
	}
	
	/**
	 * 遍历一个空背包
	 */
	@org.junit.Test
	public void testIteatorEmptyBag(){
		final long roleid=genRoleid();
		Assert.assertTrue(new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
				ItemMaps bag=fire.pb.item.Module.getInstance().getItemMaps( roleid,BagTypes.BAG,false );
				int count=0;
				for(@SuppressWarnings("unused") ItemBase bi:bag){
					count++;
				}
				Assert.assertEquals(0, count);
				return true;
			}
			
		}.call());		
	}

}

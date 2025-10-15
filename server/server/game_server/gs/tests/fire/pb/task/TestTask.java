package fire.pb.task;
import org.junit.AfterClass;
import org.junit.BeforeClass;
public class TestTask {
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
	static final long roleid = genRoleid();
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		fire.pb.main.Gs.docleanup();
	}
	private static long genRoleid() {
		long roleid;
		while(allRoleidset.contains((roleid=1+mkdb.Mkdb.random().nextInt(1<<30))));
		return roleid;
	}
/*	@Test
	public void testAccept() throws InterruptedException {
		
		new AcceptTask( roleid, 10001 ).accept();
		Thread.sleep( 3000 );
	 	xbean.QuestList quests = xtable.Rolequest.select( roleid );
	 	System.out.println( "accept::" + roleid );
	 	for ( xbean.QuestInfo quest : quests.getQuests().values() ) {
	 		System.out.println( quest );
	 	}
	}
	@Test
	public void testFinish() throws InterruptedException {
		new xdb.Procedure() {
			protected boolean process() {
				BasicTask bt = new BasicTask( roleid, 10, 1, false );
				bt.finish();
				return true;
			}
		}.submit();
		Thread.sleep( 3000 );
		System.out.println( "finish::" + roleid );
	 	xbean.QuestList quests = xtable.Rolequest.select( roleid );
	 	for ( xbean.QuestInfo quest : quests.getQuests().values() ) {
	 		System.out.println( quest );
	 	}
		
	}
	@Test
	public void testCommit() throws InterruptedException {
		Thread.sleep( 3000 );
		new CommitTask( roleid, 10001 ).commit();
		Thread.sleep( 3000 );
		System.out.println( "commit::" + roleid );
	 	xbean.QuestList quests = xtable.Rolequest.select( roleid );
	 	for ( xbean.QuestInfo quest : quests.getQuests().values() ) {
	 		System.out.println( quest );
	 	}
		
	}*/
}

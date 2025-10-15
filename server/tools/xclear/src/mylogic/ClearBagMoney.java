package mylogic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class ClearBagMoney {
	
	public static void clearBagGoldMoney(){
		Set<Long> roleIds = new HashSet<Long>();
		
		try {
			xtable.Bag.getTable().browse(
					new mkdb.TTable.IWalk<Long, xbean.Bag>() {
						@Override
						public boolean onRecord(Long roleId, xbean.Bag value) {
							roleIds.add(roleId);
							return true;
						}
					});
			
			
			//update
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					if (roleIds.isEmpty()) {
						return true;
					}

					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
					for (long roleId : roleIds) {
						xbean.Bag value = xtable.Bag.get(roleId);
						if(value!=null){
							value.getCurrency().clear();
							System.out.println("½ÇÉ«id\t"+roleId);
						}
					}
					return true;
				}
			};
			proc.submit().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + e);
		}
		
		
	}

}

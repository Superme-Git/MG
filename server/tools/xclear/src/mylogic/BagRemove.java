package mylogic;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public class BagRemove {
	
	public static void remove(Set<Long> roleIds){
		try {
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					if (roleIds.isEmpty()) {
						return true;
					}
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
					for (long roleId : roleIds) {
						xtable.Bag.remove(roleId);
					}
					return true;
				}
			};
			proc.submit().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

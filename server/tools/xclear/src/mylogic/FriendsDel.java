package mylogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FriendsDel {
	
	public static void remove(Set<mkdb.Procedure> proces,long roleId){
		//好友关系
		xbean.FriendGroups friendgroups=xtable.Friends.get(roleId);
		if(friendgroups!=null){
			List<Long> dellist=new ArrayList<Long>();
			for(Long id:friendgroups.getFriendmap().keySet()){
				if(id==null){
					continue;
				}
				dellist.add(id);
			}
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, dellist));
					for(long id:dellist){
						xbean.FriendGroups other=xtable.Friends.get(id);
						if(other!=null){
							other.getFriendmap().remove(roleId);
						}
					}
					return true;
				}
			};
			proces.add(proc);
		}
		xtable.Friends.remove(roleId);//friends	好友表
	}

}

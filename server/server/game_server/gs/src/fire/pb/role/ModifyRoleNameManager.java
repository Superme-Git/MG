package fire.pb.role;

import java.util.Arrays;

import fire.pb.talk.MessageMgr;
import mkdb.Transaction;


public class ModifyRoleNameManager {
	private static ModifyRoleNameManager instance = new ModifyRoleNameManager();
	
	public static final int MOD_NAME_ITEM_ID = 36752;  //改名道具id
	
	private ModifyRoleNameManager(){
	}

	public static ModifyRoleNameManager getInstance(){
		return instance;
	}
	
	
	public void buyModNameItem(final long roleid){
		mkdb.Procedure buyItemProc = new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
				Integer userid = xtable.Properties.selectUserid(roleid);
				if(userid == null)
					return false;
				lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, Arrays.asList(userid)));  //拿到userlock
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, Arrays.asList(roleid)));  //拿到rolelock
				xbean.ModifyNameRole modRole = xtable.Modnameitemroles.get(roleid);
				if(modRole == null){
					modRole = xbean.Pod.newModifyNameRole();
					modRole.setLastbuytime(System.currentTimeMillis());
					xtable.Modnameitemroles.insert(roleid, modRole);  //先设置一个值，防止后面计算时间间隔错误
				} else if(System.currentTimeMillis() - modRole.getLastbuytime() < 30L*24*60*60*1000){
					//30天内已经购买过一次改名道具了
					MessageMgr.psendMsgNotifyWhileRollback(roleid, 144660	, null);
					return false;
				}
				final fire.pb.item.ItemMaps depot = fire.pb.item.Module.getInstance().getItemMaps(roleid,
						fire.pb.item.BagTypes.DEPOT, true);   //仓库
				final fire.pb.item.ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleid,
						fire.pb.item.BagTypes.BAG, true);   //背包
				final fire.pb.item.ItemMaps temp = fire.pb.item.Module.getInstance().getItemMaps(roleid,
						fire.pb.item.BagTypes.TEMP, true);   //临时背包栏
				if(bag.getItemNum(MOD_NAME_ITEM_ID, 0) > 0 || depot.getItemNum(MOD_NAME_ITEM_ID, 0) > 0
						|| temp.getItemNum(MOD_NAME_ITEM_ID, 0) > 0){
					//背包或者仓库里还有改名道具，所以不让购买
					MessageMgr.psendMsgNotifyWhileRollback(roleid, 144659, null);
					return false;
				}
				int modCount = modRole.getBuycount()%5;
				int timeGap = (int) ((System.currentTimeMillis() - modRole.getLastbuytime())/(60L*24*60*60*1000));
				int needFushi = 6888 + 3000*modCount - 3000*timeGap;
				if(needFushi < 6888)
					needFushi = 6888;

				modRole.setBuycount(modRole.getBuycount() + 1);
				modRole.setLastbuytime(System.currentTimeMillis());
				
				return true;
			}
			
		};
		
		if(Transaction.current() == null)
			buyItemProc.submit();
		else
			buyItemProc.call();
	}
	
	
	public void sendPriceInfo(final long roleid,long npckey){
		if (!fire.pb.map.SceneNpcManager.checkDistance(npckey, roleid))
			return;
	}
}

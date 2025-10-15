package fire.pb.item;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.DateValidate;
import mkdb.Procedure;

public class PCheckUseNum extends Procedure {

	private final long roleId;
	private final int itemid;
	public PCheckUseNum(final long roleId, final int itemid) {
		this.roleId = roleId;
		this.itemid = itemid;
	}
	
	@Override
	protected boolean process() throws Exception {
		java.util.Map<Integer, SItemUseTime> useItemCnf = fire.pb.main.ConfigManager.getInstance().getConf(SItemUseTime.class);
		if (useItemCnf.containsKey(itemid)) { //需要限制使用次数
			xbean.ItemUse itemUse = xtable.Roleuseitemcount.get(roleId);
			if (itemUse == null) {
				itemUse = xbean.Pod.newItemUse();
				xtable.Roleuseitemcount.insert(roleId, itemUse);
			}
			
			String strTime = new String();
			
			xbean.ItemUseCount itemUseCnt = itemUse.getIteminfo().get(itemid);
			if (itemUseCnt == null) {
				itemUseCnt = xbean.Pod.newItemUseCount();
				itemUse.getIteminfo().put(itemid, itemUseCnt);
			}
			
			long currentTime = System.currentTimeMillis();
			SItemUseTime useItemTime = useItemCnf.get(itemid);
			
			switch(useItemTime.getTypes()) {
			case 1: {//按天
				if (!DateValidate.inTheSameDay(itemUseCnt.getLastusetime(), currentTime)) {
					itemUseCnt.setUsetimes(0);
				}
				strTime = "天";
				break;
			}
			case 2: {//按周
				if (!DateValidate.inTheSameWeek(itemUseCnt.getLastusetime(), currentTime)) {
					itemUseCnt.setUsetimes(0);
				}
				strTime = "周";
				break;
			}
			case 3: {//按月
				if (!DateValidate.inTheSameMonth(itemUseCnt.getLastusetime(), currentTime)) {
					itemUseCnt.setUsetimes(0);
				}
				strTime = "月";
				break;
			}
			default:
				return false;
			}
			
			if (itemUseCnt.getUsetimes() >= useItemTime.times) {
				List<String> para = new ArrayList<String>(4);
				
				ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemid);
				if (attr == null) {
					return false;
				}
				
				para.add(strTime);
				para.add(attr.getName());
				para.add(strTime);
				para.add(String.valueOf(useItemTime.times));
				
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 160229, para);
				Module.logger.info(new StringBuffer().append("角色[").append(roleId).append("]道具使用次数在到上限[").append(itemid).append("] 最后使用时间:").append(itemUseCnt.getLastusetime())
						.append(" 当前时间:").append(currentTime));
				return false;
			}
			
			itemUseCnt.setUsetimes(itemUseCnt.getUsetimes() + 1);
			itemUseCnt.setLastusetime(currentTime);
		}
		return true;
	}

}


package fire.pb.item;

import java.util.ArrayList;
import java.util.List;

import fire.pb.item.equip.diamond.EquipDiamondMgr;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import mkdb.Procedure;

public class PResolveGem extends Procedure {
	private final long roleId;
	private final int gemkey;
	
	public PResolveGem(final long roleId, final int keyinpack) {
		this.roleId = roleId;
		this.gemkey = keyinpack;
	}
	
	@Override
	protected boolean process() throws Exception {
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		ItemBase bi = bag.getItem(gemkey);
		if (bi == null)
			return false;
		
		//分解宝石
		if (EquipDiamondMgr.getDiamondPropMap().get(bi.getItemId()).resolve == 0)
			return false;
		
		//删除原有宝石
		int nMoveNum = bag.removeItemWithKey(gemkey, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gemfenjie, bi.getItemId(), "宝石分解");
		if (nMoveNum != 1) {
			return false;
		}
		
		final int itemid = EquipDiamondMgr.getDiamondPropMap().get(bi.getItemId()).resolveItem;
		final int itemnum = EquipDiamondMgr.getDiamondPropMap().get(bi.getItemId()).resolveNum;
		
		int realAdd = BagUtil.addItem(roleId, itemid, itemnum, "宝石分解", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gemfenjieget, itemid);
		if (realAdd != itemnum) {
			return false;
		}
//		bag.addItem(itemid, itemnum, "宝石分解", fire.log.YYLogger.COUNTER_TYPE, itemid);
		
		ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
		if (iAttr == null) {
			Module.logger.error("角色:" + roleId + "分解宝石id:" + itemid + "找不到属性!");
			return false;
		}
		
		List<String> para = new ArrayList<String>(2);
		para.add(String.valueOf(iAttr.getName()));
		para.add(String.valueOf(itemnum));
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 160164, para);
		
		return true;
	}
}

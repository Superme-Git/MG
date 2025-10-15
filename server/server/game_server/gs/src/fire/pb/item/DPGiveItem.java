
package fire.pb.item;

import mkdb.Procedure;

public class DPGiveItem extends Procedure {
	protected final long roleId;
	protected final int itemid;
	protected final int usenum;
	
	private ItemBase bi;
	
	public DPGiveItem(final long roleId, final int itemid, final int usenum) {
		this.roleId = roleId;
		this.itemid = itemid;
		this.usenum = usenum;
	}
	
	protected UseItemHandler getUseItemHandler() {
		if (bi == null)
			return null;
		return bi.getUseItemHandler();
	}
	
	@Override
	protected final boolean process() {
		bi = Module.getInstance().getItemManager().genItemBase(itemid, usenum, 0);
		if (bi == null)
			return false;
		
		PCheckUseNum checkUseNum = new PCheckUseNum(roleId, itemid);
		if (!checkUseNum.call())
			return false;
		
		
		final Integer roleLevel = xtable.Properties.selectLevel(roleId);
		if (roleLevel == null || roleLevel < bi.itemAttr.needlevel) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144815, 0, null);
			return false;
		}
		
        
		StringBuilder sb = new StringBuilder();
		Module.logger.info(sb.append("AbPGiveItem() ").append(bi.getItemId()).append(" num:").append(usenum).append(" handle:").append(getUseItemHandler().getClass().toString()));
		Commontext.UseResult ur = getUseItemHandler().onUse(roleId, bi, usenum);
		Module.logger.info("AbPGiveItem() result = " + ur.toString());
		
		if (ur == Commontext.UseResult.SUCC) {
			return true;
		}
		return false;
	}

}

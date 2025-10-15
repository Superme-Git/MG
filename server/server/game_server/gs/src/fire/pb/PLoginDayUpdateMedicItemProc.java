package fire.pb;


/**
 * 每天更新玩家公会药房购买物品数量
 *
 */
public class PLoginDayUpdateMedicItemProc extends mkdb.Procedure{
	
	
	private long roleId;
	
	public PLoginDayUpdateMedicItemProc(long roleId){
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process() throws Exception {
		//更新玩家每天购买药房物品数量
		xbean.BuyMedicItemNum buyMedicItemNum=xtable.Buymedicitemnum.get(roleId);
		if(buyMedicItemNum!=null){
			buyMedicItemNum.setBuynum(0);
			buyMedicItemNum.setImpeachdaynum(0);
		}
		
		return true;
	}

}

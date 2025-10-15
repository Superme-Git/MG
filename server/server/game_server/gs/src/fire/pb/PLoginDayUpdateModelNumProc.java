package fire.pb;


/**
 * 每天更新玩家模块计数信息
 *
 */
public class PLoginDayUpdateModelNumProc extends mkdb.Procedure{
	
	
	private long roleId;
	
	public PLoginDayUpdateModelNumProc(long roleId){
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process() throws Exception {
		xbean.RoleUpdateModelNum num=xtable.Roleupdatemodelnumtab.get(roleId);
		if(num!=null){
			num.setTradingbuyfushinum(0);// 每日交易所购买符石数量
		}
		return true;
	}

}

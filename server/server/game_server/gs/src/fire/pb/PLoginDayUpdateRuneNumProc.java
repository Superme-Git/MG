package fire.pb;


/**
 * 每天更新玩家公会请求次数信息
 *
 */
public class PLoginDayUpdateRuneNumProc extends mkdb.Procedure{
	
	
	private long roleId;
	
	public PLoginDayUpdateRuneNumProc(long roleId){
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process() throws Exception {
		//更新玩家每天购买药房物品数量
		xbean.RuneInfo otherruneinfo=xtable.Runeinfotab.get(roleId);//玩家符文相关信息
		if(otherruneinfo!=null){
			otherruneinfo.setDayrequestnum(0);
		}
		return true;
	}

}

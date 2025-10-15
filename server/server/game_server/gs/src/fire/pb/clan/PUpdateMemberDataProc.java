package fire.pb.clan;



public class PUpdateMemberDataProc extends mkdb.Procedure{
	private long roleId;
	public PUpdateMemberDataProc(long roleId){
		this.roleId = roleId;
	}
	
	public boolean process(){
		if(roleId < 0) return false;
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if(clanInfo == null || (!clanInfo.getMembers().containsKey(roleId)) ) {
			//这里统一处理踢出公会地图逻辑
			ClanUtils.jumoOutClanAllMap(roleId);
		}
		return true;
	}
}

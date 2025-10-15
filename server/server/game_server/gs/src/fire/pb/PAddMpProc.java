package fire.pb;


/**
 * 加减法力值的存储过程
 * mp > 0 为加   
 * mp < 0 为减
 *
 */
public class PAddMpProc extends mkdb.Procedure{
	private long roleid;
	private int mp;
	
	public PAddMpProc(final long roleid, int mp){
		this.roleid = roleid;
		this.mp = mp;
	}
	public boolean process(){
		if (mp == 0 || roleid < 0)
			return false;
		fire.pb.effect.Role role = new fire.pb.effect.RoleImpl(roleid);
		if (role.addMp(mp) == 0)
			return false;
		
		// 通知客户端属性值的改变
		
		final fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
		data.datas.put(fire.pb.attr.AttrType.MP, (float)role.getMp());
		psendWhileCommit(roleid, data);
		return true;
	}

}

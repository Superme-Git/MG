package fire.pb;

/**
 * 加减生命值的过程
 * hp >0 为加生命值， hp <0 为减生命值 
 *
 */
public class PAddHpProc extends mkdb.Procedure {
	private final long roleid;
	private final int hp;
	
	public PAddHpProc(long roleid, int hp){
		super();
		this.roleid = roleid;
		this.hp = hp;
	}
	
	@Override
	public boolean process(){
		if (hp == 0)
			return false;
		final xbean.Properties prop = xtable.Properties.get(roleid);
		if (null == prop)
			return false;
		fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl( roleid );
		role.addHp(hp);
		//prop.setHp(Math.max(0, Math.min(prop.getUplimithp(),prop.getHp() + hp )));		
		final SRefreshHp send = new SRefreshHp(prop.getHp());
		psendWhileCommit(roleid, send);
		return true;
	}
}

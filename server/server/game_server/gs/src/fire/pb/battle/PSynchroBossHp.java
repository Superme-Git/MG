package fire.pb.battle;

import fire.pb.map.SceneManager;
import mkdb.Procedure;

/**为没有选择操作的战斗单元 设置默认的操作*/
public class PSynchroBossHp extends Procedure {
	private final int bossid;
	private final long roleid;
	private final long maxhp;
	private final long hp;
	private byte flag = 1; //0:开始 1:更新
	private final long changehp;

	public PSynchroBossHp(final int bossid, final long roleid, final long maxhp, final long hp, byte flag, final long changehp){
		this.bossid = bossid;
		this.roleid = roleid;
		this.hp = hp;
		this.maxhp = maxhp;
		this.flag = flag;
		this.changehp = changehp;
	}
	
	public boolean process(){
		SSynchroBossHp send = new SSynchroBossHp();
		send.bossmonsterid = bossid;
		send.maxhp = maxhp;
		send.hp = hp;
		send.flag = flag;
		send.rolename = xtable.Properties.selectRolename(roleid);
		send.changehp = changehp;
		SceneManager.psendToSceneByRoleWhileCommit(send, roleid);
		return true;
	}
	
}

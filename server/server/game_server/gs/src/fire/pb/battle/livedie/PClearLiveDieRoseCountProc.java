package fire.pb.battle.livedie;

import org.apache.log4j.Logger;

/**
 * 每天刷新点赞次数
 * @author Administrator
 *
 */
public class PClearLiveDieRoseCountProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private long roleid;
	
	public PClearLiveDieRoseCountProc(final long roleid){
		this.roleid = roleid;
	}
	public boolean process(){
		xbean.LDVideoRoleRoseInfoList list = xtable.Ldvideoroleroseinfolisttab.get(roleid);
		if(list!=null){
			list.getLdvideoroleroseinfolist().clear();
		}
		return true;
	}

}

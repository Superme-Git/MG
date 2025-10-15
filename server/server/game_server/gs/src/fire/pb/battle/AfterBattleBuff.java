package fire.pb.battle;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.mission.fairyland.PTransferRole;
import fire.pb.skill.Result;
import mkdb.Procedure;
import xbean.Buff;


/** */
public class AfterBattleBuff extends ConstantlyBuff {
	public static final int EVENT_TYPE = 1;
	
	 //哪个事件需要在战斗结束后处理,根据不同id,可以共用这个buff
	public static final int DREAMLAND = 1;
	public static final int DREAMLAND_MAPID = 2;
	public static final int DREAMLAND_POSX = 3;
	public static final int DREAMLAND_POSY = 4;	
	public static final int XIALV_TASK_QTBX = 5;
	public static final int XIALV_TASK_XSXY = 6;
   public AfterBattleBuff(ConstantlyBuffConfig buffConfig){
	   super(buffConfig);
   }
	
	public AfterBattleBuff(Buff buffBean) {
		super(buffBean);
	}

	@Override
	public Result closure(BuffAgent buffAgent) {
		long roleid = ((BuffRoleImpl)buffAgent).getRoleId();
		Result result= super.closure(buffAgent);
		xbean.Buff buff = getBuffBean();
		int eventType =  buff.getEffects().get(EVENT_TYPE).intValue();
		switch (eventType) {
		case DREAMLAND:
			int mapid = buff.getEffects().get(DREAMLAND_MAPID).intValue();
			int posx = buff.getEffects().get(DREAMLAND_POSX).intValue();
			int posy = buff.getEffects().get(DREAMLAND_POSY).intValue();
			Procedure.pexecuteWhileCommit(new PTransferRole(roleid,mapid,posx,posy));
			break;
		default:
			break;
		}
		return result;
	}

	
	

}


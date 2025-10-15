package fire.pb.team;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;

/***
 * 光环 设置
 * @author changhao
 *
 */
public class PSetFormationProc extends mkdb.Procedure{
	
	private final long roleId;
	private final int formation;
	
	public PSetFormationProc(long roleId, int formId){
		this.roleId = roleId;
		this.formation = formId;
	}

	@Override
	protected boolean process() throws Exception {

		Team  team = TeamManager.getTeamByRoleId(roleId);
		xbean.Properties prop = xtable.Properties.get(roleId);
		PropRole  prole = new PropRole(roleId, false);
		
		if(!checkForm(prop)){
			return false;
		}
		
		int formLevel = prole.getFormLevel(formation);
		prop.setDefultform(formation);
		if(team != null && team.getTeamInfo().getTeamleaderid() == roleId){
			team.changeFormationWithSP(formation,formLevel, true);//换阵型
		}
		
		SSetMyFormation sSetMyFormation = new SSetMyFormation();
		sSetMyFormation.formation = formation;
		sSetMyFormation.entersend = 0;
		mkdb.Procedure.psendWhileCommit(roleId, sSetMyFormation);
		return true;
	}
	
	private boolean checkForm(xbean.Properties prop){

		if(!checkOnline(roleId)){
			TeamManager.logger.debug("FAIL:光环设置者不在线,roleid: " + roleId);
			return false;
		}
		
		//设置的光环不合法（illegal）
		if(!checkSetedFormationValid(formation,prop)){
			TeamManager.logger.debug("FAIL:设置的光环不合法,formation: " + formation );
			return false;
		}
		
		BuffAgent buffagent = new BuffRoleImpl(roleId);
		if(!buffagent.canAddBuff(OperateType.TEAM_SET_FORMATION)){
			TeamManager.logger.debug("FAIL:设置者处于不可以设置光环的状态，例如战斗,roleid: " + roleId);
			return false;
		}
		
		return true;
	}
	
	//设置者在线？只能在Procedure中被调用
	private boolean checkOnline(long roleId){
		if(StateCommon.isOnline(roleId))
			return true;
		else
			return false;
	}
	
	//设置的光环合法？只能在Peocedure中被调用
	private boolean checkSetedFormationValid(int formation,xbean.Properties prop){
		if(formation >=0 && formation <= 10){
			if(formation == 0)//普通光环，必会
				return true;
			xbean.FormBean formLevel = prop.getFormationsmap().get(formation);
			if(formLevel != null && formLevel.getLevel()>0){
				return true;
			}
		}
		return false;
	}
}

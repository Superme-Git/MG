package fire.pb.huoban;

import fire.pb.PropRole;
import fire.pb.team.PSetFormationProc;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSwitchZhenfa extends Procedure {
	
	private final long roleId;
	private final int zhenrongId;
	private final int zhenfaId;
	
	public PSwitchZhenfa(final long roleId, final int zhenrongId, final int zhenfaId) {
		this.roleId = roleId;
		this.zhenrongId = zhenrongId;
		this.zhenfaId = zhenfaId;
	}
	
	public boolean process(){
		if (zhenrongId < 0 || zhenrongId > 2) return false;
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		
		xbean.HuoBanZhenrong zhenrongs = xtable.Huobanzhenrongs.get(roleId);
		if (zhenrongs == null) {
			zhenrongs = xbean.Pod.newHuoBanZhenrong();
			zhenrongs.setCurrent(zhenrongId);
			
			xbean.HuoBanZhenrongInfo currentZhenrong = xbean.Pod.newHuoBanZhenrongInfo();
			currentZhenrong.setZhenfa(zhenfaId);
			zhenrongs.getZhenrong().put(zhenrongId, currentZhenrong);
			
			xtable.Huobanzhenrongs.insert(roleId, zhenrongs);
		}
		
		xbean.HuoBanZhenrongInfo info = zhenrongs.getZhenrong().get(zhenrongId);
		if(info == null) {
			info = xbean.Pod.newHuoBanZhenrongInfo();
			zhenrongs.getZhenrong().put(zhenrongId, info);
		}
		
		info.setZhenfa(zhenfaId);
		
		xbean.Properties prop = xtable.Properties.get(this.roleId);
		
		//设置战斗光环
		if (zhenrongs.getCurrent() == zhenrongId) {
			prop.setDefultform(info.getZhenfa());
			if (team!=null && team.isTeamLeader(this.roleId)) {
				PropRole prole = new PropRole(this.roleId, true);
				int formLevel = prole.getFormLevel(prole.getDealutFormId());
				team.getTeamInfo().setFormation(prole.getDealutFormId());
				team.getTeamInfo().setFormationlevel(formLevel);
			}
			
			new PSetFormationProc(roleId, zhenfaId).call();
		}
		
		SSwitchZhenfa snd = new SSwitchZhenfa();
		snd.zhenfaid = zhenfaId;
		snd.zhenrongid = zhenrongId;
		
		mkdb.Procedure.psendWhileCommit(this.roleId, snd);
		
		return true;
	}

}

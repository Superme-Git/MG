package fire.pb.huoban;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSwitchZhenrong extends Procedure {
	
	private long roleId;
	private int zhenrong;
	
	public PSwitchZhenrong(long roleId, int zhenrong){
		this.roleId = roleId;
		this.zhenrong = zhenrong;
	}
	
	public boolean process(){
		Team team = TeamManager.getTeamByRoleId(roleId);
		xbean.Properties prop = xtable.Properties.get(this.roleId);
		int defaultForm = prop.getDefultform();
		xbean.HuoBanZhenrong zhenrongs = xtable.Huobanzhenrongs.get(roleId);
		if (zhenrongs == null) {
			zhenrongs = xbean.Pod.newHuoBanZhenrong();
			zhenrongs.setCurrent(0);
			xbean.HuoBanZhenrongInfo currentZhenrong = xbean.Pod.newHuoBanZhenrongInfo();
			currentZhenrong.setZhenfa(defaultForm);
			zhenrongs.getZhenrong().put(1, currentZhenrong);
			xtable.Huobanzhenrongs.insert(roleId, zhenrongs);
		}
		if (zhenrong<0 || zhenrong>2) return false;
		zhenrongs.setCurrent(this.zhenrong);
		xbean.HuoBanZhenrongInfo info = zhenrongs.getZhenrong().get(zhenrongs.getCurrent());
		if(info == null){
			info = xbean.Pod.newHuoBanZhenrongInfo();
			info.setZhenfa(0);
			zhenrongs.getZhenrong().put(zhenrongs.getCurrent(), info);
		}
		//参战伙伴处理
		int len = info.getHuoban().size();
		List<Integer> fightHuoBanIds = new ArrayList<Integer>();
		
		HuoBanColumn huobanColumn = HuoBanColumn.getHuoBanColumn(this.roleId, false);
		
		if(len > 0) {			
			for (int index=0; index<len; index++) {
				int fightHuoBanId = info.getHuoban().get(index);
				HuoBan fightHuoBan = huobanColumn.getHuoBan(fightHuoBanId);
				
				if (fightHuoBan != null) {
					fightHuoBanIds.add(fightHuoBanId);
				}
				
			}
		}
		//设置新参战伙伴
		huobanColumn.setFightHuoBans(fightHuoBanIds);
		
		
		info.getHuoban().clear();
		info.getHuoban().addAll(fightHuoBanIds);
		//设置战斗光环
		prop.setDefultform(info.getZhenfa());
		if (team!=null && team.isTeamLeader(this.roleId)) {
			PropRole prole = new PropRole(this.roleId, true);
			int formLevel = prole.getFormLevel(prole.getDealutFormId());
			team.getTeamInfo().setFormation(prole.getDealutFormId());
			team.getTeamInfo().setFormationlevel(formLevel);
		}
		SChangeZhenrong snd = new SChangeZhenrong();
		snd.zhenrong = zhenrongs.getCurrent();
		int zhenfa = info.getZhenfa();
		if (zhenfa < 0) zhenfa = 0;
		info.setZhenfa(zhenfa);
		snd.zhenfa = info.getZhenfa();
		snd.huobanlist.addAll(info.getHuoban());
		snd.reason = 4;
		mkdb.Procedure.psendWhileCommit(this.roleId, snd);
		
		return true;
	}
}

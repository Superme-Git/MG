package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;

public class PLiveDieBattleRankView extends Procedure {
	private final long roleid;
	private final int modeltype;

	public PLiveDieBattleRankView(long roleid, int modeltype) {
		this.roleid = roleid;
		this.modeltype = modeltype;
	}
	
	public static final int PerPageMax=6;
	public static final int RANK_MAX_HIS=10;
	public static final int RANK_MAX_DAY=100;
	public static final int RANK_MAX_WEEK=50;

	@Override
	protected boolean process() throws Exception {
		java.util.ArrayList<fire.pb.battle.livedie.LDVideoRoleInfoDes> rolefightlist=new ArrayList<fire.pb.battle.livedie.LDVideoRoleInfoDes>();
		SLiveDieBattleRankView sLiveDieBattleRankView=new SLiveDieBattleRankView();
		List<xbean.LDVideoRoleInfoDes> templist=new ArrayList<xbean.LDVideoRoleInfoDes>();
		List<xbean.LDVideoRoleInfoDes> list=new ArrayList<xbean.LDVideoRoleInfoDes>();
		templist.addAll(LiveDieMange.getLDVideoRoleInfoDesByType(modeltype,roleid));
		
		if(modeltype==LDmodelType.DAY_FIGHT){
			int min=Math.min(RANK_MAX_DAY, templist.size());
			for(int i=0;i<min;i++){
				list.add(templist.get(i));
			}
		}else if(modeltype==LDmodelType.WEEK_FIGHT){
			int min=Math.min(RANK_MAX_WEEK, templist.size());
			for(int i=0;i<min;i++){
				list.add(templist.get(i));
			}
		}else if(modeltype==LDmodelType.HIS_FIGHT){
			int min=Math.min(RANK_MAX_HIS, templist.size());
			for(int i=0;i<min;i++){
				list.add(templist.get(i));
			}
		}else{
			list.addAll(templist);
		}
		for (xbean.LDVideoRoleInfoDes p : list) {
			LDVideoRoleInfoDes lDVideoRoleInfoDes=new LDVideoRoleInfoDes();
			lDVideoRoleInfoDes.role1 = changeLDRoleInfoDes(p.getRole1());
			lDVideoRoleInfoDes.role2 = changeLDRoleInfoDes(p.getRole2());
			lDVideoRoleInfoDes.teamlist1 = changeLDTeamRoleInfoDes(p.getTeamlist1());
			lDVideoRoleInfoDes.teamlist2 = changeLDTeamRoleInfoDes(p.getTeamlist2());
			lDVideoRoleInfoDes.battleresult = p.getBattleresult();
			lDVideoRoleInfoDes.rosenum = p.getRosenum();
			lDVideoRoleInfoDes.videoid = p.getVideoid();
			int flag=0;
			xbean.LDVideoRoleRoseInfoList rolelist = xtable.Ldvideoroleroseinfolisttab.select(roleid);
			if(rolelist!=null){
				xbean.LDVideoRoleRoseInfo lDVideoRoleRoseInfo=rolelist.getLdvideoroleroseinfolist().get(p.getVideoid());
				if(lDVideoRoleRoseInfo!=null){
					if(lDVideoRoleRoseInfo.getRosenum()>0){
						flag=1;
					}
				}
			}
			lDVideoRoleInfoDes.roseflag=flag;
			
			rolefightlist.add(lDVideoRoleInfoDes);
		}
		
		sLiveDieBattleRankView.modeltype=modeltype;
		sLiveDieBattleRankView.rolefightlist.addAll(rolefightlist);
		psendWhileCommit(roleid, sLiveDieBattleRankView);
		
		return true;
	}
	
	public LDRoleInfoDes changeLDRoleInfoDes(xbean.LDRoleInfoDes bean){
		LDRoleInfoDes role1=new LDRoleInfoDes();
		role1.roleid = bean.getRoleid();
		role1.rolename = bean.getRolename();
		role1.shape = bean.getShape();
		role1.level = bean.getLevel();
		role1.school = bean.getSchool();
		role1.teamnum = bean.getTeamnum();
		role1.teamnummax = bean.getTeamnummax();
		return role1;
	}
	
	public java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes> changeLDTeamRoleInfoDes(java.util.List<xbean.LDTeamRoleInfoDes> list){
		java.util.ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes> teamlist1 =new ArrayList<fire.pb.battle.livedie.LDTeamRoleInfoDes>();
		if(list!=null&&list.size()>0){
			for(xbean.LDTeamRoleInfoDes bean : list){
				LDTeamRoleInfoDes role1=new LDTeamRoleInfoDes();
				if(bean!=null){
					role1.roleid = bean.getRoleid();
					role1.rolename = bean.getRolename();
					role1.shape = bean.getShape();
					role1.level = bean.getLevel();
					role1.school = bean.getSchool();
					teamlist1.add(role1);
				}
			}
		}
		return teamlist1;
	}
	
	
	
	
	
	/**
	 * 获得页数和个数
	 * @param count
	 * @param page
	 * @param pp
	 */
	private static void getPage(int count, int page, int[] pp) {
		if (count == 0) {
			pp[0] = 1;
			pp[1] = 0;
			return;
		}
		if (count % PerPageMax == 0) {
			pp[0] = count / PerPageMax;
		} else {
			pp[0] = count / PerPageMax + 1;
		}
		pp[1] = PerPageMax;
		if (page == pp[0]) {
			pp[1] = count % PerPageMax;
			if (count % PerPageMax == 0) {
				pp[1] = PerPageMax;
			}
		}
	}
	
	
	


}

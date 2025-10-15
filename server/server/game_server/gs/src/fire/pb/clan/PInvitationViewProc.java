package fire.pb.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import fire.pb.GameSystemConfig;
import fire.pb.SysConfigType;
import fire.pb.clan.srv.ClanBaseManager;

public class PInvitationViewProc extends mkdb.Procedure{
	public static final int MAX_NUM=10;//查询一次返回的最大人数
	private final long roleid;
	private final int type_level;
	private final int type_school;
	private final int type_sex;
	
	
	
	public PInvitationViewProc(long roleid,int type_level, int type_school, int type_sex) {
		super();
		this.type_level=type_level;
		this.type_school=type_school;
		this.type_sex=type_sex;
		this.roleid=roleid;
	}
	
	@Override
	protected boolean process() throws Exception {

		List<Long> tempRoleIds = new LinkedList<Long>();
		do {
			List<gnet.link.Role> roles = gnet.link.Onlines.getInstance().getSafeRoles();
			for (gnet.link.Role role : roles) {
				// 过滤自己
				if (role.getRoleid() == roleid) {
					continue;
				}
				tempRoleIds.add(role.getRoleid());
			}
		} while (false);
		// 打乱,抽取一部分
		if (tempRoleIds.size() > 1) {
			Collections.shuffle(tempRoleIds);
			int maxIndex = Math.min(tempRoleIds.size(), 500);
			tempRoleIds = tempRoleIds.subList(0, maxIndex);
		}
		// id升序,查询时方便
		tempRoleIds.sort(new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return (int)(o1.longValue() - o2.longValue());
			}
		});

		int joinLevel = ClanBaseManager.getInstance().getJoinClanLevel();

		List<Long> roleIds = new LinkedList<Long>();
		for (Long rId : tempRoleIds) {
			// 过滤有公会的
			long clanKey = xtable.Properties.selectClankey(rId);
			if (clanKey > 0) {
				continue;
			}
			// 过滤设置了不让邀请公会的
			Integer refuse = GameSystemConfig.getSysConfig(rId, SysConfigType.refuseclan);
			if (refuse != null && refuse.intValue() == 1) {
				continue;
			}
			// 判断被邀请入会者等级
			int level = xtable.Properties.selectLevel(rId);
			if (level < joinLevel) {
				continue;
			}
			// 筛选等级
			if (type_level != -1 && level < type_level) {
				continue;
			}
			// 筛选职业
			int school = xtable.Properties.selectSchool(rId);
			if (type_school != -1 && getResultSchool(type_school, school) == false) {
				continue;
			}
			// 筛选性别
			int sex = xtable.Properties.selectSex(rId);
			if (type_sex != -1 && sex != type_sex) {
				continue;
			}
			roleIds.add(rId);
			if (roleIds.size() >= MAX_NUM) {
				break;
			}
		}

		// 发送给客户端
		SClanInvitationView send = new SClanInvitationView();
		for (long rId : roleIds) {
			xbean.Properties prop = xtable.Properties.select(rId);
			if (prop != null) {
				InvitationRoleInfo invitationRoleInfo = PInvitationViewProc.createInvitationRoleInfo(rId, prop);
				send.invitationroleinfolist.add(invitationRoleInfo);
			}
		}
		if (send.invitationroleinfolist.size() > 1) {
			Collections.shuffle(send.invitationroleinfolist);
		}
		gnet.link.Onlines.getInstance().send(roleid, send);
		return true;
	}
	
	/**
	 * 生成InvitationRoleInfo
	 * @param roid
	 * @param prop
	 * @return
	 */
	public static InvitationRoleInfo createInvitationRoleInfo(long roid,xbean.Properties prop){
		InvitationRoleInfo invitationRoleInfo=new InvitationRoleInfo();
		invitationRoleInfo.roleid = roid;
		invitationRoleInfo.rolename = prop.getRolename();
		invitationRoleInfo.shape = prop.getShape();
		invitationRoleInfo.level = prop.getLevel();
		invitationRoleInfo.sex = prop.getSex();
		invitationRoleInfo.school = prop.getSchool();
		invitationRoleInfo.fightvalue = prop.getRolezonghemaxscore();
		invitationRoleInfo.vip = prop.getViplevel();
		//人物造型
		fire.pb.map.Role.getPlayerComponents(roid, invitationRoleInfo.components);
		return invitationRoleInfo;
	}
	
	
	public boolean getResultSchool(int type_school,int school){
//		11	战士     1
//		12	圣骑     2
//		13	猎人     4
//		14	德鲁伊  8
//		15	法师    16
//		16	牧师    32
//		17	萨满    64
//		18	盗贼    128
//		19	术士  256
		List<Integer> list=new ArrayList<Integer>();
		if((type_school&1)==1){
			list.add(11);
		}
		if((type_school&2)==2){
			list.add(12);
		}
		if((type_school&4)==4){
			list.add(13);//			13	猎人     4
		}
		if((type_school&8)==8){
			list.add(14);//			14	德鲁伊  8
		}
		if((type_school&16)==16){
			list.add(15);//			15	法师    16
		}
		if((type_school&32)==32){
			list.add(16);//			16	牧师    32
		}
		if((type_school&64)==64){
			list.add(17);//			17	萨满    64
		}
		if((type_school&128)==128){
			list.add(18);//			18	盗贼    128
		}
		if((type_school&256)==256){
			list.add(19);//			19	术士  256
		}
		if(list.contains(school)){
			return true;
		}
		return false;

	}
	

}

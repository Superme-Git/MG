package fire.pb.scene.movable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.move.SSetRoleTeamInfo;
import fire.pb.scene.Scene;
import fire.pb.scene.manager.RoleManager;

public class SceneTeam {

	SceneTeam(long tid, long roleID)
	{
		teamID = tid;
		capitan = RoleManager.getInstance().getRoleByID(roleID);
		capitan.setTeam(this);
		capitan.setTeamIndex(1);
	}
	
	public long getTeamID(){
		return teamID;
	}
	
	public void sendTeamInfoToRole(final Role sendrole){
		
		final SSetRoleTeamInfo snd = new SSetRoleTeamInfo();
		
		if(null == capitan)
			return;
		
		snd.roleid = capitan.getRoleID();
		snd.teamid = teamID;
		snd.teamindex = 1;
		snd.teamstate = SceneTeam.TEAM_NORMAL;
		snd.teamnormalnum = getAllMemberIDs().size();
		sendrole.send(snd);

		for(Role tm : members){
			
			final SSetRoleTeamInfo snd2 = new SSetRoleTeamInfo();
			snd2.roleid = tm.getRoleID();
			snd2.teamid = teamID;
			snd2.teamindex = tm.getTeamIndex();
			snd2.teamstate = SceneTeam.TEAM_NORMAL;
			snd2.teamnormalnum = getAllMemberIDs().size();
			
			sendrole.send(snd2);
		}
	}
	
	public void broadcastTeam(){
		final SSetRoleTeamInfo snd = new SSetRoleTeamInfo();
		StringBuilder sb = new StringBuilder("sendTeamInfoOut : teamid = ").append(teamID);

		if(null == capitan) return;
		final Scene rs = capitan.getScene();
		if(null == rs) return;
		capitan.marshal();
		snd.roleid = capitan.getRoleID();
		snd.teamid = teamID;
		snd.teamindex = 1;
		snd.teamstate = SceneTeam.TEAM_NORMAL;
		snd.teamnormalnum = this.getAllMemberIDs().size();
		
		
		capitan.sendAround(snd);
		for(Role tm : members)
		{
			tm.marshal();
			final SSetRoleTeamInfo snd2 = new SSetRoleTeamInfo();
			snd2.roleid = tm.getRoleID();
			snd2.teamid = teamID;
			snd2.teamindex = tm.getTeamIndex();
			snd2.teamstate = SceneTeam.TEAM_NORMAL;
			snd2.teamnormalnum = this.getAllMemberIDs().size();
			sb.append("; roleid = ").append(snd2.roleid).append(",index=")
					.append(snd2.teamindex).append(",state=")
					.append(snd2.teamstate);

			tm.sendAround(snd2);

		}
		for(Role tl : leavers)
		{
			tl.marshal();
			SSetRoleTeamInfo snd2 = new SSetRoleTeamInfo();
			snd2.roleid = tl.getRoleID();
			snd2.teamid = teamID;
			snd2.teamindex = tl.getTeamIndex();
			snd2.teamstate = SceneTeam.TEAM_APART;
			snd2.teamnormalnum = this.getAllMemberIDs().size();
			sb.append("; roleid = ").append(snd2.roleid).append(",index=")
					.append(snd2.teamindex).append(",state=")
					.append(snd2.teamstate);

			tl.sendAround(snd2);
		}
		Scene.LOG.debug(sb.toString());
	}
	
	public int size()
	{
		return members.size() + 1;
	}
	
	public java.util.List<Role> getMembers()
	{
		return members;
	}

	public boolean canMove()
	{
		for (Role r : members)
		{
			if (!r.checkCanMoveState())
				return false;
		}
		return true;
	}
	
	public Role getCapitan()
	{
		return capitan;
	}

	public long getCapitanRoleID()
	{
		return capitan.getUniqueID();
	}
	
	public java.util.Set<Role> getLeavers()
	{
		return leavers;
	}
	
	/**
	 * 交换队长
	 */
	public void changeCapitan(final Role newCap)
	{
		final Role oldCap = capitan;
		final int oi = newCap.getTeamIndex();
		if(oi == 1 || oi == 0)
			return;//无队伍或者已经是队长
		oldCap.setTeamIndex(oi);
		newCap.setTeamIndex(1);
		capitan = newCap;
		if(oi == -1)
		{
			leavers.remove(newCap);
			leavers.add(oldCap);//原暂离队员做队长
		}
		else if(oi >= 2)
			members.set((oi-2), oldCap);//原正常队员做队长
		//跳跃中换队长，直接拉回跳跃点
		if(oldCap.isJumping())
		{
			oldCap.getJumpRole().confirmPositionToStart();
		}
	}
	
	/**
	 * 作为正常队员，加入队伍
	 */
	public void joinAsMember(Role mem)
	{
		if(members.size() >= (MAX_TEAMSIZE-1))
			return;
		
		if(members.contains(mem))
		{
			Scene.LOG.error("添加正常队员时，已经存在了：" + mem);
			return;
		}
		
		mem.setTeam(this);
		onBackTeam(mem);
		mem.notifySeeTeammates();
	}
	
	/**
	 * 从队伍中暂离, 普通->暂离，无队->暂离
	 */
	public void leaveFromTeam(Role leaver)
	{
		leaver.setTeam(this);
		leaver.leaveFromTeam();
		leavers.add(leaver);
		if(!members.remove(leaver))
		{//正常队员中没有该角色，是无队->暂离
			leaver.notifySeeTeammates();
			return;//不需要走后续了
		}
		
		// 重新整理队伍索引
		int i = 2;
		for(Role m : members)
		{
			m.setTeamIndex(i++);
		}
		leaver.notifyLeaverPosition();
		getCapitan().notifyLeaverPosition();
	}
	
	/**
	 * 队员（正常队员/暂离队员）退出队伍
	 */
	public void quitFromTeam(Role q)
	{
		q.quitFromTeam();
		if(leavers.remove(q))
			return;//暂离中离队的不需要后续处理
		members.remove(q);
		// 重新整理队伍索引
		int i = 2;
		for(Role m : members)
		{
			m.setTeamIndex(i++);
		}
	}
	
	/**
	 * 正常队员交换顺序
	 */
	public void changeSeq(Role r1, Role r2)
	{
		final int i1 = r1.getTeamIndex() - 2;
		final int i2 = r2.getTeamIndex() - 2;
		
		members.set(i1, r2);
		members.set(i2, r1);
		
		r1.setTeamIndex(i2 + 2);
		r2.setTeamIndex(i1 + 2);
	}
	
	/**
	 * 暂离队员 归队
	 */
	public void backToTeam(Role mem)
	{
		if(size() > MAX_TEAMSIZE)
			return;
		
		if(!leavers.remove(mem))
			Scene.LOG.error("暂离队员归队时，不存在：" + mem);
		
		onBackTeam(mem);
	}
	
	/**
	 * 队伍解散的处理
	 */
	public void destroy()
	{
		capitan.quitFromTeam();
		
		for(Role m : members)
		{
			m.quitFromTeam();
		}
		
		for(Role l : leavers)
		{
			l.quitFromTeam();
		}
		
		members.clear();
		leavers.clear();
	}
	/**
	 * 当归队时，有暂离-归队，也有一进入即归队的情况
	 * @param mem
	 */
	private void onBackTeam(Role mem)
	{
		if(members.contains(mem))
			members.remove(mem);
		members.add(mem);
		mem.setTeamIndex(size());
		mem.updatePosition(capitan.getPos());
	}
	
	/**
	 * 获取所有正常队员（包括队长）的role id
	 */
	java.util.Set<Long> getAllMemberIDs()
	{
		final java.util.Set<Long> set = new java.util.HashSet<Long>();
		set.add(capitan.getRoleID());
		for(Role r : members)
		{
			set.add(r.getRoleID());
		}
		if(set.size() != members.size() + 1)
			Scene.LOG.error("队伍的正常队员有重复 : " + members.toString());
		return set;
	}
	
	/**
	 * 获取正常队员（不包括队长）的role id
	 */
	java.util.Set<Long> getMemberIDs()
	{
		final java.util.Set<Long> set = new java.util.HashSet<Long>();
		for(Role r : members)
		{
			set.add(r.getRoleID());
		}
		return set;
	}
	
	/**
	 * 获取暂离队员的roleid
	 */
	java.util.Set<Long> getLeaverIDs()
	{
		final java.util.Set<Long> set = new java.util.HashSet<Long>();
		for(Role r : leavers)
		{
			set.add(r.getRoleID());
		}
		return set;
	}
	
	public byte getHugIndex(long roleId)
	{
		if(hugs == null || hugs.isEmpty())
			return (byte)0;
		List<Long> normalrids = new LinkedList<Long>();
		normalrids.add(capitan.getRoleID());
		for(Role mem : getMembers())
			normalrids.add(mem.getRoleID());
		
		if(!normalrids.contains(roleId))
			return (byte)0;
		
		for(Iterator<Map.Entry<Long, Long>>it = hugs.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<Long, Long> entry = it.next();
			if(!normalrids.contains(entry.getKey())
					|| !normalrids.contains(entry.getValue()))
			{
				it.remove();
				continue;
			}
			if(entry.getKey() == roleId)
			{
				int index = normalrids.indexOf(entry.getValue().longValue());
				if(index < 0)
					return (byte)0;
				return (byte)(index + 1);
			}
			if(entry.getValue() == roleId)
			{
				int index = normalrids.indexOf(entry.getKey().longValue());
				if(index < 0)
					return (byte)0;
				return (byte)(index + 1);
			}
		}
		return (byte)0;
	}
	
	/*private void checkHugs(Map<Long,Long> hugs)
	{
		List<Long> normalrids = new LinkedList<Long>();
		normalrids.add(capitan.getRoleID());
		for(Role mem : getMembers())
			normalrids.add(mem.getRoleID());
		for(Iterator<Map.Entry<Long, Long>>it = hugs.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<Long, Long> entry = it.next();
			if(entry.getKey().equals(entry.getValue())
					|| !normalrids.contains(entry.getKey())
					|| !normalrids.contains(entry.getValue()))
				it.remove();
		}
	}*/
	
	/**
	 * 获取队伍内所有成员，包括队长，正常、暂离的队员
	 * @return
	 */
	public java.util.List<Role> getAllTeammates()
	{
		final java.util.List<Role> list = new java.util.LinkedList<Role>();
		list.add(getCapitan());
		list.addAll(getMembers());
		list.addAll(getLeavers());
		return list;
	}
	
	private final long teamID;
	private Role capitan;
	private final java.util.List<Role> members = new java.util.ArrayList<Role>();
	private final java.util.Set<Role> leavers = new java.util.HashSet<Role>();
	private Map<Long,Long> hugs = new HashMap<Long, Long>();
	
	public final static int TEAM_NONE	= 0;	// 无队伍
	public final static int TEAM_NORMAL = 1; 	// 正常状态
	public final static int TEAM_APART 	= 2; 	// 暂离状态
	public final static int TEAM_LEADER = 5;	// 队长
	
	private final static int MAX_TEAMSIZE = 5;

}

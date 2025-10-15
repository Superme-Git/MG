package fire.pb.scene.manager;

import fire.msp.team.TeamChangeType;
import fire.pb.scene.Scene;
import fire.pb.scene.movable.God;
import fire.pb.scene.movable.Role;
import fire.pb.scene.movable.SceneTeam;

/**
 * 场景上的队伍管理器
 * 
 * @author nobody
 * 
 */
public class SceneTeamManager
{
	public final SceneTeam getTeamByID(long id)
	{
		return teams.get(id);
	}
	
	/**
	 * 创建队伍
	 */
	public void onCreateNewTeam(long teamID, long leaderID)
	{
		
		SceneTeam st = teams.get(teamID);
		if(null != st) 
			return;
		
		st = God.createTeam(teamID, leaderID);
		teams.put(teamID, st);
		
		st.broadcastTeam();
	}

	/**
	 * 解散队伍
	 */
	public void onDestroyTeam(long teamID)
	{
		final SceneTeam st = teams.get(teamID);
		if (null == st) return;
		st.destroy();
		
		teams.remove(teamID);
	}
	
	/**
	 * 更新地图队伍信息
	 * 
	 * @param mapTeam
	 */
	public void onUpdateTeam(long teamid, int type, long roleid, long roleid2)
	{
		Scene.LOG.debug("OnUpdateTeam : teamid = " + teamid + ", type = " + type + ", roleid = " + roleid + ", roleid2 = "+ roleid2);
		if(TeamChangeType.CREATE == type)
		{
			onCreateNewTeam(teamid, roleid);
			return;
		}
		else if(TeamChangeType.DISMISS == type)
		{
			onDestroyTeam(teamid);
			return;
		}
		
		final SceneTeam st = teams.get(teamid);
		if(null == st)
		{
			Scene.LOG.error("OnUpdateTeam : 场景队伍为空, teamid = " + teamid);
			return;
		}
		
		final Role r1 = RoleManager.getInstance().getRoleByID(roleid);
		if(null == r1)
		{
			Scene.LOG.info("OnUpdateTeam : 场景角色不存在, roleid = " + roleid + ", type = " + type);
			return;
		}
		
		switch(type)
		{
			case TeamChangeType.SWITCH_LEADER:
			{
				st.changeCapitan(r1);
			}break;
			
			// 无队-> 普通队员
			case TeamChangeType.ADD_NORMAL_MEMBER:
			{
				st.joinAsMember(r1);
			}break;
			
			// 无队->暂离
			case TeamChangeType.ADD_ABSENT_MEMBER:
			{
				st.leaveFromTeam(r1);
			}break;
			
			// 离队
			case TeamChangeType.REMOVE_MEMBER:
			{
				st.quitFromTeam(r1);
			}break;
			
			// 正常队员交换
			case TeamChangeType.SWITCH_MEMBER:
			{
				Role r2 = RoleManager.getInstance().getRoleByID(roleid2);
				if(null == r2)
					return;
				
				st.changeSeq(r1, r2);
			}break;
			
			// 暂离->普通队员
			case TeamChangeType.CHANGE_MEMBER_NORMAL:
			{
				st.backToTeam(r1);
			}break;
			// 普通->暂离
			case TeamChangeType.CHANGE_MEMBER_ABSENT:
			{
				st.leaveFromTeam(r1);
			}break;
			// 下线离队
			case TeamChangeType.MEMBER_OFFLINE:
			{
				st.quitFromTeam(r1);
			}break;
			
			default: break;
			
		}
		st.broadcastTeam();
	}

	public static SceneTeamManager getInstance()
	{
		if (null == instance)
		{
			instance = new SceneTeamManager();
		}
		return instance;
	}
	
	private SceneTeamManager(){}
	private static SceneTeamManager instance;
	
	private final java.util.Map<Long, SceneTeam> teams = new java.util.HashMap<Long, SceneTeam>();
}

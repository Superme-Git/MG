package fire.pb.buff;

/**
 * 操作类型
 * 
 * 抽象出操作来，分配ID，是为了参与buff和状态互斥
 * 
 * 操作的ID与一次性buff的类似
 * @author nobody
 *
 */
public class OperateType
{
	public static final int TEAM_CREATE = 516000;//创建队伍,判断队长
	public static final int TEAM_INVITE = 516001;//邀请组队,判断队长(队员判断队伍中状态)
	public static final int TEAM_EXPEL_MEMBER = 516002;//请离队员，判断队长(队员不判断)
	public static final int TEAM_ABSENT = 516003;//主动暂离队伍，判断队员
	public static final int TEAM_QUIT = 516004;//主动退出队伍，判断队员（正常和暂离退出队伍都需要判断）
	public static final int TEAM_SET_LEADER = 516005;//主动更换队长，判断现任队长(队员判断队长状态)
	public static final int TEAM_CALL_BACK_MEMBER = 516006;//召唤队员，判断队长(队员判断归队队员状态)
	public static final int TEAM_RETURN = 516007;//回归队伍，判断队长(队员判断归队队员状态)
	public static final int TEAM_SET_FORMATION = 516008;//光环设置，判断队长
	public static final int UPDATE_INBORN = 516009;//冲穴
	public static final int USE_SCENE_SKILL = 516011;//使用场景技能
	public static final int TEAM_DISMISS = 516026;//队长主动解散队伍
}

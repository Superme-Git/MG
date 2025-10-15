package fire.pb.team;

public abstract class TeamFilter
{
	/**
	 * 建立队伍时，检查该过滤器是否生效，Procedure内
	 * @param teamleaderId 建立
	 * @return
	 */
	public abstract boolean checkActive(long leaderId);
	
	/**
	 * 在进队伍时检查，Procedure内
	 * 这个检查是在最后进队时检查，必须实现
	 * @param leaderId 队长
	 * @param roleId 要进入队伍的人
	 * @return
	 */
	protected abstract boolean checkEnterTeam(long leaderId, long roleId);
	
	/**
	 * 在邀请某人时检查，Procedure内
	 * 注意：邀请时可能邀请人没有队伍
	 * @param inviterId 邀请人
	 * @param roleId 被邀请的人
	 * @return
	 */
	protected boolean checkInviteJoin(long inviterId, long roleId)
	{
		return checkEnterTeam(inviterId, roleId);
	}
	
	/**
	 * 在某人申请加队时检查，Procedure内
	 * @param leaderId 队长
	 * @param roleId 申请加队的人
	 * @return
	 */
	protected boolean checkRequestJoin(long leaderId, long roleId)
	{
		return checkEnterTeam(leaderId, roleId);
	}
}

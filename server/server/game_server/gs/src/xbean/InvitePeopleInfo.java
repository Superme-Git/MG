
package xbean;

public interface InvitePeopleInfo extends mkdb.Bean {
	public InvitePeopleInfo copy(); // deep clone
	public InvitePeopleInfo toData(); // a Data instance
	public InvitePeopleInfo toBean(); // a Bean instance
	public InvitePeopleInfo toDataIf(); // a Data instance If need. else return this
	public InvitePeopleInfo toBeanIf(); // a Bean instance If need. else return this

	public long getInviteme(); // 邀请我的人
	public java.util.List<Long> getAminvites(); // 我邀请的人
	public java.util.List<Long> getAminvitesAsData(); // 我邀请的人
	public java.util.List<Integer> getAwardhistory(); // 已经领取过的奖励ID 0-填写邀请人奖励
	public java.util.List<Integer> getAwardhistoryAsData(); // 已经领取过的奖励ID 0-填写邀请人奖励

	public void setInviteme(long _v_); // 邀请我的人
}

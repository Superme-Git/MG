
package xbean;

public interface LDVideoRoleInfoDes extends mkdb.Bean {
	public LDVideoRoleInfoDes copy(); // deep clone
	public LDVideoRoleInfoDes toData(); // a Data instance
	public LDVideoRoleInfoDes toBean(); // a Bean instance
	public LDVideoRoleInfoDes toDataIf(); // a Data instance If need. else return this
	public LDVideoRoleInfoDes toBeanIf(); // a Bean instance If need. else return this

	public xbean.LDRoleInfoDes getRole1(); // 对手1
	public xbean.LDRoleInfoDes getRole2(); // 对手1
	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1(); // //如果是组队，队员详情
	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist1AsData(); // //如果是组队，队员详情
	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2(); // //如果是组队，队员详情
	public java.util.List<xbean.LDTeamRoleInfoDes> getTeamlist2AsData(); // //如果是组队，队员详情
	public int getBattleresult(); // 1胜利  -1失败  0平局
	public int getRosenum(); // 点赞次数
	public String getVideoid(); // 录像id
	public com.locojoy.base.Octets getVideoidOctets(); // 录像id
	public long getFighttime(); // 时间
	public int getBeforevideosize(); // 压缩前录像大小
	public long getAftervideosize(); // 压缩后录像大小
	public String getVideourl(); // url
	public com.locojoy.base.Octets getVideourlOctets(); // url
	public int getSaveresult(); // 0没有保存   1保存成功

	public void setBattleresult(int _v_); // 1胜利  -1失败  0平局
	public void setRosenum(int _v_); // 点赞次数
	public void setVideoid(String _v_); // 录像id
	public void setVideoidOctets(com.locojoy.base.Octets _v_); // 录像id
	public void setFighttime(long _v_); // 时间
	public void setBeforevideosize(int _v_); // 压缩前录像大小
	public void setAftervideosize(long _v_); // 压缩后录像大小
	public void setVideourl(String _v_); // url
	public void setVideourlOctets(com.locojoy.base.Octets _v_); // url
	public void setSaveresult(int _v_); // 0没有保存   1保存成功
}

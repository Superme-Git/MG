
package xbean;

public interface ClanProgressRankRecord extends mkdb.Bean {
	public ClanProgressRankRecord copy(); // deep clone
	public ClanProgressRankRecord toData(); // a Data instance
	public ClanProgressRankRecord toBean(); // a Bean instance
	public ClanProgressRankRecord toDataIf(); // a Data instance If need. else return this
	public ClanProgressRankRecord toBeanIf(); // a Bean instance If need. else return this

	public int getRank(); // 排名 by changhao
	public long getClankey(); // 
	public String getClanname(); // 公会名称 by changhao
	public com.locojoy.base.Octets getClannameOctets(); // 公会名称 by changhao
	public long getTime(); // 时间 by changhao
	public int getProgress(); // 进度 by changhao
	public String getClanmastername(); // 会长名字 by changhao
	public com.locojoy.base.Octets getClanmasternameOctets(); // 会长名字 by changhao
	public long getTriggertime(); // 触发时间 by changhao
	public int getCopyid(); // 副本id by changhao
	public String getCopyname(); // 副本名字 by changhao
	public com.locojoy.base.Octets getCopynameOctets(); // 副本名字 by changhao
	public float getBosshp(); // BOSS血量 by changhao

	public void setRank(int _v_); // 排名 by changhao
	public void setClankey(long _v_); // 
	public void setClanname(String _v_); // 公会名称 by changhao
	public void setClannameOctets(com.locojoy.base.Octets _v_); // 公会名称 by changhao
	public void setTime(long _v_); // 时间 by changhao
	public void setProgress(int _v_); // 进度 by changhao
	public void setClanmastername(String _v_); // 会长名字 by changhao
	public void setClanmasternameOctets(com.locojoy.base.Octets _v_); // 会长名字 by changhao
	public void setTriggertime(long _v_); // 触发时间 by changhao
	public void setCopyid(int _v_); // 副本id by changhao
	public void setCopyname(String _v_); // 副本名字 by changhao
	public void setCopynameOctets(com.locojoy.base.Octets _v_); // 副本名字 by changhao
	public void setBosshp(float _v_); // BOSS血量 by changhao
}

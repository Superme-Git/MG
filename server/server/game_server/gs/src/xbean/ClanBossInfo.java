
package xbean;

public interface ClanBossInfo extends mkdb.Bean {
	public ClanBossInfo copy(); // deep clone
	public ClanBossInfo toData(); // a Data instance
	public ClanBossInfo toBean(); // a Bean instance
	public ClanBossInfo toDataIf(); // a Data instance If need. else return this
	public ClanBossInfo toBeanIf(); // a Bean instance If need. else return this

	public int getBossmonsterid(); // 
	public long getMaxhp(); // 
	public long getHp(); // 
	public int getFighteindx(); // 
	public java.util.List<Long> getWinroleids(); // 最后一击的角色
	public java.util.List<Long> getWinroleidsAsData(); // 最后一击的角色
	public long getBossnpckey(); // 

	public void setBossmonsterid(int _v_); // 
	public void setMaxhp(long _v_); // 
	public void setHp(long _v_); // 
	public void setFighteindx(int _v_); // 
	public void setBossnpckey(long _v_); // 
}

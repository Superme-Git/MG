
package xbean;

public interface CrossTempRole extends mkdb.Bean {
	public CrossTempRole copy(); // deep clone
	public CrossTempRole toData(); // a Data instance
	public CrossTempRole toBean(); // a Bean instance
	public CrossTempRole toDataIf(); // a Data instance If need. else return this
	public CrossTempRole toBeanIf(); // a Bean instance If need. else return this

	public int getZoneid(); // 
	public long getLastcopytime(); // 
	public short getCopyresult(); // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
	public java.util.Map<String, Boolean> getFinishedtable(); // 已经完成拷贝的数据表.这个字段在跨服服务器用到
	public java.util.Map<String, Boolean> getFinishedtableAsData(); // 已经完成拷贝的数据表.这个字段在跨服服务器用到
	public boolean getCrosstatus(); // 跨服状态
	public int getFlag(); // 0=战队赛

	public void setZoneid(int _v_); // 
	public void setLastcopytime(long _v_); // 
	public void setCopyresult(short _v_); // 0为未开始，1为正在拷贝，2为成功，3为拷贝失败
	public void setCrosstatus(boolean _v_); // 跨服状态
	public void setFlag(int _v_); // 0=战队赛
}

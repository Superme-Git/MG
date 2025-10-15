
package xbean;

public interface BuffAgent extends mkdb.Bean {
	public BuffAgent copy(); // deep clone
	public BuffAgent toData(); // a Data instance
	public BuffAgent toBean(); // a Bean instance
	public BuffAgent toDataIf(); // a Data instance If need. else return this
	public BuffAgent toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.Buff> getBuffs(); // key为buffId
	public java.util.Map<Integer, xbean.Buff> getBuffsAsData(); // key为buffId
	public java.util.Map<Integer, Integer> getBattleendclear(); // 如果在战斗中buff到时，id放入此表，等战斗结束一起执行。key为buffId，value为处理方式：1为detach，2为period process
	public java.util.Map<Integer, Integer> getBattleendclearAsData(); // 如果在战斗中buff到时，id放入此表，等战斗结束一起执行。key为buffId，value为处理方式：1为detach，2为period process
	public java.util.Map<Integer, Long> getTimerfutures(); // key=buffId value=future id
	public java.util.Map<Integer, Long> getTimerfuturesAsData(); // key=buffId value=future id

}


package xbean;

public interface TransfromInfo extends mkdb.Bean {
	public TransfromInfo copy(); // deep clone
	public TransfromInfo toData(); // a Data instance
	public TransfromInfo toBean(); // a Bean instance
	public TransfromInfo toDataIf(); // a Data instance If need. else return this
	public TransfromInfo toBeanIf(); // a Bean instance If need. else return this

	public int getTransformid(); // 人物变身时的shape
	public int getQuestid(); // 
	public long getValiddate(); // 人物变身时的时限
	public int getRide(); // 人物变身时的坐骑

	public void setTransformid(int _v_); // 人物变身时的shape
	public void setQuestid(int _v_); // 
	public void setValiddate(long _v_); // 人物变身时的时限
	public void setRide(int _v_); // 人物变身时的坐骑
}


package xbean;

public interface BasicFightProperties extends mkdb.Bean {
	public BasicFightProperties copy(); // deep clone
	public BasicFightProperties toData(); // a Data instance
	public BasicFightProperties toBean(); // a Bean instance
	public BasicFightProperties toDataIf(); // a Data instance If need. else return this
	public BasicFightProperties toBeanIf(); // a Bean instance If need. else return this

	public int getStr(); // 力量
	public int getIq(); // 智力
	public int getCons(); // 体质
	public int getEndu(); // 耐力
	public int getAgi(); // 敏捷

	public void setStr(int _v_); // 力量
	public void setIq(int _v_); // 智力
	public void setCons(int _v_); // 体质
	public void setEndu(int _v_); // 耐力
	public void setAgi(int _v_); // 敏捷
}

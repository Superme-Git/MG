
package xbean;

public interface totalNumber extends mkdb.Bean {
	public totalNumber copy(); // deep clone
	public totalNumber toData(); // a Data instance
	public totalNumber toBean(); // a Bean instance
	public totalNumber toDataIf(); // a Data instance If need. else return this
	public totalNumber toBeanIf(); // a Bean instance If need. else return this

	public long getTotalnumber(); // 

	public void setTotalnumber(long _v_); // 
}

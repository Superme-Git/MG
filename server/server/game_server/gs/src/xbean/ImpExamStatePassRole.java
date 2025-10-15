
package xbean;

public interface ImpExamStatePassRole extends mkdb.Bean {
	public ImpExamStatePassRole copy(); // deep clone
	public ImpExamStatePassRole toData(); // a Data instance
	public ImpExamStatePassRole toBean(); // a Bean instance
	public ImpExamStatePassRole toDataIf(); // a Data instance If need. else return this
	public ImpExamStatePassRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public int getAccrightnum(); // 累积答对次数(vill---prov--state)
	public long getStateexamtime(); // 通过stateexam所用的时间

	public void setRoleid(long _v_); // 
	public void setAccrightnum(int _v_); // 累积答对次数(vill---prov--state)
	public void setStateexamtime(long _v_); // 通过stateexam所用的时间
}

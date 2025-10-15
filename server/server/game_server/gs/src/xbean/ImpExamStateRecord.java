
package xbean;

public interface ImpExamStateRecord extends mkdb.Bean {
	public ImpExamStateRecord copy(); // deep clone
	public ImpExamStateRecord toData(); // a Data instance
	public ImpExamStateRecord toBean(); // a Bean instance
	public ImpExamStateRecord toDataIf(); // a Data instance If need. else return this
	public ImpExamStateRecord toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getIdslist(); // 能参加智慧试炼state测试的id列表
	public java.util.List<Long> getIdslistAsData(); // 能参加智慧试炼state测试的id列表
	public java.util.List<Long> getPassidslist(); // 通过的角色id列表
	public java.util.List<Long> getPassidslistAsData(); // 通过的角色id列表
	public java.util.List<xbean.ImpExamStatePassRole> getPassrolelist(); // 通过的角色列表
	public java.util.List<xbean.ImpExamStatePassRole> getPassrolelistAsData(); // 通过的角色列表

}

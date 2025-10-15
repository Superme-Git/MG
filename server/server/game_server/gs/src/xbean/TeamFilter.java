
package xbean;

public interface TeamFilter extends mkdb.Bean {
	public TeamFilter copy(); // deep clone
	public TeamFilter toData(); // a Data instance
	public TeamFilter toBean(); // a Bean instance
	public TeamFilter toDataIf(); // a Data instance If need. else return this
	public TeamFilter toBeanIf(); // a Bean instance If need. else return this

	public fire.pb.team.TeamFilter getFilter(); // 

	public void setFilter(fire.pb.team.TeamFilter _v_); // 
}


package xbean;

public interface ETeamMatch extends mkdb.Bean {
	public ETeamMatch copy(); // deep clone
	public ETeamMatch toData(); // a Data instance
	public ETeamMatch toBean(); // a Bean instance
	public ETeamMatch toDataIf(); // a Data instance If need. else return this
	public ETeamMatch toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdata(); // 
	public java.util.Map<Long, xbean.TeamMatch> getRoleid2matchdataAsData(); // 
	public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdata(); // 
	public java.util.Map<Long, xbean.TeamMatch> getTeamid2matchdataAsData(); // 
	public java.util.List<xbean.TeamMatch> getTeammatchdatalist(); // 
	public java.util.List<xbean.TeamMatch> getTeammatchdatalistAsData(); // 
	public java.util.List<xbean.TeamMatch> getRolematchdatalist(); // 
	public java.util.List<xbean.TeamMatch> getRolematchdatalistAsData(); // 

}

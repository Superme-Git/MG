package fire.pb.mission.activelist;

public class POpenAwardBox extends mkdb.Procedure{
	final private long roleId;
	final private int boxid;
	
	public POpenAwardBox(long roleId, int boxid){
		this.roleId = roleId;
		this.boxid = boxid;
	}

	@Override
	protected boolean process() throws Exception{
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleId, false);
		if(actrole == null)
			return false;
		return actrole.openAwardBox(boxid);
	}
}

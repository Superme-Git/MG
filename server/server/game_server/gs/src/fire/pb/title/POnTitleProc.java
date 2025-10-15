package fire.pb.title;


//佩戴和卸下称谓的过程(title == -1 (title < 0)为卸下称谓)
public class POnTitleProc extends mkdb.Procedure {
	private long roleid;
	private int titleid;
	
	public POnTitleProc(final long roleid, final int titleid){
		this.roleid = roleid;
		this.titleid = titleid;
	}
	public boolean process(){
		if (roleid < 0)
			return false;
		Title title = new Title(roleid, false);
		
		if (!title.checkUpdateTitle())
			return false;
		
		if (titleid >= 0)
			return title.onTitle(titleid);
		else return title.offTitle();
	}

}

package fire.pb.title;

public class PRemoveTitleProc extends mkdb.Procedure{
	private long roleid;
	private int titleid;
	
	public PRemoveTitleProc(final long roleid, final int titleid){
		this.roleid = roleid;
		this.titleid = titleid;
	}
	public boolean process(){
		Title title = new Title(roleid, false);
		return title.removeTitle(titleid);
	}

}

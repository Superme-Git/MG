package fire.pb.npc;

import mkdb.Procedure.Done;

public class DoneWhileSubmitPet<T extends mkdb.Procedure> implements Done<T> {
	@SuppressWarnings("unused")
	private final long roleid;
	private final java.util.List<xbean.PetInfo> pis;
	@SuppressWarnings("unused")
	private final int questid;
	public DoneWhileSubmitPet(final long roleid,final int questid, final java.util.List<xbean.PetInfo> pis ){
		this.roleid = roleid;
		this.questid = questid;
		this.pis = pis;
	}
	@Override
	public void doDone(T p) {
		if ( pis != null && p.isSuccess() ) {		
//			for ( xbean.PetInfo pi : pis) {
//				
//			}
		}
	}
}

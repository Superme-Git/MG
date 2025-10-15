

package fire.pb.util;

import fire.pb.SStartPlayCG;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;
import mkdb.Transaction;


public class CGPlayUtil {

	public static int cgBuffID = FireProp.getIntValue("task", "mtgserv.cg.buffid");

	public static class PRemoveCGBuff extends Procedure{
		private long roleid;
		public PRemoveCGBuff(long roleid){
			this.roleid = roleid;
		}
		@Override
		protected boolean process() throws Exception {
			fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(roleid,false);
			agent.removeCBuffWithSP(CGPlayUtil.cgBuffID);
			return true;
		}
	}
	public static void sendStartCGProtocal(final long roleid, int cgID) {

		final SStartPlayCG sspcg = new SStartPlayCG();
		sspcg.id = cgID;
		if (Transaction.current() != null) {
			BuffAgent agent = new BuffRoleImpl(roleid);
			if(agent.addCBuffWithSP(cgBuffID))
			{//GsClient.pSendWhileCommit(new GSetPlayCGState(roleid,(byte)1));
			Procedure.psendWhileCommit(roleid, sspcg);}
		} else
			{
			 new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception {
					BuffAgent agent = new BuffRoleImpl(roleid);
					if(agent.addCBuffWithSP(cgBuffID))
					{//GsClient.pSendWhileCommit(new GSetPlayCGState(roleid,(byte)1));
					Procedure.psendWhileCommit(roleid, sspcg);}
					return true;
				}
			 }.submit();}

	}
	public static void setCGStateOn(final long roleid){
		if (Transaction.current() != null) {
			BuffAgent agent = new BuffRoleImpl(roleid);
			agent.addCBuffWithSP(cgBuffID);
			//GsClient.pSendWhileCommit(new GSetPlayCGState(roleid,(byte)1));
		} else
			{
			 new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception {
					BuffAgent agent = new BuffRoleImpl(roleid);
					agent.addCBuffWithSP(cgBuffID);
					//GsClient.pSendWhileCommit(new GSetPlayCGState(roleid,(byte)1));
					return true;
				}
			 }.submit();}
	}
	public static void setCGStateOff(final long roleid){
		
	}
	public static boolean existCGBuff(final long roleid){
		BuffAgent agent = new BuffRoleImpl(roleid, true);
		if (agent.existBuff(cgBuffID))
			return true;
		else {
			return false;
		}
	}
	public static void removeTeamMemberCGBuff(final Long leaderid){
		Procedure removeCGBuff =new Procedure(){
			@Override
			protected boolean process() throws Exception {
              Team team = TeamManager.selectTeamByRoleId(leaderid);
				if (team!=null&&team.isTeamLeader(leaderid)){
					java.util.List<Long> normalMems = team.getNormalMemberIds();
					normalMems.remove(leaderid);
					for (final Long rid : normalMems) {
						pexecuteWhileCommit(new PRemoveCGBuff(rid));
					}
				}
				return true;
			}
			
		};
		if (Transaction.current()==null)
			removeCGBuff.submit();
		else
			Procedure.pexecuteWhileCommit(removeCGBuff);
	}
}

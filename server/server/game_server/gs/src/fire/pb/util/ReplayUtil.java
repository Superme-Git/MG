package fire.pb.util;

import fire.pb.buff.BuffConstant;
import mkdb.Procedure;


public class ReplayUtil {

	public static class PRemoveReplayBuff extends Procedure{
		private long roleid;
		public PRemoveReplayBuff(long roleid){
			this.roleid = roleid;
		}
		@Override
		protected boolean process() throws Exception {
			fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(roleid,false);
			agent.removeCBuffWithSP(BuffConstant.StateType.STATE_REPLAY);
			return true;
		}
	}
}

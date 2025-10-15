package fire.pb;

import java.util.concurrent.ScheduledFuture;
public class PAddOfflineFuture extends mkdb.Procedure{
	
	private long roleid;
	private ScheduledFuture<?> fur;
	private boolean add;
	
	public PAddOfflineFuture(final long roleid, final ScheduledFuture<?> fur, boolean add){
		this.roleid = roleid;
		this.fur = fur;
		this.add = add;
	}
	
	public boolean process(){
		xbean.OfflineFuture efur = xtable.Offlinefuture.get(roleid);
		if (add){
			if (efur != null && efur.getTimefuture()!=null ){
				efur.getTimefuture().cancel(true);
			}
			if (efur == null){
				efur = xbean.Pod.newOfflineFuture();
				xtable.Offlinefuture.add(roleid, efur);
			}
			efur.setTimefuture(fur);
		}
		else{
			if (efur != null && efur.getTimefuture() != null){
				efur.getTimefuture().cancel(true);
			}
		}
		return true;
	}
}

package fire.pb.activity;


public class CancelDelayActivity implements Runnable {

	private DelayActivity activity;
	
	public CancelDelayActivity(DelayActivity activity) {
       this.activity = activity;
	}

	@Override
	public void run() {
      DelayActivityManager.getInstance().removeDelayActivity(activity);
	}

}


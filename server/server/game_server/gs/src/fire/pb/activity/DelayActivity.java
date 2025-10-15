
package fire.pb.activity;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;



//有些活动是跟副本相关的,如果副本没有创建,要推迟到第一个人进入副本之后开始,
//这个类存储了类似这样的活动,当第一个玩家创建并进入副本的时候,根据mapid和
//ownerid来判断是否要触发事件
public class DelayActivity {

	private int mapid;  //副本的base mapid

	private long ownerid; //副本的ownerid

	private DelayActivityThread triggerEventThread; //要触发的事件写在这里

	private Future<?> future; //一般是用来时间到的时候取消这个activity,如果在到期之前activity执行了,则不用执行了
	
	private long endTime; //如果超过这个时间,则不执行了

	public DelayActivity(int mapid, long ownerid, Class<? extends DelayActivityThread> triggerEvent,long endTime) {

		super();
		this.mapid = mapid;
		this.ownerid = ownerid;
		try {
			Constructor<? extends DelayActivityThread> con = triggerEvent.getConstructor(int.class,long.class);
			this.triggerEventThread = con.newInstance(mapid,ownerid);
		} catch (Exception e) {
			this.triggerEventThread = null;
		} 
		this.endTime = endTime;
	}

	public int getMapid() {

		return mapid;
	}

	public long getOwnerid() {

		return ownerid;
	}

	public Future<?> getFuture() {

		return future;
	}

	public DelayActivityThread getTriggerEventThread() {

		return triggerEventThread;
	}
	
	public void setEndTime(long endTime) {

		this.endTime = endTime;
	}
	
	public long getEndTime() {

		return endTime;
	}
	
	public void setFuture(Future<?> future) {
	
		this.future = future;
	}

}

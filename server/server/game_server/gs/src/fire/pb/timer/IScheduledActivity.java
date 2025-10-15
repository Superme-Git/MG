
package fire.pb.timer;

import org.apache.log4j.Logger;


public interface IScheduledActivity {
	
	public static final Logger logger = Logger.getLogger("ACTIVE");

	
	/**
	 * start:定时活动开始,一般各个活动在开始的时候都要初始化,比如放出npc,读取数据,拉人,等等.统一在这个方法里调用.
	 * 注意,如果活动前有公告,也是算在活动自己的逻辑里面的,除非是间隔时间超过一天的公告
	 * @return
	 * @throws Exception
	 */
	public boolean start() throws Exception;
	
	/**
	 * stop:临时中止该活动.游戏上线之后可能会有需求突然中断这个活动.如果有这个需求,统一在这个方法里调用.
	 * @param cancelFuture
	 * @return
	 * @throws Exception
	 */
	public boolean stop(boolean cancelFuture) throws Exception;
	
	/**
	 * end:活动正常结束时调用该方法,做一些清除操作,比如清掉npc,统计玩家成绩等等
	 * @param cancelFuture
	 * @return
	 * @throws Exception
	 */
	public boolean end(boolean cancelFuture) throws Exception;
	
	
	
	/**
	 * resume:重新开始活动,有两种,一种是gm指令继续该活动,还有一种是服务器停服之后继续该活动.
	 * AbstractScheduledActivity里默认的实现是什么都不干(return 0不会触发任何操作).子类可以覆盖这个方法.
	 * 
	 * @param gm true:是gm指令继续活动. false:服务器重启继续该活动
	 * @param lastStartTime 上次活动的开始时间
	 * @param lastEndTime 上次活动的结束时间
	 * @param costTime 在resume之前活动已经举行了多长时间.注意costTime不一定等于lastEndTime-lastStartTime,因为可能
	 * 活动时间很长,在这期间服务器重启过不只一次,lastEndTime,lastStartTime只是记录最近一次的起止时间,costTime是活动从开始之后
	 * 所耗费的总时间
	 * @return  返回这个活动的结束时间.因为活动是暂停后继续的,所以原来配的结束时间肯定不适用了,需要自己根据各个活动的情况返回结束时间,结束时间一到,
	 * 会自动调用endActivity    
	 * @throws Exception
	 */
	public long resume(boolean gm,long lastStartTime,long lastEndTime,long costTime) throws Exception;
	

}


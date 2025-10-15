package fire.pb.shop.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import fire.pb.shop.srv.market.MarketManager;

public class TimingQueue<E> extends LinkedBlockingQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -818906940627447482L;
	
	// 最后从队列中取数据的时间,单位:毫秒
	private AtomicLong lastPollTime = new AtomicLong(0);
	
	public TimingQueue(int capacity) {
		super(capacity);
	}

	/**
	 * 带有定频获取并移除此队列的头部，有5秒钟等待时间前等待可用的元素
	 * @param timingTime  从队列中取数据间隔频率,用 unit 的时间单位表示(目前只支持毫秒)
	 * @param timeout 放弃之前要等待的时间长度，用 unit 的时间单位表示
	 * @param unit 确定如何解释 timeout 参数的 TimeUnit
	 * @return 此队列的头部；如果在元素可用前超过了指定的等待时间，则返回 null 
	 * @throws InterruptedException 如果在等待时被中断
	 */
	public E poll(long timingTime, long timeout, TimeUnit unit) throws InterruptedException {
		if (!checkTime(timingTime, unit))
			return null;
		return super.poll(timeout, unit);
	}
	
	/**
	 * 带有定频获取并移除此队列的头部，如果此队列为空，则返回 null。 
	 * @param timingTime  从队列中取数据间隔频率,用 unit 的时间单位表示(目前只支持毫秒)
	 * @param unit 确定如何解释 timeout 参数的 TimeUnit
	 * @return 队列的头，如果此队列为空，则返回 null 
	 */
	public E poll(long timingTime, TimeUnit unit) {
		if (!checkTime(timingTime, unit))
			return null;
		return poll();
	}
	
	/**
	 * 获取并移除此队列的头部，在元素变得可用之前一直等待（如果有必要）。 
	 * @param timingTime  从队列中取数据间隔频率,用 unit 的时间单位表示(目前只支持毫秒)
	 * @param unit 确定如何解释 timeout 参数的 TimeUnit
	 * @return 此队列的头部 
	 * @throws InterruptedException 如果在等待时被中断
	 */
	public E take(long timingTime, TimeUnit unit) throws InterruptedException {
		if (!checkTime(timingTime, unit))
			return null;
		return take();
	}
	
	/**
	 * 验证间隔时间是否合法
	 * @param timingTime 从队列中取数据间隔频率,用 unit 的时间单位表示(目前只支持毫秒)
	 * @param unit 确定如何解释 timeout 参数的 TimeUnit
	 */
	private boolean checkTime(long timingTime, TimeUnit unit) {
		long tmp = lastPollTime.get();
		Long currTime = System.currentTimeMillis();
		if (tmp > currTime) {//防止向前调整时间,时间戳记录的时间大于当前时间好多,一直返回false
			lastPollTime.set(currTime);
			return true;
		}
		if (tmp > 0) {
			if (currTime - tmp < timingTime) {// 时间未到
//				if (MarketManager.LOG.isDebugEnabled()) {
//					MarketManager.LOG.debug("摆摊操作过于频繁,拒绝请求服务.");
//				}
				return false;
			}
		}
		lastPollTime.set(currTime);
		return true;
	}

}

package fire.pb.shop.srv.market;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import fire.pb.shop.utils.TimingQueue;
import mkdb.ThreadHelper;

/**
 * 限速器
 * 
 * @author liangyanpeng
 *
 */
public class MarketThread extends ThreadHelper {

	private static final Logger LOG = Logger.getLogger("MARKET");

	private final static String THREAD_NAME = "MarketThread";
	
	private final static int MAX_QUEUE_SIZE = 100;
	
	private MarketThread(String name) {
		super(name);
	}

	static class MarketThreadHolder {
		static MarketThread instance = new MarketThread(THREAD_NAME);
	}

	public static MarketThread getInstance() {
		return MarketThreadHolder.instance;
	}

	@Override
	public void run() {
		while (isRunning() || !fastProtocols.isEmpty()) {
			try {
				mkio.Protocol fastP = fastProtocols.poll(20, 5, TimeUnit.SECONDS);
				if (null != fastP) {
					fastP.run();
				}
//				mkio.Protocol slowP = slowProtcols.poll(20, 1, TimeUnit.SECONDS);
//				if (null != slowP) {
//					slowP.run();
//				}
			} catch (Exception e) {
				LOG.error("err", e);
			}
		}
	}

	/**
	 * 区分协议的类型，如果不是摆摊则返回false，并不dispatch
	 */
	public boolean tryAdd(mkio.Protocol p) {
		if (p.getType() >= 810639 && p.getType() <= 810664) {
//			if (p.getType() == 810665) {
//				try {
//					return slowAdd(p);
//				} catch (Exception e) {
//					LOG.error("拍卖慢查询队列已满, 拒绝新的请求服务!");
//					return true;
//				}
//			}
//			
			try {
				if (fastProtocols.size() < MAX_QUEUE_SIZE) {
					return fastAdd(p);
				}
				else {
					if (LOG.isInfoEnabled()) {
						LOG.info("拍卖快查询队列已满, 拒绝新的请求服务!");
					}
					return true;
				}
			} catch (Exception e) {
				LOG.error("拍卖主线程：", e);
				return true;
			}
		}
		return false;
	}

	public boolean fastAdd(mkio.Protocol p) {
		return fastProtocols.add(p);
	}
	
	public boolean slowAdd(mkio.Protocol p) {
		return slowProtcols.add(p);
	}

	public void clear() {
		fastProtocols.clear();
//		slowProtcols.clear();
	}
	// 队列长度50, 超过阀值拒绝添加到队列
	private final TimingQueue<mkio.Protocol> fastProtocols = new TimingQueue<>(MAX_QUEUE_SIZE);
	// 队列长度25, 超过阀值拒绝添加到队列
	// 大查询时间队列处理
	private final TimingQueue<mkio.Protocol> slowProtcols = new TimingQueue<>(25);

}

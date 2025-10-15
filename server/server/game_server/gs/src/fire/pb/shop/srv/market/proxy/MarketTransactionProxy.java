package fire.pb.shop.srv.market.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.jdbc.ConnectionUtils;
import fire.pb.shop.srv.market.jdbc.Transaction;

/**
 * 
 * 动态代理, 为了处理摆摊中物品上架、下架、购买时候的数据库事务处理.
 * 
 * @author liangyanpeng
 *
 */
public class MarketTransactionProxy implements InvocationHandler {
	
	// 目标对象 
	private Object target;
	
	public MarketTransactionProxy(Object target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		before();
		
		// 执行目标对象的方法
		Object result = new Boolean(false);
		try {
			result = method.invoke(target, args);
		} catch (Exception e) {
			result = new Boolean(false);
			MarketManager.LOG.error(e);
		} finally {
			after(result);
		}
		
		return result;
	}
	
	protected void before() {
		Transaction.begin();
	}
	
	protected void after(Object result) {
		if (null == result) {
			if (ConnectionUtils.closeConnetion()) {
				if (MarketManager.LOG.isDebugEnabled()) {
					MarketManager.LOG.debug(Thread.currentThread().getName() + "结果Null关闭连接");
				}
			}
			return;
		}

		Boolean success = (Boolean) result;
		if (success) {
			if (!Transaction.commit()) {
				MarketManager.LOG.error("数据库事务提交失败");
			}
		} else {
			if (!Transaction.rollback()) {
				MarketManager.LOG.error("数据库事务回滚失败");
			}
		}

		ConnectionUtils.closeConnetion();
	}

	/**
	 * 获取目标对象的代理对象
	 * @return 代理对象
	 */
	public Object getProxy() {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
				target.getClass().getInterfaces(), this);
	}


}

package mkdb;

import java.util.concurrent.*;
import java.util.*;

import fire.pb.hotdeploy.Module;


/**
 * 存储过程实现基类.应用通过继承重载process来访问xbean,xtable.
 * 过程支持嵌套, <b>异常处理:不能捕获xdb.XError及其子类.</b>
 */
public class Procedure {

	protected final void begin() {
		Transaction.current().begin();
	}

	protected final int beginAndSavepoint() {
		Transaction transaction = Transaction.current();
		transaction.begin();
		return transaction.savepoint();
	}

	protected final void commit() {
		Transaction.current().commit();
	}

	protected final int savepoint() {
		return Transaction.current().savepoint();
	}

	/**
	 * 存储过程回滚限制:在嵌套调用时,只允许回滚到在当前过程中定义的保存点.
	 * 
	 * 保存点是按时间线排列的,只要知道保存点编号,原则上可以回滚到当前存在的任意保存点.
	 * 这在一般数据库中是允许的,但对于编程来说,很难进行正确性控制.
	 * 比如:存储过程A中定义的保存点被嵌套调用的存储过程B回滚了,那么当B退出时,就一定要
	 * 告诉A,A也要进行适当的处理.总之,比较复杂,不容易控制.
	 * 
	 * 目前实现没有检查回滚限制.应该没有人这么用.
	 * 不允许回滚到外层保存点比较容易实现,如下被注释掉代码.
	 * 不允许回滚到内层保存点还没想到好办法.
	 */
	protected final void rollback(int savepoint) {
		Transaction.current().rollback(savepoint);
	}

	protected final int trancount() {
		return Transaction.current().trancount();
	}
	
	protected final void rollbackAll() {
		throw new XError("rollbackAll");
	}

	protected void lock(TTable<?, ?> table, Collection<?> keys) {
		Lockeys.lock(table, keys);
	}

	/**
	 * 获得多条记录锁，对锁的进行顺序并且加入当前事务。
	 * 当明确知道需要锁多条记录时，使用这个方法，可以减少死锁的可能。
	 * @param lockeys 锁
	 */
	protected void lock(Lockey[] lockeys) {
		Lockeys.lock(lockeys);
	}

	/**
	 * 定义保存点,处理提交回滚.
	 * 嵌套调用或者直接调用.
	 * 
	 * 除非需要特别的保存点管理方式,一般不重载实现这个方法.
	 */
	public boolean call() {
		if (Transaction.current() == null) {
			try {
				// perform 将回调本函数,然后执行事务已经存在的分支.
				Transaction.create().perform(this);
			} catch (Throwable e) {
				// this.setException(e); 在 Transaction.perform 里面会保存异常,这里没什么事可做了.
			} finally {
				Transaction.destroy();
				this.fetchTasks();
			}
			return this.isSuccess();
		}

		int savepoint = beginAndSavepoint();

		// 捕捉所有异常,在发生异常和process返回false时,回滚到过程开始的保存点.
		// 不捕捉错误,所有的错误抛到外层.
		try {
	        if (Module.hasNewProcedureClass()){//if new classes are loaded
	        	Procedure hotdeployProc = Module.getHotdeployProcedure(this.getClass().getName(), this);
	        	   if (hotdeployProc == null) {//if this procedure has no new version
	        		   if (process()) {
	       				commit();
	       				this.setSuccess(true);
	       				return true;
	       			  }
	        	   }else if(hotdeployProc.process()){
	                   commit();
	                   this.setSuccess(true);
	                   return true;
	               }
	        }
	        else if (process()) {
				commit();
				this.setSuccess(true);
				return true;
			}
		} catch (Exception ex) {
			this.setException(ex);
			Trace.log(this.getConf().getTrace(), "Procedure execute", ex);
		}

		rollback(savepoint);
		return false;
	}

	/**
	 * 重载,写程序的地方.
	 * <pre>
	 * 异常错误处理.
	 *    catch (Exception) : 如果 catch 只记日志,就不要 catch.释放资源,使用 finally.
	 *    DO NOT catch(Error) 
	 *    DO NOT catch(Throwable)
	 * </pre>
	 * @return
	 *     true  数据处理成功.提交修改,* 嵌套调用。
	 *     false 数据处理失败,当前过程所有的修改被放弃,回滚到过程开始时的保存点.
	 */
	protected boolean process() throws Exception {
		return false;
	}

	/////////////////////////////////////////////////////////
	// 调用接口
	private static void verify() {
		if (Transaction.current() != null)
			throw new IllegalStateException("can not submit in transaction.");
	}

	/**
	 * 创建存储过程执行任务，提交到线程池中。
	 * @return Future 用于等待存储过程执行完成。
	 * @throws IllegalStateException 如果在在事务执行中使用。
	 */
	public final Future<Procedure> submit() {
		verify();
		return new ProcedureFuture<Procedure>(this);
	}

	/**
	 * 创建存储过程执行任务，提交到线程池中。
	 * 
	 * @param p 提交执行的存储过程。
	 * @return Future 用于等待存储过程执行完成。
	 * @throws IllegalStateException 如果在在事务执行中使用。
	 */
	public static <P extends Procedure> Future<P> submit(P p) {
		verify();
		return new ProcedureFuture<P>(p);
	}

	/**
	 * 存储过程执行完成回调接口。
	 * 用于异步等待过程执行完成。
	 * 
	 * @param <P> 回调的时候保留存储过程类型
	 */
	public static interface Done<P extends Procedure> {
		public void doDone(P p);
	}

	/**
	 * 创建存储过程执行任务，提交到线程池中。
	 * 
	 * @param p 提交执行的存储过程。 
	 * @param done 回调接口。
	 */
	public static <P extends Procedure> void execute(P p, Done<P> done) {
		new ProcedureFuture<P>(p, done);
	}

	/**
	 * 创建存储过程执行任务，提交到线程池中。
	 * 
	 * @param p 提交执行的存储过程。
	 */
	public static void execute(Procedure p) {
		new ProcedureFuture<Procedure>(p, null);
	}

	/**
	 * 提交到线程池中。
	 */
	public void execute() {
		new ProcedureFuture<Procedure>(this, null);
	}

	/////////////////////////////////////////////////////////////////////////////
	// 内部状态和配置.根据编程需要添加
	private volatile ProcedureConf conf;

	// execute 结果
	private volatile boolean success = false;
	private volatile Throwable exception;

	public Procedure() {
	}

	public Procedure(ProcedureConf conf) {
		this.conf = conf;
	}

	public final ProcedureConf getConf() {
		if (null != conf)
			return conf;
		return Mkdb.getInstance().getConf().getProcedureConf();
	}

	public final synchronized void setConf(ProcedureConf conf) {
		this.conf = conf;
	}

	public final boolean isSuccess() {
		return success;
	}

	public final void setSuccess(boolean success) {
		this.success = success;
	}

	public final Throwable getException() {
		return exception;
	}

	// setup by execute OR Transaction.perform
	public final void setException(Throwable exception) {
		this.exception = exception;
	}


	/**
	\\ - - //  
	(-● ●-)  
	\ (_) /  
	 \ u /  
	┏oOOo-━━━━━━━━┓  
	┃　　　　　　　　　　     ┃  
	┃　　　收集任务！　　     ┃  
	┃      并在事务结束时执行    ┃  
	┃　　　　　　　　　　     ┃  
	┗━━━━━━━━-oOOo┛  
	*/

	/**
	 * 重载 run 并实现任务处理。
	 */
	public abstract static class Task implements Runnable, Log {
		private boolean actived = true;
		private boolean expected = true;

		public final void setActived(boolean actived) {
			this.actived = actived;
		}

		public final void setExpected(boolean expected) {
			this.expected = expected;
		}

		/**
		 * 任务执行入口，暂不开放重载。
		 */
		public final void process() {
			if (this.actived)
				this.run();
		}

		@Override
		public void commit() {
			this.setActived(this.expected);
		}

		@Override
		public void rollback() {
			this.setActived(!this.expected);
		}
	}

	/**
	 * 一般来说,一个存储过程里面不会提交太多任务,这里使用  ArrayList.
	 */
	private static ThreadLocal<ArrayList<Task>> ptasks = new ThreadLocal<ArrayList<Task>>() {
		protected java.util.ArrayList<Task> initialValue() {
			return new ArrayList<Task>();
		}
	};

	private ArrayList<Task> tasks = null;

	private static ThreadLocal<TreeMap<Bean, mkdb.util.BeanPool<?>>> pbeans = new ThreadLocal<TreeMap<Bean, mkdb.util.BeanPool<?>>>() {
		protected TreeMap<Bean, mkdb.util.BeanPool<?>> initialValue() {
			return new TreeMap<Bean, mkdb.util.BeanPool<?>>(BeanComparator.instance);
		}
	};

	static class BeanComparator implements Comparator<Bean> {
		@Override
		public int compare(Bean o1, Bean o2) {
			final long id1 = o1.xdbObjId();
			final long id2 = o2.xdbObjId();
			return id1 > id2 ? 1 : (id1 == id2 ? 0 : -1);
		}
		static BeanComparator instance = new BeanComparator();
	}

	public static void padd(Bean bean, mkdb.util.BeanPool<?> pool) {
		pbeans.get().put(bean, pool);
	}

	/**
	 * 添加任务到列表中，当事务结束时执行，而不管事务执行的结果。
	 */
	public static void ppost(Task task) {
		ptasks.get().add(task);
	}

	/**
	 * 把存储过程执行期间收集到的任务从线程局部存储器中提取出来，并保存到存储过程成员变量中。
	 */
	void fetchTasks() {
		this.tasks = ptasks.get();
		ptasks.remove();
	}

	/**
	 * 返回存储过程最后一次执行后收集到的任务。
	 * 当存储过程没有执行过时，返回null。
	 */
	public ArrayList<Task> getLastTasks() {
		return this.tasks;
	}

	/**
	 * 执行存储过程最后一次执行后收集到的任务。
	 * 任务执行失败时不会中断其他任务的执行。
	 * 任务执行错误记录到 Trace 日志中。
	 */
	public void runLastTasks() {
		if (null != this.tasks)
			for (Task task : this.tasks) {
				try {
					task.process();
				} catch (Throwable e) {
					// 这些任务如果执行失败，仅记录日志。
					// 在这里无法做更多处理。
					mkdb.Trace.error("Procedure.runTasks", e);
				}
			}
	}

	public static void ppostWhileCommit(Task task) {
		task.setExpected(true);
		Transaction.currentSavepoint().add(new LogKey(new XBean(null, null), ""), task);
		ppost(task);
	}

	public static void ppostWhileRollback(Task task) {
		task.setExpected(false);
		Transaction.currentSavepoint().add(new LogKey(new XBean(null, null), ""), task);
		ppost(task);
	}

	/**
	 * {@link gnet.link.Onlines} 
	 * 
	 * 先有鸡还是先有蛋？
	 */
	public static interface IOnlines {
		public boolean sendResponse(mkio.Protocol THIS, mkio.Protocol p2);

		public boolean send(Long roleid, mkio.Protocol p2);

		public boolean send(java.util.Set<Long> roleids, mkio.Protocol p2);

		public void broadcast(mkio.Protocol p2, int timems);
	}

	private static volatile IOnlines onlines = null;

	protected static IOnlines getOlines() {
		return onlines;
	}

	public static void setOlines(IOnlines onlines) {
		Procedure.onlines = onlines;
	}
	
	/**
	\\ - - //  
	(-● ●-)  
	\ (_) /  
	 \ u /  
	┏oOOo-━━━━━━━━━┓  
	┃　　　　　　　　　　          ┃  
	┃      下面方法提交的任务         ┃  
	┃     在存储过程结束时被执行┃  
	┃　　　　　　　　　　          ┃  
	┗━━━━━━━━━-oOOo┛  
	*/
	public static void psend(long roleid, mkio.Protocol p) {
		ppost(new SendToRole(roleid, p));
	}

	public static void psend(long roleid1, long roleid2, mkio.Protocol p) {
		ppost(new SendToRoles(roleid1, roleid2, p));
	}

	public static void psend(java.util.Collection<Long> roleids, mkio.Protocol p) {
		ppost(new SendToRoles(roleids, p));
	}

	public static void psendResponse(mkio.Protocol pFromLink, mkio.Protocol p) {
		ppost(new SendResponse(pFromLink, p));
	}

	public static void pbroadcast(mkio.Protocol p, int timems) {
		ppost(new Broadcast(p, timems));
	}

	public static void psend(mkio.Mkio conn, mkio.Protocol p) {
		ppost(new SendToXio(conn, p));
	}

	public static void pexecute(Procedure p) {
		ppost(new ExecuteProcedure(p));
	}

	public static void pexecute(Runnable r) {
		ppost(new ExecuteRunnable(r));
	}

	/**
	\\ - - //  
	(-● ●-)  
	\ (_) /  
	 \ u /  
	┏oOOo-━━━━━━━━━┓  
	┃　　　　　　　　　　          ┃  
	┃      下面方法提交的任务在    ┃  
	┃     存储过程最终提交时有效┃  
	┃　　　　　　　　　　          ┃  
	┗━━━━━━━━━-oOOo┛  
	*/
	public static void psendWhileCommit(long roleid, mkio.Protocol p) {
		ppostWhileCommit(new SendToRole(roleid, p));
	}

	public static void psendWhileCommit(long roleid1, long roleid2, mkio.Protocol p) {
		ppostWhileCommit(new SendToRoles(roleid1, roleid2, p));
	}

	public static void psendWhileCommit(java.util.Collection<Long> roleids, mkio.Protocol p) {
		ppostWhileCommit(new SendToRoles(roleids, p));
	}

	public static void psendResponseWhileCommit(mkio.Protocol pFromLink, mkio.Protocol p) {
		ppostWhileCommit(new SendResponse(pFromLink, p));
	}

	public static void pbroadcastWhileCommit(mkio.Protocol p, int timems) {
		ppostWhileCommit(new Broadcast(p, timems));
	}

	public static void psendWhileCommit(mkio.Mkio conn, mkio.Protocol p) {
		ppostWhileCommit(new SendToXio(conn, p));
	}

	public static void pexecuteWhileCommit(Procedure p) {
		ppostWhileCommit(new ExecuteProcedure(p));
	}

	public static void pexecuteWhileCommit(Runnable r) {
		ppostWhileCommit(new ExecuteRunnable(r));
	}
	
	/**
	\\ - - //  
	(-● ●-)  
	\ (_) /  
	 \ u /  
	┏oOOo-━━━━━━━━━┓  
	┃　　　　　　　　　　          ┃  
	┃      下面方法提交的任务在    ┃  
	┃     存储过程最终回滚时有效┃  
	┃　　　　　　　　　　          ┃  
	┗━━━━━━━━━-oOOo┛  
	*/
	public static void psendWhileRollback(long roleid, mkio.Protocol p) {
		ppostWhileRollback(new SendToRole(roleid, p));
	}

	public static void psendWhileRollback(long roleid1, long roleid2, mkio.Protocol p) {
		ppostWhileRollback(new SendToRoles(roleid1, roleid2, p));
	}

	public static void psendWhileRollback(java.util.Collection<Long> roleids, mkio.Protocol p) {
		ppostWhileRollback(new SendToRoles(roleids, p));
	}

	public static void psendResponseWhileRollback(mkio.Protocol pFromLink, mkio.Protocol p) {
		ppostWhileRollback(new SendResponse(pFromLink, p));
	}

	public static void pbroadcastWhileRollback(mkio.Protocol p, int timems) {
		ppostWhileRollback(new Broadcast(p, timems));
	}

	public static void psendWhileRollback(mkio.Mkio conn, mkio.Protocol p) {
		ppostWhileRollback(new SendToXio(conn, p));
	}

	public static void pexecuteWhileRollback(Procedure p) {
		ppostWhileRollback(new ExecuteProcedure(p));
	}

	public static void pexecuteWhileRollback(Runnable r) {
		ppostWhileRollback(new ExecuteRunnable(r));
	}

	/**
	\\ - - //  
	(-● ●-)  
	\ (_) /  
	 \ u /  
	┏oOOo-━━━━━━━━━┓  
	┃　　　　　　　　　　          ┃  
	┃     任务实现                  	     ┃  
	┃     					     ┃  
	┃　　　　　　　　　　          ┃  
	┗━━━━━━━━━-oOOo┛  
	*/
	public static class SendToRole extends Task {
		private long roleid;
		private mkio.Protocol p;

		public SendToRole(long roleid, mkio.Protocol p) {
			this.roleid = roleid;
			this.p = p;
		}

		@Override
		public void run() {
			Procedure.getOlines().send(roleid, p);
		}
	}

	public static class SendToRoles extends Task {
		private java.util.HashSet<Long> roleids = new java.util.HashSet<Long>();
		private mkio.Protocol p;

		public SendToRoles(java.util.Collection<Long> roleids, mkio.Protocol p) {
			this.roleids.addAll(roleids);
			this.p = p;
		}

		public SendToRoles(long roleid1, long roleid2, mkio.Protocol p) {
			this.roleids.add(roleid1);
			this.roleids.add(roleid2);
			this.p = p;
		}

		@Override
		public void run() {
			Procedure.getOlines().send(roleids, p);
		}
	}

	public static class SendResponse extends Task {
		private mkio.Protocol pFromLink;
		private mkio.Protocol p;

		public SendResponse(mkio.Protocol pFromLink, mkio.Protocol p) {
			this.pFromLink = pFromLink;
			this.p = p;
		}

		@Override
		public void run() {
			Procedure.getOlines().sendResponse(pFromLink, p);
		}
	}

	public static class Broadcast extends Task {
		private mkio.Protocol p;
		private int timems;

		public Broadcast(mkio.Protocol p) {
			this.p = p;
			this.timems = 0;
		}

		public Broadcast(mkio.Protocol p, int timems) {
			this.p = p;
			this.timems = timems;
		}

		@Override
		public void run() {
			Procedure.getOlines().broadcast(p, timems);
		}
	}

	public static class SendToXio extends Task {
		private mkio.Mkio conn;
		private mkio.Protocol p;

		public SendToXio(mkio.Mkio conn, mkio.Protocol p) {
			this.conn = conn;
			this.p = p;
		}

		@Override
		public void run() {
			this.p.send(conn);
		}
	}

	public static class ExecuteProcedure extends Task {
		private Procedure proc;

		public ExecuteProcedure(Procedure proc) {
			this.proc = proc;
		}

		@Override
		public void run() {
			this.proc.execute();
		}
	}

	public static class ExecuteRunnable extends Task {
		private Runnable command;

		public ExecuteRunnable(Runnable r) {
			this.command = r;
		}

		@Override
		public void run() {
			mkdb.Mkdb.executor().execute(command);
		}
	}
	
}

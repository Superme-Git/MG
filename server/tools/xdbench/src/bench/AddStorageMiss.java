package bench;

/**
 * 一般加入速度。50000 / s
 * 
 * 增加记录的时候必须成功。cache中没有该记录的任何提示信息，db中也没有。
 * 
 * 由于 RemoveStorageMiss 和 AddStorageMiss 在内部都调用 db.exists，所以不单独测试了。
 * GetStorageMiss 和 exists 相比，多了数据编码和传输，也不单独做测试了。。
 * 
 *
 */
public class AddStorageMiss {

	static class Add extends xdb.Procedure {
		@Override
		protected boolean process() throws Exception {
			xtable.Family.insert(xbean.Pod.newFamily());
			return false;
		}
	}
	java.util.concurrent.atomic.AtomicInteger jobs = new java.util.concurrent.atomic.AtomicInteger();
	java.util.concurrent.atomic.AtomicInteger error = new java.util.concurrent.atomic.AtomicInteger();
	xdb.Procedure.Done<Add> done = new xdb.Procedure.Done<Add>() {
		@Override
		public void doDone(Add p) {
			if (p.getException()!=null)
				error.incrementAndGet();
			if (jobs.decrementAndGet() <= 0) {
				synchronized(this) {
					notify();
				}
			}
		}
	};
	void foo() throws Exception {
		xdb.XdbConf xdbConf = new xdb.XdbConf("../xdbench/xdb.xml");
		xdb.Xdb.getInstance().setConf(xdbConf);
		xdb.Xdb.getInstance().start();
		try {
			int count = 100000;
			jobs.set(count);
			xdb.Trace.error("start");
			for (int i = 0; i < count; ++i)
				xdb.Procedure.execute(new Add(), done);
			synchronized (done) {
				done.wait();
			}
			xdb.Trace.error("end jobs=" + jobs + " error=" + error);
		} finally {
			xdb.Xdb.getInstance().stop();
		}
	}
	public static void main(String args[]) throws Exception {
		new AddStorageMiss().foo();
	}
}

package bench;

/**
 * һ������ٶȡ�50000 / s
 * 
 * ���Ӽ�¼��ʱ�����ɹ���cache��û�иü�¼���κ���ʾ��Ϣ��db��Ҳû�С�
 * 
 * ���� RemoveStorageMiss �� AddStorageMiss ���ڲ������� db.exists�����Բ����������ˡ�
 * GetStorageMiss �� exists ��ȣ��������ݱ���ʹ��䣬Ҳ�������������ˡ���
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

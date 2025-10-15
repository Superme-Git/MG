package mkdb;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

/**
 * <p> <b>��־����</b> ����־�����������ʱ����Ϣ���ᱻ��ʾ������Ϣ������ʽ����Ȼ���У���ʱ�������ɺܴ�Ŀ������磺
 * <p> <code>Trace.debug("The large object is " + largeObject.toString());</code>
 * <p> �����largeObject.toString���ܷǳ���ʱ��Ϊ�˱������ֿ�����������ķ�ʽ����¼��־��
 * <p> <code>if (Trace.isDebugEnabled()) Trace.debug("The large object is", largeObject); </code>
 * 
 * <p> ��ʼ��ʱ������������װjava.util.logging��log4j��һֱ��ûʱ��ȥ�о���װ��ʽ�����д��������Ҳ����Ū�ˡ�
 */
public enum Trace {
	DEBUG, INFO, WARN, ERROR, FATAL;

	private volatile static Trace logger = INFO;

	public static final boolean isDebugEnabled() { return logger.ordinal() <= DEBUG.ordinal(); }
	public static final boolean isInfoEnabled()  { return logger.ordinal() <= INFO.ordinal(); }
	public static final boolean isWarnEnabled()  { return logger.ordinal() <= WARN.ordinal(); }
	public static final boolean isErrorEnabled() { return logger.ordinal() <= ERROR.ordinal(); }
	public static final boolean isFatalEnabled() { return logger.ordinal() <= FATAL.ordinal(); }

	public final static void debug(Object message) { logger.trace(DEBUG, null, message); }
	public final static void info(Object message)  { logger.trace(INFO, null, message); }
	public final static void warn(Object message)  { logger.trace(WARN, null, message); }
	public final static void error(Object message) { logger.trace(ERROR, null, message); }
	public final static void fatal(Object message) { logger.trace(FATAL, null, message); }

	public final static void debug(Object message, Throwable e) { logger.trace(DEBUG, e, message); }
	public final static void info(Object message, Throwable e)  { logger.trace(INFO, e, message); }
	public final static void warn(Object message, Throwable e)  { logger.trace(WARN, e, message); }
	public final static void error(Object message, Throwable e) { logger.trace(ERROR, e, message); }
	public final static void fatal(Object message, Throwable e) { logger.trace(FATAL, e, message); }

	public final static void log(Trace level, Object message)              { logger.trace(level, null, message); }
	public final static void log(Trace level, Object message, Throwable e) { logger.trace(level, e, message); }

	public final static void set(Trace trace) {
		logger = trace;
	}

	public final static Trace get() {
		return logger;
	}

	///////////////////////////////////////////////////////////////////////////
	public final static class Log extends java.util.TimerTask {
		private final Lock lock = new ReentrantLock();
		private final File home;
		private boolean console;
		private PrintStream ps;

		public Log(File home, boolean console, int hourOfDay, int minute) {
			this.home = home;
			this.console = console;
			try {
				if (null != home) {
					File file = new File(home, "trace.log");
					// open with append
					ps = new PrintStream(new FileOutputStream(file, true), false, "GBK");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ps = null;
			}

			if (hourOfDay < 0 || minute < 0 || null == ps)
				return; // no rotate

			java.util.Calendar firstTime = java.util.Calendar.getInstance();
			firstTime.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
			firstTime.set(java.util.Calendar.MINUTE, minute);
			firstTime.set(java.util.Calendar.SECOND, 0);
			firstTime.set(java.util.Calendar.MILLISECOND, 0);
			if (firstTime.before(Calendar.getInstance())) // �����һ�ε�ʱ��ȵ�ǰʱ���磬�Ƶ����졣
				firstTime.add(java.util.Calendar.DAY_OF_MONTH, 1); // tomorrow!

			//System.out.println("mkdb.Trace.rotate schedule. firstTime=" + dateFormat.format(firstTime.getTime()));
			//new Exception().printStackTrace(System.out);
			Mkdb.timer().schedule(this, firstTime.getTime(), 24 * 3600 * 1000); // every day
			//System.out.println("TimerTask.executionTime=" + dateFormat.format(scheduledExecutionTime()));
		}

		public boolean rotate() {
			//System.out.println("mkdb.Trace.rotate :");
			//new Exception().printStackTrace(System.out);

			lock.lock();
			try {
				if (null == ps)
					return false;

				File dest = new File(this.home, "trace."
						+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS")
						.format(Calendar.getInstance().getTime()) + ".log");
				if (dest.exists())
					return false;

				File file = new File(this.home, "trace.log");
				ps.close(); // close before rename
				boolean renameOk = file.renameTo(dest);
				// open with truncate if renameOk
				ps = new PrintStream(new FileOutputStream(file, !renameOk), false, "GBK");
				return renameOk;
			} catch (Exception ex) {
				ex.printStackTrace();
				ps = null;
			} finally {
				lock.unlock();
			}
			return false;
		}

		public void println(String str, Throwable e) {
			lock.lock();
			try {
				if (null != ps) {
					ps.println(str);
					if (null != e)
						e.printStackTrace(ps);
				}
				if (this.console) {
					System.out.println(str);
					if (null != e)
						e.printStackTrace(System.out);
				}
			} finally {
				lock.unlock();
			}
		}

		public void close() {
			lock.lock();
			try {
				if (null != ps) {
					ps.close();
					ps = null;
				}
				this.console = false;
				this.cancel();
			} finally {
				lock.unlock();
			}
		}

		@Override
		public void run() {
			this.rotate(); // skip error
		}
	}

	/**
	 * ����־�����ݾɽӿڡ�
	 * @see openNew
	 * @param conf
	 */
	public static void open(MkdbConf conf) {
		openNew(conf.getDbHome(), conf.getTraceTo(),
			conf.getTraceRotateHourOfDay(), conf.getTraceRotateMinute());
	}

	/**
	 * �ڵ�ǰĿ¼��������־�ļ���ͬʱ��ӡ����׼������С�
	 */
	public static Log openNew() {
		return openNew(new File("."), ":file:out", -1, -1);
	}

	/**
	 * �����־ʵ�������򷵻ط������openNew����������һ��ʵ����
	 */
	public static Log openIf() {
		loglock.lock();
		try {
			if (null != log)
				return log;
			return openNew();
		} finally {
			loglock.unlock();
		}
	}

	private static Lock loglock = new ReentrantLock();
	private static Log log = null;

	public static void close() {
		loglock.lock();
		try {
			if (null != log)
				log.close();
			log = null;
		} finally {
			loglock.unlock();
		}
	}

	/**
	 * ����־���رվ���־�����´򿪡�
	 * @param dir       ��־�ļ�����Ŀ¼��null��ʾ����¼���ļ��С�
	 * @param out       ������ļ����߱�׼������У���ʽΪ ":file:out"��
	 * @param hourOfDay ÿ�������־֮Сʱ��-1��������
	 * @param minute    ÿ�������־֮���ӡ�-1��������
	 * @return
	 */
	public static Log openNew(File dir, String out, int hourOfDay, int minute) {
		final File logdir = out.indexOf(":file")  != -1 ? dir : null;
		final boolean console = out.indexOf(":out") != -1;
		loglock.lock();
		try {
			if (null != log)
				log.close();
			log = new Log(logdir, console, hourOfDay, minute);;
			return log;
		} finally {
			loglock.unlock();
		}
	}

	/**
	 * �õ���־ʵ����
	 * @return null if not open
	 */
	public static Log getLog() {
		loglock.lock();
		try {
			return log;
		} finally {
			loglock.unlock();
		}
	}

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private final String traceName(Trace t) {
		String l = t.toString();
		return (l.length() == 4) ? (l + " ") : l;
	}

	private final void trace(Trace t, Throwable e, Object message) {
		Log log = getLog();
		if (null != log && t.ordinal() >= this.ordinal()) {
			StringBuilder sb = new StringBuilder();

			/* time   */ sb.append(dateFormat.format(Calendar.getInstance().getTime()));
			/* level  */ sb.append(' ').append(traceName(t)).append(' ');
			/* thread */ sb.append('<').append(Thread.currentThread().getName()).append('>');
			/* message*/ sb.append(' ').append(message);

			log.println(sb.toString(), e);
		}
	}
}

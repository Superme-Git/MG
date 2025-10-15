package snail.tools;

public class Deadlocked extends Tool {

	@Override
	public void build(Options options) {
		options.add(Jmxc.options());
	}

	@Override
	public void run(Options options) throws Exception {
		Jmxc jmxc = Jmxc.connect(options);
		try {
			long [] threads = jmxc.getProxy().findDeadlockedThreads();
			System.out.println("deadlocked = " + (threads != null && threads.length > 0));
		} finally {
			jmxc.close();
		}
	}

}

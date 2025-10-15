package snail.tools;

public class Killall extends Tool {

	@Override
	public void build(Options options) {
		String [][] opts = {
				{ null, "hostname[:port]", "hostid", null },
		};
		options.add(opts);
	}

	@Override
	public void run(Options options) throws Exception {
		String hostid = null;
		switch (options.getTokens().size() ) {
		case 0: break;
		case 1: hostid = options.getTokens().get(0); break;
		default:
			throw new IllegalArgumentException("too many arguments!");
		}
		for (Integer pid : Ps.ps(hostid)) {
			try {
				Kill.kill(new Jmxc(pid));
			} catch (Throwable x) {
				System.out.println(x);
			}
		}
	}

}

package snail.tools;

import java.util.Arrays;

public class Domains extends Tool {

	@Override
	public void build(Options options) {
		options.add(Jmxc.options());
	}

	@Override
	public void run(Options options) throws Exception {
		Jmxc jmxc = Jmxc.connect(options);
		try {
			String [] domains = jmxc.getProxy().getDomains();
			Arrays.sort(domains);

			System.out.println();
			System.out.println("JMX Domains:");
			for (String domain : domains)
				System.out.println("	" + domain);
		} finally {
			jmxc.close();
		}
	}

}

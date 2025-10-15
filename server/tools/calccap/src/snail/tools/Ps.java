package snail.tools;

import java.util.*;
import java.net.*;
import sun.jvmstat.monitor.*;
import javax.management.*;

public class Ps extends Tool {

	@Override
	public void build(Options options) {
		String[][] _options_ = {
				{ "-mk", null, "check MKDB. ('mk'=is mkdb, '-'=is not mkdb, '?'=check error)", null },
				{ "-q", null, "quiet", null },
				{ "-d", null, "debug", null },
				{ "-m", null, "show main args", null },
				{ "-s", null, "show short name", null },
				{ "-v", null, "show vm args", null },
				{ "-V", null, "show vm flags", null },
				{ null, "hostname[:port]", "HostIdentifier", null },
				};
		options.add(_options_);
	}

	private static String checkMkdb(int lvmid) {
		try {
			Jmxc jmxc = new Jmxc(lvmid);
			try {
				MBeanInfo mkdb = jmxc.getMBeanInfo("mkdb:type=Mkdb");
				return isMkdb(mkdb) ? " mk " : " - ";
			} finally {
				jmxc.close();
			}
		} catch (InstanceNotFoundException x) {
			return " - ";
		} catch (Throwable x) {
			return " ? ";
		}
	}

	public static boolean isMkdb(MBeanInfo beanInfo) {
		return beanInfo.getDescriptor().getFieldValue("interfaceClassName").equals("mkdb.MkdbMBean");
	}

	/**
	 * 根据主机ID列出java应用的进程ID。
	 * @param hostid
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> ps(String hostid) throws Exception {
		HostIdentifier hostId = new HostIdentifier(hostid);
		MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(hostId);
		// get the set active JVMs on the specified host.
		Set<?> jvms = monitoredHost.activeVms();

		List<Integer> pids = new ArrayList<Integer>();
		for (Iterator<?> j = jvms.iterator(); j.hasNext(); /* empty */) {
			pids.add(((Integer) j.next()).intValue());
		}

		return pids;
	}

	static class Info {
		private final int pid;
		private final String mainClassName;
		private String mainArgs;
		private String jvmArgs;
		private String jvmFlags;

		public int getPid() {
			return pid;
		}

		public String getMainClassName() {
			return mainClassName;
		}

		public String getMainArgs() {
			return mainArgs;
		}

		public String getJvmArgs() {
			return jvmArgs;
		}
	
		public String getJvmFlags() {
			return jvmFlags;
		}

		Info(MonitoredHost monitoredHost, int lvmid) throws Exception {
			final VmIdentifier id = new VmIdentifier("//" + lvmid + "?mode=r");
			final MonitoredVm vm = monitoredHost.getMonitoredVm(id, 0);
			try {
				this.pid = lvmid;
				this.mainClassName = MonitoredVmUtil.mainClass(vm, true);
				this.mainArgs = MonitoredVmUtil.mainArgs(vm);
				this.jvmArgs = MonitoredVmUtil.jvmArgs(vm);
				this.jvmFlags = MonitoredVmUtil.jvmFlags(vm);
			} finally {
				monitoredHost.detach(vm);
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			final String lvmidString = String.valueOf(pid);
			sb.append(lvmidString).append(Tool.spaceN(10, lvmidString));
			sb.append(this.mainClassName);
			return sb.toString();
		}
	}

	/**
	 * 根据主机ID列出java应用的进程信息。
	 * @param hostid
	 * @return List<Info>
	 * @throws Exception
	 */
	public static List<Info> psx(String hostid) throws Exception {
		HostIdentifier hostId = new HostIdentifier(hostid);
		MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(hostId);
		// get the set active JVMs on the specified host.
		Set<?> jvms = monitoredHost.activeVms();

		List<Info> infos = new ArrayList<Info>();
		for (Iterator<?> j = jvms.iterator(); j.hasNext(); /* empty */) {
			int lvmid = ((Integer) j.next()).intValue();
			try {
				infos.add(new Info(monitoredHost, lvmid));
			} catch (URISyntaxException e) {
				// unexpected as vmidString is based on a validated hostid
				assert false;
			} catch (Exception e) {
				// skip error;
			}
		}
		return infos;
	}

	@Override
	public void run(Options options) throws Exception {
		boolean isCheckMkdb = options.getValue("-mk") != null;
		boolean isQuiet = options.getValue("-q") != null;
		boolean isShowMainArgs = options.getValue("-m") != null;
		boolean isShowShortName = options.getValue("-s") != null;
		boolean isShowVmArgs = options.getValue("-v") != null;
		boolean isShowVmFlags = options.getValue("-V") != null;
		boolean isDebug = options.getValue("-d") != null;

		// see sun.tools.jps.Jps.java
		try {
			String hostid = null;
			switch (options.getTokens().size() ) {
			case 0: break;
			case 1: hostid = options.getTokens().get(0); break;
			default:
				throw new IllegalArgumentException("too many arguments!");
			}
			HostIdentifier hostId = new HostIdentifier(hostid);
			MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(hostId);

			// get the set active JVMs on the specified host.
			Set<?> jvms = monitoredHost.activeVms();

			for (Iterator<?> j = jvms.iterator(); j.hasNext(); /* empty */) {
				StringBuilder output = new StringBuilder();
				Throwable lastError = null;

				int lvmid = ((Integer) j.next()).intValue();
				String lvmidString = String.valueOf(lvmid);
				output.append(lvmidString);
				if (isCheckMkdb) {
					output.append(Tool.spaceN(10, lvmidString)).append(checkMkdb(lvmid));
				}

				if (isQuiet) {
					System.out.println(output);
					continue;
				}

				MonitoredVm vm = null;
				String vmidString = "//" + lvmid + "?mode=r";

				try {
					VmIdentifier id = new VmIdentifier(vmidString);
					vm = monitoredHost.getMonitoredVm(id, 0);
				} catch (URISyntaxException e) {
					// unexpected as vmidString is based on a validated hostid
					lastError = e;
					assert false;
				} catch (Exception e) {
					lastError = e;
				} finally {
					if (vm == null) {
						/*
						 * we ignore most exceptions, as there are race
						 * conditions where a JVM in 'jvms' may terminate
						 * before we get a chance to list its information.
						 * Other errors, such as access and I/O exceptions
						 * should stop us from iterating over the complete set.
						 */
						output.append(" -- process information unavailable");
						if (isDebug) {
							if ((lastError != null) && (lastError.getMessage() != null)) {
								output.append("\n\t");
								output.append(lastError.getMessage());
							}
						}
						System.out.println(output);
						continue;
					}
				}

				output.append(" ");
				output.append(MonitoredVmUtil.mainClass(vm, !isShowShortName));

				if (isShowMainArgs) {
					String mainArgs = MonitoredVmUtil.mainArgs(vm);
					if (mainArgs != null && mainArgs.length() > 0) {
						output.append(" ").append(mainArgs);
					}
				}
				if (isShowVmArgs) {
					String jvmArgs = MonitoredVmUtil.jvmArgs(vm);
					if (jvmArgs != null && jvmArgs.length() > 0) {
						output.append(" ").append(jvmArgs);
					}
				}
				if (isShowVmFlags) {
					String jvmFlags = MonitoredVmUtil.jvmFlags(vm);
					if (jvmFlags != null && jvmFlags.length() > 0) {
						output.append(" ").append(jvmFlags);
					}
				}

				System.out.println(output);

				monitoredHost.detach(vm);
			}
		} catch (MonitorException e) {
			if (e.getMessage() != null) {
				System.err.println(e.getMessage());
			} else {
				Throwable cause = e.getCause();
				if ((cause != null) && (cause.getMessage() != null)) {
					System.err.println(cause.getMessage());
				} else {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String []args) throws Exception {
		Options options = new Options();
		Ps ps = new Ps();
		ps.build(options);
		options.parse(args);
		System.out.println(options);
		ps.run(options);
		System.out.println(Ps.psx(null));
	}
}

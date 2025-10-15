package fire.pb.gm;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import config.CompileArg;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.effect.Module;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.FireProp;

public class GMInteface implements GMIntefaceMXBean {
	private static Logger logger = Logger.getLogger("GMCOMMAND");

	static List<String> lowLvGMCmd = new ArrayList<String>();
	{
		lowLvGMCmd.add("starttripple");
		lowLvGMCmd.add("stoptripple");
		lowLvGMCmd.add("post");
		lowLvGMCmd.add("startact");
		lowLvGMCmd.add("stopact");
	}
	static Properties prop = ConfigManager.getInstance().getPropConf("sys");

	private static AtomicBoolean isGMOn = new AtomicBoolean(
			(CompileArg.isdebug) ? true : (FireProp.getIntValue(prop,
					"sys.gm.initalOn") == 1));

	public static void setGMOn(boolean isOn) {
		isGMOn.getAndSet(isOn);
	}

	public static boolean getGMOn() {
		return isGMOn.get();
	}

	/**
	 * 判断玩家是否有gm权限
	 * 
	 * @param userid
	 * @param roleid
	 * @return
	 */
	public static boolean gmPriv(int userid) {
		Integer isgm = xtable.Auuserinfo.selectBlisgm(userid);
		if (isgm == null || isgm != 1) {
			return false;
		}
		return true;
	}


	/**
	 * for debug only
	 * 
	 * @param cmd
	 *            要执行的命令
	 */
	@Override
	public int execCommand(String cmd) {
		return execCommand(-1, -1, -1, cmd, false);
	}

	public static int execCommand(int userid, long gmroleid, int localsid,
			String cmd) {
		return execCommand(userid, gmroleid, localsid, cmd, true);
	}

	/**
	 * 这里面不要涉及protocol
	 * 
	 * @param gmroleid
	 * @param cmd
	 * @param needgmuser
	 *            是否需要gm账号才能操作
	 * @return
	 */
	public static int execCommand(int userid, long gmroleid, int localsid,
			String cmd, boolean needgmuser) {
		// TODO:检查权限

		final String[] argv = cmd.split("\\s+");
		if (argv.length == 0)
			return -2;
		if (argv[0].length() < 2)
			return -2;

		argv[0] = argv[0].substring(2);
		if (!argv[0].matches("[a-zA-Z]+"))
			return -2;
		// 检查GM权限 开发阶段先注释掉
		logger.info("准备执行命令\tuid=" + userid + "\troleId=" + gmroleid + "\tcmd=" + cmd);
		if (needgmuser && xtable.Auuserinfo.selectBlisgm(userid) == 2
				&& isLowLevelGMCmd(argv[0])) {

		} else if (needgmuser && !isGMOn.get()) {
			// 这里加一个通过roleid来控制的gm权限
			boolean rolegm = false;
			Properties prop = ConfigManager.getInstance().getPropConf("sys");
			if (prop != null) {
				String value = prop.getProperty("sys.gm.roleids");
				if (value != null) {
					String gms[] = value.split(";");
					if (gms != null) {
						for (String gmstr : gms) {
							if (gmstr != null) {
								if (gmstr.equals(gmroleid + "")) {
									rolegm = true;
									break;
								}
							}
						}
					}
				}
			}
			if (xtable.Auuserinfo.selectBlisgm(userid) != 1 && rolegm == false) {
//				MessageMgr.sendMsgToPop(gmroleid, "GM开关已经关闭，请联系GM开启！！！");
				MessageMgr.sendMsgToPop(gmroleid, "非法输入！！！");
				return -2;
			}
		}

		GMCommand obj;
		try {
			obj = (GMCommand) Class.forName(
					GMInteface.class.getPackage().getName() + ".GM_"
							+ argv[0].toLowerCase()).newInstance();
			obj.setGmroleid(gmroleid);
			obj.setGmUserid(userid);
			obj.setGmLocalsid(localsid);
		} catch (final ClassNotFoundException ex) {
			return execExtCommand(userid, gmroleid, localsid, argv);// 当在正常的包内找不到gm指令时，去ExtJar找
		} catch (final IllegalAccessException ex) {
			logger.error("err", ex);
			return -3;
		} catch (final InstantiationException ex) {
			logger.error("err", ex);
			return -3;
		}

		try {
			String[] parameters = java.util.Arrays.copyOfRange(argv, 1,
					argv.length);

			xbean.Properties proper = xtable.Properties.select(gmroleid);
			String temp = "";
			String rolename = "";
			if (userid > 0) {
				xbean.AUUserInfo userinfo = xtable.Auuserinfo.select(userid);
				if (userinfo != null) {
					temp = userinfo.getUsername();
				}
			}
			if (proper != null) {
				rolename = proper.getRolename();
			}
			int succ = obj.exec(parameters) ? 0 : -4;
			if (succ == 0) {
				logger.info("执行GM指令成功\t" + "userId=" + userid + gmroleid
						+ "\troleId=" +  "\trolename="
						+ rolename + "\t" + "\tusername=" + temp + cmd);
			} else {
				logger.info("执行GM指令失败\t" + "userId=" + userid + gmroleid
						+ "\troleId=" +  "\trolename="
						+ rolename + "\t" + "\tusername=" + temp + cmd);
			}
			return succ;
		} catch (final NumberFormatException ex) {
			logger.error("err", ex);
			obj.sendToGM("参数格式错误：" + obj.usage());
			return -5;
		}
	}

	private static boolean isLowLevelGMCmd(String str) {

		if (lowLvGMCmd.contains(str))
			return true;
		return false;
	}

	private static int execExtCommand(int userid, long gmroleid, int localsid,
			String[] argv) {
		String jarfilename = ConfigManager.getInstance().getPropConf("sys")
				.getProperty("sys.extcommand.filename");
		File jarFile = new File(jarfilename);
		if (!jarFile.exists()) {
			if (gmroleid > 0)
				MessageMgr.sendMsgToPop(gmroleid, argv[0].toLowerCase()
						+ " 不是合法的gm指令。");
			return -3;// 不存在ext command
		}
		Class<?> cls;
		GMCommand obj;
		ExtraCommandClassLoader clzloader = new ExtraCommandClassLoader(".",
				jarfilename);
		try {
			cls = clzloader.loadClass(GMInteface.class.getPackage().getName()
					+ ".GM_" + argv[0].toLowerCase());
			obj = (GMCommand) (cls).newInstance();
			obj.setGmroleid(gmroleid);
			obj.setGmUserid(userid);
			obj.setGmLocalsid(localsid);
		} catch (ClassNotFoundException e) {
			logger.info("不是合法的GM命令\tuid=" + userid + "\troleId=" + gmroleid + "\tcmd=//" + argv[0]);
			if (gmroleid > 0)
				MessageMgr.sendMsgToPop(gmroleid, argv[0].toLowerCase()
						+ " 不是合法的gm指令。");
			return -3;
		} catch (InstantiationException e) {
			logger.error("err", e);
			return -3;
		} catch (IllegalAccessException e) {
			logger.error("err", e);
			return -3;
		}

		try {
			String[] parameters = java.util.Arrays.copyOfRange(argv, 1,
					argv.length);
			Method m = cls.getDeclaredMethod("exec", String[].class);// .getMethod("usage",
																		// new
																		// Class[]{});
			m.setAccessible(true);
			boolean succ = (Boolean) (m
					.invoke(obj, new Object[] { parameters }));
			return succ ? 0 : -4;
		} catch (final NumberFormatException ex) {
			logger.error("err", ex);
			obj.sendToGM("参数格式错误");
			return -5;
		} catch (Exception e) {
			logger.error("err", e);
			obj.sendToGM("其他错误");
			return -5;
		}
	}

	public static void outputattr(long dstroleid)
	{
		fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(dstroleid,true);
		logger.error("=====begin=====");
		logger.error("printFinalAttrs:");
		logger.error("Role id:" + erole.getRoleId()
				+ "\t" + "lv:" + erole.getLevel());
		for (int i = 0; i < Module.fightAttrTypeIds.length; i++)
		{
			int attrId = Module.fightAttrTypeIds[i];
			float value = erole.getAttrById(attrId);
			logger.error("attr id:" + attrId + "\t" + "value:" + value);
			if(value<0)
			{
				if(erole.GetEffect(attrId+1)!=null)
					logger.error("ext attr id:" + (attrId+1) + "\t" + "value:" + erole.GetEffect(attrId+1).floatValue());
				if(erole.GetEffect(attrId+2)!=null)
					logger.error("ext attr id:" + (attrId+2) + "\t" + "value:" + erole.GetEffect(attrId+2).floatValue());
			}
				
		}
		logger.error("printbuffs:");
		
		BuffRoleImpl agent = new BuffRoleImpl(dstroleid,true);		
		for (xbean.Buff buff : agent.getAllBuffBeans().values()) {
			logger.error("BUFF: " + buff.getIndex()
					+ " ; buff Impacttime: " + buff.getImpacttime()
					+ " ; buff Remaintime: " + buff.getRemaintime() + "s. 效果有：");
			for (Map.Entry<Integer, Float> effect : buff
					.getEffects().entrySet()) {
				String name = fire.pb.effect.Module.getInstance().getEffectNameById(effect.getKey());
				logger.error(name + "="+ effect.getValue() + ";");
			}
		}
		logger.error("=====end=====");
	}

}

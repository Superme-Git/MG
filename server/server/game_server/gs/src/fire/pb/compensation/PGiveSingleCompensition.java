package fire.pb.compensation;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mkdb.Lockeys;
import fire.msp.IGetRolesCallBack;
import fire.pb.item.SMailInfo;
import fire.pb.map.RoleManager;

public class PGiveSingleCompensition extends mkdb.Procedure
{
	private final Set<Long> roleIds;
	private final List<xbean.SingleCompensationAward> awards;
	private final String title;
	private final String content;

	private long endTime = 0; // 结束时间
	private int giveType = Helper.GIVETYPE_ROLEID; // 发送类型
	private Map<Integer, List<String>> conds = null;
	
	// 运营相关的变量
	private String opid = ""; // 操作流水号
	private String sign = ""; // 签名

	// 创建的Key
	private long compensekey;
	//private int msgId;

	public PGiveSingleCompensition(Set<Long> roleIds, List<xbean.SingleCompensationAward> awards,
			String title, String content, Map<Integer, List<String>> conds)
	{
		this.roleIds = roleIds;
		this.awards = awards;
		this.title = title;
		this.content = content;
		this.conds = conds;
	}

//	public PGiveSingleCompensition(Set<Long> roleIds, java.util.Map<Integer, Integer> items,
//			String title, String content)
//	{
//		this.roleIds = roleIds;
//		this.items = items;
//		this.title = title;
//		this.content = content;
//		this.awardexp=0;
//		this.awardfushi=0;
//		this.awardgold=0;
//		this.awardmoney=0;
//		this.awardvipexp=0;
//	}

	/**
	 * 创建活动邮件
	 * @param roleIds
	 * @param items
	 * @param title
	 * @param content
	 * @return
	 */
	public static PGiveSingleCompensition createFromActivity(Set<Long> roleIds, List<xbean.SingleCompensationAward> awards,
			String title, String content, long endTime)
	{
		PGiveSingleCompensition proc = new PGiveSingleCompensition(roleIds, awards, title, content, null);
		proc.endTime = endTime;
		return proc;
	}

	/**
	 * 创建运营邮件
	 * @param roleIds 角色id列表
	 * @param items 物品列表
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @param endTime 结束时间
	 * @param opid 操作流水号
	 * @param sign 签名
	 * @return
	 */
	public static PGiveSingleCompensition createFromYunYing(Set<Long> roleIds, List<xbean.SingleCompensationAward> awards,
			String title, String content, long endTime, String opid, String sign)
	{
		PGiveSingleCompensition proc = new PGiveSingleCompensition(roleIds, awards, title, content, null);
		proc.endTime = endTime;
		proc.opid = opid;
		proc.sign = sign;
		return proc;
	}

	/**
	 * 创建运营邮件
	 * @param conds 发送条件
	 * @param items 物品列表
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @param endTime 结束时间
	 * @param opid 操作流水号
	 * @param sign 签名
	 * @return
	 */
	public static PGiveSingleCompensition createFromYunYingCond(Map<Integer, List<String>> conds, List<xbean.SingleCompensationAward> awards, 
			String title, String content, long endTime, String opid, String sign)
	{
		PGiveSingleCompensition proc = new PGiveSingleCompensition(new HashSet<Long>(), awards, title, content, conds);
		proc.endTime = endTime;
		proc.opid = opid;
		proc.sign = sign;
		proc.giveType = Helper.GIVETYPE_CONDITION;
		return proc;
	}

	@Override
	protected boolean process() throws Exception
	{
		xbean.SingleCompensation xcompens = xbean.Pod.newSingleCompensation();
		// 填充内容
		xcompens.setTime(System.currentTimeMillis());
		xcompens.setValidtime(endTime); // =0,默认1个月有效期
		xcompens.setTitle(title);
		xcompens.setContent(content);
		xcompens.setOpid(opid);
		xcompens.setSign(sign);
		for (xbean.SingleCompensationAward award : awards) {
			xbean.SingleCompensationAward newAward = xbean.Pod.newSingleCompensationAward();
			newAward.setType(award.getType());
			newAward.setId(award.getId());
			newAward.setNum(award.getNum());
			newAward.setFlag(award.getFlag());
			xcompens.getAwards().add(newAward);
		}
		// 发送类型
		xcompens.setGivetype(giveType);
		if (giveType == Helper.GIVETYPE_ROLEID) {
			xcompens.getRoleids().addAll(roleIds);
		} else if (giveType == Helper.GIVETYPE_CONDITION) {
			if (conds == null) {
				return false;
			}
			for (Map.Entry<Integer, List<String>> e : conds.entrySet()) {
				xbean.SingleCompensationCond cond = xbean.Pod.newSingleCompensationCond();
				cond.setCondid(e.getKey());
				cond.getCondparams().addAll(e.getValue());
				xcompens.getConditions().add(cond);
			}
		}
		compensekey = xtable.Singlecompensations.insert(xcompens);

		if (giveType == Helper.GIVETYPE_ROLEID) {
			giveOfRoles(xcompens, compensekey, giveType);
		} else if (giveType == Helper.GIVETYPE_CONDITION) {
			giveOfCond(xcompens, compensekey, giveType);
		}
		return true;
	}

	public long getCompensekey()
	{
		return compensekey;
	}

	public void setCompensekey(long compensekey)
	{
		this.compensekey = compensekey;
	}

	/**
	 * 给指定id的人
	 */
	private static void giveOfRoles(xbean.SingleCompensation xcompens, long compensekey, int giveType) {

		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.COMPENSATIONROLES, xcompens.getRoleids()));
		for (long roleId : xcompens.getRoleids()) {
			mkdb.Procedure proc = giveOfRole(roleId, compensekey, giveType, true);
			proc.call();
		}

		// 记录日志
		onLog(xcompens, compensekey, giveType);
	}

	/**
	 * 给指定条件的人
	 */
	private static void giveOfCond(xbean.SingleCompensation xcompens, long compensekey, int giveType) {

		Module.addGiveKeyRecord(compensekey, new HashSet<Long>());

		RoleManager.getRolesByConditions(-1, -1, -1, -1, -1,
				new IGetRolesCallBack() {
					@Override
					public void process(List<Long> roleIds) {
						List<CompensationCondition> conditions = PGiveSingleCompensition.getCondition(compensekey);
						for (Long roleId : roleIds) {
							if (CompensationManager.getInstance().checkCondition(conditions, roleId) == false) {
								// 添加到排除列表
								mkdb.Procedure proc = Helper.roleToExcept(compensekey, roleId);
								mkdb.Procedure.pexecuteWhileCommit(proc);
								continue;
							}
							mkdb.Procedure proc = giveOfRole(roleId, compensekey, giveType, true);
							mkdb.Procedure.pexecuteWhileCommit(proc);
						}
					}
				});

		// 记录日志
		onLog(xcompens, compensekey, giveType);
	}

	/**
	 * 给一个角色
	 */
	public static mkdb.Procedure giveOfRole(long roleId, long compensekey, int giveType, boolean isSendMailInfo) {

		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {

				if (giveType == Helper.GIVETYPE_CONDITION) {
					// 缓存
					Module.addGiveRoleId(compensekey, roleId);
					// 存储
					xbean.SingleCompensation xsingle = xtable.Singlecompensations.get(compensekey);
					if (xsingle != null) {
						xsingle.getRoleidsofget().add(roleId);
					}
				}

				CompensationRole role = CompensationRole.getSingleCompensation(roleId, false);
				if (role == null)
					return false;

				role.giveCompensition(compensekey);

				// 通知客户端
				if (isSendMailInfo) {
					fire.pb.item.MailInfo mailInfo = CompensationToMail.SingleCompensation2Mail(role, compensekey);
					if (mailInfo != null) {
						SMailInfo send = new SMailInfo();
						send.mail = mailInfo;
						mkdb.Procedure.psendWhileCommit(roleId, send);
					}
				}
				return true;
			}
		};

		return proc;
	}

	/**
	 * 获得筛选条件
	 */
	public static List<CompensationCondition> getCondition(long compensekey) {
		List<CompensationCondition> conditions = new LinkedList<CompensationCondition>();
		xbean.SingleCompensation xsingle = xtable.Singlecompensations.select(compensekey);
		if (xsingle != null) {
			for (xbean.SingleCompensationCond cond : xsingle.getConditions()) {
				String[] strings = new String[cond.getCondparams().size() + 1];
				strings[0] = String.valueOf(cond.getCondid());
				for (int i = 1, c = 0; i < strings.length && c < cond.getCondparams().size(); i++, c++) {
					strings[i] = cond.getCondparams().get(c);
				}
				conditions.add(new CompensationCondition(strings));
			}
		}
		return conditions;
	}

	/**
	 * 记录日志
	 */
	public static void onLog(xbean.SingleCompensation xcompens, long compensekey, int givetype) {
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			String itemDetails = "";
			String otherDetails = "";
			for (xbean.SingleCompensationAward award : xcompens.getAwards()) {
				switch (award.getType()) {
				case Helper.AWARDTYPE_ITEM:
					itemDetails += "[" + award.getId() + "," + award.getNum() + "," + award.getFlag() + "],";
					break;
				case Helper.AWARDTYPE_MONEY:
					otherDetails += " MONEY=" + award.getNum();
					break;
				case Helper.AWARDTYPE_GOLD:
					otherDetails += " GOLD=" + award.getNum();
					break;
				case Helper.AWARDTYPE_FUSHI:
					otherDetails += " FUSHI=" + award.getNum();
					break;
				case Helper.AWARDTYPE_EXP:
					otherDetails += " EXP=" + award.getNum();
					break;
				case Helper.AWARDTYPE_VIPEXP:
					otherDetails += " VIPEXP=" + award.getNum();
					break;
				default:
					break;
				}
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strStartTime = dateFormat.format(xcompens.getTime());
			String strEndTime = (xcompens.getValidtime() > 0 ? dateFormat.format(xcompens.getValidtime()) : "0");
			// info
			String strInfo = "[Mail.PGiveSingleCompensition]"
					+ " givetype:" + givetype
					+ " id:" + compensekey
					+ " startTime:" + strStartTime
					+ " endTime:" + strEndTime
					+ " itemDetails:" + itemDetails
					+ " otherDetails:" + otherDetails
					+ " opid:" + xcompens.getOpid()
					+ " sign:" + xcompens.getSign()
					+ " title:" + xcompens.getTitle()
					+ " content:" + xcompens.getContent();
			if (givetype == Helper.GIVETYPE_ROLEID) {
				strInfo += " roleIds:" + Arrays.toString(xcompens.getRoleids().toArray());
			} else if (givetype == Helper.GIVETYPE_CONDITION) {
				String strCond = "";
				for (xbean.SingleCompensationCond cond : xcompens.getConditions()) {
					if (strCond.isEmpty() == false) {
						strCond += ",";
					}
					strCond += cond.getCondid();
					for (String s : cond.getCondparams()) {
						strCond += "|" + s;
					}
				}
				strInfo += " conds:" + strCond;
			}
			Module.logger.info(strInfo);
		}
	}
}

package fire.pb.compensation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import fire.pb.login.SCompensationConfig;
import fire.pb.main.ConfigManager;

/**
 * 补偿转换成邮件
 * @author XGM
 *
 */

public class CompensationToMail {

	/**
	 * GM补偿转换成邮件
	 * @param compensRole
	 * @param compensKey
	 * @return
	 */
	static public fire.pb.item.MailInfo SingleCompensation2Mail(CompensationRole compensRole, long compensKey) {

		if (compensRole.getSinglecompensations().containsKey(compensKey) == false) {
			return null; // 角色身上没有这个补偿
		}
		long readflag = compensRole.getSinglecompensations().get(compensKey);

		xbean.SingleCompensation xsingle = xtable.Singlecompensations.select(compensKey);
		if(xsingle == null) {
			return null; // 补偿系统没有这个补偿
		}

		if (xsingle.getValidtime() > 0) {
			long now = System.currentTimeMillis();
			if (now > xsingle.getValidtime()) {
				return null; // 超时
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strTime = dateFormat.format(xsingle.getTime());

		fire.pb.item.MailInfo mailInfo = new fire.pb.item.MailInfo();
		mailInfo.kind = 1;
		mailInfo.id = compensKey;
		mailInfo.readflag = (byte) readflag;
		mailInfo.time = strTime;
		mailInfo.title = xsingle.getTitle();
		mailInfo.contentid = 0;
		mailInfo.content = xsingle.getContent();
		mailInfo.awardid = 0;
		mailInfo.awardmoney = 0;
		mailInfo.awardgold = 0;
		mailInfo.awardfushi = 0;
		mailInfo.awardexp = 0;
		mailInfo.awardvipexp = 0;
		for (xbean.SingleCompensationAward award : xsingle.getAwards()) {
			switch (award.getType()) {
			case Helper.AWARDTYPE_ITEM:
				mailInfo.items.put((int) award.getId(), (int) award.getNum());
				break;
			case Helper.AWARDTYPE_MONEY:
				mailInfo.awardmoney += award.getNum();
				break;
			case Helper.AWARDTYPE_GOLD:
				mailInfo.awardgold += award.getNum();
				break;
			case Helper.AWARDTYPE_FUSHI:
				mailInfo.awardfushi += award.getNum();
				break;
			case Helper.AWARDTYPE_EXP:
				mailInfo.awardexp += award.getNum();
				break;
			case Helper.AWARDTYPE_VIPEXP:
				mailInfo.awardvipexp += award.getNum();
				break;
			}
		}
		mailInfo.npcid = 0;
		return mailInfo;
	}

	/**
	 * GM补偿转换成邮件
	 * @param compenskey
	 * @return
	 */
	static public fire.pb.item.MailInfo SingleCompensation2Mail(long roleId, long compensKey) {
		// 获取个人补偿信息
		CompensationRole compensRole = CompensationRole.getSingleCompensation(roleId, true);
		// 转换
		return SingleCompensation2Mail(compensRole, compensKey);
	}

	/**
	 * 定时补偿转换成邮件
	 */
	static public fire.pb.item.MailInfo CompensationConfig2Mail(long roleId, int compensationId, final xbean.CompensationInfo cInfo) {

		java.util.NavigableMap<Integer, SCompensationConfig> confs = ConfigManager
				.getInstance().getConf(fire.pb.login.SCompensationConfig.class);

		for (Entry<Integer, SCompensationConfig> entry : confs.entrySet()) {

			SCompensationConfig conf = entry.getValue();
			if (conf.compensationid == compensationId) {

				long curTime = System.currentTimeMillis();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");

				long startTime = 0;
				long endTime = 0;

				try {
					String startTimeStr = conf.startTime;
					Date startDate = sdf.parse(startTimeStr);
					String endTimeStr = conf.endTime;
					Date endDate = sdf.parse(endTimeStr);
					startTime = startDate.getTime();
					endTime = endDate.getTime();
				} catch (ParseException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

				if ((startTime < curTime && curTime < endTime) == false) {
					break;
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String strTime = dateFormat.format(startTime);

				fire.pb.item.MailInfo mailInfo = new fire.pb.item.MailInfo();
				mailInfo.kind = 0;
				mailInfo.id = compensationId;
				mailInfo.readflag = (byte)cInfo.getReadflag();
				mailInfo.time = strTime;
				mailInfo.title = conf.getServiceName();
				mailInfo.contentid = conf.getMsgid();
				mailInfo.awardid = conf.getItemid();
				mailInfo.npcid = 0;
				return mailInfo;
			}
		}

		return null;
	}

	/**
	 * 定时补偿转换成邮件
	 */
	static public fire.pb.item.MailInfo CompensationConfig2Mail(long roleId, int compensationId) {
		xbean.Compensation cTable = xtable.Compensation.select(roleId);
		if (cTable != null) {
			xbean.CompensationInfo cInfo = cTable.getCompensationmap().get(compensationId);
			if (cInfo != null) {
				return CompensationConfig2Mail(roleId, compensationId, cInfo);
			}
		}
		return null;
	}
}

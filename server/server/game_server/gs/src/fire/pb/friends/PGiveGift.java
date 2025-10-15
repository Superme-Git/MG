package fire.pb.friends;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import fire.pb.http.HttpCallBackHandler;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.talk.MessageMgr;
import fire.pb.util.FireProp;
import mkdb.Procedure;

/**
 * 赠送礼物
 * @author XGM
 */
public class PGiveGift extends Procedure {
	public static final Logger logger = Logger.getLogger("FRIEND");
	public static final String address = FireProp.getStringValue("sys", "sys.weibo.address");
	public static final String func = "/bbs/create";
	
	private static class GiveGiftHttpHandler extends HttpCallBackHandler {
		private HttpGet request;
		private final long roleId;
		private final long spaceRoleId; //空间角色id
		private final PGiveGift pgg;
		
		public GiveGiftHttpHandler(long roleId, long spaceRoleId, PGiveGift pgg, HttpGet request) {
			this.request = request;
			this.roleId = roleId;
			this.spaceRoleId = spaceRoleId;
			this.pgg = pgg;
		}
		
		@Override
		protected boolean process(JSONObject json) {
			final String errno = json.getString("errno");
            if(errno.isEmpty() || errno.equals("")) {
            	new mkdb.Procedure() {
    				@Override
    				protected boolean process() {	
    					xbean.RoleSpace rs = xtable.Rolespaces.get(spaceRoleId);
    					if(rs == null)
    						return false;
    					rs.setRecvgift(rs.getRecvgift() + pgg.itemNum);
    					SGiveGift send = new SGiveGift();
    					send.result = 0;
    					psendWhileCommit(roleId, send);
    					return true;
    				}
    			}.submit();
            } else {
            	logger.error("StepSpace.httpReqCallBack errno=" + errno + "!");
            }
			return true;
		};
	}

	private final long roleId;
	private final long targetRoleId; // 赠送目标的角色id
	private final int itemId; // 赠送道具的id
	private byte itemNum; // 赠送道具的数量
	private final String content; // 附加留言(这个是给空间用的)
	private final byte force; // 0:赠送(如果不是双向好友,服务器返回错误,需要二次确认是否强行赠送) 1:强行赠送(非双向好友时赠送)

	public PGiveGift(long roleId, long targetRoleId, int itemId, byte itemNum,
			String content, byte force) {
		this.roleId = roleId;
		this.targetRoleId = targetRoleId;
		this.itemId = itemId;
		this.itemNum = itemNum;
		this.content = content;
		this.force = force;
	}
	
	private int give() {
		FriendGiveGift conf = getFriendGiveConfig(this.itemId);
		if (conf == null) {
			return -1;
		}
		// 查看双方是不是好友
		xbean.FriendGroups selfFriendGroups = xtable.Friends.get(roleId);
		xbean.FriendGroups targetFriendGroups = xtable.Friends.get(targetRoleId);

		// 是否为双向好友
		boolean ret = FriendHelper.isBothwayFriend(selfFriendGroups, roleId, targetFriendGroups, targetRoleId);
		if (ret == false) {
			if (this.force == 0) {
				// 普通赠送
				// 不是双向好友,客户端弹出2次确认框
				SGiveGift send = new SGiveGift();
				send.result = 1;
				psendWhileRollback(roleId, send);
				return -2;
			} else {
				// 强行赠送
				MessageMgr.sendMsgNotify(roleId, 180009, MessageMgr.getStringList(0));
				return 0; // 不是双向好友,赠送不加好友度
			}
		}

		// 加好友度
		Integer selfSex = xtable.Properties.selectSex(roleId);
		Integer targetSex = xtable.Properties.selectSex(targetRoleId);
		if (selfSex == null || targetSex == null) {
			return -21;
		}
		int friendlyDegrees = 0;
		if (selfSex.intValue() != targetSex.intValue()) {
			// 异性增加
			friendlyDegrees = conf.getOppositeSexFriendlyDegrees();
		} else {
			// 同性增加
			friendlyDegrees = conf.getSameSexFriendlyDegrees();
		}
		if (friendlyDegrees > 0) {
			int addValue = friendlyDegrees * this.itemNum;
			int nowValue = FriendHelper.getFriendlyDegrees(selfFriendGroups, targetRoleId);
			if (nowValue >= FriendHelper.getFriendlyDegreesMaxOfGiveGift()) {
				// 超出送花上限时,限制
				addValue = 0;
			}
			int realAddValue1 = FriendHelper.addFriendlyDegrees(selfFriendGroups, roleId, targetRoleId, addValue);
			if (realAddValue1 > 0 && realAddValue1 < addValue) {
				// 好友度即将达上限时,使用最优的道具数量
				int fixItemNum = realAddValue1 / friendlyDegrees;
				fixItemNum += (realAddValue1 % friendlyDegrees != 0 ? 1 : 0);
				this.itemNum = (byte) fixItemNum;
			}
			int realAddValue2 = FriendHelper.addFriendlyDegrees(targetFriendGroups, targetRoleId, roleId, realAddValue1);
			if (realAddValue2 != realAddValue1) {
				Module.logger.warn("[PGiveGift] roleId:" + roleId
						+ " targetRoleId:" + targetRoleId
						+ " realAddValue1:" + realAddValue1
						+ " !="
						+ " realAddValue2:" + realAddValue2);
			}
			// 提示
			MessageMgr.sendMsgNotify(roleId, 180009, MessageMgr.getStringList(realAddValue1));
		} else {
			MessageMgr.sendMsgNotify(roleId, 180014, null);
		}

		// 目标收到赠送的提示
		String roleName = xtable.Properties.selectRolename(roleId);
		String itemName = "";
		fire.pb.item.ItemMgr im = fire.pb.item.Module.getInstance().getItemManager();
		fire.pb.item.ItemShuXing itemAttr = im.getAttr(this.itemId);
		if (itemAttr != null) {
			itemName = itemAttr.getName();
		} else {
			itemName = "no exist item id:" + this.itemId;
		}
		MessageMgr.sendMsgNotify(targetRoleId, 180028, MessageMgr.getStringList(roleName, this.itemNum, itemName));

		// 赠送礼物的成就
		fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.ZENG_SONG);

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PGiveGift] roleId:" + roleId
					+ " targetRoleId:" + targetRoleId
					+ " itemId:" + this.itemId
					+ " itemNum:" + this.itemNum
					+ " itemName:" + itemName);
		}
		return 0;
	}

	@Override
	protected boolean process() throws Exception{
		if (itemId <= 0 || itemNum <= 0)
			return false;
		// 是否能赠送
		if (isCanGiveByItemId(itemId) == false)
			return false;
		// 锁定角色
		List<Long> roleIds = new ArrayList<Long>();
		roleIds.add(roleId);
		roleIds.add(targetRoleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
		// 检查道具数量
		Pack selfBag = new Pack(roleId, false);
		if (selfBag.getBagItemNum(itemId) < itemNum)
			return false;
		// 赠送
		if (give() != 0)
			return false;
		// 删除道具
		int num = selfBag.removeItemById(this.itemId, this.itemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_friendgive, 0, "Friend give gift cost");
		if (num != this.itemNum)
			return false;
		// 通知客户端
		SGiveGift send = new SGiveGift();
		send.result = 0;
		psendWhileCommit(roleId, send);

		//交给空间部分处理
		String serverkey = Gs.serverid;
		String roleid = String.valueOf(roleId);
		String to_roleid = String.valueOf(targetRoleId);
		String getcontent = java.net.URLEncoder.encode(this.content, "utf-8");
		String gift_id = String.valueOf(this.itemId);
		String gift_count = String.valueOf(this.itemNum);
		final String buildurl = String.format(
				"http://%1$s%2$s?serverkey=%3$s&roleid=%4$s&to_roleid=%5$s&content=%6$s&gift_id=%7$s&gift_count=%8$s&is_popularity=0", 
				address, func, serverkey, roleid, to_roleid, getcontent, gift_id, gift_count);
		logger.info("PStepSpace.buildurl:" + buildurl);
		final HttpGet request = new HttpGet(buildurl);
		Gs.getHttpClient().execute(request, new GiveGiftHttpHandler(roleId, targetRoleId, this, request));
		
		//送花排行榜 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PGiveFlowerkRankProc(this.roleId, this.itemId, this.itemNum));
		//收花排行榜 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PReceFlowerkRankProc(this.targetRoleId, this.itemId, this.itemNum));

		return true;
	}

	/**
	 * 通过道具id判断是否能赠送
	 * @param itemId
	 * @return
	 */
	public static boolean isCanGiveByItemId(int itemId) {
		return getFriendGiveConfig(itemId) != null;
	}

	/**
	 * 获得赠送礼物配置
	 * @param itemId
	 * @return
	 */
	public static FriendGiveGift getFriendGiveConfig(int itemId) {
		java.util.Map<Integer, FriendGiveGift> confs = ConfigManager.getInstance().getConf(FriendGiveGift.class);
		return confs.get(itemId);
	}
}

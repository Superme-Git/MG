package fire.pb.friends;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;
import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import fire.pb.http.HttpCallBackHandler;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.util.FireProp;

/**
 * 赠送礼物
 * 
 * @author XGM
 */
public class PXshGiveGift extends Procedure {

	public static final Logger logger = Logger.getLogger("FRIEND");

	public static final String address = FireProp.getStringValue("sys", "sys.weibo.address");

	public static final String func = "/bbs/create";

	private static class GiveGiftHttpHandler extends HttpCallBackHandler {

		private final long spaceRoleId; // 空间角色id

		private final long roleId; // 空间角色id

		private final PXshGiveGift pgg;

		public GiveGiftHttpHandler(long roleId, long spaceRoleId, PXshGiveGift pgg) {
			this.roleId = roleId;
			this.spaceRoleId = spaceRoleId;
			this.pgg = pgg;
		}

		@Override
		protected boolean process(JSONObject json) {
			final String errno = json.getString("errno");
			if (errno.isEmpty() || errno.equals("")) {
				new mkdb.Procedure() {

					@Override
					protected boolean process() {
						xbean.RoleSpace rs = xtable.Rolespaces.get(spaceRoleId);
						if (rs == null)
							return false;
						rs.setRecvgift(rs.getRecvgift() + pgg.itemNum);
						// 通知客户端
						SXshGiveGift send = new SXshGiveGift();
						send.result = 0;
						psendWhileCommit(roleId, send);
						return true;
					}
				}.submit();
			} else {
				logger.error("PXshGiveGift.httpReqCallBack errno=" + errno + "!");
			}
			return true;
		};
	}

	private final long roleId;

	private final long targetRoleId; // 赠送目标的角色id

	private final int itemId; // 赠送道具的id

	private byte itemNum; // 赠送道具的数量

	private final String content; // 附加留言(这个是给空间用的)

	public PXshGiveGift(long roleId, long targetRoleId, int itemId, byte itemNum, String content, byte force) {
		this.roleId = roleId;
		this.targetRoleId = targetRoleId;
		this.itemId = itemId;
		this.itemNum = itemNum;
		this.content = content;
	}

	private int give() {
		XshGiveGift conf = getFriendGiveConfig(this.itemId);
		if (conf == null) {
			return -1;
		}
		// 目标收到赠送的提示
		String itemName = "";
		fire.pb.item.ItemMgr im = fire.pb.item.Module.getInstance().getItemManager();
		fire.pb.item.ItemShuXing itemAttr = im.getAttr(this.itemId);
		if (itemAttr != null) {
			itemName = itemAttr.getName();
		} else {
			itemName = "no exist item id:" + this.itemId;
		}
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PXshGiveGift] roleId:" + roleId + " targetRoleId:" + targetRoleId + " itemId:" + this.itemId
					+ " itemNum:" + this.itemNum + " itemName:" + itemName);
		}
		return 0;
	}

	@Override
	protected boolean process() throws Exception {
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
		int num = selfBag.removeItemById(this.itemId, this.itemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_friendgive, 0,
				"Xsh give gift cost");
		if (num != this.itemNum)
			return false;
		// 交给空间部分处理
		String serverkey = Gs.serverid;
		String roleid = String.valueOf(roleId);
		String to_roleid = String.valueOf(targetRoleId);
		String getcontent = java.net.URLEncoder.encode(this.content, "utf-8");
		String gift_id = String.valueOf(this.itemId);
		String gift_count = String.valueOf(this.itemNum);
		final String buildurl = String
				.format(
						"http://%1$s%2$s?serverkey=%3$s&roleid=%4$s&to_roleid=%5$s&content=%6$s&gift_id=%7$s&gift_count=%8$s&is_popularity=0",
						address, func, serverkey, roleid, to_roleid, getcontent, gift_id, gift_count);
		logger.info("PXshGiveGift.buildurl:" + buildurl);
		final HttpGet request = new HttpGet(buildurl);
		Gs.getHttpClient().execute(request, new GiveGiftHttpHandler(roleId, targetRoleId, this));
		return true;
	}

	/**
	 * 通过道具id判断是否能赠送
	 * 
	 * @param itemId
	 * @return
	 */
	public static boolean isCanGiveByItemId(int itemId) {
		return getFriendGiveConfig(itemId) != null;
	}

	/**
	 * 获得赠送礼物配置
	 * 
	 * @param itemId
	 * @return
	 */
	public static XshGiveGift getFriendGiveConfig(int itemId) {
		java.util.Map<Integer, XshGiveGift> confs = ConfigManager.getInstance().getConf(XshGiveGift.class);
		return confs.get(itemId);
	}
}

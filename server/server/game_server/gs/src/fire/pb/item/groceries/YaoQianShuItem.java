package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.List;

import xbean.Item;
import fire.pb.GsClient;
import fire.pb.game.MoneyType;
import fire.pb.item.Commontext.UseResult;
import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.map.MapConfig;
import fire.pb.map.SceneManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;

public class YaoQianShuItem extends GroceryItem {

	/**
	 * 摇钱树npc id
	 */
	public static int YAOQIANSHUNPC = 300001;
	
	public YaoQianShuItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public YaoQianShuItem(ItemMgr im, Item item) {
		super(im, item);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseYaoQianShuItemHandler();
	}
	
	public static class UseYaoQianShuItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			//发声望
			fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
			long realAdd = pack.addSysCurrency(500,
					MoneyType.MoneyType_ShengWang, "awardID:",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_yaoqianshu, 0);

			if (realAdd > 0) {
				MessageUtil.pSendAddOrRemoveCurrency(roleId, realAdd, MoneyType.MoneyType_ShengWang);
			}
			//获取角色的位置
			final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
			MapConfig conf = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(role.getMapId());
			if (conf == null) {
				return Commontext.UseResult.FAIL;
			}
			
			if (conf.dynamic == 1) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 180027, null);
				return Commontext.UseResult.FAIL;
			}
			
			List<String> param = new ArrayList<String>(4);
			param.add(xtable.Properties.selectRolename(roleId));
			param.add(conf.getMapName());
			param.add(String.valueOf(role.getPos().getX()/16));
			param.add(String.valueOf(role.getPos().getY()/16));
			
			SceneManager.sendAll(MessageMgr.getMsgNotify(180023, 0, param));
			
			//创建一个npc
			StringBuilder sb = new StringBuilder();
			sb.append(xtable.Properties.selectRolename(roleId)).append("的摇钱树");
			
			fire.msp.npc.GCreateYaoQianShuNpc gcreateYaoQian = new fire.msp.npc.GCreateYaoQianShuNpc();
			gcreateYaoQian.roleid = roleId;
			gcreateYaoQian.npcname = sb.toString();
			gcreateYaoQian.npcid = YAOQIANSHUNPC;
			gcreateYaoQian.sceneid = role.getScene();
			gcreateYaoQian.posx = role.getPos().getX();
			gcreateYaoQian.posy = role.getPos().getY();
			
			GsClient.sendToScene(gcreateYaoQian);
			
			/*long npckey = SceneNpcManager.getInstance().getNextId();
			SceneNpcManager.createNpcByGridScale(npckey, YAOQIANSHUNPC, sb.toString(), role.getScene(), 4, 
					role.getPos().getX(), role.getPos().getY(), 6, 1800000, 0);
			
			mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					new mkdb.Procedure() {
						@Override
						protected boolean process() throws Exception {
							//添加数据到表里
							xbean.YaoQianShuInfo yaoqianInfo = xbean.Pod.newYaoQianShuInfo();
							xtable.Yaoqianshutables.insert(npckey, yaoqianInfo);
							
							NPC npc = fire.pb.scene.manager.SceneNpcManager.getInstance().getNpcByKey(npckey);
							if (npc == null)
								return false;
							
							yaoqianInfo.setRoleid(roleId);
							yaoqianInfo.setMapid(role.getMapId());
							yaoqianInfo.setPosx(npc.getPos().getX());
							yaoqianInfo.setPosy(npc.getPos().getY());
							
							xbean.RoleYaoQianShuInfoMaps yaoqianMap = xtable.Roleyaoqianshutables.get(roleId);
							if (yaoqianMap == null) {
								yaoqianMap = xbean.Pod.newRoleYaoQianShuInfoMaps();
								xtable.Roleyaoqianshutables.insert(roleId, yaoqianMap);
							}
							
							xbean.RoleYaoQianShuInfo yqInfo = xbean.Pod.newRoleYaoQianShuInfo();
							yaoqianMap.getYaoqianshumaps().put(npckey, yqInfo);
							
							NPC npc = fire.pb.scene.manager.SceneNpcManager.getInstance().getNpcByKey(npckey);
							if (npc == null)
								return false;
							
							yqInfo.setMapid(role.getMapId());
							yqInfo.setPosx(npc.getPos().getX());
							yqInfo.setPosy(npc.getPos().getY());
							
							return true;
						}
						
					}.submit();
				}
			}, 5, TimeUnit.SECONDS);*/
			
			return Commontext.UseResult.SUCC;
		}
	}

}

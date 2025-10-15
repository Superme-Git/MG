package mylogic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class RemoveAllRoleTable {

	public static void removeAllRoleTable(Set<Long> roleIds){
		try {
			Set<mkdb.Procedure> procss = new HashSet<mkdb.Procedure>();
			
			if (roleIds.isEmpty()) {
				return;
			}
			for (long roleId : roleIds) {
				mkdb.Procedure player = new mkdb.Procedure() {
					@Override
					protected boolean process() {
						String name=xtable.Properties.selectRolename(roleId);
						if(name!=null){
							xtable.Rolename2key.remove(name);//rolename2key	角色名称对应表，key为角色名称（包括曾用名）
						}
						AllItemEquipDel.remove(roleId);//bag背包     //depot仓库    //equip装备背包  //temp	临时背包   //questbag 任务背包     //quality恢复物品属性 //equips装备信息表
						
						xtable.Properties.remove(roleId);//properties	角色基础属性表
						xtable.Helpcount.remove(roleId);//helpcount	援助统计
						xtable.Roleupdatetimetab.remove(roleId);//roleupdatetimetab	角色更新时间表
						
						UniquepetsDel.remove(roleId);//petdepot	宠物仓库  //pet	宠物    //uniquepets	宠物的唯一信息（宠物唯一id和roleid的映射关系）
						
						xtable.Rolepetsellcount.remove(roleId);//rolepetsellcount	宠物每天出售限制
						xtable.Multiexp.remove(roleId);//multiexp	保存角色多倍时间的状态
						
						xtable.Buffrolestodisk.remove(roleId);//buffrolestodisk	附带的Effect
						xtable.Debugflag.remove(roleId);//debugflag	战斗调试表
						xtable.Skillroles.remove(roleId);//skillroles	角色技能
						xtable.Roleid2liveskill.remove(roleId);//roleid2liveskill	每个角色的生活技能信息
						xtable.Roleid2particleskill.remove(roleId);//roleid2particleskill	修炼技能等级
						xtable.Role2impexam.remove(roleId);//role2impexam	智慧试炼答题记录
//						xtable.Winnerscore.remove(roleId);//winnerscore	冠军试炼记录的个人成绩表
						xtable.Vipinfo.remove(roleId);//vipinfo	vip信息
						
						xtable.Friendgivenum.remove(roleId);//friendgivenum	好友每天赠送的数量限制
						xtable.Rolespaces.remove(roleId);//rolespaces	个人空间 
						xtable.Festival.remove(roleId);//festival	节日礼物
						xtable.Offlinemsg.remove(roleId);//offlinemsg	离线消息表
						xtable.Buymedicitemnum.remove(roleId);//buymedicitemnum	药房道具购买数量
						xtable.Runeinfotab.remove(roleId);//runeinfotab	公会符文存储信息表
						xtable.Roleidclan.remove(roleId);//roleidclan	角色id和公会id对应表
						xtable.Roleclanpoint.remove(roleId);//roleclanpoint	公会贡献表
						xtable.Roleapplyclantab.remove(roleId);//roleapplyclantab	角色申请公会
						xtable.Commitedmission.remove(roleId);//commitedmission	提交任务表
						xtable.Commitedscenariomission.remove(roleId);//commitedscenariomission	提交的支线任务表
						xtable.Acceptedscenariomission.remove(roleId);//acceptedscenariomission	支线任务
						xtable.Majorscenariomission.remove(roleId);//majorscenariomission	主线任务
						xtable.Trackedmission.remove(roleId);//trackedmission	接受任务
						xtable.Anyemaxituan.remove(roleId);//anyemaxituan	暗夜马戏团
						xtable.Rolecircletask.remove(roleId);//rolecircletask	角色循环任务
						xtable.Rolerenxingdata.remove(roleId);//rolerenxingdata	角色任性表
						xtable.Rolecircletaskcomplete.remove(roleId);//rolecircletaskcomplete	角色循环任务完成
						xtable.Professionleadervotechallenge.remove(roleId);//professionleadervotechallenge	玩家的投票和挑战信息
						xtable.Roleid2professionleadertickets.remove(roleId);//roleid2professionleadertickets	候选人的得票情况
						xtable.Professionleaderfightdata.remove(roleId);//professionleaderfightdata	候选人的的战斗数据
						xtable.Roletasks.remove(roleId);//roletasks	角色任务信息
						xtable.Roleposes.remove(roleId);//roleposes	玩家进入幻境之前的坐标,出幻境后要回到原地
						xtable.Timingreward.remove(roleId);//timingreward	定时奖励礼包
						xtable.Roleinstancetask.remove(roleId);//roleinstancetask	角色副本任务
						xtable.Activeroletable.remove(roleId);//activeroletable	活跃角色
						xtable.Beginnertip.remove(roleId);//beginnertip	新手引导
						xtable.Rolequitstatistics.remove(roleId);//rolequitstatistics	角色统计表
						xtable.Courses.remove(roleId);//courses	历程表
						xtable.Activitycalendar.remove(roleId);//activitycalendar	活动日历
						xtable.Rolesplayactive.remove(roleId);//rolesplayactive	角色每日活动
						xtable.Rolesplayactiveweek.remove(roleId);//rolesplayactiveweek	角色每周活动
						xtable.Role2instancezone.remove(roleId);//role2instancezone	角色当前在哪个副本玩法中
						xtable.Bingfengroles.remove(roleId);//bingfengroles	 冰封王座副本
						xtable.Triggerroles.remove(roleId);//triggerroles	角色触发
						xtable.Pvp1roles.remove(roleId);//pvp1roles	单人pvp记录
						xtable.Pvp3roles.remove(roleId);//pvp3roles	pvp记录
						xtable.Pvp5roles.remove(roleId);//pvp5roles	pvp记录
						xtable.Modnameitemroles.remove(roleId);//modnameitemroles	记录角色购买改名道具信息
						xtable.Rolerollcard.remove(roleId);//rolerollcard	随机卡信息
						xtable.Rolesafelock.remove(roleId);//rolesafelock	玩家安全锁表
						xtable.Rolemuldayloginreward.remove(roleId);//rolemuldayloginreward	七日登录
						xtable.Roletimernpcinfos.remove(roleId);//roletimernpcinfos	角色定时活动npc信息
						xtable.Roleeventnpcinfos.remove(roleId);//roleeventnpcinfos	定时刷怪活动相关信息
						xtable.Role2weibonotify.remove(roleId);//role2weibonotify	微博相关数据
						xtable.Rolepingbirolesinfos.remove(roleId);//rolepingbirolesinfos	角色黑名单(屏蔽的玩家)列表信息
						xtable.Regtbl.remove(roleId);//regtbl	签到
						xtable.Roledoublecharge.remove(roleId);//roledoublecharge	充值返还多倍
						xtable.Roleid2chargereturnprofit.remove(roleId);//roleid2chargereturnprofit	充值返还利
						xtable.Role2npcawards.remove(roleId);//role2npcawards	NPC发奖励功能
						xtable.Huobancolumns.remove(roleId);//huobancolumns	参战伙伴
						xtable.Huobanzhenrongs.remove(roleId);//huobanzhenrongs	伙伴阵容
						xtable.Rolebuygoodslimits.remove(roleId);//rolebuygoodslimits	角色购买道具记录
						xtable.Rolesalegoodslimits.remove(roleId);//rolesalegoodslimits	角色出售道具记录
						xtable.Market.remove(roleId);//market	摆摊道具容器表
						xtable.Marketpet.remove(roleId);//marketpet	摆摊宠物容器表
						xtable.Rolehook.remove(roleId);//rolehook	挂机
						xtable.Roleuseitemcount.remove(roleId);//roleuseitemcount	角色道具使用记录
						xtable.Rolekaibaoxiang.remove(roleId);//rolekaibaoxiang	角色捡宝箱次数
						xtable.Rolebestow.remove(roleId);//rolebestow	每个宝箱的开启次数
						xtable.Ldvideoroleroseinfolisttab.remove(roleId);//ldvideoroleroseinfolisttab	点赞记录
						xtable.Redpackroleinfolisttab.remove(roleId);//redpackroleinfolisttab	红包
						xtable.Srredpacknumlisttab.remove(roleId);//srredpacknumlisttab	红包
						xtable.Roleredpackrecordtab.remove(roleId);//roleredpackrecordtab	红包
						xtable.Rolereceiveredpackrecordtab.remove(roleId);//rolereceiveredpackrecordtab	红包
						xtable.Roletradinghisrecordlisttab.remove(roleId);//roletradinghisrecordlisttab	点卡交易
						xtable.Roletradinginfolisttab.remove(roleId);//roletradinginfolisttab	点卡交易
						xtable.Accusationinfos.remove(roleId);//accusationinfos	举报相关
						xtable.Roleyaoqianshutables.remove(roleId);//roleyaoqianshutables	摇钱树
						xtable.Roleid2monthcard.remove(roleId);//roleid2monthcard	月卡
						xtable.Rolekaigonghuibaoxiang.remove(roleId);//rolekaigonghuibaoxiang	公会宝箱
						
						xtable.Roleid2worldredpacksendtotal.remove(roleId);//roleid2worldredpacksendtotal	世界红包发送总和
						xtable.Roleid2weekgiverecegift.remove(roleId);//roleid2weekgiverecegift	每周的送收鲜花数据
						xtable.Subscriptions.remove(roleId);//subscriptions	玩家订阅数据
						xtable.Markettradelogtab.remove(roleId);//markettradelogtab	买卖日志表
						
						FriendsDel.remove(procss,roleId);//friends	好友表
						
						return true;
					}
				};
				player.submit().get();
			}
			
			for(mkdb.Procedure pp:procss){
				if(pp!=null){
					pp.submit().get();
				}
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

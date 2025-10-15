			--服务器与客户端通信的相关协议介绍--
			
一、通用格式 
	1、 SExampleProtocol 的协议类型，都是从服务器发往客户端的协议
	2、 CExampleProtocol 的协议类型，都是从客户端发往服务器的协议
	
二、各协议文件说明
	pb.xml 			角色登录流程中，服务器客户端通信的协议，其它各玩法、功能、模块、子系统的协议分别定义在相关的协议文件中。
						具体功能中的协议，如下：
	
由
	attr.xml 			人物宠物属性相关
	battle.xml			战斗协议
	boat.xml			赛船活动相关
	buff.xml			buff相关
	cofc.xml			商会功能
	effect.xml			战斗效果相关的枚举值
	faction.xml			家庭、公会相关的协议
	friends.xml			好友功能相关的协议
	gm.xml				客户端发关的GM功能相关协议
	item.xml			道具相关功能的协议
	master.xml			拜师相关协议	
	talk.xml			聊天功能协议
	npc.xml				NPC 服务相关协议
	pet.xml				宠物相关功能
	pk.xml				角色间PK、切磋相关功能协议
	privatestore.xml	摆摊功能
	product.xml			武器库功能，主要包括装备打造、炼药功能
	race.xml			工会战功能相关协议
	school.xml			职业相关协议
	skill.xml			技能相关
	specialquest.xml	特殊任务相关
	task.xml			任务相关协议
	team.xml			组队功能
	teamcontract.xml	团队协约功能
	title.xml			角色称谓
	trade.xml			交易功能
	fushi.xml			符石交易功能
	move.xml			角色移动相关
	shop.xml 			所有与商店购买有关的都放到这里（以前好多包中涉及到购买）
	hook.xml			挂机相关协议

三、协议书写的要求
	1、严格遵守“通用格式”中的命名要求
	2、不要把逻辑相关的协议写在不同的协议文件里，如果必须，那么请写好备忘
	3、各字段必须有相应的注释描述信息
	4、协议名称、字段名、常量名以及Bean和Bean中的字段的命名必须符合规范
	5、如果逻辑因逻辑复杂导致的 协议发送流程复杂，请相应地用ReadMe或相关的开发文档进行详细描述
	6、每个协议文件中起始和终止的协议type范围必须写在注释中
	7、功能中如果存在较多或较为复杂的协议收发来实现功能时，请提供相应功能时序的文件描述。
	8、尽量用字段标识的方法来减少协议的大量定义，减少功能的复杂程度
	
	
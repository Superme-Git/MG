
package fire.pb.activity.reg;

import java.util.List;

import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/**
 * 
 */
public class RoleProcedure extends Procedure{
	protected long roleId;
	
	protected ConfigManager configManager;
	
	
	public RoleProcedure(long roleId){
		this(roleId,ConfigManager.getInstance());
	}
	
	
	public RoleProcedure(long roleId,ConfigManager configManager){
		this.roleId=roleId;
		this.configManager=configManager;
	}
	
	/**
	 * 发送消息通知
	 * @author cn
	 */
	protected final void sendNotice(int msgId){
		this.sendNotice(msgId,0);
	}
	
	protected final void sendNotice(int msgId,int npcId){
		this.sendNotice(msgId,npcId,null);
	}
	
	/**
	 * 发送带参数的消息
	 * @author cn
	 */
	protected void sendNotice(int msgId,int npcId,List<String> parStrings){
		MessageMgr.sendMsgNotify(roleId, msgId,npcId, parStrings);
	}
	
	protected void psendSystemMessageToRole(int msgId,List<String> parStrings){
		MessageMgr.psendSystemMessageToRole(roleId, msgId,parStrings);//系统邮件发给玩家
	}
	
	
	
	protected void sendMsg(mkio.Protocol protocol){
		gnet.link.Onlines.getInstance().send(roleId,protocol);
	}
	
	
}

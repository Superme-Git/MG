package fire.pb.callback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;
import fire.pb.fushi.PPayOrderResponse;
import fire.pb.main.ConfigManager;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * @author dc
 * 消息分发
 */
public class MessageHandler {
	public static final Logger logger = Logger.getLogger("RECHARGE");
	
	public static void handleMessage(Message revMsg, SocketChannel sc) {
		try {
			revMsg.readHeader();
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("MessageHandler.handleMessage:readHeader异常:" + e);
			e.printStackTrace();
		}
		switch(revMsg.getMessageNum())	{
		case 3:
			doPay(revMsg, sc);
			break;
		default:
			logger.info("MessageHandler.handleMessage:未定义的消息号=" + revMsg.getMessageNum());
			break;
		}
	}
	
	private static void doVersion3Pay(Message revMsg, SocketChannel sc, short iVersion) {
		//TODO:
		logger.warn("MessageHandler.doVersion3Pay:版本3的充值回调不做处理!");
	}
	
	private static void doVersion5Pay(Message revMsg, SocketChannel sc, short iVersion) {
		String strPayData = revMsg.readString();
		String strSign = revMsg.readString();
		logger.info("MessageHandler.doVersion5Pay:iVersion=" + iVersion + "充值回调处理开始!strPayData=" + strPayData + ",strSign=" + strSign);
		String strGameKey = ConfigManager.getGameKey();
		String strPrepare = strGameKey + iVersion + strPayData;
		String strMySign = Encrypter.MD5(strPrepare);
		if(!strMySign.toLowerCase().equals(strSign.toLowerCase())) {
			logger.error("MessageHandler.doVersion5Pay:iVersion=" + iVersion + "充值回调签名效验失败!");
			sendFailure(sc);
		} else {
			logger.info("MessageHandler.doVersion5Pay:iVersion=" + iVersion + "充值回调处理开始!签名效验成功.");
			//{"OrderId":"0406d4941ca84ec8acc3ebd7f0c9be0128787","Channel":"locojoy","AppId":"10000","ConsumeId":"41137979117087590452","PlatformId":"200000001","ProductId":"100001","ProductCount":1,"Money":"10.00","Currency":"CNY","Points":0,"BonusPoints":0,"CpInfo":"MTAxMDB8MjAwOTAwMDAwfDQwMDYyMzU2fDE5Mi4xNjguMC4xfDMzOjY2OjQ0OjU1OjY2Ojc3fA==","CpExtra":"","ServerId":10100,"RoleId":40062356,"OrderTime":1444966580}
			try {
				JSONObject jsonObject = JSONObject.fromObject(strPayData);
				String strOrderId = jsonObject.getString("OrderId");
				String strChannel = jsonObject.getString("Channel");
//				String strAppId = jsonObject.getString("AppId");
//				String strConsumeId = jsonObject.getString("ConsumeId");
				String strPlatformId = jsonObject.getString("PlatformId");
//				String strProductId = jsonObject.getString("ProductId");
//				int iProductCount = jsonObject.getInt("ProductCount");
//				Double dMoney = jsonObject.getDouble("Money");
//				String strCurrency = jsonObject.getString("Currency");
//				int iPoints = jsonObject.getInt("Points");
//				int iBonusPoints = jsonObject.getInt("BonusPoints");
				String strCpInfo = jsonObject.getString("CpInfo");
//				String strCpExtra = jsonObject.getString("CpExtra");
				int iServerId = jsonObject.getInt("ServerId");		
				long lRoleId = jsonObject.getLong("RoleId");
//				int iOrderTime = jsonObject.getInt("OrderTime");
				logger.info("MessageHandler.doVersion5Pay:OrderId=" + strOrderId + ",Channel=" + strChannel + ",PlatformId=" + strPlatformId
						 + ",ServerId=" + iServerId + ",RoleId=" + lRoleId);
				final String decodeCpInfo = ParseUtil.getFromBase64(strCpInfo); 
				if(null != decodeCpInfo){
					logger.info("MessageHandler.doVersion5Pay decodeCpInfo=" + decodeCpInfo);
				} else {
					logger.error("MessageHandler.doVersion5Pay decodeCpInfo=" + decodeCpInfo);
					sendFailure(sc);
					return;
				}
				
				String selfchannel = "";
				String[] cpinfos = decodeCpInfo.split("\\|");
				String cpinfoGamesn = "";
				if(strChannel.equals("1SDK")){
					if(cpinfos.length < 7){
						logger.error("MessageHandler.doVersion5Pay cpinfos=" + cpinfos.toString() + ",length=" + cpinfos.length);
						sendFailure(sc);
						return;
					} else if(cpinfos.length == 7) {
						//String cpinfoServerId = cpinfos[0];
						String cpinfoSelfChannel = cpinfos[1];
						selfchannel = cpinfoSelfChannel;
						String cpinfoRoleId = cpinfos[2];
						//String cpinfoIp = cpinfos[3];
						//String cpinfoDeviceId = cpinfos[4];
						//String cpinfoGameId = cpinfos[5];
						//String cpinfoProductId = cpinfos[6];
						cpinfoGamesn = "-1";
						logger.info("MessageHandler.doVersion5Pay roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn + ",selfchannel=" + selfchannel);
					} else {
						//String cpinfoServerId = cpinfos[0];
						String cpinfoSelfChannel = cpinfos[1];
						selfchannel = cpinfoSelfChannel;
						String cpinfoRoleId = cpinfos[2];
						//String cpinfoIp = cpinfos[3];
						//String cpinfoDeviceId = cpinfos[4];
						//String cpinfoGameId = cpinfos[5];
						//String cpinfoProductId = cpinfos[6];
						cpinfoGamesn = cpinfos[cpinfos.length - 1];
						logger.info("MessageHandler.doVersion5Pay roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn + ",selfchannel=" + selfchannel);
					}
				} else {
					if(cpinfos.length < 5){
						logger.error("MessageHandler.doVersion5Pay cpinfos=" + cpinfos.toString() + ",length=" + cpinfos.length);
						sendFailure(sc);
						return;
					} else if(cpinfos.length == 5){
						//String cpinfoServerId = cpinfos[0];
						String cpinfoSelfChannel = cpinfos[1];
						selfchannel = cpinfoSelfChannel;
						String cpinfoRoleId = cpinfos[2];
						//String cpinfoIp = cpinfos[3];
						//String cpinfoDeviceId = cpinfos[4];
						cpinfoGamesn = "-1";
						logger.info("MessageHandler.doVersion5Pay roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn + ",selfchannel=" + selfchannel);
					} else {
						//String cpinfoServerId = cpinfos[0];
						String cpinfoSelfChannel = cpinfos[1];
						selfchannel = cpinfoSelfChannel;
						String cpinfoRoleId = cpinfos[2];
						//String cpinfoIp = cpinfos[3];
						//String cpinfoDeviceId = cpinfos[4];
						cpinfoGamesn = cpinfos[cpinfos.length - 1];
						logger.info("MessageHandler.doVersion5Pay roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn + ",selfchannel=" + selfchannel);
					}
				}	
				String selfchannel0 = selfchannel.substring(0,1);
				//因为是混服，增加一个安卓和ios的渠道区分，同服但是帐号不互通。
				String account = strPlatformId + "@@" + strChannel + "@" + selfchannel0;
				logger.info("MessageHandler.doVersion5Pay account=" + account);
				new PPayOrderResponse(jsonObject, iVersion, iServerId, lRoleId, cpinfoGamesn).submit();
				sendSuccess(sc, strOrderId);
			} catch (JSONException e) {
				sendFailure(sc);
			}
		}
	}
	
	private static void doPay(Message revMsg, SocketChannel sc) {
		try {
			short iVersion = revMsg.readShort();
			if(iVersion == 3) {
				doVersion3Pay(revMsg, sc, iVersion);
			} else if(iVersion == 5) {
				doVersion5Pay(revMsg, sc, iVersion);
			} else {
				logger.error("MessageHandler.handleMessage:doPay.version=" + iVersion + ",未接入的处理版本号！");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("MessageHandler.handleMessage:doPay.处理异常e=" + e);
			sendFailure(sc);
		}
	}
	
	private static void sendSuccess(SocketChannel sc, String strOrderId) {
		try {
			Message msgOut = new Message((short)2);
			msgOut.setMessageNum((short)4);
			msgOut.writeString(strOrderId);
			msgOut.writeShort((short)1);
			ByteBuffer buf = msgOut.toSend();
			sc.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	private static void sendFailure(SocketChannel sc) {
		try {
			Message msgOut = new Message((short)2);
			msgOut.setMessageNum((short)4);
			msgOut.writeString("");
			msgOut.writeShort((short)2);
			ByteBuffer buf = msgOut.toSend();
			sc.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}

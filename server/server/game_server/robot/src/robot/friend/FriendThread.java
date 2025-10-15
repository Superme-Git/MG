package robot.friend;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import robot.Misc;
import robot.task.FriendZone;

public class FriendThread extends Thread{
	private FriendZone zone;
	
	private int op_num = 0;
	
	private long signature_num = 0L;
	private long status_num = 0L;
  
	private long op_status_mine = 0;
	private long op_status_user = 0;
	
	private static final String HTTP_PREV = "http://192.168.32.2:8803/";
  
	private static final int OP_ROLE_SHOW = 1;
	private static final int OP_UPDATE_SIGNATURE = 2;
	private static final int OP_UPDATE_AVATAR = 3;
	private static final int OP_UPDATE_SOUND = 4;
	private static final int OP_GET_UNREAD = 5;
	private static final int OP_CLEAN_UNREAD = 6;
	private static final int OP_UPDATE_STATUS = 7;
	private static final int OP_STATUS_SHOW = 8;
	private static final int OP_STATUS_MINE = 9;
	private static final int OP_STATUS_USER = 10;
	private static final int OP_STATUS_COMMENT_DEL = 11;
	private static final int OP_STATUS_UNREAD_FETCH = 12;
  
  
	private static final int OP_STATUS_FAVORITE = 20;
	private static final int OP_STATUS_COMMENT_ADD = 21;
	private static final int OP_BBS_CREATE = 22;
	private static final int OP_BBS_ADD_POP = 23;
  
	private int[] op_self = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	private int[] weight_self = { 3, 6, 6, 6, 10, 10, 30, 10, 30, 20, 10, 10}; //151
	private int[] op_user = {OP_STATUS_FAVORITE, OP_STATUS_COMMENT_ADD, OP_BBS_CREATE, OP_BBS_ADD_POP};
	private int[] weight_user = {1, 1, 1, 1};

	public FriendThread(FriendZone zoneNew) {
		zone = zoneNew;
	}

	@Override
	public void run() {
		while (true) {
			if (op_num <= 0) {
				zone.setOpState();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			op_num -= 1;
			if (Math.random() >= 0.3)
				opZoneMine();
			else
				opZoneUser();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void addOpNum() {
		op_num += 1;
	}
	
	private void opZoneMine() {
		int i = Misc.getProbability(weight_self, 151);
		switch (this.op_self[i]) {
		case OP_ROLE_SHOW:
			opRoleShow();
			break;
		case OP_UPDATE_SIGNATURE:
			opUpdateSignature();
			break;
		case OP_UPDATE_AVATAR:
			opUpdateAvatar();
			break;
		case OP_UPDATE_SOUND:
			opUpdateSound();
			break;
		case OP_GET_UNREAD:
			opUnreadGet();
			break;
		case OP_CLEAN_UNREAD:
			opUnreadClean();
			break;
		case OP_UPDATE_STATUS:
			opUpdateStatus();
			break;
		case OP_STATUS_SHOW:
			opStatusShow();
			break;
		case OP_STATUS_MINE:
			opStatusMine();
			break;
		case OP_STATUS_USER:
			opStatusUser();
			break;
		case OP_STATUS_COMMENT_DEL:
			opStatusCommentDel();
			break;
		case OP_STATUS_UNREAD_FETCH:
			opStatusUnreadFetch();
			break;
		default:
			break;
    }
  }
 
  
	private void opRoleShow() {
		String url = HTTP_PREV + "role/show";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		String str2 = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(str2);
	}
  
	private void opUpdateSignature() {
		signature_num += 1;
		String url = HTTP_PREV + "role/update_signature";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		request.put("signature", zone.role.getRoleBase().rolename + String.valueOf(signature_num));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
	}
  
	private void opUpdateAvatar() {
	}
  
	private void opUpdateSound() {
	}
  
	private void opUnreadGet() {
		String url = HTTP_PREV + "role/get_unread";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
  }
  
	private void opUnreadClean() {
		String url = HTTP_PREV + "role/clean_unreadd";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
  }
  
	private void opUpdateStatus() {
		status_num += 1;
		String url = HTTP_PREV + "status/update";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		request.put("content", zone.role.getRoleBase().rolename + String.valueOf(status_num));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
	}
  
	private void opStatusShow() {
		if (op_status_mine == 0)
			return;
	  
		String url = HTTP_PREV + "status/show";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("status_id", String.valueOf(op_status_mine));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
	}
  
	private void opStatusMine() {
		String url = HTTP_PREV + "status/home_timeline";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		request.put("status_id", String.valueOf(0));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
	  
		try {
			JSONObject httpRet = JSONObject.fromObject(ret);
			JSONArray  dataArray = JSONArray.fromObject(httpRet.getString("data"));
	  
			if (dataArray.size() > 0)
				op_status_mine = dataArray.getJSONObject(0).getLong("status_id");
		} catch (Exception e) {
		}
	}
  
	private void opStatusUser() {
		String url = HTTP_PREV + "status/user_timeline";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		request.put("page", String.valueOf(1));
		String ret = FriendHttp.getMethod(url, header, request);
	  
		try {
			JSONObject httpRet = JSONObject.fromObject(ret);
			JSONArray  dataArray = JSONArray.fromObject(httpRet.getString("data"));
	  
			for (int i = 0; i < dataArray.size(); i++) {
				JSONObject dataOne = dataArray.getJSONObject(i);
				long dataRoleid = dataOne.getLong("roleid");
				if (dataRoleid == zone.role.roleId)
					op_status_mine = op_status_mine == 0 ? dataOne.getLong("status_id") : op_status_mine;
					else
						op_status_user = op_status_user == 0 ? dataOne.getLong("status_id") : op_status_user;
			} 
		} catch (Exception e) {
			
		}

  }
  
	private void opStatusCommentDel() {
	}
  
	private void opStatusUnreadFetch() {
		String url = HTTP_PREV + "status/fetch_unread_list";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
	}
  
	private void opZoneUser(){
	    int i = Misc.getProbability(weight_user, 4);
	    switch (op_user[i]) {
	    case OP_STATUS_FAVORITE:
	    	opStatusFavorite();
	    	break;
	    case OP_STATUS_COMMENT_ADD:
	    	opStatusCommentAdd();
	    	break;
	    case OP_BBS_CREATE:
	    	opBBSCreate();
	    	break;
	    case OP_BBS_ADD_POP:
	    	opBBSPopAdd();
	    	break;
	    default:
	    	break;
	    }
  }
  
	private void opStatusFavorite() {
		if (op_status_user == 0)
			return;
	  
		String url = HTTP_PREV + "status/update_favorite";
		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> request = new HashMap<String, String>();
		request.put("serverkey", "0000100001");
		request.put("roleid", String.valueOf(zone.role.roleId));
		request.put("status_id", String.valueOf(op_status_user));
		String ret = FriendHttp.getMethod(url, header, request);
		mkdb.Trace.debug(ret);
	  
  }
  
	private void opStatusCommentAdd() {
		  if (op_status_user == 0)
			  return;
		  
		  String url = HTTP_PREV + "status/update_favorite";
		  Map<String, String> header = new HashMap<String, String>();
		  Map<String, String> request = new HashMap<String, String>();
		  request.put("serverkey", "0000100001");
		  request.put("roleid", String.valueOf(zone.role.roleId));
		  request.put("status_id", String.valueOf(op_status_user));
		  request.put("content", zone.role.getRoleBase().rolename);
		  request.put("reply_id", String.valueOf(0));
		  String ret = FriendHttp.getMethod(url, header, request);
		  mkdb.Trace.debug(ret);
  }
  
	private void opBBSCreate() {
		long friendid = 0;
		  int i = 0;
		  for (long friend_one : zone.role.getFriends().keySet()) {
			  i += 1;
			  if (Math.random() * (i + 1) < 0.3)
				  continue;
			  
			  friendid = friend_one;
			  break;
		  }
		  
		  if (friendid == 0)
			  return;
		  
		  String url = HTTP_PREV + "bbs/create";
		  Map<String, String> header = new HashMap<String, String>();
		  Map<String, String> request = new HashMap<String, String>();
		  request.put("serverkey", "0000100001");
		  request.put("roleid", String.valueOf(zone.role.roleId));
		  request.put("to_roleid", String.valueOf(friendid));
		  request.put("content", zone.role.getRoleBase().rolename);
		  request.put("reply_id", String.valueOf(0));
		  request.put("gift_type", String.valueOf(0));
		  request.put("is_popularity", String.valueOf(0));
		  String ret = FriendHttp.getMethod(url, header, request);
		  mkdb.Trace.debug(ret);
}
  
	private void opBBSPopAdd() {
		 long friendid = 0;
		  int i = 0;
		  for (long friend_one : zone.role.getFriends().keySet()) {
			  i += 1;
			  if (Math.random() * (i + 1) < 0.3)
				  continue;
			  
			  friendid = friend_one;
			  break;
		  }
		  
		  if (friendid == 0)
			  return;
		  
		  String url = HTTP_PREV + "bbs/add_popularity";
		  Map<String, String> header = new HashMap<String, String>();
		  Map<String, String> request = new HashMap<String, String>();
		  request.put("serverkey", "0000100001");
		  request.put("roleid", String.valueOf(zone.role.roleId));
		  request.put("to_roleid", String.valueOf(friendid));
		  String ret = FriendHttp.getMethod(url, header, request);
		  mkdb.Trace.debug(ret);
		  
  }
	
	
}

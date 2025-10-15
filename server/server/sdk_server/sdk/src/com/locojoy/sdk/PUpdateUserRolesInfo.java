package com.locojoy.sdk;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Procedure;

public class PUpdateUserRolesInfo extends Procedure {
	private final mkio.Protocol proto;
	
	public PUpdateUserRolesInfo(openau.DataBetweenAuAnyAndGS proto) {
		this.proto = proto;
	}
	
	@Override
	protected boolean process() throws Exception {
		openau.DataBetweenAuAnyAndGS p = (openau.DataBetweenAuAnyAndGS)proto;
		OctetsStream temp = new OctetsStream(p.info);
		int userid = temp.unmarshal_int();
		String username = temp.unmarshal_String();
		long roleid = temp.unmarshal_long();
		String rolename = temp.unmarshal_String();
		int school = temp.unmarshal_int();
		int shape = temp.unmarshal_int();
		int sex = temp.unmarshal_int();
		int level = temp.unmarshal_int();
		String server = temp.unmarshal_String();

		if(mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug("DataBetweenAuAnyAndGS.info:" + 
			userid + "," + 
			username + "," + 
			roleid + "," + 
			rolename + "," + 
			school + "," + 
			shape + "," + 
			sex + "," + 
			level + "," +
			server + ".");
		//final String params = 
		
		//final String loginurl = String.format("%1$s?params=%2$s", urlstring, params);
		//final String result = HttpIO.getRequest(loginurl, httpconfig);
		/*
		xbean.UserServersInfo usersrvsinfo = xtable.Userserversinfos.get(username);
		if(null != usersrvsinfo) {
			xbean.ServerRolesInfo srvinfo = usersrvsinfo.getUserservers().get(server);
			if(null != srvinfo) {
				xbean.RoleInfo rinfo = srvinfo.getServerroles().get(new Long(roleid));
				if(null != rinfo) {
					//以前存过，更新等级就够了
					rinfo.setLevel(level);
				} else {
					//以前没存过，插入新记录
					final xbean.RoleInfo newrinfo = xbean.Pod.newRoleInfo();
					newrinfo.setServer(server);
					newrinfo.setRoleid(roleid);
					newrinfo.setRolename(rolename);
					newrinfo.setSchool(school);
					newrinfo.setShape(shape);
					newrinfo.setSex(sex);
					newrinfo.setLevel(level);
					srvinfo.getServerroles().put(newrinfo.getRoleid(), newrinfo);
				}
			} else {
				//以前没存过，插入新记录
				final xbean.RoleInfo newrinfo = xbean.Pod.newRoleInfo();
				newrinfo.setServer(server);
				newrinfo.setRoleid(roleid);
				newrinfo.setRolename(rolename);
				newrinfo.setSchool(school);
				newrinfo.setShape(shape);
				newrinfo.setSex(sex);
				newrinfo.setLevel(level);
				final xbean.ServerRolesInfo newsrvinfo = xbean.Pod.newServerRolesInfo();
				newsrvinfo.getServerroles().put(newrinfo.getRoleid(), newrinfo);
				usersrvsinfo.getUserservers().put(server, newsrvinfo);
			}
		} else {
			//以前没存过，插入新记录
			final xbean.RoleInfo newrinfo = xbean.Pod.newRoleInfo();
			newrinfo.setServer(server);
			newrinfo.setRoleid(roleid);
			newrinfo.setRolename(rolename);
			newrinfo.setSchool(school);
			newrinfo.setShape(shape);
			newrinfo.setSex(sex);
			newrinfo.setLevel(level);
			final xbean.ServerRolesInfo newsrvinfo = xbean.Pod.newServerRolesInfo();
			newsrvinfo.getServerroles().put(newrinfo.getRoleid(), newrinfo);
			final xbean.UserServersInfo newusersrvsinfo = xbean.Pod.newUserServersInfo();
			newusersrvsinfo.getUserservers().put(server, newsrvinfo);
			xtable.Userserversinfos.insert(username, newusersrvsinfo);
		}
		*/
		return true;
	}
}

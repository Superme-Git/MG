package xtable;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class _Tables_ extends mkdb.Tables {
	static volatile boolean isExplicitLockCheck = false;

	public static void startExplicitLockCheck() {
		isExplicitLockCheck = true;
	}

	public static _Tables_ getInstance() {
		return (_Tables_)mkdb.Mkdb.getInstance().getTables();
	}

	public _Tables_() {
		add(buffrolestodisk);
		add(debugflag);
		add(bag);
		add(yingyongbaoinfos);
		add(frole);
		add(rolecircletaskcomplete);
		add(userpunish);
		add(roletradinginfolisttab);
		add(bingfenginfos);
		add(totalregisternum);
		add(roleranknotifytime);
		add(gmcreatenpcuniversal);
		add(roleid2battleid);
		add(marketfloatingtab);
		add(rolerecourse);
		add(roleid2monthcard);
		add(rolerankdatalist);
		add(festival);
		add(acceptedscenariomission);
		add(buffroles);
		add(clanid2clanfightids);
		add(quality);
		add(pvp5roles);
		add(clanfighthistroylist);
		add(clanweeknumtab);
		add(rolebuygoodslimits);
		add(roleupdatemodelnumtab);
		add(role2weibonotify);
		add(npcsalebusinesstab);
		add(rolekaigonghuibaoxiang);
		add(petrecyclebin);
		add(activitycalendar);
		add(roleupdatetimetab);
		add(weekindexs);
		add(instanceactivitys);
		add(claninstbestlevel);
		add(buymedicitemnum);
		add(rolespecialquesttimer);
		add(battleflag);
		add(funopenclosetab);
		add(bingfenglists);
		add(archmaps);
		add(professionleader);
		add(pvp1roles);
		add(clanindex);
		add(getrolescallback);
		add(team);
		add(roleschargelimit);
		add(roledoublecharge);
		add(transformbyitem);
		add(auuserinfo);
		add(rolesalegoodslimits);
		add(battle);
		add(crosstemproles);
		add(rolenpcjiangliinfos);
		add(eventnpcinfotable);
		add(apprents);
		add(pvp5historyscorelist);
		add(roleshidelist);
		add(roleyaoqianshutables);
		add(menstor);
		add(commitedscenariomission);
		add(equiptab);
		add(roleofflinehook);
		add(subscriptions);
		add(ldvideoroleinfodeslisttab);
		add(teamfilters);
		add(blackmarketbagtab);
		add(enhancement);
		add(equips);
		add(singlecompensations);
		add(refreshmonsterinfo);
		add(huobancolumns);
		add(petdepot);
		add(roleeventnpcinfos);
		add(medicitemlisttab);
		add(professionleadervotechallenge);
		add(role2npcawards);
		add(marketattentionroletab);
		add(onlinerefresh);
		add(roleid2activityquestion);
		add(role2instancezone);
		add(battlemelonid2melon);
		add(roleid2kickinfo);
		add(bagtimelock);
		add(roleonoffstate);
		add(courses);
		add(requestruneinfotab);
		add(pvp3historyscorelist);
		add(livedieweeknumtab);
		add(ldvideoroleroseinfolisttab);
		add(userid2forbidtime);
		add(gather);
		add(timingreward);
		add(npcfollowroles);
		add(roleid2particleskill);
		add(bingfengwangzuos);
		add(clanfights);
		add(roleprofessionranklist);
		add(chattimetab);
		add(multiexp);
		add(rolehook);
		add(ldvideohislisttab);
		add(roleid2bj);
		add(platorderhistroy);
		add(yybfushi);
		add(lianyunaward);
		add(anyelegendtimer);
		add(petrecover);
		add(watcherid2battleid);
		add(levelseal);
		add(beginnertip);
		add(bingfengroles);
		add(roleid2liveskill);
		add(chargeorder);
		add(regtbl);
		add(missioncameras);
		add(opentreasuremaptable);
		add(requestgoto);
		add(professionleaderfightdata);
		add(rolespaces);
		add(pvp3races);
		add(role2impexam);
		add(payplatform);
		add(instanceturngroups);
		add(huobanvips);
		add(tradingidtab);
		add(rolequitstatistics);
		add(pettab);
		add(roleidclan);
		add(clanzonghelist);
		add(ticketiteminfo);
		add(timerfutures);
		add(fushinum);
		add(compensation);
		add(huobanzhenrongs);
		add(rolesplayactive);
		add(broadcastmsgtab);
		add(compensationroles);
		add(changeschool);
		add(opentraderwheeltable);
		add(rolepetsellcount);
		add(clanfightbattlefield);
		add(role2npcbattle);
		add(pvp5races);
		add(roletradinghisrecordlisttab);
		add(awarditemlimit);
		add(usercompensation);
		add(rolereceiveredpackrecordtab);
		add(roleid2worldredpacksendtotal);
		add(roleyaoqianshuinfos);
		add(yybchargeorder);
		add(instancefuture);
		add(roletaskeventtab);
		add(pet);
		add(gacdpic);
		add(instanceaskinfos);
		add(pvp5scorelist);
		add(bestownpc);
		add(rolemuldayloginreward);
		add(marketpet);
		add(blackmarketpettab);
		add(runeinfotab);
		add(rolezonghelist);
		add(roleid2chargereturnprofit);
		add(redpackinfoviewlisttab);
		add(showingmission);
		add(huobanproperty);
		add(roleid2teamid);
		add(rolenumfornewserver);
		add(rolerecruitaward);
		add(wheelitemlimit);
		add(fdqmazelist);
		add(blackmarkettab);
		add(activitytable);
		add(rolerollcard);
		add(roleid2clanfightid);
		add(anyemaxituan);
		add(marketattentiongoodstab);
		add(rolelevellist);
		add(roleid2battlemelonid);
		add(notify);
		add(normaltab);
		add(roleid2userid);
		add(worldeventtab);
		add(preselltab);
		add(pvp1races);
		add(roleinstancetask);
		add(roletradingrecordstab);
		add(professionleadercandidates);
		add(roleid2professionleadertickets);
		add(targetid2teammatch);
		add(questbag);
		add(claninstances);
		add(yaoqianshutables);
		add(firstchargenew);
		add(triggerroles);
		add(offlinefuture);
		add(clans);
		add(rolerenxingdata);
		add(activeroletable);
		add(roleuseitemcount);
		add(activeusertable);
		add(timernpcinfotable);
		add(npcs);
		add(takebackmarketcontainertab);
		add(depot);
		add(friendgivenum);
		add(majorscenariomission);
		add(rolename2key);
		add(rolekaibaoxiang);
		add(livedieroleinfotab);
		add(itemrecover);
		add(commitedmission);
		add(instancetask);
		add(bianjieteam);
		add(openschoolwheeltable);
		add(clanranklist);
		add(orderidtab);
		add(singleinviting);
		add(roleredpackrecordtab);
		add(redpacklist);
		add(servicesets);
		add(itemrecyclebin);
		add(erole);
		add(markettradelogtab);
		add(clanfightracelist);
		add(userdeviceinfotab);
		add(onetimeeventtab);
		add(rolesplayactiveweek);
		add(rolesfestivaldata);
		add(friends);
		add(market);
		add(chargehistory);
		add(teaminvite);
		add(roleposes);
		add(roletimernpcinfos);
		add(fushirecordtable);
		add(instancezones);
		add(livedie2key);
		add(roleapplyclantab);
		add(roleid2weekgiverecegift);
		add(rolepingbirolesinfos);
		add(redpackroleinfolisttab);
		add(progressbartable);
		add(winnerscore);
		add(randomnametab);
		add(clanprogressranklist);
		add(tablekeynum);
		add(invitepeopleinfo);
		add(rolelockscreen);
		add(skillroles);
		add(roleid2clanfightstatistics);
		add(rolesafelock);
		add(uniquepets);
		add(petscorelist);
		add(rolecircletask);
		add(modnameitemroles);
		add(failedchargeorder);
		add(trackedmission);
		add(roleclanpoint);
		add(offlinemsg);
		add(npcfightnum);
		add(temp);
		add(pvp3roles);
		add(helpcount);
		add(itemrecycleidx);
		add(transfromroles);
		add(impexamstatetbl);
		add(roledaypay);
		add(vipinfo);
		add(npcbattleinfo);
		add(serviceinfos);
		add(receflowerlist);
		add(srredpacknumlisttab);
		add(wheelprogress);
		add(equip);
		add(rolebestow);
		add(giveflowerlist);
		add(accusationinfos);
		add(user);
		add(properties);
		add(roletasks);
		add(appstoretidstatus);
	}

	// visible in package
	mkdb.TTable<Long, xbean.StoredBuffRole> buffrolestodisk = new mkdb.TTable<Long, xbean.StoredBuffRole>() {
		@Override
		public String getName() {
			return "buffrolestodisk";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.StoredBuffRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.StoredBuffRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.StoredBuffRole value = xbean.Pod.newStoredBuffRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.StoredBuffRole newValue() {
			xbean.StoredBuffRole value = xbean.Pod.newStoredBuffRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.DebugFlag> debugflag = new mkdb.TTable<Long, xbean.DebugFlag>() {
		@Override
		public String getName() {
			return "debugflag";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.DebugFlag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.DebugFlag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DebugFlag value = xbean.Pod.newDebugFlag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.DebugFlag newValue() {
			xbean.DebugFlag value = xbean.Pod.newDebugFlag();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> bag = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "bag";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.YingYongBao> yingyongbaoinfos = new mkdb.TTable<Integer, xbean.YingYongBao>() {
		@Override
		public String getName() {
			return "yingyongbaoinfos";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.YingYongBao value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.YingYongBao unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.YingYongBao value = xbean.Pod.newYingYongBao();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.YingYongBao newValue() {
			xbean.YingYongBao value = xbean.Pod.newYingYongBao();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ERole> frole = new mkdb.TTable<Long, xbean.ERole>() {
		@Override
		public String getName() {
			return "frole";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ERole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ERole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ERole value = xbean.Pod.newERole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ERole newValue() {
			xbean.ERole value = xbean.Pod.newERole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CircleTaskCompleteTimes> rolecircletaskcomplete = new mkdb.TTable<Long, xbean.CircleTaskCompleteTimes>() {
		@Override
		public String getName() {
			return "rolecircletaskcomplete";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CircleTaskCompleteTimes value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CircleTaskCompleteTimes unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CircleTaskCompleteTimes value = xbean.Pod.newCircleTaskCompleteTimes();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CircleTaskCompleteTimes newValue() {
			xbean.CircleTaskCompleteTimes value = xbean.Pod.newCircleTaskCompleteTimes();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.UserPunish> userpunish = new mkdb.TTable<Integer, xbean.UserPunish>() {
		@Override
		public String getName() {
			return "userpunish";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.UserPunish value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.UserPunish unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UserPunish value = xbean.Pod.newUserPunish();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.UserPunish newValue() {
			xbean.UserPunish value = xbean.Pod.newUserPunish();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleTradingInfoList> roletradinginfolisttab = new mkdb.TTable<Long, xbean.RoleTradingInfoList>() {
		@Override
		public String getName() {
			return "roletradinginfolisttab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleTradingInfoList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleTradingInfoList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleTradingInfoList value = xbean.Pod.newRoleTradingInfoList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleTradingInfoList newValue() {
			xbean.RoleTradingInfoList value = xbean.Pod.newRoleTradingInfoList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.BingFengInfo> bingfenginfos = new mkdb.TTable<Integer, xbean.BingFengInfo>() {
		@Override
		public String getName() {
			return "bingfenginfos";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BingFengInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.BingFengInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BingFengInfo value = xbean.Pod.newBingFengInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BingFengInfo newValue() {
			xbean.BingFengInfo value = xbean.Pod.newBingFengInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.totalNumber> totalregisternum = new mkdb.TTable<Integer, xbean.totalNumber>() {
		@Override
		public String getName() {
			return "totalregisternum";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.totalNumber value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.totalNumber unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.totalNumber value = xbean.Pod.newtotalNumber();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.totalNumber newValue() {
			xbean.totalNumber value = xbean.Pod.newtotalNumber();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleRankNotifyTimeInfo> roleranknotifytime = new mkdb.TTable<Long, xbean.RoleRankNotifyTimeInfo>() {
		@Override
		public String getName() {
			return "roleranknotifytime";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleRankNotifyTimeInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleRankNotifyTimeInfo value = xbean.Pod.newRoleRankNotifyTimeInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo newValue() {
			xbean.RoleRankNotifyTimeInfo value = xbean.Pod.newRoleRankNotifyTimeInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CreateNpcExecType> gmcreatenpcuniversal = new mkdb.TTable<Long, xbean.CreateNpcExecType>() {
		@Override
		public String getName() {
			return "gmcreatenpcuniversal";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CreateNpcExecType value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CreateNpcExecType unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CreateNpcExecType value = xbean.Pod.newCreateNpcExecType();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CreateNpcExecType newValue() {
			xbean.CreateNpcExecType value = xbean.Pod.newCreateNpcExecType();
			return value;
		}

	};

	mkdb.TTable<Long, Long> roleid2battleid = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "roleid2battleid";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<String, xbean.MarketFloatingGoods> marketfloatingtab = new mkdb.TTable<String, xbean.MarketFloatingGoods>() {
		@Override
		public String getName() {
			return "marketfloatingtab";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MarketFloatingGoods value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.MarketFloatingGoods unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MarketFloatingGoods value = xbean.Pod.newMarketFloatingGoods();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MarketFloatingGoods newValue() {
			xbean.MarketFloatingGoods value = xbean.Pod.newMarketFloatingGoods();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleRecourse> rolerecourse = new mkdb.TTable<Long, xbean.RoleRecourse>() {
		@Override
		public String getName() {
			return "rolerecourse";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleRecourse value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleRecourse unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleRecourse value = xbean.Pod.newRoleRecourse();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleRecourse newValue() {
			xbean.RoleRecourse value = xbean.Pod.newRoleRecourse();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EMonthCard> roleid2monthcard = new mkdb.TTable<Long, xbean.EMonthCard>() {
		@Override
		public String getName() {
			return "roleid2monthcard";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EMonthCard value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EMonthCard unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EMonthCard value = xbean.Pod.newEMonthCard();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EMonthCard newValue() {
			xbean.EMonthCard value = xbean.Pod.newEMonthCard();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RoleRankList> rolerankdatalist = new mkdb.TTable<Integer, xbean.RoleRankList>() {
		@Override
		public String getName() {
			return "rolerankdatalist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RoleRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleRankList value = xbean.Pod.newRoleRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleRankList newValue() {
			xbean.RoleRankList value = xbean.Pod.newRoleRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.FestivalGift> festival = new mkdb.TTable<Long, xbean.FestivalGift>() {
		@Override
		public String getName() {
			return "festival";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.FestivalGift value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.FestivalGift unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.FestivalGift value = xbean.Pod.newFestivalGift();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.FestivalGift newValue() {
			xbean.FestivalGift value = xbean.Pod.newFestivalGift();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.AcceptedScenarioMission> acceptedscenariomission = new mkdb.TTable<Long, xbean.AcceptedScenarioMission>() {
		@Override
		public String getName() {
			return "acceptedscenariomission";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AcceptedScenarioMission value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.AcceptedScenarioMission unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AcceptedScenarioMission value = xbean.Pod.newAcceptedScenarioMission();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AcceptedScenarioMission newValue() {
			xbean.AcceptedScenarioMission value = xbean.Pod.newAcceptedScenarioMission();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BuffRole> buffroles = new mkdb.TTable<Long, xbean.BuffRole>() {
		@Override
		public String getName() {
			return "buffroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BuffRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BuffRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BuffRole value = xbean.Pod.newBuffRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BuffRole newValue() {
			xbean.BuffRole value = xbean.Pod.newBuffRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EClanfightids> clanid2clanfightids = new mkdb.TTable<Long, xbean.EClanfightids>() {
		@Override
		public String getName() {
			return "clanid2clanfightids";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EClanfightids value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EClanfightids unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EClanfightids value = xbean.Pod.newEClanfightids();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EClanfightids newValue() {
			xbean.EClanfightids value = xbean.Pod.newEClanfightids();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RecoveryAttr> quality = new mkdb.TTable<Long, xbean.RecoveryAttr>() {
		@Override
		public String getName() {
			return "quality";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RecoveryAttr value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RecoveryAttr unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RecoveryAttr value = xbean.Pod.newRecoveryAttr();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RecoveryAttr newValue() {
			xbean.RecoveryAttr value = xbean.Pod.newRecoveryAttr();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.PvP5Role> pvp5roles = new mkdb.TTable<Long, xbean.PvP5Role>() {
		@Override
		public String getName() {
			return "pvp5roles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP5Role value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.PvP5Role unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP5Role value = xbean.Pod.newPvP5Role();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP5Role newValue() {
			xbean.PvP5Role value = xbean.Pod.newPvP5Role();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ClanFightHistroyRankList> clanfighthistroylist = new mkdb.TTable<Integer, xbean.ClanFightHistroyRankList>() {
		@Override
		public String getName() {
			return "clanfighthistroylist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanFightHistroyRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ClanFightHistroyRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanFightHistroyRankList value = xbean.Pod.newClanFightHistroyRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanFightHistroyRankList newValue() {
			xbean.ClanFightHistroyRankList value = xbean.Pod.newClanFightHistroyRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ClanWeekNum> clanweeknumtab = new mkdb.TTable<Long, xbean.ClanWeekNum>() {
		@Override
		public String getName() {
			return "clanweeknumtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanWeekNum value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ClanWeekNum unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanWeekNum value = xbean.Pod.newClanWeekNum();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanWeekNum newValue() {
			xbean.ClanWeekNum value = xbean.Pod.newClanWeekNum();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleBuyGoodsLimit> rolebuygoodslimits = new mkdb.TTable<Long, xbean.RoleBuyGoodsLimit>() {
		@Override
		public String getName() {
			return "rolebuygoodslimits";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleBuyGoodsLimit value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleBuyGoodsLimit unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleBuyGoodsLimit value = xbean.Pod.newRoleBuyGoodsLimit();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleBuyGoodsLimit newValue() {
			xbean.RoleBuyGoodsLimit value = xbean.Pod.newRoleBuyGoodsLimit();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleUpdateModelNum> roleupdatemodelnumtab = new mkdb.TTable<Long, xbean.RoleUpdateModelNum>() {
		@Override
		public String getName() {
			return "roleupdatemodelnumtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleUpdateModelNum value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleUpdateModelNum unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleUpdateModelNum value = xbean.Pod.newRoleUpdateModelNum();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleUpdateModelNum newValue() {
			xbean.RoleUpdateModelNum value = xbean.Pod.newRoleUpdateModelNum();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.WeiBoNotify> role2weibonotify = new mkdb.TTable<Long, xbean.WeiBoNotify>() {
		@Override
		public String getName() {
			return "role2weibonotify";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.WeiBoNotify value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.WeiBoNotify unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.WeiBoNotify value = xbean.Pod.newWeiBoNotify();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.WeiBoNotify newValue() {
			xbean.WeiBoNotify value = xbean.Pod.newWeiBoNotify();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.NpcSaleBusiness> npcsalebusinesstab = new mkdb.TTable<Long, xbean.NpcSaleBusiness>() {
		@Override
		public String getName() {
			return "npcsalebusinesstab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NpcSaleBusiness value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.NpcSaleBusiness unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NpcSaleBusiness value = xbean.Pod.newNpcSaleBusiness();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NpcSaleBusiness newValue() {
			xbean.NpcSaleBusiness value = xbean.Pod.newNpcSaleBusiness();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.GongHuiBaoXiangInfo> rolekaigonghuibaoxiang = new mkdb.TTable<Long, xbean.GongHuiBaoXiangInfo>() {
		@Override
		public String getName() {
			return "rolekaigonghuibaoxiang";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.GongHuiBaoXiangInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.GongHuiBaoXiangInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.GongHuiBaoXiangInfo value = xbean.Pod.newGongHuiBaoXiangInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.GongHuiBaoXiangInfo newValue() {
			xbean.GongHuiBaoXiangInfo value = xbean.Pod.newGongHuiBaoXiangInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.DiscardPet> petrecyclebin = new mkdb.TTable<Long, xbean.DiscardPet>() {
		@Override
		public String getName() {
			return "petrecyclebin";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.DiscardPet value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.DiscardPet unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DiscardPet value = xbean.Pod.newDiscardPet();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.DiscardPet newValue() {
			xbean.DiscardPet value = xbean.Pod.newDiscardPet();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ActivityCalendarStatus> activitycalendar = new mkdb.TTable<Long, xbean.ActivityCalendarStatus>() {
		@Override
		public String getName() {
			return "activitycalendar";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ActivityCalendarStatus value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ActivityCalendarStatus unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActivityCalendarStatus value = xbean.Pod.newActivityCalendarStatus();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ActivityCalendarStatus newValue() {
			xbean.ActivityCalendarStatus value = xbean.Pod.newActivityCalendarStatus();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleUpdateTime> roleupdatetimetab = new mkdb.TTable<Long, xbean.RoleUpdateTime>() {
		@Override
		public String getName() {
			return "roleupdatetimetab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleUpdateTime value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleUpdateTime unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleUpdateTime value = xbean.Pod.newRoleUpdateTime();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleUpdateTime newValue() {
			xbean.RoleUpdateTime value = xbean.Pod.newRoleUpdateTime();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.WeekIndex> weekindexs = new mkdb.TTable<Integer, xbean.WeekIndex>() {
		@Override
		public String getName() {
			return "weekindexs";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.WeekIndex value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.WeekIndex unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.WeekIndex value = xbean.Pod.newWeekIndex();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.WeekIndex newValue() {
			xbean.WeekIndex value = xbean.Pod.newWeekIndex();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.InstanceActivity> instanceactivitys = new mkdb.TTable<Integer, xbean.InstanceActivity>() {
		@Override
		public String getName() {
			return "instanceactivitys";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceActivity value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.InstanceActivity unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceActivity value = xbean.Pod.newInstanceActivity();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceActivity newValue() {
			xbean.InstanceActivity value = xbean.Pod.newInstanceActivity();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ClanInstBestLevel> claninstbestlevel = new mkdb.TTable<Integer, xbean.ClanInstBestLevel>() {
		@Override
		public String getName() {
			return "claninstbestlevel";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanInstBestLevel value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ClanInstBestLevel unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanInstBestLevel value = xbean.Pod.newClanInstBestLevel();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanInstBestLevel newValue() {
			xbean.ClanInstBestLevel value = xbean.Pod.newClanInstBestLevel();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BuyMedicItemNum> buymedicitemnum = new mkdb.TTable<Long, xbean.BuyMedicItemNum>() {
		@Override
		public String getName() {
			return "buymedicitemnum";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BuyMedicItemNum value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BuyMedicItemNum unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BuyMedicItemNum value = xbean.Pod.newBuyMedicItemNum();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BuyMedicItemNum newValue() {
			xbean.BuyMedicItemNum value = xbean.Pod.newBuyMedicItemNum();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.SpecailquestFuture> rolespecialquesttimer = new mkdb.TTable<Long, xbean.SpecailquestFuture>() {
		@Override
		public String getName() {
			return "rolespecialquesttimer";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.SpecailquestFuture value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.SpecailquestFuture unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.SpecailquestFuture value = xbean.Pod.newSpecailquestFuture();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.SpecailquestFuture newValue() {
			xbean.SpecailquestFuture value = xbean.Pod.newSpecailquestFuture();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BattleFlags> battleflag = new mkdb.TTable<Long, xbean.BattleFlags>() {
		@Override
		public String getName() {
			return "battleflag";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BattleFlags value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BattleFlags unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BattleFlags value = xbean.Pod.newBattleFlags();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BattleFlags newValue() {
			xbean.BattleFlags value = xbean.Pod.newBattleFlags();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.FunOpenClose> funopenclosetab = new mkdb.TTable<Integer, xbean.FunOpenClose>() {
		@Override
		public String getName() {
			return "funopenclosetab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.FunOpenClose value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.FunOpenClose unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.FunOpenClose value = xbean.Pod.newFunOpenClose();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.FunOpenClose newValue() {
			xbean.FunOpenClose value = xbean.Pod.newFunOpenClose();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.BingFengRank> bingfenglists = new mkdb.TTable<Integer, xbean.BingFengRank>() {
		@Override
		public String getName() {
			return "bingfenglists";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BingFengRank value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.BingFengRank unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BingFengRank value = xbean.Pod.newBingFengRank();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BingFengRank newValue() {
			xbean.BingFengRank value = xbean.Pod.newBingFengRank();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ArchMap> archmaps = new mkdb.TTable<Long, xbean.ArchMap>() {
		@Override
		public String getName() {
			return "archmaps";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ArchMap value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ArchMap unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ArchMap value = xbean.Pod.newArchMap();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ArchMap newValue() {
			xbean.ArchMap value = xbean.Pod.newArchMap();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ProfessionLeaderInfo> professionleader = new mkdb.TTable<Integer, xbean.ProfessionLeaderInfo>() {
		@Override
		public String getName() {
			return "professionleader";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ProfessionLeaderInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ProfessionLeaderInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ProfessionLeaderInfo value = xbean.Pod.newProfessionLeaderInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ProfessionLeaderInfo newValue() {
			xbean.ProfessionLeaderInfo value = xbean.Pod.newProfessionLeaderInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.PvP1Role> pvp1roles = new mkdb.TTable<Long, xbean.PvP1Role>() {
		@Override
		public String getName() {
			return "pvp1roles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP1Role value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.PvP1Role unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP1Role value = xbean.Pod.newPvP1Role();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP1Role newValue() {
			xbean.PvP1Role value = xbean.Pod.newPvP1Role();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ClanIndex> clanindex = new mkdb.TTable<Integer, xbean.ClanIndex>() {
		@Override
		public String getName() {
			return "clanindex";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanIndex value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ClanIndex unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanIndex value = xbean.Pod.newClanIndex();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanIndex newValue() {
			xbean.ClanIndex value = xbean.Pod.newClanIndex();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.GetRolesCallBackInst> getrolescallback = new mkdb.TTable<Long, xbean.GetRolesCallBackInst>() {
		@Override
		public String getName() {
			return "getrolescallback";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.GetRolesCallBackInst value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.GetRolesCallBackInst unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.GetRolesCallBackInst value = xbean.Pod.newGetRolesCallBackInst();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.GetRolesCallBackInst newValue() {
			xbean.GetRolesCallBackInst value = xbean.Pod.newGetRolesCallBackInst();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TeamInfo> team = new mkdb.TTable<Long, xbean.TeamInfo>() {
		@Override
		public String getName() {
			return "team";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TeamInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TeamInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TeamInfo value = xbean.Pod.newTeamInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TeamInfo newValue() {
			xbean.TeamInfo value = xbean.Pod.newTeamInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleChargeLimit> roleschargelimit = new mkdb.TTable<Long, xbean.RoleChargeLimit>() {
		@Override
		public String getName() {
			return "roleschargelimit";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleChargeLimit value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleChargeLimit unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleChargeLimit value = xbean.Pod.newRoleChargeLimit();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleChargeLimit newValue() {
			xbean.RoleChargeLimit value = xbean.Pod.newRoleChargeLimit();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleDoubleChareInfo> roledoublecharge = new mkdb.TTable<Long, xbean.RoleDoubleChareInfo>() {
		@Override
		public String getName() {
			return "roledoublecharge";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleDoubleChareInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleDoubleChareInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleDoubleChareInfo value = xbean.Pod.newRoleDoubleChareInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleDoubleChareInfo newValue() {
			xbean.RoleDoubleChareInfo value = xbean.Pod.newRoleDoubleChareInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TransfromByItemData> transformbyitem = new mkdb.TTable<Long, xbean.TransfromByItemData>() {
		@Override
		public String getName() {
			return "transformbyitem";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TransfromByItemData value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TransfromByItemData unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TransfromByItemData value = xbean.Pod.newTransfromByItemData();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TransfromByItemData newValue() {
			xbean.TransfromByItemData value = xbean.Pod.newTransfromByItemData();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.AUUserInfo> auuserinfo = new mkdb.TTable<Integer, xbean.AUUserInfo>() {
		@Override
		public String getName() {
			return "auuserinfo";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AUUserInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.AUUserInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AUUserInfo value = xbean.Pod.newAUUserInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AUUserInfo newValue() {
			xbean.AUUserInfo value = xbean.Pod.newAUUserInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleSaleGoodsLimit> rolesalegoodslimits = new mkdb.TTable<Long, xbean.RoleSaleGoodsLimit>() {
		@Override
		public String getName() {
			return "rolesalegoodslimits";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleSaleGoodsLimit value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleSaleGoodsLimit unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleSaleGoodsLimit value = xbean.Pod.newRoleSaleGoodsLimit();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleSaleGoodsLimit newValue() {
			xbean.RoleSaleGoodsLimit value = xbean.Pod.newRoleSaleGoodsLimit();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BattleInfo> battle = new mkdb.TTable<Long, xbean.BattleInfo>() {
		@Override
		public String getName() {
			return "battle";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BattleInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BattleInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BattleInfo value = xbean.Pod.newBattleInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BattleInfo newValue() {
			xbean.BattleInfo value = xbean.Pod.newBattleInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CrossTempRole> crosstemproles = new mkdb.TTable<Long, xbean.CrossTempRole>() {
		@Override
		public String getName() {
			return "crosstemproles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CrossTempRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CrossTempRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CrossTempRole value = xbean.Pod.newCrossTempRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CrossTempRole newValue() {
			xbean.CrossTempRole value = xbean.Pod.newCrossTempRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RolenpcjiangliInfo> rolenpcjiangliinfos = new mkdb.TTable<Long, xbean.RolenpcjiangliInfo>() {
		@Override
		public String getName() {
			return "rolenpcjiangliinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RolenpcjiangliInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RolenpcjiangliInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RolenpcjiangliInfo value = xbean.Pod.newRolenpcjiangliInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RolenpcjiangliInfo newValue() {
			xbean.RolenpcjiangliInfo value = xbean.Pod.newRolenpcjiangliInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.eventNpcInfoCol> eventnpcinfotable = new mkdb.TTable<Integer, xbean.eventNpcInfoCol>() {
		@Override
		public String getName() {
			return "eventnpcinfotable";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.eventNpcInfoCol value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.eventNpcInfoCol unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.eventNpcInfoCol value = xbean.Pod.neweventNpcInfoCol();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.eventNpcInfoCol newValue() {
			xbean.eventNpcInfoCol value = xbean.Pod.neweventNpcInfoCol();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.MenstorInfo> apprents = new mkdb.TTable<Long, xbean.MenstorInfo>() {
		@Override
		public String getName() {
			return "apprents";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MenstorInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.MenstorInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MenstorInfo value = xbean.Pod.newMenstorInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MenstorInfo newValue() {
			xbean.MenstorInfo value = xbean.Pod.newMenstorInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PvP5ScoreRankList> pvp5historyscorelist = new mkdb.TTable<Integer, xbean.PvP5ScoreRankList>() {
		@Override
		public String getName() {
			return "pvp5historyscorelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP5ScoreRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PvP5ScoreRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP5ScoreRankList value = xbean.Pod.newPvP5ScoreRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP5ScoreRankList newValue() {
			xbean.PvP5ScoreRankList value = xbean.Pod.newPvP5ScoreRankList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ShiDeZhiRankList> roleshidelist = new mkdb.TTable<Integer, xbean.ShiDeZhiRankList>() {
		@Override
		public String getName() {
			return "roleshidelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ShiDeZhiRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ShiDeZhiRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ShiDeZhiRankList value = xbean.Pod.newShiDeZhiRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ShiDeZhiRankList newValue() {
			xbean.ShiDeZhiRankList value = xbean.Pod.newShiDeZhiRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleYaoQianShuInfoMaps> roleyaoqianshutables = new mkdb.TTable<Long, xbean.RoleYaoQianShuInfoMaps>() {
		@Override
		public String getName() {
			return "roleyaoqianshutables";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleYaoQianShuInfoMaps value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleYaoQianShuInfoMaps value = xbean.Pod.newRoleYaoQianShuInfoMaps();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleYaoQianShuInfoMaps newValue() {
			xbean.RoleYaoQianShuInfoMaps value = xbean.Pod.newRoleYaoQianShuInfoMaps();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.MenstorAndApprent> menstor = new mkdb.TTable<Long, xbean.MenstorAndApprent>() {
		@Override
		public String getName() {
			return "menstor";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MenstorAndApprent value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.MenstorAndApprent unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MenstorAndApprent value = xbean.Pod.newMenstorAndApprent();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MenstorAndApprent newValue() {
			xbean.MenstorAndApprent value = xbean.Pod.newMenstorAndApprent();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CommitedScenarioMission> commitedscenariomission = new mkdb.TTable<Long, xbean.CommitedScenarioMission>() {
		@Override
		public String getName() {
			return "commitedscenariomission";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CommitedScenarioMission value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CommitedScenarioMission unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CommitedScenarioMission value = xbean.Pod.newCommitedScenarioMission();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CommitedScenarioMission newValue() {
			xbean.CommitedScenarioMission value = xbean.Pod.newCommitedScenarioMission();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EquipItem> equiptab = new mkdb.TTable<Long, xbean.EquipItem>() {
		@Override
		public String getName() {
			return "equiptab";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EquipItem value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EquipItem unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EquipItem value = xbean.Pod.newEquipItem();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EquipItem newValue() {
			xbean.EquipItem value = xbean.Pod.newEquipItem();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.OffLineHook> roleofflinehook = new mkdb.TTable<Long, xbean.OffLineHook>() {
		@Override
		public String getName() {
			return "roleofflinehook";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.OffLineHook value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.OffLineHook unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.OffLineHook value = xbean.Pod.newOffLineHook();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.OffLineHook newValue() {
			xbean.OffLineHook value = xbean.Pod.newOffLineHook();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.subscription> subscriptions = new mkdb.TTable<Long, xbean.subscription>() {
		@Override
		public String getName() {
			return "subscriptions";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.subscription value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.subscription unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.subscription value = xbean.Pod.newsubscription();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.subscription newValue() {
			xbean.subscription value = xbean.Pod.newsubscription();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.LDVideoRoleInfoDesList> ldvideoroleinfodeslisttab = new mkdb.TTable<Integer, xbean.LDVideoRoleInfoDesList>() {
		@Override
		public String getName() {
			return "ldvideoroleinfodeslisttab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LDVideoRoleInfoDesList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.LDVideoRoleInfoDesList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LDVideoRoleInfoDesList value = xbean.Pod.newLDVideoRoleInfoDesList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LDVideoRoleInfoDesList newValue() {
			xbean.LDVideoRoleInfoDesList value = xbean.Pod.newLDVideoRoleInfoDesList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TeamFilter> teamfilters = new mkdb.TTable<Long, xbean.TeamFilter>() {
		@Override
		public String getName() {
			return "teamfilters";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TeamFilter value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TeamFilter unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TeamFilter value = xbean.Pod.newTeamFilter();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TeamFilter newValue() {
			xbean.TeamFilter value = xbean.Pod.newTeamFilter();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> blackmarketbagtab = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "blackmarketbagtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EnhancementAttr> enhancement = new mkdb.TTable<Long, xbean.EnhancementAttr>() {
		@Override
		public String getName() {
			return "enhancement";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EnhancementAttr value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EnhancementAttr unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EnhancementAttr value = xbean.Pod.newEnhancementAttr();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EnhancementAttr newValue() {
			xbean.EnhancementAttr value = xbean.Pod.newEnhancementAttr();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Equip> equips = new mkdb.TTable<Long, xbean.Equip>() {
		@Override
		public String getName() {
			return "equips";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Equip value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Equip unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Equip value = xbean.Pod.newEquip();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Equip newValue() {
			xbean.Equip value = xbean.Pod.newEquip();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.SingleCompensation> singlecompensations = new mkdb.TTable<Long, xbean.SingleCompensation>() {
		@Override
		public String getName() {
			return "singlecompensations";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.SingleCompensation value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.SingleCompensation unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.SingleCompensation value = xbean.Pod.newSingleCompensation();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.SingleCompensation newValue() {
			xbean.SingleCompensation value = xbean.Pod.newSingleCompensation();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RefreshMonsterNpcInfo> refreshmonsterinfo = new mkdb.TTable<Integer, xbean.RefreshMonsterNpcInfo>() {
		@Override
		public String getName() {
			return "refreshmonsterinfo";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RefreshMonsterNpcInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RefreshMonsterNpcInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RefreshMonsterNpcInfo value = xbean.Pod.newRefreshMonsterNpcInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RefreshMonsterNpcInfo newValue() {
			xbean.RefreshMonsterNpcInfo value = xbean.Pod.newRefreshMonsterNpcInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.HuoBanColumn> huobancolumns = new mkdb.TTable<Long, xbean.HuoBanColumn>() {
		@Override
		public String getName() {
			return "huobancolumns";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.HuoBanColumn value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.HuoBanColumn unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.HuoBanColumn value = xbean.Pod.newHuoBanColumn();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.HuoBanColumn newValue() {
			xbean.HuoBanColumn value = xbean.Pod.newHuoBanColumn();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Pets> petdepot = new mkdb.TTable<Long, xbean.Pets>() {
		@Override
		public String getName() {
			return "petdepot";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Pets value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Pets unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Pets value = xbean.Pod.newPets();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Pets newValue() {
			xbean.Pets value = xbean.Pod.newPets();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleEventNpcInfoColumn> roleeventnpcinfos = new mkdb.TTable<Long, xbean.RoleEventNpcInfoColumn>() {
		@Override
		public String getName() {
			return "roleeventnpcinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleEventNpcInfoColumn value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleEventNpcInfoColumn unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleEventNpcInfoColumn value = xbean.Pod.newRoleEventNpcInfoColumn();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleEventNpcInfoColumn newValue() {
			xbean.RoleEventNpcInfoColumn value = xbean.Pod.newRoleEventNpcInfoColumn();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.MedicItemList> medicitemlisttab = new mkdb.TTable<Long, xbean.MedicItemList>() {
		@Override
		public String getName() {
			return "medicitemlisttab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MedicItemList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.MedicItemList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MedicItemList value = xbean.Pod.newMedicItemList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MedicItemList newValue() {
			xbean.MedicItemList value = xbean.Pod.newMedicItemList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ProfessionLeaderVoteInfo> professionleadervotechallenge = new mkdb.TTable<Long, xbean.ProfessionLeaderVoteInfo>() {
		@Override
		public String getName() {
			return "professionleadervotechallenge";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ProfessionLeaderVoteInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ProfessionLeaderVoteInfo value = xbean.Pod.newProfessionLeaderVoteInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo newValue() {
			xbean.ProfessionLeaderVoteInfo value = xbean.Pod.newProfessionLeaderVoteInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.npcawardrecords> role2npcawards = new mkdb.TTable<Long, xbean.npcawardrecords>() {
		@Override
		public String getName() {
			return "role2npcawards";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.npcawardrecords value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.npcawardrecords unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.npcawardrecords value = xbean.Pod.newnpcawardrecords();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.npcawardrecords newValue() {
			xbean.npcawardrecords value = xbean.Pod.newnpcawardrecords();
			return value;
		}

	};

	mkdb.TTable<String, xbean.AttentionRole> marketattentionroletab = new mkdb.TTable<String, xbean.AttentionRole>() {
		@Override
		public String getName() {
			return "marketattentionroletab";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AttentionRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.AttentionRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AttentionRole value = xbean.Pod.newAttentionRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AttentionRole newValue() {
			xbean.AttentionRole value = xbean.Pod.newAttentionRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.OnlinerefreshFuture> onlinerefresh = new mkdb.TTable<Long, xbean.OnlinerefreshFuture>() {
		@Override
		public String getName() {
			return "onlinerefresh";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.OnlinerefreshFuture value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.OnlinerefreshFuture unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.OnlinerefreshFuture value = xbean.Pod.newOnlinerefreshFuture();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.OnlinerefreshFuture newValue() {
			xbean.OnlinerefreshFuture value = xbean.Pod.newOnlinerefreshFuture();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ActivityQuestion> roleid2activityquestion = new mkdb.TTable<Long, xbean.ActivityQuestion>() {
		@Override
		public String getName() {
			return "roleid2activityquestion";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ActivityQuestion value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ActivityQuestion unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActivityQuestion value = xbean.Pod.newActivityQuestion();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ActivityQuestion newValue() {
			xbean.ActivityQuestion value = xbean.Pod.newActivityQuestion();
			return value;
		}

	};

	mkdb.TTable<Long, Long> role2instancezone = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "role2instancezone";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ETeamMelon> battlemelonid2melon = new mkdb.TTable<Long, xbean.ETeamMelon>() {
		@Override
		public String getName() {
			return "battlemelonid2melon";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ETeamMelon value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ETeamMelon unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ETeamMelon value = xbean.Pod.newETeamMelon();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ETeamMelon newValue() {
			xbean.ETeamMelon value = xbean.Pod.newETeamMelon();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.KickInfo> roleid2kickinfo = new mkdb.TTable<Long, xbean.KickInfo>() {
		@Override
		public String getName() {
			return "roleid2kickinfo";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.KickInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.KickInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.KickInfo value = xbean.Pod.newKickInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.KickInfo newValue() {
			xbean.KickInfo value = xbean.Pod.newKickInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BagTimeLock> bagtimelock = new mkdb.TTable<Long, xbean.BagTimeLock>() {
		@Override
		public String getName() {
			return "bagtimelock";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BagTimeLock value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BagTimeLock unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BagTimeLock value = xbean.Pod.newBagTimeLock();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BagTimeLock newValue() {
			xbean.BagTimeLock value = xbean.Pod.newBagTimeLock();
			return value;
		}

	};

	mkdb.TTable<Long, Integer> roleonoffstate = new mkdb.TTable<Long, Integer>() {
		@Override
		public String getName() {
			return "roleonoffstate";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.lichengxinxi> courses = new mkdb.TTable<Long, xbean.lichengxinxi>() {
		@Override
		public String getName() {
			return "courses";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.lichengxinxi value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.lichengxinxi unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.lichengxinxi value = xbean.Pod.newlichengxinxi();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.lichengxinxi newValue() {
			xbean.lichengxinxi value = xbean.Pod.newlichengxinxi();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RequestRuneInfoList> requestruneinfotab = new mkdb.TTable<Long, xbean.RequestRuneInfoList>() {
		@Override
		public String getName() {
			return "requestruneinfotab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RequestRuneInfoList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RequestRuneInfoList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RequestRuneInfoList value = xbean.Pod.newRequestRuneInfoList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RequestRuneInfoList newValue() {
			xbean.RequestRuneInfoList value = xbean.Pod.newRequestRuneInfoList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PvP3ScoreRankList> pvp3historyscorelist = new mkdb.TTable<Integer, xbean.PvP3ScoreRankList>() {
		@Override
		public String getName() {
			return "pvp3historyscorelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP3ScoreRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PvP3ScoreRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP3ScoreRankList value = xbean.Pod.newPvP3ScoreRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP3ScoreRankList newValue() {
			xbean.PvP3ScoreRankList value = xbean.Pod.newPvP3ScoreRankList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.LiveDieWeekNum> livedieweeknumtab = new mkdb.TTable<Integer, xbean.LiveDieWeekNum>() {
		@Override
		public String getName() {
			return "livedieweeknumtab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LiveDieWeekNum value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.LiveDieWeekNum unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LiveDieWeekNum value = xbean.Pod.newLiveDieWeekNum();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LiveDieWeekNum newValue() {
			xbean.LiveDieWeekNum value = xbean.Pod.newLiveDieWeekNum();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.LDVideoRoleRoseInfoList> ldvideoroleroseinfolisttab = new mkdb.TTable<Long, xbean.LDVideoRoleRoseInfoList>() {
		@Override
		public String getName() {
			return "ldvideoroleroseinfolisttab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LDVideoRoleRoseInfoList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LDVideoRoleRoseInfoList value = xbean.Pod.newLDVideoRoleRoseInfoList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList newValue() {
			xbean.LDVideoRoleRoseInfoList value = xbean.Pod.newLDVideoRoleRoseInfoList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.forbidinfo> userid2forbidtime = new mkdb.TTable<Integer, xbean.forbidinfo>() {
		@Override
		public String getName() {
			return "userid2forbidtime";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.forbidinfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.forbidinfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.forbidinfo value = xbean.Pod.newforbidinfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.forbidinfo newValue() {
			xbean.forbidinfo value = xbean.Pod.newforbidinfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Gather> gather = new mkdb.TTable<Long, xbean.Gather>() {
		@Override
		public String getName() {
			return "gather";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Gather value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Gather unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Gather value = xbean.Pod.newGather();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Gather newValue() {
			xbean.Gather value = xbean.Pod.newGather();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RewardData> timingreward = new mkdb.TTable<Long, xbean.RewardData>() {
		@Override
		public String getName() {
			return "timingreward";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RewardData value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RewardData unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RewardData value = xbean.Pod.newRewardData();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RewardData newValue() {
			xbean.RewardData value = xbean.Pod.newRewardData();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.NpcFollowInfo> npcfollowroles = new mkdb.TTable<Long, xbean.NpcFollowInfo>() {
		@Override
		public String getName() {
			return "npcfollowroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NpcFollowInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.NpcFollowInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NpcFollowInfo value = xbean.Pod.newNpcFollowInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NpcFollowInfo newValue() {
			xbean.NpcFollowInfo value = xbean.Pod.newNpcFollowInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EParticleSkill> roleid2particleskill = new mkdb.TTable<Long, xbean.EParticleSkill>() {
		@Override
		public String getName() {
			return "roleid2particleskill";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EParticleSkill value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EParticleSkill unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EParticleSkill value = xbean.Pod.newEParticleSkill();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EParticleSkill newValue() {
			xbean.EParticleSkill value = xbean.Pod.newEParticleSkill();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BingFengWangZuo> bingfengwangzuos = new mkdb.TTable<Long, xbean.BingFengWangZuo>() {
		@Override
		public String getName() {
			return "bingfengwangzuos";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BingFengWangZuo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BingFengWangZuo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BingFengWangZuo value = xbean.Pod.newBingFengWangZuo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BingFengWangZuo newValue() {
			xbean.BingFengWangZuo value = xbean.Pod.newBingFengWangZuo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ClanFights> clanfights = new mkdb.TTable<Long, xbean.ClanFights>() {
		@Override
		public String getName() {
			return "clanfights";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanFights value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ClanFights unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanFights value = xbean.Pod.newClanFights();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanFights newValue() {
			xbean.ClanFights value = xbean.Pod.newClanFights();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RoleProfessionRankList> roleprofessionranklist = new mkdb.TTable<Integer, xbean.RoleProfessionRankList>() {
		@Override
		public String getName() {
			return "roleprofessionranklist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleProfessionRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RoleProfessionRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleProfessionRankList value = xbean.Pod.newRoleProfessionRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleProfessionRankList newValue() {
			xbean.RoleProfessionRankList value = xbean.Pod.newRoleProfessionRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ChatTime> chattimetab = new mkdb.TTable<Long, xbean.ChatTime>() {
		@Override
		public String getName() {
			return "chattimetab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ChatTime value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ChatTime unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ChatTime value = xbean.Pod.newChatTime();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ChatTime newValue() {
			xbean.ChatTime value = xbean.Pod.newChatTime();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.MultiExpInfo> multiexp = new mkdb.TTable<Long, xbean.MultiExpInfo>() {
		@Override
		public String getName() {
			return "multiexp";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MultiExpInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.MultiExpInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MultiExpInfo value = xbean.Pod.newMultiExpInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MultiExpInfo newValue() {
			xbean.MultiExpInfo value = xbean.Pod.newMultiExpInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.HookData> rolehook = new mkdb.TTable<Long, xbean.HookData>() {
		@Override
		public String getName() {
			return "rolehook";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.HookData value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.HookData unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.HookData value = xbean.Pod.newHookData();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.HookData newValue() {
			xbean.HookData value = xbean.Pod.newHookData();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.LDVideoHisList> ldvideohislisttab = new mkdb.TTable<Integer, xbean.LDVideoHisList>() {
		@Override
		public String getName() {
			return "ldvideohislisttab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LDVideoHisList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.LDVideoHisList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LDVideoHisList value = xbean.Pod.newLDVideoHisList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LDVideoHisList newValue() {
			xbean.LDVideoHisList value = xbean.Pod.newLDVideoHisList();
			return value;
		}

	};

	mkdb.TTable<Long, Integer> roleid2bj = new mkdb.TTable<Long, Integer>() {
		@Override
		public String getName() {
			return "roleid2bj";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<String, Long> platorderhistroy = new mkdb.TTable<String, Long>() {
		@Override
		public String getName() {
			return "platorderhistroy";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.YybFushiNums> yybfushi = new mkdb.TTable<Integer, xbean.YybFushiNums>() {
		@Override
		public String getName() {
			return "yybfushi";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.YybFushiNums value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.YybFushiNums unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.YybFushiNums value = xbean.Pod.newYybFushiNums();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.YybFushiNums newValue() {
			xbean.YybFushiNums value = xbean.Pod.newYybFushiNums();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.LianyunAwardInfo> lianyunaward = new mkdb.TTable<Integer, xbean.LianyunAwardInfo>() {
		@Override
		public String getName() {
			return "lianyunaward";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LianyunAwardInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.LianyunAwardInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LianyunAwardInfo value = xbean.Pod.newLianyunAwardInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LianyunAwardInfo newValue() {
			xbean.LianyunAwardInfo value = xbean.Pod.newLianyunAwardInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.AnYeLegendFuture> anyelegendtimer = new mkdb.TTable<Long, xbean.AnYeLegendFuture>() {
		@Override
		public String getName() {
			return "anyelegendtimer";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AnYeLegendFuture value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.AnYeLegendFuture unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AnYeLegendFuture value = xbean.Pod.newAnYeLegendFuture();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AnYeLegendFuture newValue() {
			xbean.AnYeLegendFuture value = xbean.Pod.newAnYeLegendFuture();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Petrecoverlist> petrecover = new mkdb.TTable<Long, xbean.Petrecoverlist>() {
		@Override
		public String getName() {
			return "petrecover";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Petrecoverlist value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Petrecoverlist unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Petrecoverlist value = xbean.Pod.newPetrecoverlist();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Petrecoverlist newValue() {
			xbean.Petrecoverlist value = xbean.Pod.newPetrecoverlist();
			return value;
		}

	};

	mkdb.TTable<Long, Long> watcherid2battleid = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "watcherid2battleid";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.LevelSeal> levelseal = new mkdb.TTable<Integer, xbean.LevelSeal>() {
		@Override
		public String getName() {
			return "levelseal";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LevelSeal value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.LevelSeal unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LevelSeal value = xbean.Pod.newLevelSeal();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LevelSeal newValue() {
			xbean.LevelSeal value = xbean.Pod.newLevelSeal();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BeginnerTip> beginnertip = new mkdb.TTable<Long, xbean.BeginnerTip>() {
		@Override
		public String getName() {
			return "beginnertip";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BeginnerTip value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BeginnerTip unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BeginnerTip value = xbean.Pod.newBeginnerTip();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BeginnerTip newValue() {
			xbean.BeginnerTip value = xbean.Pod.newBeginnerTip();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BingFengRole> bingfengroles = new mkdb.TTable<Long, xbean.BingFengRole>() {
		@Override
		public String getName() {
			return "bingfengroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BingFengRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BingFengRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BingFengRole value = xbean.Pod.newBingFengRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BingFengRole newValue() {
			xbean.BingFengRole value = xbean.Pod.newBingFengRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ELiveSkill> roleid2liveskill = new mkdb.TTable<Long, xbean.ELiveSkill>() {
		@Override
		public String getName() {
			return "roleid2liveskill";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ELiveSkill value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ELiveSkill unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ELiveSkill value = xbean.Pod.newELiveSkill();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ELiveSkill newValue() {
			xbean.ELiveSkill value = xbean.Pod.newELiveSkill();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ChargeOrder> chargeorder = new mkdb.TTable<Long, xbean.ChargeOrder>() {
		@Override
		public String getName() {
			return "chargeorder";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ChargeOrder value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ChargeOrder unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ChargeOrder value = xbean.Pod.newChargeOrder();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ChargeOrder newValue() {
			xbean.ChargeOrder value = xbean.Pod.newChargeOrder();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RegRec> regtbl = new mkdb.TTable<Long, xbean.RegRec>() {
		@Override
		public String getName() {
			return "regtbl";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RegRec value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RegRec unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RegRec value = xbean.Pod.newRegRec();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RegRec newValue() {
			xbean.RegRec value = xbean.Pod.newRegRec();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CameraInfo> missioncameras = new mkdb.TTable<Long, xbean.CameraInfo>() {
		@Override
		public String getName() {
			return "missioncameras";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CameraInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CameraInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CameraInfo value = xbean.Pod.newCameraInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CameraInfo newValue() {
			xbean.CameraInfo value = xbean.Pod.newCameraInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TreasureMapInfo> opentreasuremaptable = new mkdb.TTable<Long, xbean.TreasureMapInfo>() {
		@Override
		public String getName() {
			return "opentreasuremaptable";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TreasureMapInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TreasureMapInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TreasureMapInfo value = xbean.Pod.newTreasureMapInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TreasureMapInfo newValue() {
			xbean.TreasureMapInfo value = xbean.Pod.newTreasureMapInfo();
			return value;
		}

	};

	mkdb.TTable<Long, Long> requestgoto = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "requestgoto";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Monster> professionleaderfightdata = new mkdb.TTable<Long, xbean.Monster>() {
		@Override
		public String getName() {
			return "professionleaderfightdata";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Monster value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Monster unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Monster value = xbean.Pod.newMonster();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Monster newValue() {
			xbean.Monster value = xbean.Pod.newMonster();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleSpace> rolespaces = new mkdb.TTable<Long, xbean.RoleSpace>() {
		@Override
		public String getName() {
			return "rolespaces";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleSpace value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleSpace unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleSpace value = xbean.Pod.newRoleSpace();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleSpace newValue() {
			xbean.RoleSpace value = xbean.Pod.newRoleSpace();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PvP3Race> pvp3races = new mkdb.TTable<Integer, xbean.PvP3Race>() {
		@Override
		public String getName() {
			return "pvp3races";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP3Race value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PvP3Race unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP3Race value = xbean.Pod.newPvP3Race();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP3Race newValue() {
			xbean.PvP3Race value = xbean.Pod.newPvP3Race();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ImpExamRecord> role2impexam = new mkdb.TTable<Long, xbean.ImpExamRecord>() {
		@Override
		public String getName() {
			return "role2impexam";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ImpExamRecord value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ImpExamRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ImpExamRecord value = xbean.Pod.newImpExamRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ImpExamRecord newValue() {
			xbean.ImpExamRecord value = xbean.Pod.newImpExamRecord();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PlatformInfo> payplatform = new mkdb.TTable<Integer, xbean.PlatformInfo>() {
		@Override
		public String getName() {
			return "payplatform";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PlatformInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PlatformInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PlatformInfo value = xbean.Pod.newPlatformInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PlatformInfo newValue() {
			xbean.PlatformInfo value = xbean.Pod.newPlatformInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.InstanceTurnGroup> instanceturngroups = new mkdb.TTable<Integer, xbean.InstanceTurnGroup>() {
		@Override
		public String getName() {
			return "instanceturngroups";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceTurnGroup value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.InstanceTurnGroup unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceTurnGroup value = xbean.Pod.newInstanceTurnGroup();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceTurnGroup newValue() {
			xbean.InstanceTurnGroup value = xbean.Pod.newInstanceTurnGroup();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.HuoBanVip> huobanvips = new mkdb.TTable<Integer, xbean.HuoBanVip>() {
		@Override
		public String getName() {
			return "huobanvips";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.HuoBanVip value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.HuoBanVip unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.HuoBanVip value = xbean.Pod.newHuoBanVip();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.HuoBanVip newValue() {
			xbean.HuoBanVip value = xbean.Pod.newHuoBanVip();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.TradingMap> tradingidtab = new mkdb.TTable<Integer, xbean.TradingMap>() {
		@Override
		public String getName() {
			return "tradingidtab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TradingMap value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.TradingMap unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TradingMap value = xbean.Pod.newTradingMap();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TradingMap newValue() {
			xbean.TradingMap value = xbean.Pod.newTradingMap();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleQuitStatistics> rolequitstatistics = new mkdb.TTable<Long, xbean.RoleQuitStatistics>() {
		@Override
		public String getName() {
			return "rolequitstatistics";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleQuitStatistics value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleQuitStatistics unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleQuitStatistics value = xbean.Pod.newRoleQuitStatistics();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleQuitStatistics newValue() {
			xbean.RoleQuitStatistics value = xbean.Pod.newRoleQuitStatistics();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.PetItem> pettab = new mkdb.TTable<Long, xbean.PetItem>() {
		@Override
		public String getName() {
			return "pettab";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PetItem value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.PetItem unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PetItem value = xbean.Pod.newPetItem();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PetItem newValue() {
			xbean.PetItem value = xbean.Pod.newPetItem();
			return value;
		}

	};

	mkdb.TTable<Long, Long> roleidclan = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "roleidclan";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ClanZongHeRankList> clanzonghelist = new mkdb.TTable<Integer, xbean.ClanZongHeRankList>() {
		@Override
		public String getName() {
			return "clanzonghelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanZongHeRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ClanZongHeRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanZongHeRankList value = xbean.Pod.newClanZongHeRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanZongHeRankList newValue() {
			xbean.ClanZongHeRankList value = xbean.Pod.newClanZongHeRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TicketItemExInfo> ticketiteminfo = new mkdb.TTable<Long, xbean.TicketItemExInfo>() {
		@Override
		public String getName() {
			return "ticketiteminfo";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TicketItemExInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TicketItemExInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TicketItemExInfo value = xbean.Pod.newTicketItemExInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TicketItemExInfo newValue() {
			xbean.TicketItemExInfo value = xbean.Pod.newTicketItemExInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TimerFuture> timerfutures = new mkdb.TTable<Long, xbean.TimerFuture>() {
		@Override
		public String getName() {
			return "timerfutures";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TimerFuture value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TimerFuture unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TimerFuture value = xbean.Pod.newTimerFuture();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TimerFuture newValue() {
			xbean.TimerFuture value = xbean.Pod.newTimerFuture();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.YbNums> fushinum = new mkdb.TTable<Integer, xbean.YbNums>() {
		@Override
		public String getName() {
			return "fushinum";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.YbNums value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.YbNums unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.YbNums value = xbean.Pod.newYbNums();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.YbNums newValue() {
			xbean.YbNums value = xbean.Pod.newYbNums();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Compensation> compensation = new mkdb.TTable<Long, xbean.Compensation>() {
		@Override
		public String getName() {
			return "compensation";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Compensation value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Compensation unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Compensation value = xbean.Pod.newCompensation();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Compensation newValue() {
			xbean.Compensation value = xbean.Pod.newCompensation();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.HuoBanZhenrong> huobanzhenrongs = new mkdb.TTable<Long, xbean.HuoBanZhenrong>() {
		@Override
		public String getName() {
			return "huobanzhenrongs";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.HuoBanZhenrong value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.HuoBanZhenrong unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.HuoBanZhenrong value = xbean.Pod.newHuoBanZhenrong();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.HuoBanZhenrong newValue() {
			xbean.HuoBanZhenrong value = xbean.Pod.newHuoBanZhenrong();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.NewPlayActiveDay> rolesplayactive = new mkdb.TTable<Long, xbean.NewPlayActiveDay>() {
		@Override
		public String getName() {
			return "rolesplayactive";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NewPlayActiveDay value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.NewPlayActiveDay unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NewPlayActiveDay value = xbean.Pod.newNewPlayActiveDay();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NewPlayActiveDay newValue() {
			xbean.NewPlayActiveDay value = xbean.Pod.newNewPlayActiveDay();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.BroadcastMsgList> broadcastmsgtab = new mkdb.TTable<Integer, xbean.BroadcastMsgList>() {
		@Override
		public String getName() {
			return "broadcastmsgtab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BroadcastMsgList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.BroadcastMsgList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BroadcastMsgList value = xbean.Pod.newBroadcastMsgList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BroadcastMsgList newValue() {
			xbean.BroadcastMsgList value = xbean.Pod.newBroadcastMsgList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CompensationRole> compensationroles = new mkdb.TTable<Long, xbean.CompensationRole>() {
		@Override
		public String getName() {
			return "compensationroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CompensationRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CompensationRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CompensationRole value = xbean.Pod.newCompensationRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CompensationRole newValue() {
			xbean.CompensationRole value = xbean.Pod.newCompensationRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ChangeSchoolInfo> changeschool = new mkdb.TTable<Long, xbean.ChangeSchoolInfo>() {
		@Override
		public String getName() {
			return "changeschool";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ChangeSchoolInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ChangeSchoolInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ChangeSchoolInfo value = xbean.Pod.newChangeSchoolInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ChangeSchoolInfo newValue() {
			xbean.ChangeSchoolInfo value = xbean.Pod.newChangeSchoolInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TraderWheel> opentraderwheeltable = new mkdb.TTable<Long, xbean.TraderWheel>() {
		@Override
		public String getName() {
			return "opentraderwheeltable";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TraderWheel value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TraderWheel unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TraderWheel value = xbean.Pod.newTraderWheel();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TraderWheel newValue() {
			xbean.TraderWheel value = xbean.Pod.newTraderWheel();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.PetSellCount> rolepetsellcount = new mkdb.TTable<Long, xbean.PetSellCount>() {
		@Override
		public String getName() {
			return "rolepetsellcount";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PetSellCount value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.PetSellCount unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PetSellCount value = xbean.Pod.newPetSellCount();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PetSellCount newValue() {
			xbean.PetSellCount value = xbean.Pod.newPetSellCount();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ClanFightBattleField> clanfightbattlefield = new mkdb.TTable<Long, xbean.ClanFightBattleField>() {
		@Override
		public String getName() {
			return "clanfightbattlefield";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanFightBattleField value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ClanFightBattleField unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanFightBattleField value = xbean.Pod.newClanFightBattleField();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanFightBattleField newValue() {
			xbean.ClanFightBattleField value = xbean.Pod.newClanFightBattleField();
			return value;
		}

	};

	mkdb.TTable<Long, Long> role2npcbattle = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "role2npcbattle";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PvP5Race> pvp5races = new mkdb.TTable<Integer, xbean.PvP5Race>() {
		@Override
		public String getName() {
			return "pvp5races";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP5Race value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PvP5Race unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP5Race value = xbean.Pod.newPvP5Race();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP5Race newValue() {
			xbean.PvP5Race value = xbean.Pod.newPvP5Race();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleTradingHisRecordList> roletradinghisrecordlisttab = new mkdb.TTable<Long, xbean.RoleTradingHisRecordList>() {
		@Override
		public String getName() {
			return "roletradinghisrecordlisttab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleTradingHisRecordList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleTradingHisRecordList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleTradingHisRecordList value = xbean.Pod.newRoleTradingHisRecordList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleTradingHisRecordList newValue() {
			xbean.RoleTradingHisRecordList value = xbean.Pod.newRoleTradingHisRecordList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ActivityItemLimit> awarditemlimit = new mkdb.TTable<Integer, xbean.ActivityItemLimit>() {
		@Override
		public String getName() {
			return "awarditemlimit";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ActivityItemLimit value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ActivityItemLimit unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActivityItemLimit value = xbean.Pod.newActivityItemLimit();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ActivityItemLimit newValue() {
			xbean.ActivityItemLimit value = xbean.Pod.newActivityItemLimit();
			return value;
		}

	};

	mkdb.TTable<String, xbean.Compensation> usercompensation = new mkdb.TTable<String, xbean.Compensation>() {
		@Override
		public String getName() {
			return "usercompensation";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Compensation value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.Compensation unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Compensation value = xbean.Pod.newCompensation();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Compensation newValue() {
			xbean.Compensation value = xbean.Pod.newCompensation();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleReceiveRedPackRecordList> rolereceiveredpackrecordtab = new mkdb.TTable<Long, xbean.RoleReceiveRedPackRecordList>() {
		@Override
		public String getName() {
			return "rolereceiveredpackrecordtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleReceiveRedPackRecordList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleReceiveRedPackRecordList value = xbean.Pod.newRoleReceiveRedPackRecordList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleReceiveRedPackRecordList newValue() {
			xbean.RoleReceiveRedPackRecordList value = xbean.Pod.newRoleReceiveRedPackRecordList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RedPack> roleid2worldredpacksendtotal = new mkdb.TTable<Long, xbean.RedPack>() {
		@Override
		public String getName() {
			return "roleid2worldredpacksendtotal";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RedPack value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RedPack unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RedPack value = xbean.Pod.newRedPack();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RedPack newValue() {
			xbean.RedPack value = xbean.Pod.newRedPack();
			return value;
		}

	};

	mkdb.TTable<Long, Long> roleyaoqianshuinfos = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "roleyaoqianshuinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.YybOrder> yybchargeorder = new mkdb.TTable<Long, xbean.YybOrder>() {
		@Override
		public String getName() {
			return "yybchargeorder";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.YybOrder value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.YybOrder unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.YybOrder value = xbean.Pod.newYybOrder();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.YybOrder newValue() {
			xbean.YybOrder value = xbean.Pod.newYybOrder();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InstanceFutureInfo> instancefuture = new mkdb.TTable<Long, xbean.InstanceFutureInfo>() {
		@Override
		public String getName() {
			return "instancefuture";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceFutureInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InstanceFutureInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceFutureInfo value = xbean.Pod.newInstanceFutureInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceFutureInfo newValue() {
			xbean.InstanceFutureInfo value = xbean.Pod.newInstanceFutureInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TaskEventInfo> roletaskeventtab = new mkdb.TTable<Long, xbean.TaskEventInfo>() {
		@Override
		public String getName() {
			return "roletaskeventtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TaskEventInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TaskEventInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TaskEventInfo value = xbean.Pod.newTaskEventInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TaskEventInfo newValue() {
			xbean.TaskEventInfo value = xbean.Pod.newTaskEventInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Pets> pet = new mkdb.TTable<Long, xbean.Pets>() {
		@Override
		public String getName() {
			return "pet";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Pets value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Pets unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Pets value = xbean.Pod.newPets();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Pets newValue() {
			xbean.Pets value = xbean.Pod.newPets();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.GacdPicStatus> gacdpic = new mkdb.TTable<Integer, xbean.GacdPicStatus>() {
		@Override
		public String getName() {
			return "gacdpic";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.GacdPicStatus value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.GacdPicStatus unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.GacdPicStatus value = xbean.Pod.newGacdPicStatus();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.GacdPicStatus newValue() {
			xbean.GacdPicStatus value = xbean.Pod.newGacdPicStatus();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InstanceAskInfo> instanceaskinfos = new mkdb.TTable<Long, xbean.InstanceAskInfo>() {
		@Override
		public String getName() {
			return "instanceaskinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceAskInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InstanceAskInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceAskInfo value = xbean.Pod.newInstanceAskInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceAskInfo newValue() {
			xbean.InstanceAskInfo value = xbean.Pod.newInstanceAskInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PvP5ScoreRankList> pvp5scorelist = new mkdb.TTable<Integer, xbean.PvP5ScoreRankList>() {
		@Override
		public String getName() {
			return "pvp5scorelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP5ScoreRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PvP5ScoreRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP5ScoreRankList value = xbean.Pod.newPvP5ScoreRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP5ScoreRankList newValue() {
			xbean.PvP5ScoreRankList value = xbean.Pod.newPvP5ScoreRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BestowNpcInfo> bestownpc = new mkdb.TTable<Long, xbean.BestowNpcInfo>() {
		@Override
		public String getName() {
			return "bestownpc";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BestowNpcInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BestowNpcInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BestowNpcInfo value = xbean.Pod.newBestowNpcInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BestowNpcInfo newValue() {
			xbean.BestowNpcInfo value = xbean.Pod.newBestowNpcInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.MulDayLoginGift> rolemuldayloginreward = new mkdb.TTable<Long, xbean.MulDayLoginGift>() {
		@Override
		public String getName() {
			return "rolemuldayloginreward";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MulDayLoginGift value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.MulDayLoginGift unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MulDayLoginGift value = xbean.Pod.newMulDayLoginGift();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MulDayLoginGift newValue() {
			xbean.MulDayLoginGift value = xbean.Pod.newMulDayLoginGift();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Pets> marketpet = new mkdb.TTable<Long, xbean.Pets>() {
		@Override
		public String getName() {
			return "marketpet";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Pets value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Pets unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Pets value = xbean.Pod.newPets();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Pets newValue() {
			xbean.Pets value = xbean.Pod.newPets();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Pets> blackmarketpettab = new mkdb.TTable<Long, xbean.Pets>() {
		@Override
		public String getName() {
			return "blackmarketpettab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Pets value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Pets unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Pets value = xbean.Pod.newPets();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Pets newValue() {
			xbean.Pets value = xbean.Pod.newPets();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RuneInfo> runeinfotab = new mkdb.TTable<Long, xbean.RuneInfo>() {
		@Override
		public String getName() {
			return "runeinfotab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RuneInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RuneInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RuneInfo value = xbean.Pod.newRuneInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RuneInfo newValue() {
			xbean.RuneInfo value = xbean.Pod.newRuneInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RoleZongheRankList> rolezonghelist = new mkdb.TTable<Integer, xbean.RoleZongheRankList>() {
		@Override
		public String getName() {
			return "rolezonghelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleZongheRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RoleZongheRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleZongheRankList value = xbean.Pod.newRoleZongheRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleZongheRankList newValue() {
			xbean.RoleZongheRankList value = xbean.Pod.newRoleZongheRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EChargeReturnProfit> roleid2chargereturnprofit = new mkdb.TTable<Long, xbean.EChargeReturnProfit>() {
		@Override
		public String getName() {
			return "roleid2chargereturnprofit";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EChargeReturnProfit value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EChargeReturnProfit unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EChargeReturnProfit value = xbean.Pod.newEChargeReturnProfit();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EChargeReturnProfit newValue() {
			xbean.EChargeReturnProfit value = xbean.Pod.newEChargeReturnProfit();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RedPackInfoViewList> redpackinfoviewlisttab = new mkdb.TTable<Integer, xbean.RedPackInfoViewList>() {
		@Override
		public String getName() {
			return "redpackinfoviewlisttab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RedPackInfoViewList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RedPackInfoViewList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RedPackInfoViewList value = xbean.Pod.newRedPackInfoViewList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RedPackInfoViewList newValue() {
			xbean.RedPackInfoViewList value = xbean.Pod.newRedPackInfoViewList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ShowingMission> showingmission = new mkdb.TTable<Long, xbean.ShowingMission>() {
		@Override
		public String getName() {
			return "showingmission";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ShowingMission value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ShowingMission unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ShowingMission value = xbean.Pod.newShowingMission();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ShowingMission newValue() {
			xbean.ShowingMission value = xbean.Pod.newShowingMission();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.HuoBanShuxingLevel> huobanproperty = new mkdb.TTable<Integer, xbean.HuoBanShuxingLevel>() {
		@Override
		public String getName() {
			return "huobanproperty";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.HuoBanShuxingLevel value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.HuoBanShuxingLevel unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.HuoBanShuxingLevel value = xbean.Pod.newHuoBanShuxingLevel();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.HuoBanShuxingLevel newValue() {
			xbean.HuoBanShuxingLevel value = xbean.Pod.newHuoBanShuxingLevel();
			return value;
		}

	};

	mkdb.TTable<Long, Long> roleid2teamid = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "roleid2teamid";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ServerRoles> rolenumfornewserver = new mkdb.TTable<Integer, xbean.ServerRoles>() {
		@Override
		public String getName() {
			return "rolenumfornewserver";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ServerRoles value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ServerRoles unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ServerRoles value = xbean.Pod.newServerRoles();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ServerRoles newValue() {
			xbean.ServerRoles value = xbean.Pod.newServerRoles();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RecruitAward> rolerecruitaward = new mkdb.TTable<Long, xbean.RecruitAward>() {
		@Override
		public String getName() {
			return "rolerecruitaward";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RecruitAward value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RecruitAward unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RecruitAward value = xbean.Pod.newRecruitAward();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RecruitAward newValue() {
			xbean.RecruitAward value = xbean.Pod.newRecruitAward();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.WheelItemLimit> wheelitemlimit = new mkdb.TTable<Integer, xbean.WheelItemLimit>() {
		@Override
		public String getName() {
			return "wheelitemlimit";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.WheelItemLimit value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.WheelItemLimit unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.WheelItemLimit value = xbean.Pod.newWheelItemLimit();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.WheelItemLimit newValue() {
			xbean.WheelItemLimit value = xbean.Pod.newWheelItemLimit();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.VecList> fdqmazelist = new mkdb.TTable<Long, xbean.VecList>() {
		@Override
		public String getName() {
			return "fdqmazelist";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.VecList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.VecList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.VecList value = xbean.Pod.newVecList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.VecList newValue() {
			xbean.VecList value = xbean.Pod.newVecList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleBlackMarket> blackmarkettab = new mkdb.TTable<Long, xbean.RoleBlackMarket>() {
		@Override
		public String getName() {
			return "blackmarkettab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleBlackMarket value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleBlackMarket unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleBlackMarket value = xbean.Pod.newRoleBlackMarket();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleBlackMarket newValue() {
			xbean.RoleBlackMarket value = xbean.Pod.newRoleBlackMarket();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ActivityStatus> activitytable = new mkdb.TTable<Integer, xbean.ActivityStatus>() {
		@Override
		public String getName() {
			return "activitytable";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ActivityStatus value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ActivityStatus unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActivityStatus value = xbean.Pod.newActivityStatus();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ActivityStatus newValue() {
			xbean.ActivityStatus value = xbean.Pod.newActivityStatus();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RollCardInfo> rolerollcard = new mkdb.TTable<Long, xbean.RollCardInfo>() {
		@Override
		public String getName() {
			return "rolerollcard";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RollCardInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RollCardInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RollCardInfo value = xbean.Pod.newRollCardInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RollCardInfo newValue() {
			xbean.RollCardInfo value = xbean.Pod.newRollCardInfo();
			return value;
		}

	};

	mkdb.TTable<Long, Long> roleid2clanfightid = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "roleid2clanfightid";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.AnYeData> anyemaxituan = new mkdb.TTable<Long, xbean.AnYeData>() {
		@Override
		public String getName() {
			return "anyemaxituan";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AnYeData value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.AnYeData unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AnYeData value = xbean.Pod.newAnYeData();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AnYeData newValue() {
			xbean.AnYeData value = xbean.Pod.newAnYeData();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.AttentionGoods> marketattentiongoodstab = new mkdb.TTable<Long, xbean.AttentionGoods>() {
		@Override
		public String getName() {
			return "marketattentiongoodstab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AttentionGoods value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.AttentionGoods unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AttentionGoods value = xbean.Pod.newAttentionGoods();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AttentionGoods newValue() {
			xbean.AttentionGoods value = xbean.Pod.newAttentionGoods();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RoleLevelRankList> rolelevellist = new mkdb.TTable<Integer, xbean.RoleLevelRankList>() {
		@Override
		public String getName() {
			return "rolelevellist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleLevelRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RoleLevelRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleLevelRankList value = xbean.Pod.newRoleLevelRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleLevelRankList newValue() {
			xbean.RoleLevelRankList value = xbean.Pod.newRoleLevelRankList();
			return value;
		}

	};

	mkdb.TTable<Long, Long> roleid2battlemelonid = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "roleid2battlemelonid";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RolesNotifyMap> notify = new mkdb.TTable<Integer, xbean.RolesNotifyMap>() {
		@Override
		public String getName() {
			return "notify";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RolesNotifyMap value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RolesNotifyMap unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RolesNotifyMap value = xbean.Pod.newRolesNotifyMap();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RolesNotifyMap newValue() {
			xbean.RolesNotifyMap value = xbean.Pod.newRolesNotifyMap();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.NormalItem> normaltab = new mkdb.TTable<Long, xbean.NormalItem>() {
		@Override
		public String getName() {
			return "normaltab";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NormalItem value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.NormalItem unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NormalItem value = xbean.Pod.newNormalItem();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NormalItem newValue() {
			xbean.NormalItem value = xbean.Pod.newNormalItem();
			return value;
		}

	};

	mkdb.TTable<Long, Integer> roleid2userid = new mkdb.TTable<Long, Integer>() {
		@Override
		public String getName() {
			return "roleid2userid";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EventInfo> worldeventtab = new mkdb.TTable<Long, xbean.EventInfo>() {
		@Override
		public String getName() {
			return "worldeventtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EventInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EventInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EventInfo value = xbean.Pod.newEventInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EventInfo newValue() {
			xbean.EventInfo value = xbean.Pod.newEventInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.PresellItem> preselltab = new mkdb.TTable<Long, xbean.PresellItem>() {
		@Override
		public String getName() {
			return "preselltab";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PresellItem value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.PresellItem unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PresellItem value = xbean.Pod.newPresellItem();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PresellItem newValue() {
			xbean.PresellItem value = xbean.Pod.newPresellItem();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PvP1Race> pvp1races = new mkdb.TTable<Integer, xbean.PvP1Race>() {
		@Override
		public String getName() {
			return "pvp1races";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP1Race value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PvP1Race unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP1Race value = xbean.Pod.newPvP1Race();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP1Race newValue() {
			xbean.PvP1Race value = xbean.Pod.newPvP1Race();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InstanceInfoCol> roleinstancetask = new mkdb.TTable<Long, xbean.InstanceInfoCol>() {
		@Override
		public String getName() {
			return "roleinstancetask";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceInfoCol value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InstanceInfoCol unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceInfoCol value = xbean.Pod.newInstanceInfoCol();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceInfoCol newValue() {
			xbean.InstanceInfoCol value = xbean.Pod.newInstanceInfoCol();
			return value;
		}

	};

	mkdb.TTable<String, xbean.RoleTradingRecord> roletradingrecordstab = new mkdb.TTable<String, xbean.RoleTradingRecord>() {
		@Override
		public String getName() {
			return "roletradingrecordstab";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleTradingRecord value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.RoleTradingRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleTradingRecord value = xbean.Pod.newRoleTradingRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleTradingRecord newValue() {
			xbean.RoleTradingRecord value = xbean.Pod.newRoleTradingRecord();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ProfessionLeaderCand> professionleadercandidates = new mkdb.TTable<Integer, xbean.ProfessionLeaderCand>() {
		@Override
		public String getName() {
			return "professionleadercandidates";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ProfessionLeaderCand value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ProfessionLeaderCand unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ProfessionLeaderCand value = xbean.Pod.newProfessionLeaderCand();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ProfessionLeaderCand newValue() {
			xbean.ProfessionLeaderCand value = xbean.Pod.newProfessionLeaderCand();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ProfessionLeaderTicketInfo> roleid2professionleadertickets = new mkdb.TTable<Long, xbean.ProfessionLeaderTicketInfo>() {
		@Override
		public String getName() {
			return "roleid2professionleadertickets";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ProfessionLeaderTicketInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ProfessionLeaderTicketInfo value = xbean.Pod.newProfessionLeaderTicketInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo newValue() {
			xbean.ProfessionLeaderTicketInfo value = xbean.Pod.newProfessionLeaderTicketInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ETeamMatch> targetid2teammatch = new mkdb.TTable<Integer, xbean.ETeamMatch>() {
		@Override
		public String getName() {
			return "targetid2teammatch";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ETeamMatch value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ETeamMatch unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ETeamMatch value = xbean.Pod.newETeamMatch();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ETeamMatch newValue() {
			xbean.ETeamMatch value = xbean.Pod.newETeamMatch();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> questbag = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "questbag";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ClanInstances> claninstances = new mkdb.TTable<Long, xbean.ClanInstances>() {
		@Override
		public String getName() {
			return "claninstances";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanInstances value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ClanInstances unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanInstances value = xbean.Pod.newClanInstances();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanInstances newValue() {
			xbean.ClanInstances value = xbean.Pod.newClanInstances();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.YaoQianShuInfo> yaoqianshutables = new mkdb.TTable<Long, xbean.YaoQianShuInfo>() {
		@Override
		public String getName() {
			return "yaoqianshutables";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.YaoQianShuInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.YaoQianShuInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.YaoQianShuInfo value = xbean.Pod.newYaoQianShuInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.YaoQianShuInfo newValue() {
			xbean.YaoQianShuInfo value = xbean.Pod.newYaoQianShuInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.FirstCharge> firstchargenew = new mkdb.TTable<Integer, xbean.FirstCharge>() {
		@Override
		public String getName() {
			return "firstchargenew";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.FirstCharge value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.FirstCharge unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.FirstCharge value = xbean.Pod.newFirstCharge();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.FirstCharge newValue() {
			xbean.FirstCharge value = xbean.Pod.newFirstCharge();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TriggerRole> triggerroles = new mkdb.TTable<Long, xbean.TriggerRole>() {
		@Override
		public String getName() {
			return "triggerroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TriggerRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TriggerRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TriggerRole value = xbean.Pod.newTriggerRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TriggerRole newValue() {
			xbean.TriggerRole value = xbean.Pod.newTriggerRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.OfflineFuture> offlinefuture = new mkdb.TTable<Long, xbean.OfflineFuture>() {
		@Override
		public String getName() {
			return "offlinefuture";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.OfflineFuture value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.OfflineFuture unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.OfflineFuture value = xbean.Pod.newOfflineFuture();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.OfflineFuture newValue() {
			xbean.OfflineFuture value = xbean.Pod.newOfflineFuture();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ClanInfo> clans = new mkdb.TTable<Long, xbean.ClanInfo>() {
		@Override
		public String getName() {
			return "clans";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ClanInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanInfo value = xbean.Pod.newClanInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanInfo newValue() {
			xbean.ClanInfo value = xbean.Pod.newClanInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RenXingData> rolerenxingdata = new mkdb.TTable<Long, xbean.RenXingData>() {
		@Override
		public String getName() {
			return "rolerenxingdata";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RenXingData value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RenXingData unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RenXingData value = xbean.Pod.newRenXingData();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RenXingData newValue() {
			xbean.RenXingData value = xbean.Pod.newRenXingData();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ActiveRoleInfo> activeroletable = new mkdb.TTable<Long, xbean.ActiveRoleInfo>() {
		@Override
		public String getName() {
			return "activeroletable";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ActiveRoleInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ActiveRoleInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActiveRoleInfo value = xbean.Pod.newActiveRoleInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ActiveRoleInfo newValue() {
			xbean.ActiveRoleInfo value = xbean.Pod.newActiveRoleInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ItemUse> roleuseitemcount = new mkdb.TTable<Long, xbean.ItemUse>() {
		@Override
		public String getName() {
			return "roleuseitemcount";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ItemUse value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ItemUse unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ItemUse value = xbean.Pod.newItemUse();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ItemUse newValue() {
			xbean.ItemUse value = xbean.Pod.newItemUse();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ActiveUserInfo> activeusertable = new mkdb.TTable<Integer, xbean.ActiveUserInfo>() {
		@Override
		public String getName() {
			return "activeusertable";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ActiveUserInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ActiveUserInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ActiveUserInfo value = xbean.Pod.newActiveUserInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ActiveUserInfo newValue() {
			xbean.ActiveUserInfo value = xbean.Pod.newActiveUserInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.timerNpcInfoCol> timernpcinfotable = new mkdb.TTable<Integer, xbean.timerNpcInfoCol>() {
		@Override
		public String getName() {
			return "timernpcinfotable";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.timerNpcInfoCol value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.timerNpcInfoCol unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.timerNpcInfoCol value = xbean.Pod.newtimerNpcInfoCol();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.timerNpcInfoCol newValue() {
			xbean.timerNpcInfoCol value = xbean.Pod.newtimerNpcInfoCol();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.NpcInfo> npcs = new mkdb.TTable<Long, xbean.NpcInfo>() {
		@Override
		public String getName() {
			return "npcs";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NpcInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.NpcInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NpcInfo value = xbean.Pod.newNpcInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NpcInfo newValue() {
			xbean.NpcInfo value = xbean.Pod.newNpcInfo();
			return value;
		}

	};

	mkdb.TTable<Long, Integer> takebackmarketcontainertab = new mkdb.TTable<Long, Integer>() {
		@Override
		public String getName() {
			return "takebackmarketcontainertab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> depot = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "depot";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.FriendGiveNum> friendgivenum = new mkdb.TTable<Long, xbean.FriendGiveNum>() {
		@Override
		public String getName() {
			return "friendgivenum";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.FriendGiveNum value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.FriendGiveNum unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.FriendGiveNum value = xbean.Pod.newFriendGiveNum();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.FriendGiveNum newValue() {
			xbean.FriendGiveNum value = xbean.Pod.newFriendGiveNum();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Mission> majorscenariomission = new mkdb.TTable<Long, xbean.Mission>() {
		@Override
		public String getName() {
			return "majorscenariomission";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Mission value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Mission unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Mission value = xbean.Pod.newMission();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Mission newValue() {
			xbean.Mission value = xbean.Pod.newMission();
			return value;
		}

	};

	mkdb.TTable<String, Long> rolename2key = new mkdb.TTable<String, Long>() {
		@Override
		public String getName() {
			return "rolename2key";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.BaoXiangInfo> rolekaibaoxiang = new mkdb.TTable<Long, xbean.BaoXiangInfo>() {
		@Override
		public String getName() {
			return "rolekaibaoxiang";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BaoXiangInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.BaoXiangInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BaoXiangInfo value = xbean.Pod.newBaoXiangInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BaoXiangInfo newValue() {
			xbean.BaoXiangInfo value = xbean.Pod.newBaoXiangInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.LiveDieRoleInfo> livedieroleinfotab = new mkdb.TTable<Long, xbean.LiveDieRoleInfo>() {
		@Override
		public String getName() {
			return "livedieroleinfotab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LiveDieRoleInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.LiveDieRoleInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LiveDieRoleInfo value = xbean.Pod.newLiveDieRoleInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LiveDieRoleInfo newValue() {
			xbean.LiveDieRoleInfo value = xbean.Pod.newLiveDieRoleInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Itemrecoverlist> itemrecover = new mkdb.TTable<Long, xbean.Itemrecoverlist>() {
		@Override
		public String getName() {
			return "itemrecover";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Itemrecoverlist value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Itemrecoverlist unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Itemrecoverlist value = xbean.Pod.newItemrecoverlist();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Itemrecoverlist newValue() {
			xbean.Itemrecoverlist value = xbean.Pod.newItemrecoverlist();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CommitedMissions> commitedmission = new mkdb.TTable<Long, xbean.CommitedMissions>() {
		@Override
		public String getName() {
			return "commitedmission";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CommitedMissions value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CommitedMissions unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CommitedMissions value = xbean.Pod.newCommitedMissions();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CommitedMissions newValue() {
			xbean.CommitedMissions value = xbean.Pod.newCommitedMissions();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InstanceInfo> instancetask = new mkdb.TTable<Long, xbean.InstanceInfo>() {
		@Override
		public String getName() {
			return "instancetask";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InstanceInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceInfo value = xbean.Pod.newInstanceInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceInfo newValue() {
			xbean.InstanceInfo value = xbean.Pod.newInstanceInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.BJTeamInfo> bianjieteam = new mkdb.TTable<Integer, xbean.BJTeamInfo>() {
		@Override
		public String getName() {
			return "bianjieteam";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.BJTeamInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.BJTeamInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.BJTeamInfo value = xbean.Pod.newBJTeamInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.BJTeamInfo newValue() {
			xbean.BJTeamInfo value = xbean.Pod.newBJTeamInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.SchoolWheel> openschoolwheeltable = new mkdb.TTable<Long, xbean.SchoolWheel>() {
		@Override
		public String getName() {
			return "openschoolwheeltable";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.SchoolWheel value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.SchoolWheel unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.SchoolWheel value = xbean.Pod.newSchoolWheel();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.SchoolWheel newValue() {
			xbean.SchoolWheel value = xbean.Pod.newSchoolWheel();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ClanRankList> clanranklist = new mkdb.TTable<Integer, xbean.ClanRankList>() {
		@Override
		public String getName() {
			return "clanranklist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ClanRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanRankList value = xbean.Pod.newClanRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanRankList newValue() {
			xbean.ClanRankList value = xbean.Pod.newClanRankList();
			return value;
		}

	};

	mkdb.TTable<Long, Long> orderidtab = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "orderidtab";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.SingleInvitings> singleinviting = new mkdb.TTable<Long, xbean.SingleInvitings>() {
		@Override
		public String getName() {
			return "singleinviting";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.SingleInvitings value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.SingleInvitings unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.SingleInvitings value = xbean.Pod.newSingleInvitings();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.SingleInvitings newValue() {
			xbean.SingleInvitings value = xbean.Pod.newSingleInvitings();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleRedPackRecord> roleredpackrecordtab = new mkdb.TTable<Long, xbean.RoleRedPackRecord>() {
		@Override
		public String getName() {
			return "roleredpackrecordtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleRedPackRecord value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleRedPackRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleRedPackRecord value = xbean.Pod.newRoleRedPackRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleRedPackRecord newValue() {
			xbean.RoleRedPackRecord value = xbean.Pod.newRoleRedPackRecord();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RedPackRankList> redpacklist = new mkdb.TTable<Integer, xbean.RedPackRankList>() {
		@Override
		public String getName() {
			return "redpacklist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RedPackRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RedPackRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RedPackRankList value = xbean.Pod.newRedPackRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RedPackRankList newValue() {
			xbean.RedPackRankList value = xbean.Pod.newRedPackRankList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ServiceSet> servicesets = new mkdb.TTable<Integer, xbean.ServiceSet>() {
		@Override
		public String getName() {
			return "servicesets";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ServiceSet value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ServiceSet unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ServiceSet value = xbean.Pod.newServiceSet();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ServiceSet newValue() {
			xbean.ServiceSet value = xbean.Pod.newServiceSet();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.DiscardItem> itemrecyclebin = new mkdb.TTable<Long, xbean.DiscardItem>() {
		@Override
		public String getName() {
			return "itemrecyclebin";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.DiscardItem value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.DiscardItem unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.DiscardItem value = xbean.Pod.newDiscardItem();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.DiscardItem newValue() {
			xbean.DiscardItem value = xbean.Pod.newDiscardItem();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ERole> erole = new mkdb.TTable<Long, xbean.ERole>() {
		@Override
		public String getName() {
			return "erole";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ERole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ERole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ERole value = xbean.Pod.newERole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ERole newValue() {
			xbean.ERole value = xbean.Pod.newERole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.MarketTradeLog> markettradelogtab = new mkdb.TTable<Long, xbean.MarketTradeLog>() {
		@Override
		public String getName() {
			return "markettradelogtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.MarketTradeLog value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.MarketTradeLog unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.MarketTradeLog value = xbean.Pod.newMarketTradeLog();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.MarketTradeLog newValue() {
			xbean.MarketTradeLog value = xbean.Pod.newMarketTradeLog();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ClanFightRaceRankList> clanfightracelist = new mkdb.TTable<Long, xbean.ClanFightRaceRankList>() {
		@Override
		public String getName() {
			return "clanfightracelist";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanFightRaceRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ClanFightRaceRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanFightRaceRankList value = xbean.Pod.newClanFightRaceRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanFightRaceRankList newValue() {
			xbean.ClanFightRaceRankList value = xbean.Pod.newClanFightRaceRankList();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.UserDeviceInfo> userdeviceinfotab = new mkdb.TTable<Integer, xbean.UserDeviceInfo>() {
		@Override
		public String getName() {
			return "userdeviceinfotab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.UserDeviceInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.UserDeviceInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UserDeviceInfo value = xbean.Pod.newUserDeviceInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.UserDeviceInfo newValue() {
			xbean.UserDeviceInfo value = xbean.Pod.newUserDeviceInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.OnetimeEvent> onetimeeventtab = new mkdb.TTable<Long, xbean.OnetimeEvent>() {
		@Override
		public String getName() {
			return "onetimeeventtab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.OnetimeEvent value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.OnetimeEvent unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.OnetimeEvent value = xbean.Pod.newOnetimeEvent();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.OnetimeEvent newValue() {
			xbean.OnetimeEvent value = xbean.Pod.newOnetimeEvent();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.NewPlayActiveWeek> rolesplayactiveweek = new mkdb.TTable<Long, xbean.NewPlayActiveWeek>() {
		@Override
		public String getName() {
			return "rolesplayactiveweek";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NewPlayActiveWeek value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.NewPlayActiveWeek unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NewPlayActiveWeek value = xbean.Pod.newNewPlayActiveWeek();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NewPlayActiveWeek newValue() {
			xbean.NewPlayActiveWeek value = xbean.Pod.newNewPlayActiveWeek();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.FestivalData> rolesfestivaldata = new mkdb.TTable<Long, xbean.FestivalData>() {
		@Override
		public String getName() {
			return "rolesfestivaldata";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.FestivalData value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.FestivalData unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.FestivalData value = xbean.Pod.newFestivalData();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.FestivalData newValue() {
			xbean.FestivalData value = xbean.Pod.newFestivalData();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.FriendGroups> friends = new mkdb.TTable<Long, xbean.FriendGroups>() {
		@Override
		public String getName() {
			return "friends";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.FriendGroups value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.FriendGroups unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.FriendGroups value = xbean.Pod.newFriendGroups();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.FriendGroups newValue() {
			xbean.FriendGroups value = xbean.Pod.newFriendGroups();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> market = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "market";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ChargeHistory> chargehistory = new mkdb.TTable<Integer, xbean.ChargeHistory>() {
		@Override
		public String getName() {
			return "chargehistory";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ChargeHistory value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ChargeHistory unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ChargeHistory value = xbean.Pod.newChargeHistory();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ChargeHistory newValue() {
			xbean.ChargeHistory value = xbean.Pod.newChargeHistory();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InviteInfo> teaminvite = new mkdb.TTable<Long, xbean.InviteInfo>() {
		@Override
		public String getName() {
			return "teaminvite";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InviteInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InviteInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InviteInfo value = xbean.Pod.newInviteInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InviteInfo newValue() {
			xbean.InviteInfo value = xbean.Pod.newInviteInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RolePos> roleposes = new mkdb.TTable<Long, xbean.RolePos>() {
		@Override
		public String getName() {
			return "roleposes";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RolePos value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RolePos unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RolePos value = xbean.Pod.newRolePos();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RolePos newValue() {
			xbean.RolePos value = xbean.Pod.newRolePos();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleActiveTimerNpcInfo> roletimernpcinfos = new mkdb.TTable<Long, xbean.RoleActiveTimerNpcInfo>() {
		@Override
		public String getName() {
			return "roletimernpcinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleActiveTimerNpcInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleActiveTimerNpcInfo value = xbean.Pod.newRoleActiveTimerNpcInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo newValue() {
			xbean.RoleActiveTimerNpcInfo value = xbean.Pod.newRoleActiveTimerNpcInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleFuShiRecord> fushirecordtable = new mkdb.TTable<Long, xbean.RoleFuShiRecord>() {
		@Override
		public String getName() {
			return "fushirecordtable";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleFuShiRecord value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleFuShiRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleFuShiRecord value = xbean.Pod.newRoleFuShiRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleFuShiRecord newValue() {
			xbean.RoleFuShiRecord value = xbean.Pod.newRoleFuShiRecord();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InstanceZone> instancezones = new mkdb.TTable<Long, xbean.InstanceZone>() {
		@Override
		public String getName() {
			return "instancezones";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InstanceZone value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InstanceZone unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InstanceZone value = xbean.Pod.newInstanceZone();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InstanceZone newValue() {
			xbean.InstanceZone value = xbean.Pod.newInstanceZone();
			return value;
		}

	};

	mkdb.TTable<Long, Long> livedie2key = new mkdb.TTable<Long, Long>() {
		@Override
		public String getName() {
			return "livedie2key";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Long value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Long unmarshalValue(OctetsStream _os_) throws MarshalException {
			long value = 0;
			value = _os_.unmarshal_long();
			return value;
		}

		@Override
		public Long newValue() {
			long value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleApplyClanList> roleapplyclantab = new mkdb.TTable<Long, xbean.RoleApplyClanList>() {
		@Override
		public String getName() {
			return "roleapplyclantab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleApplyClanList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleApplyClanList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleApplyClanList value = xbean.Pod.newRoleApplyClanList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleApplyClanList newValue() {
			xbean.RoleApplyClanList value = xbean.Pod.newRoleApplyClanList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.WeekGiveReceGift> roleid2weekgiverecegift = new mkdb.TTable<Long, xbean.WeekGiveReceGift>() {
		@Override
		public String getName() {
			return "roleid2weekgiverecegift";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.WeekGiveReceGift value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.WeekGiveReceGift unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.WeekGiveReceGift value = xbean.Pod.newWeekGiveReceGift();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.WeekGiveReceGift newValue() {
			xbean.WeekGiveReceGift value = xbean.Pod.newWeekGiveReceGift();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RolePingBiRolesInfo> rolepingbirolesinfos = new mkdb.TTable<Long, xbean.RolePingBiRolesInfo>() {
		@Override
		public String getName() {
			return "rolepingbirolesinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RolePingBiRolesInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RolePingBiRolesInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RolePingBiRolesInfo value = xbean.Pod.newRolePingBiRolesInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RolePingBiRolesInfo newValue() {
			xbean.RolePingBiRolesInfo value = xbean.Pod.newRolePingBiRolesInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RedPackRoleInfoList> redpackroleinfolisttab = new mkdb.TTable<Long, xbean.RedPackRoleInfoList>() {
		@Override
		public String getName() {
			return "redpackroleinfolisttab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RedPackRoleInfoList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RedPackRoleInfoList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RedPackRoleInfoList value = xbean.Pod.newRedPackRoleInfoList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RedPackRoleInfoList newValue() {
			xbean.RedPackRoleInfoList value = xbean.Pod.newRedPackRoleInfoList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ProgressBarInfo> progressbartable = new mkdb.TTable<Long, xbean.ProgressBarInfo>() {
		@Override
		public String getName() {
			return "progressbartable";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ProgressBarInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ProgressBarInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ProgressBarInfo value = xbean.Pod.newProgressBarInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ProgressBarInfo newValue() {
			xbean.ProgressBarInfo value = xbean.Pod.newProgressBarInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.WinnerRoleRecord> winnerscore = new mkdb.TTable<Long, xbean.WinnerRoleRecord>() {
		@Override
		public String getName() {
			return "winnerscore";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.WinnerRoleRecord value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.WinnerRoleRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.WinnerRoleRecord value = xbean.Pod.newWinnerRoleRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.WinnerRoleRecord newValue() {
			xbean.WinnerRoleRecord value = xbean.Pod.newWinnerRoleRecord();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.RandomNameInfo> randomnametab = new mkdb.TTable<Integer, xbean.RandomNameInfo>() {
		@Override
		public String getName() {
			return "randomnametab";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RandomNameInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.RandomNameInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RandomNameInfo value = xbean.Pod.newRandomNameInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RandomNameInfo newValue() {
			xbean.RandomNameInfo value = xbean.Pod.newRandomNameInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ClanProgressRankList> clanprogressranklist = new mkdb.TTable<Integer, xbean.ClanProgressRankList>() {
		@Override
		public String getName() {
			return "clanprogressranklist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ClanProgressRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ClanProgressRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ClanProgressRankList value = xbean.Pod.newClanProgressRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ClanProgressRankList newValue() {
			xbean.ClanProgressRankList value = xbean.Pod.newClanProgressRankList();
			return value;
		}

	};

	mkdb.TTable<Integer, Integer> tablekeynum = new mkdb.TTable<Integer, Integer>() {
		@Override
		public String getName() {
			return "tablekeynum";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.InvitePeopleInfo> invitepeopleinfo = new mkdb.TTable<Long, xbean.InvitePeopleInfo>() {
		@Override
		public String getName() {
			return "invitepeopleinfo";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.InvitePeopleInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.InvitePeopleInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.InvitePeopleInfo value = xbean.Pod.newInvitePeopleInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.InvitePeopleInfo newValue() {
			xbean.InvitePeopleInfo value = xbean.Pod.newInvitePeopleInfo();
			return value;
		}

	};

	mkdb.TTable<Long, Boolean> rolelockscreen = new mkdb.TTable<Long, Boolean>() {
		@Override
		public String getName() {
			return "rolelockscreen";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Boolean value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Boolean unmarshalValue(OctetsStream _os_) throws MarshalException {
			boolean value = false;
			value = _os_.unmarshal_boolean();
			return value;
		}

		@Override
		public Boolean newValue() {
			boolean value = false;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.SkillRole> skillroles = new mkdb.TTable<Long, xbean.SkillRole>() {
		@Override
		public String getName() {
			return "skillroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.SkillRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.SkillRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.SkillRole value = xbean.Pod.newSkillRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.SkillRole newValue() {
			xbean.SkillRole value = xbean.Pod.newSkillRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.EClanFightStatistics> roleid2clanfightstatistics = new mkdb.TTable<Long, xbean.EClanFightStatistics>() {
		@Override
		public String getName() {
			return "roleid2clanfightstatistics";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EClanFightStatistics value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.EClanFightStatistics unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EClanFightStatistics value = xbean.Pod.newEClanFightStatistics();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EClanFightStatistics newValue() {
			xbean.EClanFightStatistics value = xbean.Pod.newEClanFightStatistics();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleLockInfo> rolesafelock = new mkdb.TTable<Long, xbean.RoleLockInfo>() {
		@Override
		public String getName() {
			return "rolesafelock";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleLockInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleLockInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleLockInfo value = xbean.Pod.newRoleLockInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleLockInfo newValue() {
			xbean.RoleLockInfo value = xbean.Pod.newRoleLockInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.UniquePet> uniquepets = new mkdb.TTable<Long, xbean.UniquePet>() {
		@Override
		public String getName() {
			return "uniquepets";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.UniquePet value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.UniquePet unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UniquePet value = xbean.Pod.newUniquePet();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.UniquePet newValue() {
			xbean.UniquePet value = xbean.Pod.newUniquePet();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.PetScoreRankList> petscorelist = new mkdb.TTable<Integer, xbean.PetScoreRankList>() {
		@Override
		public String getName() {
			return "petscorelist";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PetScoreRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.PetScoreRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PetScoreRankList value = xbean.Pod.newPetScoreRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PetScoreRankList newValue() {
			xbean.PetScoreRankList value = xbean.Pod.newPetScoreRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.CircleTaskMap> rolecircletask = new mkdb.TTable<Long, xbean.CircleTaskMap>() {
		@Override
		public String getName() {
			return "rolecircletask";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.CircleTaskMap value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.CircleTaskMap unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.CircleTaskMap value = xbean.Pod.newCircleTaskMap();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.CircleTaskMap newValue() {
			xbean.CircleTaskMap value = xbean.Pod.newCircleTaskMap();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ModifyNameRole> modnameitemroles = new mkdb.TTable<Long, xbean.ModifyNameRole>() {
		@Override
		public String getName() {
			return "modnameitemroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ModifyNameRole value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ModifyNameRole unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ModifyNameRole value = xbean.Pod.newModifyNameRole();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ModifyNameRole newValue() {
			xbean.ModifyNameRole value = xbean.Pod.newModifyNameRole();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ChargeOrder> failedchargeorder = new mkdb.TTable<Long, xbean.ChargeOrder>() {
		@Override
		public String getName() {
			return "failedchargeorder";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ChargeOrder value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ChargeOrder unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ChargeOrder value = xbean.Pod.newChargeOrder();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ChargeOrder newValue() {
			xbean.ChargeOrder value = xbean.Pod.newChargeOrder();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TrackedMission> trackedmission = new mkdb.TTable<Long, xbean.TrackedMission>() {
		@Override
		public String getName() {
			return "trackedmission";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TrackedMission value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TrackedMission unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TrackedMission value = xbean.Pod.newTrackedMission();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TrackedMission newValue() {
			xbean.TrackedMission value = xbean.Pod.newTrackedMission();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleClanPoint> roleclanpoint = new mkdb.TTable<Long, xbean.RoleClanPoint>() {
		@Override
		public String getName() {
			return "roleclanpoint";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleClanPoint value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleClanPoint unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleClanPoint value = xbean.Pod.newRoleClanPoint();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleClanPoint newValue() {
			xbean.RoleClanPoint value = xbean.Pod.newRoleClanPoint();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.OfflineMsgList> offlinemsg = new mkdb.TTable<Long, xbean.OfflineMsgList>() {
		@Override
		public String getName() {
			return "offlinemsg";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.OfflineMsgList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.OfflineMsgList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.OfflineMsgList value = xbean.Pod.newOfflineMsgList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.OfflineMsgList newValue() {
			xbean.OfflineMsgList value = xbean.Pod.newOfflineMsgList();
			return value;
		}

	};

	mkdb.TTable<Long, Integer> npcfightnum = new mkdb.TTable<Long, Integer>() {
		@Override
		public String getName() {
			return "npcfightnum";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> temp = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "temp";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.PvP3Role> pvp3roles = new mkdb.TTable<Long, xbean.PvP3Role>() {
		@Override
		public String getName() {
			return "pvp3roles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.PvP3Role value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.PvP3Role unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.PvP3Role value = xbean.Pod.newPvP3Role();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.PvP3Role newValue() {
			xbean.PvP3Role value = xbean.Pod.newPvP3Role();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.helpcount> helpcount = new mkdb.TTable<Long, xbean.helpcount>() {
		@Override
		public String getName() {
			return "helpcount";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.helpcount value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.helpcount unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.helpcount value = xbean.Pod.newhelpcount();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.helpcount newValue() {
			xbean.helpcount value = xbean.Pod.newhelpcount();
			return value;
		}

	};

	mkdb.TTable<xbean.ItemRecycleDate, xbean.ItemRecycleIndex> itemrecycleidx = new mkdb.TTable<xbean.ItemRecycleDate, xbean.ItemRecycleIndex>() {
		@Override
		public String getName() {
			return "itemrecycleidx";
		}

		@Override
		public OctetsStream marshalKey(xbean.ItemRecycleDate key) {
			OctetsStream _os_ = new OctetsStream();
			key.marshal(_os_);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ItemRecycleIndex value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public xbean.ItemRecycleDate unmarshalKey(OctetsStream _os_) throws MarshalException {
			xbean.ItemRecycleDate key = new xbean.ItemRecycleDate();
			key.unmarshal(_os_);
			return key;
		}

		@Override
		public xbean.ItemRecycleIndex unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ItemRecycleIndex value = xbean.Pod.newItemRecycleIndex();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ItemRecycleIndex newValue() {
			xbean.ItemRecycleIndex value = xbean.Pod.newItemRecycleIndex();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TransfromInfo> transfromroles = new mkdb.TTable<Long, xbean.TransfromInfo>() {
		@Override
		public String getName() {
			return "transfromroles";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TransfromInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TransfromInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TransfromInfo value = xbean.Pod.newTransfromInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TransfromInfo newValue() {
			xbean.TransfromInfo value = xbean.Pod.newTransfromInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ImpExamStateRecord> impexamstatetbl = new mkdb.TTable<Integer, xbean.ImpExamStateRecord>() {
		@Override
		public String getName() {
			return "impexamstatetbl";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ImpExamStateRecord value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ImpExamStateRecord unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ImpExamStateRecord value = xbean.Pod.newImpExamStateRecord();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ImpExamStateRecord newValue() {
			xbean.ImpExamStateRecord value = xbean.Pod.newImpExamStateRecord();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.EDayPay> roledaypay = new mkdb.TTable<Integer, xbean.EDayPay>() {
		@Override
		public String getName() {
			return "roledaypay";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.EDayPay value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.EDayPay unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.EDayPay value = xbean.Pod.newEDayPay();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.EDayPay newValue() {
			xbean.EDayPay value = xbean.Pod.newEDayPay();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Vipinfo> vipinfo = new mkdb.TTable<Long, xbean.Vipinfo>() {
		@Override
		public String getName() {
			return "vipinfo";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Vipinfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Vipinfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Vipinfo value = xbean.Pod.newVipinfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Vipinfo newValue() {
			xbean.Vipinfo value = xbean.Pod.newVipinfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.npcBattleInfoCol> npcbattleinfo = new mkdb.TTable<Long, xbean.npcBattleInfoCol>() {
		@Override
		public String getName() {
			return "npcbattleinfo";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.npcBattleInfoCol value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.npcBattleInfoCol unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.npcBattleInfoCol value = xbean.Pod.newnpcBattleInfoCol();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.npcBattleInfoCol newValue() {
			xbean.npcBattleInfoCol value = xbean.Pod.newnpcBattleInfoCol();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.ServiceInfo> serviceinfos = new mkdb.TTable<Integer, xbean.ServiceInfo>() {
		@Override
		public String getName() {
			return "serviceinfos";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ServiceInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.ServiceInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ServiceInfo value = xbean.Pod.newServiceInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ServiceInfo newValue() {
			xbean.ServiceInfo value = xbean.Pod.newServiceInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.ReceFlowerRankList> receflowerlist = new mkdb.TTable<Long, xbean.ReceFlowerRankList>() {
		@Override
		public String getName() {
			return "receflowerlist";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.ReceFlowerRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.ReceFlowerRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.ReceFlowerRankList value = xbean.Pod.newReceFlowerRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.ReceFlowerRankList newValue() {
			xbean.ReceFlowerRankList value = xbean.Pod.newReceFlowerRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.SRRedPackNumList> srredpacknumlisttab = new mkdb.TTable<Long, xbean.SRRedPackNumList>() {
		@Override
		public String getName() {
			return "srredpacknumlisttab";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.SRRedPackNumList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.SRRedPackNumList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.SRRedPackNumList value = xbean.Pod.newSRRedPackNumList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.SRRedPackNumList newValue() {
			xbean.SRRedPackNumList value = xbean.Pod.newSRRedPackNumList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.WheelInfos> wheelprogress = new mkdb.TTable<Long, xbean.WheelInfos>() {
		@Override
		public String getName() {
			return "wheelprogress";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.WheelInfos value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.WheelInfos unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.WheelInfos value = xbean.Pod.newWheelInfos();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.WheelInfos newValue() {
			xbean.WheelInfos value = xbean.Pod.newWheelInfos();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Bag> equip = new mkdb.TTable<Long, xbean.Bag>() {
		@Override
		public String getName() {
			return "equip";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Bag value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Bag unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Bag value = xbean.Pod.newBag();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Bag newValue() {
			xbean.Bag value = xbean.Pod.newBag();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.RoleBestowInfo> rolebestow = new mkdb.TTable<Long, xbean.RoleBestowInfo>() {
		@Override
		public String getName() {
			return "rolebestow";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.RoleBestowInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.RoleBestowInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.RoleBestowInfo value = xbean.Pod.newRoleBestowInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.RoleBestowInfo newValue() {
			xbean.RoleBestowInfo value = xbean.Pod.newRoleBestowInfo();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.GiveFlowerRankList> giveflowerlist = new mkdb.TTable<Long, xbean.GiveFlowerRankList>() {
		@Override
		public String getName() {
			return "giveflowerlist";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.GiveFlowerRankList value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.GiveFlowerRankList unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.GiveFlowerRankList value = xbean.Pod.newGiveFlowerRankList();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.GiveFlowerRankList newValue() {
			xbean.GiveFlowerRankList value = xbean.Pod.newGiveFlowerRankList();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.AccusationInfo> accusationinfos = new mkdb.TTable<Long, xbean.AccusationInfo>() {
		@Override
		public String getName() {
			return "accusationinfos";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AccusationInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.AccusationInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AccusationInfo value = xbean.Pod.newAccusationInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AccusationInfo newValue() {
			xbean.AccusationInfo value = xbean.Pod.newAccusationInfo();
			return value;
		}

	};

	mkdb.TTable<Integer, xbean.User> user = new mkdb.TTable<Integer, xbean.User>() {
		@Override
		public String getName() {
			return "user";
		}

		@Override
		public OctetsStream marshalKey(Integer key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.User value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Integer unmarshalKey(OctetsStream _os_) throws MarshalException {
			int key = 0;
			key = _os_.unmarshal_int();
			return key;
		}

		@Override
		public xbean.User unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.User value = xbean.Pod.newUser();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.User newValue() {
			xbean.User value = xbean.Pod.newUser();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.Properties> properties = new mkdb.TTable<Long, xbean.Properties>() {
		@Override
		public String getName() {
			return "properties";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.Properties value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.Properties unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.Properties value = xbean.Pod.newProperties();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.Properties newValue() {
			xbean.Properties value = xbean.Pod.newProperties();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.TaskInfos> roletasks = new mkdb.TTable<Long, xbean.TaskInfos>() {
		@Override
		public String getName() {
			return "roletasks";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.TaskInfos value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.TaskInfos unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.TaskInfos value = xbean.Pod.newTaskInfos();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.TaskInfos newValue() {
			xbean.TaskInfos value = xbean.Pod.newTaskInfos();
			return value;
		}

	};

	mkdb.TTable<Long, xbean.AppstoretidStatus> appstoretidstatus = new mkdb.TTable<Long, xbean.AppstoretidStatus>() {
		@Override
		public String getName() {
			return "appstoretidstatus";
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.AppstoretidStatus value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.AppstoretidStatus unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.AppstoretidStatus value = xbean.Pod.newAppstoretidStatus();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.AppstoretidStatus newValue() {
			xbean.AppstoretidStatus value = xbean.Pod.newAppstoretidStatus();
			return value;
		}

	};


}

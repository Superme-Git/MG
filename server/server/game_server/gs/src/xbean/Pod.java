
package xbean;

/**
 * bean factory
 */
public final class Pod {
	public static mkdb.util.BeanPool<Item> poolItem = new mkdb.util.BeanPool<Item>() {
		@Override
		protected Item newBean() {
			return new xbean.__.Item();
		}
	};

	public static Item newItem() {
		return poolItem.get();
	}

	public static void _reset_unsafe_add_(Item bean) {
		poolItem._reset_unsafe_add_(bean);
	}

	public static void padd(Item bean) {
		mkdb.Procedure.padd(bean, poolItem);
	}

	public static Item newItemData() {
		return new xbean.__.Item.Data();
	}

	public static mkdb.util.BeanPool<Bag> poolBag = new mkdb.util.BeanPool<Bag>() {
		@Override
		protected Bag newBean() {
			return new xbean.__.Bag();
		}
	};

	public static Bag newBag() {
		return poolBag.get();
	}

	public static void _reset_unsafe_add_(Bag bean) {
		poolBag._reset_unsafe_add_(bean);
	}

	public static void padd(Bag bean) {
		mkdb.Procedure.padd(bean, poolBag);
	}

	public static Bag newBagData() {
		return new xbean.__.Bag.Data();
	}

	public static mkdb.util.BeanPool<BagTimeLock> poolBagTimeLock = new mkdb.util.BeanPool<BagTimeLock>() {
		@Override
		protected BagTimeLock newBean() {
			return new xbean.__.BagTimeLock();
		}
	};

	public static BagTimeLock newBagTimeLock() {
		return poolBagTimeLock.get();
	}

	public static void _reset_unsafe_add_(BagTimeLock bean) {
		poolBagTimeLock._reset_unsafe_add_(bean);
	}

	public static void padd(BagTimeLock bean) {
		mkdb.Procedure.padd(bean, poolBagTimeLock);
	}

	public static BagTimeLock newBagTimeLockData() {
		return new xbean.__.BagTimeLock.Data();
	}

	public static mkdb.util.BeanPool<DiscardItem> poolDiscardItem = new mkdb.util.BeanPool<DiscardItem>() {
		@Override
		protected DiscardItem newBean() {
			return new xbean.__.DiscardItem();
		}
	};

	public static DiscardItem newDiscardItem() {
		return poolDiscardItem.get();
	}

	public static void _reset_unsafe_add_(DiscardItem bean) {
		poolDiscardItem._reset_unsafe_add_(bean);
	}

	public static void padd(DiscardItem bean) {
		mkdb.Procedure.padd(bean, poolDiscardItem);
	}

	public static DiscardItem newDiscardItemData() {
		return new xbean.__.DiscardItem.Data();
	}

	public static mkdb.util.BeanPool<DiscardPet> poolDiscardPet = new mkdb.util.BeanPool<DiscardPet>() {
		@Override
		protected DiscardPet newBean() {
			return new xbean.__.DiscardPet();
		}
	};

	public static DiscardPet newDiscardPet() {
		return poolDiscardPet.get();
	}

	public static void _reset_unsafe_add_(DiscardPet bean) {
		poolDiscardPet._reset_unsafe_add_(bean);
	}

	public static void padd(DiscardPet bean) {
		mkdb.Procedure.padd(bean, poolDiscardPet);
	}

	public static DiscardPet newDiscardPetData() {
		return new xbean.__.DiscardPet.Data();
	}

	public static mkdb.util.BeanPool<UniquePet> poolUniquePet = new mkdb.util.BeanPool<UniquePet>() {
		@Override
		protected UniquePet newBean() {
			return new xbean.__.UniquePet();
		}
	};

	public static UniquePet newUniquePet() {
		return poolUniquePet.get();
	}

	public static void _reset_unsafe_add_(UniquePet bean) {
		poolUniquePet._reset_unsafe_add_(bean);
	}

	public static void padd(UniquePet bean) {
		mkdb.Procedure.padd(bean, poolUniquePet);
	}

	public static UniquePet newUniquePetData() {
		return new xbean.__.UniquePet.Data();
	}

	public static mkdb.util.BeanPool<Itemrecoverlist> poolItemrecoverlist = new mkdb.util.BeanPool<Itemrecoverlist>() {
		@Override
		protected Itemrecoverlist newBean() {
			return new xbean.__.Itemrecoverlist();
		}
	};

	public static Itemrecoverlist newItemrecoverlist() {
		return poolItemrecoverlist.get();
	}

	public static void _reset_unsafe_add_(Itemrecoverlist bean) {
		poolItemrecoverlist._reset_unsafe_add_(bean);
	}

	public static void padd(Itemrecoverlist bean) {
		mkdb.Procedure.padd(bean, poolItemrecoverlist);
	}

	public static Itemrecoverlist newItemrecoverlistData() {
		return new xbean.__.Itemrecoverlist.Data();
	}

	public static mkdb.util.BeanPool<Petrecoverlist> poolPetrecoverlist = new mkdb.util.BeanPool<Petrecoverlist>() {
		@Override
		protected Petrecoverlist newBean() {
			return new xbean.__.Petrecoverlist();
		}
	};

	public static Petrecoverlist newPetrecoverlist() {
		return poolPetrecoverlist.get();
	}

	public static void _reset_unsafe_add_(Petrecoverlist bean) {
		poolPetrecoverlist._reset_unsafe_add_(bean);
	}

	public static void padd(Petrecoverlist bean) {
		mkdb.Procedure.padd(bean, poolPetrecoverlist);
	}

	public static Petrecoverlist newPetrecoverlistData() {
		return new xbean.__.Petrecoverlist.Data();
	}

	public static mkdb.util.BeanPool<User> poolUser = new mkdb.util.BeanPool<User>() {
		@Override
		protected User newBean() {
			return new xbean.__.User();
		}
	};

	public static User newUser() {
		return poolUser.get();
	}

	public static void _reset_unsafe_add_(User bean) {
		poolUser._reset_unsafe_add_(bean);
	}

	public static void padd(User bean) {
		mkdb.Procedure.padd(bean, poolUser);
	}

	public static User newUserData() {
		return new xbean.__.User.Data();
	}

	public static mkdb.util.BeanPool<UserDeviceInfo> poolUserDeviceInfo = new mkdb.util.BeanPool<UserDeviceInfo>() {
		@Override
		protected UserDeviceInfo newBean() {
			return new xbean.__.UserDeviceInfo();
		}
	};

	public static UserDeviceInfo newUserDeviceInfo() {
		return poolUserDeviceInfo.get();
	}

	public static void _reset_unsafe_add_(UserDeviceInfo bean) {
		poolUserDeviceInfo._reset_unsafe_add_(bean);
	}

	public static void padd(UserDeviceInfo bean) {
		mkdb.Procedure.padd(bean, poolUserDeviceInfo);
	}

	public static UserDeviceInfo newUserDeviceInfoData() {
		return new xbean.__.UserDeviceInfo.Data();
	}

	public static mkdb.util.BeanPool<AUUserInfo> poolAUUserInfo = new mkdb.util.BeanPool<AUUserInfo>() {
		@Override
		protected AUUserInfo newBean() {
			return new xbean.__.AUUserInfo();
		}
	};

	public static AUUserInfo newAUUserInfo() {
		return poolAUUserInfo.get();
	}

	public static void _reset_unsafe_add_(AUUserInfo bean) {
		poolAUUserInfo._reset_unsafe_add_(bean);
	}

	public static void padd(AUUserInfo bean) {
		mkdb.Procedure.padd(bean, poolAUUserInfo);
	}

	public static AUUserInfo newAUUserInfoData() {
		return new xbean.__.AUUserInfo.Data();
	}

	public static mkdb.util.BeanPool<YingYongBao> poolYingYongBao = new mkdb.util.BeanPool<YingYongBao>() {
		@Override
		protected YingYongBao newBean() {
			return new xbean.__.YingYongBao();
		}
	};

	public static YingYongBao newYingYongBao() {
		return poolYingYongBao.get();
	}

	public static void _reset_unsafe_add_(YingYongBao bean) {
		poolYingYongBao._reset_unsafe_add_(bean);
	}

	public static void padd(YingYongBao bean) {
		mkdb.Procedure.padd(bean, poolYingYongBao);
	}

	public static YingYongBao newYingYongBaoData() {
		return new xbean.__.YingYongBao.Data();
	}

	public static mkdb.util.BeanPool<YybFushiNum> poolYybFushiNum = new mkdb.util.BeanPool<YybFushiNum>() {
		@Override
		protected YybFushiNum newBean() {
			return new xbean.__.YybFushiNum();
		}
	};

	public static YybFushiNum newYybFushiNum() {
		return poolYybFushiNum.get();
	}

	public static void _reset_unsafe_add_(YybFushiNum bean) {
		poolYybFushiNum._reset_unsafe_add_(bean);
	}

	public static void padd(YybFushiNum bean) {
		mkdb.Procedure.padd(bean, poolYybFushiNum);
	}

	public static YybFushiNum newYybFushiNumData() {
		return new xbean.__.YybFushiNum.Data();
	}

	public static mkdb.util.BeanPool<YybFushiNums> poolYybFushiNums = new mkdb.util.BeanPool<YybFushiNums>() {
		@Override
		protected YybFushiNums newBean() {
			return new xbean.__.YybFushiNums();
		}
	};

	public static YybFushiNums newYybFushiNums() {
		return poolYybFushiNums.get();
	}

	public static void _reset_unsafe_add_(YybFushiNums bean) {
		poolYybFushiNums._reset_unsafe_add_(bean);
	}

	public static void padd(YybFushiNums bean) {
		mkdb.Procedure.padd(bean, poolYybFushiNums);
	}

	public static YybFushiNums newYybFushiNumsData() {
		return new xbean.__.YybFushiNums.Data();
	}

	public static mkdb.util.BeanPool<YybOrder> poolYybOrder = new mkdb.util.BeanPool<YybOrder>() {
		@Override
		protected YybOrder newBean() {
			return new xbean.__.YybOrder();
		}
	};

	public static YybOrder newYybOrder() {
		return poolYybOrder.get();
	}

	public static void _reset_unsafe_add_(YybOrder bean) {
		poolYybOrder._reset_unsafe_add_(bean);
	}

	public static void padd(YybOrder bean) {
		mkdb.Procedure.padd(bean, poolYybOrder);
	}

	public static YybOrder newYybOrderData() {
		return new xbean.__.YybOrder.Data();
	}

	public static mkdb.util.BeanPool<TitleInfo> poolTitleInfo = new mkdb.util.BeanPool<TitleInfo>() {
		@Override
		protected TitleInfo newBean() {
			return new xbean.__.TitleInfo();
		}
	};

	public static TitleInfo newTitleInfo() {
		return poolTitleInfo.get();
	}

	public static void _reset_unsafe_add_(TitleInfo bean) {
		poolTitleInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TitleInfo bean) {
		mkdb.Procedure.padd(bean, poolTitleInfo);
	}

	public static TitleInfo newTitleInfoData() {
		return new xbean.__.TitleInfo.Data();
	}

	public static mkdb.util.BeanPool<BasicFightProperties> poolBasicFightProperties = new mkdb.util.BeanPool<BasicFightProperties>() {
		@Override
		protected BasicFightProperties newBean() {
			return new xbean.__.BasicFightProperties();
		}
	};

	public static BasicFightProperties newBasicFightProperties() {
		return poolBasicFightProperties.get();
	}

	public static void _reset_unsafe_add_(BasicFightProperties bean) {
		poolBasicFightProperties._reset_unsafe_add_(bean);
	}

	public static void padd(BasicFightProperties bean) {
		mkdb.Procedure.padd(bean, poolBasicFightProperties);
	}

	public static BasicFightProperties newBasicFightPropertiesData() {
		return new xbean.__.BasicFightProperties.Data();
	}

	public static mkdb.util.BeanPool<RoleAddPointProperties> poolRoleAddPointProperties = new mkdb.util.BeanPool<RoleAddPointProperties>() {
		@Override
		protected RoleAddPointProperties newBean() {
			return new xbean.__.RoleAddPointProperties();
		}
	};

	public static RoleAddPointProperties newRoleAddPointProperties() {
		return poolRoleAddPointProperties.get();
	}

	public static void _reset_unsafe_add_(RoleAddPointProperties bean) {
		poolRoleAddPointProperties._reset_unsafe_add_(bean);
	}

	public static void padd(RoleAddPointProperties bean) {
		mkdb.Procedure.padd(bean, poolRoleAddPointProperties);
	}

	public static RoleAddPointProperties newRoleAddPointPropertiesData() {
		return new xbean.__.RoleAddPointProperties.Data();
	}

	public static mkdb.util.BeanPool<FormBean> poolFormBean = new mkdb.util.BeanPool<FormBean>() {
		@Override
		protected FormBean newBean() {
			return new xbean.__.FormBean();
		}
	};

	public static FormBean newFormBean() {
		return poolFormBean.get();
	}

	public static void _reset_unsafe_add_(FormBean bean) {
		poolFormBean._reset_unsafe_add_(bean);
	}

	public static void padd(FormBean bean) {
		mkdb.Procedure.padd(bean, poolFormBean);
	}

	public static FormBean newFormBeanData() {
		return new xbean.__.FormBean.Data();
	}

	public static mkdb.util.BeanPool<RoleColorType> poolRoleColorType = new mkdb.util.BeanPool<RoleColorType>() {
		@Override
		protected RoleColorType newBean() {
			return new xbean.__.RoleColorType();
		}
	};

	public static RoleColorType newRoleColorType() {
		return poolRoleColorType.get();
	}

	public static void _reset_unsafe_add_(RoleColorType bean) {
		poolRoleColorType._reset_unsafe_add_(bean);
	}

	public static void padd(RoleColorType bean) {
		mkdb.Procedure.padd(bean, poolRoleColorType);
	}

	public static RoleColorType newRoleColorTypeData() {
		return new xbean.__.RoleColorType.Data();
	}

	public static mkdb.util.BeanPool<Properties> poolProperties = new mkdb.util.BeanPool<Properties>() {
		@Override
		protected Properties newBean() {
			return new xbean.__.Properties();
		}
	};

	public static Properties newProperties() {
		return poolProperties.get();
	}

	public static void _reset_unsafe_add_(Properties bean) {
		poolProperties._reset_unsafe_add_(bean);
	}

	public static void padd(Properties bean) {
		mkdb.Procedure.padd(bean, poolProperties);
	}

	public static Properties newPropertiesData() {
		return new xbean.__.Properties.Data();
	}

	public static mkdb.util.BeanPool<ChangeSchoolRecord> poolChangeSchoolRecord = new mkdb.util.BeanPool<ChangeSchoolRecord>() {
		@Override
		protected ChangeSchoolRecord newBean() {
			return new xbean.__.ChangeSchoolRecord();
		}
	};

	public static ChangeSchoolRecord newChangeSchoolRecord() {
		return poolChangeSchoolRecord.get();
	}

	public static void _reset_unsafe_add_(ChangeSchoolRecord bean) {
		poolChangeSchoolRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ChangeSchoolRecord bean) {
		mkdb.Procedure.padd(bean, poolChangeSchoolRecord);
	}

	public static ChangeSchoolRecord newChangeSchoolRecordData() {
		return new xbean.__.ChangeSchoolRecord.Data();
	}

	public static mkdb.util.BeanPool<ChangeSchoolInfo> poolChangeSchoolInfo = new mkdb.util.BeanPool<ChangeSchoolInfo>() {
		@Override
		protected ChangeSchoolInfo newBean() {
			return new xbean.__.ChangeSchoolInfo();
		}
	};

	public static ChangeSchoolInfo newChangeSchoolInfo() {
		return poolChangeSchoolInfo.get();
	}

	public static void _reset_unsafe_add_(ChangeSchoolInfo bean) {
		poolChangeSchoolInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ChangeSchoolInfo bean) {
		mkdb.Procedure.padd(bean, poolChangeSchoolInfo);
	}

	public static ChangeSchoolInfo newChangeSchoolInfoData() {
		return new xbean.__.ChangeSchoolInfo.Data();
	}

	public static mkdb.util.BeanPool<subscription> poolsubscription = new mkdb.util.BeanPool<subscription>() {
		@Override
		protected subscription newBean() {
			return new xbean.__.subscription();
		}
	};

	public static subscription newsubscription() {
		return poolsubscription.get();
	}

	public static void _reset_unsafe_add_(subscription bean) {
		poolsubscription._reset_unsafe_add_(bean);
	}

	public static void padd(subscription bean) {
		mkdb.Procedure.padd(bean, poolsubscription);
	}

	public static subscription newsubscriptionData() {
		return new xbean.__.subscription.Data();
	}

	public static mkdb.util.BeanPool<CrossTempRole> poolCrossTempRole = new mkdb.util.BeanPool<CrossTempRole>() {
		@Override
		protected CrossTempRole newBean() {
			return new xbean.__.CrossTempRole();
		}
	};

	public static CrossTempRole newCrossTempRole() {
		return poolCrossTempRole.get();
	}

	public static void _reset_unsafe_add_(CrossTempRole bean) {
		poolCrossTempRole._reset_unsafe_add_(bean);
	}

	public static void padd(CrossTempRole bean) {
		mkdb.Procedure.padd(bean, poolCrossTempRole);
	}

	public static CrossTempRole newCrossTempRoleData() {
		return new xbean.__.CrossTempRole.Data();
	}

	public static mkdb.util.BeanPool<crossMessageBean> poolcrossMessageBean = new mkdb.util.BeanPool<crossMessageBean>() {
		@Override
		protected crossMessageBean newBean() {
			return new xbean.__.crossMessageBean();
		}
	};

	public static crossMessageBean newcrossMessageBean() {
		return poolcrossMessageBean.get();
	}

	public static void _reset_unsafe_add_(crossMessageBean bean) {
		poolcrossMessageBean._reset_unsafe_add_(bean);
	}

	public static void padd(crossMessageBean bean) {
		mkdb.Procedure.padd(bean, poolcrossMessageBean);
	}

	public static crossMessageBean newcrossMessageBeanData() {
		return new xbean.__.crossMessageBean.Data();
	}

	public static mkdb.util.BeanPool<helpcount> poolhelpcount = new mkdb.util.BeanPool<helpcount>() {
		@Override
		protected helpcount newBean() {
			return new xbean.__.helpcount();
		}
	};

	public static helpcount newhelpcount() {
		return poolhelpcount.get();
	}

	public static void _reset_unsafe_add_(helpcount bean) {
		poolhelpcount._reset_unsafe_add_(bean);
	}

	public static void padd(helpcount bean) {
		mkdb.Procedure.padd(bean, poolhelpcount);
	}

	public static helpcount newhelpcountData() {
		return new xbean.__.helpcount.Data();
	}

	public static mkdb.util.BeanPool<RoleUpdateTime> poolRoleUpdateTime = new mkdb.util.BeanPool<RoleUpdateTime>() {
		@Override
		protected RoleUpdateTime newBean() {
			return new xbean.__.RoleUpdateTime();
		}
	};

	public static RoleUpdateTime newRoleUpdateTime() {
		return poolRoleUpdateTime.get();
	}

	public static void _reset_unsafe_add_(RoleUpdateTime bean) {
		poolRoleUpdateTime._reset_unsafe_add_(bean);
	}

	public static void padd(RoleUpdateTime bean) {
		mkdb.Procedure.padd(bean, poolRoleUpdateTime);
	}

	public static RoleUpdateTime newRoleUpdateTimeData() {
		return new xbean.__.RoleUpdateTime.Data();
	}

	public static mkdb.util.BeanPool<RoleUpdateModelNum> poolRoleUpdateModelNum = new mkdb.util.BeanPool<RoleUpdateModelNum>() {
		@Override
		protected RoleUpdateModelNum newBean() {
			return new xbean.__.RoleUpdateModelNum();
		}
	};

	public static RoleUpdateModelNum newRoleUpdateModelNum() {
		return poolRoleUpdateModelNum.get();
	}

	public static void _reset_unsafe_add_(RoleUpdateModelNum bean) {
		poolRoleUpdateModelNum._reset_unsafe_add_(bean);
	}

	public static void padd(RoleUpdateModelNum bean) {
		mkdb.Procedure.padd(bean, poolRoleUpdateModelNum);
	}

	public static RoleUpdateModelNum newRoleUpdateModelNumData() {
		return new xbean.__.RoleUpdateModelNum.Data();
	}

	public static mkdb.util.BeanPool<NpcFollowInfo> poolNpcFollowInfo = new mkdb.util.BeanPool<NpcFollowInfo>() {
		@Override
		protected NpcFollowInfo newBean() {
			return new xbean.__.NpcFollowInfo();
		}
	};

	public static NpcFollowInfo newNpcFollowInfo() {
		return poolNpcFollowInfo.get();
	}

	public static void _reset_unsafe_add_(NpcFollowInfo bean) {
		poolNpcFollowInfo._reset_unsafe_add_(bean);
	}

	public static void padd(NpcFollowInfo bean) {
		mkdb.Procedure.padd(bean, poolNpcFollowInfo);
	}

	public static NpcFollowInfo newNpcFollowInfoData() {
		return new xbean.__.NpcFollowInfo.Data();
	}

	public static mkdb.util.BeanPool<TransfromInfo> poolTransfromInfo = new mkdb.util.BeanPool<TransfromInfo>() {
		@Override
		protected TransfromInfo newBean() {
			return new xbean.__.TransfromInfo();
		}
	};

	public static TransfromInfo newTransfromInfo() {
		return poolTransfromInfo.get();
	}

	public static void _reset_unsafe_add_(TransfromInfo bean) {
		poolTransfromInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TransfromInfo bean) {
		mkdb.Procedure.padd(bean, poolTransfromInfo);
	}

	public static TransfromInfo newTransfromInfoData() {
		return new xbean.__.TransfromInfo.Data();
	}

	public static mkdb.util.BeanPool<ChatTime> poolChatTime = new mkdb.util.BeanPool<ChatTime>() {
		@Override
		protected ChatTime newBean() {
			return new xbean.__.ChatTime();
		}
	};

	public static ChatTime newChatTime() {
		return poolChatTime.get();
	}

	public static void _reset_unsafe_add_(ChatTime bean) {
		poolChatTime._reset_unsafe_add_(bean);
	}

	public static void padd(ChatTime bean) {
		mkdb.Procedure.padd(bean, poolChatTime);
	}

	public static ChatTime newChatTimeData() {
		return new xbean.__.ChatTime.Data();
	}

	public static mkdb.util.BeanPool<OfflineFuture> poolOfflineFuture = new mkdb.util.BeanPool<OfflineFuture>() {
		@Override
		protected OfflineFuture newBean() {
			return new xbean.__.OfflineFuture();
		}
	};

	public static OfflineFuture newOfflineFuture() {
		return poolOfflineFuture.get();
	}

	public static void _reset_unsafe_add_(OfflineFuture bean) {
		poolOfflineFuture._reset_unsafe_add_(bean);
	}

	public static void padd(OfflineFuture bean) {
		mkdb.Procedure.padd(bean, poolOfflineFuture);
	}

	public static OfflineFuture newOfflineFutureData() {
		return new xbean.__.OfflineFuture.Data();
	}

	public static mkdb.util.BeanPool<OnlinerefreshFuture> poolOnlinerefreshFuture = new mkdb.util.BeanPool<OnlinerefreshFuture>() {
		@Override
		protected OnlinerefreshFuture newBean() {
			return new xbean.__.OnlinerefreshFuture();
		}
	};

	public static OnlinerefreshFuture newOnlinerefreshFuture() {
		return poolOnlinerefreshFuture.get();
	}

	public static void _reset_unsafe_add_(OnlinerefreshFuture bean) {
		poolOnlinerefreshFuture._reset_unsafe_add_(bean);
	}

	public static void padd(OnlinerefreshFuture bean) {
		mkdb.Procedure.padd(bean, poolOnlinerefreshFuture);
	}

	public static OnlinerefreshFuture newOnlinerefreshFutureData() {
		return new xbean.__.OnlinerefreshFuture.Data();
	}

	public static mkdb.util.BeanPool<PetInfo> poolPetInfo = new mkdb.util.BeanPool<PetInfo>() {
		@Override
		protected PetInfo newBean() {
			return new xbean.__.PetInfo();
		}
	};

	public static PetInfo newPetInfo() {
		return poolPetInfo.get();
	}

	public static void _reset_unsafe_add_(PetInfo bean) {
		poolPetInfo._reset_unsafe_add_(bean);
	}

	public static void padd(PetInfo bean) {
		mkdb.Procedure.padd(bean, poolPetInfo);
	}

	public static PetInfo newPetInfoData() {
		return new xbean.__.PetInfo.Data();
	}

	public static mkdb.util.BeanPool<PetSkill> poolPetSkill = new mkdb.util.BeanPool<PetSkill>() {
		@Override
		protected PetSkill newBean() {
			return new xbean.__.PetSkill();
		}
	};

	public static PetSkill newPetSkill() {
		return poolPetSkill.get();
	}

	public static void _reset_unsafe_add_(PetSkill bean) {
		poolPetSkill._reset_unsafe_add_(bean);
	}

	public static void padd(PetSkill bean) {
		mkdb.Procedure.padd(bean, poolPetSkill);
	}

	public static PetSkill newPetSkillData() {
		return new xbean.__.PetSkill.Data();
	}

	public static mkdb.util.BeanPool<Pets> poolPets = new mkdb.util.BeanPool<Pets>() {
		@Override
		protected Pets newBean() {
			return new xbean.__.Pets();
		}
	};

	public static Pets newPets() {
		return poolPets.get();
	}

	public static void _reset_unsafe_add_(Pets bean) {
		poolPets._reset_unsafe_add_(bean);
	}

	public static void padd(Pets bean) {
		mkdb.Procedure.padd(bean, poolPets);
	}

	public static Pets newPetsData() {
		return new xbean.__.Pets.Data();
	}

	public static mkdb.util.BeanPool<PetSellCount> poolPetSellCount = new mkdb.util.BeanPool<PetSellCount>() {
		@Override
		protected PetSellCount newBean() {
			return new xbean.__.PetSellCount();
		}
	};

	public static PetSellCount newPetSellCount() {
		return poolPetSellCount.get();
	}

	public static void _reset_unsafe_add_(PetSellCount bean) {
		poolPetSellCount._reset_unsafe_add_(bean);
	}

	public static void padd(PetSellCount bean) {
		mkdb.Procedure.padd(bean, poolPetSellCount);
	}

	public static PetSellCount newPetSellCountData() {
		return new xbean.__.PetSellCount.Data();
	}

	public static mkdb.util.BeanPool<MultiExpInfo> poolMultiExpInfo = new mkdb.util.BeanPool<MultiExpInfo>() {
		@Override
		protected MultiExpInfo newBean() {
			return new xbean.__.MultiExpInfo();
		}
	};

	public static MultiExpInfo newMultiExpInfo() {
		return poolMultiExpInfo.get();
	}

	public static void _reset_unsafe_add_(MultiExpInfo bean) {
		poolMultiExpInfo._reset_unsafe_add_(bean);
	}

	public static void padd(MultiExpInfo bean) {
		mkdb.Procedure.padd(bean, poolMultiExpInfo);
	}

	public static MultiExpInfo newMultiExpInfoData() {
		return new xbean.__.MultiExpInfo.Data();
	}

	public static mkdb.util.BeanPool<Decision> poolDecision = new mkdb.util.BeanPool<Decision>() {
		@Override
		protected Decision newBean() {
			return new xbean.__.Decision();
		}
	};

	public static Decision newDecision() {
		return poolDecision.get();
	}

	public static void _reset_unsafe_add_(Decision bean) {
		poolDecision._reset_unsafe_add_(bean);
	}

	public static void padd(Decision bean) {
		mkdb.Procedure.padd(bean, poolDecision);
	}

	public static Decision newDecisionData() {
		return new xbean.__.Decision.Data();
	}

	public static mkdb.util.BeanPool<BattleFlags> poolBattleFlags = new mkdb.util.BeanPool<BattleFlags>() {
		@Override
		protected BattleFlags newBean() {
			return new xbean.__.BattleFlags();
		}
	};

	public static BattleFlags newBattleFlags() {
		return poolBattleFlags.get();
	}

	public static void _reset_unsafe_add_(BattleFlags bean) {
		poolBattleFlags._reset_unsafe_add_(bean);
	}

	public static void padd(BattleFlags bean) {
		mkdb.Procedure.padd(bean, poolBattleFlags);
	}

	public static BattleFlags newBattleFlagsData() {
		return new xbean.__.BattleFlags.Data();
	}

	public static mkdb.util.BeanPool<Fighter> poolFighter = new mkdb.util.BeanPool<Fighter>() {
		@Override
		protected Fighter newBean() {
			return new xbean.__.Fighter();
		}
	};

	public static Fighter newFighter() {
		return poolFighter.get();
	}

	public static void _reset_unsafe_add_(Fighter bean) {
		poolFighter._reset_unsafe_add_(bean);
	}

	public static void padd(Fighter bean) {
		mkdb.Procedure.padd(bean, poolFighter);
	}

	public static Fighter newFighterData() {
		return new xbean.__.Fighter.Data();
	}

	public static mkdb.util.BeanPool<Protectors> poolProtectors = new mkdb.util.BeanPool<Protectors>() {
		@Override
		protected Protectors newBean() {
			return new xbean.__.Protectors();
		}
	};

	public static Protectors newProtectors() {
		return poolProtectors.get();
	}

	public static void _reset_unsafe_add_(Protectors bean) {
		poolProtectors._reset_unsafe_add_(bean);
	}

	public static void padd(Protectors bean) {
		mkdb.Procedure.padd(bean, poolProtectors);
	}

	public static Protectors newProtectorsData() {
		return new xbean.__.Protectors.Data();
	}

	public static mkdb.util.BeanPool<BattleResult> poolBattleResult = new mkdb.util.BeanPool<BattleResult>() {
		@Override
		protected BattleResult newBean() {
			return new xbean.__.BattleResult();
		}
	};

	public static BattleResult newBattleResult() {
		return poolBattleResult.get();
	}

	public static void _reset_unsafe_add_(BattleResult bean) {
		poolBattleResult._reset_unsafe_add_(bean);
	}

	public static void padd(BattleResult bean) {
		mkdb.Procedure.padd(bean, poolBattleResult);
	}

	public static BattleResult newBattleResultData() {
		return new xbean.__.BattleResult.Data();
	}

	public static mkdb.util.BeanPool<RoundResultItems> poolRoundResultItems = new mkdb.util.BeanPool<RoundResultItems>() {
		@Override
		protected RoundResultItems newBean() {
			return new xbean.__.RoundResultItems();
		}
	};

	public static RoundResultItems newRoundResultItems() {
		return poolRoundResultItems.get();
	}

	public static void _reset_unsafe_add_(RoundResultItems bean) {
		poolRoundResultItems._reset_unsafe_add_(bean);
	}

	public static void padd(RoundResultItems bean) {
		mkdb.Procedure.padd(bean, poolRoundResultItems);
	}

	public static RoundResultItems newRoundResultItemsData() {
		return new xbean.__.RoundResultItems.Data();
	}

	public static mkdb.util.BeanPool<BattleInfo> poolBattleInfo = new mkdb.util.BeanPool<BattleInfo>() {
		@Override
		protected BattleInfo newBean() {
			return new xbean.__.BattleInfo();
		}
	};

	public static BattleInfo newBattleInfo() {
		return poolBattleInfo.get();
	}

	public static void _reset_unsafe_add_(BattleInfo bean) {
		poolBattleInfo._reset_unsafe_add_(bean);
	}

	public static void padd(BattleInfo bean) {
		mkdb.Procedure.padd(bean, poolBattleInfo);
	}

	public static BattleInfo newBattleInfoData() {
		return new xbean.__.BattleInfo.Data();
	}

	public static mkdb.util.BeanPool<BattleEndProcedure> poolBattleEndProcedure = new mkdb.util.BeanPool<BattleEndProcedure>() {
		@Override
		protected BattleEndProcedure newBean() {
			return new xbean.__.BattleEndProcedure();
		}
	};

	public static BattleEndProcedure newBattleEndProcedure() {
		return poolBattleEndProcedure.get();
	}

	public static void _reset_unsafe_add_(BattleEndProcedure bean) {
		poolBattleEndProcedure._reset_unsafe_add_(bean);
	}

	public static void padd(BattleEndProcedure bean) {
		mkdb.Procedure.padd(bean, poolBattleEndProcedure);
	}

	public static BattleEndProcedure newBattleEndProcedureData() {
		return new xbean.__.BattleEndProcedure.Data();
	}

	public static mkdb.util.BeanPool<CameraInfo> poolCameraInfo = new mkdb.util.BeanPool<CameraInfo>() {
		@Override
		protected CameraInfo newBean() {
			return new xbean.__.CameraInfo();
		}
	};

	public static CameraInfo newCameraInfo() {
		return poolCameraInfo.get();
	}

	public static void _reset_unsafe_add_(CameraInfo bean) {
		poolCameraInfo._reset_unsafe_add_(bean);
	}

	public static void padd(CameraInfo bean) {
		mkdb.Procedure.padd(bean, poolCameraInfo);
	}

	public static CameraInfo newCameraInfoData() {
		return new xbean.__.CameraInfo.Data();
	}

	public static mkdb.util.BeanPool<Monster> poolMonster = new mkdb.util.BeanPool<Monster>() {
		@Override
		protected Monster newBean() {
			return new xbean.__.Monster();
		}
	};

	public static Monster newMonster() {
		return poolMonster.get();
	}

	public static void _reset_unsafe_add_(Monster bean) {
		poolMonster._reset_unsafe_add_(bean);
	}

	public static void padd(Monster bean) {
		mkdb.Procedure.padd(bean, poolMonster);
	}

	public static Monster newMonsterData() {
		return new xbean.__.Monster.Data();
	}

	public static mkdb.util.BeanPool<FailedMonster> poolFailedMonster = new mkdb.util.BeanPool<FailedMonster>() {
		@Override
		protected FailedMonster newBean() {
			return new xbean.__.FailedMonster();
		}
	};

	public static FailedMonster newFailedMonster() {
		return poolFailedMonster.get();
	}

	public static void _reset_unsafe_add_(FailedMonster bean) {
		poolFailedMonster._reset_unsafe_add_(bean);
	}

	public static void padd(FailedMonster bean) {
		mkdb.Procedure.padd(bean, poolFailedMonster);
	}

	public static FailedMonster newFailedMonsterData() {
		return new xbean.__.FailedMonster.Data();
	}

	public static mkdb.util.BeanPool<MonsterSkill> poolMonsterSkill = new mkdb.util.BeanPool<MonsterSkill>() {
		@Override
		protected MonsterSkill newBean() {
			return new xbean.__.MonsterSkill();
		}
	};

	public static MonsterSkill newMonsterSkill() {
		return poolMonsterSkill.get();
	}

	public static void _reset_unsafe_add_(MonsterSkill bean) {
		poolMonsterSkill._reset_unsafe_add_(bean);
	}

	public static void padd(MonsterSkill bean) {
		mkdb.Procedure.padd(bean, poolMonsterSkill);
	}

	public static MonsterSkill newMonsterSkillData() {
		return new xbean.__.MonsterSkill.Data();
	}

	public static mkdb.util.BeanPool<BattleAI> poolBattleAI = new mkdb.util.BeanPool<BattleAI>() {
		@Override
		protected BattleAI newBean() {
			return new xbean.__.BattleAI();
		}
	};

	public static BattleAI newBattleAI() {
		return poolBattleAI.get();
	}

	public static void _reset_unsafe_add_(BattleAI bean) {
		poolBattleAI._reset_unsafe_add_(bean);
	}

	public static void padd(BattleAI bean) {
		mkdb.Procedure.padd(bean, poolBattleAI);
	}

	public static BattleAI newBattleAIData() {
		return new xbean.__.BattleAI.Data();
	}

	public static mkdb.util.BeanPool<TeamInfo> poolTeamInfo = new mkdb.util.BeanPool<TeamInfo>() {
		@Override
		protected TeamInfo newBean() {
			return new xbean.__.TeamInfo();
		}
	};

	public static TeamInfo newTeamInfo() {
		return poolTeamInfo.get();
	}

	public static void _reset_unsafe_add_(TeamInfo bean) {
		poolTeamInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TeamInfo bean) {
		mkdb.Procedure.padd(bean, poolTeamInfo);
	}

	public static TeamInfo newTeamInfoData() {
		return new xbean.__.TeamInfo.Data();
	}

	public static mkdb.util.BeanPool<TeamMember> poolTeamMember = new mkdb.util.BeanPool<TeamMember>() {
		@Override
		protected TeamMember newBean() {
			return new xbean.__.TeamMember();
		}
	};

	public static TeamMember newTeamMember() {
		return poolTeamMember.get();
	}

	public static void _reset_unsafe_add_(TeamMember bean) {
		poolTeamMember._reset_unsafe_add_(bean);
	}

	public static void padd(TeamMember bean) {
		mkdb.Procedure.padd(bean, poolTeamMember);
	}

	public static TeamMember newTeamMemberData() {
		return new xbean.__.TeamMember.Data();
	}

	public static mkdb.util.BeanPool<SingleInvitings> poolSingleInvitings = new mkdb.util.BeanPool<SingleInvitings>() {
		@Override
		protected SingleInvitings newBean() {
			return new xbean.__.SingleInvitings();
		}
	};

	public static SingleInvitings newSingleInvitings() {
		return poolSingleInvitings.get();
	}

	public static void _reset_unsafe_add_(SingleInvitings bean) {
		poolSingleInvitings._reset_unsafe_add_(bean);
	}

	public static void padd(SingleInvitings bean) {
		mkdb.Procedure.padd(bean, poolSingleInvitings);
	}

	public static SingleInvitings newSingleInvitingsData() {
		return new xbean.__.SingleInvitings.Data();
	}

	public static mkdb.util.BeanPool<InviteInfo> poolInviteInfo = new mkdb.util.BeanPool<InviteInfo>() {
		@Override
		protected InviteInfo newBean() {
			return new xbean.__.InviteInfo();
		}
	};

	public static InviteInfo newInviteInfo() {
		return poolInviteInfo.get();
	}

	public static void _reset_unsafe_add_(InviteInfo bean) {
		poolInviteInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InviteInfo bean) {
		mkdb.Procedure.padd(bean, poolInviteInfo);
	}

	public static InviteInfo newInviteInfoData() {
		return new xbean.__.InviteInfo.Data();
	}

	public static mkdb.util.BeanPool<TeamInvite> poolTeamInvite = new mkdb.util.BeanPool<TeamInvite>() {
		@Override
		protected TeamInvite newBean() {
			return new xbean.__.TeamInvite();
		}
	};

	public static TeamInvite newTeamInvite() {
		return poolTeamInvite.get();
	}

	public static void _reset_unsafe_add_(TeamInvite bean) {
		poolTeamInvite._reset_unsafe_add_(bean);
	}

	public static void padd(TeamInvite bean) {
		mkdb.Procedure.padd(bean, poolTeamInvite);
	}

	public static TeamInvite newTeamInviteData() {
		return new xbean.__.TeamInvite.Data();
	}

	public static mkdb.util.BeanPool<TeamFilter> poolTeamFilter = new mkdb.util.BeanPool<TeamFilter>() {
		@Override
		protected TeamFilter newBean() {
			return new xbean.__.TeamFilter();
		}
	};

	public static TeamFilter newTeamFilter() {
		return poolTeamFilter.get();
	}

	public static void _reset_unsafe_add_(TeamFilter bean) {
		poolTeamFilter._reset_unsafe_add_(bean);
	}

	public static void padd(TeamFilter bean) {
		mkdb.Procedure.padd(bean, poolTeamFilter);
	}

	public static TeamFilter newTeamFilterData() {
		return new xbean.__.TeamFilter.Data();
	}

	public static mkdb.util.BeanPool<BJTeamInfo> poolBJTeamInfo = new mkdb.util.BeanPool<BJTeamInfo>() {
		@Override
		protected BJTeamInfo newBean() {
			return new xbean.__.BJTeamInfo();
		}
	};

	public static BJTeamInfo newBJTeamInfo() {
		return poolBJTeamInfo.get();
	}

	public static void _reset_unsafe_add_(BJTeamInfo bean) {
		poolBJTeamInfo._reset_unsafe_add_(bean);
	}

	public static void padd(BJTeamInfo bean) {
		mkdb.Procedure.padd(bean, poolBJTeamInfo);
	}

	public static BJTeamInfo newBJTeamInfoData() {
		return new xbean.__.BJTeamInfo.Data();
	}

	public static mkdb.util.BeanPool<ETeamMatch> poolETeamMatch = new mkdb.util.BeanPool<ETeamMatch>() {
		@Override
		protected ETeamMatch newBean() {
			return new xbean.__.ETeamMatch();
		}
	};

	public static ETeamMatch newETeamMatch() {
		return poolETeamMatch.get();
	}

	public static void _reset_unsafe_add_(ETeamMatch bean) {
		poolETeamMatch._reset_unsafe_add_(bean);
	}

	public static void padd(ETeamMatch bean) {
		mkdb.Procedure.padd(bean, poolETeamMatch);
	}

	public static ETeamMatch newETeamMatchData() {
		return new xbean.__.ETeamMatch.Data();
	}

	public static mkdb.util.BeanPool<TeamMatch> poolTeamMatch = new mkdb.util.BeanPool<TeamMatch>() {
		@Override
		protected TeamMatch newBean() {
			return new xbean.__.TeamMatch();
		}
	};

	public static TeamMatch newTeamMatch() {
		return poolTeamMatch.get();
	}

	public static void _reset_unsafe_add_(TeamMatch bean) {
		poolTeamMatch._reset_unsafe_add_(bean);
	}

	public static void padd(TeamMatch bean) {
		mkdb.Procedure.padd(bean, poolTeamMatch);
	}

	public static TeamMatch newTeamMatchData() {
		return new xbean.__.TeamMatch.Data();
	}

	public static mkdb.util.BeanPool<ETeamMelon> poolETeamMelon = new mkdb.util.BeanPool<ETeamMelon>() {
		@Override
		protected ETeamMelon newBean() {
			return new xbean.__.ETeamMelon();
		}
	};

	public static ETeamMelon newETeamMelon() {
		return poolETeamMelon.get();
	}

	public static void _reset_unsafe_add_(ETeamMelon bean) {
		poolETeamMelon._reset_unsafe_add_(bean);
	}

	public static void padd(ETeamMelon bean) {
		mkdb.Procedure.padd(bean, poolETeamMelon);
	}

	public static ETeamMelon newETeamMelonData() {
		return new xbean.__.ETeamMelon.Data();
	}

	public static mkdb.util.BeanPool<RollItem> poolRollItem = new mkdb.util.BeanPool<RollItem>() {
		@Override
		protected RollItem newBean() {
			return new xbean.__.RollItem();
		}
	};

	public static RollItem newRollItem() {
		return poolRollItem.get();
	}

	public static void _reset_unsafe_add_(RollItem bean) {
		poolRollItem._reset_unsafe_add_(bean);
	}

	public static void padd(RollItem bean) {
		mkdb.Procedure.padd(bean, poolRollItem);
	}

	public static RollItem newRollItemData() {
		return new xbean.__.RollItem.Data();
	}

	public static mkdb.util.BeanPool<TeamMelon> poolTeamMelon = new mkdb.util.BeanPool<TeamMelon>() {
		@Override
		protected TeamMelon newBean() {
			return new xbean.__.TeamMelon();
		}
	};

	public static TeamMelon newTeamMelon() {
		return poolTeamMelon.get();
	}

	public static void _reset_unsafe_add_(TeamMelon bean) {
		poolTeamMelon._reset_unsafe_add_(bean);
	}

	public static void padd(TeamMelon bean) {
		mkdb.Procedure.padd(bean, poolTeamMelon);
	}

	public static TeamMelon newTeamMelonData() {
		return new xbean.__.TeamMelon.Data();
	}

	public static mkdb.util.BeanPool<ERole> poolERole = new mkdb.util.BeanPool<ERole>() {
		@Override
		protected ERole newBean() {
			return new xbean.__.ERole();
		}
	};

	public static ERole newERole() {
		return poolERole.get();
	}

	public static void _reset_unsafe_add_(ERole bean) {
		poolERole._reset_unsafe_add_(bean);
	}

	public static void padd(ERole bean) {
		mkdb.Procedure.padd(bean, poolERole);
	}

	public static ERole newERoleData() {
		return new xbean.__.ERole.Data();
	}

	public static mkdb.util.BeanPool<EPet> poolEPet = new mkdb.util.BeanPool<EPet>() {
		@Override
		protected EPet newBean() {
			return new xbean.__.EPet();
		}
	};

	public static EPet newEPet() {
		return poolEPet.get();
	}

	public static void _reset_unsafe_add_(EPet bean) {
		poolEPet._reset_unsafe_add_(bean);
	}

	public static void padd(EPet bean) {
		mkdb.Procedure.padd(bean, poolEPet);
	}

	public static EPet newEPetData() {
		return new xbean.__.EPet.Data();
	}

	public static mkdb.util.BeanPool<RecoveryAttr> poolRecoveryAttr = new mkdb.util.BeanPool<RecoveryAttr>() {
		@Override
		protected RecoveryAttr newBean() {
			return new xbean.__.RecoveryAttr();
		}
	};

	public static RecoveryAttr newRecoveryAttr() {
		return poolRecoveryAttr.get();
	}

	public static void _reset_unsafe_add_(RecoveryAttr bean) {
		poolRecoveryAttr._reset_unsafe_add_(bean);
	}

	public static void padd(RecoveryAttr bean) {
		mkdb.Procedure.padd(bean, poolRecoveryAttr);
	}

	public static RecoveryAttr newRecoveryAttrData() {
		return new xbean.__.RecoveryAttr.Data();
	}

	public static mkdb.util.BeanPool<EnhancementAttr> poolEnhancementAttr = new mkdb.util.BeanPool<EnhancementAttr>() {
		@Override
		protected EnhancementAttr newBean() {
			return new xbean.__.EnhancementAttr();
		}
	};

	public static EnhancementAttr newEnhancementAttr() {
		return poolEnhancementAttr.get();
	}

	public static void _reset_unsafe_add_(EnhancementAttr bean) {
		poolEnhancementAttr._reset_unsafe_add_(bean);
	}

	public static void padd(EnhancementAttr bean) {
		mkdb.Procedure.padd(bean, poolEnhancementAttr);
	}

	public static EnhancementAttr newEnhancementAttrData() {
		return new xbean.__.EnhancementAttr.Data();
	}

	public static mkdb.util.BeanPool<BuffRole> poolBuffRole = new mkdb.util.BeanPool<BuffRole>() {
		@Override
		protected BuffRole newBean() {
			return new xbean.__.BuffRole();
		}
	};

	public static BuffRole newBuffRole() {
		return poolBuffRole.get();
	}

	public static void _reset_unsafe_add_(BuffRole bean) {
		poolBuffRole._reset_unsafe_add_(bean);
	}

	public static void padd(BuffRole bean) {
		mkdb.Procedure.padd(bean, poolBuffRole);
	}

	public static BuffRole newBuffRoleData() {
		return new xbean.__.BuffRole.Data();
	}

	public static mkdb.util.BeanPool<BuffAgent> poolBuffAgent = new mkdb.util.BeanPool<BuffAgent>() {
		@Override
		protected BuffAgent newBean() {
			return new xbean.__.BuffAgent();
		}
	};

	public static BuffAgent newBuffAgent() {
		return poolBuffAgent.get();
	}

	public static void _reset_unsafe_add_(BuffAgent bean) {
		poolBuffAgent._reset_unsafe_add_(bean);
	}

	public static void padd(BuffAgent bean) {
		mkdb.Procedure.padd(bean, poolBuffAgent);
	}

	public static BuffAgent newBuffAgentData() {
		return new xbean.__.BuffAgent.Data();
	}

	public static mkdb.util.BeanPool<NotifyList> poolNotifyList = new mkdb.util.BeanPool<NotifyList>() {
		@Override
		protected NotifyList newBean() {
			return new xbean.__.NotifyList();
		}
	};

	public static NotifyList newNotifyList() {
		return poolNotifyList.get();
	}

	public static void _reset_unsafe_add_(NotifyList bean) {
		poolNotifyList._reset_unsafe_add_(bean);
	}

	public static void padd(NotifyList bean) {
		mkdb.Procedure.padd(bean, poolNotifyList);
	}

	public static NotifyList newNotifyListData() {
		return new xbean.__.NotifyList.Data();
	}

	public static mkdb.util.BeanPool<Buff> poolBuff = new mkdb.util.BeanPool<Buff>() {
		@Override
		protected Buff newBean() {
			return new xbean.__.Buff();
		}
	};

	public static Buff newBuff() {
		return poolBuff.get();
	}

	public static void _reset_unsafe_add_(Buff bean) {
		poolBuff._reset_unsafe_add_(bean);
	}

	public static void padd(Buff bean) {
		mkdb.Procedure.padd(bean, poolBuff);
	}

	public static Buff newBuffData() {
		return new xbean.__.Buff.Data();
	}

	public static mkdb.util.BeanPool<Effect> poolEffect = new mkdb.util.BeanPool<Effect>() {
		@Override
		protected Effect newBean() {
			return new xbean.__.Effect();
		}
	};

	public static Effect newEffect() {
		return poolEffect.get();
	}

	public static void _reset_unsafe_add_(Effect bean) {
		poolEffect._reset_unsafe_add_(bean);
	}

	public static void padd(Effect bean) {
		mkdb.Procedure.padd(bean, poolEffect);
	}

	public static Effect newEffectData() {
		return new xbean.__.Effect.Data();
	}

	public static mkdb.util.BeanPool<StoredBuffRole> poolStoredBuffRole = new mkdb.util.BeanPool<StoredBuffRole>() {
		@Override
		protected StoredBuffRole newBean() {
			return new xbean.__.StoredBuffRole();
		}
	};

	public static StoredBuffRole newStoredBuffRole() {
		return poolStoredBuffRole.get();
	}

	public static void _reset_unsafe_add_(StoredBuffRole bean) {
		poolStoredBuffRole._reset_unsafe_add_(bean);
	}

	public static void padd(StoredBuffRole bean) {
		mkdb.Procedure.padd(bean, poolStoredBuffRole);
	}

	public static StoredBuffRole newStoredBuffRoleData() {
		return new xbean.__.StoredBuffRole.Data();
	}

	public static mkdb.util.BeanPool<TimerFuture> poolTimerFuture = new mkdb.util.BeanPool<TimerFuture>() {
		@Override
		protected TimerFuture newBean() {
			return new xbean.__.TimerFuture();
		}
	};

	public static TimerFuture newTimerFuture() {
		return poolTimerFuture.get();
	}

	public static void _reset_unsafe_add_(TimerFuture bean) {
		poolTimerFuture._reset_unsafe_add_(bean);
	}

	public static void padd(TimerFuture bean) {
		mkdb.Procedure.padd(bean, poolTimerFuture);
	}

	public static TimerFuture newTimerFutureData() {
		return new xbean.__.TimerFuture.Data();
	}

	public static mkdb.util.BeanPool<DebugFlag> poolDebugFlag = new mkdb.util.BeanPool<DebugFlag>() {
		@Override
		protected DebugFlag newBean() {
			return new xbean.__.DebugFlag();
		}
	};

	public static DebugFlag newDebugFlag() {
		return poolDebugFlag.get();
	}

	public static void _reset_unsafe_add_(DebugFlag bean) {
		poolDebugFlag._reset_unsafe_add_(bean);
	}

	public static void padd(DebugFlag bean) {
		mkdb.Procedure.padd(bean, poolDebugFlag);
	}

	public static DebugFlag newDebugFlagData() {
		return new xbean.__.DebugFlag.Data();
	}

	public static mkdb.util.BeanPool<AssistSkill> poolAssistSkill = new mkdb.util.BeanPool<AssistSkill>() {
		@Override
		protected AssistSkill newBean() {
			return new xbean.__.AssistSkill();
		}
	};

	public static AssistSkill newAssistSkill() {
		return poolAssistSkill.get();
	}

	public static void _reset_unsafe_add_(AssistSkill bean) {
		poolAssistSkill._reset_unsafe_add_(bean);
	}

	public static void padd(AssistSkill bean) {
		mkdb.Procedure.padd(bean, poolAssistSkill);
	}

	public static AssistSkill newAssistSkillData() {
		return new xbean.__.AssistSkill.Data();
	}

	public static mkdb.util.BeanPool<SpecialSkill> poolSpecialSkill = new mkdb.util.BeanPool<SpecialSkill>() {
		@Override
		protected SpecialSkill newBean() {
			return new xbean.__.SpecialSkill();
		}
	};

	public static SpecialSkill newSpecialSkill() {
		return poolSpecialSkill.get();
	}

	public static void _reset_unsafe_add_(SpecialSkill bean) {
		poolSpecialSkill._reset_unsafe_add_(bean);
	}

	public static void padd(SpecialSkill bean) {
		mkdb.Procedure.padd(bean, poolSpecialSkill);
	}

	public static SpecialSkill newSpecialSkillData() {
		return new xbean.__.SpecialSkill.Data();
	}

	public static mkdb.util.BeanPool<SkillRole> poolSkillRole = new mkdb.util.BeanPool<SkillRole>() {
		@Override
		protected SkillRole newBean() {
			return new xbean.__.SkillRole();
		}
	};

	public static SkillRole newSkillRole() {
		return poolSkillRole.get();
	}

	public static void _reset_unsafe_add_(SkillRole bean) {
		poolSkillRole._reset_unsafe_add_(bean);
	}

	public static void padd(SkillRole bean) {
		mkdb.Procedure.padd(bean, poolSkillRole);
	}

	public static SkillRole newSkillRoleData() {
		return new xbean.__.SkillRole.Data();
	}

	public static mkdb.util.BeanPool<FirstFullSkillbase> poolFirstFullSkillbase = new mkdb.util.BeanPool<FirstFullSkillbase>() {
		@Override
		protected FirstFullSkillbase newBean() {
			return new xbean.__.FirstFullSkillbase();
		}
	};

	public static FirstFullSkillbase newFirstFullSkillbase() {
		return poolFirstFullSkillbase.get();
	}

	public static void _reset_unsafe_add_(FirstFullSkillbase bean) {
		poolFirstFullSkillbase._reset_unsafe_add_(bean);
	}

	public static void padd(FirstFullSkillbase bean) {
		mkdb.Procedure.padd(bean, poolFirstFullSkillbase);
	}

	public static FirstFullSkillbase newFirstFullSkillbaseData() {
		return new xbean.__.FirstFullSkillbase.Data();
	}

	public static mkdb.util.BeanPool<LiveSkill> poolLiveSkill = new mkdb.util.BeanPool<LiveSkill>() {
		@Override
		protected LiveSkill newBean() {
			return new xbean.__.LiveSkill();
		}
	};

	public static LiveSkill newLiveSkill() {
		return poolLiveSkill.get();
	}

	public static void _reset_unsafe_add_(LiveSkill bean) {
		poolLiveSkill._reset_unsafe_add_(bean);
	}

	public static void padd(LiveSkill bean) {
		mkdb.Procedure.padd(bean, poolLiveSkill);
	}

	public static LiveSkill newLiveSkillData() {
		return new xbean.__.LiveSkill.Data();
	}

	public static mkdb.util.BeanPool<ELiveSkill> poolELiveSkill = new mkdb.util.BeanPool<ELiveSkill>() {
		@Override
		protected ELiveSkill newBean() {
			return new xbean.__.ELiveSkill();
		}
	};

	public static ELiveSkill newELiveSkill() {
		return poolELiveSkill.get();
	}

	public static void _reset_unsafe_add_(ELiveSkill bean) {
		poolELiveSkill._reset_unsafe_add_(bean);
	}

	public static void padd(ELiveSkill bean) {
		mkdb.Procedure.padd(bean, poolELiveSkill);
	}

	public static ELiveSkill newELiveSkillData() {
		return new xbean.__.ELiveSkill.Data();
	}

	public static mkdb.util.BeanPool<ParticleSkill> poolParticleSkill = new mkdb.util.BeanPool<ParticleSkill>() {
		@Override
		protected ParticleSkill newBean() {
			return new xbean.__.ParticleSkill();
		}
	};

	public static ParticleSkill newParticleSkill() {
		return poolParticleSkill.get();
	}

	public static void _reset_unsafe_add_(ParticleSkill bean) {
		poolParticleSkill._reset_unsafe_add_(bean);
	}

	public static void padd(ParticleSkill bean) {
		mkdb.Procedure.padd(bean, poolParticleSkill);
	}

	public static ParticleSkill newParticleSkillData() {
		return new xbean.__.ParticleSkill.Data();
	}

	public static mkdb.util.BeanPool<PracticeSkill> poolPracticeSkill = new mkdb.util.BeanPool<PracticeSkill>() {
		@Override
		protected PracticeSkill newBean() {
			return new xbean.__.PracticeSkill();
		}
	};

	public static PracticeSkill newPracticeSkill() {
		return poolPracticeSkill.get();
	}

	public static void _reset_unsafe_add_(PracticeSkill bean) {
		poolPracticeSkill._reset_unsafe_add_(bean);
	}

	public static void padd(PracticeSkill bean) {
		mkdb.Procedure.padd(bean, poolPracticeSkill);
	}

	public static PracticeSkill newPracticeSkillData() {
		return new xbean.__.PracticeSkill.Data();
	}

	public static mkdb.util.BeanPool<EParticleSkill> poolEParticleSkill = new mkdb.util.BeanPool<EParticleSkill>() {
		@Override
		protected EParticleSkill newBean() {
			return new xbean.__.EParticleSkill();
		}
	};

	public static EParticleSkill newEParticleSkill() {
		return poolEParticleSkill.get();
	}

	public static void _reset_unsafe_add_(EParticleSkill bean) {
		poolEParticleSkill._reset_unsafe_add_(bean);
	}

	public static void padd(EParticleSkill bean) {
		mkdb.Procedure.padd(bean, poolEParticleSkill);
	}

	public static EParticleSkill newEParticleSkillData() {
		return new xbean.__.EParticleSkill.Data();
	}

	public static mkdb.util.BeanPool<ArchMap> poolArchMap = new mkdb.util.BeanPool<ArchMap>() {
		@Override
		protected ArchMap newBean() {
			return new xbean.__.ArchMap();
		}
	};

	public static ArchMap newArchMap() {
		return poolArchMap.get();
	}

	public static void _reset_unsafe_add_(ArchMap bean) {
		poolArchMap._reset_unsafe_add_(bean);
	}

	public static void padd(ArchMap bean) {
		mkdb.Procedure.padd(bean, poolArchMap);
	}

	public static ArchMap newArchMapData() {
		return new xbean.__.ArchMap.Data();
	}

	public static mkdb.util.BeanPool<ImpExamRecord> poolImpExamRecord = new mkdb.util.BeanPool<ImpExamRecord>() {
		@Override
		protected ImpExamRecord newBean() {
			return new xbean.__.ImpExamRecord();
		}
	};

	public static ImpExamRecord newImpExamRecord() {
		return poolImpExamRecord.get();
	}

	public static void _reset_unsafe_add_(ImpExamRecord bean) {
		poolImpExamRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ImpExamRecord bean) {
		mkdb.Procedure.padd(bean, poolImpExamRecord);
	}

	public static ImpExamRecord newImpExamRecordData() {
		return new xbean.__.ImpExamRecord.Data();
	}

	public static mkdb.util.BeanPool<ImpExamStatePassRole> poolImpExamStatePassRole = new mkdb.util.BeanPool<ImpExamStatePassRole>() {
		@Override
		protected ImpExamStatePassRole newBean() {
			return new xbean.__.ImpExamStatePassRole();
		}
	};

	public static ImpExamStatePassRole newImpExamStatePassRole() {
		return poolImpExamStatePassRole.get();
	}

	public static void _reset_unsafe_add_(ImpExamStatePassRole bean) {
		poolImpExamStatePassRole._reset_unsafe_add_(bean);
	}

	public static void padd(ImpExamStatePassRole bean) {
		mkdb.Procedure.padd(bean, poolImpExamStatePassRole);
	}

	public static ImpExamStatePassRole newImpExamStatePassRoleData() {
		return new xbean.__.ImpExamStatePassRole.Data();
	}

	public static mkdb.util.BeanPool<ImpExamStateRecord> poolImpExamStateRecord = new mkdb.util.BeanPool<ImpExamStateRecord>() {
		@Override
		protected ImpExamStateRecord newBean() {
			return new xbean.__.ImpExamStateRecord();
		}
	};

	public static ImpExamStateRecord newImpExamStateRecord() {
		return poolImpExamStateRecord.get();
	}

	public static void _reset_unsafe_add_(ImpExamStateRecord bean) {
		poolImpExamStateRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ImpExamStateRecord bean) {
		mkdb.Procedure.padd(bean, poolImpExamStateRecord);
	}

	public static ImpExamStateRecord newImpExamStateRecordData() {
		return new xbean.__.ImpExamStateRecord.Data();
	}

	public static mkdb.util.BeanPool<WinnerRoleRecord> poolWinnerRoleRecord = new mkdb.util.BeanPool<WinnerRoleRecord>() {
		@Override
		protected WinnerRoleRecord newBean() {
			return new xbean.__.WinnerRoleRecord();
		}
	};

	public static WinnerRoleRecord newWinnerRoleRecord() {
		return poolWinnerRoleRecord.get();
	}

	public static void _reset_unsafe_add_(WinnerRoleRecord bean) {
		poolWinnerRoleRecord._reset_unsafe_add_(bean);
	}

	public static void padd(WinnerRoleRecord bean) {
		mkdb.Procedure.padd(bean, poolWinnerRoleRecord);
	}

	public static WinnerRoleRecord newWinnerRoleRecordData() {
		return new xbean.__.WinnerRoleRecord.Data();
	}

	public static mkdb.util.BeanPool<YbNum> poolYbNum = new mkdb.util.BeanPool<YbNum>() {
		@Override
		protected YbNum newBean() {
			return new xbean.__.YbNum();
		}
	};

	public static YbNum newYbNum() {
		return poolYbNum.get();
	}

	public static void _reset_unsafe_add_(YbNum bean) {
		poolYbNum._reset_unsafe_add_(bean);
	}

	public static void padd(YbNum bean) {
		mkdb.Procedure.padd(bean, poolYbNum);
	}

	public static YbNum newYbNumData() {
		return new xbean.__.YbNum.Data();
	}

	public static mkdb.util.BeanPool<YbNums> poolYbNums = new mkdb.util.BeanPool<YbNums>() {
		@Override
		protected YbNums newBean() {
			return new xbean.__.YbNums();
		}
	};

	public static YbNums newYbNums() {
		return poolYbNums.get();
	}

	public static void _reset_unsafe_add_(YbNums bean) {
		poolYbNums._reset_unsafe_add_(bean);
	}

	public static void padd(YbNums bean) {
		mkdb.Procedure.padd(bean, poolYbNums);
	}

	public static YbNums newYbNumsData() {
		return new xbean.__.YbNums.Data();
	}

	public static mkdb.util.BeanPool<BiaoQing> poolBiaoQing = new mkdb.util.BeanPool<BiaoQing>() {
		@Override
		protected BiaoQing newBean() {
			return new xbean.__.BiaoQing();
		}
	};

	public static BiaoQing newBiaoQing() {
		return poolBiaoQing.get();
	}

	public static void _reset_unsafe_add_(BiaoQing bean) {
		poolBiaoQing._reset_unsafe_add_(bean);
	}

	public static void padd(BiaoQing bean) {
		mkdb.Procedure.padd(bean, poolBiaoQing);
	}

	public static BiaoQing newBiaoQingData() {
		return new xbean.__.BiaoQing.Data();
	}

	public static mkdb.util.BeanPool<FirstCharge> poolFirstCharge = new mkdb.util.BeanPool<FirstCharge>() {
		@Override
		protected FirstCharge newBean() {
			return new xbean.__.FirstCharge();
		}
	};

	public static FirstCharge newFirstCharge() {
		return poolFirstCharge.get();
	}

	public static void _reset_unsafe_add_(FirstCharge bean) {
		poolFirstCharge._reset_unsafe_add_(bean);
	}

	public static void padd(FirstCharge bean) {
		mkdb.Procedure.padd(bean, poolFirstCharge);
	}

	public static FirstCharge newFirstChargeData() {
		return new xbean.__.FirstCharge.Data();
	}

	public static mkdb.util.BeanPool<Vipinfo> poolVipinfo = new mkdb.util.BeanPool<Vipinfo>() {
		@Override
		protected Vipinfo newBean() {
			return new xbean.__.Vipinfo();
		}
	};

	public static Vipinfo newVipinfo() {
		return poolVipinfo.get();
	}

	public static void _reset_unsafe_add_(Vipinfo bean) {
		poolVipinfo._reset_unsafe_add_(bean);
	}

	public static void padd(Vipinfo bean) {
		mkdb.Procedure.padd(bean, poolVipinfo);
	}

	public static Vipinfo newVipinfoData() {
		return new xbean.__.Vipinfo.Data();
	}

	public static mkdb.util.BeanPool<DayPay> poolDayPay = new mkdb.util.BeanPool<DayPay>() {
		@Override
		protected DayPay newBean() {
			return new xbean.__.DayPay();
		}
	};

	public static DayPay newDayPay() {
		return poolDayPay.get();
	}

	public static void _reset_unsafe_add_(DayPay bean) {
		poolDayPay._reset_unsafe_add_(bean);
	}

	public static void padd(DayPay bean) {
		mkdb.Procedure.padd(bean, poolDayPay);
	}

	public static DayPay newDayPayData() {
		return new xbean.__.DayPay.Data();
	}

	public static mkdb.util.BeanPool<EDayPay> poolEDayPay = new mkdb.util.BeanPool<EDayPay>() {
		@Override
		protected EDayPay newBean() {
			return new xbean.__.EDayPay();
		}
	};

	public static EDayPay newEDayPay() {
		return poolEDayPay.get();
	}

	public static void _reset_unsafe_add_(EDayPay bean) {
		poolEDayPay._reset_unsafe_add_(bean);
	}

	public static void padd(EDayPay bean) {
		mkdb.Procedure.padd(bean, poolEDayPay);
	}

	public static EDayPay newEDayPayData() {
		return new xbean.__.EDayPay.Data();
	}

	public static mkdb.util.BeanPool<EMonthCard> poolEMonthCard = new mkdb.util.BeanPool<EMonthCard>() {
		@Override
		protected EMonthCard newBean() {
			return new xbean.__.EMonthCard();
		}
	};

	public static EMonthCard newEMonthCard() {
		return poolEMonthCard.get();
	}

	public static void _reset_unsafe_add_(EMonthCard bean) {
		poolEMonthCard._reset_unsafe_add_(bean);
	}

	public static void padd(EMonthCard bean) {
		mkdb.Procedure.padd(bean, poolEMonthCard);
	}

	public static EMonthCard newEMonthCardData() {
		return new xbean.__.EMonthCard.Data();
	}

	public static mkdb.util.BeanPool<WheelItem> poolWheelItem = new mkdb.util.BeanPool<WheelItem>() {
		@Override
		protected WheelItem newBean() {
			return new xbean.__.WheelItem();
		}
	};

	public static WheelItem newWheelItem() {
		return poolWheelItem.get();
	}

	public static void _reset_unsafe_add_(WheelItem bean) {
		poolWheelItem._reset_unsafe_add_(bean);
	}

	public static void padd(WheelItem bean) {
		mkdb.Procedure.padd(bean, poolWheelItem);
	}

	public static WheelItem newWheelItemData() {
		return new xbean.__.WheelItem.Data();
	}

	public static mkdb.util.BeanPool<WheelInfo> poolWheelInfo = new mkdb.util.BeanPool<WheelInfo>() {
		@Override
		protected WheelInfo newBean() {
			return new xbean.__.WheelInfo();
		}
	};

	public static WheelInfo newWheelInfo() {
		return poolWheelInfo.get();
	}

	public static void _reset_unsafe_add_(WheelInfo bean) {
		poolWheelInfo._reset_unsafe_add_(bean);
	}

	public static void padd(WheelInfo bean) {
		mkdb.Procedure.padd(bean, poolWheelInfo);
	}

	public static WheelInfo newWheelInfoData() {
		return new xbean.__.WheelInfo.Data();
	}

	public static mkdb.util.BeanPool<WheelInfos> poolWheelInfos = new mkdb.util.BeanPool<WheelInfos>() {
		@Override
		protected WheelInfos newBean() {
			return new xbean.__.WheelInfos();
		}
	};

	public static WheelInfos newWheelInfos() {
		return poolWheelInfos.get();
	}

	public static void _reset_unsafe_add_(WheelInfos bean) {
		poolWheelInfos._reset_unsafe_add_(bean);
	}

	public static void padd(WheelInfos bean) {
		mkdb.Procedure.padd(bean, poolWheelInfos);
	}

	public static WheelInfos newWheelInfosData() {
		return new xbean.__.WheelInfos.Data();
	}

	public static mkdb.util.BeanPool<WheelItemLimit> poolWheelItemLimit = new mkdb.util.BeanPool<WheelItemLimit>() {
		@Override
		protected WheelItemLimit newBean() {
			return new xbean.__.WheelItemLimit();
		}
	};

	public static WheelItemLimit newWheelItemLimit() {
		return poolWheelItemLimit.get();
	}

	public static void _reset_unsafe_add_(WheelItemLimit bean) {
		poolWheelItemLimit._reset_unsafe_add_(bean);
	}

	public static void padd(WheelItemLimit bean) {
		mkdb.Procedure.padd(bean, poolWheelItemLimit);
	}

	public static WheelItemLimit newWheelItemLimitData() {
		return new xbean.__.WheelItemLimit.Data();
	}

	public static mkdb.util.BeanPool<EnhancementData> poolEnhancementData = new mkdb.util.BeanPool<EnhancementData>() {
		@Override
		protected EnhancementData newBean() {
			return new xbean.__.EnhancementData();
		}
	};

	public static EnhancementData newEnhancementData() {
		return poolEnhancementData.get();
	}

	public static void _reset_unsafe_add_(EnhancementData bean) {
		poolEnhancementData._reset_unsafe_add_(bean);
	}

	public static void padd(EnhancementData bean) {
		mkdb.Procedure.padd(bean, poolEnhancementData);
	}

	public static EnhancementData newEnhancementDataData() {
		return new xbean.__.EnhancementData.Data();
	}

	public static mkdb.util.BeanPool<Equip> poolEquip = new mkdb.util.BeanPool<Equip>() {
		@Override
		protected Equip newBean() {
			return new xbean.__.Equip();
		}
	};

	public static Equip newEquip() {
		return poolEquip.get();
	}

	public static void _reset_unsafe_add_(Equip bean) {
		poolEquip._reset_unsafe_add_(bean);
	}

	public static void padd(Equip bean) {
		mkdb.Procedure.padd(bean, poolEquip);
	}

	public static Equip newEquipData() {
		return new xbean.__.Equip.Data();
	}

	public static mkdb.util.BeanPool<FriendBean> poolFriendBean = new mkdb.util.BeanPool<FriendBean>() {
		@Override
		protected FriendBean newBean() {
			return new xbean.__.FriendBean();
		}
	};

	public static FriendBean newFriendBean() {
		return poolFriendBean.get();
	}

	public static void _reset_unsafe_add_(FriendBean bean) {
		poolFriendBean._reset_unsafe_add_(bean);
	}

	public static void padd(FriendBean bean) {
		mkdb.Procedure.padd(bean, poolFriendBean);
	}

	public static FriendBean newFriendBeanData() {
		return new xbean.__.FriendBean.Data();
	}

	public static mkdb.util.BeanPool<FriendlyDegreesLimit> poolFriendlyDegreesLimit = new mkdb.util.BeanPool<FriendlyDegreesLimit>() {
		@Override
		protected FriendlyDegreesLimit newBean() {
			return new xbean.__.FriendlyDegreesLimit();
		}
	};

	public static FriendlyDegreesLimit newFriendlyDegreesLimit() {
		return poolFriendlyDegreesLimit.get();
	}

	public static void _reset_unsafe_add_(FriendlyDegreesLimit bean) {
		poolFriendlyDegreesLimit._reset_unsafe_add_(bean);
	}

	public static void padd(FriendlyDegreesLimit bean) {
		mkdb.Procedure.padd(bean, poolFriendlyDegreesLimit);
	}

	public static FriendlyDegreesLimit newFriendlyDegreesLimitData() {
		return new xbean.__.FriendlyDegreesLimit.Data();
	}

	public static mkdb.util.BeanPool<FriendGroups> poolFriendGroups = new mkdb.util.BeanPool<FriendGroups>() {
		@Override
		protected FriendGroups newBean() {
			return new xbean.__.FriendGroups();
		}
	};

	public static FriendGroups newFriendGroups() {
		return poolFriendGroups.get();
	}

	public static void _reset_unsafe_add_(FriendGroups bean) {
		poolFriendGroups._reset_unsafe_add_(bean);
	}

	public static void padd(FriendGroups bean) {
		mkdb.Procedure.padd(bean, poolFriendGroups);
	}

	public static FriendGroups newFriendGroupsData() {
		return new xbean.__.FriendGroups.Data();
	}

	public static mkdb.util.BeanPool<OfflineMsg> poolOfflineMsg = new mkdb.util.BeanPool<OfflineMsg>() {
		@Override
		protected OfflineMsg newBean() {
			return new xbean.__.OfflineMsg();
		}
	};

	public static OfflineMsg newOfflineMsg() {
		return poolOfflineMsg.get();
	}

	public static void _reset_unsafe_add_(OfflineMsg bean) {
		poolOfflineMsg._reset_unsafe_add_(bean);
	}

	public static void padd(OfflineMsg bean) {
		mkdb.Procedure.padd(bean, poolOfflineMsg);
	}

	public static OfflineMsg newOfflineMsgData() {
		return new xbean.__.OfflineMsg.Data();
	}

	public static mkdb.util.BeanPool<ShowInfoBean> poolShowInfoBean = new mkdb.util.BeanPool<ShowInfoBean>() {
		@Override
		protected ShowInfoBean newBean() {
			return new xbean.__.ShowInfoBean();
		}
	};

	public static ShowInfoBean newShowInfoBean() {
		return poolShowInfoBean.get();
	}

	public static void _reset_unsafe_add_(ShowInfoBean bean) {
		poolShowInfoBean._reset_unsafe_add_(bean);
	}

	public static void padd(ShowInfoBean bean) {
		mkdb.Procedure.padd(bean, poolShowInfoBean);
	}

	public static ShowInfoBean newShowInfoBeanData() {
		return new xbean.__.ShowInfoBean.Data();
	}

	public static mkdb.util.BeanPool<FriendGiveNum> poolFriendGiveNum = new mkdb.util.BeanPool<FriendGiveNum>() {
		@Override
		protected FriendGiveNum newBean() {
			return new xbean.__.FriendGiveNum();
		}
	};

	public static FriendGiveNum newFriendGiveNum() {
		return poolFriendGiveNum.get();
	}

	public static void _reset_unsafe_add_(FriendGiveNum bean) {
		poolFriendGiveNum._reset_unsafe_add_(bean);
	}

	public static void padd(FriendGiveNum bean) {
		mkdb.Procedure.padd(bean, poolFriendGiveNum);
	}

	public static FriendGiveNum newFriendGiveNumData() {
		return new xbean.__.FriendGiveNum.Data();
	}

	public static mkdb.util.BeanPool<RoleSpace> poolRoleSpace = new mkdb.util.BeanPool<RoleSpace>() {
		@Override
		protected RoleSpace newBean() {
			return new xbean.__.RoleSpace();
		}
	};

	public static RoleSpace newRoleSpace() {
		return poolRoleSpace.get();
	}

	public static void _reset_unsafe_add_(RoleSpace bean) {
		poolRoleSpace._reset_unsafe_add_(bean);
	}

	public static void padd(RoleSpace bean) {
		mkdb.Procedure.padd(bean, poolRoleSpace);
	}

	public static RoleSpace newRoleSpaceData() {
		return new xbean.__.RoleSpace.Data();
	}

	public static mkdb.util.BeanPool<RecruitAward> poolRecruitAward = new mkdb.util.BeanPool<RecruitAward>() {
		@Override
		protected RecruitAward newBean() {
			return new xbean.__.RecruitAward();
		}
	};

	public static RecruitAward newRecruitAward() {
		return poolRecruitAward.get();
	}

	public static void _reset_unsafe_add_(RecruitAward bean) {
		poolRecruitAward._reset_unsafe_add_(bean);
	}

	public static void padd(RecruitAward bean) {
		mkdb.Procedure.padd(bean, poolRecruitAward);
	}

	public static RecruitAward newRecruitAwardData() {
		return new xbean.__.RecruitAward.Data();
	}

	public static mkdb.util.BeanPool<FestivalGift> poolFestivalGift = new mkdb.util.BeanPool<FestivalGift>() {
		@Override
		protected FestivalGift newBean() {
			return new xbean.__.FestivalGift();
		}
	};

	public static FestivalGift newFestivalGift() {
		return poolFestivalGift.get();
	}

	public static void _reset_unsafe_add_(FestivalGift bean) {
		poolFestivalGift._reset_unsafe_add_(bean);
	}

	public static void padd(FestivalGift bean) {
		mkdb.Procedure.padd(bean, poolFestivalGift);
	}

	public static FestivalGift newFestivalGiftData() {
		return new xbean.__.FestivalGift.Data();
	}

	public static mkdb.util.BeanPool<OfflineMsgList> poolOfflineMsgList = new mkdb.util.BeanPool<OfflineMsgList>() {
		@Override
		protected OfflineMsgList newBean() {
			return new xbean.__.OfflineMsgList();
		}
	};

	public static OfflineMsgList newOfflineMsgList() {
		return poolOfflineMsgList.get();
	}

	public static void _reset_unsafe_add_(OfflineMsgList bean) {
		poolOfflineMsgList._reset_unsafe_add_(bean);
	}

	public static void padd(OfflineMsgList bean) {
		mkdb.Procedure.padd(bean, poolOfflineMsgList);
	}

	public static OfflineMsgList newOfflineMsgListData() {
		return new xbean.__.OfflineMsgList.Data();
	}

	public static mkdb.util.BeanPool<OfflineMsgProtocol> poolOfflineMsgProtocol = new mkdb.util.BeanPool<OfflineMsgProtocol>() {
		@Override
		protected OfflineMsgProtocol newBean() {
			return new xbean.__.OfflineMsgProtocol();
		}
	};

	public static OfflineMsgProtocol newOfflineMsgProtocol() {
		return poolOfflineMsgProtocol.get();
	}

	public static void _reset_unsafe_add_(OfflineMsgProtocol bean) {
		poolOfflineMsgProtocol._reset_unsafe_add_(bean);
	}

	public static void padd(OfflineMsgProtocol bean) {
		mkdb.Procedure.padd(bean, poolOfflineMsgProtocol);
	}

	public static OfflineMsgProtocol newOfflineMsgProtocolData() {
		return new xbean.__.OfflineMsgProtocol.Data();
	}

	public static mkdb.util.BeanPool<BroadcastMsgList> poolBroadcastMsgList = new mkdb.util.BeanPool<BroadcastMsgList>() {
		@Override
		protected BroadcastMsgList newBean() {
			return new xbean.__.BroadcastMsgList();
		}
	};

	public static BroadcastMsgList newBroadcastMsgList() {
		return poolBroadcastMsgList.get();
	}

	public static void _reset_unsafe_add_(BroadcastMsgList bean) {
		poolBroadcastMsgList._reset_unsafe_add_(bean);
	}

	public static void padd(BroadcastMsgList bean) {
		mkdb.Procedure.padd(bean, poolBroadcastMsgList);
	}

	public static BroadcastMsgList newBroadcastMsgListData() {
		return new xbean.__.BroadcastMsgList.Data();
	}

	public static mkdb.util.BeanPool<BroadcastMsg> poolBroadcastMsg = new mkdb.util.BeanPool<BroadcastMsg>() {
		@Override
		protected BroadcastMsg newBean() {
			return new xbean.__.BroadcastMsg();
		}
	};

	public static BroadcastMsg newBroadcastMsg() {
		return poolBroadcastMsg.get();
	}

	public static void _reset_unsafe_add_(BroadcastMsg bean) {
		poolBroadcastMsg._reset_unsafe_add_(bean);
	}

	public static void padd(BroadcastMsg bean) {
		mkdb.Procedure.padd(bean, poolBroadcastMsg);
	}

	public static BroadcastMsg newBroadcastMsgData() {
		return new xbean.__.BroadcastMsg.Data();
	}

	public static mkdb.util.BeanPool<ClanIndex> poolClanIndex = new mkdb.util.BeanPool<ClanIndex>() {
		@Override
		protected ClanIndex newBean() {
			return new xbean.__.ClanIndex();
		}
	};

	public static ClanIndex newClanIndex() {
		return poolClanIndex.get();
	}

	public static void _reset_unsafe_add_(ClanIndex bean) {
		poolClanIndex._reset_unsafe_add_(bean);
	}

	public static void padd(ClanIndex bean) {
		mkdb.Procedure.padd(bean, poolClanIndex);
	}

	public static ClanIndex newClanIndexData() {
		return new xbean.__.ClanIndex.Data();
	}

	public static mkdb.util.BeanPool<ClanMemberInfo> poolClanMemberInfo = new mkdb.util.BeanPool<ClanMemberInfo>() {
		@Override
		protected ClanMemberInfo newBean() {
			return new xbean.__.ClanMemberInfo();
		}
	};

	public static ClanMemberInfo newClanMemberInfo() {
		return poolClanMemberInfo.get();
	}

	public static void _reset_unsafe_add_(ClanMemberInfo bean) {
		poolClanMemberInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ClanMemberInfo bean) {
		mkdb.Procedure.padd(bean, poolClanMemberInfo);
	}

	public static ClanMemberInfo newClanMemberInfoData() {
		return new xbean.__.ClanMemberInfo.Data();
	}

	public static mkdb.util.BeanPool<ClanSkill> poolClanSkill = new mkdb.util.BeanPool<ClanSkill>() {
		@Override
		protected ClanSkill newBean() {
			return new xbean.__.ClanSkill();
		}
	};

	public static ClanSkill newClanSkill() {
		return poolClanSkill.get();
	}

	public static void _reset_unsafe_add_(ClanSkill bean) {
		poolClanSkill._reset_unsafe_add_(bean);
	}

	public static void padd(ClanSkill bean) {
		mkdb.Procedure.padd(bean, poolClanSkill);
	}

	public static ClanSkill newClanSkillData() {
		return new xbean.__.ClanSkill.Data();
	}

	public static mkdb.util.BeanPool<DynamicMapInfo2> poolDynamicMapInfo2 = new mkdb.util.BeanPool<DynamicMapInfo2>() {
		@Override
		protected DynamicMapInfo2 newBean() {
			return new xbean.__.DynamicMapInfo2();
		}
	};

	public static DynamicMapInfo2 newDynamicMapInfo2() {
		return poolDynamicMapInfo2.get();
	}

	public static void _reset_unsafe_add_(DynamicMapInfo2 bean) {
		poolDynamicMapInfo2._reset_unsafe_add_(bean);
	}

	public static void padd(DynamicMapInfo2 bean) {
		mkdb.Procedure.padd(bean, poolDynamicMapInfo2);
	}

	public static DynamicMapInfo2 newDynamicMapInfo2Data() {
		return new xbean.__.DynamicMapInfo2.Data();
	}

	public static mkdb.util.BeanPool<ClanInfo> poolClanInfo = new mkdb.util.BeanPool<ClanInfo>() {
		@Override
		protected ClanInfo newBean() {
			return new xbean.__.ClanInfo();
		}
	};

	public static ClanInfo newClanInfo() {
		return poolClanInfo.get();
	}

	public static void _reset_unsafe_add_(ClanInfo bean) {
		poolClanInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ClanInfo bean) {
		mkdb.Procedure.padd(bean, poolClanInfo);
	}

	public static ClanInfo newClanInfoData() {
		return new xbean.__.ClanInfo.Data();
	}

	public static mkdb.util.BeanPool<ImpeachMent> poolImpeachMent = new mkdb.util.BeanPool<ImpeachMent>() {
		@Override
		protected ImpeachMent newBean() {
			return new xbean.__.ImpeachMent();
		}
	};

	public static ImpeachMent newImpeachMent() {
		return poolImpeachMent.get();
	}

	public static void _reset_unsafe_add_(ImpeachMent bean) {
		poolImpeachMent._reset_unsafe_add_(bean);
	}

	public static void padd(ImpeachMent bean) {
		mkdb.Procedure.padd(bean, poolImpeachMent);
	}

	public static ImpeachMent newImpeachMentData() {
		return new xbean.__.ImpeachMent.Data();
	}

	public static mkdb.util.BeanPool<RoleClanPoint> poolRoleClanPoint = new mkdb.util.BeanPool<RoleClanPoint>() {
		@Override
		protected RoleClanPoint newBean() {
			return new xbean.__.RoleClanPoint();
		}
	};

	public static RoleClanPoint newRoleClanPoint() {
		return poolRoleClanPoint.get();
	}

	public static void _reset_unsafe_add_(RoleClanPoint bean) {
		poolRoleClanPoint._reset_unsafe_add_(bean);
	}

	public static void padd(RoleClanPoint bean) {
		mkdb.Procedure.padd(bean, poolRoleClanPoint);
	}

	public static RoleClanPoint newRoleClanPointData() {
		return new xbean.__.RoleClanPoint.Data();
	}

	public static mkdb.util.BeanPool<ClanFight> poolClanFight = new mkdb.util.BeanPool<ClanFight>() {
		@Override
		protected ClanFight newBean() {
			return new xbean.__.ClanFight();
		}
	};

	public static ClanFight newClanFight() {
		return poolClanFight.get();
	}

	public static void _reset_unsafe_add_(ClanFight bean) {
		poolClanFight._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFight bean) {
		mkdb.Procedure.padd(bean, poolClanFight);
	}

	public static ClanFight newClanFightData() {
		return new xbean.__.ClanFight.Data();
	}

	public static mkdb.util.BeanPool<ClanFightBattleField> poolClanFightBattleField = new mkdb.util.BeanPool<ClanFightBattleField>() {
		@Override
		protected ClanFightBattleField newBean() {
			return new xbean.__.ClanFightBattleField();
		}
	};

	public static ClanFightBattleField newClanFightBattleField() {
		return poolClanFightBattleField.get();
	}

	public static void _reset_unsafe_add_(ClanFightBattleField bean) {
		poolClanFightBattleField._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFightBattleField bean) {
		mkdb.Procedure.padd(bean, poolClanFightBattleField);
	}

	public static ClanFightBattleField newClanFightBattleFieldData() {
		return new xbean.__.ClanFightBattleField.Data();
	}

	public static mkdb.util.BeanPool<ClanFights> poolClanFights = new mkdb.util.BeanPool<ClanFights>() {
		@Override
		protected ClanFights newBean() {
			return new xbean.__.ClanFights();
		}
	};

	public static ClanFights newClanFights() {
		return poolClanFights.get();
	}

	public static void _reset_unsafe_add_(ClanFights bean) {
		poolClanFights._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFights bean) {
		mkdb.Procedure.padd(bean, poolClanFights);
	}

	public static ClanFights newClanFightsData() {
		return new xbean.__.ClanFights.Data();
	}

	public static mkdb.util.BeanPool<EClanfightids> poolEClanfightids = new mkdb.util.BeanPool<EClanfightids>() {
		@Override
		protected EClanfightids newBean() {
			return new xbean.__.EClanfightids();
		}
	};

	public static EClanfightids newEClanfightids() {
		return poolEClanfightids.get();
	}

	public static void _reset_unsafe_add_(EClanfightids bean) {
		poolEClanfightids._reset_unsafe_add_(bean);
	}

	public static void padd(EClanfightids bean) {
		mkdb.Procedure.padd(bean, poolEClanfightids);
	}

	public static EClanfightids newEClanfightidsData() {
		return new xbean.__.EClanfightids.Data();
	}

	public static mkdb.util.BeanPool<EClanFightStatistics> poolEClanFightStatistics = new mkdb.util.BeanPool<EClanFightStatistics>() {
		@Override
		protected EClanFightStatistics newBean() {
			return new xbean.__.EClanFightStatistics();
		}
	};

	public static EClanFightStatistics newEClanFightStatistics() {
		return poolEClanFightStatistics.get();
	}

	public static void _reset_unsafe_add_(EClanFightStatistics bean) {
		poolEClanFightStatistics._reset_unsafe_add_(bean);
	}

	public static void padd(EClanFightStatistics bean) {
		mkdb.Procedure.padd(bean, poolEClanFightStatistics);
	}

	public static EClanFightStatistics newEClanFightStatisticsData() {
		return new xbean.__.EClanFightStatistics.Data();
	}

	public static mkdb.util.BeanPool<BattleFieldScroeRank> poolBattleFieldScroeRank = new mkdb.util.BeanPool<BattleFieldScroeRank>() {
		@Override
		protected BattleFieldScroeRank newBean() {
			return new xbean.__.BattleFieldScroeRank();
		}
	};

	public static BattleFieldScroeRank newBattleFieldScroeRank() {
		return poolBattleFieldScroeRank.get();
	}

	public static void _reset_unsafe_add_(BattleFieldScroeRank bean) {
		poolBattleFieldScroeRank._reset_unsafe_add_(bean);
	}

	public static void padd(BattleFieldScroeRank bean) {
		mkdb.Procedure.padd(bean, poolBattleFieldScroeRank);
	}

	public static BattleFieldScroeRank newBattleFieldScroeRankData() {
		return new xbean.__.BattleFieldScroeRank.Data();
	}

	public static mkdb.util.BeanPool<ClanEventRecord> poolClanEventRecord = new mkdb.util.BeanPool<ClanEventRecord>() {
		@Override
		protected ClanEventRecord newBean() {
			return new xbean.__.ClanEventRecord();
		}
	};

	public static ClanEventRecord newClanEventRecord() {
		return poolClanEventRecord.get();
	}

	public static void _reset_unsafe_add_(ClanEventRecord bean) {
		poolClanEventRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ClanEventRecord bean) {
		mkdb.Procedure.padd(bean, poolClanEventRecord);
	}

	public static ClanEventRecord newClanEventRecordData() {
		return new xbean.__.ClanEventRecord.Data();
	}

	public static mkdb.util.BeanPool<MedicItem> poolMedicItem = new mkdb.util.BeanPool<MedicItem>() {
		@Override
		protected MedicItem newBean() {
			return new xbean.__.MedicItem();
		}
	};

	public static MedicItem newMedicItem() {
		return poolMedicItem.get();
	}

	public static void _reset_unsafe_add_(MedicItem bean) {
		poolMedicItem._reset_unsafe_add_(bean);
	}

	public static void padd(MedicItem bean) {
		mkdb.Procedure.padd(bean, poolMedicItem);
	}

	public static MedicItem newMedicItemData() {
		return new xbean.__.MedicItem.Data();
	}

	public static mkdb.util.BeanPool<MedicItemList> poolMedicItemList = new mkdb.util.BeanPool<MedicItemList>() {
		@Override
		protected MedicItemList newBean() {
			return new xbean.__.MedicItemList();
		}
	};

	public static MedicItemList newMedicItemList() {
		return poolMedicItemList.get();
	}

	public static void _reset_unsafe_add_(MedicItemList bean) {
		poolMedicItemList._reset_unsafe_add_(bean);
	}

	public static void padd(MedicItemList bean) {
		mkdb.Procedure.padd(bean, poolMedicItemList);
	}

	public static MedicItemList newMedicItemListData() {
		return new xbean.__.MedicItemList.Data();
	}

	public static mkdb.util.BeanPool<ClanWeekNum> poolClanWeekNum = new mkdb.util.BeanPool<ClanWeekNum>() {
		@Override
		protected ClanWeekNum newBean() {
			return new xbean.__.ClanWeekNum();
		}
	};

	public static ClanWeekNum newClanWeekNum() {
		return poolClanWeekNum.get();
	}

	public static void _reset_unsafe_add_(ClanWeekNum bean) {
		poolClanWeekNum._reset_unsafe_add_(bean);
	}

	public static void padd(ClanWeekNum bean) {
		mkdb.Procedure.padd(bean, poolClanWeekNum);
	}

	public static ClanWeekNum newClanWeekNumData() {
		return new xbean.__.ClanWeekNum.Data();
	}

	public static mkdb.util.BeanPool<BuyMedicItemNum> poolBuyMedicItemNum = new mkdb.util.BeanPool<BuyMedicItemNum>() {
		@Override
		protected BuyMedicItemNum newBean() {
			return new xbean.__.BuyMedicItemNum();
		}
	};

	public static BuyMedicItemNum newBuyMedicItemNum() {
		return poolBuyMedicItemNum.get();
	}

	public static void _reset_unsafe_add_(BuyMedicItemNum bean) {
		poolBuyMedicItemNum._reset_unsafe_add_(bean);
	}

	public static void padd(BuyMedicItemNum bean) {
		mkdb.Procedure.padd(bean, poolBuyMedicItemNum);
	}

	public static BuyMedicItemNum newBuyMedicItemNumData() {
		return new xbean.__.BuyMedicItemNum.Data();
	}

	public static mkdb.util.BeanPool<RuneInfo> poolRuneInfo = new mkdb.util.BeanPool<RuneInfo>() {
		@Override
		protected RuneInfo newBean() {
			return new xbean.__.RuneInfo();
		}
	};

	public static RuneInfo newRuneInfo() {
		return poolRuneInfo.get();
	}

	public static void _reset_unsafe_add_(RuneInfo bean) {
		poolRuneInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RuneInfo bean) {
		mkdb.Procedure.padd(bean, poolRuneInfo);
	}

	public static RuneInfo newRuneInfoData() {
		return new xbean.__.RuneInfo.Data();
	}

	public static mkdb.util.BeanPool<RequestRuneInfo> poolRequestRuneInfo = new mkdb.util.BeanPool<RequestRuneInfo>() {
		@Override
		protected RequestRuneInfo newBean() {
			return new xbean.__.RequestRuneInfo();
		}
	};

	public static RequestRuneInfo newRequestRuneInfo() {
		return poolRequestRuneInfo.get();
	}

	public static void _reset_unsafe_add_(RequestRuneInfo bean) {
		poolRequestRuneInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RequestRuneInfo bean) {
		mkdb.Procedure.padd(bean, poolRequestRuneInfo);
	}

	public static RequestRuneInfo newRequestRuneInfoData() {
		return new xbean.__.RequestRuneInfo.Data();
	}

	public static mkdb.util.BeanPool<RequestRuneInfoList> poolRequestRuneInfoList = new mkdb.util.BeanPool<RequestRuneInfoList>() {
		@Override
		protected RequestRuneInfoList newBean() {
			return new xbean.__.RequestRuneInfoList();
		}
	};

	public static RequestRuneInfoList newRequestRuneInfoList() {
		return poolRequestRuneInfoList.get();
	}

	public static void _reset_unsafe_add_(RequestRuneInfoList bean) {
		poolRequestRuneInfoList._reset_unsafe_add_(bean);
	}

	public static void padd(RequestRuneInfoList bean) {
		mkdb.Procedure.padd(bean, poolRequestRuneInfoList);
	}

	public static RequestRuneInfoList newRequestRuneInfoListData() {
		return new xbean.__.RequestRuneInfoList.Data();
	}

	public static mkdb.util.BeanPool<RoleApplyClan> poolRoleApplyClan = new mkdb.util.BeanPool<RoleApplyClan>() {
		@Override
		protected RoleApplyClan newBean() {
			return new xbean.__.RoleApplyClan();
		}
	};

	public static RoleApplyClan newRoleApplyClan() {
		return poolRoleApplyClan.get();
	}

	public static void _reset_unsafe_add_(RoleApplyClan bean) {
		poolRoleApplyClan._reset_unsafe_add_(bean);
	}

	public static void padd(RoleApplyClan bean) {
		mkdb.Procedure.padd(bean, poolRoleApplyClan);
	}

	public static RoleApplyClan newRoleApplyClanData() {
		return new xbean.__.RoleApplyClan.Data();
	}

	public static mkdb.util.BeanPool<RoleApplyClanList> poolRoleApplyClanList = new mkdb.util.BeanPool<RoleApplyClanList>() {
		@Override
		protected RoleApplyClanList newBean() {
			return new xbean.__.RoleApplyClanList();
		}
	};

	public static RoleApplyClanList newRoleApplyClanList() {
		return poolRoleApplyClanList.get();
	}

	public static void _reset_unsafe_add_(RoleApplyClanList bean) {
		poolRoleApplyClanList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleApplyClanList bean) {
		mkdb.Procedure.padd(bean, poolRoleApplyClanList);
	}

	public static RoleApplyClanList newRoleApplyClanListData() {
		return new xbean.__.RoleApplyClanList.Data();
	}

	public static mkdb.util.BeanPool<ClanRankList> poolClanRankList = new mkdb.util.BeanPool<ClanRankList>() {
		@Override
		protected ClanRankList newBean() {
			return new xbean.__.ClanRankList();
		}
	};

	public static ClanRankList newClanRankList() {
		return poolClanRankList.get();
	}

	public static void _reset_unsafe_add_(ClanRankList bean) {
		poolClanRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ClanRankList bean) {
		mkdb.Procedure.padd(bean, poolClanRankList);
	}

	public static ClanRankList newClanRankListData() {
		return new xbean.__.ClanRankList.Data();
	}

	public static mkdb.util.BeanPool<ClanRankRecord> poolClanRankRecord = new mkdb.util.BeanPool<ClanRankRecord>() {
		@Override
		protected ClanRankRecord newBean() {
			return new xbean.__.ClanRankRecord();
		}
	};

	public static ClanRankRecord newClanRankRecord() {
		return poolClanRankRecord.get();
	}

	public static void _reset_unsafe_add_(ClanRankRecord bean) {
		poolClanRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ClanRankRecord bean) {
		mkdb.Procedure.padd(bean, poolClanRankRecord);
	}

	public static ClanRankRecord newClanRankRecordData() {
		return new xbean.__.ClanRankRecord.Data();
	}

	public static mkdb.util.BeanPool<ClanZongHeRankList> poolClanZongHeRankList = new mkdb.util.BeanPool<ClanZongHeRankList>() {
		@Override
		protected ClanZongHeRankList newBean() {
			return new xbean.__.ClanZongHeRankList();
		}
	};

	public static ClanZongHeRankList newClanZongHeRankList() {
		return poolClanZongHeRankList.get();
	}

	public static void _reset_unsafe_add_(ClanZongHeRankList bean) {
		poolClanZongHeRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ClanZongHeRankList bean) {
		mkdb.Procedure.padd(bean, poolClanZongHeRankList);
	}

	public static ClanZongHeRankList newClanZongHeRankListData() {
		return new xbean.__.ClanZongHeRankList.Data();
	}

	public static mkdb.util.BeanPool<ClanZongHeRankRecord> poolClanZongHeRankRecord = new mkdb.util.BeanPool<ClanZongHeRankRecord>() {
		@Override
		protected ClanZongHeRankRecord newBean() {
			return new xbean.__.ClanZongHeRankRecord();
		}
	};

	public static ClanZongHeRankRecord newClanZongHeRankRecord() {
		return poolClanZongHeRankRecord.get();
	}

	public static void _reset_unsafe_add_(ClanZongHeRankRecord bean) {
		poolClanZongHeRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ClanZongHeRankRecord bean) {
		mkdb.Procedure.padd(bean, poolClanZongHeRankRecord);
	}

	public static ClanZongHeRankRecord newClanZongHeRankRecordData() {
		return new xbean.__.ClanZongHeRankRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleZongheRankList> poolRoleZongheRankList = new mkdb.util.BeanPool<RoleZongheRankList>() {
		@Override
		protected RoleZongheRankList newBean() {
			return new xbean.__.RoleZongheRankList();
		}
	};

	public static RoleZongheRankList newRoleZongheRankList() {
		return poolRoleZongheRankList.get();
	}

	public static void _reset_unsafe_add_(RoleZongheRankList bean) {
		poolRoleZongheRankList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleZongheRankList bean) {
		mkdb.Procedure.padd(bean, poolRoleZongheRankList);
	}

	public static RoleZongheRankList newRoleZongheRankListData() {
		return new xbean.__.RoleZongheRankList.Data();
	}

	public static mkdb.util.BeanPool<RoleZongheRankRecord> poolRoleZongheRankRecord = new mkdb.util.BeanPool<RoleZongheRankRecord>() {
		@Override
		protected RoleZongheRankRecord newBean() {
			return new xbean.__.RoleZongheRankRecord();
		}
	};

	public static RoleZongheRankRecord newRoleZongheRankRecord() {
		return poolRoleZongheRankRecord.get();
	}

	public static void _reset_unsafe_add_(RoleZongheRankRecord bean) {
		poolRoleZongheRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleZongheRankRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleZongheRankRecord);
	}

	public static RoleZongheRankRecord newRoleZongheRankRecordData() {
		return new xbean.__.RoleZongheRankRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleProfessionRankRecord> poolRoleProfessionRankRecord = new mkdb.util.BeanPool<RoleProfessionRankRecord>() {
		@Override
		protected RoleProfessionRankRecord newBean() {
			return new xbean.__.RoleProfessionRankRecord();
		}
	};

	public static RoleProfessionRankRecord newRoleProfessionRankRecord() {
		return poolRoleProfessionRankRecord.get();
	}

	public static void _reset_unsafe_add_(RoleProfessionRankRecord bean) {
		poolRoleProfessionRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleProfessionRankRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleProfessionRankRecord);
	}

	public static RoleProfessionRankRecord newRoleProfessionRankRecordData() {
		return new xbean.__.RoleProfessionRankRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleProfessionRankList> poolRoleProfessionRankList = new mkdb.util.BeanPool<RoleProfessionRankList>() {
		@Override
		protected RoleProfessionRankList newBean() {
			return new xbean.__.RoleProfessionRankList();
		}
	};

	public static RoleProfessionRankList newRoleProfessionRankList() {
		return poolRoleProfessionRankList.get();
	}

	public static void _reset_unsafe_add_(RoleProfessionRankList bean) {
		poolRoleProfessionRankList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleProfessionRankList bean) {
		mkdb.Procedure.padd(bean, poolRoleProfessionRankList);
	}

	public static RoleProfessionRankList newRoleProfessionRankListData() {
		return new xbean.__.RoleProfessionRankList.Data();
	}

	public static mkdb.util.BeanPool<ClanProgressRankRecord> poolClanProgressRankRecord = new mkdb.util.BeanPool<ClanProgressRankRecord>() {
		@Override
		protected ClanProgressRankRecord newBean() {
			return new xbean.__.ClanProgressRankRecord();
		}
	};

	public static ClanProgressRankRecord newClanProgressRankRecord() {
		return poolClanProgressRankRecord.get();
	}

	public static void _reset_unsafe_add_(ClanProgressRankRecord bean) {
		poolClanProgressRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ClanProgressRankRecord bean) {
		mkdb.Procedure.padd(bean, poolClanProgressRankRecord);
	}

	public static ClanProgressRankRecord newClanProgressRankRecordData() {
		return new xbean.__.ClanProgressRankRecord.Data();
	}

	public static mkdb.util.BeanPool<ClanProgressRankList> poolClanProgressRankList = new mkdb.util.BeanPool<ClanProgressRankList>() {
		@Override
		protected ClanProgressRankList newBean() {
			return new xbean.__.ClanProgressRankList();
		}
	};

	public static ClanProgressRankList newClanProgressRankList() {
		return poolClanProgressRankList.get();
	}

	public static void _reset_unsafe_add_(ClanProgressRankList bean) {
		poolClanProgressRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ClanProgressRankList bean) {
		mkdb.Procedure.padd(bean, poolClanProgressRankList);
	}

	public static ClanProgressRankList newClanProgressRankListData() {
		return new xbean.__.ClanProgressRankList.Data();
	}

	public static mkdb.util.BeanPool<RoleRankRecord> poolRoleRankRecord = new mkdb.util.BeanPool<RoleRankRecord>() {
		@Override
		protected RoleRankRecord newBean() {
			return new xbean.__.RoleRankRecord();
		}
	};

	public static RoleRankRecord newRoleRankRecord() {
		return poolRoleRankRecord.get();
	}

	public static void _reset_unsafe_add_(RoleRankRecord bean) {
		poolRoleRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleRankRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleRankRecord);
	}

	public static RoleRankRecord newRoleRankRecordData() {
		return new xbean.__.RoleRankRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleRankList> poolRoleRankList = new mkdb.util.BeanPool<RoleRankList>() {
		@Override
		protected RoleRankList newBean() {
			return new xbean.__.RoleRankList();
		}
	};

	public static RoleRankList newRoleRankList() {
		return poolRoleRankList.get();
	}

	public static void _reset_unsafe_add_(RoleRankList bean) {
		poolRoleRankList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleRankList bean) {
		mkdb.Procedure.padd(bean, poolRoleRankList);
	}

	public static RoleRankList newRoleRankListData() {
		return new xbean.__.RoleRankList.Data();
	}

	public static mkdb.util.BeanPool<RoleLevelRankList> poolRoleLevelRankList = new mkdb.util.BeanPool<RoleLevelRankList>() {
		@Override
		protected RoleLevelRankList newBean() {
			return new xbean.__.RoleLevelRankList();
		}
	};

	public static RoleLevelRankList newRoleLevelRankList() {
		return poolRoleLevelRankList.get();
	}

	public static void _reset_unsafe_add_(RoleLevelRankList bean) {
		poolRoleLevelRankList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleLevelRankList bean) {
		mkdb.Procedure.padd(bean, poolRoleLevelRankList);
	}

	public static RoleLevelRankList newRoleLevelRankListData() {
		return new xbean.__.RoleLevelRankList.Data();
	}

	public static mkdb.util.BeanPool<RoleLevelListRecord> poolRoleLevelListRecord = new mkdb.util.BeanPool<RoleLevelListRecord>() {
		@Override
		protected RoleLevelListRecord newBean() {
			return new xbean.__.RoleLevelListRecord();
		}
	};

	public static RoleLevelListRecord newRoleLevelListRecord() {
		return poolRoleLevelListRecord.get();
	}

	public static void _reset_unsafe_add_(RoleLevelListRecord bean) {
		poolRoleLevelListRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleLevelListRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleLevelListRecord);
	}

	public static RoleLevelListRecord newRoleLevelListRecordData() {
		return new xbean.__.RoleLevelListRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalRoleLevelRecord> poolMarshalRoleLevelRecord = new mkdb.util.BeanPool<MarshalRoleLevelRecord>() {
		@Override
		protected MarshalRoleLevelRecord newBean() {
			return new xbean.__.MarshalRoleLevelRecord();
		}
	};

	public static MarshalRoleLevelRecord newMarshalRoleLevelRecord() {
		return poolMarshalRoleLevelRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalRoleLevelRecord bean) {
		poolMarshalRoleLevelRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalRoleLevelRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalRoleLevelRecord);
	}

	public static MarshalRoleLevelRecord newMarshalRoleLevelRecordData() {
		return new xbean.__.MarshalRoleLevelRecord.Data();
	}

	public static mkdb.util.BeanPool<ApprenticelListRecord> poolApprenticelListRecord = new mkdb.util.BeanPool<ApprenticelListRecord>() {
		@Override
		protected ApprenticelListRecord newBean() {
			return new xbean.__.ApprenticelListRecord();
		}
	};

	public static ApprenticelListRecord newApprenticelListRecord() {
		return poolApprenticelListRecord.get();
	}

	public static void _reset_unsafe_add_(ApprenticelListRecord bean) {
		poolApprenticelListRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ApprenticelListRecord bean) {
		mkdb.Procedure.padd(bean, poolApprenticelListRecord);
	}

	public static ApprenticelListRecord newApprenticelListRecordData() {
		return new xbean.__.ApprenticelListRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalApprenticeRecord> poolMarshalApprenticeRecord = new mkdb.util.BeanPool<MarshalApprenticeRecord>() {
		@Override
		protected MarshalApprenticeRecord newBean() {
			return new xbean.__.MarshalApprenticeRecord();
		}
	};

	public static MarshalApprenticeRecord newMarshalApprenticeRecord() {
		return poolMarshalApprenticeRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalApprenticeRecord bean) {
		poolMarshalApprenticeRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalApprenticeRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalApprenticeRecord);
	}

	public static MarshalApprenticeRecord newMarshalApprenticeRecordData() {
		return new xbean.__.MarshalApprenticeRecord.Data();
	}

	public static mkdb.util.BeanPool<RedPackRankList> poolRedPackRankList = new mkdb.util.BeanPool<RedPackRankList>() {
		@Override
		protected RedPackRankList newBean() {
			return new xbean.__.RedPackRankList();
		}
	};

	public static RedPackRankList newRedPackRankList() {
		return poolRedPackRankList.get();
	}

	public static void _reset_unsafe_add_(RedPackRankList bean) {
		poolRedPackRankList._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackRankList bean) {
		mkdb.Procedure.padd(bean, poolRedPackRankList);
	}

	public static RedPackRankList newRedPackRankListData() {
		return new xbean.__.RedPackRankList.Data();
	}

	public static mkdb.util.BeanPool<RedPackRecord> poolRedPackRecord = new mkdb.util.BeanPool<RedPackRecord>() {
		@Override
		protected RedPackRecord newBean() {
			return new xbean.__.RedPackRecord();
		}
	};

	public static RedPackRecord newRedPackRecord() {
		return poolRedPackRecord.get();
	}

	public static void _reset_unsafe_add_(RedPackRecord bean) {
		poolRedPackRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackRecord bean) {
		mkdb.Procedure.padd(bean, poolRedPackRecord);
	}

	public static RedPackRecord newRedPackRecordData() {
		return new xbean.__.RedPackRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalRedPackRecord> poolMarshalRedPackRecord = new mkdb.util.BeanPool<MarshalRedPackRecord>() {
		@Override
		protected MarshalRedPackRecord newBean() {
			return new xbean.__.MarshalRedPackRecord();
		}
	};

	public static MarshalRedPackRecord newMarshalRedPackRecord() {
		return poolMarshalRedPackRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalRedPackRecord bean) {
		poolMarshalRedPackRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalRedPackRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalRedPackRecord);
	}

	public static MarshalRedPackRecord newMarshalRedPackRecordData() {
		return new xbean.__.MarshalRedPackRecord.Data();
	}

	public static mkdb.util.BeanPool<RedPack> poolRedPack = new mkdb.util.BeanPool<RedPack>() {
		@Override
		protected RedPack newBean() {
			return new xbean.__.RedPack();
		}
	};

	public static RedPack newRedPack() {
		return poolRedPack.get();
	}

	public static void _reset_unsafe_add_(RedPack bean) {
		poolRedPack._reset_unsafe_add_(bean);
	}

	public static void padd(RedPack bean) {
		mkdb.Procedure.padd(bean, poolRedPack);
	}

	public static RedPack newRedPackData() {
		return new xbean.__.RedPack.Data();
	}

	public static mkdb.util.BeanPool<GiveFlowerRankList> poolGiveFlowerRankList = new mkdb.util.BeanPool<GiveFlowerRankList>() {
		@Override
		protected GiveFlowerRankList newBean() {
			return new xbean.__.GiveFlowerRankList();
		}
	};

	public static GiveFlowerRankList newGiveFlowerRankList() {
		return poolGiveFlowerRankList.get();
	}

	public static void _reset_unsafe_add_(GiveFlowerRankList bean) {
		poolGiveFlowerRankList._reset_unsafe_add_(bean);
	}

	public static void padd(GiveFlowerRankList bean) {
		mkdb.Procedure.padd(bean, poolGiveFlowerRankList);
	}

	public static GiveFlowerRankList newGiveFlowerRankListData() {
		return new xbean.__.GiveFlowerRankList.Data();
	}

	public static mkdb.util.BeanPool<GiveFlowerRecord> poolGiveFlowerRecord = new mkdb.util.BeanPool<GiveFlowerRecord>() {
		@Override
		protected GiveFlowerRecord newBean() {
			return new xbean.__.GiveFlowerRecord();
		}
	};

	public static GiveFlowerRecord newGiveFlowerRecord() {
		return poolGiveFlowerRecord.get();
	}

	public static void _reset_unsafe_add_(GiveFlowerRecord bean) {
		poolGiveFlowerRecord._reset_unsafe_add_(bean);
	}

	public static void padd(GiveFlowerRecord bean) {
		mkdb.Procedure.padd(bean, poolGiveFlowerRecord);
	}

	public static GiveFlowerRecord newGiveFlowerRecordData() {
		return new xbean.__.GiveFlowerRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalGiveFlowerRecord> poolMarshalGiveFlowerRecord = new mkdb.util.BeanPool<MarshalGiveFlowerRecord>() {
		@Override
		protected MarshalGiveFlowerRecord newBean() {
			return new xbean.__.MarshalGiveFlowerRecord();
		}
	};

	public static MarshalGiveFlowerRecord newMarshalGiveFlowerRecord() {
		return poolMarshalGiveFlowerRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalGiveFlowerRecord bean) {
		poolMarshalGiveFlowerRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalGiveFlowerRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalGiveFlowerRecord);
	}

	public static MarshalGiveFlowerRecord newMarshalGiveFlowerRecordData() {
		return new xbean.__.MarshalGiveFlowerRecord.Data();
	}

	public static mkdb.util.BeanPool<ReceFlowerRankList> poolReceFlowerRankList = new mkdb.util.BeanPool<ReceFlowerRankList>() {
		@Override
		protected ReceFlowerRankList newBean() {
			return new xbean.__.ReceFlowerRankList();
		}
	};

	public static ReceFlowerRankList newReceFlowerRankList() {
		return poolReceFlowerRankList.get();
	}

	public static void _reset_unsafe_add_(ReceFlowerRankList bean) {
		poolReceFlowerRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ReceFlowerRankList bean) {
		mkdb.Procedure.padd(bean, poolReceFlowerRankList);
	}

	public static ReceFlowerRankList newReceFlowerRankListData() {
		return new xbean.__.ReceFlowerRankList.Data();
	}

	public static mkdb.util.BeanPool<ReceFlowerRecord> poolReceFlowerRecord = new mkdb.util.BeanPool<ReceFlowerRecord>() {
		@Override
		protected ReceFlowerRecord newBean() {
			return new xbean.__.ReceFlowerRecord();
		}
	};

	public static ReceFlowerRecord newReceFlowerRecord() {
		return poolReceFlowerRecord.get();
	}

	public static void _reset_unsafe_add_(ReceFlowerRecord bean) {
		poolReceFlowerRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ReceFlowerRecord bean) {
		mkdb.Procedure.padd(bean, poolReceFlowerRecord);
	}

	public static ReceFlowerRecord newReceFlowerRecordData() {
		return new xbean.__.ReceFlowerRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalReceFlowerRecord> poolMarshalReceFlowerRecord = new mkdb.util.BeanPool<MarshalReceFlowerRecord>() {
		@Override
		protected MarshalReceFlowerRecord newBean() {
			return new xbean.__.MarshalReceFlowerRecord();
		}
	};

	public static MarshalReceFlowerRecord newMarshalReceFlowerRecord() {
		return poolMarshalReceFlowerRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalReceFlowerRecord bean) {
		poolMarshalReceFlowerRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalReceFlowerRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalReceFlowerRecord);
	}

	public static MarshalReceFlowerRecord newMarshalReceFlowerRecordData() {
		return new xbean.__.MarshalReceFlowerRecord.Data();
	}

	public static mkdb.util.BeanPool<WeekGiveReceGift> poolWeekGiveReceGift = new mkdb.util.BeanPool<WeekGiveReceGift>() {
		@Override
		protected WeekGiveReceGift newBean() {
			return new xbean.__.WeekGiveReceGift();
		}
	};

	public static WeekGiveReceGift newWeekGiveReceGift() {
		return poolWeekGiveReceGift.get();
	}

	public static void _reset_unsafe_add_(WeekGiveReceGift bean) {
		poolWeekGiveReceGift._reset_unsafe_add_(bean);
	}

	public static void padd(WeekGiveReceGift bean) {
		mkdb.Procedure.padd(bean, poolWeekGiveReceGift);
	}

	public static WeekGiveReceGift newWeekGiveReceGiftData() {
		return new xbean.__.WeekGiveReceGift.Data();
	}

	public static mkdb.util.BeanPool<GiveReceGift> poolGiveReceGift = new mkdb.util.BeanPool<GiveReceGift>() {
		@Override
		protected GiveReceGift newBean() {
			return new xbean.__.GiveReceGift();
		}
	};

	public static GiveReceGift newGiveReceGift() {
		return poolGiveReceGift.get();
	}

	public static void _reset_unsafe_add_(GiveReceGift bean) {
		poolGiveReceGift._reset_unsafe_add_(bean);
	}

	public static void padd(GiveReceGift bean) {
		mkdb.Procedure.padd(bean, poolGiveReceGift);
	}

	public static GiveReceGift newGiveReceGiftData() {
		return new xbean.__.GiveReceGift.Data();
	}

	public static mkdb.util.BeanPool<MarshalClanFightRaceRankRecord> poolMarshalClanFightRaceRankRecord = new mkdb.util.BeanPool<MarshalClanFightRaceRankRecord>() {
		@Override
		protected MarshalClanFightRaceRankRecord newBean() {
			return new xbean.__.MarshalClanFightRaceRankRecord();
		}
	};

	public static MarshalClanFightRaceRankRecord newMarshalClanFightRaceRankRecord() {
		return poolMarshalClanFightRaceRankRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalClanFightRaceRankRecord bean) {
		poolMarshalClanFightRaceRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalClanFightRaceRankRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalClanFightRaceRankRecord);
	}

	public static MarshalClanFightRaceRankRecord newMarshalClanFightRaceRankRecordData() {
		return new xbean.__.MarshalClanFightRaceRankRecord.Data();
	}

	public static mkdb.util.BeanPool<ClanFightRaceRankRecord> poolClanFightRaceRankRecord = new mkdb.util.BeanPool<ClanFightRaceRankRecord>() {
		@Override
		protected ClanFightRaceRankRecord newBean() {
			return new xbean.__.ClanFightRaceRankRecord();
		}
	};

	public static ClanFightRaceRankRecord newClanFightRaceRankRecord() {
		return poolClanFightRaceRankRecord.get();
	}

	public static void _reset_unsafe_add_(ClanFightRaceRankRecord bean) {
		poolClanFightRaceRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFightRaceRankRecord bean) {
		mkdb.Procedure.padd(bean, poolClanFightRaceRankRecord);
	}

	public static ClanFightRaceRankRecord newClanFightRaceRankRecordData() {
		return new xbean.__.ClanFightRaceRankRecord.Data();
	}

	public static mkdb.util.BeanPool<ClanFightRaceRankList> poolClanFightRaceRankList = new mkdb.util.BeanPool<ClanFightRaceRankList>() {
		@Override
		protected ClanFightRaceRankList newBean() {
			return new xbean.__.ClanFightRaceRankList();
		}
	};

	public static ClanFightRaceRankList newClanFightRaceRankList() {
		return poolClanFightRaceRankList.get();
	}

	public static void _reset_unsafe_add_(ClanFightRaceRankList bean) {
		poolClanFightRaceRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFightRaceRankList bean) {
		mkdb.Procedure.padd(bean, poolClanFightRaceRankList);
	}

	public static ClanFightRaceRankList newClanFightRaceRankListData() {
		return new xbean.__.ClanFightRaceRankList.Data();
	}

	public static mkdb.util.BeanPool<MarshalClanFightHistroyRankRecord> poolMarshalClanFightHistroyRankRecord = new mkdb.util.BeanPool<MarshalClanFightHistroyRankRecord>() {
		@Override
		protected MarshalClanFightHistroyRankRecord newBean() {
			return new xbean.__.MarshalClanFightHistroyRankRecord();
		}
	};

	public static MarshalClanFightHistroyRankRecord newMarshalClanFightHistroyRankRecord() {
		return poolMarshalClanFightHistroyRankRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalClanFightHistroyRankRecord bean) {
		poolMarshalClanFightHistroyRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalClanFightHistroyRankRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalClanFightHistroyRankRecord);
	}

	public static MarshalClanFightHistroyRankRecord newMarshalClanFightHistroyRankRecordData() {
		return new xbean.__.MarshalClanFightHistroyRankRecord.Data();
	}

	public static mkdb.util.BeanPool<ClanFightHistroyRankRecord> poolClanFightHistroyRankRecord = new mkdb.util.BeanPool<ClanFightHistroyRankRecord>() {
		@Override
		protected ClanFightHistroyRankRecord newBean() {
			return new xbean.__.ClanFightHistroyRankRecord();
		}
	};

	public static ClanFightHistroyRankRecord newClanFightHistroyRankRecord() {
		return poolClanFightHistroyRankRecord.get();
	}

	public static void _reset_unsafe_add_(ClanFightHistroyRankRecord bean) {
		poolClanFightHistroyRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFightHistroyRankRecord bean) {
		mkdb.Procedure.padd(bean, poolClanFightHistroyRankRecord);
	}

	public static ClanFightHistroyRankRecord newClanFightHistroyRankRecordData() {
		return new xbean.__.ClanFightHistroyRankRecord.Data();
	}

	public static mkdb.util.BeanPool<ClanFightHistroyRankList> poolClanFightHistroyRankList = new mkdb.util.BeanPool<ClanFightHistroyRankList>() {
		@Override
		protected ClanFightHistroyRankList newBean() {
			return new xbean.__.ClanFightHistroyRankList();
		}
	};

	public static ClanFightHistroyRankList newClanFightHistroyRankList() {
		return poolClanFightHistroyRankList.get();
	}

	public static void _reset_unsafe_add_(ClanFightHistroyRankList bean) {
		poolClanFightHistroyRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ClanFightHistroyRankList bean) {
		mkdb.Procedure.padd(bean, poolClanFightHistroyRankList);
	}

	public static ClanFightHistroyRankList newClanFightHistroyRankListData() {
		return new xbean.__.ClanFightHistroyRankList.Data();
	}

	public static mkdb.util.BeanPool<PetScoreRankList> poolPetScoreRankList = new mkdb.util.BeanPool<PetScoreRankList>() {
		@Override
		protected PetScoreRankList newBean() {
			return new xbean.__.PetScoreRankList();
		}
	};

	public static PetScoreRankList newPetScoreRankList() {
		return poolPetScoreRankList.get();
	}

	public static void _reset_unsafe_add_(PetScoreRankList bean) {
		poolPetScoreRankList._reset_unsafe_add_(bean);
	}

	public static void padd(PetScoreRankList bean) {
		mkdb.Procedure.padd(bean, poolPetScoreRankList);
	}

	public static PetScoreRankList newPetScoreRankListData() {
		return new xbean.__.PetScoreRankList.Data();
	}

	public static mkdb.util.BeanPool<PetScoreListRecord> poolPetScoreListRecord = new mkdb.util.BeanPool<PetScoreListRecord>() {
		@Override
		protected PetScoreListRecord newBean() {
			return new xbean.__.PetScoreListRecord();
		}
	};

	public static PetScoreListRecord newPetScoreListRecord() {
		return poolPetScoreListRecord.get();
	}

	public static void _reset_unsafe_add_(PetScoreListRecord bean) {
		poolPetScoreListRecord._reset_unsafe_add_(bean);
	}

	public static void padd(PetScoreListRecord bean) {
		mkdb.Procedure.padd(bean, poolPetScoreListRecord);
	}

	public static PetScoreListRecord newPetScoreListRecordData() {
		return new xbean.__.PetScoreListRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalPetScoreRecord> poolMarshalPetScoreRecord = new mkdb.util.BeanPool<MarshalPetScoreRecord>() {
		@Override
		protected MarshalPetScoreRecord newBean() {
			return new xbean.__.MarshalPetScoreRecord();
		}
	};

	public static MarshalPetScoreRecord newMarshalPetScoreRecord() {
		return poolMarshalPetScoreRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalPetScoreRecord bean) {
		poolMarshalPetScoreRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalPetScoreRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalPetScoreRecord);
	}

	public static MarshalPetScoreRecord newMarshalPetScoreRecordData() {
		return new xbean.__.MarshalPetScoreRecord.Data();
	}

	public static mkdb.util.BeanPool<PvP3ScoreRecord> poolPvP3ScoreRecord = new mkdb.util.BeanPool<PvP3ScoreRecord>() {
		@Override
		protected PvP3ScoreRecord newBean() {
			return new xbean.__.PvP3ScoreRecord();
		}
	};

	public static PvP3ScoreRecord newPvP3ScoreRecord() {
		return poolPvP3ScoreRecord.get();
	}

	public static void _reset_unsafe_add_(PvP3ScoreRecord bean) {
		poolPvP3ScoreRecord._reset_unsafe_add_(bean);
	}

	public static void padd(PvP3ScoreRecord bean) {
		mkdb.Procedure.padd(bean, poolPvP3ScoreRecord);
	}

	public static PvP3ScoreRecord newPvP3ScoreRecordData() {
		return new xbean.__.PvP3ScoreRecord.Data();
	}

	public static mkdb.util.BeanPool<PvP3ScoreRankList> poolPvP3ScoreRankList = new mkdb.util.BeanPool<PvP3ScoreRankList>() {
		@Override
		protected PvP3ScoreRankList newBean() {
			return new xbean.__.PvP3ScoreRankList();
		}
	};

	public static PvP3ScoreRankList newPvP3ScoreRankList() {
		return poolPvP3ScoreRankList.get();
	}

	public static void _reset_unsafe_add_(PvP3ScoreRankList bean) {
		poolPvP3ScoreRankList._reset_unsafe_add_(bean);
	}

	public static void padd(PvP3ScoreRankList bean) {
		mkdb.Procedure.padd(bean, poolPvP3ScoreRankList);
	}

	public static PvP3ScoreRankList newPvP3ScoreRankListData() {
		return new xbean.__.PvP3ScoreRankList.Data();
	}

	public static mkdb.util.BeanPool<PvP5ScoreRecord> poolPvP5ScoreRecord = new mkdb.util.BeanPool<PvP5ScoreRecord>() {
		@Override
		protected PvP5ScoreRecord newBean() {
			return new xbean.__.PvP5ScoreRecord();
		}
	};

	public static PvP5ScoreRecord newPvP5ScoreRecord() {
		return poolPvP5ScoreRecord.get();
	}

	public static void _reset_unsafe_add_(PvP5ScoreRecord bean) {
		poolPvP5ScoreRecord._reset_unsafe_add_(bean);
	}

	public static void padd(PvP5ScoreRecord bean) {
		mkdb.Procedure.padd(bean, poolPvP5ScoreRecord);
	}

	public static PvP5ScoreRecord newPvP5ScoreRecordData() {
		return new xbean.__.PvP5ScoreRecord.Data();
	}

	public static mkdb.util.BeanPool<PvP5ScoreRankList> poolPvP5ScoreRankList = new mkdb.util.BeanPool<PvP5ScoreRankList>() {
		@Override
		protected PvP5ScoreRankList newBean() {
			return new xbean.__.PvP5ScoreRankList();
		}
	};

	public static PvP5ScoreRankList newPvP5ScoreRankList() {
		return poolPvP5ScoreRankList.get();
	}

	public static void _reset_unsafe_add_(PvP5ScoreRankList bean) {
		poolPvP5ScoreRankList._reset_unsafe_add_(bean);
	}

	public static void padd(PvP5ScoreRankList bean) {
		mkdb.Procedure.padd(bean, poolPvP5ScoreRankList);
	}

	public static PvP5ScoreRankList newPvP5ScoreRankListData() {
		return new xbean.__.PvP5ScoreRankList.Data();
	}

	public static mkdb.util.BeanPool<BingFengRank> poolBingFengRank = new mkdb.util.BeanPool<BingFengRank>() {
		@Override
		protected BingFengRank newBean() {
			return new xbean.__.BingFengRank();
		}
	};

	public static BingFengRank newBingFengRank() {
		return poolBingFengRank.get();
	}

	public static void _reset_unsafe_add_(BingFengRank bean) {
		poolBingFengRank._reset_unsafe_add_(bean);
	}

	public static void padd(BingFengRank bean) {
		mkdb.Procedure.padd(bean, poolBingFengRank);
	}

	public static BingFengRank newBingFengRankData() {
		return new xbean.__.BingFengRank.Data();
	}

	public static mkdb.util.BeanPool<BingFengRankList> poolBingFengRankList = new mkdb.util.BeanPool<BingFengRankList>() {
		@Override
		protected BingFengRankList newBean() {
			return new xbean.__.BingFengRankList();
		}
	};

	public static BingFengRankList newBingFengRankList() {
		return poolBingFengRankList.get();
	}

	public static void _reset_unsafe_add_(BingFengRankList bean) {
		poolBingFengRankList._reset_unsafe_add_(bean);
	}

	public static void padd(BingFengRankList bean) {
		mkdb.Procedure.padd(bean, poolBingFengRankList);
	}

	public static BingFengRankList newBingFengRankListData() {
		return new xbean.__.BingFengRankList.Data();
	}

	public static mkdb.util.BeanPool<BingFengListRecord> poolBingFengListRecord = new mkdb.util.BeanPool<BingFengListRecord>() {
		@Override
		protected BingFengListRecord newBean() {
			return new xbean.__.BingFengListRecord();
		}
	};

	public static BingFengListRecord newBingFengListRecord() {
		return poolBingFengListRecord.get();
	}

	public static void _reset_unsafe_add_(BingFengListRecord bean) {
		poolBingFengListRecord._reset_unsafe_add_(bean);
	}

	public static void padd(BingFengListRecord bean) {
		mkdb.Procedure.padd(bean, poolBingFengListRecord);
	}

	public static BingFengListRecord newBingFengListRecordData() {
		return new xbean.__.BingFengListRecord.Data();
	}

	public static mkdb.util.BeanPool<MarshalBingFengRecord> poolMarshalBingFengRecord = new mkdb.util.BeanPool<MarshalBingFengRecord>() {
		@Override
		protected MarshalBingFengRecord newBean() {
			return new xbean.__.MarshalBingFengRecord();
		}
	};

	public static MarshalBingFengRecord newMarshalBingFengRecord() {
		return poolMarshalBingFengRecord.get();
	}

	public static void _reset_unsafe_add_(MarshalBingFengRecord bean) {
		poolMarshalBingFengRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MarshalBingFengRecord bean) {
		mkdb.Procedure.padd(bean, poolMarshalBingFengRecord);
	}

	public static MarshalBingFengRecord newMarshalBingFengRecordData() {
		return new xbean.__.MarshalBingFengRecord.Data();
	}

	public static mkdb.util.BeanPool<ProgressBarInfo> poolProgressBarInfo = new mkdb.util.BeanPool<ProgressBarInfo>() {
		@Override
		protected ProgressBarInfo newBean() {
			return new xbean.__.ProgressBarInfo();
		}
	};

	public static ProgressBarInfo newProgressBarInfo() {
		return poolProgressBarInfo.get();
	}

	public static void _reset_unsafe_add_(ProgressBarInfo bean) {
		poolProgressBarInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ProgressBarInfo bean) {
		mkdb.Procedure.padd(bean, poolProgressBarInfo);
	}

	public static ProgressBarInfo newProgressBarInfoData() {
		return new xbean.__.ProgressBarInfo.Data();
	}

	public static mkdb.util.BeanPool<TreasureMapInfo> poolTreasureMapInfo = new mkdb.util.BeanPool<TreasureMapInfo>() {
		@Override
		protected TreasureMapInfo newBean() {
			return new xbean.__.TreasureMapInfo();
		}
	};

	public static TreasureMapInfo newTreasureMapInfo() {
		return poolTreasureMapInfo.get();
	}

	public static void _reset_unsafe_add_(TreasureMapInfo bean) {
		poolTreasureMapInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TreasureMapInfo bean) {
		mkdb.Procedure.padd(bean, poolTreasureMapInfo);
	}

	public static TreasureMapInfo newTreasureMapInfoData() {
		return new xbean.__.TreasureMapInfo.Data();
	}

	public static mkdb.util.BeanPool<TraderWheel> poolTraderWheel = new mkdb.util.BeanPool<TraderWheel>() {
		@Override
		protected TraderWheel newBean() {
			return new xbean.__.TraderWheel();
		}
	};

	public static TraderWheel newTraderWheel() {
		return poolTraderWheel.get();
	}

	public static void _reset_unsafe_add_(TraderWheel bean) {
		poolTraderWheel._reset_unsafe_add_(bean);
	}

	public static void padd(TraderWheel bean) {
		mkdb.Procedure.padd(bean, poolTraderWheel);
	}

	public static TraderWheel newTraderWheelData() {
		return new xbean.__.TraderWheel.Data();
	}

	public static mkdb.util.BeanPool<SchoolWheel> poolSchoolWheel = new mkdb.util.BeanPool<SchoolWheel>() {
		@Override
		protected SchoolWheel newBean() {
			return new xbean.__.SchoolWheel();
		}
	};

	public static SchoolWheel newSchoolWheel() {
		return poolSchoolWheel.get();
	}

	public static void _reset_unsafe_add_(SchoolWheel bean) {
		poolSchoolWheel._reset_unsafe_add_(bean);
	}

	public static void padd(SchoolWheel bean) {
		mkdb.Procedure.padd(bean, poolSchoolWheel);
	}

	public static SchoolWheel newSchoolWheelData() {
		return new xbean.__.SchoolWheel.Data();
	}

	public static mkdb.util.BeanPool<ShowingMission> poolShowingMission = new mkdb.util.BeanPool<ShowingMission>() {
		@Override
		protected ShowingMission newBean() {
			return new xbean.__.ShowingMission();
		}
	};

	public static ShowingMission newShowingMission() {
		return poolShowingMission.get();
	}

	public static void _reset_unsafe_add_(ShowingMission bean) {
		poolShowingMission._reset_unsafe_add_(bean);
	}

	public static void padd(ShowingMission bean) {
		mkdb.Procedure.padd(bean, poolShowingMission);
	}

	public static ShowingMission newShowingMissionData() {
		return new xbean.__.ShowingMission.Data();
	}

	public static mkdb.util.BeanPool<CommitedMissions> poolCommitedMissions = new mkdb.util.BeanPool<CommitedMissions>() {
		@Override
		protected CommitedMissions newBean() {
			return new xbean.__.CommitedMissions();
		}
	};

	public static CommitedMissions newCommitedMissions() {
		return poolCommitedMissions.get();
	}

	public static void _reset_unsafe_add_(CommitedMissions bean) {
		poolCommitedMissions._reset_unsafe_add_(bean);
	}

	public static void padd(CommitedMissions bean) {
		mkdb.Procedure.padd(bean, poolCommitedMissions);
	}

	public static CommitedMissions newCommitedMissionsData() {
		return new xbean.__.CommitedMissions.Data();
	}

	public static mkdb.util.BeanPool<CommitedScenarioMission> poolCommitedScenarioMission = new mkdb.util.BeanPool<CommitedScenarioMission>() {
		@Override
		protected CommitedScenarioMission newBean() {
			return new xbean.__.CommitedScenarioMission();
		}
	};

	public static CommitedScenarioMission newCommitedScenarioMission() {
		return poolCommitedScenarioMission.get();
	}

	public static void _reset_unsafe_add_(CommitedScenarioMission bean) {
		poolCommitedScenarioMission._reset_unsafe_add_(bean);
	}

	public static void padd(CommitedScenarioMission bean) {
		mkdb.Procedure.padd(bean, poolCommitedScenarioMission);
	}

	public static CommitedScenarioMission newCommitedScenarioMissionData() {
		return new xbean.__.CommitedScenarioMission.Data();
	}

	public static mkdb.util.BeanPool<Mission> poolMission = new mkdb.util.BeanPool<Mission>() {
		@Override
		protected Mission newBean() {
			return new xbean.__.Mission();
		}
	};

	public static Mission newMission() {
		return poolMission.get();
	}

	public static void _reset_unsafe_add_(Mission bean) {
		poolMission._reset_unsafe_add_(bean);
	}

	public static void padd(Mission bean) {
		mkdb.Procedure.padd(bean, poolMission);
	}

	public static Mission newMissionData() {
		return new xbean.__.Mission.Data();
	}

	public static mkdb.util.BeanPool<AcceptedScenarioMission> poolAcceptedScenarioMission = new mkdb.util.BeanPool<AcceptedScenarioMission>() {
		@Override
		protected AcceptedScenarioMission newBean() {
			return new xbean.__.AcceptedScenarioMission();
		}
	};

	public static AcceptedScenarioMission newAcceptedScenarioMission() {
		return poolAcceptedScenarioMission.get();
	}

	public static void _reset_unsafe_add_(AcceptedScenarioMission bean) {
		poolAcceptedScenarioMission._reset_unsafe_add_(bean);
	}

	public static void padd(AcceptedScenarioMission bean) {
		mkdb.Procedure.padd(bean, poolAcceptedScenarioMission);
	}

	public static AcceptedScenarioMission newAcceptedScenarioMissionData() {
		return new xbean.__.AcceptedScenarioMission.Data();
	}

	public static mkdb.util.BeanPool<Track> poolTrack = new mkdb.util.BeanPool<Track>() {
		@Override
		protected Track newBean() {
			return new xbean.__.Track();
		}
	};

	public static Track newTrack() {
		return poolTrack.get();
	}

	public static void _reset_unsafe_add_(Track bean) {
		poolTrack._reset_unsafe_add_(bean);
	}

	public static void padd(Track bean) {
		mkdb.Procedure.padd(bean, poolTrack);
	}

	public static Track newTrackData() {
		return new xbean.__.Track.Data();
	}

	public static mkdb.util.BeanPool<TrackedMission> poolTrackedMission = new mkdb.util.BeanPool<TrackedMission>() {
		@Override
		protected TrackedMission newBean() {
			return new xbean.__.TrackedMission();
		}
	};

	public static TrackedMission newTrackedMission() {
		return poolTrackedMission.get();
	}

	public static void _reset_unsafe_add_(TrackedMission bean) {
		poolTrackedMission._reset_unsafe_add_(bean);
	}

	public static void padd(TrackedMission bean) {
		mkdb.Procedure.padd(bean, poolTrackedMission);
	}

	public static TrackedMission newTrackedMissionData() {
		return new xbean.__.TrackedMission.Data();
	}

	public static mkdb.util.BeanPool<AnYeTask> poolAnYeTask = new mkdb.util.BeanPool<AnYeTask>() {
		@Override
		protected AnYeTask newBean() {
			return new xbean.__.AnYeTask();
		}
	};

	public static AnYeTask newAnYeTask() {
		return poolAnYeTask.get();
	}

	public static void _reset_unsafe_add_(AnYeTask bean) {
		poolAnYeTask._reset_unsafe_add_(bean);
	}

	public static void padd(AnYeTask bean) {
		mkdb.Procedure.padd(bean, poolAnYeTask);
	}

	public static AnYeTask newAnYeTaskData() {
		return new xbean.__.AnYeTask.Data();
	}

	public static mkdb.util.BeanPool<AnYeData> poolAnYeData = new mkdb.util.BeanPool<AnYeData>() {
		@Override
		protected AnYeData newBean() {
			return new xbean.__.AnYeData();
		}
	};

	public static AnYeData newAnYeData() {
		return poolAnYeData.get();
	}

	public static void _reset_unsafe_add_(AnYeData bean) {
		poolAnYeData._reset_unsafe_add_(bean);
	}

	public static void padd(AnYeData bean) {
		mkdb.Procedure.padd(bean, poolAnYeData);
	}

	public static AnYeData newAnYeDataData() {
		return new xbean.__.AnYeData.Data();
	}

	public static mkdb.util.BeanPool<AnYeLegendFuture> poolAnYeLegendFuture = new mkdb.util.BeanPool<AnYeLegendFuture>() {
		@Override
		protected AnYeLegendFuture newBean() {
			return new xbean.__.AnYeLegendFuture();
		}
	};

	public static AnYeLegendFuture newAnYeLegendFuture() {
		return poolAnYeLegendFuture.get();
	}

	public static void _reset_unsafe_add_(AnYeLegendFuture bean) {
		poolAnYeLegendFuture._reset_unsafe_add_(bean);
	}

	public static void padd(AnYeLegendFuture bean) {
		mkdb.Procedure.padd(bean, poolAnYeLegendFuture);
	}

	public static AnYeLegendFuture newAnYeLegendFutureData() {
		return new xbean.__.AnYeLegendFuture.Data();
	}

	public static mkdb.util.BeanPool<CircleTaskInfo> poolCircleTaskInfo = new mkdb.util.BeanPool<CircleTaskInfo>() {
		@Override
		protected CircleTaskInfo newBean() {
			return new xbean.__.CircleTaskInfo();
		}
	};

	public static CircleTaskInfo newCircleTaskInfo() {
		return poolCircleTaskInfo.get();
	}

	public static void _reset_unsafe_add_(CircleTaskInfo bean) {
		poolCircleTaskInfo._reset_unsafe_add_(bean);
	}

	public static void padd(CircleTaskInfo bean) {
		mkdb.Procedure.padd(bean, poolCircleTaskInfo);
	}

	public static CircleTaskInfo newCircleTaskInfoData() {
		return new xbean.__.CircleTaskInfo.Data();
	}

	public static mkdb.util.BeanPool<CircleTaskMap> poolCircleTaskMap = new mkdb.util.BeanPool<CircleTaskMap>() {
		@Override
		protected CircleTaskMap newBean() {
			return new xbean.__.CircleTaskMap();
		}
	};

	public static CircleTaskMap newCircleTaskMap() {
		return poolCircleTaskMap.get();
	}

	public static void _reset_unsafe_add_(CircleTaskMap bean) {
		poolCircleTaskMap._reset_unsafe_add_(bean);
	}

	public static void padd(CircleTaskMap bean) {
		mkdb.Procedure.padd(bean, poolCircleTaskMap);
	}

	public static CircleTaskMap newCircleTaskMapData() {
		return new xbean.__.CircleTaskMap.Data();
	}

	public static mkdb.util.BeanPool<RenXingData> poolRenXingData = new mkdb.util.BeanPool<RenXingData>() {
		@Override
		protected RenXingData newBean() {
			return new xbean.__.RenXingData();
		}
	};

	public static RenXingData newRenXingData() {
		return poolRenXingData.get();
	}

	public static void _reset_unsafe_add_(RenXingData bean) {
		poolRenXingData._reset_unsafe_add_(bean);
	}

	public static void padd(RenXingData bean) {
		mkdb.Procedure.padd(bean, poolRenXingData);
	}

	public static RenXingData newRenXingDataData() {
		return new xbean.__.RenXingData.Data();
	}

	public static mkdb.util.BeanPool<CircleTaskCompleteTimes> poolCircleTaskCompleteTimes = new mkdb.util.BeanPool<CircleTaskCompleteTimes>() {
		@Override
		protected CircleTaskCompleteTimes newBean() {
			return new xbean.__.CircleTaskCompleteTimes();
		}
	};

	public static CircleTaskCompleteTimes newCircleTaskCompleteTimes() {
		return poolCircleTaskCompleteTimes.get();
	}

	public static void _reset_unsafe_add_(CircleTaskCompleteTimes bean) {
		poolCircleTaskCompleteTimes._reset_unsafe_add_(bean);
	}

	public static void padd(CircleTaskCompleteTimes bean) {
		mkdb.Procedure.padd(bean, poolCircleTaskCompleteTimes);
	}

	public static CircleTaskCompleteTimes newCircleTaskCompleteTimesData() {
		return new xbean.__.CircleTaskCompleteTimes.Data();
	}

	public static mkdb.util.BeanPool<SpecailquestFuture> poolSpecailquestFuture = new mkdb.util.BeanPool<SpecailquestFuture>() {
		@Override
		protected SpecailquestFuture newBean() {
			return new xbean.__.SpecailquestFuture();
		}
	};

	public static SpecailquestFuture newSpecailquestFuture() {
		return poolSpecailquestFuture.get();
	}

	public static void _reset_unsafe_add_(SpecailquestFuture bean) {
		poolSpecailquestFuture._reset_unsafe_add_(bean);
	}

	public static void padd(SpecailquestFuture bean) {
		mkdb.Procedure.padd(bean, poolSpecailquestFuture);
	}

	public static SpecailquestFuture newSpecailquestFutureData() {
		return new xbean.__.SpecailquestFuture.Data();
	}

	public static mkdb.util.BeanPool<CreateNpcExecType> poolCreateNpcExecType = new mkdb.util.BeanPool<CreateNpcExecType>() {
		@Override
		protected CreateNpcExecType newBean() {
			return new xbean.__.CreateNpcExecType();
		}
	};

	public static CreateNpcExecType newCreateNpcExecType() {
		return poolCreateNpcExecType.get();
	}

	public static void _reset_unsafe_add_(CreateNpcExecType bean) {
		poolCreateNpcExecType._reset_unsafe_add_(bean);
	}

	public static void padd(CreateNpcExecType bean) {
		mkdb.Procedure.padd(bean, poolCreateNpcExecType);
	}

	public static CreateNpcExecType newCreateNpcExecTypeData() {
		return new xbean.__.CreateNpcExecType.Data();
	}

	public static mkdb.util.BeanPool<MazeInfo> poolMazeInfo = new mkdb.util.BeanPool<MazeInfo>() {
		@Override
		protected MazeInfo newBean() {
			return new xbean.__.MazeInfo();
		}
	};

	public static MazeInfo newMazeInfo() {
		return poolMazeInfo.get();
	}

	public static void _reset_unsafe_add_(MazeInfo bean) {
		poolMazeInfo._reset_unsafe_add_(bean);
	}

	public static void padd(MazeInfo bean) {
		mkdb.Procedure.padd(bean, poolMazeInfo);
	}

	public static MazeInfo newMazeInfoData() {
		return new xbean.__.MazeInfo.Data();
	}

	public static mkdb.util.BeanPool<VecList> poolVecList = new mkdb.util.BeanPool<VecList>() {
		@Override
		protected VecList newBean() {
			return new xbean.__.VecList();
		}
	};

	public static VecList newVecList() {
		return poolVecList.get();
	}

	public static void _reset_unsafe_add_(VecList bean) {
		poolVecList._reset_unsafe_add_(bean);
	}

	public static void padd(VecList bean) {
		mkdb.Procedure.padd(bean, poolVecList);
	}

	public static VecList newVecListData() {
		return new xbean.__.VecList.Data();
	}

	public static mkdb.util.BeanPool<ProfessionLeaderInfo> poolProfessionLeaderInfo = new mkdb.util.BeanPool<ProfessionLeaderInfo>() {
		@Override
		protected ProfessionLeaderInfo newBean() {
			return new xbean.__.ProfessionLeaderInfo();
		}
	};

	public static ProfessionLeaderInfo newProfessionLeaderInfo() {
		return poolProfessionLeaderInfo.get();
	}

	public static void _reset_unsafe_add_(ProfessionLeaderInfo bean) {
		poolProfessionLeaderInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ProfessionLeaderInfo bean) {
		mkdb.Procedure.padd(bean, poolProfessionLeaderInfo);
	}

	public static ProfessionLeaderInfo newProfessionLeaderInfoData() {
		return new xbean.__.ProfessionLeaderInfo.Data();
	}

	public static mkdb.util.BeanPool<ProfessionLeaderTicketInfo> poolProfessionLeaderTicketInfo = new mkdb.util.BeanPool<ProfessionLeaderTicketInfo>() {
		@Override
		protected ProfessionLeaderTicketInfo newBean() {
			return new xbean.__.ProfessionLeaderTicketInfo();
		}
	};

	public static ProfessionLeaderTicketInfo newProfessionLeaderTicketInfo() {
		return poolProfessionLeaderTicketInfo.get();
	}

	public static void _reset_unsafe_add_(ProfessionLeaderTicketInfo bean) {
		poolProfessionLeaderTicketInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ProfessionLeaderTicketInfo bean) {
		mkdb.Procedure.padd(bean, poolProfessionLeaderTicketInfo);
	}

	public static ProfessionLeaderTicketInfo newProfessionLeaderTicketInfoData() {
		return new xbean.__.ProfessionLeaderTicketInfo.Data();
	}

	public static mkdb.util.BeanPool<ProfessionLeaderVoteInfo> poolProfessionLeaderVoteInfo = new mkdb.util.BeanPool<ProfessionLeaderVoteInfo>() {
		@Override
		protected ProfessionLeaderVoteInfo newBean() {
			return new xbean.__.ProfessionLeaderVoteInfo();
		}
	};

	public static ProfessionLeaderVoteInfo newProfessionLeaderVoteInfo() {
		return poolProfessionLeaderVoteInfo.get();
	}

	public static void _reset_unsafe_add_(ProfessionLeaderVoteInfo bean) {
		poolProfessionLeaderVoteInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ProfessionLeaderVoteInfo bean) {
		mkdb.Procedure.padd(bean, poolProfessionLeaderVoteInfo);
	}

	public static ProfessionLeaderVoteInfo newProfessionLeaderVoteInfoData() {
		return new xbean.__.ProfessionLeaderVoteInfo.Data();
	}

	public static mkdb.util.BeanPool<ProfessionLeaderCand> poolProfessionLeaderCand = new mkdb.util.BeanPool<ProfessionLeaderCand>() {
		@Override
		protected ProfessionLeaderCand newBean() {
			return new xbean.__.ProfessionLeaderCand();
		}
	};

	public static ProfessionLeaderCand newProfessionLeaderCand() {
		return poolProfessionLeaderCand.get();
	}

	public static void _reset_unsafe_add_(ProfessionLeaderCand bean) {
		poolProfessionLeaderCand._reset_unsafe_add_(bean);
	}

	public static void padd(ProfessionLeaderCand bean) {
		mkdb.Procedure.padd(bean, poolProfessionLeaderCand);
	}

	public static ProfessionLeaderCand newProfessionLeaderCandData() {
		return new xbean.__.ProfessionLeaderCand.Data();
	}

	public static mkdb.util.BeanPool<TaskDlgInfo> poolTaskDlgInfo = new mkdb.util.BeanPool<TaskDlgInfo>() {
		@Override
		protected TaskDlgInfo newBean() {
			return new xbean.__.TaskDlgInfo();
		}
	};

	public static TaskDlgInfo newTaskDlgInfo() {
		return poolTaskDlgInfo.get();
	}

	public static void _reset_unsafe_add_(TaskDlgInfo bean) {
		poolTaskDlgInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TaskDlgInfo bean) {
		mkdb.Procedure.padd(bean, poolTaskDlgInfo);
	}

	public static TaskDlgInfo newTaskDlgInfoData() {
		return new xbean.__.TaskDlgInfo.Data();
	}

	public static mkdb.util.BeanPool<TaskInfos> poolTaskInfos = new mkdb.util.BeanPool<TaskInfos>() {
		@Override
		protected TaskInfos newBean() {
			return new xbean.__.TaskInfos();
		}
	};

	public static TaskInfos newTaskInfos() {
		return poolTaskInfos.get();
	}

	public static void _reset_unsafe_add_(TaskInfos bean) {
		poolTaskInfos._reset_unsafe_add_(bean);
	}

	public static void padd(TaskInfos bean) {
		mkdb.Procedure.padd(bean, poolTaskInfos);
	}

	public static TaskInfos newTaskInfosData() {
		return new xbean.__.TaskInfos.Data();
	}

	public static mkdb.util.BeanPool<NpcInfo> poolNpcInfo = new mkdb.util.BeanPool<NpcInfo>() {
		@Override
		protected NpcInfo newBean() {
			return new xbean.__.NpcInfo();
		}
	};

	public static NpcInfo newNpcInfo() {
		return poolNpcInfo.get();
	}

	public static void _reset_unsafe_add_(NpcInfo bean) {
		poolNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(NpcInfo bean) {
		mkdb.Procedure.padd(bean, poolNpcInfo);
	}

	public static NpcInfo newNpcInfoData() {
		return new xbean.__.NpcInfo.Data();
	}

	public static mkdb.util.BeanPool<ApprentceChieve> poolApprentceChieve = new mkdb.util.BeanPool<ApprentceChieve>() {
		@Override
		protected ApprentceChieve newBean() {
			return new xbean.__.ApprentceChieve();
		}
	};

	public static ApprentceChieve newApprentceChieve() {
		return poolApprentceChieve.get();
	}

	public static void _reset_unsafe_add_(ApprentceChieve bean) {
		poolApprentceChieve._reset_unsafe_add_(bean);
	}

	public static void padd(ApprentceChieve bean) {
		mkdb.Procedure.padd(bean, poolApprentceChieve);
	}

	public static ApprentceChieve newApprentceChieveData() {
		return new xbean.__.ApprentceChieve.Data();
	}

	public static mkdb.util.BeanPool<CurrApprent> poolCurrApprent = new mkdb.util.BeanPool<CurrApprent>() {
		@Override
		protected CurrApprent newBean() {
			return new xbean.__.CurrApprent();
		}
	};

	public static CurrApprent newCurrApprent() {
		return poolCurrApprent.get();
	}

	public static void _reset_unsafe_add_(CurrApprent bean) {
		poolCurrApprent._reset_unsafe_add_(bean);
	}

	public static void padd(CurrApprent bean) {
		mkdb.Procedure.padd(bean, poolCurrApprent);
	}

	public static CurrApprent newCurrApprentData() {
		return new xbean.__.CurrApprent.Data();
	}

	public static mkdb.util.BeanPool<HasApprent> poolHasApprent = new mkdb.util.BeanPool<HasApprent>() {
		@Override
		protected HasApprent newBean() {
			return new xbean.__.HasApprent();
		}
	};

	public static HasApprent newHasApprent() {
		return poolHasApprent.get();
	}

	public static void _reset_unsafe_add_(HasApprent bean) {
		poolHasApprent._reset_unsafe_add_(bean);
	}

	public static void padd(HasApprent bean) {
		mkdb.Procedure.padd(bean, poolHasApprent);
	}

	public static HasApprent newHasApprentData() {
		return new xbean.__.HasApprent.Data();
	}

	public static mkdb.util.BeanPool<MenstorInfo> poolMenstorInfo = new mkdb.util.BeanPool<MenstorInfo>() {
		@Override
		protected MenstorInfo newBean() {
			return new xbean.__.MenstorInfo();
		}
	};

	public static MenstorInfo newMenstorInfo() {
		return poolMenstorInfo.get();
	}

	public static void _reset_unsafe_add_(MenstorInfo bean) {
		poolMenstorInfo._reset_unsafe_add_(bean);
	}

	public static void padd(MenstorInfo bean) {
		mkdb.Procedure.padd(bean, poolMenstorInfo);
	}

	public static MenstorInfo newMenstorInfoData() {
		return new xbean.__.MenstorInfo.Data();
	}

	public static mkdb.util.BeanPool<MenstorAndApprent> poolMenstorAndApprent = new mkdb.util.BeanPool<MenstorAndApprent>() {
		@Override
		protected MenstorAndApprent newBean() {
			return new xbean.__.MenstorAndApprent();
		}
	};

	public static MenstorAndApprent newMenstorAndApprent() {
		return poolMenstorAndApprent.get();
	}

	public static void _reset_unsafe_add_(MenstorAndApprent bean) {
		poolMenstorAndApprent._reset_unsafe_add_(bean);
	}

	public static void padd(MenstorAndApprent bean) {
		mkdb.Procedure.padd(bean, poolMenstorAndApprent);
	}

	public static MenstorAndApprent newMenstorAndApprentData() {
		return new xbean.__.MenstorAndApprent.Data();
	}

	public static mkdb.util.BeanPool<ShiDeZhiRankList> poolShiDeZhiRankList = new mkdb.util.BeanPool<ShiDeZhiRankList>() {
		@Override
		protected ShiDeZhiRankList newBean() {
			return new xbean.__.ShiDeZhiRankList();
		}
	};

	public static ShiDeZhiRankList newShiDeZhiRankList() {
		return poolShiDeZhiRankList.get();
	}

	public static void _reset_unsafe_add_(ShiDeZhiRankList bean) {
		poolShiDeZhiRankList._reset_unsafe_add_(bean);
	}

	public static void padd(ShiDeZhiRankList bean) {
		mkdb.Procedure.padd(bean, poolShiDeZhiRankList);
	}

	public static ShiDeZhiRankList newShiDeZhiRankListData() {
		return new xbean.__.ShiDeZhiRankList.Data();
	}

	public static mkdb.util.BeanPool<MasterRankRecord> poolMasterRankRecord = new mkdb.util.BeanPool<MasterRankRecord>() {
		@Override
		protected MasterRankRecord newBean() {
			return new xbean.__.MasterRankRecord();
		}
	};

	public static MasterRankRecord newMasterRankRecord() {
		return poolMasterRankRecord.get();
	}

	public static void _reset_unsafe_add_(MasterRankRecord bean) {
		poolMasterRankRecord._reset_unsafe_add_(bean);
	}

	public static void padd(MasterRankRecord bean) {
		mkdb.Procedure.padd(bean, poolMasterRankRecord);
	}

	public static MasterRankRecord newMasterRankRecordData() {
		return new xbean.__.MasterRankRecord.Data();
	}

	public static mkdb.util.BeanPool<RolePos> poolRolePos = new mkdb.util.BeanPool<RolePos>() {
		@Override
		protected RolePos newBean() {
			return new xbean.__.RolePos();
		}
	};

	public static RolePos newRolePos() {
		return poolRolePos.get();
	}

	public static void _reset_unsafe_add_(RolePos bean) {
		poolRolePos._reset_unsafe_add_(bean);
	}

	public static void padd(RolePos bean) {
		mkdb.Procedure.padd(bean, poolRolePos);
	}

	public static RolePos newRolePosData() {
		return new xbean.__.RolePos.Data();
	}

	public static mkdb.util.BeanPool<RewardData> poolRewardData = new mkdb.util.BeanPool<RewardData>() {
		@Override
		protected RewardData newBean() {
			return new xbean.__.RewardData();
		}
	};

	public static RewardData newRewardData() {
		return poolRewardData.get();
	}

	public static void _reset_unsafe_add_(RewardData bean) {
		poolRewardData._reset_unsafe_add_(bean);
	}

	public static void padd(RewardData bean) {
		mkdb.Procedure.padd(bean, poolRewardData);
	}

	public static RewardData newRewardDataData() {
		return new xbean.__.RewardData.Data();
	}

	public static mkdb.util.BeanPool<InstanceNpcInfo> poolInstanceNpcInfo = new mkdb.util.BeanPool<InstanceNpcInfo>() {
		@Override
		protected InstanceNpcInfo newBean() {
			return new xbean.__.InstanceNpcInfo();
		}
	};

	public static InstanceNpcInfo newInstanceNpcInfo() {
		return poolInstanceNpcInfo.get();
	}

	public static void _reset_unsafe_add_(InstanceNpcInfo bean) {
		poolInstanceNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceNpcInfo bean) {
		mkdb.Procedure.padd(bean, poolInstanceNpcInfo);
	}

	public static InstanceNpcInfo newInstanceNpcInfoData() {
		return new xbean.__.InstanceNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<InstanceInfo> poolInstanceInfo = new mkdb.util.BeanPool<InstanceInfo>() {
		@Override
		protected InstanceInfo newBean() {
			return new xbean.__.InstanceInfo();
		}
	};

	public static InstanceInfo newInstanceInfo() {
		return poolInstanceInfo.get();
	}

	public static void _reset_unsafe_add_(InstanceInfo bean) {
		poolInstanceInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceInfo bean) {
		mkdb.Procedure.padd(bean, poolInstanceInfo);
	}

	public static InstanceInfo newInstanceInfoData() {
		return new xbean.__.InstanceInfo.Data();
	}

	public static mkdb.util.BeanPool<InstanceFutureInfo> poolInstanceFutureInfo = new mkdb.util.BeanPool<InstanceFutureInfo>() {
		@Override
		protected InstanceFutureInfo newBean() {
			return new xbean.__.InstanceFutureInfo();
		}
	};

	public static InstanceFutureInfo newInstanceFutureInfo() {
		return poolInstanceFutureInfo.get();
	}

	public static void _reset_unsafe_add_(InstanceFutureInfo bean) {
		poolInstanceFutureInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceFutureInfo bean) {
		mkdb.Procedure.padd(bean, poolInstanceFutureInfo);
	}

	public static InstanceFutureInfo newInstanceFutureInfoData() {
		return new xbean.__.InstanceFutureInfo.Data();
	}

	public static mkdb.util.BeanPool<InstanceTaskInfo> poolInstanceTaskInfo = new mkdb.util.BeanPool<InstanceTaskInfo>() {
		@Override
		protected InstanceTaskInfo newBean() {
			return new xbean.__.InstanceTaskInfo();
		}
	};

	public static InstanceTaskInfo newInstanceTaskInfo() {
		return poolInstanceTaskInfo.get();
	}

	public static void _reset_unsafe_add_(InstanceTaskInfo bean) {
		poolInstanceTaskInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceTaskInfo bean) {
		mkdb.Procedure.padd(bean, poolInstanceTaskInfo);
	}

	public static InstanceTaskInfo newInstanceTaskInfoData() {
		return new xbean.__.InstanceTaskInfo.Data();
	}

	public static mkdb.util.BeanPool<InstanceTimeAwardInfo> poolInstanceTimeAwardInfo = new mkdb.util.BeanPool<InstanceTimeAwardInfo>() {
		@Override
		protected InstanceTimeAwardInfo newBean() {
			return new xbean.__.InstanceTimeAwardInfo();
		}
	};

	public static InstanceTimeAwardInfo newInstanceTimeAwardInfo() {
		return poolInstanceTimeAwardInfo.get();
	}

	public static void _reset_unsafe_add_(InstanceTimeAwardInfo bean) {
		poolInstanceTimeAwardInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceTimeAwardInfo bean) {
		mkdb.Procedure.padd(bean, poolInstanceTimeAwardInfo);
	}

	public static InstanceTimeAwardInfo newInstanceTimeAwardInfoData() {
		return new xbean.__.InstanceTimeAwardInfo.Data();
	}

	public static mkdb.util.BeanPool<InstanceInfoCol> poolInstanceInfoCol = new mkdb.util.BeanPool<InstanceInfoCol>() {
		@Override
		protected InstanceInfoCol newBean() {
			return new xbean.__.InstanceInfoCol();
		}
	};

	public static InstanceInfoCol newInstanceInfoCol() {
		return poolInstanceInfoCol.get();
	}

	public static void _reset_unsafe_add_(InstanceInfoCol bean) {
		poolInstanceInfoCol._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceInfoCol bean) {
		mkdb.Procedure.padd(bean, poolInstanceInfoCol);
	}

	public static InstanceInfoCol newInstanceInfoColData() {
		return new xbean.__.InstanceInfoCol.Data();
	}

	public static mkdb.util.BeanPool<InstanceAskInfo> poolInstanceAskInfo = new mkdb.util.BeanPool<InstanceAskInfo>() {
		@Override
		protected InstanceAskInfo newBean() {
			return new xbean.__.InstanceAskInfo();
		}
	};

	public static InstanceAskInfo newInstanceAskInfo() {
		return poolInstanceAskInfo.get();
	}

	public static void _reset_unsafe_add_(InstanceAskInfo bean) {
		poolInstanceAskInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceAskInfo bean) {
		mkdb.Procedure.padd(bean, poolInstanceAskInfo);
	}

	public static InstanceAskInfo newInstanceAskInfoData() {
		return new xbean.__.InstanceAskInfo.Data();
	}

	public static mkdb.util.BeanPool<ActiveUserInfo> poolActiveUserInfo = new mkdb.util.BeanPool<ActiveUserInfo>() {
		@Override
		protected ActiveUserInfo newBean() {
			return new xbean.__.ActiveUserInfo();
		}
	};

	public static ActiveUserInfo newActiveUserInfo() {
		return poolActiveUserInfo.get();
	}

	public static void _reset_unsafe_add_(ActiveUserInfo bean) {
		poolActiveUserInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ActiveUserInfo bean) {
		mkdb.Procedure.padd(bean, poolActiveUserInfo);
	}

	public static ActiveUserInfo newActiveUserInfoData() {
		return new xbean.__.ActiveUserInfo.Data();
	}

	public static mkdb.util.BeanPool<ActiveRoleInfo> poolActiveRoleInfo = new mkdb.util.BeanPool<ActiveRoleInfo>() {
		@Override
		protected ActiveRoleInfo newBean() {
			return new xbean.__.ActiveRoleInfo();
		}
	};

	public static ActiveRoleInfo newActiveRoleInfo() {
		return poolActiveRoleInfo.get();
	}

	public static void _reset_unsafe_add_(ActiveRoleInfo bean) {
		poolActiveRoleInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ActiveRoleInfo bean) {
		mkdb.Procedure.padd(bean, poolActiveRoleInfo);
	}

	public static ActiveRoleInfo newActiveRoleInfoData() {
		return new xbean.__.ActiveRoleInfo.Data();
	}

	public static mkdb.util.BeanPool<LianyunAwardInfo> poolLianyunAwardInfo = new mkdb.util.BeanPool<LianyunAwardInfo>() {
		@Override
		protected LianyunAwardInfo newBean() {
			return new xbean.__.LianyunAwardInfo();
		}
	};

	public static LianyunAwardInfo newLianyunAwardInfo() {
		return poolLianyunAwardInfo.get();
	}

	public static void _reset_unsafe_add_(LianyunAwardInfo bean) {
		poolLianyunAwardInfo._reset_unsafe_add_(bean);
	}

	public static void padd(LianyunAwardInfo bean) {
		mkdb.Procedure.padd(bean, poolLianyunAwardInfo);
	}

	public static LianyunAwardInfo newLianyunAwardInfoData() {
		return new xbean.__.LianyunAwardInfo.Data();
	}

	public static mkdb.util.BeanPool<BeginnerTip> poolBeginnerTip = new mkdb.util.BeanPool<BeginnerTip>() {
		@Override
		protected BeginnerTip newBean() {
			return new xbean.__.BeginnerTip();
		}
	};

	public static BeginnerTip newBeginnerTip() {
		return poolBeginnerTip.get();
	}

	public static void _reset_unsafe_add_(BeginnerTip bean) {
		poolBeginnerTip._reset_unsafe_add_(bean);
	}

	public static void padd(BeginnerTip bean) {
		mkdb.Procedure.padd(bean, poolBeginnerTip);
	}

	public static BeginnerTip newBeginnerTipData() {
		return new xbean.__.BeginnerTip.Data();
	}

	public static mkdb.util.BeanPool<Gather> poolGather = new mkdb.util.BeanPool<Gather>() {
		@Override
		protected Gather newBean() {
			return new xbean.__.Gather();
		}
	};

	public static Gather newGather() {
		return poolGather.get();
	}

	public static void _reset_unsafe_add_(Gather bean) {
		poolGather._reset_unsafe_add_(bean);
	}

	public static void padd(Gather bean) {
		mkdb.Procedure.padd(bean, poolGather);
	}

	public static Gather newGatherData() {
		return new xbean.__.Gather.Data();
	}

	public static mkdb.util.BeanPool<GetRolesCallBackInst> poolGetRolesCallBackInst = new mkdb.util.BeanPool<GetRolesCallBackInst>() {
		@Override
		protected GetRolesCallBackInst newBean() {
			return new xbean.__.GetRolesCallBackInst();
		}
	};

	public static GetRolesCallBackInst newGetRolesCallBackInst() {
		return poolGetRolesCallBackInst.get();
	}

	public static void _reset_unsafe_add_(GetRolesCallBackInst bean) {
		poolGetRolesCallBackInst._reset_unsafe_add_(bean);
	}

	public static void padd(GetRolesCallBackInst bean) {
		mkdb.Procedure.padd(bean, poolGetRolesCallBackInst);
	}

	public static GetRolesCallBackInst newGetRolesCallBackInstData() {
		return new xbean.__.GetRolesCallBackInst.Data();
	}

	public static mkdb.util.BeanPool<forbidinfo> poolforbidinfo = new mkdb.util.BeanPool<forbidinfo>() {
		@Override
		protected forbidinfo newBean() {
			return new xbean.__.forbidinfo();
		}
	};

	public static forbidinfo newforbidinfo() {
		return poolforbidinfo.get();
	}

	public static void _reset_unsafe_add_(forbidinfo bean) {
		poolforbidinfo._reset_unsafe_add_(bean);
	}

	public static void padd(forbidinfo bean) {
		mkdb.Procedure.padd(bean, poolforbidinfo);
	}

	public static forbidinfo newforbidinfoData() {
		return new xbean.__.forbidinfo.Data();
	}

	public static mkdb.util.BeanPool<RoleQuitStatistics> poolRoleQuitStatistics = new mkdb.util.BeanPool<RoleQuitStatistics>() {
		@Override
		protected RoleQuitStatistics newBean() {
			return new xbean.__.RoleQuitStatistics();
		}
	};

	public static RoleQuitStatistics newRoleQuitStatistics() {
		return poolRoleQuitStatistics.get();
	}

	public static void _reset_unsafe_add_(RoleQuitStatistics bean) {
		poolRoleQuitStatistics._reset_unsafe_add_(bean);
	}

	public static void padd(RoleQuitStatistics bean) {
		mkdb.Procedure.padd(bean, poolRoleQuitStatistics);
	}

	public static RoleQuitStatistics newRoleQuitStatisticsData() {
		return new xbean.__.RoleQuitStatistics.Data();
	}

	public static mkdb.util.BeanPool<Course> poolCourse = new mkdb.util.BeanPool<Course>() {
		@Override
		protected Course newBean() {
			return new xbean.__.Course();
		}
	};

	public static Course newCourse() {
		return poolCourse.get();
	}

	public static void _reset_unsafe_add_(Course bean) {
		poolCourse._reset_unsafe_add_(bean);
	}

	public static void padd(Course bean) {
		mkdb.Procedure.padd(bean, poolCourse);
	}

	public static Course newCourseData() {
		return new xbean.__.Course.Data();
	}

	public static mkdb.util.BeanPool<lichengxinxi> poollichengxinxi = new mkdb.util.BeanPool<lichengxinxi>() {
		@Override
		protected lichengxinxi newBean() {
			return new xbean.__.lichengxinxi();
		}
	};

	public static lichengxinxi newlichengxinxi() {
		return poollichengxinxi.get();
	}

	public static void _reset_unsafe_add_(lichengxinxi bean) {
		poollichengxinxi._reset_unsafe_add_(bean);
	}

	public static void padd(lichengxinxi bean) {
		mkdb.Procedure.padd(bean, poollichengxinxi);
	}

	public static lichengxinxi newlichengxinxiData() {
		return new xbean.__.lichengxinxi.Data();
	}

	public static mkdb.util.BeanPool<DayFinishTask> poolDayFinishTask = new mkdb.util.BeanPool<DayFinishTask>() {
		@Override
		protected DayFinishTask newBean() {
			return new xbean.__.DayFinishTask();
		}
	};

	public static DayFinishTask newDayFinishTask() {
		return poolDayFinishTask.get();
	}

	public static void _reset_unsafe_add_(DayFinishTask bean) {
		poolDayFinishTask._reset_unsafe_add_(bean);
	}

	public static void padd(DayFinishTask bean) {
		mkdb.Procedure.padd(bean, poolDayFinishTask);
	}

	public static DayFinishTask newDayFinishTaskData() {
		return new xbean.__.DayFinishTask.Data();
	}

	public static mkdb.util.BeanPool<ActivityCalendarStatus> poolActivityCalendarStatus = new mkdb.util.BeanPool<ActivityCalendarStatus>() {
		@Override
		protected ActivityCalendarStatus newBean() {
			return new xbean.__.ActivityCalendarStatus();
		}
	};

	public static ActivityCalendarStatus newActivityCalendarStatus() {
		return poolActivityCalendarStatus.get();
	}

	public static void _reset_unsafe_add_(ActivityCalendarStatus bean) {
		poolActivityCalendarStatus._reset_unsafe_add_(bean);
	}

	public static void padd(ActivityCalendarStatus bean) {
		mkdb.Procedure.padd(bean, poolActivityCalendarStatus);
	}

	public static ActivityCalendarStatus newActivityCalendarStatusData() {
		return new xbean.__.ActivityCalendarStatus.Data();
	}

	public static mkdb.util.BeanPool<PlayActiveData> poolPlayActiveData = new mkdb.util.BeanPool<PlayActiveData>() {
		@Override
		protected PlayActiveData newBean() {
			return new xbean.__.PlayActiveData();
		}
	};

	public static PlayActiveData newPlayActiveData() {
		return poolPlayActiveData.get();
	}

	public static void _reset_unsafe_add_(PlayActiveData bean) {
		poolPlayActiveData._reset_unsafe_add_(bean);
	}

	public static void padd(PlayActiveData bean) {
		mkdb.Procedure.padd(bean, poolPlayActiveData);
	}

	public static PlayActiveData newPlayActiveDataData() {
		return new xbean.__.PlayActiveData.Data();
	}

	public static mkdb.util.BeanPool<NewPlayActiveDay> poolNewPlayActiveDay = new mkdb.util.BeanPool<NewPlayActiveDay>() {
		@Override
		protected NewPlayActiveDay newBean() {
			return new xbean.__.NewPlayActiveDay();
		}
	};

	public static NewPlayActiveDay newNewPlayActiveDay() {
		return poolNewPlayActiveDay.get();
	}

	public static void _reset_unsafe_add_(NewPlayActiveDay bean) {
		poolNewPlayActiveDay._reset_unsafe_add_(bean);
	}

	public static void padd(NewPlayActiveDay bean) {
		mkdb.Procedure.padd(bean, poolNewPlayActiveDay);
	}

	public static NewPlayActiveDay newNewPlayActiveDayData() {
		return new xbean.__.NewPlayActiveDay.Data();
	}

	public static mkdb.util.BeanPool<NewPlayActiveWeek> poolNewPlayActiveWeek = new mkdb.util.BeanPool<NewPlayActiveWeek>() {
		@Override
		protected NewPlayActiveWeek newBean() {
			return new xbean.__.NewPlayActiveWeek();
		}
	};

	public static NewPlayActiveWeek newNewPlayActiveWeek() {
		return poolNewPlayActiveWeek.get();
	}

	public static void _reset_unsafe_add_(NewPlayActiveWeek bean) {
		poolNewPlayActiveWeek._reset_unsafe_add_(bean);
	}

	public static void padd(NewPlayActiveWeek bean) {
		mkdb.Procedure.padd(bean, poolNewPlayActiveWeek);
	}

	public static NewPlayActiveWeek newNewPlayActiveWeekData() {
		return new xbean.__.NewPlayActiveWeek.Data();
	}

	public static mkdb.util.BeanPool<UserPunish> poolUserPunish = new mkdb.util.BeanPool<UserPunish>() {
		@Override
		protected UserPunish newBean() {
			return new xbean.__.UserPunish();
		}
	};

	public static UserPunish newUserPunish() {
		return poolUserPunish.get();
	}

	public static void _reset_unsafe_add_(UserPunish bean) {
		poolUserPunish._reset_unsafe_add_(bean);
	}

	public static void padd(UserPunish bean) {
		mkdb.Procedure.padd(bean, poolUserPunish);
	}

	public static UserPunish newUserPunishData() {
		return new xbean.__.UserPunish.Data();
	}

	public static mkdb.util.BeanPool<PunishRecord> poolPunishRecord = new mkdb.util.BeanPool<PunishRecord>() {
		@Override
		protected PunishRecord newBean() {
			return new xbean.__.PunishRecord();
		}
	};

	public static PunishRecord newPunishRecord() {
		return poolPunishRecord.get();
	}

	public static void _reset_unsafe_add_(PunishRecord bean) {
		poolPunishRecord._reset_unsafe_add_(bean);
	}

	public static void padd(PunishRecord bean) {
		mkdb.Procedure.padd(bean, poolPunishRecord);
	}

	public static PunishRecord newPunishRecordData() {
		return new xbean.__.PunishRecord.Data();
	}

	public static mkdb.util.BeanPool<GacdPicStatus> poolGacdPicStatus = new mkdb.util.BeanPool<GacdPicStatus>() {
		@Override
		protected GacdPicStatus newBean() {
			return new xbean.__.GacdPicStatus();
		}
	};

	public static GacdPicStatus newGacdPicStatus() {
		return poolGacdPicStatus.get();
	}

	public static void _reset_unsafe_add_(GacdPicStatus bean) {
		poolGacdPicStatus._reset_unsafe_add_(bean);
	}

	public static void padd(GacdPicStatus bean) {
		mkdb.Procedure.padd(bean, poolGacdPicStatus);
	}

	public static GacdPicStatus newGacdPicStatusData() {
		return new xbean.__.GacdPicStatus.Data();
	}

	public static mkdb.util.BeanPool<CardInfo> poolCardInfo = new mkdb.util.BeanPool<CardInfo>() {
		@Override
		protected CardInfo newBean() {
			return new xbean.__.CardInfo();
		}
	};

	public static CardInfo newCardInfo() {
		return poolCardInfo.get();
	}

	public static void _reset_unsafe_add_(CardInfo bean) {
		poolCardInfo._reset_unsafe_add_(bean);
	}

	public static void padd(CardInfo bean) {
		mkdb.Procedure.padd(bean, poolCardInfo);
	}

	public static CardInfo newCardInfoData() {
		return new xbean.__.CardInfo.Data();
	}

	public static mkdb.util.BeanPool<LimitItemInfo> poolLimitItemInfo = new mkdb.util.BeanPool<LimitItemInfo>() {
		@Override
		protected LimitItemInfo newBean() {
			return new xbean.__.LimitItemInfo();
		}
	};

	public static LimitItemInfo newLimitItemInfo() {
		return poolLimitItemInfo.get();
	}

	public static void _reset_unsafe_add_(LimitItemInfo bean) {
		poolLimitItemInfo._reset_unsafe_add_(bean);
	}

	public static void padd(LimitItemInfo bean) {
		mkdb.Procedure.padd(bean, poolLimitItemInfo);
	}

	public static LimitItemInfo newLimitItemInfoData() {
		return new xbean.__.LimitItemInfo.Data();
	}

	public static mkdb.util.BeanPool<ActivityItemLimit> poolActivityItemLimit = new mkdb.util.BeanPool<ActivityItemLimit>() {
		@Override
		protected ActivityItemLimit newBean() {
			return new xbean.__.ActivityItemLimit();
		}
	};

	public static ActivityItemLimit newActivityItemLimit() {
		return poolActivityItemLimit.get();
	}

	public static void _reset_unsafe_add_(ActivityItemLimit bean) {
		poolActivityItemLimit._reset_unsafe_add_(bean);
	}

	public static void padd(ActivityItemLimit bean) {
		mkdb.Procedure.padd(bean, poolActivityItemLimit);
	}

	public static ActivityItemLimit newActivityItemLimitData() {
		return new xbean.__.ActivityItemLimit.Data();
	}

	public static mkdb.util.BeanPool<CompensationInfo> poolCompensationInfo = new mkdb.util.BeanPool<CompensationInfo>() {
		@Override
		protected CompensationInfo newBean() {
			return new xbean.__.CompensationInfo();
		}
	};

	public static CompensationInfo newCompensationInfo() {
		return poolCompensationInfo.get();
	}

	public static void _reset_unsafe_add_(CompensationInfo bean) {
		poolCompensationInfo._reset_unsafe_add_(bean);
	}

	public static void padd(CompensationInfo bean) {
		mkdb.Procedure.padd(bean, poolCompensationInfo);
	}

	public static CompensationInfo newCompensationInfoData() {
		return new xbean.__.CompensationInfo.Data();
	}

	public static mkdb.util.BeanPool<Compensation> poolCompensation = new mkdb.util.BeanPool<Compensation>() {
		@Override
		protected Compensation newBean() {
			return new xbean.__.Compensation();
		}
	};

	public static Compensation newCompensation() {
		return poolCompensation.get();
	}

	public static void _reset_unsafe_add_(Compensation bean) {
		poolCompensation._reset_unsafe_add_(bean);
	}

	public static void padd(Compensation bean) {
		mkdb.Procedure.padd(bean, poolCompensation);
	}

	public static Compensation newCompensationData() {
		return new xbean.__.Compensation.Data();
	}

	public static mkdb.util.BeanPool<SingleCompensationCond> poolSingleCompensationCond = new mkdb.util.BeanPool<SingleCompensationCond>() {
		@Override
		protected SingleCompensationCond newBean() {
			return new xbean.__.SingleCompensationCond();
		}
	};

	public static SingleCompensationCond newSingleCompensationCond() {
		return poolSingleCompensationCond.get();
	}

	public static void _reset_unsafe_add_(SingleCompensationCond bean) {
		poolSingleCompensationCond._reset_unsafe_add_(bean);
	}

	public static void padd(SingleCompensationCond bean) {
		mkdb.Procedure.padd(bean, poolSingleCompensationCond);
	}

	public static SingleCompensationCond newSingleCompensationCondData() {
		return new xbean.__.SingleCompensationCond.Data();
	}

	public static mkdb.util.BeanPool<SingleCompensationAward> poolSingleCompensationAward = new mkdb.util.BeanPool<SingleCompensationAward>() {
		@Override
		protected SingleCompensationAward newBean() {
			return new xbean.__.SingleCompensationAward();
		}
	};

	public static SingleCompensationAward newSingleCompensationAward() {
		return poolSingleCompensationAward.get();
	}

	public static void _reset_unsafe_add_(SingleCompensationAward bean) {
		poolSingleCompensationAward._reset_unsafe_add_(bean);
	}

	public static void padd(SingleCompensationAward bean) {
		mkdb.Procedure.padd(bean, poolSingleCompensationAward);
	}

	public static SingleCompensationAward newSingleCompensationAwardData() {
		return new xbean.__.SingleCompensationAward.Data();
	}

	public static mkdb.util.BeanPool<SingleCompensation> poolSingleCompensation = new mkdb.util.BeanPool<SingleCompensation>() {
		@Override
		protected SingleCompensation newBean() {
			return new xbean.__.SingleCompensation();
		}
	};

	public static SingleCompensation newSingleCompensation() {
		return poolSingleCompensation.get();
	}

	public static void _reset_unsafe_add_(SingleCompensation bean) {
		poolSingleCompensation._reset_unsafe_add_(bean);
	}

	public static void padd(SingleCompensation bean) {
		mkdb.Procedure.padd(bean, poolSingleCompensation);
	}

	public static SingleCompensation newSingleCompensationData() {
		return new xbean.__.SingleCompensation.Data();
	}

	public static mkdb.util.BeanPool<CompensationRole> poolCompensationRole = new mkdb.util.BeanPool<CompensationRole>() {
		@Override
		protected CompensationRole newBean() {
			return new xbean.__.CompensationRole();
		}
	};

	public static CompensationRole newCompensationRole() {
		return poolCompensationRole.get();
	}

	public static void _reset_unsafe_add_(CompensationRole bean) {
		poolCompensationRole._reset_unsafe_add_(bean);
	}

	public static void padd(CompensationRole bean) {
		mkdb.Procedure.padd(bean, poolCompensationRole);
	}

	public static CompensationRole newCompensationRoleData() {
		return new xbean.__.CompensationRole.Data();
	}

	public static mkdb.util.BeanPool<ActivityStatus> poolActivityStatus = new mkdb.util.BeanPool<ActivityStatus>() {
		@Override
		protected ActivityStatus newBean() {
			return new xbean.__.ActivityStatus();
		}
	};

	public static ActivityStatus newActivityStatus() {
		return poolActivityStatus.get();
	}

	public static void _reset_unsafe_add_(ActivityStatus bean) {
		poolActivityStatus._reset_unsafe_add_(bean);
	}

	public static void padd(ActivityStatus bean) {
		mkdb.Procedure.padd(bean, poolActivityStatus);
	}

	public static ActivityStatus newActivityStatusData() {
		return new xbean.__.ActivityStatus.Data();
	}

	public static mkdb.util.BeanPool<InvestQuestionnaire> poolInvestQuestionnaire = new mkdb.util.BeanPool<InvestQuestionnaire>() {
		@Override
		protected InvestQuestionnaire newBean() {
			return new xbean.__.InvestQuestionnaire();
		}
	};

	public static InvestQuestionnaire newInvestQuestionnaire() {
		return poolInvestQuestionnaire.get();
	}

	public static void _reset_unsafe_add_(InvestQuestionnaire bean) {
		poolInvestQuestionnaire._reset_unsafe_add_(bean);
	}

	public static void padd(InvestQuestionnaire bean) {
		mkdb.Procedure.padd(bean, poolInvestQuestionnaire);
	}

	public static InvestQuestionnaire newInvestQuestionnaireData() {
		return new xbean.__.InvestQuestionnaire.Data();
	}

	public static mkdb.util.BeanPool<LevelSeal> poolLevelSeal = new mkdb.util.BeanPool<LevelSeal>() {
		@Override
		protected LevelSeal newBean() {
			return new xbean.__.LevelSeal();
		}
	};

	public static LevelSeal newLevelSeal() {
		return poolLevelSeal.get();
	}

	public static void _reset_unsafe_add_(LevelSeal bean) {
		poolLevelSeal._reset_unsafe_add_(bean);
	}

	public static void padd(LevelSeal bean) {
		mkdb.Procedure.padd(bean, poolLevelSeal);
	}

	public static LevelSeal newLevelSealData() {
		return new xbean.__.LevelSeal.Data();
	}

	public static mkdb.util.BeanPool<InstanceZone> poolInstanceZone = new mkdb.util.BeanPool<InstanceZone>() {
		@Override
		protected InstanceZone newBean() {
			return new xbean.__.InstanceZone();
		}
	};

	public static InstanceZone newInstanceZone() {
		return poolInstanceZone.get();
	}

	public static void _reset_unsafe_add_(InstanceZone bean) {
		poolInstanceZone._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceZone bean) {
		mkdb.Procedure.padd(bean, poolInstanceZone);
	}

	public static InstanceZone newInstanceZoneData() {
		return new xbean.__.InstanceZone.Data();
	}

	public static mkdb.util.BeanPool<InstanceNpcSave> poolInstanceNpcSave = new mkdb.util.BeanPool<InstanceNpcSave>() {
		@Override
		protected InstanceNpcSave newBean() {
			return new xbean.__.InstanceNpcSave();
		}
	};

	public static InstanceNpcSave newInstanceNpcSave() {
		return poolInstanceNpcSave.get();
	}

	public static void _reset_unsafe_add_(InstanceNpcSave bean) {
		poolInstanceNpcSave._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceNpcSave bean) {
		mkdb.Procedure.padd(bean, poolInstanceNpcSave);
	}

	public static InstanceNpcSave newInstanceNpcSaveData() {
		return new xbean.__.InstanceNpcSave.Data();
	}

	public static mkdb.util.BeanPool<InstanceSave> poolInstanceSave = new mkdb.util.BeanPool<InstanceSave>() {
		@Override
		protected InstanceSave newBean() {
			return new xbean.__.InstanceSave();
		}
	};

	public static InstanceSave newInstanceSave() {
		return poolInstanceSave.get();
	}

	public static void _reset_unsafe_add_(InstanceSave bean) {
		poolInstanceSave._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceSave bean) {
		mkdb.Procedure.padd(bean, poolInstanceSave);
	}

	public static InstanceSave newInstanceSaveData() {
		return new xbean.__.InstanceSave.Data();
	}

	public static mkdb.util.BeanPool<InstanceNpc> poolInstanceNpc = new mkdb.util.BeanPool<InstanceNpc>() {
		@Override
		protected InstanceNpc newBean() {
			return new xbean.__.InstanceNpc();
		}
	};

	public static InstanceNpc newInstanceNpc() {
		return poolInstanceNpc.get();
	}

	public static void _reset_unsafe_add_(InstanceNpc bean) {
		poolInstanceNpc._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceNpc bean) {
		mkdb.Procedure.padd(bean, poolInstanceNpc);
	}

	public static InstanceNpc newInstanceNpcData() {
		return new xbean.__.InstanceNpc.Data();
	}

	public static mkdb.util.BeanPool<InstanceNpcServ> poolInstanceNpcServ = new mkdb.util.BeanPool<InstanceNpcServ>() {
		@Override
		protected InstanceNpcServ newBean() {
			return new xbean.__.InstanceNpcServ();
		}
	};

	public static InstanceNpcServ newInstanceNpcServ() {
		return poolInstanceNpcServ.get();
	}

	public static void _reset_unsafe_add_(InstanceNpcServ bean) {
		poolInstanceNpcServ._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceNpcServ bean) {
		mkdb.Procedure.padd(bean, poolInstanceNpcServ);
	}

	public static InstanceNpcServ newInstanceNpcServData() {
		return new xbean.__.InstanceNpcServ.Data();
	}

	public static mkdb.util.BeanPool<InstanceTimer> poolInstanceTimer = new mkdb.util.BeanPool<InstanceTimer>() {
		@Override
		protected InstanceTimer newBean() {
			return new xbean.__.InstanceTimer();
		}
	};

	public static InstanceTimer newInstanceTimer() {
		return poolInstanceTimer.get();
	}

	public static void _reset_unsafe_add_(InstanceTimer bean) {
		poolInstanceTimer._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceTimer bean) {
		mkdb.Procedure.padd(bean, poolInstanceTimer);
	}

	public static InstanceTimer newInstanceTimerData() {
		return new xbean.__.InstanceTimer.Data();
	}

	public static mkdb.util.BeanPool<InstanceActivity> poolInstanceActivity = new mkdb.util.BeanPool<InstanceActivity>() {
		@Override
		protected InstanceActivity newBean() {
			return new xbean.__.InstanceActivity();
		}
	};

	public static InstanceActivity newInstanceActivity() {
		return poolInstanceActivity.get();
	}

	public static void _reset_unsafe_add_(InstanceActivity bean) {
		poolInstanceActivity._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceActivity bean) {
		mkdb.Procedure.padd(bean, poolInstanceActivity);
	}

	public static InstanceActivity newInstanceActivityData() {
		return new xbean.__.InstanceActivity.Data();
	}

	public static mkdb.util.BeanPool<InstanceTurnGroup> poolInstanceTurnGroup = new mkdb.util.BeanPool<InstanceTurnGroup>() {
		@Override
		protected InstanceTurnGroup newBean() {
			return new xbean.__.InstanceTurnGroup();
		}
	};

	public static InstanceTurnGroup newInstanceTurnGroup() {
		return poolInstanceTurnGroup.get();
	}

	public static void _reset_unsafe_add_(InstanceTurnGroup bean) {
		poolInstanceTurnGroup._reset_unsafe_add_(bean);
	}

	public static void padd(InstanceTurnGroup bean) {
		mkdb.Procedure.padd(bean, poolInstanceTurnGroup);
	}

	public static InstanceTurnGroup newInstanceTurnGroupData() {
		return new xbean.__.InstanceTurnGroup.Data();
	}

	public static mkdb.util.BeanPool<ClanSaveInfo> poolClanSaveInfo = new mkdb.util.BeanPool<ClanSaveInfo>() {
		@Override
		protected ClanSaveInfo newBean() {
			return new xbean.__.ClanSaveInfo();
		}
	};

	public static ClanSaveInfo newClanSaveInfo() {
		return poolClanSaveInfo.get();
	}

	public static void _reset_unsafe_add_(ClanSaveInfo bean) {
		poolClanSaveInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ClanSaveInfo bean) {
		mkdb.Procedure.padd(bean, poolClanSaveInfo);
	}

	public static ClanSaveInfo newClanSaveInfoData() {
		return new xbean.__.ClanSaveInfo.Data();
	}

	public static mkdb.util.BeanPool<ClanBossInfo> poolClanBossInfo = new mkdb.util.BeanPool<ClanBossInfo>() {
		@Override
		protected ClanBossInfo newBean() {
			return new xbean.__.ClanBossInfo();
		}
	};

	public static ClanBossInfo newClanBossInfo() {
		return poolClanBossInfo.get();
	}

	public static void _reset_unsafe_add_(ClanBossInfo bean) {
		poolClanBossInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ClanBossInfo bean) {
		mkdb.Procedure.padd(bean, poolClanBossInfo);
	}

	public static ClanBossInfo newClanBossInfoData() {
		return new xbean.__.ClanBossInfo.Data();
	}

	public static mkdb.util.BeanPool<ClanInstances> poolClanInstances = new mkdb.util.BeanPool<ClanInstances>() {
		@Override
		protected ClanInstances newBean() {
			return new xbean.__.ClanInstances();
		}
	};

	public static ClanInstances newClanInstances() {
		return poolClanInstances.get();
	}

	public static void _reset_unsafe_add_(ClanInstances bean) {
		poolClanInstances._reset_unsafe_add_(bean);
	}

	public static void padd(ClanInstances bean) {
		mkdb.Procedure.padd(bean, poolClanInstances);
	}

	public static ClanInstances newClanInstancesData() {
		return new xbean.__.ClanInstances.Data();
	}

	public static mkdb.util.BeanPool<ClanInstBestLevel> poolClanInstBestLevel = new mkdb.util.BeanPool<ClanInstBestLevel>() {
		@Override
		protected ClanInstBestLevel newBean() {
			return new xbean.__.ClanInstBestLevel();
		}
	};

	public static ClanInstBestLevel newClanInstBestLevel() {
		return poolClanInstBestLevel.get();
	}

	public static void _reset_unsafe_add_(ClanInstBestLevel bean) {
		poolClanInstBestLevel._reset_unsafe_add_(bean);
	}

	public static void padd(ClanInstBestLevel bean) {
		mkdb.Procedure.padd(bean, poolClanInstBestLevel);
	}

	public static ClanInstBestLevel newClanInstBestLevelData() {
		return new xbean.__.ClanInstBestLevel.Data();
	}

	public static mkdb.util.BeanPool<RoleBFInfo> poolRoleBFInfo = new mkdb.util.BeanPool<RoleBFInfo>() {
		@Override
		protected RoleBFInfo newBean() {
			return new xbean.__.RoleBFInfo();
		}
	};

	public static RoleBFInfo newRoleBFInfo() {
		return poolRoleBFInfo.get();
	}

	public static void _reset_unsafe_add_(RoleBFInfo bean) {
		poolRoleBFInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleBFInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleBFInfo);
	}

	public static RoleBFInfo newRoleBFInfoData() {
		return new xbean.__.RoleBFInfo.Data();
	}

	public static mkdb.util.BeanPool<BingFengRole> poolBingFengRole = new mkdb.util.BeanPool<BingFengRole>() {
		@Override
		protected BingFengRole newBean() {
			return new xbean.__.BingFengRole();
		}
	};

	public static BingFengRole newBingFengRole() {
		return poolBingFengRole.get();
	}

	public static void _reset_unsafe_add_(BingFengRole bean) {
		poolBingFengRole._reset_unsafe_add_(bean);
	}

	public static void padd(BingFengRole bean) {
		mkdb.Procedure.padd(bean, poolBingFengRole);
	}

	public static BingFengRole newBingFengRoleData() {
		return new xbean.__.BingFengRole.Data();
	}

	public static mkdb.util.BeanPool<BingFengWangZuo> poolBingFengWangZuo = new mkdb.util.BeanPool<BingFengWangZuo>() {
		@Override
		protected BingFengWangZuo newBean() {
			return new xbean.__.BingFengWangZuo();
		}
	};

	public static BingFengWangZuo newBingFengWangZuo() {
		return poolBingFengWangZuo.get();
	}

	public static void _reset_unsafe_add_(BingFengWangZuo bean) {
		poolBingFengWangZuo._reset_unsafe_add_(bean);
	}

	public static void padd(BingFengWangZuo bean) {
		mkdb.Procedure.padd(bean, poolBingFengWangZuo);
	}

	public static BingFengWangZuo newBingFengWangZuoData() {
		return new xbean.__.BingFengWangZuo.Data();
	}

	public static mkdb.util.BeanPool<SchoolStageTime> poolSchoolStageTime = new mkdb.util.BeanPool<SchoolStageTime>() {
		@Override
		protected SchoolStageTime newBean() {
			return new xbean.__.SchoolStageTime();
		}
	};

	public static SchoolStageTime newSchoolStageTime() {
		return poolSchoolStageTime.get();
	}

	public static void _reset_unsafe_add_(SchoolStageTime bean) {
		poolSchoolStageTime._reset_unsafe_add_(bean);
	}

	public static void padd(SchoolStageTime bean) {
		mkdb.Procedure.padd(bean, poolSchoolStageTime);
	}

	public static SchoolStageTime newSchoolStageTimeData() {
		return new xbean.__.SchoolStageTime.Data();
	}

	public static mkdb.util.BeanPool<BingFengInfo> poolBingFengInfo = new mkdb.util.BeanPool<BingFengInfo>() {
		@Override
		protected BingFengInfo newBean() {
			return new xbean.__.BingFengInfo();
		}
	};

	public static BingFengInfo newBingFengInfo() {
		return poolBingFengInfo.get();
	}

	public static void _reset_unsafe_add_(BingFengInfo bean) {
		poolBingFengInfo._reset_unsafe_add_(bean);
	}

	public static void padd(BingFengInfo bean) {
		mkdb.Procedure.padd(bean, poolBingFengInfo);
	}

	public static BingFengInfo newBingFengInfoData() {
		return new xbean.__.BingFengInfo.Data();
	}

	public static mkdb.util.BeanPool<ItemRecycleIndex> poolItemRecycleIndex = new mkdb.util.BeanPool<ItemRecycleIndex>() {
		@Override
		protected ItemRecycleIndex newBean() {
			return new xbean.__.ItemRecycleIndex();
		}
	};

	public static ItemRecycleIndex newItemRecycleIndex() {
		return poolItemRecycleIndex.get();
	}

	public static void _reset_unsafe_add_(ItemRecycleIndex bean) {
		poolItemRecycleIndex._reset_unsafe_add_(bean);
	}

	public static void padd(ItemRecycleIndex bean) {
		mkdb.Procedure.padd(bean, poolItemRecycleIndex);
	}

	public static ItemRecycleIndex newItemRecycleIndexData() {
		return new xbean.__.ItemRecycleIndex.Data();
	}

	public static mkdb.util.BeanPool<TriggerRole> poolTriggerRole = new mkdb.util.BeanPool<TriggerRole>() {
		@Override
		protected TriggerRole newBean() {
			return new xbean.__.TriggerRole();
		}
	};

	public static TriggerRole newTriggerRole() {
		return poolTriggerRole.get();
	}

	public static void _reset_unsafe_add_(TriggerRole bean) {
		poolTriggerRole._reset_unsafe_add_(bean);
	}

	public static void padd(TriggerRole bean) {
		mkdb.Procedure.padd(bean, poolTriggerRole);
	}

	public static TriggerRole newTriggerRoleData() {
		return new xbean.__.TriggerRole.Data();
	}

	public static mkdb.util.BeanPool<RandomNameInfo> poolRandomNameInfo = new mkdb.util.BeanPool<RandomNameInfo>() {
		@Override
		protected RandomNameInfo newBean() {
			return new xbean.__.RandomNameInfo();
		}
	};

	public static RandomNameInfo newRandomNameInfo() {
		return poolRandomNameInfo.get();
	}

	public static void _reset_unsafe_add_(RandomNameInfo bean) {
		poolRandomNameInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RandomNameInfo bean) {
		mkdb.Procedure.padd(bean, poolRandomNameInfo);
	}

	public static RandomNameInfo newRandomNameInfoData() {
		return new xbean.__.RandomNameInfo.Data();
	}

	public static mkdb.util.BeanPool<EventInfo> poolEventInfo = new mkdb.util.BeanPool<EventInfo>() {
		@Override
		protected EventInfo newBean() {
			return new xbean.__.EventInfo();
		}
	};

	public static EventInfo newEventInfo() {
		return poolEventInfo.get();
	}

	public static void _reset_unsafe_add_(EventInfo bean) {
		poolEventInfo._reset_unsafe_add_(bean);
	}

	public static void padd(EventInfo bean) {
		mkdb.Procedure.padd(bean, poolEventInfo);
	}

	public static EventInfo newEventInfoData() {
		return new xbean.__.EventInfo.Data();
	}

	public static mkdb.util.BeanPool<OnetimeEvent> poolOnetimeEvent = new mkdb.util.BeanPool<OnetimeEvent>() {
		@Override
		protected OnetimeEvent newBean() {
			return new xbean.__.OnetimeEvent();
		}
	};

	public static OnetimeEvent newOnetimeEvent() {
		return poolOnetimeEvent.get();
	}

	public static void _reset_unsafe_add_(OnetimeEvent bean) {
		poolOnetimeEvent._reset_unsafe_add_(bean);
	}

	public static void padd(OnetimeEvent bean) {
		mkdb.Procedure.padd(bean, poolOnetimeEvent);
	}

	public static OnetimeEvent newOnetimeEventData() {
		return new xbean.__.OnetimeEvent.Data();
	}

	public static mkdb.util.BeanPool<TaskEventInfo> poolTaskEventInfo = new mkdb.util.BeanPool<TaskEventInfo>() {
		@Override
		protected TaskEventInfo newBean() {
			return new xbean.__.TaskEventInfo();
		}
	};

	public static TaskEventInfo newTaskEventInfo() {
		return poolTaskEventInfo.get();
	}

	public static void _reset_unsafe_add_(TaskEventInfo bean) {
		poolTaskEventInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TaskEventInfo bean) {
		mkdb.Procedure.padd(bean, poolTaskEventInfo);
	}

	public static TaskEventInfo newTaskEventInfoData() {
		return new xbean.__.TaskEventInfo.Data();
	}

	public static mkdb.util.BeanPool<PlatformInfo> poolPlatformInfo = new mkdb.util.BeanPool<PlatformInfo>() {
		@Override
		protected PlatformInfo newBean() {
			return new xbean.__.PlatformInfo();
		}
	};

	public static PlatformInfo newPlatformInfo() {
		return poolPlatformInfo.get();
	}

	public static void _reset_unsafe_add_(PlatformInfo bean) {
		poolPlatformInfo._reset_unsafe_add_(bean);
	}

	public static void padd(PlatformInfo bean) {
		mkdb.Procedure.padd(bean, poolPlatformInfo);
	}

	public static PlatformInfo newPlatformInfoData() {
		return new xbean.__.PlatformInfo.Data();
	}

	public static mkdb.util.BeanPool<ServerRoles> poolServerRoles = new mkdb.util.BeanPool<ServerRoles>() {
		@Override
		protected ServerRoles newBean() {
			return new xbean.__.ServerRoles();
		}
	};

	public static ServerRoles newServerRoles() {
		return poolServerRoles.get();
	}

	public static void _reset_unsafe_add_(ServerRoles bean) {
		poolServerRoles._reset_unsafe_add_(bean);
	}

	public static void padd(ServerRoles bean) {
		mkdb.Procedure.padd(bean, poolServerRoles);
	}

	public static ServerRoles newServerRolesData() {
		return new xbean.__.ServerRoles.Data();
	}

	public static mkdb.util.BeanPool<PvP1Role> poolPvP1Role = new mkdb.util.BeanPool<PvP1Role>() {
		@Override
		protected PvP1Role newBean() {
			return new xbean.__.PvP1Role();
		}
	};

	public static PvP1Role newPvP1Role() {
		return poolPvP1Role.get();
	}

	public static void _reset_unsafe_add_(PvP1Role bean) {
		poolPvP1Role._reset_unsafe_add_(bean);
	}

	public static void padd(PvP1Role bean) {
		mkdb.Procedure.padd(bean, poolPvP1Role);
	}

	public static PvP1Role newPvP1RoleData() {
		return new xbean.__.PvP1Role.Data();
	}

	public static mkdb.util.BeanPool<PvP1RaceRole> poolPvP1RaceRole = new mkdb.util.BeanPool<PvP1RaceRole>() {
		@Override
		protected PvP1RaceRole newBean() {
			return new xbean.__.PvP1RaceRole();
		}
	};

	public static PvP1RaceRole newPvP1RaceRole() {
		return poolPvP1RaceRole.get();
	}

	public static void _reset_unsafe_add_(PvP1RaceRole bean) {
		poolPvP1RaceRole._reset_unsafe_add_(bean);
	}

	public static void padd(PvP1RaceRole bean) {
		mkdb.Procedure.padd(bean, poolPvP1RaceRole);
	}

	public static PvP1RaceRole newPvP1RaceRoleData() {
		return new xbean.__.PvP1RaceRole.Data();
	}

	public static mkdb.util.BeanPool<PvP1QueueRole> poolPvP1QueueRole = new mkdb.util.BeanPool<PvP1QueueRole>() {
		@Override
		protected PvP1QueueRole newBean() {
			return new xbean.__.PvP1QueueRole();
		}
	};

	public static PvP1QueueRole newPvP1QueueRole() {
		return poolPvP1QueueRole.get();
	}

	public static void _reset_unsafe_add_(PvP1QueueRole bean) {
		poolPvP1QueueRole._reset_unsafe_add_(bean);
	}

	public static void padd(PvP1QueueRole bean) {
		mkdb.Procedure.padd(bean, poolPvP1QueueRole);
	}

	public static PvP1QueueRole newPvP1QueueRoleData() {
		return new xbean.__.PvP1QueueRole.Data();
	}

	public static mkdb.util.BeanPool<PvP1Race> poolPvP1Race = new mkdb.util.BeanPool<PvP1Race>() {
		@Override
		protected PvP1Race newBean() {
			return new xbean.__.PvP1Race();
		}
	};

	public static PvP1Race newPvP1Race() {
		return poolPvP1Race.get();
	}

	public static void _reset_unsafe_add_(PvP1Race bean) {
		poolPvP1Race._reset_unsafe_add_(bean);
	}

	public static void padd(PvP1Race bean) {
		mkdb.Procedure.padd(bean, poolPvP1Race);
	}

	public static PvP1Race newPvP1RaceData() {
		return new xbean.__.PvP1Race.Data();
	}

	public static mkdb.util.BeanPool<PvP3Role> poolPvP3Role = new mkdb.util.BeanPool<PvP3Role>() {
		@Override
		protected PvP3Role newBean() {
			return new xbean.__.PvP3Role();
		}
	};

	public static PvP3Role newPvP3Role() {
		return poolPvP3Role.get();
	}

	public static void _reset_unsafe_add_(PvP3Role bean) {
		poolPvP3Role._reset_unsafe_add_(bean);
	}

	public static void padd(PvP3Role bean) {
		mkdb.Procedure.padd(bean, poolPvP3Role);
	}

	public static PvP3Role newPvP3RoleData() {
		return new xbean.__.PvP3Role.Data();
	}

	public static mkdb.util.BeanPool<PvP3RaceRole> poolPvP3RaceRole = new mkdb.util.BeanPool<PvP3RaceRole>() {
		@Override
		protected PvP3RaceRole newBean() {
			return new xbean.__.PvP3RaceRole();
		}
	};

	public static PvP3RaceRole newPvP3RaceRole() {
		return poolPvP3RaceRole.get();
	}

	public static void _reset_unsafe_add_(PvP3RaceRole bean) {
		poolPvP3RaceRole._reset_unsafe_add_(bean);
	}

	public static void padd(PvP3RaceRole bean) {
		mkdb.Procedure.padd(bean, poolPvP3RaceRole);
	}

	public static PvP3RaceRole newPvP3RaceRoleData() {
		return new xbean.__.PvP3RaceRole.Data();
	}

	public static mkdb.util.BeanPool<PvP3QueueRole> poolPvP3QueueRole = new mkdb.util.BeanPool<PvP3QueueRole>() {
		@Override
		protected PvP3QueueRole newBean() {
			return new xbean.__.PvP3QueueRole();
		}
	};

	public static PvP3QueueRole newPvP3QueueRole() {
		return poolPvP3QueueRole.get();
	}

	public static void _reset_unsafe_add_(PvP3QueueRole bean) {
		poolPvP3QueueRole._reset_unsafe_add_(bean);
	}

	public static void padd(PvP3QueueRole bean) {
		mkdb.Procedure.padd(bean, poolPvP3QueueRole);
	}

	public static PvP3QueueRole newPvP3QueueRoleData() {
		return new xbean.__.PvP3QueueRole.Data();
	}

	public static mkdb.util.BeanPool<PvP3Race> poolPvP3Race = new mkdb.util.BeanPool<PvP3Race>() {
		@Override
		protected PvP3Race newBean() {
			return new xbean.__.PvP3Race();
		}
	};

	public static PvP3Race newPvP3Race() {
		return poolPvP3Race.get();
	}

	public static void _reset_unsafe_add_(PvP3Race bean) {
		poolPvP3Race._reset_unsafe_add_(bean);
	}

	public static void padd(PvP3Race bean) {
		mkdb.Procedure.padd(bean, poolPvP3Race);
	}

	public static PvP3Race newPvP3RaceData() {
		return new xbean.__.PvP3Race.Data();
	}

	public static mkdb.util.BeanPool<PvP5Role> poolPvP5Role = new mkdb.util.BeanPool<PvP5Role>() {
		@Override
		protected PvP5Role newBean() {
			return new xbean.__.PvP5Role();
		}
	};

	public static PvP5Role newPvP5Role() {
		return poolPvP5Role.get();
	}

	public static void _reset_unsafe_add_(PvP5Role bean) {
		poolPvP5Role._reset_unsafe_add_(bean);
	}

	public static void padd(PvP5Role bean) {
		mkdb.Procedure.padd(bean, poolPvP5Role);
	}

	public static PvP5Role newPvP5RoleData() {
		return new xbean.__.PvP5Role.Data();
	}

	public static mkdb.util.BeanPool<PvP5RaceRole> poolPvP5RaceRole = new mkdb.util.BeanPool<PvP5RaceRole>() {
		@Override
		protected PvP5RaceRole newBean() {
			return new xbean.__.PvP5RaceRole();
		}
	};

	public static PvP5RaceRole newPvP5RaceRole() {
		return poolPvP5RaceRole.get();
	}

	public static void _reset_unsafe_add_(PvP5RaceRole bean) {
		poolPvP5RaceRole._reset_unsafe_add_(bean);
	}

	public static void padd(PvP5RaceRole bean) {
		mkdb.Procedure.padd(bean, poolPvP5RaceRole);
	}

	public static PvP5RaceRole newPvP5RaceRoleData() {
		return new xbean.__.PvP5RaceRole.Data();
	}

	public static mkdb.util.BeanPool<PvP5QueueRole> poolPvP5QueueRole = new mkdb.util.BeanPool<PvP5QueueRole>() {
		@Override
		protected PvP5QueueRole newBean() {
			return new xbean.__.PvP5QueueRole();
		}
	};

	public static PvP5QueueRole newPvP5QueueRole() {
		return poolPvP5QueueRole.get();
	}

	public static void _reset_unsafe_add_(PvP5QueueRole bean) {
		poolPvP5QueueRole._reset_unsafe_add_(bean);
	}

	public static void padd(PvP5QueueRole bean) {
		mkdb.Procedure.padd(bean, poolPvP5QueueRole);
	}

	public static PvP5QueueRole newPvP5QueueRoleData() {
		return new xbean.__.PvP5QueueRole.Data();
	}

	public static mkdb.util.BeanPool<PvP5Race> poolPvP5Race = new mkdb.util.BeanPool<PvP5Race>() {
		@Override
		protected PvP5Race newBean() {
			return new xbean.__.PvP5Race();
		}
	};

	public static PvP5Race newPvP5Race() {
		return poolPvP5Race.get();
	}

	public static void _reset_unsafe_add_(PvP5Race bean) {
		poolPvP5Race._reset_unsafe_add_(bean);
	}

	public static void padd(PvP5Race bean) {
		mkdb.Procedure.padd(bean, poolPvP5Race);
	}

	public static PvP5Race newPvP5RaceData() {
		return new xbean.__.PvP5Race.Data();
	}

	public static mkdb.util.BeanPool<ModifyNameRole> poolModifyNameRole = new mkdb.util.BeanPool<ModifyNameRole>() {
		@Override
		protected ModifyNameRole newBean() {
			return new xbean.__.ModifyNameRole();
		}
	};

	public static ModifyNameRole newModifyNameRole() {
		return poolModifyNameRole.get();
	}

	public static void _reset_unsafe_add_(ModifyNameRole bean) {
		poolModifyNameRole._reset_unsafe_add_(bean);
	}

	public static void padd(ModifyNameRole bean) {
		mkdb.Procedure.padd(bean, poolModifyNameRole);
	}

	public static ModifyNameRole newModifyNameRoleData() {
		return new xbean.__.ModifyNameRole.Data();
	}

	public static mkdb.util.BeanPool<ChargeOrder> poolChargeOrder = new mkdb.util.BeanPool<ChargeOrder>() {
		@Override
		protected ChargeOrder newBean() {
			return new xbean.__.ChargeOrder();
		}
	};

	public static ChargeOrder newChargeOrder() {
		return poolChargeOrder.get();
	}

	public static void _reset_unsafe_add_(ChargeOrder bean) {
		poolChargeOrder._reset_unsafe_add_(bean);
	}

	public static void padd(ChargeOrder bean) {
		mkdb.Procedure.padd(bean, poolChargeOrder);
	}

	public static ChargeOrder newChargeOrderData() {
		return new xbean.__.ChargeOrder.Data();
	}

	public static mkdb.util.BeanPool<ChargeHistory> poolChargeHistory = new mkdb.util.BeanPool<ChargeHistory>() {
		@Override
		protected ChargeHistory newBean() {
			return new xbean.__.ChargeHistory();
		}
	};

	public static ChargeHistory newChargeHistory() {
		return poolChargeHistory.get();
	}

	public static void _reset_unsafe_add_(ChargeHistory bean) {
		poolChargeHistory._reset_unsafe_add_(bean);
	}

	public static void padd(ChargeHistory bean) {
		mkdb.Procedure.padd(bean, poolChargeHistory);
	}

	public static ChargeHistory newChargeHistoryData() {
		return new xbean.__.ChargeHistory.Data();
	}

	public static mkdb.util.BeanPool<AppstoretidStatus> poolAppstoretidStatus = new mkdb.util.BeanPool<AppstoretidStatus>() {
		@Override
		protected AppstoretidStatus newBean() {
			return new xbean.__.AppstoretidStatus();
		}
	};

	public static AppstoretidStatus newAppstoretidStatus() {
		return poolAppstoretidStatus.get();
	}

	public static void _reset_unsafe_add_(AppstoretidStatus bean) {
		poolAppstoretidStatus._reset_unsafe_add_(bean);
	}

	public static void padd(AppstoretidStatus bean) {
		mkdb.Procedure.padd(bean, poolAppstoretidStatus);
	}

	public static AppstoretidStatus newAppstoretidStatusData() {
		return new xbean.__.AppstoretidStatus.Data();
	}

	public static mkdb.util.BeanPool<ChargeRecord> poolChargeRecord = new mkdb.util.BeanPool<ChargeRecord>() {
		@Override
		protected ChargeRecord newBean() {
			return new xbean.__.ChargeRecord();
		}
	};

	public static ChargeRecord newChargeRecord() {
		return poolChargeRecord.get();
	}

	public static void _reset_unsafe_add_(ChargeRecord bean) {
		poolChargeRecord._reset_unsafe_add_(bean);
	}

	public static void padd(ChargeRecord bean) {
		mkdb.Procedure.padd(bean, poolChargeRecord);
	}

	public static ChargeRecord newChargeRecordData() {
		return new xbean.__.ChargeRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleChargeLimit> poolRoleChargeLimit = new mkdb.util.BeanPool<RoleChargeLimit>() {
		@Override
		protected RoleChargeLimit newBean() {
			return new xbean.__.RoleChargeLimit();
		}
	};

	public static RoleChargeLimit newRoleChargeLimit() {
		return poolRoleChargeLimit.get();
	}

	public static void _reset_unsafe_add_(RoleChargeLimit bean) {
		poolRoleChargeLimit._reset_unsafe_add_(bean);
	}

	public static void padd(RoleChargeLimit bean) {
		mkdb.Procedure.padd(bean, poolRoleChargeLimit);
	}

	public static RoleChargeLimit newRoleChargeLimitData() {
		return new xbean.__.RoleChargeLimit.Data();
	}

	public static mkdb.util.BeanPool<RollCardInfo> poolRollCardInfo = new mkdb.util.BeanPool<RollCardInfo>() {
		@Override
		protected RollCardInfo newBean() {
			return new xbean.__.RollCardInfo();
		}
	};

	public static RollCardInfo newRollCardInfo() {
		return poolRollCardInfo.get();
	}

	public static void _reset_unsafe_add_(RollCardInfo bean) {
		poolRollCardInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RollCardInfo bean) {
		mkdb.Procedure.padd(bean, poolRollCardInfo);
	}

	public static RollCardInfo newRollCardInfoData() {
		return new xbean.__.RollCardInfo.Data();
	}

	public static mkdb.util.BeanPool<WelfareBean> poolWelfareBean = new mkdb.util.BeanPool<WelfareBean>() {
		@Override
		protected WelfareBean newBean() {
			return new xbean.__.WelfareBean();
		}
	};

	public static WelfareBean newWelfareBean() {
		return poolWelfareBean.get();
	}

	public static void _reset_unsafe_add_(WelfareBean bean) {
		poolWelfareBean._reset_unsafe_add_(bean);
	}

	public static void padd(WelfareBean bean) {
		mkdb.Procedure.padd(bean, poolWelfareBean);
	}

	public static WelfareBean newWelfareBeanData() {
		return new xbean.__.WelfareBean.Data();
	}

	public static mkdb.util.BeanPool<RoleFutureNotifyMap> poolRoleFutureNotifyMap = new mkdb.util.BeanPool<RoleFutureNotifyMap>() {
		@Override
		protected RoleFutureNotifyMap newBean() {
			return new xbean.__.RoleFutureNotifyMap();
		}
	};

	public static RoleFutureNotifyMap newRoleFutureNotifyMap() {
		return poolRoleFutureNotifyMap.get();
	}

	public static void _reset_unsafe_add_(RoleFutureNotifyMap bean) {
		poolRoleFutureNotifyMap._reset_unsafe_add_(bean);
	}

	public static void padd(RoleFutureNotifyMap bean) {
		mkdb.Procedure.padd(bean, poolRoleFutureNotifyMap);
	}

	public static RoleFutureNotifyMap newRoleFutureNotifyMapData() {
		return new xbean.__.RoleFutureNotifyMap.Data();
	}

	public static mkdb.util.BeanPool<RolesNotifyMap> poolRolesNotifyMap = new mkdb.util.BeanPool<RolesNotifyMap>() {
		@Override
		protected RolesNotifyMap newBean() {
			return new xbean.__.RolesNotifyMap();
		}
	};

	public static RolesNotifyMap newRolesNotifyMap() {
		return poolRolesNotifyMap.get();
	}

	public static void _reset_unsafe_add_(RolesNotifyMap bean) {
		poolRolesNotifyMap._reset_unsafe_add_(bean);
	}

	public static void padd(RolesNotifyMap bean) {
		mkdb.Procedure.padd(bean, poolRolesNotifyMap);
	}

	public static RolesNotifyMap newRolesNotifyMapData() {
		return new xbean.__.RolesNotifyMap.Data();
	}

	public static mkdb.util.BeanPool<InvitePeopleInfo> poolInvitePeopleInfo = new mkdb.util.BeanPool<InvitePeopleInfo>() {
		@Override
		protected InvitePeopleInfo newBean() {
			return new xbean.__.InvitePeopleInfo();
		}
	};

	public static InvitePeopleInfo newInvitePeopleInfo() {
		return poolInvitePeopleInfo.get();
	}

	public static void _reset_unsafe_add_(InvitePeopleInfo bean) {
		poolInvitePeopleInfo._reset_unsafe_add_(bean);
	}

	public static void padd(InvitePeopleInfo bean) {
		mkdb.Procedure.padd(bean, poolInvitePeopleInfo);
	}

	public static InvitePeopleInfo newInvitePeopleInfoData() {
		return new xbean.__.InvitePeopleInfo.Data();
	}

	public static mkdb.util.BeanPool<OffLineHook> poolOffLineHook = new mkdb.util.BeanPool<OffLineHook>() {
		@Override
		protected OffLineHook newBean() {
			return new xbean.__.OffLineHook();
		}
	};

	public static OffLineHook newOffLineHook() {
		return poolOffLineHook.get();
	}

	public static void _reset_unsafe_add_(OffLineHook bean) {
		poolOffLineHook._reset_unsafe_add_(bean);
	}

	public static void padd(OffLineHook bean) {
		mkdb.Procedure.padd(bean, poolOffLineHook);
	}

	public static OffLineHook newOffLineHookData() {
		return new xbean.__.OffLineHook.Data();
	}

	public static mkdb.util.BeanPool<TransfromByItemData> poolTransfromByItemData = new mkdb.util.BeanPool<TransfromByItemData>() {
		@Override
		protected TransfromByItemData newBean() {
			return new xbean.__.TransfromByItemData();
		}
	};

	public static TransfromByItemData newTransfromByItemData() {
		return poolTransfromByItemData.get();
	}

	public static void _reset_unsafe_add_(TransfromByItemData bean) {
		poolTransfromByItemData._reset_unsafe_add_(bean);
	}

	public static void padd(TransfromByItemData bean) {
		mkdb.Procedure.padd(bean, poolTransfromByItemData);
	}

	public static TransfromByItemData newTransfromByItemDataData() {
		return new xbean.__.TransfromByItemData.Data();
	}

	public static mkdb.util.BeanPool<TicketItemExInfo> poolTicketItemExInfo = new mkdb.util.BeanPool<TicketItemExInfo>() {
		@Override
		protected TicketItemExInfo newBean() {
			return new xbean.__.TicketItemExInfo();
		}
	};

	public static TicketItemExInfo newTicketItemExInfo() {
		return poolTicketItemExInfo.get();
	}

	public static void _reset_unsafe_add_(TicketItemExInfo bean) {
		poolTicketItemExInfo._reset_unsafe_add_(bean);
	}

	public static void padd(TicketItemExInfo bean) {
		mkdb.Procedure.padd(bean, poolTicketItemExInfo);
	}

	public static TicketItemExInfo newTicketItemExInfoData() {
		return new xbean.__.TicketItemExInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleLockInfo> poolRoleLockInfo = new mkdb.util.BeanPool<RoleLockInfo>() {
		@Override
		protected RoleLockInfo newBean() {
			return new xbean.__.RoleLockInfo();
		}
	};

	public static RoleLockInfo newRoleLockInfo() {
		return poolRoleLockInfo.get();
	}

	public static void _reset_unsafe_add_(RoleLockInfo bean) {
		poolRoleLockInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleLockInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleLockInfo);
	}

	public static RoleLockInfo newRoleLockInfoData() {
		return new xbean.__.RoleLockInfo.Data();
	}

	public static mkdb.util.BeanPool<totalNumber> pooltotalNumber = new mkdb.util.BeanPool<totalNumber>() {
		@Override
		protected totalNumber newBean() {
			return new xbean.__.totalNumber();
		}
	};

	public static totalNumber newtotalNumber() {
		return pooltotalNumber.get();
	}

	public static void _reset_unsafe_add_(totalNumber bean) {
		pooltotalNumber._reset_unsafe_add_(bean);
	}

	public static void padd(totalNumber bean) {
		mkdb.Procedure.padd(bean, pooltotalNumber);
	}

	public static totalNumber newtotalNumberData() {
		return new xbean.__.totalNumber.Data();
	}

	public static mkdb.util.BeanPool<MulDayLoginGift> poolMulDayLoginGift = new mkdb.util.BeanPool<MulDayLoginGift>() {
		@Override
		protected MulDayLoginGift newBean() {
			return new xbean.__.MulDayLoginGift();
		}
	};

	public static MulDayLoginGift newMulDayLoginGift() {
		return poolMulDayLoginGift.get();
	}

	public static void _reset_unsafe_add_(MulDayLoginGift bean) {
		poolMulDayLoginGift._reset_unsafe_add_(bean);
	}

	public static void padd(MulDayLoginGift bean) {
		mkdb.Procedure.padd(bean, poolMulDayLoginGift);
	}

	public static MulDayLoginGift newMulDayLoginGiftData() {
		return new xbean.__.MulDayLoginGift.Data();
	}

	public static mkdb.util.BeanPool<FestivalData> poolFestivalData = new mkdb.util.BeanPool<FestivalData>() {
		@Override
		protected FestivalData newBean() {
			return new xbean.__.FestivalData();
		}
	};

	public static FestivalData newFestivalData() {
		return poolFestivalData.get();
	}

	public static void _reset_unsafe_add_(FestivalData bean) {
		poolFestivalData._reset_unsafe_add_(bean);
	}

	public static void padd(FestivalData bean) {
		mkdb.Procedure.padd(bean, poolFestivalData);
	}

	public static FestivalData newFestivalDataData() {
		return new xbean.__.FestivalData.Data();
	}

	public static mkdb.util.BeanPool<RefreshMonsterNpcInfo> poolRefreshMonsterNpcInfo = new mkdb.util.BeanPool<RefreshMonsterNpcInfo>() {
		@Override
		protected RefreshMonsterNpcInfo newBean() {
			return new xbean.__.RefreshMonsterNpcInfo();
		}
	};

	public static RefreshMonsterNpcInfo newRefreshMonsterNpcInfo() {
		return poolRefreshMonsterNpcInfo.get();
	}

	public static void _reset_unsafe_add_(RefreshMonsterNpcInfo bean) {
		poolRefreshMonsterNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RefreshMonsterNpcInfo bean) {
		mkdb.Procedure.padd(bean, poolRefreshMonsterNpcInfo);
	}

	public static RefreshMonsterNpcInfo newRefreshMonsterNpcInfoData() {
		return new xbean.__.RefreshMonsterNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<timerNpcInfo> pooltimerNpcInfo = new mkdb.util.BeanPool<timerNpcInfo>() {
		@Override
		protected timerNpcInfo newBean() {
			return new xbean.__.timerNpcInfo();
		}
	};

	public static timerNpcInfo newtimerNpcInfo() {
		return pooltimerNpcInfo.get();
	}

	public static void _reset_unsafe_add_(timerNpcInfo bean) {
		pooltimerNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(timerNpcInfo bean) {
		mkdb.Procedure.padd(bean, pooltimerNpcInfo);
	}

	public static timerNpcInfo newtimerNpcInfoData() {
		return new xbean.__.timerNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<timerNpcInfoCol> pooltimerNpcInfoCol = new mkdb.util.BeanPool<timerNpcInfoCol>() {
		@Override
		protected timerNpcInfoCol newBean() {
			return new xbean.__.timerNpcInfoCol();
		}
	};

	public static timerNpcInfoCol newtimerNpcInfoCol() {
		return pooltimerNpcInfoCol.get();
	}

	public static void _reset_unsafe_add_(timerNpcInfoCol bean) {
		pooltimerNpcInfoCol._reset_unsafe_add_(bean);
	}

	public static void padd(timerNpcInfoCol bean) {
		mkdb.Procedure.padd(bean, pooltimerNpcInfoCol);
	}

	public static timerNpcInfoCol newtimerNpcInfoColData() {
		return new xbean.__.timerNpcInfoCol.Data();
	}

	public static mkdb.util.BeanPool<eventNpcInfo> pooleventNpcInfo = new mkdb.util.BeanPool<eventNpcInfo>() {
		@Override
		protected eventNpcInfo newBean() {
			return new xbean.__.eventNpcInfo();
		}
	};

	public static eventNpcInfo neweventNpcInfo() {
		return pooleventNpcInfo.get();
	}

	public static void _reset_unsafe_add_(eventNpcInfo bean) {
		pooleventNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(eventNpcInfo bean) {
		mkdb.Procedure.padd(bean, pooleventNpcInfo);
	}

	public static eventNpcInfo neweventNpcInfoData() {
		return new xbean.__.eventNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<eventNpcInfoCol> pooleventNpcInfoCol = new mkdb.util.BeanPool<eventNpcInfoCol>() {
		@Override
		protected eventNpcInfoCol newBean() {
			return new xbean.__.eventNpcInfoCol();
		}
	};

	public static eventNpcInfoCol neweventNpcInfoCol() {
		return pooleventNpcInfoCol.get();
	}

	public static void _reset_unsafe_add_(eventNpcInfoCol bean) {
		pooleventNpcInfoCol._reset_unsafe_add_(bean);
	}

	public static void padd(eventNpcInfoCol bean) {
		mkdb.Procedure.padd(bean, pooleventNpcInfoCol);
	}

	public static eventNpcInfoCol neweventNpcInfoColData() {
		return new xbean.__.eventNpcInfoCol.Data();
	}

	public static mkdb.util.BeanPool<RoleTimerNpcInfo> poolRoleTimerNpcInfo = new mkdb.util.BeanPool<RoleTimerNpcInfo>() {
		@Override
		protected RoleTimerNpcInfo newBean() {
			return new xbean.__.RoleTimerNpcInfo();
		}
	};

	public static RoleTimerNpcInfo newRoleTimerNpcInfo() {
		return poolRoleTimerNpcInfo.get();
	}

	public static void _reset_unsafe_add_(RoleTimerNpcInfo bean) {
		poolRoleTimerNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleTimerNpcInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleTimerNpcInfo);
	}

	public static RoleTimerNpcInfo newRoleTimerNpcInfoData() {
		return new xbean.__.RoleTimerNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleActiveTimerNpcInfo> poolRoleActiveTimerNpcInfo = new mkdb.util.BeanPool<RoleActiveTimerNpcInfo>() {
		@Override
		protected RoleActiveTimerNpcInfo newBean() {
			return new xbean.__.RoleActiveTimerNpcInfo();
		}
	};

	public static RoleActiveTimerNpcInfo newRoleActiveTimerNpcInfo() {
		return poolRoleActiveTimerNpcInfo.get();
	}

	public static void _reset_unsafe_add_(RoleActiveTimerNpcInfo bean) {
		poolRoleActiveTimerNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleActiveTimerNpcInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleActiveTimerNpcInfo);
	}

	public static RoleActiveTimerNpcInfo newRoleActiveTimerNpcInfoData() {
		return new xbean.__.RoleActiveTimerNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleEventNpcInfo> poolRoleEventNpcInfo = new mkdb.util.BeanPool<RoleEventNpcInfo>() {
		@Override
		protected RoleEventNpcInfo newBean() {
			return new xbean.__.RoleEventNpcInfo();
		}
	};

	public static RoleEventNpcInfo newRoleEventNpcInfo() {
		return poolRoleEventNpcInfo.get();
	}

	public static void _reset_unsafe_add_(RoleEventNpcInfo bean) {
		poolRoleEventNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleEventNpcInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleEventNpcInfo);
	}

	public static RoleEventNpcInfo newRoleEventNpcInfoData() {
		return new xbean.__.RoleEventNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleEventNpcInfoColumn> poolRoleEventNpcInfoColumn = new mkdb.util.BeanPool<RoleEventNpcInfoColumn>() {
		@Override
		protected RoleEventNpcInfoColumn newBean() {
			return new xbean.__.RoleEventNpcInfoColumn();
		}
	};

	public static RoleEventNpcInfoColumn newRoleEventNpcInfoColumn() {
		return poolRoleEventNpcInfoColumn.get();
	}

	public static void _reset_unsafe_add_(RoleEventNpcInfoColumn bean) {
		poolRoleEventNpcInfoColumn._reset_unsafe_add_(bean);
	}

	public static void padd(RoleEventNpcInfoColumn bean) {
		mkdb.Procedure.padd(bean, poolRoleEventNpcInfoColumn);
	}

	public static RoleEventNpcInfoColumn newRoleEventNpcInfoColumnData() {
		return new xbean.__.RoleEventNpcInfoColumn.Data();
	}

	public static mkdb.util.BeanPool<npcBattleInfoCol> poolnpcBattleInfoCol = new mkdb.util.BeanPool<npcBattleInfoCol>() {
		@Override
		protected npcBattleInfoCol newBean() {
			return new xbean.__.npcBattleInfoCol();
		}
	};

	public static npcBattleInfoCol newnpcBattleInfoCol() {
		return poolnpcBattleInfoCol.get();
	}

	public static void _reset_unsafe_add_(npcBattleInfoCol bean) {
		poolnpcBattleInfoCol._reset_unsafe_add_(bean);
	}

	public static void padd(npcBattleInfoCol bean) {
		mkdb.Procedure.padd(bean, poolnpcBattleInfoCol);
	}

	public static npcBattleInfoCol newnpcBattleInfoColData() {
		return new xbean.__.npcBattleInfoCol.Data();
	}

	public static mkdb.util.BeanPool<WeiBoChoice> poolWeiBoChoice = new mkdb.util.BeanPool<WeiBoChoice>() {
		@Override
		protected WeiBoChoice newBean() {
			return new xbean.__.WeiBoChoice();
		}
	};

	public static WeiBoChoice newWeiBoChoice() {
		return poolWeiBoChoice.get();
	}

	public static void _reset_unsafe_add_(WeiBoChoice bean) {
		poolWeiBoChoice._reset_unsafe_add_(bean);
	}

	public static void padd(WeiBoChoice bean) {
		mkdb.Procedure.padd(bean, poolWeiBoChoice);
	}

	public static WeiBoChoice newWeiBoChoiceData() {
		return new xbean.__.WeiBoChoice.Data();
	}

	public static mkdb.util.BeanPool<WeiBoNotify> poolWeiBoNotify = new mkdb.util.BeanPool<WeiBoNotify>() {
		@Override
		protected WeiBoNotify newBean() {
			return new xbean.__.WeiBoNotify();
		}
	};

	public static WeiBoNotify newWeiBoNotify() {
		return poolWeiBoNotify.get();
	}

	public static void _reset_unsafe_add_(WeiBoNotify bean) {
		poolWeiBoNotify._reset_unsafe_add_(bean);
	}

	public static void padd(WeiBoNotify bean) {
		mkdb.Procedure.padd(bean, poolWeiBoNotify);
	}

	public static WeiBoNotify newWeiBoNotifyData() {
		return new xbean.__.WeiBoNotify.Data();
	}

	public static mkdb.util.BeanPool<RolenpcjiangliInfo> poolRolenpcjiangliInfo = new mkdb.util.BeanPool<RolenpcjiangliInfo>() {
		@Override
		protected RolenpcjiangliInfo newBean() {
			return new xbean.__.RolenpcjiangliInfo();
		}
	};

	public static RolenpcjiangliInfo newRolenpcjiangliInfo() {
		return poolRolenpcjiangliInfo.get();
	}

	public static void _reset_unsafe_add_(RolenpcjiangliInfo bean) {
		poolRolenpcjiangliInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RolenpcjiangliInfo bean) {
		mkdb.Procedure.padd(bean, poolRolenpcjiangliInfo);
	}

	public static RolenpcjiangliInfo newRolenpcjiangliInfoData() {
		return new xbean.__.RolenpcjiangliInfo.Data();
	}

	public static mkdb.util.BeanPool<RolePingBiRolesInfo> poolRolePingBiRolesInfo = new mkdb.util.BeanPool<RolePingBiRolesInfo>() {
		@Override
		protected RolePingBiRolesInfo newBean() {
			return new xbean.__.RolePingBiRolesInfo();
		}
	};

	public static RolePingBiRolesInfo newRolePingBiRolesInfo() {
		return poolRolePingBiRolesInfo.get();
	}

	public static void _reset_unsafe_add_(RolePingBiRolesInfo bean) {
		poolRolePingBiRolesInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RolePingBiRolesInfo bean) {
		mkdb.Procedure.padd(bean, poolRolePingBiRolesInfo);
	}

	public static RolePingBiRolesInfo newRolePingBiRolesInfoData() {
		return new xbean.__.RolePingBiRolesInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleRankNotifyTimeInfo> poolRoleRankNotifyTimeInfo = new mkdb.util.BeanPool<RoleRankNotifyTimeInfo>() {
		@Override
		protected RoleRankNotifyTimeInfo newBean() {
			return new xbean.__.RoleRankNotifyTimeInfo();
		}
	};

	public static RoleRankNotifyTimeInfo newRoleRankNotifyTimeInfo() {
		return poolRoleRankNotifyTimeInfo.get();
	}

	public static void _reset_unsafe_add_(RoleRankNotifyTimeInfo bean) {
		poolRoleRankNotifyTimeInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleRankNotifyTimeInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleRankNotifyTimeInfo);
	}

	public static RoleRankNotifyTimeInfo newRoleRankNotifyTimeInfoData() {
		return new xbean.__.RoleRankNotifyTimeInfo.Data();
	}

	public static mkdb.util.BeanPool<FuShiRecord> poolFuShiRecord = new mkdb.util.BeanPool<FuShiRecord>() {
		@Override
		protected FuShiRecord newBean() {
			return new xbean.__.FuShiRecord();
		}
	};

	public static FuShiRecord newFuShiRecord() {
		return poolFuShiRecord.get();
	}

	public static void _reset_unsafe_add_(FuShiRecord bean) {
		poolFuShiRecord._reset_unsafe_add_(bean);
	}

	public static void padd(FuShiRecord bean) {
		mkdb.Procedure.padd(bean, poolFuShiRecord);
	}

	public static FuShiRecord newFuShiRecordData() {
		return new xbean.__.FuShiRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleFuShiRecord> poolRoleFuShiRecord = new mkdb.util.BeanPool<RoleFuShiRecord>() {
		@Override
		protected RoleFuShiRecord newBean() {
			return new xbean.__.RoleFuShiRecord();
		}
	};

	public static RoleFuShiRecord newRoleFuShiRecord() {
		return poolRoleFuShiRecord.get();
	}

	public static void _reset_unsafe_add_(RoleFuShiRecord bean) {
		poolRoleFuShiRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleFuShiRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleFuShiRecord);
	}

	public static RoleFuShiRecord newRoleFuShiRecordData() {
		return new xbean.__.RoleFuShiRecord.Data();
	}

	public static mkdb.util.BeanPool<RegDay> poolRegDay = new mkdb.util.BeanPool<RegDay>() {
		@Override
		protected RegDay newBean() {
			return new xbean.__.RegDay();
		}
	};

	public static RegDay newRegDay() {
		return poolRegDay.get();
	}

	public static void _reset_unsafe_add_(RegDay bean) {
		poolRegDay._reset_unsafe_add_(bean);
	}

	public static void padd(RegDay bean) {
		mkdb.Procedure.padd(bean, poolRegDay);
	}

	public static RegDay newRegDayData() {
		return new xbean.__.RegDay.Data();
	}

	public static mkdb.util.BeanPool<RegMonth> poolRegMonth = new mkdb.util.BeanPool<RegMonth>() {
		@Override
		protected RegMonth newBean() {
			return new xbean.__.RegMonth();
		}
	};

	public static RegMonth newRegMonth() {
		return poolRegMonth.get();
	}

	public static void _reset_unsafe_add_(RegMonth bean) {
		poolRegMonth._reset_unsafe_add_(bean);
	}

	public static void padd(RegMonth bean) {
		mkdb.Procedure.padd(bean, poolRegMonth);
	}

	public static RegMonth newRegMonthData() {
		return new xbean.__.RegMonth.Data();
	}

	public static mkdb.util.BeanPool<RegRec> poolRegRec = new mkdb.util.BeanPool<RegRec>() {
		@Override
		protected RegRec newBean() {
			return new xbean.__.RegRec();
		}
	};

	public static RegRec newRegRec() {
		return poolRegRec.get();
	}

	public static void _reset_unsafe_add_(RegRec bean) {
		poolRegRec._reset_unsafe_add_(bean);
	}

	public static void padd(RegRec bean) {
		mkdb.Procedure.padd(bean, poolRegRec);
	}

	public static RegRec newRegRecData() {
		return new xbean.__.RegRec.Data();
	}

	public static mkdb.util.BeanPool<RoleDoubleChareInfo> poolRoleDoubleChareInfo = new mkdb.util.BeanPool<RoleDoubleChareInfo>() {
		@Override
		protected RoleDoubleChareInfo newBean() {
			return new xbean.__.RoleDoubleChareInfo();
		}
	};

	public static RoleDoubleChareInfo newRoleDoubleChareInfo() {
		return poolRoleDoubleChareInfo.get();
	}

	public static void _reset_unsafe_add_(RoleDoubleChareInfo bean) {
		poolRoleDoubleChareInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleDoubleChareInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleDoubleChareInfo);
	}

	public static RoleDoubleChareInfo newRoleDoubleChareInfoData() {
		return new xbean.__.RoleDoubleChareInfo.Data();
	}

	public static mkdb.util.BeanPool<ActivityQuestion> poolActivityQuestion = new mkdb.util.BeanPool<ActivityQuestion>() {
		@Override
		protected ActivityQuestion newBean() {
			return new xbean.__.ActivityQuestion();
		}
	};

	public static ActivityQuestion newActivityQuestion() {
		return poolActivityQuestion.get();
	}

	public static void _reset_unsafe_add_(ActivityQuestion bean) {
		poolActivityQuestion._reset_unsafe_add_(bean);
	}

	public static void padd(ActivityQuestion bean) {
		mkdb.Procedure.padd(bean, poolActivityQuestion);
	}

	public static ActivityQuestion newActivityQuestionData() {
		return new xbean.__.ActivityQuestion.Data();
	}

	public static mkdb.util.BeanPool<ChargeReturnProfit> poolChargeReturnProfit = new mkdb.util.BeanPool<ChargeReturnProfit>() {
		@Override
		protected ChargeReturnProfit newBean() {
			return new xbean.__.ChargeReturnProfit();
		}
	};

	public static ChargeReturnProfit newChargeReturnProfit() {
		return poolChargeReturnProfit.get();
	}

	public static void _reset_unsafe_add_(ChargeReturnProfit bean) {
		poolChargeReturnProfit._reset_unsafe_add_(bean);
	}

	public static void padd(ChargeReturnProfit bean) {
		mkdb.Procedure.padd(bean, poolChargeReturnProfit);
	}

	public static ChargeReturnProfit newChargeReturnProfitData() {
		return new xbean.__.ChargeReturnProfit.Data();
	}

	public static mkdb.util.BeanPool<EChargeReturnProfit> poolEChargeReturnProfit = new mkdb.util.BeanPool<EChargeReturnProfit>() {
		@Override
		protected EChargeReturnProfit newBean() {
			return new xbean.__.EChargeReturnProfit();
		}
	};

	public static EChargeReturnProfit newEChargeReturnProfit() {
		return poolEChargeReturnProfit.get();
	}

	public static void _reset_unsafe_add_(EChargeReturnProfit bean) {
		poolEChargeReturnProfit._reset_unsafe_add_(bean);
	}

	public static void padd(EChargeReturnProfit bean) {
		mkdb.Procedure.padd(bean, poolEChargeReturnProfit);
	}

	public static EChargeReturnProfit newEChargeReturnProfitData() {
		return new xbean.__.EChargeReturnProfit.Data();
	}

	public static mkdb.util.BeanPool<npcaward> poolnpcaward = new mkdb.util.BeanPool<npcaward>() {
		@Override
		protected npcaward newBean() {
			return new xbean.__.npcaward();
		}
	};

	public static npcaward newnpcaward() {
		return poolnpcaward.get();
	}

	public static void _reset_unsafe_add_(npcaward bean) {
		poolnpcaward._reset_unsafe_add_(bean);
	}

	public static void padd(npcaward bean) {
		mkdb.Procedure.padd(bean, poolnpcaward);
	}

	public static npcaward newnpcawardData() {
		return new xbean.__.npcaward.Data();
	}

	public static mkdb.util.BeanPool<npcawardrecords> poolnpcawardrecords = new mkdb.util.BeanPool<npcawardrecords>() {
		@Override
		protected npcawardrecords newBean() {
			return new xbean.__.npcawardrecords();
		}
	};

	public static npcawardrecords newnpcawardrecords() {
		return poolnpcawardrecords.get();
	}

	public static void _reset_unsafe_add_(npcawardrecords bean) {
		poolnpcawardrecords._reset_unsafe_add_(bean);
	}

	public static void padd(npcawardrecords bean) {
		mkdb.Procedure.padd(bean, poolnpcawardrecords);
	}

	public static npcawardrecords newnpcawardrecordsData() {
		return new xbean.__.npcawardrecords.Data();
	}

	public static mkdb.util.BeanPool<HuoBanInfo> poolHuoBanInfo = new mkdb.util.BeanPool<HuoBanInfo>() {
		@Override
		protected HuoBanInfo newBean() {
			return new xbean.__.HuoBanInfo();
		}
	};

	public static HuoBanInfo newHuoBanInfo() {
		return poolHuoBanInfo.get();
	}

	public static void _reset_unsafe_add_(HuoBanInfo bean) {
		poolHuoBanInfo._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanInfo bean) {
		mkdb.Procedure.padd(bean, poolHuoBanInfo);
	}

	public static HuoBanInfo newHuoBanInfoData() {
		return new xbean.__.HuoBanInfo.Data();
	}

	public static mkdb.util.BeanPool<HuoBanColumn> poolHuoBanColumn = new mkdb.util.BeanPool<HuoBanColumn>() {
		@Override
		protected HuoBanColumn newBean() {
			return new xbean.__.HuoBanColumn();
		}
	};

	public static HuoBanColumn newHuoBanColumn() {
		return poolHuoBanColumn.get();
	}

	public static void _reset_unsafe_add_(HuoBanColumn bean) {
		poolHuoBanColumn._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanColumn bean) {
		mkdb.Procedure.padd(bean, poolHuoBanColumn);
	}

	public static HuoBanColumn newHuoBanColumnData() {
		return new xbean.__.HuoBanColumn.Data();
	}

	public static mkdb.util.BeanPool<HuoBanZhenrongInfo> poolHuoBanZhenrongInfo = new mkdb.util.BeanPool<HuoBanZhenrongInfo>() {
		@Override
		protected HuoBanZhenrongInfo newBean() {
			return new xbean.__.HuoBanZhenrongInfo();
		}
	};

	public static HuoBanZhenrongInfo newHuoBanZhenrongInfo() {
		return poolHuoBanZhenrongInfo.get();
	}

	public static void _reset_unsafe_add_(HuoBanZhenrongInfo bean) {
		poolHuoBanZhenrongInfo._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanZhenrongInfo bean) {
		mkdb.Procedure.padd(bean, poolHuoBanZhenrongInfo);
	}

	public static HuoBanZhenrongInfo newHuoBanZhenrongInfoData() {
		return new xbean.__.HuoBanZhenrongInfo.Data();
	}

	public static mkdb.util.BeanPool<HuoBanZhenrong> poolHuoBanZhenrong = new mkdb.util.BeanPool<HuoBanZhenrong>() {
		@Override
		protected HuoBanZhenrong newBean() {
			return new xbean.__.HuoBanZhenrong();
		}
	};

	public static HuoBanZhenrong newHuoBanZhenrong() {
		return poolHuoBanZhenrong.get();
	}

	public static void _reset_unsafe_add_(HuoBanZhenrong bean) {
		poolHuoBanZhenrong._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanZhenrong bean) {
		mkdb.Procedure.padd(bean, poolHuoBanZhenrong);
	}

	public static HuoBanZhenrong newHuoBanZhenrongData() {
		return new xbean.__.HuoBanZhenrong.Data();
	}

	public static mkdb.util.BeanPool<HuoBanshuxing> poolHuoBanshuxing = new mkdb.util.BeanPool<HuoBanshuxing>() {
		@Override
		protected HuoBanshuxing newBean() {
			return new xbean.__.HuoBanshuxing();
		}
	};

	public static HuoBanshuxing newHuoBanshuxing() {
		return poolHuoBanshuxing.get();
	}

	public static void _reset_unsafe_add_(HuoBanshuxing bean) {
		poolHuoBanshuxing._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanshuxing bean) {
		mkdb.Procedure.padd(bean, poolHuoBanshuxing);
	}

	public static HuoBanshuxing newHuoBanshuxingData() {
		return new xbean.__.HuoBanshuxing.Data();
	}

	public static mkdb.util.BeanPool<HuoBanShuxingLevel> poolHuoBanShuxingLevel = new mkdb.util.BeanPool<HuoBanShuxingLevel>() {
		@Override
		protected HuoBanShuxingLevel newBean() {
			return new xbean.__.HuoBanShuxingLevel();
		}
	};

	public static HuoBanShuxingLevel newHuoBanShuxingLevel() {
		return poolHuoBanShuxingLevel.get();
	}

	public static void _reset_unsafe_add_(HuoBanShuxingLevel bean) {
		poolHuoBanShuxingLevel._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanShuxingLevel bean) {
		mkdb.Procedure.padd(bean, poolHuoBanShuxingLevel);
	}

	public static HuoBanShuxingLevel newHuoBanShuxingLevelData() {
		return new xbean.__.HuoBanShuxingLevel.Data();
	}

	public static mkdb.util.BeanPool<HuoBanVip> poolHuoBanVip = new mkdb.util.BeanPool<HuoBanVip>() {
		@Override
		protected HuoBanVip newBean() {
			return new xbean.__.HuoBanVip();
		}
	};

	public static HuoBanVip newHuoBanVip() {
		return poolHuoBanVip.get();
	}

	public static void _reset_unsafe_add_(HuoBanVip bean) {
		poolHuoBanVip._reset_unsafe_add_(bean);
	}

	public static void padd(HuoBanVip bean) {
		mkdb.Procedure.padd(bean, poolHuoBanVip);
	}

	public static HuoBanVip newHuoBanVipData() {
		return new xbean.__.HuoBanVip.Data();
	}

	public static mkdb.util.BeanPool<NumberAndTime> poolNumberAndTime = new mkdb.util.BeanPool<NumberAndTime>() {
		@Override
		protected NumberAndTime newBean() {
			return new xbean.__.NumberAndTime();
		}
	};

	public static NumberAndTime newNumberAndTime() {
		return poolNumberAndTime.get();
	}

	public static void _reset_unsafe_add_(NumberAndTime bean) {
		poolNumberAndTime._reset_unsafe_add_(bean);
	}

	public static void padd(NumberAndTime bean) {
		mkdb.Procedure.padd(bean, poolNumberAndTime);
	}

	public static NumberAndTime newNumberAndTimeData() {
		return new xbean.__.NumberAndTime.Data();
	}

	public static mkdb.util.BeanPool<RoleBuyGoodsLimit> poolRoleBuyGoodsLimit = new mkdb.util.BeanPool<RoleBuyGoodsLimit>() {
		@Override
		protected RoleBuyGoodsLimit newBean() {
			return new xbean.__.RoleBuyGoodsLimit();
		}
	};

	public static RoleBuyGoodsLimit newRoleBuyGoodsLimit() {
		return poolRoleBuyGoodsLimit.get();
	}

	public static void _reset_unsafe_add_(RoleBuyGoodsLimit bean) {
		poolRoleBuyGoodsLimit._reset_unsafe_add_(bean);
	}

	public static void padd(RoleBuyGoodsLimit bean) {
		mkdb.Procedure.padd(bean, poolRoleBuyGoodsLimit);
	}

	public static RoleBuyGoodsLimit newRoleBuyGoodsLimitData() {
		return new xbean.__.RoleBuyGoodsLimit.Data();
	}

	public static mkdb.util.BeanPool<RoleSaleGoodsLimit> poolRoleSaleGoodsLimit = new mkdb.util.BeanPool<RoleSaleGoodsLimit>() {
		@Override
		protected RoleSaleGoodsLimit newBean() {
			return new xbean.__.RoleSaleGoodsLimit();
		}
	};

	public static RoleSaleGoodsLimit newRoleSaleGoodsLimit() {
		return poolRoleSaleGoodsLimit.get();
	}

	public static void _reset_unsafe_add_(RoleSaleGoodsLimit bean) {
		poolRoleSaleGoodsLimit._reset_unsafe_add_(bean);
	}

	public static void padd(RoleSaleGoodsLimit bean) {
		mkdb.Procedure.padd(bean, poolRoleSaleGoodsLimit);
	}

	public static RoleSaleGoodsLimit newRoleSaleGoodsLimitData() {
		return new xbean.__.RoleSaleGoodsLimit.Data();
	}

	public static mkdb.util.BeanPool<GoodsBusiness> poolGoodsBusiness = new mkdb.util.BeanPool<GoodsBusiness>() {
		@Override
		protected GoodsBusiness newBean() {
			return new xbean.__.GoodsBusiness();
		}
	};

	public static GoodsBusiness newGoodsBusiness() {
		return poolGoodsBusiness.get();
	}

	public static void _reset_unsafe_add_(GoodsBusiness bean) {
		poolGoodsBusiness._reset_unsafe_add_(bean);
	}

	public static void padd(GoodsBusiness bean) {
		mkdb.Procedure.padd(bean, poolGoodsBusiness);
	}

	public static GoodsBusiness newGoodsBusinessData() {
		return new xbean.__.GoodsBusiness.Data();
	}

	public static mkdb.util.BeanPool<NpcSaleBusiness> poolNpcSaleBusiness = new mkdb.util.BeanPool<NpcSaleBusiness>() {
		@Override
		protected NpcSaleBusiness newBean() {
			return new xbean.__.NpcSaleBusiness();
		}
	};

	public static NpcSaleBusiness newNpcSaleBusiness() {
		return poolNpcSaleBusiness.get();
	}

	public static void _reset_unsafe_add_(NpcSaleBusiness bean) {
		poolNpcSaleBusiness._reset_unsafe_add_(bean);
	}

	public static void padd(NpcSaleBusiness bean) {
		mkdb.Procedure.padd(bean, poolNpcSaleBusiness);
	}

	public static NpcSaleBusiness newNpcSaleBusinessData() {
		return new xbean.__.NpcSaleBusiness.Data();
	}

	public static mkdb.util.BeanPool<LogBean> poolLogBean = new mkdb.util.BeanPool<LogBean>() {
		@Override
		protected LogBean newBean() {
			return new xbean.__.LogBean();
		}
	};

	public static LogBean newLogBean() {
		return poolLogBean.get();
	}

	public static void _reset_unsafe_add_(LogBean bean) {
		poolLogBean._reset_unsafe_add_(bean);
	}

	public static void padd(LogBean bean) {
		mkdb.Procedure.padd(bean, poolLogBean);
	}

	public static LogBean newLogBeanData() {
		return new xbean.__.LogBean.Data();
	}

	public static mkdb.util.BeanPool<MarketTradeLog> poolMarketTradeLog = new mkdb.util.BeanPool<MarketTradeLog>() {
		@Override
		protected MarketTradeLog newBean() {
			return new xbean.__.MarketTradeLog();
		}
	};

	public static MarketTradeLog newMarketTradeLog() {
		return poolMarketTradeLog.get();
	}

	public static void _reset_unsafe_add_(MarketTradeLog bean) {
		poolMarketTradeLog._reset_unsafe_add_(bean);
	}

	public static void padd(MarketTradeLog bean) {
		mkdb.Procedure.padd(bean, poolMarketTradeLog);
	}

	public static MarketTradeLog newMarketTradeLogData() {
		return new xbean.__.MarketTradeLog.Data();
	}

	public static mkdb.util.BeanPool<MarketFloatingGoods> poolMarketFloatingGoods = new mkdb.util.BeanPool<MarketFloatingGoods>() {
		@Override
		protected MarketFloatingGoods newBean() {
			return new xbean.__.MarketFloatingGoods();
		}
	};

	public static MarketFloatingGoods newMarketFloatingGoods() {
		return poolMarketFloatingGoods.get();
	}

	public static void _reset_unsafe_add_(MarketFloatingGoods bean) {
		poolMarketFloatingGoods._reset_unsafe_add_(bean);
	}

	public static void padd(MarketFloatingGoods bean) {
		mkdb.Procedure.padd(bean, poolMarketFloatingGoods);
	}

	public static MarketFloatingGoods newMarketFloatingGoodsData() {
		return new xbean.__.MarketFloatingGoods.Data();
	}

	public static mkdb.util.BeanPool<AttentionGoodsBean> poolAttentionGoodsBean = new mkdb.util.BeanPool<AttentionGoodsBean>() {
		@Override
		protected AttentionGoodsBean newBean() {
			return new xbean.__.AttentionGoodsBean();
		}
	};

	public static AttentionGoodsBean newAttentionGoodsBean() {
		return poolAttentionGoodsBean.get();
	}

	public static void _reset_unsafe_add_(AttentionGoodsBean bean) {
		poolAttentionGoodsBean._reset_unsafe_add_(bean);
	}

	public static void padd(AttentionGoodsBean bean) {
		mkdb.Procedure.padd(bean, poolAttentionGoodsBean);
	}

	public static AttentionGoodsBean newAttentionGoodsBeanData() {
		return new xbean.__.AttentionGoodsBean.Data();
	}

	public static mkdb.util.BeanPool<AttentionGoods> poolAttentionGoods = new mkdb.util.BeanPool<AttentionGoods>() {
		@Override
		protected AttentionGoods newBean() {
			return new xbean.__.AttentionGoods();
		}
	};

	public static AttentionGoods newAttentionGoods() {
		return poolAttentionGoods.get();
	}

	public static void _reset_unsafe_add_(AttentionGoods bean) {
		poolAttentionGoods._reset_unsafe_add_(bean);
	}

	public static void padd(AttentionGoods bean) {
		mkdb.Procedure.padd(bean, poolAttentionGoods);
	}

	public static AttentionGoods newAttentionGoodsData() {
		return new xbean.__.AttentionGoods.Data();
	}

	public static mkdb.util.BeanPool<AttentionRole> poolAttentionRole = new mkdb.util.BeanPool<AttentionRole>() {
		@Override
		protected AttentionRole newBean() {
			return new xbean.__.AttentionRole();
		}
	};

	public static AttentionRole newAttentionRole() {
		return poolAttentionRole.get();
	}

	public static void _reset_unsafe_add_(AttentionRole bean) {
		poolAttentionRole._reset_unsafe_add_(bean);
	}

	public static void padd(AttentionRole bean) {
		mkdb.Procedure.padd(bean, poolAttentionRole);
	}

	public static AttentionRole newAttentionRoleData() {
		return new xbean.__.AttentionRole.Data();
	}

	public static mkdb.util.BeanPool<NormalItem> poolNormalItem = new mkdb.util.BeanPool<NormalItem>() {
		@Override
		protected NormalItem newBean() {
			return new xbean.__.NormalItem();
		}
	};

	public static NormalItem newNormalItem() {
		return poolNormalItem.get();
	}

	public static void _reset_unsafe_add_(NormalItem bean) {
		poolNormalItem._reset_unsafe_add_(bean);
	}

	public static void padd(NormalItem bean) {
		mkdb.Procedure.padd(bean, poolNormalItem);
	}

	public static NormalItem newNormalItemData() {
		return new xbean.__.NormalItem.Data();
	}

	public static mkdb.util.BeanPool<EquipItem> poolEquipItem = new mkdb.util.BeanPool<EquipItem>() {
		@Override
		protected EquipItem newBean() {
			return new xbean.__.EquipItem();
		}
	};

	public static EquipItem newEquipItem() {
		return poolEquipItem.get();
	}

	public static void _reset_unsafe_add_(EquipItem bean) {
		poolEquipItem._reset_unsafe_add_(bean);
	}

	public static void padd(EquipItem bean) {
		mkdb.Procedure.padd(bean, poolEquipItem);
	}

	public static EquipItem newEquipItemData() {
		return new xbean.__.EquipItem.Data();
	}

	public static mkdb.util.BeanPool<PetItem> poolPetItem = new mkdb.util.BeanPool<PetItem>() {
		@Override
		protected PetItem newBean() {
			return new xbean.__.PetItem();
		}
	};

	public static PetItem newPetItem() {
		return poolPetItem.get();
	}

	public static void _reset_unsafe_add_(PetItem bean) {
		poolPetItem._reset_unsafe_add_(bean);
	}

	public static void padd(PetItem bean) {
		mkdb.Procedure.padd(bean, poolPetItem);
	}

	public static PetItem newPetItemData() {
		return new xbean.__.PetItem.Data();
	}

	public static mkdb.util.BeanPool<PresellItem> poolPresellItem = new mkdb.util.BeanPool<PresellItem>() {
		@Override
		protected PresellItem newBean() {
			return new xbean.__.PresellItem();
		}
	};

	public static PresellItem newPresellItem() {
		return poolPresellItem.get();
	}

	public static void _reset_unsafe_add_(PresellItem bean) {
		poolPresellItem._reset_unsafe_add_(bean);
	}

	public static void padd(PresellItem bean) {
		mkdb.Procedure.padd(bean, poolPresellItem);
	}

	public static PresellItem newPresellItemData() {
		return new xbean.__.PresellItem.Data();
	}

	public static mkdb.util.BeanPool<GoldOrder> poolGoldOrder = new mkdb.util.BeanPool<GoldOrder>() {
		@Override
		protected GoldOrder newBean() {
			return new xbean.__.GoldOrder();
		}
	};

	public static GoldOrder newGoldOrder() {
		return poolGoldOrder.get();
	}

	public static void _reset_unsafe_add_(GoldOrder bean) {
		poolGoldOrder._reset_unsafe_add_(bean);
	}

	public static void padd(GoldOrder bean) {
		mkdb.Procedure.padd(bean, poolGoldOrder);
	}

	public static GoldOrder newGoldOrderData() {
		return new xbean.__.GoldOrder.Data();
	}

	public static mkdb.util.BeanPool<RoleBlackMarket> poolRoleBlackMarket = new mkdb.util.BeanPool<RoleBlackMarket>() {
		@Override
		protected RoleBlackMarket newBean() {
			return new xbean.__.RoleBlackMarket();
		}
	};

	public static RoleBlackMarket newRoleBlackMarket() {
		return poolRoleBlackMarket.get();
	}

	public static void _reset_unsafe_add_(RoleBlackMarket bean) {
		poolRoleBlackMarket._reset_unsafe_add_(bean);
	}

	public static void padd(RoleBlackMarket bean) {
		mkdb.Procedure.padd(bean, poolRoleBlackMarket);
	}

	public static RoleBlackMarket newRoleBlackMarketData() {
		return new xbean.__.RoleBlackMarket.Data();
	}

	public static mkdb.util.BeanPool<HookData> poolHookData = new mkdb.util.BeanPool<HookData>() {
		@Override
		protected HookData newBean() {
			return new xbean.__.HookData();
		}
	};

	public static HookData newHookData() {
		return poolHookData.get();
	}

	public static void _reset_unsafe_add_(HookData bean) {
		poolHookData._reset_unsafe_add_(bean);
	}

	public static void padd(HookData bean) {
		mkdb.Procedure.padd(bean, poolHookData);
	}

	public static HookData newHookDataData() {
		return new xbean.__.HookData.Data();
	}

	public static mkdb.util.BeanPool<ItemUseCount> poolItemUseCount = new mkdb.util.BeanPool<ItemUseCount>() {
		@Override
		protected ItemUseCount newBean() {
			return new xbean.__.ItemUseCount();
		}
	};

	public static ItemUseCount newItemUseCount() {
		return poolItemUseCount.get();
	}

	public static void _reset_unsafe_add_(ItemUseCount bean) {
		poolItemUseCount._reset_unsafe_add_(bean);
	}

	public static void padd(ItemUseCount bean) {
		mkdb.Procedure.padd(bean, poolItemUseCount);
	}

	public static ItemUseCount newItemUseCountData() {
		return new xbean.__.ItemUseCount.Data();
	}

	public static mkdb.util.BeanPool<ItemUse> poolItemUse = new mkdb.util.BeanPool<ItemUse>() {
		@Override
		protected ItemUse newBean() {
			return new xbean.__.ItemUse();
		}
	};

	public static ItemUse newItemUse() {
		return poolItemUse.get();
	}

	public static void _reset_unsafe_add_(ItemUse bean) {
		poolItemUse._reset_unsafe_add_(bean);
	}

	public static void padd(ItemUse bean) {
		mkdb.Procedure.padd(bean, poolItemUse);
	}

	public static ItemUse newItemUseData() {
		return new xbean.__.ItemUse.Data();
	}

	public static mkdb.util.BeanPool<BaoXiangInfo> poolBaoXiangInfo = new mkdb.util.BeanPool<BaoXiangInfo>() {
		@Override
		protected BaoXiangInfo newBean() {
			return new xbean.__.BaoXiangInfo();
		}
	};

	public static BaoXiangInfo newBaoXiangInfo() {
		return poolBaoXiangInfo.get();
	}

	public static void _reset_unsafe_add_(BaoXiangInfo bean) {
		poolBaoXiangInfo._reset_unsafe_add_(bean);
	}

	public static void padd(BaoXiangInfo bean) {
		mkdb.Procedure.padd(bean, poolBaoXiangInfo);
	}

	public static BaoXiangInfo newBaoXiangInfoData() {
		return new xbean.__.BaoXiangInfo.Data();
	}

	public static mkdb.util.BeanPool<GongHuiBaoXiangInfo> poolGongHuiBaoXiangInfo = new mkdb.util.BeanPool<GongHuiBaoXiangInfo>() {
		@Override
		protected GongHuiBaoXiangInfo newBean() {
			return new xbean.__.GongHuiBaoXiangInfo();
		}
	};

	public static GongHuiBaoXiangInfo newGongHuiBaoXiangInfo() {
		return poolGongHuiBaoXiangInfo.get();
	}

	public static void _reset_unsafe_add_(GongHuiBaoXiangInfo bean) {
		poolGongHuiBaoXiangInfo._reset_unsafe_add_(bean);
	}

	public static void padd(GongHuiBaoXiangInfo bean) {
		mkdb.Procedure.padd(bean, poolGongHuiBaoXiangInfo);
	}

	public static GongHuiBaoXiangInfo newGongHuiBaoXiangInfoData() {
		return new xbean.__.GongHuiBaoXiangInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleBestowCount> poolRoleBestowCount = new mkdb.util.BeanPool<RoleBestowCount>() {
		@Override
		protected RoleBestowCount newBean() {
			return new xbean.__.RoleBestowCount();
		}
	};

	public static RoleBestowCount newRoleBestowCount() {
		return poolRoleBestowCount.get();
	}

	public static void _reset_unsafe_add_(RoleBestowCount bean) {
		poolRoleBestowCount._reset_unsafe_add_(bean);
	}

	public static void padd(RoleBestowCount bean) {
		mkdb.Procedure.padd(bean, poolRoleBestowCount);
	}

	public static RoleBestowCount newRoleBestowCountData() {
		return new xbean.__.RoleBestowCount.Data();
	}

	public static mkdb.util.BeanPool<RoleBestowInfo> poolRoleBestowInfo = new mkdb.util.BeanPool<RoleBestowInfo>() {
		@Override
		protected RoleBestowInfo newBean() {
			return new xbean.__.RoleBestowInfo();
		}
	};

	public static RoleBestowInfo newRoleBestowInfo() {
		return poolRoleBestowInfo.get();
	}

	public static void _reset_unsafe_add_(RoleBestowInfo bean) {
		poolRoleBestowInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleBestowInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleBestowInfo);
	}

	public static RoleBestowInfo newRoleBestowInfoData() {
		return new xbean.__.RoleBestowInfo.Data();
	}

	public static mkdb.util.BeanPool<BestowNpcInfo> poolBestowNpcInfo = new mkdb.util.BeanPool<BestowNpcInfo>() {
		@Override
		protected BestowNpcInfo newBean() {
			return new xbean.__.BestowNpcInfo();
		}
	};

	public static BestowNpcInfo newBestowNpcInfo() {
		return poolBestowNpcInfo.get();
	}

	public static void _reset_unsafe_add_(BestowNpcInfo bean) {
		poolBestowNpcInfo._reset_unsafe_add_(bean);
	}

	public static void padd(BestowNpcInfo bean) {
		mkdb.Procedure.padd(bean, poolBestowNpcInfo);
	}

	public static BestowNpcInfo newBestowNpcInfoData() {
		return new xbean.__.BestowNpcInfo.Data();
	}

	public static mkdb.util.BeanPool<ServiceInfo> poolServiceInfo = new mkdb.util.BeanPool<ServiceInfo>() {
		@Override
		protected ServiceInfo newBean() {
			return new xbean.__.ServiceInfo();
		}
	};

	public static ServiceInfo newServiceInfo() {
		return poolServiceInfo.get();
	}

	public static void _reset_unsafe_add_(ServiceInfo bean) {
		poolServiceInfo._reset_unsafe_add_(bean);
	}

	public static void padd(ServiceInfo bean) {
		mkdb.Procedure.padd(bean, poolServiceInfo);
	}

	public static ServiceInfo newServiceInfoData() {
		return new xbean.__.ServiceInfo.Data();
	}

	public static mkdb.util.BeanPool<WeekIndex> poolWeekIndex = new mkdb.util.BeanPool<WeekIndex>() {
		@Override
		protected WeekIndex newBean() {
			return new xbean.__.WeekIndex();
		}
	};

	public static WeekIndex newWeekIndex() {
		return poolWeekIndex.get();
	}

	public static void _reset_unsafe_add_(WeekIndex bean) {
		poolWeekIndex._reset_unsafe_add_(bean);
	}

	public static void padd(WeekIndex bean) {
		mkdb.Procedure.padd(bean, poolWeekIndex);
	}

	public static WeekIndex newWeekIndexData() {
		return new xbean.__.WeekIndex.Data();
	}

	public static mkdb.util.BeanPool<LiveDieRoleInfo> poolLiveDieRoleInfo = new mkdb.util.BeanPool<LiveDieRoleInfo>() {
		@Override
		protected LiveDieRoleInfo newBean() {
			return new xbean.__.LiveDieRoleInfo();
		}
	};

	public static LiveDieRoleInfo newLiveDieRoleInfo() {
		return poolLiveDieRoleInfo.get();
	}

	public static void _reset_unsafe_add_(LiveDieRoleInfo bean) {
		poolLiveDieRoleInfo._reset_unsafe_add_(bean);
	}

	public static void padd(LiveDieRoleInfo bean) {
		mkdb.Procedure.padd(bean, poolLiveDieRoleInfo);
	}

	public static LiveDieRoleInfo newLiveDieRoleInfoData() {
		return new xbean.__.LiveDieRoleInfo.Data();
	}

	public static mkdb.util.BeanPool<LDTeamRoleInfoDes> poolLDTeamRoleInfoDes = new mkdb.util.BeanPool<LDTeamRoleInfoDes>() {
		@Override
		protected LDTeamRoleInfoDes newBean() {
			return new xbean.__.LDTeamRoleInfoDes();
		}
	};

	public static LDTeamRoleInfoDes newLDTeamRoleInfoDes() {
		return poolLDTeamRoleInfoDes.get();
	}

	public static void _reset_unsafe_add_(LDTeamRoleInfoDes bean) {
		poolLDTeamRoleInfoDes._reset_unsafe_add_(bean);
	}

	public static void padd(LDTeamRoleInfoDes bean) {
		mkdb.Procedure.padd(bean, poolLDTeamRoleInfoDes);
	}

	public static LDTeamRoleInfoDes newLDTeamRoleInfoDesData() {
		return new xbean.__.LDTeamRoleInfoDes.Data();
	}

	public static mkdb.util.BeanPool<LDRoleInfoDes> poolLDRoleInfoDes = new mkdb.util.BeanPool<LDRoleInfoDes>() {
		@Override
		protected LDRoleInfoDes newBean() {
			return new xbean.__.LDRoleInfoDes();
		}
	};

	public static LDRoleInfoDes newLDRoleInfoDes() {
		return poolLDRoleInfoDes.get();
	}

	public static void _reset_unsafe_add_(LDRoleInfoDes bean) {
		poolLDRoleInfoDes._reset_unsafe_add_(bean);
	}

	public static void padd(LDRoleInfoDes bean) {
		mkdb.Procedure.padd(bean, poolLDRoleInfoDes);
	}

	public static LDRoleInfoDes newLDRoleInfoDesData() {
		return new xbean.__.LDRoleInfoDes.Data();
	}

	public static mkdb.util.BeanPool<LDVideoRoleInfoDes> poolLDVideoRoleInfoDes = new mkdb.util.BeanPool<LDVideoRoleInfoDes>() {
		@Override
		protected LDVideoRoleInfoDes newBean() {
			return new xbean.__.LDVideoRoleInfoDes();
		}
	};

	public static LDVideoRoleInfoDes newLDVideoRoleInfoDes() {
		return poolLDVideoRoleInfoDes.get();
	}

	public static void _reset_unsafe_add_(LDVideoRoleInfoDes bean) {
		poolLDVideoRoleInfoDes._reset_unsafe_add_(bean);
	}

	public static void padd(LDVideoRoleInfoDes bean) {
		mkdb.Procedure.padd(bean, poolLDVideoRoleInfoDes);
	}

	public static LDVideoRoleInfoDes newLDVideoRoleInfoDesData() {
		return new xbean.__.LDVideoRoleInfoDes.Data();
	}

	public static mkdb.util.BeanPool<LDVideoRoleInfoDesList> poolLDVideoRoleInfoDesList = new mkdb.util.BeanPool<LDVideoRoleInfoDesList>() {
		@Override
		protected LDVideoRoleInfoDesList newBean() {
			return new xbean.__.LDVideoRoleInfoDesList();
		}
	};

	public static LDVideoRoleInfoDesList newLDVideoRoleInfoDesList() {
		return poolLDVideoRoleInfoDesList.get();
	}

	public static void _reset_unsafe_add_(LDVideoRoleInfoDesList bean) {
		poolLDVideoRoleInfoDesList._reset_unsafe_add_(bean);
	}

	public static void padd(LDVideoRoleInfoDesList bean) {
		mkdb.Procedure.padd(bean, poolLDVideoRoleInfoDesList);
	}

	public static LDVideoRoleInfoDesList newLDVideoRoleInfoDesListData() {
		return new xbean.__.LDVideoRoleInfoDesList.Data();
	}

	public static mkdb.util.BeanPool<LDVideoHisList> poolLDVideoHisList = new mkdb.util.BeanPool<LDVideoHisList>() {
		@Override
		protected LDVideoHisList newBean() {
			return new xbean.__.LDVideoHisList();
		}
	};

	public static LDVideoHisList newLDVideoHisList() {
		return poolLDVideoHisList.get();
	}

	public static void _reset_unsafe_add_(LDVideoHisList bean) {
		poolLDVideoHisList._reset_unsafe_add_(bean);
	}

	public static void padd(LDVideoHisList bean) {
		mkdb.Procedure.padd(bean, poolLDVideoHisList);
	}

	public static LDVideoHisList newLDVideoHisListData() {
		return new xbean.__.LDVideoHisList.Data();
	}

	public static mkdb.util.BeanPool<LDVideoRoleRoseInfo> poolLDVideoRoleRoseInfo = new mkdb.util.BeanPool<LDVideoRoleRoseInfo>() {
		@Override
		protected LDVideoRoleRoseInfo newBean() {
			return new xbean.__.LDVideoRoleRoseInfo();
		}
	};

	public static LDVideoRoleRoseInfo newLDVideoRoleRoseInfo() {
		return poolLDVideoRoleRoseInfo.get();
	}

	public static void _reset_unsafe_add_(LDVideoRoleRoseInfo bean) {
		poolLDVideoRoleRoseInfo._reset_unsafe_add_(bean);
	}

	public static void padd(LDVideoRoleRoseInfo bean) {
		mkdb.Procedure.padd(bean, poolLDVideoRoleRoseInfo);
	}

	public static LDVideoRoleRoseInfo newLDVideoRoleRoseInfoData() {
		return new xbean.__.LDVideoRoleRoseInfo.Data();
	}

	public static mkdb.util.BeanPool<LDVideoRoleRoseInfoList> poolLDVideoRoleRoseInfoList = new mkdb.util.BeanPool<LDVideoRoleRoseInfoList>() {
		@Override
		protected LDVideoRoleRoseInfoList newBean() {
			return new xbean.__.LDVideoRoleRoseInfoList();
		}
	};

	public static LDVideoRoleRoseInfoList newLDVideoRoleRoseInfoList() {
		return poolLDVideoRoleRoseInfoList.get();
	}

	public static void _reset_unsafe_add_(LDVideoRoleRoseInfoList bean) {
		poolLDVideoRoleRoseInfoList._reset_unsafe_add_(bean);
	}

	public static void padd(LDVideoRoleRoseInfoList bean) {
		mkdb.Procedure.padd(bean, poolLDVideoRoleRoseInfoList);
	}

	public static LDVideoRoleRoseInfoList newLDVideoRoleRoseInfoListData() {
		return new xbean.__.LDVideoRoleRoseInfoList.Data();
	}

	public static mkdb.util.BeanPool<LiveDieWeekNum> poolLiveDieWeekNum = new mkdb.util.BeanPool<LiveDieWeekNum>() {
		@Override
		protected LiveDieWeekNum newBean() {
			return new xbean.__.LiveDieWeekNum();
		}
	};

	public static LiveDieWeekNum newLiveDieWeekNum() {
		return poolLiveDieWeekNum.get();
	}

	public static void _reset_unsafe_add_(LiveDieWeekNum bean) {
		poolLiveDieWeekNum._reset_unsafe_add_(bean);
	}

	public static void padd(LiveDieWeekNum bean) {
		mkdb.Procedure.padd(bean, poolLiveDieWeekNum);
	}

	public static LiveDieWeekNum newLiveDieWeekNumData() {
		return new xbean.__.LiveDieWeekNum.Data();
	}

	public static mkdb.util.BeanPool<RedPackRoleHisInfo> poolRedPackRoleHisInfo = new mkdb.util.BeanPool<RedPackRoleHisInfo>() {
		@Override
		protected RedPackRoleHisInfo newBean() {
			return new xbean.__.RedPackRoleHisInfo();
		}
	};

	public static RedPackRoleHisInfo newRedPackRoleHisInfo() {
		return poolRedPackRoleHisInfo.get();
	}

	public static void _reset_unsafe_add_(RedPackRoleHisInfo bean) {
		poolRedPackRoleHisInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackRoleHisInfo bean) {
		mkdb.Procedure.padd(bean, poolRedPackRoleHisInfo);
	}

	public static RedPackRoleHisInfo newRedPackRoleHisInfoData() {
		return new xbean.__.RedPackRoleHisInfo.Data();
	}

	public static mkdb.util.BeanPool<RedPackInfo> poolRedPackInfo = new mkdb.util.BeanPool<RedPackInfo>() {
		@Override
		protected RedPackInfo newBean() {
			return new xbean.__.RedPackInfo();
		}
	};

	public static RedPackInfo newRedPackInfo() {
		return poolRedPackInfo.get();
	}

	public static void _reset_unsafe_add_(RedPackInfo bean) {
		poolRedPackInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackInfo bean) {
		mkdb.Procedure.padd(bean, poolRedPackInfo);
	}

	public static RedPackInfo newRedPackInfoData() {
		return new xbean.__.RedPackInfo.Data();
	}

	public static mkdb.util.BeanPool<RedPackRoleInfoList> poolRedPackRoleInfoList = new mkdb.util.BeanPool<RedPackRoleInfoList>() {
		@Override
		protected RedPackRoleInfoList newBean() {
			return new xbean.__.RedPackRoleInfoList();
		}
	};

	public static RedPackRoleInfoList newRedPackRoleInfoList() {
		return poolRedPackRoleInfoList.get();
	}

	public static void _reset_unsafe_add_(RedPackRoleInfoList bean) {
		poolRedPackRoleInfoList._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackRoleInfoList bean) {
		mkdb.Procedure.padd(bean, poolRedPackRoleInfoList);
	}

	public static RedPackRoleInfoList newRedPackRoleInfoListData() {
		return new xbean.__.RedPackRoleInfoList.Data();
	}

	public static mkdb.util.BeanPool<RedPackBaseInfo> poolRedPackBaseInfo = new mkdb.util.BeanPool<RedPackBaseInfo>() {
		@Override
		protected RedPackBaseInfo newBean() {
			return new xbean.__.RedPackBaseInfo();
		}
	};

	public static RedPackBaseInfo newRedPackBaseInfo() {
		return poolRedPackBaseInfo.get();
	}

	public static void _reset_unsafe_add_(RedPackBaseInfo bean) {
		poolRedPackBaseInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackBaseInfo bean) {
		mkdb.Procedure.padd(bean, poolRedPackBaseInfo);
	}

	public static RedPackBaseInfo newRedPackBaseInfoData() {
		return new xbean.__.RedPackBaseInfo.Data();
	}

	public static mkdb.util.BeanPool<RedPackInfoViewList> poolRedPackInfoViewList = new mkdb.util.BeanPool<RedPackInfoViewList>() {
		@Override
		protected RedPackInfoViewList newBean() {
			return new xbean.__.RedPackInfoViewList();
		}
	};

	public static RedPackInfoViewList newRedPackInfoViewList() {
		return poolRedPackInfoViewList.get();
	}

	public static void _reset_unsafe_add_(RedPackInfoViewList bean) {
		poolRedPackInfoViewList._reset_unsafe_add_(bean);
	}

	public static void padd(RedPackInfoViewList bean) {
		mkdb.Procedure.padd(bean, poolRedPackInfoViewList);
	}

	public static RedPackInfoViewList newRedPackInfoViewListData() {
		return new xbean.__.RedPackInfoViewList.Data();
	}

	public static mkdb.util.BeanPool<SRRedPackNum> poolSRRedPackNum = new mkdb.util.BeanPool<SRRedPackNum>() {
		@Override
		protected SRRedPackNum newBean() {
			return new xbean.__.SRRedPackNum();
		}
	};

	public static SRRedPackNum newSRRedPackNum() {
		return poolSRRedPackNum.get();
	}

	public static void _reset_unsafe_add_(SRRedPackNum bean) {
		poolSRRedPackNum._reset_unsafe_add_(bean);
	}

	public static void padd(SRRedPackNum bean) {
		mkdb.Procedure.padd(bean, poolSRRedPackNum);
	}

	public static SRRedPackNum newSRRedPackNumData() {
		return new xbean.__.SRRedPackNum.Data();
	}

	public static mkdb.util.BeanPool<SRRedPackNumList> poolSRRedPackNumList = new mkdb.util.BeanPool<SRRedPackNumList>() {
		@Override
		protected SRRedPackNumList newBean() {
			return new xbean.__.SRRedPackNumList();
		}
	};

	public static SRRedPackNumList newSRRedPackNumList() {
		return poolSRRedPackNumList.get();
	}

	public static void _reset_unsafe_add_(SRRedPackNumList bean) {
		poolSRRedPackNumList._reset_unsafe_add_(bean);
	}

	public static void padd(SRRedPackNumList bean) {
		mkdb.Procedure.padd(bean, poolSRRedPackNumList);
	}

	public static SRRedPackNumList newSRRedPackNumListData() {
		return new xbean.__.SRRedPackNumList.Data();
	}

	public static mkdb.util.BeanPool<RoleRedPackRecord> poolRoleRedPackRecord = new mkdb.util.BeanPool<RoleRedPackRecord>() {
		@Override
		protected RoleRedPackRecord newBean() {
			return new xbean.__.RoleRedPackRecord();
		}
	};

	public static RoleRedPackRecord newRoleRedPackRecord() {
		return poolRoleRedPackRecord.get();
	}

	public static void _reset_unsafe_add_(RoleRedPackRecord bean) {
		poolRoleRedPackRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleRedPackRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleRedPackRecord);
	}

	public static RoleRedPackRecord newRoleRedPackRecordData() {
		return new xbean.__.RoleRedPackRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleReceiveRedPackRecord> poolRoleReceiveRedPackRecord = new mkdb.util.BeanPool<RoleReceiveRedPackRecord>() {
		@Override
		protected RoleReceiveRedPackRecord newBean() {
			return new xbean.__.RoleReceiveRedPackRecord();
		}
	};

	public static RoleReceiveRedPackRecord newRoleReceiveRedPackRecord() {
		return poolRoleReceiveRedPackRecord.get();
	}

	public static void _reset_unsafe_add_(RoleReceiveRedPackRecord bean) {
		poolRoleReceiveRedPackRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleReceiveRedPackRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleReceiveRedPackRecord);
	}

	public static RoleReceiveRedPackRecord newRoleReceiveRedPackRecordData() {
		return new xbean.__.RoleReceiveRedPackRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleReceiveRedPackRecordList> poolRoleReceiveRedPackRecordList = new mkdb.util.BeanPool<RoleReceiveRedPackRecordList>() {
		@Override
		protected RoleReceiveRedPackRecordList newBean() {
			return new xbean.__.RoleReceiveRedPackRecordList();
		}
	};

	public static RoleReceiveRedPackRecordList newRoleReceiveRedPackRecordList() {
		return poolRoleReceiveRedPackRecordList.get();
	}

	public static void _reset_unsafe_add_(RoleReceiveRedPackRecordList bean) {
		poolRoleReceiveRedPackRecordList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleReceiveRedPackRecordList bean) {
		mkdb.Procedure.padd(bean, poolRoleReceiveRedPackRecordList);
	}

	public static RoleReceiveRedPackRecordList newRoleReceiveRedPackRecordListData() {
		return new xbean.__.RoleReceiveRedPackRecordList.Data();
	}

	public static mkdb.util.BeanPool<RoleTradingRecord> poolRoleTradingRecord = new mkdb.util.BeanPool<RoleTradingRecord>() {
		@Override
		protected RoleTradingRecord newBean() {
			return new xbean.__.RoleTradingRecord();
		}
	};

	public static RoleTradingRecord newRoleTradingRecord() {
		return poolRoleTradingRecord.get();
	}

	public static void _reset_unsafe_add_(RoleTradingRecord bean) {
		poolRoleTradingRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleTradingRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleTradingRecord);
	}

	public static RoleTradingRecord newRoleTradingRecordData() {
		return new xbean.__.RoleTradingRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleTradingHisRecord> poolRoleTradingHisRecord = new mkdb.util.BeanPool<RoleTradingHisRecord>() {
		@Override
		protected RoleTradingHisRecord newBean() {
			return new xbean.__.RoleTradingHisRecord();
		}
	};

	public static RoleTradingHisRecord newRoleTradingHisRecord() {
		return poolRoleTradingHisRecord.get();
	}

	public static void _reset_unsafe_add_(RoleTradingHisRecord bean) {
		poolRoleTradingHisRecord._reset_unsafe_add_(bean);
	}

	public static void padd(RoleTradingHisRecord bean) {
		mkdb.Procedure.padd(bean, poolRoleTradingHisRecord);
	}

	public static RoleTradingHisRecord newRoleTradingHisRecordData() {
		return new xbean.__.RoleTradingHisRecord.Data();
	}

	public static mkdb.util.BeanPool<RoleTradingHisRecordList> poolRoleTradingHisRecordList = new mkdb.util.BeanPool<RoleTradingHisRecordList>() {
		@Override
		protected RoleTradingHisRecordList newBean() {
			return new xbean.__.RoleTradingHisRecordList();
		}
	};

	public static RoleTradingHisRecordList newRoleTradingHisRecordList() {
		return poolRoleTradingHisRecordList.get();
	}

	public static void _reset_unsafe_add_(RoleTradingHisRecordList bean) {
		poolRoleTradingHisRecordList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleTradingHisRecordList bean) {
		mkdb.Procedure.padd(bean, poolRoleTradingHisRecordList);
	}

	public static RoleTradingHisRecordList newRoleTradingHisRecordListData() {
		return new xbean.__.RoleTradingHisRecordList.Data();
	}

	public static mkdb.util.BeanPool<RoleTradingInfoList> poolRoleTradingInfoList = new mkdb.util.BeanPool<RoleTradingInfoList>() {
		@Override
		protected RoleTradingInfoList newBean() {
			return new xbean.__.RoleTradingInfoList();
		}
	};

	public static RoleTradingInfoList newRoleTradingInfoList() {
		return poolRoleTradingInfoList.get();
	}

	public static void _reset_unsafe_add_(RoleTradingInfoList bean) {
		poolRoleTradingInfoList._reset_unsafe_add_(bean);
	}

	public static void padd(RoleTradingInfoList bean) {
		mkdb.Procedure.padd(bean, poolRoleTradingInfoList);
	}

	public static RoleTradingInfoList newRoleTradingInfoListData() {
		return new xbean.__.RoleTradingInfoList.Data();
	}

	public static mkdb.util.BeanPool<TradingPrice> poolTradingPrice = new mkdb.util.BeanPool<TradingPrice>() {
		@Override
		protected TradingPrice newBean() {
			return new xbean.__.TradingPrice();
		}
	};

	public static TradingPrice newTradingPrice() {
		return poolTradingPrice.get();
	}

	public static void _reset_unsafe_add_(TradingPrice bean) {
		poolTradingPrice._reset_unsafe_add_(bean);
	}

	public static void padd(TradingPrice bean) {
		mkdb.Procedure.padd(bean, poolTradingPrice);
	}

	public static TradingPrice newTradingPriceData() {
		return new xbean.__.TradingPrice.Data();
	}

	public static mkdb.util.BeanPool<TradingMap> poolTradingMap = new mkdb.util.BeanPool<TradingMap>() {
		@Override
		protected TradingMap newBean() {
			return new xbean.__.TradingMap();
		}
	};

	public static TradingMap newTradingMap() {
		return poolTradingMap.get();
	}

	public static void _reset_unsafe_add_(TradingMap bean) {
		poolTradingMap._reset_unsafe_add_(bean);
	}

	public static void padd(TradingMap bean) {
		mkdb.Procedure.padd(bean, poolTradingMap);
	}

	public static TradingMap newTradingMapData() {
		return new xbean.__.TradingMap.Data();
	}

	public static mkdb.util.BeanPool<TaskRecourse> poolTaskRecourse = new mkdb.util.BeanPool<TaskRecourse>() {
		@Override
		protected TaskRecourse newBean() {
			return new xbean.__.TaskRecourse();
		}
	};

	public static TaskRecourse newTaskRecourse() {
		return poolTaskRecourse.get();
	}

	public static void _reset_unsafe_add_(TaskRecourse bean) {
		poolTaskRecourse._reset_unsafe_add_(bean);
	}

	public static void padd(TaskRecourse bean) {
		mkdb.Procedure.padd(bean, poolTaskRecourse);
	}

	public static TaskRecourse newTaskRecourseData() {
		return new xbean.__.TaskRecourse.Data();
	}

	public static mkdb.util.BeanPool<RoleRecourse> poolRoleRecourse = new mkdb.util.BeanPool<RoleRecourse>() {
		@Override
		protected RoleRecourse newBean() {
			return new xbean.__.RoleRecourse();
		}
	};

	public static RoleRecourse newRoleRecourse() {
		return poolRoleRecourse.get();
	}

	public static void _reset_unsafe_add_(RoleRecourse bean) {
		poolRoleRecourse._reset_unsafe_add_(bean);
	}

	public static void padd(RoleRecourse bean) {
		mkdb.Procedure.padd(bean, poolRoleRecourse);
	}

	public static RoleRecourse newRoleRecourseData() {
		return new xbean.__.RoleRecourse.Data();
	}

	public static mkdb.util.BeanPool<AccusationInfo> poolAccusationInfo = new mkdb.util.BeanPool<AccusationInfo>() {
		@Override
		protected AccusationInfo newBean() {
			return new xbean.__.AccusationInfo();
		}
	};

	public static AccusationInfo newAccusationInfo() {
		return poolAccusationInfo.get();
	}

	public static void _reset_unsafe_add_(AccusationInfo bean) {
		poolAccusationInfo._reset_unsafe_add_(bean);
	}

	public static void padd(AccusationInfo bean) {
		mkdb.Procedure.padd(bean, poolAccusationInfo);
	}

	public static AccusationInfo newAccusationInfoData() {
		return new xbean.__.AccusationInfo.Data();
	}

	public static mkdb.util.BeanPool<YaoQianShuInfo> poolYaoQianShuInfo = new mkdb.util.BeanPool<YaoQianShuInfo>() {
		@Override
		protected YaoQianShuInfo newBean() {
			return new xbean.__.YaoQianShuInfo();
		}
	};

	public static YaoQianShuInfo newYaoQianShuInfo() {
		return poolYaoQianShuInfo.get();
	}

	public static void _reset_unsafe_add_(YaoQianShuInfo bean) {
		poolYaoQianShuInfo._reset_unsafe_add_(bean);
	}

	public static void padd(YaoQianShuInfo bean) {
		mkdb.Procedure.padd(bean, poolYaoQianShuInfo);
	}

	public static YaoQianShuInfo newYaoQianShuInfoData() {
		return new xbean.__.YaoQianShuInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleYaoQianShuInfo> poolRoleYaoQianShuInfo = new mkdb.util.BeanPool<RoleYaoQianShuInfo>() {
		@Override
		protected RoleYaoQianShuInfo newBean() {
			return new xbean.__.RoleYaoQianShuInfo();
		}
	};

	public static RoleYaoQianShuInfo newRoleYaoQianShuInfo() {
		return poolRoleYaoQianShuInfo.get();
	}

	public static void _reset_unsafe_add_(RoleYaoQianShuInfo bean) {
		poolRoleYaoQianShuInfo._reset_unsafe_add_(bean);
	}

	public static void padd(RoleYaoQianShuInfo bean) {
		mkdb.Procedure.padd(bean, poolRoleYaoQianShuInfo);
	}

	public static RoleYaoQianShuInfo newRoleYaoQianShuInfoData() {
		return new xbean.__.RoleYaoQianShuInfo.Data();
	}

	public static mkdb.util.BeanPool<RoleYaoQianShuInfoMaps> poolRoleYaoQianShuInfoMaps = new mkdb.util.BeanPool<RoleYaoQianShuInfoMaps>() {
		@Override
		protected RoleYaoQianShuInfoMaps newBean() {
			return new xbean.__.RoleYaoQianShuInfoMaps();
		}
	};

	public static RoleYaoQianShuInfoMaps newRoleYaoQianShuInfoMaps() {
		return poolRoleYaoQianShuInfoMaps.get();
	}

	public static void _reset_unsafe_add_(RoleYaoQianShuInfoMaps bean) {
		poolRoleYaoQianShuInfoMaps._reset_unsafe_add_(bean);
	}

	public static void padd(RoleYaoQianShuInfoMaps bean) {
		mkdb.Procedure.padd(bean, poolRoleYaoQianShuInfoMaps);
	}

	public static RoleYaoQianShuInfoMaps newRoleYaoQianShuInfoMapsData() {
		return new xbean.__.RoleYaoQianShuInfoMaps.Data();
	}

	public static mkdb.util.BeanPool<KickInfo> poolKickInfo = new mkdb.util.BeanPool<KickInfo>() {
		@Override
		protected KickInfo newBean() {
			return new xbean.__.KickInfo();
		}
	};

	public static KickInfo newKickInfo() {
		return poolKickInfo.get();
	}

	public static void _reset_unsafe_add_(KickInfo bean) {
		poolKickInfo._reset_unsafe_add_(bean);
	}

	public static void padd(KickInfo bean) {
		mkdb.Procedure.padd(bean, poolKickInfo);
	}

	public static KickInfo newKickInfoData() {
		return new xbean.__.KickInfo.Data();
	}

	public static mkdb.util.BeanPool<FunOpenClose> poolFunOpenClose = new mkdb.util.BeanPool<FunOpenClose>() {
		@Override
		protected FunOpenClose newBean() {
			return new xbean.__.FunOpenClose();
		}
	};

	public static FunOpenClose newFunOpenClose() {
		return poolFunOpenClose.get();
	}

	public static void _reset_unsafe_add_(FunOpenClose bean) {
		poolFunOpenClose._reset_unsafe_add_(bean);
	}

	public static void padd(FunOpenClose bean) {
		mkdb.Procedure.padd(bean, poolFunOpenClose);
	}

	public static FunOpenClose newFunOpenCloseData() {
		return new xbean.__.FunOpenClose.Data();
	}

	public static mkdb.util.BeanPool<ServiceSet> poolServiceSet = new mkdb.util.BeanPool<ServiceSet>() {
		@Override
		protected ServiceSet newBean() {
			return new xbean.__.ServiceSet();
		}
	};

	public static ServiceSet newServiceSet() {
		return poolServiceSet.get();
	}

	public static void _reset_unsafe_add_(ServiceSet bean) {
		poolServiceSet._reset_unsafe_add_(bean);
	}

	public static void padd(ServiceSet bean) {
		mkdb.Procedure.padd(bean, poolServiceSet);
	}

	public static ServiceSet newServiceSetData() {
		return new xbean.__.ServiceSet.Data();
	}

}

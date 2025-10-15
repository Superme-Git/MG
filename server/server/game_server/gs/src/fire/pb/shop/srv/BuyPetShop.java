package fire.pb.shop.srv;

import java.util.ArrayList;
import java.util.List;

import fire.pb.pet.PetColumn;
import fire.pb.shop.utils.ShopParameters;

/**
 * 宠物商店购买放入宠物栏中
 * 
 * @author liangyanpeng
 *
 */
public class BuyPetShop extends BuyShopBase implements Buy {

	protected PetColumn petcol;

	public BuyPetShop(ShopParameters params) {
		super(params);
	}

	@Override
	public boolean cost() {
		return super.commonCost();
	}

	@Override
	public void success() {
		super.commonSuccess();
		//购买成功消息
		List<String> parameters = new ArrayList<>(1); 
		parameters.add(this.name);
		fire.pb.talk.MessageMgr.sendMsgNotify(role.getRoleId(), 150501 , parameters);
		
	}

	@Override
	public void fail() {

	}

	@Override
	public boolean verify() {
		return super.commonVerify();
	}

	@Override
	public boolean add() {
		return new StorePetContainer(this).add(false);
	}

	@Override
	public boolean exc() {
		if (!verify()) {
			return false;
		}
		if (!super.commonCheckEnoughMoney()) {
			super.commonNotEnoughMoney();
			return false;
		}
		if (!cost()) {
			fail();
			return false;
		}
		if (!add()) {
			fail();
			return false;
		}
		success();
		return true;
	}

}

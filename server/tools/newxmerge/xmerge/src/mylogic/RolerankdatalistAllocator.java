package mylogic;

import java.util.Random;

import xmerge.IAllocator;

public class RolerankdatalistAllocator implements IAllocator {

	@Override
	public Object allocateKey(String tableName, Object oldKey) {
		// TODO Auto-generated method stub
		return new Random().nextInt(1000);
	}

}

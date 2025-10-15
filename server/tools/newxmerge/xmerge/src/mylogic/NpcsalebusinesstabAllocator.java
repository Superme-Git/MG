package mylogic;

import java.util.Random;

import xmerge.IAllocator;

public class NpcsalebusinesstabAllocator implements IAllocator {

	@Override
	public Object allocateKey(String tableName, Object oldKey) {
		// TODO Auto-generated method stub
		return new Random().nextLong();
	}

}

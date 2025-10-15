package com.pwrd.serializable.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.pwrd.command.Command;
import com.pwrd.serializable.JavaSerializationManager;

public class LoadCommand implements Command
{
	public static final String NAME = "load";
	
	@Override
	public void execute(String cmdStr)
	{
		String[] cmdArray = cmdStr.split("[\t\n\f\r ]+");
		assert cmdArray.length == 2;
		File jobjFile = new File(cmdArray[1]);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{
			fis = new FileInputStream(jobjFile);
			ois = new ObjectInputStream(fis);
			Object instance = ois.readObject();
			JavaSerializationManager.getInstance().put(cmdArray[1], instance);
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(ois != null)
			{
				try
				{
					ois.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}

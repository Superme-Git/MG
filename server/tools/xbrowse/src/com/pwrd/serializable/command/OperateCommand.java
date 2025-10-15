package com.pwrd.serializable.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pwrd.command.Command;
import com.pwrd.io.IO;
import com.pwrd.reflect.model.ObjectWrapper;
import com.pwrd.serializable.JavaSerializationManager;
import com.pwrd.util.Toolkit;

public class OperateCommand implements Command
{
	public static final String NAME = "operate";
	
	@Override
	public void execute(String cmdStr)
	{
		cmdStr = cmdStr.substring(NAME.length()).trim();
		List<String> cmdList = parseCommand(cmdStr);
		// assign value
		if(cmdList.size() >=3 && "=".equals(cmdList.get(cmdList.size() - 2)))
		{
			List<String> subCmdList = cmdList.subList(0, cmdList.size() - 2);
			Object instance = JavaSerializationManager.getInstance().getCurrent();
			ObjectWrapper variableObject = ObjectWrapper.createObjectWrapper(instance.getClass().getName(), instance, null, null);
			
			for(String childName : subCmdList) 
			{
				Map<String, ObjectWrapper> childObjects = variableObject.getFields();
				variableObject = childObjects.get(childName);
			}
			variableObject.getParent().setChildValue(variableObject, Toolkit.convert(cmdList.get(cmdList.size() - 1), variableObject.getValue()));
		}
		else // select value
		{
			Object instance = JavaSerializationManager.getInstance().getCurrent();
			ObjectWrapper variableObject = ObjectWrapper.createObjectWrapper(instance.getClass().getName(), instance, null, null);
			
			for(String childName : cmdList) 
			{
				Map<String, ObjectWrapper> childObjects = variableObject.getFields();
				variableObject = childObjects.get(childName);
			}
			IO.getInstance().println(variableObject.toCommandView(0));
		}
	}

	private static List<String> parseCommand(String command) 
	{
		List<String> cmdList = new LinkedList<String>();
		for(String cmd : command.split("\\.")) {
			if(cmd.indexOf("[") != -1) 
			{
				cmdList.add(cmd.substring(0, cmd.indexOf("[")));
				cmdList.add(cmd.substring(cmd.indexOf("[") + 1, cmd.indexOf("]")));
			}
			else if(cmd.indexOf("=") != -1)
			{
				cmdList.add(cmd.substring(0, cmd.indexOf("=")).trim());
				cmdList.add("=");
				cmdList.add(cmd.substring(cmd.indexOf("=") + 1).trim());
			}
			else 
				cmdList.add(cmd);
		}
		cmdList.remove(0);
		return cmdList;
	}
}

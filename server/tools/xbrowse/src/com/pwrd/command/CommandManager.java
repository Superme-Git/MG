package com.pwrd.command;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CommandManager
{
	private Map<String, Command> commands;
	
	public CommandManager()
	{
		commands = new HashMap<String, Command>();
		register(ExitCommand.NAME, new ExitCommand());
		register(InvalidCommand.NAME, new InvalidCommand());
	}
	
	public Command register(String name, Command command)
	{
		return commands.put(name, command);
	}
	
	public void register(String cfgXml)
	{
		DocumentBuilderFactory docBuilderFacotry = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder docBuilder = docBuilderFacotry.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(cfgXml));
			Element commandsElem = doc.getDocumentElement();
			NodeList commandNodeList = commandsElem.getElementsByTagName("command");
			for(int i = 0; i < commandNodeList.getLength(); ++ i)
			{
				Element commandElem = (Element)commandNodeList.item(i);
				String name = commandElem.getAttribute("name");
				String clazz = commandElem.getAttribute("class");
				try
				{
					commands.put(name, (Command)Class.forName(clazz).newInstance());
				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} 
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} 
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Command get(String name)
	{
		Command command = commands.get(name);
		if(command == null)
			return commands.get(InvalidCommand.NAME);
		return command;
	}
}

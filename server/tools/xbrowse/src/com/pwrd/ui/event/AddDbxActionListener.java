package com.pwrd.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import com.pwrd.dbx.DbxManager;

public class AddDbxActionListener implements ActionListener
{
	private JComboBox parent;
	
	public AddDbxActionListener(JComboBox _parent)
	{
		parent = _parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			if(DbxManager.getInstance().addDbx(file))
			{
				// 将新加入的xdb作为当前选中的
				parent.addItem(file.getName());
				parent.setSelectedItem(file.getName());
			}
		}
	}

}

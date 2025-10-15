package com.pwrd.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.dbx.DbxManager;

public class RemoveDbxActionListener implements ActionListener
{
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(RemoveDbxActionListener.class);
	private JComboBox parent;
	
	public RemoveDbxActionListener(JComboBox _parent)
	{
		parent = _parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		if(parent.getSelectedItem() != null)
		{
			int selectedIndex = parent.getSelectedIndex();
			String dbxName = parent.getSelectedItem().toString();
			
			if(DbxManager.getInstance().removeDbx(dbxName))
			{
				int index;
				/* 
				 * 如果删除的不是JComboBox的最后一个元素，
				 * 则使用下一个元素作为选中的元素，否则
				 * 使用上一个元素作为选中的元素。
				 */
				if(selectedIndex != parent.getItemCount() - 1)
					index = selectedIndex + 1;
				else
					index = selectedIndex - 1;
				/*
				 * 删除当前选中的元素会抛出异常，
				 * 删除之前先选中其他的元素。 
				 */
				parent.setSelectedIndex(index);
				parent.removeItemAt(selectedIndex);
			}
		}
	}

}

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
				 * ���ɾ���Ĳ���JComboBox�����һ��Ԫ�أ�
				 * ��ʹ����һ��Ԫ����Ϊѡ�е�Ԫ�أ�����
				 * ʹ����һ��Ԫ����Ϊѡ�е�Ԫ�ء�
				 */
				if(selectedIndex != parent.getItemCount() - 1)
					index = selectedIndex + 1;
				else
					index = selectedIndex - 1;
				/*
				 * ɾ����ǰѡ�е�Ԫ�ػ��׳��쳣��
				 * ɾ��֮ǰ��ѡ��������Ԫ�ء� 
				 */
				parent.setSelectedIndex(index);
				parent.removeItemAt(selectedIndex);
			}
		}
	}

}

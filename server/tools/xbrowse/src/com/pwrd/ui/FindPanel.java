package com.pwrd.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
// import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FindPanel extends JPanel
{
	private static final long serialVersionUID = -8574061378573379563L;
	
	private JLabel label;
	private JComboBox inputBox;
	private JButton btn; 
	private Set<String> queryHistory = new HashSet<String>();
	private String _lastQuery;
	private boolean _isQueryChanged;
	
	// private static Logger logger = Logger.getLogger("FindPanel");
	
	public FindPanel() 
	{
		label = new JLabel("Find: ");
		inputBox = new JComboBox();
		inputBox.setEditable(true);
		inputBox.addItem("");
		queryHistory.add("");
		ActionListener listener = new InputBoxActionListener();
		inputBox.addActionListener(listener);
		btn = new JButton("Find");
		btn.addActionListener(listener);
		
		add(label);
		add(inputBox);
		add(btn);
	}
	
	public String getQuery()
	{
		return inputBox.getSelectedItem().toString();
	}
	
	/**
	 * ��ѯ����Ƿ��Ѿ��ı䡣
	 * 
	 * @return true - �����ѯ����Ѿ��ı䣻false - �����ѯ���û�иı䡣
	 */
	public boolean isQueryChanged()
	{
		return _isQueryChanged;
	}
	
	public void addFindActionListener(ActionListener listener)
	{
		btn.addActionListener(listener);
	}
	
	public void removeFindActionListener(ActionListener listener)
	{
		btn.removeActionListener(listener);
	}
	
	private class InputBoxActionListener implements ActionListener
	{
		/**
		 * ��һ��<code>ActionEvent</code>���͵�ʱ�䣬��������
		 * �ظ����͵���Ϣ��
		 */
		private long _lastWhen;
		
		public InputBoxActionListener()
		{
			_lastWhen = 0;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(_lastWhen == e.getWhen())
				return;
			else
				_lastWhen = e.getWhen();
				
			String query = inputBox.getSelectedItem().toString();
			if(query != null)
			{	
				if(query.equalsIgnoreCase(_lastQuery))
					_isQueryChanged = false;
				else
				{
					_isQueryChanged = true;
					_lastQuery = query;
				}
				if(!queryHistory.contains(query))
				{
					queryHistory.add(query);
					inputBox.addItem(query);
				}
			}
		}
	}
}

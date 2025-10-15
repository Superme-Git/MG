package com.pwrd.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.dbx.DbxManager;
import com.pwrd.util.ConflictChecker;

public class DbxSelectionDlg extends JDialog
{
	private static final long serialVersionUID = 6397558509137927484L;
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(DbxSelectionDlg.class);
	
	private JCheckBox[] checkBoxs = null;
	private JButton okBtn = new JButton("Ok");
	private JButton cancelBtn = new JButton("Cancel");
	
	public DbxSelectionDlg(Frame owner)
	{
		super(owner, "Select dbxs");
		
		List<String> dbxNames = new ArrayList<String>(DbxManager.getInstance().getDbxNames());
		int size = dbxNames.size();
		setBounds(400, 300, 250, 30 * size + 70);
		getContentPane().setLayout(null);
		checkBoxs = new JCheckBox[dbxNames.size()];
		
		for(int i = 0; i < size; ++ i)
		{
			checkBoxs[i] = new JCheckBox(dbxNames.get(i));
			getContentPane().add(checkBoxs[i]);
			checkBoxs[i].setBounds(50, i*25, 200, 25);
		}
		getContentPane().add(okBtn);
		okBtn.setBounds(30, size*25, 80, 25);
		getContentPane().add(cancelBtn);
		cancelBtn.setBounds(130, size*25, 80, 25);
		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				List<String> selectedDbxNames = new ArrayList<String>();
				for(int i = 0; i < checkBoxs.length; ++ i)
				{
					DbxSelectionDlg.this.setVisible(false);
					if(checkBoxs[i].isSelected())
						selectedDbxNames.add(checkBoxs[i].getText());
					String[] array = new String[selectedDbxNames.size()];
					ConflictChecker checker = new ConflictChecker(selectedDbxNames.toArray(array));
					// SwingUtilities.invokeLater(checker);
					Thread checkerThread = new Thread(checker);
					checkerThread.start();
				}
			}
			
		});
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				DbxSelectionDlg.this.setVisible(false);
			}
			
		});
	}
}

package com.mammoth.logger.gui;

import org.eclipse.swt.widgets.*;

public class WaitDialog extends Composite {

	public WaitDialog() {
		super(new Shell(), 0);
	}

	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		return result;
	}

	protected void createContents() {
		shell = new Shell(0x10000);
		shell.setSize(120, 70);
		shell.setText("MT_G Dialog");
		int x = (getParent().getSize().x - shell.getSize().x) / 2
				+ getParent().getLocation().x;
		int y = (getParent().getSize().y - shell.getSize().y) / 2
				+ getParent().getLocation().y;
		shell.setLocation(x, y);
		Label label = new Label(shell, 0);
		label.setText("LOADING......");
		label.setBounds(28, 27, 80, 25);
	}

	public void dispose() {
		try {
			shell.dispose();
			getParent().layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Object result;
	protected Shell shell;
}
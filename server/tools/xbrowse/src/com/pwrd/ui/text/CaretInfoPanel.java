package com.pwrd.ui.text;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CaretInfoPanel extends JPanel implements CaretListener
{
	private static final long serialVersionUID = 8362734241619694893L;

	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(CaretInfoPanel.class);
	
	private JToolBar noTipTB;
	private JToolBar editorTipTB;
	private JLabel rwLabel;
	private JLabel infoLabel;
	
	public CaretInfoPanel()
	{
		super();
		setAlignmentX(RIGHT_ALIGNMENT);
		noTipTB = new JToolBar();
		editorTipTB = new JToolBar();
		rwLabel = new JLabel("Writable");
		infoLabel = new JLabel();
		editorTipTB.add(rwLabel);
		editorTipTB.addSeparator();
		editorTipTB.add(infoLabel);
		noTipTB.add(new JLabel("Caret Status"));
		add(noTipTB);
		add(editorTipTB);
	}
	
	@Override
	public void caretUpdate(CaretEvent e) 
	{
		JTextPane textPane = (JTextPane)e.getSource();
		StyledDocument doc = textPane.getStyledDocument();
		if(textPane.isEditable())
		{
			int offset = e.getDot();
			int index = doc.getDefaultRootElement().getElementIndex(offset);
			int row = index + 1;
			Element elem = doc.getDefaultRootElement().getElement(index);
			int column = offset - elem.getStartOffset() + 1;
			infoLabel.setText("Ln : " + row + "  Col : " + column);
			noTipTB.setVisible(false);
			editorTipTB.setVisible(true);
		}
		else
		{
			noTipTB.setVisible(true);
			editorTipTB.setVisible(false);
		}
	}

}

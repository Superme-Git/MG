package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;

import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.resources.ConstantIcons;
import com.pwrd.ui.text.KeywordHighlighter;

public class QueryPanel extends JPanel
{
	private static final long serialVersionUID = 4535778219487213170L;
	
	
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(QueryPanel.class);
	
	private JTextPane _queryPane;
	private JScrollPane _scrollPane;
	private LineNumberPanel _lineNumber;
	private StyledDocument _queryDoc;
	
	/** ��ǰ�������ɫ */
	private String _curFontFamily;
	private int _curFontStyle;
	private int _curFontSize;
	private Color _curForegroundColor;
	
	private Map<Object, Action> _actions;
	
	private Map<String, JButton> _fontSizeBtns;
	private Map<String, JButton> _fontFamilyBtns;
	private Map<String, JButton> _colorBtns;
	
	/** undo��redo */
	UndoManager undoManager = new UndoManager();
	UndoAction undoAction = new UndoAction();
	RedoAction redoAction = new RedoAction();
	
	public QueryPanel()
	{
		super(new BorderLayout());
		/** ���ı������ػ�ʱ��Ҳ֪ͨ�к������ػ� */
		_queryPane = new JTextPane() 
		{
			private static final long serialVersionUID = 6946905755300232581L;

			public void paint(Graphics g)
			{
				super.paint(g);
				_lineNumber.repaint();
			}
		};
		_queryDoc = _queryPane.getStyledDocument();
		_scrollPane = new JScrollPane(_queryPane);
		_lineNumber = new LineNumberPanel(_scrollPane, _queryPane);
		// Highlighter highlighter = _queryPane.getHighlighter();
		// logger.info(highlighter.getClass().getName());
		
		_curFontFamily = _queryPane.getFont().getFamily();
		// _curFontSize = _queryPane.getFont().getSize();
		_curFontSize = 14; // �޸�Ĭ��ֵ
		_curFontStyle = _queryPane.getFont().getStyle();
		_curForegroundColor = _queryPane.getForeground();
		_queryPane.setFont(new Font(_curFontFamily, _curFontStyle, _curFontSize));
		
		_actions = createActionMap(_queryPane);
		/** ���������� */
		JToolBar toolbar = new JToolBar();
		addActions(toolbar);
		
		add(toolbar, BorderLayout.PAGE_START);
		add(_lineNumber, BorderLayout.WEST);
		add(_scrollPane, BorderLayout.CENTER);
		
		_queryDoc.addUndoableEditListener(new QueryUndoableEditListener());
		_queryDoc.addDocumentListener(new KeywordHighlighter());
		// _queryPane.addCaretListener(new QueryStatementHighlighter());
	}
	
	
	private Map<Object, Action> createActionMap(JTextComponent textCompo)
	{
		assert textCompo != null;
		Map<Object, Action> actionMap = new HashMap<Object, Action>();
		Action[] actionArray = textCompo.getActions();
		for(Action a : actionArray)
			actionMap.put(a.getValue(Action.NAME), a);
		return actionMap;
	}
	
	private void addActions(JToolBar toolBar)
	{
		assert toolBar != null;
		
		/** undo and redo */
		JButton undoBtn = toolBar.add(undoAction);
		undoBtn.setIcon(ConstantIcons.getIcon("undo"));
		undoBtn.setHideActionText(true);
		JButton redoBtn = toolBar.add(redoAction);
		redoBtn.setIcon(ConstantIcons.getIcon("redo"));
		redoBtn.setHideActionText(true);
		toolBar.addSeparator();
		
		/** Edit */
		JButton cutBtn = toolBar.add(getActionByName(DefaultEditorKit.cutAction));
		cutBtn.setIcon(ConstantIcons.getIcon("cut"));
		cutBtn.setHideActionText(true);
		JButton copyBtn = toolBar.add(getActionByName(DefaultEditorKit.copyAction));
		copyBtn.setIcon(ConstantIcons.getIcon("copy"));
		copyBtn.setHideActionText(true);
		JButton pasteBtn = toolBar.add(getActionByName(DefaultEditorKit.pasteAction));
		pasteBtn.setIcon(ConstantIcons.getIcon("paste"));
		pasteBtn.setHideActionText(true);
		// JButton selectAllBtn = toolBar.add(getActionByName(DefaultEditorKit.selectAllAction));
		toolBar.addSeparator();
		
		/** font style */
		Action boldAction = new StyledEditorKit.BoldAction();
		boldAction.putValue(Action.NAME, "Bold");
		JButton boldBtn = toolBar.add(boldAction);
		boldBtn.setIcon(ConstantIcons.getIcon("bold"));
		boldBtn.setHideActionText(true);
		boldBtn.setRolloverEnabled(true);
		Action italicAction = new StyledEditorKit.ItalicAction();
		italicAction.putValue(Action.NAME, "Italic");
		JButton italicBtn = toolBar.add(italicAction);
		italicBtn.setIcon(ConstantIcons.getIcon("italic"));
		italicBtn.setHideActionText(true);
		Action underlineAction = new StyledEditorKit.UnderlineAction();
		underlineAction.putValue(Action.NAME, "Underline");
		JButton underlineBtn = toolBar.add(underlineAction);
		underlineBtn.setIcon(ConstantIcons.getIcon("underline"));
		underlineBtn.setHideActionText(true);
		toolBar.addSeparator();
		
		/** font size */
		JComboBox sizeBox = new JComboBox();
		sizeBox.addActionListener(new TextPropertyComboBoxActionListener("size"));
		_fontSizeBtns = new HashMap<String, JButton>();
		_fontSizeBtns.put("12", new JButton(new StyledEditorKit.FontSizeAction("12", 12)));
		_fontSizeBtns.put("14", new JButton(new StyledEditorKit.FontSizeAction("14", 14)));
		_fontSizeBtns.put("18", new JButton(new StyledEditorKit.FontSizeAction("18", 18)));
		if(!_fontSizeBtns.keySet().contains(_curFontSize + ""))
			_fontSizeBtns.put(_curFontSize + "", new JButton(new StyledEditorKit.FontSizeAction(_curFontSize + "", _curFontSize)));
		for(String item : _fontSizeBtns.keySet())
			sizeBox.addItem(item);
		sizeBox.setSelectedItem(_curFontSize + "");
		toolBar.add(sizeBox);
		toolBar.addSeparator();
		
		/** font family */
		JComboBox fontBox = new JComboBox();
		fontBox.setRenderer(new FontFamilyComboBoxRenderer());
		fontBox.addActionListener(new TextPropertyComboBoxActionListener("family"));
		_fontFamilyBtns = new HashMap<String, JButton>();
		_fontFamilyBtns.put("Serif", new JButton(new StyledEditorKit.FontFamilyAction("Serif", "Serif")));
		_fontFamilyBtns.put("SansSerif", new JButton(new StyledEditorKit.FontFamilyAction("SansSerif", "SansSerif")));
		if(!_fontFamilyBtns.keySet().contains(_curFontFamily))
			_fontFamilyBtns.put(_curFontFamily, new JButton(new StyledEditorKit.FontFamilyAction(_curFontFamily, _curFontFamily)));
		for(String item : _fontFamilyBtns.keySet())
			fontBox.addItem(item);
		fontBox.setSelectedItem(_curFontFamily);
		toolBar.add(fontBox);
		toolBar.addSeparator();
		
		/** Color */
		JComboBox colorBox = new JComboBox();
		colorBox.setRenderer(new ColorComboBoxRenderer());
		colorBox.addActionListener(new TextPropertyComboBoxActionListener("color"));
		_colorBtns = new HashMap<String, JButton>();
		for(Map.Entry<String, Color> entry : ColorComboBoxRenderer.getCommonColors().entrySet())
			_colorBtns.put(entry.getKey(), new JButton(new StyledEditorKit.ForegroundAction(entry.getKey(), entry.getValue())));
		String selectedColor = "Black";
		if(!ColorComboBoxRenderer.getCommonColors().values().contains(_curForegroundColor))
		{
			ColorComboBoxRenderer.getCommonColors().put("Default", _curForegroundColor);
			_colorBtns.put("Default", new JButton(new StyledEditorKit.ForegroundAction("Default", _curForegroundColor)));
			selectedColor = "Default";
		}
		for(String item : _colorBtns.keySet())
			colorBox.addItem(item);
		colorBox.setSelectedItem(selectedColor);
		toolBar.add(colorBox);
	}
	
	private Action getActionByName(String name)
	{
		return _actions.get(name);
	}
	
	/**
	 * �õ��û�����Ĳ�ѯ��䡣
	 * 
	 * @return �û�����Ĳ�ѯ��䣬��������쳣����null
	 */
	public String getQueryStatements()
	{
		String queryStmts = null;
		try 
		{
			queryStmts = _queryDoc.getText(0, _queryDoc.getLength());
		} 
		catch (BadLocationException e) 
		{
			e.printStackTrace();
		}
		
		return queryStmts;
	}
	
	/**
	 * �ڲ�ѯ����ı���Ŀ�ͷ��������Ĳ�ѯ��䡣
	 * 
	 * @param queryStmts - ������Ĳ�ѯ���
	 */
	public void setQueryStatements(String queryStmts)
	{
		try 
		{
			_queryDoc.insertString(0, queryStmts, null);
		} 
		catch (BadLocationException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addCaretListener(CaretListener listener)
	{
		_queryPane.addCaretListener(listener);
	}
	
	/**
	 * ���������С��font family����ɫ��<code>JComboBox</code>��������
	 * ���ı������С��font family����ɫʱ��ϵͳ��ִ�еĶ���ֻ��ȥ����
	 * <code>StyledEditorKit</code>����Ӧ<code>Action</code>�����
	 * �������ߵļ������ϲ���һ���û��ı��ı����ԣ������С��font family����ɫ��
	 * ʱ����<code>JTextPane</code>��ʾЧ����
	 * 
	 * @author wp853
	 *
	 */
	private class TextPropertyComboBoxActionListener implements ActionListener
	{
		// static Log innerLogger = LogFactory.getLog(SizeComboBoxActionListener.class);
		private String _type;
		
		public TextPropertyComboBoxActionListener(String type)
		{
			_type = type;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			JComboBox box = (JComboBox)e.getSource();

			if("size".equals(_type))
				_fontSizeBtns.get(box.getSelectedItem().toString()).doClick();
			else if("family".equals(_type))
				_fontFamilyBtns.get(box.getSelectedItem().toString()).doClick();
			else if("color".equals(_type))
				_colorBtns.get(box.getSelectedItem().toString()).doClick();
		}
		
	}
	
	/**
	 * 
	 * 
	 * @author wp853
	 *
	 */
	private class QueryUndoableEditListener implements UndoableEditListener
	{
		@Override
		public void undoableEditHappened(UndoableEditEvent e) 
		{
			undoManager.addEdit(e.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}
	
	/**
	 * 
	 * @author wp853
	 *
	 */
	private class UndoAction extends AbstractAction
	{
		private static final long serialVersionUID = 4903465833356849292L;

		public UndoAction()
		{
			super("Undo");
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			undoManager.undo();
			updateUndoState();
			redoAction.updateRedoState();
		}
		
		public void updateUndoState()
		{
			if(undoManager.canUndo())
			{
				setEnabled(true);
				putValue(Action.NAME, undoManager.getUndoPresentationName());
			}
			else
			{
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}
	
	/**
	 * 
	 * @author wp853
	 *
	 */
	private class RedoAction extends AbstractAction
	{
		private static final long serialVersionUID = 7830161048980810111L;

		public RedoAction()
		{
			super("Redo");
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			undoManager.redo();
			updateRedoState();
			undoAction.updateUndoState();
		}
		
		public void updateRedoState()
		{
			if(undoManager.canRedo())
			{
				setEnabled(true);
				putValue(Action.NAME, undoManager.getRedoPresentationName());
			}
			else
			{
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
	
	/**
	 * ���г�font family��<code>JComboBox</code>��ʾʱ���ͻ����
	 * <code>FontFamilyComboBoxRenderer</code>����ÿһ������û�����
	 * �����Ч��û��ֱ�ӵ�ӡ������ڻ���ÿһ��ʱ�������õ�font family
	 * ��Ϊ��������Ӧ��font family��
	 * 
	 * @author wp853
	 *
	 */
	private static class FontFamilyComboBoxRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = 7353209866332870109L;
		// static Log logger = LogFactory.getLog(FontFamilyComboBoxRenderer.class);
		
		public FontFamilyComboBoxRenderer()
		{
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) 
		{
			setText(value.toString());
			/** �����ı������family���� */
			Font oldFont = getFont();
			Font newFont = new Font(value.toString(), oldFont.getStyle(), oldFont.getSize());
			setFont(newFont);
			if(isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			
			return this;
		}
		
	}
}
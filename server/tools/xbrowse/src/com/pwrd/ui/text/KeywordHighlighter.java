package com.pwrd.ui.text;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.xql.Token;
import com.pwrd.xql.Tokenizer;

/**
 * 关键字高亮度显示，目前该类只支持<code>StyledDocument</code>中关键字
 * 的高亮度显示。通过使用词法分析器进行解析，可以比较灵活地支持高亮度显示。
 * 
 * @author wp853
 *
 */
public class KeywordHighlighter implements DocumentListener
{
	private static Log logger = LogFactory.getLog(KeywordHighlighter.class);
	private SimpleAttributeSet keywordAS;
	private SimpleAttributeSet plainAS;
	// private Set<String> keywords = new HashSet<String>();
	
	public KeywordHighlighter()
	{
		keywordAS = new SimpleAttributeSet();
		keywordAS.addAttribute(StyleConstants.Foreground, Color.BLUE);
		keywordAS.addAttribute(StyleConstants.Bold, true);
		plainAS = new SimpleAttributeSet();
	}
	
	/**
	 * 
	 * @param doc
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	private char getDocumentChar(StyledDocument doc, int offset) throws BadLocationException
	{
		char ch = doc.getText(offset, 1).charAt(0);
		
		return ch;
	}
	
	/**
	 * 当插入一个字符序列时，需要判断这次插入产生了哪些新的关键字，
	 * 消除了哪些之前存在的关键字。新的关键字以高亮度显示，消除的
	 * 关键字则恢复默认风格。如果通过键盘手动输入，每插入一个字符
	 * <code>Document</code>就会触发一个<code>DocumentEvent</code>，
	 * 如果通过粘贴，则一次粘贴（即插入一个字符序列）才会触发一个
	 * <code>DocumentEvent</code>。一个字符可以看做是长度为1的字
	 * 符序列，因此该方法考虑插入一个字符序列时，<code>Document</code>
	 * 需要作出的变化。
	 */
	@Override
	public void insertUpdate(DocumentEvent e) 
	{
		// ++ count;
		StyledDocument queryDoc = (StyledDocument)e.getDocument();
		int offset = e.getOffset();
		int length = e.getLength();
		int sentinel = queryDoc.getLength();
		
		/*
		 * 如果插入的第一个字符是空格，那么这次插入不会影响插入位置
		 * 之前的字符串；如果插入的最后一个字符是空格，那么这次插入
		 * 不会影响插入位置之后的字符串。
		 */
		try 
		{
			int backward = offset - 1;
			
			while(backward >= 0) // 如果偏移位置合法，则进入循环
			{
				char ch = getDocumentChar(queryDoc, backward);
				if(Tokenizer.isIdChar(ch))						
					-- backward;
				else
					break;
			}
			// 跳出循环后，backward < 0或者backward所指向的字符不为id char。
			++ backward; // 指向合法的位置
			
			int forward = offset + length;	
			while(forward < sentinel) // 如果偏移位置合法，则进入循环
			{
				char ch = getDocumentChar(queryDoc, forward);
				if(Tokenizer.isIdChar(ch))
					++ forward;
				else
					break;
			}
			// 跳出循环后，forward == sentinel或者forward所指向的字符不为id char。
			// 由于需要处理的字符串为[backward, forward)因此不需要进行操作。
			
			// String text = queryDoc.getText(backward, forward - backward);
			// logger.debug(text + " backward: " + backward + " forward: " + forward);
			Tokenizer tokenizer = new Tokenizer(queryDoc.getText(backward, forward - backward));
			while(tokenizer.hasNextToken())
			{
				Token token = tokenizer.getNextToken();
				int off = backward + token.getOffset();
				int len = token.getText().length();
				if(Tokenizer.isKeyword(token.getText()))
					SwingUtilities.invokeLater(new SetDocumentAttributes(queryDoc, off, len, keywordAS, true));
				else
					SwingUtilities.invokeLater(new SetDocumentAttributes(queryDoc, off, len, plainAS, true));	
			}
		} 
		catch (BadLocationException e1) 
		{
			e1.printStackTrace();
			logger.warn(e1.getMessage());
		}
	}

	/**
	 * 当删除掉一个字符序列之后，只需要考虑这次删除对其两侧造成的影响，
	 * 并且每一侧最多影响一个单词。
	 * @see insertUpdate
	 */
	@Override
	public void removeUpdate(DocumentEvent e) 
	{
		// 删除掉一个字符序列，只会改变位于其两侧的单词
		StyledDocument queryDoc = (StyledDocument)e.getDocument();
		int offset = e.getOffset(); // offset >= 0 && offset <= sentinel
		int sentinel = queryDoc.getLength(); // 文档结束位置（非法位置）
		
		// 不考虑从有删除至无的情况
		if(offset <= sentinel)
		{
			try
			{
				// 向后找到一个分隔符，从当前位置的上一个位置开始查找（backward）
				int backward = offset - 1;
				while(backward >= 0) // 如果找到文档开头都没有找到分隔符，则startPos采用默认值
				{
					if(Tokenizer.isIdChar(getDocumentChar(queryDoc, backward)))
						-- backward;
					else
						break;
				}
				// 循环结束后，backward < 0或者backward所指向的不为id char
				++ backward; // 指向合法的位置
				
				// 向前找到一个分隔符，从当前位置的下一个位置开始查找。
				int forward = offset + 1;
				while(forward < sentinel)
				{
					if(Tokenizer.isIdChar(getDocumentChar(queryDoc, forward)))
						++ forward;
					else
						break;
				}
				// 循环结束后，forward == sentinel或者forward所指向的不为id char
				forward = (forward > sentinel) ? sentinel : forward;
				// String text = queryDoc.getText(backward, forward - backward);
				// logger.debug(text + " backward: " + backward + " forward: " + forward);
				Tokenizer tokenizer = new Tokenizer(queryDoc.getText(backward, forward - backward));
				while(tokenizer.hasNextToken())
				{
					Token token = tokenizer.getNextToken();
					int off = backward + token.getOffset();
					int len = token.getText().length();
					if(Tokenizer.isKeyword(token.getText()))
						SwingUtilities.invokeLater(new SetDocumentAttributes(queryDoc, off, len, keywordAS, true));
					else
						SwingUtilities.invokeLater(new SetDocumentAttributes(queryDoc, off, len, plainAS, true));	
				}
			}
			catch (BadLocationException e1)
			{
				e1.printStackTrace();
				logger.warn(e1.getMessage());
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) 
	{
		// logger.info("changedUpdate");
	}
	
	private static class SetDocumentAttributes implements Runnable
	{
		private StyledDocument _doc;
		private int _offset;
		private int _length;
		private AttributeSet _as;
		private boolean _isReplace;
		
		public SetDocumentAttributes(StyledDocument doc, int offset, int length, AttributeSet as, boolean isReplace)
		{
			_doc = doc;
			_offset = offset;
			_length = length;
			_as = as;
			_isReplace = isReplace;
		}
		
		
		@Override
		public void run() 
		{
			assert _doc != null;
			_doc.setCharacterAttributes(_offset, _length, _as, _isReplace);
		}
		
	}
}
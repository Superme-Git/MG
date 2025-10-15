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
 * �ؼ��ָ�������ʾ��Ŀǰ����ֻ֧��<code>StyledDocument</code>�йؼ���
 * �ĸ�������ʾ��ͨ��ʹ�ôʷ����������н��������ԱȽ�����֧�ָ�������ʾ��
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
	 * ������һ���ַ�����ʱ����Ҫ�ж���β����������Щ�µĹؼ��֣�
	 * ��������Щ֮ǰ���ڵĹؼ��֡��µĹؼ����Ը�������ʾ��������
	 * �ؼ�����ָ�Ĭ�Ϸ�����ͨ�������ֶ����룬ÿ����һ���ַ�
	 * <code>Document</code>�ͻᴥ��һ��<code>DocumentEvent</code>��
	 * ���ͨ��ճ������һ��ճ����������һ���ַ����У��Żᴥ��һ��
	 * <code>DocumentEvent</code>��һ���ַ����Կ����ǳ���Ϊ1����
	 * �����У���˸÷������ǲ���һ���ַ�����ʱ��<code>Document</code>
	 * ��Ҫ�����ı仯��
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
		 * �������ĵ�һ���ַ��ǿո���ô��β��벻��Ӱ�����λ��
		 * ֮ǰ���ַ����������������һ���ַ��ǿո���ô��β���
		 * ����Ӱ�����λ��֮����ַ�����
		 */
		try 
		{
			int backward = offset - 1;
			
			while(backward >= 0) // ���ƫ��λ�úϷ��������ѭ��
			{
				char ch = getDocumentChar(queryDoc, backward);
				if(Tokenizer.isIdChar(ch))						
					-- backward;
				else
					break;
			}
			// ����ѭ����backward < 0����backward��ָ����ַ���Ϊid char��
			++ backward; // ָ��Ϸ���λ��
			
			int forward = offset + length;	
			while(forward < sentinel) // ���ƫ��λ�úϷ��������ѭ��
			{
				char ch = getDocumentChar(queryDoc, forward);
				if(Tokenizer.isIdChar(ch))
					++ forward;
				else
					break;
			}
			// ����ѭ����forward == sentinel����forward��ָ����ַ���Ϊid char��
			// ������Ҫ������ַ���Ϊ[backward, forward)��˲���Ҫ���в�����
			
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
	 * ��ɾ����һ���ַ�����֮��ֻ��Ҫ�������ɾ������������ɵ�Ӱ�죬
	 * ����ÿһ�����Ӱ��һ�����ʡ�
	 * @see insertUpdate
	 */
	@Override
	public void removeUpdate(DocumentEvent e) 
	{
		// ɾ����һ���ַ����У�ֻ��ı�λ��������ĵ���
		StyledDocument queryDoc = (StyledDocument)e.getDocument();
		int offset = e.getOffset(); // offset >= 0 && offset <= sentinel
		int sentinel = queryDoc.getLength(); // �ĵ�����λ�ã��Ƿ�λ�ã�
		
		// �����Ǵ���ɾ�����޵����
		if(offset <= sentinel)
		{
			try
			{
				// ����ҵ�һ���ָ������ӵ�ǰλ�õ���һ��λ�ÿ�ʼ���ң�backward��
				int backward = offset - 1;
				while(backward >= 0) // ����ҵ��ĵ���ͷ��û���ҵ��ָ�������startPos����Ĭ��ֵ
				{
					if(Tokenizer.isIdChar(getDocumentChar(queryDoc, backward)))
						-- backward;
					else
						break;
				}
				// ѭ��������backward < 0����backward��ָ��Ĳ�Ϊid char
				++ backward; // ָ��Ϸ���λ��
				
				// ��ǰ�ҵ�һ���ָ������ӵ�ǰλ�õ���һ��λ�ÿ�ʼ���ҡ�
				int forward = offset + 1;
				while(forward < sentinel)
				{
					if(Tokenizer.isIdChar(getDocumentChar(queryDoc, forward)))
						++ forward;
					else
						break;
				}
				// ѭ��������forward == sentinel����forward��ָ��Ĳ�Ϊid char
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
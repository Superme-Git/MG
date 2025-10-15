
package com.pwrd.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author wp853
 */
public class TimerKitDlg extends javax.swing.JDialog 
{
	private static final long serialVersionUID = -3206137007288717259L;
	
	private static Log logger = LogFactory.getLog(TimerKitDlg.class);
	private SimpleDateFormat dateFormat;
	
    public TimerKitDlg(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        setTitle("TimerKit");
        initComponents();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        stamp2TimeBtn.addActionListener(new ActionListener() 
        {
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				String text = timestampText.getText();
				try
				{
					long timestamp = Long.parseLong(text);
					java.sql.Date date = new java.sql.Date(timestamp);
					String dateStr = dateFormat.format(date);
					timeText.setText(dateStr);
				}
				catch(NumberFormatException e)
				{
					logger.info(text + " isn't a valid number");
				}
			}	
        });
        cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				TimerKitDlg.this.setVisible(false);
			}
        	
        });
        time2StampBtn.addActionListener(new ActionListener() 
        {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String time = timeText.getText();
				try 
				{
					java.util.Date date = dateFormat.parse(time);
					timestampText.setText("" + date.getTime());
				} 
				catch (ParseException e1) 
				{
					logger.info(time + " isn't in valid time format yyyy-MM-dd HH:mm:ss.SSS.");
					// e1.printStackTrace();
				}
			}
        	
        });
    }

    private void initComponents() 
    {
        jLabel1 = new javax.swing.JLabel();
        timestampText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        timeText = new javax.swing.JTextField();
        stamp2TimeBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        time2StampBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("TimerKitDlg"); // NOI18N

        jLabel1.setText("时间戳: "); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        timestampText.setText(""); // NOI18N
        timestampText.setName("timestampText"); // NOI18N

        jLabel2.setText("时间: "); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        timeText.setText(""); // NOI18N
        timeText.setName("timeText"); // NOI18N
        timeText.setToolTipText("yyyy-MM-dd HH:mm:ss.SSS");

        stamp2TimeBtn.setText("->"); // NOI18N
        stamp2TimeBtn.setName("stamp2TimeBtn"); // NOI18N
        stamp2TimeBtn.setPreferredSize(new java.awt.Dimension(81, 23));
        stamp2TimeBtn.setToolTipText("Timestamp to Time");
        
        cancelBtn.setText("Cancel"); // NOI18N
        cancelBtn.setName("cancelBtn"); // NOI18N
        cancelBtn.setPreferredSize(new java.awt.Dimension(81, 23));

        time2StampBtn.setText("<-"); // NOI18N
        time2StampBtn.setName("time2StampBtn"); // NOI18N
        time2StampBtn.setPreferredSize(new java.awt.Dimension(81, 23));
        time2StampBtn.setToolTipText("Time to Timestamp");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(time2StampBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stamp2TimeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeText)
                            .addComponent(timestampText, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(timestampText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(timeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(stamp2TimeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(time2StampBtn)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }
    
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton time2StampBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton stamp2TimeBtn;
    private javax.swing.JTextField timeText;
    private javax.swing.JTextField timestampText;
}

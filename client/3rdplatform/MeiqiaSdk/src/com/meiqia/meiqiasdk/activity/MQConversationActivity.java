package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.callback.OnClientOnlineCallback;
import com.meiqia.meiqiasdk.callback.OnGetMessageListCallBack;
import com.meiqia.meiqiasdk.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.callback.SimpleCallback;
import com.meiqia.meiqiasdk.controller.ControllerImpl;
import com.meiqia.meiqiasdk.controller.MQController;
import com.meiqia.meiqiasdk.dialog.MQEvaluateDialog;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.AgentChangeMessage;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.EvaluateMessage;
import com.meiqia.meiqiasdk.model.LeaveTipMessage;
import com.meiqia.meiqiasdk.model.PhotoMessage;
import com.meiqia.meiqiasdk.model.TextMessage;
import com.meiqia.meiqiasdk.model.VoiceMessage;
import com.meiqia.meiqiasdk.util.ErrorCode;
import com.meiqia.meiqiasdk.util.MQAudioPlayerManager;
import com.meiqia.meiqiasdk.util.MQAudioRecorderManager;
import com.meiqia.meiqiasdk.util.MQChatAdapter;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQSimpleTextWatcher;
import com.meiqia.meiqiasdk.util.MQSoundPoolManager;
import com.meiqia.meiqiasdk.util.MQTimeUtils;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQCustomKeyboardLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MQConversationActivity extends Activity implements View.OnClickListener, MQEvaluateDialog.Callback, MQCustomKeyboardLayout.Callback, View.OnTouchListener {
    private static final String TAG = MQConversationActivity.class.getSimpleName();

    public static final String CLIENT_ID = "clientId";
    public static final String CUSTOMIZED_ID = "customizedId";
    public static final String CLIENT_INFO = "clientInfo";

    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int REQUEST_CODE_PHOTO = 1;
    private static int MESSAGE_PAGE_COUNT = 30; //娑堟伅姣忛〉鍔犺浇鏁伴噺

    private MQController mController;

    // 鎺т欢
    private RelativeLayout mTitleRl;
    private RelativeLayout backRl;
    private TextView mBackTv;
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ListView mConversationListView;
    private EditText mInputEt;
    private ImageButton mSendTextBtn;
    private View mEmojiSelectBtn;
    private View mPhotoSelectBtn;
    private View mCameraSelectBtn;
    private View mVoiceBtn;
    private View mEvaluateBtn;
    private ProgressBar mLoadProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mEmojiSelectIndicator;
    private ImageView mEmojiSelectImg;
    private View mVoiceSelectIndicator;
    private ImageView mVoiceSelectImg;

    private List<BaseMessage> mChatMessageList = new ArrayList<BaseMessage>();
    private MQChatAdapter mChatMsgAdapter;
    private MessageReceiver mMessageReceiver;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    // 鏀瑰彉title鐘舵��
    private Handler mHandler;
    private MQSoundPoolManager mSoundPoolManager;

    // 鏄惁宸茬粡鍔犺浇鏁版嵁鐨勬爣璇�
    private boolean mHasLoadData = false;
    private boolean isPause;

    private Agent mCurrentAgent; // 褰撳墠瀹㈡湇

    private MQCustomKeyboardLayout mCustomKeyboardLayout;
    private MQEvaluateDialog mEvaluateDialog;
    private String mCameraPicPath;

    private String mConversationId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = MQConfig.getController(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 淇濇寔灞忓箷闀夸寒
        setContentView(R.layout.mq_activity_conversation);

        findViews();
        init();
        setListeners();
        applyCustomUIConfig();
        registerReceiver();

        mCustomKeyboardLayout.init(this, mInputEt, this);
    }

    /**
     * 濡傛灉閰嶇疆浜嗙晫闈㈢浉鍏崇殑 config锛屽湪杩欓噷搴旂敤
     */
    private void applyCustomUIConfig() {
        if (MQConfig.DEFAULT != MQConfig.ui.backArrowIconResId) {
            mBackIv.setImageResource(MQConfig.ui.backArrowIconResId);
        }

        // 澶勭悊鏍囬鏍忚儗鏅壊
        MQUtils.applyCustomUITintDrawable(mTitleRl, android.R.color.white, R.color.mq_activity_title_bg, MQConfig.ui.titleBackgroundResId);

        // 澶勭悊鏍囬銆佽繑鍥炪�佽繑鍥炵澶撮鑹�
        MQUtils.applyCustomUITextAndImageColor(R.color.mq_activity_title_textColor, MQConfig.ui.titleTextColorResId, mBackIv, mBackTv, mTitleTv);

        // 澶勭悊鏍囬鏂囨湰鐨勫鍏舵柟寮�
        MQUtils.applyCustomUITitleGravity(mBackTv, mTitleTv);

        // 澶勭悊搴曢儴鍔熻兘鎸夐挳鍥剧墖
        MQUtils.tintPressedIndicator((ImageView) findViewById(R.id.photo_select_iv), R.drawable.mq_ic_image_normal, R.drawable.mq_ic_image_active);
        MQUtils.tintPressedIndicator((ImageView) findViewById(R.id.camera_select_iv), R.drawable.mq_ic_camera_normal, R.drawable.mq_ic_camera_active);
        MQUtils.tintPressedIndicator((ImageView) findViewById(R.id.evaluate_select_iv), R.drawable.mq_ic_evaluate_normal, R.drawable.mq_ic_evaluate_active);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 璁剧疆椤惧涓婄嚎锛岃姹傚垎閰嶅鏈�
        setClientOnline();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mChatMsgAdapter != null) {
            mChatMsgAdapter.stopPlayVoice();
            MQAudioPlayerManager.release();
        }
        if (mChatMessageList != null && mChatMessageList.size() > 0) {
            mController.saveConversationOnStopTime(mChatMessageList.get(mChatMessageList.size() - 1).getCreatedOn());
        } else {
            mController.saveConversationOnStopTime(System.currentTimeMillis());
        }
    }

    @Override
    protected void onDestroy() {
        MQUtils.closeKeyboard(this);
        try {
            mSoundPoolManager.release();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            //鏈変簺鏃跺�欎細鍑虹幇鏈敞鍐屽氨鍙栨秷娉ㄥ唽鐨勬儏鍐碉紝鏆傛椂涓嶇煡閬撲负浠�涔�
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //濡傛灉鍦ㄨ〃鎯呴�夋嫨鐨勬椂鍊欐寜涓� Back 閿紝闅愯棌琛ㄦ儏 panel
        if (keyCode == KeyEvent.KEYCODE_BACK && mCustomKeyboardLayout.isEmotionKeyboardVisible()) {
            mCustomKeyboardLayout.closeEmotionKeyboard();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void init() {
        if (mController == null) {
            mController = new ControllerImpl(this);
        }
        MQTimeUtils.init(this);
        // 鍒濆鍖� ImageLoader
        MQUtils.initImageLoader(this);

        // handler
        mHandler = new Handler();

        mSoundPoolManager = MQSoundPoolManager.getInstance(this);
        mChatMsgAdapter = new MQChatAdapter(MQConversationActivity.this, mChatMessageList, mConversationListView);
        mConversationListView.setAdapter(mChatMsgAdapter);
    }

    private void findViews() {
        mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        backRl = (RelativeLayout) findViewById(R.id.back_rl);
        mBackTv = (TextView) findViewById(R.id.back_tv);
        mBackIv = (ImageView) findViewById(R.id.back_iv);
        mConversationListView = (ListView) findViewById(R.id.messages_lv);
        mInputEt = (EditText) findViewById(R.id.input_et);
        mEmojiSelectBtn = findViewById(R.id.emoji_select_btn);
        mCustomKeyboardLayout = (MQCustomKeyboardLayout) findViewById(R.id.customKeyboardLayout);
        mSendTextBtn = (ImageButton) findViewById(R.id.send_text_btn);
        mPhotoSelectBtn = findViewById(R.id.photo_select_btn);
        mCameraSelectBtn = findViewById(R.id.camera_select_btn);
        mVoiceBtn = findViewById(R.id.mic_select_btn);
        mEvaluateBtn = findViewById(R.id.evaluate_select_btn);
        mLoadProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mEmojiSelectIndicator = findViewById(R.id.emoji_select_indicator);
        mEmojiSelectImg = (ImageView) findViewById(R.id.emoji_select_img);
        mVoiceSelectIndicator = findViewById(R.id.conversation_voice_indicator);
        mVoiceSelectImg = (ImageView) findViewById(R.id.conversation_voice_img);
    }

    private void setListeners() {
        backRl.setOnClickListener(this);
        mSendTextBtn.setOnClickListener(this);
        mPhotoSelectBtn.setOnClickListener(this);
        mCameraSelectBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
        mEvaluateBtn.setOnClickListener(this);
        // 缁戝畾 EditText 鐨勭洃鍚櫒
        mInputEt.addTextChangedListener(inputTextWatcher);
        mInputEt.setOnTouchListener(this);
        mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mSendTextBtn.performClick();
                    MQUtils.closeKeyboard(MQConversationActivity.this);
                    return true;
                }
                return false;
            }
        });
        // 琛ㄦ儏
        mEmojiSelectBtn.setOnClickListener(this);
        // 瀵硅瘽鍒楄〃锛屽崟鍑汇�岄殣钘忛敭鐩樸�嶃�併�岃〃鎯� panel銆�
        mConversationListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (MotionEvent.ACTION_DOWN == arg1.getAction()) {
                    mCustomKeyboardLayout.closeAllKeyboard();
                    hideEmojiSelectIndicator();
                    hideVoiceSelectIndicator();
                }
                return false;
            }
        });
        // 娣诲姞闀挎寜澶嶅埗鍔熻兘
        mConversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String content = mChatMessageList.get(arg2).getContent();
                if (!TextUtils.isEmpty(content)) {
                    MQUtils.clip(MQConversationActivity.this, content);
                    MQUtils.show(MQConversationActivity.this, R.string.mq_copy_success);
                    return true;
                }
                return false;
            }
        });
        // 涓嬫媺鍒锋柊
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (MQConfig.isLoadMessagesFromNativeOpen) {
                    loadMoreDataFromDatabase();
                } else {
                    loadMoreDataFromService();
                }
            }
        });
    }

    /**
     * 娉ㄥ唽骞挎挱
     */
    private void registerReceiver() {
        // 娉ㄥ唽娑堟伅鎺ユ敹
        mMessageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQController.ACTION_AGENT_INPUTTING);
        intentFilter.addAction(MQController.ACTION_NEW_MESSAGE_RECEIVED);
        intentFilter.addAction(MQController.ACTION_CLIENT_IS_REDIRECTED_EVENT);
        intentFilter.addAction(MQController.ACTION_INVITE_EVALUATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);

        // 缃戠粶鐩戝惉
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, mFilter);
    }

    /**
     * 灏� title 鏀逛负瀹㈡湇鍚嶅瓧
     *
     * @param agentName 瀹㈡湇鍚�
     */
    protected void changeTitleToAgentName(String agentName) {
        mTitleTv.setText(agentName);
    }

    /**
     * 灏� title 鏀逛负瀹㈡湇鍚嶅瓧
     *
     * @param agent 瀹㈡湇瀹炰綋
     */
    protected void changeTitleToAgentName(Agent agent) {
        if (agent != null) {
            mTitleTv.setText(agent.getNickname());
        } else {
            changeTitleToNoAgentState();
        }
    }

    /**
     * 灏� title 鏀逛负 姝ｅ湪杈撳叆
     */
    protected void changeTitleToInputting() {
        mTitleTv.setText(getResources().getString(R.string.mq_title_inputting));
    }

    /**
     * 灏� title 鏀逛负 姝ｅ湪鍒嗛厤瀹㈡湇
     */
    protected void changeTitleToAllocatingAgent() {
        mTitleTv.setText(getResources().getString(R.string.mq_allocate_agent));
    }

    /**
     * 灏� title 鏀逛负娌℃湁瀹㈡湇鐨勭姸鎬�
     */
    protected void changeTitleToNoAgentState() {
        mTitleTv.setText(getResources().getString(R.string.mq_title_leave_msg));
        mEvaluateBtn.setVisibility(View.GONE);
    }

    /**
     * 灏� title 鏀逛负娌℃湁缃戠粶鐘舵��
     */
    protected void changeTitleToNetErrorState() {
        mTitleTv.setText(getResources().getString(R.string.mq_title_net_not_work));
    }

    /**
     * 灏� title 鏀逛负鏈煡閿欒鐘舵��
     */
    protected void changeTitleToUnknownErrorState() {
        mTitleTv.setText(getResources().getString(R.string.mq_title_unknown_error));
    }

    /**
     * 娣诲姞 杞帴瀹㈡湇 鐨勬秷鎭� Tip 鍒板垪琛�
     *
     * @param agentNickName 瀹㈡湇鍚嶅瓧
     */
    protected void addDirectAgentMessageTip(String agentNickName) {
        mTitleTv.setText(agentNickName);
        AgentChangeMessage agentChangeMessage = new AgentChangeMessage();
        agentChangeMessage.setAgentNickname(agentNickName);
        mChatMsgAdapter.addMQMessage(agentChangeMessage);
    }

    private boolean isAddLeaveTip;

    /**
     * 娣诲姞 鐣欒█ 鐨� Tip
     */
    protected void addLeaveMessageTip() {
        mEvaluateBtn.setVisibility(View.GONE);
        if (!isAddLeaveTip) {
            changeTitleToNoAgentState();
            LeaveTipMessage leaveTip = new LeaveTipMessage();
            //娣诲姞鍒板綋鍓嶆秷鎭殑涓婁竴涓綅缃�
            int position = mChatMessageList.size();
            if (position != 0) {
                position = position - 1;
            }
            mChatMsgAdapter.addMQMessage(leaveTip, position);
            isAddLeaveTip = true;
        }
    }

    /**
     * 浠庡垪琛ㄧЩ闄� 鐣欒█ 鐨� Tip
     */
    protected void removeLeaveMessageTip() {
        mEvaluateBtn.setVisibility(View.VISIBLE);
        Iterator<BaseMessage> chatItemViewBaseIterator = mChatMessageList.iterator();
        while (chatItemViewBaseIterator.hasNext()) {
            BaseMessage baseMessage = chatItemViewBaseIterator.next();
            if (baseMessage.getItemViewType() == BaseMessage.TYPE_TIP) {
                chatItemViewBaseIterator.remove();
                mChatMsgAdapter.notifyDataSetChanged();
                return;
            }
        }
        isAddLeaveTip = false;
    }


    private void setCurrentAgent(Agent agent) {
        mCurrentAgent = agent;
    }

    /**
     * 浠庢湇鍔″櫒鑾峰彇鏇村娑堟伅骞跺姞杞�
     */
    private void loadMoreDataFromService() {
        // 鏈�鏃╂秷鎭殑鍒涘缓鏃堕棿
        long lastMessageCreateOn = System.currentTimeMillis();
        if (mChatMessageList.size() > 0)
            lastMessageCreateOn = mChatMessageList.get(0).getCreatedOn();
        // 鑾峰彇璇ユ椂闂翠箣鍓嶇殑娑堟伅
        mController.getMessageFromService(lastMessageCreateOn, MESSAGE_PAGE_COUNT, new OnGetMessageListCallBack() {
            @Override
            public void onSuccess(final List<BaseMessage> messageList) {
                // 鏍规嵁璁剧疆锛岃繃婊よ闊虫秷鎭�
                cleanVoiceMessage(messageList);
                //娣诲姞鏃堕棿鎴�
                MQTimeUtils.refreshMQTimeItem(messageList);
                mChatMsgAdapter.loadMoreMessage(cleanDupMessages(mChatMessageList, messageList));
                mConversationListView.setSelection(messageList.size());
                mSwipeRefreshLayout.setRefreshing(false);
                // 娌℃湁娑堟伅鍚庯紝绂佹涓嬫媺鍔犺浇
                if (messageList.size() == 0) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onFailure(int code, String responseString) {
                mChatMsgAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 浠庢暟鎹簱鏇村娑堟伅骞跺姞杞�
     */
    private void loadMoreDataFromDatabase() {
        // 鏈�鏃╂秷鎭殑鍒涘缓鏃堕棿
        long lastMessageCreateOn = System.currentTimeMillis();
        if (mChatMessageList.size() > 0)
            lastMessageCreateOn = mChatMessageList.get(0).getCreatedOn();
        // 鑾峰彇璇ユ椂闂翠箣鍓嶇殑娑堟伅
        mController.getMessagesFromDatabase(lastMessageCreateOn, MESSAGE_PAGE_COUNT, new OnGetMessageListCallBack() {
            @Override
            public void onSuccess(final List<BaseMessage> messageList) {
                // 鏍规嵁璁剧疆锛岃繃婊よ闊虫秷鎭�
                cleanVoiceMessage(messageList);
                //娣诲姞鏃堕棿鎴�
                MQTimeUtils.refreshMQTimeItem(messageList);
                mChatMsgAdapter.loadMoreMessage(cleanDupMessages(mChatMessageList, messageList));
                mConversationListView.setSelection(messageList.size());
                mSwipeRefreshLayout.setRefreshing(false);
                // 娌℃湁娑堟伅鍚庯紝绂佹涓嬫媺鍔犺浇
                if (messageList.size() == 0) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onFailure(int code, String responseString) {
                mChatMsgAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 杩囨护鎺夊垪琛ㄥ瓨鍦ㄧ殑娑堟伅
     *
     * @param messageList    鍒楄〃涓殑娑堟伅
     * @param newMessageList 鍔犺浇鐨勬柊娑堟伅
     * @return
     */
    private List<BaseMessage> cleanDupMessages(List<BaseMessage> messageList, List<BaseMessage> newMessageList) {
        Iterator<BaseMessage> iterator = newMessageList.iterator();
        while (iterator.hasNext()) {
            BaseMessage newMessage = iterator.next();
            if (messageList.contains(newMessage)) {
                iterator.remove();
            }
        }
        return newMessageList;
    }

    /**
     * 璁剧疆椤惧涓婄嚎
     */
    private void setClientOnline() {
        if (mCurrentAgent == null) {
            // Title 鏄剧ず姝ｅ湪鍒嗛厤瀹㈡湇
            changeTitleToAllocatingAgent();

            // 浠� intent 鑾峰彇 clientId銆乧ustomizedId 鍜� clientInfo
            Intent intent = getIntent();
            String clientId = null;
            String customizedId = null;
            HashMap<String, String> clientInfo = null;
            if (intent != null) {
                clientId = getIntent().getStringExtra(CLIENT_ID);
                customizedId = getIntent().getStringExtra(CUSTOMIZED_ID);
                Serializable clientInfoSerializable = getIntent().getSerializableExtra(CLIENT_INFO);
                if (clientInfoSerializable != null) {
                    clientInfo = (HashMap<String, String>) clientInfoSerializable;
                }
            }
            final HashMap<String, String> finalClientInfo = clientInfo;

            // 涓婄嚎
            mController.setCurrentClientOnline(clientId, customizedId, new OnClientOnlineCallback() {

                @Override
                public void onSuccess(Agent agent, String conversationId, List<BaseMessage> conversationMessageList) {
                    setCurrentAgent(agent);
                    changeTitleToAgentName(agent);
                    removeLeaveMessageTip();
                    mConversationId = conversationId;
                    mMessageReceiver.setConversationId(conversationId);

                    // 鏍规嵁璁剧疆锛岃繃婊よ闊虫秷鎭�
                    cleanVoiceMessage(conversationMessageList);

                    //鍔犺浇鏁版嵁
                    mChatMessageList.clear();
                    mChatMessageList.addAll(conversationMessageList);
                    loadData();
                    onLoadDataComplete(MQConversationActivity.this, agent);

                    // 涓婄嚎鎴愬姛鍚庯紝鏍规嵁璁剧疆鏄惁涓婁紶椤惧淇℃伅
                    if (finalClientInfo != null) {
                        mController.setClientInfo(finalClientInfo, null);
                    }
                }

                @Override
                public void onFailure(int code, String message) {
                    if (ErrorCode.NET_NOT_WORK == code) {
                        changeTitleToNetErrorState();
                    } else if (ErrorCode.NO_AGENT_ONLINE == code) {
                        setCurrentAgent(null);
                        changeTitleToNoAgentState();
                    } else {
                        changeTitleToUnknownErrorState();
                        Toast.makeText(MQConversationActivity.this, "code = " + code + "\n" + "message = " + message, Toast.LENGTH_SHORT).show();
                    }
                    //濡傛灉娌℃湁鍔犺浇鏁版嵁锛屽垯鍔犺浇鏁版嵁
                    if (!mHasLoadData) {
                        getMessageDataFromDatabaseAndLoad();
                        onLoadDataComplete(MQConversationActivity.this, null);
                    }
                }
            });
        } else {
            changeTitleToAgentName(mCurrentAgent);
        }
    }

    /**
     * 浠庢暟鎹簱鑾峰彇娑堟伅骞跺姞杞�
     */
    private void getMessageDataFromDatabaseAndLoad() {
        // 浠庢暟鎹簱鑾峰彇鏁版嵁
        mController.getMessagesFromDatabase(System.currentTimeMillis(), MESSAGE_PAGE_COUNT, new OnGetMessageListCallBack() {

            @Override
            public void onSuccess(List<BaseMessage> messageList) {
                // 鏍规嵁璁剧疆锛岃繃婊よ闊虫秷鎭�
                cleanVoiceMessage(messageList);

                mChatMessageList.addAll(messageList);
                loadData();
            }

            @Override
            public void onFailure(int code, String responseString) {

            }
        });
    }

    /**
     * 鍔犺浇娑堟伅鍒板垪琛ㄤ腑
     */
    private void loadData() {
        // 娣诲姞TimeItem
        MQTimeUtils.refreshMQTimeItem(mChatMessageList);
        // 鍔犺浇鍒癠I
        mLoadProgressBar.setVisibility(View.GONE);
        // 灏嗘鍦ㄥ彂閫佹樉绀轰负宸插彂閫�
        for (BaseMessage message : mChatMessageList) {
            if (BaseMessage.STATE_SENDING.equals(message.getStatus())) {
                message.setStatus(BaseMessage.STATE_ARRIVE);
            }
        }
        MQUtils.scrollListViewToBottom(mConversationListView);
        mChatMsgAdapter.downloadAndNotifyDataSetChanged(mChatMessageList);
        mChatMsgAdapter.notifyDataSetChanged();
        mHasLoadData = true;
    }

    /**
     * 鏁版嵁鍔犺浇瀹屾垚鍚庣殑鍥炶皟
     *
     * @param mqConversationActivity 褰撳墠 Activity
     * @param agent                  褰撳墠瀹㈡湇锛屽彲鑳戒负 null
     */
    protected void onLoadDataComplete(MQConversationActivity mqConversationActivity, Agent agent) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_rl) {
            // 杩斿洖鎸夐挳

            onBackPressed();
        } else if (id == R.id.emoji_select_btn) {
            // 琛ㄦ儏鎸夐挳

            if (mCustomKeyboardLayout.isEmotionKeyboardVisible()) {
                hideEmojiSelectIndicator();
            } else {
                showEmojiSelectIndicator();
            }

            hideVoiceSelectIndicator();

            mCustomKeyboardLayout.toggleEmotionOriginKeyboard();
        } else if (id == R.id.send_text_btn) {
            // 鍙戦�佹寜閽�

            if (!mHasLoadData) {
                MQUtils.show(this, R.string.mq_data_is_loading);
                return;
            }

            createAndSendTextMessage();

        } else if (id == R.id.photo_select_btn) {
            // 閫夋嫨鍥剧墖
            hideEmojiSelectIndicator();
            hideVoiceSelectIndicator();
            chooseFromPhotoPicker();
        } else if (id == R.id.camera_select_btn) {
            // 鎵撳紑鐩告満
            hideEmojiSelectIndicator();
            hideVoiceSelectIndicator();
            choosePhotoFromCamera();
        } else if (id == R.id.mic_select_btn) {
            if (mCustomKeyboardLayout.isVoiceKeyboardVisible()) {
                hideVoiceSelectIndicator();
            } else {
                showVoiceSelectIndicator();
            }

            hideEmojiSelectIndicator();

            mCustomKeyboardLayout.toggleVoiceOriginKeyboard();
        } else if (id == R.id.evaluate_select_btn) {
            hideEmojiSelectIndicator();
            hideVoiceSelectIndicator();
            showEvaluateDialog();
        }
    }

    private void showEvaluateDialog() {
        // 濡傛灉娌℃湁姝ｅ湪褰曢煶鎵嶅脊鍑鸿瘎浠峰璇濇
        if (!mCustomKeyboardLayout.isRecording()) {
            mCustomKeyboardLayout.closeAllKeyboard();
            if (!TextUtils.isEmpty(mConversationId)) {
                if (mEvaluateDialog == null) {
                    mEvaluateDialog = new MQEvaluateDialog(this);
                    mEvaluateDialog.setCallback(this);
                }
                mEvaluateDialog.show();
            }
        }
    }

    private void showEmojiSelectIndicator() {
        mEmojiSelectIndicator.setVisibility(View.VISIBLE);
        mEmojiSelectImg.setImageResource(R.drawable.mq_ic_emoji_active);
        mEmojiSelectImg.setColorFilter(getResources().getColor(R.color.mq_indicator_selected));
    }

    private void hideEmojiSelectIndicator() {
        mEmojiSelectIndicator.setVisibility(View.GONE);
        mEmojiSelectImg.setImageResource(R.drawable.mq_ic_emoji_normal);
        mEmojiSelectImg.clearColorFilter();
    }

    private void showVoiceSelectIndicator() {
        mVoiceSelectIndicator.setVisibility(View.VISIBLE);
        mVoiceSelectImg.setImageResource(R.drawable.mq_ic_mic_active);
        mVoiceSelectImg.setColorFilter(getResources().getColor(R.color.mq_indicator_selected));
    }

    private void hideVoiceSelectIndicator() {
        mVoiceSelectIndicator.setVisibility(View.GONE);
        mVoiceSelectImg.setImageResource(R.drawable.mq_ic_mic_normal);
        mVoiceSelectImg.clearColorFilter();
    }


    /**
     * 浠庢湰鍦伴�夋嫨鍥剧墖
     */
    private void chooseFromPhotoPicker() {
        if (!mHasLoadData) {
            MQUtils.show(this, R.string.mq_data_is_loading);
            return;
        }

        startActivityForResult(MQPhotoPickerActivity.newIntent(this, null, 6, null, getString(R.string.mq_send)), REQUEST_CODE_PHOTO);
    }


    /**
     * 鎵撳紑鐩告満
     */
    private void choosePhotoFromCamera() {
        if (!mHasLoadData) {
            MQUtils.show(this, R.string.mq_data_is_loading);
            return;
        }

        MQUtils.closeKeyboard(MQConversationActivity.this);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(MQUtils.getPicStorePath(this));
        file.mkdirs();
        String path = MQUtils.getPicStorePath(this) + "/" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(path);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        mCameraPicPath = path;
        try {
            startActivityForResult(camera, MQConversationActivity.REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            MQUtils.show(this, R.string.mq_photo_not_support);
        }
    }

    /**
     * 鍒涘缓骞跺彂閫乀extMessage銆傚鏋滄病鏈夊鏈嶅湪绾匡紝鍙戦�佺绾挎秷鎭�
     */
    private void createAndSendTextMessage() {
        String msg = mInputEt.getText().toString();
        //鍐呭涓虹┖涓嶅彂閫侊紝鍙湁绌烘牸鏃朵篃涓嶅彂閫�
        if (TextUtils.isEmpty(msg.trim())) {
            return;
        }
        sendMessage(new TextMessage(msg));
    }

    /**
     * 鍒涘缓骞跺彂閫両mageMessage
     *
     * @param imageFile 闇�瑕佷笂浼犵殑imageFile
     */
    private void createAndSendImageMessage(File imageFile) {
        PhotoMessage imageMessage = new PhotoMessage();
        imageMessage.setLocalPath(imageFile.getAbsolutePath());
        sendMessage(imageMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                // 浠� 鐩告満 鑾峰彇鐨勫浘鐗�

                File cameraPicFile = getCameraPicFile();
                if (cameraPicFile != null) {
                    createAndSendImageMessage(cameraPicFile);
                }
            } else if (requestCode == REQUEST_CODE_PHOTO) {
                // 浠� 鐩稿唽 鑾峰彇鐨勫浘鐗�

                ArrayList<String> selectedPhotos = MQPhotoPickerActivity.getSelectedImages(data);
                for (String photoPath : selectedPhotos) {
                    createAndSendImageMessage(new File(photoPath));
                }
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // 濡傛灉褰撳墠绯荤粺涓病鏈夐偖浠跺鎴风鍙緵璋冪敤锛岀▼搴忎細鐩存帴鎸傛帀锛岀郴缁熸姏鍑轰簡ActivityNotFoundException
        if (intent.toString().contains("mailto")) {
            PackageManager pm = getPackageManager();
            // The first Method
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            if (activities == null || activities.size() == 0) {
                // Do anything you like, or just return
                return;
            }
        }
        super.startActivity(intent);
    }

    public File getCameraPicFile() {
        String sdState = Environment.getExternalStorageState();
        if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File imageFile = new File(mCameraPicPath);
        if (imageFile.exists()) {
            return imageFile;
        } else {
            return null;
        }
    }

    /**
     * 妫�鏌ュ彂閫佹潯浠跺苟涓斿鐞嗕竴浜涘噯澶囧彂閫佺殑鐘舵��
     *
     * @param message 寰呭彂閫佹秷鎭�
     * @return true锛屽彲浠ュ彂閫侊紱false锛屼笉鑳藉彂閫�
     */
    private boolean checkAndPreSend(BaseMessage message) {
        // 鏁版嵁杩樻病鏈夊姞杞界殑鏃跺��
        if (mChatMsgAdapter == null) {
            return false;
        }
        // 鐘舵�佹敼涓恒�屾鍦ㄥ彂閫併�嶏紝浠ヤ究鍦ㄦ暟鎹垪琛ㄤ腑灞曠ず姝ｅ湪鍙戦�佹秷鎭殑鐘舵��
        message.setStatus(BaseMessage.STATE_SENDING);
        // 娣诲姞鍒板璇濆垪琛�
        mChatMessageList.add(message);
        mInputEt.setText("");
        MQTimeUtils.refreshMQTimeItem(mChatMessageList);
        mChatMsgAdapter.notifyDataSetChanged();
        return true;
    }

    /**
     * 鍙戦�佹秷鎭�
     *
     * @param message 娑堟伅
     */
    public void sendMessage(final BaseMessage message) {
        boolean isPreSendSuc = checkAndPreSend(message);
        if (!isPreSendSuc) {
            return;
        }

        // 寮�濮嬪彂閫�
        mController.sendMessage(message, new OnMessageSendCallback() {
            @Override
            public void onSuccess(BaseMessage message, int state) {
                renameVoiceFilename(message);

                // 鍒锋柊鐣岄潰
                mChatMsgAdapter.notifyDataSetChanged();

                // 瀹㈡湇涓嶅湪绾跨殑鏃跺�欙紝浼氳嚜鍔ㄥ彂閫佺暀瑷�娑堟伅锛岃繖涓椂鍊欒娣诲姞涓�涓� tip 鍒板垪琛�
                if (ErrorCode.NO_AGENT_ONLINE == state) {
                    addLeaveMessageTip();
                }
                // 鍙戦�佹垚鍔熸挱鏀惧０闊�
                if (MQConfig.isSoundSwitchOpen) {
                    mSoundPoolManager.playSound(R.raw.mq_send_message); // 
                }
            }

            @Override
            public void onFailure(BaseMessage failureMessage, int code, String failureInfo) {
                mChatMsgAdapter.notifyDataSetChanged();
            }
        });
        MQUtils.scrollListViewToBottom(mConversationListView);
    }

    /**
     * 閲嶅彂娑堟伅
     *
     * @param message 寰呴噸鍙戠殑娑堟伅
     */
    public void resendMessage(final BaseMessage message) {
        // 寮�濮嬮噸鍙�
        mController.resendMessage(message, new OnMessageSendCallback() {
            @Override
            public void onSuccess(BaseMessage message, int state) {
                renameVoiceFilename(message);

                // 鍒锋柊鐣岄潰
                mChatMsgAdapter.notifyDataSetChanged();

                // 瀹㈡湇涓嶅湪绾跨殑鏃跺�欙紝浼氳嚜鍔ㄥ彂閫佺暀瑷�娑堟伅锛岃繖涓椂鍊欒娣诲姞涓�涓� tip 鍒板垪琛�
                if (ErrorCode.NO_AGENT_ONLINE == state) {
                    addLeaveMessageTip();
                }
            }

            @Override
            public void onFailure(BaseMessage failureMessage, int code, String failureInfo) {
                mChatMsgAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 閲嶅懡鍚嶆湰鍦拌闊虫枃浠�
     *
     * @param message
     */
    private void renameVoiceFilename(BaseMessage message) {
        if (message instanceof VoiceMessage) {
            VoiceMessage voiceMessage = (VoiceMessage) message;
            MQAudioRecorderManager.renameVoiceFilename(MQConversationActivity.this, voiceMessage.getLocalPath(), voiceMessage.getContent());
            mChatMsgAdapter.downloadAndNotifyDataSetChanged(Arrays.asList(message));
        }
    }

    // 鐩戝惉EditText杈撳叆妗嗘暟鎹埌鍙樺寲
    private TextWatcher inputTextWatcher = new MQSimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 鍚戞湇鍔″櫒鍙戦�佷竴涓鍦ㄨ緭鍏ョ殑鍑芥暟
            if (!TextUtils.isEmpty(s)) {
                inputting(s.toString());
//zanshizhushi
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mSendTextBtn.setElevation(MQUtils.dip2px(MQConversationActivity.this, 3));
//                }
                mSendTextBtn.setImageResource(R.drawable.mq_ic_send_icon_white);
                mSendTextBtn.setBackgroundResource(R.drawable.mq_shape_send_back_pressed);
            } else {

//zanshizhushi
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mSendTextBtn.setElevation(0);
//                }
                mSendTextBtn.setImageResource(R.drawable.mq_ic_send_icon_grey);
                mSendTextBtn.setBackgroundResource(R.drawable.mq_shape_send_back_normal);
            }
        }
    };

    /**
     * 鍚戞湇鍔″櫒鍙戦�併�岄【瀹㈡鍦ㄨ緭鍏ャ�嶇殑鐘舵��
     *
     * @param content 鍐呭
     */
    private void inputting(String content) {
        mController.sendClientInputtingWithContent(content);
    }

    /**
     * 杩囨护璇煶娑堟伅
     *
     * @param messageList 娑堟伅鍒楄〃
     */
    private void cleanVoiceMessage(List<BaseMessage> messageList) {
        if (!MQConfig.isVoiceSwitchOpen && messageList.size() > 0) {
            Iterator<BaseMessage> baseMessageIterator = messageList.iterator();
            while (baseMessageIterator.hasNext()) {
                BaseMessage baseMessage = baseMessageIterator.next();
                if (BaseMessage.TYPE_CONTENT_VOICE.equals(baseMessage.getContentType())) {
                    baseMessageIterator.remove();
                }
            }
        }
    }

    /**
     * 娣诲姞 璇勪环 鐨勬秷鎭� Tip 鍒板垪琛�
     *
     * @param level 璇勪环鐨勭瓑绾�
     */
    protected void addEvaluateMessageTip(int level, String content) {
        EvaluateMessage evaluateMessage = new EvaluateMessage(level, content);
        mChatMsgAdapter.addMQMessage(evaluateMessage);
    }

    @Override
    public void executeEvaluate(final int level, final String content) {
        mController.executeEvaluate(mConversationId, level, content, new SimpleCallback() {
            @Override
            public void onFailure(int code, String message) {
                MQUtils.show(MQConversationActivity.this, R.string.mq_evaluate_failure);
            }

            @Override
            public void onSuccess() {
                addEvaluateMessageTip(level, content);
            }
        });
    }

    @Override
    public void onAudioRecorderFinish(int time, String filePath) {
        VoiceMessage voiceMessage = new VoiceMessage();
        voiceMessage.setDuration(time);
        voiceMessage.setLocalPath(filePath);
        sendMessage(voiceMessage);
    }

    @Override
    public void onAudioRecorderTooShort() {
        MQUtils.show(this, R.string.mq_record_record_time_is_short);
    }

    @Override
    public void scrollContentToBottom() {
        MQUtils.scrollListViewToBottom(mConversationListView);
    }

    @Override
    public void onAudioRecorderNoPermission() {
        MQUtils.show(this, R.string.mq_recorder_no_permission);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideEmojiSelectIndicator();
        hideVoiceSelectIndicator();
        return false;
    }

    private class MessageReceiver extends com.meiqia.meiqiasdk.controller.MessageReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }

        @Override
        public void receiveNewMsg(BaseMessage message) {
            MQConversationActivity.this.receiveNewMsg(message);
        }

        @Override
        public void changeTitleToInputting() {
            MQConversationActivity.this.changeTitleToInputting();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MQConversationActivity.this.changeTitleToAgentName(mCurrentAgent);
                }
            }, 2000);

        }

        @Override
        public void changeTitleToAgentName(String agentNickname) {
            MQConversationActivity.this.changeTitleToAgentName(agentNickname);
        }

        @Override
        public void addDirectAgentMessageTip(String agentNickname) {
            MQConversationActivity.this.addDirectAgentMessageTip(agentNickname);
        }

        @Override
        public void setCurrentAgent(Agent agent) {
            MQConversationActivity.this.setCurrentAgent(agent);
        }

        @Override
        public void inviteEvaluation() {
            showEvaluateDialog();
        }

        @Override
        public void setNewConversationId(String newConversationId) {
            mConversationId = newConversationId;
            removeLeaveMessageTip();
        }
    }

    /**
     * 澶勭悊鏀跺埌鐨勬柊娑堟伅
     *
     * @param baseMessage 鏂版秷鎭�
     */
    private void receiveNewMsg(BaseMessage baseMessage) {
        if (mChatMsgAdapter != null && !isDupMessage(baseMessage)) {
            // 濡傛灉鏄厤缃簡涓嶆樉绀鸿闊筹紝鏀跺埌璇煶娑堟伅鐩存帴杩囨护
            if (!MQConfig.isVoiceSwitchOpen && BaseMessage.TYPE_CONTENT_VOICE.equals(baseMessage.getContentType())) {
                return;
            }

            mChatMessageList.add(baseMessage);
            MQTimeUtils.refreshMQTimeItem(mChatMessageList);

            if (baseMessage instanceof VoiceMessage) {
                mChatMsgAdapter.downloadAndNotifyDataSetChanged(Arrays.asList(baseMessage));
            } else {
                mChatMsgAdapter.notifyDataSetChanged();
            }

            int lastVisiblePosition = mConversationListView.getLastVisiblePosition();
            // -2 鍥犱负鏄厛娣诲姞
            if (lastVisiblePosition == (mChatMsgAdapter.getCount() - 2)) {
                MQUtils.scrollListViewToBottom(mConversationListView);
            }
            // 鍦ㄧ晫闈腑鎾斁澹伴煶
            if (!isPause && MQConfig.isSoundSwitchOpen) {
                mSoundPoolManager.playSound(R.raw.mq_new_message);
            }
        }

    }

    /**
     * 娑堟伅鏄惁宸茬粡鍦ㄥ垪琛ㄤ腑
     *
     * @param baseMessage
     * @return true锛屽凡缁忓瓨鍦ㄤ笌鍒楄〃锛沠alse锛屼笉瀛樺湪
     */
    private boolean isDupMessage(BaseMessage baseMessage) {
        for (BaseMessage message : mChatMessageList) {
            if (message.equals(baseMessage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 鐩戝惉缃戠粶
     */
    private class NetworkChangeReceiver extends BroadcastReceiver {

        private ConnectivityManager connectivityManager;
        // 绗竴娆¤繘鍏ョ殑鏃跺�欙紝浼氱珛鍗虫敹鍒板箍鎾紝闇�瑕侀伩鍏嶄互涓�
        private boolean isFirstReceiveBroadcast = true;

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (!isFirstReceiveBroadcast) {
                    // 鏈夌綉缁�
                    if (info != null && info.isAvailable()) {
                        // 鏂綉鍚庯紝杩斿洖閲嶆柊杩涘叆锛� 鍙堟湁缃戜簡鍒锋柊 Agent
                        mCurrentAgent = mController.getCurrentAgent();
                        changeTitleToAgentName(mCurrentAgent);
                    }
                    // 娌℃湁缃戠粶
                    else {
                        changeTitleToNetErrorState();
                    }
                } else {
                    isFirstReceiveBroadcast = false;
                }
            }
        }

    }

    private boolean isSdcardAvailable() {
        boolean isSdcardAvailable = MQUtils.isSdcardAvailable();
        if (!isSdcardAvailable) MQUtils.show(MQConversationActivity.this, R.string.mq_no_sdcard);
        return isSdcardAvailable;
    }

}

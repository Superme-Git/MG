package com.meiqia.meiqiasdk.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.PhotoMessage;
import com.meiqia.meiqiasdk.model.TextMessage;
import com.meiqia.meiqiasdk.model.VoiceMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MQUtils {
    /**
     * 閿洏鍒囨崲寤舵椂鏃堕棿
     */
    public static final int KEYBOARD_CHANGE_DELAY = 300;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

    public static void runInUIThread(Runnable task) {
        sHandler.post(task);
    }

    public static void runInUIThread(Runnable task, long delayMillis) {
        sHandler.postDelayed(task, delayMillis);
    }

    public static BaseMessage parseMQMessageIntoBaseMessage(MQMessage message, BaseMessage baseMessage) {
        int itemType;
        if (MQMessage.TYPE_FROM_CLIENT.equals(message.getFrom_type())) {
            itemType = BaseMessage.TYPE_CLIENT;
        } else {
            itemType = BaseMessage.TYPE_AGENT;
        }
        baseMessage.setStatus(message.getStatus());
        baseMessage.setItemViewType(itemType);
        baseMessage.setContent(message.getContent());
        baseMessage.setContentType(message.getContent_type());
        baseMessage.setStatus(message.getStatus());
        baseMessage.setId(message.getId());
        baseMessage.setType(message.getType());
        baseMessage.setAgentNickname(message.getAgent_nickname());
        baseMessage.setCreatedOn(message.getCreated_on());
        baseMessage.setAvatar(message.getAvatar());
        baseMessage.setIsRead(message.is_read());
        if (MQMessage.TYPE_CONTENT_PHOTO.equals(message.getContent_type())) {
            ((PhotoMessage) baseMessage).setUrl(message.getMedia_url());
        } else if (MQMessage.TYPE_CONTENT_VOICE.equals(message.getContent_type())) {
            ((VoiceMessage) baseMessage).setUrl(message.getMedia_url());
        }
        return baseMessage;
    }

    public static BaseMessage parseMQMessageToBaseMessage(MQMessage message) {
        BaseMessage baseMessage;
        int itemType;
        if (MQMessage.TYPE_FROM_CLIENT.equals(message.getFrom_type())) {
            itemType = BaseMessage.TYPE_CLIENT;
        } else {
            itemType = BaseMessage.TYPE_AGENT;
        }
        if (MQMessage.TYPE_CONTENT_PHOTO.equals(message.getContent_type())) {
            // message.getMedia_url() 鍙兘鏄湰鍦拌矾寰�
            baseMessage = new PhotoMessage();
            if (isLocalPath(message.getMedia_url())) {
                ((PhotoMessage) baseMessage).setLocalPath(message.getMedia_url());
            } else {
                ((PhotoMessage) baseMessage).setUrl(message.getMedia_url());
            }
            baseMessage.setContent("[photo]");
        } else if (MQMessage.TYPE_CONTENT_VOICE.equals(message.getContent_type())) {
            baseMessage = new VoiceMessage(message.getMedia_url());
            // message.getMedia_url() 鍙兘鏄湰鍦拌矾寰�
            if (isLocalPath(message.getMedia_url())) {
                ((VoiceMessage) baseMessage).setLocalPath(message.getMedia_url());
            } else {
                ((VoiceMessage) baseMessage).setUrl(message.getMedia_url());
            }
            baseMessage.setContent("[voice]");
        } else {
            baseMessage = new TextMessage(message.getContent());
            baseMessage.setContent(message.getContent());
        }
        baseMessage.setStatus(message.getStatus());
        baseMessage.setItemViewType(itemType);
        baseMessage.setContentType(message.getContent_type());
        baseMessage.setType(message. getType());
        baseMessage.setStatus(message.getStatus());
        baseMessage.setId(message.getId());
        baseMessage.setAgentNickname(message.getAgent_nickname());
        baseMessage.setCreatedOn(message.getCreated_on());
        baseMessage.setAvatar(message.getAvatar());
        baseMessage.setIsRead(message.is_read());
        return baseMessage;
    }

    /**
     * 灏� MQMessage 杞崲涓� BaseMessage
     *
     * @param mqMessageList 寰呰浆鎹㈢殑娑堟伅
     * @return 杞崲鍚庣殑娑堟伅
     */
    public static List<BaseMessage> parseMQMessageToChatBaseList(List<MQMessage> mqMessageList) {
        List<BaseMessage> baseMessages = new ArrayList<BaseMessage>();
        for (MQMessage mqMessage : mqMessageList) {
            baseMessages.add(parseMQMessageToBaseMessage(mqMessage));
        }
        return baseMessages;
    }

    public static Agent parseMQAgentToAgent(MQAgent mqAgent) {
        if (mqAgent == null) return null;
        Agent agent = new Agent();
        agent.setId(mqAgent.getId());
        agent.setNickname(mqAgent.getNickname());
        return agent;
    }

    private static boolean isLocalPath(String path) {
        return !TextUtils.isEmpty(path) && !path.startsWith("http");
    }

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @param context
     * @param bitmap
     * @param cornerRadius
     * @return
     */
    public static RoundedBitmapDrawable getRoundedDrawable(Context context, Bitmap bitmap, float cornerRadius) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        return roundedBitmapDrawable;
    }

    /**
     * 澶勭悊鑷畾涔夊浘鐗囧拰鏂囧瓧棰滆壊
     *
     * @param resourceResId 閫氳繃璧勬簮鏂囦欢id鐨勫舰寮忚嚜瀹氫箟鐨刬d
     * @param codeResId     閫氳繃java浠ｇ爜鐨勬柟寮忚嚜瀹氫箟鐨刬d
     * @param iconIv        瑕佹敼鍙榯int棰滆壊鐨勫浘鐗囨帶浠�
     * @param textViews     瑕佹敼鍙樻枃瀛楅鑹茬殑鏂囨湰鎺т欢
     */
    public static void applyCustomUITextAndImageColor(int resourceResId, int codeResId, ImageView iconIv, TextView... textViews) {
        Context context = null;
        if (iconIv != null) {
            context = iconIv.getContext();
        }
        if (textViews != null && textViews.length > 0) {
            context = textViews[0].getContext();
        }

        if (context != null) {
            if (MQConfig.DEFAULT != codeResId) {
                resourceResId = codeResId;
            }

            int color = context.getResources().getColor(resourceResId);
            if (iconIv != null) {
                iconIv.setColorFilter(color);
            }
            if (textViews != null) {
                for (TextView textView : textViews) {
                    textView.setTextColor(color);
                }
            }
        }
    }

    /**
     * 澶勭悊鑷畾涔夋爣棰樻枃鏈鍏舵柟寮�
     *
     * @param backTv  杩斿洖鏂囨湰鎺т欢
     * @param titleTv 鏍囬鏂囨湰鎺т欢
     */
    public static void applyCustomUITitleGravity(TextView backTv, TextView titleTv) {
        if (MQConfig.ui.MQTitleGravity.LEFT == MQConfig.ui.titleGravity) {
            RelativeLayout.LayoutParams titleTvParams = (RelativeLayout.LayoutParams) titleTv.getLayoutParams();
            titleTvParams.addRule(RelativeLayout.RIGHT_OF, R.id.back_rl);
            titleTv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            if (backTv != null) {
                backTv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 澶勭悊鑷畾涔夊浘鐗囪儗鏅壊
     *
     * @param view          鍖呭惈鑳屾櫙鍥剧墖鐨勬帶浠�
     * @param finalResId    榛樿棰滆壊鐨勮祫婧恑d
     * @param resourceResId 閫氳繃璧勬簮鏂囦欢id鐨勫舰寮忚嚜瀹氫箟鐨刬d
     * @param codeResId     閫氳繃java浠ｇ爜鐨勬柟寮忚嚜瀹氫箟鐨刬d
     */
    public static void applyCustomUITintDrawable(View view, int finalResId, int resourceResId, int codeResId) {
        Context context = view.getContext();
        if (MQConfig.DEFAULT != codeResId) {
            resourceResId = codeResId;
        }
        if (context.getResources().getColor(resourceResId) != context.getResources().getColor(finalResId)) {
            Drawable tintDrawable = tintDrawable(context, view.getBackground(), resourceResId);
            setBackground(view, tintDrawable);
        }
    }

    public static Drawable tintDrawable(Context context, Drawable drawable, @ColorRes int color) {
        if (drawable == null) {
            return null;
        }

        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(color));
        return wrappedDrawable;
    }

    public static void setBackground(View v, Drawable bgDrawable) {
    	/* zanshizhushi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(bgDrawable);
        } else {
            v.setBackgroundDrawable(bgDrawable);
        }
        */
    }

    public static void tintPressedIndicator(ImageView imageView, @DrawableRes int normalResId, @DrawableRes int pressedResId) {
        Drawable normal = imageView.getResources().getDrawable(normalResId);
        Drawable pressed = imageView.getResources().getDrawable(pressedResId);
        pressed = MQUtils.tintDrawable(imageView.getContext(), pressed, R.color.mq_indicator_selected);
        imageView.setImageDrawable(getPressedSelectorDrawable(normal, pressed));
    }

    /**
     * 寰楀埌鐐瑰嚮鏀瑰彉鐘舵�佺殑Selector,涓�鑸粰setBackgroundDrawable浣跨敤
     *
     * @param normal
     * @param pressed
     * @return
     */
    public static StateListDrawable getPressedSelectorDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 鍒ゆ柇鏄惁鏈夊閮ㄥ瓨鍌ㄨ澶噑dcard
     *
     * @return true | false
     */
    public static boolean isSdcardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠� dp 鐨勫崟浣� 杞垚涓� px(鍍忕礌)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean isFileExist(String filePath) {
        boolean isFileExist;
        try {
            File file = new File(filePath);
            isFileExist = file.exists();
        } catch (Exception e) {
            isFileExist = false;
        }
        return isFileExist;
    }

    public static String getPicStorePath(Context ctx) {
        File file = ctx.getExternalFilesDir(null);
        if (!file.exists()) {
            file.mkdir();
        }
        File imageStoreFile = new File(file.getAbsolutePath() + "/mq");
        if (!imageStoreFile.exists()) {
            imageStoreFile.mkdir();
        }
        return imageStoreFile.getAbsolutePath();
    }

    /**
     * 鑾峰彇鐘舵�佹爮楂樺害
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 鑾峰彇灞忓箷楂樺害
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 鏄剧ず鍚愬徃
     *
     * @param context
     * @param text
     */
    public static void show(Context context, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.length() < 10) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 鏄剧ず鍚愬徃
     *
     * @param context
     * @param resId
     */
    public static void show(Context context, @StringRes int resId) {
        show(context, context.getResources().getString(resId));
    }

    /**
     * 鍦ㄥ瓙绾跨▼涓樉绀哄悙鍙告椂浣跨敤璇ユ柟娉�
     *
     * @param context
     * @param text
     */
    public static void showSafe(final Context context, final CharSequence text) {
        runInUIThread(new Runnable() {
            @Override
            public void run() {
                show(context, text);
            }
        });
    }

    /**
     * 鍦ㄥ瓙绾跨▼涓樉绀哄悙鍙告椂浣跨敤璇ユ柟娉�
     *
     * @param context
     * @param resId
     */
    public static void showSafe(Context context, @StringRes int resId) {
        showSafe(context, context.getResources().getString(resId));
    }

    /**
     * 鎷疯礉鏂囨。鍒伴粡璐存澘
     *
     * @param text
     */
    public static void clip(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        } else {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("mq_content", text));
        }
    }

    /**
     * 鍏抽棴activity涓墦寮�鐨勯敭鐩�
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 鍏抽棴dialog涓墦寮�鐨勯敭鐩�
     *
     * @param dialog
     */
    public static void closeKeyboard(Dialog dialog) {
        View view = dialog.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 鎵撳紑閿洏
     *
     * @param editText
     */
    public static void openKeyboard(final EditText editText) {
        runInUIThread(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                editText.setSelection(editText.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 300);
    }

    /**
     * 婊氬姩ListView鍒板簳閮�
     *
     * @param absListView
     */
    public static void scrollListViewToBottom(final AbsListView absListView) {
        if (absListView != null) {
            if (absListView.getAdapter() != null && absListView.getAdapter().getCount() > 0) {
                absListView.post(new Runnable() {
                    @Override
                    public void run() {
                        absListView.setSelection(absListView.getAdapter().getCount() - 1);
                    }
                });
            }
        }
    }

    /**
     * 鏍规嵁Uri鑾峰彇鏂囦欢鐨勭湡瀹炶矾寰�
     *
     * @param uri
     * @param context
     * @return
     */
    public static String getRealPathByUri(Context context, Uri uri) {
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return uri.getPath();
        }

        try {
            ContentResolver resolver = context.getContentResolver();
            String[] proj = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = MediaStore.Images.Media.query(resolver, uri, proj);
            String realPath = null;
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    realPath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            return realPath;
        } catch (Exception e) {
            return uri.getPath();
        }
    }

    /**
     * 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠� px(鍍忕礌) 鐨勫崟浣� 杞垚涓� dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 鑾峰彇鍙栧睆骞曞搴�
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 灏嗗瓧绗︿覆杞垚MD5鍊�
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }


    public static File getImageDir(Context context) {
        File imageDir = null;
        if (isExternalStorageWritable()) {
            String appName = context.getString(context.getApplicationInfo().labelRes);
            imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MeiqiaSDK" + File.separator + appName);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
        } else {
            MQUtils.showSafe(context, R.string.mq_no_sdcard);
        }
        return imageDir;
    }

    /**
     * 鍒ゆ柇澶栧瓨鍌ㄦ槸鍚﹀彲鍐�
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 鍒濆鍖朓mageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.mq_ic_gallery_holder).cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3).defaultDisplayImageOptions(options).build();
            ImageLoader.getInstance().init(config);
        }
    }
}

package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.model.ImageFolderModel;
import com.meiqia.meiqiasdk.pw.MQPhotoFolderPw;
import com.meiqia.meiqiasdk.util.MQImageCaptureManager;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 浣滆��:鐜嬫旦 閭欢:bingoogolapple@gmail.com
 * 鍒涘缓鏃堕棿:16/2/24 涓嬪崍5:36
 * 鎻忚堪:鍥剧墖閫夋嫨鐣岄潰
 */
public class MQPhotoPickerActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String EXTRA_IMAGE_DIR = "EXTRA_IMAGE_DIR";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_TOP_RIGHT_BTN_TEXT = "EXTRA_TOP_RIGHT_BTN_TEXT";

    /**
     * 鎷嶇収鐨勮姹傜爜
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    /**
     * 棰勮鐓х墖鐨勮姹傜爜
     */
    private static final int REQUEST_CODE_PREVIEW = 2;

    private RelativeLayout mTitleRl;
    private TextView mTitleTv;
    private ImageView mArrowIv;
    private TextView mSubmitTv;
    private GridView mContentGv;

    private ImageFolderModel mCurrentImageFolderModel;

    /**
     * 鏄惁鍙互鎷嶇収
     */
    private boolean mTakePhotoEnabled;
    /**
     * 鏈�澶氶�夋嫨澶氬皯寮犲浘鐗囷紝榛樿绛変簬1锛屼负鍗曢��
     */
    private int mMaxChooseCount = 1;
    /**
     * 鍙充笂瑙掓寜閽枃鏈�
     */
    private String mTopRightBtnText;
    /**
     * 鍥剧墖鐩綍鏁版嵁闆嗗悎
     */
    private ArrayList<ImageFolderModel> mImageFolderModels;

    private PicAdapter mPicAdapter;

    private MQImageCaptureManager mImageCaptureManager;

    private MQPhotoFolderPw mPhotoFolderPw;
    /**
     * 涓婁竴娆℃樉绀哄浘鐗囩洰褰曠殑鏃堕棿鎴筹紝闃叉鐭椂闂村唴閲嶅鐐瑰嚮鍥剧墖鐩綍鑿滃崟鏃剁晫闈㈤敊涔�
     */
    private long mLastShowPhotoFolderTime;

    /**
     * @param context         搴旂敤绋嬪簭涓婁笅鏂�
     * @param imageDir        鎷嶇収鍚庡浘鐗囦繚瀛樼殑鐩綍銆傚鏋滀紶null琛ㄧず娌℃湁鎷嶇収鍔熻兘锛屽鏋滀笉涓簄ull鍒欏叿鏈夋媿鐓у姛鑳斤紝
     * @param maxChooseCount  鍥剧墖閫夋嫨寮犳暟鐨勬渶澶у��
     * @param selectedImages  褰撳墠宸查�変腑鐨勫浘鐗囪矾寰勯泦鍚堬紝鍙互浼爊ull
     * @param topRightBtnText 鍙充笂瑙掓寜閽殑鏂囨湰
     * @return
     */
    public static Intent newIntent(Context context, File imageDir, int maxChooseCount, ArrayList<String> selectedImages, String topRightBtnText) {
        Intent intent = new Intent(context, MQPhotoPickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_DIR, imageDir);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putExtra(EXTRA_TOP_RIGHT_BTN_TEXT, topRightBtnText);
        return intent;
    }

    /**
     * 鑾峰彇宸查�夋嫨鐨勫浘鐗囬泦鍚�
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        processLogic(savedInstanceState);
    }

    private void initView() {
        setContentView(R.layout.mq_activity_photo_picker);
        mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mArrowIv = (ImageView) findViewById(R.id.arrow_iv);
        mSubmitTv = (TextView) findViewById(R.id.submit_tv);
        mContentGv = (GridView) findViewById(R.id.content_gv);
    }

    private void initListener() {
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.folder_ll).setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);
        mContentGv.setOnItemClickListener(this);
    }

    private void processLogic(Bundle savedInstanceState) {
        // 鑾峰彇鎷嶇収鍥剧墖淇濆瓨鐩綍
        File imageDir = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_DIR);
        if (imageDir != null) {
            mTakePhotoEnabled = true;
            mImageCaptureManager = new MQImageCaptureManager(this, imageDir);
        }
        // 鑾峰彇鍥剧墖閫夋嫨鐨勬渶澶у紶鏁�
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        // 鑾峰彇鍙充笂瑙掓寜閽枃鏈�
        mTopRightBtnText = getIntent().getStringExtra(EXTRA_TOP_RIGHT_BTN_TEXT);


        MQUtils.initImageLoader(this);

        mPicAdapter = new PicAdapter();
        mPicAdapter.setSelectedImages(getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES));
        mContentGv.setAdapter(mPicAdapter);

        renderTopRightBtn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_iv) {
            onBackPressed();
        } else if (v.getId() == R.id.folder_ll && System.currentTimeMillis() - mLastShowPhotoFolderTime > MQPhotoFolderPw.ANIM_DURATION) {
            showPhotoFolderPw();
            mLastShowPhotoFolderTime = System.currentTimeMillis();
        } else if (v.getId() == R.id.submit_tv) {
            returnSelectedImages(mPicAdapter.getSelectedImages());
        }
    }

    /**
     * 杩斿洖宸查�変腑鐨勫浘鐗囬泦鍚�
     *
     * @param selectedImages
     */
    private void returnSelectedImages(ArrayList<String> selectedImages) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showPhotoFolderPw() {
        if (mPhotoFolderPw == null) {
            mPhotoFolderPw = new MQPhotoFolderPw(this, mTitleRl, new MQPhotoFolderPw.Callback() {
                @Override
                public void onSelectedFolder(int position) {
                    reloadPhotos(position);
                }

                @Override
                public void executeDismissAnim() {
                    ViewCompat.animate(mArrowIv).setDuration(MQPhotoFolderPw.ANIM_DURATION).rotation(0).start();
                }
            });
        }
        mPhotoFolderPw.setDatas(mImageFolderModels);
        mPhotoFolderPw.show();

        ViewCompat.animate(mArrowIv).setDuration(MQPhotoFolderPw.ANIM_DURATION).rotation(-180).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCurrentImageFolderModel.isTakePhotoEnabled() && position == 0) {
            if (mPicAdapter.getSelectedCount() == mMaxChooseCount) {
                toastMaxCountTip();
            } else {
                takePhoto();
            }
        } else {
            int currentPosition = position;
            if (mCurrentImageFolderModel.isTakePhotoEnabled()) {
                currentPosition--;
            }
            startActivityForResult(MQPhotoPickerPreviewActivity.newIntent(this, mMaxChooseCount, mPicAdapter.getSelectedImages(), mPicAdapter.getDatas(), currentPosition, mTopRightBtnText, false), REQUEST_CODE_PREVIEW);
        }
    }

    /**
     * 鏄剧ず鍙兘閫夋嫨 mMaxChooseCount 寮犲浘鐨勬彁绀�
     */
    private void toastMaxCountTip() {
        MQUtils.show(this, getString(R.string.mq_toast_photo_picker_max, mMaxChooseCount));
    }

    /**
     * 鎷嶇収
     */
    private void takePhoto() {
        try {
            startActivityForResult(mImageCaptureManager.getTakePictureIntent(), REQUEST_CODE_TAKE_PHOTO);
        } catch (Exception e) {
            MQUtils.show(this, R.string.mq_photo_not_support);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                // 鏇存柊鍥惧簱
                mImageCaptureManager.refreshGallery();

                String photoPath = mImageCaptureManager.getCurrentPhotoPath();
                mPicAdapter.getSelectedImages().add(photoPath);
                mPicAdapter.getDatas().add(0, photoPath);
                renderTopRightBtn();

                startActivityForResult(MQPhotoPickerPreviewActivity.newIntent(this, mMaxChooseCount, mPicAdapter.getSelectedImages(), mPicAdapter.getDatas(), 0, mTopRightBtnText, true), REQUEST_CODE_PREVIEW);
            } else if (requestCode == REQUEST_CODE_PREVIEW) {
                returnSelectedImages(MQPhotoPickerPreviewActivity.getSelectedImages(data));
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE_PREVIEW) {
            mPicAdapter.setSelectedImages(MQPhotoPickerPreviewActivity.getSelectedImages(data));
            renderTopRightBtn();
        }
    }

    /**
     * 娓叉煋鍙充笂瑙掓寜閽�
     */
    private void renderTopRightBtn() {
        if (mPicAdapter.getSelectedCount() == 0) {
            mSubmitTv.setEnabled(false);
            mSubmitTv.setText(mTopRightBtnText);
        } else {
            mSubmitTv.setEnabled(true);
            mSubmitTv.setText(mTopRightBtnText + "(" + mPicAdapter.getSelectedCount() + "/" + mMaxChooseCount + ")");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void loadDatas() {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");

        ImageFolderModel allImageFolderModel = new ImageFolderModel(mTakePhotoEnabled);

        HashMap<String, ImageFolderModel> imageFolderModelMap = new HashMap<String, ImageFolderModel>();
        ImageFolderModel otherImageFolderModel = null;
        if (cursor != null && cursor.getCount() > 0) {
            boolean firstInto = true;
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                if (firstInto) {
                    allImageFolderModel.name = getString(R.string.mq_all_image);
                    allImageFolderModel.coverPath = imagePath;
                    firstInto = false;
                }
                // 鎵�鏈夊浘鐗囩洰褰曟瘡娆￠兘娣诲姞
                allImageFolderModel.addLastImage(imagePath);

                // 鍏朵粬鍥剧墖鐩綍
                String folderName = new File(imagePath).getParentFile().getName();
                if (imageFolderModelMap.containsKey(folderName)) {
                    otherImageFolderModel = imageFolderModelMap.get(folderName);
                } else {
                    otherImageFolderModel = new ImageFolderModel(folderName, imagePath);
                    imageFolderModelMap.put(folderName, otherImageFolderModel);
                }
                otherImageFolderModel.addLastImage(imagePath);
            }
            cursor.close();
        }

        mImageFolderModels = new ArrayList();
        // 娣诲姞鎵�鏈夊浘鐗囩洰褰�
        mImageFolderModels.add(allImageFolderModel);

        // 娣诲姞鍏朵粬鍥剧墖鐩綍
        Iterator<Map.Entry<String, ImageFolderModel>> iterator = imageFolderModelMap.entrySet().iterator();
        while (iterator.hasNext()) {
            mImageFolderModels.add(iterator.next().getValue());
        }

        reloadPhotos(mPhotoFolderPw == null ? 0 : mPhotoFolderPw.getCurrentPosition());
    }

    private void reloadPhotos(int position) {
        mCurrentImageFolderModel = mImageFolderModels.get(position);
        mTitleTv.setText(mCurrentImageFolderModel.name);
        mPicAdapter.setDatas(mCurrentImageFolderModel.getImages());
    }

    private class PicAdapter extends BaseAdapter {
        private ArrayList<String> mSelectedImages = new ArrayList<String>();
        private ArrayList<String> mDatas;
        private ImageSize mImageSize;

        public PicAdapter() {
            mDatas = new ArrayList<String>();
            int size = MQUtils.getScreenWidth(getApplicationContext()) / 10;
            mImageSize = new ImageSize(size, size);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public String getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // 閲嶇敤ViewHolder
            PicViewHolder picViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mq_item_square_image, parent, false);
                picViewHolder = new PicViewHolder();
                picViewHolder.photoIv = (ImageView) convertView.findViewById(R.id.photo_iv);
                picViewHolder.tipTv = (TextView) convertView.findViewById(R.id.tip_tv);
                picViewHolder.flagIv = (ImageView) convertView.findViewById(R.id.flag_iv);
                convertView.setTag(picViewHolder);
            } else {
                picViewHolder = (PicViewHolder) convertView.getTag();
            }

            String imagePath = getItem(position);
            if (mCurrentImageFolderModel.isTakePhotoEnabled() && position == 0) {
                picViewHolder.tipTv.setVisibility(View.VISIBLE);
                picViewHolder.photoIv.setScaleType(ImageView.ScaleType.CENTER);
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.mq_ic_gallery_camera, picViewHolder.photoIv);

                picViewHolder.flagIv.setVisibility(View.INVISIBLE);

                picViewHolder.photoIv.setColorFilter(null);
            } else {
                picViewHolder.tipTv.setVisibility(View.INVISIBLE);
                picViewHolder.photoIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage("file://" + imagePath, new ImageViewAware(picViewHolder.photoIv), null, mImageSize, null, null);

                picViewHolder.flagIv.setVisibility(View.VISIBLE);


                if (mSelectedImages.contains(imagePath)) {
                    picViewHolder.flagIv.setImageResource(R.drawable.mq_ic_cb_checked);
                    picViewHolder.photoIv.setColorFilter(getResources().getColor(R.color.mq_photo_selected_color));
                } else {
                    picViewHolder.flagIv.setImageResource(R.drawable.mq_ic_cb_normal);
                    picViewHolder.photoIv.setColorFilter(null);
                }

                setFlagClickListener(picViewHolder.flagIv, position);
            }

            return convertView;
        }

        private void setFlagClickListener(ImageView flagIv, final int position) {
            flagIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String image = getItem(position);
                    if (!mSelectedImages.contains(image) && getSelectedCount() == mMaxChooseCount) {
                        toastMaxCountTip();
                    } else {
                        if (mSelectedImages.contains(image)) {
                            mSelectedImages.remove(image);
                        } else {
                            mSelectedImages.add(image);
                        }
                        notifyDataSetChanged();

                        renderTopRightBtn();
                    }
                }
            });
        }

        public void setDatas(ArrayList<String> datas) {
            if (datas != null) {
                mDatas = datas;
            } else {
                mDatas.clear();
            }
            notifyDataSetChanged();
        }

        public ArrayList<String> getDatas() {
            return mDatas;
        }

        public void setSelectedImages(ArrayList<String> selectedImages) {
            if (selectedImages != null) {
                mSelectedImages = selectedImages;
            }
            notifyDataSetChanged();
        }

        public ArrayList<String> getSelectedImages() {
            return mSelectedImages;
        }

        public int getSelectedCount() {
            return mSelectedImages.size();
        }
    }

    private class PicViewHolder {
        public ImageView photoIv;
        public TextView tipTv;
        public ImageView flagIv;
    }
}
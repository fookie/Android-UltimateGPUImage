package cn.co.willow.android.ultimate.gpuimage.sample.function_holder;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.co.willow.android.ultimate.gpuimage.sample.R;
import cn.co.willow.android.ultimate.gpuimage.sample.util.UIUtils;
import cn.co.willow.android.ultimate.gpuimage.utils.LogUtil;

/**
 * 组件：视频录制
 * <p>
 * Created by willow.li on 2016/10/22.
 */
public class VideoControlHolder extends BaseHolder implements View.OnClickListener {

    /*关键变量=======================================================================================*/
    private CardView     mCVLastVideoCont;
    private ImageView    mBtnLastestVideo;
    private LinearLayout mRLRecordControl;
    private Button       mBtnRecordsVideo;
    private Button       mBtnSwitchCamera;
    private boolean      isRecording;

    public VideoControlHolder(Activity context) {
        super(context);
        initWhenConstruct();
    }


    /*初始化 initialize ultimate gpuimage===========================================================*/
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.holder_video_controller, null);
        mCVLastVideoCont = (CardView) view.findViewById(R.id.last_video_cont);
        mRLRecordControl = (LinearLayout) view.findViewById(R.id.record_controller);
        mBtnRecordsVideo = (Button) view.findViewById(R.id.start_record_btn);
        mBtnSwitchCamera = (Button) view.findViewById(R.id.switch_camera_btn);
        mBtnLastestVideo = (ImageView) view.findViewById(R.id.last_video_btn);
        mBtnRecordsVideo.setOnClickListener(this);
        mBtnSwitchCamera.setOnClickListener(this);
        mBtnLastestVideo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_record_btn:
                if (mRecordControlCallBack != null) {
                    if (isRecording) {
                        mRecordControlCallBack.stopRecord();
                        setReviewState(true);
                    } else {
                        mRecordControlCallBack.startRecord();
                        setReviewState(false);
                    }
                }
                break;
            case R.id.switch_camera_btn:
                if (mRecordControlCallBack != null) {
                    mRecordControlCallBack.switchCamera();
                }
                break;
            case R.id.last_video_btn:
                if (mRecordControlCallBack != null) {
                    mRecordControlCallBack.playVideo();
                }
                break;
        }
    }

    public void switchRecordState(final boolean isRecording) {
        this.isRecording = isRecording;
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    mBtnRecordsVideo.setText(R.string.control_stop_record);
                    mBtnRecordsVideo.setBackgroundResource(R.drawable.control_alert_camera_btn);
                } else {
                    mBtnRecordsVideo.setText(R.string.control_start_record);
                    mBtnRecordsVideo.setBackgroundResource(R.drawable.control_default_camera_btn);
                }
            }
        });
    }

    public void setVideoCover(String gifUrl) {
        LogUtil.w("Cover URL is::" + gifUrl);
        try {
            Glide.with(context)
                 .load(gifUrl)
                 .diskCacheStrategy(DiskCacheStrategy.NONE)
                 .centerCrop()
                 .crossFade()
                 .into(mBtnLastestVideo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /*预览动画========================================================================================*/
    public void setReviewState(boolean isShow) {
        if (mCVLastVideoCont != null && mRLRecordControl != null) {
            TransitionManager.beginDelayedTransition(mCVLastVideoCont, new Fade());
            TransitionManager.beginDelayedTransition(mRLRecordControl, new ChangeBounds());
            mCVLastVideoCont.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /*滤镜说明动画====================================================================================*/
    public void enterAnim() {
        AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
        alphaAnim.setDuration(200);
        view.setAnimation(alphaAnim);
        view.setVisibility(View.VISIBLE);
        view.animate();
    }

    public void exitAnim() {
        AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnim.setDuration(200);
        view.setAnimation(alphaAnim);
        view.animate();
    }


    /*资源回收======================================================================================*/
    @Override
    protected void clearAllResource() {
    }


    /*对外暴露监听==================================================================================*/
    /** 播放器状态监听 */
    private RecordControlCallBack mRecordControlCallBack;

    public interface RecordControlCallBack {
        void playVideo();
        void startRecord();
        void stopRecord();
        void switchCamera();
    }

    public void setOnRecordStateListener(RecordControlCallBack mRecordControlCallBack) {
        if (mRecordControlCallBack != null) {
            this.mRecordControlCallBack = mRecordControlCallBack;
        }
    }
}

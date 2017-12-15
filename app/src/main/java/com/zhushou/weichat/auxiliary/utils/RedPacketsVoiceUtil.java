package com.zhushou.weichat.auxiliary.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.zhushou.weichat.R;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/13.
 * 红包提醒音效
 */
public class RedPacketsVoiceUtil {

    private MediaPlayer mPlayer;
    private SoundPool mSound;
    private HashMap<Integer, Integer> soundPoolMap;
    private Context mContext;

    public RedPacketsVoiceUtil(Context context){
        this.mContext = context;
        InitSounds();
    }

    /**
     * 初始化声音
     */
    private void InitSounds() {
        // 设置播放音效
        mPlayer = MediaPlayer.create(mContext, R.raw.qhb);
        // 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        mSound = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, mSound.load(mContext, R.raw.qhb, 1));
        //可以在后面继续put音效文件
    }

    /**
     * soundPool播放
     *
     * @param sound
     *            播放第一个
     * @param loop
     *            是否循环
     */
    public void PlaySound(int sound, int loop) {
        AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统声音的当前音量
        float currentVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 获取系统声音的最大音量
        float maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量的百分比
        float volume = currentVolume / maxVolume;

        // 第一个参数是声效ID,第二个是左声道音量，第三个是右声道音量，第四个是流的优先级，最低为0，第五个是是否循环播放，第六个播放速度(1.0 =正常播放,范围0.5 - 2.0)
        mSound.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }
}

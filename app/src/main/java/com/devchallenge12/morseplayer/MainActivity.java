package com.devchallenge12.morseplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final int PROGRESS_UPDATE = 10;
    private static final float[] AUDIO_SPEEDS = new float[]{0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};

    private EditText mMorseInput;
    private TextView mMorseOutput;
    private SeekBar mSeekPosition;
    private TextView mSpeedStatus;
    private SeekBar mSeekSpeed;
    private Button mPlayPause;

    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private Runnable mRunnable;

    private int mPosition = 0;
    private int mSpeed = 2;
    private boolean isAudioRelevant = false;
    private boolean isPalying = false;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isPalying){
                pauseAction();
            } else {
                playAction();
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isAudioRelevant = false;
            setSeekPositionProgress(0);
            setMorseOutputText(MorseConvertor.textToMorseMsg(s.toString()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()){
                case R.id.sbSeekPosition:
                    setAudioPosition(progress, fromUser);
                    break;
                case R.id.sbSeekSpeed:
                    setAudioSpeed(progress, fromUser);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMorseInput = (EditText) findViewById(R.id.etMorseInput);
        mMorseInput.addTextChangedListener(mTextWatcher);

        mMorseOutput = (TextView) findViewById(R.id.tvMorseOutput);

        mSeekPosition = (SeekBar) findViewById(R.id.sbSeekPosition);
        mSeekPosition.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mSpeedStatus = (TextView) findViewById(R.id.tvSpeedStatus);

        mSeekSpeed = (SeekBar) findViewById(R.id.sbSeekSpeed);
        mSeekSpeed.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mPlayPause = (Button) findViewById(R.id.btnPlayPause);
        mPlayPause.setOnClickListener(mOnClickListener);

        mHandler = new Handler();
        setSpeedStatus(mSpeed);
    }

    public void setMorseOutputText(String outputText){
        mMorseOutput.setText(outputText);
    }

    private void playAction(){
        if(mMediaPlayer == null || !isAudioRelevant){
            createPlayer();
        }
        play();
    }

    public void createPlayer(){
        String inputText = mMorseInput.getText().toString();
        String filePath = "/sdcard/morse.midi";
        FileDescriptor fd = null;
        if(inputText.length() > 0 && checkPermission()){
            MorseConvertor.textToMorseAudio(inputText, filePath);

            try {
                FileInputStream fis = new FileInputStream(new File(filePath));
                fd = fis.getFD();

                if(mMediaPlayer == null){
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            endAction();
                        }
                    });
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            setAudioPosition(0, true);
                        }
                    });
                } else {
                    mMediaPlayer.reset();
                }

                mMediaPlayer.setDataSource(fd);
                mMediaPlayer.prepare();
                isAudioRelevant = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void play(){
        if(mMediaPlayer != null){
            mMediaPlayer.start();
            setAudioPosition(mPosition, true);
            setAudioSpeed(mSpeed, true);
            initializeSeekBar();
            if(mMediaPlayer.isPlaying()){
                mPlayPause.setText(R.string.btn_pause);
                isPalying = true;
            }
        }
    }

    private void pauseAction(){
        storeCurrentPosition();
        pause();
    }

    private void pause(){
        if(mMediaPlayer != null){
            mMediaPlayer.pause();
            if(!mMediaPlayer.isPlaying()){
                isPalying = false;
                mPlayPause.setText(R.string.btn_play);
            }
        }
        cleanHandler();
    }

    private void endAction(){
        pause();
        setAudioPosition(0, true);
    }

    private void stopAction(){
        if(mMediaPlayer != null){
            endAction();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            cleanHandler();
        }
    }

    private void cleanHandler(){
        if(mHandler != null){
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void initializeSeekBar(){
        mSeekPosition.setMax(mMediaPlayer.getDuration());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                updatePosition();
                mHandler.postDelayed(mRunnable, PROGRESS_UPDATE);
            }
        };
        mHandler.postDelayed(mRunnable, PROGRESS_UPDATE);
    }

    public void updatePosition() {
        if (mMediaPlayer != null) {
            storeCurrentPosition();
            setSeekPositionProgress(getPosition());
        }
    }

    private void storeCurrentPosition(){
        if(mMediaPlayer != null){
            int duration = mMediaPlayer.getDuration();
            int currentPosition = mMediaPlayer.getCurrentPosition();
            if (currentPosition != duration){
                setPosition(currentPosition);
                setSeekPositionProgress(getPosition());
            } else {
                endAction();
            }
        } else {
            setPosition(0);
        }
    }

    private void setAudioSpeed(int progress, boolean fromUser) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mMediaPlayer != null && fromUser) {
                if (mMediaPlayer.isPlaying()) {
                    PlaybackParams params = mMediaPlayer.getPlaybackParams();
                    params.setSpeed(AUDIO_SPEEDS[progress % AUDIO_SPEEDS.length]);
                    mMediaPlayer.setPlaybackParams(params);
                }
            }
            setSpeedStatus(progress);
        } else {
            // Alert needed verssion of Android
        }
    }

    private void setAudioPosition(int progress, boolean fromUser) {
        setSeekPositionProgress(progress);
        if(mMediaPlayer != null && fromUser){
            mMediaPlayer.seekTo(getPosition());
        }
    }

    private void setPosition(int position){
        mPosition = position;
    }

    private int getPosition(){
        return mPosition;
    }

    private void setSeekPositionProgress(int position){
        setPosition(position);
        mSeekPosition.setProgress(getPosition());
    }

    private void setSpeedStatus(int index){
        mSpeed = index;
        String status = "x" + AUDIO_SPEEDS[mSpeed];
        mSpeedStatus.setText(status);
    }

    private boolean checkPermission(){
        int permission = ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        if(permission != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAction();
    }
}

package com.devchallenge12.morseplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
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
    private static final int PROGRESS_DEVIDER = 10;

    private EditText mMorseInput;
    private TextView mMorseOutput;
    private SeekBar mSeekProgress;
    private TextView mSpeedStatus;
    private SeekBar mSeekSpeed;
    private Button mPlayPause;

    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private Runnable mRunnable;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startPlayer();
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                case R.id.sbSeekProgress:
                    setAudioProgress(progress, fromUser);
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

    private void setAudioProgress(int progress, boolean fromUser) {
        if(mMediaPlayer != null && fromUser){
            mMediaPlayer.seekTo(progress * PROGRESS_DEVIDER);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMorseInput = (EditText) findViewById(R.id.etMorseInput);
        mMorseInput.addTextChangedListener(mTextWatcher);

        mMorseOutput = (TextView) findViewById(R.id.tvMorseOutput);

        mSeekProgress = (SeekBar) findViewById(R.id.sbSeekProgress);
        mSeekProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mSpeedStatus = (TextView) findViewById(R.id.tvSpeedStatus);

        mSeekSpeed = (SeekBar) findViewById(R.id.sbSeekSpeed);
        mSeekSpeed.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mPlayPause = (Button) findViewById(R.id.btnPlayPause);
        mPlayPause.setOnClickListener(mOnClickListener);

        mHandler = new Handler();
    }

    public void startPlayer(){
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
                } else {
                    mMediaPlayer.reset();
                }

                mMediaPlayer.setDataSource(fd);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                initializeSeekBar();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProgress(int progress) {
        mSeekSpeed.setProgress(progress);
    }

    public void setMorseOutputText(String outputText){
        mMorseOutput.setText(outputText);
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

    private void initializeSeekBar(){
        mSeekProgress.setMax(mMediaPlayer.getDuration()/PROGRESS_DEVIDER);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer!= null){
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition()/PROGRESS_DEVIDER;
                    mSeekProgress.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(mRunnable, PROGRESS_DEVIDER);
            }
        };
        mHandler.postDelayed(mRunnable, PROGRESS_DEVIDER);
    }

    private void stopPlaying(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            if(mHandler != null){
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlaying();
    }
}

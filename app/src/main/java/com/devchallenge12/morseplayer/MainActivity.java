package com.devchallenge12.morseplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText mMorseInput;
    private TextView mMorseOutput;
    private SeekBar mSeekProgress;
    private TextView mSpeedStatus;
    private SeekBar mSeekSpeed;
    private Button mPlayPause;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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

        mSeekProgress = (SeekBar) findViewById(R.id.sbSeekProgress);
        mSeekProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mSpeedStatus = (TextView) findViewById(R.id.tvSpeedStatus);

        mSeekSpeed = (SeekBar) findViewById(R.id.sbSeekSpeed);
        mSeekSpeed.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mPlayPause = (Button) findViewById(R.id.btnPlayPause);
        mPlayPause.setOnClickListener(mOnClickListener);
    }

    public void updateProgress(int progress) {
        mSeekSpeed.setProgress(progress);
    }

    public void setMorseOutputText(String outputText){
        mMorseOutput.setText(outputText);
    }

}

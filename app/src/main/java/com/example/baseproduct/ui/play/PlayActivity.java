package com.example.baseproduct.ui.play;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.example.baseproduct.R;
import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.call_api.model.MusicModel;
import com.example.baseproduct.databinding.ActivityPlayBinding;

import java.util.Locale;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> {
    @Override
    public ActivityPlayBinding getBinding() {
        return ActivityPlayBinding.inflate(getLayoutInflater());
    }

    MusicModel itemMusic;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable updateSeekRunnable;

    private boolean isPlaying = false;

    @Override
    public void initView() {
        super.initView();
        itemMusic = (MusicModel) getIntent().getSerializableExtra("data");
        Log.e("itemMusic", "itemMusic: " + itemMusic);
        binding.tvName.setText(itemMusic.getName());
        Glide.with(this).load(itemMusic.getImage()).into(binding.ivImage);


        handler = new Handler(getMainLooper());

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(itemMusic.getLink());
            mediaPlayer.prepareAsync();

            //khởi tạo
            mediaPlayer.setOnPreparedListener(mp -> {
                binding.tvTimeEnd.setText(formatTime(mp.getDuration()));
                binding.sbAudio.setMax(mp.getDuration());
            });

            //khi phát xong reset state
            mediaPlayer.setOnCompletionListener(mp -> {
                binding.ivPp.setImageResource(R.drawable.ic_recording_play);
                binding.sbAudio.setProgress(0);
                binding.tvTimeStart.setText("00:00");
                isPlaying = false;
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindView() {
        super.bindView();

        binding.ivBack.setOnClickListener(v -> onBack());

        binding.ivTimeBack.setOnClickListener(v -> {
            int pos = mediaPlayer.getCurrentPosition() - 5000;
            if (pos < 0) pos = 0;
            mediaPlayer.seekTo(pos);
        });

        binding.ivTimeNext.setOnClickListener(v -> {
            int pos = mediaPlayer.getCurrentPosition() + 5000;
            if (pos < mediaPlayer.getDuration()) {
                mediaPlayer.seekTo(pos);
            }
        });

        binding.ivPp.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                binding.ivPp.setImageResource(R.drawable.ic_recording_play);
                handler.removeCallbacks(updateSeekRunnable);
            } else {
                mediaPlayer.start();
                binding.ivPp.setImageResource(R.drawable.ic_recording_pause);
                startSeekBarUpdate();
            }
            isPlaying = !isPlaying;
        });

        binding.sbAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    binding.tvTimeStart.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void startSeekBarUpdate() {
        updateSeekRunnable = new Runnable() {
            @Override
            public void run() {
                //kiểm tra xem mediaPlayer được khởi tạo chưa và có đang play không
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int current = mediaPlayer.getCurrentPosition();

                    binding.sbAudio.setProgress(current);
                    binding.tvTimeStart.setText(formatTime(current));

                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(updateSeekRunnable);
    }


    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clean up để tránh leak
        handler.removeCallbacksAndMessages(null);

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        binding = null;
    }


}

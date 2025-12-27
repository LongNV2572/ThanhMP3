package com.example.baseproduct.ui.both.add;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityMusicAddBinding;

public class MusicAddActivity extends BaseActivity<ActivityMusicAddBinding> {
    @Override
    public ActivityMusicAddBinding getBinding() {
        return ActivityMusicAddBinding.inflate(getLayoutInflater());
    }

    @Override
    public void bindView() {
        super.bindView();

        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
    }
}

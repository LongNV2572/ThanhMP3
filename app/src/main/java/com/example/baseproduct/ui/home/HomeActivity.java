package com.example.baseproduct.ui.home;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityHomeBinding;
import com.example.baseproduct.dialog.exit.ExitAppDialog;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this);
        exitAppDialog.init(this::finishAffinity);
        exitAppDialog.show();
    }
}

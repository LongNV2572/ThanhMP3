package com.example.baseproduct.ui.both.splash;

import android.os.Handler;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivitySplashBinding;
import com.example.baseproduct.ui.admin.home.HomeAdminActivity;
import com.example.baseproduct.ui.both.login.LoginActivity;
import com.example.baseproduct.ui.user.home.HomeUserActivity;
import com.example.baseproduct.util.Constant;
import com.example.baseproduct.util.SharePrefUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        super.initView();
        SharePrefUtils.increaseCountOpenApp(this);

        new Handler().postDelayed(this::startNextScreen, 3000);
    }

    public void startNextScreen() {
        if (SharePrefUtils.getBoolean(Constant.IS_REMEMBER_PASS, false)) {
            if (SharePrefUtils.getInt(Constant.USER_TYPE, 1) == 0) {
                startNextActivity(HomeAdminActivity.class, null);
            } else {
                startNextActivity(HomeUserActivity.class, null);
            }
        } else {
            startNextActivity(LoginActivity.class, null);
        }
        finishAffinity();
    }

    @Override
    public void onBack() {

    }
}

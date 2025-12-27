package com.example.baseproduct.ui.both.infor;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityInforBinding;
import com.example.baseproduct.ui.both.login.LoginActivity;
import com.example.baseproduct.util.Constant;
import com.example.baseproduct.util.SharePrefUtils;

public class InforAcitivity extends BaseActivity<ActivityInforBinding> {
    @Override
    public ActivityInforBinding getBinding() {
        return ActivityInforBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        super.initView();

        String account = SharePrefUtils.getString(Constant.USER_ACCOUNT, "");
        String name = SharePrefUtils.getString(Constant.USER_NAME, "");
        String pass = SharePrefUtils.getString(Constant.USER_PASSWORD, "");
        String hiddenPass = "*".repeat(pass.length());

        binding.tvName.setText(name);
        binding.tvUserName.setText(account);
        binding.tvPassword.setText(hiddenPass);
    }

    @Override
    public void bindView() {
        super.bindView();
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });

        binding.tvLogout.setOnClickListener(v -> {
            SharePrefUtils.putInt(Constant.USER_TYPE, 0);
            SharePrefUtils.putString(Constant.USER_NAME, "");
            SharePrefUtils.putString(Constant.USER_PASSWORD, "");
            SharePrefUtils.putBoolean(Constant.IS_REMEMBER_PASS, false);

            startNextActivity(LoginActivity.class, null);
            finishAffinity();
        });
    }


}

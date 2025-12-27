package com.example.baseproduct.ui.both.login;

import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityLoginBinding;
import com.example.baseproduct.model.UserModel;
import com.example.baseproduct.ui.admin.home.HomeAdminActivity;
import com.example.baseproduct.ui.user.home.HomeUserActivity;
import com.example.baseproduct.util.Constant;
import com.example.baseproduct.util.SharePrefUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    @Override
    public ActivityLoginBinding getBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    private final List<UserModel> listUser = new ArrayList<>();
    boolean isPasswordVisible = false;
    boolean isRemember = false;

    @Override
    public void initView() {
        super.initView();

        isRemember = SharePrefUtils.getBoolean(Constant.IS_REMEMBER_PASS, false);
        binding.ivCheckbox.setSelected(isRemember);

        DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference("list_user");

        musicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listUser.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserModel user = child.getValue(UserModel.class);
                    if (user != null) {
                        listUser.add(user);
                    }
                }
                Log.d("Firebase", "Size = " + listUser.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        });

    }

    @Override
    public void bindView() {
        super.bindView();

        binding.ivCheckbox.setOnClickListener(v -> {
            isRemember = !isRemember;
            SharePrefUtils.putBoolean(Constant.IS_REMEMBER_PASS, isRemember);
            binding.ivCheckbox.setSelected(isRemember);
        });

        binding.tvLogin.setOnClickListener(v -> {
            if (binding.edtUsername.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Nhập Username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (binding.edtPassword.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Nhập Password", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < listUser.size(); i++) {
                UserModel user = listUser.get(i);
                String strUser = binding.edtUsername.getText().toString().trim();
                String strPass = binding.edtPassword.getText().toString().trim();
                if (strUser.equals(user.getUsername()) && strPass.equals(user.getPassword())) {
                    SharePrefUtils.putInt(Constant.USER_TYPE, user.getType());
                    SharePrefUtils.putString(Constant.USER_NAME, user.getName());
                    SharePrefUtils.putString(Constant.USER_ACCOUNT, user.getUsername());
                    SharePrefUtils.putString(Constant.USER_PASSWORD, user.getPassword());

                    if (user.getType() == 0) {
                        startNextActivity(HomeAdminActivity.class, null);
                    } else {
                        startNextActivity(HomeUserActivity.class, null);
                    }
                    break;
                } else {
                    if (i == listUser.size() - 1) {
                        Toast.makeText(this, "Nhập sai username hoặc password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        binding.ivPass.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            binding.edtPassword.setSelection(binding.edtPassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
            binding.ivPass.setSelected(isPasswordVisible);
        });
    }


}

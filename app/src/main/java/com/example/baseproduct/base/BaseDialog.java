package com.example.baseproduct.base;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.example.baseproduct.R;

import java.util.Objects;


public abstract class BaseDialog<VB extends ViewBinding> extends Dialog {

    public VB binding;

    protected abstract VB setBinding();

    public BaseDialog(@NonNull Context context, boolean canAble) {
        super(context, R.style.BaseDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = setBinding();
        setContentView(binding.getRoot());

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setCancelable(canAble);

        initView();
        bindView();
    }

    public void initView() {

    }

    public void bindView() {

    }

}

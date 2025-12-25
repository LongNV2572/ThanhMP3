package com.example.baseproduct.dialog.exit;

import android.content.Context;

import com.example.baseproduct.base.BaseDialog;
import com.example.baseproduct.databinding.DialogExitAppBinding;

public class ExitAppDialog extends BaseDialog<DialogExitAppBinding> {
    IClickDialogExit iBaseListener;
    Context context;

    public ExitAppDialog(Context context) {
        super(context, true);
        this.context = context;
    }


    @Override
    protected DialogExitAppBinding setBinding() {
        return DialogExitAppBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.tvCancel.setOnClickListener(view -> dismiss());

        binding.tvQuit.setOnClickListener(view -> {
            iBaseListener.quit();
            dismiss();
        });

    }

    public void init(IClickDialogExit iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}

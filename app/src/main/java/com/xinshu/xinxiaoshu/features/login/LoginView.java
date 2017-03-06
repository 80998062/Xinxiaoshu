package com.xinshu.xinxiaoshu.features.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sinyuk.floating.FloatingWindowManager;
import com.sinyuk.myutils.string.RegexUtils;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.databinding.LoginViewBinding;
import com.xinshu.xinxiaoshu.utils.TextWatcherAdapter;


/**
 * Created by sinyuk on 2017/3/1.
 */

public class LoginView extends BaseFragment {

    private LoginViewBinding binding;
    private boolean isPhoneValid = false;
    private Boolean isCodeInvalid = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();

    }

    private void setupListeners() {
        binding.phoneEt.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                isPhoneValid = RegexUtils.isMobileSimple(text.toString());
                toggleButton(binding.authcodeBtn, isPhoneValid);
            }

        });


        binding.authcodeEt.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                isCodeInvalid = isCodeInvalid(text.toString());
                toggleButton(binding.loginBtn, isCodeInvalid);
            }

        });


        binding.loginBtn.setOnClickListener(this::onLogin);

    }

    private void onLogin(View view) {
//        ReceptionActivity.start(view.getContext());
        Log.d("", "setupListeners: ");
        FloatingWindowManager m = new FloatingWindowManager(getContext().getApplicationContext());
        m.addView();
    }

    private Boolean isCodeInvalid(String code) {
        return code.matches("^[0-9]{6}$");
    }


    public void toggleButton(View button, boolean enable) {
        button.setEnabled(enable);
        button.setClickable(enable);
    }

    public void toggleEditText(EditText editText, boolean enable) {
        editText.setCursorVisible(enable);
        editText.setFocusableInTouchMode(enable);
    }

    @Override
    protected boolean registerForEventBus() {
        return false;
    }
}

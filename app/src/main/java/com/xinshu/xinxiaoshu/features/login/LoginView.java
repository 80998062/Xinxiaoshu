package com.xinshu.xinxiaoshu.features.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.sinyuk.myutils.string.RegexUtils;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.databinding.LoginViewBinding;
import com.xinshu.xinxiaoshu.features.reception.ReceptionActivity;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;


/**
 * Created by sinyuk on 2017/3/1.
 */
public class LoginView extends BaseFragment implements LoginViewContract.View {

    private static final int COOL_DOWN_DURATION = 60;
    private LoginViewBinding binding;


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
        Disposable d1 = RxTextView.textChanges(binding.phoneEt)
                .skip(11)
                .map(CharSequence::toString)
                .map(RegexUtils::isMobileSimple)
                .subscribe(invalid -> {
                    Log.d("onNext()", "isMobileSimple: " + invalid);
                    if (invalid) {
                        binding.phoneEt.setError(null);
                        presenter.checkRegistered(binding.phoneEt.getText().toString());
                    } else {
                        binding.phoneEt.setError(getString(R.string.hint_phone_not_correct));
                    }
                });

        addDisposable(d1);

//        binding.authcodeEt.addTextChangedListener(new TextWatcherAdapter() {
//
//            @Override
//            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
//                isCodeInvalid = isCodeInvalid(text.toString());
//                toggleButton(binding.loginBtn, isCodeInvalid);
//            }
//
//        });

        Disposable d2 = RxView.clicks(binding.authcodeBtn)
                .throttleFirst(COOL_DOWN_DURATION, TimeUnit.SECONDS)
                .subscribe(o -> {
                    presenter.getCaptcha(
                            binding.phoneEt.getText().toString(), COOL_DOWN_DURATION);
                    toggleButton(binding.authcodeBtn, false);
                });

        addDisposable(d2);

        Disposable d3 = RxTextView.textChanges(binding.authcodeEt)
                .skip(4)
                .map(CharSequence::toString)
                .map(this::isCodeInvalid)
                .subscribe(invalid -> toggleButton(binding.loginBtn, invalid));

        addDisposable(d3);

        toggleButton(binding.loginBtn, false);
        toggleButton(binding.authcodeBtn, false);
        toggleEditText(binding.authcodeEt, false);

        binding.loginBtn.setOnClickListener(this::onLogin);
    }


    private void onLogin(final View view) {
        presenter.login(
                binding.phoneEt.getText().toString(),
                binding.authcodeEt.getText().toString());
    }

    private Boolean isCodeInvalid(String code) {
        return code.matches("^[0-9]{4}$");
    }


    /**
     * Toggle button.
     *
     * @param button the button
     * @param enable the enable
     */
    public void toggleButton(View button, boolean enable) {
        button.setEnabled(enable);
        button.setClickable(enable);
    }

    /**
     * Toggle edit text.
     *
     * @param editText the edit text
     * @param enable   the enable
     */
    public void toggleEditText(EditText editText, boolean enable) {
        editText.setCursorVisible(enable);
        editText.setFocusableInTouchMode(enable);
    }

    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    @Override
    protected void doInjection() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }


    private LoginViewContract.Presenter presenter;

    @Override
    public void setPresenter(LoginViewContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void registered() {
        toggleButton(binding.authcodeBtn, true);
    }

    @Override
    public void notRegistered() {
        toggleButton(binding.authcodeBtn, false);
        binding.phoneEt.setError(getString(R.string.hint_phone_not_registered));
    }

    @Override
    public void inCD(int countDown) {
        binding.authcodeBtn.setText(String.format("冷却中%ds", countDown));
    }

    @Override
    public void cdRefresh() {
        toggleButton(binding.authcodeBtn, true);
        binding.authcodeBtn.setText(R.string.action_get_authcode);
    }

    @Override
    public void getCaptchaSucceed() {
        toggleEditText(binding.authcodeEt, true);
        toggleButton(binding.authcodeBtn, false);
        Toast.makeText(getContext(), "已发送", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void getCaptchaFailed(Throwable e) {
        toggleEditText(binding.authcodeEt, false);
        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSucceed(UserEntity userEntity) {
        ReceptionActivity.start(getContext(), userEntity);
        getActivity().finish();
    }

    @Override
    public void loginFailed(Throwable e) {
        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}

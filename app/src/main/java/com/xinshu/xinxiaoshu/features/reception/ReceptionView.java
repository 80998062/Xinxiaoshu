package com.xinshu.xinxiaoshu.features.reception;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.myutils.ConvertUtils;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.databinding.ReceptionComingBinding;
import com.xinshu.xinxiaoshu.databinding.ReceptionViewBinding;
import com.xinshu.xinxiaoshu.features.extras.TutorialActivity;
import com.xinshu.xinxiaoshu.features.history.HistoryActivity;
import com.xinshu.xinxiaoshu.features.widthdraw.WithdrawActivity;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class ReceptionView extends BaseFragment implements ReceptionContract.View {

    public static final int STATE_OFFLINE = 0;
    public static final int STATE_ONLINE = 1;
    public static final int STATE_COME_IN = 2;
    public static final int STATE_SUCCEED = 3;
    public static final int STATE_FAILED = 4;

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

    private ReceptionViewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.reception_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListener();
    }

    private void setupListener() {


        binding.tutorial.setOnClickListener(this::onClickTutorial);
        binding.historyCount.setOnClickListener(this::gotoHistory);
        binding.withdrawAmount.setOnClickListener(this::gotoWithdraw);
        binding.footerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 下线或者上线
            }
        });

        binding.offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOffline();
            }
        });

        binding.online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOnline();
            }
        });

        binding.comming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGetReception(null);
            }
        });

        binding.failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderingResult(false);
            }
        });

        binding.succeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderingResult(true);
            }
        });

    }

    /**
     * Entries for other pages
     */

    private void gotoWithdraw(View view) {
        WithdrawActivity.start(view.getContext());
    }

    private void gotoHistory(View view) {
        HistoryActivity.start(view.getContext());
    }

    private void onClickTutorial(View view) {
        TutorialActivity.start(view.getContext());
    }

    /**
     * Entries for other pages
     */

    private ReceptionContract.Presenter presenter;

    @Override
    public void setPresenter(ReceptionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void bindPlayer(Object player) {

    }

    @Override
    public void showOffline() {
        switchViewAnimator(STATE_OFFLINE);
    }


    @Override
    public void showOnline() {
        switchViewAnimator(STATE_ONLINE);
    }

    /**
     * 改变底部按钮
     *
     * @param online
     */
    private void toggleFooter(boolean online) {
        if (binding.footerBtn.isEnabled() != online) {
            binding.footerBtn.setEnabled(online);
            final int text = online ? R.string.action_reception_turn_off : R.string.action_reception_off;
            binding.footerBtn.setText(text);
        }
    }


    @Override
    public void showGetReception(Object reception) {
        final ReceptionComingBinding comingBinding = binding.receptionComing;
        comingBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 发起抢单请求
            }
        });
        comingBinding.icon.setImageResource(R.drawable.ic_warning);
        comingBinding.fab.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_oval_red));
        comingBinding.title.setText(R.string.hint_reception_request);
        comingBinding.fab.setText(R.string.action_reception_get);
        switchViewAnimator(STATE_COME_IN);
    }


    @Override
    public void waitOrderingResult() {
        showAndStartProgress();
    }

    /**
     * 显示进度条
     */
    private void showAndStartProgress() {

    }


    private void toggleDotsView(boolean enable) {
        if (enable) {
            if (binding.receptionOnline.dotsView.isPlaying())
                binding.receptionOnline.dotsView.start();
        } else {
            if (!binding.receptionOnline.dotsView.isPlaying())
                binding.receptionOnline.dotsView.stop();
        }
    }

    @Override
    public void showOrderingResult(boolean succeed) {
        final ReceptionComingBinding comingBinding = binding.receptionComing;

        if (succeed) {
            comingBinding.icon.setImageResource(R.drawable.ic_tick_cyan);
            comingBinding.title.setText(R.string.hint_reception_succeed);
            comingBinding.fab.setText(R.string.action_reception_succeed);
            comingBinding.fab.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_oval_cyan));
            comingBinding.fab.setElevation(ConvertUtils.dp2px(getContext(), 4));
            switchViewAnimator(STATE_SUCCEED);
        } else {
            comingBinding.title.setText(R.string.hint_reception_failed);
            comingBinding.icon.setImageResource(R.drawable.ic_warning_red);
            comingBinding.fab.setText(R.string.action_reception_failed);
            comingBinding.fab.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_oval_grey));
            comingBinding.fab.setElevation(0f);
            switchViewAnimator(STATE_FAILED);
        }

        comingBinding.fab.setOnClickListener(view -> showOnline());

    }


    /**
     * 切换视图
     *
     * @param index
     */
    private void switchViewAnimator(int index) {
        switch (index) {
            case STATE_OFFLINE: {
                binding.viewAnimator.setDisplayedChildId(R.id.reception_offline);
                toggleDotsView(false);
                toggleFooter(false);
                break;
            }
            case STATE_ONLINE: {
                toggleFooter(true);
                toggleDotsView(true);
                binding.viewAnimator.setDisplayedChildId(R.id.reception_online);
                break;
            }
            case STATE_COME_IN:
            case STATE_SUCCEED:
            case STATE_FAILED: {
                binding.viewAnimator.setDisplayedChildId(R.id.reception_coming);
                toggleFooter(true);
                toggleDotsView(false);
                break;
            }
        }
    }
}

package com.xinshu.xinxiaoshu.features.reception;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sinyuk.myutils.ConvertUtils;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.databinding.ReceptionComingBinding;
import com.xinshu.xinxiaoshu.databinding.ReceptionViewBinding;
import com.xinshu.xinxiaoshu.events.OrderComingEvent;
import com.xinshu.xinxiaoshu.events.StartPollingEvent;
import com.xinshu.xinxiaoshu.events.StopPollingEvent;
import com.xinshu.xinxiaoshu.features.extras.TutorialActivity;
import com.xinshu.xinxiaoshu.features.history.HistoryActivity;
import com.xinshu.xinxiaoshu.features.upload.UploadActivity;
import com.xinshu.xinxiaoshu.features.widthdraw.WithdrawActivity;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;
import com.xinshu.xinxiaoshu.viewmodels.UserModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */
public class ReceptionView extends BaseFragment implements ReceptionContract.View {

    /**
     * The constant STATE_OFFLINE.
     */
    public static final int STATE_OFFLINE = 0;
    /**
     * The constant STATE_ONLINE.
     */
    public static final int STATE_ONLINE = 1;
    /**
     * The constant STATE_COME_IN.
     */
    public static final int STATE_COME_IN = 2;
    /**
     * The constant STATE_SUCCEED.
     */
    public static final int STATE_SUCCEED = 3;
    /**
     * The constant STATE_FAILED.
     */
    public static final int STATE_FAILED = 4;

    @Override
    protected boolean registerForEventBus() {
        return true;
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

        binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));

        binding.swipeRefreshLayout.setOnRefreshListener(() -> presenter.fetchPlayerInfo());


        if (getArguments() != null
                && getArguments().getSerializable("user") != null) {
            bindPlayer((UserEntity) getArguments().getSerializable("user"));
        } else {
            presenter.fetchPlayerInfo();
        }

    }

    private void setupListener() {
        binding.tutorial.setOnClickListener(this::onClickTutorial);
        binding.historyCount.setOnClickListener(this::gotoHistory);
        binding.withdrawAmount.setOnClickListener(this::gotoWithdraw);
        binding.footerBtn.setOnClickListener(view -> {
            boolean online = binding.footerBtn.isEnabled();
            if (online) {
                // 上线中
                presenter.offline(); // 下线休息
            } else {
                // 休息中
                //  presenter.online(); // no-op
            }
        });

        binding.receptionOffline.onlineBtn.setOnClickListener(view -> presenter.online());

        binding.offline.setOnClickListener(view -> offlineSucceed());

        binding.online.setOnClickListener(view -> onlineSucceed());

        binding.comming.setOnClickListener(view -> showGetReception(null));

        binding.failed.setOnClickListener(view -> showOrderingResult(false));

        binding.succeed.setOnClickListener(view -> showOrderingResult(true));

        binding.avatar.setOnClickListener(view -> {
        });

        binding.uploadBtn.setOnClickListener(view -> UploadActivity.start(view.getContext()));

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
    public void bindPlayer(UserEntity entity) {
        setupListener();
        binding.setData(new UserModel(entity));
    }

    @Override
    public void offlineSucceed() {
        StopPollingEvent event = new StopPollingEvent();
        event.setExecutionScope(ReceptionView.class);
        EventBus.getDefault().post(event);

        switchViewAnimator(STATE_OFFLINE);
    }

    @Override
    public void offlineFailed(Throwable e) {
        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onlineSucceed() {
        switchViewAnimator(STATE_ONLINE);
        startRequestService();
    }

    /**
     * 开始轮询
     */
    public void startRequestService() {
        StartPollingEvent event = new StartPollingEvent();
        event.setExecutionScope(ReceptionView.class);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onlineFailed(Throwable e) {
        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    public void showGetReception(final List<OrderEntity> reception) {
        final ReceptionComingBinding comingBinding = binding.receptionComing;
        comingBinding.fab.setOnClickListener(view -> {
            // TODO: 发起抢单请求
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
            binding.receptionOnline.dotsView.showAndPlay();
        } else {
            binding.receptionOnline.dotsView.hideAndStop();
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

        comingBinding.fab.setOnClickListener(view -> onlineSucceed());
    }

    @Override
    public void startRefreshing() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void refreshFailed(Throwable e) {
        binding.swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshCompleted() {
        binding.swipeRefreshLayout.setRefreshing(false);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderComing(final OrderComingEvent event) {
        if (!event.getOrderEntities().isEmpty()) {
            Log.d(getTag(), "onOrderComing: " + event.getOrderEntities().toString());

        }
    }
}

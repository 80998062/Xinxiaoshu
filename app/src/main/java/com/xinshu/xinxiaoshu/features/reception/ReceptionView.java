package com.xinshu.xinxiaoshu.features.reception;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.core.Config;
import com.xinshu.xinxiaoshu.databinding.ReceptionComingBinding;
import com.xinshu.xinxiaoshu.databinding.ReceptionViewBinding;
import com.xinshu.xinxiaoshu.events.OrderComingEvent;
import com.xinshu.xinxiaoshu.events.StartPollingEvent;
import com.xinshu.xinxiaoshu.events.StopPollingEvent;
import com.xinshu.xinxiaoshu.features.extras.TutorialActivity;
import com.xinshu.xinxiaoshu.features.upload.UploadActivity;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;
import com.xinshu.xinxiaoshu.services.PollingService;
import com.xinshu.xinxiaoshu.utils.rx.OrderDecoration;
import com.xinshu.xinxiaoshu.utils.rx.QuickAdapter;
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
    private List<OrderEntity> mReceptions = null;

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

        binding.avatar.setOnClickListener(view -> {
        });

        binding.uploadBtn.setOnClickListener(this::onUpload);

        binding.wxBtn.setOnClickListener(this::goToWechat);

        binding.swipeRefreshLayout.setEnabled(false);

    }

    /**
     * On upload.
     *
     * @param v the v
     */
    public void onUpload(View v) {
        UploadActivity.start(v.getContext());
    }

    /**
     * Entries for other pages
     */

    private void gotoWithdraw(final View view) {
//        WithdrawActivity.start(view.getContext());
    }

    private void gotoHistory(final View view) {
//        HistoryActivity.start(view.getContext());
    }

    private void onClickTutorial(final View view) {
        TutorialActivity.start(view.getContext());
    }

    /**
     * Go to wechat.
     */
    public void goToWechat(final View view) {
        Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(Config.WECHAT_PACKAGE);
        startActivity(launchIntent);
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
        Log.d(getTag(), "offlineSucceed: ");
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
        if (!isMyServiceRunning(PollingService.class)) {
            PollingService.start(getContext());
        } else {
            StartPollingEvent event = new StartPollingEvent();
            event.setExecutionScope(ReceptionView.class);
            EventBus.getDefault().post(event);
        }

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
        if (reception.isEmpty()) {
            switchViewAnimator(STATE_ONLINE);
        } else {
            switchViewAnimator(STATE_COME_IN);
            final ReceptionComingBinding comingBinding = binding.receptionComing;

            if (comingBinding.orderStacks.getAdapter() == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                layoutManager.setAutoMeasureEnabled(true);
                comingBinding.orderStacks.setLayoutManager(layoutManager);
                comingBinding.orderStacks.addItemDecoration(new OrderDecoration(getContext()));
                comingBinding.orderStacks.setHasFixedSize(true);
                comingBinding.orderStacks.setAdapter(
                        new OrderAdapter(R.layout.item_order, reception, null));
            }

            if (!reception.equals(mReceptions)) {
                mReceptions = reception;
                ((QuickAdapter) comingBinding.orderStacks.getAdapter()).setData(reception, true);
                comingBinding.fab.setVisibility(View.VISIBLE);
            }

            comingBinding.fab.setOnClickListener(view -> {
                // TODO: 发起抢单请求
                presenter.assignments();
            });
        }
    }

    private void toggleDotsView(boolean enable) {
        if (enable) {
            binding.receptionOnline.dotsView.showAndPlay();
        } else {
            binding.receptionOnline.dotsView.hideAndStop();
        }
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

    @Override
    public void assignmentSucceed(OrderEntity entity) {
        binding.receptionSucceed.setData(entity);
        binding.receptionSucceed.fab.setOnClickListener(v -> presenter.fetchPlayerInfo());
        binding.receptionSucceed.fab.setVisibility(View.VISIBLE);
        switchViewAnimator(STATE_SUCCEED);
    }

    @Override
    public void assignmentFailed() {
        Toast.makeText(getContext(), R.string.hint_reception_failed, Toast.LENGTH_SHORT).show();
        presenter.fetchPlayerInfo();
    }

    @Override
    public void assignmentError(Throwable e) {
        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        presenter.fetchPlayerInfo();
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
            case STATE_SUCCEED:
                binding.viewAnimator.setDisplayedChildId(R.id.reception_succeed);
                break;
            case STATE_COME_IN:
            case STATE_FAILED: {
                binding.viewAnimator.setDisplayedChildId(R.id.reception_coming);
                break;
            }
        }
    }


    /**
     * On order coming.
     * <p>
     * 轮询的时候收到了新的订单
     * </p>
     *
     * @param event the event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderComing(final OrderComingEvent event) {
        showGetReception(event.getOrderEntities());
    }


    /**
     * Is my service running boolean.
     *
     * @param serviceClass the service class
     * @return the boolean
     */
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

package ys.bup.lunarcalendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.adapter.FavoriteNewAdapter;
import ys.bup.lunarcalendar.bus.RxBus;
import ys.bup.lunarcalendar.common.CommUtils;
import ys.bup.lunarcalendar.common.Constants;
import ys.bup.lunarcalendar.entity.FavoriteEntity;
import ys.bup.lunarcalendar.helper.OnStartDragListener;
import ys.bup.lunarcalendar.helper.SimpleItemTouchHelperCallback;
import ys.bup.lunarcalendar.ui.DividerItemDecoration;
import ys.bup.lunarcalendar.ui.EmptyLunarDateView;
import ys.bup.lunarcalendar.ui.OverlayFavoriteDetail;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarMainAt extends BaseLoadingActivity implements OnStartDragListener {
//public class LunarMainAt extends BaseLoadingActivity {

    @BindView(R.id.lvFavorite)
//    RecyclerView lvFavorite;
    EmptyLunarDateView lvFavorite;

    @BindView(R.id.tvLunarDate)
    TextView tvLunarDate;

    @BindView(R.id.tvSolarDate)
    TextView tvSolarDate;

    @BindView(R.id.overlayFavorite)
    OverlayFavoriteDetail overlayFavorite;

    ItemTouchHelper mItemTouchHelper;

//    FavoriteAdapter adapter;
    FavoriteNewAdapter adapter;

    ArrayList<FavoriteEntity> alFavorite = new ArrayList<>();

    private RxBus rxBus;

    private Subscription subscription;

    private int selectPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_lunar_main);

        ButterKnife.bind(this);

//        adapter = new FavoriteAdapter(LunarMainAt.this, alFavorite, this);
        adapter = new FavoriteNewAdapter(LunarMainAt.this, alFavorite, cellOnClickListener);

        lvFavorite.setHasFixedSize(true);
        lvFavorite.setAdapter(adapter);
        lvFavorite.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(CommUtils.getDrawable(this, R.drawable.line_divider));
        lvFavorite.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(getBaseContext(), adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(lvFavorite);

        lvFavorite.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the empty view from the layout and set it on
        // the new recycler view
        View layEmpty = findViewById(R.id.layEmpty);
        lvFavorite.setEmptyView(layEmpty);

        setTopArea();
    }

    // List ClickListener
    FavoriteNewAdapter.CellOnClickListener cellOnClickListener = new FavoriteNewAdapter.CellOnClickListener() {
        @Override
        public void onClick(int position) {

            selectPosition = position;

            overlayFavorite.setCloseListener(closeListener, alFavorite.get(selectPosition));
            overlayFavorite.setVisibility(View.VISIBLE);

            lvFavorite.setFocusableInTouchMode(false);
        }
    };

    // Overlay CloseListener
    OverlayFavoriteDetail.CloseListener closeListener = new OverlayFavoriteDetail.CloseListener() {
        @Override
        public void onClose() {
            closeDetailView();
        }

        @Override
        public void onEdit() {

            closeDetailView();

            Intent i = new Intent(LunarMainAt.this, LunarSearchAt.class);
            i.putExtra("statusMode", Constants.STATUS_EDIT);
            i.putExtra("favoriteItem", alFavorite.get(selectPosition));
            startActivity(i);
        }
    };

    public void closeDetailView() {
        overlayFavorite.setVisibility(View.GONE);
        lvFavorite.setFocusableInTouchMode(true);
    }

    public void setTopArea() {
        // 오늘날짜 음력/양력 날짜 가져오기
        final HashMap<String, String> today = CommUtils.getTodayLunarSolar();

        tvLunarDate.setText(today.get("lunarDate"));
        tvSolarDate.setText(today.get("solarDate"));
    }

    @OnClick(R.id.ibSetting)
    public void goSettingAt() {
        startActivity(new Intent(LunarMainAt.this, SettingAt.class));
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onStart() {
        super.onStart();

        alFavorite.clear();
        getFavoriteData();
    }

    @OnClick(R.id.ibAdd)
    public void addLunarDate() {
        Intent i = new Intent(LunarMainAt.this, LunarSearchAt.class);
        startActivity(i);
    }

    public void getFavoriteData()
    {
        subscription = realm.where(FavoriteEntity.class).findAllSorted("memo", Sort.DESCENDING)
                .asObservable()
                .first()
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
                .subscribe(new Subscriber<RealmResults<FavoriteEntity>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("getFavoriteData", "onComplete");
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RealmResults<FavoriteEntity> favoriteEntities) {
                        alFavorite.addAll(favoriteEntities);
                    }
                });
    }

    @Override
    protected void onStop() {

        subscription.unsubscribe();
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        if(overlayFavorite.getVisibility() == View.VISIBLE) {
            closeDetailView();

            return;
        }

        super.onBackPressed();
    }
}

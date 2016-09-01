package ys.bup.lunarcalendar.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import io.realm.Sort;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.adapter.FavoriteAdapter;
import ys.bup.lunarcalendar.bus.RxBus;
import ys.bup.lunarcalendar.common.CommUtils;
import ys.bup.lunarcalendar.entity.FavoriteEntity;
import ys.bup.lunarcalendar.image.CameraImageEXIF;
import ys.bup.lunarcalendar.ui.DividerItemDecoration;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarMainAt extends BaseLoadingActivity {

    @BindView(R.id.lvFavorite)
    RecyclerView lvFavorite;

    FavoriteAdapter adapter;

    ArrayList<FavoriteEntity> alFavorite = new ArrayList<>();

    private RxBus rxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_lunar_main);

        ButterKnife.bind(this);

        adapter = new FavoriteAdapter(alFavorite, LunarMainAt.this);

        lvFavorite.setAdapter(adapter);
        lvFavorite.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(CommUtils.getDrawable(this, R.drawable.line_divider));
        lvFavorite.addItemDecoration(dividerItemDecoration);


//        setRxbus();
        new CameraImageTask().execute();
    }

    public class CameraImageTask extends AsyncTask<Void, Void, CameraImageEXIF> {

        @Override
        protected CameraImageEXIF doInBackground(Void... params) {
            return new CameraImageEXIF(LunarMainAt.this, rxBus, 2);
        }

        @Override
        protected void onPostExecute(CameraImageEXIF cameraImageEXIF) {
            super.onPostExecute(cameraImageEXIF);


            cameraImageEXIF.getImageInfo();

        }
    };

    public void setRxbus() {
//        rxBus = RxBus.getInstance();
//
//        rxBus.register(, rxbusCallback);
    }

//    Action1 rxbusCallback = new Action1() {
//        @Override
//        public void call(Object o) {
//
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();

        getDummyData();
    }

    @OnClick(R.id.ibAdd)
    public void addLunarDate() {
        Intent i = new Intent(LunarMainAt.this, LunarSearchAt.class);
        startActivity(i);
    }

    public void getDummyData()
    {
        alFavorite.clear();

        RealmResults<FavoriteEntity> result = realm.where(FavoriteEntity.class)
                .findAllSorted("memo", Sort.DESCENDING);

        if(result != null && result.size() > 0)
            alFavorite.addAll(result);

        adapter.notifyDataSetChanged();

    }
}

package ys.bup.lunarcalendar.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.entity.FavoriteEntity;

/**
 * Created by Albert-IM on 10/5/16.
 */

public class OverlayFavoriteDetail extends RelativeLayout {

    @BindView(R.id.tvLunarDate)
    TextView tvLunarDate;

    @BindView(R.id.tvSolarDate)
    TextView tvSolarDate;

    @BindView(R.id.tvMemo)
    TextView tvMemo;

    FavoriteEntity mFavoriteEntity;

    private OverlayFavoriteDetail.CloseListener closeListener;

    public interface CloseListener {
        public void onClose();
        public void onEdit();
    }

    public void setCloseListener(CloseListener listener, FavoriteEntity favoriteEntity) {
        this.closeListener = listener;
        this.mFavoriteEntity = favoriteEntity;

        displayView();
    }

    public OverlayFavoriteDetail(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.overlay_favorite_detail, this);

        ButterKnife.bind(this, view);
    }

    public void displayView() {

        if(mFavoriteEntity != null) {
            tvMemo.setText(mFavoriteEntity.getMemo());
            tvLunarDate.setText(mFavoriteEntity.showLunarDate());
            tvSolarDate.setText(mFavoriteEntity.showSolarDate());
        }
    }

    @OnClick(R.id.ibClose)
    public void onClickClose() {
        closeListener.onClose();
    }

    @OnClick(R.id.ibEdit)
    public void onClickEdit() {
        closeListener.onEdit();
    }


}

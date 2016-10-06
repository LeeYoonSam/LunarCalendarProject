package ys.bup.lunarcalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.entity.FavoriteEntity;
import ys.bup.lunarcalendar.helper.ItemTouchHelperAdapter;
import ys.bup.lunarcalendar.helper.ItemTouchHelperViewHolder;
import ys.bup.lunarcalendar.image.CameraImageEXIF;

/**
 * Created by Albert-IM on 9/27/16.
 */

public class FavoriteNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    public interface CellOnClickListener {
        void onClick(int position);
    }

    private CellOnClickListener cellOnClickListener;

    private Context context;
    private ArrayList<FavoriteEntity> items;

    public FavoriteNewAdapter(Context context, ArrayList<FavoriteEntity> items, CellOnClickListener cellOnClickListener) {
        this.context = context;
        this.items = items;
        this.cellOnClickListener = cellOnClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected FavoriteEntity getItem(int position) {
        return items.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FavoriteNewAdapter.ItemViewHolder(inflater.inflate(R.layout.cell_lunar_main, parent, false));
    }

    //region ViewHolder Header and Footer
    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.ivHeaderBg)
        ImageView ivHeaderBg;

        @BindView(R.id.tvHeaderTitle)
        TextView tvHeaderTitle;

        @BindView(R.id.tvHeaderDate)
        TextView tvHeaderDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FavoriteNewAdapter.ItemViewHolder) {
            final FavoriteNewAdapter.ItemViewHolder itemViewHolder = (FavoriteNewAdapter.ItemViewHolder) holder;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cellOnClickListener.onClick(position);
                }
            });

            FavoriteEntity item = getItem(position);

            int month = Integer.parseInt(item.showSolarDate().substring(6, 8));
            new FavoriteNewAdapter.CameraImageTask(context, itemViewHolder).execute(month);

            itemViewHolder.tvHeaderTitle.setText(item.getMemo());
            itemViewHolder.tvHeaderDate.setText(item.showLunarDate());
        }
    }


    public class CameraImageTask extends AsyncTask<Integer, Void, CameraImageEXIF> {

        private Context _context;
        private FavoriteNewAdapter.ItemViewHolder _itemViewHolder;

        public CameraImageTask(Context context, FavoriteNewAdapter.ItemViewHolder itemViewHolder) {
            this._context = context;
            this._itemViewHolder = itemViewHolder;
        }

        @Override
        protected CameraImageEXIF doInBackground(Integer... params) {
            return new CameraImageEXIF(context, null, params[0]);
        }

        @Override
        protected void onPostExecute(CameraImageEXIF cameraImageEXIF) {
            super.onPostExecute(cameraImageEXIF);

            Glide.with(_context).load(cameraImageEXIF.getImageInfo()).into(_itemViewHolder.ivHeaderBg);
        }
    };


    /****************************************
     * drag & swipe event
     * ****************************************/
    @Override
    public void onItemDismiss(int position) {

        if(items.size() < 1)
            return;

        Realm realm = Realm.getDefaultInstance();
        FavoriteEntity favoriteEntity = realm.where(FavoriteEntity.class)
                .equalTo("seq", items.get(position).getSeq())
                .findFirst();

        realm.beginTransaction();

        if(favoriteEntity != null)
            favoriteEntity.deleteFromRealm();

        realm.commitTransaction();
        realm.close();

        Log.d("ItemTouch onItemDismiss", "position: " + position);
        items.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        try {

            Log.d("ItemTouch onItemMove", "fromPosition: " + fromPosition + " toPosition: " + toPosition);
            Collections.swap(items, fromPosition, toPosition);
            notifyDataSetChanged();
            notifyItemMoved(fromPosition, toPosition);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}

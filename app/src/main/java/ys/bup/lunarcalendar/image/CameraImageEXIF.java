package ys.bup.lunarcalendar.image;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Singleton;

import rx.Subscription;
import ys.bup.lunarcalendar.bus.RxBus;

/**
 * Created by Albert-IM on 8/31/16.
 */
public class CameraImageEXIF {

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    private Context context;

    private final RxBus rxBus;
    private final int month;

    private Subscription mStartWorkSubscription;
    private Subscription mStopWorkSubscription;
    private boolean mRunning;

//    private ArrayList<String> thumbsDataList = new ArrayList<>();
    public ArrayList<String> thumbsIDList = new ArrayList<>();

//    @Inject
    @Singleton
    public CameraImageEXIF(Context context, RxBus rxBus, int month) {
        this.context = context;
        this.rxBus = rxBus;
        this.month = month;

        if(isSDCARDMOUNTED())
            getThumbInfo();
    }

    private boolean isSDCARDMOUNTED() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;

        return false;
    }

    public void getThumbInfo(){

        if(!isSDCARDMOUNTED())
            return;

        try {

        String[] proj = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.SIZE
        };

//        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                proj, null, null, null);

        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");

            if (imageCursor != null && imageCursor.moveToFirst()) {
                String thumbsID;
                String thumbsDateTaken;
                String thumbsImageID;
                String thumbsData;

                int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                int thumbsDateTakenCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                do {

                    thumbsID = imageCursor.getString(thumbsIDCol);
                    thumbsDateTaken = imageCursor.getString(thumbsDateTakenCol);
                    thumbsData = imageCursor.getString(thumbsDataCol);
                    thumbsImageID = imageCursor.getString(thumbsImageIDCol);
//                imgSize = imageCursor.getString(thumbsSizeCol);

                    Date takenDate = new Date(Long.parseLong(thumbsDateTaken));
                    String date = formatter.format(takenDate);
//                String date = formatter.format(new Date(System.currentTimeMillis() - Long.parseLong(thumbsDateAdded)));

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(takenDate);

                    int takeMonth = cal.get(Calendar.MONTH) + 1;

                    // 해당하는 월이 아니면 건너띄기
                    if (takeMonth != month)
                        continue;

                    Log.d("testTime", "millisecond to date: " + date + "  imagename :" + thumbsImageID);

                    if (thumbsImageID != null) {

                        if (thumbsID == null)
                            continue;

//                    if(thumbsDateAdded.)
                        thumbsIDList.add(thumbsID);
//                    thumbsDatas.add(thumbsData);
                    }
                } while (imageCursor.moveToNext());
            }
            imageCursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageInfo(){

        if(thumbsIDList.size() == 0) {
            getThumbInfo();
            return "";
        }

        int random = (int) (Math.random() * thumbsIDList.size());
        Log.d("TestCameraImage", "Test random value: " + random + " idList size: " + thumbsIDList.size());

        String thumbID = thumbsIDList.get(random);

        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, "_ID='"+ thumbID +"'", null, null);

//        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                proj, "_ID='"+ thumbID +"'", null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            if (imageCursor.getCount() > 0){
                int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imageDataPath = imageCursor.getString(imgData);
            }
        }
        imageCursor.close();

        Log.d("TestCameraImage", "Test Filter ImagePath: " + imageDataPath);

        return imageDataPath;
    }
}

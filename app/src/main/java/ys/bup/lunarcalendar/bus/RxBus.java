package ys.bup.lunarcalendar.bus;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Albert-IM on 8/31/16.
 */
public class RxBus {

    private static final RxBus INSTANCE = new RxBus();

    private final Subject<Object, Object> mBusSubject = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getInstance() {
        return INSTANCE;
    }

    public <T> Subscription register(final Class<T> eventClass, Action1<T> onNext) {
        return mBusSubject
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return o.getClass().equals(eventClass);
                    }
                })
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object obj) {
                        return (T)obj;
                    }
                })
                .subscribe((Action1) onNext);
    }

    public void post(Object event) {
        mBusSubject.onNext(event);
    }
}

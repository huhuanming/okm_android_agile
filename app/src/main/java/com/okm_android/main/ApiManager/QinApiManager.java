package com.okm_android.main.ApiManager;

import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.RestaurantBackData;
import com.okm_android.main.Model.RestaurantMenu;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by QYM on 14-10-11.
 */
public class QinApiManager extends MainApiManager{

    private static final QinApiInterface.ApiManagerVerificationCode VerificationCodeapiManager = restAdapter.create(QinApiInterface.ApiManagerVerificationCode.class);
    public static Observable<List<RestaurantMenu>> RestaurantFood(final String restaurant_id) {
        return Observable.create(new Observable.OnSubscribeFunc<List<RestaurantMenu>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<RestaurantMenu>> observer) {
                try {
                    observer.onNext(VerificationCodeapiManager.RestaurantFood(restaurant_id));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final QinApiInterface.ApiManagerDefaultAddress defaultAddress = restAdapter.create(QinApiInterface.ApiManagerDefaultAddress.class);
    public static Observable<DefaultAddressData> defaultAddressData(final String user_id){
        return Observable.create(new Observable.OnSubscribeFunc<DefaultAddressData>(){
            public Subscription onSubscribe(Observer<? super DefaultAddressData> observer){
                try{
                    observer.onNext(defaultAddress.defaultAddressData(user_id));
                    observer.onCompleted();
                }catch (Exception e){
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}

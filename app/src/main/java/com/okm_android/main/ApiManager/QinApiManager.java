package com.okm_android.main.ApiManager;

import com.okm_android.main.Model.AddressAddData;
import com.okm_android.main.Model.AddressData;
import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.RestaurantComment;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.Model.SearchBackData;
import com.okm_android.main.Model.UploadBackData;
import com.okm_android.main.Model.WatchOrderData;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by QYM on 14-10-11.
 */
public class QinApiManager extends MainApiManager{

    private static final QinApiInterface.ApiManagerVerificationCode VerificationCodeapiManager = restAdapter.create(QinApiInterface.ApiManagerVerificationCode.class);
    public static Observable<List<RestaurantMenu>> RestaurantFood(final String restaurant_id) {
        return Observable.create(new Observable.OnSubscribe<List<RestaurantMenu>>() {
            @Override
            public void call(Subscriber<? super List<RestaurantMenu>> subscriber) {
                try {
                    subscriber.onNext(VerificationCodeapiManager.RestaurantFood(restaurant_id));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final QinApiInterface.ApiManagerDefaultAddress defaultAddress = restAdapter.create(QinApiInterface.ApiManagerDefaultAddress.class);
    public static Observable<DefaultAddressData> defaultAddressData(final String user_id,final String access_token){
        return Observable.create(new Observable.OnSubscribeFunc<DefaultAddressData>(){
            public Subscription onSubscribe(Observer<? super DefaultAddressData> observer){
                try{
                    observer.onNext(defaultAddress.defaultAddressData(user_id,access_token));
                    observer.onCompleted();
                }catch (Exception e){
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final QinApiInterface.ApiManagerPustAddress AddAddress = restAdapter.create(QinApiInterface.ApiManagerPustAddress.class);
    public static Observable<AddressAddData> addressAddData(final String user_id,final String access_token,final String shipping_user, final String shipping_address, final String phone_number) {
        return Observable.create(new Observable.OnSubscribeFunc<AddressAddData>() {
            @Override
            public Subscription onSubscribe(Observer<? super AddressAddData> observer) {
                try {
                    observer.onNext(AddAddress.addressAddData(user_id,access_token , shipping_user, shipping_address, phone_number ));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
    private static final QinApiInterface.ApiManagerAddAddress AddressData = restAdapter.create(QinApiInterface.ApiManagerAddAddress.class);
    public static Observable<List<AddressData>> addressData(final String user_id,final String access_token) {
        return Observable.create(new Observable.OnSubscribeFunc<List<AddressData>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<AddressData>> observer) {
                try {
                    observer.onNext(AddressData.AddressData(user_id,access_token));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
    private static final QinApiInterface.ApiManagerDeleteAddress UploadBackData = restAdapter.create(QinApiInterface.ApiManagerDeleteAddress.class);
    public static Observable<UploadBackData> uploadBackData(final String user_id,final String address_id,final String access_token) {
        return Observable.create(new Observable.OnSubscribeFunc<UploadBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super UploadBackData> observer) {
                try {
                    observer.onNext(UploadBackData.uploadBackData(user_id,address_id,access_token));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final QinApiInterface.ApiManagerSetDefaultAddress UploadBackDataDef = restAdapter.create(QinApiInterface.ApiManagerSetDefaultAddress.class);
    public static Observable<UploadBackData> uploadBackDataDef(final String user_id,final String address_id,final String access_token) {
        return Observable.create(new Observable.OnSubscribeFunc<UploadBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super UploadBackData> observer) {
                try {
                    observer.onNext(UploadBackDataDef.uploadBackDataDef(user_id,address_id,access_token));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final QinApiInterface.ApiManagerEditAddress UploadBackDataEdit = restAdapter.create(QinApiInterface.ApiManagerEditAddress.class);
    public static Observable<UploadBackData> uploadBackDataEdit(final String user_id,final String address_id,final String access_token,final String shipping_user, final String shipping_address, final String phone_number) {
        return Observable.create(new Observable.OnSubscribeFunc<UploadBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super UploadBackData> observer) {
                try {
                    observer.onNext(UploadBackDataEdit.uploadBackDataEdit(user_id,address_id,access_token, shipping_user, shipping_address, phone_number));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
    private static final QinApiInterface.ApiManagerSearchFood SearchBack = restAdapter.create(QinApiInterface.ApiManagerSearchFood.class);
    public static Observable<List<SearchBackData>> searchBackData(final String longitude,final String latitude,final String food_name) {
        return Observable.create(new Observable.OnSubscribeFunc<List<SearchBackData>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<SearchBackData>> observer) {
                try {
                    observer.onNext(SearchBack.searchBackData(longitude,latitude,food_name));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
    private static final QinApiInterface.ApiManagerGetOrder GetOrder = restAdapter.create(QinApiInterface.ApiManagerGetOrder.class);
    public static Observable<List<WatchOrderData>> watchOrderData(final String user_id,final String access_token,final String is_finished) {
        return Observable.create(new Observable.OnSubscribeFunc<List<WatchOrderData>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<WatchOrderData>> observer) {
                try {
                    observer.onNext(GetOrder.watchOrderData(user_id,access_token,is_finished));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
    private static final QinApiInterface.ApiManagerUpdateComment UploadBackDataComment = restAdapter.create(QinApiInterface.ApiManagerUpdateComment.class);
    public static Observable<UploadBackData> uploadBackDataComment(final String restaurant_id,final String access_token,final String title, final String comment, final String point) {
        return Observable.create(new Observable.OnSubscribeFunc<UploadBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super UploadBackData> observer) {
                try {
                    observer.onNext(UploadBackDataComment.uploadBackDataComment(restaurant_id,access_token, title, comment, point));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
    private static final QinApiInterface.ApiManagerGetComment GetComment = restAdapter.create(QinApiInterface.ApiManagerGetComment.class);
    public static Observable<List<RestaurantComment>> getComment(final String restaurant_id,final String cid,final String count,final String order) {
        return Observable.create(new Observable.OnSubscribeFunc<List<RestaurantComment>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<RestaurantComment>> observer) {
                try {
                    observer.onNext(GetComment.restaurantCommentData(restaurant_id,cid,count,order));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
}

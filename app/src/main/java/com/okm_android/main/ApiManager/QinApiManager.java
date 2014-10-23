package com.okm_android.main.ApiManager;

import com.okm_android.main.Model.AddressAddData;
import com.okm_android.main.Model.AddressData;
import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.RegisterBackData;
import com.okm_android.main.Model.RestaurantBackData;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.Model.SearchBackData;
import com.okm_android.main.Model.UploadBackData;

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
        }).subscribeOn(Schedulers.threadPoolForIO());
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
        }).subscribeOn(Schedulers.threadPoolForIO());
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
        }).subscribeOn(Schedulers.threadPoolForIO());
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
        }).subscribeOn(Schedulers.threadPoolForIO());
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
        }).subscribeOn(Schedulers.threadPoolForIO());
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
        }).subscribeOn(Schedulers.threadPoolForIO());
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
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}

package com.okm_android_agile.ApiManager;


import com.okm_android_agile.Model.RegisterBackData;
import com.okm_android_agile.Model.RestaurantBackData;
import com.okm_android_agile.Model.RestaurantDetailsBackData;
import com.okm_android_agile.Model.RestaurantOrderBackData;
import com.okm_android_agile.Model.RestaurantTypeData;
import com.okm_android_agile.Model.UploadBackData;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by chen on 14-7-19.
 */
public class ChenApiManager extends MainApiManager{

    private static final ChenApiInterface.ApiManagerVerificationCode VerificationCodeapiManager = restAdapter.create(ChenApiInterface.ApiManagerVerificationCode.class);
    public static Observable<UploadBackData> getMenuUploadBackData(final String phone_number) {
        return Observable.create(new Observable.OnSubscribeFunc<UploadBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super UploadBackData> observer) {
                try {
                    observer.onNext(VerificationCodeapiManager.getVerificationCode(phone_number));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerCreateUser CreateUserapiManager = restAdapter.create(ChenApiInterface.ApiManagerCreateUser.class);
    public static Observable<RegisterBackData> createUser(final String phone_number, final String password, final String encryption_code) {
        return Observable.create(new Observable.OnSubscribeFunc<RegisterBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super RegisterBackData> observer) {
                try {
                    observer.onNext(CreateUserapiManager.createUser(phone_number, password, encryption_code));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerLogin LoginapiManager = restAdapter.create(ChenApiInterface.ApiManagerLogin.class);
    public static Observable<RegisterBackData> Login(final String phone_number, final String password) {
        return Observable.create(new Observable.OnSubscribeFunc<RegisterBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super RegisterBackData> observer) {
                try {
                    observer.onNext(LoginapiManager.login(phone_number, password));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerLoginByOauth LoginByOauthapiManager = restAdapter.create(ChenApiInterface.ApiManagerLoginByOauth.class);
    public static Observable<RegisterBackData> LoginByOauth(final String uid, final String oauth_token, final String oauth_type) {
        return Observable.create(new Observable.OnSubscribeFunc<RegisterBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super RegisterBackData> observer) {
                try {
                    observer.onNext(LoginByOauthapiManager.loginByOauth(uid, oauth_token,oauth_type));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerRestaurantsList RestaurantsListapiManager = restAdapter.create(ChenApiInterface.ApiManagerRestaurantsList.class);
    public static Observable<List<RestaurantBackData>> RestaurantsList(final String latitude, final String longitude, final String page) {
        return Observable.create(new Observable.OnSubscribeFunc<List<RestaurantBackData>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<RestaurantBackData>> observer) {
                try {
                    observer.onNext(RestaurantsListapiManager.RestaurantsList(latitude, longitude,page));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerRestaurantsTypes RestaurantsTypesapiManager = restAdapter.create(ChenApiInterface.ApiManagerRestaurantsTypes.class);
    public static Observable<List<RestaurantTypeData>> RestaurantsTypes() {
        return Observable.create(new Observable.OnSubscribeFunc<List<RestaurantTypeData>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<RestaurantTypeData>> observer) {
                try {
                    observer.onNext(RestaurantsTypesapiManager.RestaurantsTypes());
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerRestaurantsOrders RestaurantsOrdersapiManager = restAdapter.create(ChenApiInterface.ApiManagerRestaurantsOrders.class);
    public static Observable<RestaurantOrderBackData> RestaurantsOrders(final String restaurant_id, final String access_token, final String foods,
                                                                        final String ship_type,final String order_type,final String shipping_user,
                                                                        final String shipping_address,final String phone_number) {
        return Observable.create(new Observable.OnSubscribeFunc<RestaurantOrderBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super RestaurantOrderBackData> observer) {
                try {
                    observer.onNext(RestaurantsOrdersapiManager.RestaurantsOrders(restaurant_id, access_token,foods,ship_type,order_type,shipping_user,shipping_address,phone_number));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ChenApiInterface.ApiManagerRestaurantDetails RestaurantsDetailsapiManager = restAdapter.create(ChenApiInterface.ApiManagerRestaurantDetails.class);
    public static Observable<RestaurantDetailsBackData> RestaurantDetails(final String restaurant_id) {
        return Observable.create(new Observable.OnSubscribeFunc<RestaurantDetailsBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super RestaurantDetailsBackData> observer) {
                try {
                    observer.onNext(RestaurantsDetailsapiManager.RestaurantDetails(restaurant_id));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}

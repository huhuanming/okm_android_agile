package com.okm_android.main.ApiManager;


import com.okm_android.main.Model.CommentBackData;
import com.okm_android.main.Model.RegisterBackData;
import com.okm_android.main.Model.RestaurantBackData;
import com.okm_android.main.Model.RestaurantDetailsBackData;
import com.okm_android.main.Model.RestaurantOrderBackData;
import com.okm_android.main.Model.RestaurantTypeData;
import com.okm_android.main.Model.UploadBackData;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by chen on 14-7-19.
 */
public class ChenApiManager extends MainApiManager{

    private static final ChenApiInterface.ApiManagerVerificationCode VerificationCodeapiManager = restAdapter.create(ChenApiInterface.ApiManagerVerificationCode.class);
    public static Observable<UploadBackData> getMenuUploadBackData(final String phone_number) {
        return Observable.create(new Observable.OnSubscribe<UploadBackData>() {
            @Override
            public void call(Subscriber<? super UploadBackData> subscriber) {
                try {
                    subscriber.onNext(VerificationCodeapiManager.getVerificationCode(phone_number));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final ChenApiInterface.ApiManagerCreateUser CreateUserapiManager = restAdapter.create(ChenApiInterface.ApiManagerCreateUser.class);
    public static Observable<RegisterBackData> createUser(final String phone_number, final String password, final String encryption_code) {
        return Observable.create(new Observable.OnSubscribe<RegisterBackData>() {
            @Override
            public void call(Subscriber<? super RegisterBackData> subscriber) {
                try {
                    subscriber.onNext(CreateUserapiManager.createUser(phone_number, password, encryption_code));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private static final ChenApiInterface.ApiManagerLogin LoginapiManager = restAdapter.create(ChenApiInterface.ApiManagerLogin.class);
    public static Observable<RegisterBackData> Login(final String phone_number, final String password) {
        return Observable.create(new Observable.OnSubscribe<RegisterBackData>() {
            @Override
            public void call(Subscriber<? super RegisterBackData> subscriber) {
                try {
                    subscriber.onNext(LoginapiManager.login(phone_number, password));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
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
        }).subscribeOn(Schedulers.io());
    }

    private static final ChenApiInterface.ApiManagerRestaurantsList RestaurantsListapiManager = restAdapter.create(ChenApiInterface.ApiManagerRestaurantsList.class);
    public static Observable<List<RestaurantBackData>> RestaurantsList(final String latitude, final String longitude, final String page) {
        return Observable.create(new Observable.OnSubscribe<List<RestaurantBackData>>() {
            @Override
            public void call(Subscriber<? super List<RestaurantBackData>> subscriber) {
                try {
                    subscriber.onNext(RestaurantsListapiManager.RestaurantsList(latitude, longitude,page));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
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
        }).subscribeOn(Schedulers.io());
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
        }).subscribeOn(Schedulers.io());
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
        }).subscribeOn(Schedulers.io());
    }

    private static final ChenApiInterface.ApiManagerGetComments GetCommentsapiManager = restAdapter.create(ChenApiInterface.ApiManagerGetComments.class);
    public static Observable<CommentBackData> GetComments(final String restaurant_id,final String cid,final String order) {
        return Observable.create(new Observable.OnSubscribeFunc<CommentBackData>() {
            @Override
            public Subscription onSubscribe(Observer<? super CommentBackData> observer) {
                try {
                    observer.onNext(GetCommentsapiManager.GetComments(restaurant_id,cid,order));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io());
    }
}

package com.thisthat.thisthat.networking;

import com.thisthat.thisthat.models.AllCelebsModel;
import com.thisthat.thisthat.models.FetchAllModel;
import com.thisthat.thisthat.models.FetchContactListModel;
import com.thisthat.thisthat.models.FetchResultsModel;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.SpecificCelebModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderInterface {
    @GET("api/getwould")
    Call<List<WouldYouRatherModel>> getWould();

    @GET("api/getallceleb")
    Call<List<AllCelebsModel>> getAllCelebs();
    @GET("api/getalllife")
    Call<List<FetchAllModel>> getAllLife();
    @GET("api/getallfood")
    Call<List<FetchAllModel>> getAllFood();
    @GET("api/getallpartner")
    Call<List<FetchAllModel>> getAllPartner();

    @FormUrlEncoded
    @POST("api/fetchtuser")
    Call<GetUserModel> getuser(
            @Field("phone")String phone
    );

    @FormUrlEncoded
    @POST("api/registerthisthat")
    Call<MessagesModel> regUser(
            @Field("phone")String phone,
            @Field("pin")String pin
    );
    @FormUrlEncoded
    @POST("api/answerwould")
    Call<MessagesModel> answer(
            @Field("phone")String phone,
            @Field("key")String key,
            @Field("id")String id
    );
    @FormUrlEncoded
    @POST("api/getsp")
    Call<WouldYouRatherModel> speci(
            @Field("id")String id
    );
    @FormUrlEncoded
    @POST("api/getceleb")
    Call<SpecificCelebModel> specificCeleb(
            @Field("id")String id
    );

    @FormUrlEncoded
    @POST("api/selffood")
    Call<MessagesModel> selfFood(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("key")String key
    );
    @FormUrlEncoded
    @POST("api/selfpartner")
    Call<MessagesModel> selfPart(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("key")String key
    );
    @FormUrlEncoded
    @POST("api/selfceleb")
    Call<MessagesModel> selfCeleb(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("key")String key
    );
    @FormUrlEncoded
    @POST("api/selflife")
    Call<MessagesModel> selfLife(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("key")String key
    );
    //firnd
    @FormUrlEncoded
    @POST("api/friendlife")
    Call<MessagesModel> friendLife(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/friendfood")
    Call<MessagesModel> friendFood(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/friendpartner")
    Call<MessagesModel> friendPart(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/friendceleb")
    Call<MessagesModel> friendCeleb(
            @Field("phone")String phone,
            @Field("categoryId")String categoryId,
            @Field("choice")String choice,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/specificpartner")
    Call<FetchAllModel> specificPart(
            @Field("id")String categoryId
    );
    @FormUrlEncoded
    @POST("api/specificlife")
    Call<FetchAllModel> specificLife(
            @Field("id")String categoryId
    );
    @FormUrlEncoded
    @POST("api/specificfood")
    Call<FetchAllModel> specificFood(
            @Field("id")String categoryId
    );
    //results
    @FormUrlEncoded
    @POST("api/fetchmylife")
    Call<List<FetchContactListModel>> myLife(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchflife")
    Call<List<FetchContactListModel>> fLife(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchmyfood")
    Call<List<FetchContactListModel>> myFood(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchffood")
    Call<List<FetchContactListModel>> fFood(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchmyceleb")
    Call<List<FetchContactListModel>> myCeleb(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchfceleb")
    Call<List<FetchContactListModel>> fCeleb(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchmypartner")
    Call<List<FetchContactListModel>> myPartner(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchfpartner")
    Call<List<FetchContactListModel>> fPartner(
            @Field("phone")String phone
    );

    //specific results
    @FormUrlEncoded
    @POST("api/fetchmyspecificlife")
    Call<List<FetchResultsModel>> lifeTee(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/fetchfspecificlife")
    Call<List<FetchResultsModel>> lifeTor(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/fetchmyspecificfood")
    Call<List<FetchResultsModel>> foodTee(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/fetchfspecificfood")
    Call<List<FetchResultsModel>> foodTor(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/fetchmyspecificceleb")
    Call<List<FetchResultsModel>> celebTee(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/fetchfspecificceleb")
    Call<List<FetchResultsModel>> celebTor(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );

    @FormUrlEncoded
    @POST("api/fetchmyspecificpartner")
    Call<List<FetchResultsModel>> partnerTee(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );
    @FormUrlEncoded
    @POST("api/fetchfspecificpartner")
    Call<List<FetchResultsModel>> partnerTor(
            @Field("phone")String phone,
            @Field("friendPhone")String friendPhone
    );

}

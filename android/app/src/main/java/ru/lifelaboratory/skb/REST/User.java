package ru.lifelaboratory.skb.REST;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface User {

    // авторизация пользователя
    @POST("/auth")
    Call<ru.lifelaboratory.skb.Entity.User> auth(@Body ru.lifelaboratory.skb.Entity.User newUser);

    // регистрация пользователя
    @POST("/register")
    Call<ru.lifelaboratory.skb.Entity.User> register(@Body ru.lifelaboratory.skb.Entity.User newUser);


}

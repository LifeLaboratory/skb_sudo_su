package ru.lifelaboratory.skb.REST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Item {
    @GET("/search/{type}/{item}")
    Call <List<ru.lifelaboratory.skb.Entity.Item>> search(@Path("type") String type, @Path("item") String search);
}

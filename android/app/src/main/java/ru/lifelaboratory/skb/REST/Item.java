package ru.lifelaboratory.skb.REST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.lifelaboratory.skb.Entity.AddItem;
import ru.lifelaboratory.skb.Entity.DeleteItem;

public interface Item {
    @GET("/search/{type}/{item}")
    Call <List<ru.lifelaboratory.skb.Entity.Item>> search(@Path("type") String type, @Path("item") String search);

    @GET("/get_list/{page}/{id_user}")
    Call <List<ru.lifelaboratory.skb.Entity.Item>> getUserList(@Path("page") Integer page, @Path("id_user") Integer idUser);

    @GET("/get_list_expired/{id_user}")
    Call <List<ru.lifelaboratory.skb.Entity.Item>> getUserListForService(@Path("id_user") Integer idUser);

    @GET("/info/{id_item}")
    Call <List<ru.lifelaboratory.skb.Entity.Item>> info(@Path("id_item") String idItem);

    @GET("/sales/{id_user}")
    Call <List<ru.lifelaboratory.skb.Entity.Item>> sales(@Path("id_user") Integer idUser);

    @POST("/sales")
    Call <ru.lifelaboratory.skb.Entity.Item> addToSale(@Body AddItem item);

    @POST("/nomenclature")
    Call <ru.lifelaboratory.skb.Entity.Item> addToNomenclature(@Body AddItem item);

    @DELETE("/sales/{id_user}/{id_sales}")
    Call <ru.lifelaboratory.skb.Entity.Item> deleteFromSale(@Path("id_user") Integer id_user, @Path("id_sales") Integer id_sales);

    @DELETE("/nomenclature/{id_user}/{id_sales}")
    Call <ru.lifelaboratory.skb.Entity.Item> deleteFromNom(@Path("id_user") Integer id_user, @Path("id_sales") Integer id_sales);
}

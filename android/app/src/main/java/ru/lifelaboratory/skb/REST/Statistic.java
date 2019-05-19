package ru.lifelaboratory.skb.REST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Statistic {

    @GET("/statistic/top/{id_user}/{timeout}")
    Call <List<Item>> top(@Path("id_user") Integer idUser, @Path("timeout") Integer timeout);

    @GET("/statistic/all/{id_user}/{timeout}")
    Call <List<Item>> all(@Path("id_user") Integer idUser, @Path("timeout") Integer timeout);

    class Item {
        private String category;
        private Double percent;
        private Double all;
        private Double expired;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Double getPercent() {
            return percent;
        }

        public void setPercent(Double percent) {
            this.percent = percent;
        }

        public Double getAll() {
            return all;
        }

        public void setAll(Double all) {
            this.all = all;
        }

        public Double getExpired() {
            return expired;
        }

        public void setExpired(Double expired) {
            this.expired = expired;
        }
    }

}

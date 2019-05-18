package ru.lifelaboratory.skb.Entity;

public class AddItem {

    private Integer id_user;
    private Integer id_nom;
    private long expired_start;

    public AddItem(Integer idUser, Integer idNom) {
        this.id_user = idUser;
        this.id_nom = idNom;
    }

    public AddItem(Integer idUser, Integer idNom, long expiredStart) {
        this.id_user = idUser;
        this.id_nom = idNom;
        this.expired_start = expiredStart;
    }

    public Integer getIdNom() {
        return id_nom;
    }

    public void setIdNom(Integer id_nom) {
        this.id_nom = id_nom;
    }

    public Integer getIdUser() {
        return id_user;
    }

    public void setIdUser(Integer id_user) {
        this.id_user = id_user;
    }

    public long getExpiredStart() {
        return expired_start;
    }

    public void setExpiredStart(long expiredStart) {
        this.expired_start = expiredStart;
    }
}

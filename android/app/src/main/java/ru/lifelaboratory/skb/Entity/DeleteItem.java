package ru.lifelaboratory.skb.Entity;

public class DeleteItem {
        private Integer id_user;
        private Integer id_nom;
        public DeleteItem(Integer idUser, Integer idNom) {
            this.id_user = idUser;
            this.id_nom = idNom;
        }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_nom() {
        return id_nom;
    }

    public void setId_nom(Integer id_nom) {
        this.id_nom = id_nom;
    }
}

package ru.lifelaboratory.skb.Entity;

public class Item {

    private String name;
    private Integer id_nom;
    private String img;
    private String shelf_life;
    private String code;
    private String gost;
    private String weight;
    private String storage_conditions;
    private String gmo;
    private String packing;
    private String energy;

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public Item(String title) {
        this.name = title;
    }

    public Integer getId() {
        return id_nom;
    }

    public void setId(Integer id) {
        this.id_nom = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShelfLife() {
        return shelf_life;
    }

    public void setShelfLife(String shelf_life) {
        this.shelf_life = shelf_life;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGost() {
        return gost;
    }

    public void setGost(String gost) {
        this.gost = gost;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStorageConditions() {
        return storage_conditions;
    }

    public void setStorageConditions(String storage_conditions) {
        this.storage_conditions = storage_conditions;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getGmo() {
        return gmo;
    }

    public void setGmo(String gmo) {
        this.gmo = gmo;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }
}

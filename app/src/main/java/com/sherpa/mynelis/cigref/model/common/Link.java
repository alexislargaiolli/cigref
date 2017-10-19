package com.sherpa.mynelis.cigref.model.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Link implements Serializable {

    @SerializedName("href")
    private String href;

    public Link() { this(""); }

    public Link(String href) {
        this.href = href;
    }


    /**
     * Fonction d'extraction de l'id du lien
     * @return String id extrait
     */
    public String extractId() {
        //Suppression du debut de l'url pour enlever le numero de version de l'API
        String hrefSub = href.substring(6);
        hrefSub = hrefSub.substring(hrefSub.indexOf('/'));

        return hrefSub.replaceAll("[^0-9]", "");
    }

    /* Getters & setters */
    public String getHref() {
        return this.href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
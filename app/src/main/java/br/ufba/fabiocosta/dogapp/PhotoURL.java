package br.ufba.fabiocosta.dogapp;

/**
 * Created by paulatavares on 18/12/17.
 */

public class PhotoURL {
    private long id;
    private String url;
    private long dog_id;

    public PhotoURL(String url, long dog_id) {
        this.url = url;
        this.dog_id = dog_id;
    }

    public String getURl(){
        return url;
    }

    public long getDog_id(){
        return dog_id;
    }

    @Override
    public String toString() {
        return "PhotoURL{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", dog_id=" + dog_id +
                '}';
    }
}

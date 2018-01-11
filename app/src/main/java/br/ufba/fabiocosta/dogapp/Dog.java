package br.ufba.fabiocosta.dogapp;

/**
 * Created by fabiocosta on 18/11/17.
 */

public class Dog {
    private long id;
    private String breed;

    public Dog(String breed) {
        this.breed = breed;
    }

    public String getBreed() {
        return breed;
    }

    public long getId(){
        return id;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setId(long id){
        this.id = id;
    }


}

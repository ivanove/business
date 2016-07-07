package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Parameters__  extends RealmObject {

      @PrimaryKey
      private int id;

   //  private int id_params;
	private Phone_home_grid phone_home_grid;
	private int tiles_inner_padding;
	private int tiles_outer_margin;
    private String background_image;
    private Illustration illustration;

/*
    public int getId_params() {
        return id_params;
    }

    public void setId_params(int id_params) {
        this.id_params = id_params;
    }
*/

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {

        this.background_image = background_image;
        try {
            if(background_image != null && !background_image.isEmpty())
            illustration= Utils1.Download_images(background_image , illustration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public Phone_home_grid getPhone_home_grid() {
        return phone_home_grid;
    }

    public void setPhone_home_grid(Phone_home_grid phone_home_grid) {
        this.phone_home_grid = phone_home_grid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTiles_inner_padding() {
        return tiles_inner_padding;
    }

    public void setTiles_inner_padding(int tiles_inner_padding) {
        this.tiles_inner_padding = tiles_inner_padding;
    }

    public int getTiles_outer_margin() {
        return tiles_outer_margin;
    }

    public void setTiles_outer_margin(int tiles_outer_margin) {
        this.tiles_outer_margin = tiles_outer_margin;
    }

    public Parameters__() {
    }

    public Parameters__(/*int id_params,*/ Phone_home_grid phone_home_grid, int id, int tiles_inner_padding, int tiles_outer_margin) {
     //   this.id_params = id_params;
        this.phone_home_grid = phone_home_grid;
        this.id = id;
        this.tiles_inner_padding = tiles_inner_padding;
        this.tiles_outer_margin = tiles_outer_margin;
    }
}

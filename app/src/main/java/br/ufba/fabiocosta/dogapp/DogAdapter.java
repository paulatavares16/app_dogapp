package br.ufba.fabiocosta.dogapp;

/**
 * Created by fabiocosta on 18/11/17.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.MyViewHolder> {

    private List<Dog> dogsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView breed;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            breed = (TextView) view.findViewById(R.id.breed);
            icon = (ImageView) view.findViewById(R.id.icon_dog);


        }
    }


    public DogAdapter(List<Dog> dogsList) {
        this.dogsList = dogsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dog_recycler_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Dog dog = dogsList.get(position);
        holder.breed.setText(dog.getBreed());
        holder.icon.setImageResource(R.drawable.dog);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, DogPhotoActivity.class);
                intent.putExtra("title", dogsList.get(position).getBreed());
                intent.putExtra("id",dogsList.get(position).getId());
                context.startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }
}

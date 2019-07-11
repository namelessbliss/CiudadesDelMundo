package com.app.nb.ciudadesdelmundo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nb.ciudadesdelmundo.R;
import com.app.nb.ciudadesdelmundo.model.City;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private Context context;
    private List<City> cities;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonClickListener btnClickListener;

    public CityAdapter(List<City> cities, int layout, OnItemClickListener itemClickListener, OnButtonClickListener btnClickListener) {
        this.cities = cities;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.btnClickListener = btnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        context = viewGroup.getContext();
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(cities.get(position), itemClickListener, btnClickListener);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView description;
        public TextView stars;
        public ImageView image;
        public Button btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCityName);
            description = itemView.findViewById(R.id.tvCityDescription);
            stars = itemView.findViewById(R.id.tvStars);
            image = itemView.findViewById(R.id.ivCity);
            btnDelete = itemView.findViewById(R.id.btnDeleteCity);
        }

        public void bind(final City city, final OnItemClickListener itemClickListener, final OnButtonClickListener buttonClickListener) {
            name.setText(city.getName());
            description.setText(city.getDescription());
            stars.setText(city.getStars() + "");
            Picasso.get().load(city.getImage()).fit().into(image);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnClickListener.onButtonClick(city, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(city, getAdapterPosition());
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(City city, int position);
    }

    public interface OnButtonClickListener {
        void onButtonClick(City city, int position);
    }
}

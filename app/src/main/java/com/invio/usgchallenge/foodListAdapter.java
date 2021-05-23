package com.invio.usgchallenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class foodListAdapter extends RecyclerView.Adapter<foodListAdapter.CardViewObjects> {
    private List<String> AdpMealNames;
    private List<String> AdpMealThumbs;
    private List<String> AdpIdMeals;
    private String search;

    starControl control = new starControl();
    private Context context;

    public foodListAdapter(Context context, List<String> AdpMealNames, List<String> AdpMealThumbs, List<String> AdpIdMeals, String search) {
        this.context = context;
        this.AdpMealNames = AdpMealNames;
        this.AdpMealThumbs = AdpMealThumbs;
        this.AdpIdMeals = AdpIdMeals;
        this.search = search;

    }

    @NonNull
    @Override
    public CardViewObjects onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_cardview, parent, false);

        return new CardViewObjects(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewObjects holder, int position) {

        final String mealName = AdpMealNames.get(position);
        final String mealId = AdpIdMeals.get(position);
        boolean attached = control.control(context, mealId);


        if (attached)
            holder.foodStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24));

        holder.foodListTextView.setText(mealName);

        Picasso.with(holder.foodListImageView.getContext()).load(AdpMealThumbs.get(position)).into(holder.foodListImageView);
        holder.foodStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.addOrRemove(context, mealId);
                boolean temp = control.control(context, mealId);

                if (temp) {
                    holder.foodStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24));
                } else {
                    holder.foodStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_border_24));

                }

            }
        });

        holder.foodListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, foodDetail.class);
                intent.putExtra("id", mealId);
                intent.putExtra("search", search);
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return AdpIdMeals.size();
    }

    public class CardViewObjects extends RecyclerView.ViewHolder {
        public TextView foodListTextView;
        public ImageView foodListImageView, foodStar;
        public CardView foodListCardView;


        public CardViewObjects(@NonNull View itemView) {
            super(itemView);
            foodStar = itemView.findViewById(R.id.foodStar);
            foodListImageView = itemView.findViewById(R.id.foodListImageView);
            foodListTextView = itemView.findViewById(R.id.foodListTextView);
            foodListCardView = itemView.findViewById(R.id.foodListCardView);

        }
    }
}

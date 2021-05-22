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

        //Burada control sınıfımdan oluşturmuş olduğum nesne ile idsi verilen yemeğin favoride olup olmadığını kontrol ediyorum
        //Ben burada verileri SharedPreferences ile kaydetmeyi tercih ettim; starControl sayfasında da görüldüğü üzere.
        boolean attached = control.control(context, mealId);


        if (attached)//Öncesinde ben bunu favorilediysem görüntüyü renkli yıldız olarak değiştiriyorum.
            holder.foodStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24));

        holder.foodListTextView.setText(mealName);

        Picasso.with(holder.foodListImageView.getContext()).load(AdpMealThumbs.get(position)).into(holder.foodListImageView);
        holder.foodStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Yıldıza tıklanıldıgında öncelikle değişim(true/false) için addOrRemove metodunu çağırıyorum ki true ise false yapsın false ise true yapsın.
                control.addOrRemove(context, mealId);
                boolean temp = control.control(context, mealId);// Son durumdaki değeri alıyorum

                if (temp) {//True ise renkli yıldız
                    holder.foodStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24));
                } else {//false ise normal yıldızı aktarıyorum.
                    holder.foodStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_border_24));

                }

            }
        });

        holder.foodListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cardviewin üzerine tıklanıldığında sayfa geçişini yapıyorum.
                Intent intent = new Intent(context, foodDetail.class);
                intent.putExtra("id", mealId);
                intent.putExtra("search", search);//detay sayfasında sayfayı kapattıgımda foodlist sayfasının tekrar yüklenmesi için bunu burada diğer sayfaya aktarıyorum.
                context.startActivity(intent);
                ((Activity) context).finish();//bu işlem çok sağlıklı değil ama değişimi görmek için uyguluyorum.

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

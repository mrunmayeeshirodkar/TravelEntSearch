package com.example.mrunmayeeshirodkar.placesearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.squareup.picasso.Transformation;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<reviewItem> listReviewItems;

    public ReviewAdapter(List<reviewItem> listReviewItems, Context context) {
        this.listReviewItems = listReviewItems;
        this.context = context;
    }

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final reviewItem listItem = listReviewItems.get(position);
        holder.author_Name.setText(listItem.getAuthor_Name());

        if(listItem.getReview_Text().replaceAll(" ", "").isEmpty()){
            holder.review_Text.setHeight(0);
        } else {
            holder.review_Text.setText(listItem.getReview_Text());
        }


        holder.review_Date.setText(listItem.getReview_Date());
        holder.review_Stars.setRating(Float.parseFloat(listItem.getReview_Stars()));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(listItem.getAuthor_Url()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        Log.d("MainActivity",listItem.getAuthor_Profile());
        if(listItem.getAuthor_Profile() != null){
            Picasso.with(context).load(listItem.getAuthor_Profile()).resize(200,200).into(holder.author_Profile);
        }

        holder.author_Profile.getLayoutParams().height = 200;
        holder.author_Profile.getLayoutParams().width = 200;

    }

    @Override
    public int getItemCount() {
        return listReviewItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView author_Profile;
        public TextView author_Name;
        public RatingBar review_Stars;
        public TextView review_Date;
        public TextView review_Text;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.myreviewlayout);
            author_Profile = (ImageView) itemView.findViewById(R.id.author_profile);
            author_Name = (TextView) itemView.findViewById(R.id.author_name);
            review_Stars = (RatingBar) itemView.findViewById(R.id.review_stars);
            review_Date = (TextView) itemView.findViewById(R.id.review_date);
            review_Text = (TextView) itemView.findViewById(R.id.review_text);
        }
    }


}


package com.example.uaqychat.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uaqychat.R;




public class ViewHolder extends RecyclerView.ViewHolder {

    TextView textViewTitle;
    TextView textViewDescription;
    ImageView imageViewPost;
    View viewHolder;

    public ViewHolder(View view) {
        super(view);
        textViewTitle = view.findViewById(R.id.textViewTitlePostCard);
        textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
        imageViewPost = view.findViewById(R.id.imageViewPostCard);
        viewHolder = view;
    }


}

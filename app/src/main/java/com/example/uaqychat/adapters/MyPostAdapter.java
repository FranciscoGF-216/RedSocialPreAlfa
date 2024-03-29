package com.example.uaqychat.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uaqychat.R;
import com.example.uaqychat.activities.PostDetailActivity;
import com.example.uaqychat.models.Like;
import com.example.uaqychat.models.Post;
import com.example.uaqychat.providers.AuthProvider;
import com.example.uaqychat.providers.LikesProvider;
import com.example.uaqychat.providers.PostProvider;
import com.example.uaqychat.providers.UserProvider;
import com.example.uaqychat.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Post, MyPostAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;
    PostProvider mPostProvider;
    LikesProvider mLikesProvider;

    AuthProvider mAuthProvider;

    public MyPostAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {


        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();
        String relativeTime = RelativeTime.getTimeAgo(post.getTimesTamp(),context);
        holder.textViewRelativeTime.setText(relativeTime);
        holder.textViewTitle.setText(post.getTitle().toUpperCase());

        if (post.getIdUser().equals(mAuthProvider.getUid())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (post.getImage1() != null) {
            if (!post.getImage1().isEmpty()) {
                Picasso.with(context).load(post.getImage1()).into(holder.circleImageMyPost);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id",postId);
                context.startActivity(intent);

            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDelete(postId);
            }
        });



    }

    private void showConfirmDelete(String postId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar publicación")
                .setMessage("¿Quieres eliminar la publicación?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePost(postId);
                    }
                })

                .setNegativeButton("No",null)
                .show();
    }

    private void deletePost(String postId) {
        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "La publicación fue eliminada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "La publicación no fue eliminada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewRelativeTime;
        CircleImageView circleImageMyPost;
        ImageView imageViewDelete;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitleMyPost);
            textViewRelativeTime = view.findViewById(R.id.textViewRelativeTimeMyPost);
            circleImageMyPost = view.findViewById(R.id.circleImageMyPost);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteMyPost);
            viewHolder = view;
        }


    }
}

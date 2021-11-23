package com.example.uaqychat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uaqychat.R;
import com.example.uaqychat.models.Post;
import com.example.uaqychat.providers.AuthProvider;
import com.example.uaqychat.providers.ImageProvider;
import com.example.uaqychat.providers.PostProvider;
import com.example.uaqychat.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File mImageFile2;
    Button mButtonPost;
    ImageProvider mImageProvider;
    TextInputEditText mTextInputTitle;
    TextInputEditText mTextInputDescription;
    ImageView mImageViewPc;
    ImageView mImageViewPS4;
    ImageView mImageViewXBOX;
    ImageView mImageViewNINTENDO;
    TextView mTextViewCategory;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    AlertDialog mDialog;
    CircleImageView mCircleImageBack;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private  final int GALLERY_REQUEST_CODE =1;
    private  final int GALLERY_REQUEST_CODE_2 =2;
    private  final int PHOTO_REQUEST_CODE =3;
    private  final int PHOTO_REQUEST_CODE_2 =4;

    //FOTO1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    //FOTO 2
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoFile2;


    String mCategory = "";
    String mTitle = "";
    String mDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona un metodo");
        options = new CharSequence[]{"Galeria","Tomar foto"};



        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();


        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mButtonPost = findViewById(R.id.btnPost);
        mTextInputTitle = findViewById(R.id.textInputProduct);
        mTextInputDescription = findViewById(R.id.textInputDescription);
        mImageViewPc = findViewById(R.id.imageViewPc);
        mImageViewPS4 = findViewById(R.id.imageViewPS4);
        mImageViewXBOX = findViewById(R.id.imageViewXbox);
        mImageViewNINTENDO = findViewById(R.id.imageViewNintendo);
        mTextViewCategory = findViewById(R.id.textViewCategory);
        mCircleImageBack = findViewById(R.id.circleImageBack);

        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPost();
            }
        });


        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(1);

            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(2);
            }
        });

        mImageViewPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "PC";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewPS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "PS4";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewXBOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Xbox";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewNINTENDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Nintendo";
                mTextViewCategory.setText(mCategory);
            }
        });
    }

    private void selectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    if (numberImage ==1){
                    openGallery(GALLERY_REQUEST_CODE);
                    }
                    else if (numberImage == 2){
                        openGallery(GALLERY_REQUEST_CODE_2);
                    }
                }
                else if (i ==1){
                    if (numberImage ==1){
                        takePhoto(PHOTO_REQUEST_CODE);
                    }
                    else if (numberImage == 2){
                        takePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
                }
        });

        mBuilderSelector.show();

    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            }catch (Exception e){
                Toast.makeText(this, "hubo un error con el archivo" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(PostActivity.this,"com.example.uaqychat", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePictureIntent,requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
            new Date() + "_photo",
            ".jpg",
            storageDir
        );
        if (requestCode == PHOTO_REQUEST_CODE){
            mPhotoPath = "file: " + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE_2){
            mPhotoPath2 = "file: " + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void clickPost() {
        //proceso de guardado de imagenes revisar

        mTitle = mTextInputTitle.getText().toString();
        mDescription = mTextInputDescription.getText().toString();
        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            //selecciono ambas imagenes de la galeria
            if(mImageFile != null && mImageFile2 != null){
                saveImage(mImageFile,mImageFile2);
            }
            //Selecciono las dos fotos de la camara
            else if (mPhotoFile != null && mPhotoFile2 != null){
                saveImage(mPhotoFile,mPhotoFile2);
            }
            else if (mImageFile != null && mPhotoFile2 != null){
                saveImage(mImageFile,mPhotoFile2);
            }
            else if (mPhotoFile != null && mImageFile2 != null){
                saveImage(mPhotoFile,mImageFile2);

            }
            else{
                Toast.makeText(this, "Debes seleccionar dos imagenes", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveImage(File imageFile, File imageFile2) {
        mDialog.show();
        mImageProvider.save(PostActivity.this,imageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            mImageProvider.save(PostActivity.this,imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());
                                                post.setTimesTamp(new Date().getTime());

                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if (taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "La información se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(PostActivity.this, "No se logro almacenar la información", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "La imagen 2 no se logro guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });

                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Hubo un error al intentar almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearForm() {

        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.upload_image);
        mImageViewPost2.setImageResource(R.drawable.upload_image);
        mTitle = "";
        mDescription = "";
        mCategory = "";
        mImageFile = null;
        mImageFile2 = null;

    }

    private void openGallery(int requestCode) {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,requestCode);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * Seleccion de imagen desde galeria
         */
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                mPhotoFile= null;
                mImageFile = FileUtil.from(this,data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));

            }catch (Exception e){

                Log.d("Error","Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
        if(requestCode == GALLERY_REQUEST_CODE_2 && resultCode == RESULT_OK){
            try {
                mPhotoFile2= null;
                mImageFile2 = FileUtil.from(this,data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));

            }catch (Exception e){

                Log.d("Error","Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
        /**
         * Seleccion de tomar foto
         */
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            mImageFile= null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(PostActivity.this).load(mPhotoFile).into(mImageViewPost1);
        }
        /**
         * Seleccion de tomar foto2
         */
        if (requestCode == PHOTO_REQUEST_CODE_2 && resultCode == RESULT_OK){
            mImageFile2= null;
            mPhotoFile2 = new File(mAbsolutePhotoPath2);
            Picasso.with(PostActivity.this).load(mPhotoFile2).into(mImageViewPost2);
        }
    }
}
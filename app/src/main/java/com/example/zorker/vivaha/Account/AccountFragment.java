package com.example.zorker.vivaha.Account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zorker.vivaha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class AccountFragment extends Fragment {
    private Button btn_upload_photo;
    private CircleImageView imageView_profile_pic;
    private TextView tv_name,tv_gender, tv_age,tv_height_feet,tv_height_inch,tv_religion,tv_language;
    DatabaseReference mref;
    FirebaseAuth mauth;
    private static final int GALLERY = 1;
    private StorageReference mstorage;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        mauth = FirebaseAuth.getInstance();
        String uid = mauth.getUid();
        mref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        mstorage = FirebaseStorage.getInstance().getReference();
        imageView_profile_pic = (CircleImageView) view.findViewById(R.id.iv_accounts_profile_pic);
        tv_gender = (TextView) view.findViewById(R.id.tv_account_gender);
        tv_name = (TextView) view.findViewById(R.id.tv_account_profile_name);
        tv_age = (TextView) view.findViewById(R.id.tv_account_age);
        tv_height_feet = (TextView) view.findViewById(R.id.tv_account_height_feet);
        tv_height_inch = (TextView) view.findViewById(R.id.tv_account_height_inch);
        tv_religion = (TextView) view.findViewById(R.id.tv_account_religion);
        tv_language = (TextView) view.findViewById(R.id.tv_account_language);
        btn_upload_photo = (Button) view.findViewById(R.id.button_account_upload_profile_pic);
        mref.keepSynced(true);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null) {
                    tv_name.setText(dataSnapshot.child("u_fname").getValue().toString() + " " + dataSnapshot.child("u_lname").getValue().toString());
                    tv_gender.setText(dataSnapshot.child("u_gender").getValue().toString() + ",");
                    tv_age.setText(dataSnapshot.child("u_age").getValue().toString() + "");
                    tv_height_feet.setText(dataSnapshot.child("u_height_feet").getValue().toString() + "");
                    tv_height_inch.setText(dataSnapshot.child("u_height_inch").getValue().toString() + "");
                    tv_religion.setText(dataSnapshot.child("u_religion").getValue().toString());
                    tv_language.setText(dataSnapshot.child("u_language").getValue().toString());

                    String image = dataSnapshot.child("u_profile_picture").getValue().toString();
                    if (!image.equals("default")) {
                        Picasso.get().load(image).placeholder(R.mipmap.default_profile).into(imageView_profile_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"SELECT IMAGE"),GALLERY);


            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == GALLERY && resultCode == RESULT_OK)
       {

           Uri imageUri = data.getData();
           CropImage.activity(imageUri)
                   .setAspectRatio(1,1)
                   .setOutputCompressQuality(80)
                   .setMaxCropResultSize(1500,1500)
                   .start(getContext(),this);


       }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please wait. Uploading may take a little while. ");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();

                File imageFilePath = new File(resultUri.getPath());

                try {
                    Bitmap search_profile_image = new Compressor(getContext())
                            .setMaxHeight(100)
                            .setMaxWidth(100)
                            .setQuality(75)
                            .compressToBitmap(imageFilePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    search_profile_image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] bitImageData = baos.toByteArray();

                    String id = mauth.getUid();

                    StorageReference path = mstorage.child("profile_pictures").child(id+".jpg");
                    final StorageReference bitmapImagePath = mstorage.child("profile_pictures").child("search_thumbnails").child(id+".jpg");

                    path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {

                                Task<Uri> image_url= task.getResult().getMetadata().getReference().getDownloadUrl();
                                image_url.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String download_image_url = uri.toString();


                                        final UploadTask uploadTask = bitmapImagePath.putBytes(bitImageData);
                                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                                                if (task.isSuccessful())
                                                {
                                                    Task<Uri> image_bitmap_url = task.getResult().getMetadata().getReference().getDownloadUrl();
                                                    image_bitmap_url.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            final String bitmapTmagedownloadUrl = uri.toString();
                                                            Map dataMap = new HashMap<>();
                                                            dataMap.put("u_profile_picture",download_image_url);
                                                            dataMap.put("u_search_picture",bitmapTmagedownloadUrl);
                                                            mref.updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful())
                                                                    {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(getContext(), "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(getContext(), "Uploading failed, please try later", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
                                                        }
                                                    });


                                                }

                                            }
                                        });



                                    }
                                });

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

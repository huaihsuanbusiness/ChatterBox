package com.huaihsuanhuang.chatterbox.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.huaihsuanhuang.chatterbox.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference mdatabase;
    private FirebaseUser mcurrentuser;
    private CircleImageView mimage;
    private TextView mname;
    private TextView mstatus;
    private Button setting_btn_changestatus;
    private Button setting_btn_changeimage;
    private StorageReference mProfileImage;
    String current_uid;
    private ProgressBar mCAprogressbar;
    private String setting_image;
    private byte[] thumb_byte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mimage = findViewById(R.id.setting_image);
        mname = findViewById(R.id.setting_name);
        mstatus = findViewById(R.id.setting_status);
        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = mcurrentuser.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mdatabase.keepSynced(true);
        mProfileImage = FirebaseStorage.getInstance().getReference();
        mCAprogressbar = findViewById(R.id.progressbar_setting);
        mCAprogressbar.setVisibility(View.GONE);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String setting_name = dataSnapshot.child("name").getValue().toString();
                setting_image = dataSnapshot.child("image").getValue().toString();
                String setting_status = dataSnapshot.child("status").getValue().toString();
                String setting_thumb = dataSnapshot.child("thumb_image").getValue().toString();
                mname.setText(setting_name);
                mstatus.setText(setting_status);

                if (!setting_image.equals("null")) {
                    Glide.with(getApplicationContext())
                            .load(setting_image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                            .into(mimage);
                } else {
                    mimage.setImageResource(R.mipmap.empty_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        setting_btn_changeimage = findViewById(R.id.setting_btn_changeimage);
        setting_btn_changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingActivity.this);
            }
        });
        setting_btn_changestatus = findViewById(R.id.setting_btn_changestatus);
        setting_btn_changestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value = mstatus.getText().toString();
                Intent intent = new Intent(SettingActivity.this, StatusActivity.class);
                intent.putExtra("statusvalue", status_value);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri resultUri = data.getData();
            CropImage.activity(resultUri)
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(500, 500)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCAprogressbar.setVisibility(View.VISIBLE);
                Uri resultUri = result.getUri();
                File thumb_filepath = new File(resultUri.getPath());
                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxHeight(100)
                            .setMaxWidth(100)
                            .setQuality(60)
                            .compressToBitmap(thumb_filepath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    thumb_byte = baos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                final StorageReference thumb_path = mProfileImage.child("profilephoto").child("thumbphoto").child(current_uid + ".jpg");
                final StorageReference photopath = mProfileImage.child("profilephoto").child(current_uid + ".jpg");
                photopath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            photopath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    mdatabase.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                UploadTask uploadTask = thumb_path.putBytes(thumb_byte);
                                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            thumb_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {

                                                                    mdatabase.child("thumb_image").setValue(uri.toString());
                                                                    Glide.with(SettingActivity.this).load(setting_image).into(mimage);
                                                                    mCAprogressbar.setVisibility(View.GONE);
                                                                    Toast.makeText(SettingActivity.this, "Photo saved", Toast.LENGTH_LONG).show();

                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(SettingActivity.this, "error uploading", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });

                                }
                            });
                        } else {
                            Toast.makeText(SettingActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            mCAprogressbar.setVisibility(View.GONE);
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}

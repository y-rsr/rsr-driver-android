package com.ridesharerental.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.ridesharerental.adapter.Chat_detail_Adapter;
import com.ridesharerental.adapter.PdfListAdapter;
import com.ridesharerental.pojo.Chat_Bean;
import com.ridesharerental.pojo.PdfFiles;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yalantis.ucrop.util.BitmapLoadUtils.exifToDegrees;

public class Chat_detail extends Activity {

    private ListView myChatList;
    private ArrayList<Chat_Bean> MyChatARR;
    RelativeLayout Rel_back;

    Common_Loader Loader;
    SessionManager sessionManager;
    String user_id = "";
    String str_booking_number = "", str_sender_name = "";
    TextView txt_sender_name,txt_file_name;
    ImageView remove_attachment;
    LinearLayout ll_show_attachment;
    RelativeLayout Rel_send,chat_detail_cameraLAY;
    Chat_detail_Adapter adapter;

    EditText edit_message;

    String str_user_image = "";

    String str_from = "inbox";
    Dialog dialogchooser;
    PdfListAdapter pdfListAdapter;
    ArrayList<PdfFiles> pdflists;
    private File image = null;
    MultipartBody.Part filePart = null;
    int is_attachment = 0;
    private final int PERMISSION_CAMERA_REQUEST_CODE = 230;
    private final int PERMISSION_GALLERY_REQUEST_CODE = 235;
    private static int CAMERA_REQUEST_FLAG = 1021;

    private static int GALLERY_REQUEST_FLAG = 891;

    private Uri mImageCaptureUri, myOutputURI;
    private Bitmap selectedBitmap;
    File myCapturedImage, myImageRoot;
    String imagePath = "",myNameStr = "", myDirectoryNameStr = "";
    File destination, photofile;
    private static final int FILE_SELECT_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail);
        Loader = new Common_Loader(Chat_detail.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(Chat_detail.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        str_user_image = details.get(sessionManager.KEY_USER_IMAGE);
        System.out.println("------User ID-------->" + user_id);
        System.out.println("-----Profile Image--->" + str_user_image);


        myImageRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), myDirectoryNameStr);
        if (!myImageRoot.exists()) {
            myImageRoot.mkdir();
        } else if (!myImageRoot.isDirectory()) {
            myImageRoot.delete();
            myImageRoot.mkdir();
        }
        checkPermissions();
        myNameStr = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        myCapturedImage = new File(myImageRoot, myNameStr + ".jpg");

        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");


        init();

        Rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_Edit = new Intent(Chat_detail.this, Main_homepage.class);
                my_Edit.putExtra("calling_type", "inbox");
                startActivity(my_Edit);
                Chat_detail.this.finish();
            }
        });


        edit_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("---1111------->" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("---2222------->" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("---3333------->" + s);
                if (s.toString().trim().length() > 0) {
                    Rel_send.setVisibility(View.VISIBLE);
                } else {
                    Rel_send.setVisibility(View.GONE);
                }
                // do stuff
            }
        });

        Rel_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_message.getText().toString().trim().length() > 0) {
                    if(is_attachment == 0)
                        send_message();
                    else
                        sendfiletoServer();
                }

            }
        });

        chat_detail_cameraLAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageListDialogShow();

            }
        });

        remove_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_show_attachment.setVisibility(View.GONE);
                is_attachment = 0;
            }
        });
    }

    private void init() {
        MyChatARR = new ArrayList<Chat_Bean>();
        myChatList = (ListView) findViewById(R.id.chat_detail_lsitview);
        Rel_back = (RelativeLayout) findViewById(R.id.chat_detail_backLAY);
        txt_sender_name = (TextView) findViewById(R.id.txt_sender_name);
        txt_file_name = (TextView) findViewById(R.id.txt_file_name);
        remove_attachment = (ImageView) findViewById(R.id.remove_attachment);
        Rel_send = (RelativeLayout) findViewById(R.id.chat_detail_sendLAY);
        chat_detail_cameraLAY = (RelativeLayout) findViewById(R.id.chat_detail_cameraLAY);
        ll_show_attachment = (LinearLayout) findViewById(R.id.ll_show_attachment);
        Rel_send.setVisibility(View.GONE);
        edit_message = (EditText) findViewById(R.id.chat_detail_msgEDTXT);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            str_booking_number = bundle.getString("booking_no");
            str_sender_name = bundle.getString("sender_name");
            if(getIntent().hasExtra("from"))
                str_from = getIntent().getStringExtra("from");
            if(str_from.equalsIgnoreCase("admin") || str_from.equalsIgnoreCase("direct_admin"))
                chat_detail_cameraLAY.setVisibility(View.VISIBLE);
            else
                chat_detail_cameraLAY.setVisibility(View.GONE);
            txt_sender_name.setText(str_sender_name);
            load_data();
        }
    }

    public void load_data() {
        Loader.show();
        MyChatARR.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("booking_no", str_booking_number);
        System.out.println("------Booking Number------->" + str_booking_number);
        System.out.println("------commonId------->" + user_id);
        Set keys = post.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) post.get(key);

            System.out.println("" + key + ":" + value);
        }
        Call<ResponseBody> call;
        if(str_from.equalsIgnoreCase("admin"))
            call = apiService.admin_message(header, post);
        else if(str_from.equalsIgnoreCase("direct_owner"))
            call = apiService.direct_message(header, post);
        else if(str_from.equalsIgnoreCase("direct_admin"))
            call = apiService.direct_admin_message(header, post);
        else
            call = apiService.show_chat_message(header, post);

        System.out.println("-----------loading url------>" + call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj = object.getJSONObject("responseArr");

                        JSONObject common_array = object.getJSONObject("commonArr");
                        String str_profile_image = common_array.getString("profile_pic");
                        String str_msg_coutn = common_array.getString("unread_message_count");
                        sessionManager.set_msg_count(str_msg_coutn);
                        sessionManager.set_profile(str_profile_image);
                        String str_status_code = response_obj.getString("status");

                        if (str_status_code.equals("1")) {
                            JSONArray array = response_obj.getJSONArray("messages");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    Chat_Bean bean = new Chat_Bean();
                                    bean.setId(obj.getString("id"));
                                    bean.setMessage(obj.getString("message"));
                                    bean.setSenderId(obj.getString("senderId"));
                                    bean.setReceiverId(obj.getString("receiverId"));
                                    bean.setDateAdded(obj.getString("dateAdded"));
                                    bean.setSender_name(obj.getString("sender_name"));
                                    bean.setReceiver_name(obj.getString("receiver_name"));
                                    bean.setSender_pic(obj.getString("sender_pic"));
                                    bean.setReceiver_pic(obj.getString("receiver_pic"));
                                    bean.setAttachment(obj.has("attachment") && !obj.getString("attachment").equals("")&& obj.getString("attachment").length()>0 ? obj.getString("attachment") :"");
                                    Log.e("setAttachment",bean.getAttachment());
                                    MyChatARR.add(bean);
                                    //Collections.reverse(MyChatARR);
                                }
                            }
                        }
                        if (MyChatARR.size() > 0) {
                            Collections.reverse(MyChatARR);
                            adapter = new Chat_detail_Adapter(Chat_detail.this, MyChatARR);
                            myChatList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            // myChatList.setAdapter(new Chat_detail_Adapter(Chat_detail.this,MyChatARR));
                        }
                    }
                } catch (Exception e) {
                    Loader.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }

    //-------------------------------------


    public void send_message() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("booking_no", str_booking_number);
        post.put("message", edit_message.getText().toString());

        System.out.println("------Booking number-------->" + str_booking_number);
        System.out.println("-----Message-------->" + edit_message.getText().toString());

        Call<ResponseBody> call;
        if(str_from.equalsIgnoreCase("admin"))
            call = apiService.send_admin_message(header, post);
        else if(str_from.equalsIgnoreCase("direct_owner"))
            call = apiService.send_direct_message(header, post);
        else if(str_from.equalsIgnoreCase("direct_admin"))
            call = apiService.send_direct_admin_message(header, post);
        else
            call = apiService.send_message(header, post);

        System.out.println("-----------loading url------>" + call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----send_direct_admin_message Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj = object.getJSONObject("responseArr");
                        ;


                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1")) {
                            Date curDate = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                            String DateToStr = format.format(curDate);
                            System.out.println("----Date added----->" + DateToStr);

                            Chat_Bean bean = new Chat_Bean();
                            bean.setMessage(edit_message.getText().toString());
                            if (user_id != null) {
                                bean.setSenderId(user_id);
                            }

                            bean.setDateAdded(DateToStr);
                            if (str_user_image != null && !str_user_image.equals("")) {
                                System.out.println("--------User Image-------->" + str_user_image);
                                bean.setSender_pic(str_user_image);
                            }

                            MyChatARR.add(bean);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            } else {
                                if (MyChatARR.size() > 0) {
                                    Collections.reverse(MyChatARR);
                                    adapter = new Chat_detail_Adapter(Chat_detail.this, MyChatARR);
                                    myChatList.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            edit_message.setText("");

                            /*Intent intent = getIntent();
                            overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(intent);
                            edit_message.setText("");*/

                        } else {

                        }
                    }
                } catch (Exception e) {
                    Loader.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }
    private void checkPermissions() {


        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkReadExternalStoragePermission()) {
                requestGalleryPermission();
            } else {
                getPdf();
            }
        }else
        {
            getPdf();
        }
    }
    private boolean checkReadExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void getPdf() {
        pdflists = new ArrayList<PdfFiles>();
        String path =Environment.getExternalStorageDirectory().toString()+ File.separator + Environment.DIRECTORY_DOWNLOADS;



        File file = new File(path);
        String mRootPath = file.getAbsoluteFile().getPath();




        File filesInDirectory[] = file.listFiles();


        if (filesInDirectory != null) {
            for (int i = 0; i<filesInDirectory.length;i++) {
                if(filesInDirectory[i].getAbsolutePath().contains("pdf")) {
                    PdfFiles pdfFiles = new PdfFiles();
                    pdfFiles.setName(filesInDirectory[i].getName());
                    pdfFiles.setPath(filesInDirectory[i].getAbsolutePath());
                    pdflists.add(pdfFiles);
                }
                Log.e("getAbsolutePath",filesInDirectory[i].getAbsolutePath());
                try {
                    Log.e("getCanonicalPath",filesInDirectory[i].getCanonicalPath());
                    Log.e("getName",filesInDirectory[i].getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void ImageListDialogShow() {

        dialogchooser  = new Dialog(Chat_detail.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogchooser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogchooser.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogchooser.setContentView(R.layout.img_list_shoe_dialog);


        RelativeLayout Rl_close = (RelativeLayout) dialogchooser.findViewById(R.id.Rl_close);
        RelativeLayout Rl_choose = (RelativeLayout) dialogchooser.findViewById(R.id.Rl_choose);
        RelativeLayout Rl_choose_img = (RelativeLayout) dialogchooser.findViewById(R.id.Rl_choose_img);
        TextView Tv_title = (TextView) dialogchooser.findViewById(R.id.Tv_title);

        Rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogchooser.dismiss();


            }
        });


        Rl_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showFileChooser();

            }
        });

        Rl_choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog_popup();

            }
        });
        dialogchooser.show();

    }
    private void chooseImageFromGallery() {
        Intent aIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(aIntent, GALLERY_REQUEST_FLAG);
    }

    private boolean checkAccessFineLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWriteExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALLERY_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //set_process();
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestCameraPermission();
                }
                break;
            case PERMISSION_GALLERY_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //set_process();

                    chooseImageFromGallery();

                } else {
                    requestGalleryPermission();
                }
                break;
        }
    }
    private void dialog_popup() {

        final Dialog  dialog = new Dialog(Chat_detail.this);
        dialog.setContentView(R.layout.listing_profile_upload_dialog);
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        LinearLayout lin_take_photo = (LinearLayout) dialog.findViewById(R.id.lin_tak_photo);
        LinearLayout lin_chose_photo = (LinearLayout) dialog.findViewById(R.id.lin_choose_photo);
        LinearLayout lin_cancel = (LinearLayout) dialog.findViewById(R.id.lin_cancel);

        lin_chose_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    if (!checkAccessFineLocationPermission() || !checkWriteExternalStoragePermission()) {
                        requestGalleryPermission();
                    } else {
                        chooseImageFromGallery();
                    }
                } else {
                    chooseImageFromGallery();
                }

                dialog.dismiss();
            }
        });

        lin_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!checkAccessFineLocationPermission() || !checkWriteExternalStoragePermission()) {
                    requestCameraPermission();

                } else {

                    if (Build.VERSION.SDK_INT >= 21) {
                        try {
                            dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        chooseImageFromCamera();
                    }
                }
                dialog.dismiss();
            }
        });

        lin_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void chooseImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(myCapturedImage));
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takePictureIntent, CAMERA_REQUEST_FLAG);
    }
    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    private File createImageFile1() throws IOException {
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        File ranjiht = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        return ranjiht;
    }
    private File createImageFile() throws IOException {
        String name = String.valueOf(System.currentTimeMillis());
        File ranjiht = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");

        return ranjiht;
    }
    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //destination=createImageFile();

                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile1());

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                System.out.println("--------original--------->" + myCapturedImage.getAbsolutePath());
                System.out.println("--------duplicate--------->" + photoURI.getPath());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_FLAG);

            }
        }
    }

    private String getRealPathFromURI(Uri mImageCaptureUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(mImageCaptureUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return mImageCaptureUri.getPath();
        }
    }
    private void showFileChooser() {

        dialogchooser.dismiss();
        ShowPdfLists();


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        // intent.setType("image*//**//*");
//        intent.setType("application/pdf");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult(
//                    intent,
//                    FILE_SELECT_CODE);
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    private void ShowPdfLists() {

        final Dialog dialog = new Dialog(Chat_detail.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.show_pdf_dialog);



        final ExpandableHeightListView listview = (ExpandableHeightListView) dialog.findViewById(R.id.Lv_pdf);

        pdfListAdapter = new PdfListAdapter(Chat_detail.this, pdflists);
        listview.setAdapter(pdfListAdapter);
        listview.setExpanded(false);
        pdfListAdapter.notifyDataSetChanged();

        pdfListAdapter.SetonClickLinsers(new PdfListAdapter.OnClickItem() {
            @Override
            public void OnClickListners(int position, String path) {
                //Loader.show();
                Log.e("Selected pdffile",pdflists.get(position).getName());
                Log.e("Selected pdffile path",pdflists.get(position).getPath());
                image = new File(path);
                txt_file_name.setText(pdflists.get(position).getName());
                ll_show_attachment.setVisibility(View.VISIBLE);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
                // MultipartBody.Part is used to send also the actual file name

                filePart = MultipartBody.Part.createFormData("attachment", image.getName(), requestFile);

                is_attachment = 1;
                dialog.dismiss();
//                HashMap<String, String> header = new HashMap<>();
//                header.put("commonId", user_id);
//
//                HashMap<String, RequestBody> post_values = new HashMap<>();
//                post_values.put("booking_no", RequestBody.create(MediaType.parse("multipart/form-data"), Str_bookingId));
//
//                System.out.println("-------booking_no-------" + Str_bookingId);
//                System.out.println("-------commonId-------" + user_id);
//
//                ApiInterface apiService =
//                        ApiClient.getClient().create(ApiInterface.class);
//
//                callupload = apiService.due_pdf_upload(header, filePart, post_values);
//
//                System.out.println("-------Current upload url--------------" + callupload.request().url().toString());


//                callupload.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                        String str_return_status = "";
//
//                        try {
//
//                            if (response.isSuccessful()) {
//
//
//                                String Str_response = response.body().string();
//
//                                System.out.println("--------Due pdf upload--response---" + Str_response);
//                                JSONObject object = new JSONObject(Str_response);
//
//                                JSONObject responseArr = object.getJSONObject("responseArr");
//
//                                str_return_status = responseArr.getString("status");
//
//                                if (str_return_status.equalsIgnoreCase("1")) {
//
//                                    JSONArray docArr = responseArr.getJSONArray("imageArr");
//                                    if(docArr.length()>0)
//                                    {
//                                        due_pdf_list.clear();
//                                        for(int i=0;i<docArr.length();i++)
//                                        {
//                                            JSONObject documents = docArr.getJSONObject(i);
//                                            AddclaimsImagepojo ap = new AddclaimsImagepojo();
//                                            ap.setId(documents.getString("id"));
//                                            ap.setImage_url(documents.getString("image"));
//                                            due_pdf_list.add(ap);
//                                        }
//                                        dialog.dismiss();
//                                        ImageListDialogShow();
//                                        imagadapter = new AddClaimsImageListAdapter(EditDueFromDriver.this, due_pdf_list);
//                                        listview.setAdapter(imagadapter);
//                                        listview.setExpanded(false);
//                                        imagadapter.notifyDataSetChanged();
//                                        performdeleteImag();
//                                    }
//
//                                }
//
//                            }
//
//                            Loader.dismiss();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Loader.dismiss();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Loader.dismiss();
//                        String str_server_erro = t.getMessage();
//                        System.out.println("----Failure----->" + str_server_erro);
//                    }
//                });
            }
        });


        dialog.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            dialogchooser.dismiss();
            if ((requestCode == CAMERA_REQUEST_FLAG || requestCode == UCrop.REQUEST_CROP)) {
                try {
                    Log.e("Data",data+"");
                    if (requestCode == CAMERA_REQUEST_FLAG) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        imagePath = destination.getAbsolutePath();
                        //is_attachment = 1;
                        mImageCaptureUri = Uri.fromFile(new File(imagePath));
                        Log.e("selectedImagePath>", "" + imagePath);

                        String path = getRealPathFromURI(mImageCaptureUri);
                        File curFile = new File(path);
                        try {
                            ExifInterface exif = new ExifInterface(curFile.getPath());
                            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int rotationInDegrees = exifToDegrees(rotation);

                            Matrix matrix = new Matrix();
                            if (rotation != 0f) {
                                matrix.preRotate(rotationInDegrees);
                            }
                            //    thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
                        } catch (IOException ex) {
                            Log.e("TAG", "Failed to get Exif data", ex);
                        }

                        Uri picUri = Uri.fromFile(curFile);

                        UCrop.Options Uoptions = new UCrop.Options();
                        Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                        Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));


                        UCrop.of(picUri, picUri)
                                .withAspectRatio(15, 12)
                                .withMaxResultSize(8000, 8000)
                                .withOptions(Uoptions)
                                .start(Chat_detail.this);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == GALLERY_REQUEST_FLAG && data != null) {

                Uri selectedImage = data.getData();
                if (selectedImage.toString().startsWith("content://com.sec.android.gallery3d.provider")) {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    final String picturePath = c.getString(columnIndex);
                    c.close();
                    File curFile = new File(picturePath);

                    Uri picUri = Uri.fromFile(curFile);

                    UCrop.Options Uoptions = new UCrop.Options();
                    Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

                    UCrop.of(picUri, picUri)
                            .withAspectRatio(15, 12)
                            .withOptions(Uoptions)
                            .withMaxResultSize(8000, 8000)
                            .start(Chat_detail.this);


                } else {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();

                    int columnIndex = c.getColumnIndex(filePath[0]);
                    final String picturePath = c.getString(columnIndex);
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    Bitmap thumbnail = bitmap; //getResizedBitmap(bitmap, 600);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    File curFile = new File(picturePath);

                    try {
                        ExifInterface exif = new ExifInterface(curFile.getPath());
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = exifToDegrees(rotation);

                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotationInDegrees);
                        }
                        if (thumbnail != null) {
                            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
                        }
                    } catch (IOException ex) {
                        Log.e("TAG", "Failed to get Exif data", ex);
                    }
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    c.close();
                    //---new-----------new image--DIR
                    if (!myImageRoot.exists()) {
                        myImageRoot.mkdir();
                    } else if (!myImageRoot.isDirectory()) {
                        myImageRoot.delete();
                        myImageRoot.mkdir();
                    }

                    final File image = new File(myImageRoot, System.currentTimeMillis() + ".jpg");
                    myOutputURI = Uri.fromFile(image);

                    System.out.println("----image---" + image.getName());

                    UCrop.Options Uoptions = new UCrop.Options();
                    Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

                    UCrop.of(selectedImage, myOutputURI)
                            .withAspectRatio(15, 12)
                            .withOptions(Uoptions)
                            .withMaxResultSize(8000, 8000)
                            .start(Chat_detail.this);

                }
            }
        }

        if (requestCode == UCrop.REQUEST_CROP && data!= null) {

            final Uri resultUri = UCrop.getOutput(data);
            String imgpath = resultUri.getPath();
            System.out.println("=======chan_image_path==========>" + imgpath);
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);


            byte[] byteArray = byteArrayOutputStream.toByteArray();


            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                Iv_photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, Iv_photo.getWidth(),
//                        Iv_photo.getHeight(), false));

            //is_attachment = 1;
            destination = new File(getPath(resultUri));
            upload_photo();



        }
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {


            Uri uri = data.getData();


            System.out.println("-------Pdf file chooser------->" + uri.toString());

            String filePath = FilePath.getPath(this,uri);
            //String filePath ="";
///

            ///
            System.out.println("----- pdf file path----------->" + filePath);

            if (filePath != null && !filePath.equals("")) {
                Loader.show();
                if (filePath.contains("pdf")) {
                    File f = new File(filePath);
                    destination = f;
                    image = new File(destination.getAbsolutePath());
                    txt_file_name.setText(image.getName());
                    ll_show_attachment.setVisibility(View.VISIBLE);
                    //is_attachment = 1;
//                    System.out.println("----------actual file path---------" + destination.getAbsolutePath());
//
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
//                    // MultipartBody.Part is used to send also the actual file name
//
//                    filePart = MultipartBody.Part.createFormData("file", image.getName(), requestFile);
//
//
//                    HashMap<String, String> header = new HashMap<>();
//                    header.put("commonId", user_id);
//
//                    HashMap<String, RequestBody> post_values = new HashMap<>();
//                    post_values.put("booking_no", RequestBody.create(MediaType.parse("multipart/form-data"), Str_bookingId));
//
//                    System.out.println("-------booking_no-------" + Str_bookingId);
//                    System.out.println("-------commonId-------" + user_id);
//
//                    ApiInterface apiService =
//                            ApiClient.getClient().create(ApiInterface.class);
//
//                    callupload = apiService.due_pdf_upload(header, filePart, post_values);
//
//                    System.out.println("-------Current upload url--------------" + callupload.request().url().toString());
//
//
//                    callupload.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                            String str_return_status = "";
//
//                            try {
//
//                                if (response.isSuccessful()) {
//
//
//                                    String Str_response = response.body().string();
//
//                                    System.out.println("--------Due pdf upload--response---" + Str_response);
//                                    JSONObject object = new JSONObject(Str_response);
//
//                                    JSONObject responseArr = object.getJSONObject("responseArr");
//
//                                    str_return_status = responseArr.getString("status");
//
//                                    if (str_return_status.equalsIgnoreCase("1")) {
//
//                                        JSONArray docArr = responseArr.getJSONArray("imageArr");
//                                        if(docArr.length()>0)
//                                        {
//                                            due_pdf_list.clear();
//                                            for(int i=0;i<docArr.length();i++)
//                                            {
//                                                JSONObject documents = docArr.getJSONObject(i);
//                                                AddclaimsImagepojo ap = new AddclaimsImagepojo();
//                                                ap.setId(documents.getString("id"));
//                                                ap.setImage_url(documents.getString("image"));
//                                                due_pdf_list.add(ap);
//                                            }
//                                            imagadapter = new AddClaimsImageListAdapter(EditDueFromDriver.this, due_pdf_list);
//                                            listview.setAdapter(imagadapter);
//                                            listview.setExpanded(false);
//                                            imagadapter.notifyDataSetChanged();
//
//                                            performdeleteImag();
//                                        }
//
//                                    }
//
//                                }
//
//                                Loader.dismiss();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Loader.dismiss();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Loader.dismiss();
//                            String str_server_erro = t.getMessage();
//                            System.out.println("----Failure----->" + str_server_erro);
//                        }
//                    });
                }


            }
//            else if (filePath.contains("cloud")) {
//
//                try {
//                   // new AsyncTaskRunner(getContentResolver().openInputStream(uri)).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }


    }
    private void upload_photo() {

       // Loader.show();


        if (destination != null) {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            Bitmap b = BitmapFactory.decodeFile(destination.getAbsolutePath());
            Bitmap out = Bitmap.createScaledBitmap(b, 600, 400, false);
            File file = new File(dir, "ride_share.png");
            FileOutputStream fOut;
            try {
                fOut = new FileOutputStream(file);
                out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                b.recycle();
                out.recycle();
            } catch (Exception e) {
            }

            image = new File(file.getAbsolutePath());
           // sendfiletoServer();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            // MultipartBody.Part is used to send also the actual file name
            txt_file_name.setText(image.getName());
            ll_show_attachment.setVisibility(View.VISIBLE);
            filePart = MultipartBody.Part.createFormData("attachment", image.getName(), requestFile);
            is_attachment = 1;

        }

    }
    private void sendfiletoServer() {

        Loader.show();
       // ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, RequestBody> post_values = new HashMap<>();
        post_values.put("booking_no", RequestBody.create(MediaType.parse("multipart/form-data"), str_booking_number));
        post_values.put("message", RequestBody.create(MediaType.parse("multipart/form-data"), edit_message.getText().toString()));

        System.out.println("-------booking_no-------" + str_booking_number);
        System.out.println("-------commonId-------" + user_id);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = null;
        if(str_from.equalsIgnoreCase("admin"))
            call = apiService.send_admin_message_attachment(header, filePart, post_values);
        else if(str_from.equalsIgnoreCase("direct_admin"))
            call = apiService.send_direct_admin_message_attachment(header, filePart, post_values);



        System.out.println("-------Current upload url--------------" + call.request().url().toString());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String str_return_status = "";
                ll_show_attachment.setVisibility(View.GONE);
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("attachment",""+ Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj = object.getJSONObject("responseArr");



                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1")) {

                            edit_message.setText("");
                            load_data();
                            is_attachment = 0;

                        } else {

                        }
                    }
                } catch (Exception e) {
                    Loader.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----Failure----->" + str_server_erro);
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            Intent my_Edit = new Intent(Chat_detail.this, Main_homepage.class);
            my_Edit.putExtra("calling_type", "inbox");
            startActivity(my_Edit);
            Chat_detail.this.finish();
            return true;
        }
        return false;
    }
}

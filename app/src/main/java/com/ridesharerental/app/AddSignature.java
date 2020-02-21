package com.ridesharerental.app;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.squareup.picasso.Picasso;
import com.williamww.silkysignature.views.SignaturePad;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddSignature extends Fragment {
    private SignaturePad mSignaturePad;
    RelativeLayout Rl_back;
    private TextView txt_signCancel,txt_signOK;

    final int PERMISSION_REQUEST_CODE = 111;
    int is_signed = 0;

    Common_Loader Loader;
    ConnectionDetector cd;
    SessionManager session;
    MultipartBody.Part filePart = null;
    String str_userid;
    ImageView img;

    public AddSignature() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_signature, container, false);
        init(view);
        txt_signOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                try {
                    if(is_signed == 1)
                        uploadSignature(savebitmap(signatureBitmap));
                    else
                        Alert(getActivity().getString(R.string.action_opps),getActivity().getString(R.string.alert_signature));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        txt_signCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                is_signed = 0;
            }
        });
        return view;
    }

    private void init(View view) {

        mSignaturePad = (SignaturePad) view.findViewById(R.id.signature_pad);
        txt_signCancel = (TextView) view.findViewById(R.id.txt_signCancel);
        img = (ImageView) view.findViewById(R.id.img);
        txt_signOK = (TextView) view.findViewById(R.id.txt_signOK);
        cd = new ConnectionDetector(getActivity());
        session = new SessionManager(getActivity());
        Loader = new Common_Loader(getActivity());

        HashMap<String,String> userDetails = session.getUserDetails();
        str_userid = userDetails.get(SessionManager.KEY_USER_ID);

//        if(userDetails.get(SessionManager.KEY_USER_SIGN) != null && !userDetails.get(SessionManager.KEY_USER_SIGN).equals("") && userDetails.get(SessionManager.KEY_USER_SIGN).length()>0 && userDetails.get(SessionManager.KEY_USER_SIGN).contains("http"))
//        {
//            Picasso.with(getActivity()).load(userDetails.get(SessionManager.KEY_USER_SIGN)).into(img);
//        }
        Load_Data();
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (!checkAccessCameraPermission() || !checkWriteExternalStoragePermission() || !checkReadExternalStoragePermission()) {
                requestPermission();
            } else {
                AppProcessing();

            }
        } else {

            AppProcessing();
        }
        view.setFocusableInTouchMode(true);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_BACK) {

                    showBackPressedDialog();

                    System.out.println("------back pressed-----------------");

                    return true;

                }


                return true;
            }
        });

    }
    private boolean checkAccessCameraPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private boolean checkWriteExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkReadExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // need to check previous version api in playstore

                    AppProcessing();

                } else {

                    //finish();

                }
                break;
        }
    }

    private void AppProcessing() {
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                txt_signOK.setEnabled(true);
                txt_signCancel.setEnabled(true);
                is_signed = 1;

            }

            @Override
            public void onClear() {
                txt_signOK.setEnabled(true);
                txt_signCancel.setEnabled(true);
                is_signed = 0;
            }
        });
    }




    private void uploadSignature(File f) {

        Loader.show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        // MultipartBody.Part is used to send also the actual file name


        filePart = MultipartBody.Part.createFormData("signature_image", f.getName(), requestFile);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", str_userid);


        Call<ResponseBody> call = apiService.upload_driver_sign(header, filePart);
        System.out.println("-----------upload_driver_sign url------>" + call.request().url().toString());



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
                        Log.e("---- Image-->","upload_driver_sign:"+ Str_response);
                        JSONObject json = new JSONObject(Str_response);
                        JSONObject jsonObject = json.getJSONObject("responseArr");
                        if(jsonObject.getString("status").equals("1"))
                        {
                            Picasso.with(getActivity()).load(jsonObject.getString("signature_image")).into(img);
                        }
                        Intent val = new Intent(getActivity(), Main_homepage.class);
                        val.putExtra("calling_type", "signature");
                        startActivity(val);

                    }
                } catch (Exception e) {
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



    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public File savebitmap(Bitmap bmp) throws IOException {
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
//        File f = new File(Environment.getExternalStorageDirectory()
//                + File.separator + "sign"+name+".jpg");
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + "sign"+name+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        Log.e("sign path",f.getPath());
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
    public void Load_Data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", str_userid);
        Call<ResponseBody> call = apiService.show_driver_profile(header);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful())
                    {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        Log.e("----profile Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        JSONObject common_obj=object.getJSONObject("commonArr");
                        String status_code=response_obj.getString("status");
                        if(status_code!=null && !status_code.equals("") && status_code.equals("1"))
                        {

                        }
                        Picasso.with(getActivity()).load(common_obj.getString("signature_image")).into(img);
                        //signature_image

                    }
                    else
                    {
                       // linear_empty.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    //linear_empty.setVisibility(View.VISIBLE);
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
    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(getActivity());
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }
    private void showBackPressedDialog() {
        System.gc();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_app_exiting)
                .setPositiveButton(getResources().getString(R.string.navigation_drawer_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getActivity().finishAffinity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.navigation_drawer_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialog.dismiss();

                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }
}

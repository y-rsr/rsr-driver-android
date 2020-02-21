package com.ridesharerental.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Documents_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.DownloadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CAS59 on 1/10/2018.
 */

public class Document_Adapter extends BaseAdapter {

    ArrayList<Documents_Bean> mypriceARR;
    LayoutInflater mlayout;
    Context ctx;
    String currSym="";
    String cat = "";
    Common_Loader loader;
    public Document_Adapter(Context priceBreakdown, ArrayList<Documents_Bean> priceARR,String cat)
    {
        this.ctx = priceBreakdown;
        this.mypriceARR=priceARR;
        this.mlayout = LayoutInflater.from(ctx);
        loader=new Common_Loader(ctx);
        this.cat = cat;
    }



    @Override
    public int getCount() {
        return mypriceARR.size();
    }

    @Override
    public Object getItem(int i) {
        return mypriceARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolderPricing holder;

        if (view == null) {
            holder = new ViewHolderPricing();
            view = mlayout.inflate(R.layout.document_adapter_layout, null);
            holder.txt_label=(TextView)view.findViewById(R.id.txt_doc_name);
            holder.img_download=(ImageView)view.findViewById(R.id.img_download);
            view.setTag(holder);

        } else {
            holder = (ViewHolderPricing) view.getTag();
        }

        if(cat.equals("bookingDocs")) {
            //holder.txt_label.setText(mypriceARR.get(i).getLink().substring(mypriceARR.get(i).getLink().lastIndexOf('/'), mypriceARR.get(i).getLink().length()));
            holder.txt_label.setText(mypriceARR.get(i).getLink());
        }
        else
            holder.txt_label.setText(mypriceARR.get(i).getLabel());

        holder.img_download.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //getRetrofitImage(mypriceARR.get(i).getLink());
               // new DownloadTask(ctx, "https://www.tutorialspoint.com/android/android_tutorial.pdf");
                if(mypriceARR.get(i).getLink()!=null && !mypriceARR.get(i).getLink().equals(""))
                {
                    new DownloadTask(ctx, mypriceARR.get(i).getLink());
                    downloadPdfFile( mypriceARR.get(i).getLink());
                }
            }
        });

     return view;
    }

    class ViewHolderPricing
    {
        TextView txt_label;
        ImageView img_download;
    }



   public void getRetrofitImage(String url)
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.profilePicture("https://www.tutorialspoint.com/android/android_tutorial.pdf");
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                loader.dismiss();
                try {
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        boolean writtenToDisk = DownloadImage(response.body());
                        Toast.makeText(ctx,"Document downloaded and check your download path.",Toast.LENGTH_SHORT).show();
                        Log.d("", "file download was a success? " + writtenToDisk);
                    }
                } catch (Exception e) {
                    loader.dismiss();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }


    private void downloadPdfFile(String link) {
        // URL of the pdf file
        //String url_to_pdf = "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
        String url_to_pdf = link;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_to_pdf));
        request.setDescription(url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1));
        request.setTitle(url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1));
// in order for this if to run, you must use the android 3.2 to compile your app o
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1)); // set the file name to save in local mobile

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }


    private boolean DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(ctx.getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
                int c;

                while ((c = in.read()) != -1)
                {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return false;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            int width, height;
            Bitmap bMap = BitmapFactory.decodeFile(ctx.getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
            width = 2*bMap.getWidth();
            height = 6*bMap.getHeight();
            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
            System.out.println("--------Bitmap Image-------->"+bMap2.toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String EnString = Base64.encodeToString(b,Base64.DEFAULT);


            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            File dir = new File(SDCardRoot.getAbsolutePath()+ "/Download" + "/Rideshare");
            //Download the Image In Sdcard
            dir.mkdirs();
            try
            {
                if (!SDCardRoot.equals(""))
                {
                    String filename = "img"+ new Date().getTime() + ".png";
                    File file = new File(dir, filename);
                    if (file.createNewFile())
                    {
                        file.createNewFile();
                    }
                    if (EnString != null)
                    {
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] decodedString = android.util.Base64.decode(EnString,
                                android.util.Base64.DEFAULT);
                        fos.write(decodedString);
                        fos.flush();
                        fos.close();
                    }
                }

        } catch (Exception e) {
        }
            //  image.setImageBitmap(bMap2);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }
}

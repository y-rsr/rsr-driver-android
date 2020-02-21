package com.ridesharerental.adapter;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.app.ChatImageViewActivity;
import com.ridesharerental.app.Chat_detail;
import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Chat_Bean;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.CircleImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ridesharerental.app.R.id.layout_inflate_chatadapt_usermsg;

/**
 * Created by CAS59 on 12/6/2017.
 */

public class Chat_detail_Adapter extends BaseAdapter {

    private ArrayList<Chat_Bean> My_ChatARR;

    private Context Mycontext;
    private LayoutInflater myinflater;
    private Animation animShow, animHide;
    SessionManager sessionManager;
    String user_id = "";
    int file_downloadng = 0;
    public Chat_detail_Adapter(Chat_detail chat_detail, ArrayList<Chat_Bean> myChatARR) {

        this.Mycontext = chat_detail;
        this.myinflater = LayoutInflater.from(chat_detail);
        this.My_ChatARR = myChatARR;
        notifyDataSetChanged();
    }

   

    @Override
    public int getCount() {
        return My_ChatARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_ChatARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolderChat holder;

        if (view == null) {
            holder = new ViewHolderChat();
            view = myinflater.inflate(R.layout.layout_inflator_chatadapt, null);
            holder.Rel_Left=(LinearLayout)view.findViewById(R.id.layout_inflate_chatadapt_userLAY);
            holder.Rel_Right=(LinearLayout)view.findViewById(R.id.layout_inflate_chatadapt_hostLAY);
            holder.Rel_txt_attachment_top=(RelativeLayout)view.findViewById(R.id.rl_txt_attachment_top);
            holder.Rel_txt_attachment=(RelativeLayout)view.findViewById(R.id.rl_txt_attachment);

            holder.txt_left_message=(TextView)view.findViewById(layout_inflate_chatadapt_usermsg);
            holder.txt_left_date=(TextView)view.findViewById(R.id.txt_left_date);
            holder.txt_attachment_top=(TextView)view.findViewById(R.id.txt_attachment_top);
            holder.profile_left_image=(CircleImageView)view.findViewById(R.id.layout_inflate_chatadapt_userprofile_left);


            holder.txt_right_message=(TextView)view.findViewById(R.id.layout_inflate_chatadapt_hostmsg);
            holder.txt_right_date=(TextView)view.findViewById(R.id.txt_right_date);
            holder.txt_attachment=(TextView)view.findViewById(R.id.txt_attachment);
            holder.profile_right=(CircleImageView)view.findViewById(R.id.layout_inflate_chatadapt_hostprofile_right);

            sessionManager = new SessionManager(Mycontext);
            HashMap<String, String> details = sessionManager.getUserDetails();
            user_id = details.get(sessionManager.KEY_USER_ID);
            System.out.println("------User ID-------->" + user_id);
/*
            animShow = AnimationUtils.loadAnimation(Mycontext, R.anim.show_view);
            animHide = AnimationUtils.loadAnimation(Mycontext, R.anim.hide_view);*/

            view.setTag(holder);

        } else {
            holder = (ViewHolderChat) view.getTag();
        }





        /*if(user_id.equals(My_ChatARR.get(i).getSenderId()))
        {
            System.out.println("------Sender Message----------->"+My_ChatARR.get(i).getMessage());
            holder.Rel_Left.setVisibility(View.GONE);
            holder.Rel_Right.setVisibility(View.VISIBLE);
            if(My_ChatARR.get(i).getMessage()!=null && !My_ChatARR.get(i).getMessage().equals(""))
            {
                holder.txt_right_message.setText(My_ChatARR.get(i).getMessage());
            }

            if(My_ChatARR.get(i).getDateAdded()!=null && !My_ChatARR.get(i).getDateAdded().equals(""))
            {
                holder.txt_right_date.setText(My_ChatARR.get(i).getDateAdded());
            }



            if(My_ChatARR.get(i).getSender_pic()!=null && !My_ChatARR.get(i).getSender_pic().equals(""))
            {
                Picasso.with(Mycontext).load(My_ChatARR.get(i).getSender_pic()).fit().centerCrop()
                        .placeholder(R.drawable.icn_profile)
                        .into(holder.profile_left_image);
            }

            *//*System.out.println("---------SENDER PICTURE------------>"+My_ChatARR.get(i).getReceiver_pic());
            if(My_ChatARR.get(i).getReceiver_pic()!=null && !My_ChatARR.get(i).getReceiver_pic().equals(""))
            {
                Picasso.with(Mycontext).load(My_ChatARR.get(i).getReceiver_pic()).fit().centerCrop()
                        .placeholder(R.drawable.icn_profile)
                        .into(holder.profile_right);
            }*//*
        }
        else
        {

           // System.out.println("---------RECEIVER PICTURE------------>"+My_ChatARR.get(i).getReceiver_pic());
           // System.out.println("------Receiver Message----------->"+My_ChatARR.get(i).getMessage());
            holder.Rel_Left.setVisibility(View.VISIBLE);
            holder.Rel_Right.setVisibility(View.GONE);

            if(My_ChatARR.get(i).getMessage()!=null && !My_ChatARR.get(i).getMessage().equals(""))
            {
                holder.txt_left_message.setText(My_ChatARR.get(i).getMessage());
            }

            if(My_ChatARR.get(i).getDateAdded()!=null && !My_ChatARR.get(i).getDateAdded().equals(""))
            {
                holder.txt_left_date.setText(My_ChatARR.get(i).getDateAdded());
            }

            *//*if(My_ChatARR.get(i).getSender_pic()!=null && !My_ChatARR.get(i).getSender_pic().equals(""))
            {
                Picasso.with(Mycontext).load(My_ChatARR.get(i).getSender_pic()).fit().centerCrop()
                        .placeholder(R.drawable.icn_profile)
                        .into(holder.profile_left_image);
            }*//*



           // System.out.println("---------SENDER PICTURE------------>"+My_ChatARR.get(i).getReceiver_pic());
            if(My_ChatARR.get(i).getReceiver_pic()!=null && !My_ChatARR.get(i).getReceiver_pic().equals(""))
            {
                Picasso.with(Mycontext).load(My_ChatARR.get(i).getReceiver_pic()).fit().centerCrop()
                        .placeholder(R.drawable.icn_profile)
                        .into(holder.profile_right);
            }
        }*/



        if(user_id.equalsIgnoreCase(My_ChatARR.get(i).getSenderId()))
        {
            holder.Rel_Left.setVisibility(View.GONE);
            holder.Rel_Right.setVisibility(View.VISIBLE);

            System.out.println("--------SENDER DATA------>"+My_ChatARR.get(i).getSenderId());
            if(My_ChatARR.get(i).getSender_pic()!=null && !My_ChatARR.get(i).getSender_pic().equals(""))
            {
                Picasso.with(Mycontext).load(My_ChatARR.get(i).getSender_pic()).fit().centerCrop()
                        .placeholder(R.drawable.icn_profile)
                        .into(holder.profile_right);
            }

            if(My_ChatARR.get(i).getMessage()!=null && !My_ChatARR.get(i).getMessage().equals(""))
            {
                holder.txt_right_message.setText(My_ChatARR.get(i).getMessage());
            }

            if(My_ChatARR.get(i).getDateAdded()!=null && !My_ChatARR.get(i).getDateAdded().equals(""))
            {
                holder.txt_right_date.setText(My_ChatARR.get(i).getDateAdded());
            }
            if(My_ChatARR.get(i).getAttachment() != null && !My_ChatARR.get(i).getAttachment().equals("") && My_ChatARR.get(i).getAttachment().length()>0)
            {
                holder.txt_attachment.setText("View");
                holder.txt_attachment.setVisibility(View.VISIBLE);
            }else
                holder.txt_attachment.setVisibility(View.GONE);


        }
        else
        {
            holder.Rel_Left.setVisibility(View.VISIBLE);
            holder.Rel_Right.setVisibility(View.GONE);
            System.out.println("---------RECEIVER PICTURE------------>"+"---Position->"+My_ChatARR.get(i).getId()+"-------"+My_ChatARR.get(i).getSender_pic());
            //System.out.println("---------SENDER PICTURE------------>"+My_ChatARR.get(i).getSender_pic());


            if(My_ChatARR.get(i).getMessage()!=null && !My_ChatARR.get(i).getMessage().equals(""))
            {
                holder.txt_left_message.setText(My_ChatARR.get(i).getMessage());
            }

            if(My_ChatARR.get(i).getDateAdded()!=null && !My_ChatARR.get(i).getDateAdded().equals(""))
            {
                holder.txt_left_date.setText(My_ChatARR.get(i).getDateAdded());
            }

            if(My_ChatARR.get(i).getSender_pic()!=null && !My_ChatARR.get(i).getSender_pic().equals(""))
            {
                Picasso.with(Mycontext).load(My_ChatARR.get(i).getSender_pic()).fit().centerCrop()
                        .placeholder(R.drawable.icn_profile)
                        .into(holder.profile_left_image);
            }

            if(My_ChatARR.get(i).getAttachment() != null && !My_ChatARR.get(i).getAttachment().equals("") && My_ChatARR.get(i).getAttachment().length()>0)
            {
                holder.txt_attachment.setText("View");
                holder.txt_attachment_top.setVisibility(View.VISIBLE);
            }else
                holder.txt_attachment_top.setVisibility(View.GONE);
        }

        holder.Rel_txt_attachment_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_downloadng = 0;
                dialog_popup(My_ChatARR.get(i).getAttachment());
            }
        });

        holder.Rel_txt_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent ntenti = new Intent(Mycontext, ChatImageViewActivity.class);
//                ntenti.putExtra("url",My_ChatARR.get(i).getAttachment());
//                Mycontext.startActivity(ntenti);
                file_downloadng = 0;
                dialog_popup(My_ChatARR.get(i).getAttachment());
            }
        });

      //  view.startAnimation(animShow);

        return view;
    }


    class ViewHolderChat
    {
        RelativeLayout Rel_txt_attachment_top,Rel_txt_attachment;
        LinearLayout Rel_Left,Rel_Right;
        TextView txt_left_message,txt_left_date,txt_attachment_top;
        CircleImageView profile_left_image;

        TextView txt_right_message,txt_right_date,txt_attachment;
        CircleImageView profile_right;
    }

    private void dialog_popup(final String attachment) {

        //List<S>
        final Dialog dialog = new Dialog(Mycontext);
        dialog.setContentView(R.layout.doc_layout);
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        LinearLayout action_container = (LinearLayout) dialog.findViewById(R.id.action_container);
        //LinearLayout lin_chose_photo = (LinearLayout) dialog.findViewById(R.id.lin_choose_photo);
        TextView txt_file_url = (TextView) dialog.findViewById(R.id.txt_file_url);
        RelativeLayout Rl_close = (RelativeLayout) dialog.findViewById(R.id.Rl_close);


        txt_file_url.setText(attachment);

        Rl_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        txt_file_url.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(file_downloadng == 0)
                    downloadPdfFile(attachment);
            }
        });

        dialog.show();
    }

    private void downloadPdfFile(String link) {
        // URL of the pdf file
        //String url_to_pdf = "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
        String url_to_pdf = link;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_to_pdf));
        request.setDescription(url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1));
        request.setTitle(url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1));
// in order for this if to run, you must use the android 3.2 to compile your app o
        Toast.makeText(Mycontext,"Downloading "+url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1),Toast.LENGTH_SHORT).show();
        file_downloadng = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url_to_pdf.substring(url_to_pdf.lastIndexOf("/") + 1)); // set the file name to save in local mobile

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) Mycontext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
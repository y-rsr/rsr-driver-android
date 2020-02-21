package com.ridesharerental.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.Extent_Details;
import com.ridesharerental.app.Invoive;
import com.ridesharerental.app.Main_homepage;
import com.ridesharerental.app.My_Reservations;
import com.ridesharerental.app.R;
import com.ridesharerental.app.Reservation_Extent;
import com.ridesharerental.pojo.Active_Reservation_Bean;
import com.ridesharerental.pojo.Documents_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by user65 on 1/6/2018.
 */

public class Active_Reservation_Recycler_Adapter extends
        RecyclerView.Adapter<Active_Reservation_Recycler_Adapter.ViewHolder> {
    private ArrayList<Active_Reservation_Bean> ArrayLists;
    LayoutInflater mInflater;
    static Context context;
    Common_Loader loader;
    String str_timer = "";
    Activity activity_main;
    String str_to_date="";
    String timerSate = "";

    TextView txt_view;


  private Handler timerHandler = new Handler();


    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            System.out.println("-----------kannan handler working---------");

//            if(My_Reservations.timerstop){

                TimerProcess();

//            }

            timerHandler.postDelayed(this, 1000);

        }
    };








    public Active_Reservation_Recycler_Adapter(Context ctx, ArrayList<Active_Reservation_Bean> items, String st_time, Activity activity) {
        this.ArrayLists = items;
        this.context = ctx;
        this.str_timer = st_time;
        activity_main = activity;
//        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inflator_reservation, parent, false);
        loader = new Common_Loader(context);

        return new ViewHolder(v);
    }



    public void stoptimer(){

        timerHandler.removeCallbacks(timerRunnable);

        System.out.println("-----stoptimer methed------------");

    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)

    {

        holder.txt_car_make.setText(ArrayLists.get(position).getCar_make() + " " + ArrayLists.get(position).getCar_model() + " " + ArrayLists.get(position).getYear());

        holder.txt_status.setText(ArrayLists.get(position).getStatus());
        if (ArrayLists.get(position).getStatus() != null && !ArrayLists.get(position).getStatus().equals("")) {
            if (ArrayLists.get(position).getStatus().equalsIgnoreCase("Applied")) {
                holder.MyExtentBTN.setText(ArrayLists.get(position).getStatus());
            } else {
                holder.MyExtentBTN.setText(context.getResources().getString(R.string.extent_rental));
            }
        }


        holder.txt_check_In.setText(ArrayLists.get(position).getDate_from());

        if (ArrayLists.get(position).getExtended().equalsIgnoreCase("Yes")) {
            holder.Txt_check_Out.setText(ArrayLists.get(position).getExtended_date());
        } else {
            holder.Txt_check_Out.setText(ArrayLists.get(position).getDate_to());
        }


        holder.txt_total_price.setText("$" + ArrayLists.get(position).getTotal_amount());

        holder.txt_host_host_name.setText(ArrayLists.get(position).getOwnername());
        holder.txt_address.setText(ArrayLists.get(position).getStreet());
        holder.txt_vin_no.setText(ArrayLists.get(position).getVin_no());
        holder.txt_plate_no.setText(ArrayLists.get(position).getPlat_no());
        holder.txt_make.setText(ArrayLists.get(position).getCar_make());
        holder.txt_model.setText(ArrayLists.get(position).getCar_model());
        holder.txt_year.setText(ArrayLists.get(position).getYear());
        holder.txt_no_of_days.setText(ArrayLists.get(position).getNo_of_days() + " " + context.getResources().getString(R.string.day_rental));
        holder.txt_vehival_no.setText(ArrayLists.get(position).getV_no());
        holder.txt_notes.setText(ArrayLists.get(position).getNotes());


        str_to_date=ArrayLists.get(position).getDate_to();
        timerSate=ArrayLists.get(position).getTimer_date();
        txt_view=holder.txt_timer;

        System.out.println("------Start Timer------>" + ArrayLists.get(position).getStr_timer_counting());
        holder.txt_timer.setText("Timer");


        timerHandler.postDelayed(timerRunnable, 1000);


//        if(!My_Reservations.timerstop){
//
//            timerHandler.removeCallbacks(timerRunnable);
//
//            System.out.println("-----vicky out- timer--------");
//
//
//        }


//        if (ArrayLists.get(position).getTimer_date() != null && !ArrayLists.get(position).getTimer_date().equalsIgnoreCase("")) {
//            Thread myThread = null;
//            Runnable myRunnableThread = new CountDownRunner(ArrayLists.get(position).getTimer_date(), activity_main, holder.txt_timer);
//            myThread = new Thread(myRunnableThread);
//            myThread.start();
//        }

        if (ArrayLists.get(position).getProgress() != null && !ArrayLists.get(position).getProgress().equals("")) {

            if (ArrayLists.get(position).getProgress().equalsIgnoreCase("waiting")) {
                holder.img_waiting.setBackground(context.getResources().getDrawable(R.drawable.layout_inflator_reservation_uncircle));
                holder.txt_waiting.setTextColor(context.getResources().getColor(R.color.yellow));
            } else if (ArrayLists.get(position).getProgress().equalsIgnoreCase("running")) {
                holder.img_spend.setBackground(context.getResources().getDrawable(R.drawable.layout_inflator_reservation_uncircle));
                holder.txt_active.setTextColor(context.getResources().getColor(R.color.yellow));
            } else {
                holder.img_insurance.setBackground(context.getResources().getDrawable(R.drawable.layout_inflator_reservation_uncircle));
                holder.txt_insurance.setTextColor(context.getResources().getColor(R.color.yellow));
            }
        } else {
            holder.img_waiting.setBackground(context.getResources().getDrawable(R.drawable.incomplete));
            holder.img_insurance.setBackground(context.getResources().getDrawable(R.drawable.incomplete));
            holder.img_spend.setBackground(context.getResources().getDrawable(R.drawable.incomplete));

            holder.txt_waiting.setTextColor(context.getResources().getColor(R.color.app_color_1));
            holder.txt_active.setTextColor(context.getResources().getColor(R.color.app_color_1));
            holder.txt_insurance.setTextColor(context.getResources().getColor(R.color.app_color_1));
        }

        if (ArrayLists.get(position).getAllow_extend() != null && !ArrayLists.get(position).getAllow_extend().equals("")) {
            if (ArrayLists.get(position).getAllow_extend().equalsIgnoreCase("Yes")) {
                holder.MyExtentBTN.setVisibility(View.VISIBLE);
            } else {
                holder.MyExtentBTN.setVisibility(View.GONE);
            }
        } else {
            holder.MyExtentBTN.setVisibility(View.GONE);
        }


        if (!ArrayLists.get(position).getProgress().equalsIgnoreCase("waiting") && ArrayLists.get(position).getExtended().equalsIgnoreCase("yes"))

        {
            holder.txt_extend_details.setVisibility(View.VISIBLE);
            holder.txt_extend_details.setEnabled(true);
        } else if (!ArrayLists.get(position).getProgress().equalsIgnoreCase("waiting") && ArrayLists.get(position).getExtended().equalsIgnoreCase("No")) {
            holder.txt_extend_details.setVisibility(View.GONE);
        } else if (ArrayLists.get(position).getProgress().equalsIgnoreCase("waiting") && ArrayLists.get(position).getExtended().equalsIgnoreCase("yes")) {
            holder.txt_extend_details.setVisibility(View.VISIBLE);
            // holder.txt_extend_details.setText("Applied");
            holder.txt_extend_details.setEnabled(false);
        } else if (ArrayLists.get(position).getProgress().equalsIgnoreCase("waiting") && ArrayLists.get(position).getExtended().equalsIgnoreCase("No")) {
            holder.txt_extend_details.setVisibility(View.GONE);
        }

        /*if(ArrayLists.get(position).getStatus().equalsIgnoreCase("Active"))
        {
            holder.MyExtentBTN.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.MyExtentBTN.setVisibility(View.GONE);
        }*/

        if (ArrayLists.get(position).getExtended().equalsIgnoreCase("yes")) {
            holder.txt_extend_details.setVisibility(View.VISIBLE);
        } else {
            holder.txt_extend_details.setVisibility(View.GONE);
        }


        if (ArrayLists.get(position).getAllow_extend().equalsIgnoreCase("Yes")) {
            holder.MyExtentBTN.setVisibility(View.VISIBLE);
            //holder.txt_invoice.setVisibility(View.VISIBLE);

        } else {
            holder.MyExtentBTN.setVisibility(View.GONE);
            //holder.txt_invoice.setVisibility(View.GONE);
        }

        if (ArrayLists.get(position).getStatus() != null && !ArrayLists.get(position).getStatus().equals("")) {
            if (ArrayLists.get(position).getStatus().equalsIgnoreCase("Active")) {
                holder.txt_invoice.setVisibility(View.VISIBLE);
            } else {
                holder.txt_invoice.setVisibility(View.GONE);
            }
        } else {
            holder.txt_invoice.setVisibility(View.GONE);
        }


       /* if(ArrayLists.get(position).getExtended()!=null  && !ArrayLists.get(position).getExtended().equals(""))
        {
            if(ArrayLists.get(position).getExtended().equalsIgnoreCase("Yes"))
            {
                holder.txt_extend_details.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.txt_extend_details.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.txt_extend_details.setVisibility(View.GONE);
        }*/


        if (ArrayLists.get(position).getCar_image() != null && !ArrayLists.get(position).getCar_image().equals("")) {
            Picasso.with(context).load(ArrayLists.get(position).getCar_image())
                    .placeholder(R.drawable.placeholdercar)
                    .error(R.drawable.placeholdercar)
                    .into(holder.img_car);

        }


        if (ArrayLists.get(position).getProfile_pic() != null && !ArrayLists.get(position).getProfile_pic().equals("")) {
            Picasso.with(context).load(ArrayLists.get(position).getProfile_pic())
                    .placeholder(R.drawable.icn_profile)
                    .error(R.drawable.icn_profile)
                    .into(holder.profile_pic);

        }
        holder.itemView.setTag("");


        holder.MyExtentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!ArrayLists.get(position).getStatus().equalsIgnoreCase("Applied")) {
                    Intent val = new Intent(context, Reservation_Extent.class);
                    val.putExtra("car_name", ArrayLists.get(position).getCar_make() + " " + ArrayLists.get(position).getCar_model() + " " + ArrayLists.get(position).getYear());
                    val.putExtra("check_in", ArrayLists.get(position).getDate_from());

                    if (!ArrayLists.get(position).getDate_to().equalsIgnoreCase(ArrayLists.get(position).getExtended_date())) {
                        val.putExtra("check_out", ArrayLists.get(position).getExtended_date());
                    } else {
                        val.putExtra("check_out", ArrayLists.get(position).getDate_to());
                    }

                    val.putExtra("booking_no", ArrayLists.get(position).getBooking_no());
                    context.startActivity(val);
                } else {

                }

            }
        });

        holder.txt_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerHandler.removeCallbacks(timerRunnable);
                Intent val = new Intent(context, Main_homepage.class);
                val.putExtra("calling_type", "transaction");
                context.startActivity(val);
            }
        });


        holder.rel_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                My_Reservations.timerstop=false;

                //showPopUp();
            }
        });

        holder.txt_extend_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                My_Reservations.timerstop=false;

                Intent val = new Intent(context, Extent_Details.class);
                val.putExtra("booking_no", ArrayLists.get(position).getBooking_no());
                context.startActivity(val);
            }
        });


        if (ArrayLists.get(position).getDocuments() != null && ArrayLists.get(position).getDocuments().length() > 0) {
            holder.txt_download.setVisibility(View.VISIBLE);
        } else {
            holder.txt_download.setVisibility(View.GONE);
        }
        if (ArrayLists.get(position).getBooking_documents() != null && ArrayLists.get(position).getBooking_documents().length() > 0) {
            holder.txt_download_b_documents.setVisibility(View.VISIBLE);
        } else {
            holder.txt_download_b_documents.setVisibility(View.GONE);
        }
        holder.txt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ArrayLists.get(position).getDocuments().length() > 0) {
                    JSONArray array_Documents = ArrayLists.get(position).getDocuments();
                    showPopUp(array_Documents, context);
                }

            }
        });
        holder.txt_download_b_documents.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ArrayLists.get(position).getBooking_documents().length() > 0) {
                            JSONArray array_Documents = ArrayLists.get(position).getBooking_documents();
                            showPopUpBookingDoc(array_Documents, context);
                        }

                    }
                });

        holder.txt_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerHandler.removeCallbacks(timerRunnable);
                Intent val = new Intent(context, Invoive.class);
                val.putExtra("booking_no", ArrayLists.get(position).getBooking_no());
                context.startActivity(val);
            }
        });
        //holder.type_title.setText(str_calling_title);
    }

    @Override
    public int getItemCount() {
        return ArrayLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView MyExtentBTN;
        private RelativeLayout rel_details;
        private ImageView img_car;

        TextView txt_car_make;
        TextView txt_status;
        CircleImageView profile_pic;
        TextView txt_check_In, Txt_check_Out;
        TextView txt_total_price;
        TextView txt_timer;

        ImageView img_waiting, img_insurance, img_spend;
        TextView txt_waiting, txt_insurance, txt_active;

        TextView txt_host_host_name, txt_address;
        TextView txt_vin_no, txt_plate_no, txt_make, txt_model, txt_year;
        TextView txt_no_of_days;

        TextView txt_transactions;
        TextView txt_extend_details;

        TextView txt_vehival_no, txt_notes;
        TextView txt_download;
        TextView txt_download_b_documents;

        TextView txt_invoice;

        LinearLayout linear_transaction_invoice;

        public ViewHolder(View itemView) {
            super(itemView);
            MyExtentBTN = (TextView) itemView.findViewById(R.id.layout_inflator_reservation_extentBTN);
            rel_details = (RelativeLayout) itemView.findViewById(R.id.rel_details);
            img_car = (ImageView) itemView.findViewById(R.id.img_car);

            txt_car_make = (TextView) itemView.findViewById(R.id.layout_inflator_reserCarname);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            profile_pic = (CircleImageView) itemView.findViewById(R.id.layout_inflator_reserUserprofile);

            txt_check_In = (TextView) itemView.findViewById(R.id.txt_check_In);
            Txt_check_Out = (TextView) itemView.findViewById(R.id.txt_check_Out);
            txt_total_price = (TextView) itemView.findViewById(R.id.txt_total_price);
            txt_timer = (TextView) itemView.findViewById(R.id.txt_timer);

            img_waiting = (ImageView) itemView.findViewById(R.id.img_waiting);
            img_insurance = (ImageView) itemView.findViewById(R.id.img_insurance);
            img_spend = (ImageView) itemView.findViewById(R.id.img_spend);

            img_waiting.setBackground(context.getResources().getDrawable(R.drawable.incomplete));
            img_insurance.setBackground(context.getResources().getDrawable(R.drawable.incomplete));
            img_spend.setBackground(context.getResources().getDrawable(R.drawable.incomplete));

            txt_waiting = (TextView) itemView.findViewById(R.id.txt_waiting_acceptance);
            txt_insurance = (TextView) itemView.findViewById(R.id.txt_insurance);
            txt_active = (TextView) itemView.findViewById(R.id.txt_spend);

            txt_host_host_name = (TextView) itemView.findViewById(R.id.txt_host_name);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);

            txt_vin_no = (TextView) itemView.findViewById(R.id.txt_vin_val);
            txt_plate_no = (TextView) itemView.findViewById(R.id.txt_plat_no_value);
            txt_make = (TextView) itemView.findViewById(R.id.txt_make_value);
            txt_model = (TextView) itemView.findViewById(R.id.txt_model_value);
            txt_year = (TextView) itemView.findViewById(R.id.txt_year_value);
            txt_no_of_days = (TextView) itemView.findViewById(R.id.txt_no_of_days);

            txt_transactions = (TextView) itemView.findViewById(R.id.txt_my_Transactions);
            txt_extend_details = (TextView) itemView.findViewById(R.id.layout_inflator_reservation_extend_details);


            txt_vehival_no = (TextView) itemView.findViewById(R.id.txt_vehical_no);
            txt_notes = (TextView) itemView.findViewById(R.id.txt_notes);

            txt_download = (TextView) itemView.findViewById(R.id.txt_download_documents);
            txt_download_b_documents = (TextView) itemView.findViewById(R.id.txt_download_b_documents);
            txt_download.setVisibility(View.GONE);
            txt_download_b_documents.setVisibility(View.GONE);

            txt_invoice = (TextView) itemView.findViewById(R.id.txt_invoice);

            linear_transaction_invoice = (LinearLayout) itemView.findViewById(R.id.linear_transaction_invoice);

        }
    }


    public void showPopUp(JSONArray array, Context ctx) {
        Log.e("booking array",array.toString());
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.reservation_details_document);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout Rel_close = (RelativeLayout) dialog.findViewById(R.id.rel_close);
        ExpandableHeightListView rel_documets = (ExpandableHeightListView) dialog.findViewById(R.id.list_documents);
        dialog.show();
        ArrayList<Documents_Bean> arrayLists_doc = new ArrayList<>();
        arrayLists_doc.clear();

        try {

            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Documents_Bean bean = new Documents_Bean();
                    bean.setLabel(obj.getString("label"));
                    bean.setLink(obj.getString("link"));
                    arrayLists_doc.add(bean);
                }
            }

            if (arrayLists_doc.size() > 0) {
                Document_Adapter adapter = new Document_Adapter(ctx, arrayLists_doc,"Docs");
                rel_documets.setAdapter(adapter);
                rel_documets.setExpanded(true);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Rel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    public void showPopUpBookingDoc(JSONArray array, Context ctx) {
        Log.e("booking array",array.toString());
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.reservation_details_document);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout Rel_close = (RelativeLayout) dialog.findViewById(R.id.rel_close);
        ExpandableHeightListView rel_documets = (ExpandableHeightListView) dialog.findViewById(R.id.list_documents);
        dialog.show();
        ArrayList<Documents_Bean> arrayLists_doc = new ArrayList<>();
        arrayLists_doc.clear();

        try {

            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Documents_Bean bean = new Documents_Bean();
                    bean.setLabel(obj.getString("id"));
                    bean.setLink(obj.getString("name"));
                    arrayLists_doc.add(bean);
                }
            }

            if (arrayLists_doc.size() > 0) {
                Document_Adapter adapter = new Document_Adapter(ctx, arrayLists_doc,"bookingDocs");
                rel_documets.setAdapter(adapter);
                rel_documets.setExpanded(true);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Rel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


//    class CountDownRunner implements Runnable {
//        String str_curren_date = "";
//        Activity ctx;
//        TextView txt_view;
//
//        public CountDownRunner(String to_date, Activity activity, TextView txt_v) {
//            this.str_curren_date = to_date;
//            this.ctx = activity;
//            this.txt_view = txt_v;
//        }
//
//        public void run() {
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
////                    doWork(str_curren_date, ctx, txt_view);
//                    //Thread.sleep(1000); // Pause of 1 Second
//                    Thread.sleep(1000); // Pause of 1 Second
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } catch (Exception e) {
//                }
//            }
//        }
//    }


   /* public void doWork(final String str_to_date, Activity activity, final TextView txt) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Date current_date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                    String DateToStr = format.format(current_date);
                    System.out.println("--------Display current date and time---------->" + str_to_date);



                    *//*String dt = str_to_date;  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(dt));
                    c.add(Calendar.DATE, 0);  // number of days to add
                    dt = sdf.format(c.getTime());*//*


                    Date reach_date = format.parse(str_to_date);
                    //Date reach_date = format.parse("2018-01-08 23:59:59");


                    if (current_date.after(reach_date)) {
                        String dt = str_to_date;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(dt));
                        c.add(Calendar.DATE, 0);  // number of days to add
                        dt = sdf.format(c.getTime());
                        Date reach_date1 = format.parse(dt);
                        printDifference(current_date, reach_date1, txt);
                    } else {
                        String dt = str_to_date;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(dt));
                        c.add(Calendar.DATE, 1);  // number of days to add
                        dt = sdf.format(c.getTime());
                        Date reach_date1 = format.parse(dt);
                        printDifference(current_date, reach_date1, txt);
                    }


                } catch (Exception e) {
                }
            }
        });
    }
*/

    private void TimerProcess() {


        try {

            Date current_date = new Date();
            SimpleDateFormat format = new SimpleDateFormat(/*"MM/dd/yyyy"*/"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            String DateToStr = format.format(current_date);
            System.out.println("--------Display current date and time---------->" + timerSate);

            Date reach_date = format.parse(timerSate);
            //Date reach_date = format.parse("2018-01-08 23:59:59");

            if (current_date.after(reach_date)) {
                String dt = timerSate;  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat(/*"MM/dd/yyyy"*/"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(dt));
                c.add(Calendar.DATE, 0);  // number of days to add
                dt = sdf.format(c.getTime());
                Date reach_date1 = format.parse(dt);
                printDifference(current_date, reach_date1, txt_view);
            } else {
                String dt = timerSate;  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat(/*"MM/dd/yyyy"*/"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(dt));
                c.add(Calendar.DATE, 0);  // number of days to add
                dt = sdf.format(c.getTime());
                Date reach_date1 = format.parse(dt);


//                Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(dt);
//                Calendar c = Calendar.getInstance();
//                c.setTime(date1);
//                c.add(Calendar.DATE, 1);
//                Date reach_date1 =c.getTime();
                printDifference(current_date, reach_date1, txt_view);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    public void printDifference(Date startDate, Date endDate, TextView txxt) {
        //milliseconds


        if (startDate.after(endDate)) {
            System.out.println("--------After Date------>");

            //long different = endDate.getTime() - startDate.getTime();
            long different = startDate.getTime() - endDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;
            //int add=Integer.parseInt(String.valueOf(elapsedSeconds));
            //add=add+1;
            // String timer = elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + " s";
            String timer = elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + " s";
            System.out.println("----Adapter--Timer------->" + timer);
            //txxt.setText(String.valueOf(elapsedDays)+" days late");
            txxt.setText("Late (" + String.valueOf(elapsedDays+1) + " days)");

        } else {
            System.out.println("--------Before Date------>");

            long different = endDate.getTime() - startDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            String timer = "";
            timer = elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + " s";
            System.out.println("----Adapter--Timer------->" + timer);
            txxt.setText(timer);
        }

    }






}

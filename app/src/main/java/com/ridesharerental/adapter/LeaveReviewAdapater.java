package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.LeaveReviewPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user129 on 10/1/2018.
 */
public class LeaveReviewAdapater extends BaseAdapter {

    ReviewClickListner listener;


    public interface ReviewClickListner {

        public void ItemClickListner(int pos, String BookingId, String CarId, String title);


    }


    private ArrayList<LeaveReviewPojo> data;
    private LayoutInflater mInflater;
    private Context context;

    public LeaveReviewAdapater(Context c, ArrayList<LeaveReviewPojo> d) {
        context = c;
        mInflater = LayoutInflater.from(context);
        data = d;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    public class ViewHolder {
        private TextView Tv_title, Tv_satus, Tv_start_date, Tv_end_date, Tv_owner_name, Tv_location, Tv_plate_no, Tv_make, Tv_model, Tv_year, Tv_vehicle_number, Tv_notes;
        private RelativeLayout Rl_put_review;
        private ImageView vehicle_img;
    }


    public void ItemClickPerformance(ReviewClickListner listener) {
        this.listener = listener;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.leave_review_adapter, parent, false);
            holder = new ViewHolder();
            holder.vehicle_img = (ImageView) view.findViewById(R.id.vehicle_img);
            holder.Tv_title = (TextView) view.findViewById(R.id.Tv_title);
            holder.Tv_satus = (TextView) view.findViewById(R.id.Tv_satus);
            holder.Tv_start_date = (TextView) view.findViewById(R.id.Tv_start_date);
            holder.Tv_end_date = (TextView) view.findViewById(R.id.Tv_end_date);
            holder.Tv_owner_name = (TextView) view.findViewById(R.id.Tv_owner_name);
            holder.Tv_location = (TextView) view.findViewById(R.id.Tv_location);
            holder.Tv_plate_no = (TextView) view.findViewById(R.id.Tv_plate_no);
            holder.Tv_make = (TextView) view.findViewById(R.id.Tv_make);
            holder.Tv_model = (TextView) view.findViewById(R.id.Tv_model);
            holder.Tv_year = (TextView) view.findViewById(R.id.Tv_year);
            holder.Tv_vehicle_number = (TextView) view.findViewById(R.id.Tv_vehicle_number);
            holder.Tv_notes = (TextView) view.findViewById(R.id.Tv_notes);
            holder.Rl_put_review = (RelativeLayout) view.findViewById(R.id.Rl_put_review);

            view.setTag(holder);

        } else {

            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(context)
                .load(data.get(position).getCar_image())
                .resize(200, 130)
                .placeholder(context.getResources().getDrawable(R.drawable.placeholdercar))
                .into(holder.vehicle_img);


        holder.Tv_title.setText(data.get(position).getCar_make() + " " + data.get(position).getCar_model() + " " + data.get(position).getYear());
        holder.Tv_satus.setText(data.get(position).getStatus());
        holder.Tv_start_date.setText(data.get(position).getDate_from());
        holder.Tv_end_date.setText(data.get(position).getDate_to());
        holder.Tv_owner_name.setText(data.get(position).getOwnername());
        holder.Tv_location.setText(data.get(position).getStreet());
        holder.Tv_plate_no.setText(data.get(position).getPlat_no());
        holder.Tv_make.setText(data.get(position).getCar_make());
        holder.Tv_model.setText(data.get(position).getCar_model());
        holder.Tv_year.setText(data.get(position).getYear());
        holder.Tv_vehicle_number.setText(data.get(position).getV_no());
        holder.Tv_notes.setText(data.get(position).getNotes());


        holder.Rl_put_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.ItemClickListner(position, data.get(position).getBooking_no(), data.get(position).getCarId(), "Write your review here, for the car"+" "+ data.get(position).getCar_make() + " " + data.get(position).getCar_model() + " " + data.get(position).getYear() +" "+"booked from" +" "+data.get(position).getDate_from()+" " + "to" +" "+data.get(position).getDate_to());

            }
        });


        return view;
    }
}

package com.ridesharerental.ambasador.updatedtree;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;

import java.util.HashMap;

import static android.view.animation.Animation.REVERSE;
import static com.ridesharerental.app.R.id.ew_tv_node_key;

/**
 * Created by user65 on 2/23/2018.
 */

public class AdvancedTreeItemView_Updated_Tree extends LinearLayout
{
    private LinearLayout rootLayout;
    public Button btnExpend;
    public   LinearLayout linear_all,lineLayout;

    private TextView tvNodeKey;
    private TextView tvNodeSize;
    private TextView tvColon;
    private TextView tvNodeValue;

    private int lastExpendState;
    private LinearLayout container;
    private RelativeLayout rel_container;

    private TextView txt_rank;
    public View ver_view;

    public View view_line;
    private OnClickListener btnClickListener,btnClickListener_lable;
    Animation animBlink;



    public AdvancedTreeItemView_Updated_Tree(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_json_tree_item_advanced, this, true);
        initView();
    }

    public AdvancedTreeItemView_Updated_Tree(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public void initView()
    {
      //  animBlink = AnimationUtils.loadAnimation(getContext(),R.anim.blink);
        animBlink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);

        rootLayout = (LinearLayout) getRootView();
        //  rootLayout = (LinearLayout) this.findViewById(R.id.linear_all);
        btnExpend = (Button) this.findViewById(R.id.ew_btn_expend);
        tvNodeKey = (TextView) this.findViewById(ew_tv_node_key);
        tvNodeSize = (TextView) this.findViewById(R.id.ew_tv_node_size);
        tvColon = (TextView) this.findViewById(R.id.ew_tv_colon);
        tvNodeValue = (TextView) this.findViewById(R.id.ew_tv_node_value);
        container = (LinearLayout) this.findViewById(R.id.linear_container);
        txt_rank = (TextView) this.findViewById(R.id.txt_rank);
        // rel_container=(RelativeLayout)this.findViewById(R.id.rel_container);

        linear_all=(LinearLayout)this.findViewById(R.id.linear_all);
        lineLayout=this.findViewById(R.id.line_layout);
        view_line=(View)this.findViewById(R.id.view_line);

        ver_view=(View)this.findViewById(R.id.vert_view);

        btnExpend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnClickListener != null)
                {
                    btnClickListener.onClick(v);
                }
            }
        });


        container.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (btnClickListener_lable != null)
                {
                    btnClickListener_lable.onClick(v);
                }
            }
        });
    }

    public void setExpendState(int expendState) {
        this.lastExpendState = expendState;
    }

    public void setBtnClickListener(OnClickListener btnClickListener)
    {
        this.btnClickListener = btnClickListener;
    }

    public void setLblClickListener(OnClickListener btnClickListener)
    {
        this.btnClickListener_lable = btnClickListener;
    }
    public void setData(String iscild,String color,String name,String rank,String key, String size, Object value, String btnText, Boolean isVirtualNode, Boolean isExpend, String calling) {

        if(value!=null)
        {
            HashMap<String,String> map=new HashMap<>();
            map.put(key,value.toString());
        }

        if (value != null)
        {

            tvNodeKey.setTextColor(getResources().getColor(R.color.white_color));
            txt_rank.setTextColor(getResources().getColor(R.color.white_color));

            if(color!=null  && !color.equals(""))
            {
                // System.out.println("-----tttttttt--------"+iscild+"----------------->"+calling);

                System.out.println("---Name data--->"+name+"---Rank--->"+ rank+"--->"+color+"---->"+calling+"--bbbb-->"+iscild+"--->"+isVirtualNode+"--------->"+isExpend);
                tvNodeKey.setText("" + "" + name);
                txt_rank.setText(""+rank);
                container.setBackgroundColor(Color.parseColor(color));

                System.out.println("--------Modified New data------------>"+iscild);
                if(iscild!=null && !iscild.equals("") && iscild.equalsIgnoreCase("Yes"))
                {
                    System.out.println("--------Modified New dataYes------------>"+iscild +"Name-->"+name);
                   // manageBlinkEffect();
                    container.startAnimation(animBlink);
                }
            }

           /* if(key.equals("rank"))
            {
                System.out.println("-------Rank---Key and value--------->"+key+"-------->"+ value.toString());
            }

            if(key.equals("name"))
            {
                System.out.println("-------Name---Key and value--------->"+key+"-------->"+ value.toString());
            }*/

        }

       /* System.out.println("----calling type------->"+key+"--------------------->"+value);

        HashMap<String,String> map=new HashMap();
        if(key!=null && value!=null)
        {
            map.put(key,value.toString());

            String name=(String)map.get("name");
            String rank=(String)map.get("rank");
            String color_code=(String)map.get("color_code");

            System.out.println("-------color code---------->"+color_code);

            tvNodeKey.setText("ID : "+"   :  "+value.toString()+"--->"+color_code);
            if(key.equals("name"))
            {
                System.out.println("-------sssssssssssssss---------->"+name);
                tvNodeKey.setText("ID : "+"   :  "+name.toString()+"--->"+color_code);
                container.setBackgroundColor(getResources().getColor(R.color.app_color_1));
            }

            if(key.equals("rank"))
            {
                txt_rank.setText("Rank :   "+""+value.toString());
            }


            if(value!=null && color_code!=null)
            {

                if(calling.equals("three"))
                {
                    tvNodeKey.setBackgroundColor(Color.parseColor(color_code));
                    tvNodeKey.setTextColor(getResources().getColor(R.color.white_color));
                }
                else
                {
                   // tvNodeKey.setBackgroundColor(Color.parseColor(color_code));
                    tvNodeKey.setTextColor(getResources().getColor(R.color.orange_color));
                }

            }
            else
            {

            }
        }*/

           /* if(key.equals("name"))
            {
                tvNodeKey.setText("Name :   "+""+value.toString());
            }

            if(key.equals("rank"))
            {
                tvNodeKey.setText("Rank :   "+""+value.toString());
            }*/

       /* if(key.equals("color_code"))
        {
            System.out.println("---Output------>"+key+"--------------------->"+value);
            container.setBackgroundColor(Color.parseColor(value.toString()));
        }*/


        if (calling.equals("two")) {

        } else {

        }
        if (size != null && !size.isEmpty())
        {
            tvNodeSize.setText(size + "  " + calling);

            tvNodeSize.setVisibility(GONE); // ranjith
            if (value != null)
            {
                tvNodeKey.setText("ID : " + "" + value.toString());
            }

            //  btnExpend.setVisibility(VISIBLE);
            tvNodeKey.setVisibility(VISIBLE);


            if (calling.equals("one")) {
                // btnExpend.setVisibility(VISIBLE);
                container.setVisibility(GONE);
            } else {
                // btnExpend.setVisibility(VISIBLE);
                container.setVisibility(VISIBLE);
            }
        } else
        {
            //btnExpend.setVisibility(VISIBLE);
            //tvNodeKey.setVisibility(GONE);
        }
        if (value != null) {
            tvNodeValue.setText(size);
            tvNodeValue.setVisibility(GONE);  // ranjith
            if (value instanceof String) {
                tvNodeValue.setTextColor(0xff008000);
            } else if (value instanceof Integer || value instanceof Double || value instanceof Long) {
                tvNodeValue.setTextColor(0xffff8c30);
            } else if (value instanceof Boolean) {
                tvNodeValue.setTextColor(0xff3883FA);
            }
        } else {
            tvNodeValue.setVisibility(GONE);
        }

        if (btnText != null && !btnText.isEmpty())
        {
            btnExpend.setText(btnText);

            if(btnText.equals("+"))
            {
                ver_view.setVisibility(GONE);
            }
            else
            {
                ver_view.setVisibility(VISIBLE);
            }
            System.out.println("------------------->"+btnText);
        }
        if (isVirtualNode) {
            tvNodeKey.setTextColor(getResources().getColor(R.color.white_color));
        }
        View childView = rootLayout.getChildAt(2);
        if (childView != null)
        {
            childView.setVisibility(isExpend ? VISIBLE : GONE);
        }

        /*if(calling.equals("three"))
        {
            btnExpend.setVisibility(GONE);
        }
        else
        {
            btnExpend.setVisibility(VISIBLE);
        }*/

    }

    private void manageBlinkEffect() {



        ObjectAnimator anim = ObjectAnimator.ofInt(this, "backgroundColor", Color.WHITE, Color.RED,
                Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }


}

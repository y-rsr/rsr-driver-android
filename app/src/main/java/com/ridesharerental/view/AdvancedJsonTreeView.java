package com.ridesharerental.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.utils.JSONObjectKeeped;
import com.ridesharerental.widgets.CircleImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * json tree viewçš„æ”¹è¿›ç‰ˆæœ¬
 * æ”¯æŒçš„jsonå±‚çº§æ²¡æœ‰é™åˆ¶ï¼Œç†è®ºä¸Šæ”¯æŒæ— é™å±‚çº§
 * Created by WYM on 2016/8/1.
 */

public class AdvancedJsonTreeView extends LinearLayout
{

    private int level = 0;


    // private int level_next = 0;
    Handler handler = new Handler();
    private LinearLayout rootContainer;
    private static final String TAG = "AdvancedJsonTreeView";
    Context contx;
    View view;

    public AdvancedJsonTreeView(Context context, final Map<String, Object> jsonMap)
    {
        super(context);
        this.contx = context;
        rootContainer = (LinearLayout) getRootView();
        rootContainer.setOrientation(VERTICAL);


        createTreeViewOfKeepedOptimize((LinkedHashMap<String, Object>) jsonMap, true);

    }

    public AdvancedJsonTreeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    public void createTreeViewOfKeepedOptimize(LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode)
    {


        level += 1;
        // level_next+=1;
        String name = "", rank = "", colorcode = "", childs = "", btnText = "-", string_status = "false";
        String email_data="",referal_code="",host_iamge="";

        int position = 0;

        int previousLevel = level;
       final   AdvancedTreeItemView itemView = new AdvancedTreeItemView(getContext());
       final LayoutParams lp = new LayoutParams(700, LayoutParams.WRAP_CONTENT, -2);

        for (LinkedHashMap.Entry<String, Object> entry : jsonMap.entrySet())
        {
           itemView.setTag(level);
            itemView.linear_all.setTag(level);
            String keyq = entry.getKey();

            if (keyq.equals("name")) {
                name = (String) entry.getValue();
            }


            if(keyq.equals("email"))
            {
                email_data=(String) entry.getValue();
            }

            //  lp.setMargins(0,10,0,0);


            // System.out.println("-------Level status------->"+keyq+"-->"+entry.getValue().toString()+"------------->"+level);


/*
            if(keyq.equals("name"))
            {
                System.out.println("---------Level status------->"+level+"------------>"+position+"---->"+keyq+"-->"+entry.getValue().toString());
            }*/
            //   lp.setMargins(level*15,0,0,0);



/*            if(position==(jsonMap.size()-1))
            {
               *//* View my_line = (View)
                        row.findViewById(R.id.verticallineview);*//*
                //40dp, this is the ImageView height
                int dpsize = 40;
                //convert the height in dp unit to pixel (because the parameter is in px)
                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, getResources().getDisplayMetrics());
                itemView.view_line.getLayoutParams().height = px;
            }*/

            //lp.height = level*80;
            // itemView.view_line.setLayoutParams(lp);

           /* if(keyq.equals("childs"))
            {
                lp.setMargins(level*0,0,0,0);
            }
            else
            {

            }
*/


            /*LayoutParams lp1 = new LayoutParams(600, LayoutParams.WRAP_CONTENT,-2);
            lp1.setMargins(level*20,0,0,0);*/


            System.out.println("-------Ranjith ganesan--------->" + keyq);
           /* if(!keyq.equals("id") && !keyq.equals("email") && !keyq.equals("rank") && !keyq.equals("color_code") && !keyq.equals("image") && !keyq.equals("referral_code"))
            {

            }*/


            if (keyq.equals("rank")) {
                rank = (String) entry.getValue();
            }
            if (keyq.equals("color_code"))
            {
                colorcode = (String) entry.getValue();
               /* itemView.setTag(R.string.name, name);
                itemView.setTag(R.string.email_data,email_data);
                itemView.setTag(R.string.referal_code, referal_code);
                itemView.setTag(R.string.host_iamge, host_iamge);
                itemView.setTag(R.string.rank, rank);
                itemView.setTag(R.string.colorcode, colorcode);

                if (contx != null) {
                    Log.d(TAG, "@@@@: " + (level - previousLevel) + "name: " + name);
                    Log.d(TAG, "createTreeViewOfKeepedOptimize: " + level + " pre level " + previousLevel);
                    for (int k = 0; k < (level + 1) / 2; k++) {


                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                View view = LayoutInflater.from(contx).inflate(R.layout.custom_view_layout, itemView.lineLayout, false);
                                itemView.lineLayout.addView(view);
                            }
                        });


                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        rootContainer.addView(itemView, lp);
                    }
                });*/

            }

            if(keyq.equals("image"))
            {


                host_iamge=(String) entry.getValue();


            }



            if(keyq.equals("referral_code"))
            {

                referal_code=(String) entry.getValue();

                itemView.setTag(R.string.name, name);
                itemView.setTag(R.string.email_data,email_data);
                itemView.setTag(R.string.rank, rank);
                itemView.setTag(R.string.colorcode, colorcode);
                itemView.setTag(R.string.host_iamge, host_iamge);
                itemView.setTag(R.string.referal_code, referal_code);

                System.out.println("----------HostName------->"+name+"------Profile Image---------->"+host_iamge);
                if (contx != null) {
                    Log.d(TAG, "@@@@: " + (level - previousLevel) + "name: " + name);
                    Log.d(TAG, "createTreeViewOfKeepedOptimize: " + level + " pre level " + previousLevel);
                    for (int k = 0; k < (level + 1) / 2; k++) {


                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                 view = LayoutInflater.from(contx).inflate(R.layout.custom_view_layout, itemView.lineLayout, false);
                                itemView.lineLayout.addView(view);
                            }
                        });

                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        rootContainer.addView(itemView, lp);
                    }
                });


            }










            /*if(keyq.equals("childs"))
            {

                String childs="false";

               JSONArray array= (org.json.JSONArray) entry.getValue();
                System.out.println("----sssss-------->"+array.toString());
                System.out.println("-----ddddddddd------->"+entry.getValue());
                if(array.length()>0)
                {
                    childs="true";
                }


                itemView.setTag(R.string.name,name);
                itemView.setTag(R.string.rank,rank);
                itemView.setTag(R.string.colorcode,colorcode);
                itemView.setTag(childs,childs);
                Log.d(TAG, "createTreeViewOfKeepedOptimize: "+childs);
                rootContainer.addView(itemView,lp);
            }
*/

            itemView.setLblClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String name1 = (String) itemView.getTag(R.string.name);
                    String email_1 = (String) itemView.getTag(R.string.email_data);
                    String referal_code_1=(String)itemView.getTag(R.string.referal_code);
                    String host_profile=(String)itemView.getTag(R.string.host_iamge);
                    // Toast.makeText(getContext(),name1,Toast.LENGTH_SHORT).show();

                    System.out.println("----------Display--Name----->"+name1+"-----Display Email---->"+email_1);
                    simple_dialog(name1,email_1,referal_code_1,host_profile);

                }
            });

            itemView.setBtnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    String str = ((Button) v).getText().toString();
                    if (str.equals("+")) {
                        //è¿›è¡Œå±•å¼€
                        itemView.ver_view.setVisibility(VISIBLE);
                        itemView.btnExpend.setBackgroundDrawable(getResources().getDrawable(R.drawable.ex_down));
                    } else {
                        //è¿›è¡ŒæŠ˜å 
                        itemView.btnExpend.setBackgroundDrawable(getResources().getDrawable(R.drawable.ex_arrow));
                        itemView.ver_view.setVisibility(GONE);
                    }
                    itemView.btnExpend.setText(itemView.btnExpend.getText().equals("-") ? "+" : "-");

                    int currtLevelTag = Integer.valueOf(itemView.getTag().toString());

                    String name1 = (String) itemView.getTag(R.string.name);
                    System.out.println("-------Popup Name------->"+name1);



                    boolean startSearchChildNodes = false;


                    if (rootContainer.getChildCount() > 0) {
                        System.out.println("---------Have tree-------------->");
                    } else {
                        System.out.println("---------Have tree-------------->");
                    }
                    //æŸ¥æ‰¾å½“å‰èŠ‚ç‚¹çš„å­èŠ‚ç‚¹ï¼Œç„¶åŽè®¾ç½®å…¶visibility
                    for (int j = 0; j < rootContainer.getChildCount(); j++)
                    {
                        AdvancedTreeItemView childView = (AdvancedTreeItemView) rootContainer.getChildAt(j);
                        if (startSearchChildNodes) {            //å¼€å§‹æŸ¥æ‰¾å…¶å­èŠ‚ç‚¹
                            int levelTag = Integer.valueOf(childView.getTag().toString());
                            if (levelTag > currtLevelTag) {       //å­èŠ‚ç‚¹å¼€å§‹
                                /*if(str.equals("-")){
                                    //è¿›è¡ŒæŠ˜å ï¼Œä¿å­˜å­èŠ‚ç‚¹çš„å½“å‰çš„çŠ¶æ€
                                    childView.setExpendState(childView.btnExpend.getText().toString().equals("+") ? GONE : VISIBLE);
                                }*/
                                if (itemView.btnExpend.getText().equals("-")) {
                                    AdvancedTreeItemView currtParent = (AdvancedTreeItemView) rootContainer.getChildAt(getParentNodeIndex(rootContainer, levelTag, j));//æ‰¾åˆ°å…¶çˆ¶èŠ‚ç‚¹ï¼Œæ ¹æ®å…¶çˆ¶èŠ‚ç‚¹btnåˆ¤æ–­æ˜¯å¦å±•å¼€
                                    Button btn = currtParent.btnExpend;
                                    childView.setVisibility(btn.getText().equals("-") ? VISIBLE : GONE);
                                } else {
                                    childView.setVisibility(GONE);
                                    // itemView.btnExpend.setVisibility(GONE);

                                }
                            } else {                            //å­èŠ‚ç‚¹ç»“æŸ
                                break;
                            }
                        } else {
                            if (childView.equals(itemView)) {  //æ‰¾åˆ°å½“å‰èŠ‚ç‚¹ï¼Œä¸‹ä¸€ä¸ªèŠ‚ç‚¹åˆ™ä¸ºå…¶å­èŠ‚ç‚¹
                                startSearchChildNodes = true;
                            }
                        }
                    }
                }
            });

            String key, size = null;
            Object value = null;
            Boolean isExpend = false;
            if (level < 4)
            {
                isExpend = true;
//                btnText = "-";
            }
            key = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof JSONObjectKeeped)
            {
                LinkedHashMap<String, Object> map = ((JSONObjectKeeped) obj).getMap();
                size = "{" + String.valueOf(map.size()) + "}";
                if (map.size() == 0) {
                    btnText = "";
                }
                //åˆ›å»ºå½“å‰èŠ‚ç‚¹
                itemView.setData(String.valueOf(string_status), "", "", "", key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend, "one");
                //åˆ›å»ºå­èŠ‚ç‚¹çš„tree
                createTreeViewOfKeepedOptimize(map);
            } else if (obj instanceof org.json.JSONArray)
            {
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                size = "[" + String.valueOf(jsonArray.length()) + "]";


                /*if(jsonArray.length()>0)
                {
                   // String name1= (String) itemView.getTag(R.string.name);
                    string_status="true";
                    System.out.println("------status true--------->"+string_status);
                  //  itemView.btnExpend.setVisibility(VISIBLE);
                }
                else
                {

                    string_status="jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj";
                    System.out.println("------status false--------->"+string_status);
                }*/
                if (jsonArray.length() == 0) {
                    btnText = "";
                }
                for (int i = 0; i < (jsonArray).length(); i++)
                {
                    Object each = null;
                    try {
                        each = jsonArray.get(i);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    if (each != null) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        map.put(String.valueOf(i), each);
                        //åˆ›å»ºå½“å‰èŠ‚ç‚¹
                        itemView.setData(String.valueOf(string_status), "", "", "", key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend, "two");
                        //åˆ›å»ºå­èŠ‚ç‚¹çš„tree
                        createTreeViewOfKeepedOptimize(map, true);
                    }
                }
            }
            else

            {  //åŸºç¡€æ•°æ®ç±»åž‹
                //åˆ›å»ºå½“å‰èŠ‚ç‚¹
                value = obj;
                String rank_ = (String) itemView.getTag(R.string.rank);
                String name1 = (String) itemView.getTag(R.string.name);
                String color_code_1 = (String) itemView.getTag(R.string.colorcode);
                //String chils=(String) itemView.getTag(R.string.childs);
                // Boolean is_chld= (Boolean) itemView.getTag(R.string.childs);

                if (value instanceof org.json.JSONArray)
                {
                    org.json.JSONArray jsonArray = (org.json.JSONArray) value;
                    if (jsonArray.length() > 0) {
                        // String name1= (String) itemView.getTag(R.string.name);
                        string_status = "true";
                        System.out.println("------status true--------->" + string_status);
                        //  itemView.btnExpend.setVisibility(VISIBLE);
                    } else {
                        string_status = "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj";
                        System.out.println("------status false--------->" + string_status);
                    }
                }


                itemView.setData(String.valueOf(string_status), color_code_1, name1, rank_, key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend, "three");
            }

            position++;
        }
        level -= 1;
        //level_next -= 1;
    }

    public int getParentNodeIndex(LinearLayout rootView, int currtLevel, int currtIndex) {
        for (int i = currtIndex; i >= 0; i--) {
            if (Integer.valueOf(rootView.getChildAt(i).getTag().toString()) < currtLevel) {
                return i;
            }
        }
        return 0;
    }



    public void simple_dialog(String name,String email,String referal_code_1,String host_profile)
    {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.tree_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_id);
        TextView txt_email = (TextView) dialog.findViewById(R.id.txt_email);
        TextView txt_referal_code = (TextView) dialog.findViewById(R.id.txt_referal_code);
        CircleImageView profile_image=(CircleImageView) dialog.findViewById(R.id.my_profile_layout_imageview);
        ImageView img_close=(ImageView)dialog.findViewById(R.id.img_close);


        if(host_profile!=null && !host_profile.equals(""))
        {
            System.out.println("------profile image---->"+host_profile.toString());
            Picasso.with(contx)
                    .load(host_profile)
                    .placeholder(getResources().getDrawable(R.drawable.icn_profile))
                    .into(profile_image);
        }



        txt_name.setText(name);
        txt_email.setText(email);
        txt_referal_code.setText(referal_code_1);


        img_close.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



     /*   dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Android custom dialog example!");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_launcher);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();
    }
}

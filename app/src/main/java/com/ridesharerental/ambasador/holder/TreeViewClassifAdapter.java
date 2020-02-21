package com.ridesharerental.ambasador.holder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharerental.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user65 on 2/16/2018.
 */

public class TreeViewClassifAdapter extends BaseAdapter {
    private static final int TREE_ELEMENT_PADDING_VAL = 25;
    private List<TreeElementI> fileList;
    private Context context;
    private Bitmap iconCollapse;
    private Bitmap iconExpand;
    private Dialog dialog;
    private EditText textLabel;
   // private XTreeViewClassif treeView;
    public TreeViewClassifAdapter(Context context, List<TreeElementI> fileList)
    {
        this.context = context;
        this.fileList = fileList;
        this.dialog = dialog;
        this.textLabel = textLabel;
      //  this.treeView = treeView;
       // iconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.x_treeview_outline_list_collapse);
       // iconExpand = BitmapFactory.decodeResource(context.getResources(), R.drawable.x_treeview_outline_list_expand);
    }



    public List<TreeElementI> getListData()
    {
        return this.fileList;
    }

    @Override
    public int getCount() {
        return this.fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        convertView = View.inflate(context, R.layout.x_treeview_classif_list_item, null);
        holder = new ViewHolder();
        holder.setTextView((TextView) convertView.findViewById(R.id.text));
       // holder.setImageView((ImageView) convertView.findViewById(R.id.icon));
        convertView.setTag(holder);

        final TreeElementI elem = (TreeElementI) getItem(position);

        int level = elem.getLevel();
        //holder.getIcon().setPadding(TREE_ELEMENT_PADDING_VAL * (level + 1), holder.icon.getPaddingTop(), 0, holder.icon.getPaddingBottom());
        holder.getText().setText(elem.getId());
       /* if (elem.isHasChild() && (elem.isExpanded() == false)) {
            holder.getIcon().setImageBitmap(iconCollapse);
        } else if (elem.isHasChild() && (elem.isExpanded() == true)) {
            holder.getIcon().setImageBitmap(iconExpand);
        } else if (!elem.isHasChild()) {
            holder.getIcon().setImageBitmap(iconCollapse);
            holder.getIcon().setVisibility(View.INVISIBLE);
        }*/

        //IconClickListener iconListener = new IconClickListener(this, position);
        TextClickListener txtListener = new TextClickListener((ArrayList<TreeElementI>) this.getListData(), position);
      //  holder.getIcon().setOnClickListener(iconListener);
        holder.getText().setOnClickListener(txtListener);
        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView text;

        public TextView getText() {
            return this.text;
        }

        public void setTextView(TextView text) {
            this.text = text;
        }

       /* public ImageView getIcon() {
            return this.icon;
        }

        public void setImageView(ImageView icon) {
            this.icon = icon;
        }*/
    }

    /**
     * Listener For TreeElement Text Click
     */
    private class TextClickListener implements View.OnClickListener {
        private ArrayList<TreeElementI> list;
        private int position;

        public TextClickListener(ArrayList<TreeElementI> list, int position) {
            this.list = list;
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
           // treeView.setXValue(String.valueOf(list.get(position).getId()));
//            dialog.dismiss();
        }
    }

    /**
     * Listener for TreeElement "Expand" button Click
     */
    private class IconClickListener implements View.OnClickListener {
        private ArrayList<TreeElementI> list;
        private TreeViewClassifAdapter adapter;
        private int position;

        public IconClickListener(TreeViewClassifAdapter adapter, int position) {
            this.list = (ArrayList<TreeElementI>) adapter.getListData();
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (!list.get(position).isHasChild()) {
                return;
            }

            if (list.get(position).isExpanded()) {
                list.get(position).setExpanded(false);
                TreeElementI element = list.get(position);
                ArrayList<TreeElementI> temp = new ArrayList<TreeElementI>();

                for (int i = position + 1; i < list.size(); i++) {
                    if (element.getLevel() >= list.get(i).getLevel()) {
                        break;
                    }
                    temp.add(list.get(i));
                }
                list.removeAll(temp);
                adapter.notifyDataSetChanged();
            } else {
                TreeElementI obj = list.get(position);
                obj.setExpanded(true);
                int level = obj.getLevel();
                int nextLevel = level + 1;

                for (TreeElementI element : obj.getChildList()) {
                    element.setLevel(nextLevel);
                    element.setExpanded(false);
                    list.add(position + 1, element);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}

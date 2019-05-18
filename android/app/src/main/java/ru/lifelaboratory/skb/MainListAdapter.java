package ru.lifelaboratory.skb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.lifelaboratory.skb.Entity.Item;

public class MainListAdapter extends BaseAdapter {

    List<Item> items = new ArrayList<>();
    Context ctx = null;
    LayoutInflater lInflater = null;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public MainListAdapter(Context context, ArrayList<Item> items) {
        ctx = context;
        this.items = items;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.item_for_main_list, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.elementTitle)).setText(items.get(i).getTitle());
        ImageView photo = (ImageView)view.findViewById(R.id.elementImg);
        Picasso.with(this.ctx)
                .load(items.get(i).getImg())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(photo);

        final String barCodeItem = String.valueOf(items.get(i).getId());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toItemInfo = new Intent(ctx, ItemInfoActivity.class);
                toItemInfo.putExtra(Constants.ITEM_ID, barCodeItem);
                ctx.startActivity(toItemInfo);
            }
        });
        return view;
    }
}

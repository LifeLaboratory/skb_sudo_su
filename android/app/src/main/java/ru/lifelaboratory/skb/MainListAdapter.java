package ru.lifelaboratory.skb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        return view;
    }
}

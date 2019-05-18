package ru.lifelaboratory.skb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.lifelaboratory.skb.Entity.AddItem;
import ru.lifelaboratory.skb.Entity.Item;

public class MainListAdapter extends BaseAdapter {

    List<Item> items = new ArrayList<>();
    Context ctx = null;
    LayoutInflater lInflater = null;
    Dialog saleDialog = null;
    Dialog haveDialog = null;

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

    boolean deleteSaleStatus = false;
    boolean deleteListStatus = false;

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteSaleStatus = deleteStatus;
    }

    public void setDeleteListStatus(boolean deleteListStatus) {
        this.deleteListStatus = deleteListStatus;
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

        if (items.get(i).getCount() != null)
            ((TextView) view.findViewById(R.id.elementCount)).setText(String.valueOf(items.get(i).getCount()).concat(" шт "));

        final String barCodeItem = String.valueOf(items.get(i).getId());

        ((TextView) view.findViewById(R.id.elementTitle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toItemInfo = new Intent(ctx, ItemInfoActivity.class);
                toItemInfo.putExtra(Constants.ITEM_ID, barCodeItem);
                ctx.startActivity(toItemInfo);
            }
        });

        final Integer numItem = i;

        view.setOnTouchListener(new OnSwipeTouchListener(ctx) {
            public void onSwipeRight() {
                // свайп слева направо, добавление в список покупок
                saleDialog = new Dialog(ctx);
                saleDialog.setTitle("Вход");
                saleDialog.setContentView(R.layout.dialog_add_to_sale);

                if (MainListAdapter.this.deleteSaleStatus) {
                    ((TextView) saleDialog.findViewById(R.id.dialog_title)).setText("Удалить из списка покупок?");
                }

                saleDialog.show();

                ((Button) saleDialog.findViewById(R.id.btn_dialog_no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saleDialog.cancel();
                    }
                });

                ((Button) saleDialog.findViewById(R.id.btn_dialog_yes)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MainListAdapter.this.deleteSaleStatus) {
                            SharedPreferences sp = ctx.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                            if (sp.getInt(Constants.USER_ID, -1) != -1) {
                                ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                                toServerItem.addToSale(new AddItem(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId()))
                                        .enqueue(new Callback<Item>() {
                                            @Override
                                            public void onResponse(Call<Item> call, Response<Item> response) {
                                            }

                                            @Override
                                            public void onFailure(Call<Item> call, Throwable t) {
                                            }
                                        });
                            }
                        } else {
                            // TODO: удаление с сервера
                        }
                        saleDialog.cancel();
                    }
                });
            }

            public void onSwipeLeft() {
                // свайп справа налево, добавление в список наличия
                if (!MainListAdapter.this.deleteSaleStatus) {
                    haveDialog = new Dialog(ctx);
                    haveDialog.setTitle("Добавить");
                    haveDialog.setContentView(R.layout.dialog_add_to_have);
                    if (MainListAdapter.this.deleteListStatus) {
                        ((TextView) haveDialog.findViewById(R.id.title_dialog)).setText("Удалить из моего списка?");
                    }
                    haveDialog.show();

                    ((Button) haveDialog.findViewById(R.id.btn_dialog_no)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            haveDialog.cancel();
                        }
                    });

                    ((Button) haveDialog.findViewById(R.id.btn_dialog_yes)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!MainListAdapter.this.deleteListStatus) {
                                SharedPreferences sp = ctx.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                                if (sp.getInt(Constants.USER_ID, -1) != -1) {
                                    ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                                    toServerItem.addToNomenclature(new AddItem(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId()))
                                            .enqueue(new Callback<ru.lifelaboratory.skb.Entity.Item>() {
                                                @Override
                                                public void onResponse(Call<Item> call, Response<Item> response) {
                                                }

                                                @Override
                                                public void onFailure(Call<Item> call, Throwable t) {
                                                }
                                            });
                                }
                            } else {
                                // TODO: удалить из моего списка с сервера
                            }
                            haveDialog.cancel();
                        }
                    });
                }
            }
        });

        return view;
    }
}

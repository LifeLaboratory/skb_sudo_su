package ru.lifelaboratory.skb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
    boolean ifSearch = false;

    public void setIfSearch(boolean ifSearch) {
        this.ifSearch = ifSearch;
    }

    long time = 0;

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

        final Integer barCodeItem = items.get(i).getId();


        if (items.get(i).getExpiredEnd() != null) {
            ((TextView) view.findViewById(R.id.elementDateEnd)).setText(items.get(i).getExpiredEnd());
            if (items.get(i).expired()) {
                view.setBackgroundColor(ctx.getResources().getColor(R.color.colorError));
            } else {
                view.setBackgroundColor(Color.WHITE);
            }
        }

        ((TextView) view.findViewById(R.id.elementTitle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toItemInfo = new Intent(ctx, ItemInfoActivity.class);
                Log.d(Constants.LOG, barCodeItem.toString());
                toItemInfo.putExtra(Constants.ITEM_ID, barCodeItem);
                toItemInfo.putExtra("SEARCH", ifSearch);
                ctx.startActivity(toItemInfo);
            }
        });

        final Integer numItem = i;

//        if (deleteSaleStatus) {
//            ((Button) view.findViewById(R.id.btn_have)).setVisibility(View.VISIBLE);
//        }

        if (MainListAdapter.this.deleteListStatus) {
            ((Button) view.findViewById(R.id.btn_have)).setText("Убрать из холодильника");
        } else {
            ((Button) view.findViewById(R.id.btn_have)).setText("В холодильник");
        }

        ((Button) view.findViewById(R.id.btn_have)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainListAdapter.this.deleteSaleStatus) {
                    haveDialog = new Dialog(ctx);
                    haveDialog.setTitle("Добавить");
                    haveDialog.setContentView(R.layout.dialog_add_to_have);
                    if (MainListAdapter.this.deleteListStatus) {
                        ((TextView) haveDialog.findViewById(R.id.title_dialog)).setText("Удалить из моего списка?");
                        ((Button) view.findViewById(R.id.btn_have)).setText("Убрать из холодильника");
                        ((CalendarView) haveDialog.findViewById(R.id.calendar_dialog)).setVisibility(View.GONE);
                    } else {
                        ((CalendarView) haveDialog.findViewById(R.id.calendar_dialog)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                                GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                                time = calendar.getTime().getTime();
                                Log.d(Constants.LOG, String.valueOf(time));
                            }
                        });
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
                                    toServerItem.addToNomenclature(new AddItem(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId(), time))
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
                                SharedPreferences sp = ctx.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                                ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                                toServerItem.deleteFromNom(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId_user_nom())
                                        .enqueue(new Callback<Item>() {
                                            @Override
                                            public void onResponse(Call<Item> call, Response<Item> response) {
                                            }

                                            @Override
                                            public void onFailure(Call<Item> call, Throwable t) {
                                                Toast.makeText(ctx, "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            haveDialog.cancel();
                        }
                    });
                }
            }
        });

        ((Button) view.findViewById(R.id.btn_sale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            SharedPreferences sp = ctx.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                            ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                            toServerItem.deleteFromSale(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId_sales())
                                    .enqueue(new Callback<Item>() {
                                        @Override
                                        public void onResponse(Call<Item> call, Response<Item> response) {
                                            Toast.makeText(ctx, numItem.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Item> call, Throwable t) {
                                            Toast.makeText(ctx, "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        saleDialog.cancel();
                    }
                });
            }
        });

        ((CardView) view.findViewById(R.id.card_view)).setOnTouchListener(new OnSwipeTouchListener(ctx) {
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
                            SharedPreferences sp = ctx.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                            ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                            toServerItem.deleteFromSale(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId_sales())
                                    .enqueue(new Callback<Item>() {
                                        @Override
                                        public void onResponse(Call<Item> call, Response<Item> response) {
                                            Toast.makeText(ctx, numItem.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Item> call, Throwable t) {
                                            Toast.makeText(ctx, "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
                        ((CalendarView) haveDialog.findViewById(R.id.calendar_dialog)).setVisibility(View.GONE);
                    } else {
                        ((CalendarView) haveDialog.findViewById(R.id.calendar_dialog)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                                Date date= new Date(year, month, dayOfMonth);
                                time = date.getTime();
                                Toast.makeText(ctx.getApplicationContext(), String.valueOf(time), Toast.LENGTH_LONG).show();
                            }
                        });
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
                                    toServerItem.addToNomenclature(new AddItem(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId(), time))
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
                                SharedPreferences sp = ctx.getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
                                ru.lifelaboratory.skb.REST.Item toServerItem = MainActivity.server.create(ru.lifelaboratory.skb.REST.Item.class);
                                toServerItem.deleteFromNom(sp.getInt(Constants.USER_ID, -1), items.get(numItem).getId_user_nom())
                                        .enqueue(new Callback<Item>() {
                                            @Override
                                            public void onResponse(Call<Item> call, Response<Item> response) {
                                            }

                                            @Override
                                            public void onFailure(Call<Item> call, Throwable t) {
                                                Toast.makeText(ctx, "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
                                            }
                                        });
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

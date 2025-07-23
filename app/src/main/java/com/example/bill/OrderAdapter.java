package com.example.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    //1
    private List<Order> orders;

    //2
    public OrderAdapter(Context context) {
        this.context = context;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders != null ? orders.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return orders != null ? orders.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.orderlist_item, parent, false);
        }

        TextView orderName = itemView.findViewById(R.id.orderName);
        TextView orderPrice = itemView.findViewById(R.id.orderPrice);
        TextView orderOuantity = itemView.findViewById(R.id.orderQuantity);
        TextView orderDate = itemView.findViewById(R.id.orderDate);

        Order order = orders.get(position);

        orderName.setText(order.getNamee());
        orderPrice.setText("Rs. " + order.getPricee());
        orderOuantity.setText("KG. " + order.getQuantityy());
        orderDate.setText(order.getDate());

        return itemView;
    }
}

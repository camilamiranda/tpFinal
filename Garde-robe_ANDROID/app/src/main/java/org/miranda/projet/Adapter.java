package org.miranda.projet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Bus;

import org.miranda.projet.events.EventChangeState;
import org.miranda.projet.events.EventDetailItem;
import org.miranda.Item;
import org.miranda.State;


public class Adapter extends ArrayAdapter<Item> {


    public Adapter(@NonNull Context context) {
        super(context, R.layout.list_item);
        MyBus.bus.register(this);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View v = li.inflate(R.layout.list_item, null);
        final Item item = getItem(position);
        TextView tvName = v.findViewById(R.id.tvName);
        Button btnState = v.findViewById(R.id.btnState);

        tvName.setText(item.name);
        btnState.setText(item.state.toString());
        if (item.state.equals(State.sale)) {
            btnState.setBackgroundResource(R.color.colorAccent2);
        }
        else if (item.state.equals(State.perdu)) {
            btnState.setBackgroundResource(R.color.colorAccent3);
        }

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBus.bus.post(new EventDetailItem(item));
            }
        });

        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBus.bus.post(new EventChangeState(item));
            }
        });

        return v;
    }
}

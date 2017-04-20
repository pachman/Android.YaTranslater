package com.example.alexander.yatranslator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.yatranslator.db.entities.TranslationItem;

import java.util.ArrayList;

/**
 * Created by as-si_000 on 17.04.2017.
 */

public class TranslationAdapter extends ArrayAdapter<TranslationItem> {
    private final Context mContext;
    private final ArrayList<TranslationItem> dataSet;

    private static class ViewHolder {
        TextView textFrom;
        TextView textTo;
        TextView direction;
        ImageView favoriteFlag;
    }

    public TranslationAdapter(@NonNull Context context, ArrayList<TranslationItem> data) {
        super(context, R.layout.translation_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TranslationItem translationItem = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.translation_item, parent, false);
            viewHolder.textFrom = (TextView) convertView.findViewById(R.id.text_from);
            viewHolder.textTo = (TextView) convertView.findViewById(R.id.text_to);
            viewHolder.direction = (TextView) convertView.findViewById(R.id.direction);
            viewHolder.favoriteFlag = (ImageView) convertView.findViewById(R.id.favoriteFlag);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.favoriteFlag.setOnClickListener(v -> {
            Log.d("[Debug]", "Click");
            Boolean isFavorite = translationItem.getIsFavorite();
            if (isFavorite) {
                viewHolder.favoriteFlag.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            }else {
                viewHolder.favoriteFlag.setImageResource(R.drawable.ic_bookmark_black_24dp);
            }
            translationItem.setIsFavorite(!isFavorite);
            //todo реализовать добавление/удаление из избранного
        });
        viewHolder.textFrom.setText(translationItem.getParameters().getText());
        viewHolder.textTo.setText(translationItem.getValues().get(0));
        viewHolder.direction.setText(translationItem.getParameters().getDirection());

        return convertView;
    }
}

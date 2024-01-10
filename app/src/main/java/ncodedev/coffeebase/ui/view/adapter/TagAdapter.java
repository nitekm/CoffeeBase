package ncodedev.coffeebase.ui.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ncodedev.coffeebase.R;
import ncodedev.coffeebase.model.domain.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagAdapter extends ArrayAdapter<Tag> {
    public TagAdapter(@NonNull @NotNull Context context, @NonNull @NotNull List<Tag> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createTagView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        return createTagView(position, convertView, parent);
    }

    private View createTagView(int position, View convertView, ViewGroup parent) {
        Tag tag = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_spinner, parent, false);
        }

        TextView name = convertView.findViewById(R.id.nameTextView);

        if (tag != null) {
            name.setText(tag.getName());
            name.setBackgroundColor(getColorIntFromTag(tag.getColor()));
        }
        return convertView;
    }

    private int getColorIntFromTag(String color) {
        //it converts color decimal string representation into hexadecimal representation in int
        return Color.parseColor(String.format("#%06X", 0xFFFFFF & Integer.parseInt(color)));
    }
}

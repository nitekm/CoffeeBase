package coffeebase.app.activity.groups;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import coffeebase.app.activity.R;
import coffeebase.app.activity.SingleGroupActivity;
import coffeebase.app.model.CoffeeGroup;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupRecViewAdapter extends RecyclerView.Adapter<GroupRecViewAdapter.ViewHolder> {

    private static final String TAG = "GroupRecViewAdapter";

    private ArrayList<CoffeeGroup> groups;
    private Context mContext;

    public GroupRecViewAdapter(final Context mContext, final ArrayList<CoffeeGroup> groups) {
        this.groups = groups;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GroupRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.groupNameTxt.setText(groups.get(position).getName());
        holder.groupTypeTxt.setText(groups.get(position).getGroupType().toString());
        Glide.with(mContext)
                .asBitmap()
                .load(groups.get(position).getImageUrl())
                .into(holder.groupImg);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(mContext, SingleGroupActivity.class);
                //intent.putExtra(GROUP_ID_KEY, groups.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView groupImg;
        private TextView groupNameTxt, groupTypeTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.groupCardView);
            groupImg = itemView.findViewById(R.id.groupImg);
            groupNameTxt = itemView.findViewById(R.id.groupNameTxt);
            groupTypeTxt = itemView.findViewById(R.id.groupTypeTxt);
        }
    }
}

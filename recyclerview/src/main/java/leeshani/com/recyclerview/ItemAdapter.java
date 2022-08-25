package leeshani.com.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.itemViewHolder>  {
    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        Item item = itemList.get(position);
        if(item == null){
            return;
        }
        holder.imgItem.setImageResource(item.getImage());
        holder.txtDescribe.setText(item.getDescribe());
        holder.txtName.setText(item.getName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem(item);
            }
        });


    }
    private void onClickItem(Item item){
        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        if (itemList!=null) return itemList.size();
        else return 0;
    }

    public class itemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItem;
        private TextView txtName, txtDescribe;
        private RelativeLayout layoutItem;
        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtname);
            txtDescribe = itemView.findViewById(R.id.txtDescribe);
            imgItem = itemView.findViewById(R.id.image);
            layoutItem = itemView.findViewById(R.id.layoutItem);
        }
    }
}


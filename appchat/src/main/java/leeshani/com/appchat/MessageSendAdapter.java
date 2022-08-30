package leeshani.com.appchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageSendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> messages = new ArrayList<>();
    final private static int TYPE_MESSAGE_SEND = 0;
    final private static int TYPE_MESSAGE_RECEIVE = 1;

    public void setData(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_MESSAGE_SEND:
                View viewSend = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send, parent, false);
                return new MessageSendViewHolder(viewSend);
            case TYPE_MESSAGE_RECEIVE:
                View viewReceive  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recieve, parent, false);
                return new MessageReceiveViewHolder(viewReceive);
            default:
                View viewDefault  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recieve, parent, false);
                return new MessageReceiveViewHolder(viewDefault);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        switch (holder.getItemViewType()){
            case TYPE_MESSAGE_SEND:
                MessageSendViewHolder messageSendViewHolder = (MessageSendViewHolder) holder;
                messageSendViewHolder.tvMessageSend.setText(message.getMessage());
                break;
            case TYPE_MESSAGE_RECEIVE:
                MessageReceiveViewHolder messageReceiveViewHolder = (MessageReceiveViewHolder) holder;
                messageReceiveViewHolder.tvMessageReceive.setText(message.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.isInput()) {
            return TYPE_MESSAGE_SEND;
        } else return TYPE_MESSAGE_RECEIVE;
    }

    public class MessageSendViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessageSend;

        public MessageSendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageSend = itemView.findViewById(R.id.tvSendMess);

        }
    }

    public class MessageReceiveViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessageReceive;

        public MessageReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageReceive = itemView.findViewById(R.id.tvReceiveMess);
        }
    }
}

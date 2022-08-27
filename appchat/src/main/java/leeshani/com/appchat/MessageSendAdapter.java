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
    private static int TYPE_MESSAGE_SEND = 0;
    private static int TYPE_MESSAGE_RECEIVE = 1;

    public void setData(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_MESSAGE_SEND == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send, parent, false);
            return new MessageSendViewHolder(view);
        } else if (TYPE_MESSAGE_RECEIVE == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recieve, parent, false);
            return new MessageReceiveViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (TYPE_MESSAGE_SEND == holder.getItemViewType()) {
            MessageSendViewHolder messageSendViewHolder = (MessageSendViewHolder) holder;
            messageSendViewHolder.tvMessageSend.setText(message.getMessage());

        } else if (TYPE_MESSAGE_RECEIVE == holder.getItemViewType()) {
            MessageReceiveViewHolder messageReceiveViewHolder = (MessageReceiveViewHolder) holder;
            messageReceiveViewHolder.tvMessageReceive.setText(message.getMessage());
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

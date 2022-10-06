package leeshani.com.roomdatabases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteStudentDialogFragment extends DialogFragment {
    TextView tvTitle;
    TextView tvMessage;
    TextView tvAgree;
    TextView tvNotAgree;
    OnListener listener;

    public ConfirmDeleteStudentDialogFragment() {
    }
    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.confirm_delete_student_dialog,null);
        initUi(view);
        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.confirmDelete();
            }
        });
        tvNotAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void initUi(View view){
        tvTitle = view.findViewById(R.id.tv_title_confirm_delete);
        tvMessage = view.findViewById(R.id.tv_message_dialog);
        tvAgree = view.findViewById(R.id.tv_agree);
        tvNotAgree = view.findViewById(R.id.tv_not_agree);
    }

    public static final String TAG = "ConfirmDelete";
    public interface OnListener{
        void confirmDelete();
    }
}
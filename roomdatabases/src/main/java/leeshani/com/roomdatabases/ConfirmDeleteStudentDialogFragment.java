package leeshani.com.roomdatabases;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteStudentDialogFragment extends DialogFragment {
    OnListener listener;

    public ConfirmDeleteStudentDialogFragment() {
    }
    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder confirmDeleteStudent = new AlertDialog.Builder(getActivity());
        confirmDeleteStudent.setTitle("Delete Student");
        confirmDeleteStudent.setMessage("Are you sure");
        confirmDeleteStudent.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.ConfirmDelete();
            }
        });
        confirmDeleteStudent.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return confirmDeleteStudent.create();
    }
    public static String TAG = "ConfirmDelete";
    public interface OnListener{
        void ConfirmDelete();
    }
}
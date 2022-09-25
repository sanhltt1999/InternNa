package leeshani.com.roomdatabases.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import leeshani.com.roomdatabases.R;

public class TakePhotoBottomDialogFragment extends BottomSheetDialogFragment {
    private TextView tvTakePhoto;
    private TextView tvChooseFile;
    OnListener onListener;

    public TakePhotoBottomDialogFragment() {
    }

    public void setImage (OnListener onListener) {
        this.onListener = onListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.button_sheet_choose_photo_or_take_photo, container,false);

        tvTakePhoto = view.findViewById(R.id.tvTakePhoto);
        tvChooseFile = view.findViewById(R.id.tvChooseFile);
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListener.TakePhoto();
                dismiss();
            }
        });

        tvChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListener.ChooseImage();
                dismiss();
            }
        });
        return view;
    }

    public interface OnListener {
        void ChooseImage();
        void TakePhoto();
    }
}

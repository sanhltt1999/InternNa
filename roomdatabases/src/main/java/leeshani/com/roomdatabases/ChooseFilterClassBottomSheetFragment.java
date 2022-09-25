package leeshani.com.roomdatabases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChooseFilterClassBottomSheetFragment extends BottomSheetDialogFragment {

    ImageView ivClose;
    Button btnApply;
    NumberPicker npClass;
    String[] classStudents;
    OnListener listener;

    public ChooseFilterClassBottomSheetFragment(String[] classStudents) {
        this.classStudents = classStudents;
    }

    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.bottom_sheet, container, false);
        ivClose = rootView.findViewById(R.id.ivCloseBs);
        btnApply = rootView.findViewById(R.id.btnApplyClass);
        npClass = rootView.findViewById(R.id.npClass);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if(classStudents.length == 0){
            Toast.makeText(getActivity(), "Add classes and students to show ", Toast.LENGTH_SHORT).show();
        }else {
        npClass.setMinValue(0);
        npClass.setMaxValue(classStudents.length-1);
        npClass.setDisplayedValues(classStudents);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.ChooseClass(classStudents[npClass.getValue()]);

            }
        });}
        return rootView;
    }

    public interface OnListener {
        void ChooseClass(String className);
    }

}

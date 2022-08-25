package leeshani.com.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rcvData = findViewById(R.id.rcvData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvData.setLayoutManager(linearLayoutManager);
        ItemAdapter itemAdapter = new ItemAdapter(getListItem(), this);
        rcvData.setAdapter(itemAdapter);
    }
    private List<Item> getListItem(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_chick_1,"Chick","Yellow"));
        list.add(new Item(R.drawable.ic_dinosaur_1,"Dinosaur","Green"));
        list.add(new Item(R.drawable.ic_frog,"Frog","Green"));
        list.add(new Item(R.drawable.ic_heart,"Heart","Let love it"));
        list.add(new Item(R.drawable.ic_panda,"Panda","So cute"));
        list.add(new Item(R.drawable.ic_sloth,"Sloth","Yeah"));
        return list;
    }
}

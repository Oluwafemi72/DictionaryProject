package algonquin.cst2335.dictionaryproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Dictionary.MyDictionary> MyDictionaryArrayList;

    public MyAdapter(Context context, ArrayList<Dictionary.MyDictionary> myDictionaryArrayList) {
        this.context = context;
        MyDictionaryArrayList = myDictionaryArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.definition_items1,parent,false);




        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Dictionary.MyDictionary dictionary = MyDictionaryArrayList.get(position);

        holder.word.setText(MyDictionaryArrayList.get(position).words);
        holder.definition.setText(MyDictionaryArrayList.get(position).definition);



    }

    @Override
    public int getItemCount() {

        return MyDictionaryArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        EditText word;
        TextView definition;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            word = itemView.findViewById(R.id.txtWordDef);
            definition = itemView.findViewById(R.id.txtDef);


        }
    }
}

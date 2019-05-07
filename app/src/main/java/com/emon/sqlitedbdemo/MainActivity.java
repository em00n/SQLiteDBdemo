package com.emon.sqlitedbdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    FavoriteList favList = new FavoriteList();
    Button add,checkcount,list;
    Context context = this;
    DatabaseHandler db;
    ListView listView;
    List<FavoriteList> favoriteList;
    LinearLayout layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(addOnClick);
        checkcount = (Button)findViewById(R.id.checkcount);
        checkcount.setOnClickListener(checkcountOnClick);
        list = (Button)findViewById(R.id.list);
        list.setOnClickListener(listOnClick);
        layout = (LinearLayout)findViewById(R.id.layout);
        listView = (ListView)findViewById(R.id.listView);
        db = new DatabaseHandler(this);

        //db.removeFav();

  /*favList = db.getFavList();
  Toast.makeText(getApplicationContext(), ""+favList.getSongname(), Toast.LENGTH_LONG).show();*/
    }

    View.OnClickListener addOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.row);
            dialog.setTitle("Add Data to Database");
            final EditText name = (EditText) dialog.findViewById(R.id.name);
            final EditText age = (EditText) dialog.findViewById(R.id.age);
            Button Add = (Button) dialog.findViewById(R.id.Add);
            Add.setText("Add");
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText().toString() != null && name.getText().toString().length() >0 ){
                        if(age.getText().toString() != null && age.getText().toString().length() >0 ){
                            db.adddata(context, name.getText().toString(), age.getText().toString());
                            favoriteList = db.getFavList();
                            listView.setAdapter(new ViewAdapter());
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Enter the Age", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please Enter the Name", Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.show();
        }
    };

    View.OnClickListener checkcountOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int uyu = db.getCount();
            Toast.makeText(getApplicationContext(), ""+uyu, Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener listOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            favoriteList = db.getFavList();
            listView.setAdapter(new ViewAdapter());
        }
    };

    public class ViewAdapter extends BaseAdapter {

        LayoutInflater mInflater;

        public ViewAdapter() {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return favoriteList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listitem,null);
            }

            final TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
            nameText.setText("Name : "+favoriteList.get(position).getName());
            final TextView ageText = (TextView) convertView.findViewById(R.id.ageText);
            ageText.setText("Age : "+favoriteList.get(position).getAge());

            final Button delete = (Button) convertView.findViewById(R.id.delete);
            final Button update = (Button) convertView.findViewById(R.id.edit);




            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.row);
                    dialog.setTitle("update Data to Database");
                    final EditText name = (EditText) dialog.findViewById(R.id.name);
                    final EditText age = (EditText) dialog.findViewById(R.id.age);
                    name.setText(favoriteList.get(position).getName());
                    age.setText(favoriteList.get(position).getAge());
                    Button Add = (Button) dialog.findViewById(R.id.Add);
                    Add.setText("Update");
                    Add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id=String.valueOf(favoriteList.get(position).getId());
                            if(name.getText().toString() != null && name.getText().toString().length() >0 ){
                                if(age.getText().toString() != null && age.getText().toString().length() >0 ){
                                    db.updateDate(id, name.getText().toString(), age.getText().toString());
                                    favoriteList = db.getFavList();
                                    listView.setAdapter(new ViewAdapter());
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please Enter the Age", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please Enter the Name", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show();
                    }
                    });



            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.removeFav(favoriteList.get(position).getId());
                    notifyDataSetChanged();
                    favoriteList = db.getFavList();
                    listView.setAdapter(new ViewAdapter());
                }
            });
            return convertView;
        }
    }

}
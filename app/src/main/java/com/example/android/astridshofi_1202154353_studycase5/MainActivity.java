package com.example.android.astridshofi_1202154353_studycase5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Database DB;
    private RecyclerView rcView;
    private Adapter adapter;
    private ArrayList<DataKegiatan> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ToDo List");

        rcView = findViewById(R.id.rec_view);
        data_list = new ArrayList<>();
        DB = new Database(this); //membuat database
        DB.readdata(data_list); //memanggil method readdata

        SharedPreferences sharedP = this.getApplicationContext().getSharedPreferences("Preferences", 0); //menginisialisasi shared preference
        int color = sharedP.getInt("Colourground", R.color.white); //warna

        adapter = new Adapter(this,data_list, color); //membuat adapter
        rcView.setHasFixedSize(true); //merubah ukuran
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(adapter); //inisiasi adapter untuk recycler view

        DeleteSwipe(); //menjalankan method DeleteSwipe
    }

    public void DeleteSwipe(){ //method untuk dapat menghapus data dengan swipe
        ItemTouchHelper.SimpleCallback touchcall = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) { //membuat touch helper baru untuk recycler view dan swipe kanan untuk menghapus

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                DataKegiatan current = adapter.getData(position);
                if(direction==ItemTouchHelper.RIGHT){
                    if(DB.removedata(current.getTodo())){ //remove item yang dipilih dengan todo sebagai primary key
                        adapter.deleteData(position); //hapus data
                        Snackbar.make(findViewById(R.id.coordinator), "Berhasil dihapus", 2000).show(); //membuat snack bar dan pemberitahuan bahwa item sudah terhapus dengan durasi 2 sekon
                    }
                }
            }
        };
        ItemTouchHelper touchhelp = new ItemTouchHelper(touchcall); //menentukan itemtouchhelper untuk recycler view
        touchhelp.attachToRecyclerView(rcView); //memanggil rcView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //method yang dijalankan ketika item di pilih
        int id = item.getItemId(); //mengambil id dari item id
        if (id==R.id.action_settings){
            Intent intent = new Intent(MainActivity.this, Settings.class); //membuat intent
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void addlist(View view) { //method yang akan dijalankan ketika tombol add di klik
        Intent intent = new Intent(MainActivity.this, TambahKegiatan .class);
        startActivity(intent);
    }
}

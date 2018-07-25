package com.example.tomato.lesson6sqlitetdgiang.activity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tomato.lesson6sqlitetdgiang.R;
import com.example.tomato.lesson6sqlitetdgiang.adapter.ContactAdapter;
import com.example.tomato.lesson6sqlitetdgiang.database.DBManager;
import com.example.tomato.lesson6sqlitetdgiang.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        ContactAdapter.OnCallBack, View.OnClickListener {

    public static final int FLAG_INSERT = 1;
    public static final int FLAG_EDIT = 2;
    public static final int FLAG_DELETE = 3;

    Boolean status = false;
    RecyclerView recyclerView;
    SwitchCompat switchStatus;
    LinearLayoutManager layoutManager;
    ArrayList<Contact> arrayList = null;
    ImageButton ibtnAdd;
    ContactAdapter adapter;
    Button btnDelete, btnCancel;

    public static DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);

        initView();
        initEvent();
        initData();
        updateData(status);

    }


    private void initData() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        optimizationInterface();
        arrayList = new ArrayList<>();

        adapter = new ContactAdapter(arrayList, getApplicationContext(), this, status);
        recyclerView.setAdapter(adapter);

        btnDelete.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }

    private void initEvent() {
        switchStatus.setOnCheckedChangeListener(this);
        ibtnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initView() {
        switchStatus = findViewById(R.id.switch_status);
        recyclerView = findViewById(R.id.recyclerview);
        ibtnAdd = findViewById(R.id.imgbtn_add);
        btnCancel = findViewById(R.id.btn_cancel);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void changeInterface() {
        if (switchStatus.isChecked()) {
            layoutManager = new GridLayoutManager(this, 2);
            optimizationInterface();
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            optimizationInterface();
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private void optimizationInterface() {
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void addContact() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtName = dialog.findViewById(R.id.edt_name);
        final EditText edtPhoneNumber = dialog.findViewById(R.id.edt_phone_number);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtName.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtPhoneNumber.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, R.string.notification, Toast.LENGTH_SHORT).show();
                } else {
                    Contact contact = new Contact(edtName.getText().toString().trim(), edtPhoneNumber.getText().toString().trim());
                    PreparingDB preparing = new PreparingDB(contact, FLAG_INSERT);
                    preparing.execute();

                    Toast.makeText(MainActivity.this, "insert successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_add:
                addContact();
                break;
            case R.id.btn_cancel:
                hideView();
                break;
            case R.id.btn_delete:
                deleteCheckBox();
                break;
            default:
                break;
        }
    }

    private void deleteCheckBox() {
        SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
        //Check if item is selected or not via size
        if (selectedRows.size() > 0) {
            //Loop to all the selected rows array
            for (int i = (selectedRows.size() - 1); i >= 0; i--) {

                //Check if selected rows have value i.e. checked item
                if (selectedRows.valueAt(i)) {

                    //remove the checked item
                    final Contact contact = arrayList.get(selectedRows.keyAt(i));
                    PreparingDB preparingDB = new PreparingDB(contact, FLAG_DELETE);
                    preparingDB.execute();
                }
            }
            //notify the adapter and remove all checked selection
            adapter.removeSelection();
        }
    }

    private void hideView() {
        status = false;
        updateData(status);
    }

    private void editContact(final int position) {
        final Contact contact = arrayList.get(position);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        //  dialog.setCanceledOnTouchOutside(false);

        final EditText edtName = dialog.findViewById(R.id.edt_name);
        final EditText edtPhoneNumber = dialog.findViewById(R.id.edt_phone_number);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        edtName.setText(contact.getName());
        edtPhoneNumber.setText(contact.getNumberPhone());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PreparingDB preparingDB = new PreparingDB(contact, FLAG_DELETE);
                preparingDB.execute();

                Toast.makeText(MainActivity.this, "delete successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtName.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtPhoneNumber.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, R.string.notification, Toast.LENGTH_SHORT).show();
                } else {
                    contact.setName(edtName.getText().toString().trim());
                    contact.setNumberPhone(edtPhoneNumber.getText().toString().trim());

                    PreparingDB preparingDB = new PreparingDB(contact, FLAG_EDIT);
                    preparingDB.execute();

                    Toast.makeText(MainActivity.this, "update successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_status:
                changeInterface();
                break;
            default:
                break;
        }
    }

    // methos is user create in ContactAdapter
    @Override
    public void onItemClicked(int position, boolean isLongClick) {
        if (isLongClick) {    // longClick
            status = true;
            updateData(status);
        } else {
            editContact(position);
        }
    }

    public void updateData(Boolean status) {
        arrayList.clear();
        arrayList = (ArrayList<Contact>) dbManager.getAllData();
        adapter = new ContactAdapter(arrayList, getApplicationContext(), this, status);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.getLayoutManager().scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

        if (status) {
            btnCancel.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    public class PreparingDB extends AsyncTask {

        Contact mContact;
        int flag = 0;

        public PreparingDB(Contact contact, int flag) {
            this.mContact = contact;
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Do in backgground !", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            switch (flag) {
                case FLAG_INSERT:
                    dbManager.insertData(mContact);
                    break;
                case FLAG_EDIT:
                    dbManager.updateData(mContact);
                    break;
                case FLAG_DELETE:
                    dbManager.deleteData(mContact);
                    break;
                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            updateData(status);
            //Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
        }
    }

}


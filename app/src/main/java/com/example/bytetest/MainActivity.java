package com.example.bytetest;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bytedance.database.DatabaseHelper;
import com.bytedance.database.TodoContract;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mInternalPath;
    private TextView mExternalPath;
    private EditText mSaveKey;
    private EditText mSaveValue;
    private EditText mSaveDatabaseText;
    private Button mInternalPathButton;
    private Button mExternalPathButton;
    private Button mSaveKeyValueButton;
    private Button mGetValueButton;
    private Button mSaveDatabaseButton;
    private TextView mKey;
    private final static int CODE_REQUEST_PERMISSION = 1;
    private SharedPreferences sp;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInternalPath = findViewById(R.id.internal_path_show);
        mExternalPath = findViewById(R.id.external_path_show);
        mSaveKey = findViewById(R.id.save_key);
        mSaveValue = findViewById(R.id.save_value);
        mGetValueButton = findViewById(R.id.get_value);
        mKey = findViewById(R.id.key);
        mSaveDatabaseText = findViewById(R.id.content);
        mInternalPathButton = findViewById(R.id.get_internal_path);
        mExternalPathButton = findViewById(R.id.get_external_path);
        mSaveKeyValueButton = findViewById(R.id.save_key_value);
        mSaveDatabaseButton = findViewById(R.id.save_database);
        mInternalPathButton.setOnClickListener(this);
        mExternalPathButton.setOnClickListener(this);
        mSaveKeyValueButton.setOnClickListener(this);
        mSaveDatabaseButton.setOnClickListener(this);
        mGetValueButton.setOnClickListener(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        requestPermission();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.get_internal_path) {
            mInternalPath.setText(
                "File目录：" + getFilesDir() + "\r\ncache目录：" + getCacheDir() + "\r\n自定义目录：" + getDir(
                    "ByteTest", Context.MODE_PRIVATE));
        } else if (v.getId() == R.id.get_external_path) {
            if (isExternalStorageReadable() && isExternalStorageWritable()) {
                Toast.makeText(this, "存储可用", 2 * 1000).show();
            } else {
                Toast.makeText(this, "存储不可用", 2 * 1000).show();
            }
            mExternalPath.setText("私有File目录："
                + getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                + "\r\n私有cache目录："
                + getExternalCacheDir()
                + "\r\n公共标注目录："
                + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "\r\n公共根目录"
                + Environment.getExternalStorageDirectory());
        } else if (v.getId() == R.id.save_key_value) {
            String key = mSaveKey.getText().toString();
            String value = mSaveValue.getText().toString();
            putKeyValue(key, value);
        } else if(v.getId() == R.id.get_value) {
            String key = mKey.getText().toString();
            String value = getString(key);
            Toast.makeText(this, value, 2 * 1000).show();
        } else if(v.getId() == R.id.save_database) {
            String content = mSaveDatabaseText.getText().toString();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TodoContract.TodoNote.COLUMN_TODO_CONTENT, content);
            mDatabase.insert(TodoContract.TodoNote.TABLE_NAME, null, contentValues);
        }
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }, CODE_REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @Nullable String[] permissions,
                                           @Nullable int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "用户已授权", 2 * 1000).show();
            } else {
                Toast.makeText(this, "用户拒绝了授权", 2 * 1000).show();
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(
            state)) {
            return true;
        }
        return false;
    }

    public void putKeyValue(String key, String value) {
        if(sp == null) {
            sp = getSharedPreferences("byteTest", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        //editor.commit();
        editor.apply();
        System.exit(0);
    }

    public String getString(String key) {
        if(sp == null) {
            sp = getSharedPreferences("byteTest", Context.MODE_PRIVATE);
        }
        return sp.getString(key, "You didn't save this key!!!");
    }
}
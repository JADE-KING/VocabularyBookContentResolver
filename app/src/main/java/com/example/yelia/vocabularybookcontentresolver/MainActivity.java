package com.example.yelia.vocabularybookcontentresolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements WordDBConstruct {
    private ContentResolver contentResolver;
    private final static String VOCABULARY_BOOK_CONTENT_URI_STRING =
            "content://com.example.yelia.lalala/" + TABLE_NAME;
    private final static Uri VOCABULARY_BOOK_CONTENT_URI =
            Uri.parse(VOCABULARY_BOOK_CONTENT_URI_STRING);
    private List<Map<String, Object>> wordList = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = this.getContentResolver();

        final ListView wordListView = (ListView)findViewById(R.id.wordListView);
        Button getAllBtn = (Button)findViewById(R.id.getAllBtn);
        Button insertBtn = (Button)findViewById(R.id.insertBtn);
        Button deleteBtn = (Button)findViewById(R.id.deleteBtn);
        Button updateBtn = (Button)findViewById(R.id.updateBtn);
        Button selectBtn = (Button)findViewById(R.id.selectBtn);

        // 获取列表
        getAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordList.clear();

                String[] columns = new String[] {
                        COLUMN_NAME_ID,
                        COLUMN_NAME_WORD,
                        COLUMN_NAME_TRANSLATION
                };
                Cursor cursor = contentResolver.query(VOCABULARY_BOOK_CONTENT_URI, columns, null, null, COLUMN_NAME_WORD + " ASC");
                while (cursor.moveToNext()) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
                    item.put(COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WORD)));
                    item.put(COLUMN_NAME_TRANSLATION, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TRANSLATION)));
                    wordList.add(item);
                }

                SimpleAdapter wordListAdapter = new SimpleAdapter(
                        getApplicationContext(), wordList, R.layout.word_list_item,
                        new String[] { COLUMN_NAME_WORD, COLUMN_NAME_TRANSLATION },
                        new int[] { R.id.wordText, R.id.meanText });
                wordListView.setAdapter(wordListAdapter);
            }
        });

        // 添加新的Word和Translation
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.insert_dialog, null);
                final EditText insertWord = (EditText)view.findViewById(R.id.insertWord);
                final EditText insertTranslation = (EditText)view.findViewById(R.id.insertTranslation);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                builder.setTitle("增加单词");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_NAME_WORD, insertWord.getText().toString());
                        values.put(COLUMN_NAME_TRANSLATION, insertTranslation.getText().toString());

                        Uri newUri = contentResolver.insert(VOCABULARY_BOOK_CONTENT_URI, values);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        // 删除Word
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.delete_dialog, null);
                final EditText deleteWord = (EditText)view.findViewById(R.id.deleteWord);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                builder.setTitle("删除单词");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(VOCABULARY_BOOK_CONTENT_URI_STRING + "/" + deleteWord.getText().toString());
                        int result = contentResolver.delete(uri, null, null);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        // 修改Translation
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.update_translation_dialog, null);
                final EditText updateWord = (EditText)view.findViewById(R.id.updateWord);
                final EditText updateTranslation = (EditText)view.findViewById(R.id.updateTranslation);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                builder.setTitle("修改单词");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_NAME_WORD, updateWord.getText().toString());
                        values.put(COLUMN_NAME_TRANSLATION, updateTranslation.getText().toString());

                        Uri uri = Uri.parse(VOCABULARY_BOOK_CONTENT_URI_STRING + "/" + updateWord.getText().toString());
                        int result = contentResolver.update(uri, values, null, null);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        // 模糊查询
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.fuzzy_query_dialog, null);
                final EditText fuzzyQueryWord = (EditText)view.findViewById(R.id.fuzzyQueryWord);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                builder.setTitle("模糊查询");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(VOCABULARY_BOOK_CONTENT_URI_STRING + "/" + fuzzyQueryWord.getText().toString());

                        wordList.clear();

                        String[] columns = new String[] {
                                COLUMN_NAME_ID,
                                COLUMN_NAME_WORD,
                                COLUMN_NAME_TRANSLATION
                        };
                        Cursor cursor = contentResolver.query(uri, columns, null, null, COLUMN_NAME_WORD + " ASC");
                        while (cursor.moveToNext()) {
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
                            item.put(COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WORD)));
                            item.put(COLUMN_NAME_TRANSLATION, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TRANSLATION)));
                            wordList.add(item);
                        }

                        SimpleAdapter wordListAdapter = new SimpleAdapter(
                                getApplicationContext(), wordList, R.layout.word_list_item,
                                new String[] { COLUMN_NAME_WORD, COLUMN_NAME_TRANSLATION },
                                new int[] { R.id.wordText, R.id.meanText });
                        wordListView.setAdapter(wordListAdapter);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }
}

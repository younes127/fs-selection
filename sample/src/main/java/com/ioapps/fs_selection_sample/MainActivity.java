package com.ioapps.fs_selection_sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ioapps.fs_selection.FileSelector;


public class MainActivity extends Activity {

    private Button buttonChooseFile, buttonChooseFileByExt, buttonChooseMultipleFiles, buttonChooseMultipleFilesByExt,
            buttonChooseFolder, buttonChooseMultipleFolders,
            buttonChooseAnyUnique, buttonChooseAnyMultiple;
    private EditText editTextFileByExt, editTextMultipleFilesByExt, editTextResult;

    private FileSelector fileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileSelector = new FileSelector(this);

        buttonChooseFile = (Button) findViewById(R.id.buttonChooseFile);
        buttonChooseFileByExt = (Button) findViewById(R.id.buttonChooseFileByExt);
        buttonChooseMultipleFiles = (Button) findViewById(R.id.buttonChooseMultipleFiles);
        buttonChooseMultipleFilesByExt = (Button) findViewById(R.id.buttonChooseMultipleFilesByExt);
        buttonChooseFolder = (Button) findViewById(R.id.buttonChooseFolder);
        buttonChooseMultipleFolders = (Button) findViewById(R.id.buttonChooseMultipleFolders);
        buttonChooseAnyUnique = (Button) findViewById(R.id.buttonChooseAnyUnique);
        buttonChooseAnyMultiple = (Button) findViewById(R.id.buttonChooseAnyMultiple);
        editTextFileByExt = (EditText) findViewById(R.id.editTextFileByExt);
        editTextMultipleFilesByExt = (EditText) findViewById(R.id.editTextMultipleFilesByExt);
        editTextResult = (EditText) findViewById(R.id.editTextResult);

        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector.chooseUniqueFile("Choose File", "");
            }
        });

        buttonChooseFileByExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ext = editTextFileByExt.getText().toString();
                ext = ext.replaceAll("^\\.+", "");
                fileSelector.chooseUniqueFileByExtension("Choose File By Ext", "Type (" + ext + ")", ext);
            }
        });

        buttonChooseMultipleFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector.chooseMultipleFiles("Choose Multiple Files", null);
            }
        });

        buttonChooseMultipleFilesByExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ext = editTextMultipleFilesByExt.getText().toString();
                ext = ext.replaceAll("^\\.+", "");
                fileSelector.chooseMultipleFilesByExtension("Choose Multiple Files By Ext", "Type (" + ext + ")", ext);
            }
        });

        buttonChooseFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector.chooseUniqueFolder("Choose Folder", "Operation description ..");
            }
        });

        buttonChooseMultipleFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector.chooseMultipleFolders("Choose Multiple Folders", "Operation description ..");
            }
        });

        buttonChooseAnyUnique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector.chooseAnyUnique("Choose Any Unique", null);
            }
        });

        buttonChooseAnyMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelector.chooseAnyMultiple("Choose Any Multiple", null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == fileSelector.getRequestCode()) {
            editTextResult.setText("");

            if(resultCode == RESULT_OK) {
                Uri[] uris = fileSelector.loadResult(intent);

                for(int i = 0; i < uris.length; i++) {
                    editTextResult.append(uris[i].toString());

                    if(i < uris.length - 1) {
                        editTextResult.append("\n");
                    }
                }
            } else {
                editTextResult.append("Nothing!");
            }
        }
    }
}

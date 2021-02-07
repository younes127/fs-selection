package com.ioapps.fs_selection_sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ioapps.fs_selection.FileSelector;

import java.io.File;


public class MainActivity extends Activity {

    private Button buttonChooseFile, buttonChooseFileByExt, buttonChooseMultipleFiles, buttonChooseMultipleFilesByExt,
            buttonChooseFolder, buttonChooseMultipleFolders,
            buttonChooseAnyUnique, buttonChooseAnyMultiple,
            buttonOpenFolder, buttonEditTextFile, buttonZipViewer;
    private EditText editTextFileByExt, editTextMultipleFilesByExt, editTextResult, editTextOpenFolder, editTextEditTextFile, editTextZipViewer;

    private FileSelector fileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        fileSelector = new FileSelector(this);

        buttonChooseFile = (Button) findViewById(R.id.buttonChooseFile);
        buttonChooseFileByExt = (Button) findViewById(R.id.buttonChooseFileByExt);
        buttonChooseMultipleFiles = (Button) findViewById(R.id.buttonChooseMultipleFiles);
        buttonChooseMultipleFilesByExt = (Button) findViewById(R.id.buttonChooseMultipleFilesByExt);
        buttonChooseFolder = (Button) findViewById(R.id.buttonChooseFolder);
        buttonChooseMultipleFolders = (Button) findViewById(R.id.buttonChooseMultipleFolders);
        buttonChooseAnyUnique = (Button) findViewById(R.id.buttonChooseAnyUnique);
        buttonChooseAnyMultiple = (Button) findViewById(R.id.buttonChooseAnyMultiple);
        buttonOpenFolder = (Button) findViewById(R.id.buttonOpenFolder);
        buttonEditTextFile = (Button) findViewById(R.id.buttonEditTextFile);
        buttonZipViewer = (Button) findViewById(R.id.buttonZipViewer);
        editTextFileByExt = (EditText) findViewById(R.id.editTextFileByExt);
        editTextMultipleFilesByExt = (EditText) findViewById(R.id.editTextMultipleFilesByExt);
        editTextResult = (EditText) findViewById(R.id.editTextResult);
        editTextOpenFolder = (EditText) findViewById(R.id.editTextOpenFolder);
        editTextEditTextFile = (EditText) findViewById(R.id.editTextEditTextFile);
        editTextZipViewer = (EditText) findViewById(R.id.editTextZipViewer);

        //fileSelector.setCurrentDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

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

        buttonOpenFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderPath = editTextOpenFolder.getText().toString();
                Uri uri = Uri.fromFile(new File(folderPath));
                fileSelector.openFolder(uri);
            }
        });

        buttonEditTextFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = editTextEditTextFile.getText().toString();
                Uri uri = Uri.fromFile(new File(filePath));
                fileSelector.editTextFile(uri);
            }
        });

        buttonZipViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = editTextZipViewer.getText().toString();
                Uri uri = Uri.fromFile(new File(filePath));
                fileSelector.exploreZipFile(uri);
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

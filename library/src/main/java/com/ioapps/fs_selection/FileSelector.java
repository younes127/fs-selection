package com.ioapps.fs_selection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class FileSelector {

    private final Activity context;
    private String chooserTitle;
    private int requestCode = 5555;
    private boolean showOnlyFs = true, mandatoryFs = true;
    private Uri currentUri;

    public FileSelector(Activity context) {
        this.context = context;
        chooserTitle = context.getString(R.string.select_chooser);
    }

    public String getChooserTitle() {
        return chooserTitle;
    }

    public void setChooserTitle(String chooserTitle) {
        this.chooserTitle = chooserTitle;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setShowOnlyFs(boolean showOnlyFs) {
        this.showOnlyFs = showOnlyFs;
    }

    public void setMandatoryFs(boolean mandatoryFs) {
        this.mandatoryFs = mandatoryFs;
    }

    public void setCurrentDirectory(Uri uri) {
        currentUri = uri;
    }

    public void chooseUniqueFile(String title, String subject) {
        chooseFile(title, subject, "file/");
    }

    public void chooseUniqueFileByExtension(String title, String subject, String extension) {
        chooseFile(title, subject, "file/" + extension);
    }

    public void chooseMultipleFiles(String title, String subject) {
        chooseFile(title, subject, "file/multiple");
    }

    public void chooseMultipleFilesByExtension(String title, String subject, String extension) {
        chooseFile(title, subject, "file/" + extension + "/multiple");
    }

    public void chooseUniqueFolder(String title, String subject) {
        chooseFile(title, subject, "resource/folder");
    }

    public void chooseMultipleFolders(String title, String subject) {
        chooseFile(title, subject, "folder/multiple");
    }

    public void chooseAnyUnique(String title, String subject) {
        chooseFile(title, subject, "any/");
    }

    public void chooseAnyMultiple(String title, String subject) {
        chooseFile(title, subject, "any/multiple");
    }

    public void openFolder(Uri uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "resource/folder");

        Intent fsIntent = getFsIntent(intent, FsActivity.MAIN);
        if(fsIntent == null) {
            return;
        }

        context.startActivity(fsIntent);
    }

    public void editTextFile(Uri uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/plain");

        Intent fsIntent = getFsIntent(intent, FsActivity.TEXT_EDITOR);
        if(fsIntent == null) {
            return;
        }

        context.startActivity(fsIntent);
    }

    public void exploreZipFile(Uri uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/zip");

        Intent fsIntent = getFsIntent(intent, FsActivity.ZIP_VIEWER);
        if(fsIntent == null) {
            return;
        }

        context.startActivity(fsIntent);
    }

    /**
     * Must be called from {@link Activity#onActivityResult(int, int, Intent)} of the involved
     * activity.
     * @return
     * <br>an array with the selected elements
     * <br>or an empty array if the operation has been canceled by the user
     * <br>or null if the operation does not correspond
     */
    public Uri[] onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(this.requestCode == requestCode) {
            if(resultCode == Activity.RESULT_OK) {
                return loadResult(intent);
            } else {
                return new Uri[] {};
            }
        }

        return null;
    }

    public Uri[] loadResult(Intent intent) {
        if(intent == null) {
            return new Uri[] {};
        }

        Uri data = intent.getData();
        if(data != null) {
            return new Uri[] {data};
        }

        ArrayList<Uri> uriList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        return uriList.toArray(new Uri[uriList.size()]);
    }

    private void chooseFile(String title, String subject, String type) {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.setDataAndType(currentUri, type);

        Intent chooserIntent = getFsIntent(intent, FsActivity.CHOOSER);
        if(chooserIntent == null) {
            return;
        }

        context.startActivityForResult(chooserIntent, requestCode);
    }

    public Intent getFsIntent(Intent intent, FsActivity fsActivity) {
        Intent fsIntent = FsUtils.getFsIntent(context.getPackageManager(), intent, fsActivity);
        if(fsIntent == null && FsUtils.askUserInstall(context, mandatoryFs)) {
            return null;
        }

        if(!showOnlyFs) {
            fsIntent = FsUtils.createAppChooser(context.getPackageManager(), intent, chooserTitle);
        }

        return fsIntent;
    }
}

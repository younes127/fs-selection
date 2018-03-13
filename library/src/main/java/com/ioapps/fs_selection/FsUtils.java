package com.ioapps.fs_selection;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FsUtils {

    private static final String FS_PACKAGE = "com.ioapps.fileselector";
    private static final String FS_PREFS = "fs-prefs";
    private static final String PREF_DONT_SHOW_INSTALL = "dont-show-install";

    /*package*/ static Intent getFsChooserIntent(PackageManager pm, Intent intent) {
        ApplicationInfo appInfo = getAppInfo(pm, FS_PACKAGE);
        if(appInfo == null) {
            return null;
        }


        Intent fsIntent = new Intent(intent);
        fsIntent.setPackage(FS_PACKAGE);
        fsIntent.setComponent(new ComponentName(FS_PACKAGE, FS_PACKAGE + ".ChooserActivity"));
        return fsIntent;
    }

    /*package*/ static Intent getFsMainIntent(PackageManager pm, Intent intent) {
        ApplicationInfo appInfo = getAppInfo(pm, FS_PACKAGE);
        if(appInfo == null) {
            return null;
        }


        Intent fsIntent = new Intent(intent);
        fsIntent.setPackage(FS_PACKAGE);
        fsIntent.setComponent(new ComponentName(FS_PACKAGE, FS_PACKAGE + ".MainActivity"));
        return fsIntent;
    }

    /*package*/ static Intent createAppChooser(PackageManager pm, Intent intent, String title, String... patterns) {
        List<LabeledIntent> intentList = getLabeledIntents(pm, intent, patterns);
        Intent targetIntent = !intentList.isEmpty() ? intentList.remove(0) : intent;
        Intent chooserIntent = Intent.createChooser(targetIntent, title);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
        return chooserIntent;
    }

    /*package*/ static boolean askUserInstall(final Context context, final boolean mandatory) {
        final SharedPreferences prefs = FsUtils.getSharedPrefs(context);
        boolean dontShowInstall = prefs.getBoolean(FsUtils.PREF_DONT_SHOW_INSTALL, false);

        if(dontShowInstall && !mandatory) {
            return false;
        }

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.fs_install_dialog, null);
        final CheckBox checkBoxNoShowMore = view.findViewById(R.id.checkBoxNoShowMore);

        if(mandatory) {
            checkBoxNoShowMore.setChecked(false);
            checkBoxNoShowMore.setVisibility(View.GONE);
        }

        dialogBuilder.setTitle(R.string.required_action);
        dialogBuilder.setView(view);

        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO ..
            }
        });

        dialogBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(!mandatory) {
                    boolean dontShowMore = checkBoxNoShowMore.isChecked();
                    prefs.edit().putBoolean(FsUtils.PREF_DONT_SHOW_INSTALL, dontShowMore).commit();
                }
                goToMarket(context, FS_PACKAGE, 0);
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if(!mandatory) {
                    boolean dontShowMore = checkBoxNoShowMore.isChecked();
                    prefs.edit().putBoolean(FsUtils.PREF_DONT_SHOW_INSTALL, dontShowMore).commit();
                }
            }
        });

        dialogBuilder.show();
        return true;
    }

    private static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(FS_PREFS, Context.MODE_PRIVATE);
    }

    private static ApplicationInfo getAppInfo(PackageManager pm, String packageName) {
        try {
            return pm.getApplicationInfo(packageName, 0);
        } catch (Exception ex) {// PackageManager.NameNotFoundException
        }

        return null;
    }

    private static List<LabeledIntent> getLabeledIntents(PackageManager pm, Intent intent, String... patterns) {
        List<ResolveInfo> riList = pm.queryIntentActivities(intent, 0);
        Collections.sort(riList, new ResolveInfo.DisplayNameComparator(pm));
        List<LabeledIntent> resultList = new ArrayList<>();

        for (ResolveInfo ri : riList) {
            String packageName = ri.activityInfo.packageName;

            if (patterns != null && patterns.length > 0) {
                boolean hasPattern = false;

                for (String pattern : patterns) {
                    if (packageName.contains(pattern)) {
                        hasPattern = true;
                        break;
                    }
                }

                if (!hasPattern) {
                    continue;
                }
            }

            int labelId = ri.labelRes, iconId = ri.icon;

            if (labelId == 0) {
                labelId = ri.activityInfo.labelRes;
            }

            if (iconId == 0) {
                iconId = ri.activityInfo.icon;
            }

            Intent newIntent = new Intent(intent);
            newIntent.setPackage(packageName);
            newIntent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
            resultList.add(new LabeledIntent(newIntent, packageName, labelId, iconId));
        }

        return resultList;
    }

    private static String getMarketUri(String packageName) {
        return "market://details?id=" + packageName;
    }

    private static String getMarketUrl(String packageName) {
        return "https://play.google.com/store/apps/details?id=" + packageName;
    }

    private static boolean goToMarket(Context context, String packageName, int flags) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getMarketUri(packageName)));
        intent.setPackage("com.android.vending");

        if (flags != -1) {
            intent.addFlags(flags);
        }

        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getMarketUrl(packageName)));

            if (flags != -1) {
                intent.addFlags(flags);
            }

            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e1) {
            }
        }

        return false;
    }
}

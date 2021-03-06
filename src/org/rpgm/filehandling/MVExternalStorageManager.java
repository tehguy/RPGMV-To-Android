package org.rpgm.filehandling;

import android.content.Context;
import android.os.Environment;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.util.Log;

import org.rpgm.main.BuildConfig;

import java.io.File;

public class MVExternalStorageManager {
    private StorageManager storageManager;

    private File obbFile;

    private Context context;

    public MVExternalStorageManager(Context c){
        context = c;

        final String obb = "main." + BuildConfig.VERSION_CODE + "." + BuildConfig.APPLICATION_ID
                + ".obb";

        final String fullOBBPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "Android/obb/" + BuildConfig.APPLICATION_ID + File.separator + obb;

        obbFile = new File(fullOBBPath);
    }

    private OnObbStateChangeListener mListener = new OnObbStateChangeListener() {
        @Override
        public void onObbStateChange(String path, int state) {
            super.onObbStateChange(path, state);
            Log.d("PATH= ", path);
            Log.d("STATE= ", Integer.toString(state));

            if(state == OnObbStateChangeListener.MOUNTED){
                String MOUNT_PATH = storageManager.getMountedObbPath(path);
                Log.d("MOUNT= ", MOUNT_PATH + " ---->MOUNTED");
            }
            else{
                Log.e("MOUNT= ", "----->MOUNT FAILED");
            }
        }
    };

    public void setStorageManager(Object sm){
        storageManager = (StorageManager)sm;
        mountExpansion();
    }

    private void mountExpansion(){
        if(!storageManager.isObbMounted(obbFile.getAbsolutePath())){
            if(obbFile.exists()){
                if(storageManager.mountObb(obbFile.getAbsolutePath(), null, mListener)){
                    Log.d("QUEUE= ", "SUCCESSFUL");
                }
                else{
                    Log.e("QUEUE= ", "FAILED");
                }
            }
            else{
                Log.e("MAIN= ", "NOT FOUND");
            }
        }
    }

    @android.webkit.JavascriptInterface
    public String getFilesDir(){
        return context.getExternalFilesDir(null).getAbsolutePath();
    }

    @android.webkit.JavascriptInterface
    public String getMountedOBBPath(String fileName){
        if(obbFile.exists()){
            return storageManager.getMountedObbPath(obbFile.getAbsolutePath()) + File.separator + fileName;
        }

        return "";
    }
}

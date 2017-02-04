package com.baculsoft.sample.realmexporter.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baculsoft.sample.realmexporter.R;
import com.baculsoft.sample.realmexporter.utils.Permissions;

import java.io.File;

import io.realm.Realm;
import io.realm.internal.IOException;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        final Button button = (Button) findViewById(R.id.btn_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (Permissions.get().isMarshmallow()) {
                    checkPermission();
                } else {
                    exportRealmFile();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportRealmFile();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission() {
        final String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (!Permissions.get().hasSelfPermissions(this, permissions)) {
            requestPermissions(permissions, 100);
        } else {
            exportRealmFile();
        }
    }

    public void exportRealmFile() {
        final Realm realm = Realm.getDefaultInstance();

        try {
            final File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/sample.realm"));
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            realm.writeCopyTo(file);
            Toast.makeText(MainActivity.this, "Success export realm file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            realm.close();
            e.printStackTrace();
        }
    }
}
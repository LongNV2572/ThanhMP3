package com.example.baseproduct.ui.user.home;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityHomeBinding;
import com.example.baseproduct.dialog.exit.ExitAppDialog;
import com.example.baseproduct.model.MusicModel;
import com.example.baseproduct.ui.both.login.LoginActivity;
import com.example.baseproduct.ui.both.play.PlayActivity;
import com.example.baseproduct.ui.user.home.adapter.MusicAdapter;
import com.example.baseproduct.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    private MusicAdapter musicAdapter;
    private final List<MusicModel> listMusic = new ArrayList<>();
    private long downloadId;
    private BroadcastReceiver downloadCompleteReceiver;


    @Override
    public void initView() {
        musicAdapter = new MusicAdapter(new MusicAdapter.OnMusicClickListener() {
            @Override
            public void onClick(MusicModel music) {
                Utils.hideKeyboard(HomeActivity.this, binding.edtSearch);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", music);
                startNextActivity(PlayActivity.class, bundle);
            }

            @Override
            public void onAddToAlbum(MusicModel music) {

            }

            @Override
            public void onDownload(MusicModel music) {
                downloadMp3(music);
            }
        });
        binding.rcvData.setAdapter(musicAdapter);

        DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference("list_music");

        musicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listMusic.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    MusicModel music = child.getValue(MusicModel.class);
                    if (music != null) {
                        listMusic.add(music);
                    }
                }
                musicAdapter.addListData(listMusic);
                Log.d("Firebase", "Size = " + listMusic.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        });

    }

    @Override
    public void bindView() {
        binding.ivNavigate.setOnClickListener(v -> {
            Utils.hideKeyboard(this, binding.edtSearch);
            if (binding.getRoot().isDrawerOpen(GravityCompat.START)) {
                binding.getRoot().closeDrawer(GravityCompat.START);
            } else {
                binding.getRoot().openDrawer(GravityCompat.START);
            }
        });

        binding.tvAlbum.setOnClickListener(v -> {
            Utils.hideKeyboard(this, binding.edtSearch);
        });

        binding.tvDownload.setOnClickListener(v -> {
            Utils.hideKeyboard(this, binding.edtSearch);
            Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            startActivity(intent);
        });

        binding.tvLogout.setOnClickListener(v -> {
            startNextActivity(LoginActivity.class, null);
            finishAffinity();
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                List<MusicModel> listSearch = new ArrayList<>();
                String edtSearch = binding.edtSearch.getText().toString().trim();
                if (edtSearch.isEmpty()) {
                    listSearch.addAll(listMusic);
                } else {
                    for (int i = 0; i < listMusic.size(); i++) {
                        MusicModel item = listMusic.get(i);
                        if (item.getName().contains(edtSearch) || item.getSinger().contains(edtSearch)) {
                            listSearch.add(listMusic.get(i));
                        }
                    }
                }
                musicAdapter.addListData(listSearch);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    public void downloadMp3(MusicModel item) {
        Log.d("Download", "Starting download for: " + item.getName());

        // Hủy receiver cũ nếu có
        if (downloadCompleteReceiver != null) {
            try {
                unregisterReceiver(downloadCompleteReceiver);
            } catch (Exception e) {
                // Ignore
            }
        }

        // Đăng ký receiver MỚI ngay trước khi download
        downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("DownloadReceiver", "onReceive called!");

                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d("DownloadReceiver", "Received ID: " + id + ", Expected ID: " + downloadId);

                if (id == downloadId) {
                    Toast.makeText(context, "Download hoàn tất 🎵", Toast.LENGTH_LONG).show();
                    Log.d("DownloadReceiver", "Toast shown!");
                }
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(downloadCompleteReceiver, filter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(downloadCompleteReceiver, filter);
        }

        Log.d("DownloadReceiver", "Receiver registered successfully");

        // Bắt đầu download
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(item.getLink());
        Log.d("Download", "URI: " + uri.toString());

        DownloadManager.Request request = getRequest(item, uri);

        downloadId = downloadManager.enqueue(request);
        Log.d("Download", "Download ID: " + downloadId);

        Toast.makeText(this, "Đang tải xuống...", Toast.LENGTH_SHORT).show();
    }

    private static DownloadManager.Request getRequest(MusicModel item, Uri uri) {
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(item.getSinger() + " - " + item.getName());
        request.setDescription("Downloading mp3...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, item.getName() + ".mp3");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        return request;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadCompleteReceiver != null) {
            try {
                unregisterReceiver(downloadCompleteReceiver);
                Log.d("DownloadReceiver", "Receiver unregistered in onDestroy");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBack() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this);
        exitAppDialog.init(this::finishAffinity);
        exitAppDialog.show();
    }
}

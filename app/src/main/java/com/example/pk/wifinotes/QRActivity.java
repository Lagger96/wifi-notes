package com.example.pk.wifinotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;
import com.example.pk.wifinotes.models.NetworkCategory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends AppCompatActivity {

    private static final String NETWORK_ID_PARAM = "networkId";
    private static final String CATEGORY_NAME_PARAM = "categoryName";
    private static final int DEFAULT_VALUE = -1;
    private ImageView QRCodeView;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        QRCodeView = findViewById(R.id.image_view);
        dataManager = new DataManager(DbHelper.getInstance(this).getWritableDatabase());

        initView();
    }

    private void initView() {
        String content = "";
        Intent intent = getIntent();

        int networkId = intent.getIntExtra(NETWORK_ID_PARAM, DEFAULT_VALUE);
        String categoryName = intent.getStringExtra(CATEGORY_NAME_PARAM);

        if (networkId != DEFAULT_VALUE) {
            Network network = dataManager.getNetwork(networkId);
            content = network.toJSONString();
        } else if (categoryName != null) {
            NetworkCategory networkCategory = dataManager.getNetworkCategory(categoryName);
            content = networkCategory.toJSONString();
        }

        createQRCode(content);
    }

    private void createQRCode(String content) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRCodeView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}

package com.code.qr.generator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.qr.qrcode.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GenerateQR extends ActionBarActivity {
    ImageLoader imgLoader;
    ImageView qrImg;
    protected String copiedStr;
    TextView qrTxt;
    ClipboardManager clipboard;

    String BASE_QR_URL = "http://chart.apis.google.com/chart?cht=qr&chs=400x400&chld=M&choe=UTF-8&chl=";
    String fullUrl = BASE_QR_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config);

        qrImg = (ImageView)findViewById(R.id.qrImg);
        qrTxt = (TextView)findViewById(R.id.qrText);



        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);



        /*
         * clipboard.getText() is now deprecated. But I am going to use it here
         * because the new way of doing the same thing only works on API lvl 11+
         * Since I want this application to support API lvl 4+ we have to use
         * the old method.
         */
        CharSequence clipTxt = clipboard.getText();

        //This is the new, non-deprecated way of getting text from the Clipboard.
        //CharSequence clipTxt = clipboard.getPrimaryClip().getItemAt(0).getText();


        //If the clipboard has text, and it is more than 0 characters.
        if((null != clipTxt) && (clipTxt.length() > 0)){
            try {
                qrTxt.setText(clipTxt);
                copiedStr = clipTxt.toString();
                fullUrl += URLEncoder.encode(copiedStr, "UTF-8");
                imgLoader.displayImage(fullUrl, qrImg);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else{ //If no text display a dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("QRMaker")
                    .setCancelable(true)
                    .setMessage("There was no data in the clipboard! Go copy something and come back")

                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();

                        }

                    });

            AlertDialog diag = builder.create();
            diag.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generate_qr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

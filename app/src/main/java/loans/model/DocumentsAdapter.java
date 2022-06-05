package loans.model;

import android.annotation.SuppressLint;
import android.content.Context;


import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.github.barteksc.pdfviewer.PDFView;
import com.odedtech.mff.mffapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import Utilities.PreferenceConnector;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {
    private Context context;
    private List<DocumentData> documentsList;
    public PDFView pdfView;

    public DocumentsAdapter(Context context, List<DocumentData> documentsList) {
        this.context = context;
        this.documentsList = documentsList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_document, viewGroup, false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        String imageUrl = documentsList.get(position).getDownloadUrl() + "?access_token=" + PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        Glide.with(context).load(imageUrl).apply(options).into(viewHolder.iv_img);
        /*viewHolder.pdfView.setWebViewClient(new WebViewClient());


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
        context.startActivity(browserIntent);

        viewHolder.pdfView.getSettings().setJavaScriptEnabled(true);
        viewHolder.pdfView.loadUrl(imageUrl);*/

        // if (imageUrl.contains(".docx")) {
      //  new RetrivePDFfromUrl().execute(imageUrl);
        // }
    }

    //create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            //we are using inputstream for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    //response is success.
                    //we are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }

            } catch (IOException e) {
                //this is the method to handle errors.
                e.printStackTrace();
                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();

        }
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_img;
        // public WebView pdfView;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            pdfView = itemView.findViewById(R.id.pdf_view);
        }
    }
}

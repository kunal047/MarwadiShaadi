package com.sid.marwadishaadi.Blocked_Members;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;

import java.util.List;

/**
 * Created by Sid on 31-May-17.
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.MyViewHolder> {

    private List<BlockModel> blockModelList;
    private Context context;

    public BlockAdapter(Context context, List<BlockModel> blockModelList) {
        this.blockModelList = blockModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blocked_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BlockModel blocked = blockModelList.get(position);

        holder.name.setText(blocked.getName());
        holder.cusId.setText(blocked.getId());

        holder.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder unblock_user = new AlertDialog.Builder(view.getRootView().getContext());
                unblock_user.setMessage("Do you want to unblock the user ? ")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new bckend().execute("customer_id", holder.cusId.getText().toString(), Integer.toString(position));
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, blockModelList.size());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog unblock = unblock_user.create();
                unblock.setTitle("Unblock User");
                unblock.show();
            }
        });

        holder.vview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getRootView().getContext(), UserProfileActivity.class);
                v.getRootView().getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return blockModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, cusId, vview, unblock;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            cusId = view.findViewById(R.id.cusId);
            vview = view.findViewById(R.id.vview);
            unblock = view.findViewById(R.id.unblock);

        }


    }

    class bckend extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(final String... strings) {
            String query = "";
            query += "update tbl_block set blocked_id=replace(blocked_id ,\"" + strings[1] + "\" , '' ) where customer_no = \"" + strings[0] + "\" ; ";

            AndroidNetworking.post(Constants.AWS_SERVER + "/unblock")
                    .addBodyParameter("query", query)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {

                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Toast.makeText(context, "Unblocked", Toast.LENGTH_SHORT).show();
                                BlockModel b = blockModelList.get(Integer.parseInt(strings[2]));
                                blockModelList.remove(Integer.parseInt(strings[2]));

                            } else {
                                Toast.makeText(context, "Try Again, Unable to remove", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
            return null;
        }
    }

}

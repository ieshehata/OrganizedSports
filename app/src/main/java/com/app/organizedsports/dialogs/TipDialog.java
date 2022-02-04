package com.app.organizedsports.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.app.organizedsports.R;
import com.app.organizedsports.utils.SharedData;

public class TipDialog extends AppCompatDialogFragment {
    private EditText TipText;
    private TextView data;
    private TipDialog.TipDialogListener listener;
    boolean fromFragment;

    public TipDialog(boolean fromFragment) {
        this.fromFragment = fromFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_tips, null);

        builder.setView(view)
                .setNegativeButton("Cancel", (dialogInterface, i) -> listener.onError("Canceled!"))
                .setPositiveButton("Send", (dialogInterface, i) -> {
                    String tip = "";
                    if(!TextUtils.isEmpty(TipText.getText().toString())) {
                        tip = TipText.getText().toString();
                    }

                });
        TipText = view.findViewById(R.id.tips);
        data = view.findViewById(R.id.data);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if(!fromFragment) {
                listener = (TipDialogListener) context ;
            }else {
                listener = (TipDialogListener) getParentFragment();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement RequestProductDialogListener");
        }
    }

    public interface TipDialogListener {
        void getData( String TipText);
        void onError(String error);
    }


}

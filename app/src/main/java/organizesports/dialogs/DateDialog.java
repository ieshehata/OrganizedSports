package organizesports.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.app.organizesports.R;
import com.app.organizesports.utils.SharedData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateDialog extends AppCompatDialogFragment {
    public interface DateDialogListener {
        void getDate(Date date);
    }

    private DateDialogListener listener;
    private EditText dateET;
    private String title;
    private Date oldValue;
    boolean fromFragment;
    SimpleDateFormat dateFormat = new SimpleDateFormat(SharedData.formatDate, Locale.getDefault());

    Date chosenDate = Calendar.getInstance().getTime();

    public DateDialog(String title, Date oldValue, boolean fromFragment) {
        this.title = title;
        this.oldValue = oldValue;
        this.fromFragment = fromFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_date_picker, null);

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    if(!dateET.getText().toString().trim().isEmpty()) {
                        listener.getDate(chosenDate);
                    }else {
                        listener.getDate(null);
                    }
                });
        dateET = view.findViewById(R.id.date);

        dateET.setOnClickListener(this::openDatePickerDialog);

        if(oldValue != null) {
            dateET.setText(dateFormat.format(oldValue));
        }
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if(!fromFragment) {
                listener = (DateDialogListener) context ;
            }else {
                listener = (DateDialogListener) getParentFragment();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement RequestProductDialogListener");
        }
    }

    public void openDatePickerDialog(final View v) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(chosenDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    chosenDate = cal.getTime();
                    dateET.setText(dateFormat.format(chosenDate));
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }
}

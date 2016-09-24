package com.techpalle.karan.personaldictionary.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.techpalle.karan.personaldictionary.R;

public class AddSourceFragment extends AppCompatDialogFragment {

    LayoutInflater inflater;
    View view;
    NewSourceListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (NewSourceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NewSourceListener");
        }
    }

    public interface NewSourceListener {
        public boolean sourceAddedToDatabase(String sourceName);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.fragment_add_source, null);

        // Extract
        final EditText editTextSource = (EditText) view.findViewById(R.id.edit_text_add_source);
        Button buttonAddSource = (Button) view.findViewById(R.id.button_add);
        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        final TextInputLayout textInputLayoutAddSource = (TextInputLayout) view.findViewById(R.id.input_layout_add_source);

        buttonAddSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceName = editTextSource.getText().toString().trim();
                if (sourceName.length() < 1) {
                    textInputLayoutAddSource.setError(getString(R.string.error_message_empty_source));
                } else if (!listener.sourceAddedToDatabase(sourceName)) {
                    textInputLayoutAddSource.setError(getString(R.string.error_message_source_exists));
                } else {
                    textInputLayoutAddSource.setErrorEnabled(false);
                    dismiss();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);

        return builder.create();
    }
}

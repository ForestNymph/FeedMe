package pl.grudowska.feedme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import pl.grudowska.feedme.databases.ArchivedProductDataSource;

public class DeleteDialogFragment extends DialogFragment {

    private OnClearItemsCommandListener mListener;
    private AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View deleteDialogView = inflater.inflate(R.layout.delete_dialog, null);
        builder.setView(deleteDialogView).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArchivedProductDataSource dataSource = new ArchivedProductDataSource(getActivity());
                        dataSource.open();
                        if (dataSource.getAllArchivedLists().size() == 0) {
                            // do nothing
                        } else {
                            dataSource.deleteAllArchivedItems();
                            mListener.onClearItemsCommand();
                        }
                        dataSource.close();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteDialogFragment.this.getDialog().cancel();
            }
        });
        mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextGray));
            }
        });
        return mDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnClearItemsCommandListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnClearItemsCommandListener");
        }
    }

    // Callback to communicate with activity
    public interface OnClearItemsCommandListener {
        void onClearItemsCommand();
    }
}

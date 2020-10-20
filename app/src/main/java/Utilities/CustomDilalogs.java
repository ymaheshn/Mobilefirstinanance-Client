package Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.multilevelview.models.RecyclerViewItem;
import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.List;

import kyc.dto.BranchTree;
import loans.ExpandableListDialog;

/**
 * Created by gufran khan on 24-06-2018.
 */

public class CustomDilalogs {

    private static ArrayList<RecyclerViewItem> viewItems;

    public static void showListPop(Activity activity, String title, EditText editText, ArrayList<String> options) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        String[] optionsArray = options.toArray(new String[options.size()]);
        builder.setItems(optionsArray, (dialog, which) -> editText.setText(optionsArray[which]));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showBranch(Activity activity, String title, EditText editText, List<BranchTree> options) {

//        String[] optionsArray = options.toArray(new String[options.size()]);
        ExpandableListDialog listDialog = new ExpandableListDialog(activity, title, editText, options, viewItems);
        viewItems = listDialog.viewItems;
        listDialog.show();
    }

    public static void showSingleChoicePopUp(Activity activity, View view, ArrayList<String> options) {
        final EditText editText = (EditText) view;
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_gendar);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = new RadioButton(activity);
            radioButton.setText(options.get(i));
            radioButton.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    RadioButton radioButton1 = (RadioButton) view;
                    editText.setText(radioButton1.getText());
                }
            });
            radioGroup.addView(radioButton);
        }

        dialog.show();

    }
}

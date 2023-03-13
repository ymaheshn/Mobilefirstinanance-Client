package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.odedtech.mff.client.R;

import java.util.ArrayList;
import java.util.Objects;

import Utilities.PreferenceConnector;
import shufpti.ShufptiVerificationServicesActivity;
import shufpti.ShuftiModel;
import signup.listener.OnContinueButtonClickListener;
import signup.listener.SignUpDetailsInterface;

public class SignUpViewPagerAdapter extends RecyclerView.Adapter<SignUpViewPagerAdapter.ViewPagerHolder> implements View.OnClickListener {

    private final Context context;
    private final ArrayList<String> bodyLabelList;
    private final ArrayList<String> bodyTypeList;
    private final ArrayList<String> bodyValueList;
    private final OnContinueButtonClickListener continueButtonClickListener;
    private String enteredText;
    private String enteredBranchText;
    private boolean isFromBranch = false;
    private boolean isFromDateOFBirth = false;
    private boolean isFromIdVerification = false;
    private final String emailSignUp;
    private final String mobileSignUp;
    private final ArrayList<String> bodyDescriptionList;
    public String autoCompleteText;
    int clickCount = 0;
    private ShuftiModel shuftiModel;

    private String shuftiResponse;

    public SignUpViewPagerAdapter(Context context, ArrayList<String> bodyLabelList, ArrayList<String> bodyValueList, ArrayList<String> bodyTypeList, ArrayList<String> bodyDescriptionList, String emailSignUp, String mobileSignUp,
                                  OnContinueButtonClickListener continueButtonClickListener, SignUpDetailsInterface signUpDetailsInterface) {

        this.bodyLabelList = bodyLabelList;
        this.bodyValueList = bodyValueList;
        this.bodyTypeList = bodyTypeList;
        this.context = context;
        this.continueButtonClickListener = continueButtonClickListener;
        this.emailSignUp = emailSignUp;
        this.mobileSignUp = mobileSignUp;
        this.bodyDescriptionList = bodyDescriptionList;
    }

    @NonNull
    @Override
    public ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sign_up_form_view_item_list, parent, false);
        shuftiResponse = PreferenceConnector.getShuftiResponse(context);
        shuftiModel = new Gson().fromJson(shuftiResponse, ShuftiModel.class);
        return new ViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerHolder holder, @SuppressLint("RecyclerView") int position) {
        String branch = "Branch";
        String email = "Email";
        String mobile = "Phone";
        String dateOfBirth = "Date";
        String type = "List";
        String labelAge = "Is your age above 18";
        String labelFATCA = "FATCA Declaration";
        String labelDomicile = "Domicile";
        String labelIDVerify = "ID Verification";
        String firstName = "First Name";
        String lastName = "Last Name";
        String nationalId = "National ID";
        String choosePlan = "Choose your plan";

        Objects.requireNonNull(holder.textInputEditText.getText()).clear();
        if (bodyTypeList.size() > 0) {
            if (email.equals(bodyTypeList.get(position))) {
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.textInputEditText.setVisibility(View.VISIBLE);
                holder.textInputLayout.setHint(bodyTypeList.get(position));
                holder.skipText.setVisibility(View.GONE);
                holder.textInputEditText.setFocusable(true);
                if (!emailSignUp.isEmpty()) {
                    holder.textView.setText(bodyLabelList.get(position));
                    holder.textInputEditText.setText(emailSignUp);
                    holder.description.setText(bodyDescriptionList.get(position));
                    holder.textInputEditText.setFocusable(false);
                } else {
                    holder.textInputEditText.setFocusable(true);
                    holder.idVerification_name_til.setVisibility(View.GONE);
                    holder.textView.setText(bodyLabelList.get(position));
                    holder.description.setText(bodyDescriptionList.get(position));
                }
                isFromIdVerification = false;

            } else if (mobile.equals(bodyTypeList.get(position))) {
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.textInputLayout.setVisibility(View.VISIBLE);
                holder.textInputLayout.setHint(bodyTypeList.get(position));
                holder.skipText.setVisibility(View.GONE);
                holder.textInputEditText.setFocusable(true);
                if (!mobileSignUp.isEmpty()) {
                    holder.textView.setText(bodyLabelList.get(position));
                    holder.description.setText(bodyDescriptionList.get(position));
                    holder.textInputEditText.setText(mobileSignUp);
                    holder.textInputEditText.setFocusable(false);
                } else {
                    holder.textInputEditText.setFocusable(true);
                    holder.idVerification_name_til.setVisibility(View.GONE);
                    holder.textInputEditText.setVisibility(View.VISIBLE);
                    holder.textView.setText(bodyLabelList.get(position));
                    holder.description.setText(bodyDescriptionList.get(position));
                }
                isFromIdVerification = false;
            } else if (dateOfBirth.equals(bodyTypeList.get(position))) {
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.textInputLayout.setVisibility(View.GONE);
                holder.dateOfBirthTil.setVisibility(View.VISIBLE);
                holder.textView.setText(bodyLabelList.get(position));
                holder.dateOfBirthTil.setHint(bodyTypeList.get(position));
                holder.description.setText(bodyDescriptionList.get(position));
                holder.dateOfBirthEditText.setFocusable(true);
                if (shuftiModel != null) {
                    holder.dateOfBirthEditText.setText(shuftiModel.getVerificationData().getDocument().getDob());
                    holder.dateOfBirthEditText.setFocusable(false);
                }
                isFromDateOFBirth = true;
                isFromBranch = false;
                isFromIdVerification = false;
            } else if (branch.equals(bodyTypeList.get(position))) {
                holder.branchInputLayout.setVisibility(View.VISIBLE);
                holder.skipText.setVisibility(View.GONE);
                holder.textInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textView.setText(bodyLabelList.get(position));
                holder.branchInputLayout.setHint(bodyTypeList.get(position));
                holder.branchTextEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_item_next, 0);
                holder.branchTextEditText.setFocusable(false);
                isFromBranch = true;
                isFromDateOFBirth = false;
                isFromIdVerification = false;

            } else if (type.equals(bodyTypeList.get(position)) && labelAge.equals(bodyLabelList.get(position))) {
                holder.skipText.setVisibility(View.GONE);
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setHint(bodyLabelList.get(position));
                holder.textView.setText(bodyLabelList.get(position));
                isFromIdVerification = false;
                String dataValues = bodyValueList.get(position);

                setDataValuesToDropDown(dataValues, holder);

            } else if (type.equals(bodyTypeList.get(position)) && labelFATCA.equals(bodyLabelList.get(position))) {
                holder.skipText.setVisibility(View.GONE);
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setHint(bodyLabelList.get(position));
                holder.textView.setText(bodyLabelList.get(position));
                isFromIdVerification = false;
                String dataValues = bodyValueList.get(position);
                setDataValuesToDropDown(dataValues, holder);

            } else if (type.equals(bodyTypeList.get(position)) && labelDomicile.equals(bodyLabelList.get(position))) {
                holder.skipText.setVisibility(View.GONE);
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setHint(bodyLabelList.get(position));
                holder.dateOfBirthTil.setVisibility(View.GONE);
                holder.textView.setText(bodyLabelList.get(position));
                isFromIdVerification = false;
                String dataValues = bodyValueList.get(position);

                setDataValuesToDropDown(dataValues, holder);

            } else if (type.equals(bodyTypeList.get(position)) && labelIDVerify.equals(bodyLabelList.get(position))) {
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.skipText.setVisibility(View.VISIBLE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.VISIBLE);
                holder.completeTextView.getText().clear();
                holder.idVerification_name_til.setHint(bodyLabelList.get(position));
                holder.textView.setText(bodyLabelList.get(position));
                isFromIdVerification = true;
                String dataValues = bodyValueList.get(position);

                setDataValuesToDropDown(dataValues, holder);
            }
            else if (type.equals(bodyTypeList.get(position)) && choosePlan.equals(bodyLabelList.get(position))) {
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.skipText.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.GONE);
                holder.idVerification_name_til.setVisibility(View.VISIBLE);
                holder.completeTextView.getText().clear();
                holder.idVerification_name_til.setHint(bodyLabelList.get(position));
                holder.textView.setText(bodyLabelList.get(position));
                isFromIdVerification = true;
                String dataValues = bodyValueList.get(position);
                holder.dateOfBirthEditText.setFocusable(true);

                setDataValuesToDropDown(dataValues, holder);
            }

            else if (firstName.equals(bodyLabelList.get(position))) {
                holder.completeTextView.getText().clear();
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.dateOfBirthTil.setVisibility(View.GONE);
                holder.textView.setText(bodyLabelList.get(position));
                holder.textInputLayout.setHint(bodyLabelList.get(position));
                holder.dateOfBirthEditText.setFocusable(true);
                if (shuftiModel != null) {
                    holder.textInputEditText.setText(shuftiModel.getVerificationData().getDocument().getName().getFirstName());
                }
                isFromBranch = false;
                isFromDateOFBirth = false;
                isFromIdVerification = false;
            } else if (lastName.equals(bodyLabelList.get(position))) {
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.dateOfBirthTil.setVisibility(View.GONE);
                holder.textView.setText(bodyLabelList.get(position));
                holder.textInputLayout.setHint(bodyLabelList.get(position));
                holder.dateOfBirthEditText.setFocusable(true);
                if (shuftiModel != null) {
                    holder.textInputEditText.setText(shuftiModel.getVerificationData().getDocument().getName().getLastName());
                }
                isFromBranch = false;
                isFromDateOFBirth = false;
                isFromIdVerification = false;
            } else if (nationalId.equals(bodyLabelList.get(position))) {
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.dateOfBirthTil.setVisibility(View.GONE);
                holder.textInputLayout.setHint(bodyLabelList.get(position));
                holder.textView.setText(bodyLabelList.get(position));
                if (shuftiModel != null) {
                    holder.textInputEditText.setText(shuftiModel.getVerificationData().getDocument().getDocumentNumber());
                }
                isFromBranch = false;
                isFromDateOFBirth = false;
                isFromIdVerification = false;
            } else {
                holder.textInputEditText.setFocusable(true);
                holder.branchInputLayout.setVisibility(View.GONE);
                holder.skipText.setVisibility(View.GONE);
                holder.description.setText(bodyDescriptionList.get(position));
                holder.textInputLayout.setVisibility(View.VISIBLE);
                holder.idVerification_name_til.setVisibility(View.GONE);
                holder.dateOfBirthTil.setVisibility(View.GONE);
                if (!bodyTypeList.get(position).equals(type)) {
                    holder.textView.setText(bodyLabelList.get(position));
                    holder.textInputLayout.setHint(bodyLabelList.get(position));
                }
                isFromBranch = false;
                isFromDateOFBirth = false;
                isFromIdVerification = false;
            }
        }

        holder.materialButton.setOnClickListener(v -> {
            enteredText = holder.textInputEditText.getText().toString();
            autoCompleteText = holder.completeTextView.getText().toString().trim();
            String dateOfBirthResult = String.valueOf(holder.dateOfBirthEditText.getText());

            if (!dateOfBirthResult.isEmpty()) {
                continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), dateOfBirthResult, holder, isFromBranch, isFromDateOFBirth, false, false);

            } else {
                //   continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), autoCompleteText, holder, isFromBranch, isFromDateOFBirth, false, false);
                if (autoCompleteText.equals("National ID") || autoCompleteText.equals("Driving license") || autoCompleteText.equals("Passport")) {
                    Intent intent = new Intent(this.context, ShufptiVerificationServicesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), autoCompleteText, holder, isFromBranch, isFromDateOFBirth, false, false);
                   /* clickCount = clickCount + 1;
                    if (clickCount == 1) {
                        Intent intent = new Intent(this.context, ShufptiVerificationServicesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), autoCompleteText, holder, isFromBranch, isFromDateOFBirth, false, false);
                    }*/
                } else if (autoCompleteText.equals("Yes") || autoCompleteText.equals("No")) {
                    continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), autoCompleteText, holder, isFromBranch, isFromDateOFBirth, false, false);

                } else if (!enteredText.isEmpty()) {
                    continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), enteredText, holder, isFromBranch, isFromDateOFBirth, false, false);
                } else {
                    continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), autoCompleteText, holder, isFromBranch, isFromDateOFBirth, false, false);

                }
            }
        });

        holder.skipText.setOnClickListener(v -> continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), enteredText, holder, isFromBranch, isFromDateOFBirth, false, true));

       /* holder.idVerification_name_til.setOnClickListener(v -> {
            enteredText = holder.completeTextView.getText().toString();
            continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), enteredText, holder, isFromBranch, isFromDateOFBirth, isFromIdVerification, false);

        });*/

        holder.dateOfBirthEditText.setOnClickListener(v -> {
            enteredText = Objects.requireNonNull(holder.dateOfBirthEditText.getText()).toString();
            continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), enteredText, holder, false, isFromDateOFBirth, isFromIdVerification, false);
        });

        holder.branchTextEditText.setOnClickListener(v -> continueButtonClickListener.onClick(v, position, enteredBranchText, bodyLabelList.get(position), enteredText, holder, true, isFromDateOFBirth, isFromIdVerification, false));
    }


    @Override
    public int getItemCount() {
        return bodyTypeList.size();
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewPagerHolder extends RecyclerView.ViewHolder {
        public TextInputEditText textInputEditText;
        public TextInputEditText dateOfBirthEditText;
        public TextInputEditText branchTextEditText;
        public TextInputLayout textInputLayout;
        public TextInputLayout branchInputLayout;
        public TextInputLayout idVerification_name_til;
        public TextInputLayout dateOfBirthTil;
        public AppCompatTextView textView, skipText;
        public AppCompatTextView description;
        public AppCompatAutoCompleteTextView completeTextView;
        public MaterialButton materialButton;

        public ViewPagerHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.sign_up_name_text);
            skipText = itemView.findViewById(R.id.skip_text);

            textInputLayout = itemView.findViewById(R.id.name_text_input_layout);
            dateOfBirthTil = itemView.findViewById(R.id.date_of_birth_til);
            idVerification_name_til = itemView.findViewById(R.id.id_verification_name_til);
            branchInputLayout = itemView.findViewById(R.id.branch_text_input_layout);

            description = itemView.findViewById(R.id.description_sign_up);

            textInputEditText = itemView.findViewById(R.id.name_text_input_edit_text);
            dateOfBirthEditText = itemView.findViewById(R.id.date_of_birth_edit_text);
            branchTextEditText = itemView.findViewById(R.id.branch_text_input_edit_text);
            completeTextView = itemView.findViewById(R.id.id_verification_auto_text);
            materialButton = itemView.findViewById(R.id.continue_button);

        }
    }

    void setDataValuesToDropDown(String dataValues, ViewPagerHolder holder) {
        String idValue;
        ArrayList<String> dataValuesAge = new ArrayList<>();
        if (dataValues.contains(",")) {
            String[] separated = dataValues.split(",");
            for (String i : separated) {
                idValue = i.substring(1).replaceAll("[^a-zA-Z\\d]", " ");
                dataValuesAge.add(idValue);
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (context, android.R.layout.select_dialog_item, dataValuesAge);
        holder.completeTextView.setThreshold(1);
        holder.completeTextView.setAdapter(adapter);
    }
}

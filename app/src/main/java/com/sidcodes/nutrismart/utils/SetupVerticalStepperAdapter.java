package com.sidcodes.nutrismart.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sidcodes.nutrismart.R;
import com.sidcodes.nutrismart.model.StepData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupVerticalStepperAdapter extends RecyclerView.Adapter<SetupVerticalStepperAdapter.StepperViewHolder> {

    private final List<StepData> steps;
    private final RecyclerView recyclerView;
    private final Context context;
    private int currentStep = 0;
    private final Map<Integer, Map<Integer, String>> stepInputs = new HashMap<>();

    public SetupVerticalStepperAdapter(List<StepData> steps, RecyclerView recyclerView, Context context) {
        this.steps = steps;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    @NonNull
    @Override
    public StepperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepperViewHolder holder, int position) {
        StepData stepData = steps.get(position);
        holder.stepTitle.setText(stepData.getTitle());

        holder.inputContainer.removeAllViews();

        Map<Integer, String> inputsForStep = stepInputs.getOrDefault(position, new HashMap<>());
        for (int i = 0; i < stepData.getInputTitles().size(); i++) {
            EditText inputField = new EditText(holder.itemView.getContext());
            inputField.setHint(stepData.getInputTitles().get(i));
            inputField.setImeOptions(i == stepData.getInputTitles().size() - 1 ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT);
            inputField.setSingleLine(true);
            inputField.setText(inputsForStep.getOrDefault(i, ""));

            final int inputIndex = i;
            inputField.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(String text) {
                    inputsForStep.put(inputIndex, text);
                    stepInputs.put(position, inputsForStep);
                    validateTick(holder, position);
                }
            });

            holder.inputContainer.addView(inputField);
        }

        if (position == currentStep) {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.inputContainer.setVisibility(View.VISIBLE);
            holder.continueButton.setVisibility(View.VISIBLE);
            holder.iconCompleted.setVisibility(View.GONE);
            holder.editIcon.setVisibility(View.GONE);
            holder.continueButton.setText(position == steps.size() - 1 ? "Finish" : "Continue");
            animateCard(holder.cardView, 1.05f);
            animateButton(holder.continueButton);
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_purple));
            holder.inputContainer.setVisibility(View.GONE);
            holder.continueButton.setVisibility(View.GONE);
            animateCard(holder.cardView, 1.0f);

            if (isStepCompleted(inputsForStep)) {
                holder.iconCompleted.setVisibility(View.VISIBLE);
            } else {
                holder.iconCompleted.setVisibility(View.GONE);
            }
            holder.editIcon.setVisibility(View.VISIBLE);
        }

        holder.continueButton.setOnClickListener(v -> {
            if (isStepCompleted(inputsForStep)) {
                if (position < steps.size() - 1) {
                    updateActiveStep(position + 1);
                } else {
                    Toast.makeText(context, "All steps completed!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please complete all fields before continuing.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.editIcon.setOnClickListener(v -> updateActiveStep(position));
    }

    private boolean isStepCompleted(Map<Integer, String> inputsForStep) {
        for (String value : inputsForStep.values()) {
            if (value == null || value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void validateTick(StepperViewHolder holder, int position) {
        if (isStepCompleted(stepInputs.getOrDefault(position, new HashMap<>()))) {
            holder.iconCompleted.setVisibility(View.VISIBLE);
        } else {
            holder.iconCompleted.setVisibility(View.GONE);
        }
    }

    private void updateActiveStep(int newStep) {
        int previousStep = currentStep;
        currentStep = newStep;
        notifyItemChanged(previousStep);
        notifyItemChanged(currentStep);

        if (newStep >= 0 && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.post(() -> layoutManager.scrollToPositionWithOffset(newStep, 0));
        }
    }

    private void animateCard(CardView cardView, float scale) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", scale);
        scaleX.setDuration(300);
        scaleY.setDuration(300);
        scaleX.start();
        scaleY.start();
    }

    private void animateButton(Button button) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 0.5f, 1f);
        alpha.setDuration(300);
        alpha.start();
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class StepperViewHolder extends RecyclerView.ViewHolder {
        TextView stepTitle;
        ImageView logoPlaceholder;
        LinearLayout inputContainer;
        Button continueButton;
        ImageView iconCompleted, editIcon;
        CardView cardView;

        public StepperViewHolder(@NonNull View itemView) {
            super(itemView);
            logoPlaceholder = itemView.findViewById(R.id.logo_placeholder);
            stepTitle = itemView.findViewById(R.id.step_title);
            inputContainer = itemView.findViewById(R.id.input_container);
            continueButton = itemView.findViewById(R.id.btn_continue);
            iconCompleted = itemView.findViewById(R.id.icon_completed);
            editIcon = itemView.findViewById(R.id.icon_edit);
            cardView = (CardView) itemView;
        }
    }
}
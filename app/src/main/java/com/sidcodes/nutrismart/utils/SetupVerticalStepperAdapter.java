package com.sidcodes.nutrismart.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private int currentStep = 0; // Tracks the active step
    private final Map<Integer, Map<Integer, String>> stepInputs = new HashMap<>(); // Step -> Input index -> Value

    public SetupVerticalStepperAdapter(List<StepData> steps, RecyclerView recyclerView) {
        this.steps = steps;
        this.recyclerView = recyclerView;
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

        // Clear the input container
        holder.inputContainer.removeAllViews();

        // Dynamically create input fields based on stepData
        Map<Integer, String> inputsForStep = stepInputs.getOrDefault(position, new HashMap<>());
        for (int i = 0; i < stepData.getInputTitles().size(); i++) {
            EditText inputField = new EditText(holder.itemView.getContext());
            inputField.setHint(stepData.getInputTitles().get(i));

            // Restore previously entered value (if available)
            inputField.setText(inputsForStep.getOrDefault(i, ""));

            // Listen for text changes
            final int inputIndex = i;
            inputField.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(String text) {
                    inputsForStep.put(inputIndex, text);
                    stepInputs.put(position, inputsForStep);
                }
            });

            // Enable/disable based on current step
            inputField.setEnabled(position == currentStep);

            // Focus listener to update active section when clicking an input field
            inputField.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus && currentStep != position) {
                    Log.d("FocusListener", "Switching focus to position: " + position);
                    updateActiveStep(position);
                }
            });

            holder.inputContainer.addView(inputField);
        }

        // Show/Hide buttons based on the step index
        holder.btnPrevious.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.btnNext.setText(position == steps.size() - 1 ? "Finish" : "Next");

        // Handle "Previous" button click
        holder.btnPrevious.setOnClickListener(v -> {
            if (position > 0) {
                updateActiveStep(position - 1);
            }
        });

        // Handle "Next" button click
        holder.btnNext.setOnClickListener(v -> {
            if (position < steps.size() - 1) {
                updateActiveStep(position + 1);
            } else {
                // Handle form submission (last step)
                JSONObject jsonData = collectInputs();
                Toast.makeText(v.getContext(), "Form Submitted! JSON: " + jsonData, Toast.LENGTH_SHORT).show();
            }
        });

        // Highlight the current step
        holder.itemView.setAlpha(position == currentStep ? 1.0f : 0.5f);
        holder.itemView.setEnabled(position == currentStep);
    }

    private void updateActiveStep(int newStep) {
        if (newStep != currentStep) {
            Log.d("StepperAdapter", "Updating current step from " + currentStep + " to " + newStep);
            currentStep = newStep;
            notifyDataSetChanged(); // Refresh the UI to highlight the active section
            centerCurrentStep(newStep); // Scroll to the active section
        }
    }

    private void centerCurrentStep(int position) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.post(() -> {
                View stepView = layoutManager.findViewByPosition(position);
                if (stepView != null) {
                    int[] location = new int[2];
                    stepView.getLocationOnScreen(location);

                    int recyclerHeight = recyclerView.getHeight();
                    int stepHeight = stepView.getHeight();

                    int scrollOffset = location[1] - recyclerHeight / 2 + stepHeight / 2;
                    if (position == getItemCount() - 1) {
                        // Adjust for last step
                        int bottomOffset = stepHeight + location[1] - recyclerHeight;
                        scrollOffset = Math.max(scrollOffset, bottomOffset);
                    }

                    recyclerView.smoothScrollBy(0, scrollOffset);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    private JSONObject collectInputs() {
        JSONObject json = new JSONObject();
        try {
            for (Map.Entry<Integer, Map<Integer, String>> stepEntry : stepInputs.entrySet()) {
                JSONObject stepData = new JSONObject();
                for (Map.Entry<Integer, String> inputEntry : stepEntry.getValue().entrySet()) {
                    stepData.put("input_" + inputEntry.getKey(), inputEntry.getValue());
                }
                json.put("step_" + stepEntry.getKey(), stepData);
            }
        } catch (Exception e) {
            Log.e("SetupVerticalStepperAdapter", "Error generating JSON: " + e.getMessage());
        }
        return json;
    }

    static class StepperViewHolder extends RecyclerView.ViewHolder {
        TextView stepTitle;
        LinearLayout inputContainer; // Container for dynamic inputs
        Button btnPrevious, btnNext;

        public StepperViewHolder(@NonNull View itemView) {
            super(itemView);
            stepTitle = itemView.findViewById(R.id.step_title);
            inputContainer = itemView.findViewById(R.id.input_container);
            btnPrevious = itemView.findViewById(R.id.btn_previous);
            btnNext = itemView.findViewById(R.id.btn_next);
        }
    }
}
package com.esri.arcgisruntime.sample.blendrenderer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.esri.arcgisruntime.raster.ColorRamp;
import com.esri.arcgisruntime.raster.SlopeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which handles the blend renderer parameters dialog.
 */

public class
ParametersDialogFragment extends DialogFragment {

  /**
   * Interface for passing dialog parameters back to MainActivity.
   */
  public interface ParametersListener {
    void returnParameters(int altitude, int azimuth, SlopeType slopeType, ColorRamp.PresetType colorRampType);
  }

  private Integer mAltitude;

  private Integer mAzimuth;

  private SlopeType mSlopeType;

  private ColorRamp.PresetType mColorRampType;

  private TextView mCurrAltitudeTextView;

  private TextView mCurrAzimuthTextView;

  /**
   * Builds parameter dialog with values pulled through from MainActivity.
   *
   * @param savedInstanceState
   * @return create parameter dialog box
   */
  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
        (Context.LAYOUT_INFLATER_SERVICE);

    Bundle blendParameters = getArguments();
    if (blendParameters != null) {
      mAltitude = blendParameters.getInt("altitude");
      mAzimuth = blendParameters.getInt("azimuth");
      mSlopeType = (SlopeType) blendParameters.getSerializable("slope_type");
      mColorRampType = (ColorRamp.PresetType) blendParameters.getSerializable("color_ramp_type");
    }

    final AlertDialog.Builder paramDialog = new AlertDialog.Builder(getContext());
    @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_box, null);
    paramDialog.setView(dialogView);
    paramDialog.setTitle(R.string.dialog_title);
    paramDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dismiss();
      }
    });
    paramDialog.setPositiveButton("Render", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        ParametersListener activity =
            (ParametersListener) getActivity();
        activity.returnParameters(mAltitude, mAzimuth, mSlopeType, mColorRampType);
      }
    });

    mCurrAltitudeTextView = (TextView) dialogView.findViewById(R.id.curr_altitude_text);
    SeekBar altitudeSeekBar = (SeekBar) dialogView.findViewById(R.id.altitude_seek_bar);
    altitudeSeekBar.setMax(90); //altitude is restricted to 0 - 90
    //set initial altitude value
    updateAltitudeSeekBar(altitudeSeekBar);
    altitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mAltitude = progress;
        updateAltitudeSeekBar(seekBar);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });

    mCurrAzimuthTextView = (TextView) dialogView.findViewById(R.id.curr_azimuth_text);
    SeekBar azimuthSeekBar = (SeekBar) dialogView.findViewById(R.id.azimuth_seek_bar);
    azimuthSeekBar.setMax(360); //azimuth measured in degrees 0 - 360
    //set initial azimuth value
    updateAzimuthSeekBar(azimuthSeekBar);
    azimuthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mAzimuth = progress;
        updateAzimuthSeekBar(seekBar);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });

    List<String> slopeTypeArray = new ArrayList<>();
    slopeTypeArray.add("None");    //ordinals:0
    slopeTypeArray.add("Degree");           //1
    slopeTypeArray.add("Percent rise");     //2
    slopeTypeArray.add("Scaled");           //3

    ArrayAdapter<String> slopeTypeSpinnerAdapter = new ArrayAdapter<>(
        getContext(),
        R.layout.spinner_text_view,
        slopeTypeArray);

    Spinner slopeTypeSpinner = (Spinner) dialogView.findViewById(R.id.slope_type_spinner);
    slopeTypeSpinner.setAdapter(slopeTypeSpinnerAdapter);
    slopeTypeSpinner.setSelection(mSlopeType.ordinal());
    slopeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
          case 0:
            mSlopeType = SlopeType.NONE;
            break;
          case 1:
            mSlopeType = SlopeType.DEGREE;
            break;
          case 2:
            mSlopeType = SlopeType.PERCENT_RISE;
            break;
          case 3:
            mSlopeType = SlopeType.SCALED;
            break;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    List<String> colorRampTypeArray = new ArrayList<>();
    colorRampTypeArray.add("None");    //ordinals:0
    colorRampTypeArray.add("Elevation");        //1
    colorRampTypeArray.add("DEM screen");       //2
    colorRampTypeArray.add("DEM light");        //3

    ArrayAdapter<String> colorRampSpinnerAdapter = new ArrayAdapter<>(
        getContext(),
        R.layout.spinner_text_view,
        colorRampTypeArray);

    Spinner colorRampSpinner = (Spinner) dialogView.findViewById(R.id.color_ramp_spinner);
    colorRampSpinner.setAdapter(colorRampSpinnerAdapter);
    colorRampSpinner.setSelection(mColorRampType.ordinal());
    colorRampSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
          case 0:
            mColorRampType = ColorRamp.PresetType.NONE;
            break;
          case 1:
            mColorRampType = ColorRamp.PresetType.ELEVATION;
            break;
          case 2:
            mColorRampType = ColorRamp.PresetType.DEM_SCREEN;
            break;
          case 3:
            mColorRampType = ColorRamp.PresetType.DEM_LIGHT;
            break;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    return paramDialog.create();
  }

  @SuppressLint("SetTextI18n")
  private void updateAltitudeSeekBar(SeekBar altitudeSeekBar) {
    altitudeSeekBar.setProgress(mAltitude);
    mCurrAltitudeTextView.setText(mAltitude.toString());
  }

  @SuppressLint("SetTextI18n")
  private void updateAzimuthSeekBar(SeekBar azimuthSeekBar) {
    azimuthSeekBar.setProgress(mAzimuth);
    mCurrAzimuthTextView.setText(mAzimuth.toString());
  }
}

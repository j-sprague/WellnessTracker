package edu.uncc.wellnesstracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uncc.wellnesstracker.databinding.FragmentAddBinding;


public class AddFragment extends Fragment {

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentAddBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private int mYear, mMonth, mDay, mHour, mMinute;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private String ampm;

    public int selectedQuality;

    public double selectedSleep, selectedExercise, selectedWeight;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (selectedSleep > 0) {
            binding.textViewSleep.setText("Sleep Time: " + selectedSleep + " Hours");
        }
        if (selectedExercise > 0) {
            binding.textViewExercise.setText("Exercise Time: " + selectedExercise + " Hours");
        }
        if (selectedQuality > 0) {
            binding.textViewQuality.setText("Sleep Quality: " + selectedQuality);
        }
        if (selectedDay > 0) {
            binding.textViewDateTime.setText("Date: " + (selectedMonth+1)  + "/" + selectedDay + "/" + selectedYear);
        }
        if (selectedMinute > 0 || selectedHour > 0) {
            binding.textViewTime.setText("Time: " + selectedHour + ":" + String.format("%02d", selectedMinute) + " " + ampm);
        }

        binding.buttonSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoSleep();
            }
        });
        binding.buttonQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoQuality();
            }
        });
        binding.buttonExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoExercise();
            }
        });

        binding.buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        month = month + 1;
//                        Toast.makeText(getActivity(), "mdy: " + month + " " + dayOfMonth + " " + year, Toast.LENGTH_SHORT).show();
//                    }
//                };
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener,  year, month, day);
//                datePickerDialog.show();
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                binding.textViewDateTime.setText("Date: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                                selectedMonth = monthOfYear;
                                selectedDay = dayOfMonth;
                                selectedYear = year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        binding.buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

//                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                    }
//                };
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, false);
//                timePickerDialog.show();
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

//                                txtTime.setText(hourOfDay + ":" + minute);
                                if (hourOfDay < 12) {
                                    ampm = "AM";
                                } else {
                                    ampm = "PM";
                                }
                                if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                } else if (hourOfDay == 0) {
                                    hourOfDay = 12;
                                }
                                binding.textViewTime.setText("Time: " + hourOfDay + ":" + String.format("%02d", minute) + " " + ampm);
                                selectedHour = hourOfDay;
                                selectedMinute = minute;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancel();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedWeight = Double.parseDouble(binding.editTextWeight.getText().toString());

                AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "entries-db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
                String time = mMonth + "/" + mDay + "/" + mYear + " " + mHour + ":" + mMinute;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                cal.set(Calendar.MINUTE, selectedMinute);
                cal.set(Calendar.MONTH, selectedMonth);
                cal.set(Calendar.DAY_OF_MONTH, selectedDay);
                cal.set(Calendar.YEAR, selectedYear);
                Date dateTime = new java.sql.Date(cal.getTimeInMillis());
                Entry entry = new Entry(selectedQuality, selectedSleep, selectedExercise, selectedWeight, dateTime);
                db.entryDao().insertAll(entry);
                mListener.cancel();
            }
        });
    }



    public interface AddListener {
        void cancel();
        void gotoSleep();
        void gotoQuality();
        void gotoExercise();
    }

    AddListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (AddListener) context;
    }



}
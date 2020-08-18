package com.example.vibhavapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vibhavapp.R;
import com.example.vibhavapp.data.MyDbHandler;
import com.example.vibhavapp.insideSubject;
import com.example.vibhavapp.model.subject;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<subject> subjectList;
    private MyDbHandler db;
    private recyclerViewAdapter adapter;
    private String newName;

    public recyclerViewAdapter(Context context, List<subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
        this.db = new MyDbHandler(context);
        this.adapter = this;
    }

//    Where to get single row as viewHolder object?
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public recyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        return new ViewHolder(view);
    }

//    What will happen after we create the viewHolder object?
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final subject subject = subjectList.get(position);
        final int attendanceCriteriaInt = subject.getCriteria();

        //        read attendance criteria from shared preferences
//        SharedPreferences shrd = context.getSharedPreferences("newInstall", MODE_PRIVATE);
//        final int attendanceCriteriaInt = shrd.getInt("attendanceCriteria", -1);

//        set bottom margin to 82dp
        if (position + 1 == getItemCount()) {
            setBottomMargin(holder.itemView, (int) (82 * Resources.getSystem().getDisplayMetrics().density));
        }
        else {
            setBottomMargin(holder.itemView, 0);
        }

        //   set holder contents
        holder.subjectName.setText(subject.getName());
        holder.progressBar.setIndeterminate(false);

        holder.progressBar.setMax(100);

         /* holder.attendance.setText(subject.getAttendance());
        holder.statusText.setText(subject.getProgress() + "%");


//        set color for progress bar and toAttendText accordingly
        if (subject.getProgress() >= attendanceCriteriaInt) {
//            progress bar color
            holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            holder.progressBar.setProgress(subject.getProgress());
//            toAttend classes cases
            if (subject.getAbsent() + subject.getPresent() != 0) {
                if (attendanceCriteriaInt == 0)
                    holder.toAttendText.setText("You may take any no. of leaves.");
                else if (subject.getToAbsent() == 0)
                    holder.toAttendText.setText("On Track, Don't miss next class to be On Track.");
                else if (subject.getToAbsent() == 1)
                    holder.toAttendText.setText("Good going, You may take 1 leave.");
                else
                    holder.toAttendText.setText("Good going, You may take " + subject.getToAbsent() + " leaves.");
            }
        } else {
//            progress bar color
            holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            holder.progressBar.setProgress(subject.getProgress());
//            toAttend classes cases
            if (subject.getAbsent() + subject.getPresent() != 0) {
                if (subject.getToPresent() == 1)
                    holder.toAttendText.setText("Almost there, Attend next class to be On track.");
                else if (subject.getToPresent() < 0 )
                    holder.toAttendText.setText("It's Over, You can never be On Track.");
                else
                    holder.toAttendText.setText("Buck Up, Attend next " + subject.getToPresent() + " classes to be On Track.");
            }
        }
*/

       refresh(holder, position, subjectList);

       //        holder.progressBar.setProgress(subject.getProgress());

// PRESENT CLICK
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int present = db.addPresent(subject.getName());
                subject.setPresent(present);

            /*                holder.attendance.setText(subject.getAttendance());
                holder.statusText.setText(subject.getProgress() + "%");

                if (subject.getProgress() >= attendanceCriteriaInt) {
//            progress bar color
                    holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    holder.progressBar.setProgress(subject.getProgress());
//            toAttend classes cases
                    if (subject.getAbsent() + subject.getPresent() != 0) {
                        if (attendanceCriteriaInt == 0)
                            holder.toAttendText.setText("You may take any no. of leaves.");
                        else if (subject.getToAbsent() == 0)
                            holder.toAttendText.setText("On Track, Don't miss next class to be On Track.");
                        else if (subject.getToAbsent() == 1)
                            holder.toAttendText.setText("Good going, You may take 1 leave.");
                        else
                            holder.toAttendText.setText("Good going, You may take " + subject.getToAbsent() + " leaves.");
                    }
                } else {
//            progress bar color
                    holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    holder.progressBar.setProgress(subject.getProgress());
//            toAttend classes cases
                    if (subject.getAbsent() + subject.getPresent() != 0) {
                        if (subject.getToPresent() == 1)
                            holder.toAttendText.setText("Almost there, Attend next class to be On track.");
                        else if (subject.getToPresent() < 0 )
                            holder.toAttendText.setText("It's Over, You can never be On Track.");
                        else
                            holder.toAttendText.setText("Buck Up, Attend next " + subject.getToPresent() + " classes to be On Track.");
                    }
                }*/

                refresh(holder, position, subjectList);

            //      notifyItemChanged(holder.getAdapterPosition());
            }

        });

// ABSENT CLICK
        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int absent = db.addAbsent(subject.getName());
                subject.setAbsent(absent);

                /*holder.attendance.setText(subject.getAttendance());
                holder.statusText.setText(subject.getProgress() + "%");

                if (subject.getProgress() >= attendanceCriteriaInt) {
//            progress bar color
                    holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    holder.progressBar.setProgress(subject.getProgress());
//            toAttend classes cases
                    if (subject.getAbsent() + subject.getPresent() != 0) {
                        if (attendanceCriteriaInt == 0)
                            holder.toAttendText.setText("You may take any no. of leaves.");
                        else if (subject.getToAbsent() == 0)
                            holder.toAttendText.setText("On Track, Don't miss next class to be On Track.");
                        else if (subject.getToAbsent() == 1)
                            holder.toAttendText.setText("Good going, You may take 1 leave.");
                        else
                            holder.toAttendText.setText("Good going, You may take " + subject.getToAbsent() + " leaves.");
                    }
                } else {
//            progress bar color
                    holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    holder.progressBar.setProgress(subject.getProgress());
//            toAttend classes cases
                    if (subject.getAbsent() + subject.getPresent() != 0) {
                        if (subject.getToPresent() == 1)
                            holder.toAttendText.setText("Almost there, Attend next class to be On Track.");
                        else if (subject.getToPresent() < 0 )
                            holder.toAttendText.setText("It's Over, You can never be On Track.");
                        else
                            holder.toAttendText.setText("Buck Up, Attend next " + subject.getToPresent() + " classes to be On Track.");
                    }
                }*/

                refresh(holder, position, subjectList);

            //     notifyItemChanged(holder.getAdapterPosition());
            }
        });

        //        holder.undoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                char c = db.toUndo(subject.getName());
//                if (c == 'n') {
//                    Toast.makeText(v.getContext(), "Cannot Undo", Toast.LENGTH_SHORT).show();
//                } else if (c == 'p') {
//                   int present = db.subtractPresent(subject.getName());
//                    subject.setPresent(present);
//                } else if (c == 'a') {
//                    int absent = db.subtractAbsent(subject.getName());
//                    subject.setAbsent(absent);
//                }
//
//                holder.attendance.setText(subject.getAttendance());
//                holder.statusText.setText(subject.getProgress() + "%");
//
//                if (subject.getProgress() >= attendanceCriteriaInt) {
////            progress bar color
//                    holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
//                    holder.progressBar.setProgress(subject.getProgress());
////            toAttend classes cases
//                    if (subject.getAbsent() + subject.getPresent() != 0) {
//                        if (attendanceCriteriaInt == 0)
//                            holder.toAttendText.setText("You may take any no. of leaves.");
//                        else if (subject.getToAbsent() == 0)
//                            holder.toAttendText.setText("On Track, Don't miss next class to be On Track.");
//                        else if (subject.getToAbsent() == 1)
//                            holder.toAttendText.setText("Good going, You may take 1 leave.");
//                        else
//                            holder.toAttendText.setText("Good going, You may take " + subject.getToAbsent() + " leaves.");
//                    } else
//                        holder.toAttendText.setText("");
//                } else {
////            progress bar color
//                    holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//                    holder.progressBar.setProgress(subject.getProgress());
////            toAttend classes cases
//                    if (subject.getAbsent() + subject.getPresent() != 0) {
//                        if (subject.getToPresent() == 1)
//                            holder.toAttendText.setText("Almost there, Attend next class to be On track.");
//                        else if (subject.getToPresent() < 0 )
//                            holder.toAttendText.setText("It's Over, You can never be On Track.");
//                        else
//                            holder.toAttendText.setText("Buck Up, Attend next " + subject.getToPresent() + " classes to be On Track.");
//                    } else
//                        holder.toAttendText.setText("");
//                }
//                notifyItemChanged(holder.getAdapterPosition());
//            }
//        });

// SUB CLICK
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView toAttendText = v.findViewById(R.id.toAttendText);
                String toAttend = toAttendText.getText().toString();

                Intent intent = new Intent(v.getContext(), insideSubject.class);
                intent.putExtra("subjectName", subject.getName());
                intent.putExtra("toAttendText", toAttend);

                context.startActivity(intent);

            }
        });

// SUB LONG CLICK
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(), R.style.BottomSheetTheme);
                View bottomSheetView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomsheet_layout,
                                        (LinearLayout)v.findViewById(R.id.bottomsheet_contents));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                TextView subjectName_bottom = bottomSheetView.findViewById(R.id.subjectName_bottom);
                subjectName_bottom.setText(subject.getName());

// UNDO CLICK
                bottomSheetView.findViewById(R.id.undo_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        bottomSheetDialog.dismiss();
                        boolean change = true;
                    //        Toast.makeText(v.getContext(), "Undo Clicked..", Toast.LENGTH_SHORT).show();
                        char c = db.toUndo(subject.getName());
                        if (c == 'n') {
                            change = false;
                            Toast.makeText(v.getContext(), "Cannot Undo", Toast.LENGTH_SHORT).show();
                        } else if (c == 'p') {
                            int present = db.subtractPresent(subject.getName());
                            subject.setPresent(present);
                        } else if (c == 'a') {
                            int absent = db.subtractAbsent(subject.getName());
                            subject.setAbsent(absent);
                        }
            /*
                        holder.attendance.setText(subject.getAttendance());
                        holder.statusText.setText(subject.getProgress() + "%");

                        if (subject.getProgress() >= attendanceCriteriaInt) {
                            //  progress bar color
                            holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                            holder.progressBar.setProgress(subject.getProgress());
                            //    toAttend classes cases
                            if (subject.getAbsent() + subject.getPresent() != 0) {
                                if (attendanceCriteriaInt == 0)
                                    holder.toAttendText.setText("You may take any no. of leaves.");
                                else if (subject.getToAbsent() == 0)
                                    holder.toAttendText.setText("On Track, Don't miss next class to be On Track.");
                                else if (subject.getToAbsent() == 1)
                                    holder.toAttendText.setText("Good going, You may take 1 leave.");
                                else
                                    holder.toAttendText.setText("Good going, You may take " + subject.getToAbsent() + " leaves.");
                            } else
                                holder.toAttendText.setText("");
                        } else {
                            //    progress bar color
                            holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            holder.progressBar.setProgress(subject.getProgress());
                            //    toAttend classes cases
                            if (subject.getAbsent() + subject.getPresent() != 0) {
                                if (subject.getToPresent() == 1)
                                    holder.toAttendText.setText("Almost there, Attend next class to be On track.");
                                else if (subject.getToPresent() < 0)
                                    holder.toAttendText.setText("It's Over, You can never be On Track.");
                                else
                                    holder.toAttendText.setText("Buck Up, Attend next " + subject.getToPresent() + " classes to be On Track.");
                            } else
                                holder.toAttendText.setText("");
                        }*/

                        refresh(holder, position, subjectList);

                        if (change)
                            Toast.makeText(v.getContext(), "Undone Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

// RESET CLICK
                bottomSheetView.findViewById(R.id.reset_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        bottomSheetDialog.dismiss();
                        final boolean change = (subject.getPresent() + subject.getAbsent() == 0) ? false : true;
                    //      Toast.makeText(v.getContext(), "Reset Clicked..", Toast.LENGTH_SHORT).show();
                        if (change) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Reset Attendance");
                            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    db.reset(subject.getName());
                                    dialog.dismiss();

                                    //  to refresh and remove deleted item from screen
                                    subjectList = db.getSubjects();
                                    adapter.notifyDataSetChanged();

                                    refresh(holder, position, subjectList);

                                    Toast.makeText(v.getContext(), "Attendance Reset Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setMessage("Confirm to Reset Attendance of selected Subject");
                            builder.show();
                        }
                        else
                            Toast.makeText(v.getContext(), "Cannot Reset", Toast.LENGTH_SHORT).show();
                    }
                });

//   RENAME CLICK
                bottomSheetDialog.findViewById(R.id.rename_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final EditText editText_rename = new EditText(v.getContext());
                        editText_rename.setInputType(InputType.TYPE_CLASS_TEXT);
                        editText_rename.setMaxLines(1);
                        editText_rename.setFilters(new InputFilter[] {new InputFilter.LengthFilter(12)});
                //    final String newName = "";

                        editText_rename.setText(subject.getName());
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Rename Subject");
                        builder.setMessage("Confirm to Rename Subject");
                        builder.setView(editText_rename);
            //           builder.setCancelable(false);

                        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                //                                newName = editText_rename.getText().toString().trim().toUpperCase();
////                                Toast.makeText(v.getContext(), ""+newName, Toast.LENGTH_SHORT).show();
//
//                                List<String> subjectsName = db.getSubjectsName();
//                                subjectsName.remove(subject.getName());
//
//                                if (newName.equals(""))
//                                    Toast.makeText(v.getContext(), "Subject Name can't be Empty", Toast.LENGTH_SHORT).show();
//                                else if ( subjectsName.contains(newName) )
//                                    Toast.makeText(v.getContext(), "Duplicate Subject Found", Toast.LENGTH_SHORT).show();
//                                else {
//                                    if (!newName.equals(subject.getName())) {
//                                        db.updateName(subject.getName(), newName);
//                                        Toast.makeText(v.getContext(), "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
//                                    }
//                                    dialog.dismiss();
//                                    bottomSheetDialog.dismiss();
//
//                                    //  to refresh and remove deleted item from screen
//                                    subjectList = db.getSubjects();
//                                    adapter.notifyDataSetChanged();
//                                }

                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                bottomSheetDialog.dismiss();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newName = editText_rename.getText().toString().trim().toUpperCase();
                //       Toast.makeText(v.getContext(), ""+newName, Toast.LENGTH_SHORT).show();

                                List<String> subjectsName = db.getSubjectsName();
                                subjectsName.remove(subject.getName());

                                if (newName.equals(""))
                                    Toast.makeText(v.getContext(), "Subject Name can't be Empty", Toast.LENGTH_SHORT).show();
                                else if ( subjectsName.contains(newName) )
                                    Toast.makeText(v.getContext(), "Duplicate Subject Found", Toast.LENGTH_SHORT).show();
                                else {
                                    if (!newName.equals(subject.getName())) {
                                        db.updateName(subject.getName(), newName);
                                        Toast.makeText(v.getContext(), "Subject Renamed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                    bottomSheetDialog.dismiss();

                                    //  to refresh and remove deleted item from screen
                                    subjectList = db.getSubjects();
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });

//    DELETE CLICK
                bottomSheetView.findViewById(R.id.delete_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        bottomSheetDialog.dismiss();
                //             Toast.makeText(v.getContext(), "Delete Clicked..", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Delete Subject");
                        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteSubject(subject.getName());
                                dialog.dismiss();

                               //  to refresh and remove deleted item from screen
                                subjectList = db.getSubjects();
                                adapter.notifyDataSetChanged();

                                Toast.makeText(v.getContext(), "Subject Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setMessage("Confirm to Delete Subject");
                        builder.show();
                    }
                });

                return true;
            }
        });


    }

//    How many items?
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView subjectName;
        TextView attendance;
        TextView statusText;
        TextView toAttendText;
        ProgressBar progressBar;
//        ProgressBar progressBar2;
        Button presentButton;
        Button absentButton;
//        Button undoButton;
//        RotateDrawable rotateDrawable;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            subjectName = itemView.findViewById(R.id.subjectName);
            attendance = itemView.findViewById(R.id.attendance);
            progressBar = itemView.findViewById(R.id.progressBar);
//            progressBar2 = itemView.findViewById(R.id.progressBar2);
            statusText = itemView.findViewById(R.id.statusText);
            toAttendText = itemView.findViewById(R.id.toAttendText);
            presentButton = itemView.findViewById(R.id.presentButton);
            absentButton = itemView.findViewById(R.id.absentButton);
//            undoButton = itemView.findViewById(R.id.undoButton);
//            progressBar2.setIndeterminate(false);
//            rotateDrawable = (RotateDrawable) progressBar2.getIndeterminateDrawable();
        }

        @Override
        public void onClick(View v) {
            Log.d("attman","Clicked on row");
        }
    }

    private static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }
    }

    private static void refresh(ViewHolder holder, int position, List<subject> subjectList) {
        final subject subject = subjectList.get(position);
        final int attendanceCriteriaInt = subject.getCriteria();

        holder.attendance.setText(subject.getAttendance());
        holder.statusText.setText(subject.getProgress() + "%");

        if (subject.getProgress() >= attendanceCriteriaInt) {
            //  progress bar color
            holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            holder.progressBar.setProgress(subject.getProgress());
            //    toAttend classes cases
            if (subject.getAbsent() + subject.getPresent() != 0) {
                if (attendanceCriteriaInt == 0)
                    holder.toAttendText.setText("You may take any no. of leaves.");
                else if (subject.getToAbsent() == 0)
                    holder.toAttendText.setText("On Track, Don't miss next class to be On Track.");
                else if (subject.getToAbsent() == 1)
                    holder.toAttendText.setText("Good going, You may take 1 leave.");
                else
                    holder.toAttendText.setText("Good going, You may take " + subject.getToAbsent() + " leaves.");
            } else
                holder.toAttendText.setText("");
        } else {
            //    progress bar color
            holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            holder.progressBar.setProgress(subject.getProgress());
            //    toAttend classes cases
            if (subject.getAbsent() + subject.getPresent() != 0) {
                if (subject.getToPresent() == 1)
                    holder.toAttendText.setText("Almost there, Attend next class to be On track.");
                else if (subject.getToPresent() < 0)
                    holder.toAttendText.setText("It's Over, You can never be On Track.");
                else
                    holder.toAttendText.setText("Buck Up, Attend next " + subject.getToPresent() + " classes to be On Track.");
            } else
                holder.toAttendText.setText("");
        }
    }

}


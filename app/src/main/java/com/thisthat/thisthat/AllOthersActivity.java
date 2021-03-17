package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thisthat.thisthat.models.FetchAllModel;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.SpecificCelebModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllOthersActivity extends AppCompatActivity {
    LinearLayoutCompat optionA,optionB,percentages;
    TextView a,b,a_percent,b_percent;
    ProgressBar progressBar;
    Button reload;
    SharedPreferencesConfig sharedPreferencesConfig;
    String opA,opB,choice,phone,categoryId,friendPhone,key;
    int me,idd,friend,pickA,pickB,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_others);
        optionA = findViewById(R.id.optiona);
        optionB = findViewById(R.id.optionb);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        a_percent = findViewById(R.id.optiona_percent);
        b_percent = findViewById(R.id.optionb_percent);
        percentages = findViewById(R.id.percentage);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        opA = getIntent().getExtras().getString("A");
        opB = getIntent().getExtras().getString("B");
        idd = Integer.parseInt(getIntent().getExtras().getString("ID"));
        me = Integer.parseInt(getIntent().getExtras().getString("ME"));
        friend = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        a.setText(opA);
        b.setText(opB);
        phone = sharedPreferencesConfig.readClientsPhone();
        friendPhone = sharedPreferencesConfig.readFriendsPhone();
        categoryId = Integer.toString(idd);

        if (friend == 0) {
            getUser();
        }else if (friend == 1){
            optionA.setVisibility(View.VISIBLE);
            optionB.setVisibility(View.VISIBLE);
        }
        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = a.getText().toString();
                key = Integer.toString(1);
                if (friend == 0) {
                    if (me == 1) {
                        selfLifestyle();
                    } else if (me == 2) {
                        selfFood();
                    } else if (me == 3) {
                        selfPartner();
                    }
                }else if (friend == 1){
                    if (me == 1) {
                        friendLifestyle();
                    } else if (me == 2) {
                        friendFood();
                    } else if (me == 3) {
                        friendPartner();
                    }
                }
            }
        });
        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = b.getText().toString();
                key = Integer.toString(2);
                if (friend == 0) {
                    if (me == 1) {
                        selfLifestyle();
                    } else if (me == 2) {
                        selfFood();
                    } else if (me == 3) {
                        selfPartner();
                    }
                }else if (friend == 1){
                    if (me == 1) {
                        friendLifestyle();
                    } else if (me == 2) {
                        friendFood();
                    } else if (me == 3) {
                        friendPartner();
                    }
                }
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
            }
        });


    }
    private void getUser() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        optionA.setVisibility(View.GONE);
        optionB.setVisibility(View.GONE);
        reload.setVisibility(View.GONE);
        Call<GetUserModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    optionA.setVisibility(View.VISIBLE);
                    optionB.setVisibility(View.VISIBLE);
                    if (me == 1){
                        if (response.body().getLifestyle() >= idd){
                            specificLife();
                            optionA.setEnabled(false);
                            optionB.setEnabled(false);
                            already();
                        }else if ((idd-response.body().getLifestyle())>1){
                            optionA.setVisibility(View.GONE);
                            optionB.setVisibility(View.GONE);
                            AlertDialog.Builder al = new AlertDialog.Builder(AllOthersActivity.this);
                            al.setTitle("Previous")
                                    .setMessage("Please participate in lifestyle/personality "+(idd-1)+" first in order to access this section.\nThank you")
                                    .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = al.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }else {
                            optionA.setVisibility(View.VISIBLE);
                            optionB.setVisibility(View.VISIBLE);
                        }
                    }else if (me == 2){
                        if (response.body().getFood() >= idd){
                            specificFood();
                            optionA.setEnabled(false);
                            optionB.setEnabled(false);
                            already();
                        }else if ((idd-response.body().getFood())>1){
                            optionA.setVisibility(View.GONE);
                            optionB.setVisibility(View.GONE);
                            AlertDialog.Builder al = new AlertDialog.Builder(AllOthersActivity.this);
                            al.setTitle("Previous")
                                    .setMessage("Please participate in food/beverage "+(idd-1)+" first in order to access this section.\nThank you")
                                    .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = al.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }else {
                            optionA.setVisibility(View.VISIBLE);
                            optionB.setVisibility(View.VISIBLE);
                        }
                    }else if (me == 3){
                        if (response.body().getPartner() >= idd){
                            specificPartner();
                            optionA.setEnabled(false);
                            optionB.setEnabled(false);
                            already();
                        }else if ((idd-response.body().getPartner())>1){
                            optionA.setVisibility(View.GONE);
                            optionB.setVisibility(View.GONE);
                            AlertDialog.Builder al = new AlertDialog.Builder(AllOthersActivity.this);
                            al.setTitle("Previous")
                                    .setMessage("Please participate in partner type "+(idd-1)+" first in order to access this section.\nThank you")
                                    .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = al.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }else {
                            optionA.setVisibility(View.VISIBLE);
                            optionB.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void already() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Done!")
                .setMessage("Be sure to check the changes in percentages regularly.\nYou have recorded your pick in this.")
                .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = al.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    private void selfLifestyle() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .selfLife(phone,categoryId,choice,key);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(AllOthersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void friendLifestyle() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .friendLife(phone,categoryId,choice,friendPhone);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(AllOthersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void selfFood() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .selfFood(phone,categoryId,choice,key);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(AllOthersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void friendFood() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .friendFood(phone,categoryId,choice,friendPhone);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(AllOthersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void selfPartner() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .selfPart(phone,categoryId,choice,key);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(AllOthersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void friendPartner() {
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .friendPart(phone,categoryId,choice,friendPhone);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(AllOthersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void specificFood() {
        progressBar.setVisibility(View.VISIBLE);
        Call<FetchAllModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .specificFood(categoryId);
        call.enqueue(new Callback<FetchAllModel>() {
            @Override
            public void onResponse(Call<FetchAllModel> call, Response<FetchAllModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    double aaa = ((double) pickA/total)*100;
                    double bbb = ((double) pickB/total)*100;
                    int ra = (int)Math.rint(aaa);
                    int rb = (int)Math.rint(bbb);
                    optionA.setEnabled(false);
                    optionB.setEnabled(false);
                    a_percent.setText(ra+"%");
                    b_percent.setText(rb+"%");
                    percentages.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FetchAllModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void specificLife() {
        progressBar.setVisibility(View.VISIBLE);
        Call<FetchAllModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .specificLife(categoryId);
        call.enqueue(new Callback<FetchAllModel>() {
            @Override
            public void onResponse(Call<FetchAllModel> call, Response<FetchAllModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    double aaa = ((double) pickA/total)*100;
                    double bbb = ((double) pickB/total)*100;
                    int ra = (int)Math.rint(aaa);
                    int rb = (int)Math.rint(bbb);
                    optionA.setEnabled(false);
                    optionB.setEnabled(false);
                    a_percent.setText(ra+"%");
                    b_percent.setText(rb+"%");
                    percentages.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FetchAllModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void specificPartner() {
        progressBar.setVisibility(View.VISIBLE);
        Call<FetchAllModel> call = RetrofitClient.getInstance(AllOthersActivity.this)
                .getApiConnector()
                .specificPart(categoryId);
        call.enqueue(new Callback<FetchAllModel>() {
            @Override
            public void onResponse(Call<FetchAllModel> call, Response<FetchAllModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    double aaa = ((double) pickA/total)*100;
                    double bbb = ((double) pickB/total)*100;
                    int ra = (int)Math.rint(aaa);
                    int rb = (int)Math.rint(bbb);
                    optionA.setEnabled(false);
                    optionB.setEnabled(false);
                    a_percent.setText(ra+"%");
                    b_percent.setText(rb+"%");
                    percentages.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FetchAllModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
}
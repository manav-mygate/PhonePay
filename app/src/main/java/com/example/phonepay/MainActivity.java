package com.example.phonepay;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.phonepay.modal.GameData;
import com.example.phonepay.threading.BusinessExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    List<GameData> gameDataList = new ArrayList<>();

    //mapping game level to time to complete the current logo guessing
    HashMap<Integer, Integer> levelToTimeMap = new HashMap<>();

    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.et_guess)
    EditText etGuess;
    @BindView(R.id.et_suggestion)
    TextView etSuggestion;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;

    private Handler mHandler = new Handler();
    private int nCounter = 0;

    @BindView(R.id.imv_logo)
    ImageView logo;

    int qustionIndex;
    int timer;
    int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BusinessExecutor.getInstance().executeInBusinessThread(new Runnable() {
            @Override
            public void run() {
                staticDataList();
            }
        });

        levelToTimeMap.put(0, 40);
        levelToTimeMap.put(1, 40);
        levelToTimeMap.put(2, 30);
        levelToTimeMap.put(3, 20);
        levelToTimeMap.put(4, 10);

        //load data from sharedPreference
        ReadPref readPref = new ReadPref(this);
        int level = readPref.getGameLevel();

        if (levelToTimeMap != null && levelToTimeMap.containsKey(level)) ;
        timer = Objects.requireNonNull(levelToTimeMap.get(level));

        setDataAndStartTimer();
    }

    private void setDataAndStartTimer() {
        tvLevel.setText(timer);
        tvScore.setText("0");
        Glide.with(AppController.getInstance())
                .load(gameDataList.get(qustionIndex).getImgUrl())
                .into(logo);
        etSuggestion.setText(Utils.shuffle(gameDataList.get(qustionIndex).getName()));
        mHandler.removeCallbacks(timeTask);
        mHandler.postDelayed(timeTask, 1000);
    }

    private void startNextLevel() {
        tvLevel.setText(timer);
        Glide.with(AppController.getInstance())
                .load(gameDataList.get(qustionIndex).getImgUrl())
                .into(logo);
        etSuggestion.setText(Utils.shuffle(gameDataList.get(qustionIndex).getName()));
        mHandler.removeCallbacks(timeTask);
        mHandler.postDelayed(timeTask, 1000);
        level++;
    }

    @OnClick({R.id.btn_submit})
    void onBtnSubmit() {
        if (qustionIndex < gameDataList.size()) {
            if (!TextUtils.isEmpty(etGuess.getText().toString())) {
                qustionIndex++;
                if (etGuess.getText().toString().equalsIgnoreCase(gameDataList.get(qustionIndex).getName())) {
                    int currScore = Integer.parseInt(tvScore.getText().toString()) + 10;
                    tvScore.setText(currScore);
                    loadNextQuestion();
                }
            } else {
                Toast.makeText(this, "Guess text Field in empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            int level = Integer.parseInt(tvLevel.getText().toString()) + 1;
            tvLevel.setText(level);
            if (levelToTimeMap != null && levelToTimeMap.containsKey(level))
                timer = levelToTimeMap.get(level);
            qustionIndex = 0; // we are restaring from 0 again for the next level. ideally we should define number of question in each level when data size is big
            Toast.makeText(this, "Congrats!! Level up", Toast.LENGTH_SHORT).show();
            startNextLevel();
        }

    }

    private void loadNextQuestion() {
        Glide.with(AppController.getInstance())
                .load(gameDataList.get(qustionIndex).getImgUrl())
                .into(logo);
        mHandler.removeCallbacks(timeTask);
        mHandler.postDelayed(timeTask, 1000);
        etSuggestion.setText(Utils.shuffle(gameDataList.get(qustionIndex).getName()));
        etGuess.setText("");
        etSuggestion.setText(Utils.shuffle(gameDataList.get(qustionIndex).getName()));
    }


    private void staticDataList() {
        GameData gameData = new GameData();
        gameData.setName("AADHAAR");
        gameData.setImgUrl("http://www.dsource.in/sites/default/files/resource/logo-design/logos/logos-representing-india/images/01.jpeg");
        gameDataList.add(gameData);

        GameData gameData1 = new GameData();
        gameData1.setName("PHONEPE");
        gameData1.setImgUrl("https://static.digit.in/default/thumb_101067_default_td_480x480.jpeg");
        gameDataList.add(gameData1);

        GameData gameData2 = new GameData();
        gameData2.setName("BHIM");
        gameData2.setImgUrl("https://cdn.iconscout.com/icon/free/png-256/bhim-3-69845.png");
        gameDataList.add(gameData2);

        GameData gameData3 = new GameData();
        gameData3.setName("FLIPKART");
        gameData3.setImgUrl("https://media.glassdoor.com/sqll/300494/flipkart-com-squarelogo-1433217726546.png");
        gameDataList.add(gameData3);

        GameData gameData4 = new GameData();
        gameData4.setName("WALMART");
        gameData4.setImgUrl("http://logok.org/wp-content/uploads/2014/05/Walmart-Logo-880x645.png");
        gameDataList.add(gameData4);

        GameData gameData5 = new GameData();
        gameData5.setName("MYNTRA");
        gameData5.setImgUrl("http://www.thestylesymphony.com/wp-content/uploads/2015/05/Myntra-logo.png");
        gameDataList.add(gameData5);

    }

    private Runnable timeTask = new Runnable() {
        public void run() {
            int temp = timer;
            nCounter--;
            tvTimer.setText("Time : " + nCounter);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        saveGameLevel();
    }

    private void saveGameLevel() {
        SavePref savePref = new SavePref(AppController.getInstance());
        savePref.saveGameLevel(level);
    }
}

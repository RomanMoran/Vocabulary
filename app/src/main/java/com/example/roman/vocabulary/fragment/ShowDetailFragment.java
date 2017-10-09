package com.example.roman.vocabulary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.data.Words;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by roman on 30.08.2017.
 */

@FragmentWithArgs
public class ShowDetailFragment extends BaseFragment {

    @Arg(bundler = ParcelerArgsBundler.class)
    public List<Words> words;
    @Arg(bundler = ParcelerArgsBundler.class)
    public int positionWord;
    @Arg(bundler = ParcelerArgsBundler.class)
    public int positionFlipper;

    public Words word;

    @BindView(R.id.tvTranslatable)
    TextView tvTranslatable;

    @BindView(R.id.tvTranslated)
    TextView tvTranslated;
    @BindView(R.id.etTranslated)
    EditText etTranslated;
    @BindView(R.id.btnCheckWord)
    Button btnCheckWord;
    @BindView(R.id.llLearn)
    LinearLayout llLearn;

    @BindView(R.id.flipperDetails)
    ViewFlipper flipperDetails;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_details_show_words;
    }

    public static ShowDetailFragment newInstance(/*Words wordsList*/List<Words>wordsList,int positionWord, int positionFlipper) {
        ShowDetailFragment fragment = new ShowDetailFragmentBuilder(positionFlipper,positionWord,wordsList).build();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        word= words.get(positionWord);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(int position) {
        Log.d("TAG", "EVENTBUS" + position);
        this.positionFlipper = position;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (positionFlipper) {
            case 0:
                tvTranslatable.setVisibility(View.VISIBLE);
                tvTranslatable.setText(word.getWordEn());
                tvTranslated.setText(word.getWordRu());
                break;
            case 1:
                tvTranslatable.setText(word.getWordEn());
                //flipperDetails.showNext();
                break;
            case 2:
                tvTranslatable.setVisibility(View.GONE);

        }
        flipperVisible(positionFlipper);
    }



    @OnClick(R.id.btnCheckWord)
    void checkWord(){
        String translated = etTranslated.getText().toString();
        String translatedCorrect = word.getWordRu();

        if (translated.isEmpty()){
            Toast.makeText(activity, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        if (translated.equalsIgnoreCase(translatedCorrect)){
            //flipperDetails.showNext();
            flipperDetails.showNext();

        }else{
            Toast.makeText(activity, getString(R.string.right_answer)+" \" "+translatedCorrect+" \"", Toast.LENGTH_SHORT).show();
            //flipperDetails.showNext();
        }


    }

    private void flipperVisible(int pos) {
        tvTranslated.setVisibility(pos == 0 ? View.VISIBLE : View.INVISIBLE);
        /*etTranslated.setVisibility(pos==0?View.INVISIBLE:View.VISIBLE);
        btnCheckWord.setVisibility(pos==0?View.INVISIBLE:View.VISIBLE);*/
        llLearn.setVisibility(pos == 1 ? View.VISIBLE : View.INVISIBLE);
    }
}


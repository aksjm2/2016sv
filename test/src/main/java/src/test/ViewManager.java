package src.test;

import com.sun.tools.javac.util.*;

import javax.swing.*;
import java.util.*;
import java.util.List;

/**
 * Created by SuchangKo on 16. 5. 12..
 */
public class ViewManager {
    int Percentage;
    static DrawGraph mainPanel;

    public JPanel MakeGraph(HashMap<String,String> hashMap, HashMap<String, String> hashMap2) {

        ArrayList<Integer> score = new ArrayList<Integer>();
        score.add(Integer.parseInt(hashMap.get("rawtext")));
        score.add(Integer.parseInt(hashMap.get("name")));
        score.add(Integer.parseInt(hashMap.get("comment")));
        score.add(Integer.parseInt(hashMap.get("loop")));
        score.add(Integer.parseInt(hashMap.get("condition")));

        ArrayList<Integer> score2 = new ArrayList<Integer>();
        score2.add(Integer.parseInt(hashMap2.get("rawtext")));
        score2.add(Integer.parseInt(hashMap2.get("name")));
        score2.add(Integer.parseInt(hashMap2.get("comment")));
        score2.add(Integer.parseInt(hashMap2.get("loop")));
        score2.add(Integer.parseInt(hashMap2.get("condition")));

        List<Integer> scores = score;
        List<Integer> scores2 = score2;
        mainPanel = new DrawGraph(scores, scores2);

        return mainPanel;
    }


    public void Configure(int Percentage){
        this.Percentage = Percentage;
    }

    public void SelectResult(HashMap<String,String> hashMap, HashMap<String, String> hashMap2) {
        ArrayList<Integer> scores = new ArrayList<Integer>();
        scores.add(Integer.parseInt(hashMap.get("rawtext")));
        scores.add(Integer.parseInt(hashMap.get("name")));
        scores.add(Integer.parseInt(hashMap.get("comment")));
        scores.add(Integer.parseInt(hashMap.get("loop")));
        scores.add(Integer.parseInt(hashMap.get("condition")));

        ArrayList<Integer> scores2 = new ArrayList<Integer>();
        scores2.add(Integer.parseInt(hashMap2.get("rawtext")));
        scores2.add(Integer.parseInt(hashMap2.get("name")));
        scores2.add(Integer.parseInt(hashMap2.get("comment")));
        scores2.add(Integer.parseInt(hashMap2.get("loop")));
        scores2.add(Integer.parseInt(hashMap2.get("condition")));

        mainPanel.setScores(scores, scores2);
    }
}

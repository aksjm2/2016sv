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
        DrawGraph mainPanel = new DrawGraph(scores, scores2);

        return mainPanel;
    }

    public void DisplayResult(){

    }

    public void Configure(int Percentage){
        this.Percentage = Percentage;
    }

    public void SelectResult(){

    }
}

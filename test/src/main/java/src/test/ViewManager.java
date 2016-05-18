package src.test;

import javax.swing.*;
import java.util.*;

/**
 * Created by SuchangKo on 16. 5. 12..
 */
public class ViewManager {
    int Percentage;

    public JPanel MakeGraph(int index) {
        List<Integer> scores = Arrays.asList(10, 20, 100, 20, 10, 50, 100);
        List<Integer> scores2 = Arrays.asList(10, 10, 10, 10, 10, 10, 10);
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

package src.test;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SuchangKo on 16. 5. 12..
 */
public class ResultManager {
    ArrayList<HashMap<String, String>> Scores;

    public void Compare(ArrayList<String> FileList) {
        Scores = new ArrayList<HashMap<String, String>>();

        ArrayList<String> FileContentArray = ParseFile(FileList);



        for (int i = 0; i < FileList.size(); i++) {
            for (int j = i + 1; j < FileList.size(); j++) {
                // TODO: 여기 있는 score 정보들을 Scores에 넣어야 됨

                int scoreComment, scoreName, scoreLoop, scoreCondition, scoreFunctionMerge, total;
                int scoreRawText = CheckRawText(FileContentArray.get(i),FileContentArray.get(j));


                // TODO: ParseFile 추가해야 됨.
                // FIXME: ParseFile의 역할
                if (scoreRawText <= 80) {
                    scoreComment = CheckComment(FileContentArray.get(i),FileContentArray.get(j));
                    scoreName = CheckName(FileContentArray.get(i),FileContentArray.get(j));
                    scoreLoop = CheckLoop(FileContentArray.get(i),FileContentArray.get(j));
                    scoreCondition = CheckCondition(FileContentArray.get(i),FileContentArray.get(j));
                    scoreFunctionMerge = CheckFunctionMerge(FileContentArray.get(i),FileContentArray.get(j));
                } else {
                    scoreComment = scoreRawText;
                    scoreName = scoreRawText;
                    scoreLoop = scoreRawText;
                    scoreCondition = scoreRawText;
                    scoreFunctionMerge = scoreRawText;
                }


                total = (scoreRawText + scoreComment + scoreName + scoreLoop + scoreCondition + scoreFunctionMerge) / 6;

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("FileA", FileList.get(i));
                hashMap.put("FileB", FileList.get(j));

                hashMap.put("rawtext", String.valueOf(scoreRawText));
                hashMap.put("comment", String.valueOf(scoreComment));
                hashMap.put("name", String.valueOf(scoreName));
                hashMap.put("loop", String.valueOf(scoreLoop));
                hashMap.put("condition", String.valueOf(scoreCondition));
                hashMap.put("functionmerge", String.valueOf(scoreFunctionMerge));
                hashMap.put("total", String.valueOf(total));

                Scores.add(hashMap);
            }
        }

        CalculateScores();
    }


    public int CheckRawText(String a, String b) {

        String s0 = a;
        String s1 = b;

        int percentage = 0;
        // Trim and remove duplicate spaces
        s0 = s0.trim().replaceAll("\\s+", " ");
        s1 = s1.trim().replaceAll("\\s+", " ");

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }


        percentage=(int) (100 - (float) cost[len0 - 1] * 100 / (float) (s0.length() + s1.length()));

        return percentage;
    }



    public ArrayList<String> ParseFile(ArrayList<String> FileList) {
        ArrayList<String> FileArray = new ArrayList<String>();
        for(String fileaddr : FileList){
            try {
                File file = new File(fileaddr);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String content = "";
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    content += line+"\n";
                FileArray.add(content);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return FileArray;
    }

    public int CheckComment(String a, String b) {

        Pattern pattern  = pattern = Pattern.compile("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)");

        String tmp_a = a;
        String tmp_b = b;
        String s0 = "";
        String s1 = "";



        Matcher matcher = pattern.matcher(tmp_a);


        while (matcher.find()) {
            String str = matcher.group();
            s0+=str+"\n";
        }

        matcher = pattern.matcher(tmp_b);


        while (matcher.find()) {
            String str = matcher.group();
            s1+=str+"\n";
        }

        int percentage = 0;
        // Trim and remove duplicate spaces
        s0 = s0.trim().replaceAll("\\s+", " ");
        s1 = s1.trim().replaceAll("\\s+", " ");

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        percentage=(int) (100 - (float) cost[len0 - 1] * 100 / (float) (s0.length() + s1.length()));

        return percentage;
    }

    public int CheckName(String a, String b) {
        //주석제거
        a = a.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        b = b.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        a = a.replaceAll("\\("," (");
        a = a.replaceAll(","," , ");
        a = a.replaceAll("\r"," ");
        a = a.replaceAll("\n"," ");
        b = b.replaceAll("\\("," (");
        b = b.replaceAll(","," , ");
        b = b.replaceAll("\r"," ");
        b = b.replaceAll("\n"," ");
        // short int long float double char
        // 변수명 변경
        String[] array_a = a.split(" ");
        String[] array_b = b.split(" ");

        boolean prev_type = false;
        ArrayList<String> keyword = new ArrayList<String>();

        for(int i = 0; i < array_a.length; i++){
            if(prev_type){
                String key = "";
                Pattern pattern  = Pattern.compile("[a-zA-Z_0-9]");
                Matcher matcher = pattern.matcher(array_a[i]);
                while (matcher.find()) {
                    String str = matcher.group();
                    key+=str;
                }

                keyword.add(key);
                prev_type = false;
            }else{
                if(array_a[i].equals("short") ||
                        array_a[i].equals("int") ||
                        array_a[i].equals("long") ||
                        array_a[i].equals("float") ||
                        array_a[i].equals("double") ||
                        array_a[i].equals("char") ||
                        array_a[i].equals("FILE") ){
                    prev_type = true;
                }
            }
        }

        for(String key : keyword) {
            a = a.replaceAll("\\b" + key + "\\b", "tmp");
        }

        prev_type = false;


        //For B
        keyword = new ArrayList<String>();

        for(int i = 0; i < array_b.length; i++){
            if(prev_type){
                String key = "";
                Pattern pattern = Pattern.compile("[a-zA-Z_0-9]");
                Matcher matcher = pattern.matcher(array_b[i]);
                while (matcher.find()) {
                    String str = matcher.group();
                    key+=str;
                }

                keyword.add(key);
                prev_type = false;
            }else{

                if(array_b[i].equals("short") ||
                        array_b[i].equals("int") ||
                        array_b[i].equals("long") ||
                        array_b[i].equals("float") ||
                        array_b[i].equals("double") ||
                        array_b[i].equals("char") ||
                        array_b[i].equals("FILE") ){

                    prev_type = true;
                }
            }
        }

        for(String key : keyword) {

            b = b.replaceAll("\\b" + key + "\\b", "tmp");
        }







        String s0 = a;
        String s1 = b;

        int percentage = 0;
        // Trim and remove duplicate spaces
        s0 = s0.trim().replaceAll("\\s+", " ");
        s1 = s1.trim().replaceAll("\\s+", " ");

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }


        percentage=(int) (100 - (float) cost[len0 - 1] * 100 / (float) (s0.length() + s1.length()));

        return percentage;


    }



    public int CheckLoop(String a, String b) {
        //주석제거
        a = a.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        b = b.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        a = a.replaceAll("\\("," (");
        a = a.replaceAll(","," , ");
        a = a.replaceAll("\r"," ");
        a = a.replaceAll("\n"," ");
        b = b.replaceAll("\\("," (");
        b = b.replaceAll(","," , ");
        b = b.replaceAll("\r"," ");
        b = b.replaceAll("\n"," ");

        //반복문 변경

        a = a.replaceAll("switch","if");

        b = b.replaceAll("switch","if");


        String s0 = a;
        String s1 = b;

        int percentage = 0;
        // Trim and remove duplicate spaces
        s0 = s0.trim().replaceAll("\\s+", " ");
        s1 = s1.trim().replaceAll("\\s+", " ");

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }


        percentage=(int) (100 - (float) cost[len0 - 1] * 100 / (float) (s0.length() + s1.length()));

        return percentage;
    }

    public int CheckCondition(String a, String b) {
        //주석제거
        a = a.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        b = b.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
        a = a.replaceAll("\\("," (");
        a = a.replaceAll(","," , ");
        a = a.replaceAll("\r"," ");
        a = a.replaceAll("\n"," ");
        b = b.replaceAll("\\("," (");
        b = b.replaceAll(","," , ");
        b = b.replaceAll("\r"," ");
        b = b.replaceAll("\n"," ");

        //반복문 변경

        a = a.replaceAll("while","for");

        b = b.replaceAll("while","for");

        String s0 = a;
        String s1 = b;

        int percentage = 0;
        // Trim and remove duplicate spaces
        s0 = s0.trim().replaceAll("\\s+", " ");
        s1 = s1.trim().replaceAll("\\s+", " ");

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }


        percentage=(int) (100 - (float) cost[len0 - 1] * 100 / (float) (s0.length() + s1.length()));

        return percentage;
    }

    public int CheckFunctionMerge(String a, String b) {

        return 0;
    }

    public void CalculateScores() {
        int scoreRawText = 0;
        int scoreComment = 0;
        int scoreName = 0;
        int scoreLoop = 0;
        int scoreCondition = 0;
        int scoreFunctionMerge = 0;
        int total = 0;

        for (HashMap<String, String> hashMap : Scores) {
            System.out.println(hashMap.get("FileA"));
            System.out.println(hashMap.get("FileB"));
            scoreRawText += Integer.parseInt(hashMap.get("rawtext"));
            scoreComment += Integer.parseInt(hashMap.get("comment"));
            scoreName += Integer.parseInt(hashMap.get("name"));
            scoreLoop += Integer.parseInt(hashMap.get("loop"));
            scoreCondition += Integer.parseInt(hashMap.get("condition"));
            scoreFunctionMerge += Integer.parseInt(hashMap.get("functionmerge"));
            total += Integer.parseInt(hashMap.get("total"));


            for( Map.Entry<String, String> elem : hashMap.entrySet() ){
                System.out.println( String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()) );
            }

        }

        scoreRawText = scoreRawText / Scores.size();
        scoreComment = scoreComment / Scores.size();
        scoreName = scoreName / Scores.size();
        scoreLoop = scoreLoop / Scores.size();
        scoreCondition = scoreCondition / Scores.size();
        scoreFunctionMerge = scoreFunctionMerge / Scores.size();
        total = total / Scores.size();

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("FileA", "total");
        hashMap.put("FileB", "total");

        hashMap.put("rawtext", String.valueOf(scoreRawText)); //
        hashMap.put("comment", String.valueOf(scoreComment)); //
        hashMap.put("name", String.valueOf(scoreName)); //
        hashMap.put("loop", String.valueOf(scoreLoop)); //
        hashMap.put("condition", String.valueOf(scoreCondition)); //
        hashMap.put("functionmerge", String.valueOf(scoreFunctionMerge)); //
        hashMap.put("total", String.valueOf(total));

        Scores.add(hashMap);

    }
}

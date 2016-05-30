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
        int sample_total = 1;
        int sample_t = 1;
        for (int i = 0; i < FileList.size(); i++) {
            for (int j = i + 1; j < FileList.size(); j++) {
                sample_total++;
            }
        }



        for (int i = 0; i < FileList.size(); i++) {
            for (int j = i + 1; j < FileList.size(); j++) {
                // TODO: 여기 있는 score 정보들을 Scores에 넣어야 됨

                int scoreComment, scoreName, scoreLoop, scoreCondition, scoreFunctionMerge, total;
                int scoreRawText = CheckRawText(FileContentArray.get(i), FileContentArray.get(j));

                // TODO: ParseFile 추가해야 됨.
                // FIXME: ParseFile의 역할
                if (scoreRawText <= 80) {
                    scoreComment = CheckComment(FileContentArray.get(i), FileContentArray.get(j));
                    scoreName = CheckName(FileContentArray.get(i), FileContentArray.get(j));
                    scoreLoop = CheckLoop(FileContentArray.get(i), FileContentArray.get(j));
                    scoreCondition = CheckCondition(FileContentArray.get(i), FileContentArray.get(j));
                } else {
                    scoreComment = scoreRawText;
                    scoreName = scoreRawText;
                    scoreLoop = scoreRawText;
                    scoreCondition = scoreRawText;
                }

                total = (scoreRawText + scoreComment + scoreName + scoreLoop + scoreCondition) / 5;

                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (total > ViewManager.Percentage) {
                    File tmpFile = new File(FileList.get(i));

                    hashMap.put("FileApath", FileList.get(i));
                    hashMap.put("FileAname", tmpFile.getName());

                    tmpFile = new File(FileList.get(j));
                    hashMap.put("FileBpath", FileList.get(j));
                    hashMap.put("FileBname", tmpFile.getName());

                    hashMap.put("rawtext", String.valueOf(scoreRawText));
                    hashMap.put("comment", String.valueOf(scoreComment));
                    hashMap.put("name", String.valueOf(scoreName));
                    hashMap.put("loop", String.valueOf(scoreLoop));
                    hashMap.put("condition", String.valueOf(scoreCondition));
                    hashMap.put("total", String.valueOf(total));

                    Scores.add(hashMap);
                }
                System.out.println((sample_t/sample_total*100)+"%"+sample_t+"/"+sample_total);
                sample_t++;

            }
        }

        CalculateScores();
    }


    public int CheckRawText(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 100; /* both strings are zero length */
        }
    /* // If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                               (double) longerLength; */


        s1 = longer;
        s2 = shorter;

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        int editdistance = costs[s2.length()];


        return (int) (100.0 * (longerLength - editdistance) / (double) longerLength);
    }


    public ArrayList<String> ParseFile(ArrayList<String> FileList) {
        ArrayList<String> FileArray = new ArrayList<String>();
        for (String fileaddr : FileList) {
            try {
                File file = new File(fileaddr);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String content = "";
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    content += line + "\n";
                FileArray.add(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return FileArray;
    }

    public int CheckComment(String a, String b) {

        Pattern pattern = pattern = Pattern.compile("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)");

        String tmp_a = a;
        String tmp_b = b;
        String s1 = "";
        String s2 = "";


        Matcher matcher = pattern.matcher(tmp_a);


        while (matcher.find()) {
            String str = matcher.group();
            s1 += str + "\n";
        }

        matcher = pattern.matcher(tmp_b);


        while (matcher.find()) {
            String str = matcher.group();
            s2 += str + "\n";
        }

        int percentage = 0;

        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 0; /* both strings are zero length */
        }
    /* // If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                               (double) longerLength; */


        s1 = longer;
        s2 = shorter;

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        int editdistance = costs[s2.length()];


        return (int) (100.0 * (longerLength - editdistance) / (double) longerLength);
    }

    public int CheckName(String a, String b) {
        //주석제거
        a = a.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        b = b.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        a = a.replaceAll("\\(", " (");
        a = a.replaceAll(",", " , ");
        a = a.replaceAll("\r", " ");
        a = a.replaceAll("\n", " ");
        b = b.replaceAll("\\(", " (");
        b = b.replaceAll(",", " , ");
        b = b.replaceAll("\r", " ");
        b = b.replaceAll("\n", " ");
        // short int long float double char
        // 변수명 변경
        String[] array_a = a.split(" ");
        String[] array_b = b.split(" ");

        boolean prev_type = false;
        ArrayList<String> keyword = new ArrayList<String>();

        for (int i = 0; i < array_a.length; i++) {
            if (prev_type) {
                String key = "";
                Pattern pattern = Pattern.compile("[a-zA-Z_0-9]");
                Matcher matcher = pattern.matcher(array_a[i]);
                while (matcher.find()) {
                    String str = matcher.group();
                    key += str;
                }

                keyword.add(key);
                prev_type = false;
            } else {
                if (array_a[i].equals("short") ||
                        array_a[i].equals("int") ||
                        array_a[i].equals("long") ||
                        array_a[i].equals("float") ||
                        array_a[i].equals("double") ||
                        array_a[i].equals("char") ||
                        array_a[i].equals("FILE")) {
                    prev_type = true;
                }
            }
        }

        for (String key : keyword) {
            a = a.replaceAll("\\b" + key + "\\b", "tmp");
        }

        prev_type = false;


        //For B
        keyword = new ArrayList<String>();

        for (int i = 0; i < array_b.length; i++) {
            if (prev_type) {
                String key = "";
                Pattern pattern = Pattern.compile("[a-zA-Z_0-9]");
                Matcher matcher = pattern.matcher(array_b[i]);
                while (matcher.find()) {
                    String str = matcher.group();
                    key += str;
                }

                keyword.add(key);
                prev_type = false;
            } else {

                if (array_b[i].equals("short") ||
                        array_b[i].equals("int") ||
                        array_b[i].equals("long") ||
                        array_b[i].equals("float") ||
                        array_b[i].equals("double") ||
                        array_b[i].equals("char") ||
                        array_b[i].equals("FILE")) {

                    prev_type = true;
                }
            }
        }

        for (String key : keyword) {
            b = b.replaceAll("\\b" + key + "\\b", "tmp");
        }

        String s1 = a;
        String s2 = b;

        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 0; /* both strings are zero length */
        }
    /* // If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                               (double) longerLength; */


        s1 = longer;
        s2 = shorter;

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        int editdistance = costs[s2.length()];


        return (int) (100.0 * (longerLength - editdistance) / (double) longerLength);
    }

    public int CheckLoop(String a, String b) {
        //주석제거
        a = a.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        b = b.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        a = a.replaceAll("\\(", " (");
        a = a.replaceAll(",", " , ");
        a = a.replaceAll("\r", " ");
        a = a.replaceAll("\n", " ");
        b = b.replaceAll("\\(", " (");
        b = b.replaceAll(",", " , ");
        b = b.replaceAll("\r", " ");
        b = b.replaceAll("\n", " ");


        //반복문 변경

        a = a.replaceAll("while", "for");

        b = b.replaceAll("while", "for");


        String s1 = a;
        String s2 = b;

        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 0; /* both strings are zero length */
        }
    /* // If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                               (double) longerLength; */


        s1 = longer;
        s2 = shorter;

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        int editdistance = costs[s2.length()];


        return (int) (100.0 * (longerLength - editdistance) / (double) longerLength);
    }

    public int CheckCondition(String a, String b) {
        //주석제거
        a = a.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        b = b.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
        a = a.replaceAll("\\(", " (");
        a = a.replaceAll(",", " , ");
        a = a.replaceAll("\r", " ");
        a = a.replaceAll("\n", " ");
        b = b.replaceAll("\\(", " (");
        b = b.replaceAll(",", " , ");
        b = b.replaceAll("\r", " ");
        b = b.replaceAll("\n", " ");

        //조건문 변경

        a = a.replaceAll("switch", "if");
        b = b.replaceAll("switch", "if");

        String s1 = a;
        String s2 = b;

        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 0; /* both strings are zero length */
        }
    /* // If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
                               (double) longerLength; */


        s1 = longer;
        s2 = shorter;

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        int editdistance = costs[s2.length()];


        return (int) (100.0 * (longerLength - editdistance) / (double) longerLength);
    }

    public void CalculateScores() {
        int scoreRawText = 0;
        int scoreComment = 0;
        int scoreName = 0;
        int scoreLoop = 0;
        int scoreCondition = 0;
        int scoreFunctionMerge = 0;
        int total = 0;

        HashMap<String, String> temp;
        int cnt = 0;


        if (Scores.size() == 0)
            return;

        for (int m = 0; m < Scores.size(); m++) {
            System.out.print(Scores.get(m).get("total") + " ");

        }

        for (int i = 0; i < Scores.size(); i++) {
            //
            for (int j = 0; j < Scores.size() - i - 1; j++) {
                cnt++;
                System.out.println("J:" + j);
                if (Integer.parseInt(Scores.get(j).get("total")) > Integer.parseInt(Scores.get(j + 1).get("total"))) {
                    temp = Scores.get(j);
                    Scores.set(j, Scores.get(j + 1));
                    Scores.set(j + 1, temp);
                }
            }
        }


        for (int k = 0; k < Scores.size(); k++) {
            System.out.print(Scores.get(k).get("total") + " ");

        }

        System.out.println("\n\n 총 회전 수 : " + cnt);

        for (HashMap<String, String> hashMap : Scores) {
            System.out.println(hashMap.get("FileA"));
            System.out.println(hashMap.get("FileB"));
            scoreRawText += Integer.parseInt(hashMap.get("rawtext"));
            scoreComment += Integer.parseInt(hashMap.get("comment"));
            scoreName += Integer.parseInt(hashMap.get("name"));
            scoreLoop += Integer.parseInt(hashMap.get("loop"));
            scoreCondition += Integer.parseInt(hashMap.get("condition"));

            total += Integer.parseInt(hashMap.get("total"));

            for (Map.Entry<String, String> elem : hashMap.entrySet()) {
                System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
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

        hashMap.put("total", String.valueOf(total));

        Scores.add(hashMap);

    }


}

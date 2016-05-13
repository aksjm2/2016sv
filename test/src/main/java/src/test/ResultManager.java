package src.test;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SuchangKo on 16. 5. 12..
 */
public class ResultManager {
    ArrayList<HashMap<String,String>> Scores;

    // FIXME: 파일을 한줄씩 스트링 단위로 읽어내서 ArrayList에 저장 -> 문서에 추가할지 함수를 Compare 안으로 집어 넣을지
    public ArrayList<String> FileRead(ArrayList<String> FileList, int index) {
        try {
            File file = new File(FileList.get(index));
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> content = new ArrayList<String>();

            String line;
            while ((line = bufferedReader.readLine()) != null)
                content.add(line);

            fileReader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void Compare(ArrayList<String> FileList) {
        ArrayList[] contents = new ArrayList[FileList.size()];
        for(int i = 0; i < FileList.size(); i++)
            contents[i] = FileRead(FileList, i);

        for(int i = 0; i < FileList.size(); i++) {
            for(int j = i + 1; j < FileList.size(); j++) {
                // TODO: 여기 있는 score 정보들을 Scores에 넣어야 됨
                int scoreRawText = CheckRawText(contents[i], contents[j]);
                if(scoreRawText >= 80) {
                    System.out.println("same");
                    continue;
                }

                // TODO: ParseFile 추가해야 됨.
                int scoreComment = CheckComment(contents[i], contents[j]);
                int scoreName = CheckName(contents[i], contents[j]);
                int scoreLoop = CheckLoop(contents[i], contents[j]);
                int scoreCondition = CheckCondition(contents[i], contents[j]);
                int scoreFunctionMerge = CheckFunctionMerge(contents[i], contents[j]);
            }
        }

        CalculateScores();
    }

    // FIXME: 파라미터가 File에서 ArrayList로 바뀜 -> 전부 다
    public int CheckRawText(ArrayList<String> a, ArrayList<String> b) {
        for(int i = 0; i < a.size(); i++) {
            if(!a.get(i).equals(b.get(i)))
                return 0;
        }

        return 100;
    }

    public void ParseFile(ArrayList<String> FileList) {

    }

    public int CheckComment(ArrayList<String> a, ArrayList<String> b){
        return 0;
    }

    public int CheckName(ArrayList<String> a, ArrayList<String> b){
        return 0;
    }

    public int CheckLoop(ArrayList<String> a, ArrayList<String> b){
        return 0;
    }

    public int CheckCondition(ArrayList<String> a, ArrayList<String> b){
        return 0;
    }

    public int CheckFunctionMerge(ArrayList<String> a, ArrayList<String> b){
        return 0;
    }

    public void CalculateScores(){

    }
}

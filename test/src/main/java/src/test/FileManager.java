package src.test;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by SuchangKo on 16. 5. 12..
 */
public class FileManager {
    ArrayList<String> FileList;

    public ArrayList<String> OpenFiles(ArrayList<String> FileList) {
        this.FileList = FileList;
        MainSystem.fc.setMultiSelectionEnabled(true);
        MainSystem.fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String[] filename = file.getName().split("\\.");
                String file_ext = filename[filename.length-1];
                if(file_ext.toLowerCase().equals("c") || file.isDirectory()){
                    if(file.exists()){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }

            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        int returnVal = MainSystem.fc.showOpenDialog(MainSystem.mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = MainSystem.fc.getSelectedFiles();
            for (File file : files) {
                this.FileList.add(file.getAbsolutePath());

            }
        } else {
            System.out.println("Open command cancelled by user.");
        }

        return this.FileList;
    }

    public int[] SelectFiles(ArrayList<String> FileList) {
        this.FileList = FileList;
        int[] selectedidx = MainSystem.fileListVew.getSelectedIndices();

        return selectedidx;
    }

    public void DeleteFiles(ArrayList<String> FileList) {
        int[] selectedIdx = SelectFiles(FileList);

        for (int i = selectedIdx.length - 1; i >= 0; i--) {
            this.FileList.remove(selectedIdx[i]);
        }

        DisplayFileList(this.FileList);
    }

    public void DisplayFileList(ArrayList<String> FileList) {
        ArrayList<String> temp = new ArrayList<String>();
        for (String s : FileList) {
            File file = new File(s);
            temp.add(file.getName());
        }

        String[] strings = temp.toArray(new String[]{""});
        MainSystem.fileListVew.setListData(strings);
    }

    public void Clear() {
        this.FileList.clear();
    }
}
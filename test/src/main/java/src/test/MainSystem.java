package src.test;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainSystem {
    FileManager fileManager = new FileManager();
    ResultManager resultManager = new ResultManager();
    ViewManager viewManager = new ViewManager();

    //Container
    static JFrame mainFrame = new JFrame("Graphical Clone Checker-Main");
    JFrame resultFrame = new JFrame("Graphical Clone Checker-Result");
    JDialog configureDialog = new JDialog(mainFrame, "Graphical Clone Checker-Configure");

    //Component-Main
    JButton openButton = new JButton("Open");
    JButton deleteButton = new JButton("Delete");
    JButton clearButton = new JButton("Clear");
    JButton compareButton = new JButton("Compare");
    JButton configureButton = new JButton("Configure");
    JButton exitButton = new JButton("Exit");
    static JList fileListVew = new JList();
    static JList resultList = new JList();
    static JPanel upPanel = new JPanel();
    static JPanel downPanel = new JPanel();
    static JPanel graphPanel;
    static JLabel rawtext = new JLabel();
    static JLabel name = new JLabel();
    static JLabel condition = new JLabel();
    static JLabel comment = new JLabel();
    static JLabel loop = new JLabel();
    static JLabel total = new JLabel();

    //Variable-Main
    static JFileChooser fc;
    ArrayList<String> FileList = new ArrayList<String>();

    //Component-Configure
    JButton configureApplyBtnDialog = new JButton("Apply");
    JButton configureCancelBtnDialog = new JButton("Cancel");

    static SpinnerModel spinnerModel = new SpinnerNumberModel(ViewManager.Percentage, //initial value
            0, //min
            100, //max
            5);//step
    static JSpinner spinner = new JSpinner(spinnerModel);

    public void createMainFrame() {
        JPanel upPanel = new JPanel();
        JPanel downPanel = new JPanel();
        JPanel sideButtonPanel = new JPanel();

        fc = new JFileChooser();

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickOpenBtn();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickDeleteBtn();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickClearBtn();
            }
        });
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickCompareBtn();
            }
        });
        configureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickConfigureBtn();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClickExitBtn();
            }
        });
        configureCancelBtnDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureDialog.dispose();
            }
        });
        configureApplyBtnDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewManager.Configure((Integer) spinnerModel.getValue());
                configureDialog.dispose();
            }
        });

        JScrollPane scrollPane = new JScrollPane(fileListVew);

        sideButtonPanel.setLayout(new BoxLayout(sideButtonPanel, BoxLayout.PAGE_AXIS));
        sideButtonPanel.add(compareButton);
        sideButtonPanel.add(configureButton);
        sideButtonPanel.add(exitButton);

        upPanel.setLayout(new BorderLayout());
        upPanel.add(scrollPane, BorderLayout.CENTER);
        upPanel.add(sideButtonPanel, BorderLayout.EAST);

        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.LINE_AXIS));
        downPanel.add(openButton);
        downPanel.add(deleteButton);
        downPanel.add(clearButton);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(upPanel, BorderLayout.CENTER);
        mainFrame.add(downPanel, BorderLayout.SOUTH);

        //프레임 크기 지정
        mainFrame.setSize(400, 300); // (width,height)
        //Fix mainFrame Size
        mainFrame.setMaximumSize(new Dimension(400, 300));
        mainFrame.setMinimumSize(new Dimension(400, 300));
        //프레임 보이기
        mainFrame.setVisible(true);

        //swing에만 있는 X버튼 클릭시 종료
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void DisplayResult() {
        ArrayList<HashMap<String, String>> ResultarrayList = resultManager.Scores;

        if (ResultarrayList.size() == 0) {
            JOptionPane.showMessageDialog(null, "복제된 것으로 의심되는 소스가 없습니다");
            return;
        }

        HashMap<String, String> hashMap = ResultarrayList.get(ResultarrayList.size() - 2);
        HashMap<String, String> hashMap2 = ResultarrayList.get(ResultarrayList.size() - 1);

        graphPanel = viewManager.MakeGraph(hashMap, hashMap2);

        resultList.setPreferredSize(new Dimension(200, 600));
        ArrayList<String> temp = new ArrayList<String>();

        for (int i = ResultarrayList.size() - 2; i >= 0; i--) {
            HashMap<String, String> hashTemp = ResultarrayList.get(i);
            temp.add(hashTemp.get("FileAname") + " - " + hashTemp.get("FileBname"));
        }

        resultList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = resultList.getSelectedIndex();

                ArrayList<HashMap<String, String>> Resultarray = resultManager.Scores;
                HashMap<String, String> hash = Resultarray.get(Resultarray.size() - index - 2);
                HashMap<String, String> hash2 = Resultarray.get(Resultarray.size() - 1);

                viewManager.SelectResult(hash, hash2);
                upPanel.repaint();

                rawtext.setText(hash.get("rawtext") + "%");
                name.setText(hash.get("name") + "%");
                comment.setText(hash.get("comment") + "%");
                condition.setText(hash.get("condition") + "%");
                loop.setText(hash.get("loop") + "%");
                total.setText(hash.get("total") + "%");

                resultFrame.repaint();
            }
        });

        String[] strings = temp.toArray(new String[]{""});
        resultList.setListData(strings);

        upPanel.setLayout(new FlowLayout());
        upPanel.add(new JScrollPane(resultList));
        upPanel.add(graphPanel);

        rawtext.setText(hashMap.get("rawtext") + "%");
        name.setText(hashMap.get("name") + "%");
        comment.setText(hashMap.get("comment") + "%");
        condition.setText(hashMap.get("condition") + "%");
        loop.setText(hashMap.get("loop") + "%");
        total.setText(hashMap.get("total") + "%");

        downPanel.setLayout(new GridLayout(0, 3));
        downPanel.add(new JLabel("Check Point"));
        downPanel.add(new JLabel(""));
        downPanel.add(new JLabel("Percentage"));

        downPanel.add(new JLabel("Raw Text"));
        downPanel.add(new JLabel(""));
        downPanel.add(rawtext);

        downPanel.add(new JLabel("Variable & Function"));
        downPanel.add(new JLabel(""));
        downPanel.add(name);

        downPanel.add(new JLabel("Comment"));
        downPanel.add(new JLabel(""));
        downPanel.add(comment);

        downPanel.add(new JLabel("Condition"));
        downPanel.add(new JLabel(""));
        downPanel.add(condition);

        downPanel.add(new JLabel("Loop"));
        downPanel.add(new JLabel(""));
        downPanel.add(loop);

        downPanel.add(new JLabel("Total"));
        downPanel.add(new JLabel(""));
        downPanel.add(total);

        resultFrame.setLayout(new BorderLayout());
        resultFrame.add(upPanel, BorderLayout.CENTER);
        resultFrame.add(downPanel, BorderLayout.SOUTH);

        resultFrame.setSize(700, 400);
        resultFrame.setMaximumSize(new Dimension(700, 400));
        resultFrame.setMinimumSize(new Dimension(700, 400));
        resultFrame.setVisible(true);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void showConfigureDialog() {

        JSpinner.NumberEditor jsEditor = (JSpinner.NumberEditor) spinner.getEditor();
        jsEditor.getTextField().setEnabled(false);


        configureDialog.setLayout(new FlowLayout());
        configureDialog.add(spinner);
        configureDialog.add(configureApplyBtnDialog);
        configureDialog.add(configureCancelBtnDialog);
        configureDialog.setSize(300, 80);
        configureDialog.setVisible(true);
        configureDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        MainSystem frameExam = new MainSystem();
        frameExam.createMainFrame();
    }

    //GUI Function
    public void ClickOpenBtn() {
        FileList = fileManager.OpenFiles(FileList);
        fileManager.DisplayFileList(FileList);
    }

    public void ClickDeleteBtn() {
        fileManager.DeleteFiles(FileList);
    }

    public void ClickClearBtn() {
        fileManager.Clear();
        FileList.clear();
        fileManager.DisplayFileList(FileList);
    }

    public void ClickConfigureBtn() {
        showConfigureDialog();
    }

    public void ClickCompareBtn() {
        if (FileList.size() < 2) {
            JOptionPane.showMessageDialog(null, "비교할 파일이 2개 이상이어야 합니다.");
            return;
        }

        resultManager.Compare(FileList);
        DisplayResult();
    }

    public void ClickExitBtn() {
        Exit();
    }

    public void Exit() {
        System.exit(0);
    }




}
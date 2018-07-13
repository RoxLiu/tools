package com.rox.tools.film;

import com.rox.tools.film.excel.AutoFillExcelContent;
import com.rox.tools.film.searcher.IqiyiFilmInfoSearcher;
import com.rox.tools.film.searcher.LeFilmInfoSearcher;
import com.rox.tools.film.searcher.YoukuFilmInfoSearcher;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

/*
 *
 * @author  Rox
 * @since   1.0.0
 */
public class ExcelSearchFrame extends JFrame implements WindowListener, Loggable {
    JMenu fileMenu = new JMenu("文件");
    JTextArea textArea = new JTextArea();
    //
    private Searcher searcher = new IqiyiFilmInfoSearcher();

    public ExcelSearchFrame() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item = new JMenuItem("打开");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFileAndSearch();
            }
        });
        fileMenu.add(item);
        item = new JMenuItem("退出");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(item);
        menuBar.add(fileMenu);

        JMenu menu = new JMenu("选项");
        ButtonGroup group = new ButtonGroup();
        item = new JRadioButtonMenuItem("爱奇艺");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searcher = new IqiyiFilmInfoSearcher();
            }
        });
        item.setSelected(true);
        group.add(item);
        menu.add(item);
        item = new JRadioButtonMenuItem("优酷");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searcher = new YoukuFilmInfoSearcher();
            }
        });
        group.add(item);
        menu.add(item);
        item = new JRadioButtonMenuItem("乐视");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searcher = new LeFilmInfoSearcher();
            }
        });
        group.add(item);
        menu.add(item);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        textArea.setText("请选择文件。。。");

        JPanel jp = new JPanel(new BorderLayout());
        jp.add(textArea, BorderLayout.CENTER);
        contentPane.add(new JScrollPane(jp));

        setBounds(700, 200, 400, 300);
        setTitle("电影信息搜索");
        addWindowListener(this);
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    public void windowActivated(WindowEvent e) {
    }

    protected void chooseFileAndSearch() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".xls") || name.endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "Excel文件";
            }
        });

        chooser.showOpenDialog(this);
        final File file = chooser.getSelectedFile();

        if(file != null) {
            textArea.setText("打开文件：" + file.getName() + "\n");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    AutoFillExcelContent auto = new AutoFillExcelContent(ExcelSearchFrame.this);
                    auto.setSearcher(searcher);

                    if(file.getName().toLowerCase().endsWith(".xls")) {
                        auto.fillXls(file);
                    } else {
                        auto.fillXlsx(file);
                    }
                }
            }).start();
        }
    }


    @Override
    public void println(final String log) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(log + "\n");
            }
        });
    }

    public static void main(String[] args) {
        ExcelSearchFrame w = new ExcelSearchFrame();
        w.setVisible(true);
    }
}
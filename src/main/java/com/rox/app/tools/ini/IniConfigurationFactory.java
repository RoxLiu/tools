package com.rox.app.tools.ini;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rox on 2014/6/29.
 *
 */
public class IniConfigurationFactory {
    private String confFileName = null;
    List<Section> sections = new ArrayList<>();

    public IniConfigurationFactory(String file) {
        confFileName = file;

        build();
    }

    private void build() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(confFileName), "UTF-8"));
            String line;
            Section currentSection = null;
            while((line = reader.readLine()) != null) {
                line = line.trim();

                //comment: #
                if(line.length() > 0 && !line.startsWith("#")) {
                    if(line.startsWith("[")) {
                        if(!line.endsWith("]")) {
                            throw new IllegalArgumentException("The Section has wrong format, not ends with ']': " + line);
                        }
                        //trim the '[' and ']'
                        currentSection = new Section(line.substring(1, line.length() - 1));
                        sections.add(currentSection);
                    } else {
                        String key, value;
                        int index = line.indexOf('=');
                        if(index != -1) {
                            key = line.substring(0, index);
                            value = line.substring(index + 1);
                        } else {
                            key = line;
                            value="";
                        }

                        //
                        if(currentSection == null) {
                            System.err.println("The property is not defined under a section: " + line);
                            currentSection = new Section("");
                            sections.add(currentSection);
                        }

                        currentSection.property(key, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Section section(String name) {
        return find(name, true);
    }

    private Section find(String name, boolean addIfAbsent) {
        for(Section one : sections) {
            if(one.getName().equals(name)) {
                return one;
            }
        }

        if(addIfAbsent) {
            Section section = new Section(name);
            sections.add(section);
            return section;
        }

        return null;
    }

    public void save() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(confFileName));

            for(Section section : sections) {
                pw.println(section);

                List<Property> properties = section.getProperties();
                for(Property property : properties) {
                    pw.println(property);
                }
            }
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                pw.close();
            }
        }
    }
}


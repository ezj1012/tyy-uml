package com.tyy.uml.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class UMLModelParser {

    public static String toClassTitle(UMLModel m) {
        StringBuilder sb = new StringBuilder();
        if (m.getClassName() == null || m.getClassName().trim().length() == 0) { return sb.toString(); }

        sb.append(String.valueOf(m.getClassName().trim()));
        if (m.getClassDesc() != null && m.getClassDesc().trim().length() > 0) {
            sb.append(" - ").append(m.getClassDesc().trim());
        }

        return sb.toString();
    }

    public static String toUMLString(UMLModelField field) {
        StringBuilder sb = new StringBuilder();
        sb.append("- ");
        sb.append(field.getName()).append(":")//
                .append(javaTypeToSimpleName.get(field.getJavaType()))//
        ;
        return sb.toString();
    }

    public static void parser(String sourceString, UMLModel m) {
        if (sourceString.trim().length() == 0) {
            parseClassLine(sourceString, sourceString, m, new ArrayList<>());
            m.setFields(new ArrayList<>());
            return;
        }

        StringTokenizer st = new StringTokenizer(sourceString, "\n");

        int step = 0;
        String line = null;
        List<String> desc = new ArrayList<String>();
        List<UMLModelField> newFields = new ArrayList<UMLModelField>();
        UMLModelField loopField = null;
        while (st.hasMoreElements()) {
            line = st.nextToken();
            String trimLine = line.trim();
            if (trimLine.length() == 0) {
                continue;
            }
            if (trimLine.startsWith("#")) {
                desc.add(trimLine);
                continue;
            }
            if (step == 0) {
                parseClassLine(line, trimLine, m, desc);
                step++;
            } else if (step > 0) {
                if (line.startsWith("-")) {
                    if (loopField != null) {
                        newFields.add(loopField);
                    }
                    loopField = new UMLModelField();
                }
                if (loopField != null) {
                    parserField(step++, line, trimLine, loopField, desc);
                }
            }
        }
        if (loopField != null) {
            newFields.add(loopField);
        }
        m.setFields(newFields);
    }

    private static void parserFieldEnum(String trimString, UMLModelField newFields) {

    }

    private static void parserField(int i, String line, String trimString, UMLModelField newFields, List<String> desc) {
        if (line.startsWith("  -")) {
            parserFieldEnum(trimString, newFields);
            return;
        }
        try {
            String[] split = trimString.split(" ");

            String name = "";
            String javaType = DEFAULT_JAVA_TYPE;
            long len = 16;
            long dlen = 0;

            if (split.length > 1) {
                // name:int(12,12)
                String nameItems = split[1];

                int nameSubIndex = nameItems.indexOf(":");
                if (nameSubIndex < 0) {
                    name = split[1].trim();
                } else {
                    name = nameItems.substring(0, nameSubIndex);
                    // int(12,12)
                    String typeItems = nameItems.substring(nameSubIndex + 1);
                    int indexOf = typeItems.indexOf("(");
                    if (indexOf <= 0) {
                        javaType = getJavaType(typeItems);
                    } else {
                        javaType = getJavaType(typeItems.substring(0, indexOf));
                        int end = typeItems.indexOf(")");
                        end = end < 0 ? typeItems.length() : end;
                        typeItems = typeItems.substring(indexOf + 1, end); // (1,0 => 1,0 或 (1,29) = > 1,29
                        indexOf = typeItems.indexOf(",");
                        String ds = null;
                        if (indexOf > 0) {
                            ds = typeItems.substring(indexOf + 1);
                            typeItems = typeItems.substring(0, indexOf);
                        }
                        try {
                            len = Long.parseLong(typeItems);
                        } catch (Exception e) {
                            len = 16;
                        }
                        try {
                            dlen = Long.parseLong(ds);
                        } catch (Exception e) {
                            dlen = 0;
                        }
                    }
                }
            }

            if (split.length > 2) {
                desc.add(split[2]);
            }
            String nptes = null;
            if (desc.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (String d : desc) {
                    d = d.trim().substring(1, d.length());
                    sb.append(d).append("\n");
                }
                sb.delete(sb.length() - 1, sb.length());
                nptes = sb.toString();
            }

            newFields.setName(name);
            newFields.setJavaType(javaType);
            newFields.setLen(len);
            newFields.setDlen(dlen);
            newFields.setNotes(nptes);
            newFields.setPosition(i + 0L);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseClassLine(String line, String trimLine, UMLModel m, List<String> desc) {
        int indexOf = trimLine.indexOf(" ");
        String className = null;
        String classDesc = "";
        if (indexOf > 0) {
            className = trimLine.substring(0, indexOf);
            classDesc = trimLine.substring(indexOf, trimLine.length());
        } else {
            className = trimLine;
        }

        StringBuffer subPack = new StringBuffer();
        if (className.length() > 0) {

            subPack.append(Character.toLowerCase(className.charAt(0)));
            for (int i = 1; i < className.length(); i++) {
                char c = className.charAt(i);
                if (Character.isUpperCase(c)) {
                    break;
                }
                subPack.append(c);
            }
        }

        if (desc.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("/**\n");
            for (String string : desc) {

                sb.append(" * ").append(string.substring(1, string.length())).append("<br/>\n");
            }
            sb.append(" *\n */\n");
            m.setClassDescs(sb.toString());
            desc.clear();
        }

        m.setSubPack(subPack.toString());
        m.setClassName(className);
        m.setClassDesc(classDesc);
    }

    public static final Set<String> javaTypes = new HashSet<>(Arrays.asList("int", "long", "str", "string"));

    public static final Map<String, String> javaTypeMap = new HashMap<>();

    public static final Map<String, String> javaTypeToSimpleName = new HashMap<>();

    public static final String DEFAULT_JAVA_TYPE = "java.lang.String";

    private static void set(String key, String v) {
        javaTypeMap.put(key, v);
        for (int i = key.length() - 1; i > 0; i--) {
            javaTypeMap.put(key.substring(0, i), v);
        }
    }

    static {
        String t = "integer";
        for (int i = t.length(); i > 0; i--) {
            javaTypeMap.put("integer", "java.lang.Integer");
        }
        set("integer", "java.lang.Integer");
        set("long", "java.lang.Long");
        set("string", "java.lang.String");

        javaTypeToSimpleName.put("java.lang.Integer", "Integer");
        javaTypeToSimpleName.put("java.lang.Long", "Long");
        javaTypeToSimpleName.put("java.lang.String", "String");
    }

    private static String getJavaType(String s) {
        String type = javaTypeMap.get(s.toLowerCase());
        return type == null ? DEFAULT_JAVA_TYPE : type;
    }

}

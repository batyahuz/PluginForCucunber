package com.example.demo;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFromFile {
    public static List<String> readCategoriesFromFile(Project project) {
        List<String> parameterNames = new ArrayList<>();

        try {
            VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
            if (baseDir == null) return null;

            VirtualFile file = baseDir.findFileByRelativePath("/src/main/java/ParameterDefinitions/MyDefinitions.java");
            if (file == null) return parameterNames;

            // קריאת תוכן הקובץ
            String content = VfsUtil.loadText(file);

            // Regex למציאת שמות פונקציות עם @ParameterType
            Pattern pattern = Pattern.compile("@ParameterType\\([^)]*\\)\\s+public\\s+\\w+\\s+(\\w+)\\s*\\(");
            Matcher matcher = pattern.matcher(content);

            // הוספת השמות לרשימה
            while (matcher.find()) {
                parameterNames.add(matcher.group(1)); // השם של הפונקציה
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return parameterNames;
    }

    public static String[] readSuggestionsFromFile(Project project, String categoryName) {
        List<String> parameterNames = new ArrayList<>();

        try {
            VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
            if (baseDir == null) return new String[0];

            VirtualFile dir = baseDir.findFileByRelativePath("/src/main/java/ParametersValues");
            if (dir == null || !dir.isDirectory()) return new String[0];

            VirtualFile file = Arrays.stream(dir.getChildren())
                    .filter(child -> child.getName().startsWith(categoryName))
                    .findFirst()
                    .orElse(null);
            if (file == null) return new String[0];

            // קריאת תוכן הקובץ
            String content = VfsUtil.loadText(file);

            // Regex למציאת שמות סטרינגים הנשלחים ל registerPage
//            Pattern pattern = Pattern.compile("\"([^\"]+)\"");
            Pattern pattern = Pattern.compile("registerPage\\s*.\\s*\"([^\"]+)\"\\s*");
            Matcher matcher = pattern.matcher(content);

            // הוספת השמות לרשימה
            while (matcher.find()) {
                parameterNames.add(matcher.group(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return parameterNames.toArray(new String[0]);
    }

}

package com.example.demo;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFromFile {
    public static List<String> readSuggestionsFromFile(Project project) {
        List<String> parameterNames = new ArrayList<>();

        try {
            VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
            if (baseDir == null) return null;

            System.out.println("base dir::" + baseDir);

            VirtualFile file = baseDir.findFileByRelativePath("/src/main/java//ParameterDefinitions/MyDefinitions.java");
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
}

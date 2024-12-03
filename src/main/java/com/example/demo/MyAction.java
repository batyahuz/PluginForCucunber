package com.example.demo;

import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.demo.ReadFromFile.readCategoriesFromFile;
import static com.example.demo.ReadFromFile.readSuggestionsFromFile;

public class MyAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        if (editor == null) return;

        int offset = editor.getCaretModel().getPrimaryCaret().getOffset();
        String documentText = editor.getDocument().getText();

        // מציאת הטווח של הטקסט (כולל סוגריים מסולסלים אם יש)
        int startBraceIndex = findOpeningBraceIndex(documentText, offset);
        if (startBraceIndex == -1) {
            return; // הסמן לא בתוך סוגריים מסולסלים
        }

        // מציאת סיום הצמד
        int endBraceIndex = findClosingBraceIndex(documentText, startBraceIndex);

        // בדיקה אם הסמן נמצא בתוך הצמד
        if (offset <= startBraceIndex || offset > endBraceIndex) {
            return; // הסמן לא בתוך הצמד
        }

        List<String> list = readCategoriesFromFile(project);
        if (list == null) {
            Messages.showMessageDialog(project, "You must add --- file to define the plugin", "Plugin Message", Messages.getInformationIcon());
            return;
        }

        String wordInsideBraces = editor.getDocument().getText().substring(startBraceIndex + 1, endBraceIndex).trim();
        if (!list.contains(wordInsideBraces)) {
            return;
        }
        // רשימת מילים להצעה
        String[] suggestions = addPrefixToEach(readSuggestionsFromFile(project, wordInsideBraces));
        LookupManager lookupManager = LookupManager.getInstance(project);
        Lookup lookup = lookupManager.showLookup(editor, buildLookupElements(suggestions));

        // מאזין לבחירת מילה
        if (lookup != null) {
            lookup.addLookupListener(new LookupListener() {
                @Override
                public void itemSelected(@NotNull LookupEvent event) {
                    LookupElement item = event.getItem();
                    if (item != null) {
                        String selected = item.getLookupString();

                        CommandProcessor.getInstance().executeCommand(project, () -> {
                            // מציאת הטווח מחדש לפני מחיקה
                            int endBraceIndex = findClosingBraceIndex(editor.getDocument().getText(), startBraceIndex);
                            if (endBraceIndex == -1) {
                                return; // צומד סוגר לא נמצא
                            }

                            // מחיקת הטקסט הקיים כולל סוגריים מסולסלים
                            editor.getDocument().deleteString(startBraceIndex, endBraceIndex + 1); // כולל {}

                            // הכנסת המילה החדשה במקום
                            editor.getDocument().insertString(startBraceIndex, selected);
                        }, "Replace Text Inside Braces", null);
                    }
                }
            });
        }

    }

    // מציאת אינדקס של סוגר מסולסל פותח '{'
    private int findOpeningBraceIndex(String text, int offset) {
        int index = offset;
        while (index > 0 && text.charAt(index - 1) != '{') {
            index--;
        }
        return (index > 0 && text.charAt(index - 1) == '{') ? index - 1 : -1;
    }

    // מציאת אינדקס של סוגר מסולסל סוגר '}'
    private int findClosingBraceIndex(String text, int start) {
        int index = start;
        while (index < text.length() && text.charAt(index) != '}') {
            index++;
        }
        return (index < text.length() && text.charAt(index) == '}') ? index : -1;
    }

    // בניית רשימת LookupElement מתוך המילים
    private LookupElementBuilder[] buildLookupElements(String[] suggestions) {
        LookupElementBuilder[] elements = new LookupElementBuilder[suggestions.length];
        for (int i = 0; i < suggestions.length; i++) {
            elements[i] = LookupElementBuilder.create(suggestions[i]);
        }
        return elements;
    }

    public static String[] addPrefixToEach(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = "\"" + strings[i] + "\"";
        }
        return strings;
    }
}

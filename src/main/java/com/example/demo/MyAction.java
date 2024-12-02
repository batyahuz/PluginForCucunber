package com.example.demo;

import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {

        // קבלת הפרויקט והעורך (Editor)
        Project project = e.getProject();
        System.out.println("PROJECT:: " + project);
        if (project == null) return;

        Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        System.out.println("EDITOR:: " + editor);
        if (editor == null) return;

        // קבלת המיקום של הסמן
        Caret caret = editor.getCaretModel().getPrimaryCaret();
        int offset = caret.getOffset();
        String documentText = editor.getDocument().getText();
        System.out.println("CARET:: " + caret);

        // מציאת הטווח של הטקסט (כולל סוגריים מסולסלים אם יש)
        int[] wordRange = findBracesRange(documentText, offset);
        if (wordRange == null) {
            System.out.println("Cursor is not inside curly braces.");
            return; // הסמן לא בתוך סוגריים מסולסלים
        }

        // קבלת המילה בתוך הסוגריים
        String wordInBraces = documentText.substring(wordRange[0], wordRange[1]).trim();
        System.out.println("Word inside braces: " + wordInBraces);
        if (!"name".equals(wordInBraces)) {
            System.out.println("Word is not 'name'.");
            return; // המילה לא "name"
        }

        // רשימת מילים להצעה
        String[] suggestions = {"Alice", "Bob", "Charlie"};
        LookupManager lookupManager = LookupManager.getInstance(project);
        System.out.println("AFTER:: LookupManager lookupManager = LookupManager.getInstance(project);");
        Lookup lookup = lookupManager.showLookup(editor, buildLookupElements(suggestions));
        System.out.println("AFTER:: Lookup lookup = lookupManager.showLookup(editor, buildLookupElements(suggestions));");

        // מאזין לבחירת מילה
        if (lookup != null) {
            System.out.println("(lookup != null)::");
            lookup.addLookupListener(new LookupListener() {
                @Override
                public void itemSelected(@NotNull LookupEvent event) {
                    LookupElement item = event.getItem();
                    if (item != null) {
                        System.out.println("In:: public void itemSelected(@NotNull LookupEvent event) {");
                        String selected = item.getLookupString();

                        // שימוש ב-CommandProcessor כדי לסנכרן את השינויים עם ה-IDE
                        CommandProcessor.getInstance().executeCommand(project, () -> {
                            // מחיקת הטקסט הקיים כולל סוגריים מסולסלים
                            editor.getDocument().deleteString(wordRange[0] - 1, wordRange[1] + 1); // מחיקת { ו-}
                            System.out.println("Deleted range from " + (wordRange[0] - 1) + " to " + (wordRange[1] + 1));

                            // הוספת הטקסט שנבחר
                            editor.getDocument().insertString(wordRange[0] - 1, selected);
                            System.out.println("Inserted selected text: " + selected);
                        }, "Replace Text Inside Braces", null);

                       /*
                        // מחיקת הטקסט הקיים כולל סוגריים מסולסלים
                        editor.getDocument().deleteString(wordRange[0], wordRange[1]);
                        System.out.println("Deleted range from " + wordRange[0] + " to " + wordRange[1]);

                        // הוספת הטקסט שנבחר
                        editor.getDocument().insertString(wordRange[0], selected);
                        System.out.println("Inserted selected text: " + selected);*/
                    }
                }

                @Override
                public void lookupCanceled(@NotNull LookupEvent event) {
                    System.out.println("In:: public void lookupCanceled(@NotNull LookupEvent event) {");
                }
            });
        }
    }

    // מציאת הטווח של המילה בתוך סוגריים מסולסלים
    private int[] findBracesRange(String text, int offset) {
        int start = offset;
        int end = offset;

        // חיפוש אחורה עד '{'
        while (start > 0 && text.charAt(start) != '{') {
            start--;
        }

        if (text.charAt(start) != '{') {
            return null; // סוגר פותח לא נמצא
        }

        // חיפוש קדימה עד '}'
        while (end < text.length() && text.charAt(end) != '}') {
            end++;
        }

        if (end >= text.length() || text.charAt(end) != '}') {
            return null; // סוגר סוגר לא נמצא
        }

        // מחזיר את הטווח של הטקסט בתוך הסוגריים
        return new int[]{start + 1, end};
    }

    // בניית רשימת LookupElement מתוך המילים
    private LookupElementBuilder[] buildLookupElements(String[] suggestions) {
        LookupElementBuilder[] elements = new LookupElementBuilder[suggestions.length];
        for (int i = 0; i < suggestions.length; i++) {
            elements[i] = LookupElementBuilder.create(suggestions[i]);
        }
        return elements;
    }
}
